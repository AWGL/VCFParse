import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import htsjdk.variant.variantcontext.VariantContext;

import java.util.*;

import com.google.common.collect.Multimap;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableList;

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
        //System.out.println(vepInfo.getDescription().split("Format:")[1]);

        String vepHeader = vepInfo.getDescription().split("Format:")[1].trim();
        //String vepHeader = vepInfo.getDescription().split("\\.")[1].split(":")[1].trim();
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
        createCsqRecordOfVepAnnObjectsTEST(String variantHeaders, ArrayList<String> csqRec) {

        ArrayList<VepAnnotationObject> csqArray = new ArrayList<VepAnnotationObject>();
        ListMultimap<Integer,VepAnnotationObject> csqHashMap = ArrayListMultimap.create();

        //HashMap<Integer,VepAnnotationObject> csqMap = new HashMap<Integer,VepAnnotationObject>();

        //Identify a useful unique identifier for each transcript- OUTSTANDING- currently using 'record number'

        //System.out.println(csqRec.split("\\,"));
        //System.out.println(csqRec);
        //System.out.println(csqRec.getClass());
        //System.out.println(csqRec.replaceAll("^\\["," ").replaceAll("\\]$",""));
        //String csqRecStripped = csqRec.replaceAll("^\\["," ").replaceAll("\\]$","");
        //System.out.println(csqRecStripped.split("\\,"));
        //String[] csqRecStripSplit = csqRecStripped.split("\\,");

        //String[] csqRecSplit = csqRec.split("\\,");

        //for (String entry : csqRec){
            //System.out.println(entry);
        //}


        //String[] csqStr = (csqRec.replaceAll("^\\["," ").replaceAll("\\]$","n ").split("\\,")); //THIS IS WHERE THE ISSUE WITH THE EXTRA [[]] AND THE MISSING ENTRIES IS CAUSED
        //System.out.println(csqRec.replaceAll("^\\["," ").replaceAll("\\]$","n ").split("\\,")); //TESTING HERE

        ///*

        //Is retrieved as object array containing a string array
        //Object[] objects = (Object[]) csqRec;
        //String[] csqReci = (String[])objects[0];

        //if(csqRec instanceof Object){System.out.println("object");}

        //for (Object t : csqRec ){
            //System.out.println(t);
        //}




        for (int i = 0; i < csqRec.size(); i++){
            //System.out.println((i+1)); // +1 so that entries start at 1 instead of 0
            //System.out.println(csqRecord.get(i));

            //CsqUtilities csq = new CsqUtilities();
            VepAnnotationObject currentVepAnnotationObject =
                    createVepAnnotationObject(variantHeaders, csqRec.get(i)); //Returned from function below


            //Create ArrayList of VepAnnotationObjects
            csqArray.add(currentVepAnnotationObject);

            //System.out.println(currentVepAnnotationObject.getAlleleNum());

            //See if it is working
            //System.out.println(csqMap);

            //Retrieve allele num ????????
            //System.out.println();

        }
        //*/

        //Remove duplicates from the array before populating the hashmap
        //System.out.println(csqArray);

        //the set doesn't need to preserve the order of the elements in the ArrayList as the association with the
        //allele num is retrieved from within the set- for comparison purposes it would be useful if it did however
        /*
        Set<VepAnnotationObject> csqSet =
                new HashSet<VepAnnotationObject>(csqArray);
        System.out.println(csqSet);
        */

        ImmutableSet<VepAnnotationObject> csqImmutableSet = ImmutableSet.copyOf(csqArray);
        //System.out.println(csqImmutableSet);

        ImmutableList<VepAnnotationObject> csqImmutableList = ImmutableList.copyOf(csqImmutableSet);
        //System.out.println(csqImmutableList);


        csqHashMap = createCsqRecord(csqImmutableList);

        //Return the hash map
        //System.out.println(csqHashMap);
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
        LinkedHashMap<String, String> vepHashMap = new LinkedHashMap<String, String>();

        String[] annotations = vepAnnotation.split("\\|", -1); //-1 stops this removing empty trailing spaces
        String[] headers = variantHeaders.split("\\|");

        for (int i=0 ; i < annotations.length; i++) {
            //System.out.println(headers[i]);
            //System.out.println(vepEntries.get(i));
            //Generate HashMap on the fly
            vepHashMap.put(headers[i],annotations[i]);
            //System.out.println(vepHashMap);
        }

        //Create a variant annotation object to hold the k,v pairs for each vep annotation for each csq entry
        VepAnnotationObject currentVepAnnotationObject = new VepAnnotationObject();
        //Populate the object with the hash map
        //headers to data hashmap retrieved
        currentVepAnnotationObject.setVepRecord(vepHashMap);

        //VepAnnotationObject tr = currentVepAnnotationObject.setVepRecord(currentVepAnnotation.vepAnnotationRecord());

        //Testing the object is a VepAnnotationObject going in to the csq hashmap
        //System.out.println("Ann obj is " + currentVepAnnotationObject);
        //System.out.println(currentVepAnnotationObject.getVepRecord()); //test using VepAnnotationObject methods

        //System.out.println(currentVepAnnotationObject.getAlleleNum()); //Identify the allele (09/12/16)

        return currentVepAnnotationObject; //Change this potentially
    }

    public ListMultimap<Integer,VepAnnotationObject> createCsqRecord(ImmutableList<VepAnnotationObject> vepAnnList){
        ListMultimap<Integer,VepAnnotationObject> csqMap = ArrayListMultimap.create(); //HashMultimap.create()

        //Iterator<VepAnnotationObject> vepAnnIter = vepAnnSet.iterator();

        //while (vepAnnIter.hasNext()){

         //System.out.println(vepAnnSet);
         //System.out.println(vepAnnIter);
        for (int i=0; i<vepAnnList.size(); i++){

            csqMap.put(Integer.parseInt(vepAnnList.get(i).getAlleleNum()),vepAnnList.get(i));
        }
        return csqMap;
    }


}
