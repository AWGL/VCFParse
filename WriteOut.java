package nhs.genetics.cardiff;

import htsjdk.variant.variantcontext.Genotype;
import nhs.genetics.cardiff.framework.GenomeVariant;
import nhs.genetics.cardiff.framework.Pair;
import nhs.genetics.cardiff.framework.vep.VepAnnotationObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Writes out desired fields to a tab-delimited text file
 *
 * @author  Sara Rey
 * @since   2016-12-08
 */

public class WriteOut {
    public static void writeToTable(VepVcf vepVcf, HashSet<String> preferredTranscripts, HashMap<GenomeVariant, Integer> classifiedVariants, Boolean onlyReportKnownRefSeq) throws IOException {

        //write header lines
        for (String sampleId : vepVcf.getSampleNames()){
            try (PrintWriter writer = new PrintWriter(vepVcf.getSampleMetaDataHashMap().get(sampleId).getSeqId() + "_" + sampleId + "_VariantReport.txt")){
                writer.println("SampleID\tVariant\tFrequency\tDepth\tGenotype\tQuality\tClassification\tPreferredTranscript\tdbSNP\tCosmic\tHGMD\tExAC_AFR\tExAC_AMR\tExAC_EAS\tExAC_FIN\tExAC_NFE\tExAC_SAS\tExAC_OTH\t1KG_African\t1KG_American\t1KG_EastAsian\t1KG_European\t1KG_SouthAsian\tGene\tTranscript\tHGVSc\tHGVSp\tConsequence\tIntron\tExon\tSIFT\tPolyPhen");
            }
        }

        //loop over all variants
        for (Map.Entry<GenomeVariant, ArrayList<Pair<Genotype, Double>>> variantGenotypeEntry : vepVcf.getSampleVariants().entrySet()){

            //loop over all genotypes
            for (Pair<Genotype, Double> genotype : variantGenotypeEntry.getValue()){

                //write to variant report
                try (FileWriter fileWriter = new FileWriter(vepVcf.getSampleMetaDataHashMap().get(genotype.getLeft().getSampleName()).getSeqId() + "_" + genotype.getLeft().getSampleName() + "_VariantReport.txt", true);
                     BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                     PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

                    //loop over functional annotations
                    for (VepAnnotationObject vepAnnotation : vepVcf.getAnnotatedVariants().get(variantGenotypeEntry.getKey())){
                        if (onlyReportKnownRefSeq && (vepAnnotation.getFeature() == null || !vepAnnotation.getFeature().startsWith("NM_"))) continue;

                        //print variant report
                        printWriter.print(genotype.getLeft().getSampleName());
                        printWriter.print("\t");
                        printWriter.print(variantGenotypeEntry.getKey());
                        printWriter.print("\t");
                        printWriter.print(String.format("%.2f", calcAllelePercent(genotype.getLeft().getAD()[0], genotype.getLeft().getAD()[1])) + "%");
                        printWriter.print("\t");
                        printWriter.print(genotype.getLeft().getDP());
                        printWriter.print("\t");
                        printWriter.print(genotype.getLeft().getType());
                        printWriter.print("\t");
                        printWriter.print(genotype.getRight());
                        printWriter.print("\t");
                        if (classifiedVariants.containsKey(variantGenotypeEntry.getKey())) printWriter.print(classifiedVariants.get(variantGenotypeEntry.getKey()));
                        printWriter.print("\t");
                        printWriter.print(preferredTranscripts.contains(vepAnnotation.getFeature()));
                        printWriter.print("\t");
                        printWriter.print(vepAnnotation);
                        printWriter.print("\n");

                    }

                }

            }

        }

    }

    private static double calcAllelePercent(Integer refDp, Integer altDP){
        return (double) altDP / (refDp + altDP) * 100;
    }
}


