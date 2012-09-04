package com.telenav.cserver.poi.holder;

import java.util.ArrayList;
import java.util.List;

public class PoiLocalParametersCollection {
	
	private String carrierName;
	private String poiFindVersion;
	private ArrayList<PoiCategoryItem> subCategory = new ArrayList<PoiCategoryItem>();
	
	
	public String getCarrierName() {
		return carrierName;
	}
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	public String getPoiFindVersion() {
		return poiFindVersion;
	}
	public void setPoiFindVersion(String poiFindVersion) {
		this.poiFindVersion = poiFindVersion;
	}
	public List<PoiCategoryItem> getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(ArrayList<PoiCategoryItem> subCategory) {
		this.subCategory = subCategory;
	}
	
	public void addSubCategory(PoiCategoryItem sc)
	{
		subCategory.add(sc);
	}

}
