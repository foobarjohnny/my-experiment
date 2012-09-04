package com.telenav.cserver.matcher;

import org.easymock.IArgumentMatcher;
import org.json.me.JSONObject;
public class JSONObjectMatcher implements IArgumentMatcher {

	private JSONObject jSONObject;

	public JSONObjectMatcher(JSONObject o) {
		this.jSONObject = o;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof JSONObject)
			return true;
		
		return false;
	}

	@Override
	public void appendTo(StringBuffer arg0) {

	}

}
