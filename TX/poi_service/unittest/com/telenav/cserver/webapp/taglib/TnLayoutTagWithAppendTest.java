package com.telenav.cserver.webapp.taglib;


import javax.servlet.jsp.JspException;
import junit.framework.Assert;

import org.apache.struts.mock.MockPageContext;
import org.apache.struts.mock.MockServletConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.cserver.util.helper.Log2TxNode;


public class TnLayoutTagWithAppendTest {
	
	MockHttpServletRequest req = new MockHttpServletRequest(null);
	MockHttpServletResponse resp = new MockHttpServletResponse();
	MockServletConfig conf = new MockServletConfig();
	TnLayoutTagWithAppend instance = new TnLayoutTagWithAppend();
	MockPageContext pageContext = null;
	
	@Before
	public void setUp() throws Exception {
		req = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(req, "", 110);
		req.setServletPath("http://63.151.122.95:8080/poi_service/jsp/dsr/SpeakSearch1.jsp");
		pageContext = new MockPageContext(conf, req, resp);

		instance.setPageContext(pageContext);
		String includeFiles = "BrowserCommon";
		instance.setIncludeFiles(includeFiles);	
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testdoStartTag() throws JspException{
		instance.doStartTag();
	}
	
	@Test
	public void testOthers() throws JspException
	{
		instance.setSupportDual("supportDual");
		Assert.assertEquals(instance.getSupportDual(), "supportDual");
		
		instance.setSupportLocal("supportLocal");
		Assert.assertEquals(instance.getSupportLocal(), "supportLocal");
		
		instance.setIncludeFiles("includeFiles");
		Assert.assertEquals(instance.getIncludeFiles(), "includeFiles");
		
		instance.doAfterBody();
		instance.doEndTag();
		instance.clean();
	}
}
