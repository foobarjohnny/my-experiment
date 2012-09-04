package com.telenav.cserver.resource.manager;

import junit.framework.Assert;

import org.junit.Test;

import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.resource.common.ServiceLocatorHolder;
import com.telenav.cserver.resource.datatypes.ServiceMapping;
import com.telenav.kernel.util.datatypes.TnContext;

public class TestServiceMappingManager {

	private static UserProfile userProfile = null;
	private static ServiceMapping serviceMapping = null;

	private static void createUserProfile() {
		userProfile = new UserProfile();
		userProfile.setProgramCode("ATTNAVPROG");
		userProfile.setProduct("ATT_NAV");
		userProfile.setPlatform("IPHONE");
		userProfile.setVersion("7.0.01");
		userProfile.setBuildNumber("7010000");
		userProfile.setLocale("en_US");
		userProfile.setRegion("NA");
		userProfile.setCarrier("ATT");
		userProfile.setDevice("test_device");
	}

	private static TnContext tnContext = new TnContext();

	private static void createServiceMapping() {
		ServiceLocatorHolder serviceLocatorHolder = (ServiceLocatorHolder) ResourceHolderManager.getResourceHolder(HolderType.SERVICE_LOCATOR_TYPE);
		ResourceContent rs = serviceLocatorHolder.getResourceContent(userProfile, tnContext);
		serviceMapping = (ServiceMapping) rs.getObject();
	}

	static {
		createUserProfile();
		createServiceMapping();
	}

	@Test
	public void testGetUrlByKey() {
		String url = ServiceMappingManager.getUrlByKey(serviceMapping, userProfile, tnContext, "poi");

		Assert.assertEquals("http://s-tn60-rim-poi.telenav.com:8080", url);
	}

	@Test
	public void testGetUrlByKeyWithNull() {
		String url = ServiceMappingManager.getUrlByKey(null, userProfile, tnContext, "poi");

		Assert.assertNull(url);

		String url2 = ServiceMappingManager.getUrlByKey(new ServiceMapping(), userProfile, tnContext, "poi");
		Assert.assertEquals("", url2);
	}

	@Test
	public void testGetUserBasedServiceLocator() {
		ServiceMapping mapping = ServiceMappingManager.getUserBasedServiceLocator(serviceMapping, userProfile, tnContext);

		Assert.assertNotNull(mapping);
	}

	@Test
	public void testGetServiceMapping() {
		ServiceMapping mapping = ServiceMappingManager.getServiceMapping(userProfile);

		Assert.assertNotNull(mapping);
	}
}
