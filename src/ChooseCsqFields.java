import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sara on 10-Jan-17.
 */
public class ChooseCsqFields {

    public ChooseCsqFields(){}

    public List<String> selectedCsqFields() {
        //Put this in in the order you want them to come out
        List<String> csqFieldsToPrint = new ArrayList<String>();
        csqFieldsToPrint.add("Allele");
        csqFieldsToPrint.add("AFR_MAF");
        csqFieldsToPrint.add("AMR_MAF");
        csqFieldsToPrint.add("EAS_MAF");
        csqFieldsToPrint.add("EUR_MAF");
        csqFieldsToPrint.add("SAS_MAF");
        csqFieldsToPrint.add("ExAC_AFR_MAF");
        csqFieldsToPrint.add("ExAC_AMR_MAF");
        csqFieldsToPrint.add("ExAC_EAS_MAF");
        csqFieldsToPrint.add("ExAC_FIN_MAF");
        csqFieldsToPrint.add("ExAC_NFE_MAF");
        csqFieldsToPrint.add("ExAC_OTH_MAF");
        csqFieldsToPrint.add("ExAC_SAS_MAF");
        csqFieldsToPrint.add("Feature");
        csqFieldsToPrint.add("SYMBOL");
        csqFieldsToPrint.add("HGVSc");
        csqFieldsToPrint.add("HGVSp");
        csqFieldsToPrint.add("Consequence");
        csqFieldsToPrint.add("EXON");
        csqFieldsToPrint.add("INTRON");
        csqFieldsToPrint.add("SIFT");
        csqFieldsToPrint.add("PolyPhen");
        return csqFieldsToPrint;
    }

}
