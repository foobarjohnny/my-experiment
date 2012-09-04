package com.telenav.cserver.poi.struts.util;

import org.json.me.JSONException;

import com.telenav.cserver.backend.datatypes.cose.Advertisement;
import com.telenav.cserver.poi.datatypes.Stop;
import com.telenav.cserver.poi.datatypes.BizPOI;
import com.telenav.cserver.poi.datatypes.POI;

import junit.framework.TestCase;

public class TestPoiUtil extends TestCase {
	public void testFunction() throws JSONException {
		PoiUtil.toTxNode(getPOI(), 1);
		System.out.println(PoiUtil.getXMLString("PoiUtil.getXMLString....."));
		System.out.println(PoiUtil.getString("PoiUtil.getString....."));
		System.out.println(PoiUtil.amend("PoiUtil.amend....."));
		System.out.println(PoiUtil.filterLastPara("PoiUtil.filterLastPara....."));
		System.out.println(PoiUtil.getLongFrom("123456000"));
		
	}

	public POI getPOI() {
		POI poi = new POI();
		poi.avgRating = 5;
		poi.popularity = 1;
		poi.ratingNumber = 5;
		poi.userPreviousRating = 5;
		poi.isRatingEnable = true;
		poi.priceRange = 5;
		poi.menu = "";
		BizPOI bizPoi = new BizPOI();
		Stop address = new Stop();
		address.lat = 3735237;
		address.lon = -12199984;
		address.city = "SANTA CLARA";
		address.label = "";
		address.state = "Ca";
		address.firstLine = "3755 EL CAMINO REAL";
		address.country = "US";
		address.zip = "94086";

		bizPoi.address = address;
		bizPoi.brand = "KFC";
		bizPoi.categoryId = "-1";
		bizPoi.distance = 5;

		String[] supplementalInfo = new String[2];
		supplementalInfo[0] = "000";
		supplementalInfo[1] = "111";
		poi.bizPoi.supplementalInfo = supplementalInfo;

		poi.bizPoi = bizPoi;

		Advertisement ad = new Advertisement();
		ad.setShortMessage("shot");
		ad.setAdSource("");
		ad.setMessage("message");
		ad.setSourceAdId("");
		poi.ad = ad;

		return poi;
	}
}
