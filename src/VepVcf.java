//Dependencies htsjdk library 2.7.0
/*
Comment here
 */

import java.io.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import htsjdk.samtools.*;
import htsjdk.tribble.*;
import htsjdk.tribble.readers.*;
import htsjdk.variant.variantcontext.*;
import htsjdk.variant.variantcontext.writer.*;
import htsjdk.samtools.util.CloseableIterator;
import htsjdk.variant.vcf.*;
import java.util.LinkedHashMap;

import htsjdk.variant.utils.*;
import java.util.List;
import java.util.ArrayList;
import org.broadinstitute.hellbender.utils.reference.ReferenceUtils.*;
import com.google.common.collect.Multimap;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import java.util.Collection;
import com.google.common.collect.TreeMultimap;


/**
 * Created by Sara on 08-Nov-16.
 */

public class VepVcf {

    private static final Logger Log = Logger.getLogger(OpenVEPVCF.class.getName());
    //private File vcfFilePath;

    //Maybe move this inside the method
    private LinkedHashMap<GenomeVariant, CsqObject> variantHashMap =  new LinkedHashMap<GenomeVariant, CsqObject>(); //Linked hash map preserves order

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
        for (final VariantContext vc : vcfFile){
            //System.out.println(vc.getContig());
            //System.out.print(vc.getContig());
            //System.out.print("\t");
            //System.out.print(vc.getStart());
            //System.out.print("\t");
            //System.out.print(vc.getEnd()); //Could be useful for indels etc.
            //System.out.print("\t");
            //System.out.print(vc.getAttributeAsList("ID")); //This is returning null at present- no key found?
            //System.out.print("\t");
            //Allele refAl = vc.getReference();
            //System.out.print(refAl); //Reference allele
            //System.out.print("\t");
            //System.out.println(vc.getAlleles()); //Returns all the alleles
            List<Allele> altAlleles = vc.getAlternateAlleles();
            //System.out.println(altAlleles); //Returns all the potential alternate alleles- test with an indel
            //System.out.print("\t");
            //System.out.print(vc.getID());
            //System.out.print("\t");
            //System.out.print(vc.getPhredScaledQual());
            //System.out.print("\t");
            //System.out.print(vc.isFiltered());
            //System.out.print("\t");
            //System.out.print(vc.getAttribute("DP")); //Depth
            //System.out.print("\t");
            //System.out.print(vc.getAttribute("CSQ")); //Long- commented out for now
            //System.out.print("\t");
            //Need to find a better way of getting a transcript
            ///System.out.print(VariantContextUtils.match(VariantContext vc, ));
            //System.out.print("\t");

            //System.out.print(vc.hasAttribute("Transcript")); //To see if a particular attribute is available as a key
            //System.out.print("\t");


            ///Requires further parsing- do later
            //System.out.print(vc.getGenotypes());
            //System.out.print("\t");


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
                    ArrayList<VepAnnotationObject> forCsq = new ArrayList<VepAnnotationObject>(alleleCsq.get(key)); //Get the correct type for this object
                    //System.out.println(key + " " + alleleCsq.get(key));

                    altAllele = key; //Makes this robust to any changes in the order as the key is dynamically determined

                    GenomeVariant variantObject = new GenomeVariant(vc.getContig(), vc.getStart(),
                            vc.getReference().toString().replaceAll("\\*", ""), altAllele);

                    System.out.println(variantObject);


                    CsqUtilities alleleCsqRecord = new CsqUtilities();
                    System.out.println(alleleCsqRecord.createCsqRecord(forCsq)); //Creation of csqObject- this isn't being properly created
                    System.out.println(alleleCsqRecord.createCsqRecord(forCsq).getClass());

                    /////variantHashMap.put(variantObject, alleleCsqRecord);


                    //Create a CsqObject to hold the data paired with the Genome Variant object as the key
                    //////CsqObject currentCsqObject = new CsqObject(); //Empty object created
                    /////currentCsqObject.setCsqObject((currentCsqRecord.createCsqRecordOfVepAnnObjects(
                            /////currentCsqRecord.vepHeaders(vcfFile), currentCsqRecord.vepAnnotations(vc))));

                    //Need to create a new csqObject to put into the variant hashmap, as the previous one is associated
                    //with the entire CSQ for all alternate alleles

                    /*
                    CsqObject currentCsqObject = new CsqObject(); //Empty object created
                    currentCsqObject.setCsqObject((currentCsqRecord.csqRecord(currentCsqRecord.vepHeaders(vcfFile),
                            currentCsqRecord.vepAnnotations(vc))));

                    for (VepAnnotationObject vepEntry : alleleCsq.get(key)){ //Iterate through Collection<VepAnnotationObject>
                        System.out.println(vepEntry);
                    }
                    */

                    //Turn the multiple VepAnnotationObject entries into a CsqObject


                }


            }else {
                altAllele = altAlleles.get(0).getBaseString(); //Requires String for GenomeVariant class

                //This is intended as the key to the hashmap
                GenomeVariant variantObject = new GenomeVariant(vc.getContig(), vc.getStart(),
                        vc.getReference().toString().replaceAll("\\*", ""), altAllele);

                System.out.println(variantObject);

                //Associate the variant object with the CsqObject on a per record basis
                variantHashMap.put(variantObject, currentCsqObject);

            }
        }

        //Test hash map is working correctly
        //System.out.println(variantHashMap);
        //System.out.print("\n");
        return variantHashMap;

    }
}
