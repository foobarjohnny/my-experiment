package com.telenav.browser.movie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.runner.RunWith;

import com.telenav.datatypes.content.movie.v10.MovieImageTypeEnum;
import com.telenav.services.content.moviesearchservice.v10.MovieImageRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchService;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchServiceStub;
import com.telenav.services.content.v10.ContentSearchService;
import com.telenav.services.content.v10.ContentSearchServiceStub;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.ws.datatypes.services.ServiceRequest;

@RunWith(Theories.class)
public class TestUtil {

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetService() throws Exception {
		MovieSearchService movieSearchService = Util.getService();
		assertNotNull(movieSearchService);
		assertTrue(movieSearchService instanceof MovieSearchServiceStub);
	}

	@Test
	public void testGetPoiService() throws Exception {
		ContentSearchService contentSearchService = Util.getPoiService();
		assertNotNull(contentSearchService);
		assertTrue(contentSearchService instanceof ContentSearchServiceStub);
	}

	@Test
	public void testSetClientProps() {
		ServiceRequest serviceRequest = new ServiceRequest();
		Util.setClientProps(serviceRequest);
		assertEquals(serviceRequest.getClientName(), "cose");
		assertEquals(serviceRequest.getClientVersion(),"1.0");
		assertEquals(serviceRequest.getTransactionId(), "1");
	}
	
	@Theory
	public void testCreateMovieImageRequestDTO1(@TestedOn(ints={240,320,360,400,480})int height,@TestedOn(ints={240,320,360,400,480})int width) {
		DataHandler mockDataHandler = EasyMock.createMock(DataHandler.class);
		String movieId = "1111";
		
		EasyMock.expect(mockDataHandler.getClientInfo(DataHandler.KEY_WIDTH)).andStubReturn(String.valueOf(height));
		EasyMock.expect(mockDataHandler.getClientInfo(DataHandler.KEY_HEIGHT)).andStubReturn(String.valueOf(width));
		EasyMock.replay(mockDataHandler); 
		MovieImageRequestDTO iReq = Util.createMovieImageRequestDTO(mockDataHandler, movieId);
		assertEquals(movieId, iReq.getMovieId());
		assertEquals(MovieImageTypeEnum.SMALL_IMAGE, iReq.getMovieImageType());
		assertEquals(120, iReq.getDisplayHeight());
		assertEquals(80, iReq.getDisplayWidth());
	}
	
	@Theory
	public void testCreateMovieImageRequestDTO2(@TestedOn(ints={480,640,800})int height,@TestedOn(ints={640,800})int width) {
		DataHandler mockDataHandler = EasyMock.createMock(DataHandler.class);
		String movieId = "2222";
		
		EasyMock.expect(mockDataHandler.getClientInfo(DataHandler.KEY_WIDTH)).andStubReturn(String.valueOf(height));
		EasyMock.expect(mockDataHandler.getClientInfo(DataHandler.KEY_HEIGHT)).andStubReturn(String.valueOf(width));
		EasyMock.replay(mockDataHandler); 
		MovieImageRequestDTO iReq = Util.createMovieImageRequestDTO(mockDataHandler, movieId);
		assertEquals(movieId, iReq.getMovieId());
		assertEquals(MovieImageTypeEnum.BIG_IMAGE, iReq.getMovieImageType());
		assertEquals(150, iReq.getDisplayHeight());
		assertEquals(100, iReq.getDisplayWidth());	
	}
}
