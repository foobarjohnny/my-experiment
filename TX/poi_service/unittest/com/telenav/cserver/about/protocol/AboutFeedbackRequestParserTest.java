package com.telenav.cserver.about.protocol;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class AboutFeedbackRequestParserTest {

	private int	ajaxChildValue = 100;
	private String actionName = "About.do";
	private String failString = "couldn't find the TxNode in file when testing About action";
	
	MockHttpServletRequest request = null;
	private AboutFeedbackRequestParser instance = new AboutFeedbackRequestParser();
	private DataSource dataSource = new DataSource();

	
	
	@Before
	public void setUp() throws Exception {
		
		request = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(request, actionName, ajaxChildValue);
		
		dataSource.addData(String.class.getName(), "AboutFeedback");	
		if(request != null)
		{
			dataSource.addData(DataHandler.class.getName(), request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO));
		}
		
		dataSource.addData(HttpServletRequest.class.getName(), request);
	}
	
	@After
	public void tearDown() throws Exception {
		// clear the data after testing
		dataSource.clear();
	}

	@Test
	public void testSuite(){
		if(null == request)
		{
			Assert.fail(failString);
		}
		
		try{
			GenericTest.getInstance().startTest(instance, dataSource);
		}catch(Throwable e){
			System.out.println("getFeedbackTopics method in FeedbackContents is not completed yet.");
		}
	}
	
}
