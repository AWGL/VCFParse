import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFInfoHeaderLine;

import java.io.*;

/**
 * Created by Sara on 15-Nov-16.
 */

public class CSQ {

    private File vcfFilePath;

    public CSQ(File vcfFile)    { this.vcfFilePath = vcfFile; }

    public void vepHeaders()   {
        //Read in the file
        VCFFileReader vcfFile = new VCFFileReader(vcfFilePath, false); //False to not require index

        //Create the VCF Header object
        VCFHeader currentHeader = vcfFile.getFileHeader(); //This is very messy in light of opening the file below as well- FIX

        VCFInfoHeaderLine vepInfo = currentHeader.getInfoHeaderLine("CSQ");
        String vepHeader = vepInfo.getDescription().split("\\.")[1].split(":")[1].trim();
        System.out.println(vepHeader);



    }

    public String getVepHeaders(){return vcfFilePath.toString();} //Fix this line to return what is required

}
