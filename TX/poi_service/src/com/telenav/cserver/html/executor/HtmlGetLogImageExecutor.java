/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * @TODO	Call the executor to implement business logic
 * @author jhjin@telenav.cn
 * @version 1.0 Feb 22, 2011
 */
public class HtmlGetLogImageExecutor extends AbstractExecutor
{

    private static Logger logger = Logger.getLogger(HtmlGetLogImageExecutor.class);

    @Override
    public void doExecute(ExecutorRequest executorRequest, ExecutorResponse executorResponse, ExecutorContext context)
            throws ExecutorException
    {
         HtmlGetLogImageResponse response = (HtmlGetLogImageResponse)executorResponse;
         HtmlGetLogImageRequest request = (HtmlGetLogImageRequest)executorRequest;
         response.setImageName(request.getImageName());
         
         String imageName = request.getImageName();
         //
         if(!"".equals(request.getTheme()))
         {
        	 imageName = request.getTheme() + imageName;
         }
         logger.info("logo path:" + imageName);
         response.setImage(HtmlPoiDetailServiceProxy.getInstance().getLogoImage(imageName, request.getWidth(), request.getHeight()));
    }
    


}
