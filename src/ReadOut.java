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

            //Skip hom ref sites, which we don't want to readout- or should we vaoid storing them


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

            System.out.println(sampleVariants.get(sampleVariantHashMapKey).getSampleDataObject().getSampleName());
            System.out.println(forVariantRetrieval);
            System.out.println(sampleVariants.get(sampleVariantHashMapKey).getVariantObjectKey()); //Check data correct
            //Allele frequency
            System.out.println(sampleVariants.get(sampleVariantHashMapKey).getSampleDataObject().getGenotypeQuality());
            System.out.println(variants);
            System.out.println(variants.get(forVariantRetrieval).getIdField());





            //Calculate allele frequency

        }

    }


}
