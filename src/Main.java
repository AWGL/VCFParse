package nhs.genetics.cardiff;

import java.io.*;
import java.util.LinkedHashMap;
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
    private static final String version = "null";

    public static void main(String[] args) {

        //Instantiate VepVcf class and pass VCF file location
        VepVcf vepVcf = new VepVcf(new File(args[0]));

        //Open the file and parse the opened file
        vepVcf.parseVepVcf();

        LinkedHashMap<String, SampleVariantDataObject> sampleVariantHashMap = retrieveVepVcfData.getSampleVariantHashMap();
        LinkedHashMap<String, VariantDataObject> variantHashMap = retrieveVepVcfData.getVariantHashMap();

        WriteOut writeOut = new WriteOut(sampleVariantHashMap, variantHashMap);
        writeOut.writeOutVepAnnotations();

    }
}
