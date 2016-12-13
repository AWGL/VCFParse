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
import com.google.common.collect.ListMultimap;


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

    //Complete this
    //private LinkedHashMap<String, String> finalHashMap = new LinkedHashMap<String, String>();


    public VCFFileReader openFiles(File vcfFilePath) { //throws IOException  {
        Log.log(Level.INFO, "Opening VEP VCF file");
        //Read in the file
        //try(final VCFFileReader vcfFile = new VCFFileReader(vcfFilePath, false)){
        //VCFHeader currentHeader = vcfFile.getFileHeader();
        //System.out.println(currentHeader); //The file header

        try {
            final VCFFileReader vcfFile = new VCFFileReader(vcfFilePath, false);
            return vcfFile;
        } catch (Exception e) {
            Log.log(Level.SEVERE, "Could not read VCF file: " + e.getMessage());
        }
        return null; //This is what it returns if the try catch block fails- syntax??
    }

    public void parseVepVcf(VCFFileReader vcfFile) {
        Log.log(Level.INFO, "Parsing VEP VCF file");
        //For the alternate alleles
        //Required for code execution as otherwise variable is initialised only in else clause
        boolean variantFiltered = false; //Default setting
        boolean variantSite = false; //Default setting
        String idField = null; //Default setting

        for (final VariantContext vc : vcfFile) {

            ///Work on allele minimisation here?///- no- this is all the variants- maybe do per sample?
            //Associate each allele with an allelenum (in case ordering is changed later on and so that the
            //genotyping part of the code has access to this information)- not done

            //Create an object to store the alleles- preferably immutable as allelenum will be dependent on order- not needed
            List<Allele> allAlleles = Collections.unmodifiableList(vc.getAlleles());

            //Store the alleles that are not the reference allele
            List<Allele> altAlleles = vc.getAlternateAlleles();
            ///System.out.print(vc.getAttributes()); //Allows to obtain what is in the INFO field


            //Make the object to hold the annotations- note this currently iterates every time and gets the same headers (same vcf)
            //Obtain keys for each transcript entry (header in vcf file)

            CsqUtilities currentCsqRecord = new CsqUtilities();
            //System.out.println(vc.getAttributeAsString("CSQ", "null"));
            //System.out.println(currentCsqRecord.vepAnnotations(vc));
            ListMultimap<Integer,VepAnnotationObject> csq = currentCsqRecord.createCsqRecordOfVepAnnObjectsTEST(
                    currentCsqRecord.vepHeaders(vcfFile), vc.getAttributeAsString("CSQ", "null"));

            //Create a CsqObject (optional step)
            CsqObject currentCsqObject = new CsqObject();
            currentCsqObject.setCsqObject(csq);

            //System.out.println(csq);
            //System.out.println(currentCsqObject.getClass());
            //System.out.println(csq.get(1));
            //System.out.println(currentCsqObject.get(1));
            System.out.println(currentCsqObject.getCsqObject());


            /* OLD CODE FOR CSQUTILITIES METHOD
            //The entire CSQ record including all of the entries for this variant context
            CsqUtilities currentCsqRecord = new CsqUtilities();
            //System.out.println(csqObject); //Just gives a reference to the object

            //Create a CsqObject to hold the data paired with the Genome Variant object as the key
            CsqObject currentCsqObject = new CsqObject(); //Empty object created
            currentCsqObject.setCsqObject((currentCsqRecord.createCsqRecordOfVepAnnObjects(
                    currentCsqRecord.vepHeaders(vcfFile), currentCsqRecord.vepAnnotations(vc))));
            //Might be worth retrieving the headers outside of this loop//
            */

            variantFiltered = vc.isFiltered();
            variantSite = vc.isVariant();
            idField = vc.getID();




             //COMMENTED OUT FOR TESTING
            //This part of the code associates a specific alternate allele with its data in the CSQ field
            //if (altAlleles.size() > 1) {
                //Retrieve all entries for each allele and generate a csq object
                //CsqObject alleleCsq = linkMultipleAllelesToCsq(currentCsqObject, altAlleles);
                //System.out.println(alleleCsq);


            for (int allele = 0; allele < altAlleles.size(); allele++){
                System.out.println(altAlleles.get(allele));
                GenomeVariant variantObject = createAlleleKey(vc, altAlleles.get(allele).toString());

                Iterator<VepAnnotationObject> csqIter = currentCsqObject.getSpecificCsqObject(allele+1).iterator();
                while (csqIter.hasNext()) {
                    VepAnnotationObject csqEntry = csqIter.next();
                    System.out.println(csqEntry);
                    System.out.println(csqEntry.getEntireVepRecordValues());
                }


                Collection<VepAnnotationObject> alleleCsq = currentCsqObject.getSpecificCsqObject(allele);

                VariantDataObject currentVariantDataObject = new VariantDataObject(alleleCsq,
                        variantFiltered, variantSite, idField);

                variantHashMap.put(variantObject.toString(), currentVariantDataObject);

            }

                /*
                for (String key : alleleCsq.keySet()) {
                    GenomeVariant variantObject = createAlleleKey(vc, key); //Robust to any changes in allele ordering

                    VariantDataObject currentVariantDataObject = new VariantDataObject(currentCsqObject,
                            variantFiltered, variantSite, idField);

                    variantHashMap.put(variantObject.toString(), currentVariantDataObject);
                }
                */

                //parseMultipleAlleles(vc, alleleCsq);

            //} else {
                //String altAllele = altAlleles.get(0).getBaseString();
                //GenomeVariant variantObject = createAlleleKey(vc, altAllele);

                //Collection<VepAnnotationObject> alleleCsq = currentCsqObject.getSpecificCsqObject(1);

                //VariantDataObject currentVariantDataObject = new VariantDataObject(alleleCsq,
                        //variantFiltered, variantSite, idField);

                //variantHashMap.put(variantObject.toString(), currentVariantDataObject);

            //}


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
                    SampleDataObject currentSampleDataObject =
                            new SampleDataObject(currentGenotype.getSampleName(),
                                    currentGenotype.isFiltered(), currentGenotype.isMixed(),
                                    currentGenotype.getPloidy(), zygosity, currentGenotype.getGQ());

                    //Testing
                    /*
                    for (int a = 0 ; a < currentGenotype.getAD().length; a++) {
                        System.out.println(vc.getAlleles().get(a));
                        System.out.println(currentGenotype.getAD()[a]);
                    }
                    */

                    //System.out.println(currentGenotypeAlleles);

                    //COMMENTED OUT FOR TESTING
                    //Creation of the SampleVariantData object and sampleVariantHashMap
                    //Locate what is the key in the variantHashMap for the specific allele
                    //Generate keyForVariant
                    for (Allele currentAllele : currentGenotypeAlleles) {
                        if (currentAllele.isNonReference()) {

                            //System.out.println(currentAllele.toString().replaceAll("\\*", ""));
                            //System.out.println(allAlleles.indexOf(currentAllele));

                            //System.out.println(currentGenotype.getAD()[allAlleles.indexOf(currentAllele)]);

                            int alleleNum = allAlleles.indexOf(currentAllele);

                            GenomeVariant keyForVariant = createSampleVariantKey(vc, currentAllele);

                            SampleVariant currentSampleVariant = new SampleVariant(currentGenotype.getSampleName(),
                                    vc.getContig(), vc.getStart(), currentGenotypeAlleles, currentAllele,
                                    vc.getReference());

                            SampleVariantDataObject currentSampleVariantDataObject =
                                    new SampleVariantDataObject(keyForVariant, currentGenotype.getAD()[alleleNum],
                                            alleleNum, currentSampleDataObject);

                            sampleVariantHashMap.put(currentSampleVariant.toString(), currentSampleVariantDataObject);
                        }

                    }

                }

            }

            //break; //first allele only for ease of testing

        }

        //Test hash map is working correctly

        //System.out.println(sampleVariantHashMap);
        //System.out.println(variantHashMap);

        /*
        System.out.println(sampleVariantHashMap.get("23M,1:241663902 TGAGA"));
        System.out.println(sampleVariantHashMap.get("23M,1:241663902 T").getVariantObjectKey());
        System.out.println(sampleVariantHashMap.get("23M,1:241663902 TGAGA").getVariantObjectKey());
        System.out.println(variantHashMap.get(sampleVariantHashMap.get("23M,1:241663902 TGAGA").getVariantObjectKey()));
        System.out.println(variantHashMap.get("1:241663902TGA>TGAGA"));


        Iterator<VepAnnotationObject> vpIter = variantHashMap.get(sampleVariantHashMap.get("23M,1:241663902 TGAGA").
                getVariantObjectKey()).getCsqObject().getEntireCsqObject().iterator();
        while (vpIter.hasNext()){
            System.out.println(vpIter.next().getVepRecord());

        */
        }



        //This is hom ref so although it is in the sample variant hash map it won't be found in the variant hash map
        //This is het ref not hom ref, hom ref has not been stored
        //System.out.println(variantHashMap.get("1:241663902TGA>TGA"));


        //return variantHashMap;
        //return sampleHashMap

    public GenomeVariant createAlleleKey(VariantContext vc, String altAllele) { //LinkedHashMap
        //Log.log(Level.INFO, "Parsing Alleles");
        //Requires String for GenomeVariant class
        //This is intended as the key to the hashmap
        return new GenomeVariant(vc.getContig(), vc.getStart(),
                vc.getReference().toString().replaceAll("\\*", ""), altAllele);
    }


    /*
    public CsqObject linkMultipleAllelesToCsq(CsqObject currentCsqObject, List<Allele> altAlleles) { //LinkedHashMap
        //Log.log(Level.INFO, "Parsing Alleles");
        //Create an appropriate store to associate the specific csq entries with the alt allele
        Multimap<Integer, VepAnnotationObject> alleleCsq = ArrayListMultimap.create();

        //Create a map associating multiple CSQ entries with the correct alternate allele
        //This for loop needs to start at 1 because of the current naming of the CSQ Objects numerically

        //System.out.println(altAlleles.get(alleleIndex));
        System.out.println(currentCsqObject.getCsqObject());
        System.out.println(altAlleles.get(1));

        //alleleCsq.put((altAlleles.get(alleleIndex).getBaseString()), (vepAnn));

        CsqObject alleleCsqObject = new CsqObject();
        alleleCsqObject.setCsqObject(alleleCsq);

        return alleleCsqObject;

}
*/


    public GenomeVariant createSampleVariantKey(VariantContext vc, Allele currentAllele){

        return new GenomeVariant(vc.getContig(), vc.getStart(), vc.getReference().toString().replaceAll("\\*", ""),
                currentAllele.toString().replaceAll("\\*", ""));

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


    public LinkedHashMap<String, VariantDataObject> getVariantHashMap() {return this.variantHashMap;}

    public LinkedHashMap<String, SampleVariantDataObject> getSampleVariantHashMap() {return this.sampleVariantHashMap;}

}

