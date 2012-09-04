/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 28, 2008
 * File name: BrowserBaseAction.java
 * Package name: com.telenav.j2me.browser.action
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 4:37:03 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.PropertyResourceBundle;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.browser.common.datatype.BSUserInfoWrapper;
import com.telenav.browser.common.resource.holder.LayoutHolder;
import com.telenav.browser.common.resource.holder.LayoutProperties;
import com.telenav.browser.movie.datatype.MessageWrap;
import com.telenav.browser.movie.datatype.MovieImage;
import com.telenav.browser.movie.exception.MovieException;
import com.telenav.browser.movie.service.ManagerFactory;
import com.telenav.browser.movie.service.MoviesManager;
import com.telenav.browser.movie.util.MovieThunderUtil;
import com.telenav.cli.client.CliTransaction;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.server.entity.UserInfoWrapper;
import com.telenav.j2me.server.resource.ErrorMessageHolder;
import com.telenav.tnbrowser.util.DataHandler;
import com.televigation.log.TVCategory;

/**
 * @author lwei (lwei@telenav.cn) 4:37:03 PM, Oct 28, 2008
 */
public abstract class BrowserBaseAction extends Action {
    private static final TVCategory logger = (TVCategory) TVCategory
            .getInstance(BrowserBaseAction.class);
	private static final PropertyResourceBundle serverBundle = (PropertyResourceBundle) PropertyResourceBundle
			.getBundle("config.movie");

