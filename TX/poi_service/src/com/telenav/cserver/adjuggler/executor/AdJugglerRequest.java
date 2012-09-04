package com.telenav.cserver.adjuggler.executor;

import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.tnbrowser.util.DataHandler;

public class AdJugglerRequest  extends ExecutorRequest {
	private DataHandler handler;
	private JSONObject actionJO;
	private String imageKey;
	
	public String getImageKey() {
		return imageKey;
	}
	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}
	public DataHandler getHandler() {
		return handler;
	}
	public void setHandler(DataHandler handler) {
		this.handler = handler;
	}
	public JSONObject getActionJO() {
		return actionJO;
	}
	public void setActionJO(JSONObject actionJO) {
		this.actionJO = actionJO;
	}
}
