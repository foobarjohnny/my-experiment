package com.telenav.cserver.util;

import java.util.Date;

import org.easymock.EasyMock;
import org.json.me.JSONException;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestUtil;
import com.telenav.tnbrowser.util.DataHandler;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestTnUtil extends TestCase {
	public void testFunction() throws JSONException {
		System.out.println(TnUtil.convertToInt("123"));
		System.out.println(TnUtil.getString("PoiUtil.getString....."));
		System.out.println(TnUtil.getWeatherDisplayDate(new Date()));
		System.out.println(TnUtil.getShotWeekDesc(1));
		System.out.println(TnUtil.getLongWeekDesc(1));
		System.out.println(TnUtil.getWeatherUnit(""));

		System.out.println(TnUtil.getWeatherUnitForCelsius());
		System.out.println(TnUtil.getWeatherUnit(""));
		System.out.println(TnUtil.getDefaultCountry(
				com.telenav.cserver.util.TestUtil.getTnContext(),
				com.telenav.cserver.util.TestUtil.getProfile()));
		System.out.println(TnUtil
				.getPoiReivewFlag(com.telenav.cserver.util.TestUtil
						.getProfile()));
		System.out.println(TnUtil
				.getTouchFlag(com.telenav.cserver.util.TestUtil.getProfile()));
		System.out.println(TnUtil
				.getPoiFinderVersion(com.telenav.cserver.util.TestUtil
						.getProfile()));
		System.out.println(TnUtil.isAndroid(com.telenav.cserver.util.TestUtil
				.getProfile()));
		System.out.println(TnUtil.isRim(com.telenav.cserver.util.TestUtil
				.getProfile()));
		System.out.println(TnUtil.isDual(com.telenav.cserver.util.TestUtil
				.getProfile()));

		System.out.println(TnUtil.amend("<>"));
		System.out.println(TnUtil.flagAsBold("<>"));
		System.out.println(TnUtil.filterLastPara(""));
		System.out.println(TnUtil.convertToStop(TestUtil.getLocationJSON()));
		System.out.println(TnUtil.isMaps("ATT_MAPS"));
		System.out.println(TnUtil.isTN("TN"));
		System.out.println(TnUtil.isATT("ATT_MAPS"));
		System.out.println(TnUtil.isSprintUser("SprintPCS"));
		System.out.println(TnUtil.isBell_VMCUser("BellMOb"));
		System.out.println(TnUtil.isBoostNotTN60("6.1.01", "Boost"));
		System.out.println(TnUtil.isTMOUser("TMO_NAV"));
		System.out.println(TnUtil.isUsccUser("UN_MAPS"));

		System.out.println(TnUtil.isRogersCarrier(""));
		System.out.println(TnUtil.isVNUser("Rogers"));
		System.out.println(TnUtil.getXMLString("Verizon"));
		System.out.println(TnUtil.getCarrierForBannerAds("Verizon"));
		System.out.println(TnUtil.formatTemp(10.2135f));
		System.out.println(TnUtil.isValid(""));
		System.out.println(TnUtil.getTitleCase("xx\\sxx"));
		System.out.println(TnUtil.getArea(3737391, -12199926, 700).toString());

		TnUtil
				.getDSMDataFromCServer(com.telenav.cserver.util.TestUtil
						.getTnContext(), com.telenav.cserver.util.TestUtil
						.getProfile());
	}

	public void testGetUserProfile() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("7.0.01").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("Android").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_DEVICEMODEL))
				.andReturn("1").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_LOCALE))
				.andReturn("en_US").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("ATT").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE))
				.andReturn("ATT_NAV").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_REGION))
				.andReturn("0").anyTimes();
		EasyMock
				.expect(
						handler
								.getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH))
				.andReturn("480").anyTimes();
		EasyMock
				.expect(
						handler
								.getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT))
				.andReturn("800").anyTimes();
		PowerMock.replayAll();
		TnUtil.getUserProfile(handler);
		PowerMock.verifyAll();
	}

	public void testGetOptionParam() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_USERACCOUNT))
				.andReturn("4085057537").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_USERPIN))
				.andReturn("7537").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_USERID))
				.andReturn("7537").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("ATT").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("Android").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("7.0.01").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_DEVICEMODEL))
				.andReturn("1").anyTimes();
		PowerMock.replayAll();
		TnUtil.getOptionParam(handler);
		PowerMock.verifyAll();
	}

	public void testIsRogersANDROID() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("Rogers").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("ANDROID").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isRogersANDROID(handler));
		PowerMock.verifyAll();
	}

	public void testIsRogersANDROID62() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.0").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("Rogers").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("ANDROID").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isRogersANDROID62(handler));
		PowerMock.verifyAll();
	}

	public void testIsATTANDROID62() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.0").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("ATT").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("ANDROID").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isATTANDROID62(handler));
		PowerMock.verifyAll();
	}

	public void testIsATTRIM623() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.0").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("ATT").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("RIM").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isATTRIM623(handler));
		PowerMock.verifyAll();
	}

	public void testIsBoostRIM6001User() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.0.01").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("Boost").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("RIM").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isBoostRIM6001User(handler));
		PowerMock.verifyAll();
	}

	public void testIsSprintAndroid62() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.01").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("SprintPCS").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("ANDROID").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isSprintAndroid62(handler));
		PowerMock.verifyAll();
	}

	public void testIsBoostAndroid62() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.01").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("Boost").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("ANDROID").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isBoostAndroid62(handler));
		PowerMock.verifyAll();
	}

	public void testIsUSCCRIM62() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("USCC").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.01").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("RIM").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isUSCCRIM62(handler));
		PowerMock.verifyAll();
	}

	public void testIsUSCC62() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("USCC").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.01").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isUSCC62(handler));
		PowerMock.verifyAll();
	}

	public void testIsSprintRim62() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("SprintPCS").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.01").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("RIM").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isSprintRim62(handler));
		PowerMock.verifyAll();
	}

	public void testIsUsccAndroid62() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("USCC").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.01").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("ANDROID").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isUsccAndroid62(handler));
		PowerMock.verifyAll();
	}

	public void testIsTMOAndroidUser() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("T-Mobile").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("ANDROID").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isTMOAndroidUser(handler));
		PowerMock.verifyAll();
	}

	public void testIsTMORIM62() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("T-Mobile").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("RIM").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.01").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isTMORIM62(handler));
		PowerMock.verifyAll();
	}

	public void testIsTMOAndroid62() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("T-Mobile").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("ANDROID").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.01").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isTMOAndroid62(handler));
		PowerMock.verifyAll();
	}

	public void testIsTMOAndroidLiteUser() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("T-Mobile").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("ANDROID").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE))
				.andReturn("TMO_lite").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isTMOAndroidLiteUser(handler));
		PowerMock.verifyAll();
	}

	public void testIsVNAndroidUser() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("Verizon").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("ANDROID").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isVNAndroidUser(handler));
		PowerMock.verifyAll();
	}

	public void testIsVNRIM62() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("Verizon").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("RIM").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.01").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isVNRIM62(handler));
		PowerMock.verifyAll();
	}

	public void testIsVNAndroidFreeUser() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("Verizon").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("ANDROID").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE))
				.andReturn("VN_FREE").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isVNAndroidFreeUser(handler));
		PowerMock.verifyAll();
	}

	public void testIsBAWPAIDUser() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE))
				.andReturn("BAW_PAID").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("Verizon").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("RIM").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.01").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isBAWPAIDUser(handler));
		PowerMock.verifyAll();
	}

	public void testIsBAWUser() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE))
				.andReturn("BAW").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("Verizon").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("RIM").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.01").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isBAWUser(handler));
		PowerMock.verifyAll();
	}

	public void testIsCanadianCarrier() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("BellMob").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isCanadianCarrier(handler));
		PowerMock.verifyAll();
	}

	public void testIsBell_VMC() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("BellMob").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isBell_VMC(handler));
		PowerMock.verifyAll();
	}

	public void testIsBellCarrier() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("BellMob").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isBellCarrier(handler));
		PowerMock.verifyAll();
	}

	public void testIsVMCCarrier() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("VMC").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isVMCCarrier(handler));
		PowerMock.verifyAll();
	}

	public void testGetLoginFolder() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("Boost").anyTimes();
		PowerMock.replayAll();
		assertEquals("/boost", TnUtil.getLoginFolder(handler));
		PowerMock.verifyAll();
	}

	public void testIsBellANDROID62() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.01").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("BellMob").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("ANDROID").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isBellANDROID62(handler));
		PowerMock.verifyAll();
	}

	public void testIsVMCANDROID62() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_VERSION))
				.andReturn("6.2.01").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn("VMC").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_PLATFORM))
				.andReturn("ANDROID").anyTimes();
		PowerMock.replayAll();
		assertEquals(true, TnUtil.isVMCANDROID62(handler));
		PowerMock.verifyAll();
	}
}
