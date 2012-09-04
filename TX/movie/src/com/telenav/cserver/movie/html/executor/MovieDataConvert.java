package com.telenav.cserver.movie.html.executor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.movie.html.datatypes.BookingInfoItem;
import com.telenav.cserver.movie.html.datatypes.MovieItem;
import com.telenav.cserver.movie.html.datatypes.ScheduleItem;
import com.telenav.cserver.movie.html.datatypes.TheaterItem;
import com.telenav.cserver.movie.html.datatypes.TicketItem;
import com.telenav.cserver.movie.html.util.HtmlMovieUtil;
import com.telenav.datatypes.content.movie.v10.DetailTheaterInfoServiceResponse;
import com.telenav.datatypes.content.movie.v10.Movie20;
import com.telenav.datatypes.content.movie.v10.MovieAndScheduleInfo;
import com.telenav.datatypes.content.movie.v10.MovieFullInfo;
import com.telenav.datatypes.content.movie.v10.MovieListing;
import com.telenav.datatypes.content.movie.v10.MovieListingWithDetailTheaterInfo;
import com.telenav.datatypes.content.movie.v10.MovieServiceResponse;
import com.telenav.datatypes.content.movie.v10.Schedule;
import com.telenav.datatypes.content.movie.v10.ScheduleServiceResponse;
import com.telenav.datatypes.content.movie.v10.Show;
import com.telenav.datatypes.content.movie.v10.TheaterAndScheduleInfo;
import com.telenav.datatypes.content.movie.v10.TheaterFullInfo;
import com.telenav.datatypes.content.movie.v10.ThirdPartyReview;
import com.telenav.datatypes.content.tnpoi.v10.TnPoi;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.services.content.moviesearchservice.v10.MovieFullInfoServiceResponseDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieServiceResponseDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieWithDetailTheaterInfoServiceResponseDTO;
import com.telenav.services.content.moviesearchservice.v10.TheaterFullInfoServiceResponseDTO;
import com.telenav.ws.datatypes.localapps.movie.v10.MovieBookingInfo;
import com.telenav.ws.datatypes.localapps.movie.v10.Ticket;
import com.telenav.ws.services.localapps.movie.v10.GetTicketResponse;

public class MovieDataConvert {
	private static Logger logger = Logger.getLogger(MovieDataConvert.class);

	public static void convertTheaterListResponse(TheaterListResponse response,DetailTheaterInfoServiceResponse tResp)
	{
		TnPoi[] theaters = tResp.getDetailTheaterInfo();
		List<TheaterItem> theaterList = new ArrayList<TheaterItem>();
		TheaterItem theaterItem;
		if(theaters != null)
		{
			for(int i=0;i<theaters.length;i++)
			{
				theaterItem = new TheaterItem();
				theaterItem.setId(theaters[i].getPoiId());
				theaterItem.setName(theaters[i].getBrandName());
				theaterItem.setAddress(wsAddressToAddress(theaters[i].getAddress()));
				theaterItem.setPhoneNo(theaters[i].getPhoneNumber());
				theaterItem.setVendor(theaters[i].getVendor());

				theaterList.add(theaterItem);

			}
		}

		response.setTheaterList(theaterList);
	}

