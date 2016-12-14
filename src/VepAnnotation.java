import java.util.ArrayList;
import java.util.LinkedHashMap; //Replaced hash map as was easier for debugging (SR 21/11/2016)

/**
 * Created by Sara on 17-Nov-16.
 */

class VepAnnotation {

    private String variantHeaders;
    private String variantAnnPerCSQ;

    public VepAnnotation(){}

    public void setVepAnnotation(String variantHeaders, String variantAnnPerCSQ){
        this.variantHeaders = variantHeaders;
        this.variantAnnPerCSQ = variantAnnPerCSQ;
    }

    public LinkedHashMap<String, String> vepAnnotationRecord(){

        ArrayList<String> vepEntries = new ArrayList<>();
        LinkedHashMap<String, String> vepHashMap = new LinkedHashMap<String, String>();

        String[] annotations = variantAnnPerCSQ.split("\\|", -1);
        String[] headers = variantHeaders.split("\\|");

        System.out.println(variantAnnPerCSQ);
        System.out.println(headers.length);
        System.out.println(annotations.length);

        //This will store empty entries where the CSQ does not contain any data for that header (headers.length)
        for (int i=0 ; i < annotations.length; i++) {
            //System.out.println(headers[i]);
            //System.out.println(vepEntries.get(i));
            //Generate HashMap on the fly
            vepHashMap.put(headers[i],annotations[i]);
            //System.out.println(vepHashMap);
        }
        //Test output
        //System.out.println(vepHashMap);
        return vepHashMap;
        //System.out.println(vepHashMap.get("Allele"));
        //System.out.println(vepHashMap.get("Feature"));
    }

}

