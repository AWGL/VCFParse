import java.util.LinkedHashMap;

/**
 * Created by Sara on 08-Dec-16.
 */
public class ReadOut {

    private LinkedHashMap<String, VariantDataObject> variants;
    private LinkedHashMap<String, SampleVariantDataObject> sampleVariants;


    public ReadOut(LinkedHashMap<String, VariantDataObject> variants,
                   LinkedHashMap<String, SampleVariantDataObject> sampleVariants){
        this.variants = variants;
        this.sampleVariants = sampleVariants;
    }


    public void readOutTest(){

        for (String sampleVariantHashMapKey : sampleVariants.keySet()) {
            //System.out.println(test);
            //System.out.println(sampleVariantHashMap.get(test));
            String[] splitted = sampleVariantHashMapKey.split(",");
            //System.out.println(splitted[0]);
            //System.out.println(splitted[1]);
            //System.out.println(splitted[2]);
            //System.out.println(splitted[1]+splitted[2]);
            String forVariantRetrieval = splitted[1] + splitted[2];
            //tested below as working- retrieves the same variant data object for the same
            ////System.out.println(forVariantRetrieval);
            //This is null where the allele is the same as the reference for that sample- may want to remove later
            ////System.out.println(variantHashMap.get(forVariantRetrieval));

            //System.out.println(sampleVariantHashMap.get(forVariantRetrieval));

            System.out.println(sampleVariantHashMapKey);
            //System.out.println(sampleVariantHashMap.get(sampleVariantHashMapKey).getAlleleDepth());
            //System.out.println(sampleVariantHashMap.get(sampleVariantHashMapKey).getSampleDataObject().getGenotypeQuality());
        }

    }


}
