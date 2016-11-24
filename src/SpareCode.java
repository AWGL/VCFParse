import htsjdk.tribble.AbstractFeatureReader;
import htsjdk.tribble.readers.LineIterator;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.Options;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder;
import htsjdk.variant.vcf.VCFCodec;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Sara on 15-Nov-16.
 * Dumped the syntax for creating an output file in here as not wanted at present and don't want to over-complicate
 * things
 */
public class SpareCode {

    public SpareCode(File vcfFilePath) {
        this.vcfFilePath = vcfFilePath;
    }

    private static final Logger Log = Logger.getLogger(OpenVEPVCF.class.getName());
    private File vcfFilePath;

    public void openWriteFile() {
        //Need these temporarily for the code below to execute- find a better solution later
        File inputFile = vcfFilePath;
        File outputFile = null;

        try (final VariantContextWriter writer = outputFile == null ? null : new VariantContextWriterBuilder().setOutputFile(outputFile).setOutputFileType(VariantContextWriterBuilder.OutputType.VCF).unsetOption(Options.INDEX_ON_THE_FLY).build();
             final AbstractFeatureReader<VariantContext, LineIterator> reader = AbstractFeatureReader.getFeatureReader(inputFile.getAbsolutePath(), new VCFCodec(), false)) {
        } catch (Exception e) {

            Log.log(Level.SEVERE, "Could not read VCF file: " + e.getMessage());
        }


        //Just some code to show that the convert to minimal representation method of GenomeVariant works
            /*
            System.out.println(variantObject);
            System.out.println(variantObject.getRef());
            System.out.println(variantObject.getAlt());
            variantObject.convertToMinimalRepresentation();
            System.out.println(variantObject.getRef());
            System.out.println(variantObject.getAlt());
            */




    }
}