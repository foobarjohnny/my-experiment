/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.resource.manager;

import org.junit.Test;

import junit.framework.Assert;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestWebPageMappingManager.java
 * 
 * @author njiang
 * @version 1.0 Jan 18, 2012
 */
public class TestWebPageMappingManager {

	public static final String LOGIN_SERVICE = "login";

	public static final String LOGIN_SERVICE_URL_ENTRY = "login.http";

	private static UserProfile userProfile = null;

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

	static {
		createUserProfile();
	}

	private TnContext tnContext = new TnContext();

	@Test
	public void testGetDomainUrl() {
		String url = WebPageMappingManager.getDomainUrl(userProfile, tnContext, LOGIN_SERVICE, LOGIN_SERVICE_URL_ENTRY);

		Assert.assertEquals("http://s-tn60-rim-login.telenav.com:8080", url);
	}

	@Test
	public void testGetDomainUrlWithWrongEntry() {
		String url = WebPageMappingManager.getDomainUrl(userProfile, tnContext, LOGIN_SERVICE, "wrong_entry");

		// wrong_entry not exist
		Assert.assertEquals("", url);
	}

	@Test
	public void testGetPoiDetailUrl() {
		String url = WebPageMappingManager.getPoiDetailUrl(userProfile, tnContext);

		Assert.assertTrue(url.startsWith("http://s-tn60-rim-poi.telenav.com:8080/poi_service/html/poidetail.do?"));
	}

	@Test
	public void testGetWelcomeUrl() {
		String url = WebPageMappingManager.getWelcomeUrl(userProfile, tnContext);

		Assert.assertEquals("http://s-tn60-rim-login.telenav.com:8080", url);
	}

	@Test
	public void testGetEmailLoginUrl() {
		String url = WebPageMappingManager.getEmailLoginUrl(userProfile, tnContext);

		Assert.assertEquals("http://s-tn60-rim-login.telenav.com:8080", url);
	}

	@Test
	public void testGetUpsellUrl() {
		String url = WebPageMappingManager.getUpsellUrl(userProfile, tnContext);

		Assert.assertEquals("http://s-tn60-rim-login.telenav.com:8080", url);
	}
}
