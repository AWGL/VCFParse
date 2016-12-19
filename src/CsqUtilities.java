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


    public ArrayListMultimap<Integer,VepAnnotationObject>
        createCsqRecordOfVepAnnObjects(String variantHeaders, ArrayList<String> csqRec) {

        ArrayList<VepAnnotationObject> csqArray = new ArrayList<VepAnnotationObject>();
        ArrayListMultimap<Integer,VepAnnotationObject> csqHashMap = ArrayListMultimap.create();


        for (int i = 0; i < csqRec.size(); i++){

            VepAnnotationObject currentVepAnnotationObject =
                    createVepAnnotationObject(variantHeaders, csqRec.get(i)); //Returned from function below


            //Create ArrayList of VepAnnotationObjects
            csqArray.add(currentVepAnnotationObject);

        }

        //Remove duplicates from the array before populating the hashmap
        //the set doesn't need to preserve the order of the elements in the ArrayList as the association with the
        //allele num is retrieved from within the set

        ImmutableSet<VepAnnotationObject> csqImmutableSet = ImmutableSet.copyOf(csqArray);
        //System.out.println(csqImmutableSet);

        ArrayList<VepAnnotationObject> csqList = new ArrayList<VepAnnotationObject>(csqImmutableSet);
        //System.out.println(csqImmutableList);

        //ArrayList<VepAnnotationObject> csqArrayList = new ArrayList<VepAnnotationObject>();

        csqHashMap = createCsqRecord(csqList);

        //Return the hash map
        //System.out.println(csqHashMap);
        return csqHashMap; //Could return is a call to another function
    }



    public VepAnnotationObject createVepAnnotationObject(String variantHeaders, String vepAnnotation){
        //Creating a HashMap of objects
        //Create vepAnnotation object- this is where we decide which CSQ to retrieve the VEP annotations for
        LinkedHashMap<String, String> vepHashMap = new LinkedHashMap<String, String>();

        String[] annotations = vepAnnotation.split("\\|", -1); //-1 stops this removing empty trailing spaces
        String[] headers = variantHeaders.split("\\|");

        for (int i=0 ; i < annotations.length; i++) {
            vepHashMap.put(headers[i],annotations[i]);
            //System.out.println(vepHashMap);
        }

        //Create a variant annotation object to hold the k,v pairs for each vep annotation for each csq entry
        VepAnnotationObject currentVepAnnotationObject = new VepAnnotationObject();
        //Populate the object with the hash map
        //headers to data hashmap retrieved
        currentVepAnnotationObject.setVepRecord(vepHashMap);

        return currentVepAnnotationObject; //Change this potentially
    }

    public ArrayListMultimap<Integer,VepAnnotationObject> createCsqRecord(ArrayList<VepAnnotationObject> vepAnnList){
        ArrayListMultimap<Integer,VepAnnotationObject> csqMap = ArrayListMultimap.create(); //HashMultimap.create()

        for (int i=0; i<vepAnnList.size(); i++){

            //If fails here no allele number
            csqMap.put(Integer.parseInt(vepAnnList.get(i).getAlleleNum()),vepAnnList.get(i));
        }
        return csqMap;
    }


}