    /**
     * override from Action
     */
    public final ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        return (_execute(mapping, form, request, response));

    }

    /**
     * override from Action
     */
    public final ActionForward execute(ActionMapping mapping, ActionForm form,
            ServletRequest request, ServletResponse response) throws Exception {
        return (_execute(mapping, form, (HttpServletRequest) request,
                (HttpServletResponse) response));
    }

    private ActionForward _execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // CLI
        CliTransaction cli = new CliTransaction(CliTransaction.TYPE_URL);
        cli.setApplicationId("Movies");
        cli.setFunctionName(request.getRequestURI());

        try {
            // Data handler.
            DataHandler handler = new DataHandler(request);
            request.setAttribute("DataHandler", handler);
            // Host url
			String requestURL = request.getRequestURL().toString();
			String servletPath = request.getServletPath();
			request.setAttribute("Host_url", getHostURL(requestURL, servletPath));
            // Layout.
            LayoutHolder layoutHolder = LayoutHolder.getInstance();
            UserInfoWrapper userInfo = new BSUserInfoWrapper(handler);
            Collection layoutSupported = layoutHolder.getLayoutSupported(userInfo);
            request.setAttribute("MoviesLayout", layoutSupported);
            
            //Message
    	    ErrorMessageHolder holder = ErrorMessageHolder.getInstance();
    	    if(holder != null)
    	    {
    	    	Map message = holder.getAllStrings(userInfo);
    	    	if(message != null)
    	    	{
    	    		MessageWrap messageWrap = new MessageWrap(message);
    	    		request.setAttribute("message", messageWrap);
    	    	}
    	    }
            
            // Do action.
            return doAction(mapping, handler, request, response);
        } catch (Exception e) {
            cli.setStatus(e);
            logger.error("error:", e);
            return mapping.findForward("Globe_Exception");
        } finally {
            cli.complete();
        }
    }
    
    /**
     * Abstract method do the action.
     * 
     * @throws MovieException
     */
    protected abstract ActionForward doAction(ActionMapping mapping,
            DataHandler handler, HttpServletRequest request,
            HttpServletResponse response) throws MovieException;
    
    /**
     * 
     * @param imageString
     * @param handler
     * @param request
     * @return
     * @throws MovieException 
     */
    protected MovieImage getMovieImage(String imageString,DataHandler handler, HttpServletRequest request) throws MovieException
    {
    	MoviesManager moviesManager = ManagerFactory.getMoviesManager();
        MovieImage smallImage = null;
        if (imageString != null) {
            String height = handler.getClientInfo(DataHandler.KEY_HEIGHT);
            String width = handler.getClientInfo(DataHandler.KEY_WIDTH);
            //if it's thunder, get the height/width from layout file
            if(MovieThunderUtil.isThunder(handler))
            {
            	//currently suppose dual layout will use the same image
            	Collection layoutSupported1 = (Collection) request.getAttribute("MoviesLayout");
            	//set screen default value
            	java.util.Iterator iterator = layoutSupported1.iterator();
            	
            	LayoutProperties layout = (LayoutProperties) iterator.next();
            	height = layout.getIntProperty("movies.info.showtimes.pic.max.height") + "";
            	width = layout.getIntProperty("movies.info.showtimes.pic.max.width") + "";
            }
			// if movie of this size(screen size) hasn't been stored in
			// database(support.movie.image.in.screen.size), it will use common
			// movie image(indicating that this device of screen size support
			// automatic size).
			String screenSize = width + "x" + height;
			String supportScreenSize = serverBundle
					.getString("support.movie.image.in.screen.size");
			if (supportScreenSize.indexOf(screenSize) == -1) {
				String commonScreenSize = serverBundle
						.getString("common.movie.image.in.screen.size");
				width = commonScreenSize.split("x")[0];
				height = commonScreenSize.split("x")[1];
			}
            smallImage = moviesManager.getMovieImage(imageString,
                    width, height);
        }
        
        return smallImage;
    }
    /**
     * 
     * @param imageString
     * @param handler
     * @param request
     * @throws MovieException
     */
	protected void setMultiMovieImage(String imageString, DataHandler handler, HttpServletRequest request, String prefix) throws MovieException 
	{
		MoviesManager moviesManager = ManagerFactory.getMoviesManager();
		MovieImage image = null;
		if (imageString != null) {
			String height = null;
			String width = null;
			Collection layoutSupported = (Collection) request.getAttribute("MoviesLayout");
			if (null != layoutSupported) {
				for (Iterator iterator = layoutSupported.iterator(); iterator
						.hasNext();) {
					LayoutProperties layout = (LayoutProperties) iterator.next();
					width = layout.getIntProperty("screen.width") + "";
					height = layout.getIntProperty("screen.height") + "";
					String key = prefix + "MultiImage_" + width + "x" + height;
					// if movie of this size(screen size) hasn't been stored in
					// database(support.movie.image.in.screen.size), it will use
					// common movie image(indicating that this device of screen
					// size
					// support automatic size).
					String screenSize = width + "x" + height;
					String supportScreenSize = serverBundle
							.getString("support.movie.image.in.screen.size");
					if (supportScreenSize.indexOf(screenSize) == -1) {
						String commonScreenSize = serverBundle
								.getString("common.movie.image.in.screen.size");
						width = commonScreenSize.split("x")[0];
						height = commonScreenSize.split("x")[1];
					}
					image = moviesManager.getMovieImage(imageString, width,
							height);
					if (image != null) {
						request.setAttribute(key, image);
					}
				}
			}
		}
        
	}
    
	/**
	 * 
	 * @param key
	 * @return
	 */
	protected String getApplicationString(String key,HttpServletRequest request)
	{
		MessageWrap message = (MessageWrap)request.getAttribute("message");
		
		return (String)message.get(key);
	}
	
    /**
     * 
     * @param origin
     * @return
     */
	protected String getStopText(Stop origin,HttpServletRequest request)
	{
		String text = getApplicationString("movies.current.location",request);
		if(origin == null) return text;
		
	    if(!"".equals(MovieThunderUtil.getString(origin.firstLine)))
	    	text =  origin.firstLine;
	    else if(!"".equals(MovieThunderUtil.getString(origin.city)))
	    	text =  origin.city; 
	    else if(!"".equals(MovieThunderUtil.getString(origin.label)))
	    	text =  origin.label; 
	    else if(origin.lat != 0)
	    	text = getApplicationString("movies.current.location",request);
	    
	    return text;
	}
	
	/**
	 * 
	 * @param requestURL
	 * @param servletPath
	 * @return host url
	 */
	public static String getHostURL(String requestURL, String servletPath) {
		String hostURL = "";
		int pos = 0;
		if (requestURL != null) {
			pos = requestURL.length();
			if (servletPath != null) {
				pos = requestURL.indexOf(servletPath);
			}
			if (pos != -1) {
				hostURL = requestURL.substring(0, pos);
			}
		}
		return hostURL;
	}
}
