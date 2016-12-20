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

            //Retrieve id field which contains dbSNP identifier (if applicable)
            VariantDataObject variantDataObject = variantHashMap.get(forVariantRetrieval);

            //writer.write("test");
            for (VepAnnotationObject vepAnnObj : variantDataObject.getCsqObject()) {

                //Create key array for robust lookup ordering to ensure that there are no issues if order changes
                List<String> keyArray = new ArrayList<String>();

                //Write headers
                writer.write("SampleID");
                writer.write("\t");
                writer.write("Chromosome");
                writer.write("\t");
                writer.write("Variant");
                writer.write("\t");
                writer.write("AlleleFrequency");
                writer.write("\t");
                writer.write("Quality");

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

                //Sample variant as g.posref>alt
                //System.out.println("g." + (sampleVariantHashMapKey.split(",")[1].split(":")[1]) +
                    //(sampleVariantHashMapKey.split(",")[2]));

                //Minimise alleles? GenomeVariant class
                GenomeVariant genomeVariant = new GenomeVariant((sampleVariantHashMapKey.split(",")[1]) +
                        (sampleVariantHashMapKey.split(",")[2]));
                //genomeVariant.convertToMinimalRepresentation();
                //System.out.println(genomeVariant);

                writer.write("g." + (genomeVariant));
                writer.write("\t");

                //Sample allele frequency
                //Truncate output
                writer.write(String.format("%.2f", sampleVariantHashMap.get(sampleVariantHashMapKey).getAlleleFrequency()));
                writer.write("\t");

                //Sample quality
                writer.write(Integer.toString(sampleVariantHashMap.get(sampleVariantHashMapKey).getGenotypeQuality()));
                writer.write("\t");

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

