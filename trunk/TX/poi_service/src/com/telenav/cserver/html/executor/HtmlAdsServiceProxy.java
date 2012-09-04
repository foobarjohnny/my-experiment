/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.html.datatypes.AdsDetail;
import com.telenav.cserver.html.datatypes.AdsOffer;
import com.telenav.cserver.html.datatypes.MenuItem;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.cserver.html.util.HtmlPoiUtil;
import com.telenav.cserver.util.WebServiceConfigurator;
import com.telenav.datatypes.address.v30.Address;
import com.telenav.datatypes.ads.v20.AdBasicInfo;
import com.telenav.datatypes.ads.v20.AdDetail;
import com.telenav.datatypes.ads.v20.AdIdentifier;
import com.telenav.datatypes.ads.v20.Contact;
import com.telenav.datatypes.ads.v20.ContactType;
import com.telenav.datatypes.ads.v20.Media;
import com.telenav.datatypes.ads.v20.MediaType;
import com.telenav.datatypes.ads.v20.Menu;
import com.telenav.datatypes.ads.v20.Offer;
import com.telenav.datatypes.ads.v20.PoiIdentifierType;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.ads.v20.AdBasicInfoResponseDTO;
import com.telenav.services.ads.v20.AdDetailRequestDTO;
import com.telenav.services.ads.v20.AdDetailResponseDTO;
import com.telenav.services.ads.v20.AdService;
import com.telenav.services.ads.v20.AdServiceStub;

/**
 * @TODO	Implement specific business logic
 * @author panzhang@telenav.cn Feb 21, 2011
 * @version 1.0
 */

public class HtmlAdsServiceProxy
{

    private static HtmlAdsServiceProxy instance = new HtmlAdsServiceProxy();

    // log
    private static final Logger logger = Logger.getLogger(HtmlAdsServiceProxy.class);

    /**
     * 
     * @return
     */
    public static HtmlAdsServiceProxy getInstance()
    {
        return instance;
    }
    /**
     * @TODO	handle the poi id and put it into adsDetail
     * @param adsDetail
     * @param ds
     * @param poidataset
     */
    private void processPoiId(AdsDetail adsDetail,AdIdentifier ds,String poidataset)
    {
    	String poiid = "";
		PoiIdentifierType[] poiids = ds.getPoiIdentifier();
		if(poiids != null)
		{
    		for(int i=0;i<poiids.length;i++)
    		{
    			if(poiids[i] != null && poiids[i].getPoiProvider() != null)
    			{
	    			String dataset = HtmlCommonUtil.getString(poiids[i].getPoiProvider().getValue());
	    			if(poidataset.equalsIgnoreCase(dataset))
	    			{
	    				poiid = HtmlCommonUtil.getString(poiids[i].getPoiId());
	    				break;
	    			}
    			}
    		}
		}

		adsDetail.setPoiId(poiid);
    }
    /**
     * @TODO	get ad. basic information from AD server for VBB
     * @param request
     * @param response
     * @param tnContext
     * @return
     */
    public HtmlAdsResponse getAdsBasic(HtmlAdsRequest request, HtmlAdsResponse response, TnContext tnContext)
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getAdsBasic");
        long adId = request.getAdId();
        response.setAdId(adId);
        AdsDetail adsDetail = null;
        logger.debug("adsId:" + adId);
		AdServiceStub stub = null;
        try
        {
            stub = getAdService();
            AdDetailRequestDTO reqdbito = createAdDetailRequestDTO(adId, tnContext);
            AdBasicInfoResponseDTO adDetailResponse = stub.getAdBasicInfo(reqdbito);
            String poidataset = tnContext.getProperty(TnContext.PROP_POI_DATASET);
            if (adDetailResponse != null)// set response param
            {
                AdBasicInfo[] adDetail = adDetailResponse.getAd();

                if (adDetail != null && adDetail[0] != null)
                {
                	adsDetail = new AdsDetail();
                    adsDetail.setAdId(adId);
                    adsDetail.setPoiType(HtmlConstants.POITYPE_VBB);
                    adsDetail.setBrandName(HtmlCommonUtil.getString(adDetail[0].getBusinessName()));
                    // get the main description
                    adsDetail.setTagline(HtmlCommonUtil.getString(adDetail[0].getShortDescription()));
                    // get Ads Source
                    if(adDetail[0].getAdIdentifier() != null)
                    {
                    	adsDetail.setAdSource(HtmlCommonUtil.getString(adDetail[0].getAdIdentifier().getProviderCode()));
                    	processPoiId(adsDetail,adDetail[0].getAdIdentifier(),poidataset);
                    	
                    }
                    // get the logo image
                    String logoName = "";
                    if(adDetail[0].getAdMedia()!=null && adDetail[0].getAdMedia()[0]!=null){
                    	logoName = HtmlCommonUtil.getString(adDetail[0].getAdMedia()[0].getSourceUrl());
                    }
                    adsDetail.setLogoName(logoName);
                    
                    adsDetail.setAddress(wsAddressToAddress(adDetail[0].getAddress(), tnContext));
                    //set the Address label as brand name to fix bug http://jira.telenav.com:8080/browse/TN-2349
                    adsDetail.getAddress().label = adsDetail.getBrandName();
                }
            }
            else
            {
                response.setStatus(ExecutorResponse.STATUS_FAIL);
                cli.addData("warning", "getAdsBasic EXCEPTION or Failed");
                cli.setState(CliConstants.STATUS_FAIL);
            }
            cli.addData("Response", "Status Code:" + adDetailResponse.getResponseStatus().getStatusCode() + ",Status Message:" + adDetailResponse.getResponseStatus().getStatusMessage());
            // nickNameResponse.setMessage("Status Code =" + statusCode + " Message=" + statusMsg);
        }
        catch (Throwable e)
        {
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

        if(adsDetail == null)
        {
        	adsDetail = createEmptyData(adId,tnContext); 
        }
        
        response.setAdsDetail(adsDetail);
        
        return response;
    }
    /**
     * @TODO	create an empty ad. data to avoid error message on the page. 
     * @param adId
     * @param tnContext
     * @return
     */
    private AdsDetail createEmptyData(long adId,TnContext tnContext)
    {
    	AdsDetail adsDetail = new AdsDetail(); 
        adsDetail.setBrandName("");
        adsDetail.setAdId(adId);
        adsDetail.setTagline("");
        adsDetail.setAdSource("");
        adsDetail.setAddress(wsAddressToAddress(new Address(), tnContext));
        adsDetail.setLogoName("");
        
        return adsDetail;
    }
    /**
     * @TODO	convert wsAddress to Stop object.
     * @param wsAddr
     * @param context
     * @return
     */
    public static Stop wsAddressToAddress(Address wsAddr, TnContext context)
    {
        Stop address = new Stop();

        if (wsAddr == null)
        {
            return address;
        }

        address.lat = HtmlCommonUtil.convertToDM5(wsAddr.getLatitude());
        address.lon = HtmlCommonUtil.convertToDM5(wsAddr.getLongitude());

//        if (wsAddr.getCityName() == null || wsAddr.getPostalCode() != null)
//        {
//            RGCUtil util = new RGCUtil();
//            address = util.getCurrentLocation(address.lat, address.lon, context);
//            if (address.stopId == null || "".equals(address.stopId))
//                address.stopId = "0";
//        }
//        else
//        {
            //address.label = wsAddr.getLastLine() != null ? wsAddr.getLastLine().getStandardizedName() : "";
            address.firstLine = wsAddr.getStreetName() != null ? wsAddr.getStreetName().getStandardizedName() : "";
            address.city = wsAddr.getCityName() != null ? wsAddr.getCityName().getStandardizedName() : "";
            address.state = wsAddr.getState() != null ? wsAddr.getState().getStandardizedName() : "";
            address.zip = wsAddr.getPostalCode() != null ? wsAddr.getPostalCode() : "";
            address.country = wsAddr.getCounty() != null ? wsAddr.getCounty() : "";
            address.stopId = "0";
//        }

        return address;
    }

