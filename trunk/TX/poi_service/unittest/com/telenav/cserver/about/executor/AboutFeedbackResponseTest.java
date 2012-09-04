package com.telenav.cserver.about.executor;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AboutFeedbackResponseTest {
	
	private AboutFeedbackResponse instance = new AboutFeedbackResponse();
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSuite(){
		// have nothing to do
		System.out.append(instance.getClass().getName() + " has do nothing now");
		Assert.assertTrue(true);
	}

}
