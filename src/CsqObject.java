import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.common.collect.Multimap;
import com.google.common.collect.ListMultimap;

/**
 * Created by Sara on 17-Nov-16.
 */

class CsqObject {

    //This class is designed to hold the HashMap of all CSQ records held for each variant
    //Inside the HashMap is a VepAnnotation object


    private ListMultimap<Integer, VepAnnotationObject> csqHashMap;
    //private Set<Integer> csqID = this.csqHashMap.keySet();
    //private Collection<VepAnnotationObject> vepAnn = this.csqHashMap.values();

    //public CsqObject() {    }

    public void setCsqObject(ListMultimap<Integer, VepAnnotationObject> csqHashMap){
        this.csqHashMap = csqHashMap;
        //this.csqHashMap = csqer;
    }

    public ListMultimap getCsqObject(){
        return this.csqHashMap;
    }

    public List<VepAnnotationObject> getSpecificCsqObject(int alleleNum) { return this.csqHashMap.get(alleleNum); }

    //public VepAnnotationObject getTest(int alleleNum){ return this.csqHashMap.get(alleleNum)[0]; }

    //public Collection<String> getVepAnnObjects(int alleleNum) { return this.csqHashMap.getVepRecord; } //view what is inside object

    /*
    public Vep getVepAnnObjects() {
        for (vepAnn :this.csqHashMap){

        }
    }
*/



    //public CsqObject getCsqObjectTest(){
        //return this.csqHashMap;
    //}  //This doesn't work


    /*

    public VepAnnotationObject getSpecificCsqObject(int k){

        return this.csqHashMap.get(k);
    }

    public Collection<VepAnnotationObject> getEntireCsqObject(){
        return this.csqHashMap.values();
    }

    public Collection<String> getCsqObjectVepAnnotationValues(int k){
        return this.csqHashMap.get(k).getEntireVepRecordValues();
    }

    public VepAnnotationObject getVepAnn(Integer key){
            return this.csqHashMap.get(key);
    }

    public HashMap getCsqObject3(int k){
        return this.csqHashMap.get(k).getVepRecord(); //Testing
    }

    */


}

