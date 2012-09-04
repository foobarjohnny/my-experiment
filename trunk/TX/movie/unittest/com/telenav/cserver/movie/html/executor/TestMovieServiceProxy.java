/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.browser.movie.Constant;
import com.telenav.browser.movie.Util;
import com.telenav.cserver.movie.html.datatypes.TheaterItem;
import com.telenav.datatypes.content.movie.v10.Area;
import com.telenav.datatypes.content.movie.v10.MovieSearchDate;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.services.content.moviesearchservice.v10.DetailTheaterInfoServiceResponseDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchService;
import com.telenav.services.content.moviesearchservice.v10.TheaterLookupRequestDTO;

/**
 * TestMovieServiceProxy.java
 * @TODO
 * @author  xljiang@telenav.cn
 * @version 1.0 2011-8-30
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MovieServiceProxy.class,MovieDataConvert.class,Util.class})
@SuppressStaticInitializationFor({"com.telenav.browser.movie.Util"})
public class TestMovieServiceProxy extends TestCase{
	private MovieCommonRequest movieCommonRequest = PowerMock.createMock(MovieCommonRequest.class);
	private TheaterListResponse theaterListResponse = PowerMock.createMock(TheaterListResponse.class);
	private LookUpScheduleRequest lookUpScheduleRequest = PowerMock.createMock(LookUpScheduleRequest.class);
	private LookUpScheduleResponse lookUpScheduleResponse = PowerMock.createMock(LookUpScheduleResponse.class);
	private MovieServiceProxy movieServiceProxy = new MovieServiceProxy ();// for coverage, don't delete me
	public void testLookupSubTheatreList() throws Exception{
		//prepare and replay
		PowerMock.mockStaticPartial(MovieServiceProxy.class, "lookupTheatresWithDetailInfo","lookupSchedules","matchTheaterSchedule");
		MovieCommonRequest movieCommonRequest = new MovieCommonRequest();
		TheaterListResponse theaterListResponse = new TheaterListResponse();
		//define
		List<TheaterItem> theaterList = new ArrayList<TheaterItem>();
		
		theaterList.add(new TheaterItem());
		theaterList.add(new TheaterItem());
		theaterList.add(new TheaterItem());
		theaterListResponse.setTheaterList(theaterList);
		//process
		MovieServiceProxy.lookupTheatresWithDetailInfo(movieCommonRequest, theaterListResponse);
		MovieServiceProxy.lookupSchedules(EasyMock.anyObject(LookUpScheduleRequest.class),EasyMock.anyObject(LookUpScheduleResponse.class));
		MovieServiceProxy.matchTheaterSchedule(EasyMock.anyObject(TheaterItem.class),EasyMock.anyObject(List.class));
		EasyMock.expectLastCall().times(3);
		PowerMock.replayAll();
		//invoke and verify
		MovieServiceProxy.lookupSubTheatreList(movieCommonRequest, theaterListResponse);
		PowerMock.verifyAll();
		//assert
		assertEquals(3,theaterListResponse.getTheaterList().size());
	}
	public void testLookupTheatresWithDetailInfo() throws Exception{
		lookupTheatresWithDetailInfo0("");
		lookupTheatresWithDetailInfo0("aaa");
	}
	public void lookupTheatresWithDetailInfo0(String movieId) throws Exception{
		//prepare and replay
		PowerMock.mockStaticPartial(MovieServiceProxy.class, "getService");
		PowerMock.mockStatic(MovieDataConvert.class);
		PowerMock.mockStatic(Util.class);
		MovieCommonRequest movieCommonRequest = new MovieCommonRequest();
		TheaterListResponse theaterListResponse = new TheaterListResponse();
		MovieSearchService service = PowerMock.createMock(MovieSearchService.class);
		DetailTheaterInfoServiceResponseDTO tResp = PowerMock.createMock(DetailTheaterInfoServiceResponseDTO.class);
		com.telenav.ws.datatypes.services.ResponseStatus rsps = new com.telenav.ws.datatypes.services.ResponseStatus();
		MovieSearchDate searchDate = PowerMock.createMock(MovieSearchDate.class);
		//define
		Stop address = new Stop();
		address.city = "SH";
		address.lat = -1;
		address.lon = -11;
		movieCommonRequest.setAddress(address);
		movieCommonRequest.setDistanceUnit(250);
		movieCommonRequest.setSearchDate("2011-05-25");
		movieCommonRequest.setMovieId(movieId);
		
		//process
		EasyMock.expect(Util.getSearchDate("2011-05-25")).andReturn(searchDate);
		//EasyMock.expect(MovieServiceProxy.getService()).andReturn(service);
		EasyMock.expect(Util.getArea(-1, -11, Constant.SEARCH_RADIUS)).andReturn(new Area());
		EasyMock.expect(service.lookupTheatresWithDetailInfo(EasyMock.anyObject(TheaterLookupRequestDTO.class))).andReturn(tResp);
		Util.setClientProps(EasyMock.anyObject(TheaterLookupRequestDTO.class));
		EasyMock.expect(tResp.getResponseStatus()).andReturn(rsps);
		MovieDataConvert.convertTheaterListResponse(theaterListResponse,tResp);
		PowerMock.replayAll();
		//invoke and verify
		
		MovieServiceProxy.lookupTheatresWithDetailInfo(movieCommonRequest,theaterListResponse);
		PowerMock.verifyAll();
		//assert
	}
	public void testLookupTheatresWithDetailInfo_exception() throws Exception{
		//prepare and replay
		PowerMock.mockStaticPartial(MovieServiceProxy.class, "getService");
		PowerMock.mockStatic(Util.class);
		MovieCommonRequest movieCommonRequest = new MovieCommonRequest();
		TheaterListResponse theaterListResponse = new TheaterListResponse();
		//define
		Stop address = new Stop();
		address.city = "SH";
		address.lat = -1;
		address.lon = -11;
		movieCommonRequest.setAddress(address);
		movieCommonRequest.setDistanceUnit(250);
		movieCommonRequest.setSearchDate("2011-05-25");
		
		//process
		EasyMock.expect(Util.getSearchDate("2011-05-25")).andReturn(null);
		EasyMock.expect(MovieServiceProxy.getService()).andThrow(new Exception());
		PowerMock.replayAll();
		//invoke and verify
		
		MovieServiceProxy.lookupTheatresWithDetailInfo(movieCommonRequest,theaterListResponse);
		PowerMock.verifyAll();
		//assert
	}

}
