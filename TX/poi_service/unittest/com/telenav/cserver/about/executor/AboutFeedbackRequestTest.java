package com.telenav.cserver.about.executor;

import java.util.Vector;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.xnav.feedback.FeedbackData;

public class AboutFeedbackRequestTest {
	
	private AboutFeedbackRequest instance = new AboutFeedbackRequest();
	
	Vector<FeedbackData> answer = new Vector<FeedbackData>();
	private DataSource dataSrc = new DataSource();

	@Before
	public void setUp() throws Exception {
		
		dataSrc.addData(long.class.getName(),Long.valueOf(111111111));
		dataSrc.addData(String.class.getName(),String.valueOf("toString"));
		answer.add(new FeedbackData(1234, "Telenav's mission is to make people's life more easier and productive on the road!"));
		answer.add(new FeedbackData(1234, "go, Telenaver"));
		dataSrc.addData(Vector.class.getName(), answer);
	
	}

	@After
	public void tearDown() throws Exception {
		dataSrc.clear();  		// clear the data
	}

	@Test
	public void testSuite(){

		try
		{
			GenericTest.getInstance().startTest(instance, dataSrc);
		}catch(Throwable e)
		{
			Assert.fail("Fail to pass the test!");
		}
	}
	

}
