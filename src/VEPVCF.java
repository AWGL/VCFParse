//Dependencies htsjdk library 2.7.0 and gatk utils 2.6
/*
The issue with opening the file twice should be resolved soon in the tribble package (the VariantContextAdaptors
package referred to in the htsjdk doccumentation appears to be part of gatk and getting that set up properly did not
look very easy
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
import org.broadinstitute.hellbender.utils.reference.ReferenceUtils.*;


/**
 * Created by Sara on 08-Nov-16.
 */

public class VepVcf {

    private static final Logger Log = Logger.getLogger(OpenVEPVCF.class.getName());
    private File vcfFilePath;
    private LinkedHashMap<GenomeVariant, CsqObject> variantHashMap =  new LinkedHashMap<GenomeVariant, CsqObject>(); //Linked hash map preserves order

    //Constructor- invoked at the time of object creation
    public VepVcf(File vcfFilePath) {
        this.vcfFilePath = vcfFilePath;
    }

    public void openFiles() { //throws IOException  {

        Log.log(Level.INFO, "Opening VEP VCF file");
        //Declare HashMap


        //For the alternate alleles
        Object altAllele = null; //Required for code execution as otherwise variable is initialised only in else clause

        //Read in the file
        //try(final VCFFileReader vcfFile = new VCFFileReader(vcfFilePath, false)){
            //VCFHeader currentHeader = vcfFile.getFileHeader();
            //System.out.println(currentHeader); //The file header

            final VCFFileReader vcfFile = new VCFFileReader(vcfFilePath, false);
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
                List altAlleles = vc.getAlternateAlleles();
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

                if (altAlleles.size() > 1){
                    //System.out.println("Loop");
                    altAllele = altAlleles.get(0);

                    //Logic required here to deal with more than one alternate allele//
                    //Or perhaps we directly put it out as a List/Array- deal with in getter for this//


                }else{
                    //System.out.println("No loop");
                    altAllele = altAlleles.get(0);
                    //System.out.println(altAllele);
                }

                //This is intended as the key to the hashmap
                GenomeVariant variantObject = new GenomeVariant(vc.getContig(), vc.getStart(),
                        vc.getReference().toString().replaceAll("\\*",""), altAllele.toString()); //test
                System.out.print(variantObject); //This can be the key for each variant entry
                System.out.print("\n");

                //Make the object to hold the annotations- note this currently iterates every time and gets the same headers (same vcf)
                //Obtain keys for each transcript entry (header in vcf file)

                //The entire CSQ record including all of the entries for this variant context
                CSQ c = new CSQ();
                //System.out.println(csqObject); //Just gives a reference to the object

                //c.vepHeaders(); //This object should contain the headers
                ////System.out.println(c.vepHeaders(vcfFile)); //Checking that the object returns the headers

                //c.vepAnnotations(vc); //This object should be an ArrayList of the annotations in the CSQ field
                //System.out.println(c.vepAnnotations(vc)); //Checking that the object returns the datalist

                //Create a CSQ recordset per this Variant Context entry
                //c.CSQRecord(c.vepHeaders(vcfFile),c.vepAnnotations(vc)); //Might be worth retrieving the headers outside of this loop
                //System.out.println(c.CSQRecord(c.vepHeaders(vcfFile),c.vepAnnotations(vc))); //Print to check

                //Create a CsqObject to hold the data paired with the Genome Variant object as the key
                CsqObject currentCsqObject = new CsqObject(c.CSQRecord(c.vepHeaders(vcfFile),c.vepAnnotations(vc)));
                //Might be worth retrieving the headers outside of this loop
                variantHashMap.put(variantObject,currentCsqObject);


                //csqObject.vepHashMap(csqObject.vepHeaders(vcfFile),csqObject.vepAnnotations(vc)); //FIX THIS LINE

                //CSQObject t = new CSQObject(c.vepHeaders(), c.vepAnnotations(vc));
                        //csqObject.vepAnnotations(vc).toString().replaceAll("^\\[","").replaceAll("\\]$",""));
                //t.tester();

                System.out.println(variantHashMap);
                System.out.print("\n");

            }


        //}catch(Exception e) {

            //Log.log(Level.SEVERE, "Could not read VCF file: " + e.getMessage());
        //}

    }
}
