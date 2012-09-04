/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.telenav.client.dsm.ContextMgrService;
import com.telenav.client.dsm.ContextMgrStatus;
import com.telenav.client.dsm.Error;
import com.telenav.cserver.backend.ace.GeoCodeResponse;
import com.telenav.cserver.backend.ace.GeoCodingProxy;
import com.telenav.cserver.backend.addresssharing.AddressSharingRequest;
import com.telenav.cserver.backend.addresssharing.AddressSharingResponse;
import com.telenav.cserver.backend.addresssharing.AddressSharingServiceProxy;
import com.telenav.cserver.backend.contents.ContentManagerServiceProxy;
import com.telenav.cserver.backend.contents.SaveReviewsRequest;
import com.telenav.cserver.backend.cose.PoiSearchProxy;
import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.backend.cose.PoiSearchResponse;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.ErrorCode;
import com.telenav.cserver.backend.datatypes.addresssharing.ContactInfo;
import com.telenav.cserver.backend.poicategory.PoiCategoryRequest;
import com.telenav.cserver.backend.poicategory.PoiCategoryResponse;
import com.telenav.cserver.backend.poicategory.PoiCategoryServiceProxy;
import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.backend.telepersonalize.CSCredentialType;
import com.telenav.cserver.backend.telepersonalize.EmailConfirmationRequest;
import com.telenav.cserver.backend.telepersonalize.EmailConfirmationResponse;
import com.telenav.cserver.backend.telepersonalize.TelepersonalizeProxy;
import com.telenav.cserver.backend.telepersonalize.UserProfileRequest;
import com.telenav.cserver.backend.telepersonalize.UserProfileResponse;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.backend.xnav.FetchBrandNamesRequest;
import com.telenav.cserver.backend.xnav.FetchBrandNamesResponse;
import com.telenav.cserver.backend.xnav.XnavServiceProxy;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.management.jmx.AbstractBackendServerMonitor;
import com.telenav.cserver.framework.management.jmx.DetectResult;
import com.telenav.cserver.framework.management.jmx.ExceptionUtil;
import com.telenav.cserver.framework.management.jmx.config.Monitor;
import com.telenav.cserver.framework.management.jmx.config.ServiceUrlParserFactory;
import com.telenav.cserver.framework.throttling.ThrottlingConfiguration;
import com.telenav.cserver.html.executor.HtmlAdsRequest;
import com.telenav.cserver.html.executor.HtmlAdsResponse;
import com.telenav.cserver.html.executor.HtmlAdsServiceProxy;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.cserver.html.util.HtmlPoiUtil;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.datatypes.carriers.v10.CarrierCode;
import com.telenav.datatypes.content.ads.v10.ImageSizeType;
import com.telenav.datatypes.content.ads.v10.SearchInformation;
import com.telenav.datatypes.content.cose.v10.CategoryParam;
import com.telenav.datatypes.services.v20.AppInformation;
import com.telenav.datatypes.services.v20.DeviceInformation;
import com.telenav.datatypes.services.v20.MobilePlatform;
import com.telenav.datatypes.services.v20.PageInformation;
import com.telenav.datatypes.services.v20.UserInformation;
import com.telenav.datatypes.user.management.v10.UserQueryBy;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.ads.banner.v10.BannerAdRequest;
import com.telenav.services.ads.banner.v10.BannerAdResponse;
import com.telenav.services.ads.banner.v10.BannerAdStub;
import com.telenav.services.content.ads.v10.AdServiceOrganicAdsRequestDTO;
import com.telenav.services.content.ads.v10.AdServiceResponseDTO;
import com.telenav.services.content.v10.ContentInfoServiceStub;
import com.telenav.services.content.v10.GetPoiDetailsRequest;
import com.telenav.services.content.v10.GetPoiDetailsResponse;
import com.telenav.services.search.onebox.v10.ClientNameEnum;
import com.telenav.services.search.onebox.v10.OneboxSearchRequest;
import com.telenav.services.search.onebox.v10.OneboxSearchResponse;
import com.telenav.services.search.onebox.v10.OneboxSearchServiceStub;
import com.telenav.services.user.management.v10.GetUserRequestDTO;
import com.telenav.services.user.management.v10.GetUserResponseDTO;
import com.telenav.services.user.management.v10.UserManagementServiceStub;
import com.telenav.tnbrowser.util.Utility;
import com.telenav.ws.datatypes.address.Location;
import com.telenav.ws.datatypes.content.weather.I18NWeatherServiceRequestDTO;
import com.telenav.ws.datatypes.content.weather.I18NWeatherServiceResponseDTO;
import com.telenav.ws.services.feedback.FeedbackServiceStub;
import com.telenav.ws.services.feedback.GetFeedbackQuestionsRequestDTO;
import com.telenav.ws.services.feedback.GetFeedbackQuestionsResponseDTO;
import com.telenav.ws.services.weather.WeatherServiceStub;
import com.telenav.xnav.user.UserTypeDef;

