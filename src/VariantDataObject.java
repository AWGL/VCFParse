package nhs.genetics.cardiff;

import java.util.ArrayList;

/**
 * Class for storing variant and annotation objects
 *
 * @author  Sara Rey
 * @since   2016-12-05
 */

public class VariantDataObject {

    //http://stackoverflow.com/questions/4956844/hashmap-with-multiple-values-under-the-same-key

    private ArrayList<VepAnnotationObject> vepAnnotationObjects;
    private boolean variantFiltered;
    private boolean variantSite;
    private String idField;
    private double variantQual;

    public VariantDataObject(ArrayList<VepAnnotationObject> vepAnnotationObjects, boolean variantFiltered,
                             boolean variantSite, String idField, double variantQual) {
        this.vepAnnotationObjects = vepAnnotationObjects;
        this.variantFiltered = variantFiltered;
        this.variantSite = variantSite;
        this.idField = idField;
        this.variantQual = variantQual;
    }

    public ArrayList<VepAnnotationObject> getCsqObject() { return this.vepAnnotationObjects; }
    public boolean getIsVariantFiltered() { return this.variantFiltered; }
    public boolean getIsVariantSite() { return this.variantSite; }
    public String getIdField() { return this.idField; }
    public double getVariantQuality() {return this.variantQual;}

}
