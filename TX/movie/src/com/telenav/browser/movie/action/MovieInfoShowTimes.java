package com.telenav.browser.movie.action;

import java.io.InputStream;
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
import com.telenav.browser.movie.datatypes.RatingImage;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.datatypes.content.movie.v10.Area;
import com.telenav.datatypes.content.movie.v10.DetailTheaterInfoServiceResponse;
import com.telenav.datatypes.content.movie.v10.Movie;
import com.telenav.datatypes.content.movie.v10.MovieImage;
import com.telenav.datatypes.content.movie.v10.MovieImageResponse;
import com.telenav.datatypes.content.movie.v10.MovieListing;
import com.telenav.datatypes.content.movie.v10.MovieSearchDate;
import com.telenav.datatypes.content.movie.v10.MovieServiceResponse;
import com.telenav.datatypes.content.movie.v10.Schedule;
import com.telenav.datatypes.content.movie.v10.ScheduleServiceResponse;
import com.telenav.datatypes.content.movie.v10.TheaterSortTypeEnum;
import com.telenav.datatypes.content.tnpoi.v10.TnPoi;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.services.content.moviesearchservice.v10.MovieDetailsRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieImageRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchServiceStub;
import com.telenav.services.content.moviesearchservice.v10.ScheduleLookupRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.TheaterLookupRequestDTO;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.tnbrowser.util.Utility;
import com.telenav.ws.datatypes.services.ResponseStatus;

public class MovieInfoShowTimes extends MovieBaseAction {
	private static Logger logger = Logger.getLogger(MovieInfoShowTimes.class);
	public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        DataHandler handler = (DataHandler) request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
        
        TxNode node = (TxNode) handler.getParameter("movieId");
        String movieId = node!=null? node.msgAt(0) : null;
	    
        node = (TxNode) handler.getParameter("dateIndex");
	    String dateIdx = node!=null? node.msgAt(0) : "0";
	
	    // get values for current location lat and lon, and also distUnit
	    node = (TxNode) handler.getParameter("anchorLat");

        // done for 6.0 compatibility, as it uses different variables for jsp pages
		if(node == null)
		{
			node = (TxNode) handler.getParameter("lat");
		}

	    long anchorLat = node!=null? node.valueAt(0) : -1;
	    
	    node = (TxNode) handler.getParameter("anchorLon");

        // done for 6.0 compatibility, as it uses different variables for jsp pages
		if(node == null)
		{
			node = (TxNode) handler.getParameter("lon");
		}

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

        // done for 6.0 compatibility, as it uses different variables for jsp pages
        request.setAttribute("address_lat", anchorLat);
        request.setAttribute("address_lon", anchorLon);    

       //Movie movie = Util.generateMovieShort();
        Movie movie = null;
        
        MovieDetailsRequestDTO mReq = new MovieDetailsRequestDTO();
        mReq.setMovieId(movieId);
        Util.setClientProps(mReq);
        
