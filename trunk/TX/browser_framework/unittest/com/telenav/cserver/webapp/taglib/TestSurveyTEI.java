package com.telenav.cserver.webapp.taglib;

import java.util.Hashtable;

import junit.framework.TestCase;
import com.telenav.cserver.webapp.taglib.TnSurveyTEI;
import javax.servlet.jsp.tagext.TagData;

public class TestSurveyTEI extends TestCase {
	
	TnSurveyTEI tst;
	
	public void setUp()
	{
		tst = new TnSurveyTEI();
	}
	
	public void testIsValid()
	{
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put("pageSize", "100");
		ht.put("pageNumber", "1");
		TagData data = new TagData(ht);
		assertTrue(tst.isValid(data));
		
	}
	
	public void testIsValidFalse1()
	{
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put("pageSize", (TagData.REQUEST_TIME_VALUE).toString());
		ht.put("pageNumber", "1");
		TagData data = new TagData(ht);
		assertTrue(!tst.isValid(data));
	}
	
	public void testIsValidFalse2()
	{
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put("pageSize", "100");
		ht.put("pageNumber", (TagData.REQUEST_TIME_VALUE).toString());
		TagData data = new TagData(ht);
		assertTrue(!tst.isValid(data));
	}
	
	public void testIsValidFalse3()
	{
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put("pageSize", "-1");
		ht.put("pageNumber", "1");
		TagData data = new TagData(ht);
		assertTrue(!tst.isValid(data));
	}

}
