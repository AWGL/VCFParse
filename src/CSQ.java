import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import htsjdk.variant.variantcontext.VariantContext;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sara on 15-Nov-16.
 */

public class CSQ {

    private VCFFileReader vcfFile;

    public CSQ(VCFFileReader vcfFile)    { this.vcfFile = vcfFile; }

    public String vepHeaders()   {
        //Create the VCF Header object
        VCFHeader currentHeader = vcfFile.getFileHeader();
        //System.out.println(currentHeader);
        VCFInfoHeaderLine vepInfo = currentHeader.getInfoHeaderLine("CSQ");
        //System.out.println(vepInfo);
        String vepHeader = vepInfo.getDescription().split("\\.")[1].split(":")[1].trim();
        //System.out.println(vepHeader); //prints the header
        return vepHeader; //returns the header
    }

    public ArrayList vepAnnotations(VariantContext vc){

        ArrayList<String> entries = new ArrayList<>(); //Note: Should this be new ArrayList<String>?

        //Access the data in the CSQ field of the INFO field- per record
        //Obtain the CSQ field and remove the square brackets
        String CSQ = vc.getAttributeAsString("CSQ", "null").replaceAll("^\\[","").replaceAll("\\]$",""); //Could just pass this in instead of the VariantContext object
        //System.out.println(CSQ);
        //System.out.println(vc.getAttribute("CSQ"));

        for (String splitVal : CSQ.split("\\,")) { //Splits multiple records per entry
            //System.out.println(splitVal); //Prints out the individual records
            //Append these to some sort of array
            entries.add(splitVal);
        }
        return entries; //Return the Array
    }


    public void CSQRecord(String variantHeaders, ArrayList<String> csqRecord) {

        //ArrayList<Integer,CSQObject> csqArray = new ArrayList<Integer,CSQObject>();
        HashMap<Integer,CSQObject> csqMap = new HashMap<Integer,CSQObject>();

        for (String splitEntries : csqRecord) {
            //Iterate over the annotation array containing the different entries for each transcript/effect etc.
            //System.out.println(vepHead);
            //System.out.println(splitEntries);
            //Identify a useful unique identifier for each transcript

            //Check that it works now it has been split out- Working
            VepAnnotation t = new VepAnnotation(variantHeaders, splitEntries);
            t.vepAnnotationRecord();

            //Try creating a HashMap of objects
            csqMap.put(); //In here put the key and the value pair- see the model class (CSQObject)



            //return splitEntries;
        }
    }

    /*
    public String vepHashMap(String vh, ArrayList va){
        //Declare HashMap
        HashMap<String, String> vepHashMap = new HashMap<String, String>();

        for (int i = 0; i < va.size(); i++ ){
            //System.out.println(vh);
            //System.out.println(va.get(i));

           //CSQObject t = new CSQObject(vh , va.get(i));
            //t.tester();
            System.out.print("\n");
        }
        System.out.print("\n");


        //Create hashmap
        vepHashMap.put("a", "b");
        //System.out.println(vepHashMap);

        //create the HashMap of header value pairs for each annotation

        return "Null";
    }
    */

    //public String getVepHeaders(){return vcfFile.toString();} //Fix this line to return what is required

}
