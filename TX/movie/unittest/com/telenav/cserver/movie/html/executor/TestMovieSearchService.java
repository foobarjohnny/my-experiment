/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.movie.html.datatypes.MovieItem;
import com.telenav.cserver.movie.html.datatypes.TheaterItem;
import com.telenav.cserver.movie.html.util.HtmlConstants.RRKey;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestAddressSharingService
 * @author pzhang
 * @date 2010-8-9
 */
public class TestMovieSearchService extends TestCase
{
    private TnContext tnContext;
	private String searchDate;
	private Stop address;

    @Override
    protected void setUp() throws Exception
    {
        tnContext = new TnContext();
        tnContext.addProperty(TnContext.PROP_CARRIER, "ATT");
        tnContext.addProperty(TnContext.PROP_DEVICE, "9000");
        tnContext.addProperty(TnContext.PROP_PRODUCT, "RIM");
        tnContext.addProperty(TnContext.PROP_VERSION, "6.0.01");
        tnContext.addProperty("application", "ATT_NAV");
        tnContext.addProperty("login", "3817799999");
        tnContext.addProperty("userid", "3707312");
        
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        searchDate = sdf.format(today);
        
        System.out.println(searchDate);
        
        address = new Stop();
        address.lat = 3761386;
        address.lon = -12239382;
    }

    
    /**
     * 
     * @throws ThrottlingException
     */
    public void testSearchSimpleMovieList()
    {
    	//test simple movie list
    	MovieCommonRequest request = new MovieCommonRequest();
    	request.setSearchDate(searchDate);
    	request.setAddress(address);
    	MovieListResponse response = new MovieListResponse();
    	
    	MovieServiceProxy.searchMovies(request, response);
    	List<MovieItem> movieList = response.getMoiveList();
    	MovieItem movieItem = new MovieItem();
    	if(movieList.size() >0)
    	{
    		movieItem = movieList.get(0);
    		assertEquals("1", "1"); 
    	}
    	else
    	{
    		assertEquals("1", "0"); 
    	}
    	
    	//test sub theater list and schedule
    	request.setMovieId(movieItem.getId());
    	TheaterListResponse theaterListResponse = new TheaterListResponse();
    	MovieServiceProxy.lookupSubTheatreList(request, theaterListResponse);
    	
    	String[] movieIds = new String[movieList.size()];
    	for(int i=0;i<movieList.size();i++)
    	{
    		movieIds[i] = movieList.get(i).getId();
    	}
    	//test load images
    	 String[] images = MovieServiceProxy.loadImages(movieIds);
    }
    
    /**
     * 
     * @throws ThrottlingException
     */
    public void testSearchSimpleTheaterList()
    {
    	MovieCommonRequest request = new MovieCommonRequest();
    	request.setSearchDate(searchDate);
    	request.setAddress(address);
    	TheaterListResponse response = new TheaterListResponse();
    	
    	MovieServiceProxy.searchTheatresWithMovieAndSchedule(request, response);
    	List<TheaterItem> theaterList = response.getTheaterList();
    	TheaterItem theaterItem = new TheaterItem();
    	if(theaterList.size() >0)
    	{
    		theaterItem = theaterList.get(0);
    		assertEquals("1", "1"); 
    	}
    	else
    	{
    		assertEquals("1", "0"); 
    	}
    	//test sub movie list and schedule
    	request.setTheaterId(theaterItem.getId());
    	MovieListResponse movieListResponse = new MovieListResponse();
    	MovieServiceProxy.lookupSubMovieList(request, movieListResponse);
    	
    }
    
    public void testGetTicketPrice()
    {
    	GetTicketQuantityRequest request = new GetTicketQuantityRequest();
        GetTicketQuantityResponse response = new GetTicketQuantityResponse();
        
        request.setMovieId("101380");
        request.setTicketTheaterId("AAQLT");
        request.setSearchDate("2011:12:24");
        request.setShowTime("14:55");
        request.setUserId(3707312L);
        MovieServiceProxy.getTicketPrices(request, response); 
    }
}
