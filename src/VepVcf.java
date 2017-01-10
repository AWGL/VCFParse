//Dependencies htsjdk library 2.7.0
/*
Comment here
 */

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import htsjdk.variant.variantcontext.*;
import htsjdk.variant.vcf.*;
import com.google.common.collect.ArrayListMultimap;


/**
 * Created by Sara on 08-Nov-16.
 */

public class VepVcf {

    private static final Logger Log = Logger.getLogger(VepVcf.class.getName());
    //private File vcfFilePath;

    //Maybe move this inside the method
    //private LinkedHashMap<GenomeVariant, CsqObject> variantHashMap =  new LinkedHashMap<GenomeVariant, CsqObject>(); //Linked hash map preserves order

    //HashMap containing per-variant and allele information
    private LinkedHashMap<String, VariantDataObject> variantHashMap = new LinkedHashMap<String, VariantDataObject>();

    //HashMap containing per sample, per position (and contig), (and including alternate allele information)
    private LinkedHashMap<String, SampleVariantDataObject> sampleVariantHashMap = new LinkedHashMap<String, SampleVariantDataObject>();



    public VCFFileReader openFiles(File vcfFilePath) { //throws IOException  {
        Log.log(Level.INFO, "Opening VEP VCF file");
        //Read in the file
        try {
            final VCFFileReader vcfFile = new VCFFileReader(vcfFilePath, false);
            return vcfFile;
        } catch (Exception e) {
            Log.log(Level.SEVERE, "Could not read VCF file: " + e.getMessage());
            System.exit(-1); //return -1 indicating error
        }
        return null; //This is what it returns if the try catch block fails- needed for compilation but should never happen
    }

    public void parseVepVcf(VCFFileReader vcfFile) {
        Log.log(Level.INFO, "Parsing VEP VCF file");
        //For the alternate alleles
        //Required for code execution as otherwise variable is initialised only in else clause
        boolean variantFiltered = false; //Default setting
        boolean variantSite = false; //Default setting
        String idField = null; //Default setting

        //Obtain VEP Headers- same for the whole VCF file
        String vHeaders = vepHeaders(vcfFile);

        for (final VariantContext vc : vcfFile) {

            //Each allele is associated with its allelenum (in case ordering is changed later on and so that the
            //genotyping part of the code has access to this information)

            //Create an object to store the alleles- preferably immutable (allelenum is dependent on order)
            List<Allele> allAlleles = Collections.unmodifiableList(vc.getAlleles());

            //Store the alleles that are not the reference allele
            List<Allele> altAlleles = vc.getAlternateAlleles();

            //Obtain keys for each transcript entry (header in vcf file)
            CsqUtilities currentCsqRecord = new CsqUtilities();

            List attribute = vc.getAttributeAsList("CSQ");

            ArrayList<String> attributeArr = new ArrayList<String>(); // ArrayLists are ordered and can be indexed into

            // Bug fixed 19/12/2016 to fix issue where variant contexts with only one CSQ entry were erroring
            // This was caused by single objects not being correctly stored as a list of Strings using the htsjdk method
            for (int i = 0; i < attribute.size(); i++){
                attributeArr.add(attribute.get(i).toString());
            }

            ArrayListMultimap<Integer, VepAnnotationObject> csq = currentCsqRecord.createCsqRecordOfVepAnnObjects(
                    vHeaders, attributeArr);

            //Create a CsqObject (optional step)- could leave as hashmap if desired
            CsqObject currentCsqObject = new CsqObject();
            currentCsqObject.setCsqObject(csq);

            variantFiltered = vc.isFiltered();
            variantSite = vc.isVariant();
            idField = vc.getID();

            for (int allele = 0; allele < altAlleles.size(); allele++) {

                //Avoid storing an empty object (as there are no Vep annotations) nested within the VariantDataObject
                //* just denotes an overlapping indel and is not a SNV at that position
                if (altAlleles.get(allele).toString().equals("*")) {
                    //Break out of for loop and start next iteration
                    continue;
                }

                //Key
                GenomeVariant variantObject = createAlleleKey(vc, altAlleles.get(allele).toString());

                //Allele num starts at 1 for the altAlleles, as 0 is the reference allele
                ArrayList<VepAnnotationObject> alleleCsq = currentCsqObject.getSpecificVepAnnObjects(allele + 1);

                //System.out.println(alleleCsq);

                //Data
                VariantDataObject currentVariantDataObject = new VariantDataObject(alleleCsq,
                        variantFiltered, variantSite, idField);

                variantHashMap.put(variantObject.toString(), currentVariantDataObject);

            }


            //This part of the code associates the specific alleles and some additional specific sample-allele metadata
            //with the different samples present in the vcf

            //Extract data associated with each specific genotype within the variant context
            GenotypesContext gt = vc.getGenotypes();
            //Iterate through each sample entry within the variant context
            Iterator<Genotype> gtIter = gt.iterator();
            while (gtIter.hasNext()) {
                Genotype currentGenotype = gtIter.next();

                //Don't add the variant to the sample if there is no call or only a hom ref call
                if (!(currentGenotype.isHomRef()) && !(currentGenotype.isNoCall())) {

                    //Create immutable list here as order is important
                    List<Allele> currentGenotypeAlleles = Collections.unmodifiableList(currentGenotype.getAlleles());

                    //Zygosity
                    String zygosity = obtainZygosity(currentGenotype);

                    //Creation of SampleData object- the data associated once with each sample
                    //(rather than with each specific allele in the sample)
                    //This object will be encapsulated within the object which incorporates the variant-specific
                    //sample information- called SampleVariantDataObject
                    //SampleDataObject currentSampleDataObject =
                    //new SampleDataObject(currentGenotype.getSampleName(),
                    //currentGenotype.isFiltered(), currentGenotype.isMixed(),
                    //currentGenotype.getPloidy(), zygosity, currentGenotype.getGQ());


                    //Obtain depth of both alleles at the locus
                    int locusDepth = calcLocusDepth(currentGenotypeAlleles, allAlleles, currentGenotype);

                    // Iterate through the max of two alleles in the current Genotype (there may be one if deletion ?)
                    for (Allele currentAllele : currentGenotypeAlleles) {

                        //System.out.println(currentAllele);

                        //Skip everything when genotype could not be called- there will be no metadata associated with it
                        if (currentAllele.toString().equals(".")){
                            continue;
                        }

                        int alleleNum = allAlleles.indexOf(currentAllele);
                        int alleleDepth = currentGenotype.getAD()[alleleNum];
                        double alleleFrequency = calcAlleleFrequency(locusDepth, alleleDepth);

                        //For homozygotes allele frequency is always 1 and this is not calculated correctly using the
                        //above method
                        if (currentGenotype.isHom()){
                            alleleFrequency = 1;
                        }

                        // Key for variant object- to enable it to be linked up with the sample later
                        GenomeVariant keyForVariant = createAlleleKey(vc,
                                currentAllele.toString().replaceAll("\\*", ""));

                        // Key for sample variant object- stores variant and allele data which is sample specific
                        SampleVariant currentSampleVariant = new SampleVariant(currentGenotype.getSampleName(),
                                vc.getContig(), vc.getStart(), currentGenotypeAlleles, currentAllele,
                                vc.getReference());

                        // Skip reference allele and where there is an overlapping deletion (don't store it)
                        // This won't create a problem for allele depth as locus depth is calculated above
                        if (currentAllele.isNonReference() && !(currentAllele.toString().equals("*"))) {

                            //System.out.println("Variant to store"); //for test

                            //Data which is associated with the variant and allele but is sample specific
                            SampleVariantDataObject currentSampleVariantDataObject =
                                    new SampleVariantDataObject(keyForVariant, alleleDepth, alleleNum,
                                            currentGenotype.getSampleName(), currentGenotype.isFiltered(),
                                            currentGenotype.isMixed(), currentGenotype.getPloidy(), zygosity,
                                            currentGenotype.getGQ(), alleleFrequency);

                            sampleVariantHashMap.put(currentSampleVariant.toString(), currentSampleVariantDataObject);
                        }

                    }

                }

            }

            //break; //first allele only for ease of testing

        }
    }

