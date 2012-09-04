/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.axis2.databinding.types.Day;
import org.apache.axis2.databinding.types.Month;
import org.apache.axis2.databinding.types.Year;
import org.apache.log4j.Logger;

import com.telenav.LocalAppsMovieServicesStub;
import com.telenav.browser.movie.Util;
import com.telenav.cserver.backend.addresssharing.AddressSharingResponse;
import com.telenav.cserver.backend.addresssharing.AddressSharingServiceProxy;
import com.telenav.cserver.backend.addresssharing.LocationSharingRequest;
import com.telenav.cserver.backend.datatypes.addresssharing.ContactInfo;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.framework.management.jmx.AbstractBackendServerMonitor;
import com.telenav.cserver.framework.management.jmx.DetectResult;
import com.telenav.cserver.framework.management.jmx.ExceptionUtil;
import com.telenav.cserver.framework.management.jmx.config.Monitor;
import com.telenav.cserver.framework.management.jmx.config.ServiceUrlParserFactory;
import com.telenav.cserver.framework.throttling.ThrottlingConfiguration;
import com.telenav.datatypes.content.movie.v10.Area;
import com.telenav.datatypes.content.movie.v10.MovieImageTypeEnum;
import com.telenav.datatypes.content.movie.v10.MovieSearchDate;
import com.telenav.datatypes.content.movie.v10.MovieSortTypeEnum;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.content.moviesearchservice.v10.MovieBatchImagesRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieBatchImagesResponseDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchServiceStub;
import com.telenav.services.content.moviesearchservice.v10.MovieWithDetailTheaterInfoServiceResponseDTO;
import com.telenav.services.content.v10.ContentSearchServiceStub;
import com.telenav.services.content.v10.GetPoiByIdsRequest;
import com.telenav.services.content.v10.GetPoiByIdsResponse;
import com.telenav.ws.datatypes.services.ResponseStatus;
import com.telenav.ws.datatypes.services.v20.UserInformation;
import com.telenav.ws.services.localapps.movie.v10.GetTicketRequest;
import com.telenav.ws.services.localapps.movie.v10.GetTicketResponse;


/**
 * BackendServerMonitor.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 July 18, 2011
 * 
 */
public class BackendServerMonitor extends AbstractBackendServerMonitor {
    
    private static final Logger logger = Logger.getLogger(BackendServerMonitor.class);
	
    private final static String SHAREADDRESS_SERVER = "ShareAddress(shareMovie)";
    private final static String MOVIESEARCH20_SERVER = "MovieSearch20(loadImage)";
    private final static String MOVIESEARCH_SERVER = "MovieSearch(searchMovies)";
    private final static String MOVIETICKET_SERVER = "MovieTicket(getTicketPrice*)";
    private final static String POISEARCH_SERVER = "PoiSearch(getPoiByIds*)";
    
