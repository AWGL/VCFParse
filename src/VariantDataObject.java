import java.util.Collection;

/**
 * Created by Sara on 05-Dec-16.
 */

//http://stackoverflow.com/questions/4956844/hashmap-with-multiple-values-under-the-same-key


public class VariantDataObject {

    private Collection<VepAnnotationObject> vepAnnotationObjects;
    private boolean variantFiltered;
    private boolean variantSite;
    private String idField;

    public VariantDataObject(Collection<VepAnnotationObject> vepAnnotationObjects, boolean variantFiltered,
                             boolean variantSite, String idField) {
        this.vepAnnotationObjects = vepAnnotationObjects;
        this.variantFiltered = variantFiltered;
        this.variantSite = variantSite;
        this.idField = idField;
    }

    public Collection<VepAnnotationObject> getCsqObject() { return this.vepAnnotationObjects; }
    public boolean getIsVariantFiltered() { return this.variantFiltered; }
    public boolean getIsVariantSite() { return this.variantSite; }
    public String getIdField() { return this.idField; }

}