	public static void convertMovieFullInfoServiceResponse(MovieListResponse movieListResponse,  MovieFullInfoServiceResponseDTO movieInfoResponseDTO)
	{
        MovieFullInfo[] fullInfos = movieInfoResponseDTO.getMovieFullInfo();
        if (fullInfos != null)
        {
            for (MovieFullInfo info : fullInfos)
            {
                Movie20 movie = (Movie20)info.getMovie();
                if (logger.isDebugEnabled())
                {
                    logger.debug("get movie: " + movie.getId() + " || name: " + movie.getName());
                }
                MovieItem movieItem = convertMovie(movie);
                TheaterAndScheduleInfo[] theaterAndSchedules = info.getTheaterAndScheduleInfo();
                for (TheaterAndScheduleInfo theaterAndSchedule : theaterAndSchedules)
                {
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("Schudle theater Id: " + theaterAndSchedule.getSchedule().getTheatrePoiId());
                        logger.debug("Theater brand name: " + theaterAndSchedule.getTheater().getBrandName());
                    }
                    TheaterItem theaterItem = convertTheater(theaterAndSchedule.getTheater());
                    theaterItem.setScheduleItem(convertSchedule(theaterAndSchedule.getSchedule()));
                    movieItem.getTheaterList().add(theaterItem);

                }
                movieListResponse.getMoiveList().add(movieItem);
            }
        }
	}

	public static void convertTheaterFullInfoServiceResponse(TheaterListResponse response,TheaterFullInfoServiceResponseDTO theaterInfoResponseDTO)
	{
	    TheaterFullInfo[] fullInfos = theaterInfoResponseDTO.getTheaterFullInfo();
	    if (fullInfos != null)
        {
            for (TheaterFullInfo info : fullInfos)
            {
                TnPoi theater = info.getTheater();
                if (logger.isDebugEnabled())
                {
                    logger.debug("theater: " + theater.getPoiId() + " || name: " + theater.getBrandName());
                }
                TheaterItem theaterItem = convertTheater(theater);
                MovieAndScheduleInfo[] movieAndSchedules = info.getMovieAndScheduleInfo();
                for (MovieAndScheduleInfo movieAndSchedule : movieAndSchedules)
                {
                    if (logger.isDebugEnabled())
                    {
                    	logger.debug("Schudle movie Id: " + movieAndSchedule.getSchedule().getMovieId());
                    	logger.debug("Movie name: " + movieAndSchedule.getMovie().getName());
                    }
                    MovieItem movieItem = convertMovie((Movie20)movieAndSchedule.getMovie());
                    movieItem.setScheduleItem(convertSchedule(movieAndSchedule.getSchedule()));
                    theaterItem.getMovieList().add(movieItem);
                }
                response.getTheaterList().add(theaterItem);
            }
        }
	}

	/**
	 *
	 * @param response
	 * @param sResp
	 */
	public static void convertLookUpScheduleResponse(LookUpScheduleResponse response,ScheduleServiceResponse sResp)
	{
		List<ScheduleItem> scheduleList = new ArrayList<ScheduleItem>();
		Schedule[] schedules = sResp.getSchedule();
    	if (schedules != null){
    		for (int i = 0; i < schedules.length; i++)
    		{
    			scheduleList.add(convertSchedule(schedules[i]));
			}
    	}

    	response.setScheduleList(scheduleList);
	}


	private static ScheduleItem convertSchedule(Schedule schedule)
	{
	    ScheduleItem item = new ScheduleItem();
        item.setId(schedule.getId());
        item.setMovieId(schedule.getMovieId());
        item.setTheaterId(schedule.getTheatrePoiId());
        item.setVendorName(schedule.getVendorName());
        item.setVendorTheaterId(schedule.getVendorTheaterId());
        item.setTicketURI(schedule.getTicketURI());

        Show[] shows= schedule.getShow();
        List<String> showTimes = new ArrayList<String>();
        if(shows != null)
        {
            for (int j = 0; j < shows.length; j++)
            {
                showTimes.add(shows[j].getTime().toString());
            }
        }

        item.setShowTimes(showTimes);
        item.setShowPageTimes(showTimes);
        item.setShowTimeString(item.formatShowTimes());
        return item;
	}

	/**
     * convert webservice response to cserver response
     * @param movieResponse
     * @param mResp
     */
    public static void  convertMovieListResponseSimple(MovieListResponse movieResponse,MovieServiceResponseDTO mResp)
    {
    	Movie20 movie;
    	MovieItem movieItem;
    	List<MovieItem> movieList = new ArrayList<MovieItem>();
    	if(mResp == null || mResp.getMovieListing() == null)
    	{
    		movieResponse.setMoiveList(movieList);
    		return;

    	}

    	for (MovieListing movieListing : mResp.getMovieListing())
    	{
        	movie = (Movie20)movieListing.getMovie();
        	movieItem = convertMovie(movie);
        	movieList.add(movieItem);
    	}

    	movieResponse.setMoiveList(movieList);
    }


	/**
     * convert webservice response to cserver response
     * @param movieResponse
     * @param mResp
     */
    public static void  convertMovieListResponse(MovieListResponse movieResponse,MovieWithDetailTheaterInfoServiceResponseDTO mResp)
    {
    	Movie20 movie;
    	MovieItem movieItem;
    	List<MovieItem> movieList = new ArrayList<MovieItem>();
    	if(mResp == null || mResp.getMovieListing() == null)
    	{
    		movieResponse.setMoiveList(movieList);
    		return;

    	}
    	for (MovieListingWithDetailTheaterInfo movieListing : mResp.getMovieListing())
    	{
        	movie = (Movie20)movieListing.getMovie();
        	movieItem = convertMovie(movie);

        	TnPoi[] theaters = movieListing.getDetailTheaterInfo();
        	List<TheaterItem> theaterList = new ArrayList<TheaterItem>();
        	if (theaters != null){
            	TnPoi theater = null;
            	TheaterItem theaterItem;
    	        for (int i=0; i< theaters.length ; i++)
    	        {
    	        	theater = theaters[i];
    	        	if(theater != null)
    	        	{
	    	        	theaterItem = convertTheater(theater);
	    	        	theaterList.add(theaterItem);
    	        	}
    	        }
        	}

        	movieItem.setTheaterList(theaterList);

        	movieList.add(movieItem);
    	}

    	movieResponse.setMoiveList(movieList);
    }

    private static TheaterItem convertTheater(TnPoi theater)
    {
        TheaterItem theaterItem = new TheaterItem();

        theaterItem.setId(theater.getPoiId());
        theaterItem.setName(theater.getBrandName());
        theaterItem.setPhoneNo(theater.getPhoneNumber());
        theaterItem.setAddress(wsAddressToAddress(theater.getAddress()));
        theaterItem.setVendor(theater.getVendor());

        return theaterItem;

    }

    private static MovieItem convertMovie(Movie20 movie, String theaterId)
    {
        MovieItem movieItem = convertMovie( movie);
        movieItem.setTheaterId(theaterId);
        return movieItem;
    }

    private static MovieItem convertMovie(Movie20 movie)
    {
        MovieItem movieItem = new MovieItem();

        movieItem.setId(movie.getId());
        movieItem.setName(movie.getName());
        movieItem.setCast(movie.getCast());
        movieItem.setGrade(movie.getGrade());
        String time=HtmlMovieUtil.timeFormat(movie.getRuntime());
        movieItem.setRuntime(time);
        movieItem.setDescription(movie.getDescription());
        movieItem.setDirector(movie.getDirector());
        movieItem.setGenres(movie.getGenres());
        movieItem.setVendorId(movie.getVendorId());
        ThirdPartyReview thirdPartyReview = movie.getThirdPartyReview();
        String tomatoRating = "";
        String tomatoRatingUrl = "";

        //0=no rating data
        //1=good rating data
        //-1=bad rating data
        int tomatoRatingStatus = 0;
        if(thirdPartyReview != null)
        {
	        tomatoRating = HtmlCommonUtil.getString(thirdPartyReview.getVoterPercent());
	        if(!"".equals(tomatoRating) && !"-1".equals(tomatoRating))
	        {
	        	int tomatoRate = 0;
	        	try
	        	{
	        		tomatoRate = Integer.parseInt(tomatoRating);
	        	}
	        	catch(NumberFormatException  e)
	        	{

	        	}
	        	if(tomatoRate >= 60)
	        	{
	        		tomatoRatingStatus = 1;
	        	}
	        	else
	        	{
	        		tomatoRatingStatus = -1;
	        	}
	        	tomatoRating += "%";
	        }
	        tomatoRatingUrl = HtmlCommonUtil.useNativeBrowser(HtmlCommonUtil.getString(thirdPartyReview.getMovieUrl()));
        }
        movieItem.setTomatoRating(tomatoRating);
        movieItem.setTomatoRatingUrl(tomatoRatingUrl);
        movieItem.setTomatoRatingStatus(tomatoRatingStatus);

        double rating = 0.0;
        try{
            rating = Double.parseDouble(movie.getRating());
        }
        catch(Exception ex){
            //ignore
        }
        movieItem.setRating(rating);
        return movieItem;
    }



	public static Stop wsAddressToAddress(com.telenav.ws.datatypes.address.Address wsAddr)
	{
		Stop address = new Stop();

		if(wsAddr == null)
		{
			return address;
		}

		if(wsAddr.getGeoCode() != null)
		{
			address.lat = HtmlCommonUtil.convertToDM5(wsAddr.getGeoCode().getLatitude());
			address.lon = HtmlCommonUtil.convertToDM5(wsAddr.getGeoCode().getLongitude());
		}

        address.label = wsAddr.getLabel();
        address.firstLine = wsAddr.getStreetName();
        address.city = wsAddr.getCity();
        address.state = wsAddr.getState();
        address.zip = wsAddr.getPostalCode();
        address.country = wsAddr.getCountry();


        return address;
	}


	/**
     * convert webservice response to cserver response
     * @param movieResponse
     * @param mResp
     */
    public static void  convertLookUpMovieResponse(MovieListResponse movieResponse,MovieServiceResponse mResp)
    {
    	Movie20 movie;
    	MovieItem movieItem;
    	List<MovieItem> movieList = new ArrayList<MovieItem>();
    	if(mResp == null || mResp.getMovieListing() == null)
    	{
    		movieResponse.setMoiveList(movieList);
    		return;

    	}
    	for (MovieListing movieListing : mResp.getMovieListing())
    	{
        	String theaterId = ""+movieListing.getTheaterInfo()[0].getPoiId();
        	movie = (Movie20)movieListing.getMovie();
        	movieItem = convertMovie(movie,theaterId);

        	movieList.add(movieItem);
    	}

    	movieResponse.setMoiveList(movieList);
    }

    public static void convertGetTicketQuantityResponse(GetTicketQuantityResponse gtResponse, GetTicketResponse response){

    }

    public static TicketItem convertTikcet(Ticket ticket){
    	TicketItem ticketItem = new TicketItem();
    	ticketItem.setTicketId(ticket.getTicketId());
    	ticketItem.setPrice(ticket.getPrice());
    	ticketItem.setType(ticket.getType());
    	ticketItem.setQuantity(ticket.getQuantity());
    	ticketItem.setCurrency(ticket.getCurrency());
    	return ticketItem;
    }

    public static MovieBookingInfo convertBookingInfoItem(BookingInfoItem item){
    	MovieBookingInfo info =new MovieBookingInfo();
    	info.setMovieId(item.getMovieId());
    	info.setTheaterId(item.getTheaterId());
    	info.setShowDate(item.getShowDate());
    	info.setShowTime(item.getShowTime());
    	info.setConfirmEmail(item.getConfirmEmail());
    	info.setTotalAmount(item.getTotalAmout());

    	List<TicketItem> ticketItems = item.getTickets();
    	//only add the ticket whose quantity is not 0

    	ArrayList<Ticket> validTicketList = new ArrayList<Ticket>();
    	for(int i=0;i < ticketItems.size();i++){
    		TicketItem ticketItem = ticketItems.get(i);
    		if(ticketItem.getQuantity()>0){
        		Ticket ticket = new Ticket();
        		ticket.setTicketId(ticketItem.getTicketId());
        		ticket.setPrice(ticketItem.getPrice());
        		ticket.setCurrency(ticketItem.getCurrency());
        		ticket.setQuantity(ticketItem.getQuantity());
        		ticket.setType(ticketItem.getType());
        		validTicketList.add(ticket);
    		}

    	}
    	Ticket[] tickets = new Ticket[validTicketList.size()];
    	tickets = validTicketList.toArray(new Ticket[validTicketList.size()]);
    	//end

    	info.setTickets(tickets);

    	return info;
    }
}