    public BackendServerMonitor()
    {
        // !!!make sure has initial all the class before invoking monitor
        WebServiceConfiguration.getInstance();
        ThrottlingConfiguration.getInstance();
        Util.class.getClass();
    }
    
    
    /* used in ShareAddressExecutor */
    @Monitor(server=SHAREADDRESS_SERVER, parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE, serviceUrlKeys="ADDRESSSHARING")
    private DetectResult monitorShareAddressServer()
    {
        DetectResult result = new DetectResult();
        
        try {
            UserProfile userProfile = BackendServerMonitorUtil.createUserProfile();
            TnContext tc = BackendServerMonitorUtil.createTnContext();
            
            LocationSharingRequest request = new LocationSharingRequest();
            request.setUserId(Long.parseLong(userProfile.getUserId()));
            request.setPtn(userProfile.getMin());
            request.setContextString(tc.toContextString());
            request.setAddress(BackendServerMonitorUtil.createDestStop());
            
            ContactInfo contact = new ContactInfo();
            contact.setName("test");
            contact.setPtn(userProfile.getMin());
            List<ContactInfo> contactList = new ArrayList<ContactInfo>();
            contactList.add(contact);
            
            request.setContactList(contactList);
            request.setMovieName("test");
            request.setBrandName("test");
            Stop address = request.getAddress();
            address.label = "test";
            request.setAddress(address);
            
            AddressSharingResponse response = AddressSharingServiceProxy.getInstance().shareMovie(request, tc);
            String statusCode = response.getStatusCode();
            
            if ("OK".equalsIgnoreCase(statusCode)){
                result.isSuccess = true;
            } else {
                result.isSuccess = false;
                result.msg = "statusCode = "+statusCode+", statusMessage = "+response.getStatusMessage();
            }
        }
        catch (Exception ex) {
            logger.fatal("#monitorShareAddressServer", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when share movie" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }
        
        return result;
    }
    
    /**
     * movie ids are fake, we only need to check response status
     * @return
     */
    @Monitor(server=MOVIESEARCH20_SERVER, parserClass=ServiceUrlParserFactory.CLASS_PROPERTIES, filePath="web_services.properties", serviceUrlKeys="URL_MOVIE_SEARCH20")
    private DetectResult monitorMovieSearch20()
    {
        DetectResult result = new DetectResult();
        MovieSearchServiceStub stub = null;
        try {
            String[] movieIds = {"1","2","3"};
            MovieBatchImagesRequestDTO request = new MovieBatchImagesRequestDTO();
            request.setMovieIds(movieIds);
            request.setMovieImageType(MovieImageTypeEnum.SMALL_IMAGE);
            request.setDisplayHeight(100);
            request.setDisplayWidth(150);

            stub = new MovieSearchServiceStub(HtmlCommonUtil.getWSContext(),Util.getWsMovieURL20());
            Util.setClientProps(request);
            MovieBatchImagesResponseDTO imageResponse = stub.getMovieBatchImages(request);
            ResponseStatus status = imageResponse.getResponseStatus();
           
            if ("OK".equalsIgnoreCase(status.getStatusCode())){
                result.isSuccess = true;
            } else {
                result.isSuccess = false;
                result.msg = "statusCode = "+status.getStatusCode()+", statusMessage = "+status.getStatusMessage();
            }
        }
        catch (Exception ex) {
            logger.fatal("#monitorMovieSearch20", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when load image" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }
        finally{
            WebServiceUtils.cleanupStub(stub);
        }
        
        return result;
    }
    
    @Monitor(server=MOVIESEARCH_SERVER, parserClass=ServiceUrlParserFactory.CLASS_PROPERTIES, filePath="web_services.properties", serviceUrlKeys="URL_MOVIE_SEARCH")
    private DetectResult monitorMovieSearch()
    {
        DetectResult result = new DetectResult();
        MovieSearchServiceStub stub = null;
        try {
            
            MovieSearchRequestDTO movieSearchRequest = new MovieSearchRequestDTO();
            movieSearchRequest.setKeyword("");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            
            MovieSearchDate date = new MovieSearchDate();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            date.setDay(new Day(cal.get(Calendar.DAY_OF_MONTH)));
            date.setMonth(new Month(cal.get(Calendar.MONTH)+1));
            date.setYear(new Year(cal.get(Calendar.YEAR)-1970));
            
            movieSearchRequest.setDate(date);
           // movieSearchRequest.setDate(Util.getSearchDate(sdf.format(new Date())));
            Area area = Util.getArea(3737515,-12199769, (int)(500*1609.344));
            movieSearchRequest.setArea(area);
            movieSearchRequest.setPageLength(10);
            movieSearchRequest.setPageNumber(0);
            
            movieSearchRequest.setSortType(MovieSortTypeEnum.ALPHABET); 
            Util.setClientProps(movieSearchRequest);

            stub = (MovieSearchServiceStub)Util.getService();
            MovieWithDetailTheaterInfoServiceResponseDTO response = stub.searchMoviesWithDetailTheaterInfo(movieSearchRequest);
            ResponseStatus status = response.getResponseStatus();
            
            if ("OK".equalsIgnoreCase(status.getStatusCode())){
                result.isSuccess = true;
            } else {
                result.isSuccess = false;
                result.msg = "statusCode = "+status.getStatusCode()+", statusMessage = "+status.getStatusMessage();
            }
        }
        catch (Exception ex) {
            logger.fatal("#monitorMovieSearch", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when search movie" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }
        finally{
            WebServiceUtils.cleanupStub(stub);
        }
        
        return result;
    }
    
    /**
     * the request is fake, we only check the interface is connected. Because we can't always have success case, fail response is ok.
     * So no exception is ok
     * @return
     */
    @Monitor(server=MOVIETICKET_SERVER, parserClass=ServiceUrlParserFactory.CLASS_PROPERTIES, filePath="web_services.properties", serviceUrlKeys="URL_MOVIE_TICKET")
    private DetectResult monitorMovieTicketServer()
    {
        DetectResult result = new DetectResult();
        
        LocalAppsMovieServicesStub stub = null;
        try {  
            UserProfile userProfile = BackendServerMonitorUtil.createUserProfile();
            
            GetTicketRequest getTicketRequest = new GetTicketRequest();   
            
            getTicketRequest.setMovieId("1");
            getTicketRequest.setTheaterId("1");
            getTicketRequest.setShowDate("2010-9-1");
            getTicketRequest.setShowTime("10:00");   
            getTicketRequest.setTransactionId("1");
            getTicketRequest.setClientName("cose");
            getTicketRequest.setClientVersion("1");
            getTicketRequest.setPartnerCode("FANDANGO_MOVIE");
            
            UserInformation userInfo = new UserInformation();
            userInfo.setUserId(String.valueOf(userProfile.getUserId()));
            getTicketRequest.setUserInfo(userInfo);
            
            stub = new LocalAppsMovieServicesStub(HtmlCommonUtil.getWSContext(),Util.getWsMovieTicketURL());
            GetTicketResponse getTicketResponse = stub.getTicket(getTicketRequest);
            com.telenav.ws.datatypes.services.v20.ResponseStatus status = getTicketResponse.getResponseStatus();
            //no exception is ok
            
            result.isSuccess = true;
        }
        catch (Exception ex) {
            logger.fatal("#monitorMovieSearch20", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when load image" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }
        finally
        {
            WebServiceUtils.cleanupStub(stub);
        }
        
        return result;
    }
    
    
    @Monitor(server=POISEARCH_SERVER, parserClass=ServiceUrlParserFactory.CLASS_PROPERTIES, filePath="web_services.properties", serviceUrlKeys="URL_POI_SEARCH")
    private DetectResult monitorPoiSearch()
    {
        DetectResult result = new DetectResult();
        ContentSearchServiceStub stub = null;
        try {
            GetPoiByIdsRequest tInforReq = new GetPoiByIdsRequest();
            tInforReq.setPoiIds(new long[]{1});
            Util.setClientProps(tInforReq);
            stub = (ContentSearchServiceStub)Util.getPoiService(); 
            GetPoiByIdsResponse poiResp = stub.getPoiByIds(tInforReq);
            com.telenav.ws.datatypes.services.ResponseStatus status = poiResp.getResponseStatus();
            //no exception is ok
            result.isSuccess = true;
        }
        catch (Exception ex) {
            logger.fatal("#monitorMovieSearch20", ex);
            result.isSuccess = false;
            result.msg = "Exception occurs when load image" + ". Exception msg->" + ExceptionUtil.collectExceptionMsg(ex);
        }
        finally{
            WebServiceUtils.cleanupStub(stub);
        }
        
        return result;
    }
   
}
