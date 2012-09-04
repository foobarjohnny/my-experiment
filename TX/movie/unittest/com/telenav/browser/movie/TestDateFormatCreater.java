package com.telenav.browser.movie;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDateFormatCreater {
	
	Logger logger = Logger.getLogger(TestDateFormatCreater.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetDateFormatCommon() {
		String format = DateFormatCreater.DATE_FORMAT_MONTH_DATE;
		assertTrue(DateFormatCreater.getDateFormat(format,"en_US") instanceof SimpleDateFormat);
	}
	
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void testGetDateFormatException() {	
		String format = DateFormatCreater.DATE_FORMAT_MONTH_DATE;
		assertTrue(DateFormatCreater.getDateFormat(format,"fds") instanceof SimpleDateFormat);
	}

	@Test
	public void testTransformStringToLocale() {
		Locale expected_es_MX = new Locale("es","MX");
		Locale expected_default = new Locale("en","US");
		
		String localString_es_MX = "es_MX";
		String localString_default = "";
		
		Locale actual_es_MX = DateFormatCreater.transformStringToLocale(localString_es_MX);
		assertEquals(expected_es_MX, actual_es_MX);
		
		Locale actual_default = DateFormatCreater.transformStringToLocale(localString_default);
		assertEquals(expected_default, actual_default);
	}

}
