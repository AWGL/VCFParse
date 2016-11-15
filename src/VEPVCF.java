import java.io.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import htsjdk.samtools.*;
import htsjdk.tribble.*;
import htsjdk.tribble.readers.*;
import htsjdk.variant.variantcontext.*;
import htsjdk.variant.variantcontext.writer.*;
import htsjdk.variant.vcf.*;
import java.util.HashMap;

import htsjdk.variant.utils.*;
import java.util.List;

/**
 * Created by Sara on 08-Nov-16.
 */

public class VepVcf {

    private static final Logger Log = Logger.getLogger(OpenVEPVCF.class.getName());
    private File vcfFilePath;

    //Constructor- invoked at the time of object creation
    public VepVcf(File vcfFilePath) {
        this.vcfFilePath = vcfFilePath;
    }

    public void openFiles() { //throws IOException  {

        //Declare
        Log.log(Level.INFO, "Opening VEP VCF file");
        //Declare HashMap
        HashMap<Integer, String> vepHashMap = new HashMap<Integer, String>();

        //String line; //Not required

        //For the alternate alleles
        Object altAllele = null; //Required for code execution as otherwise variable is initialised only in else clause


        //final File inputFile = new File(vcfFilePath); //How to input a file
        //final File outputFile = args.length >= 2 ? new File(args[1]) : null; //for output

        //Need these temporarily for the code below to execute- find a better solution later- NEEDS A FIX
        File inputFile = vcfFilePath;

        //Read in the file
        try(final AbstractFeatureReader<VariantContext, LineIterator> reader = AbstractFeatureReader.getFeatureReader(inputFile.getAbsolutePath(), new VCFCodec(), false)) {
            //while ((line = reader.readLine()) != null) {
            System.out.println("line"); //This is just in here for the moment to allow the try except block to work. Replace with better solution.

            System.out.println(reader.getHeader());



            //final VariantContext vcfFile = new VariantContext();  //??
            //VCFHeader currentHeader = reader.getFileHeader();
            //System.out.println(currentHeader);

            /*
            //final ProgressLogger pl = new ProgressLogger(log, 1000000);
            for (final VariantContext vc : reader.iterator()) { //Creation of VariantContext object and iteration over all records
                //System.out.print(vc.getContig());
                //System.out.print("\t");
                //System.out.print(vc.getStart());
                //System.out.print("\t");
                //System.out.print(vc.getEnd()); //Could be useful for indels etc.
                //System.out.print("\t");
                //System.out.print(vc.getAttributeAsList("ID")); //This is returning null at present- no key found?
                //System.out.print("\t");
                Allele refAl = vc.getReference();
                System.out.print(refAl); //Reference allele
                //System.out.print("\t");
                //System.out.println(vc.getAlleles()); //Returns all the alleles
                List altAlleles = vc.getAlternateAlleles();
                System.out.println(altAlleles); //Returns all the potential alternate alleles- test with an indel
                //System.out.print("\t");
                //System.out.print(vc.getID());
                //System.out.print("\t");
                //System.out.print(vc.getPhredScaledQual());
                //System.out.print("\t");
                //System.out.print(vc.isFiltered());
                //System.out.print("\t");
                //System.out.print(vc.getAttribute("DP")); //Depth
                //System.out.print("\t");
                ///System.out.print(vc.getAttribute("CSQ")); //Long- commented out for now
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
                    System.out.println("Loop");
                    altAllele = altAlleles.get(0);

                    //Logic required here to deal with more than one alternate allele//


                }else{
                    System.out.println("No loop");
                    altAllele = altAlleles.get(0);
                    System.out.println(altAllele);
                }

                //This is intended as the key to the hashmap
                GenomeVariant variantObject = new GenomeVariant(vc.getContig(), vc.getStart(),
                        vc.getReference().toString().replaceAll("\\*",""), altAllele.toString()); //test
                System.out.print(variantObject); //This can be the key for each variant entry
                System.out.print("\n");

                //Make the object to hold the annotations- note this currently iterates every time and gets the same headers (same vcf)
                //Obtain keys for each transcript entry (header in vcf file)
                CSQ csqObject = new CSQ(vcfFilePath);
                //System.out.println(csqObject); //Just gives a reference to the object
                csqObject.vepHeaders(); //This object should contain the headers
                System.out.println(csqObject.vepHeaders()); //Checking that the object contains the headers


                String CSQ = vc.getAttributeAsString("CSQ", "null"); //Fix this variable name
                //System.out.print(CSQ); //Not needed for now
                for (String splitVal : CSQ.split("\\,")) { //Splits multiple records per entry
                    System.out.println(splitVal); //Prints out the individual records
                    //for (String splitEntries: splitVal.split("\\|")){ //Need to escape the string because it's regex
                    //System.out.println(splitEntries);
                    //}
                }
                System.out.print("\n");

            }

            */

        }catch(Exception e) {

            Log.log(Level.SEVERE, "Could not read VCF file: " + e.getMessage());
        }

    }
}
