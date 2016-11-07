import java.io.*;
/**
 * Created by Sara on 07-Nov-16.
 */

public class Main {

    public static void main(String[] args) {

        //Temp for variables
        String path;


        System.out.println("Hello World");

        //An instance of the class needs to be created before the non-static methods can be referenced
        //Instance of the TestClass, which I have called obj
        TestClass obj = new TestClass ();

        //calling the parSer method of the TestClass within the obj instance of the class
        obj.parSer();

        //Stuff for fiddling- move in a bit
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\160916_M00766_0079_000000000-ATNNU_16M13176_filtered_meta_annotated.vcf";
        File vcf_file = new File(path);

        //Instantiate second class
        OpenVEPVCF obj2 = new OpenVEPVCF(vcf_file);
        obj2.openFiles();


    }
}
