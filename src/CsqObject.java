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
    //private Set<Integer> csqID = this.csqHashMap.keySet();
    //private Collection<VepAnnotationObject> vepAnn = this.csqHashMap.values();

    //public CsqObject() {    }

    public void setCsqObject(ArrayListMultimap<Integer, VepAnnotationObject> csqHashMap){
        this.csqHashMap = csqHashMap;
        //this.csqHashMap = csqer;
    }

    public ArrayListMultimap<Integer, VepAnnotationObject> getCsqObject(){
        return this.csqHashMap;
    }

    //public CsqObject getSpecificCsqObject(int alleleNum) {return this.csqHashMap.get(alleleNum); }

    public  Collection<String> getVepAnnotationHeaders() {return this.csqHashMap.get(0).get(0).getVepHeaders();}

    //public ArrayList<VepAnnotationObject> getSpecificVepAnnObjects(int alleleNum) { return (ArrayList)(this.csqHashMap.get(alleleNum)); }
    public ArrayList<VepAnnotationObject> getSpecificVepAnnObjects(int alleleNum) {
        ArrayList<VepAnnotationObject> vepAnns = new ArrayList<VepAnnotationObject>(this.csqHashMap.get(alleleNum));
        return vepAnns; }

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

