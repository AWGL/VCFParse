import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;

/**
 * Created by Sara on 08-Dec-16.
 */
public class WriteOut{

    private LinkedHashMap<String, SampleVariantDataObject> sampleVariantHashMap;
    private LinkedHashMap<String, VariantDataObject> variantHashMap;

    public WriteOut(LinkedHashMap<String, SampleVariantDataObject> sampleVariantHashMap,
                    LinkedHashMap<String, VariantDataObject> variantHashMap){
            this.sampleVariantHashMap = sampleVariantHashMap;
            this.variantHashMap = variantHashMap;
        }

    //Create logic to write out variables to a file (tsv)

    //Temp write out all vep annotations
    public void writeOutVepAnnotations() throws Exception{
    final File outputFile = new File("C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\OutputFiles\\VEP.txt");
    //outputFile.createNewFile();

    //Use bufferedwriter (syntax for Java 7 and above)- try automatically closes the stream
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(outputFile), "utf-8"))){
        //writer.write("bibble");

        //System.out.println(arr.size());


        for (String sampleVariantHashMapKey : sampleVariantHashMap.keySet()) {
            String[] splitKey = sampleVariantHashMapKey.split(",");
            String forVariantRetrieval = splitKey[1] + splitKey[2];

            //writer.write("test");
            for (VepAnnotationObject vepAnnObj : variantHashMap.get(forVariantRetrieval).getCsqObject()) {

                //Create key array for robust lookup ordering to ensure that there are no issues if order changes
                List<String> keyArray = new ArrayList<String>();

                //Write headers
                writer.write("SampleID");
                writer.write("\t");
                writer.write("Chromosome");
                writer.write("\t");
                writer.write("Position");

                for (String key : vepAnnObj.getVepAnnotationHashMap().keySet()) {
                    keyArray.add(key);
                    writer.write("\t");
                    writer.write(key);
                }

                writer.newLine();

                //Sample name
                writer.write(sampleVariantHashMapKey.split(",")[0]);
                writer.write("\t");

                //Sample chromosome
                writer.write(sampleVariantHashMapKey.split(",")[1].split(":")[0]);
                writer.write("\t");

                //Sample position
                writer.write(sampleVariantHashMapKey.split(",")[1].split(":")[1]);
                writer.write("\t");

                //writer.newLine();
                for (String key : keyArray){
                    writer.write(vepAnnObj.getVepEntry(key));
                    writer.write("\t");
                }

                writer.newLine();
            }

        }
        }
    }
}

