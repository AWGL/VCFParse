/**
 * Created by Sara on 06-Dec-16.
 */
public class SampleVariantDataObject {

    //private VariantDataObject variant;
    private String sampleName;
    private GenomeVariant variantObjectKey;
    private boolean filtered;
    private boolean mixed;
    private int ploidy;
    private String zygosity;
    private int genotypeQuality;


    public SampleVariantDataObject(String sampleName, GenomeVariant variantObjectKey, boolean filtered,
                                   boolean mixed, int ploidy, String zygosity, int genotypeQuality){
        this.sampleName = sampleName;
        this.variantObjectKey = variantObjectKey;
        this.filtered = filtered;
        this.mixed = mixed;
        this.ploidy = ploidy;
        this.zygosity = zygosity;
        this.genotypeQuality = genotypeQuality;
    }

    public String getSampleName(){return this.sampleName;}
    public GenomeVariant getVariantObjectKey(){return this.variantObjectKey;}
    public boolean getFiltered(){return this.filtered;}
    public boolean getMixed(){return this.mixed;}
    public int getPloidy(){return this.ploidy;}
    private String getZygosity() {return this.zygosity;}
    private int getGenotypeQuality(){return this.genotypeQuality;}
}
