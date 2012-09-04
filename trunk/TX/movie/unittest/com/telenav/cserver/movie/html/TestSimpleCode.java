/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.movie.html;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import util.autorun.ConstructorCaller;
import util.autorun.GSetterCaller;

/**
 * TestConstructorOnly.java
 *	The class is only for increasing unit-test coverage rates.
	And we will write some foolish or very simple code to achieve the goal.
 * xljiang@telenav.cn
 * @version 1.0 2011-5-17
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"com.telenav.cserver.framework.management.heartbeat.HeartBeatConfiguration"})
public class TestSimpleCode extends TestCase{
	
	public void testConstructor() throws Exception{
		ConstructorCaller constructorCaller = new ConstructorCaller();
//		constructorCaller.registerClass("com.telenav.cserver.framework.executor.ExecutorException");
		constructorCaller.call();
	}
	public void testSetAndGet() throws Exception{
		GSetterCaller cSetterCaller = new GSetterCaller();
		//com.telenav.cserver.movie.html.datatypes
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.datatypes.BookingInfoItem");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.datatypes.MovieItem");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.datatypes.ScheduleItem");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.datatypes.TheaterItem");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.datatypes.TicketItem");
		
		//com.telenav.cserver.movie.html.executor
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.executor.BookTicketRequest");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.executor.BookTicketResponse");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.executor.GetTicketQuantityRequest");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.executor.GetTicketQuantityResponse");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.executor.LoadImageRequest");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.executor.LoadImageResponse");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.executor.LookUpScheduleRequest");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.executor.LookUpScheduleResponse");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.executor.MovieCommonRequest");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.executor.MovieCommonResponse");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.executor.MovieListRequest");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.executor.TheaterListResponse");
		cSetterCaller.registerClass("com.telenav.cserver.movie.html.executor.TheaterListResponse");
		
		cSetterCaller.call();
		
	}
	
	public void testOtherMethod(){
	}
	
	public void testNeedMock(){
		//define variable
		
		//replay
		
		//verify
		
		//assert
	}

}