/**
 * CServerConfiguration.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 July 14, 2011
 * 
 */
public class BackendServerMonitor extends AbstractBackendServerMonitor {
	private static Logger logger = Logger.getLogger(BackendServerMonitor.class);
	
	private final static String DSM_SERVER = "Dsm";
    private final static String EMAIL_SERVER = "Email";
    private final static String USERPROFILE_MANAGEMENT_SERVER = "UserProfileManagement(getUserProfile)";
    private final static String FEEDBACK_SERVER = "Feedback(getFeedbackQuestions)";
    private final static String POICATEGORY_SERVER = "PoiCategory";
    private final static String SHAREADDRESS_SERVER = "ShareAddress";
    private final static String CONTENTMANAGER_SERVER = "ReviewService(getReviewOptions*)";
    private final static String POISEARCH_SERVER = "PoiSearch";
    private final static String ADVERTISE_SERVER = "Advertise(getBillBoardAds*)";
    private final static String XNAV_SERVER = "XNav(fetchBrandNames)";  
    private final static String WHETHER_SERVER = "Whether"; 
    private final static String BANNERADVERTISE_SERVER = "BannerAdvertise";
    private final static String ONEBOXSEARCH_SERVER = "OneBoxSearch";
    private final static String USER_MANAGEMENT_SERVER = "UserManagementService(getUserName)";
    private final static String LOGOIMAGE_SERVER = "LogoImage(7.0)";
    private final static String POIDETAIL_SERVER = "PoiDetail(7.0)";
    private final static String ORGANICADVERTISE_SERVER = "OrganicAdvertise";
    private final static String ACE_SERVER = "Ace";

    
    //the sample id will be computed by poi search, please call getSamplePoiId to get poi id 
    private long samplePoiId = 100;
    private boolean hasRunMonitorPoiSearch = false;
    
    public BackendServerMonitor()
    {
        // !!!make sure has initial all the class before invoking monitor
        WebServiceConfiguration.getInstance();
        ThrottlingConfiguration.getInstance();
        WebServiceConfigurator.class.getClass();
        BackendProxyManager.getBackendProxyFactory();
    }
    
    private long getSamplePoiId()
    {
        if( !hasRunMonitorPoiSearch  ){
            monitorPoiSearchServer();
            hasRunMonitorPoiSearch = true;
        }
        return samplePoiId;
    }
   
    
    @Monitor(server=DSM_SERVER, parserClass=ServiceUrlParserFactory.CLASS_PROPERTIES, filePath="config/commonServices.properties", serviceUrlKeys="dsmServiceUrl")
    private DetectResult monitorDSMServer()
    {
        DetectResult result = new DetectResult();

        TnContext tc = BackendServerMonitorUtil.newTnContextBy(BackendServerMonitorUtil.createUserProfile());

        ContextMgrService cms = new ContextMgrService();
        ContextMgrStatus myStatus = null;
        try
        {
            myStatus = cms.updateContext(tc);

            if (myStatus == null || myStatus.getStatusCode() != Error.NO_ERROR)
            {
                logger.warn("DSM SERVER can not work.");
                result.isSuccess = false;
                result.errCode = myStatus.getStatusCode();
                result.msg = "Can not get response from DSM SERVER.";
            }
            else
            {
                result.isSuccess = true;
            }
        }
        catch (Exception e)
        {
            logger.fatal("Can not get response from DSM SERVER.", e);
            result.isSuccess = false;
            result.msg = "Exception occurs when updateContext in DSM SERVER" + " \n Exception msg->" + ExceptionUtil.collectExceptionMsg(e);
        }

        return result;
    }
    
