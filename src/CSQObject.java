import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sara on 16-Nov-16.
 */

public class CSQObject {

   private String vepHead;
   private ArrayList vepAnn;

    public CSQObject(String vh, ArrayList va){
        this.vepHead = vh;
        this.vepAnn = va;
    }

    public void tester(){

        ArrayList<String> vepEntries = new ArrayList<>();
        HashMap<String, String> vepHashMap = new HashMap<String, String>();

        for (Object splitEntries: vepAnn) {
            //System.out.println(splitEntries);
            String[] annotations = splitEntries.toString().split("\\|");
            String[] headers = vepHead.split("\\|");
            for (String element: annotations) {
                vepEntries.add(element);

                //System.out.println(element);
                //if (element.length() < 1){
                    //vepEntries.add(" ");
                    //System.out.println("Triggered");
                //}else{
                    //vepEntries.add(element);
                //}
            }
            ////System.out.println(t[0]);
            ////System.out.println(u[0]);
            //System.out.print("\n");
            //System.out.println(vepEntries);
            //System.out.println(vepEntries.get(0));
            //System.out.println(vepEntries.size());
            //System.out.println(u.length);

            for (int i=0 ; i < annotations.length; i++) {
                //System.out.println(headers[i]);
                //System.out.println(vepEntries.get(i));
                vepHashMap.put(headers[i],vepEntries.get(i));
                //System.out.println(vepHashMap);
            }
        System.out.println(vepHashMap);
        }
    }


}
