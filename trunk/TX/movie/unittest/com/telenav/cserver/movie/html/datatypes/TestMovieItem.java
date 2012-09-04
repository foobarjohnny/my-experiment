/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.movie.html.datatypes;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * TestMovieItem.java
 * @TODO
 * @author  xljiang@telenav.cn
 * @version 1.0 2011-9-27
 */

public class TestMovieItem extends TestCase{
	private List<TheaterItem> theaterList = new ArrayList<TheaterItem>();
	
	public void testGetTheaterIds(){
		TheaterItem theaterItem = new TheaterItem();
		TheaterItem theaterItem1 = new TheaterItem();
		theaterItem.setId(326);
		theaterItem1.setId(327);
		theaterList.add(theaterItem);
		theaterList.add(theaterItem1);
		
		MovieItem movieItem = new MovieItem();
		movieItem.setTheaterList(theaterList);
		
		long[] result = movieItem.getTheaterIds();
		
		assertEquals(2,result.length);
		assertEquals(326,result[0]);
		assertEquals(327,result[1]);
	}

}
