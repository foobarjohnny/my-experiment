package com.telenav.cserver.webapp.taglib;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;

public class TnSurveyTEI extends TagExtraInfo{
	public boolean isValid(TagData data){
		Object o = data.getAttribute("pageSize");
		Object o1 = data.getAttribute("pageNumber");
		return validateAttribute(o) && validateAttribute(o1);
	}
	
	private boolean validateAttribute(Object o){
		if (o != null && o != TagData.REQUEST_TIME_VALUE) {
			try {
				int pageSize = Integer.valueOf((String) o).intValue();
				if (pageSize <= 0) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}
	

}
