package com.telenav.cserver.html.protocol;

import com.telenav.cserver.TestResponseFormatter;
import com.telenav.cserver.html.datatypes.AdsDetail;
import com.telenav.cserver.html.executor.HtmlAdsResponse;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.j2me.datatypes.Stop;

public class TestHtmlAdsFormatter extends TestResponseFormatter{
	public void testParseBrowserResponse() {
		HtmlAdsResponse rResponse = getHtmlAdsResponse();
		HtmlAdsFormatter formatter = new HtmlAdsFormatter();
		try {
			formatter.parseBrowserResponse(httpRequest, rResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testParseBrowserResponseForfetchDetailData() {
		HtmlAdsResponse rResponse = getHtmlAdsResponseForfetchDetailData();
		HtmlAdsFormatter formatter = new HtmlAdsFormatter();
		try {
			formatter.parseBrowserResponse(httpRequest, rResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private HtmlAdsResponse getHtmlAdsResponse(){
		HtmlAdsResponse rResponse = new HtmlAdsResponse();
		rResponse.setOperateType(HtmlConstants.OPERATE_ADSVIEW_BASIC);
		Stop stop = new Stop();
		stop.firstLine="1130 kifer rd";
		AdsDetail detail = new AdsDetail();
		detail.setAddress(stop);
		rResponse.setAdsDetail(detail);
		return rResponse;
	}
	
	private HtmlAdsResponse getHtmlAdsResponseForfetchDetailData(){
		HtmlAdsResponse rResponse = new HtmlAdsResponse();
		rResponse.setOperateType(HtmlConstants.OPERATE_ADSVIEW_FETCH_DETAIL_DATA);
		Stop stop = new Stop();
		stop.firstLine="1130 kifer rd";
		AdsDetail detail = new AdsDetail();
		detail.setAddress(stop);
		rResponse.setAdsDetail(detail);
		return rResponse;
	}
}
