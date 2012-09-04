package com.telenav.cserver.poi.executor;

import java.util.List;

import org.apache.log4j.Logger;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.WebServiceConfigurator;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.ws.datatypes.address.GeoCode;
import com.telenav.ws.datatypes.common.Property;
import com.telenav.ws.datatypes.feedback.Comment20;
import com.telenav.ws.datatypes.feedback.Feedback40;
import com.telenav.ws.datatypes.feedback.FeedbackServiceRequestDTO;
import com.telenav.ws.datatypes.feedback.FeedbackServiceResponseDTO;
import com.telenav.ws.datatypes.feedback.FeedbackTopic;
import com.telenav.ws.datatypes.feedback.MediumType;
import com.telenav.ws.datatypes.feedback.NavigationContext;
import com.telenav.ws.datatypes.feedback.TextAnswerType;
import com.telenav.ws.services.feedback.FeedbackServiceStub;

public class GenericFeedbackSaveExecutor extends AbstractExecutor
{
	private static Logger logger = Logger.getLogger(GenericFeedbackSaveExecutor.class);
	
    /** feedback service end-point in property file.*/
    private static final String FEEDBACK_SERVICE_END_POINT = WebServiceConfigurator.getUrlOfFeedbackServer();
 
    private boolean isPoiList(String page)
    {
    	if(("POIListFeedback").equals(page))
		{
    		return true;
		}
    	else if(("poilist").equals(page))
		{
    		return true;
		}
    	else
    	{
    		return false;
    	}
    }
    
    private boolean isPoiDetail(String page)
    {
    	if(("POIDetailFeedback").equals(page))
		{
    		return true;
		}
    	else if(("poidetail").equals(page))
		{
    		return true;
		}
    	else
    	{
    		return false;
    	}    	
    }
    
