/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.poicategory;

/**
 * category
 * @author zhjdou
 * 2010-4-27
 */
public class Category
{
    private long id;
    private long parentId;
    private String label;
    private Category[] subCategories;
    private String categoryPath;
    
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("Category==>");
        sb.append("id=").append(this.id);
        sb.append(" &parentId=").append(this.parentId);
        sb.append(" &label=").append(this.label);
        sb.append(" &categoryPath=").append(this.categoryPath);
        if(subCategories!=null) {
           for(int i=0;i<this.subCategories.length;i++) {
               sb.append("&Category>"+i+" = ");
               sb.append(this.subCategories[i].toString());
           }
        }
        return sb.toString();
    }
    
    /**
     * @return the id
     */
    public long getId()
    {
        return id;
    }
    /**
     * @return the parentId
     */
    public long getParentId()
    {
        return parentId;
    }
    /**
     * @return the label
     */
    public String getLabel()
    {
        return label;
    }
    /**
     * @return the subCategories
     */
    public Category[] getSubCategories()
    {
        return subCategories;
    }
    /**
     * @return the categoryPath
     */
    public String getCategoryPath()
    {
        return categoryPath;
    }
    /**
     * @param id the id to set
     */
    public void setId(long id)
    {
        this.id = id;
    }
    /**
     * @param parentId the parentId to set
     */
    public void setParentId(long parentId)
    {
        this.parentId = parentId;
    }
    /**
     * @param label the label to set
     */
    public void setLabel(String label)
    {
        this.label = label;
    }
    /**
     * @param subCategories the subCategories to set
     */
    public void setSubCategories(Category[] subCategories)
    {
        this.subCategories = subCategories;
    }
    /**
     * @param categoryPath the categoryPath to set
     */
    public void setCategoryPath(String categoryPath)
    {
        this.categoryPath = categoryPath;
    }
    
}
