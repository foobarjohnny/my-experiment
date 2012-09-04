/*
 *  @file BrowserProtocolRequestParser.java	
 *  @copyright (c) 2008 Telenav, Inc.
 */
package com.telenav.cserver.framework.html.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * 
 * @author panzhang
 * @version 1.0 2010-12-09
 */

public abstract class HtmlProtocolRequestParser implements
        ProtocolRequestParser {

    @SuppressWarnings("static-access")
    public final ExecutorRequest[] parse(Object object)
            throws ExecutorException {
        HttpServletRequest request = (HttpServletRequest) object;
        ExecutorRequest browserRequest = null;
        try {
            // Get the browser request.
            browserRequest = parseBrowserRequest(request);

            // Set the executor type
            browserRequest.setExecutorType(getExecutorType());

            
            UserProfile userProfile = createUserProfile(request);
            browserRequest.setUserProfile(userProfile);
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
            throw new ExecutorException(e);
        }
        // Parse detail request.
        return new ExecutorRequest[] { browserRequest };
    }
    
	/**
	 * Convert HtmlClientInfo object into UserProfile object, and this is final
	 * method, sub-calss can't override this. If you want to make any further
	 * change to UserProfile, need to handle it in postCreateUserProfile
	 * 
	 * @param request
	 * @return UserProfile, which will be used in Executor system
	 */
    public final UserProfile createUserProfile(HttpServletRequest request)
    {
    	  // Get user profile from browser request.
        HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
    	// Set the user profile.
        UserProfile userProfile = new UserProfile();
        userProfile.setPassword("");
        userProfile.setUserId(clientInfo.getUserId());
        userProfile.setVersion(clientInfo.getVersion());
        userProfile.setPlatform(clientInfo.getPlatform());
        userProfile.setDevice(clientInfo.getDevice());
        userProfile.setLocale(clientInfo.getLocale());
        userProfile.setCarrier(clientInfo.getCarrier());
        userProfile.setDeviceCarrier(clientInfo.getDeviceCarrier());
        userProfile.setProduct(clientInfo.getProduct());
        userProfile.setRegion(clientInfo.getRegion());
        userProfile.setProgramCode(clientInfo.getProgramCode());
        userProfile.setBuildNumber(clientInfo.getBuildNo());
        userProfile.setScreenWidth(clientInfo.getWidth() + "-" + clientInfo.getHeight());
        userProfile.setScreenHeight(clientInfo.getHeight() + "-" + clientInfo.getWidth());
        userProfile.setMin("");    	
//    	String ssoTokenStr = HtmlCommonUtil.getString(request.getParameter("ssoToken"));
//    	if(!"".equals(ssoTokenStr))
//    	{
//    		 userProfile.setUserId(HtmlCommonUtil.getUserId(ssoTokenStr));
//    	}
        postCreateUserProfile(userProfile,request);
        return userProfile;
    }
    
    /**
     * It provide a way to modify the UserProfile after its creation, like injecting ptn.
     * @param userProfile
     * @param request
     */
    protected void postCreateUserProfile(UserProfile userProfile,HttpServletRequest request)
    {
    	
    }

    /**
     * Parse the <code>ExecutorRequest</code> from
     * <code>HttpServletRequest</code>
     * 
     * @param request
     * @return
     * @throws Exception
     */
    public abstract ExecutorRequest parseBrowserRequest(
            HttpServletRequest request) throws Exception;

    /**
     * Get the execution type of the request.
     * 
     * @return
     */
    public abstract String getExecutorType();

    /**
     * Get the String from the handler.
     * 
     * @param handler The data handler.
     * @param key Key of the parameter.
     * @return The String
     */
    protected String getStringParm(DataHandler handler, String key) {
        TxNode parameter = (TxNode) handler.getParameter(key);
        if (parameter == null) {
            return "";
        }

        String msg = parameter.msgAt(0);
        if (msg == null) {
            return "";
        }

        return msg;
    }

}
