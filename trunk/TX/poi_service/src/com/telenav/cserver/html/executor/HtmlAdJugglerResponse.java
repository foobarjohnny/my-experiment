/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.ExecutorResponse;
/**
 * @TODO	Define the response Object
 * @author  
 * @version 1.0 
 */
public class HtmlAdJugglerResponse extends ExecutorResponse {
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
