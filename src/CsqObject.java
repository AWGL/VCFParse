import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Sara on 17-Nov-16.
 */

class CsqObject {

    //This class is designed to hold the HashMap of all CSQ records held for each variant
    //Inside the HashMap is a VepAnnotation object


    private HashMap<Integer, VepAnnotationObject> csqHashMap;

    //public CsqObject() {}

    public void setCsqObject(HashMap<Integer,VepAnnotationObject> csqHashMap){
        this.csqHashMap = csqHashMap;
    }

    public HashMap getCsqObject(){
        return this.csqHashMap;
    }

    //public CsqObject getCsqObject(){
        //return this.csqHashMap;
    //}  //This doesn't work

    public VepAnnotationObject getCsqObject2(int k){
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


}

