/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axis2.AxisFault;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.html.datatypes.AdsOffer;
import com.telenav.cserver.html.datatypes.AmenityMapping;
import com.telenav.cserver.html.datatypes.ExtraProperty;
import com.telenav.cserver.html.datatypes.GasPrice;
import com.telenav.cserver.html.datatypes.HotelItem;
import com.telenav.cserver.html.datatypes.MenuItem;
import com.telenav.cserver.html.datatypes.PoiDetail;
import com.telenav.cserver.html.datatypes.TripAdvisor;
import com.telenav.cserver.html.datatypes.YelpReview;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.cserver.html.util.HtmlPoiUtil;
import com.telenav.cserver.util.WebServiceConfigurator;
import com.telenav.datatypes.content.ads.v10.Advertisement;
import com.telenav.datatypes.content.tnpoi.v10.ExternalPoiAttributesSchema;
import com.telenav.datatypes.content.tnpoi.v10.Offer;
import com.telenav.datatypes.content.tnpoi.v10.PoiDetails;
import com.telenav.datatypes.content.tnpoi.v10.TnPoi;
import com.telenav.datatypes.content.tnpoi.v20.ExternalPoi;
import com.telenav.datatypes.content.tnpoi.v20.ExternalPoiSource;
import com.telenav.datatypes.content.tnpoi.v20.PoiExtraAttributes;
import com.telenav.datatypes.content.tnpoi.v20.PoiMenu;
import com.telenav.datatypes.content.tnpoi.v20.PoiRichContent;
import com.telenav.datatypes.content.tnpoi.v20.PoiVendor;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.content.ads.v10.AdServiceOrganicAdsRequestDTO;
import com.telenav.services.content.ads.v10.AdServiceResponseDTO;
import com.telenav.services.content.v10.ContentInfoService;
import com.telenav.services.content.v10.ContentInfoServiceStub;
import com.telenav.services.content.v10.GetPoiDetailsRequest;
import com.telenav.services.content.v10.GetPoiDetailsResponse;
import com.telenav.services.content.v20.ContentInfoService20;
import com.telenav.services.content.v20.ContentInfoService20Stub;
import com.telenav.services.content.v20.GetPoiExtraAttributesRequest;
import com.telenav.services.content.v20.GetPoiExtraAttributesResponse;
import com.telenav.services.content.v20.GetPoiMenuRequest;
import com.telenav.services.content.v20.GetPoiMenuResponse;
import com.telenav.services.content.v20.GetPoiRichContentRequest;
import com.telenav.services.content.v20.GetPoiRichContentResponse;
import com.telenav.services.content.v20.MatchWithExternalPoiSourceRequest;
import com.telenav.services.content.v20.MatchWithExternalPoiSourceResponse;
import com.telenav.tnbrowser.util.Utility;
import com.telenav.ws.datatypes.common.Property;

/**
* @TODO	Implement specific business logic
* @author panzhang@telenav.cn
* @version 1.0 Feb 21, 2011
*
*/

public class HtmlPoiDetailServiceProxy {
	
	private static HtmlPoiDetailServiceProxy instance = new HtmlPoiDetailServiceProxy(); 
	//log
	private static final Logger logger = Logger.getLogger(HtmlPoiDetailServiceProxy.class);
	/**
	 * 
	 * @return
	 */
	public static HtmlPoiDetailServiceProxy getInstance()
	{
		return instance;
	}
	
