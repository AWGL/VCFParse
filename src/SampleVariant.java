import java.util.ArrayList;
import java.util.List;
import htsjdk.variant.variantcontext.Allele;

/**
 * Created by Sara on 06-Dec-16.
 */
public class SampleVariant {

    private String sampleName;
    private String contig;
    private int position;
    private List<Allele> alleleList;
    private Allele allele;
    private Allele refAllele;

    public SampleVariant(String sampleName, String contig, int position, List<Allele> alleleList, Allele allele,
                         Allele refAllele){
        this.sampleName = sampleName;
        this.contig = contig;
        this.position = position;
        this.alleleList = alleleList;
        this.allele = allele;
        this.refAllele = refAllele;
    }

    public String getSampleName() {return this.sampleName;}
    public String getContig() {return this.contig;}
    public int getPosition() {return this.position;}
    private List<Allele> getAlleleList() {return this.alleleList;}
    private Allele getAllele() {return this.allele;}
    private Allele getRefAllele() {return this.refAllele;}

    @Override
    public String toString() {
        return sampleName + "," + contig + ":" + Integer.toString(position) + ","
                + refAllele.toString().replaceAll("\\*", "") + ">" + allele.toString().replaceAll("\\*", "");
    }

}



