package com.telenav.cserver.movie.html.executor;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hamcrest.core.IsInstanceOf;

import com.telenav.LocalAppsHotelServices;
import com.telenav.LocalAppsMovieServices;
import com.telenav.browser.movie.Constant;
import com.telenav.browser.movie.Util;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.movie.html.datatypes.MovieItem;
import com.telenav.cserver.movie.html.datatypes.ScheduleItem;
import com.telenav.cserver.movie.html.datatypes.TheaterItem;
import com.telenav.cserver.movie.html.datatypes.TicketItem;
import com.telenav.datatypes.content.movie.v10.Area;
import com.telenav.datatypes.content.movie.v10.DetailTheaterInfoServiceResponse;
import com.telenav.datatypes.content.movie.v10.MovieImage;
import com.telenav.datatypes.content.movie.v10.MovieImageResponse;
import com.telenav.datatypes.content.movie.v10.MovieImageTypeEnum;
import com.telenav.datatypes.content.movie.v10.MovieSearchDate;
import com.telenav.datatypes.content.movie.v10.MovieServiceResponse;
import com.telenav.datatypes.content.movie.v10.MovieSortTypeEnum;
import com.telenav.datatypes.content.movie.v10.ScheduleServiceResponse;
import com.telenav.datatypes.content.movie.v10.TheaterSortTypeEnum;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.services.content.moviesearchservice.v10.MovieBatchImagesRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieBatchImagesResponseDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieFullInfoRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieFullInfoServiceResponseDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieImageRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieLookupRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchService;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchServiceStub;
import com.telenav.services.content.moviesearchservice.v10.MovieServiceResponseDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieWithDetailTheaterInfoServiceResponseDTO;
import com.telenav.services.content.moviesearchservice.v10.ScheduleLookupRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.TheaterFullInfoServiceResponseDTO;
import com.telenav.services.content.moviesearchservice.v10.TheaterLookupRequestDTO;
import com.telenav.tnbrowser.util.Utility;
import com.telenav.ws.datatypes.localapps.movie.v10.Ticket;
import com.telenav.ws.datatypes.services.ResponseStatus;
import com.telenav.ws.datatypes.services.v20.AppInformation;
import com.telenav.ws.datatypes.services.v20.UserInformation;
import com.telenav.ws.services.localapps.movie.v10.GetTicketRequest;
import com.telenav.ws.services.localapps.movie.v10.GetTicketResponse;
import com.telenav.ws.services.localapps.movie.v10.SetBookingRequest;
import com.telenav.ws.services.localapps.movie.v10.SetBookingResponse;
import com.telenav.LocalAppsMovieServicesStub;;


/**
 * 
 * @author panzhang
 *
 */
public class MovieServiceProxy {

    public static Logger logger = Logger.getLogger(MovieServiceProxy.class);

	public static void lookupSubTheatreList(MovieCommonRequest request,TheaterListResponse response)
	{
    	MovieServiceProxy.lookupTheatresWithDetailInfo(request, response);
		//search for schedule
		LookUpScheduleRequest scheduleRequest = new LookUpScheduleRequest();
		LookUpScheduleResponse scheduleResponse;
		List<TheaterItem> theaterList = response.getTheaterList();
		int listSize = theaterList.size();
		long[] theaterIds = new long[listSize];
		for(int i=0 ; i < listSize; i++)
		{
			TheaterItem theaterItem = theaterList.get(i);
			theaterIds[i] = theaterItem.getId();
		}
		
		scheduleRequest = new LookUpScheduleRequest();
		scheduleResponse = new LookUpScheduleResponse();
		
		scheduleRequest.setMovieIds(new String[]{request.getMovieId()});
		scheduleRequest.setTheaterIds(theaterIds);
		scheduleRequest.setSearchDate(request.getSearchDate());
		
		MovieServiceProxy.lookupSchedules(scheduleRequest, scheduleResponse);
		//match the schedule to theater info
		for(TheaterItem theaterItem: theaterList)
	    {
			MovieServiceProxy.matchTheaterSchedule(theaterItem,scheduleResponse.getScheduleList());
	    }
		response.setTheaterList(theaterList);
	}
	
