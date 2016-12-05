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


            GenotypesContext gt = vc.getGenotypes();
            Iterator<Genotype> gtIter = vc.getGenotypes().iterator();
            while (gtIter.hasNext()) {
                //System.out.println(gt); // Iterator Object
                Genotype currentGenotype = gtIter.next();
                //System.out.println(currentGenotype);
                //System.out.println(gt.next().getClass()); //Can use methods associated with FastGenotype
                //System.out.println(currentGenotype.getSampleName());
                //System.out.println(currentGenotype.isNoCall()); //./.
                //System.out.println(currentGenotype.isHomRef());
                //System.out.println(currentGenotype.isFiltered());
            }

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


                    variantHashMap.put(variantObject, alleleCsqObject);

                    System.out.println(variantFiltered);
                    System.out.println(variantSite);

                    //Turn the multiple VepAnnotationObject entries into a CsqObject

                }


            }else {
                altAllele = altAlleles.get(0).getBaseString(); //Requires String for GenomeVariant class

                //This is intended as the key to the hashmap
                GenomeVariant variantObject = new GenomeVariant(vc.getContig(), vc.getStart(),
                        vc.getReference().toString().replaceAll("\\*", ""), altAllele);

                //System.out.println(variantObject);

                //Associate the variant object with the CsqObject on a per record basis
                variantHashMap.put(variantObject, currentCsqObject);

                System.out.println(variantFiltered);
                System.out.println(variantSite);

            }
        }

        //Test hash map is working correctly
        return variantHashMap;

    }
}