	/**
	 * TODO	get Poi Details information
	 * @param poiRequest
	 * @param poiResponse
	 * @param tnContext
	 * @return
	 */
	public HtmlPoiDetailResponse getPoiDetailsNew(HtmlPoiDetailRequest poiRequest,HtmlPoiDetailResponse poiResponse,TnContext tnContext)
	{	
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getPoiDetails");
    	PoiDetail poiDetail = new PoiDetail();
    	poiResponse.setPoiDetail(poiDetail);
    	
    	com.telenav.services.content.v20.ContentInfoService20Stub stub = null;
		try {
			//set query conditions
			com.telenav.services.content.v20.GetPoiDetailsRequest getPoiDetailRequest = new com.telenav.services.content.v20.GetPoiDetailsRequest();
			getPoiDetailRequest.setClientName(HtmlConstants.clientName);
			getPoiDetailRequest.setClientVersion(HtmlConstants.clientVersion);
			getPoiDetailRequest.setContextString(tnContext.toContextString());
			getPoiDetailRequest.setTransactionId(HtmlPoiUtil.getTrxId());
			getPoiDetailRequest.setPoiId(poiRequest.getPoiId());
			//
			stub = this.getContentInfoService20();
			com.telenav.services.content.v20.GetPoiDetailsResponse  getPoiDetailResponse = stub.getPoiDetails(getPoiDetailRequest);
	        //
            if (getPoiDetailResponse != null)// set response param
            {
            	com.telenav.datatypes.content.tnpoi.v20.PoiDetails backendData = getPoiDetailResponse.getDetails();
            	if(backendData != null)
            	{
	            	poiDetail.setDescription(HtmlCommonUtil.getString(backendData.getDescription()));
	            	String priceRange =  HtmlCommonUtil.getString(backendData.getPriceRange());
	            	poiDetail.setPriceRange(String.valueOf(priceRange.length()));
	            	poiDetail.setBusinessHours(HtmlCommonUtil.getString(backendData.getBusinessHours()));
	            	//poiDetail.setBusinessHoursNote(backendData.getBusinessHoursNote());
	            	poiDetail.setOlsonTimezone(HtmlCommonUtil.getString(backendData.getOlsonTimezone()));
	            	//get poi Logo
	            	//poi->brand->category
	            	String logoName = HtmlCommonUtil.getString(backendData.getPoiLogo());
	        		if("".equals(logoName))
	        		{
	        			logoName = HtmlCommonUtil.getString(backendData.getBrandLogo());
	        		}
	        		if("".equals(logoName))
	        		{
	        			logoName = HtmlCommonUtil.getString(backendData.getCategoryLogo());
	        		}
	        		poiDetail.setLogoName(logoName);
	        		//get tab flags
	            	poiDetail.setHasPoiExtraAttributes(backendData.getHasPoiExtraAttributes());
	            	poiDetail.setHasPoiMenu(backendData.getHasPoiMenu());
	            	poiDetail.setHasTheater(HtmlPoiUtil.isTheater(poiRequest.getCategoryId()));
	            	poiDetail.setHasReview(backendData.getIsRatingEnabled());
	            	//
	            	//get vendor code
	            	PoiVendor[] vendors = backendData.getAdditionalVendors();
	            	if(vendors != null)
	            	{
	            		for(int i=0 ; i< vendors.length ; i++)
	            		{
	            			if(vendors[i] != null)
	            			{
		            			String vendorCode = HtmlCommonUtil.getString(vendors[i].getVendorCode());
		            			String vendorId = HtmlCommonUtil.getString(vendors[i].getVendorId());
	            				poiDetail.setVendorId(vendorId);
	            				poiDetail.setVendorCode(vendorCode);
	            				
		            			if(HtmlConstants.VENDOR_CODE_GASPRICE.equalsIgnoreCase(vendorCode))
		            			{
		            				poiDetail.setHasGasPrice(true);
		            			}
		            			else if(HtmlConstants.VENDOR_CODE_HOTEL.equalsIgnoreCase(vendorCode))
		            			{
		            				poiDetail.setHasHotel(true);
		            			}
		            			else if(HtmlConstants.VENDOR_CODE_OPENTABLE.equalsIgnoreCase(vendorCode))
		            			{
		            				poiDetail.setHasOpenTable(true);

		            			}
	            			}
	            		}
	            	}
	            	//
	            	poiResponse.setPoiDetail(poiDetail);
            	}
            }
            else
            {
            	poiResponse.setStatus(ExecutorResponse.STATUS_FAIL);
                cli.addData("warning", "getPoiDetailsNew EXCEPTION or Failed");
                cli.setState(CliConstants.STATUS_FAIL);
            }

            cli.addData("Response", "Status Code:" + getPoiDetailResponse.getResponseStatus());
           // nickNameResponse.setMessage("Status Code =" + statusCode + " Message=" + statusMsg);
        }catch (Exception e) {
			// TODO Auto-generated catch block
        	logger.error(e);
        	e.printStackTrace();
        	poiResponse.setStatus(ExecutorResponse.STATUS_FAIL);
            cli.setStatus(e);
            cli.setState(CliConstants.STATUS_FAIL);
		} 
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }
        
