import com.google.common.collect.ArrayListMultimap;

import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.LinkedHashMap;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


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

    //Write out required data
    //Set file path to desired location
    public void writeOutVepAnnotations() throws Exception {
        String outputPath = "C:\\Users\\Sara\\Documents\\Work\\VCFtoTab\\OutputFiles\\VEP.txt";
        final File outputFile = new File(outputPath);
        //outputFile.createNewFile();

        ArrayList<ArrayList<String>> toWriteOut = new ArrayList<ArrayList<String>>();

        //Use bufferedwriter (syntax for Java 7 and above)- try automatically closes the stream on exception
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            //Set variable to write headers only once at the beginning of the output file
            boolean headers = true;

            //Create key array for robust lookup ordering to ensure that there are no issues if order changes
            //May need later if rename headers from how they are stored as keys- see code below
            //List<String> keyArray = new ArrayList<String>();

            //Decides which fields from the CSQ object to print out- put what field is named in keyArray in search
            ChooseCsqFields choice = new ChooseCsqFields();
            List<String> selectedFields = choice.debuggingCsqFields();

            for (String sampleVariantHashMapKey : sampleVariantHashMap.keySet()) {
                String[] splitKey = sampleVariantHashMapKey.split(",");
                String forVariantRetrieval = splitKey[1] + splitKey[2];

                //Create header ArrayList<String> to append headers too (could also just use outputRows,
                // but then need to clear it when headers is set to null
                ArrayList<String> headerRows = new ArrayList<String>();

                //Create inner ArrayList<String> to append each 'row' of the final output file
                ArrayList<String> outputRows = new ArrayList<String>();

                //Retrieve all csq fields associated with the variant in the current iteration
                VariantDataObject variantDataObject = variantHashMap.get(forVariantRetrieval);

                for (VepAnnotationObject vepAnnObj : variantDataObject.getCsqObject()) {

                    //Write headers which are not from the CSQ object- set the text to what want to output
                    //Come up with a better solution depending on how want to specify fields to appear in the output
                    if (headers) {

                        headerRows.add("SampleID");
                        headerRows.add("Chromosome");
                        headerRows.add("Variant");
                        headerRows.add("AlleleFrequency");
                        headerRows.add("Quality");
                        headerRows.add("ID");

                        //Write out the headers from the csq fields
                        for (String headersKey : selectedFields) {
                            //Populate the keyArray with the headers for later retrieval of data
                            headerRows.add(headersKey);
                        }
                        headers = false;
                        toWriteOut.add(headerRows);
                    }

                    //Sample name
                    outputRows.add(sampleVariantHashMapKey.split(",")[0]);

                    //Create list of sample names (saves iterating again later)- ?? (18/01/17- needed?)


                    //Sample chromosome
                    outputRows.add(sampleVariantHashMapKey.split(",")[1].split(":")[0]);

                    //Minimise alleles using the GenomeVariant class and write out the variant in this format
                    GenomeVariant genomeVariant = new GenomeVariant((sampleVariantHashMapKey.split(",")[1]) +
                            (sampleVariantHashMapKey.split(",")[2]));
                    genomeVariant.convertToMinimalRepresentation(); //Untested for accuracy
                    outputRows.add("g." + (genomeVariant));

                    //Sample allele frequency
                    //Truncate output to 3 decimal places
                    outputRows.add(String.format("%.3f",
                            sampleVariantHashMap.get(sampleVariantHashMapKey).getAlleleFrequency()));

                    //Sample quality (genotype) - not required 12/01/17
                    //writer.write(Integer.toString(sampleVariantHashMap.get(sampleVariantHashMapKey).getGenotypeQuality()));
                    //writer.write("\t");

                    //Variant quality
                    //Truncate output to 3 decimal places
                    outputRows.add(String.format("%.3f", variantHashMap.get(forVariantRetrieval).getVariantQuality()));

                    //dbSNP
                    outputRows.add((variantHashMap.get(forVariantRetrieval).getIdField()));

                    for (String keyToPrint : selectedFields) {
                        outputRows.add(vepAnnObj.getVepEntry(keyToPrint));
                    }

                    /*
                    for (String key : keyArray){
                        writer.write(vepAnnObj.getVepEntry(key));
                        writer.write("\t");
                    }
                    */
                    toWriteOut.add(outputRows);
                }

            }
            //Workaround to remove apparent duplicates (which occurs because all fields from the CSQ are not written out)
            Set<String> dataSetRow = new TreeSet<String>(toWriteOut);

            //Writeout duplicate removed data
            for (ArrayList<String>dataRow : toWriteOut) {
                for (String dataEntry : dataRow){
                    writer.write(dataEntry);
                    writer.write("\t");
                }
                writer.newLine();
            }
        }
        //split Multisample(outputFile); //To split out the multisample vcf into separate file names
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
                break; //First sample only for ease of testing
            }
        }
    }
}