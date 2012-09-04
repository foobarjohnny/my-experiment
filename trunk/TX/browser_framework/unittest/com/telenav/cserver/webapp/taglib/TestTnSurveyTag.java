package com.telenav.cserver.webapp.taglib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.struts.mock.MockHttpServletRequest;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;

import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;

import junit.framework.TestCase;

public class TestTnSurveyTag extends TestCase {
	
	TnSurveyTag tst;
    private TnContext tnContext;
    private DataHandler handler;
    
	public void setUp()
	{
		tst = new TnSurveyTag();
        tnContext = new TnContext();
        tnContext.addProperty(TnContext.PROP_CARRIER, "ATT");
        tnContext.addProperty(TnContext.PROP_DEVICE, "genericTest");
        tnContext.addProperty(TnContext.PROP_PRODUCT, "ANDROID");
        tnContext.addProperty(TnContext.PROP_VERSION, "6.2.01");
        tnContext.addProperty("application", "ATT_NAV");
        tnContext.addProperty("login", "3817799999");
        tnContext.addProperty("userid", "3707312");
        
        HttpServletRequest request = new MockHttpServletRequest();
        handler = new DataHandler(request);
        
        Hashtable clientInfo = new Hashtable();
        clientInfo.put(DataHandler.KEY_CARRIER, "ATT");
        clientInfo.put(DataHandler.KEY_PLATFORM, "ANDROID");
        clientInfo.put(DataHandler.KEY_VERSION, "6.2.01");
        clientInfo.put(DataHandler.KEY_PRODUCTTYPE, "ATT_NAV");
        clientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
        
        clientInfo.put(DataHandler.KEY_WIDTH, "480");
        clientInfo.put(DataHandler.KEY_HEIGHT, "800");
        clientInfo.put(DataHandler.KEY_LOCALE, "en_US");
        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "480-800");
        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "800-480");   
        handler.setClientInfo(clientInfo);
	}
	
	public void testGetComments() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		String comments = (String)setValue(tst, "comments", "This is test!");
		assertEquals(comments, tst.getComments());
	}
	
	public void testSetComments() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		String comments = "This is test2!";
		tst.setComments(comments);
		assertEquals(comments, (String)getValue(tst, "comments"));
	}
	
	public void testGetSubKey() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		String subKey = (String)setValue(tst, "subKey", "This is test!");
		assertEquals(subKey, tst.getSubKey());
	}
	
	public void testSetSubKey() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		String subKey = "This is test2!";
		tst.setSubKey(subKey);
		assertEquals(subKey, (String)getValue(tst, "subKey"));
	}
	
	public void testGetSubValue() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		String subValue = (String)setValue(tst, "subValue", "This is test!");
		assertEquals(subValue, tst.getSubValue());
	}
	
	public void testSetSubValue() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		String subValue = "This is test2!";
		tst.setSubValue(subValue);
		assertEquals(subValue, (String)getValue(tst, "subValue"));
	}
	
	
	public void testGetChoiceLabel() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		String choiceLabel = (String)setValue(tst, "choiceLabel", "This is test!");
		assertEquals(choiceLabel, tst.getChoiceLabel());
	}
	
	public void testSetChoiceLabel() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		String choiceLabel  = "This is test2!";
		tst.setChoiceLabel(choiceLabel);
		assertEquals(choiceLabel, (String)getValue(tst, "choiceLabel"));
	}
	
	public void testGetQuestionLabel() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		String questionLabel = (String)setValue(tst, "questionLabel", "This is test!");
		assertEquals(questionLabel, tst.getQuestionLabel());
	}
	
	public void testSetQuestionLabel() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		String questionLabel  = "This is test2!";
		tst.setQuestionLabel(questionLabel);
		assertEquals(questionLabel, (String)getValue(tst, "questionLabel"));
	}
	
	public void testGetQuestionsTxNodeName() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		String questionsTxNodeName = (String)setValue(tst, "questionsTxNodeName", "This is test!");
		assertEquals(questionsTxNodeName, tst.getQuestionsTxNodeName());
	}
	
	public void testSetQuestionsTxNodeName() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		String questionsTxNodeName  = "This is test2!";
		tst.setQuestionsTxNodeName(questionsTxNodeName);
		assertEquals(questionsTxNodeName, (String)getValue(tst, "questionsTxNodeName"));
	}
	
	
	public void testGetPageSize() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Object pageSize = setValue(tst, "pageSize", 100);
		assertEquals(pageSize, tst.getPageSize());
	}
	
	public void testSetPageSize() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		int pageSize  = 100;
		tst.setPageSize(pageSize);
		assertEquals(pageSize, getValue(tst, "pageSize"));
	}
	
	public void testGetPageNumber() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Object pageNumber = setValue(tst, "pageNumber", 100);
		assertEquals(pageNumber, tst.getPageNumber());
	}
	
	public void testSetPageNumber() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		int pageNumber  = 100;
		tst.setPageNumber(pageNumber);
		assertEquals(pageNumber, getValue(tst, "pageNumber"));
	}
	
	public void testClean() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		tst.clean();
		assertEquals(0, ((StringBuilder)getValue(tst, "outputText")).length());
		assertEquals(1, tst.getPageNumber());
		assertEquals(1, tst.getPageSize());
		assertEquals("", tst.getChoiceLabel());
		assertEquals("", tst.getQuestionsTxNodeName());
		assertEquals("", tst.getQuestionLabel());
	}
	
	public void testDoStartTag() throws JspException
	{
		assertEquals(0,tst.doStartTag());
	}
	
	public void testDoEndTagSingleWithText() throws JspException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		TxNode root = new TxNode();
		root.addValue(1);
		
		tst.setQuestionsTxNodeName("SurveyNodeName");
		TxNode node =  new TxNode();
		node.addValue(1);
		node.addMsg("SINGLE_WITH_TEXT");
		node.addMsg("Why did you decide not to subscribe to Premium Navigation?");
		node.addMsg("1");
		TxNode childNode = new TxNode();
		childNode.addMsg("Don&apos;t need these features");
		childNode.addMsg("Price is too high");
		
		node.addChild(childNode);
		root.addChild(node);
		IMocksControl RequestControl = EasyMock.createControl();
		
		HttpServletRequest request = RequestControl.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getAttribute(tst.getQuestionsTxNodeName())).andReturn(root).anyTimes();
        RequestControl.replay();
        
        IMocksControl pageContextControl = EasyMock.createControl();
		javax.servlet.jsp.PageContext pageContext = pageContextControl.createMock(javax.servlet.jsp.PageContext.class);
		EasyMock.expect(pageContext.getRequest()).andReturn(request).anyTimes();
		
		
		IMocksControl JspWritersControl = EasyMock.createControl();
		JspWriter jspwriter = JspWritersControl.createMock(JspWriter.class);
		EasyMock.expect(pageContext.getOut()).andReturn(jspwriter);
		pageContextControl.replay();
		
		tst.setPageContext(pageContext);
		tst.setPageNumber(1);
		
		tst.doEndTag();
		
		String strActual = ((StringBuilder)getValue(tst,"outputText")).toString();
		
		assertEquals("", strActual);
	}
	
	public void testDoEndTagMultipleWithText() throws JspException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		TxNode root = new TxNode();
		root.addValue(1);
		
		tst.setQuestionsTxNodeName("SurveyNodeName");
		TxNode node =  new TxNode();
		node.addValue(1);
		node.addMsg("MULTIPLE_WITH_TEXT");
		node.addMsg("Why did you decide not to subscribe to Premium Navigation?");
		node.addMsg("1");
		TxNode childNode = new TxNode();
		childNode.addMsg("Don&apos;t need these features");
		childNode.addMsg("Price is too high");
		
		node.addChild(childNode);
		root.addChild(node);
		IMocksControl RequestControl = EasyMock.createControl();
		
		HttpServletRequest request = RequestControl.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getAttribute(tst.getQuestionsTxNodeName())).andReturn(root).anyTimes();
        RequestControl.replay();
        
        IMocksControl pageContextControl = EasyMock.createControl();
		javax.servlet.jsp.PageContext pageContext = pageContextControl.createMock(javax.servlet.jsp.PageContext.class);
		EasyMock.expect(pageContext.getRequest()).andReturn(request).anyTimes();
		
		
		IMocksControl JspWritersControl = EasyMock.createControl();
		JspWriter jspwriter = JspWritersControl.createMock(JspWriter.class);
		EasyMock.expect(pageContext.getOut()).andReturn(jspwriter);
		pageContextControl.replay();
		
		tst.setPageContext(pageContext);
		tst.setPageNumber(1);
		
		tst.doEndTag();
		
		String strActual = ((StringBuilder)getValue(tst,"outputText")).toString();
		
		assertEquals("", strActual);
	}
	
	public void testDoEndTagMultipleWithTextCommentNotNull() throws JspException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		TxNode root = new TxNode();
		root.addValue(1);
		
		tst.setQuestionsTxNodeName("SurveyNodeName");
		TxNode node =  new TxNode();
		node.addValue(1);
		node.addMsg("MULTIPLE_WITH_TEXT");
		node.addMsg("Why did you decide not to subscribe to Premium Navigation?");
		node.addMsg("1");
		TxNode childNode = new TxNode();
		childNode.addMsg("Don&apos;t need these features");
		childNode.addMsg("Price is too high");
		
		node.addChild(childNode);
		root.addChild(node);
		IMocksControl RequestControl = EasyMock.createControl();
		
		HttpServletRequest request = RequestControl.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getAttribute(tst.getQuestionsTxNodeName())).andReturn(root).anyTimes();
        RequestControl.replay();
        
        IMocksControl pageContextControl = EasyMock.createControl();
		javax.servlet.jsp.PageContext pageContext = pageContextControl.createMock(javax.servlet.jsp.PageContext.class);
		EasyMock.expect(pageContext.getRequest()).andReturn(request).anyTimes();
		
		
		IMocksControl JspWritersControl = EasyMock.createControl();
		JspWriter jspwriter = JspWritersControl.createMock(JspWriter.class);
		EasyMock.expect(pageContext.getOut()).andReturn(jspwriter);
		pageContextControl.replay();
		
		tst.setPageContext(pageContext);
		tst.setPageNumber(1);
		tst.setComments("This is test Comments");
		
		tst.doEndTag();
		
		String strActual = ((StringBuilder)getValue(tst,"outputText")).toString();
		
		assertEquals("", strActual);
	}
	
	public void testDoEndTagMultiple() throws JspException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		TxNode root = new TxNode();
		root.addValue(1);
		
		tst.setQuestionsTxNodeName("SurveyNodeName");
		TxNode node =  new TxNode();
		node.addValue(1);
		node.addMsg("MULTIPLE");
		node.addMsg("Why did you decide not to subscribe to Premium Navigation?");
		node.addMsg("1");
		TxNode childNode = new TxNode();
		childNode.addMsg("Don&apos;t need these features");
		childNode.addMsg("Price is too high");
		
		node.addChild(childNode);
		root.addChild(node);
		IMocksControl RequestControl = EasyMock.createControl();
		
		HttpServletRequest request = RequestControl.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getAttribute(tst.getQuestionsTxNodeName())).andReturn(root).anyTimes();
        RequestControl.replay();
        
        IMocksControl pageContextControl = EasyMock.createControl();
		javax.servlet.jsp.PageContext pageContext = pageContextControl.createMock(javax.servlet.jsp.PageContext.class);
		EasyMock.expect(pageContext.getRequest()).andReturn(request).anyTimes();
		
		
		IMocksControl JspWritersControl = EasyMock.createControl();
		JspWriter jspwriter = JspWritersControl.createMock(JspWriter.class);
		EasyMock.expect(pageContext.getOut()).andReturn(jspwriter);
		pageContextControl.replay();
		
		tst.setPageContext(pageContext);
		tst.setPageNumber(1);
		
		tst.doEndTag();
		
		String strActual = ((StringBuilder)getValue(tst,"outputText")).toString();
		
		assertEquals("", strActual);
	}
	
	public void testDoEndTagSingle() throws JspException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		TxNode root = new TxNode();
		root.addValue(1);
		
		tst.setQuestionsTxNodeName("SurveyNodeName");
		TxNode node =  new TxNode();
		node.addValue(1);
		node.addMsg("SINGLE");
		node.addMsg("Why did you decide not to subscribe to Premium Navigation?");
		node.addMsg("1");
		TxNode childNode = new TxNode();
		childNode.addMsg("Don&apos;t need these features");
		childNode.addMsg("Price is too high");
		
		node.addChild(childNode);
		root.addChild(node);
		IMocksControl RequestControl = EasyMock.createControl();
		
		HttpServletRequest request = RequestControl.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getAttribute(tst.getQuestionsTxNodeName())).andReturn(root).anyTimes();
        RequestControl.replay();
        
        IMocksControl pageContextControl = EasyMock.createControl();
		javax.servlet.jsp.PageContext pageContext = pageContextControl.createMock(javax.servlet.jsp.PageContext.class);
		EasyMock.expect(pageContext.getRequest()).andReturn(request).anyTimes();
		
		
		IMocksControl JspWritersControl = EasyMock.createControl();
		JspWriter jspwriter = JspWritersControl.createMock(JspWriter.class);
		EasyMock.expect(pageContext.getOut()).andReturn(jspwriter);
		pageContextControl.replay();
		
		tst.setPageContext(pageContext);
		tst.setPageNumber(1);
		
		tst.doEndTag();
		
		String strActual = ((StringBuilder)getValue(tst,"outputText")).toString();
		
		assertEquals("", strActual);
	}
	
	public void testDoEndTagSingleLineText() throws JspException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		TxNode root = new TxNode();
		root.addValue(1);
		
		tst.setQuestionsTxNodeName("SurveyNodeName");
		TxNode node =  new TxNode();
		node.addValue(1);
		node.addMsg("SINGLE_LINE_TEXT");
		node.addMsg("Why did you decide not to subscribe to Premium Navigation?");
		node.addMsg("1");
		TxNode childNode = new TxNode();
		childNode.addMsg("Don&apos;t need these features");
		childNode.addMsg("Price is too high");
		
		node.addChild(childNode);
		root.addChild(node);
		IMocksControl RequestControl = EasyMock.createControl();
		
		HttpServletRequest request = RequestControl.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getAttribute(tst.getQuestionsTxNodeName())).andReturn(root).anyTimes();
        RequestControl.replay();
        
        IMocksControl pageContextControl = EasyMock.createControl();
		javax.servlet.jsp.PageContext pageContext = pageContextControl.createMock(javax.servlet.jsp.PageContext.class);
		EasyMock.expect(pageContext.getRequest()).andReturn(request).anyTimes();
		
		
		IMocksControl JspWritersControl = EasyMock.createControl();
		JspWriter jspwriter = JspWritersControl.createMock(JspWriter.class);
		EasyMock.expect(pageContext.getOut()).andReturn(jspwriter);
		pageContextControl.replay();
		
		tst.setPageContext(pageContext);
		tst.setPageNumber(1);
		
		tst.doEndTag();
		
		String strActual = ((StringBuilder)getValue(tst,"outputText")).toString();
		
		assertEquals("", strActual);
	}
	
	public void testDoEndTagText() throws JspException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		TxNode root = new TxNode();
		root.addValue(1);
		
		tst.setQuestionsTxNodeName("SurveyNodeName");
		TxNode node =  new TxNode();
		node.addValue(1);
		node.addMsg("TEXT");
		node.addMsg("Why did you decide not to subscribe to Premium Navigation?");
		node.addMsg("1");
		TxNode childNode = new TxNode();
		childNode.addMsg("Don&apos;t need these features");
		childNode.addMsg("Price is too high");
		
		node.addChild(childNode);
		root.addChild(node);
		IMocksControl RequestControl = EasyMock.createControl();
		
		HttpServletRequest request = RequestControl.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getAttribute(tst.getQuestionsTxNodeName())).andReturn(root).anyTimes();
        RequestControl.replay();
        
        IMocksControl pageContextControl = EasyMock.createControl();
		javax.servlet.jsp.PageContext pageContext = pageContextControl.createMock(javax.servlet.jsp.PageContext.class);
		EasyMock.expect(pageContext.getRequest()).andReturn(request).anyTimes();
		
		
		IMocksControl JspWritersControl = EasyMock.createControl();
		JspWriter jspwriter = JspWritersControl.createMock(JspWriter.class);
		EasyMock.expect(pageContext.getOut()).andReturn(jspwriter);
		pageContextControl.replay();
		
		tst.setPageContext(pageContext);
		tst.setPageNumber(1);
		
		tst.doEndTag();
		
		String strActual = ((StringBuilder)getValue(tst,"outputText")).toString();
		
		assertEquals("", strActual);
	}
	
	public void testDoEndTagInfo() throws JspException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		TxNode root = new TxNode();
		root.addValue(1);
		
		tst.setQuestionsTxNodeName("SurveyNodeName");
		TxNode node =  new TxNode();
		node.addValue(1);
		node.addMsg("INFO");
		node.addMsg("Why did you decide not to subscribe to Premium Navigation?");
		node.addMsg("1");
		TxNode childNode = new TxNode();
		childNode.addMsg("Don&apos;t need these features");
		childNode.addMsg("Price is too high");
		
		node.addChild(childNode);
		root.addChild(node);
		IMocksControl RequestControl = EasyMock.createControl();
		
		HttpServletRequest request = RequestControl.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getAttribute(tst.getQuestionsTxNodeName())).andReturn(root).anyTimes();
        RequestControl.replay();
        
        IMocksControl pageContextControl = EasyMock.createControl();
		javax.servlet.jsp.PageContext pageContext = pageContextControl.createMock(javax.servlet.jsp.PageContext.class);
		EasyMock.expect(pageContext.getRequest()).andReturn(request).anyTimes();
		
		
		IMocksControl JspWritersControl = EasyMock.createControl();
		JspWriter jspwriter = JspWritersControl.createMock(JspWriter.class);
		EasyMock.expect(pageContext.getOut()).andReturn(jspwriter);
		pageContextControl.replay();
		
		tst.setPageContext(pageContext);
		tst.setPageNumber(1);
		
		tst.doEndTag();
		
		String strActual = ((StringBuilder)getValue(tst,"outputText")).toString();
		
		assertEquals("", strActual);
	}
	
	public static Object getValue(Object instance, String fieldName) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Field field = getField(instance.getClass(), fieldName);
		field.setAccessible(true);
		return field.get(instance);
	}
	
	public static Object setValue(Object instance, String fieldName, Object newValue) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Field field = getField(instance.getClass(), fieldName);
		field.setAccessible(true);
		field.set(instance, newValue);
		return field.get(instance);
	}
	
	public static Field getField(Class<? extends Object> thisClass, String fieldName)throws NoSuchFieldException
	{
		return thisClass.getDeclaredField(fieldName);
	}
	
	public static Method getMethod(Class<? extends Object> thisClass, String methodName ,Class<?> parameter)throws SecurityException, NoSuchMethodException
	{
		Method method = null;
		if(null == parameter)
		{
			method = thisClass.getDeclaredMethod(methodName);
		}
		else
		{
			method = thisClass.getDeclaredMethod(methodName, parameter);
		}
		method.setAccessible(true);
		return method;
	}

}
