/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;

/**
 * @TODO	Define the request Object
 * @author  zhhyan@telenav.cn
 * @version 1.0 
 */
public class HtmlAboutRequest extends ExecutorRequest {
	private String ssoToken;
	private HtmlClientInfo clientInfo;

	public String getSsoToken() {
		return ssoToken;
	}

	public void setSsoToken(String ssoToken) {
		this.ssoToken = ssoToken;
	}

	public HtmlClientInfo getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(HtmlClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}
	
}
