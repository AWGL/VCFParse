/**
 * Created by Sara on 06-Dec-16.
 */
public class SampleVariantDataObject {

    //private VariantDataObject variant;
    private GenomeVariant variantObjectKey;
    private SampleDataObject sampleDataObject;


    public SampleVariantDataObject(GenomeVariant variantObjectKey, SampleDataObject sampleDataObject){
        this.variantObjectKey = variantObjectKey;
        this.sampleDataObject = sampleDataObject;
    }

    public GenomeVariant getVariantObjectKeyAsObject(){return this.variantObjectKey;}
    public SampleDataObject getsampleDataObject(){return this.sampleDataObject;}
    public String getVariantObjectKey() {return this.variantObjectKey.toString();}

    //@Override
    //public String toString() {
        //return contig + ":" + Integer.toString(pos) + ref + ">" + alt;
    //}

}
