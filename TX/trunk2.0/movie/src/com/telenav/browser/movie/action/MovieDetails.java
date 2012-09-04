/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 15, 2008
 * File name: Startup.java
 * Package name: com.telenav.j2me.browser.action
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 4:59:19 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.action;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.browser.common.resource.holder.LayoutProperties;
import com.telenav.browser.movie.Constants;
import com.telenav.browser.movie.datatype.Movie;
import com.telenav.browser.movie.datatype.MovieImage;
import com.telenav.browser.movie.datatype.Theater;
import com.telenav.browser.movie.exception.MovieException;
import com.telenav.browser.movie.service.ManagerFactory;
import com.telenav.browser.movie.service.MoviesManager;
import com.telenav.browser.movie.util.MovieUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author lwei (lwei@telenav.cn) 4:59:19 PM, Oct 15, 2008
 */
public class MovieDetails extends BrowserBaseAction {

    protected ActionForward doAction(ActionMapping mapping,
            DataHandler handler, HttpServletRequest request,
            HttpServletResponse response) throws MovieException {

        // Get movie
        TxNode idNode = (TxNode) handler.getParameter("movieId");
        long movieId = idNode.valueAt(0);
        MoviesManager moviesManager = ManagerFactory.getMoviesManager();
        List movies = moviesManager.getMoviesByIds(new long[] { movieId });
        Movie movie = (Movie) movies.get(0);
        request.setAttribute("movie", movie);
        // Get movie big image [FOR NORMAL CASE]
        if (movie != null) {
            MovieImage bigImage = getMovieImage(movie.getBigImage(),handler,request);
            if (bigImage != null) {
                request.setAttribute("bigImage", bigImage);
            }
        }
		// Get movie small image [FOR THUNDER]
        //if(MovieThunderUtil.isThunder(handler))
        //{
	        // Get movie small image
	        if (movie != null) {
	        	MovieImage smallImage = getMovieImage(movie.getSmallImage(),handler,request);
	            if (smallImage != null) {
	                request.setAttribute("smallImage", smallImage);
	            }
	        }
        //}
	    // Set multilayout images into request  [FOR ANDROID]
		if (movie != null) {
			setMultiMovieImage(movie.getBigImage(), handler, request, "big");
		}
        // When
        TxNode dateNode = (TxNode) handler.getShareData(Constants.MOVIES_DATE);
        Date date = new Date(Long.parseLong(dateNode.msgAt(1)));
        request.setAttribute("date", Constants.DATE_FORMAT.format(date));

        // Check the movie ticket is available in at least one theater
        // TODO create new Dao ?

        // Where(Theaters)
        TxNode theaterIdArrayNode = (TxNode) handler
                .getShareData(Constants.THEATER_ID_ARRAY);
        long[] theaterIds = new long[theaterIdArrayNode.msgsSize()];
        for (int i = 0; i < theaterIds.length; i++) {
            theaterIds[i] = Long.parseLong(theaterIdArrayNode.msgAt(i));
        }

        Map scheduleMap = moviesManager.searchScheduleByMovie(movieId,
                theaterIds, date);
		boolean ticketAvailable = MovieUtil.checkTicketAvailabe(scheduleMap)
				&& !MovieUtil.isDisableLinkDevice(handler);
        
        request.setAttribute("ticketAvailable", new Boolean(ticketAvailable));

        // Check ticket availabe

        // Check the flow.
        idNode = (TxNode) handler.getParameter("theaterId");
        long theaterId = 0;
        Theater theater = null;
        if (idNode != null) {
            theaterId = idNode.valueAt(0);

            // Get theater
            List theaters = moviesManager
                    .getTheatersByIds(new long[] { theaterId });
            theater = (Theater) theaters.get(0);
            request.setAttribute("theater", theater);
            // Get schedule
            List searchSchedule = moviesManager.searchSchedule(movieId,
                    theaterId, date);
            // Check the theater is ticketing
			boolean theaterTicketing = MovieUtil
					.checkTheaterTicketing(searchSchedule)
					&& !MovieUtil.isDisableLinkDevice(handler);

            request.setAttribute("theaterTicketing", new Boolean(
                    theaterTicketing));
            String showTime = MovieUtil.composeShowTimeAm(searchSchedule);

            // Store shortDate
            request.setAttribute("shortDate", Constants.SHORT_DATE_FORMAT
                    .format(date));

            request.setAttribute("showTime", showTime);
        }

        return mapping.findForward("success");
    }
}