	public static void lookupTheatresWithDetailInfo(MovieCommonRequest request,TheaterListResponse response)
	{
	    CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("lookupTheatresWithDetailInfo");
        // Get the request and response
    	Stop address = request.getAddress();
    	response.setAddress(address);
    	response.setDistanceUnit(request.getDistanceUnit());
    	
    	MovieSearchDate searchDate = Util.getSearchDate(request.getSearchDate());
    	
		MovieSearchServiceStub stub = null;
		try {
			stub = getService();
	    	TheaterLookupRequestDTO tReq = new TheaterLookupRequestDTO();
	        tReq.setDate(searchDate);
	        Area area = Util.getArea(address.lat, address.lon, Constant.SEARCH_RADIUS);
	        tReq.setArea(area);
	        if(!"" .equals(request.getMovieId()))
	        {
	        	tReq.setMovieId(request.getMovieId());
	        }
	       	tReq.setSortType(TheaterSortTypeEnum.DISTANCE);
	        tReq.setPageLength(request.getBatchSize());
	        tReq.setPageNumber(request.getBatchNumber());
	        Util.setClientProps(tReq);

	        cli.addData("search criteria:", "searchDate=" + request.getSearchDate() + "&lat=" + address.lat + "&lon=" + address.lon + "&movieId:" + request.getMovieId());
	        DetailTheaterInfoServiceResponse tResp = stub.lookupTheatresWithDetailInfo(tReq);
	        com.telenav.ws.datatypes.services.ResponseStatus status = tResp.getResponseStatus();
	        
	        MovieDataConvert.convertTheaterListResponse(response,tResp);
	        response.setStartIndex(request.getStartIndex());
	        
	        cli.addData("status:", "status code=" + status.getStatusCode() + "&status message=" + status.getStatusMessage());
		} catch (Exception e) {
			cli.setState(CliTransaction.STATUS_FAIL);
            cli.setStatus(e);
			if(e instanceof RemoteException){
				logger.error("error occured during lookupTheatresWithDetailInfo.");
			}else{
				logger.error("error occured during initialising MovieSearchService.");
			}
		}finally{
	        cli.complete();
	        response.setStatus(ExecutorResponse.STATUS_OK);
	        WebServiceUtils.cleanupStub(stub);
		}		
	}
	
	public static void searchMovies(MovieCommonRequest request,MovieListResponse response)
	{
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("searchMovies");
        // Get the request and response
        if(response == null)
        {
        	response = new MovieListResponse();
        }
       
    	Stop address = request.getAddress();
        
        MovieSearchRequestDTO movieSearchRequestDTO = new MovieSearchRequestDTO();
        movieSearchRequestDTO.setKeyword("");
        MovieSearchDate searchDate = Util.getSearchDate(request.getSearchDate()); 
        movieSearchRequestDTO.setDate(searchDate);
        Area area = Util.getArea(address.lat, address.lon, Constant.SEARCH_RADIUS);
        movieSearchRequestDTO.setArea(area);
        movieSearchRequestDTO.setPageLength(request.getBatchSize());
        movieSearchRequestDTO.setPageNumber(request.getBatchNumber());
        movieSearchRequestDTO.setSortType(MovieSortTypeEnum.ALPHABET);
    
        Util.setClientProps(movieSearchRequestDTO);
        cli.addData("search criteria:", "searchDate=" + request.getSearchDate() + "&lat=" + address.lat + "&lon=" + address.lon);
        
        debug("searchMovies:searchDate:" + request.getSearchDate() + "&lat=" + address.lat + "&lon=" + address.lon);
		MovieSearchServiceStub stub = null;
        try{
        	stub = getService();
        	MovieServiceResponseDTO mResp = stub.searchMovies(movieSearchRequestDTO);
        	ResponseStatus status = mResp.getResponseStatus();
        	cli.addData("status:", "status code=" + status.getStatusCode() + "&status message=" + status.getStatusMessage());
        	response.setStartIndex(request.getStartIndex());
        	MovieDataConvert.convertMovieListResponseSimple(response,mResp);
        	
        }catch(Exception ex){
            cli.setState(CliTransaction.STATUS_FAIL);
            cli.setStatus(ex);
            if(ex instanceof RemoteException){
				logger.error("error occured during searchMovies.");
			}else{
				logger.error("error occured during initialising MovieSearchService.");
			}
        }finally {
            cli.complete();
            response.setStatus(ExecutorResponse.STATUS_OK);
            WebServiceUtils.cleanupStub(stub);
        }		
	}
	
