import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.variantcontext.Genotype;

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

            Iterator<Genotype> gt = vc.getGenotypes().iterator();

            while (gt.hasNext()) {
                System.out.println(gt.next());
            }


            break;
        }

        //Could be possible to use JEXL to extract subsets of information such as variants in which sample
        //http://gatkforums.broadinstitute.org/wdl/discussion/1255/using-jexl-to-apply-hard-filters-or-select-variants-based-on-annotation-values




    }

}
