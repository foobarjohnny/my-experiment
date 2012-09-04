package com.telenav.cserver.matcher;

import org.easymock.IArgumentMatcher;

import com.telenav.cserver.common.resource.ResourceHolder;

public class ResourceHolderMatcher implements IArgumentMatcher {

	private ResourceHolder resourceHolder;

	public ResourceHolderMatcher(ResourceHolder resourceHolder) {
		this.resourceHolder = resourceHolder;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof ResourceHolder)
			return true;
		
		return false;
	}

	@Override
	public void appendTo(StringBuffer arg0) {

	}

}
