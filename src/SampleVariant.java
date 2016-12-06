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

    public SampleVariant(String sampleName, String contig, int position, List<Allele> alleleList){
        this.sampleName = sampleName;
        this.contig = contig;
        this.position = position;
        this.alleleList = alleleList;
    }

    public String getSampleName() {return this.sampleName;}
    public String getContig() {return this.contig;}
    public int getPosition() {return this.position;}
    private List<Allele> getAlleleList() {return this.alleleList;}

    @Override
    public String toString() {
        return sampleName + "," + contig + ":" + Integer.toString(position) + alleleList;
    }

}



