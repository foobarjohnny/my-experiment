package com.telenav.browser.movie.datatypes;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestRatingImage {
	
	private int ordinal;
	private String starImage1;
	private String starImage2;
	private String starImage3;
	private String starImage4;
	
	public TestRatingImage(int ordinal, String starImage1, String starImage2, String starImage3, String starImage4) {
		this.ordinal = ordinal;
		this.starImage1 = starImage1;
		this.starImage2 = starImage2;
		this.starImage3 = starImage3;
		this.starImage4 = starImage4;
	}
	
	@Parameters       
	public static Collection<?> prepareData() 
	{        
		Object[][] data = 
		{
				{-1,"$imageUnSolid","$imageUnSolid","$imageUnSolid","$imageUnSolid"},
				{0,"$imageUnSolid","$imageUnSolid","$imageUnSolid","$imageUnSolid"}, 
				{1,"$imageHalf","$imageUnSolid","$imageUnSolid","$imageUnSolid"},
				{2,"$imageSolid","$imageUnSolid","$imageUnSolid","$imageUnSolid"},
				{3,"$imageSolid","$imageHalf","$imageUnSolid","$imageUnSolid"},
				{4,"$imageSolid","$imageSolid","$imageUnSolid","$imageUnSolid"},
				{5,"$imageSolid","$imageSolid","$imageHalf","$imageUnSolid"},
				{6,"$imageSolid","$imageSolid","$imageSolid","$imageUnSolid"},
				{7,"$imageSolid","$imageSolid","$imageSolid","$imageHalf"},
				{8,"$imageSolid","$imageSolid","$imageSolid","$imageSolid"},
		};                
		return Arrays.asList(data);    
	} 

	@Test
	public void testSelectImage() {
		RatingImage rImage = RatingImage.selectImage(ordinal);
		assertEquals(starImage1, rImage.getStarImage1());
		assertEquals(starImage2, rImage.getStarImage2());
		assertEquals(starImage3, rImage.getStarImage3());
		assertEquals(starImage4, rImage.getStarImage4());
	}

}
