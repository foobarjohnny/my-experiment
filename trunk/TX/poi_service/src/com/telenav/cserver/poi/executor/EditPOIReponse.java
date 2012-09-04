package com.telenav.cserver.poi.executor;



import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.datatypes.POI;

public class EditPOIReponse extends ExecutorResponse 
{

	public String contentMgrStatusCode="";
	private POI poi;
	
	public POI getPoi() {
		return poi;
	}
	public void setPoi(POI poi) {
		this.poi = poi;
	}
	
}