	public void doExecute(ExecutorRequest req, ExecutorResponse resp,
            ExecutorContext context) throws ExecutorException
	{
		// Get the request and response
		GenericFeedbackSaveRequest poiRequest   = (GenericFeedbackSaveRequest)req;
		GenericFeedbackSaveResponse poiResponse = (GenericFeedbackSaveResponse) resp;
		
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("GenericFeedbackSave");
		try{
 			List<GenericFeedbackSaveRequest.FeedbackResponse> fResponses =  poiRequest.getFeedbackResponses();
 			Feedback40 feedback = new Feedback40();
 			
			String topic = poiRequest.getFeedbackTopic();
			String email = poiRequest.getEmail();//add for Feedback40
			String OSVersion = poiRequest.getOSVersion();
			if (OSVersion!=null&&OSVersion!="") {
				feedback.addProperty(getProperty("osVersion", OSVersion));
			}
			
			NavigationContext navCtx = new NavigationContext();
			UserProfile userProfile = poiRequest.getUserProfile();
			String version = poiRequest.getVersion();

			if(isPoiList(poiRequest.getFeedbackPage()))
			{
				String searchKeyword = poiRequest.getSearchKeyword();
 				Stop searchLocation  = poiRequest.getSearchLocation();
				String searchCatName  = poiRequest.getSearchCatName();
				String pageIndex  = poiRequest.getPageIndex();

				// construct email subject
//				topic = "Feeback for Search Results: " + searchKeyword;
				
				GeoCode geoCode = new GeoCode();
				if(searchLocation != null){
					geoCode.setLatitude(searchLocation.lat);
					geoCode.setLongitude(searchLocation.lon);
				}
				navCtx.addCurrentLocation(geoCode);
				feedback.setNavContext(navCtx);
				
				//add search string
	            feedback.addProperty(getProperty("SearchString", searchKeyword));
	            //add search category
	            feedback.addProperty(getProperty("SearchCategory", searchCatName));
	            //add search location
	            feedback.addProperty(getProperty("SearchLocation", convertStopToStr(searchLocation)));
	            feedback.addProperty(getProperty("PageIndex", pageIndex));
	            feedback.addProperty(getProperty("Product", userProfile.getProduct()));
	            feedback.addProperty(getProperty("Region", userProfile.getRegion()));
	            String tID = poiRequest.getTransactionID();
                if(tID == null){
                	tID="unknown";
                }
                feedback.addProperty(getProperty("TransactionID", tID));
			}
			else if(isPoiDetail(poiRequest.getFeedbackPage()))
			{
				String poiName        = poiRequest.getPoiName();
 				Stop poiLocation      = poiRequest.getPoiLocation();
				String poiPhoneNumber = poiRequest.getPoiPhoneNumber();
				String poiID = poiRequest.getPoiID();

				GeoCode geoCode = new GeoCode();
				if(poiLocation != null){
					geoCode.setLatitude(poiLocation.lat);
					geoCode.setLongitude(poiLocation.lon);
				}
				navCtx.addCurrentLocation(geoCode);
				feedback.setNavContext(navCtx);
				
				// construct email subject
				//topic = "Feeback for POI: " + poiName;

				//add POI name
	            feedback.addProperty(getProperty("POIName", poiName));
	            //add POI location
	            feedback.addProperty(getProperty("POILocation", convertStopToStr(poiLocation)));
	            //add POI location
	            feedback.addProperty(getProperty("POIPhonenumber", poiPhoneNumber));
	            feedback.addProperty(getProperty("PoiID", poiID));
	            feedback.addProperty(getProperty("Product", userProfile.getProduct()));
	            feedback.addProperty(getProperty("Region", userProfile.getRegion()));
	            String tID = poiRequest.getTransactionID();
                if(tID == null){
                	tID="unknown";
                }
                feedback.addProperty(getProperty("TransactionID", tID));
	            
			}else {
				Stop currentLocation = poiRequest.getCurrentLocation();
				GeoCode geoCode = new GeoCode();
				if(currentLocation != null){
					geoCode.setLatitude(currentLocation.lat);
					geoCode.setLongitude(currentLocation.lon);
				}
				navCtx.addCurrentLocation(geoCode);
				feedback.setNavContext(navCtx);
			}
 
			// call feedback service to send info
			FeedbackServiceStub stub = null;
			try 
			{
//				System.out.println("feedback sending>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				
				stub = new FeedbackServiceStub(FEEDBACK_SERVICE_END_POINT);
				FeedbackServiceRequestDTO request = new FeedbackServiceRequestDTO();
                
				//request.setTopic(FeedbackTopic.UNKNOWN);
                request.setCarrier(userProfile.getCarrier());// SprintPCS
                request.setPlatform(userProfile.getPlatform());// RIM
//                request.setProgramCode(userProfile.getProgramCode());//SNNAVPROG
                request.setClientName("c-server");//component name who is calling this api:junit-test
                request.setClientVersion(userProfile.getVersion());//never mind, you can always set 1.0
                request.setTransactionId("unknown");//never mind, you can always set unknown
                request.setUserClient(userProfile.getProduct());
//                request.setUserClient(ps.getUserClient());//mapping to client type:AT&T Navigator
                if (version==""||version==null) {
                	 request.setUserClientVersion(userProfile.getVersion());//mapping to version
				}else {
					request.setUserClientVersion(version);
				}
               
                request.setDevice(userProfile.getDevice());//
                request.setTopicString(topic);
                
                feedback.setFeedbackTime(System.currentTimeMillis());
                feedback.setTitle(poiRequest.getFeedbackPage());
                feedback.setTopic(FeedbackTopic.UNKNOWN);
                feedback.setTopicString(topic);
                String min = userProfile.getMin();
                if (min==null) {
					min = "";
				}
                feedback.setPtn(min);// "4088366110"
                
                long userId = poiRequest.getUserId();//for 7.x
                if (userProfile.getUserId()!="") {
                	userId = Long.parseLong(userProfile.getUserId());// for 6.x
				}
                feedback.setUserId(userId);// 798010 or 0
                
                if (email!=null&&email!="") {
                	feedback.setEmail(email);
				}
                
                // add all answers to the feedback
				for (GenericFeedbackSaveRequest.FeedbackResponse answer : fResponses) {
					Comment20 comment = new Comment20();
					comment.setCommentator(userId+"");
					comment.setComments(answer.getComments());// the value for this doesn't matter
					comment.setCommentTypeId(answer.getQuestionID());// question number
					comment.setMediumType(MediumType.TEXT);
					comment.setAnswerType(TextAnswerType.MULTIPLE);// multiple choice question
					comment.setChoice(answer.getFeedbacks());// the answer for the single choice question
					comment.setBinaryData(null);

					feedback.addComment(comment);
				}
                request.setFeedback(feedback);

                cli.addData("FeedbackServiceRequest",
                		request != null ? request.toString() : "FeedbackServiceRequest is null.");	
				
//                System.out.println("Feed back save request: " + poiRequest);
                FeedbackServiceResponseDTO response = stub.createFeedback(request);
                cli.addData("FeedbackServiceResponse Status",
                		response != null ? response.getResponseStatus().toString() : "FeedbackServiceResponse is null.");	
				 
				logger.info("Generic feedback save request sent: " + request);
//				System.out.println("feedback sent>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			} catch(Exception e) {
				logger.error("feedback handling failed: " + e.getMessage());
				throw e;
			}
			finally {
				WebServiceUtils.cleanupStub(stub);
			}
			
			poiResponse.setStatus(ExecutorResponse.STATUS_OK);
            
		} catch(Exception ex) {
			cli.setStatus(ex);
			logger.error("Error thrown" + ex.getMessage());
			poiResponse.setErrorMessage("COSE.notAvailable");
			poiResponse.setStatus(ExecutorResponse.STATUS_FAIL);
		} finally {
			cli.complete();
		}
	}

	/**
	 * Construct custom property object for feedback service
	 * @param key
	 * @param value
	 * @return property
	 */
	private Property getProperty(String key, String value){
		Property property = new Property();
		property.setKey(key);
		property.setValue(value);
		
		return property;
	}
	
	private String convertStopToStr(Stop stop)
	{
		if(stop == null) return "";
		StringBuffer strBuf = new StringBuffer();

		// if everything is blank, then it is current location
		if((stop.firstLine == null || "".equals(stop.firstLine)) &&
		   (stop.city      == null || "".equals(stop.city))      &&
		   (stop.state     == null || "".equals(stop.state))     &&
		   (stop.country   == null || "".equals(stop.country)))
		{
			strBuf.append("Current location (");
			strBuf.append("Lat: " + stop.lat + " ");
			strBuf.append("Lon: " + stop.lon);
			strBuf.append(")");
		}

		if(!"".equals(stop.firstLine))
		{
			strBuf.append(stop.firstLine + ", ");
		}
		if(!"".equals(stop.city))
		{
			strBuf.append(stop.city + ", ");
		}
		if(!"".equals(stop.state))
		{
			strBuf.append(stop.state + ", ");
		}
		if(!"".equals(stop.country))
		{
			strBuf.append(stop.country + "");
		}

		return strBuf.toString();
	}
	
}
