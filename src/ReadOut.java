import java.util.LinkedHashMap;

/**
 * Created by Sara on 08-Dec-16.
 */
public class ReadOut {

    private LinkedHashMap<String, VariantDataObject> variants;
    private LinkedHashMap<String, SampleVariantDataObject> sampleVariants;


    public ReadOut(LinkedHashMap<String, SampleVariantDataObject> sampleVariants,
                   LinkedHashMap<String, VariantDataObject> variants){
        this.variants = variants;
        this.sampleVariants = sampleVariants;
    }


    public void readOutTest(){

        for (String sampleVariantHashMapKey : sampleVariants.keySet()) {

            //Skip hom ref sites, which we don't want to readout- or should we vaoid storing them


            //System.out.println(test);
            //System.out.println(sampleVariantHashMap.get(test));
            String[] splitKey = sampleVariantHashMapKey.split(",");
            //System.out.println(splitKey[0]);
            //System.out.println(splitKey[1]);
            //System.out.println(splitKey[2]);
            //System.out.println(splitKey[1]+splitKey[2]);
            String forVariantRetrieval = splitKey[1] + splitKey[2];
            //tested below as working- retrieves the same variant data object for the same
            ////System.out.println(forVariantRetrieval);
            //This is null where the allele is the same as the reference for that sample- may want to remove later
            ////System.out.println(variantHashMap.get(forVariantRetrieval));

            //System.out.println(sampleVariantHashMap.get(forVariantRetrieval));

            //System.out.println(sampleVariantHashMapKey);
            //System.out.println(sampleVariants.get(sampleVariantHashMapKey).getSampleDataObject().getSampleName());



            //Commented out to work on CSQ identification, duplication issue and logic changes to link ONLY the
               //relevant VepAnnotationObjects to each allele. At present all (for each variant context) are linked
               //to every allele in that variant context.

            System.out.println(forVariantRetrieval);
            System.out.println(sampleVariants.get(sampleVariantHashMapKey).getVariantObjectKey()); //Check data correct
            //Allele frequency
            //System.out.println(sampleVariants.get(sampleVariantHashMapKey).getSampleDataObject().getGenotypeQuality());
            //System.out.println(variants);
            System.out.println(variants.get(forVariantRetrieval).getIdField()); //dbSNP
            System.out.println(sampleVariants.get(sampleVariantHashMapKey).getAlleleNum()); //null pointer exception for ref allele-FIX

            //Allele frequency
            System.out.println(sampleVariants.get(sampleVariantHashMapKey).getAlleleFrequency());

            //Get required data from CSQ fields
            for (VepAnnotationObject vepAnnObj : variants.get(forVariantRetrieval).getCsqObject()) {
                /*
                System.out.println(vepAnnObj);
                System.out.println(vepAnnObj.getVepHeaders());
                System.out.println(vepAnnObj.getEntireVepRecordValues());
                System.out.println(vepAnnObj.getAlleleNum());
                System.out.println(vepAnnObj.getVepEntry("AFR_MAF"));
                System.out.println(vepAnnObj.getVepEntry("AMR_MAF"));
                System.out.println(vepAnnObj.getVepEntry("EAS_MAF"));
                System.out.println(vepAnnObj.getVepEntry("EUR_MAF"));
                System.out.println(vepAnnObj.getVepEntry("SAS_MAF"));
                System.out.println(vepAnnObj.getVepEntry("AA_MAF"));
                System.out.println(vepAnnObj.getVepEntry("EA_MAF"));
                System.out.println(vepAnnObj.getVepEntry("ExAC_MAF"));
                System.out.println(vepAnnObj.getVepEntry("ExAC_Adj_MAF"));
                System.out.println(vepAnnObj.getVepEntry("ExAC_AFR_MAF"));
                System.out.println(vepAnnObj.getVepEntry("ExAC_AMR_MAF"));
                System.out.println(vepAnnObj.getVepEntry("ExAC_EAS_MAF"));
                System.out.println(vepAnnObj.getVepEntry("ExAC_FIN_MAF"));
                System.out.println(vepAnnObj.getVepEntry("ExAC_NFE_MAF"));
                System.out.println(vepAnnObj.getVepEntry("ExAC_OTH_MAF"));
                System.out.println(vepAnnObj.getVepEntry("ExAC_SAS_MAF"));
                //Transcript
                System.out.println(vepAnnObj.getVepEntry("Feature"));
                //Boolean- data not available for preferred transcript- Matt
                //System.out.println(vepAnnObj.getVepEntry("PreferredTranscript"));

                System.out.println(vepAnnObj.getVepEntry("Gene")); //Sometimes this is a number
                System.out.println(vepAnnObj.getVepEntry("HGVSc"));
                System.out.println(vepAnnObj.getVepEntry("HGVSp"));
                System.out.println(vepAnnObj.getVepEntry("Consequence"));
                System.out.println(vepAnnObj.getVepEntry("Exon")); //Either or with Intron
                System.out.println(vepAnnObj.getVepEntry("Intron")); //Either or with Exon
                */

                if (vepAnnObj.getVepEntry("SIFT").equals("") && vepAnnObj.getVepEntry("PolyPhen").equals("")){
                    continue; //Need to skip where it is empty or the [1] index won't work
                }

                System.out.println(vepAnnObj.getVepEntry("SIFT"));
                System.out.println(vepAnnObj.getVepEntry("PolyPhen"));

                //SIFT Result
                System.out.println(vepAnnObj.getVepEntry("SIFT").split("[\\(\\)]")[0]);
                //SIFT Score
                System.out.println(vepAnnObj.getVepEntry("SIFT").split("[\\(\\)]")[1]);
                //Polyphen Result
                System.out.println(vepAnnObj.getVepEntry("PolyPhen").split("\\(")[0]);
                //Polyphen Score
                System.out.println(vepAnnObj.getVepEntry("PolyPhen").split("[\\(\\)]")[1]);

            }


            //System.out.println(variants.get(forVariantRetrieval).getCsqObject()); //Multiple VepAnnotation objects for this variant


            for (VepAnnotationObject vepAnnObj : variants.get(forVariantRetrieval).getCsqObject()) {
                //System.out.println(vepAnnObj.getEntireVepRecordValues());
            }

            //System.out.println(variants.get(forVariantRetrieval).getCsqObject().getClass());


            //System.out.println(variants.get(forVariantRetrieval).getCsqObject().getEntireCsqObject());
            //System.out.println(variants.get(forVariantRetrieval).getCsqObject().getSpecificCsqObject(1));
            //System.out.println(variants.get(forVariantRetrieval).getCsqObject().getCsqObjectVepAnnotationValues(1));





            //Calculate allele frequency

        }

    }


}
