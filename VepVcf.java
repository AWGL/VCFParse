package nhs.genetics.cardiff;

import com.fasterxml.jackson.databind.ObjectMapper;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import nhs.genetics.cardiff.framework.GenomeVariant;
import nhs.genetics.cardiff.framework.Pair;
import nhs.genetics.cardiff.framework.SampleMetaData;
import nhs.genetics.cardiff.framework.vep.VepAnnotationObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for extracting VCF genotype and VEP information using htsjdk
 *
 * @author  Sara Rey
 * @since   2016-11-08
 */

public class VepVcf {

    private static final Logger log = Logger.getLogger(VepVcf.class.getName());
    private File vcfFilePath;
    private String[] vepHeaders;
    private Integer vepVersion;
    private LinkedHashMap<GenomeVariant, ArrayList<VepAnnotationObject>> annotatedVariants = new LinkedHashMap<>();
    private LinkedHashMap<GenomeVariant, ArrayList<Pair<Genotype, Double>>> sampleVariants = new LinkedHashMap<>();
    private ArrayList<String> sampleNames;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HashMap<String, SampleMetaData> sampleMetaDataHashMap = new HashMap<>();

    public VepVcf(File vcfFilePath) {
        this.vcfFilePath = vcfFilePath;
    }

    public void parseVepVcf() {
        log.log(Level.INFO, "Parsing VEP annotated VCF file");

        //open VCF file
        //Wrap in a try-catch block- or code fails at the next step//
        VCFFileReader vcfFileReader = new VCFFileReader(vcfFilePath, false); //Do we require index?

        //get VEP version
        try {
            vepVersion = Integer.parseInt(vcfFileReader.getFileHeader().getOtherHeaderLine("VEP").getValue().split(" ")[0].split("v")[1]);
        } catch (NullPointerException e){
            log.log(Level.WARNING, "Could not determine VEP version");
        }

        //get vcf metadata
        //Need to change the SampleMetaData class to support output from the Germline Enrichment pipeline
        //Doesn't trip the catch block below, gives a key not found error
        try {
            setSampleMetaData(vcfFileReader.getFileHeader().getMetaDataLine("SAMPLE").getValue());
        } catch (NullPointerException e){
            log.log(Level.SEVERE, "Could not read metadata from SAMPLE tag. Check metadata has been applied to VCF file.");
            throw e;
        }

        //extract VEP headers
        try {
            vepHeaders = vcfFileReader.getFileHeader().getInfoHeaderLine("CSQ").getDescription().split("Format:")[1].trim().split("\\|");
        } catch (NullPointerException e){
            log.log(Level.WARNING, "Could not find VEP header. Assuming VCF has not been annotated");
        }

        //get sample list
        sampleNames = vcfFileReader.getFileHeader().getSampleNamesInOrder();

        //read VCF line by line
        vcfFileReader.iterator()
                .stream()
                .filter(VariantContext::isVariant)
                .filter(variantContext -> !variantContext.isFiltered())
                .forEach(variantContext -> {

                    //split site level annotations and pair with headers
                    if (variantContext.hasAttribute("CSQ")){
                        HashSet<VepAnnotationObject> annotations = new HashSet<>();

                        try {
                            annotations.add(deserialiseVepAnnotation((String) variantContext.getAttribute("CSQ")));
                        } catch (ClassCastException e) {
                            for (String field : (ArrayList<String>) variantContext.getAttribute("CSQ")) {
                                annotations.add(deserialiseVepAnnotation(field));
                            }
                        }

                        //loop over alternative alleles, create genomeVariants and associate with allele num
                        for (int i=0; i < variantContext.getAlternateAlleles().size(); i++){

                            //create genome variant & convert to minimal representation
                            GenomeVariant genomeVariant = new GenomeVariant(variantContext.getContig(), variantContext.getStart(), variantContext.getReference().getBaseString(), variantContext.getAlternateAlleles().get(i).getBaseString());
                            genomeVariant.convertToMinimalRepresentation();

                            //prep annotated variants map
                            annotatedVariants.put(genomeVariant, new ArrayList<>());

                            //find annotation associated with this allele_num
                            for (VepAnnotationObject vepAnnotation : annotations){
                                if (vepAnnotation.getAlleleNum() == (i+1)){
                                    annotatedVariants.get(genomeVariant).add(vepAnnotation);
                                }
                            }

                        }
                    }

                    //loop over genotypes
                    variantContext.getGenotypes()
                            .stream()
                            .filter(genotype -> !genotype.isNoCall())
                            .filter(genotype -> !genotype.isHomRef())
                            .filter(genotype -> !genotype.isFiltered())
                            .forEach(genotype -> {

                                //check genotype validity
                                if (genotype.isMixed()) {
                                    throw new UnsupportedOperationException(genotype.getSampleName() + ": " + variantContext.getContig() + " " + variantContext.getStart() + " " + variantContext.getReference() + variantContext.getAlternateAlleles().toString() + " has mixed genotype ( " + genotype.getGenotypeString());
                                }

                                if (genotype.getPloidy() != 2 || genotype.getAlleles().size() != 2){
                                    throw new UnsupportedOperationException("Allele " + genotype.getAlleles().toString() + " is not diploid.");
                                }

                                if (genotype.getAlleles().get(0).getBaseString().equals("*") || genotype.getAlleles().get(1).getBaseString().equals("*")) {
                                    return;
                                }

                                //store genotype
                                if (genotype.isHom()){
                                    GenomeVariant genomeVariant = new GenomeVariant(variantContext.getContig(), variantContext.getStart(), variantContext.getReference().getBaseString(), genotype.getAlleles().get(1).getBaseString());
                                    genomeVariant.convertToMinimalRepresentation();

                                    if (!sampleVariants.containsKey(genomeVariant)){
                                        sampleVariants.put(genomeVariant, new ArrayList<>());
                                    }

                                    sampleVariants.get(genomeVariant).add(new Pair<>(genotype, variantContext.getPhredScaledQual()));

                                } else if (genotype.isHet()){
                                    GenomeVariant genomeVariant = new GenomeVariant(variantContext.getContig(), variantContext.getStart(), variantContext.getReference().getBaseString(), genotype.getAlleles().get(1).getBaseString());
                                    genomeVariant.convertToMinimalRepresentation();

                                    if (!sampleVariants.containsKey(genomeVariant)){
                                        sampleVariants.put(genomeVariant, new ArrayList<>());
                                    }

                                    sampleVariants.get(genomeVariant).add(new Pair<>(genotype, variantContext.getPhredScaledQual()));

                                    if (genotype.isHetNonRef()){
                                        genomeVariant = new GenomeVariant(variantContext.getContig(), variantContext.getStart(), variantContext.getReference().getBaseString(), genotype.getAlleles().get(0).getBaseString());
                                        genomeVariant.convertToMinimalRepresentation();

                                        if (!sampleVariants.containsKey(genomeVariant)){
                                            sampleVariants.put(genomeVariant, new ArrayList<>());
                                        }

                                        sampleVariants.get(genomeVariant).add(new Pair<>(genotype, variantContext.getPhredScaledQual()));
                                    }

                                } else {
                                    log.log(Level.WARNING, "Inheritance unknown: " + variantContext.toString() + " and could not be added.");
                                }

                            });

                });
    }

