package com.telenav.browser.movie.datatypes;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.runner.RunWith;

import com.telenav.datatypes.content.movie.v10.MovieImageTypeEnum;
import com.telenav.tnbrowser.util.DataHandler;

@RunWith(Theories.class)
public class TestMovieImageType {
	
	@Test
	public void testGetType() {
		assertEquals(MovieImageTypeEnum.BIG_IMAGE, MovieImageType.LandscapeBigImage.getType());
	}

	@Test
	public void testGetHeight() {
		assertEquals(150, MovieImageType.LandscapeBigImage.getHeight());
	}

	@Test
	public void testGetWidth() {
		assertEquals(225, MovieImageType.LandscapeBigImage.getWidth());
	}

	@Theory
	public void testSelectImage1(@TestedOn(ints={240,320,360,400,480})int height,@TestedOn(ints={240,320,360,400,480})int width) {	
		DataHandler mockDataHandler = EasyMock.createMock(DataHandler.class);
	
		EasyMock.expect(mockDataHandler.getClientInfo(DataHandler.KEY_WIDTH)).andStubReturn(String.valueOf(height));
		EasyMock.expect(mockDataHandler.getClientInfo(DataHandler.KEY_HEIGHT)).andStubReturn(String.valueOf(width));
		EasyMock.replay(mockDataHandler); 	
		assertEquals(MovieImageType.PortraitSmallImage, MovieImageType.selectImage(mockDataHandler));
	}
	
	@Theory
	public void testSelectImage2(@TestedOn(ints={480,640,800})int height,@TestedOn(ints={640,800})int width) {	
		DataHandler mockDataHandler = EasyMock.createMock(DataHandler.class);
	
		EasyMock.expect(mockDataHandler.getClientInfo(DataHandler.KEY_WIDTH)).andStubReturn(String.valueOf(height));
		EasyMock.expect(mockDataHandler.getClientInfo(DataHandler.KEY_HEIGHT)).andStubReturn(String.valueOf(width));
		EasyMock.replay(mockDataHandler); 	
		assertEquals(MovieImageType.PortraitBigImage, MovieImageType.selectImage(mockDataHandler));
	}
}
