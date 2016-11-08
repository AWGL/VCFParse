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

        Log.log(Level.INFO, "Opening VEP VCF file");

        String line;

        //final File inputFile = new File(vcfFilePath); //How to input a file
        //final File outputFile = args.length >= 2 ? new File(args[1]) : null; //for output

        //Need theses temporarily for the code below to execute- find a better solution later
        File inputFile = vcfFilePath;
        File outputFile = null;

        try(final VariantContextWriter writer = outputFile == null ? null : new VariantContextWriterBuilder().setOutputFile(outputFile).setOutputFileType(VariantContextWriterBuilder.OutputType.VCF).unsetOption(Options.INDEX_ON_THE_FLY).build();
            final AbstractFeatureReader<VariantContext, LineIterator> reader = AbstractFeatureReader.getFeatureReader(inputFile.getAbsolutePath(), new VCFCodec(), false)) {
            //while ((line = reader.readLine()) != null) {
            System.out.println("line");

            //final ProgressLogger pl = new ProgressLogger(log, 1000000);
            for (final VariantContext vc : reader.iterator()) {
                if (writer != null) {
                    writer.add(vc);
                }
                System.out.println(vc.getContig());
                System.out.println(vc.getStart());
            }

        }catch(Exception e) {

            Log.log(Level.SEVERE, "Could not read VCF file: " + e.getMessage());
        }
    }
}
