package nhs.genetics.cardiff;

import java.util.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;

/**
 * Class for extracting VCF genotype and VEP information using htsjdk
 *
 * @author  Sara Rey
 * @since   2016-11-15
 */

public class CsqUtilities {

    public static ArrayListMultimap<Integer,VepAnnotationObject> createCsqRecordOfVepAnnObjects(String variantHeaders, ArrayList<String> csqRec) {

        ArrayList<VepAnnotationObject> csqArray = new ArrayList<VepAnnotationObject>();

        for (int i = 0; i < csqRec.size(); i++){
            VepAnnotationObject currentVepAnnotationObject =
                    createVepAnnotationObject(variantHeaders, csqRec.get(i)); // Returned from function below
            //Create ArrayList of VepAnnotationObjects
            csqArray.add(currentVepAnnotationObject);
        }

        //Remove duplicates from the array before populating the hashmap
        //the set doesn't need to preserve the order of the elements in the ArrayList as the association with the
        //allele num is retrieved from within the set- this assumption should be tested

        ImmutableSet<VepAnnotationObject> csqImmutableSet = ImmutableSet.copyOf(csqArray);
        ArrayList<VepAnnotationObject> csqList = new ArrayList<VepAnnotationObject>(csqImmutableSet);
        ArrayListMultimap<Integer,VepAnnotationObject> csqHashMap = createCsqRecord(csqList);

        //Return the hash map
        //System.out.println(csqHashMap);
        return csqHashMap; //Could return is a call to another function
    }


    private static VepAnnotationObject createVepAnnotationObject(String variantHeaders, String vepAnnotation){

        //Creating a HashMap of objects
        LinkedHashMap<String, String> vepHashMap = new LinkedHashMap<String, String>();

        String[] annotations = vepAnnotation.split("\\|", -1); //-1 stops this removing empty trailing spaces
        String[] headers = variantHeaders.split("\\|");

        String currentCsqAllele = null;

        for (int i=0 ; i < annotations.length; i++) {
            //Identify the allele in the CSQ field
            if (headers[i].equals("Allele")){
                //System.out.print("Current allele is: ");
                //System.out.println(annotations[i]);
                currentCsqAllele = annotations[i];
                vepHashMap.put(headers[i],annotations[i]);
            // To handle cases where formatting in CSQ field is not as desired in output
            } else if (headers[i].equals("EXON") && !annotations[i].isEmpty() ||
                    headers[i].equals("INTRON") && !annotations[i].isEmpty()) {
                vepHashMap.put(headers[i], annotations[i].split("/")[0]);
            } else if (headers[i].equals("HGVSc") && !annotations[i].isEmpty() ||
                    headers[i].equals("HGVSp") && !annotations[i].isEmpty()) {
                vepHashMap.put(headers[i], annotations[i].split(":")[1]);
            } else if (headers[i].matches("^.*\\_MAF$") && !(annotations[i].isEmpty())){
                //Change formatting of MAF fields to remove leading character and store as double
                for (String alleles : annotations[i].split("&")) {
                    String mafAllele = alleles.split(":")[0];
                    if (mafAllele.equals(currentCsqAllele)) {
                        /*
                        Note that if it is required to change this to a double, the hashmap used will need to change
                        For database write, I recommend being more explicit anyway in the model- flexibility is
                        not an option when writing to a database
                        */
                        vepHashMap.put(headers[i], alleles.split(":")[1]);
                    }
                    //Handles case where there is a MAF entry but not for the current allele
                    else{
                        vepHashMap.put(headers[i],"");
                    }
                    //System.out.println(alleles.split(":")[1]);
                }
                //vepHashMap.put(headers[i],annotations[i]);
            }
            //Usual behaviour for the not-special cases
            else{vepHashMap.put(headers[i],annotations[i]);}
        }
        //System.out.println(vepHashMap);

        //Create a variant annotation object to hold the k,v pairs for each vep annotation for each csq entry
        VepAnnotationObject currentVepAnnotationObject = new VepAnnotationObject();
        //Populate the object with the hash map
        //headers to data hashmap retrieved
        currentVepAnnotationObject.setVepRecord(vepHashMap);

        return currentVepAnnotationObject;
    }

    private static ArrayListMultimap<Integer,VepAnnotationObject> createCsqRecord(ArrayList<VepAnnotationObject> vepAnnList){

        ArrayListMultimap<Integer,VepAnnotationObject> csqMap = ArrayListMultimap.create(); //HashMultimap.create()

        for (int i=0; i<vepAnnList.size(); i++){
            //If fails here no allele number
            csqMap.put(Integer.parseInt(vepAnnList.get(i).getAlleleNum()),vepAnnList.get(i));
        }
        return csqMap;
    }

}
