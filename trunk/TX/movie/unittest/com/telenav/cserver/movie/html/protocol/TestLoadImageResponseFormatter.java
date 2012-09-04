package com.telenav.cserver.movie.html.protocol;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.movie.html.executor.LoadImageResponse;

public class TestLoadImageResponseFormatter {

	private LoadImageResponseFormatter responseFormattor;
	private MockHttpServletRequest httpRequest;
	private LoadImageResponse executorResponse;
	
	@Before
	public void setUp() throws Exception {
		 Map<String,String> images = new LinkedHashMap<String, String>();
		 images.put("image1", "image1");
		 images.put("image2", "image2");
		 images.put("image3", "image3");
		 executorResponse = new LoadImageResponse();
		 executorResponse.setImages(images);
		 
		 httpRequest = new MockHttpServletRequest();
		 responseFormattor = new LoadImageResponseFormatter();
	}

	@Test
	public void testParseBrowserResponse() {
		try {
			responseFormattor.parseBrowserResponse(httpRequest, executorResponse);
			Assert.assertNotNull(httpRequest.getAttribute("ajaxResponse"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
