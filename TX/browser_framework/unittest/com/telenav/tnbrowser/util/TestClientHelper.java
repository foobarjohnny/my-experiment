package com.telenav.tnbrowser.util;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.Assert;

import com.telenav.cserver.browser.datatype.DeviceConfig;
import com.telenav.cserver.browser.util.ClientHelper;

import junit.framework.TestCase;

public class TestClientHelper extends TestCase {

	private DataHandler handler;

	@Override
	protected void setUp() {
		HttpServletRequest request = new MockHttpServletRequest();
		handler = new DataHandler(request);

		Hashtable clientInfo = new Hashtable();
		clientInfo.put(DataHandler.KEY_CARRIER, "ATT");
		clientInfo.put(DataHandler.KEY_PLATFORM, "ANDROID");
		clientInfo.put(DataHandler.KEY_VERSION, "6.2.01");
		clientInfo.put(DataHandler.KEY_PRODUCTTYPE, "ATT_NAV");
		clientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");

		clientInfo.put(DataHandler.KEY_WIDTH, "480");
		clientInfo.put(DataHandler.KEY_HEIGHT, "800");
		clientInfo.put(DataHandler.KEY_LOCALE, "en_US");
		clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "480-800");
		clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "800-480");
		handler.setClientInfo(clientInfo);
	}

	public void testGetVersion() {
		Assert.assertEquals("6_0_01", ClientHelper.getVersion("6.0.01"));
		Assert.assertEquals("6_2_01", ClientHelper.getVersion("6.2.01"));
		Assert.assertEquals("6_4_01", ClientHelper.getVersion("6.4.01"));
	}

	public void testGetProduct() {
		Assert.assertEquals("ATT", ClientHelper.getProduct("ATT_NAV"));
		Assert.assertEquals("ATT", ClientHelper.getProduct("ATT_MAPS"));
	}

	public void testGetDevice() {
		DeviceConfig deviceConfig = new DeviceConfig();
		deviceConfig.setDefaultDeviceName("800x480_480x800");
		deviceConfig.setClosestDeviceName("800x480_480x800");
		deviceConfig.setRealDeviceName("genericTest");
		deviceConfig.setDeviceLevelConfigExist(true);
		deviceConfig.setScreenSizeLevelConfigExist(true);
		Assert.assertEquals("genericTest", ClientHelper.getDevice(handler, deviceConfig));

		deviceConfig.setDeviceLevelConfigExist(false);
		deviceConfig.setScreenSizeLevelConfigExist(false);
		Assert.assertEquals("800x480_480x800", ClientHelper.getDevice(handler, deviceConfig));

		deviceConfig.setDeviceLevelConfigExist(true);
		deviceConfig.setScreenSizeLevelConfigExist(false);
		Assert.assertEquals("genericTest", ClientHelper.getDevice(handler, deviceConfig));

		deviceConfig.setDeviceLevelConfigExist(false);
		deviceConfig.setScreenSizeLevelConfigExist(true);
		Assert.assertEquals("800x480_480x800", ClientHelper.getDevice(handler, deviceConfig));
	}

	public void testGetMessageI18NKey() {
		DeviceConfig deviceConfig = new DeviceConfig();
		deviceConfig.setDefaultDeviceName("800x480_480x800");
		deviceConfig.setClosestDeviceName("800x480_480x800");
		deviceConfig.setRealDeviceName("genericTest");
		deviceConfig.setDeviceLevelConfigExist(true);
		deviceConfig.setScreenSizeLevelConfigExist(true);

		Assert.assertEquals("ATT.ANDROID.6_2_01.ATT.genericTest.en_US", ClientHelper.getMessageI18NKey(handler, deviceConfig));
	}

	public void testGetImageKey() {
		DeviceConfig deviceConfig = new DeviceConfig();
		deviceConfig.setDefaultDeviceName("800x480_480x800");
		deviceConfig.setClosestDeviceName("800x480_480x800");
		deviceConfig.setRealDeviceName("genericTest");
		deviceConfig.setDeviceLevelConfigExist(true);
		deviceConfig.setScreenSizeLevelConfigExist(true);

		Assert.assertEquals("/ATT/ANDROID/6_2_01/ATT/genericTest/480x800", ClientHelper.getImageKey(handler, deviceConfig));

		deviceConfig.setDeviceLevelConfigExist(false);
		deviceConfig.setScreenSizeLevelConfigExist(false);
		Assert.assertEquals("/ATT/ANDROID/6_2_01/ATT/800x480_480x800/480x800", ClientHelper.getImageKey(handler, deviceConfig));
	}

	public void testGetImageKeyWithoutSize() {
		DeviceConfig deviceConfig = new DeviceConfig();
		deviceConfig.setDefaultDeviceName("800x480_480x800");
		deviceConfig.setClosestDeviceName("800x480_480x800");
		deviceConfig.setRealDeviceName("genericTest");
		deviceConfig.setDeviceLevelConfigExist(true);
		deviceConfig.setScreenSizeLevelConfigExist(true);

		Assert.assertEquals("/ATT/ANDROID/6_2_01/ATT/genericTest/", ClientHelper.getImageKeyWithoutSize(handler, deviceConfig));
	}

	public void testGetLayoutKeyWithDevice() {
		DeviceConfig deviceConfig = new DeviceConfig();
		deviceConfig.setDefaultDeviceName("800x480_480x800");
		deviceConfig.setClosestDeviceName("800x480_480x800");
		deviceConfig.setRealDeviceName("genericTest");
		deviceConfig.setDeviceLevelConfigExist(true);
		deviceConfig.setScreenSizeLevelConfigExist(true);

		Assert.assertEquals("ATT.ANDROID.6_2_01.ATT.genericTest", ClientHelper.getLayoutKeyWithDevice(handler, deviceConfig));
	}

	public void testGetHostURL() {
		String requestURL = "http://172.16.214.46:58080/poi_service/touch62/jsp/about/AboutAbout.jsp";
		String servletPath = "/touch62/jsp/about/AboutAbout.jsp";
		Assert.assertEquals("http://172.16.214.46:58080/poi_service", ClientHelper.getHostURL(requestURL, servletPath));
	}
	
	public void testGetCacheKey() {
		Assert.assertEquals("ATT_ANDROID_6_2_01_ATT_genericTest_480x800_800x480_en_US", ClientHelper.getCacheKey(handler));
	}
}
