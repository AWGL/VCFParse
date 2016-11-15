import htsjdk.tribble.AbstractFeatureReader;
import htsjdk.tribble.readers.LineIterator;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.Options;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder;
import htsjdk.variant.vcf.VCFCodec;
import htsjdk.variant.vcf.VCFHeader;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

//Extra imports for testing
import htsjdk.variant.vcf.VCFInfoHeaderLine;

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
        
        VCFHeader currentHeader = new VCFHeader(); //Need to get the vcf file path into this



        try (final VariantContextWriter writer = outputFile == null ? null : new VariantContextWriterBuilder().setOutputFile(outputFile).setOutputFileType(VariantContextWriterBuilder.OutputType.VCF).unsetOption(Options.INDEX_ON_THE_FLY).build();
             final AbstractFeatureReader<VariantContext, LineIterator> reader = AbstractFeatureReader.getFeatureReader(inputFile.getAbsolutePath(), new VCFCodec(), false)) {
            //while ((line = reader.readLine()) != null) {
            System.out.println("line"); //This is just in here for the moment to allow the try except block to work. Replace with better solution.

            System.out.print(currentHeader.getFormatHeaderLines());

            //final ProgressLogger pl = new ProgressLogger(log, 1000000);
            for (final VariantContext vc : reader.iterator()) {
                //vc.fullyDecode((vc.getFormatHeaderLines(vcfFilePath)),true); //Testing
                if (writer != null) {
                    writer.add(vc);
                }
            }
        } catch (Exception e) {

            Log.log(Level.SEVERE, "Could not read VCF file: " + e.getMessage());
        }
    }
}