		return poiResponse;
	}
	/**
	 * TODO	getPoiDetails
	 * @param poiRequest
	 * @param poiResponse
	 * @param tnContext
	 * @return
	 */
	public HtmlPoiDetailResponse getPoiDetails(HtmlPoiDetailRequest poiRequest,HtmlPoiDetailResponse poiResponse,TnContext tnContext)
	{	
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getPoiDetails");
    	PoiDetail poiDetail = new PoiDetail();
    	
		ContentInfoServiceStub stub = null;
		try {
			//set query conditions
			GetPoiDetailsRequest getPoiDetailRequest = new GetPoiDetailsRequest();
			getPoiDetailRequest.setClientName(HtmlConstants.clientName);
			getPoiDetailRequest.setClientVersion(HtmlConstants.clientVersion);
			getPoiDetailRequest.setContextString(tnContext.toContextString());
			getPoiDetailRequest.setTransactionId(HtmlPoiUtil.getTrxId());
			getPoiDetailRequest.setPoiId(poiRequest.getPoiId());
			//
			stub = getService();
			GetPoiDetailsResponse  getPoiDetailResponse = stub.getPoiDetails(getPoiDetailRequest);
	        //
            if (getPoiDetailResponse != null)// set response param
            {
            	PoiDetails backendData = getPoiDetailResponse.getDetails();
            	if(backendData != null)
            	{
	            	
	            	poiDetail.setDescription(backendData.getDescription());
	            	String priceRange =  HtmlCommonUtil.getString(backendData.getPriceRange());
	            	poiDetail.setPriceRange(String.valueOf(priceRange.length()));
	            	poiDetail.setBusinessHours(HtmlCommonUtil.getString(backendData.getBusinessHours()));
	            	poiDetail.setOlsonTimezone(HtmlCommonUtil.getString(backendData.getOlsonTimezone()));
            	}
            }
            else
            {
            	poiResponse.setStatus(ExecutorResponse.STATUS_FAIL);
                cli.addData("warning", "getPoiDetails EXCEPTION or Failed");
                cli.setState(CliConstants.STATUS_FAIL);
            }

            cli.addData("Response", "Status Code:" + getPoiDetailResponse.getResponseStatus());
        }catch (Exception e) {
			// TODO Auto-generated catch block
        	logger.error(e);
        	e.printStackTrace();
        	poiResponse.setStatus(ExecutorResponse.STATUS_FAIL);
            cli.setStatus(e);
            cli.setState(CliConstants.STATUS_FAIL);
		} 
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }
        
    	poiResponse.setPoiDetail(poiDetail);
        
		return poiResponse;
	}
	/**
	 * TODO	getPoiExtra
	 * @param poiRequest
	 * @param poiResponse
	 * @param tnContext
	 * @return
	 */
	public HtmlPoiDetailResponse getPoiExtras(HtmlPoiDetailRequest poiRequest,HtmlPoiDetailResponse poiResponse,TnContext tnContext)
	{	
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getPoiExtras");
		List<ExtraProperty> extrasList = new ArrayList<ExtraProperty>();
		ContentInfoService20Stub stub = null;
		try {
			//set query conditions
			GetPoiExtraAttributesRequest getPoiExtrasRequest = new GetPoiExtraAttributesRequest();
			getPoiExtrasRequest.setClientName(HtmlConstants.clientName);
			getPoiExtrasRequest.setClientVersion(HtmlConstants.clientVersion);
			getPoiExtrasRequest.setContextString(tnContext.toContextString());
			getPoiExtrasRequest.setTransactionId(HtmlPoiUtil.getTrxId());
			getPoiExtrasRequest.setPoiId(poiRequest.getPoiId());
			//
			stub = getContentInfoService20();
			GetPoiExtraAttributesResponse  getPoiExtraResponse = stub.getPoiExtraAttributes(getPoiExtrasRequest);
	        //
            if (getPoiExtraResponse != null)// set response param
            {
            	PoiExtraAttributes extras = getPoiExtraResponse.getExtraAttributes();
            	if(extras != null)
            	{
            		Property[] attributes = extras.getAttributes();
            		if(attributes != null)
            		{

            			ExtraProperty item;
        				ArrayList<String> filterList =  HtmlPoiExtraHelper.getInstance().getExtraList();
            			for(int i=0;i<attributes.length;i++)
            			{
            				item = new ExtraProperty();
            				//add my code here
            				if(null != attributes[i].getValue() && null != attributes[i].getKey())
            				{
	            				if (filterList.contains(attributes[i].getKey().toLowerCase())) {
	            					item.setKey(attributes[i].getKey());
	                				item.setValue(attributes[i].getValue());
								}
	            				extrasList.add(item);
            				}
            			}
            		}
            	}
            }
            else
            {
            	poiResponse.setStatus(ExecutorResponse.STATUS_FAIL);
                cli.addData("warning", "getPoiExtras EXCEPTION or Failed");
                cli.setState(CliConstants.STATUS_FAIL);
            }

            cli.addData("Response", "Status Code:" + getPoiExtraResponse.getResponseStatus());
           // nickNameResponse.setMessage("Status Code =" + statusCode + " Message=" + statusMsg);
        }catch (Exception e) {
			// TODO Auto-generated catch block
        	logger.error(e);
        	e.printStackTrace();
        	poiResponse.setStatus(ExecutorResponse.STATUS_FAIL);
            cli.setStatus(e);
            cli.setState(CliConstants.STATUS_FAIL);
		} 
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }
        
		poiResponse.setExtras(extrasList);
		return poiResponse;
	}
	/**
	 * TODO	 check if Support Image Format
	 * @param imageName
	 * @return
	 */
	private boolean isSupportImageFormat(String imageName)
	{
		boolean isImageFormat = false;
		imageName = imageName.toLowerCase();
		if(imageName.endsWith(".png") || imageName.endsWith(".jpg") || imageName.endsWith(".jpeg")||imageName.endsWith(".gif"))
		{
			isImageFormat = true;
		}
		
		return isImageFormat;
	}
	/**
	 * TODO	getPoiMenu
	 * @param poiRequest
	 * @param poiResponse
	 * @param tnContext
	 * @return
	 */
	public HtmlPoiDetailResponse getPoiMenu(HtmlPoiDetailRequest poiRequest,HtmlPoiDetailResponse poiResponse,TnContext tnContext)
	{	
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getPoiMenu");
		MenuItem menu = new MenuItem();
		ContentInfoService20Stub stub = null;
		try {
			//set query conditions
			GetPoiMenuRequest getPoiMenuRequest = new GetPoiMenuRequest();
			getPoiMenuRequest.setClientName(HtmlConstants.clientName);
			getPoiMenuRequest.setClientVersion(HtmlConstants.clientVersion);
			getPoiMenuRequest.setContextString(tnContext.toContextString());
			getPoiMenuRequest.setTransactionId(HtmlPoiUtil.getTrxId());
			getPoiMenuRequest.setPoiId(poiRequest.getPoiId());
			//
			stub = getContentInfoService20();
			GetPoiMenuResponse  getPoiMenuResponse = stub.getPoiMenu(getPoiMenuRequest);
	        //
            if (getPoiMenuResponse != null)// set response param
            {
            	PoiMenu backendData = getPoiMenuResponse.getMenu();
            	if(backendData != null)
            	{
            		String menuText = HtmlCommonUtil.getString(backendData.getMenuText());
            		String imageName = HtmlCommonUtil.getString(backendData.getMediaServerKey());
            		cli.addData("data", "imageName name:" + imageName);
            		menu.setMenuText(menuText);
            		//poiResponse.setMenuText(menuText);
            		if("".equals(menuText))
            		{
            			//Check if it's image
            			if(isSupportImageFormat(imageName))
            			{
            				cli.addData("data", "call media server iamgeName:" + imageName + "&width=" + poiRequest.getMenuWidth() + "&height:" + poiRequest.getMenuHeight());
            				String menuImage = getLogoImage(imageName, poiRequest.getMenuWidth(), poiRequest.getMenuHeight());
            				//poiResponse.setMenuImage(menuImage);
            				menu.setMenuImage(menuImage);
            			}
            			
            		}
            	}
            }
            else
            {
            	poiResponse.setStatus(ExecutorResponse.STATUS_FAIL);
                cli.addData("warning", "getPoiMenu EXCEPTION or Failed");
                cli.setState(CliConstants.STATUS_FAIL);
            }

            cli.addData("Response", "Status Code:" + getPoiMenuResponse.getResponseStatus());
           // nickNameResponse.setMessage("Status Code =" + statusCode + " Message=" + statusMsg);
        }catch (Exception e) {
			// TODO Auto-generated catch block
        	logger.error(e);
        	e.printStackTrace();
        	poiResponse.setStatus(ExecutorResponse.STATUS_FAIL);
            cli.setStatus(e);
            cli.setState(CliConstants.STATUS_FAIL);
		} 
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }
        poiResponse.setMenu(menu);
		return poiResponse;
	}
	
	/**
	 * TODO	getPoiMenu
	 * @param poiRequest
	 * @param poiResponse
	 * @param tnContext
	 * @return
	 */
	public HtmlPoiDetailResponse getGasPrice(HtmlPoiDetailRequest poiRequest,HtmlPoiDetailResponse poiResponse,TnContext tnContext)
	{	
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getGasPrice");
        
		try {
			//
			List<GasPrice> priceList = new ArrayList<GasPrice>();
			poiResponse.setGasPriceList(priceList);
			PoiRichContent[] richContentArr = getPoiRichContent(HtmlConstants.VENDOR_CODE_GASPRICE,poiRequest.getPoiId());

			if( richContentArr == null || richContentArr.length <= 0 ){
				return poiResponse;
			}
			//[3] fill data to hotelItem
			PoiRichContent richContent = richContentArr[0];
			//Data Format
			//{"brand_name": "76","poi_id": 612345677,"gas_price": [{"gas_type": "REGULAR","price": 3.34},{"gas_type": "MID_GRADE","price": 3.74},{"gas_type": "PREMIUM","price": 3.94},{"gas_type": "DIESEL_FUEL","price": 3.66}]}
			JSONObject jsonAll = new JSONObject(richContent.getData());
			JSONArray jsonArray = new JSONArray(jsonAll.getString("gas_price"));
			GasPrice price = null;
			for(int i=0; i<jsonArray.length();i++)
			{
				JSONObject json = new JSONObject(jsonArray.get(i).toString());
				price = new GasPrice();
				
				price.setName(json.getString("gas_type"));
				price.setPrice(json.getString("price"));
				
				priceList.add(price);
			}
			
			//
        }catch (Exception e) {
			// TODO Auto-generated catch block
        	logger.error(e);
        	e.printStackTrace();
        	poiResponse.setStatus(ExecutorResponse.STATUS_FAIL);
            cli.setStatus(e);
            cli.setState(CliConstants.STATUS_FAIL);
		} 
        finally
        {
            cli.complete();
        }
        
		return poiResponse;
	}
	/**
	 * TODO	get Poi Rich Content
	 * @param vendor
	 * @param poiId
	 * @return
	 */
	private PoiRichContent[] getPoiRichContent(String vendor,long poiId)
	{
		PoiRichContent[] richContentArr = null;
		ContentInfoService20Stub stub = null;
		try {
			//[1] warp web service request
			String contextString = "";
			stub = this.getContentInfoService20();
			String[] vendorCodes = {vendor};
			GetPoiRichContentRequest request = new GetPoiRichContentRequest();
			request.setPoiId(poiId);
			request.setVendorCodes(vendorCodes);
			request.setClientName(HtmlConstants.clientName);
			request.setClientVersion(HtmlConstants.clientVersion);
			request.setContextString(contextString);
			request.setTransactionId(HtmlPoiUtil.getTrxId());
			request.setVendorCodes(vendorCodes);
		
			GetPoiRichContentResponse response = null;
			//[2] invoke and fetch web service response
			response = stub.getPoiRichContent(request);
		
			richContentArr = response.getRichContent();

			
		} catch (Throwable e) {
			if(e instanceof AxisFault){
				logger.error("can not get ContentInfoService20,please check your configuration.");
				logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
			}else{
				logger.error("can not get getPoiRichContent from backend server. this is a backend issue.");
				logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
			}
			e.printStackTrace();
			return null;
		} 
		finally {
			WebServiceUtils.cleanupStub(stub);
		}
		return richContentArr;
	}
	/**
	 * TODO	get Hotel Detail info
	 * @param poiId
	 * @return
	 */
	public HotelItem getHotelDetail(long poiId){
		HotelItem hotelItem = new HotelItem();
		
		try {
			PoiRichContent[] richContentArr = getPoiRichContent(HtmlConstants.VENDOR_CODE_HOTEL,poiId);

			if( richContentArr == null || richContentArr.length <= 0 ){
				return hotelItem;
			}
			//[3] fill data to hotelItem
			PoiRichContent richContent = richContentArr[0];
			JSONObject json = new JSONObject(richContent.getData());
			
			//[3.1] hotel basic info
			hotelItem.setPoiId(richContent.getPoiId());
			hotelItem.setPartnerPoiId(json.getLong("property_id"));
			hotelItem.setName(json.getString("property_name"));
			hotelItem.setBrandName(json.getString("brand_name"));
			hotelItem.setPhoneNumber(json.getString("property_phone"));
			hotelItem.setLocationDesc(json.getString("location_description"));
			hotelItem.setDescription(json.getString("property_description"));
			hotelItem.setLogo(json.getString("brand_image"));
			hotelItem.setStarRating(Integer.parseInt(json.getString("mobil_star_rating")));
			hotelItem.setTotalFloors(Integer.parseInt(json.getString("num_floors")));
			hotelItem.setTotalRooms(Integer.parseInt(json.getString("num_rooms")));
			hotelItem.setYearBuilt(json.getString("year_build"));
			hotelItem.setYearOfLastRenovation(json.getString("year_of_last_renov"));
			//[3.2]amenities
			Set<Integer> amenities = new HashSet<Integer>();
			JSONArray amenityArr = json.getJSONArray("amenity");
			String amenity_id="";
			int amenityGroup_id;
			for(int i = 0;i<amenityArr.length();i++){
				amenity_id = amenityArr.getJSONObject(i).getString("amenity_id");
				amenityGroup_id = AmenityMapping.getAmenityGroupId(amenity_id);
				if(amenityGroup_id != -1){//-1 represents that the amenity group does not exist
					amenities.add(amenityGroup_id);
				}
			}
			hotelItem.setAmenities(amenities);
			//[3.3] stop
			Stop stop = new Stop();
			stop.lat = (int)(Double.parseDouble(json.getString("latitude")) * HtmlConstants.DEGREE_MULTIPLIER);
			stop.lon = (int)(Double.parseDouble(json.getString("longitude")) * HtmlConstants.DEGREE_MULTIPLIER);
			stop.city = json.getString("city");
			stop.country = json.getString("country_code");
			stop.firstLine = json.getString("address1");
			stop.state = json.getString("state_code");
			stop.zip = json.getString("postal");
			
			hotelItem.setStop(stop);
			//[3.4] photo
			Map<String, Integer> photoMap = new HashMap<String, Integer>();
			JSONArray photoArr = json.getJSONArray("property_image");
			String imgPath,display_order;
			//3.4.1 generate map
			for(int i = 0;i<photoArr.length();i++){
				imgPath = photoArr.getJSONObject(i).getString("source_image_path");
				display_order = photoArr.getJSONObject(i).optString("display_order","");
				if(display_order == null || display_order.equals("")){//if display_order is abnormal, put it to last
					display_order = "99"; 
				}
				if(imgPath != null && !imgPath.equals("")){
					photoMap.put(imgPath, Integer.parseInt(display_order));
				}
			}
			
			//photoMap.put("/tnimages/logo/~Outback-Steakhouse.png",-1);//delete me when release
			//3.4.2 sort the map
			List<Map.Entry<String, Integer>> photoMapEntryList = new ArrayList<Map.Entry<String, Integer>>(photoMap.entrySet()); 
			Collections.sort(photoMapEntryList, new Comparator<Map.Entry<String, Integer>>() { 
				public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) { 
					return (o1.getValue() - o2.getValue()); 
				} 
			}); 
			hotelItem.setPhotos(photoMapEntryList);
			
		} catch (Throwable e) {
			logger.error(e);
			e.printStackTrace();
			return null;
		} 
		return hotelItem;
	}
	
	/**
	 * TODO	get Organic Ads
	 * @param request
	 * @param response
	 * @param tnContext
	 * @return
	 */
	public HtmlPoiDetailResponse getOrganicAds(HtmlPoiDetailRequest request,HtmlPoiDetailResponse response,TnContext tnContext)
	{	
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getOrganicAds");
		com.telenav.services.content.ads.v10.AdServiceStub stub = null;
		try {
			//set query conditions
			AdServiceOrganicAdsRequestDTO requestDTO = new AdServiceOrganicAdsRequestDTO();
			requestDTO.setClientName(HtmlConstants.clientName);
			requestDTO.setClientVersion(HtmlConstants.clientVersion);
			requestDTO.setContextString(tnContext.toContextString());
			requestDTO.setTransactionId(HtmlPoiUtil.getTrxId());
			long[] poiIds = new long[1];
			poiIds[0] = request.getPoiId();
			requestDTO.setPoiIds(poiIds);
			//
			stub = getAdServiceV10();
			AdServiceResponseDTO  responseDTO = stub.getOrganicAds(requestDTO);
	        //
            if (responseDTO != null)
            {
            	Advertisement[] advertisement = responseDTO.getAdvertisement();
            	if(advertisement != null && advertisement.length >0)
            	{
            		Advertisement item = advertisement[0];
            		if(item != null)
            		{
            			PoiDetail poiDetail = new PoiDetail();
            			poiDetail.setDescription(item.getMessage());
            			poiDetail.setAdSource(item.getAdSourceName());
            			poiDetail.setAdID(item.getSourceAdId());
            			response.setPoiDetail(poiDetail);
            			TnPoi tnPoi = item.getTnPoi();
            			
            			MenuItem menu = new MenuItem();
            			menu.setMenuText(advertisement[0].getMenu());
            			response.setMenu(menu);
            			if(tnPoi != null)
            			{
            				Offer[] offers = tnPoi.getOffers();
            				if(offers != null)
                    		{
                    			List<AdsOffer> offerList = new ArrayList<AdsOffer>();
                    			for(int i=0;i<offers.length;i++)
                    			{
                    				AdsOffer adsOffer = new AdsOffer();
                    				adsOffer.setDescription(offers[i].getDescription());
                    				adsOffer.setName(offers[i].getName());
                    				offerList.add(adsOffer);
                    			}
                    			response.setOfferList(offerList);
                    		}
            			}
            		}
            		
            	}
            }
            else
            {
            	response.setStatus(ExecutorResponse.STATUS_FAIL);
                cli.addData("warning", "getAdsBasic EXCEPTION or Failed");
                cli.setState(CliConstants.STATUS_FAIL);
            }

            cli.addData("Response", "Status Code:" + responseDTO.getResponseStatus());
           // nickNameResponse.setMessage("Status Code =" + statusCode + " Message=" + statusMsg);
        }catch (Exception e) {
			// TODO Auto-generated catch block
        	logger.error(e);
        	e.printStackTrace();
        	response.setStatus(ExecutorResponse.STATUS_FAIL);
            cli.setStatus(e);
            cli.setState(CliConstants.STATUS_FAIL);
		} 
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
		}
        
		return response;
	}
	
	public HtmlPoiReviewResponse getYelpAndTripAdvisorReview(HtmlPoiReviewRequest poiRequest, HtmlPoiReviewResponse poiResponse, TnContext tnContext)
	{
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("getYelpAndTripAdvisorReview");

		try {
			// set query conditions
			ContentInfoService20 server = getContentInfoService20();

			LinkedList<ExternalPoiSource> list = new LinkedList<ExternalPoiSource>();
			if (!(poiRequest.isTripAdvisorSupported() && HtmlPoiUtil.isHotel(poiRequest.getCategoryId() + "")) && poiRequest.isYelpSupported()) {
				list.offer(ExternalPoiSource.YELP);
			}
			if (poiRequest.isTripAdvisorSupported()) {
				list.offer(ExternalPoiSource.TRIPADVISOR);
			}
			ExternalPoiSource[] externalSources = new ExternalPoiSource[list.size()];
			list.toArray(externalSources);
			
			MatchWithExternalPoiSourceRequest request = new MatchWithExternalPoiSourceRequest();
			request.setClientName(HtmlConstants.clientName);
			request.setClientVersion(HtmlConstants.clientVersion);
			request.setContextString(tnContext.toContextString());
			request.setTransactionId(HtmlPoiUtil.getTrxId());
			request.setPoiId(poiRequest.getPoiId());
			request.setExternalPoiSource(externalSources);

			MatchWithExternalPoiSourceResponse response = server.matchWithExternalPoiSource(request);
			if (response != null && response.getExternalPoi() != null) {
				ExternalPoi[] externalPois = response.getExternalPoi();
				for (ExternalPoi externalPoi : externalPois) {
					if (externalPoi != null) {
						if (externalPoi.getSource() == ExternalPoiSource.YELP) {
							YelpReview yelp = new YelpReview();
							yelp.setYelpPoiId(externalPoi.getId());

							Property[] props = externalPoi.getAttributes();
							if (props != null) {
								Property yelpRatingProperty = null;
								Property yelpReviewCountProperty = null;
								for (Property prop : props) {
									if (prop != null && ExternalPoiAttributesSchema._YELP_RATING.equals(prop.getKey())) {
										yelpRatingProperty = prop;
									}
									else if (prop != null && ExternalPoiAttributesSchema._YELP_REVIEW_COUNT.equals(prop.getKey())) {
										yelpReviewCountProperty = prop;
									}
								}
								String avgRatingReviews = yelpRatingProperty.getValue();
								String yelpReviewCount = yelpReviewCountProperty.getValue();
								try {
									yelp.setAvgRatingReviews((int) (Float.parseFloat(avgRatingReviews) * 10));
									yelp.setReviewCount((int) (Float.parseFloat(yelpReviewCount)));
								}
								catch (NumberFormatException e) {
									cli.addData("Error", "Yelp average rating reviews format: " + avgRatingReviews);
									cli.addData("Error", "Yelp review count format: " + yelpReviewCount);
								}
							}
							poiResponse.setYelpReview(yelp);
						}
						else if (externalPoi.getSource() == ExternalPoiSource.TRIPADVISOR) {
							TripAdvisor tripAdvisor = new TripAdvisor();
							tripAdvisor.setId(externalPoi.getId());
							Property[] props = externalPoi.getAttributes();
							if (props != null) {
								Property tripAdvisorRatingProperty = null;
								Property tripAdvisorReviewCountProperty = null;
								for (Property prop : props) {
									if (prop != null && ExternalPoiAttributesSchema._TRIPADVISOR_RATING.equals(prop.getKey())) {
										tripAdvisorRatingProperty = prop;
									}
									else if (prop != null && ExternalPoiAttributesSchema._TRIPADVISOR_REVIEW_COUNT.equals(prop.getKey())) {
										tripAdvisorReviewCountProperty = prop;
									}
								}
								String avgRatingReviews = tripAdvisorRatingProperty.getValue();
								String reviewCount = tripAdvisorReviewCountProperty.getValue();
								try {
									tripAdvisor.setAvgRatingReviews((int) (Float.parseFloat(avgRatingReviews) * 10));
									tripAdvisor.setReviewCount((int) (Float.parseFloat(reviewCount)));
								}
								catch (NumberFormatException e) {
									cli.addData("Error", "Trip Advisor average rating reviews format: " + avgRatingReviews);
									cli.addData("Error", "Trip Advisor review count format: " + reviewCount);
								}
							}
							poiResponse.setTripAdvisor(tripAdvisor);
						}
					}
				}
			}
			else {
				cli.addData("warning", "No YelpAndTripAdvisor poi matched");
				cli.setState(CliConstants.STATUS_FAIL);
			}
		}
		catch (Exception e) {
			logger.error(e);
			cli.setStatus(e);
			cli.setState(CliConstants.STATUS_FAIL);
		}
		finally {
			cli.complete();
		}

		return poiResponse;

	}
	
	/**
	 * TODO	get web-service
	 * @return
	 * @throws AxisFault
	 */
	private com.telenav.services.content.ads.v10.AdServiceStub getAdServiceV10() throws AxisFault
	{
		//String endPoints = "http://192.168.86.11:8080/tnwsbb/services/AdService";
		String endPoints = WebServiceConfigurator.getUrlOfOrganicAdsSearch();
		com.telenav.services.content.ads.v10.AdServiceStub server = new com.telenav.services.content.ads.v10.AdServiceStub(HtmlCommonUtil.getWSContext(),endPoints);
		return server;
	}
	/**
	 * TODO	get web-service
	 * @return
	 * @throws AxisFault
	 */
	private ContentInfoServiceStub getService() throws AxisFault
	{
		//String endPoints = "http://contentinfo-ws.mypna.com/tnws/services/ContentInfoService?wsdl";
		String endPoints = WebServiceConfigurator.getUrlOfPoiDetail();
		ContentInfoServiceStub server = new ContentInfoServiceStub(HtmlCommonUtil.getWSContext(),endPoints);
		return server;
	}
	
	/**
	 * TODO	get web-service
	 * @return
	 * @throws AxisFault
	 */
	private ContentInfoService20Stub getContentInfoService20() throws AxisFault
	{
		//String endPoints = "http://10.224.74.164:8080/tnws/services/ContentInfoService20?wsdl";
		String endPoints = WebServiceConfigurator.getUrlOfPoiDetail20();
		ContentInfoService20Stub server = new ContentInfoService20Stub(HtmlCommonUtil.getWSContext(),endPoints);
		return server;
	}
	
	/**
	 * TODO	get Logo Image
	 * @param imageName
	 * @param width
	 * @param height
	 * @return
	 */
    public String getLogoImage(String imageName, String width,String height)
    {
    	String getLogImageUrl = WebServiceConfigurator.getUrlOfLogoImage();
        String url = MessageFormat.format(getLogImageUrl,imageName,width,height);
        
        if( logger.isDebugEnabled() )
        {
            logger.debug("get log image url: "+ url);
        }
        String result = "";
        HttpMethod method = new GetMethod(url);
        try{
            httpClient.executeMethod(method);
            byte[] imageBytes = method.getResponseBody();
            if (method.getStatusCode() == 200)
            {
                if (imageBytes != null && imageBytes.length != 0)
                {
                    result = Utility.byteArrayToBase64(imageBytes);
                }
            }
        }catch(IOException ex){
            logger.error("HtmlPoiDetailServiceProxy#getLogoImage: can't load image "+url, ex);
        }
        return result;
    }
    
    private static int maxConnPerHost = 10;
    private static int connectionTimeout = 5000;
    private static int soTimeout = 5000;
    private static HttpClient httpClient = null;
    static
    {
        MultiThreadedHttpConnectionManager mgr = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams connectionManagerParams = new HttpConnectionManagerParams();
        connectionManagerParams.setDefaultMaxConnectionsPerHost(maxConnPerHost);
        connectionManagerParams.setTcpNoDelay(true);
        connectionManagerParams.setStaleCheckingEnabled(true);
        connectionManagerParams.setLinger(0);

        mgr.setParams(connectionManagerParams);
        try
        {
            httpClient = new HttpClient(mgr);
        }
        catch (Exception e)
        {
            httpClient = new HttpClient();
        }

        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(soTimeout);
    }
}
