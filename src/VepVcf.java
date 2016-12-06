//Dependencies htsjdk library 2.7.0
/*
Comment here
 */

import java.io.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import htsjdk.samtools.*;
import htsjdk.tribble.*;
import htsjdk.tribble.readers.*;
import htsjdk.variant.variantcontext.*;
import htsjdk.variant.variantcontext.writer.*;
import htsjdk.samtools.util.CloseableIterator;
import htsjdk.variant.vcf.*;

import htsjdk.variant.utils.*;
import org.broadinstitute.hellbender.utils.reference.ReferenceUtils.*;
import com.google.common.collect.Multimap;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.TreeMultimap;


/**
 * Created by Sara on 08-Nov-16.
 */

public class VepVcf {

    private static final Logger Log = Logger.getLogger(OpenVEPVCF.class.getName());
    //private File vcfFilePath;

    //Maybe move this inside the method
    //private LinkedHashMap<GenomeVariant, CsqObject> variantHashMap =  new LinkedHashMap<GenomeVariant, CsqObject>(); //Linked hash map preserves order

    //HashMap containing per-variant and allele information
    private LinkedHashMap<String, VariantDataObject> variantHashMap =  new LinkedHashMap<String, VariantDataObject>();

    //HashMap containing per sample, per position (and contig), (and including alternate allele information)
    private LinkedHashMap<String, SampleVariantDataObject> sampleVariantHashMap =  new LinkedHashMap<String, SampleVariantDataObject>();


    //HashMap containing
    //private LinkedHashMap<String, SampleVariantDataObject> sampleHashMap = new LinkedHashMap<String, SampleVariantDataObject>();
    //Constructor- invoked at the time of object creation
    //public VepVcf(File vcfFilePath) {
        //this.vcfFilePath = vcfFilePath;
    //}

    public VCFFileReader openFiles(File vcfFilePath) { //throws IOException  {
        Log.log(Level.INFO, "Opening VEP VCF file");
        //Read in the file
        //try(final VCFFileReader vcfFile = new VCFFileReader(vcfFilePath, false)){
        //VCFHeader currentHeader = vcfFile.getFileHeader();
        //System.out.println(currentHeader); //The file header

        try{
            final VCFFileReader vcfFile = new VCFFileReader(vcfFilePath, false);
            return vcfFile;
        }catch(Exception e) {
            Log.log(Level.SEVERE, "Could not read VCF file: " + e.getMessage());
        }
        return null; //This is what it returns if the try catch block fails- syntax??
    }

    public LinkedHashMap parseVepVcf(VCFFileReader vcfFile)