	public static void searchMoviesWithDetailTheaterInfo(MovieCommonRequest request,MovieListResponse response)
	{
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("searchMoviesWithDetailTheaterInfo");
        // Get the request and response
        if(response == null)
        {
        	response = new MovieListResponse();
        }
       
    	Stop address = request.getAddress();
        
        MovieSearchRequestDTO movieSearchRequestDTO = new MovieSearchRequestDTO();
        movieSearchRequestDTO.setKeyword("");
        MovieSearchDate searchDate = Util.getSearchDate(request.getSearchDate()); 
        movieSearchRequestDTO.setDate(searchDate);
        Area area = Util.getArea(address.lat, address.lon, Constant.SEARCH_RADIUS);
        movieSearchRequestDTO.setArea(area);
        movieSearchRequestDTO.setPageLength(request.getBatchSize());
        movieSearchRequestDTO.setPageNumber(request.getBatchNumber());
        movieSearchRequestDTO.setSortType(MovieSortTypeEnum.ALPHABET);
    
        Util.setClientProps(movieSearchRequestDTO);
        cli.addData("search criteria:", "searchDate=" + request.getSearchDate() + "&lat=" + address.lat + "&lon=" + address.lon);
        
        debug("searchMoviesWithDetailTheaterInfo:searchDate:" + request.getSearchDate() + "&lat=" + address.lat + "&lon=" + address.lon);
		MovieSearchServiceStub stub = null;
        try{
        	stub = getService();
        	MovieWithDetailTheaterInfoServiceResponseDTO mResp = stub.searchMoviesWithDetailTheaterInfo(movieSearchRequestDTO);
        	ResponseStatus status = mResp.getResponseStatus();
        	cli.addData("status:", "status code=" + status.getStatusCode() + "&status message=" + status.getStatusMessage());
        	MovieDataConvert.convertMovieListResponse(response,mResp);
        	
        }catch(Exception ex){
            cli.setState(CliTransaction.STATUS_FAIL);
            cli.setStatus(ex);
            if(ex instanceof RemoteException){
				logger.error("error occured during searchMoviesWithDetailTheaterInfo.");
			}else{
				logger.error("error occured during initialising MovieSearchService.");
			}
        }finally {
            cli.complete();
            response.setStatus(ExecutorResponse.STATUS_OK);
            WebServiceUtils.cleanupStub(stub);
        }		
	}
	
