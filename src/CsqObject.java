import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

import com.google.common.collect.Multimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;

/**
 * Created by Sara on 17-Nov-16.
 */

class CsqObject {

    //This class is designed to hold the HashMap of all CSQ records held for each variant
    //Inside the HashMap is a VepAnnotation object

    private ArrayListMultimap<Integer, VepAnnotationObject> csqHashMap;
    private Set<Integer> alleleNums; //All the unique keys in the CSQ object

    public CsqObject() {    }

    public void setCsqObject(ArrayListMultimap<Integer, VepAnnotationObject> csqHashMap){
        this.csqHashMap = csqHashMap;
        this.alleleNums = csqHashMap.keySet();
    }

    public ArrayListMultimap<Integer, VepAnnotationObject> getCsqObject(){
        return this.csqHashMap;
    }

    public ArrayList<VepAnnotationObject> getSpecificVepAnnObjects(int alleleNum) {
        ArrayList<VepAnnotationObject> vepAnns = new ArrayList<VepAnnotationObject>(this.csqHashMap.get(alleleNum));
        return vepAnns; }

    public Set<Integer> getAlleleNums() { return this.alleleNums; }

}

