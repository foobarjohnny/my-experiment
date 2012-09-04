<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>	
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<% 
	DataHandler handler = (DataHandler)request.getAttribute("DataHandler");
%>
<tml:TML outputMode="TxNode">
	<%@ include file="Search1Common.jsp"%>
	<%@ include file="/touch/jsp/weather/controller/WeatherController.jsp"%>
	<%@ include file="/touch/jsp/poi/controller/PoiListController.jsp"%>
	<tml:script language="fscript" version="1">
	<![CDATA[
		func loadSpecific()
			DSRCommon_M_saveDSRType("<%=AudioConstants.DSR_RECOGNIZE_COMMAND_CONTROL%>")
		endfunc
		
		func onClickInstruction()
			releaseResource()
		endfunc

		func getCurrentLocationOfTheater(int type, int gpstime, int netTime, int timeout)
			JSONObject locParm
			JSONObject.put(locParm,"LocationType",type)
			JSONObject.put(locParm,"GpsLocationValidTime",gpstime*1000)
			JSONObject.put(locParm,"NetworkLocationValidTime",netTime*1000)
			JSONObject.put(locParm,"Timeout", timeout*1000)
			TxNode locParmNode
			TxNode.addMsg(locParmNode,""+locParm)
			MenuItem.setBean("doGetGpsOfTheater","locParam",locParmNode)
			 System.doAction("doGetGpsOfTheater")
		endfunc
			
		func returnCurrentLocationOfTheater()
			TxNode currentLocationNode = ParameterSet.getParam("currentLocation")
        	JSONObject stop 
			 String joStr =TxNode.msgAt(currentLocationNode,0)
			 JSONObject joStop = JSONObject.fromString(joStr)
			 String callbackId = JSONObject.get(joStop,"CallbackID")
			 
			 if(callbackId!="Success")
				handleGpsErrorOfTheater(callbackId)
			 else
				 String location = JSONObject.getString(joStop,"Location")
				 JSONObject gps= JSONObject.fromString(location)
				 
				 JSONObject.put(stop,"type",6)	
				 JSONObject.put(stop,"lat",JSONObject.get(gps,"Lat"))
				 JSONObject.put(stop,"lon",JSONObject.get(gps,"Lon"))
				 JSONObject.put(stop,"label","")
				 JSONObject.put(stop,"firstLine","")
				 JSONObject.put(stop,"city","")
				 JSONObject.put(stop,"state","")
				 JSONObject.put(stop,"country","")
				 JSONObject.put(stop,"zip","")
				 setCurrentLocationOfTheater(stop)				
			 endif
		endfunc
			
		func handleGpsErrorOfTheater(String callbackId)
			System.showGeneralMsg(NULL,"<%=msg.get("selectaddress.gps.error")%>","<%=msg.get("common.button.OK")%>",NULL,NULL,"CallBack_GPS_ErrorOfTheater")
			return FAIL		
		endfunc	

   		func CallBack_GPS_ErrorOfTheater(int param)
   		
		endfunc		
	]]>
	</tml:script>
	
	<tml:menuItem name="commuteAlert" pageURL="<%=cAlertURL%>"/>
	<tml:menuItem name="movieMenu" pageURL="<%=movieURL%>" />
	<tml:menuItem name="home" pageURL="<%=host + "/startUp.do?pageRegion=" + region%>"/>
	<tml:menuItem name="instructionMenu" onClick="onClickInstruction" pageURL="<%=getPage + "SpeakCommandInstruction"%>" text="<%=msg.get("dsr.cc.button")%>" trigger="KEY_MENU|TRACKBALL_CLICK"/>
	<tml:menuItem name="typeOnVolume" onClick="onClickTypeOnVolume" trigger="TRACKBALL_CLICK"/>
	<tml:actionItem name="getGPSOfTheater" action="getGPS"
		progressBarText="<%=msg.get("common.readinggps") %>">
		<tml:input name="locParam"></tml:input>
		<tml:output name="currentLocation" />
	</tml:actionItem>
	<tml:menuItem actionRef="getGPSOfTheater" name="doGetGpsOfTheater"
		onClick="returnCurrentLocationOfTheater">
	</tml:menuItem>
				
	<tml:page id="SpeakCommand" url="<%=getDsrPage + "SpeakCommand1"%>" type="<%=pageType%>" background="" 
		 genericMenu="4" helpMsg="$//$saycommand" groupId="<%=GROUP_ID_DSR%>" supportback="false" >
		<%
			handler.toXML(out);
		%>	
		<tml:image	id="shadowBg1" url="" align="left|top"/>
		<tml:image	id="controlBg1" url="" align="left|top"/>
		
		<tml:label id="label1" align="center" fontWeight="bold">
			<%=msg.get("dsr.cc.prompt")%>
		</tml:label>
		<tml:label id="label2" fontWeight="system_large" align="center" textWrap="wrap">
			<%=PoiUtil.amend(msg.get("dsr.example") + msg.get("dsr.cc.example1"))%>	
		</tml:label>
		<tml:label id="label3" fontWeight="system_large" align="center" textWrap="wrap">
			<%=PoiUtil.amend(msg.get("dsr.cc.example2"))%>
		</tml:label>
		<tml:urlLabel id="label4" fontWeight="system_median"  fontColor="blue">			
			<%=msg.get("dsr.cc.button2")%>
			<tml:menuRef name="instructionMenu" />
		</tml:urlLabel>
		<tml:audioVolume id="imageSpeaker" align="center"/>

		<tml:button id="typeInButton" text="<%=msg.get("dsr.finish")%>"
			fontWeight="system_median" isFocusable="true" visible="1"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="typeOnVolume" />
		</tml:button>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>