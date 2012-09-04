/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.poicategory;

import com.telenav.cserver.backend.datatypes.poicategory.POICategory;

/**
 * 
 * @author zhjdou
 * 2010-4-27
 */
public class PoiCategoryResponse
{
    private String statusCode;
    private String statusMsg;
    private POICategory[] categories;
    
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("PoiCategoryResponse==>");
        sb.append("statusCode=").append(this.statusCode);
        sb.append(" &statusMsg=").append(this.statusMsg);
        if(this.categories!=null) {
            for(int i=0;i<this.categories.length;i++) {
                sb.append("&POICategory>"+i+" = ");
                sb.append(this.categories[i].toString()+"\n");
            }
         }
        return sb.toString();
    }
    
    /**
     * @return the categories
     */
    public POICategory[] getCategories()
    {
        return categories;
    }
    /**
     * @param categories the categories to set
     */
    public void setCategories(POICategory[] categories)
    {
        this.categories = categories;
    }
    /**
     * @return the statusCode
     */
    public String getStatusCode()
    {
        return statusCode;
    }
    /**
     * @return the statusMsg
     */
    public String getStatusMsg()
    {
        return statusMsg;
    }
    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(String statusCode)
    {
        this.statusCode = statusCode;
    }
    /**
     * @param statusMsg the statusMsg to set
     */
    public void setStatusMsg(String statusMsg)
    {
        this.statusMsg = statusMsg;
    }
}
