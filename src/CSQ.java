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

    public String vepHeaders()   {
        //Read in the file- I want to move this so that the file is only opened once
        VCFFileReader vcfFile = new VCFFileReader(vcfFilePath, false); //False to not require index

        //Create the VCF Header object
        VCFHeader currentHeader = vcfFile.getFileHeader();

        VCFInfoHeaderLine vepInfo = currentHeader.getInfoHeaderLine("CSQ");
        String vepHeader = vepInfo.getDescription().split("\\.")[1].split(":")[1].trim();
        //System.out.println(vepHeader); //prints the header
        return vepHeader; //returns the header
    }

    public void vepAnnotations(){
        //Read in the file- I want to move this so that the file is only opened once
        VCFFileReader vcfFile = new VCFFileReader(vcfFilePath, false); //False to not require index

        //Access the data in the CSQ field of the INFO field- per record
        VCFHeader currentHeader = vcfFile.getFileHeader();




    }

    public String getVepHeaders(){return vcfFilePath.toString();} //Fix this line to return what is required

}
