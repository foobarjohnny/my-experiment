/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.util;

import org.apache.log4j.Category;

import com.telenav.client.dsm.ContextMgrService;
import com.telenav.client.dsm.ContextMgrStatus;
import com.telenav.client.dsm.Error;
import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.framework.Constants;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.management.jmx.BackendServerConstants;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.navstar.proxy.facade.NavPreference;
import com.telenav.navstar.proxy.facade.NavPreference.RouteStyle;
import com.televigation.proxycommon.ProxyAddress;

/**
 * BackendServerMonitorUtil
 * 
 * @author kwwang
 * 
 */
public class BackendServerMonitorUtil {
	public static Category logger = Category
			.getInstance(BackendServerMonitorUtil.class);

	public static UserProfile createUserProfile() {
	    UserProfile user = new UserProfile();
        user.setAudioFormat("amr");
        user.setAudioLevel("3");
        user.setBuildNumber("1001");
        user.setCarrier("ATT");
        user.setDeviceCarrier("ATT");
        user.setProgramCode("ATTNAVPROG");
        user.setDevice("9000");
        user.setEqPin("eq_pin");
        user.setGpsType("AGPS");
        user.setImageType("png");
        user.setLocale("en_US");
        user.setMin("4082453800");
        user.setPassword("3800");
        user.setUserId("468766");
        user.setPlatform("ANDROID");
        user.setProduct("ATT_NAV");
        user.setRegion("NA");
        user.setVersion("7.0.01");
        return user;
	}

	public static TnContext createTnContext() throws Exception {
		UserProfile userProfile = createUserProfile();
		TnContext tc = newTnContextBy(userProfile);
		if (logger.isDebugEnabled()) {
			logger.debug("tc:" + tc);
		}
		ContextMgrService cms = new ContextMgrService();
		ContextMgrStatus myStatus = null;
		myStatus = cms.updateContext(tc);

		if (myStatus == null || myStatus.getStatusCode() != Error.NO_ERROR) {
			logger.warn("DSM SERVER can not work.");
		}

		return tc;

	}

	public static TnContext newTnContextBy(UserProfile userProfile) {
		TnContext tc = new TnContext();

		String loginName = userProfile.getMin();
		String carrier = userProfile.getCarrier();
		String device = userProfile.getDevice();
		String product = userProfile.getPlatform();
		String version = userProfile.getVersion();
		String applicationName = userProfile.getProduct();

		tc.addProperty(TnContext.PROP_LOGIN_NAME, loginName);
		tc.addProperty(TnContext.PROP_CARRIER, carrier);
		tc.addProperty(TnContext.PROP_DEVICE, device);
		tc.addProperty(TnContext.PROP_PRODUCT, product);
		tc.addProperty(TnContext.PROP_VERSION, version);
		tc.addProperty("application", applicationName);// "application" should
		// defined in TnContext
		tc.addProperty("c-server class", Constants.CSERVER_CLASS);

		tc.addProperty(TnContext.PROP_REQUESTOR,
				TnContext.TT_REQUESTOR_TNCLIENT);

		return tc;
	}
	
	/**
	 * Create a stop txnode, use the addr of our company
	 * 
	 * @return
	 */
	public static TxNode createOrginalTxNode() {
		TxNode addr = new TxNode();
		addr.addValue(DataConstants.TYPE_STOP);
		addr.addValue(3737390);
		addr.addValue(-12199964);
		addr.addValue(0);
		addr.addValue(0);
		addr.addValue(1);
		addr.addValue(0);
		addr.addValue(0);

		addr.addMsg("");// label
		addr.addMsg("1130 KIFER RD");// firstline
		addr.addMsg("SUNNYVALE");// city
		addr.addMsg("CA");// state
		addr.addMsg("0");// sotp id
		addr.addMsg("94086");// zip
		addr.addMsg("US");// country

		return addr;
	}
	
	public static Stop createOrginalStop()
    {
        Stop stop = new Stop();
        stop.city = "SUNNYVALE";
        stop.state = "CA";
        stop.stopId = "0";
        stop.zip = "94086";
        stop.country = "US";
        stop.firstLine = "1130 KIFER RD";
        stop.label = "1130 KIFER RD";
        stop.lat = 3737390;
        stop.lon = -12199964;
        return stop;
    }

	public static double DM5ToDegree(int dm5) {
		return dm5 / BackendServerConstants.DEGREE_MULTIPLIER;
	}

