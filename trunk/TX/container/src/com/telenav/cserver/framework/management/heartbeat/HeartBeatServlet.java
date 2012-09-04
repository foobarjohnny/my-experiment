/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.heartbeat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.telenav.cserver.framework.Constants;
import com.telenav.cserver.framework.transportation.ServletUtil;

/**
 * HeartBeatServlet.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public class HeartBeatServlet extends HttpServlet
{

  HeartBeatConfiguration hbc = null;
  public void init()
  {
	  hbc = HeartBeatConfiguration.getInstance();
  }
  
  /**
   * doGet() just calls doPost()
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
     doPost(request, response);
  }
  
  public void doPost( HttpServletRequest request, HttpServletResponse response)
      throws ServletException, java.io.IOException
  {
	  byte [] respBuff = generateHeartbeatResult(true);
	  ServletUtil.sendResponse(response, respBuff);
  }
  
  
  private byte[] generateHeartbeatResult(boolean status)
  {

      String str_head = hbc.getHeadString();
      str_head = str_head.replaceAll(HeartBeatConfiguration.REPLACE_FLAG1, hbc.getServerName()==null?Constants.CSERVER_CLASS:hbc.getServerName());
      String sStatus = "";
      if (status)
      {
      	sStatus = "ok";
      } else 
      {
      	sStatus = "fail";
      }
      str_head = str_head.replaceAll(HeartBeatConfiguration.REPLACE_FLAG2, sStatus);  
      str_head = str_head.replaceAll(HeartBeatConfiguration.REPLACE_FLAG3, ""); 
      str_head = str_head.replaceAll(HeartBeatConfiguration.REPLACE_FLAG4, "");
      return str_head.getBytes();
  }

}
