/**
 * Created by Sara on 06-Dec-16.
 */
public class SampleVariantDataObject {

    //private VariantDataObject variant;
    private GenomeVariant variantObjectKey;
    private int alleleDepth;
    private int alleleNum;
    private SampleDataObject sampleDataObject;


    public SampleVariantDataObject(GenomeVariant variantObjectKey, int alleleDepth, int alleleNum,
                                   SampleDataObject sampleDataObject){
        this.variantObjectKey = variantObjectKey;
        this.alleleDepth = alleleDepth;
        this.sampleDataObject = sampleDataObject;
        this.alleleNum = alleleNum;
    }

    public GenomeVariant getVariantObjectKeyAsObject(){return this.variantObjectKey;}
    public int getAlleleDepth(){return this.alleleDepth;}
    public SampleDataObject getSampleDataObject(){return this.sampleDataObject;}
    public String getVariantObjectKey() {return this.variantObjectKey.toString();}
    public int getAlleleNum() {return this.alleleNum;}

    //@Override
    //public String toString() {
        //return contig + ":" + Integer.toString(pos) + ref + ">" + alt;
    //}

}