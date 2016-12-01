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
            x+=1;
            System.out.println(x);

            //System.out.println(vc.getGenotypes());

            //gen.copy(gen);

            //System.out.println(copy(vc.getGenotypes() 'TGA');

            GenotypesContext gen = vc.getGenotypes();
            Iterator<Genotype> gt = vc.getGenotypes().iterator();

            System.out.println(gen.get(1).getAllele(0));

            while (gt.hasNext()) {
                //System.out.println(gt); // Iterator Object
                System.out.println(gt.next());
                //System.out.println(gt.next().getClass()); //Can use methods associated with FastGenotype
                System.out.println(gt.next().getAlleles());

            }


            break;
        }

        //Could be possible to use JEXL to extract subsets of information such as variants in which sample
        //http://gatkforums.broadinstitute.org/wdl/discussion/1255/using-jexl-to-apply-hard-filters-or-select-variants-based-on-annotation-values




    }

}
