package com.telenav.cserver.backend.proxy.zagat;

import java.io.InputStream;

import com.telenav.cserver.backend.proxy.HttpClientResponse;

public class ZagatResponse extends HttpClientResponse {
	InputStream input;
	String encoding;
	public InputStream getInput() {
		return input;
	}
	public void setInput(InputStream input) {
		this.input = input;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
