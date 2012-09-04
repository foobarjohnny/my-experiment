package com.telenav.cserver.backend.proxy.adjuggler;

import com.telenav.cserver.backend.proxy.HttpClientResponse;

public class AdjugglerResponse extends HttpClientResponse {
	private String jsonStr;

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public String toString() {
		return jsonStr;
	}
}
