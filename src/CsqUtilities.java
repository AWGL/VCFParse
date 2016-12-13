import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import htsjdk.variant.variantcontext.VariantContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Collection;

import com.google.common.collect.Multimap;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

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
        VCFInfoHeaderLine vepInfo = currentHeader.getInfoHeaderLine("CSQ"); //This is null if no annotation has been performed
        //System.out.println(vepInfo);
        //System.out.println(vepInfo.getDescription());
        String vepHeader = vepInfo.getDescription().split("\\.")[1].split(":")[1].trim();
        //System.out.println(vepHeader); //prints the header
        return vepHeader; //returns the header
    }

    public ArrayList<String> vepAnnotations(VariantContext vc){

        ArrayList<String> entries = new ArrayList<String>(); //Note: Should this be new ArrayList<String>?

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


    public ListMultimap<Integer,VepAnnotationObject>
        createCsqRecordOfVepAnnObjectsTEST(String variantHeaders, String csqRec) {

        ArrayList<VepAnnotationObject> csqArray = new ArrayList<VepAnnotationObject>();

        ListMultimap<Integer,VepAnnotationObject> csqHashMap = ArrayListMultimap.create();

        //HashMap<Integer,VepAnnotationObject> csqMap = new HashMap<Integer,VepAnnotationObject>();

        //Identify a useful unique identifier for each transcript- OUTSTANDING- currently using 'record number'

        //System.out.println(csqRec.split("\\,"));
        String[] csqStr = (csqRec.split("\\,"));

        ///*
        for (int i = 0; i < csqStr.length; i++){
            //System.out.println((i+1)); // +1 so that entries start at 1 instead of 0
            //System.out.println(csqRecord.get(i));

            //CsqUtilities csq = new CsqUtilities();
            VepAnnotationObject currentVepAnnotationObject =
                    createVepAnnotationObject(variantHeaders, csqStr[i]); //Returned from function below


            //Create ArrayList of VepAnnotationObjects
            csqArray.add(currentVepAnnotationObject);

            //System.out.println(currentVepAnnotationObject.getAlleleNum());

            //See if it is working
            //System.out.println(csqMap);

            //Retrieve allele num ????????
            //System.out.println();

        }
        //*/

        csqHashMap = createCsqRecord(csqArray);

        //Return the hash map
        return csqHashMap; //Could return is a call to another function
    }






/*
    public LinkedHashMap<Integer,VepAnnotationObject>
        createCsqRecordOfVepAnnObjects(String variantHeaders, ArrayList<String> csqRec) {

        ArrayList<VepAnnotationObject> csqArray = new ArrayList<VepAnnotationObject>();
        //HashMap<Integer,VepAnnotationObject> csqMap = new HashMap<Integer,VepAnnotationObject>();

            //Identify a useful unique identifier for each transcript- OUTSTANDING- currently using 'record number'

        for (int i = 0; i < csqRec.size(); i++){
            //System.out.println((i+1)); // +1 so that entries start at 1 instead of 0
            //System.out.println(csqRecord.get(i));

            CsqUtilities csq = new CsqUtilities();
            VepAnnotationObject currentVepAnnotationObject =
                    csq.createVepAnnotationObject(variantHeaders, csqRec.get(i)); //Returned from function below

            */
            /*
            if (currentVepAnnotationObject.getAlleleNum().equals("1")) {

                System.out.println(currentVepAnnotationObject.getEntireVepRecordValues());
                System.out.println(currentVepAnnotationObject.getEntireVepRecordValues().hashCode());

                Collection<String> aL = currentVepAnnotationObject.getEntireVepRecordValues();

            }
            */

            /*
            //Create ArrayList of VepAnnotationObjects
            csqArray.add(currentVepAnnotationObject);

            //See if it is working
            //System.out.println(csqMap);

            //Retrieve allele num ????????
            //System.out.println();

        }
        //Return the hash map
        CsqUtilities csqUtil = new CsqUtilities();
        return csqUtil.createCsqRecord(csqArray); //Return is a call to another function. Could be changed if flexibility needed
    }

*/

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

        //System.out.println(currentVepAnnotationObject.getAlleleNum()); //Identify the allele (09/12/16)

        return currentVepAnnotationObject; //Change this potentially
    }

    public ListMultimap<Integer,VepAnnotationObject> createCsqRecord(ArrayList<VepAnnotationObject> vepAnnColl){
        ListMultimap<Integer,VepAnnotationObject> csqMap = ArrayListMultimap.create(); //HashMultimap.create()

        for (int i=0; i<vepAnnColl.size(); i++){

            csqMap.put(Integer.parseInt(vepAnnColl.get(i).getAlleleNum()),vepAnnColl.get(i));
        }
        return csqMap;
    }


}
