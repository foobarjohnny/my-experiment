package com.telenav.browser.movie.action;

import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis2.databinding.types.Time;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.browser.MovieBaseAction;
import com.telenav.browser.movie.Constant;
import com.telenav.browser.movie.Util;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.datatypes.content.movie.v10.DetailTheaterInfoServiceResponse;
import com.telenav.datatypes.content.movie.v10.Movie;
import com.telenav.datatypes.content.movie.v10.MovieImage;
import com.telenav.datatypes.content.movie.v10.MovieImageResponse;
import com.telenav.datatypes.content.movie.v10.MovieImageTypeEnum;
import com.telenav.datatypes.content.movie.v10.MovieListing;
import com.telenav.datatypes.content.movie.v10.MovieSearchDate;
import com.telenav.datatypes.content.movie.v10.MovieServiceResponse;
import com.telenav.datatypes.content.movie.v10.Schedule;
import com.telenav.datatypes.content.movie.v10.ScheduleServiceResponse;
import com.telenav.datatypes.content.movie.v10.Show;
import com.telenav.datatypes.content.tnpoi.v10.TnPoi;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.services.content.moviesearchservice.v10.MovieDetailsRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieImageRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchServiceStub;
import com.telenav.services.content.moviesearchservice.v10.ScheduleDetailsRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.TheaterDetailsRequestDTO;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.tnbrowser.util.Utility;
import com.telenav.ws.datatypes.services.ResponseStatus;

