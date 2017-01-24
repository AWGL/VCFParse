package nhs.genetics.cardiff;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import htsjdk.variant.variantcontext.*;
import htsjdk.variant.vcf.*;
import com.google.common.collect.ArrayListMultimap;

/**
 * Class for extracting VCF genotype and VEP information using htsjdk
 *
 * @author  Sara Rey
 * @since   2016-11-08
 */

public class VepVcf {

    private static final Logger Log = Logger.getLogger(VepVcf.class.getName());
    private VCFFileReader vcfFileReader;
    private String csqHeaders;

    //HashMap containing per-variant and allele information
    private LinkedHashMap<String, VariantDataObject> variantHashMap = new LinkedHashMap<String, VariantDataObject>();

    //HashMap containing per sample, per position (and contig), (and including alternate allele information)
    private LinkedHashMap<String, SampleVariantDataObject> sampleVariantHashMap = new LinkedHashMap<String, SampleVariantDataObject>();

    public VepVcf(File vcfFilePath) {
        this.vcfFileReader = new VCFFileReader(vcfFilePath, false);
    }

    public void parseVepVcf() {
        Log.log(Level.INFO, "Parsing VEP VCF file");

        //Obtain VEP Headers- same for the whole VCF file
        this.setVepHeaders();

        for (final VariantContext vc : vcfFileReader) {

            /* Each allele is associated with its allelenum (in case ordering is changed later on and so that the
            genotyping part of the code has access to this information) */

            List attribute = vc.getAttributeAsList("CSQ");

            ArrayList<String> attributeArr = new ArrayList<String>(); // ArrayLists are ordered and can be indexed into

            // Bug fixed 19/12/2016 to fix issue where variant contexts with only one CSQ entry were erroring
            // This was caused by single objects not being correctly stored as a list of Strings using the htsjdk method
            for (int i = 0; i < attribute.size(); i++){
                attributeArr.add(attribute.get(i).toString());
            }

            ArrayListMultimap<Integer, VepAnnotationObject> csq = CsqUtilities.createCsqRecordOfVepAnnObjects(csqHeaders, attributeArr);

            //Create a CsqObject (optional step)- could leave as hashmap if desired
            CsqObject currentCsqObject = new CsqObject(csq);

            for (int allele = 0; allele < vc.getAlternateAlleles().size(); allele++) {

                /* Avoid storing an empty object (as there are no Vep annotations) nested within the VariantDataObject
                just denotes an overlapping indel and is not a SNV at that position */

                if (vc.getAlternateAlleles().get(allele).toString().equals("*")) {
                    continue; //Break out of for loop and start next iteration
                }

                //Key
                GenomeVariant variantObject = createAlleleKey(vc, vc.getAlternateAlleles().get(allele).toString(), vc.getAlleles().indexOf(vc.getAlternateAlleles().get(allele)));

                //Allele num starts at 1 for the altAlleles, as 0 is the reference allele
                ArrayList<VepAnnotationObject> alleleCsq = currentCsqObject.getSpecificVepAnnObjects(allele + 1);

                //Data
                VariantDataObject currentVariantDataObject = new VariantDataObject(alleleCsq, vc.isFiltered(), vc.isVariant(), vc.getID(), vc.getPhredScaledQual());

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

                    //Obtain depth of both alleles at the locus
                    int locusDepth = calcLocusDepth(currentGenotypeAlleles, vc.getAlleles(), currentGenotype);

                    // Iterate through the max of two alleles in the current Genotype (there may be one if deletion ?)
                    for (Allele currentAllele : currentGenotypeAlleles) {

                        //System.out.println(currentAllele);

                        //Skip everything when genotype could not be called- there will be no metadata associated with it
                        if (currentAllele.toString().equals(".")){
                            continue;
                        }

                        int alleleNum = vc.getAlleles().indexOf(currentAllele);
                        int alleleDepth = currentGenotype.getAD()[alleleNum];
                        double alleleFrequency = calcAlleleFrequency(locusDepth, alleleDepth);

                        //For homozygotes allele frequency is always 1 and this is not calculated correctly using the
                        //above method
                        if (currentGenotype.isHom()){
                            alleleFrequency = 1;
                        }

                        // Key for variant object- to enable it to be linked up with the sample later
                        GenomeVariant keyForVariant = createAlleleKey(vc,
                                currentAllele.toString().replaceAll("\\*", ""), alleleNum);

                        // Key for sample variant object- stores variant and allele data which is sample specific
                        SampleVariant currentSampleVariant = new SampleVariant(currentGenotype.getSampleName(),
                                vc.getContig(), vc.getStart(), currentGenotypeAlleles, currentAllele,
                                vc.getReference(), alleleNum);

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

        }
    }

    private static GenomeVariant createAlleleKey(VariantContext vc, String altAllele, int alleleNum) { //LinkedHashMap
        Log.log(Level.FINE, "Parsing Alleles");
        //Requires String for GenomeVariant class
        //This is intended as the key to the hashmap
        return new GenomeVariant(vc.getContig(), vc.getStart(), vc.getReference().toString().replaceAll("\\*", ""), altAllele, alleleNum);
    }
    private static String obtainZygosity(Genotype genotype){
        if (genotype.isHom()) {
            return "HOM";
        } else if (genotype.isHet()) {
            return "HET";
        } else {
            throw new IllegalArgumentException("Could not determine the genotype");
        }
    }
    private static double calcAlleleFrequency(int locusDepth, int alleleDepth) {
        return (((double)alleleDepth) / ((double)locusDepth));
    }
    private static int calcLocusDepth(List<Allele> Alleles, List<Allele> locusAlleles, Genotype currentGenotype){
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

    private void setVepHeaders() {
        VCFInfoHeaderLine vcfCsqInfoHeaderLine = vcfFileReader.getFileHeader().getInfoHeaderLine("CSQ"); //This is null if no annotation has been performed
        this.csqHeaders = vcfCsqInfoHeaderLine.getDescription().split("Format:")[1].trim();
    }
    public LinkedHashMap<String, VariantDataObject> getVariantHashMap() {return this.variantHashMap;}
    public LinkedHashMap<String, SampleVariantDataObject> getSampleVariantHashMap() {return this.sampleVariantHashMap;}

}

