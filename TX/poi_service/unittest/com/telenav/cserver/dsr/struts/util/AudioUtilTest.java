package com.telenav.cserver.dsr.struts.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AudioUtilTest{

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAuidoById() {
		String ids = "1234,5678";
		AudioUtil.getAuidoById(ids);
		
	}

	@Test
	public void testGetFlowAction() {
		String screenType = "#";
		String flowType = "$";
		AudioUtil.getFlowAction(screenType, flowType);
	}

	@Test
	public void testGetFirstFlowAction() {
		String screenType = "#";
		AudioUtil.getFirstFlowAction(screenType);
	}

	@Test
	public void testGetSecondFlowAction() {
		String screenType = "#";
		AudioUtil.getSecondFlowAction(screenType);
	}

	@Test
	public void testGetThirdFlowAction() {
		String screenType = "#";
		AudioUtil.getThirdFlowAction(screenType);
	}

	@Test
	public void testGetForthFlowAction() {
		String screenType = "#";
		AudioUtil.getForthFlowAction(screenType);
	}

}
