/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.browser.framework.struts;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.transportation.ServletUtil;
import com.telenav.cserver.framework.trump.ExecutorServiceSupport;
import com.telenav.cserver.framework.trump.TrumpRunnable;

/**
 * @author pzhang
 * 
 * @version 1.0, 2009-7-20
 */
public class BrowserStartupAction extends HttpServlet {
	
	private static Logger logger = Logger.getLogger(BrowserStartupAction.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	  /**
	   * doGet() just calls doPost()
	   */
	  public void doGet(HttpServletRequest request, HttpServletResponse response)
	      throws ServletException, IOException
	  {
	     doPost(request, response);
	  }
	  
	  /**
	   * 
	   */
	  public void doPost( HttpServletRequest request, HttpServletResponse response)
	      throws ServletException, java.io.IOException
	  {
		  //re-run trump
		  runTrump();
		  //
		  Date currentDate = new Date();
		  String displayContent = "Refresh OK on " + currentDate.toString() + "\r\n<br/>\r\n<br/>";
		  ServletUtil.sendResponse(response, displayContent.getBytes());
		 
	  }
	 
	  /**
	   * 
	   */
	  private void runTrump()
	  {
			//first will unzipAll resource, since these resources as resource_loader.xml would be used in the later initialization
			logger.debug("-----before unzip device config files-------");
			try
			{
				TrumpRunnable.getTrumpRunnable().unzipAll();
			}
			catch(Exception e)
			{
				System.err.println("-----throw exception when try to unzip device config files-------");
				logger.error("-----throw exception when try to unzip device config files-------");
			}
			logger.debug("-----after unzip device config files-------");
					  
	  }
	  
	@Override
	public void init() throws ServletException {
		super.init();
		
		runTrump();
		
		logger.debug("-----before ExecutorServiceSupport.sumbitWithHourPeriod, the parameter is 1000------");
		ExecutorServiceSupport.sumbitWithHourPeriod(TrumpRunnable.getTrumpRunnable(),1000, 1);
		logger.debug("-----after ExecutorServiceSupport.sumbitWithHourPeriod------");
    }
	
	@Override
	public void destroy() {
		super.destroy();
		ExecutorServiceSupport.stop();
		try
		{
			//sleep for a while
			Thread.sleep(2000);
		}catch(Exception e)
		{
			logger.warn("Exception occurs when destroying BrowserStartupAction",e);
		}
	}
}
