/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 11, 2008
 * File name: ChangeTheaterPage.java
 * Package name: com.telenav.j2me.browser.action
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 3:39:13 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.action;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.browser.movie.Constants;
import com.telenav.browser.movie.datatype.GeoCircle;
import com.telenav.browser.movie.datatype.Theater;
import com.telenav.browser.movie.exception.MovieException;
import com.telenav.browser.movie.service.ManagerFactory;
import com.telenav.browser.movie.service.MoviesManager;
import com.telenav.browser.movie.util.MovieThunderUtil;
import com.telenav.browser.movie.util.MovieUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author dysong (dysong@telenav.cn) 3:39:13 PM, Nov 11, 2008
 */
public class ChangeTheaterPage extends BrowserBaseAction {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telenav.j2me.browser.action.BrowserBaseAction#doAction(org.apache
     * .struts.action.ActionMapping, com.telenav.tnbrowser.util.DataHandler,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    protected ActionForward doAction(ActionMapping mapping,
            DataHandler handler, HttpServletRequest request,
            HttpServletResponse response) throws MovieException {

        // Check whether this page is from wrap.
        if (!"true".equals(request.getParameter("theaterFromWrap"))) {
            // Theater id array.
            TxNode theaterIdsNode = (TxNode) handler
                    .getShareData(Constants.THEATER_ID_ARRAY);
            if(theaterIdsNode != null)
            {
	            StringBuffer sb = new StringBuffer();
	            sb.append(theaterIdsNode.msgAt(0));
	            for (int i = 1; i < theaterIdsNode.msgsSize(); i++) {
	                sb.append(",").append(theaterIdsNode.msgAt(i));
	            }
	            request.setAttribute("theaterIds", sb.toString());
            }
            // Set total size.
            request.setAttribute("theaterPagesAmount",
                    new Integer(MovieUtil.pageAmount(theaterIdsNode.msgsSize(),
                            Constants.PAGE_SIZE)));

            request.setAttribute("theaterTimestamp", request
                    .getParameter("theaterTimestamp"));
            
            String pageNo = MovieUtil.filterLastPara(request.getParameter("theaterPageNumber"));
            
            request.setAttribute("theaterStartPage", new Integer(pageNo));
            request.setAttribute("lon", request.getParameter("lon"));
            request.setAttribute("lat", request.getParameter("lat"));
            request.setAttribute("radius", request.getParameter("radius"));
            request.setAttribute("circle", request.getParameter("circle"));
            request.setAttribute("date", request.getParameter("date"));
            request.setAttribute("location", request.getParameter("location"));
            return mapping.findForward("wrap");
        }

        long[] theaterIdArray;
        int theaterPageNumber;
        String theaterTimestamp;
        long lat;
        long lon;
        int radius;
        // Get parameters.
        String pageNo = MovieUtil.filterLastPara(request.getParameter("theaterPageNumber"));
        theaterPageNumber = Integer.parseInt(pageNo);
        theaterTimestamp = request.getParameter("theaterTimestamp");
        lat = Long.parseLong(request.getParameter("lat"));
        lon = Long.parseLong(request.getParameter("lon"));
        radius = Integer.parseInt(request.getParameter("radius"));

        String[] split = request.getParameter("theaterIds").split(",");
        theaterIdArray = new long[split.length];
        for (int i = 0; i < split.length; i++) {
            theaterIdArray[i] = Long.parseLong(split[i]);
        }

        request.setAttribute("theaterTimestamp", theaterTimestamp);
        request.setAttribute("theaterPageNumber", theaterPageNumber + "");
        request.setAttribute("lon", lon + "");
        request.setAttribute("lat", lat + "");
        request.setAttribute("radius", radius + "");

        // Set pagination information.
        request.setAttribute("currentPage", new Integer(theaterPageNumber));
        if (theaterPageNumber > 0) {
            request.setAttribute("previousPage", new Boolean(true));
        } else {
            request.setAttribute("previousPage", new Boolean(false));
        }

        int theaterPageSize = Constants.PAGE_SIZE;
        if (theaterPageSize >= theaterIdArray.length - theaterPageNumber
                * Constants.PAGE_SIZE) {
            theaterPageSize = theaterIdArray.length - theaterPageNumber
                    * Constants.PAGE_SIZE;
            request.setAttribute("nextPage", new Boolean(false));
        } else {
            request.setAttribute("nextPage", new Boolean(true));
        }

        // Get page theaters
        long[] pageIdArray = new long[theaterPageSize];
        for (int i = 0; i < theaterPageSize; i++) {
            pageIdArray[i] = theaterIdArray[i + theaterPageNumber
                    * Constants.PAGE_SIZE];

        }

        // Set amount of theater pages
        request.setAttribute("theaterPagesAmount", new Integer(MovieUtil
                .pageAmount(theaterIdArray.length, Constants.PAGE_SIZE)));

        // Get page theaters
        MoviesManager moviesManager = ManagerFactory.getMoviesManager();
        List pageTheaters = moviesManager.getTheatersByIds(pageIdArray);
        // Get lat lon.
        GeoCircle circle = new GeoCircle();
        circle.setLat(((double) lat) / GeoCircle.MULTIPLIER);
        circle.setLon(((double) lon) / GeoCircle.MULTIPLIER);
        circle.setRadius(radius);
        // Compute distance
        for (Iterator iterator = pageTheaters.iterator(); iterator.hasNext();) {
            Theater theater = (Theater) iterator.next();
            theater.setReferencePoint(circle.getLat(), circle.getLon());
        }
        // sort by distance asc
        pageTheaters = MovieUtil.sortTheaters(pageTheaters);
        //if(MovieThunderUtil.isThunder(handler))
        //{
            request.setAttribute("circle", request.getParameter("circle"));
            request.setAttribute("date", request.getParameter("date"));
            request.setAttribute("location", request.getParameter("location"));
            //date
            String strDate = request.getParameter("date");
            if(strDate != null)
            {
	            Date date = new Date(Long.parseLong(strDate));
		        request.setAttribute("theaterIds",request.getParameter("theaterIds"));
		    	//get search title
				StringBuffer title = new StringBuffer();
				title.append("Where:");
				title.append((String)request.getParameter("location"));
				title.append("\n");
				title.append("When:");
				title.append(MovieThunderUtil.getShortDate(date));
				//
				request.setAttribute("title",title.toString());
            }
        //}
        request.setAttribute("pageTheaters", pageTheaters);
        return mapping.findForward("success");
    }
}
