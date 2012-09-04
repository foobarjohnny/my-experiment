package com.telenav.cserver.about.datatypes;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class FeedbackContentsTest {
	
	public static final String parameterOfString = "string";
	public static final String unknowError = "unknown reason from %s";
	public static final String locale = "en_US";
	
	HashMap<String, Object> dataCenter = new HashMap<String, Object>();
	
	@Before
	public void setUp() throws Exception {
		dataCenter.put(FeedbackContentsTest.parameterOfString, FeedbackContentsTest.locale);
	}

	@After
	public void tearDown() throws Exception {
		dataCenter.clear(); //clear the resource
	}

	@Test
	public void testGetFeedbackTopics() {
		// use reflect mechanism to test private method in FeedbackContents
		String methodName = "getFeedbackTopics";
		try{
			Class<?>[] parameter = {String.class};
			Object[] parameterObj = {dataCenter.get(FeedbackContentsTest.parameterOfString)};
			
			Method m = FeedbackContents.class.getMethod(methodName, parameter);
			Object o = m.invoke(FeedbackContents.getInstance(), parameterObj);
			Assert.assertNull(o);
			
		}catch(NoSuchMethodException e){
			e.printStackTrace();
		}catch(Throwable e){
			e.printStackTrace();
		}
	}

}
