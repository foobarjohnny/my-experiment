/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.Map;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * MockTestHolder, this class is intended to load test.properties file, in device\ATTNAVPROG\IPHONE\7_0_01\ATT_NAV\default
 * @author kwwang
 *
 */
public class MockTestHolder extends AbstractResourceHolder {

	@Override
	public ResourceContent createObject(String key, UserProfile profile,
			TnContext tnContext) {
		Map map = (Map)ResourceFactory.createResource(this, profile, tnContext);
		ResourceContent rc=new ResourceContent();
		rc.setObject(map);
		return rc;
	}
	
	public String getLoadingPath(UserProfile profile,
			TnContext tc)
	{
		return userKeyMapping.get(getKey(profile, tc));
	}

}
