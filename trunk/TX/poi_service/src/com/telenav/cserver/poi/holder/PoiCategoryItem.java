/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.poi.holder;

import java.util.List;

/**
 * PoiCategoryItem
 * @author kwwang
 *
 */
public class PoiCategoryItem implements Comparable{
	private String name;
	private String id;
	private String imageId;
	private String mostPopular;
	private String indicator;	//0=Hot list with one layer; 1=Hot list with two layers; 2=Hot list with more flag
	private List<PoiCategoryItem> subCategory;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getMostPopular() {
		return mostPopular;
	}
	public void setMostPopular(String mostPopular) {
		this.mostPopular = mostPopular;
	}
	public String getIndicator() {
		return indicator;
	}
	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

    public List<PoiCategoryItem> getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(List<PoiCategoryItem> subCategory) {
		this.subCategory = subCategory;
	}
	public int compareTo(Object o)
    {
        if (o instanceof PoiCategoryItem)
        {
            return compareTo((PoiCategoryItem) o);
        }
        else
        {
        	return 0;
        }
    }
    public int compareTo(PoiCategoryItem obj1)
    {
        return this.getName().compareTo(obj1.getName());
    }
}
