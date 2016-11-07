/**
 * Created by Sara on 07-Nov-16.
 */

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileReader;

public class OpenVEPVCF {

    private static final Logger Log = Logger.getLogger(OpenVEPVCF.class.getName());

    private File vcfFilePath;

    public OpenVEPVCF(File vcfFilePath) {
        this.vcfFilePath = vcfFilePath;
    }

    public void openFile()    {

        Log.log(Level.INFO, "Parsing GTF file");

        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(vcfFilePath))) {
            while ((line = reader.readLine()) != null) {
                System.out.println("line");
            }

        }
    }




}
