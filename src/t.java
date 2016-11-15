//This turns the import into a fully decoded one. It's a bit messy right now though- might add to the VepVcf class later

import htsjdk.tribble.AbstractFeatureReader;
import htsjdk.tribble.readers.LineIterator;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.Options;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder;
import htsjdk.variant.vcf.VCFCodec;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//Extra imports for testing
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFFileReader;

/**
 * Created by Sara on 15-Nov-16.
 */
public class t {

    private static final Logger Log = Logger.getLogger(OpenVEPVCF.class.getName());
    private File vcfFilePath;

    //Constructor- invoked at the time of object creation
    public t(File vcfFilePath) {
        this.vcfFilePath = vcfFilePath;
    }

    public void openFiles() { //throws IOException  {

        //Declare
        Log.log(Level.INFO, "Opening VEP VCF file");
        //Declare HashMap
        HashMap<Integer, String> vepHashMap = new HashMap<Integer, String>();

        //String line; //Not required

        //final File inputFile = new File(vcfFilePath); //How to input a file
        //final File outputFile = args.length >= 2 ? new File(args[1]) : null; //for output

        //Need these temporarily for the code below to execute- find a better solution later
        File inputFile = vcfFilePath; //Here we have the input file
        File outputFile = null;

        //Create a vcf header
        //Testing
        //VCFInfoHeaderLine currentHeaderLine = new VCFInfoHeaderLine();

        //Read in the file
        VCFFileReader vcfFile = new VCFFileReader(vcfFilePath, false); //False to not require index

        //Create the VCF Header object
        //VCFHeader currentHeader = new VCFHeader(); //Need to get the vcf file path into this
        VCFHeader currentHeader = vcfFile.getFileHeader(); //This is very messy in light of opening the file below as well- FIX


        try (final VariantContextWriter writer = outputFile == null ? null : new VariantContextWriterBuilder().setOutputFile(outputFile).setOutputFileType(VariantContextWriterBuilder.OutputType.VCF).unsetOption(Options.INDEX_ON_THE_FLY).build();
             final AbstractFeatureReader<VariantContext, LineIterator> reader = AbstractFeatureReader.getFeatureReader(inputFile.getAbsolutePath(), new VCFCodec(), false)) {
            //while ((line = reader.readLine()) != null) {
            System.out.println("line"); //This is just in here for the moment to allow the try except block to work. Replace with better solution.

            //Access data in header object
            //System.out.print(currentHeader.getFormatHeaderLines());
            //System.out.print("\n");
            //System.out.print(currentHeader.getHeaderFields());
            System.out.println(currentHeader.getInfoHeaderLines());
            System.out.println(currentHeader.getInfoHeaderLine("CSQ"));

            //WORK HERE ON OBTAINING THE HEADER LINES DIRECT FROM THE VCF

            //final ProgressLogger pl = new ProgressLogger(log, 1000000);
            for (final VariantContext vc : reader.iterator()) {
                //vc.fullyDecode((vc.getFormatHeaderLines(vcfFilePath)),true); //This won't work
                if (writer != null) {
                    writer.add(vc);
                }
                //Fully decoded?- this converts e.g. ints to int objects in java instead of leaving as a string
                VariantContext nvc = vc.fullyDecode(currentHeader, false); //boolean is lenient decoding
                //System.out.print("\n");
                //System.out.print(nvc.isFullyDecoded()); //Boolean whether the object is fully decoded or not

                //System.out.println(nvc.getAttributes());

                //System.out.println(vc.getReference()); //Reference allele
                //System.out.println(nvc.getReference()); //Reference allele
                Object refAllele = nvc.getReference();
                String refAllele2 = refAllele.toString();
                String refAllele3 = nvc.getReference().toString();
                //System.out.println(refAllele3);
                String refAllele4 = refAllele3.replaceAll("\\*","");
                //System.out.println(refAllele4);
                String refAllele5 = nvc.getReference().toString().replaceAll("\\*","");
                //System.out.println(refAllele5);
            }

        } catch (Exception e) {

            Log.log(Level.SEVERE, "Could not read VCF file: " + e.getMessage());
        }
    }
}