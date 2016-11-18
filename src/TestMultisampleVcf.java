import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;

import java.io.*;

/**
 * Created by Sara on 18-Nov-16.
 */
public class TestMultisampleVcf {


    public TestMultisampleVcf(){}


    public void openMultisampleVcf(File vcfFilePath){
        VCFFileReader vcfFile = new VCFFileReader(vcfFilePath, false);
        for (final VariantContext vc : vcfFile){
            System.out.println(vc.getContig());
            System.out.println(vc.getStart());
            System.out.println(vc);
        }



    }

}
