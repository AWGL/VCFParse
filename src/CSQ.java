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

    //private VCFFileReader vcfFile;

    //public CSQ()    { this.vcfFile = vcfFile; }

    public String vepHeaders(VCFFileReader vcfFile)   {
        //Create the VCF Header object
        VCFHeader currentHeader = vcfFile.getFileHeader();
        //System.out.println(currentHeader);
        VCFInfoHeaderLine vepInfo = currentHeader.getInfoHeaderLine("CSQ");
        //System.out.println(vepInfo);
        String vepHeader = vepInfo.getDescription().split("\\.")[1].split(":")[1].trim();
        //System.out.println(vepHeader); //prints the header
        return vepHeader; //returns the header
    }

    public ArrayList<String> vepAnnotations(VariantContext vc){

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


    public HashMap<Integer,VepAnnotationObject> CSQRecord(String variantHeaders, ArrayList<String> csqRecord) {

        //ArrayList<Integer,CSQObject> csqArray = new ArrayList<Integer,CSQObject>();
        HashMap<Integer,VepAnnotationObject> csqMap = new HashMap<Integer,VepAnnotationObject>();

            //Identify a useful unique identifier for each transcript- OUTSTANDING- currently using 'record number'

        for (int i = 0; i < csqRecord.size(); i++){
            //System.out.println((i+1)); // +1 so that entries start at 1 instead of 0
            //System.out.println(csqRecord.get(i));

            //Creating a HashMap of objects
            //Create vepAnnotation object- this is where we decide which CSQ to retrieve the VEP annotations for
            VepAnnotation currentVepAnnotation = new VepAnnotation(variantHeaders, csqRecord.get(i));
            //Check that it works now it has been split out- Working- comment out later
            //System.out.println(currentVepAnnotationObject.vepAnnotationRecord());

            //Create a variant annotation object
            VepAnnotationObject currentVepAnnotationObject = new VepAnnotationObject(currentVepAnnotation.vepAnnotationRecord());

            //Create hashmap of objects
            csqMap.put((i+1),currentVepAnnotationObject); //In here put the key and the value pair- see the model class (CSQObject)

            //See if it is working
            //System.out.println(csqMap);
        }
        //Return the hash map
        return csqMap;
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
