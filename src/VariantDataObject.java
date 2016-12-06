/**
 * Created by Sara on 05-Dec-16.
 */

//http://stackoverflow.com/questions/4956844/hashmap-with-multiple-values-under-the-same-key




public class VariantDataObject {

    private CsqObject csqObject;
    private boolean variantFiltered;
    private boolean variantSite;

    public VariantDataObject(CsqObject csqObject, boolean variantFiltered, boolean variantSite) {
        this.csqObject = csqObject;
        this.variantFiltered = variantFiltered;
        this.variantSite = variantSite;
    }

    public CsqObject getCsqObject() { return this.csqObject; }
    public boolean getIsVariantFiltered() { return this.variantFiltered; }
    public boolean getIsVariantSite() { return this.variantSite; }

}
