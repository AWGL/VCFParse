import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Sara on 16-Nov-16.
 */

public class VepAnnotationObject {

    //This class is designed to hold the HashMap of each VEP annotation record held in each separate CSQ field

    private LinkedHashMap<String, String> vepAnnotationHashMap;

    public VepAnnotationObject() {}

    public void setVepRecord(LinkedHashMap<String,String> vepAnnotationHashMap) {
        this.vepAnnotationHashMap = vepAnnotationHashMap;
    }

    public LinkedHashMap getVepRecord(){
        //Returns entire vep record entry as a hash map
        return this.vepAnnotationHashMap;
    }

    public Collection<String> getEntireVepRecordValues() {
        return this.vepAnnotationHashMap.values();

        }

    public String getAlleleNum(){
        return this.vepAnnotationHashMap.get("ALLELE_NUM");
    }

    public void getVepAnnotationObjectFromAlleleNum(String inp){ //Change from void to correct output
        //Get the allele number


        //Return a collection of every Vep Annotation Object where the allele number matches the input string



        //If it matches the input inp then append it to some sort of appropriate collection
        //Note that here will want to replace whatever number CSQ we are on with a new number reflecting only the
        //CSQs for that allele



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
