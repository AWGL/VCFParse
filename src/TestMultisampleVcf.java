import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.variantcontext.Genotype;

import htsjdk.variant.variantcontext.GenotypesContext;

import java.util.Iterator;

import java.io.*;

/**
 * Created by Sara on 18-Nov-16.
 */
public class TestMultisampleVcf {


    public TestMultisampleVcf(){}


    public void openMultisampleVcf(File vcfFilePath){
        VCFFileReader vcfFile = new VCFFileReader(vcfFilePath, false);
        int x=0;
        for (final VariantContext vc : vcfFile){
            System.out.println(vc.getContig());
            System.out.println(vc.getStart());
            System.out.println(vc);
            System.out.println(vc.getAlleles());
            x+=1;
            System.out.println(x);

            //System.out.println(vc.getGenotypes());

            //gen.copy(gen);

            //System.out.println(copy(vc.getGenotypes() 'TGA');

            GenotypesContext gt = vc.getGenotypes();
            Iterator<Genotype> gtIter = vc.getGenotypes().iterator();

            System.out.println(gt.get(0).getAllele(0)); //First allele from first genotype entry
            System.out.println(gt.get(0).getAllele(1)); //Second allele from first genotype entry

            while (gtIter.hasNext()) {
                //System.out.println(gt); // Iterator Object
                Genotype currentGenotype = gtIter.next();
                System.out.println(currentGenotype);
                //System.out.println(gt.next().getClass()); //Can use methods associated with FastGenotype
                System.out.println(currentGenotype.getSampleName());
                System.out.println(currentGenotype.getAlleles());
                System.out.println(currentGenotype.isFiltered());
                //System.out.println(currentGenotype.getAnyAttribute("GQ"));
                System.out.println(currentGenotype.getAlleles());
                int[] alleleDepths =  currentGenotype.getAD();
                System.out.println(currentGenotype.getDP());

                System.out.print(currentGenotype.getGQ());

                for (int ad : alleleDepths){System.out.print(ad + ", ");}
                System.out.print("\n");
            }

            break;
        }

        //Could be possible to use JEXL to extract subsets of information such as variants in which sample
        //http://gatkforums.broadinstitute.org/wdl/discussion/1255/using-jexl-to-apply-hard-filters-or-select-variants-based-on-annotation-values


    }

}
