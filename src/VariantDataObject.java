import java.util.Collection;
import java.util.ArrayList;
import com.google.common.collect.ImmutableList;

/**
 * Created by Sara on 05-Dec-16.
 */

//http://stackoverflow.com/questions/4956844/hashmap-with-multiple-values-under-the-same-key


public class VariantDataObject {

    private ArrayList<VepAnnotationObject> vepAnnotationObjects;
    private boolean variantFiltered;
    private boolean variantSite;
    private String idField;

    public VariantDataObject(ArrayList<VepAnnotationObject> vepAnnotationObjects, boolean variantFiltered,
                             boolean variantSite, String idField) {
        this.vepAnnotationObjects = vepAnnotationObjects;
        this.variantFiltered = variantFiltered;
        this.variantSite = variantSite;
        this.idField = idField;
    }

    public ArrayList<VepAnnotationObject> getCsqObject() { return this.vepAnnotationObjects; }
    public Collection<String> getVepAnnotationHeaders() {return this.vepAnnotationObjects.get(0).getVepHeaders();}
    public boolean getIsVariantFiltered() { return this.variantFiltered; }
    public boolean getIsVariantSite() { return this.variantSite; }
    public String getIdField() { return this.idField; }

}
