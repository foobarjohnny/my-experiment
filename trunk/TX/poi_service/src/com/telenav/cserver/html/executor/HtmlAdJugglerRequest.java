/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
/**
 * @TODO	Define the request Object
 * @author  
 * @version 1.0 
 */
public class HtmlAdJugglerRequest  extends ExecutorRequest {
	private JSONObject actionJO;
	private String imageKey;
	private HtmlClientInfo clientInfo;
	private String userId;
	private JSONObject paramJSON;
	private String hostUrl;
	
	public String getHostUrl() {
		return hostUrl;
	}
	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}
	public JSONObject getParamJSON() {
		return paramJSON;
	}
	public void setParamJSON(JSONObject paramJSON) {
		this.paramJSON = paramJSON;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public HtmlClientInfo getClientInfo() {
		return clientInfo;
	}
	public void setClientInfo(HtmlClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}
	public String getImageKey() {
		return imageKey;
	}
	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}
	public JSONObject getActionJO() {
		return actionJO;
	}
	public void setActionJO(JSONObject actionJO) {
		this.actionJO = actionJO;
	}
}