public class BuyTickets extends MovieBaseAction{
	private static Logger logger = Logger.getLogger(BuyTickets.class);   
	public ActionForward doAction(ActionMapping mapping,
	            HttpServletRequest request, HttpServletResponse response)
	            throws Exception {
	        DataHandler handler = (DataHandler) request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
	        
	        TxNode node = (TxNode) handler.getParameter("movieId");
	        String movieId = node != null? node.msgAt(0) : null;
	        request.setAttribute("movieId", movieId);
	        node = (TxNode) handler.getParameter("theaterId");
	        long tId = -1;
	        if (node != null){
	        	tId = Long.parseLong(node.msgAt(0));
		        request.setAttribute("theaterId", tId);
	        }
	        node = (TxNode) handler.getParameter("scheduleId");
	        String scheduleId = node != null? node.msgAt(0) : null;
	        node = (TxNode) handler.getParameter("fromTSearch");
	        Boolean fromTSearch = node != null? new Boolean(node.msgAt(0)) : Boolean.FALSE;
	        logger.debug("BuyTickets. MovieId:" + movieId + " TheaterId:" + tId + " ScheduleId:" + scheduleId);
	        request.setAttribute("fromTSearch", fromTSearch);

	        // get values for current location lat and lon, and also distUnit
	        node = (TxNode) handler.getParameter("anchorLat");
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

		    boolean ticketsOnline = true; 
	        
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
	        	logger.debug("BuyTickets.getMovieByID(). Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
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
		        
		        TnPoi[] theaters = null;
		        if (tId  != -1){
		        	TheaterDetailsRequestDTO tReq = new TheaterDetailsRequestDTO();
		        	tReq.setTheaterIds(new long[]{tId});
			        Util.setClientProps(tReq);
			        DetailTheaterInfoServiceResponse tResp = stub.getDetailTheaterInfosByTheaterIDs(tReq);
		        	status = tResp.getResponseStatus();
		        	logger.debug("MovieInfoShowTimes.getPoiByIds() Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
			        theaters = tResp.getDetailTheaterInfo();
		        }
		        TnPoi theater = null;
		        if (theaters != null) {
		        	theater = theaters[0];
		        	theater.setPhoneNumber(Util.stripPhoneNumber(theater.getPhoneNumber()));
		        }else{
			        // TODO add error handling
		        	//theater = Util.generateTheater(tId);
		        }
		        request.setAttribute("TheaterPOI", theater);
	
		        ScheduleDetailsRequestDTO sReq = new ScheduleDetailsRequestDTO();
		        sReq.setScheduleId(scheduleId);
		        Util.setClientProps(sReq);
		        ScheduleServiceResponse sResp = stub.getScheduleByID(sReq);
	        	status = sResp.getResponseStatus();
	        	logger.debug("BuyTickets.getScheduleByID(). Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
	        	Schedule[] sArr = sResp.getSchedule();
	        	Schedule s = sArr !=null? sArr[0] : null;

		        //if (s==null) s = Util.generateSchedule();
		        request.setAttribute("schedule", s);
		        
		        MovieSearchDate showDate = s.getDate();
				
		        if(!request.getAttribute("LOCALE_KEY").equals("en_US")){
		        	request.setAttribute("date", Util.getDisplayDateWithLocale(showDate , (String)request.getAttribute("LOCALE_KEY")));
		        }else{
		        	request.setAttribute("date", Util.getDisplayDate(showDate)); 
		        } 
		        
		        String fUrl = s.getTicketURI();
		        if (fUrl == null || fUrl.length() < 0) ticketsOnline = false;
		        
		        MovieImageRequestDTO iReq = new MovieImageRequestDTO();
		        iReq.setMovieId(movieId);
		        iReq.setMovieImageType(MovieImageTypeEnum.SMALL_IMAGE);
		        //TODO change it from hard-coded values to values from property
		        int height = 120;
		        int width = 80;
		        iReq.setDisplayHeight(height);
		        iReq.setDisplayWidth(width);
		        Util.setClientProps(iReq);
		        
				Calendar curCal = new GregorianCalendar();
				double theaterLat = theater.getAddress().getGeoCode().getLatitude();
				double theaterLon = theater.getAddress().getGeoCode()
						.getLongitude();
				TimeZone theaterTimeZone = Util.getTimeZoneFromOTService(
						theaterLat, theaterLon);
				Calendar showCal = new GregorianCalendar(theaterTimeZone);
				Show[] showTimes = s.getShow();
				Time showTime;
				int[] availables = new int[showTimes.length];
				for (int i = 0; i < showTimes.length; i++) {
					showTime = showTimes[i].getTime();
					Calendar showtimeCal = showTime.getAsCalendar();
					showCal.set(showDate.getYear().getYear(), showDate.getMonth()
							.getMonth() - 1, showDate.getDay().getDay(),
							showtimeCal.get(Calendar.HOUR_OF_DAY), showtimeCal
									.get(Calendar.MINUTE), 0);
					availables[i] = showCal.after(curCal) ? 1 : 0;
				}
				request.setAttribute("availables", availables);
		        try{
			        Util.setClientProps(iReq);
			        MovieImageResponse iResp = stub.getMovieImage(iReq);
		        	status = sResp.getResponseStatus();
		        	logger.debug("getMovieImage() Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
			        MovieImage image = iResp.getImage();
			        if (image != null){
				        int imageWidth = image.getWidth();
				        int imageHeight = image.getHeight();
				        InputStream io = image.getData().getInputStream();
				        int avail = io.available();
				        byte[] img = new byte[avail];
				        io.read(img);
				        String bigImage = Utility.byteArrayToBase64(img);
				        request.setAttribute("smallImage", bigImage);
				        request.setAttribute("imgH", imageHeight);
				        request.setAttribute("imgW", imageWidth);
			        }
		        }catch(Exception ex){} //image not available use default
		        
	        }catch(Exception ex){
		        request.setAttribute("ErrorMessage", "COSE.notAvailable");
	        	return mapping.findForward("failure");
	        }
	        finally {
				WebServiceUtils.cleanupStub(stub);
			}
	        
	        if (ticketsOnline && !Util.isCanadianCarrier(handler))
	        	return mapping.findForward("success");
	        return mapping.findForward("noTickets");
	    }

}
