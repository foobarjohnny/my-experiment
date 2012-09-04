package com.telenav.cserver.about.executor;

import java.util.Vector;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.cserver.weather.executor.TestUtil;

public class AboutFeedbackExecutorTest {

	private AboutFeedbackExecutor instance = new AboutFeedbackExecutor();
	private DataSource dataSource = new DataSource();

	
	
	@Before
	public void setUp() throws Exception {

		dataSource.addData(ExecutorRequest.class.getName(), getAboutFeedbackRequest());
		dataSource.addData(ExecutorResponse.class.getName(), getAboutFeedbackResponse());
		dataSource.addData(AboutFeedbackRequest.class.getName(), getAboutFeedbackRequest());
		dataSource.addData(AboutFeedbackResponse.class.getName(), getAboutFeedbackResponse());
		dataSource.addData(ExecutorContext.class.getName(), TestUtil.getExecutorContext());
		
	}

	@After
	public void tearDown() throws Exception {
		dataSource.clear();		// clear up the data
	}

	@Test
	public void testDoExecuteExecutorRequestExecutorResponseExecutorContext() {
		try
		{
			GenericTest.getInstance().startTest(instance, dataSource);
			
		}catch(Throwable e)
		{
			Assert.fail();
		}
	}
	
	private AboutFeedbackRequest getAboutFeedbackRequest(){
		
		AboutFeedbackRequest request = new AboutFeedbackRequest();
		request.setUserId(9826225L);
		Vector<String> answer = new Vector<String>();
		answer.add("Telenav's mission is to make people's life more easier and productive on the road!");
		request.setAnswers(answer);
		return request;
	}
	
	private AboutFeedbackResponse getAboutFeedbackResponse(){
		return new AboutFeedbackResponse();
	}

}
