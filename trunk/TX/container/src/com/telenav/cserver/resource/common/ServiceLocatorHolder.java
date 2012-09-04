/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.resource.common;


import org.springframework.util.Assert;

import com.telenav.cserver.common.resource.AbstractResourceHolder;
import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.common.resource.SpringObjectNameAware;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * This class will deal with the Service locator file.
 * @author kwwang
 *
 */
public class ServiceLocatorHolder extends AbstractResourceHolder implements SpringObjectNameAware{

	@Override
	public ResourceContent createObject(String key, UserProfile profile,
			TnContext tnContext) {
		//FIXME:this assert should be replaced with our own one.
		Assert.notNull(profile, "UserProfile can not be null.");
		
		Object result=ResourceFactory.createResource(this, profile, tnContext);
		
		ResourceContent rc=new ResourceContent();
		rc.setObject(result);
		
		return rc;
	}

	@Override
	public String getObjectName() {
		return "basic_service_mapping";
	}

	

}