        MovieSearchServiceStub stub = null;
        try{
        	stub = Util.getService();
        	MovieServiceResponse mResp = stub.getMovieByID(mReq);
        	ResponseStatus status = mResp.getResponseStatus();
        	logger.debug("MovieInfoShowTimes.getMovieByID(). Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
        	MovieListing[] mList = mResp.getMovieListing();
        	if (mList != null){
	        	movie = mList[0].getMovie();
        	}else{
        		// it should be movie description
        		request.setAttribute("ErrorCode", "NO_MOVIE_DESC");
        		request.setAttribute("ErrorMessage", "No movie description for ID:" + movieId);
                return mapping.findForward("failure");
        	}
	        request.setAttribute("MovieDetails", movie);
	        
	        double rating = Double.parseDouble(movie.getRating());
	        RatingImage rImage = RatingImage.selectImage((int)(rating/Constant.RATEUNIT));
	        request.setAttribute("rImage", rImage);
	        
	        MovieSearchDate searchDate = Util.getSearchDate(dateIdx);
	        request.setAttribute("dateDisplay", Util.getDisplayDate(searchDate,handler.getClientInfo(DataHandler.KEY_LOCALE))); 
	        
	        TheaterLookupRequestDTO tReq = new TheaterLookupRequestDTO();
	        tReq.setDate(searchDate);
	        Area area = Util.getArea(anchorLat, anchorLon, Constant.SEARCH_RADIUS);
	        tReq.setArea(area);
	        tReq.setMovieId(movieId);
	       	tReq.setSortType(TheaterSortTypeEnum.DISTANCE);
	        tReq.setPageLength(25);
	        tReq.setPageNumber(1);
	        Util.setClientProps(tReq);

	        DetailTheaterInfoServiceResponse tResp = stub.lookupTheatresWithDetailInfo(tReq);
        	status = tResp.getResponseStatus();
        	logger.debug("MovieInfoShowTimes.lookupTheatres() Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
	        
        	TnPoi[] theaters = tResp.getDetailTheaterInfo();
 	        long[] tIds = null;
        	if (theaters != null){
        		tIds = new long[theaters.length];
 		        for (int i = 0; i < theaters.length; i++) {
 					tIds[i] = theaters[i].getPoiId();
 			        logger.debug("TheaterID:" + tIds[i]);
 				}
		        request.setAttribute("Theaters", theaters);
	        }
	        
	        //Map<String,Schedule> schedules = Util.generateSchedules(tIds);
	        Map<String,Schedule> schedules = new HashMap<String,Schedule>();
	        boolean onlineTickets = false;
	        
	        // lookup schedules for movies
	        ScheduleLookupRequestDTO scheduleRequest = new ScheduleLookupRequestDTO();
	        scheduleRequest.setDate(searchDate);
	        scheduleRequest.setMovieId(new String[]{movieId});
	        scheduleRequest.setTheaterPoiId(tIds);
	        Util.setClientProps(scheduleRequest);
	        
	        ScheduleServiceResponse sResp = stub.lookupSchedules(scheduleRequest);
        	status = sResp.getResponseStatus();
        	logger.debug("MovieInfoShowTimes.lookupSchedules() Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
        	Schedule[] listS = sResp.getSchedule();
        	if (listS != null){
        		schedules = new HashMap<String, Schedule>();
        		for (int i = 0; i < listS.length; i++) {
					String tId = "" + listS[i].getTheatrePoiId();
					if (!onlineTickets && listS[i].getTicketURI() != null) onlineTickets = true; // has tickets online
					schedules.put(tId, listS[i]);
				}
        	}
	        request.setAttribute("Schedules", schedules);
	        if(Util.isCanadianCarrier(handler)){
	        	onlineTickets = false;
	        }
	        request.setAttribute("HasTickets", onlineTickets);
        
	        
	        
	        MovieImageRequestDTO iReq = Util.createMovieImageRequestDTO(handler, movieId);
	        
	        try{
		        Util.setClientProps(iReq);
		        MovieImageResponse iResp = stub.getMovieImage(iReq);
	        	status = iResp.getResponseStatus();
	        	logger.debug("getMovieImage() Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
		        MovieImage image = iResp.getImage();
		        if (image != null){
			        int imageWidth = image.getWidth();
			        int imageHeight = image.getHeight();
			        InputStream io = image.getData().getInputStream();
			        int avail = io.available();
			        byte[] img = new byte[avail];
			        io.read(img);
			        String smallImage = Utility.byteArrayToBase64(img);
			        request.setAttribute("smallImage", smallImage);
			        request.setAttribute("imgH", imageHeight);
			        request.setAttribute("imgW", imageWidth);
		        }
	        }catch(Exception ex){
	        	logger.warn("there is no image for this movie, use default N/A image");
	        } //no image, use default N/A image 
	        
        }catch(Exception e){
        	logger.error("COSE is not available now.");
        	logger.error("cause:" + e.getCause());
        	logger.error("error message:" + e.getMessage());
	        request.setAttribute("ErrorMessage", "COSE.notAvailable");
        	return mapping.findForward("failure");
        }
        finally {
			WebServiceUtils.cleanupStub(stub);
		}

        return mapping.findForward("success");
    }

}