    /**
     * @TODO	get ad. detail information from AD server for VBB
     * @param request
     * @param response
     * @param tnContext
     * @return
     */
    public HtmlAdsResponse getAdsDetailData(HtmlAdsRequest request, HtmlAdsResponse response, TnContext tnContext)
    {

        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getAdsDetail");
        AdsDetail adsDetail = null;
        long adId = request.getAdId();
        response.setAdId(adId);
        AdServiceStub stub = null;
        try
        {
            stub = getAdService();
            AdDetailRequestDTO reqdbito = createAdDetailRequestDTO(adId, tnContext);
            AdDetailResponseDTO adDetailResponse = stub.getAdDetail(reqdbito);
            String poidataset = tnContext.getProperty(TnContext.PROP_POI_DATASET);
            if (adDetailResponse != null)// set response param
            {
                AdDetail[] adDetail = adDetailResponse.getAdDetail();
                if (adDetail != null && adDetail[0] != null)
                {
                    adsDetail = new AdsDetail();
                    adsDetail.setAdId(adId);
                    
                    adsDetail.setPoiType(HtmlConstants.POITYPE_VBB);
                    adsDetail.setBrandName(HtmlCommonUtil.getString(adDetail[0].getBusinessName()));
                    // get the main description
                    adsDetail.setTagline(HtmlCommonUtil.getString(adDetail[0].getLongDescription()));
                    // get Ads Source
                    if(adDetail[0].getAdIdentifier() != null)
                    {
                    	adsDetail.setAdSource(HtmlCommonUtil.getString(adDetail[0].getAdIdentifier().getProviderCode()));
                    	processPoiId(adsDetail,adDetail[0].getAdIdentifier(),poidataset);
                    }
                    // get the logo image
                    String logoName = "";
                    if(adDetail[0].getAdMedia()!=null && adDetail[0].getAdMedia()[0]!=null){
                    	logoName = HtmlCommonUtil.getString(adDetail[0].getAdMedia()[0].getSourceUrl());
                    }
                    adsDetail.setLogoName(logoName);
                    if(!request.isFromPoiDetail())
                    {
	                    adsDetail.setAddress(wsAddressToAddress(adDetail[0].getAddress(), tnContext));
	                    adsDetail.getAddress().label = adsDetail.getBrandName();
	                    // get the phoneNum
	                    Contact[] contacts = adDetail[0].getContact();
	                    if (contacts != null )
	                    {	String tempPhone = "";
	                    	for(Contact contact : contacts){
	                    		if(contact!=null){
	                        		ContactType type = contact.getType();
	                        		if(type==ContactType.PHONE && contact.getValue()!=null && !"".equals(contact.getValue())){
	                        			tempPhone = contact.getValue();
	                        			break;
	                        		}
	                    		}
	                    	}
	                        adsDetail.setPhoneNo(tempPhone);
	                    }else
	                    {
	                        adsDetail.setPhoneNo("");
	                    }
	
	                    adsDetail.setLogoImage(logoName);
                    }
 
                    // get the menu
                    Menu menu = adDetail[0].getMenu();
                    MenuItem menuItem = new MenuItem();
                    if (menu != null)
                    {
                    	adsDetail.setHasPoiMenu(true);
                    	String menuText = HtmlCommonUtil.getString(menu.getMenuText());
                    	menuItem.setMenuText(menuText.replaceAll("<bold>", "<b>").replaceAll("</bold>", "</b><br/>"));
                    }
                    else
                    {
                    	menuItem.setMenuText("");
                    }
                    
                    adsDetail.setMenu(menuItem);
                    
                    // get the deals
                    Offer[] offers = adDetail[0].getOffer();
                    if (offers != null)
                    {
                        List<AdsOffer> offerList = new ArrayList<AdsOffer>();
                        int size = offers.length;
                        if(size >0)
                        {
                        	adsDetail.setHasDeal(true);
                        }
                        
                        for (int i = 0; i < size; i++)
                        {
                            AdsOffer adsOffer = new AdsOffer();
                            adsOffer.setDescription(HtmlCommonUtil.getString(offers[i].getOfferDescription()));
                            adsOffer.setName(HtmlCommonUtil.getString(offers[i].getOfferName()));
                            Media[] medias = offers[i].getOfferMedia();
                            adsOffer.setDealImage(this.getDealImageByOfferMedia(medias));
//                            adsOffer.setDealImage("http://hqt-imageads-01.telenav.com/adSmallDealImage/1323200001355/upload_b7cd1df_1340c2d0f2b__7ffd_00000490.tmp");
                            offerList.add(adsOffer);
                        }
                        adsDetail.setOfferList(offerList);
                    }
                    // add the adsdetail to the response

                    cli.addData("Response", "Status Code:" + adDetailResponse.getResponseStatus().getStatusCode() + ",Status Message:" + adDetailResponse.getResponseStatus().getStatusMessage());
                }
            }
            else
            {
                response.setStatus(ExecutorResponse.STATUS_FAIL);
                cli.addData("warning", "getAdsBasic EXCEPTION or Failed");
                cli.setState(CliConstants.STATUS_FAIL);
            }
        }
        catch (Throwable e)
        {
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

        if(adsDetail == null)
        {
        	adsDetail = createEmptyData(adId,tnContext); 
        }
        
        response.setAdsDetail(adsDetail);
        
        return response;
    }
    /**
     * @TODO	get web-service object
     * @return
     * @throws AxisFault
     */
    private AdServiceStub getAdService() throws AxisFault
    {
       //String endPoints = "http://192.168.86.34:8080/tnws/services/AdServiceV20";
        String endPoints = WebServiceConfigurator.getUrlOfBillingBoard() ;
        AdServiceStub server = new AdServiceStub(HtmlCommonUtil.getWSContext(), endPoints);
        return server;
    }

    /**
     * @TODO	Just for test
     * @return
     */
    private AdDetailRequestDTO createAdDetailRequestDTO(long adid, TnContext tnContext)
    {
        AdDetailRequestDTO adDetailRequest = new AdDetailRequestDTO();
        adDetailRequest.setClientName(HtmlConstants.clientName);
        adDetailRequest.setClientVersion(HtmlConstants.clientVersion);
        adDetailRequest.setContextString(tnContext.toContextString());
        adDetailRequest.setTransactionId(HtmlPoiUtil.getTrxId());
        adDetailRequest.setAdId(adid);

        return adDetailRequest;
    }
    /**
     * @TODO	get deal image according to offer media
     * @param medias
     * @return
     */
    private String getDealImageByOfferMedia(Media[] medias){
		if(null != medias){
        	for(Media media : medias){
        		if(MediaType.DEAL_IMAGE_MEDIUM.equals(media.getType())){
        			return HtmlCommonUtil.getString(media.getSourceUrl());
        		}
        	}
        }
		
		return "";
	}
}
