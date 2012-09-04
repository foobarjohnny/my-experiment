package com.telenav.cserver.framework.util;

import org.junit.Assert;
import org.junit.Test;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UnittestUtil;

public class TestUrlUtil {

	@Test
	public void testGetHostAndPort() {
		String[] expect1 = { "172.16.214.46", "58080" };
		Assert.assertArrayEquals(expect1, UrlUtil.getHostAndPort("http://172.16.214.46:58080/poi_service"));

		String[] expect2 = { "172.16.214.46", "58080" };
		Assert.assertArrayEquals(expect2, UrlUtil.getHostAndPort("http://172.16.214.46:58080/"));

		String[] expect3 = { "172.16.214.46", "58080" };
		Assert.assertArrayEquals(expect3, UrlUtil.getHostAndPort("http://172.16.214.46:58080"));

		String[] expect4 = { "172.16.214.46", "80" };
		Assert.assertArrayEquals(expect4, UrlUtil.getHostAndPort("http://172.16.214.46/poi_service"));

		String[] expect5 = { "172.16.214.46", "80" };
		Assert.assertArrayEquals(expect5, UrlUtil.getHostAndPort("http://172.16.214.46"));
	}

	@Test
	public void testProcessUrl() {
		String tn6xrequest = "http://172.16.214.46/poi_service/touch60/goToJsp.do?pageRegion=NA&amp;jsp=PoiList";
		Assert.assertEquals(tn6xrequest, UrlUtil.appendClientInfo(tn6xrequest, null));

		UserProfile userProfile = new UserProfile();
		userProfile.setVersion("6.0.01");
		Assert.assertEquals(tn6xrequest, UrlUtil.appendClientInfo(tn6xrequest, userProfile));

		userProfile.setVersion("7.0.01");
		Assert.assertNull(UrlUtil.appendClientInfo(null, userProfile));

		UserProfile userProfile2 = UnittestUtil.createUserProfile();
		userProfile2.setVersion("7.0.1");
		Assert.assertTrue(UrlUtil.appendClientInfo(tn6xrequest, userProfile2).startsWith(
				"http://172.16.214.46/poi_service/touch60/goToJsp.do?pageRegion=NA&amp;jsp=PoiList"));

		String tn6xrequest2 = "http://172.16.214.46/poi_service/touch60/goToJsp.do";
		Assert.assertTrue(UrlUtil.appendClientInfo(tn6xrequest2, userProfile2).startsWith("http://172.16.214.46/poi_service/touch60/goToJsp.do?"));

		new UrlUtil(); // for code coverage
	}
}