	public static void searchMoviesWithTheaterAndSchedule(MovieCommonRequest request,MovieListResponse response)
    {
	    CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
	    cli.setFunctionName("searchMoviesWithTheaterAndSchedule");
	    if(response == null)
        {
            response = new MovieListResponse();
        }
	    
        MovieFullInfoRequestDTO movieFullInfoRequestDTO = new MovieFullInfoRequestDTO();
        Stop address = request.getAddress();
        movieFullInfoRequestDTO.setArea(Util.getArea(address.lat, address.lon, Constant.SEARCH_RADIUS));
        movieFullInfoRequestDTO.setDate(Util.getSearchDate(request.getSearchDate()));
        movieFullInfoRequestDTO.setMovieSortType(MovieSortTypeEnum.ALPHABET);
        movieFullInfoRequestDTO.setPageNumber(request.getBatchNumber());
        movieFullInfoRequestDTO.setPageLength(request.getBatchSize());
        Util.setClientProps(movieFullInfoRequestDTO);

        cli.addData("search criteria:", "searchDate=" + request.getSearchDate() + "&lat=" + address.lat + "&lon=" + address.lon);
        debug("searchMoviesWithTheaterAndSchedule:" + request.getSearchDate() + "&lat=" + address.lat + "&lon=" + address.lon);
        MovieSearchServiceStub stub = null;
        try{
            stub = getService();
            MovieFullInfoServiceResponseDTO movieInfoResponseDTO = stub.searchMoviesFullInfo(movieFullInfoRequestDTO);
            MovieDataConvert.convertMovieFullInfoServiceResponse(response, movieInfoResponseDTO);

        }catch(Exception ex){
            cli.setState(CliTransaction.STATUS_FAIL);
            cli.setStatus(ex);
            if(ex instanceof RemoteException){
				logger.error("error occured during searchMoviesWithTheaterAndSchedule.");
			}else{
				logger.error("error occured during initialising MovieSearchService.");
			}
        }finally {
            cli.complete();
            response.setStatus(ExecutorResponse.STATUS_OK);
            WebServiceUtils.cleanupStub(stub);
        }       
    }
	
	public static void searchTheatresWithMovieAndSchedule(MovieCommonRequest request, TheaterListResponse response)
    {
	    CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("searchTheatresWithMovieAndSchedule");
        // Get the request and response
        Stop address = request.getAddress();
        response.setAddress(address);
        response.setDistanceUnit(request.getDistanceUnit());
        
        MovieFullInfoRequestDTO movieFullInfoRequestDTO = new MovieFullInfoRequestDTO();
        // request.setKeyword(name);
        movieFullInfoRequestDTO.setArea(Util.getArea(address.lat, address.lon, Constant.SEARCH_RADIUS));
        movieFullInfoRequestDTO.setDate(Util.getSearchDate(request.getSearchDate()));
        movieFullInfoRequestDTO.setTheaterSortType(TheaterSortTypeEnum.DISTANCE);
        movieFullInfoRequestDTO.setPageNumber(request.getBatchNumber());
        movieFullInfoRequestDTO.setPageLength(request.getBatchSize());
        Util.setClientProps(movieFullInfoRequestDTO); 
        
        cli.addData("search criteria:", "searchDate=" + request.getSearchDate() + "&lat=" + address.lat + "&lon=" + address.lon);
        
        debug("searchMoviesWithTheaterAndSchedule:" + request.getSearchDate() + "&lat=" + address.lat + "&lon=" + address.lon);
        MovieSearchServiceStub stub = null;
        try{
            stub = getService();
            
            TheaterFullInfoServiceResponseDTO movieInfoResponseDTO = stub.searchTheatersFullInfo(movieFullInfoRequestDTO);
            MovieDataConvert.convertTheaterFullInfoServiceResponse(response, movieInfoResponseDTO);

        }catch(Exception ex){
            cli.setState(CliTransaction.STATUS_FAIL);
            cli.setStatus(ex);
            if(ex instanceof RemoteException){
				logger.error("error occured during searchTheatresWithMovieAndSchedule.");
			}else{
				logger.error("error occured during initialising MovieSearchService.");
			}
        }finally {
            cli.complete();
            response.setStatus(ExecutorResponse.STATUS_OK);
            WebServiceUtils.cleanupStub(stub);
        }    
    }
     