        //Test hash map is working correctly
        //System.out.println(sampleVariantHashMap);
        //System.out.println(variantHashMap);


    public GenomeVariant createAlleleKey(VariantContext vc, String altAllele) { //LinkedHashMap
        //Log.log(Level.INFO, "Parsing Alleles");
        //Requires String for GenomeVariant class
        //This is intended as the key to the hashmap
        return new GenomeVariant(vc.getContig(), vc.getStart(),
                vc.getReference().toString().replaceAll("\\*", ""), altAllele);
    }


    public String obtainZygosity(Genotype currentGenotype){

        String zygosity = "UNDETERMINED";

        if (currentGenotype.isHom()) {
            zygosity = "HOM";
        } else if (currentGenotype.isHet()) {
            zygosity = "HET";
        }

        return zygosity;
    }

    public double calcAlleleFrequency(int locusDepth, int alleleDepth) {
        double alleleFrequency = (((double)alleleDepth) / ((double)locusDepth));
        return alleleFrequency;
    }

    public int calcLocusDepth(List<Allele> Alleles, List<Allele> locusAlleles, Genotype currentGenotype){
        int locusDepth = 0;
        for (Allele currentAllele : Alleles) {
            //System.out.println(Alleles);
            //System.out.println(currentAllele);
            //Skip everything when genotype could not be called
            if (currentAllele.toString().equals(".")){
                continue;
            }
            //System.out.println(currentGenotype.getAD()[locusAlleles.indexOf(currentAllele)]);
            locusDepth += (currentGenotype.getAD()[locusAlleles.indexOf(currentAllele)]);
        }
        return locusDepth;
    }

    public String vepHeaders(VCFFileReader vcfFile)   {
        //Create the VCF Header object
        VCFHeader currentHeader = vcfFile.getFileHeader();
        VCFInfoHeaderLine vepInfo = currentHeader.getInfoHeaderLine("CSQ"); //This is null if no annotation has been performed
        String vepHeader = vepInfo.getDescription().split("Format:")[1].trim();
        return vepHeader; //returns the header
    }


    public LinkedHashMap<String, VariantDataObject> getVariantHashMap() {return this.variantHashMap;}

    public LinkedHashMap<String, SampleVariantDataObject> getSampleVariantHashMap() {return this.sampleVariantHashMap;}

}

