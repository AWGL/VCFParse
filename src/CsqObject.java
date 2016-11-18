import java.util.HashMap;

/**
 * Created by Sara on 17-Nov-16.
 */

class CsqObject {

    private HashMap<Integer, VepAnnotationObject> csqHashMap;

    public CsqObject() {}

    public void setCsqObject(HashMap<Integer,VepAnnotationObject> csqHashMap){
        this.csqHashMap = csqHashMap;
    }

    public HashMap getCsqObject(){
        return this.csqHashMap;
    }

}