	/**
	 * 
	 * @param request
	 * @return
	 */
	public static void lookupSchedules(LookUpScheduleRequest request,LookUpScheduleResponse response)
	{
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
	    cli.setFunctionName("lookupSchedules");
	     
	    if(response == null)
	    {
	    	response = new LookUpScheduleResponse();
	    	List<ScheduleItem> scheduleList = new ArrayList<ScheduleItem>();
	    	response.setScheduleList(scheduleList);
	    }
	    MovieSearchServiceStub stub = null;
		try {

			stub = getService();
			// lookup schedules for movies
	        ScheduleLookupRequestDTO scheduleRequest = new ScheduleLookupRequestDTO();
	        MovieSearchDate searchDate = Util.getSearchDate(request.getSearchDate()); 
	        
	        scheduleRequest.setDate(searchDate);
	        scheduleRequest.setMovieId(request.getMovieIds());
	        scheduleRequest.setTheaterPoiId(request.getTheaterIds());
	        Util.setClientProps(scheduleRequest);
	        
	        //cli.addData("search criteria:", "searchDate=" + request.getSearchDate() + "&movieId=" + request.getMovieIds() + "&theaterPoiId=" + request.getTheaterIds());
	        
	        ScheduleServiceResponse sResp = stub.lookupSchedules(scheduleRequest);
	        ResponseStatus status = sResp.getResponseStatus();
	    	
	    	cli.addData("status:", "status code=" + status.getStatusCode() + "&status message=" + status.getStatusMessage());
	    	MovieDataConvert.convertLookUpScheduleResponse(response,sResp);
		} catch (Exception e) {
			cli.setState(CliTransaction.STATUS_FAIL);
            cli.setStatus(e);
            if(e instanceof RemoteException){
				logger.error("error occured during lookupSchedules.");
			}else{
				logger.error("error occured during initialising MovieSearchService.");
			}
        }finally {
            cli.complete();
            response.setStatus(ExecutorResponse.STATUS_OK);
            WebServiceUtils.cleanupStub(stub);
        }
	}
	