    private VepAnnotationObject deserialiseVepAnnotation(String csqField){

        HashMap<String, String> hashMap = new HashMap<String, String>();

        //split annotation fields
        String[] annotations = csqField.split("\\|"); //-1 stops this removing empty trailing spaces

        //pair headers with fields
        for (int i=0 ; i < annotations.length; i++) {
            hashMap.put(vepHeaders[i].trim(), annotations[i].trim());
        }

        return objectMapper.convertValue(hashMap, VepAnnotationObject.class);
    }
    private void setSampleMetaData(String sampleHeaderLine){
        HashMap<String, String> temp = new HashMap<>();

        for (String keyValue : sampleHeaderLine.substring(1, sampleHeaderLine.length() - 1).split(",")){
            temp.put(keyValue.split("=")[0], keyValue.split("=")[1]);
        }

        sampleMetaDataHashMap.put(temp.get("ID"), objectMapper.convertValue(temp, SampleMetaData.class));
    }

    public Integer getVepVersion() {
        return vepVersion;
    }
    public String[] getVepHeaders() {
        return vepHeaders;
    }
    public LinkedHashMap<GenomeVariant, ArrayList<VepAnnotationObject>> getAnnotatedVariants() {
        return annotatedVariants;
    }
    public LinkedHashMap<GenomeVariant, ArrayList<Pair<Genotype, Double>>> getSampleVariants() {
        return sampleVariants;
    }
    public ArrayList<String> getSampleNames() {
        return sampleNames;
    }
    public HashMap<String, SampleMetaData> getSampleMetaDataHashMap() {
        return sampleMetaDataHashMap;
    }
}
