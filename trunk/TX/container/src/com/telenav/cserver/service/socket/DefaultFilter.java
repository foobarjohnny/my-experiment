package com.telenav.cserver.service.socket;

import java.io.IOException;

import com.sun.grizzly.Context;
import com.sun.grizzly.ProtocolFilter;

public class DefaultFilter implements ProtocolFilter{

	public boolean execute(Context ctx) throws IOException {
		return false;
	}

	public boolean postExecute(Context ctx) throws IOException {
		return true;
	}

}
