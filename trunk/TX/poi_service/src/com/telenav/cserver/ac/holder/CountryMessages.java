package com.telenav.cserver.ac.holder;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CountryMessages {

	private Map<String , List<String>> regionMap = new TreeMap<String, List<String>>();

	public Map<String, List<String>> getRegionMap() {
		return regionMap;
	}

	public void setRegionMap(Map<String, List<String>> regionMap) {
		this.regionMap = regionMap;
	}
	
	
}