    {
        Log.log(Level.INFO, "Parsing VEP VCF file");
        //For the alternate alleles
        //Required for code execution as otherwise variable is initialised only in else clause
        String altAllele = null;
        boolean variantFiltered = false; //Default setting
        boolean variantSite = false; //Default setting
        for (final VariantContext vc : vcfFile){
            List<Allele> altAlleles = vc.getAlternateAlleles();
            ///System.out.print(vc.getAttributes()); //Allows to obtain what is in the INFO field


            //Make the object to hold the annotations- note this currently iterates every time and gets the same headers (same vcf)
            //Obtain keys for each transcript entry (header in vcf file)

            //The entire CSQ record including all of the entries for this variant context
            CsqUtilities currentCsqRecord = new CsqUtilities();
            //System.out.println(csqObject); //Just gives a reference to the object

            //Create a CsqObject to hold the data paired with the Genome Variant object as the key
            CsqObject currentCsqObject = new CsqObject(); //Empty object created
            currentCsqObject.setCsqObject((currentCsqRecord.createCsqRecordOfVepAnnObjects(
                    currentCsqRecord.vepHeaders(vcfFile), currentCsqRecord.vepAnnotations(vc))));
            //Might be worth retrieving the headers outside of this loop//

            variantFiltered = vc.isFiltered();
            variantSite = vc.isVariant();


            //This part of the code associates a specific alternate allele with its data in the CSQ field
            if (altAlleles.size() > 1){
                //Create an appropriate store to associate the specific csq entries with the alt allele
                Multimap<String,VepAnnotationObject> alleleCsq = ArrayListMultimap.create();

                //Create a map associating multiple CSQ entries with the correct alternate allele
                //This for loop needs to start at 1 because of the current naming of the CSQ Objects numerically
                for (int j = 1; j <= currentCsqObject.getCsqObject().size(); j++ ){

                    VepAnnotationObject vepAnn = currentCsqObject.getSpecificCsqObject(j); //This is the particular Vep record
                    int alleleIndex = (Integer.parseInt(vepAnn.getAlleleNum()) - 1); //Java is zero-indexed (-1)

                    //System.out.println(altAlleles.get(alleleIndex));
                    alleleCsq.put((altAlleles.get(alleleIndex).getBaseString()),(vepAnn));

                    //System.out.println(alleleCsq); //Could be a useful statement when deciding on which for loops to keep
                }

                //Retrieve all entries for each allele and generate a csq object

                //System.out.println(alleleCsq.keys());
                //System.out.println(alleleCsq.keySet());

                for (String key : alleleCsq.keySet()){
                    //ArrayList<VepAnnotationObject> forCsq = new ArrayList<VepAnnotationObject>(alleleCsq.get(key)); //Get the correct type for this object
                    //System.out.println(key + " " + alleleCsq.get(key));

                    altAllele = key; //Makes this robust to any changes in the order as the key is dynamically determined

                    GenomeVariant variantObject = new GenomeVariant(vc.getContig(), vc.getStart(),
                            vc.getReference().toString().replaceAll("\\*", ""), altAllele);

                    //System.out.println(variantObject);


                    CsqUtilities alleleCsqRecord = new CsqUtilities();
                    //System.out.println(alleleCsqRecord.createCsqRecord(forCsq)); //Creation of csqObject- this isn't being properly created

                    CsqObject alleleCsqObject = new CsqObject();
                    //System.out.println(alleleCsqRecord.createCsqRecord(forCsq).getClass());

                    alleleCsqObject.setCsqObject((alleleCsqRecord.createCsqRecordOfVepAnnObjects(
                        alleleCsqRecord.vepHeaders(vcfFile), alleleCsqRecord.vepAnnotations(vc))));

                    VariantDataObject currentVariantDataObject = new VariantDataObject(alleleCsqObject,
                            variantFiltered, variantSite);

                    variantHashMap.put(variantObject.toString(), currentVariantDataObject );

                    //variantHashMap.put(variantObject, alleleCsqObject);

                    //System.out.println(variantFiltered);
                    //System.out.println(variantSite);

                    //Turn the multiple VepAnnotationObject entries into a CsqObject

                }


            }else {
                altAllele = altAlleles.get(0).getBaseString(); //Requires String for GenomeVariant class

                //This is intended as the key to the hashmap
                GenomeVariant variantObject = new GenomeVariant(vc.getContig(), vc.getStart(),
                        vc.getReference().toString().replaceAll("\\*", ""), altAllele);

                //System.out.println(variantObject);

                VariantDataObject currentVariantDataObject = new VariantDataObject(currentCsqObject,
                        variantFiltered, variantSite);

                variantHashMap.put(variantObject.toString(), currentVariantDataObject );

                //Associate the variant object with the CsqObject on a per record basis
                //variantHashMap.put(variantObject, currentCsqObject);

                //System.out.println(variantFiltered);
                //System.out.println(variantSite);

            }

            //This part of the code associates the specific alleles and some additional specific sample-allele metadata
            //with the different samples present in the vcf
            //System.out.println(vc);
            //Extract data associated with each specific genotype within the variant context
            GenotypesContext gt = vc.getGenotypes();
            Iterator<Genotype> gtIter = vc.getGenotypes().iterator();
            while (gtIter.hasNext()) {

                String zygosity = null;

                //System.out.println(gt); // Iterator Object
                Genotype currentGenotype = gtIter.next();
                System.out.println(currentGenotype);


                //Don't add the variant to the sample if there is no call or only a hom ref call
                //Could re-do this logic here
                if(currentGenotype.isHomRef() || currentGenotype.isNoCall()){continue;}
                else{
                    List<Allele> currentGenotypeAlleles = currentGenotype.getAlleles();

                    ////vc.getReference().toString().replaceAll("\\*", ""), altAllele);
                    //System.out.println(currentSampleVariant);


                    //Zygosity
                    if (currentGenotype.isHom()){zygosity = "HOM";}
                    else if (currentGenotype.isHet()){zygosity = "HET";}
                    else {zygosity = "UNDETERMINED";}

                    //Creation of SampleData object
                    SampleDataObject currentSampleDataObject =
                            new SampleDataObject(currentGenotype.getSampleName(),
                                    currentGenotype.isFiltered(), currentGenotype.isMixed(),
                                    currentGenotype.getPloidy(), zygosity, currentGenotype.getGQ());


                    //Creation of the SampleVariantData object and sampleVariantHashMap
                    //Locate what is the key in the variantHashMap for the specific allele
                    //Generate keyForVariant
                    for (Allele currentAllele : currentGenotypeAlleles){
                        GenomeVariant keyForVariant = new GenomeVariant(vc.getContig(), vc.getStart(),
                                vc.getReference().toString().replaceAll("\\*", ""),
                                currentAllele.toString().replaceAll("\\*", ""));
                        //System.out.println(keyForVariant);
                        SampleVariant currentSampleVariant = new SampleVariant(currentGenotype.getSampleName(),
                                vc.getContig(), vc.getStart(), currentGenotypeAlleles, currentAllele);

                        SampleVariantDataObject currentSampleVariantDataObject =
                            new SampleVariantDataObject(keyForVariant, currentSampleDataObject);

                    sampleVariantHashMap.put(currentSampleVariant.toString(), currentSampleVariantDataObject);

                    }

                }

            }

            break; //first allele only for ease of testing

        }

        //Test hash map is working correctly
        System.out.println(sampleVariantHashMap);
        System.out.println(variantHashMap);

        System.out.println(sampleVariantHashMap.get("23M,1:241663902 TGAGA"));
        System.out.println(sampleVariantHashMap.get("23M,1:241663902 T").getVariantObjectKey());
        System.out.println(sampleVariantHashMap.get("23M,1:241663902 TGAGA").getVariantObjectKey());
        System.out.println(variantHashMap.get(sampleVariantHashMap.get("23M,1:241663902 TGAGA").getVariantObjectKey()));
        System.out.println(variantHashMap.get("1:241663902TGA>TGAGA"));


        Iterator<VepAnnotationObject> vpIter = variantHashMap.get(sampleVariantHashMap.get("23M,1:241663902 TGAGA").
                getVariantObjectKey()).getCsqObject().getEntireCsqObject().iterator();
        while (vpIter.hasNext()){
            System.out.println(vpIter.next().getVepRecord());
    }


        //This is hom ref so although it is in the sample variant hash map it won't be found in the variant hash map
        //This is het ref not hom ref, hom ref has not been stored
        System.out.println(variantHashMap.get("1:241663902TGA>TGA"));



        return variantHashMap;
        //return sampleHashMap

    }

}
