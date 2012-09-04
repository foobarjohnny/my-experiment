/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.poicategory;

/**
 * 
 * @author zhjdou
 * 2010-4-27
 */
public class PoiCategoryRequest
{
    private long categoryId=0L;
    
    private String keyWord;
    
    private String version;
    
    private int poiHierarchyId;
    
    private String strContext;
    
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("PoiCategoryRequest==>");
        sb.append("categoryId=").append(this.categoryId);
        sb.append(" &keyWord=").append(this.keyWord);
        sb.append(" &version=").append(this.version);
        sb.append(" &poiHierarchyId=").append(this.poiHierarchyId);
        sb.append(" &strContext=").append(this.strContext);
        return sb.toString();
    }
    
    /**
     * @return the strContext
     */
    public String getStrContext()
    {
        return strContext;
    }



    /**
     * @param strContext the strContext to set
     */
    public void setStrContext(String strContext)
    {
        this.strContext = strContext;
    }



    /**
     * @return the poiHierarchyId
     */
    public int getPoiHierarchyId()
    {
        return poiHierarchyId;
    }


    /**
     * @param poiHierarchyId the poiHierarchyId to set
     */
    public void setPoiHierarchyId(int poiHierarchyId)
    {
        this.poiHierarchyId = poiHierarchyId;
    }


    /**
     * @return the version
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     * @return the keyWord
     */
    public String getKeyWord()
    {
        return keyWord;
    }

    /**
     * @param keyWord the keyWord to set
     */
    public void setKeyWord(String keyWord)
    {
        this.keyWord = keyWord;
    }

    /**
     * @return the categoryId
     */
    public long getCategoryId()
    {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(long categoryId)
    {
        this.categoryId = categoryId;
    }
}
