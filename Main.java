package nhs.genetics.cardiff;

import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.vcf.VCFFileReader;
import nhs.genetics.cardiff.framework.GenomeVariant;
import org.apache.commons.cli.*;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Program for writing VCF file to text output. VCF should be annotated with VEP.
 *
 * @author  Sara Rey
 * @since   2016-11-07
 *
 */

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());
    private static final String program = "VCFParse";
    private static final String version = "pre-release";

    public static void main(String[] args) {

        //parse command line
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine = null;
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();

        options.addOption("V", "Variant", true, "Path to input VCF file");
        options.addOption("T", "Transcript", true, "Path to preferred transcript list");
        options.addOption("C", "Classification", true, "Path to VCF with classifications");
        options.addOption("K", "KnownRefSeq", false, "Report only known RefSeq transcripts (NM)");

        try {
            commandLine = commandLineParser.parse(options, args);

            if (!commandLine.hasOption("V")){
                throw new NullPointerException("Incorrect arguments");
            }

        } catch (ParseException | NullPointerException e){
            formatter.printHelp(program + " " + version, options);
            log.log(Level.SEVERE, e.getMessage());
            System.exit(-1);
        }

        //get opts
        boolean onlyReportKnownRefSeq = commandLine.hasOption("K");

        //parse preferred transcripts list
        HashSet<String> preferredTranscripts = new HashSet<>();

        if (commandLine.hasOption("T")){
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(commandLine.getOptionValue("T")))) {
                String line = bufferedReader.readLine();

                while (line != null) {
                    preferredTranscripts.add(line);
                }

            } catch (IOException e) {
                log.log(Level.SEVERE, "Could not read transcript list: " + e.getMessage());
                System.exit(-1);
            }
        }

        //parse classification VCF
        HashMap<GenomeVariant, Integer> classifiedVariants = new HashMap<>();

        if (commandLine.hasOption("C")){
            VCFFileReader vcfFileReader = new VCFFileReader(new File(commandLine.getOptionValue("C")));

            //read VCF line by line
            vcfFileReader.iterator()
                    .stream()
                    .forEach(variantContext -> {

                        //create genome variants
                        for (Allele alternativeAllele : variantContext.getAlternateAlleles()){

                            //create genome variant & convert to minimal representation
                            GenomeVariant genomeVariant = new GenomeVariant(variantContext.getContig(), variantContext.getStart(), variantContext.getReference().getBaseString(), alternativeAllele.getBaseString());
                            genomeVariant.convertToMinimalRepresentation();

                            //get classification
                            classifiedVariants.put(genomeVariant, Integer.parseInt((String) variantContext.getAttribute("Classification")));

                        }

                    });
        }

        //parse VEP annotated VCF file
        VepVcf vepVcf = new VepVcf(new File(commandLine.getOptionValue("V")));
        vepVcf.parseVepVcf();

        //write to file
        try {
            WriteOut.writeToTable(vepVcf, preferredTranscripts, classifiedVariants, onlyReportKnownRefSeq);
        } catch (IOException e){
            log.log(Level.SEVERE, "Could not write to file:" + e.getMessage());
            System.exit(-1);
        }

    }

}