	public static PoiSearchRequest createPoiRequest() {

		Stop stop = Stop.fromTxNode(createOrginalTxNode());
		Address anchor = new Address();
		anchor.setFirstLine(stop.firstLine);
		anchor.setLatitude(DM5ToDegree(stop.lat));
		anchor.setLongitude(DM5ToDegree(stop.lon));
		anchor.setCityName(stop.city);
		anchor.setState(stop.state);
		anchor.setCountry(stop.country);
		anchor.setPostalCode(stop.zip);

		UserProfile user = createUserProfile();

		PoiSearchRequest poiReq = new PoiSearchRequest();
		poiReq.setRegion(user.getRegion());
		poiReq.setAnchor(anchor);
		poiReq.setCategoryId(-1);
		poiReq.setCategoryVersion("YP50");
		poiReq.setHierarchyId(1);
		poiReq.setNeedUserPreviousRating(true);
		poiReq.setPoiQuery("KFC");
		poiReq.setPoiSortType(5);
		poiReq.setRadiusInMeter(7230);
		poiReq.setUserId(Long.parseLong(user.getUserId()));
		poiReq.setPageNumber(3);
		poiReq.setPageSize(30);
		poiReq.setOnlyMostPopular(false);
		poiReq.setAutoExpandSearchRadius(true);
		poiReq.setNeedUserGeneratePois(true);
		poiReq.setNeedSponsoredPois(true);

		return poiReq;
	}

	public static Address createAddress() {
		Address addr = new Address();
		addr.setFirstLine("1130 KIFER RD");
		addr.setCityName("SUNNYVALE");
		addr.setState("CA");
		addr.setPostalCode("94086");
		addr.setCountry("US");

		return addr;
	}
	
	public static NavPreference createNavPreference() {
        int routeStyle = 1; //fastest
        int routeSetting = NavPreference.AVOID_TRAFFIC;
        int getMFIDs = NavPreference.GETMFIDS_YES;
        RouteStyle navRouteStyle = RouteStyle.getRouteStyle(routeStyle);

        NavPreference preference = new NavPreference();
        preference.setStyle(navRouteStyle);
        preference.setFilters(routeSetting);
        preference.setSettingGetMFID(getMFIDs);
        return preference;
    }

    public static ProxyAddress createProxyAddress(String type) {
        if (type.equals(BackendServerConstants.DESTINATION))
            return convertStopToProxyAddress(Stop
                    .fromTxNode(createDestTxNode()));
        else
            return convertStopToProxyAddress(Stop
                    .fromTxNode(createOrginalTxNode()));
    }
    
    public static ProxyAddress convertStopToProxyAddress(Stop stop)
    {
        ProxyAddress pa = new ProxyAddress(stop.firstLine, stop.country, 
                stop.city, stop.state, stop.zip,
                convertToDegree(stop.lat), convertToDegree(stop.lon));
        return pa;
    }
    
    public static final double DEGREE_MULTIPLIER = 1.e5;
    public static double convertToDegree(int dm5)
    {
        return dm5 / DEGREE_MULTIPLIER;
    }

	/**
	 * Create a stop txnode, which is nearby our company,1130 KIFER RD
	 * 
	 * @return
	 */
	private static TxNode createDestTxNode() {
		TxNode addr = new TxNode();
		addr.addValue(DataConstants.TYPE_STOP);
		addr.addValue(3737389);
		addr.addValue(-12200299);
		addr.addValue(0);
		addr.addValue(0);
		addr.addValue(1);
		addr.addValue(0);
		addr.addValue(0);

		addr.addMsg("KIFER RD at SEMICONDUCTOR DR");// label
		addr.addMsg("KIFER RD at SEMICONDUCTOR DR");// firstline
		addr.addMsg("SUNNYVALE");// city
		addr.addMsg("CA");// state
		addr.addMsg("0");// sotp id
		addr.addMsg("94086");// zip
		addr.addMsg("US");// country

		return addr;
	}
	
	public static Stop createDestStop()
	{
	    Stop stop = new Stop();
	    stop.city = "SUNNYVALE";
	    stop.state = "CA";
	    stop.stopId = "0";
	    stop.zip = "94086";
	    stop.country = "US";
	    stop.firstLine = "KIFER RD at SEMICONDUCTOR DR";
	    stop.label = "KIFER RD at SEMICONDUCTOR DR";
	    stop.lat = 3737389;
	    stop.lon = -12200299;
	    return stop;
	}

	public static TxNode createMapTxNode() throws Exception {
		TxNode map = new TxNode();
		map.addMsg(createTnContext().toContextString());
		TxNode child = new TxNode();
		child.addValue(2);// type 2 vector map
		child.addValue(354318075546l);// tile id which is nearby our company
		child.addValue(2);// zoom
		map.addChild(child);
		return map;
	}
}
