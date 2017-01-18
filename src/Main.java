import java.io.*;
import java.util.LinkedHashMap;

/**
 * Created by Sara on 07-Nov-16.
 */

public class Main {

    public static void main(String[] args) throws Exception {

        //Declare variables
        String path;

        //Move the path to the vcf file to be analysed
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\AWDRF\\161007_M04557_0008_000000000-AWDRF_15M15332-5B_filtered_meta_annotated.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\150410_M00766_0089_000000000-ACBU4_filtered_meta_annotated.vcf";

        //Creates a new File instance by converting given pathname string into an abstract pathname
        File vcf_file = new File(path);

        //Instantiate VepVcf class
        VepVcf retrieveVepVcfData = new VepVcf();

        //Open the file and parse the opened file
        retrieveVepVcfData.parseVepVcf(retrieveVepVcfData.openFiles(vcf_file));

        LinkedHashMap<String, SampleVariantDataObject> sampleVariantHashMap =
                retrieveVepVcfData.getSampleVariantHashMap();
        LinkedHashMap<String, VariantDataObject> variantHashMap = retrieveVepVcfData.getVariantHashMap();

        //System.out.println(sampleVariantHashMap);
        //System.out.println(variantHashMap);

        //TESTING READOUT
        //ReadOut retrieveData = new ReadOut(sampleVariantHashMap, variantHashMap);
        //retrieveData.readOutTest();

        //TESTING WRITEOUT
        WriteOut writeData = new WriteOut(sampleVariantHashMap, variantHashMap);
        writeData.writeOutVepAnnotations();

        }
}
