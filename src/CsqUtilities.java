import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import htsjdk.variant.variantcontext.VariantContext;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sara on 15-Nov-16.
 */

public class CsqUtilities {

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
        String csq =
                vc.getAttributeAsString("CSQ", "null").replaceAll("^\\[","").replaceAll("\\]$",""); //Could just pass this in instead of the VariantContext object
        //System.out.println(CSQ);
        //System.out.println(vc.getAttribute("CSQ"));

        for (String splitVal : csq.split("\\,")) { //Splits multiple records per entry
            //Remove leading whitespace
            //System.out.println(splitVal.trim()); //Prints out the individual records
            entries.add(splitVal.trim());
        }
        //System.out.println("Entries are " + entries);
        return entries; //Return the Array
    }


    public HashMap<Integer,VepAnnotationObject> csqRecord(String variantHeaders, ArrayList<String> csqRec) {

        //ArrayList<Integer,CSQObject> csqArray = new ArrayList<Integer,CSQObject>();
        HashMap<Integer,VepAnnotationObject> csqMap = new HashMap<Integer,VepAnnotationObject>();

            //Identify a useful unique identifier for each transcript- OUTSTANDING- currently using 'record number'

        for (int i = 0; i < csqRec.size(); i++){
            //System.out.println((i+1)); // +1 so that entries start at 1 instead of 0
            //System.out.println(csqRecord.get(i));

            CsqUtilities csq = new CsqUtilities();
            VepAnnotationObject currentVepAnnotationObject =
                    csq.createVepAnnotationObject(variantHeaders, csqRec.get(i));

            //Create hashmap of objects
            csqMap.put((i+1),currentVepAnnotationObject); //In here put the key and the value pair- see the model class (CSQObject)

            //See if it is working
            //System.out.println(csqMap);

            //Retrieve allele num ????????
            //System.out.println();

        }
        //Return the hash map
        return csqMap;
    }

    public VepAnnotationObject createVepAnnotationObject(String variantHeaders, String vepAnnotation){
        //Creating a HashMap of objects
        //Create vepAnnotation object- this is where we decide which CSQ to retrieve the VEP annotations for
        VepAnnotation currentVepAnnotation = new VepAnnotation();
        currentVepAnnotation.setVepAnnotation(variantHeaders, vepAnnotation); //Data in to the VepAnnotation as Strings

        //Check that it works now it has been split out- Working- comment out later
        //System.out.println(currentVepAnnotation.vepAnnotationRecord());

        //Create a variant annotation object to hold the k,v pairs for each vep annotation for each csq entry
        VepAnnotationObject currentVepAnnotationObject = new VepAnnotationObject();
        //Populate the object with the hash map
        //headers to data hashmap retrieved
        currentVepAnnotationObject.setVepRecord(currentVepAnnotation.vepAnnotationRecord());

        //VepAnnotationObject tr = currentVepAnnotationObject.setVepRecord(currentVepAnnotation.vepAnnotationRecord());

        //Testing the object is a VepAnnotationObject going in to the csq hashmap
        //System.out.println("Ann obj is " + currentVepAnnotationObject);
        //System.out.println(currentVepAnnotationObject.getVepRecord()); //test using VepAnnotationObject methods

        return currentVepAnnotationObject; //Change this
    }

    public HashMap<Integer,VepAnnotationObject> createCsqRecord(ArrayList<VepAnnotationObject> vepAnnColl){
        HashMap<Integer,VepAnnotationObject> csqMap = new HashMap<Integer,VepAnnotationObject>();

        for (int i=0; i<vepAnnColl.size(); i++){
            csqMap.put((i+1),vepAnnColl.get(i));
        }
        return csqMap;
    }

    public String parseCsq(VariantContext vc) {


        return "test";

    }

}
