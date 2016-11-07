import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.io.FileReader;
/**
 * Created by Sara on 07-Nov-16.
 */


public class OpenVEPVCF {

    //Stuff that is wanted
    private static final Logger Log = Logger.getLogger(OpenVEPVCF.class.getName());

    private File vcfFilePath;
    private List<String> theRecords = new ArrayList<String>();

    public OpenVEPVCF(File vcfFilePath) {
        this.vcfFilePath = vcfFilePath;
    }


    public void openFiles() {

        Log.log(Level.INFO, "Opening VEP VCF file");

        String line;

        //try (BufferedReader reader = new BufferedReader(new FileReader(vcfFilePath))){

        //}
    }
}




}
