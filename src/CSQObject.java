/**
 * Created by Sara on 16-Nov-16.
 */
public class CSQObject {

   private String vepHead;
   private String vepAnn;

    public CSQObject(String vh, String va){
        this.vepHead = vh;
        this.vepAnn = va;
    }

    public void tester(){
        //for (String splitEntries: vepAnn.split("\\|")) { //Need to escape the string because it's regex
            //System.out.print(splitEntries);
        String[] t = vepAnn.split("\\|");
        String[] u = vepHead.split("\\|");
        for (int i=0 ; i < t.length; i++){
            System.out.println(u[i]);
            System.out.println(t[i]);
        }
    }


}
