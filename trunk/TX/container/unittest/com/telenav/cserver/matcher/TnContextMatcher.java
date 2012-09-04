package com.telenav.cserver.matcher;

import org.easymock.IArgumentMatcher;

import com.telenav.kernel.util.datatypes.TnContext;
public class TnContextMatcher implements IArgumentMatcher {

	private TnContext tnContext;

	public TnContextMatcher(TnContext tnContext) {
		this.tnContext = tnContext;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof TnContext)
			return true;
		
		return false;
	}

	@Override
	public void appendTo(StringBuffer arg0) {

	}

}
