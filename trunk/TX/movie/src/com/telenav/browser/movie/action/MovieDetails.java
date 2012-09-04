package com.telenav.browser.movie.action;

import java.io.InputStream;

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
import com.telenav.datatypes.content.movie.v10.Movie;
import com.telenav.datatypes.content.movie.v10.MovieImage;
import com.telenav.datatypes.content.movie.v10.MovieImageResponse;
import com.telenav.datatypes.content.movie.v10.MovieListing;
import com.telenav.datatypes.content.movie.v10.MovieSearchDate;
import com.telenav.datatypes.content.movie.v10.MovieServiceResponse;
import com.telenav.datatypes.content.movie.v10.Schedule;
import com.telenav.datatypes.content.movie.v10.ScheduleServiceResponse;
import com.telenav.datatypes.content.tnpoi.v10.TnPoi;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.services.content.moviesearchservice.v10.MovieDetailsRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieImageRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchServiceStub;
import com.telenav.services.content.moviesearchservice.v10.ScheduleDetailsRequestDTO;
import com.telenav.services.content.v10.ContentSearchService;
import com.telenav.services.content.v10.ContentSearchServiceStub;
import com.telenav.services.content.v10.GetPoiByIdsRequest;
import com.telenav.services.content.v10.GetPoiByIdsResponse;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.tnbrowser.util.Utility;
import com.telenav.ws.datatypes.services.ResponseStatus;

public class MovieDetails extends MovieBaseAction{
	private static Logger logger = Logger.getLogger(MovieDetails.class);   
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
	        
	        node = (TxNode) handler.getParameter("movieId");
	        String movieId = node != null?node.msgAt(0):null; 
	        node = (TxNode) handler.getParameter("theaterId");
	        long theaterId = -1;
	        if (node != null){
	        	theaterId = Long.parseLong(node.msgAt(0));
		        request.setAttribute("theaterId", theaterId);
	        }
	        request.setAttribute("movieId", movieId);
	        node = (TxNode) handler.getParameter("hasTickets");
	        boolean hasTickets = node != null?Boolean.parseBoolean(node.msgAt(0)):false;
	        logger.debug("MovieDetails. MovieId:" + movieId + " TheaterId:" + theaterId + " hasTickets:" + hasTickets);
	        request.setAttribute("HasTickets", hasTickets);
	        
	        
	        //Movie movie = Util.generateMovieShort();
	        Movie movie = null;
	        
	        MovieDetailsRequestDTO mReq = new MovieDetailsRequestDTO();
	        mReq.setMovieId(movieId);
	        Util.setClientProps(mReq);
	        
	        MovieSearchServiceStub movieServiceStub = null;
	        ContentSearchServiceStub poiServiceStub = null;
	        try{
	        	movieServiceStub = Util.getService();
	        	MovieServiceResponse mResp = movieServiceStub.getMovieByID(mReq);
	        	ResponseStatus status = mResp.getResponseStatus();
	        	logger.debug("MovieDetails.getMovieByID(). Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
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
		        
		        MovieImageRequestDTO iReq = Util.createMovieImageRequestDTO(handler, movieId);
		              
	        	try{
			        Util.setClientProps(iReq);
			        MovieImageResponse iResp = movieServiceStub.getMovieImage(iReq);
	        		status = iResp.getResponseStatus();
	        		logger.debug("MovieDetails.getMovieImage() Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
		        	MovieImage image = iResp.getImage(); 
			        if (image != null){
				        int imageWidth = image.getWidth();
				        int imageHeight = image.getHeight();
				        InputStream io = image.getData().getInputStream();
				        int avail = io.available();
				        byte[] img = new byte[avail];
				        io.read(img);
				        String bigImage = Utility.byteArrayToBase64(img);
				        request.setAttribute("bigImage", bigImage);
				        request.setAttribute("imgH", imageHeight);
				        request.setAttribute("imgW", imageWidth);
			        }
	        	}catch(Exception ex){
	        	} // image not available, use default
		        
		        if (theaterId != -1){
			        TnPoi[] theaters = null;
		        	GetPoiByIdsRequest tInforReq = new GetPoiByIdsRequest();
		        	tInforReq.setPoiIds(new long[]{theaterId});
			        Util.setClientProps(tInforReq);
			        poiServiceStub = Util.getPoiService(); 
			        GetPoiByIdsResponse poiResp = poiServiceStub.getPoiByIds(tInforReq);
		        	status = poiResp.getResponseStatus();
		        	logger.debug("MovieInfoShowTimes.getPoiByIds() Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
			        theaters = poiResp.getPois();
			        TnPoi theater = null;
			        if (theaters != null) {
			        	theater = theaters[0];
			        }else{
				        // TODO add error handling
			        }
			        request.setAttribute("TheaterPOI", theater);
		
			        node = (TxNode) handler.getParameter("scheduleId");
			        String scheduleId = node != null?node.msgAt(0):null;
			        
			        ScheduleDetailsRequestDTO sReq = new ScheduleDetailsRequestDTO();
			        sReq.setScheduleId(scheduleId);
			        Util.setClientProps(sReq);
			        ScheduleServiceResponse sResp = movieServiceStub.getScheduleByID(sReq);
		        	status = sResp.getResponseStatus();
		        	logger.debug("MovieDetails.getScheduleByID(). Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
		        	Schedule[] sArr = sResp.getSchedule();
		        	Schedule s = sArr !=null? sArr[0] : null;

			        //if (s==null) s = Util.generateSchedule();
			        request.setAttribute("schedule", s);
			        
			        MovieSearchDate showDate = s.getDate();
			        request.setAttribute("date", Util.getDisplayDate(showDate)); 
			        
			        String fUrl = s.getTicketURI();
			        if (fUrl == null || fUrl.length() < 0)
			        	request.setAttribute("HasTickets", false);
			        else
			        	request.setAttribute("HasTickets", true);
		        }
	        
	        }catch(Exception ex){
		        request.setAttribute("ErrorMessage", "COSE.notAvailable");
	        	return mapping.findForward("failure");
	        }
	        finally {
				WebServiceUtils.cleanupStub(movieServiceStub);
				WebServiceUtils.cleanupStub(poiServiceStub);
			}
	        
	        if(Util.isCanadianCarrier(handler)){
	        	request.setAttribute("HasTickets", false);
	        }
	        return mapping.findForward("success");
	    }
}
