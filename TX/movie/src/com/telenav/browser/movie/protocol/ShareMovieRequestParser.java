package com.telenav.browser.movie.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONObject;

import com.telenav.browser.movie.Util;
import com.telenav.browser.movie.executor.ShareMovieRequest;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.browser.movie.Constant.RRKey;

public class ShareMovieRequestParser extends BrowserProtocolRequestParser{
	private static Logger logger = Logger.getLogger(ShareMovieRequestParser.class);
	
	public String getExecutorType() {
        return "shareMovie";
    }

    public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest) throws Exception {
    	
    	ShareMovieRequest req = new ShareMovieRequest();
    	
    	DataHandler handler = (DataHandler) httpRequest.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
    	req.setPtn(handler.getClientInfo(DataHandler.KEY_USERACCOUNT));
        TxNode body = handler.getAJAXBody();
        String details = body.msgAt(0);
        
        JSONObject joDetails = new JSONObject(details);
        req.setMovieName(joDetails.getString(RRKey.M_NAME));
        req.setTheaterName(joDetails.getString(RRKey.M_THEATER_NAME));
        
        String addressStr = joDetails.getString(RRKey.M_THEATER_ADDRESS);
        addressStr = addressStr.replaceAll("_", "\"");
        logger.debug("ShareMovieRequestParser.addressJSON:" + addressStr);
        JSONObject joAddress = new JSONObject(addressStr);
        Stop addr = Util.convertAddress(joAddress);
        req.setAddress(addr);
        
        String userID = handler.getClientInfo(DataHandler.KEY_USERID);
        if("".equals(userID) || "0".equals(userID))
        {
            userID = "3817798466";
        }
        
        req.setUserId(Long.parseLong(userID));
        
        String recepients = body.msgAt(1);
        JSONObject joRecepients = new JSONObject(recepients);
        if (joRecepients.has(RRKey.SM_RECIPIENT)){
        	// only one number
        	String phone = joRecepients.getString(RRKey.SM_RECIPIENT);
        	phone = Util.stripPhoneNumber(phone);
        	req.setRecipients(new String[]{phone});
        }else{
        	// multiple numbers
        	int count = Integer.parseInt(joRecepients.getString(RRKey.SM_RECIPIENT_NUMBER));
        	String[] recipients = new String[count];
        	for(int i=0; i< count; i++){
        		recipients[i] = Util.stripPhoneNumber(joRecepients.getString(RRKey.SM_RECIPIENT + i));
        	}
        	req.setRecipients(recipients);
        }
        
        return req;
    }
}
