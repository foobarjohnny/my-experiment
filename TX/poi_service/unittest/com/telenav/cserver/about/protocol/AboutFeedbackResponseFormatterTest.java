package com.telenav.cserver.about.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.about.executor.AboutFeedbackResponse;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.tnbrowser.util.DataHandler;

public class AboutFeedbackResponseFormatterTest {

	private AboutFeedbackResponseFormatter instance = new AboutFeedbackResponseFormatter();
	private DataSource dataSource = new DataSource();

	@Before
	public void setUp() throws Exception {

		dataSource.addData(HttpServletRequest.class.getName(), new MockHttpServletRequest());		
		dataSource.addData(ExecutorResponse.class.getName(), getAboutFeedbackResponse());
		dataSource.addData(AboutFeedbackResponse.class.getName(), getAboutFeedbackResponse());
	}

	@After
	public void tearDown() throws Exception {
		// clear the data
		dataSource.clear();
	}

	@Test
	public void testAboutFeedbackResponseFormatter() {
		GenericTest.getInstance().startTest(instance, dataSource);
	}
	
	public AboutFeedbackResponse getAboutFeedbackResponse(){
		
		AboutFeedbackResponse response = new AboutFeedbackResponse();
		response.setStatus(ExecutorResponse.STATUS_OK);
		return response;
	}

	public DataHandler getDataHandler(){
		
		DataHandler dataHandler = null;
		return dataHandler;
	}
	
}
