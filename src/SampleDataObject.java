/**
 * Created by Sara on 06-Dec-16.
 */
public class SampleDataObject {

    //private VariantDataObject variant;
    private String sampleName;
    private boolean filtered;
    private boolean mixed;
    private int ploidy;
    private String zygosity;
    private int genotypeQuality;


    public SampleDataObject(String sampleName, boolean filtered,
                                   boolean mixed, int ploidy, String zygosity, int genotypeQuality){
        this.sampleName = sampleName;
        this.filtered = filtered;
        this.mixed = mixed;
        this.ploidy = ploidy;
        this.zygosity = zygosity;
        this.genotypeQuality = genotypeQuality;
        }

    public String getSampleName(){return this.sampleName;}
    public boolean getFiltered(){return this.filtered;}
    public boolean getMixed(){return this.mixed;}
    public int getPloidy(){return this.ploidy;}
    public String getZygosity() {return this.zygosity;}
    public int getGenotypeQuality(){return this.genotypeQuality;}

}
