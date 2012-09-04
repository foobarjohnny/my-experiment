/*
 *  @file BrowserProtocolRequestParser.java	
 *  @copyright (c) 2008 Telenav, Inc.
 */
package com.telenav.cserver.browser.framework.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * TODO move to container?
 * 
 * @author zywang
 * @version 1.0 2009-4-14
 */

public abstract class BrowserProtocolRequestParser implements
        ProtocolRequestParser {

    @SuppressWarnings("static-access")
    public final ExecutorRequest[] parse(Object object)
            throws ExecutorException {
        HttpServletRequest request = (HttpServletRequest) object;

        // Get user profile from browser request.
        DataHandler handler = (DataHandler) request
                .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);

        ExecutorRequest browserRequest = null;
        try {
            // Get the browser request.
            browserRequest = parseBrowserRequest(request);

            // Set the executor type
            browserRequest.setExecutorType(getExecutorType());

            // Set the user profile.
            UserProfile userProfile = new UserProfile();
			userProfile.setMin(handler.getClientInfo(DataHandler.KEY_USERACCOUNT));  
			String userId = handler.getClientInfo(DataHandler.KEY_USERID);
			if(userId == null || "".equals(userId.trim()))
			{
				userId = "0";
			}
            userProfile.setUserId(userId);
            userProfile.setVersion(handler.getClientInfo(DataHandler.KEY_VERSION));
            userProfile.setPassword(handler.getClientInfo(DataHandler.KEY_USERPIN));
            userProfile.setPlatform(handler.getClientInfo(DataHandler.KEY_PLATFORM));
            userProfile.setDevice(handler.getClientInfo(DataHandler.KEY_DEVICEMODEL));
            userProfile.setLocale(handler.getClientInfo(DataHandler.KEY_LOCALE));
            userProfile.setCarrier(handler.getClientInfo(DataHandler.KEY_CARRIER));
            userProfile.setProduct(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));
            userProfile.setRegion(handler.getClientInfo(DataHandler.KEY_REGION));
            userProfile.setScreenWidth(handler.getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH));
            userProfile.setScreenHeight(handler.getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT));
            userProfile.setGuideTone(handler.getClientInfo(DataHandler.KEY_GUIDETONE));
            userProfile.setProgramCode(userProfile.getCarrier());
            
            browserRequest.setUserProfile(userProfile);
        } catch (Exception e) {
            // TODO Rain We should not print stack trace
            e.printStackTrace();
            throw new ExecutorException(e);
        }
        // Parse detail request.
        return new ExecutorRequest[] { browserRequest };
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
