package com.telenav.cserver.poi.executor;

import com.telenav.cserver.backend.datatypes.contents.EditablePoi;
import com.telenav.cserver.framework.executor.ExecutorRequest;




public class EditPOIRequest extends ExecutorRequest 
{
	public EditablePoi poiToEdit= null;	
	private long useId =1l;
	private String categoryId;
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public long getUseId() {
		return useId;
	}
	public void setUseId(long useId) {
		this.useId = useId;
	}
	
	public String toString() {
		String poiToEditStr = poiToEdit != null ? poiToEdit.toString() : "";
		return "[poiToEdit] = " + poiToEditStr + "; [useId] = " + useId;
	}
}
