package nhs.genetics.cardiff;

import org.apache.commons.cli.*;

import javax.imageio.IIOException;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Program for writing VCF file to text output
 *
 * @author  Sara Rey
 * @since   2016-11-07
 */

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());
    private static final String program = "VCFParse";
    private static final String version = "pre-release";

    public static void main(String[] args) {

        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine = null;
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();

        options.addOption("V", "Variant", true, "Path to input VCF file");
        options.addOption("T", "Text", true, "Path to output Text file");

        try {
            commandLine = commandLineParser.parse(options, args);

            if (!commandLine.hasOption("V") | !commandLine.hasOption("T")) throw new NullPointerException("Incorrect arguments");

        } catch (ParseException | NullPointerException e){
            formatter.printHelp(program + " " + version, options);
            log.log(Level.SEVERE, e.getMessage());
            System.exit(-1);
        }

        //Instantiate VepVcf class and pass VCF file location
        VepVcf vepVcf = new VepVcf(new File(commandLine.getOptionValue("V")));

        //Open the file and parse the opened file
        vepVcf.parseVepVcf();

        LinkedHashMap<String, SampleVariantDataObject> sampleVariantHashMap = vepVcf.getSampleVariantHashMap();
        LinkedHashMap<String, VariantDataObject> variantHashMap = vepVcf.getVariantHashMap();

        //write to file
        try {
            WriteOut writeOut = new WriteOut(sampleVariantHashMap, variantHashMap, new File(commandLine.getOptionValue("V")));
            writeOut.writeOutVepAnnotations();
        } catch (IOException e){
            log.log(Level.SEVERE, "Could not write to file:" + e.getMessage());
            System.exit(-1);
        }

    }
}
