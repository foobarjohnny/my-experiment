package com.telenav.cserver.html.protocol;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.TestResponseFormatter;
import com.telenav.cserver.html.datatypes.ExtraProperty;
import com.telenav.cserver.html.datatypes.GasPrice;
import com.telenav.cserver.html.datatypes.PoiDetail;
import com.telenav.cserver.html.executor.HtmlPoiDetailResponse;
import com.telenav.cserver.html.util.HtmlConstants;

public class TestHtmlPoiDetailFormatter extends TestResponseFormatter{
	public void testParseBrowserResponse() {
		HtmlPoiDetailResponse rResponse = getHtmlPoiDetailResponse();
		HtmlPoiDetailFormatter formatter = new HtmlPoiDetailFormatter();
		try {
			formatter.parseBrowserResponse(httpRequest, rResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private HtmlPoiDetailResponse getHtmlPoiDetailResponse(){
		HtmlPoiDetailResponse response = new HtmlPoiDetailResponse();
		response.setOperateType(HtmlConstants.OPERATE_POIDETAIL_MAIN);
		PoiDetail detail = new PoiDetail();
		detail.setBusinessHours("1@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm");
		detail.setOlsonTimezone("0");
		detail.setDescription("Description");
		detail.setPriceRange("PriceRange");
		detail.setAdSource("TN");
		response.setPoiDetail(detail);
		return response;
	}
	
	public void testParseBrowserResponseForMainnewPoiRequest() {
		HtmlPoiDetailResponse rResponse = getHtmlPoiDetailResponseMainnewPoiRequest();
		HtmlPoiDetailFormatter formatter = new HtmlPoiDetailFormatter();
		try {
			formatter.parseBrowserResponse(httpRequest, rResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private HtmlPoiDetailResponse getHtmlPoiDetailResponseMainnewPoiRequest(){
		HtmlPoiDetailResponse response = new HtmlPoiDetailResponse();
		response.setOperateType(HtmlConstants.OPERATE_POIDETAIL_MAINNEW);
		PoiDetail detail = new PoiDetail();
		detail.setBusinessHours("1@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm");
		detail.setOlsonTimezone("0");
		detail.setDescription("Description");
		detail.setPriceRange("PriceRange");
		detail.setAdSource("TN");
		
		detail.setLogoName("");
		detail.setHasPoiMenu(true);
		detail.setHasPoiExtraAttributes(true);
		detail.setHasGasPrice(true);
		detail.setHasHotel(true);
		detail.setHasOpenTable(true);
		detail.setHasTheater(true);
		detail.setHasDeal(true);
		detail.setHasReview(true);
		detail.setVendorId("");
		response.setPoiDetail(detail);
		response.setPoiRequest(true);
		return response;
	}
	
	public void testParseBrowserResponseForMainnewNotPoiRequest() {
		HtmlPoiDetailResponse rResponse = getHtmlPoiDetailResponseMainnewNotPoiRequest();
		HtmlPoiDetailFormatter formatter = new HtmlPoiDetailFormatter();
		try {
			formatter.parseBrowserResponse(httpRequest, rResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private HtmlPoiDetailResponse getHtmlPoiDetailResponseMainnewNotPoiRequest(){
		HtmlPoiDetailResponse response = new HtmlPoiDetailResponse();
		response.setOperateType(HtmlConstants.OPERATE_POIDETAIL_MAINNEW);
		PoiDetail detail = new PoiDetail();
		detail.setBusinessHours("1@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm|2@9:00am@10:00pm");
		detail.setOlsonTimezone("0");
		detail.setDescription("Description");
		detail.setPriceRange("PriceRange");
		detail.setAdSource("TN");
		
		detail.setLogoName("");
		detail.setHasPoiMenu(true);
		detail.setHasPoiExtraAttributes(true);
		detail.setHasGasPrice(true);
		detail.setHasHotel(true);
		detail.setHasOpenTable(true);
		detail.setHasTheater(true);
		detail.setHasDeal(true);
		detail.setHasReview(true);
		detail.setVendorId("");
		response.setPoiDetail(detail);
		response.setPoiRequest(false);
		return response;
	}
	
	public void testParseBrowserResponseForExtra() {
		HtmlPoiDetailResponse rResponse = getHtmlPoiDetailResponseExtra();
		HtmlPoiDetailFormatter formatter = new HtmlPoiDetailFormatter();
		try {
			formatter.parseBrowserResponse(httpRequest, rResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private HtmlPoiDetailResponse getHtmlPoiDetailResponseExtra(){
		HtmlPoiDetailResponse response = new HtmlPoiDetailResponse();
		response.setOperateType(HtmlConstants.OPERATE_POIDETAIL_EXTRA);
		List<ExtraProperty> extra = new ArrayList<ExtraProperty>();
		ExtraProperty extraItem = new ExtraProperty();
		extraItem.setValue("");
		extraItem.setKey("");
		extra.add(extraItem);
		response.setExtras(extra);
		return response;
	}
	
	public void testParseBrowserResponseForGasprice() {
		HtmlPoiDetailResponse rResponse = getHtmlPoiDetailResponseGasprice();
		HtmlPoiDetailFormatter formatter = new HtmlPoiDetailFormatter();
		try {
			formatter.parseBrowserResponse(httpRequest, rResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private HtmlPoiDetailResponse getHtmlPoiDetailResponseGasprice(){
		HtmlPoiDetailResponse response = new HtmlPoiDetailResponse();
		response.setOperateType(HtmlConstants.OPERATE_POIDETAIL_GASPRICE);
		List<GasPrice> dataList = new ArrayList<GasPrice>();
		GasPrice itemObj = new GasPrice();
		itemObj.setName("");
		itemObj.setPrice("");
		dataList.add(itemObj);
		response.setGasPriceList(dataList);
		
		return response;
	}
}
