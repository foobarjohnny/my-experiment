package com.telenav.browser.movie.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.browser.MovieBaseAction;
import com.telenav.browser.movie.Constant;
import com.telenav.browser.movie.Util;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.datatypes.content.movie.v10.Movie;
import com.telenav.datatypes.content.movie.v10.MovieListing;
import com.telenav.datatypes.content.movie.v10.MovieSearchDate;
import com.telenav.datatypes.content.movie.v10.MovieServiceResponse;
import com.telenav.datatypes.content.movie.v10.MovieSortTypeEnum;
import com.telenav.datatypes.content.movie.v10.Schedule;
import com.telenav.datatypes.content.movie.v10.ScheduleServiceResponse;
import com.telenav.datatypes.content.movie.v10.TheaterInfo;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.services.content.moviesearchservice.v10.MovieLookupRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchServiceStub;
import com.telenav.services.content.moviesearchservice.v10.ScheduleLookupRequestDTO;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.ws.datatypes.services.ResponseStatus;

public class ShowMovies extends MovieBaseAction{
	private static Logger logger = Logger.getLogger(ShowMovies.class);   
	public ActionForward doAction(ActionMapping mapping,
	            HttpServletRequest request, HttpServletResponse response)
	            throws Exception {
	        
	        DataHandler handler = (DataHandler) request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);

	        // get values for current location lat and lon, and also distUnit
	        TxNode node = (TxNode) handler.getParameter("anchorLat");
	        long anchorLat = node!=null? node.valueAt(0) : -1;
	    
	        node = (TxNode) handler.getParameter("anchorLon");
	        long anchorLon = node!=null? node.valueAt(0) : -1;
	    
	        node = (TxNode) handler.getParameter("distUnit");
	        long distUnit = node!=null? node.valueAt(0) : 1;

	        logger.debug("Lat:" + anchorLat + " Lon:" + anchorLon + "Dist Unit:" + distUnit);

	        // convert distUnit to mi or km
	        String scale = "mi";
	        if (distUnit != Constant.DUNIT_MILES) {
	        	scale = "km";
	        }

	        // set attributes for anchor lat, lon, distUnit and scale
	        request.setAttribute("anchorLat", anchorLat);
	        request.setAttribute("anchorLon", anchorLon);
	        request.setAttribute("distUnit",  distUnit);
	        request.setAttribute("scale",     scale);
	        
	        String theaterId = null;
	        String dateIndex = "0";
	        node = (TxNode) handler.getParameter("theaterId");
        	if (node != null){
        		theaterId = node.msgAt(0); // from POI_search
        		if (theaterId == null) {
        			theaterId = "" + node.valueAt(0); //from same page
        		}
        	}
	        
	        node = (TxNode) handler.getParameter("dateIndex");
	        if (node != null){
	        	dateIndex = node.msgAt(0); 
	        }
	        
	        MovieSortTypeEnum sortBy = MovieSortTypeEnum.ALPHABET; 
	        node = (TxNode) handler.getParameter("sortType");
	        if (node != null && "0".equals("" + node.valueAt(0))){
	        	logger.debug("Real Sort=" + node.valueAt(0));
	        	sortBy = MovieSortTypeEnum.RANK;
	        }
	        request.setAttribute("SortType", sortBy);
	        
	        logger.debug("ShowMovies. TheaterId:" + theaterId + " Sort=" + sortBy + " date" + dateIndex);
	        
	        MovieSearchDate searchDate = Util.getSearchDate(dateIndex);
	        request.setAttribute("dateDisplay", Util.getDisplayDate(searchDate,handler.getClientInfo(DataHandler.KEY_LOCALE))); 
	        
	        long tId = Long.parseLong(theaterId);
	        
	        MovieSearchServiceStub stub = null;
	        try{
	        	stub = Util.getService();
		        // lookup movies for the theater
		        MovieLookupRequestDTO moviesInTheaterReq = new MovieLookupRequestDTO();
		        moviesInTheaterReq.setDate(searchDate);
		        moviesInTheaterReq.setTheaterPoiId(tId);
		        moviesInTheaterReq.setSortType(sortBy);
		        moviesInTheaterReq.setPageNumber(1);
		        moviesInTheaterReq.setPageLength(20);
		        Util.setClientProps(moviesInTheaterReq);
		        
		        MovieServiceResponse mResp = stub.lookupMoviesInTheatre(moviesInTheaterReq);
	        	ResponseStatus status = mResp.getResponseStatus();
	        	logger.debug("ShowMovies.lookupMoviesInTheatre(). Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
	        	MovieListing[] mList = mResp.getMovieListing();

	        	//Movie[] movies = Util.generateMovies();
	        	Movie[] movies = null;
	        	if (mList != null && mList.length > 0){
	        		TheaterInfo[] info = mList[0].getTheaterInfo();
	        		if (info != null && info.length > 0){
	        			tId = info[0].getPoiId();
	        		}
	        		movies = new Movie[mList.length];
	        		for (int i = 0; i < mList.length; i++) {
	        			movies[i] = mList[i].getMovie(); 
					}
	        	}else{
	        	    request.setAttribute("theaterId", tId + "");
	                return mapping.findForward("noMovies");
	        	}
	        	request.setAttribute("theaterId", tId + "");
	        	
		        request.setAttribute("Movies", movies);
		        
		        String[] movieIds = new String[movies.length];
		        for (int i = 0; i < movies.length; i++) {
					movieIds[i] = movies[i].getId();
				}
		        
		        //Map<String,Schedule> schedules = Util.generateSchedules(movieIds);
		        Map<String,Schedule> schedules = new HashMap<String,Schedule>();
		        boolean onlineTickets = true;
		        
		        // lookup schedules for movies
		        ScheduleLookupRequestDTO scheduleRequest = new ScheduleLookupRequestDTO();
		        scheduleRequest.setDate(searchDate);
		        scheduleRequest.setMovieId(movieIds);
		        scheduleRequest.setTheaterPoiId(new long[]{tId});
		        Util.setClientProps(scheduleRequest);
		        
		        ScheduleServiceResponse sResp = stub.lookupSchedules(scheduleRequest);
	        	status = sResp.getResponseStatus();
	        	logger.debug("lookupSchedules() Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
	        	Schedule[] listS = sResp.getSchedule();
	        	if (listS != null){
	        		schedules = new HashMap<String, Schedule>();
	        		for (int i = 0; i < listS.length; i++) {
						String mId = listS[i].getMovieId();
						logger.debug("MovieId:" + mId + " ScheduleId:" + listS[i].getId());
						if (!onlineTickets && listS[i].getTicketURI() != null) onlineTickets = true; // has tickets online
						schedules.put(mId, listS[i]);
					}
	        	}
	        	
		        request.setAttribute("Schedules", schedules);
		        if(Util.isCanadianCarrier(handler)){
		        	onlineTickets = false;
		        }
		        request.setAttribute("HasTickets", onlineTickets);
		        
	        }catch(Exception ex){
		        request.setAttribute("ErrorMessage", "COSE.notAvailable");
	        	return mapping.findForward("failure");
	        }
	        finally {
				WebServiceUtils.cleanupStub(stub);
			}
	        
	        return mapping.findForward("success");
	    }
}