    @Monitor(server=EMAIL_SERVER, parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE, serviceUrlKeys="EMAILSERVICE")
    private DetectResult monitorEmailServer()
    {
        DetectResult result = new DetectResult();
        try
        {
            UserProfile userProfile = BackendServerMonitorUtil.createUserProfile();
            TnContext tc = BackendServerMonitorUtil.createTnContext();
            EmailConfirmationRequest request = new EmailConfirmationRequest();
            request.setContextString(tc.toContextString());
            request.setEmail("jhjin@telenav.cn");
            request.setEmailType(UserTypeDef.COMMON_EMAIL);
            request.setUserId(userProfile.getUserId());
            EmailConfirmationResponse response = TelepersonalizeProxy.getInstance().sendEmail(request, tc);
            if( response.isSuccess() )
            {
                result.isSuccess = true;
            }
            else
            {
                result.isSuccess = false;
                result.msg = response.getErrorMessage();
            }
            
        }
        catch(Exception ex)
        {   
            logger.fatal("#monitorEmailServer", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when sendMail()" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }
        return result;
    }
    
  
    @Monitor(server=USERPROFILE_MANAGEMENT_SERVER, parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE, serviceUrlKeys="TELEPERSONALIZATION")
    private DetectResult monitorUserProfileManagementServer()
    {
        DetectResult result = new DetectResult();

        try
        {
            TnContext tc = BackendServerMonitorUtil.createTnContext();

            UserProfileRequest userProfileRequest = new UserProfileRequest();
            userProfileRequest.setCredentialType(CSCredentialType.PTN);
            userProfileRequest.setCredentialId(BackendServerMonitorUtil.createUserProfile().getMin());

            UserProfileResponse response = TelepersonalizeProxy.getInstance().getUserProfile(userProfileRequest, tc);

            if (response != null && response.getUserStatus() != null && response.getUserStatus().getUserId() > 0)
            {
                result.isSuccess = true;
            }
            else
            {
                result.isSuccess = false;
                result.msg = "getUserProfile() fail.";
            }
        }
        catch (Exception ex)
        {
            logger.fatal("#monitorUserProfileManagementServer", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when getUserProfile" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }

        return result;
    }
    
    
    @Monitor(server=FEEDBACK_SERVER, parserClass=ServiceUrlParserFactory.CLASS_PROPERTIES, filePath="config/web_services.properties", serviceUrlKeys="URL_FEEDBACK_SERVER")
    private DetectResult monitorFeedbackServer()
    {
        DetectResult result = new DetectResult();

        FeedbackServiceStub stub = null;
        try
        {       
            stub = new FeedbackServiceStub(WebServiceConfigurator.getUrlOfFeedbackServer());
            GetFeedbackQuestionsRequestDTO requestDTO = new GetFeedbackQuestionsRequestDTO();

            // requestDTO.setTopic(topic);
            requestDTO.setLocale("en_US");
            requestDTO.setTopicString("fakeTopic");
            requestDTO.setCarrier(null);// (userProfile.getCarrier());
            requestDTO.setPlatform(null);// userProfile.getPlatform());
            requestDTO.setClientName("default");// ("c-server");
            requestDTO.setClientVersion("default");// (userProfile.getVersion());
            // requestDTO.setDevice(null);//(userProfile.getDevice());
            requestDTO.setTransactionId("default");// ("unknown");
            
            GetFeedbackQuestionsResponseDTO response = stub.getAllFeedbackQuestions(requestDTO);
            
            if ("OK".equals(response.getResponseStatus().getStatusCode()))
            {
                result.isSuccess = true;
            }
            else
            {
                result.isSuccess = false;
                result.msg = "responseCode = "+response.getResponseStatus().getStatusCode()+", responseMessage = "+response.getResponseStatus().getStatusMessage();
            }
        }
        catch (Exception e)
        {
            result.isSuccess = false;
            result.msg = "Error when getAllFeedbackQuestions. Exception occurs->" + e.getMessage();
        }
        finally
        {
            if( stub != null )
            {
                WebServiceUtils.cleanupStub(stub);
            }
        }

        return result;
    }
    
    @Monitor(server=POICATEGORY_SERVER, parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE, serviceUrlKeys="POICATEGORY")
    private DetectResult monitorPoiCategoryServer()
    {
        DetectResult result = new DetectResult();
        
        try
        {
            TnContext tc = BackendServerMonitorUtil.createTnContext();
            PoiCategoryRequest request = new PoiCategoryRequest();
            request.setStrContext(tc.toContextString());
            request.setCategoryId(0);
            request.setPoiHierarchyId(1);
            request.setVersion("YP50");
            PoiCategoryResponse response = PoiCategoryServiceProxy.getInstance().fetchPoiCategories(request, tc);
            if ( response.getCategories() != null && response.getCategories().length > 0 )
            {
                result.isSuccess = true;
            }
            else
            {
                result.isSuccess = false;
                result.msg = "statusCode = "+response.getStatusCode()+", statusMsg = "+response.getStatusMsg();
            }
        }
        catch(Exception e)
        {
            logger.fatal("fetch poi category tree", e);
            result.isSuccess = false;
            result.msg = "Exception occurs when get poi category tree" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(e);
        }
        
        return result;
    }
    

    /* used in ShareAddressExecutor */
    @Monitor(server=SHAREADDRESS_SERVER, parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE, serviceUrlKeys="ADDRESSSHARING")
    private DetectResult monitorShareAddressServer()
    {
        DetectResult result = new DetectResult();
        
        
        try {
            UserProfile userProfile = BackendServerMonitorUtil.createUserProfile();
            TnContext tc = BackendServerMonitorUtil.createTnContext();
            
            AddressSharingRequest addressSharingRequest = new AddressSharingRequest();
            addressSharingRequest.setUserId(PoiUtil.getLongFrom(userProfile.getUserId()));
            addressSharingRequest.setPtn(userProfile.getMin());
            addressSharingRequest.setContextString(tc.toContextString());
            addressSharingRequest.setAddress(BackendServerMonitorUtil.createStop());
            
            ContactInfo contact = new ContactInfo();
            contact.setName("test");
            contact.setPtn(userProfile.getMin());
            List<ContactInfo> contactlist = new ArrayList<ContactInfo>();
            contactlist.add(contact);
            addressSharingRequest.setContactList(contactlist);
            
            AddressSharingResponse addressSharingResponse = AddressSharingServiceProxy.getInstance().shareAddress(addressSharingRequest, tc);
            String statusCode = addressSharingResponse.getStatusCode();
            String statusMessage = addressSharingResponse.getStatusMessage();
            
            if ("OK".equalsIgnoreCase(statusCode)) {
                result.isSuccess = true;
            } else {
                result.isSuccess = false;
                result.msg = "statusCode = "+statusCode+", statusMessage = "+statusMessage;
            }
        }
        catch (Exception ex) {
            logger.fatal("#monitorShareAddressServer", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when share address" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }
        
        return result;
    }
    

    /* used in HtmlPoiReviewExecutor */
    @Monitor(server=CONTENTMANAGER_SERVER, parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE, serviceUrlKeys="CONTENT_MANAGER")
    private DetectResult monitorContentManagerServer()
    {
        DetectResult result = new DetectResult();
        
        try {
            SaveReviewsRequest request = new SaveReviewsRequest();
            request.setCategoryId(1);
            request.setPoiId(getSamplePoiId());
            ContentManagerServiceProxy.getInstance().getReviewOptions(request, BackendServerMonitorUtil.createTnContext());
            //no exception means ok
            result.isSuccess = true;
        }
        catch (Exception ex) {
            logger.fatal("#monitorContentManagerServer", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when get aggregated rating" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }
        
        return result;
    }
    

    @Monitor(server=POISEARCH_SERVER, parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE, serviceUrlKeys="POI_SEARCH")
    private DetectResult monitorPoiSearchServer()
    {       
        DetectResult result = new DetectResult();
        
        try {
            UserProfile userProfile = BackendServerMonitorUtil.createUserProfile();
            
            PoiSearchProxy proxy = new PoiSearchProxy(new TnContext());
   
            PoiSearchRequest poiReq = new PoiSearchRequest();
            poiReq.setRegion(userProfile.getRegion());
            Address anchor = new Address();
            anchor.setLatitude(37.37453);
            anchor.setLongitude(-121.99983);
            poiReq.setAnchor(anchor);
            poiReq.setCategoryId(-1);
            poiReq.setCategoryVersion("YP50");
            poiReq.setHierarchyId(1);
            poiReq.setNeedUserPreviousRating(false);
            poiReq.setPoiQuery("coff");
            //poiReq.setPoiSortType();
            poiReq.setRadiusInMeter(7000);
            poiReq.setUserId(Long.parseLong(userProfile.getUserId()));
            poiReq.setPageNumber(0);
            poiReq.setPageSize(10);
            poiReq.setMaxResult(10);
            poiReq.setOnlyMostPopular(false);
            poiReq.setAutoExpandSearchRadius(true);
            //poiReq.setSponsorListingNumber(poiRequest.getSponsorListingNumber());
            poiReq.setNeedUserGeneratePois(true);
            poiReq.setNeedSponsoredPois(true);
            
            poiReq.setTransactionId(System.currentTimeMillis()+"");
            
            PoiSearchResponse response = proxy.searchWithinDistance(poiReq);
            
            if(response != null && response.getPoiSearchStatus() == ErrorCode.POI_STATUS_SUCCESS)
            {
                if( response.getPois().size() != 0 )
                {
                    result.isSuccess = true;
                    this.samplePoiId = response.getPois().get(0).getPoiId();
                }
                else
                {
                    result.isSuccess = false;
                    result.msg = "Status is successful. But response don't contain any poi";
                }
            }
            else
            {
                result.isSuccess = false;
                result.msg = response == null ? "response is null" : "StatusCode = "+response.getPoiSearchStatus();
            }
        }
        catch (Exception ex) {
            logger.fatal("#monitorPoiSearchServer", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when search poi" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }
        
        return result;
    }
    
    /* used in HtmlPoiReviewExecutor */
    @Monitor(server=ADVERTISE_SERVER, parserClass=ServiceUrlParserFactory.CLASS_PROPERTIES, filePath="config/web_services.properties", serviceUrlKeys="URL_BILLING_BOARD")
    private DetectResult monitorAdvertiseServer()
    {
        DetectResult result = new DetectResult();
        
        try {
            HtmlAdsRequest request = new HtmlAdsRequest();
            HtmlAdsResponse response = new HtmlAdsResponse();
            TnContext tc = new TnContext();
            HtmlAdsServiceProxy.getInstance().getAdsBasic(request, response, tc);
            //no exception is ok
            result.isSuccess = true;
        }
        catch (Exception ex) {
            logger.fatal("#monitorAdvertiseServer", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when get Bill borads ads" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }
        
        return result;
    }
    
    @Monitor(server=XNAV_SERVER, parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE, serviceUrlKeys="XNAV")
    private DetectResult monitorXnavServer()
    {
        DetectResult result = new DetectResult();
        
        FetchBrandNamesRequest fetchBrandReq = new FetchBrandNamesRequest();

        fetchBrandReq.setCount(10);
        fetchBrandReq.setCountry("US");
        
        try
        {
            FetchBrandNamesResponse fetchBrandResp = XnavServiceProxy.getInstance().fetchBrandNames(fetchBrandReq,  BackendServerMonitorUtil.createTnContext());
            List<String> nameList = fetchBrandResp.getBrandNames();
            
            if( nameList != null && nameList.size() > 0 )
            {
                result.isSuccess = true;
            }
            else
            {
                result.isSuccess = false;
                result.msg = "can't get any brand names.";
            }
        }
        catch (Exception e)
        {
            logger.fatal("getHotBrandName", e);
            result.isSuccess = false;
            result.msg = "Exception occurs when get brand anems" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(e);
        }
        
        return result;
    }
    
    @Monitor(server=WHETHER_SERVER, parserClass=ServiceUrlParserFactory.CLASS_PROPERTIES, filePath="config/web_services.properties", serviceUrlKeys="URL_WEATHER")
    private DetectResult monitorWhetherServer()
    {
        DetectResult result = new DetectResult();
        
        WeatherServiceStub stub = null;
        try
        {
            I18NWeatherServiceRequestDTO request = new I18NWeatherServiceRequestDTO();
            request.setClientName("6x-cserver");
            request.setClientVersion("1.0");
            request.setTransactionId("unknown");
            
            Location location = new Location();
            com.telenav.ws.datatypes.address.GeoCode latLon = new com.telenav.ws.datatypes.address.GeoCode();
            latLon.setLatitude(37.37515);
            latLon.setLongitude(-121.99769);
            location.setGeoCode(latLon);
            request.setLocation(location);
            
            request.setNumberOfDays(2);

            stub = new WeatherServiceStub(WebServiceConfigurator.getUrlOfWeather());
            I18NWeatherServiceResponseDTO response = stub.getI18NCurrentNForecast(request);
            String statusCode = response.getResponseStatus().getStatusCode();
            String statusMessage = response.getResponseStatus().getStatusMessage();
            if("OK".equals(statusCode) )
            {
                result.isSuccess = true;
            }
            else
            {
                result.isSuccess = false;
                result.msg = "StatusCode = "+statusCode+", StatusMessage = "+statusMessage;
            }

        }
        catch (Exception e)
        {
            logger.fatal("#monitorWhetherServer", e);
            result.isSuccess = false;
            result.msg = "Exception occurs when getI18NCurrentNForecast" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(e);
        }
        finally
        {
            if( stub != null )
            {
                WebServiceUtils.cleanupStub(stub);
            }
        }
        return result;
    }
    
//    @Monitor(server=BANNERADVERTISE_SERVER, parserClass=ServiceUrlParserFactory.CLASS_PROPERTIES, filePath="config/web_services.properties", serviceUrlKeys="URL_BANNER_ADS")
//    private DetectResult monitorBannerAdvertiseServer()
//    {
//        DetectResult result = new DetectResult();
//        
//        BannerAdStub stub = null;
//        try
//        {
//            BannerAdRequest soapRequest = new BannerAdRequest();
//            UserProfile userProfile = BackendServerMonitorUtil.createUserProfile();
//
//            UserInformation userInfo = new UserInformation();
//            userInfo.setPtn(userProfile.getMin());
//            userInfo.setUserId(userProfile.getUserId());
//
//            AppInformation appInfo = new AppInformation();
//            appInfo.setAppName(userProfile.getProduct());
//            appInfo.setAppVersion(userProfile.getVersion());
//
//            DeviceInformation deviceInfo = new DeviceInformation();
//            String carrier = TnUtil.getCarrierForBannerAds(userProfile.getCarrier());
//            deviceInfo.setCarrier(CarrierCode.Factory.fromValue(carrier));
//            deviceInfo.setPlatform(MobilePlatform.Factory.fromValue(userProfile.getPlatform()));
//            deviceInfo.setModel(userProfile.getDevice());
//
//            PageInformation pageInfo = new PageInformation();
//            pageInfo.setPageId("1");
//            // use String index of Integer per backend's request
//            pageInfo.setPageIndex("1");
//
//            SearchInformation searchInfo = new SearchInformation();
//            
//            CategoryParam category = new CategoryParam();
//            category.setCategoryHierarchyId(1L);
//            category.setCategoryVersion("YP50");
//            category.setCategoryId(new long[] { -1 });
//            
//            searchInfo.setCategory(category);
//            searchInfo.setSearchId("-1");
//            searchInfo.setSearchString("");
//
//            soapRequest.setClientName("client");
//            soapRequest.setClientVersion("6.0.01");
//            soapRequest.setTransactionId("transaction1");
//            // encrypt before sending to 3rd party
//            soapRequest.setDeviceId(CipherUtil.encrypt(userProfile.getUserId()));
//            soapRequest.setUserAgent("NATIVE_BROWSER_USER_AGENT");
//
//            Location location = new Location();
//            com.telenav.ws.datatypes.address.GeoCode latLon = new com.telenav.ws.datatypes.address.GeoCode();
//            latLon.setLatitude(37.37515);
//            latLon.setLongitude(-121.99769);
//            location.setGeoCode(latLon);
//
//            soapRequest.setSearchLocation(location);
//            
//            soapRequest.setUserInfo(userInfo);
//            soapRequest.setAppInfo(appInfo);
//            soapRequest.setDeviceInfo(deviceInfo);
//            soapRequest.setPageInfo(pageInfo);
//            ImageSizeType maxSize = new ImageSizeType();
//            maxSize.setHeight(400);
//            maxSize.setWidth(800);
//            soapRequest.setMaxImageSize(maxSize);
//            soapRequest.setMinImageSize(maxSize);
//            soapRequest.setSearchInfo(searchInfo);
//            soapRequest.setPublicIp("172.16.10.36");
//            
//            stub = new BannerAdStub(WebServiceConfigurator.getUrlOfBannerAds());
//            BannerAdResponse response = stub.getBannerAd(soapRequest);
//            
//            if( response != null && "OK".equals(response.getResponseStatus().getStatusCode()))
//            {
//                result.isSuccess = true;
//            }else
//            {
//                result.isSuccess = false;
//                result.msg = response == null ? "response is null" : "StatusCode = "+response.getResponseStatus().getStatusCode()+", StatusMessage = "+response.getResponseStatus().getStatusMessage();
//            }
//        }
//        catch (Exception e)
//        {
//            logger.fatal("#monitorBannerAdvertiseServer", e);
//            result.isSuccess = false;
//            result.msg = "Exception occurs when getBannerAd" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(e);
//        }
//        finally
//        {
//            if( stub != null )
//            {
//                WebServiceUtils.cleanupStub(stub);
//            }
//        }
//        return result;
//    }
    

    @Monitor(server=ONEBOXSEARCH_SERVER, parserClass=ServiceUrlParserFactory.CLASS_PROPERTIES, filePath="config/web_services.properties", serviceUrlKeys="URL_ONE_BOX_SEARCH")
    private DetectResult monitorOneBoxSearchServer()
    {
        DetectResult result = new DetectResult();
        
        OneboxSearchServiceStub stub = null;
        try
        {
            UserProfile userProfile = BackendServerMonitorUtil.createUserProfile();
            TnContext tc = BackendServerMonitorUtil.createTnContext();
            
            OneboxSearchRequest obReq = new OneboxSearchRequest();

            // set user
            UserInformation user = new UserInformation();
            UserProfile usrProfile = userProfile;
            user.setUserId(usrProfile.getUserId());
            user.setPtn(usrProfile.getMin());
            obReq.setUserInfo(user);
            
            // set client info
            Date today = new Date();
            //using date only
            String trxnId = String.valueOf(today.getTime());
            obReq.setClientName(ClientNameEnum._MOBILE);
            obReq.setClientVersion("1");
            obReq.setTransactionId(trxnId);
            
            // set POI data set() and MAP data set
            obReq.setContextString(tc.toContextString());

            // set anchor
            com.telenav.ws.datatypes.address.GeoCode latLon = new com.telenav.ws.datatypes.address.GeoCode();
            latLon.setLatitude(37.37453);
            latLon.setLongitude(-121.99983);
            obReq.setAnchor(latLon);

            // set query
            obReq.setQuery("coffee");
            obReq.setStartIndex(0);
            obReq.setResultCount(10);
            //TODO: check sponsor number
            obReq.setSponsorStartIndex(0);
            obReq.setSponsorResultCount(1);
            obReq.setLocale(CommonUtil.getTnLocal(userProfile.getLocale()));
            obReq.setSponsorResultCount(100);
            obReq.setTransactionId(System.currentTimeMillis()+"");
            
            stub = new OneboxSearchServiceStub(WebServiceConfigurator.getUrlOfOneBox());
            OneboxSearchResponse response = stub.oneboxSearch(obReq);
            
            if( response != null && "RESULT_FOUND".equals(response.getResponseStatus().getStatusCode()) )
            {
                result.isSuccess = true;
            }
            else
            {
                result.isSuccess = false;
                result.msg = response == null ? "Response is null." : "StatusCode = "+response.getResponseStatus().getStatusCode()+", StatusMessage = "+response.getResponseStatus().getStatusMessage();
            }

        }
        catch (Exception e)
        {
            logger.fatal("#monitorOneBoxSearchServer", e);
            result.isSuccess = false;
            result.msg = "Exception occurs when one box search" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(e);
        }
        finally
        {
            if( stub != null )
            {
                WebServiceUtils.cleanupStub(stub);
            }
        }
        return result;
    }
    

    @Monitor(server=USER_MANAGEMENT_SERVER, parserClass=ServiceUrlParserFactory.CLASS_PROPERTIES, filePath="config/web_services.properties", serviceUrlKeys="URL_POI_REVIEW_WRITE")
    private DetectResult monitorUserManagementServer()
    {

        DetectResult result = new DetectResult();
        
        UserManagementServiceStub stub = null;
        try
        {
            UserProfile userProfile = BackendServerMonitorUtil.createUserProfile();
            TnContext tc = BackendServerMonitorUtil.createTnContext();
            
            GetUserRequestDTO getUserRequest = new GetUserRequestDTO();
            getUserRequest.setParam(userProfile.getUserId()+"");
            getUserRequest.setParamType(UserQueryBy.USER_ID);
            
            getUserRequest.setClientName(HtmlConstants.clientName);
            getUserRequest.setClientVersion(HtmlConstants.clientVersion);
            getUserRequest.setContextString(tc.toContextString());
            getUserRequest.setTransactionId(HtmlPoiUtil.getTrxId());
            //get userInfo
            stub = new UserManagementServiceStub(WebServiceConfigurator.getUrlOfPoiReviewWrite());
            GetUserResponseDTO response = stub.getUser(getUserRequest);
            if (response != null)
            {
                if (response.getUser() != null)
                {
                    result.isSuccess = true;
                }
                else
                {
                    result.isSuccess = false;
                    result.msg = "GetUserResponseDTO.getUser() is null";
                }
            }
            else
            {
                result.isSuccess = false;
                result.msg = "GetUserResponseDTO is Null";
            }
        }
        catch (Exception ex)
        {
            logger.fatal("#monitorUserManagementServer", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when getUserName" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }
        finally
        {
            if( stub != null )
            {
                WebServiceUtils.cleanupStub(stub);
            }
        }
        return result;
    }

    @Monitor(server=LOGOIMAGE_SERVER, parserClass=ServiceUrlParserFactory.CLASS_PROPERTIES, filePath="config/web_services.properties", serviceUrlKeys="URL_GETLOGOIMAGE")
    private DetectResult monitorLogoImageServer()
    {
        DetectResult result = new DetectResult();
        
        HttpClient httpClient = null;
        HttpMethod method = null;
        try
        {
            String getLogImageUrl = WebServiceConfigurator.getUrlOfLogoImage();
            String url = MessageFormat.format(getLogImageUrl,"/tnimages/logo/cat-accomodation.png","90","90");
            method = new GetMethod(url);
            httpClient = new HttpClient();
            httpClient.executeMethod(method);
            String image = "";
            byte[] imageBytes = method.getResponseBody();
            if (method.getStatusCode() == 200)
            {
                if (imageBytes != null && imageBytes.length != 0)
                {
                    image = Utility.byteArrayToBase64(imageBytes);
                }
                
                if (image != null && image.length() > 0 )
                {
                    result.isSuccess = true;
                }
                else
                {
                    result.isSuccess = false;
                    result.msg = image == null ? "Image is null. Image url is " + url: "result is empty. Image url is " + url;
                }
            }
            else
            {
                result.isSuccess = false;
                result.msg = "http status code is "+method.getStatusCode();
            }
            
        }
        catch (Exception ex)
        {
            logger.fatal("#monitorLogoImageServer", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when get logo image" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }
        finally
        {
            if (method != null)
            {
                try
                {
                    method.releaseConnection();
                }
                catch (Exception ex)
                {
                }
            }
        }
        return result;
    }
    

    @Monitor(server=POIDETAIL_SERVER, parserClass=ServiceUrlParserFactory.CLASS_PROPERTIES, filePath="config/web_services.properties", serviceUrlKeys="URL_POI_DETAIL")
    private DetectResult monitorPoiDetailServer()
    {
        DetectResult result = new DetectResult();
        
        ContentInfoServiceStub stub = null;
        try
        {
            TnContext tc = BackendServerMonitorUtil.createTnContext();
            
            GetPoiDetailsRequest getPoiDetailRequest = new GetPoiDetailsRequest();
            getPoiDetailRequest.setClientName(HtmlConstants.clientName);
            getPoiDetailRequest.setClientVersion(HtmlConstants.clientVersion);
            getPoiDetailRequest.setContextString(tc.toContextString());
            getPoiDetailRequest.setTransactionId(System.currentTimeMillis()+"");
            getPoiDetailRequest.setPoiId(getSamplePoiId());
            
            stub = new ContentInfoServiceStub(WebServiceConfigurator.getUrlOfPoiDetail());
            GetPoiDetailsResponse  getPoiDetailResponse = stub.getPoiDetails(getPoiDetailRequest);
            com.telenav.datatypes.services.v20.ResponseStatus status = getPoiDetailResponse.getResponseStatus();
            
            //poi detail can't be found is ok. only 20%-30% has detail information from zhangpan.
            if ("OK".equals(status.getStatusCode()) || "DB_DATA_NOT_FOUND".equals(status.getStatusCode()))
            {
                result.isSuccess = true;
            }
            else
            {
                result.isSuccess = false;
                result.msg = "responseCode = "+status.getStatusCode()+", responseMessage = "+status.getStatusMessage();
                logger.debug(result.msg);
            }
            
        }
        catch (Exception ex)
        {
            logger.fatal("#monitorLogoImageServer", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when get logo image" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }
        finally
        {
            if (stub != null)
            {
                try
                {
                    WebServiceUtils.cleanupStub(stub);
                }
                catch (Exception ex)
                {
                }
            }
        }
        return result;
    }

    @Monitor(server=ORGANICADVERTISE_SERVER, parserClass=ServiceUrlParserFactory.CLASS_PROPERTIES, filePath="config/web_services.properties", serviceUrlKeys="URL_ORGANIC_ADS_SEARCH")
    private DetectResult monitorOrganicAdvertiseServer()
    {
        DetectResult result = new DetectResult();
        
        com.telenav.services.content.ads.v10.AdServiceStub stub = null;
        try
        {
            TnContext tc = BackendServerMonitorUtil.createTnContext();
            
            AdServiceOrganicAdsRequestDTO requestDTO = new AdServiceOrganicAdsRequestDTO();
            requestDTO.setClientName(HtmlConstants.clientName);
            requestDTO.setClientVersion(HtmlConstants.clientVersion);
            requestDTO.setContextString(tc.toContextString());
            requestDTO.setTransactionId(HtmlPoiUtil.getTrxId());
            long[] poiIds = new long[1];
            poiIds[0] = getSamplePoiId();
            requestDTO.setPoiIds(poiIds);
            
            stub = new com.telenav.services.content.ads.v10.AdServiceStub(WebServiceConfigurator.getUrlOfOrganicAdsSearch());
            AdServiceResponseDTO  response = stub.getOrganicAds(requestDTO);
            
            if( response != null ){
                result.isSuccess = true;
               
            }else{
                result.isSuccess = false;
                result.msg = "response is null";
            }
        }
        catch (Exception ex)
        {
            logger.fatal("#monitorOrganicAdvertiseServer", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when get logo get organic advertise" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }
        finally
        {
            if (stub != null)
            {
                try
                {
                    WebServiceUtils.cleanupStub(stub);
                }
                catch (Exception ex)
                {
                }
            }
        }
        return result;
    }
    
    @Monitor(server=ACE_SERVER, parserClass=ServiceUrlParserFactory.CLASS_ACE)
    private DetectResult monitorAceServer()
    {
        DetectResult result = new DetectResult();

        try
        {
            GeoCodingProxy proxy = GeoCodingProxy.getInstance(BackendServerMonitorUtil.createTnContext());
            
            GeoCodeResponse geoCodeResponse = proxy.geoCode(BackendServerMonitorUtil.createAddress());
            if (geoCodeResponse != null && geoCodeResponse.getStatus().isSuccessful())
            {
                result.isSuccess = true;
            }
            else
            {
                result.isSuccess = false;
                result.errCode = geoCodeResponse != null ? geoCodeResponse.getStatus().getStatusCode() : -1;
                result.msg = "Can not get response from ACE SERVER";
            }
        }
        catch (Exception e)
        {
            logger.warn("Throttling Exception occurs", e);
            result.isSuccess = false;
            result.msg = "Exception occurs->" + ExceptionUtil.collectExceptionMsg(e);
        }

        return result;
    }
    
    
}
