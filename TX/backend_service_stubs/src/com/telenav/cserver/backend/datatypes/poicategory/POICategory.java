/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.poicategory;

/**
 * 
 * @author zhjdou
 * 2010-4-27
 */
public class POICategory extends Category
{
     private String verision="";
     //HOTLIST or FULL
     private String Hierarchy;
     
     private int displaySequence=-1;
     
     private boolean isNavigable;
     
     private boolean isPopularSearch;
     
     public String toString() {
         StringBuilder sb=new StringBuilder();
         sb.append(super.toString());
         sb.append("PoiCategory==>");
         sb.append("verision=").append(this.verision);
         sb.append(" &Hierarchy=").append(this.Hierarchy);
         sb.append(" &displaySequence=").append(this.displaySequence);
         sb.append(" &isNavigable=").append(this.isNavigable);
         sb.append(" &isPopularSearch=").append(this.isPopularSearch);
         return sb.toString();
     }
     
     
    /**
     * @return the verision
     */
    public String getVerision()
    {
        return verision;
    }

    /**
     * @return the hierarchy
     */
    public String getHierarchy()
    {
        return Hierarchy;
    }

    /**
     * @return the displaySequence
     */
    public int getDisplaySequence()
    {
        return displaySequence;
    }

    /**
     * @return the isNavigable
     */
    public boolean isNavigable()
    {
        return isNavigable;
    }

    /**
     * @return the isPopularSearch
     */
    public boolean isPopularSearch()
    {
        return isPopularSearch;
    }

    /**
     * @param verision the verision to set
     */
    public void setVerision(String verision)
    {
        this.verision = verision;
    }

    /**
     * @param hierarchy the hierarchy to set
     */
    public void setHierarchy(String hierarchy)
    {
        Hierarchy = hierarchy;
    }

    /**
     * @param displaySequence the displaySequence to set
     */
    public void setDisplaySequence(int displaySequence)
    {
        this.displaySequence = displaySequence;
    }

    /**
     * @param isNavigable the isNavigable to set
     */
    public void setNavigable(boolean isNavigable)
    {
        this.isNavigable = isNavigable;
    }

    /**
     * @param isPopularSearch the isPopularSearch to set
     */
    public void setPopularSearch(boolean isPopularSearch)
    {
        this.isPopularSearch = isPopularSearch;
    }
     
     
}
