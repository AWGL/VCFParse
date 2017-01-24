package nhs.genetics.cardiff;

/**
 * Class for storing sample & variant data
 *
 * @author  Sara Rey
 * @since   2016-12-06
 */

public class SampleVariantDataObject {

    private GenomeVariant variantObjectKey;
    private int alleleDepth;
    private int alleleNum;
    private String sampleName;
    private boolean filtered;
    private boolean mixed;
    private int ploidy;
    private String zygosity;
    private int genotypeQuality;
    private double alleleFrequency;


    public SampleVariantDataObject(GenomeVariant variantObjectKey, int alleleDepth, int alleleNum,
                                   String sampleName, boolean filtered, boolean mixed, int ploidy,
                                   String zygosity, int genotypeQuality, double alleleFrequency){
        this.variantObjectKey = variantObjectKey;
        this.alleleDepth = alleleDepth;
        this.alleleNum = alleleNum;
        this.sampleName = sampleName;
        this.filtered = filtered;
        this.mixed = mixed;
        this.ploidy = ploidy;
        this.zygosity = zygosity;
        this.genotypeQuality = genotypeQuality;
        this.alleleFrequency = alleleFrequency;
    }

    public GenomeVariant getVariantObjectKeyAsGenomeVariant(){return this.variantObjectKey;}
    public int getAlleleDepth(){return this.alleleDepth;}
    public String getVariantObjectKey() {return this.variantObjectKey.toString();}
    public int getAlleleNum() {return this.alleleNum;}
    public String getSampleName(){return this.sampleName;}
    public boolean getFiltered(){return this.filtered;}
    public boolean getMixed(){return this.mixed;}
    public int getPloidy(){return this.ploidy;}
    public String getZygosity() {return this.zygosity;}
    public int getGenotypeQuality(){return this.genotypeQuality;}
    public double getAlleleFrequency(){return this.alleleFrequency;}

    //@Override
    //public String toString() {
        //return contig + ":" + Integer.toString(pos) + ref + ">" + alt;
    //}

}
