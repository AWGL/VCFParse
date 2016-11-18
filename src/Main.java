import java.io.*;
import java.util.LinkedHashMap;

/**
 * Created by Sara on 07-Nov-16.
 */

public class Main {

    public static void main(String[] args) {

        //Declare variables
        String path;


        //System.out.println("Hello World");

        //An instance of the class needs to be created before the non-static methods can be referenced
        //Instance of the TestClass, which I have called obj
        //TestClass obj = new TestClass ();

        //calling the parSer method of the TestClass within the obj instance of the class
        //obj.parSer();

        //Stuff for fiddling- move in a bit- to create the File object for the vcf
        //path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\160916_M00766_0079_000000000-ATNNU_16M13176_filtered_meta_annotated.vcf";
        //path="C:\\Users\\Admin\\Documents\\Work\\VCFtoTab\\ExampleFiles\\vcf.vcf";
        path="C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\ExampleFiles\\vcf.vcf";
        //Creates a new File instance by converting given pathname string into an abstract pathname
        File vcf_file = new File(path);

        //Verifying type is correct and the absolute filepath is correct
        String type = ((Object)vcf_file).getClass().getName();
        //System.out.println(type);
        //System.out.println(vcf_file);

        //Instantiate second class
        VepVcf obj2 = new VepVcf();
        //t obj2 = new t(vcf_file); //temp for testing purposes

        //Open the file
        //obj2.parseVepVCF(obj2.openFiles(vcf_file));
        //obj2.parseVepVcf(); //Write this method
        //System.out.println(obj2.openFiles());
        //System.out.println(obj2.parseVepVCF(obj2.openFiles(vcf_file)));
        LinkedHashMap obj3 = obj2.parseVepVCF(obj2.openFiles(vcf_file));

        System.out.println(obj3);
        //System.out.println(obj3.getCsqObject());





        //Parse the file



    }

    //Method probably not needed- delete later
    public static File getAbsoluteFile(File root, String path)  {
        File file = new File(path);
        if (file.isAbsolute())
                return file;
        if (root == null)
                return null;
        return new File(root, path);
    }
}
