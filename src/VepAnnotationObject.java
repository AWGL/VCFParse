import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Sara on 16-Nov-16.
 */

public class VepAnnotationObject {

    //This class is designed to hold the HashMap of each VEP annotation record held in each separate CSQ field

    private HashMap<String, String> vepAnnotationHashMap;

    public VepAnnotationObject() {}

    public void setVepRecord(HashMap<String,String> vepAnnotationHashMap) {
        this.vepAnnotationHashMap = vepAnnotationHashMap;
    }

    public HashMap getVepRecord(){
        //Returns entire vep record entry as a hash map
        return this.vepAnnotationHashMap;
    }

    public Collection<String> getEntireVepRecord() {
        return this.vepAnnotationHashMap.values();

        }

}








    /*
    public void tester(){

        ArrayList<String> vepEntries = new ArrayList<>();
        HashMap<String, String> vepHashMap = new HashMap<String, String>();

        for (Object splitEntries: vepAnn) {
            //Iterate over the annotation array containing the different entries for each transcript/effect etc.
            //System.out.println(vepHead);
            System.out.println(splitEntries);
            //Identify a useful unique identifier for each transcript

            String[] annotations = splitEntries.toString().split("\\|");
            String[] headers = vepHead.split("\\|");
            for (String element: annotations) {
                //Obtain each element of the CSQ entry and append to an array
                vepEntries.add(element);
            }
            for (int i=0 ; i < annotations.length; i++) {
                //System.out.println(headers[i]);
                //System.out.println(vepEntries.get(i));
                //Generate HashMap on the fly
                vepHashMap.put(headers[i],vepEntries.get(i));
                //System.out.println(vepHashMap);
            }
        //Test output
        // System.out.println(vepHashMap);
        //System.out.println(vepHashMap.get("Allele"));
        //System.out.println(vepHashMap.get("Feature"));
        }
    }

}
*/
