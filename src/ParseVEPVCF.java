import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Sara on 07-Nov-16.
 */
public class ParseVEPVCF {

    private static final Logger Log = Logger.getLogger(OpenVEPVCF.class.getName());

    private File vcfFilePath;

    public ParseVEPVCF(File vcfFilePath) {
        this.vcfFilePath = vcfFilePath;
    }


    public void parseFile() {

        Log.log(Level.INFO, "Parsing VEP VCF file");

        String line;

        //try (BufferedReader reader = new BufferedReader(new FileReader(vcfFilePath))) {
            //while ((line = reader.readLine()) != null) {
                //System.out.println("line");
            //}
        //}
    }

}
