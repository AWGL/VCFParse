import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sara on 17-Nov-16.
 */
public class VepAnnotation {

    private String variantHeaders;
    private String variantAnnPerCSQ;

    public VepAnnotation(String variantHeaders, String variantAnnPerCSQ){
        this.variantHeaders = variantHeaders;
        this.variantAnnPerCSQ = variantAnnPerCSQ;
    }

    public void vepAnnotationRecord(){
        ArrayList<String> vepEntries = new ArrayList<>();
        HashMap<String, String> vepHashMap = new HashMap<String, String>();

        String[] annotations = variantAnnPerCSQ.split("\\|");
        String[] headers = variantHeaders.split("\\|");
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
        System.out.println(vepHashMap);
        //System.out.println(vepHashMap.get("Allele"));
        //System.out.println(vepHashMap.get("Feature"));
    }

}

