import java.util.ArrayList;

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
        for (Object splitEntries: vepAnn) {
            System.out.println(splitEntries);
            String[] t = splitEntries.toString().split("\\|"); //This method creates a problem because the || are nothing so the lengths are unequal
            String[] u = vepHead.split("\\|");
            System.out.println(t);
            //System.out.println(t[0]);
            //System.out.println(u[0]);
            System.out.println(t.length);
            System.out.println(u.length);

            for (int i=0 ; i < t.length; i++) {
                //System.out.println(u[i]);
                //System.out.println(t[i]);

            }
        }
    }


}