	public static void loadImage(MovieItem movieItem)
    {
		MovieSearchServiceStub stub = null;
        try
        {
            MovieImageRequestDTO imageRequest = new MovieImageRequestDTO();
            imageRequest.setMovieId(movieItem.getId());
            imageRequest.setMovieImageType(MovieImageTypeEnum.SMALL_IMAGE);
            int height = 120; 
            int width = 80;
            imageRequest.setDisplayHeight(height);
            imageRequest.setDisplayWidth(width);
            
            stub = getService();
            Util.setClientProps(imageRequest);
            MovieImageResponse imageResponse = stub.getMovieImage(imageRequest);
            ResponseStatus status = imageResponse.getResponseStatus();
            debug("MovieDetails.getMovieImage() Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
            
            MovieImage image = imageResponse.getImage();
            if (image != null)
            {
                int imageWidth = image.getWidth();
                int imageHeight = image.getHeight();
                InputStream inputStream = image.getData().getInputStream();
                int avail = inputStream.available();
                byte[] imageBytes = new byte[avail];
                inputStream.read(imageBytes);
                String imageString = Utility.byteArrayToBase64(imageBytes);
                movieItem.setImage(imageString);
                movieItem.setImageHeight(imageHeight);
                movieItem.setImageWidth(imageWidth);
                
                if( inputStream != null )
                {
                    inputStream.close();
                }
            }
            else{
                debug("WARN: movie with id = "+ movieItem.getId()+" don't have image data");
                
            }
        }
        catch (Exception ex)
        {
            logger.error("MovieListExecutor#getMovieImage",ex);
		}
		finally {
			WebServiceUtils.cleanupStub(stub);
		}
    }
	
	public static String loadImage(String movieId)
	{
	    MovieItem movieItem = new MovieItem();
	    movieItem.setId(movieId);
	    loadImage(movieItem);
	    return movieItem.getImage();
	}
	
	public static String[] loadImages(String[] movieIds, int height, int width)
    {

		MovieSearchServiceStub stub = null;
        String[] results = new String[movieIds.length];
        try
        {

            MovieBatchImagesRequestDTO request = new MovieBatchImagesRequestDTO();
            request.setMovieIds(movieIds);
            request.setMovieImageType(MovieImageTypeEnum.SMALL_IMAGE);
            request.setDisplayHeight(height);
            request.setDisplayWidth(width);

            stub = getService();
            Util.setClientProps(request);
            MovieBatchImagesResponseDTO imageResponse = stub.getMovieBatchImages(request);
            ResponseStatus status = imageResponse.getResponseStatus();
            debug("MovieSearchService.getMovieBatchImages() Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
            
            MovieImage[] images = imageResponse.getImages();

            if (images != null)
            {
                for (int i = 0; i < images.length; i++)
                {
                    if( images[i] == null )
                    {
                    	results[i] = null;
                    	continue;
                    }    
                    
                    InputStream inputStream = images[i].getData().getInputStream();
                    int avail = inputStream.available();
                    byte[] imageBytes = new byte[avail];
                    inputStream.read(imageBytes);
                    String imageString = Utility.byteArrayToBase64(imageBytes);
                    results[i] = imageString;
                    if( inputStream != null )
                    {
                        inputStream.close();
                    }
                }
            }
            else{
               debug("WARN: can't get image data.");
                
            }
        }
        catch (Exception ex)
        {
            logger.error("MovieServiceProxy#loadImages", ex);
        }
        finally {
			WebServiceUtils.cleanupStub(stub);
		}
        return results;

    }
	
	public static String[] loadImages(String[] movieIds)
    {
        return loadImages(movieIds, 120,80);
    }
	
	public static void lookupSubMovieList(MovieCommonRequest request,MovieListResponse movieListResponse)
	{
		MovieServiceProxy.lookupMoviesInTheatre(request, movieListResponse);
		//search for schedule
		LookUpScheduleRequest scheduleRequest = new LookUpScheduleRequest();
		LookUpScheduleResponse scheduleResponse;
		List<MovieItem> movieList = movieListResponse.getMoiveList();
		int listSize = movieList.size();
		String[] movieIds = new String[listSize];
		for(int i=0 ; i < listSize; i++)
		{
			MovieItem movieItem = movieList.get(i);
			movieIds[i] = movieItem.getId();
		}
		
		scheduleRequest = new LookUpScheduleRequest();
		scheduleResponse = new LookUpScheduleResponse();
		
		scheduleRequest.setMovieIds(movieIds);
		
		Long theaterId = request.getTheaterId();
		try{
			theaterId = Long.parseLong(movieListResponse.getMoiveList().get(0).getTheaterId());
		}catch(Exception e){
		}
		
		scheduleRequest.setTheaterIds(new long[]{theaterId});
		scheduleRequest.setSearchDate(request.getSearchDate());
		
		MovieServiceProxy.lookupSchedules(scheduleRequest, scheduleResponse);
		//match the schedule to theater info
		for(MovieItem movieItem: movieList)
	    {
			MovieServiceProxy.matchMovieSchedule(movieItem,scheduleResponse.getScheduleList());
	    }
		movieListResponse.setMoiveList(movieList);
	}
	
	public static void lookupMoviesInTheatre(MovieCommonRequest request,MovieListResponse response)
	{
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("lookupMoviesInTheatre");
        // Get the request and response
        if(response == null)
        {
        	response = new MovieListResponse();
        }
       
        long theaterId = request.getTheaterId();
        MovieSearchDate searchDate = Util.getSearchDate(request.getSearchDate());
        MovieLookupRequestDTO moviesInTheaterReq = new MovieLookupRequestDTO();
        moviesInTheaterReq.setDate(searchDate);
        moviesInTheaterReq.setTheaterPoiId(theaterId);
        moviesInTheaterReq.setSortType(MovieSortTypeEnum.ALPHABET);
        moviesInTheaterReq.setPageNumber(request.getBatchNumber());
        moviesInTheaterReq.setPageLength(request.getBatchSize());
        
    
        Util.setClientProps(moviesInTheaterReq);
        cli.addData("search criteria:", "searchDate=" + request.getSearchDate() + "&theaterId=" +theaterId);
        debug("lookupMoviesInTheatre:searchDate:" + request.getSearchDate() + "&theaterId=" + theaterId);
        MovieSearchServiceStub stub = null;
        try{
        	stub = getService();
        	MovieServiceResponse mResp = stub.lookupMoviesInTheatre(moviesInTheaterReq);
        	ResponseStatus status = mResp.getResponseStatus();
        	cli.addData("status:", "status code=" + status.getStatusCode() + "&status message=" + status.getStatusMessage());
        	MovieDataConvert.convertLookUpMovieResponse(response,mResp);
        	
        }catch(Exception ex){
            cli.setState(CliTransaction.STATUS_FAIL);
            cli.setStatus(ex);
            if(ex instanceof RemoteException){
				logger.error("error occured during lookupMoviesInTheatre.");
			}else{
				logger.error("error occured during initialising MovieSearchService.");
			}
        }finally {
            cli.complete();
            response.setStatus(ExecutorResponse.STATUS_OK);
            WebServiceUtils.cleanupStub(stub);
        }		
	}
	
	public static void getTicketPrices(GetTicketQuantityRequest request,GetTicketQuantityResponse response)
	{
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getTicketPrice");
        // Get the request and response
        if(response == null)
        {
        	response = new GetTicketQuantityResponse();
        }
       
        GetTicketRequest gtReq = new GetTicketRequest();   
        
        gtReq.setMovieId(request.getMovieId());
        gtReq.setTheaterId(request.getTicketTheaterId());
        gtReq.setShowDate(request.getSearchDate());
        gtReq.setShowTime(request.getShowTime());   
        gtReq.setTransactionId("1");
        gtReq.setClientName("cose");
        gtReq.setClientVersion("1");
        gtReq.setPartnerCode("FANDANGO_MOVIE");
        
        UserInformation userInfo = new UserInformation();
        userInfo.setUserId(""+request.getUserId());
        gtReq.setUserInfo(userInfo);

        cli.addData("get ticket prices:", "theaterId="+request.getTheaterId()+"&movieId="+request.getMovieId()+"showDate=" + request.getSearchDate() + "&showTime=" +request.getShowTime());
     
        debug("getTicketPrices:theaterId="+request.getTheaterId()+"&movieId="+request.getMovieId()+"showDate=" + request.getSearchDate() + "&showTime=" +request.getShowTime());
        LocalAppsMovieServicesStub stub = null;
        try{
        	stub = getMovieService();
        	GetTicketResponse gtResponse = stub.getTicket(gtReq);

        	Ticket[] tickets = gtResponse.getTickets();
        	List<TicketItem> ticketList = new ArrayList<TicketItem>();
        	
        	if(tickets == null||tickets.length<=0){//If no tickets returned.
                debug("No ticket found. "+"theaterId="+request.getTheaterId()+"&movieId="+request.getMovieId()+"showDate=" + request.getSearchDate() + "&showTime=" +request.getShowTime());
            	response.setTicketList(ticketList); 
        	}else{
            	response.setSurcharge(gtResponse.getSurcharge());    
            	response.setConvenienceCharge(gtResponse.getConvenienceCharge()); 
            	for(Ticket ticket: tickets){
            		debug("Ticket,id:"+ticket.getTicketId()+" price:"+ticket.getPrice()+" type:"+ticket.getType());
            		
            		cli.addData("Ticket info:", "Ticket,id:"+ticket.getTicketId()+" price:"+ticket.getPrice()+" type:"+ticket.getType());
            		ticketList.add(MovieDataConvert.convertTikcet(ticket));
            	}
            	response.setTicketList(ticketList); 
        	}
        		
            com.telenav.ws.datatypes.services.v20.ResponseStatus status = gtResponse.getResponseStatus();
            cli.addData("status:", "status code=" + status.getStatusCode() + "&status message=" + status.getStatusMessage());

        }catch(Throwable ex){
            cli.setState(CliTransaction.STATUS_FAIL);
            cli.setStatus(ex);
            if(ex instanceof RemoteException){
				logger.error("error occured during getTicketPrices.");
			}else{
				logger.error("error occured during initialising MovieSearchService.");
			}
        }finally {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }		
	}
	
	public static void getOrderId(BookTicketRequest request,BookTicketResponse response){

		
		 CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
	        cli.setFunctionName("getOrderId");
	        // Get the request and response
	        if(response == null)
	        {
	        	response = new BookTicketResponse();
	        }
	       
	        SetBookingRequest  sbRequest = new SetBookingRequest ();        
	        
	        sbRequest.setBookingInfo(MovieDataConvert.convertBookingInfoItem(request.getBookingInfo()));

	        sbRequest.setTransactionId("1");
	        sbRequest.setClientName("cose");
	        sbRequest.setClientVersion("1");
	        sbRequest.setPartnerCode("FANDANGO_MOVIE");
	        AppInformation appInfo = new AppInformation();
	        appInfo.setAppName("telenav");
	        sbRequest.setAppInfo(appInfo);
	        UserInformation userInfo = new UserInformation();
	        userInfo.setUserId(""+request.getUserId());

	        sbRequest.setUserInfo(userInfo);
	        LocalAppsMovieServicesStub stub = null;
	        try{
	        	stub = getMovieService();
	        	SetBookingResponse sbResponse = stub.setBooking(sbRequest);
	        	String orderId = sbResponse.getOrderId();
	        	
	        	debug("order Id: "+orderId);
	        	cli.addData("Order Id:", orderId);
	        	
	        	response.setOrderId(orderId);
	        	
	        	com.telenav.ws.datatypes.services.v20.ResponseStatus status = sbResponse.getResponseStatus();
	        	cli.addData("status:", "status code=" + status.getStatusCode() + "&status message=" + status.getStatusMessage());
	        	debug("Responde Code:"+ status.getStatusCode());
	        	
	        }catch(Throwable ex){
	            cli.setState(CliTransaction.STATUS_FAIL);
	            cli.setStatus(ex);
	            if(ex instanceof RemoteException){
					logger.error("error occured during getOrderId.");
				}else{
					logger.error("error occured during initialising MovieSearchService.");
				}
	        }finally {
	            cli.complete();
	            response.setStatus(ExecutorResponse.STATUS_OK);
	    		WebServiceUtils.cleanupStub(stub);
	        }		
		
	}
	
    public static LocalAppsMovieServicesStub getMovieService() throws Exception {
    	LocalAppsMovieServicesStub stub = new LocalAppsMovieServicesStub(HtmlCommonUtil.getWSContext(),Util.getWsMovieTicketURL());
    	return stub;
    }
    
    public static MovieSearchServiceStub getService() throws Exception {
    	MovieSearchServiceStub stub = new MovieSearchServiceStub(HtmlCommonUtil.getWSContext(),Util.getWsMovieURL20());
    	return stub;
    }
    
    private static void debug(String message){
        if( logger.isDebugEnabled() )
        {
            logger.debug(message);
        }
    }
    
    public static void matchTheaterSchedule(TheaterItem theaterItem,List<ScheduleItem> scheduleList)
    {
		for(ScheduleItem scheduleItem:scheduleList)
		{
			if(theaterItem.getId() == scheduleItem.getTheaterId())
			{
				theaterItem.setScheduleItem(scheduleItem);
			}
		}
    }
    
    public static void matchMovieSchedule(MovieItem movieItem,List<ScheduleItem> scheduleList)
    {
		for(ScheduleItem scheduleItem:scheduleList)
		{
			if(movieItem.getId().equals(scheduleItem.getMovieId()))
			{
				movieItem.setScheduleItem(scheduleItem);
			}
		}
    }
}
