/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * @TODO	Define the response Object
 * @author  zhhyan@telenav.cn
 * @version 1.0 
 */
public class HtmlAboutResponse extends ExecutorResponse{
	private String dataSet;

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}
	
}
