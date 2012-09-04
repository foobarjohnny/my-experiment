/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * LoadImageExecutor.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Dec 28, 2010
 *
 */
public class LoadImageExecutor  extends AbstractExecutor
{
            
        public void doExecute(ExecutorRequest request, ExecutorResponse response, ExecutorContext context) throws ExecutorException
        {
            LoadImageRequest loadImageRequest = (LoadImageRequest) request;
            LoadImageResponse loadImageResponse = (LoadImageResponse) response;
            String[] movieIds = loadImageRequest.getMovieIds();
            String[] images = MovieServiceProxy.loadImages(movieIds,loadImageRequest.getHeight(),loadImageRequest.getWidth());

            for(int i=0; i<movieIds.length; i++)
            {
                loadImageResponse.getImages().put(movieIds[i], images[i] != null ? images[i] : "null");
            }
            
        }

}
