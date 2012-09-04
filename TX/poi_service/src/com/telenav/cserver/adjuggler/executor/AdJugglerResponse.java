package com.telenav.cserver.adjuggler.executor;

import com.telenav.cserver.framework.executor.ExecutorResponse;

public class AdJugglerResponse extends ExecutorResponse {
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
