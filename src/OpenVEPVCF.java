import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
//import java.io.FileReader; //Redundant import- java.io.* already imported

/**
 * Created by Sara on 07-Nov-16.
 */

public class OpenVEPVCF {

    //Stuff that is wanted
    private static final Logger Log = Logger.getLogger(OpenVEPVCF.class.getName());

    private File vcfFilePath;
    //private List<String> theRecords = new ArrayList<String>(); //uncomment to store results in an array

    //Constructor- invoked at the time of object creation
    public OpenVEPVCF(File vcfFilePath) {
        this.vcfFilePath = vcfFilePath;
    }


    public void openFiles()   { //(File vcfFile) {   //throws Exception{

        Log.log(Level.INFO, "Opening VEP VCF file");

        String line;

        try{
                BufferedReader reader = new BufferedReader(new FileReader(vcfFilePath)); //Changed from vcfFile
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    System.out.println("printing lines");
                }

        }catch(Exception e) { //(IOException e) {

                Log.log(Level.SEVERE, "Could not read VCF file: " + e.getMessage());
        }
        }
    }
