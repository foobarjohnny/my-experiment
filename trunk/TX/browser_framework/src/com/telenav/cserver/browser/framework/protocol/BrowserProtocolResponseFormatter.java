/*
 *  @file BrowserProtocolResponseFormatter.java	
 *  @copyright (c) 2008 Telenav, Inc.
 */
package com.telenav.cserver.browser.framework.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;

/**
 * TODO move to container?
 * 
 * @author zywang
 * @version 1.0 2009-4-14
 */

public abstract class BrowserProtocolResponseFormatter implements ProtocolResponseFormatter
{

    public final void format(Object formatTarget, ExecutorResponse[] responses)
            throws ExecutorException
    {
    	HttpServletRequest httpRequest = (HttpServletRequest)formatTarget;
    	if(responses != null && responses.length > 0)
        {
    		ExecutorResponse response = responses[0];
    		try
            {
                parseBrowserResponse(httpRequest,response);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new ExecutorException(e);
            }
    		
    		//TODO Process common logics such as get supported layout.
        }
    }
    
    public abstract void parseBrowserResponse(HttpServletRequest httpRequest,ExecutorResponse responses) throws Exception;

}
