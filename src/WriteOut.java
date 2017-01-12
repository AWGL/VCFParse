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

    //Writes out desired fields to a tab-delimited text file

    private LinkedHashMap<String, SampleVariantDataObject> sampleVariantHashMap;
    private LinkedHashMap<String, VariantDataObject> variantHashMap;

    public WriteOut(LinkedHashMap<String, SampleVariantDataObject> sampleVariantHashMap,
                    LinkedHashMap<String, VariantDataObject> variantHashMap){
            this.sampleVariantHashMap = sampleVariantHashMap;
            this.variantHashMap = variantHashMap;
        }

    //Temp write out all vep annotations
    public void writeOutVepAnnotations() throws Exception{
        final File outputFile = new File("C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\OutputFiles\\VEP.txt");
        //outputFile.createNewFile();

        //Use bufferedwriter (syntax for Java 7 and above)- try automatically closes the stream
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(outputFile), "utf-8"))){

            //Set variable to write headers only once at the beginning of the output file
            boolean headers = true;

            //Create key array for robust lookup ordering to ensure that there are no issues if order changes
            //May need later if rename headers from how they are stored as keys- see code below
            //List<String> keyArray = new ArrayList<String>();

            //Decides which fields from the CSQ object to print out- put what field is named in keyArray in search
            ChooseCsqFields choice = new ChooseCsqFields();
            List<String> selectedFields = choice.selectedCsqFields();

            for (String sampleVariantHashMapKey : sampleVariantHashMap.keySet()) {
                String[] splitKey = sampleVariantHashMapKey.split(",");
                String forVariantRetrieval = splitKey[1] + splitKey[2];

                //Retrieve id field which contains dbSNP identifier (if applicable)
                VariantDataObject variantDataObject = variantHashMap.get(forVariantRetrieval);

                for (VepAnnotationObject vepAnnObj : variantDataObject.getCsqObject()) {

                    //Write headers which are not from the CSQ object- set the text to what want to output
                    if (headers) {

                        writer.write("SampleID");
                        writer.write("\t");
                        writer.write("Chromosome");
                        writer.write("\t");
                        writer.write("Variant");
                        writer.write("\t");
                        writer.write("AlleleFrequency");
                        writer.write("\t");
                        writer.write("Quality");
                        writer.write("\t");
                        writer.write("dbSNP");

                        /*
                        //keyArray is all of the headers- not required at present but could be useful if decide to
                        //rename what headers are called
                        for (String key : vepAnnObj.getVepAnnotationHashMap().keySet()) {
                            keyArray.add(key); //Could consider linking this with what want to call each field in output
                            //writer.write("\t"); //Commented out as only want to write out required data
                            //writer.write(key); //Commented out as only want to write out required data
                        }
                        */

                        //Write out the headers- temporary workaround
                        for (String headersKey : selectedFields) {
                            //Populate the keyArray with the headers for later retrieval of data
                            writer.write("\t");
                            writer.write(headersKey);
                        }
                        headers = false;
                        writer.newLine();
                    }

                    //Sample name
                    writer.write(sampleVariantHashMapKey.split(",")[0]);
                    writer.write("\t");

                    //Sample chromosome
                    writer.write(sampleVariantHashMapKey.split(",")[1].split(":")[0]);
                    writer.write("\t");

                    //Sample variant as g.posref>alt
                    //System.out.println("g." + (sampleVariantHashMapKey.split(",")[1].split(":")[1]) +
                    //(sampleVariantHashMapKey.split(",")[2]));

                    //Minimise alleles using the GenomeVariant class and write out the variant in this format
                    GenomeVariant genomeVariant = new GenomeVariant((sampleVariantHashMapKey.split(",")[1]) +
                        (sampleVariantHashMapKey.split(",")[2]));
                    genomeVariant.convertToMinimalRepresentation(); //Untested for accuracy
                    writer.write("g." + (genomeVariant));
                    writer.write("\t");

                    //Sample allele frequency
                    //Truncate output to 3 decimal places
                    writer.write(String.format("%.3f", sampleVariantHashMap.get(sampleVariantHashMapKey).getAlleleFrequency()));
                    writer.write("\t");

                    //Sample quality (genotype) - not required 12/01/17
                    //writer.write(Integer.toString(sampleVariantHashMap.get(sampleVariantHashMapKey).getGenotypeQuality()));
                    //writer.write("\t");

                    //Variant quality
                    //Truncate output to 3 decimal places
                    writer.write(String.format("%.3f", variantHashMap.get(forVariantRetrieval).getVariantQuality()));
                    writer.write("\t");

                    //dbSNP
                    //Truncate output to 3 decimal places
                    writer.write((variantHashMap.get(forVariantRetrieval).getIdField()));
                    writer.write("\t");

                    for (String keyToPrint : selectedFields) {
                        //System.out.println(keyToPrint);
                        //writer.write(keyToPrint);
                        writer.write(vepAnnObj.getVepEntry(keyToPrint));
                        writer.write("\t");
                        //writer.write(vepAnnObj.getVepEntry(keyArray.get(keyArray.indexOf(keyToPrint))));
                    }

                    /*
                    for (String key : keyArray){
                        writer.write(vepAnnObj.getVepEntry(key));
                        writer.write("\t");
                    }
                    */

                    writer.newLine();
                }
            }
        }
    }
}

