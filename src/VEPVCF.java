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
            System.out.println("line"); //This is just in here for the moment to allow the try except block to work. Replace with better solution.

            //final ProgressLogger pl = new ProgressLogger(log, 1000000);
            for (final VariantContext vc : reader.iterator()) {
                if (writer != null) {
                    writer.add(vc);
                }
                System.out.print(vc.getContig());
                System.out.print("\t");
                System.out.print(vc.getStart());
                System.out.print("\t");
                System.out.print(vc.getEnd()); //Could be useful for indels etc.
                System.out.print("\t");
                System.out.print(vc.getAttributeAsList("ID")); //This is returning null at present- no key found?
                System.out.print("\t");
                System.out.print(vc.getReference()); //Reference allele
                System.out.print("\t");
                System.out.print(vc.getAlleles()); //Returns all the potential alternate alleles- test with an indel
                System.out.print("\t");
                System.out.print(vc.getID());
                System.out.print("\t");
                System.out.print(vc.getPhredScaledQual());
                System.out.print("\t");
                System.out.print(vc.isFiltered());
                System.out.print("\t");
                System.out.print(vc.getAttribute("DP")); //Depth
                System.out.print("\t");
                //System.out.print(vc.getAttribute("CSQ")); //Long- commented out for now
                System.out.print("\t");
                //Need to find a better way of getting a transcript
                //System.out.print(VariantContextUtils.match(VariantContext vc, ));
                System.out.print("\t");

                //System.out.print(vc.hasAttribute("Transcript")); //To see if a particular attribute is available as a key
                System.out.print("\t");


                //Requires further parsing
                System.out.print(vc.getGenotypes());
                System.out.print("\t");


                //System.out.print(vc.getAttributes()); //Allows to obtain what is in the INFO field
                System.out.print(vc.getAttributeAsString("CSQ","null"));
                System.out.print("\n");
            }

        }catch(Exception e) {

            Log.log(Level.SEVERE, "Could not read VCF file: " + e.getMessage());
        }
    }
}
