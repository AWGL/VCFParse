import java.io.*;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sara on 07-Nov-16.
 */

public class Main {

    public static void main(String[] args) throws Exception {

        //Declare variables
        String path;

        //Move the path to the vcf file to be analysed
        //path="C:\\Users\\Admin\\Documents\\Work\\VCFtoTab\\ExampleFiles\\vcf.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\vcf.vcf"; //Single sample vcf
        //path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\multisample_ann.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\multisample_ann2.vcf"; //Bugged Ensembl transcripts
        //path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\multisample_ann_new.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\multisample_ann_new2.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\minimisation.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\multialleles.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\multisample_annotated_minimisation.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\multisample_annotated_multialleles.vcf";
        //path="C:\\Users\\Admin\\Documents\\Work\\VCFtoTab\\ExampleFiles\\multisample_annotated_multialleles.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\multisample_annotated_multialleles_test.vcf";
        //path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\multisample_gatk3-6.vcf";
        //path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\multisample_gatk3-6_test.vcf";
        //path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\multisample_gatk3-6_altalleles.vcf";
        //path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\multisample_gatk3-6_altalleles_test.vcf";
        //path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\vcf.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\gatk_36.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\gatk_37.vcf";
        //path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\testingold.vcf";
        //path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\testing.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\gatk_36_new.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\gatk_37_new.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\gatk_36_newann.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\gatk_37_newann.vcf";
        //path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\gatk_37_newannterss.vcf"; //test readfile error
        path="E:\\SomaticAmpliconVcfs\\AWDRF\\161007_M04557_0008_000000000-AWDRF_15M15332-1A_filtered_meta_annotated.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\AL3C0\\160310_M02641_0089_000000000-AL3C0_15M12209_filtered_meta_annotated.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\AWDRF\\161007_M04557_0008_000000000-AWDRF_15M15332-5B_filtered_meta_annotated.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\150410_M00766_0089_000000000-ACBU4_filtered_meta_annotated.vcf";
        //path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\MAF_test.vcf";

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
        //WriteOut writeData = new WriteOut(sampleVariantHashMap, variantHashMap);
        //writeData.writeOutVepAnnotations();

        }
}
