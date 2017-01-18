import com.google.common.collect.ArrayListMultimap;

import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.LinkedHashMap;
import java.io.BufferedReader;
import java.util.Collection;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Collections;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Sara on 08-Dec-16.
 */

public class WriteOut {

    //Writes out desired fields to a tab-delimited text file

    private LinkedHashMap<String, SampleVariantDataObject> sampleVariantHashMap;
    private LinkedHashMap<String, VariantDataObject> variantHashMap;

    public WriteOut(LinkedHashMap<String, SampleVariantDataObject> sampleVariantHashMap,
                    LinkedHashMap<String, VariantDataObject> variantHashMap) {
        this.sampleVariantHashMap = sampleVariantHashMap;
        this.variantHashMap = variantHashMap;
    }

    //Temp write out all vep annotations
    //Set file path to desired location
    public void writeOutVepAnnotations() throws Exception {
        String outputPath = "C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\OutputFiles\\VEP.txt";
        final File outputFile = new File(outputPath);
        //outputFile.createNewFile();

        //Use bufferedwriter (syntax for Java 7 and above)- try automatically closes the stream on exception
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

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

                //Retrieve all csq fields associated with the variant in the current iteration
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
                        writer.write("ID");

                        /*
                        //keyArray is all of the headers- not required at present but could be useful if decide to
                        //rename what headers are called
                        for (String key : vepAnnObj.getVepAnnotationHashMap().keySet()) {
                            keyArray.add(key); //Could consider linking this with what want to call each field in output
                            //writer.write("\t"); //Commented out as only want to write out required data
                            //writer.write(key); //Commented out as only want to write out required data
                        }
                        */

                        //Write out the headers from the csq fields
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
                    //Create list of sample names (saves iterating again later)


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
        splitMultisample(outputFile);
    }

    public void splitMultisample(File inputMultisample) throws Exception {
        try (BufferedReader multisample = new BufferedReader(new FileReader(inputMultisample))) {
            ArrayListMultimap<String, String> samplesData = ArrayListMultimap.create();

            //Assign data to specific samples
            String headers = multisample.readLine(); //headers line- needed for every sample
            String line;
            while ((line = multisample.readLine()) != null) {
                samplesData.put(line.split("\\t")[0], line);
            }

            for (String key : samplesData.keySet()) {
                String outputPath = "C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\OutputFiles\\";
                String outputSampleName = key;
                File outputFile = new File(outputPath + outputSampleName + ".txt");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                    writer.write(headers);
                    writer.newLine();
                    for (String data : samplesData.get(key)) {
                        writer.write(data);
                        writer.newLine();
                    }
                }
            }
        }
    }
}