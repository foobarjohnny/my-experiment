/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.browser.movie.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.telenav.browser.movie.Constants;
import com.telenav.browser.movie.datatype.GeoCircle;
import com.telenav.browser.movie.exception.MovieException;
import com.telenav.browser.movie.util.MovieThunderUtil;
import com.telenav.browser.movie.util.MovieUtil;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;
import com.televigation.log.TVCategory;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-3-20
 */
public abstract class Search extends BrowserBaseAction{
	
    private static final TVCategory logger = (TVCategory) TVCategory
    .getInstance(Search.class);
    
    protected Map getSearchCriteria(DataHandler handler, HttpServletRequest request) throws MovieException {
        
        // Get date.
        TxNode dateNode = (TxNode) handler.getParameter("date");
        Date date = null;
        if (dateNode != null) {
            // From search
            String str = dateNode.msgAt(0);
            date = new Date(Long.parseLong(str));

            String dateStr = Constants.DATE_FORMAT.format(date);

            // Save date to share data.
            dateNode = new TxNode();
            dateNode.addMsg(dateStr);
            dateNode.addMsg(str);
            handler.addShareData(Constants.MOVIES_DATE, dateNode);
        } else {
            // From sort
            dateNode = (TxNode) handler.getShareData(Constants.MOVIES_DATE);
            if(dateNode == null)
            {
            	date = new Date();
            	String dateStr = Constants.DATE_FORMAT.format(date);
            	// Save date to share data.
                dateNode = new TxNode();
                dateNode.addMsg(dateStr);
                dateNode.addMsg(date.getTime()+"");
                handler.addShareData(Constants.MOVIES_DATE, dateNode);
            }
            else
            {
            	date = new Date(Long.parseLong(dateNode.msgAt(1)));
            }

        }
        
        TxNode location = null;
        Stop stop = null;
        //get location from GPS or for change location
        if (handler.getParameter("addrFromAC") != null) {
        	Object o1 = handler.getParameter("addrFromAC");
            if(o1 instanceof TxNode)
            {
            	location = (TxNode)o1;
            }
            else
            {
            	stop = (Stop)o1;	
            }
		}
        else if (request.getParameter("lat") != null) {
			String lat = MovieThunderUtil.getString((String)request.getParameter("lat"));
			String lon = MovieUtil.filterLastPara((String)request.getParameter("lon"));
			
			if(!"".equals(lat) && !"".equals(lon) )
			{
				stop = new Stop();
				stop.lat = Integer.parseInt(lat);
				stop.lon = Integer.parseInt(lon);
				
				System.out.println("MovieSearch.doAction() getGPS():" + stop.lat + "," + stop.lon);
			}
		}
        else if ((TxNode) handler.getParameter("currentLocation") != null) {
		    location = (TxNode) handler.getParameter("currentLocation");
		}else
		{
			//get location from sharedate
            // Where
			Object o = handler.getShareData(Constants.SEARCH_LOCATION);
			if(o != null)
			{
	            if(o instanceof TxNode)
	            {
	            	location = (TxNode)o;
	            }
	            else
	            {
	            	stop = (Stop)o;	
	            }
			}
		}
		
		if(stop == null && location != null)
		{
			stop = new Stop();
			stop.lat = (int) location.valueAt(1);
			stop.lon = (int) location.valueAt(2);
		}
		
		if(location == null && stop!= null)
		{
			location = stop.toTxNode();
		}
		
		GeoCircle circle = null;
		Map crietria = new HashMap();
		crietria.put("date", date);
		
		if(stop != null)
		{
			circle = new GeoCircle();
			circle.setRadius(Constants.SEARCH_RADIUS);
		    circle.setLat(changeLatLonFromClient(stop.lat));
		    circle.setLon(changeLatLonFromClient(stop.lon));
		    
	        // Store circle for theater page
	        handler.addShareData(Constants.SEARCH_CIRCLE, circle.toTxNode());
			logger.info("Search Criteria-Date:" + date + " , Location:" + circle.getLat() + "-" + circle.getLon());
			
			//if(MovieThunderUtil.isThunder(handler))
			//{
			    handler.addShareData(Constants.SEARCH_LOCATION, location);
			    request.setAttribute("location", getStopText(stop,request));
			    //search movie name
			    TxNode movieNameNode = (TxNode) handler.getParameter("moviename");
			    String movieName = "";
			    if(movieNameNode != null)
			    {
			    	movieName = movieNameNode.msgAt(0);
			    }
			    crietria.put("movieName", movieName);
			//}
		}

		crietria.put("circle", circle);
    	return crietria;
    }
    
    private double changeLatLonFromClient(long latLon) {
        return ((double) latLon) / 100000;
    }
}
