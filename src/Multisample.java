import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * Created by Sara on 17-Jan-17.
 */


public class Multisample {
    private ArrayList<ArrayList<String>> sampleFullData;

    public Multisample(ArrayList<ArrayList<String>> sampleFullData){
        this.sampleFullData = sampleFullData;
    }

    public void splitSorted() {

        System.out.println(this.sampleFullData.get(0)); //First entry
        //writer.write(this.sampleFullData.get(0));

        for (int i = 0; i < (this.sampleFullData.size() - 1); i++) {
            //Compare sample names
            if (this.sampleFullData.get(i).get(0).equals(this.sampleFullData.get(i + 1).get(0))) {

                System.out.println(this.sampleFullData.get(i + 1));
                //writer.write(this.sampleFullData.get(i + 1));
                //writer.write("\t");

            }else{

                System.out.println();

                //Close file

            }
        }


    }

    public class writeMultisample{



    }

/*
    public void compLines() {
        Iterator<ArrayList<String>> lineIterData = this.sampleFullData.iterator();
        ArrayList<Integer> breakLine = new ArrayList<Integer>();
        lineIterData.next();

        int different = 0;

        while (lineIterData.hasNext()) {
            ArrayList<String> currentLine = lineIterData.next();

            if (currentLine != lineIterData.next()) {
                different += 1;
            }
            //System.out.println(lineIterData.next());
        }
        System.out.print(different);
    }
        //return breakLine; //base case
        //if (lineIterData.next() == null){
            //return breakLine;
        //} else {
            //System.out.println(lineIterData.next());
        //}


    //public String sortMultisample(){
        //Collections.sort(this.sampleName);
    //}
*/

}

