/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.datatypes.content.movie.v10.DetailTheaterInfoServiceResponse;
import com.telenav.datatypes.content.tnpoi.v10.TnPoi;

/**
 * TestMovieDataConvert.java
 * @TODO
 * @author  xljiang@telenav.cn
 * @version 1.0 2011-9-27
 */

public class TestMovieDataConvert extends TestCase{
	private MovieDataConvert convert = new MovieDataConvert();
	//theaters == null
	public void testConvertTheaterListResponse(){
		//prepare and replay
		TheaterListResponse response = new TheaterListResponse();
		DetailTheaterInfoServiceResponse tResp = PowerMock.createMock(DetailTheaterInfoServiceResponse.class);
		EasyMock.expect(tResp.getDetailTheaterInfo()).andReturn(null);
		
		PowerMock.replayAll();
		
		//invoke and verify
		MovieDataConvert.convertTheaterListResponse(response,tResp);
		PowerMock.verifyAll();
		
		//assert
		assertEquals(0,response.getTheaterList().size());
	}
	//theaters != null
	public void testConvertTheaterListResponse1(){
		//prepare and replay
		TheaterListResponse response = new TheaterListResponse();
		TnPoi tnPoi0 = new TnPoi();
		TnPoi tnPoi1 = new TnPoi();
		tnPoi0.setPoiId(0);
		tnPoi0.setBrandName("7 Days Inn");
		tnPoi1.setPoiId(1);
		tnPoi1.setPhoneNumber("1101");
		TnPoi[] theaters = new TnPoi[]{tnPoi0,tnPoi1};
		
		DetailTheaterInfoServiceResponse tResp = PowerMock.createMock(DetailTheaterInfoServiceResponse.class);
		EasyMock.expect(tResp.getDetailTheaterInfo()).andReturn(theaters);
		
		PowerMock.replayAll();
		
		//invoke and verify
		MovieDataConvert.convertTheaterListResponse(response,tResp);
		PowerMock.verifyAll();
		
		//assert
		assertEquals(2,response.getTheaterList().size());
		assertEquals(0,response.getTheaterList().get(0).getId());
		assertEquals("1101",response.getTheaterList().get(1).getPhoneNo());
	}
	
	public void testConvertMovieFullInfoServiceResponse(){
		
	}
}
