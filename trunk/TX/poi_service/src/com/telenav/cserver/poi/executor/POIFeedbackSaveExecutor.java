package com.telenav.cserver.poi.executor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PropertyResourceBundle;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.WebServiceConfigurator;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.ws.services.messaging.EmailMessage;
import com.telenav.ws.services.messaging.MsgServiceRequestDTO;
import com.telenav.ws.services.messaging.MsgServiceStub;

public class POIFeedbackSaveExecutor extends AbstractExecutor
{
    private static String CONFIG_FILE = "config.feedback";
    private static String emailToRecipients = "", emailFromAddress = "";

    /** msg service end point in property file. */
    private static final String MSG_SERVICE_END_POINT = WebServiceConfigurator
            .getUrlOfMsgService();

	 static {
   	    PropertyResourceBundle bundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle(CONFIG_FILE);

        emailToRecipients = bundle.getString("EMAIL_TO_RECIPIENTS");
        emailFromAddress  = bundle.getString("EMAIL_FROM_ADDRESS");
    }

	public void doExecute(ExecutorRequest req, ExecutorResponse resp,
            ExecutorContext context) throws ExecutorException
	{
		// Get the request and response
		POIFeedbackSaveRequest poiRequest   = (POIFeedbackSaveRequest)req;
		POIFeedbackSaveResponse poiResponse = (POIFeedbackSaveResponse) resp;

		try{
			// get parameters
 			String[] feedbacks   = poiRequest.getFeedbacks();
 			String comments      = poiRequest.getComments();
 			
			// construct email template
			String       emailSubject = null;
			StringBuffer emailBodyBuf = new StringBuffer();
			String       emailBody;


			Date  timeNow        = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			String dateStr       = sdf.format(timeNow);

			emailBodyBuf.append("PTN: "      + poiRequest.getUserProfile().getMin()      + "<br>");
			emailBodyBuf.append("Product: "  + poiRequest.getUserProfile().getProduct()  + "<br>");
			emailBodyBuf.append("Version: "  + poiRequest.getUserProfile().getVersion()  + "<br>");
			emailBodyBuf.append("Carrier: "  + poiRequest.getUserProfile().getCarrier()  + "<br>");
			emailBodyBuf.append("Platform: " + poiRequest.getUserProfile().getPlatform() + "<br>");
			emailBodyBuf.append("Device: "   + poiRequest.getUserProfile().getDevice()   + "<br>");
			emailBodyBuf.append("Region: "   + poiRequest.getUserProfile().getRegion()   + "<br>");
			emailBodyBuf.append("Submitted On: "   + dateStr   + "<br>");
			emailBodyBuf.append("<br>");

			emailBodyBuf.append("Question: " + poiRequest.getFeedbackQuestion()          + "<br>");
			
			if(poiRequest.getFeedbackPage().equals("POIListFeedback"))
			{
				String searchKeyword = poiRequest.getSearchKeyword();
 				Stop searchLocation  = poiRequest.getSearchLocation();
				String searchCatName  = poiRequest.getSearchCatName();

				// construct email subject
				emailSubject = "Feeback for Search Results: " + searchKeyword;
				
				// get search string
				emailBodyBuf.append("Search String: " + searchKeyword + "<br>");

				// get category ID
				emailBodyBuf.append("Search Category: " + searchCatName + "<br>");

				// get search location
				emailBodyBuf.append("Where: ");
				emailBodyBuf.append(convertStopToStr(searchLocation));
				emailBodyBuf.append("<br><br>");
			}
			else if(poiRequest.getFeedbackPage().equals("POIDetailFeedback"))
			{
				String poiName        = poiRequest.getPoiName();
 				Stop poiLocation      = poiRequest.getPoiLocation();

				// construct email subject
				emailSubject = "Feeback for POI: " + poiName;

				// get poi name
				emailBodyBuf.append("POI Name: " + poiName + "<br>");

				// get poi location
				emailBodyBuf.append("POI Location: ");
				emailBodyBuf.append(convertStopToStr(poiLocation));
				emailBodyBuf.append("<br><br>");				
			}

			// get feedback
			emailBodyBuf.append("Customer Feedback:<br>");
			for(int i = 0; i < feedbacks.length; i++)
			{
				emailBodyBuf.append((i+1) + ". " + feedbacks[i] + "<br>");
			}
			
			emailBodyBuf.append("<br>");
			emailBodyBuf.append("Comments: " + comments);
			
			emailBody = emailBodyBuf.toString();

			// call msg service to send email
			try 
			{
				System.out.println("sending email");
				// TODO change to
				String[] sendToEmails = {emailToRecipients};
				MsgServiceStub stub = new com.telenav.ws.services.messaging.MsgServiceStub(MSG_SERVICE_END_POINT);
				MsgServiceRequestDTO request2 = new com.telenav.ws.services.messaging.MsgServiceRequestDTO();
				EmailMessage email = new com.telenav.ws.services.messaging.EmailMessage();
				email.setEmail(sendToEmails);
				// TODO change from
				email.setFrom(emailFromAddress);
				// email.setFrom("vijayantp@telenav.com");
				email.setSubject(emailSubject);
				email.setSendType(3);
				email.setContentType("text/html");
				email.setMessage(emailBody);
				request2.addMessages(email);
				stub.send(request2);
				System.out.println("email sent");
			} catch(Exception e) {
				System.out.println("email fails" + e.getMessage());
				throw e;
			}
			
			poiResponse.setStatus(ExecutorResponse.STATUS_OK);
            
		} catch(Exception ex) {
			poiResponse.setErrorMessage("COSE.notAvailable");
			poiResponse.setStatus(ExecutorResponse.STATUS_FAIL);
		}
	}

	private String convertStopToStr(Stop stop)
	{
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
