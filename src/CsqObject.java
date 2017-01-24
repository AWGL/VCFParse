package nhs.genetics.cardiff;

import java.util.Set;
import java.util.ArrayList;

import com.google.common.collect.ArrayListMultimap;

/**
 * Class is designed to hold the HashMap of all CSQ records held for each variant inside the HashMap is a VepAnnotation object
 *
 * @author  Sara Rey
 * @since   2016-11-17
 */

class CsqObject {

    private ArrayListMultimap<Integer, VepAnnotationObject> csqHashMap;
    private Set<Integer> alleleNums; //All the unique keys in the CSQ object

    public CsqObject(ArrayListMultimap<Integer, VepAnnotationObject> csqHashMap){
        this.csqHashMap = csqHashMap;
        this.alleleNums = csqHashMap.keySet();
    }

    public ArrayListMultimap<Integer, VepAnnotationObject> getCsqObject(){
        return this.csqHashMap;
    }
    public ArrayList<VepAnnotationObject> getSpecificVepAnnObjects(int alleleNum) {
        return new ArrayList<VepAnnotationObject>(this.csqHashMap.get(alleleNum));
    }
    public Set<Integer> getAlleleNums() { return this.alleleNums; }

}

