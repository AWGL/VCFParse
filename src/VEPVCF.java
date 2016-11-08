import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import htsjdk.samtools.*;
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
    public void openFiles()   { 

        Log.log(Level.INFO, "Opening VEP VCF file");

        String line;

        try{
            BufferedReader reader = new BufferedReader(new FileReader(vcfFilePath));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        }catch(Exception e) { //(IOException e) {

            Log.log(Level.SEVERE, "Could not read VCF file: " + e.getMessage());
        }
    }
}
