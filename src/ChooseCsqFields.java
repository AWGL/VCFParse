import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

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
        csqFieldsToPrint.add("Existing_variation");
        return csqFieldsToPrint;
    }
    public List<String> debuggingCsqFields() {
        //Put this in in the order you want them to come out
        List<String> csqFieldsToPrint = new ArrayList<>(Arrays.asList("Allele","Consequence","IMPACT","SYMBOL",
                "Gene","Feature_type","Feature","BIOTYPE","EXON","INTRON","HGVSc","HGVSp","cDNA_position",
                "CDS_position","Protein_position","Amino_acids","Codons","Existing_variation","ALLELE_NUM","DISTANCE",
                "STRAND","FLAGS","VARIANT_CLASS","SYMBOL_SOURCE","HGNC_ID","CANONICAL","TSL","APPRIS","CCDS","ENSP",
                "SWISSPROT","TREMBL","UNIPARC","REFSEQ_MATCH","GENE_PHENO","SIFT","PolyPhen","DOMAINS","HGVS_OFFSET",
                "GMAF","AFR_MAF","AMR_MAF","EAS_MAF","EUR_MAF","SAS_MAF","AA_MAF","EA_MAF","ExAC_MAF","ExAC_Adj_MAF",
                "ExAC_AFR_MAF","ExAC_AMR_MAF","ExAC_EAS_MAF","ExAC_FIN_MAF","ExAC_NFE_MAF","ExAC_OTH_MAF",
                "ExAC_SAS_MAF","CLIN_SIG","SOMATIC","PHENO","PUBMED","MOTIF_NAME","MOTIF_POS","HIGH_INF_POS",
                "MOTIF_SCORE_CHANGE"));

        return csqFieldsToPrint;

    }

}
