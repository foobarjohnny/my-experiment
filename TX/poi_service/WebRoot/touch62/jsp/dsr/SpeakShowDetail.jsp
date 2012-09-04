<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<%@ include file="model/DSRModel.jsp"%>
<%@include file="/touch62/jsp/ac/model/SetUpHomeModel.jsp"%>
	<tml:script language="fscript" version="1" feature="<%=FeatureConstant.DSR%>">
		<![CDATA[
		func DSR_showDetail(int i)
			releaseResource()
			DSRCommon_M_saveSelectedIndex(i)
			string choosedText = getOneDataText(i)
			DSRCommon_M_saveSelectedText(choosedText)
			
			string logText = "<%=AudioConstants.LOG_TYPE_DSR%>" + "|" + DSRCommon_M_getTrxnId() + "|" + i + "|" + choosedText + "|" + 0
			if DSRCommon_M_getTrxnId() != (-1)
				System.log(logText)
			endif
			string dsrType = DSRCommon_M_getDSRType()
			
			if dsrType == "<%=AudioConstants.DSR_RECOGNIZE_COMMAND_CONTROL%>"
				showCommandDetail(i)
			else
				TxNode stop = getDataStop(i)
				if isInvalidAddress(stop)
					handleInvalidAddress()
				else
					JSONObject jo = convertStopToJSON(stop)
					if JSONObject.has(jo,"label")
						JSONObject.put(jo,"label","")
					endif
					DSRCommon_M_saveSelectedAddress(jo)
								
					if dsrType == "<%=AudioConstants.DSR_RECOGNIZE_POI%>" || dsrType == "<%=AudioConstants.DSR_RECOGNIZE_SEARCHALONG%>"
						if DSR_M_isSearchAlong()
							System.doAction("SpeakSearchAlong2")
							return FAIL
						else
		        			System.doAction("gettingPOI")
		        			return FAIL
		        		endif				
					else
					
						#RecentPlace_saveAddress(jo)
						
			        	TxNode node
			        	TxNode.addMsg(node,JSONObject.toString(jo))
			        	
			        	JSONObject joParameter = DSR_M_getParameter() 		
						string callbackurl = JSONObject.getString(joParameter,"callbackpageurl")
						string fromPage = JSONObject.getString(joParameter,"from")
						if fromPage == NULL
							fromPage = ""
						endif
						if callbackurl == "<%=getPageCallBack + "DriveToWrap"%>" && fromPage == "DriveTo"
							callbackurl = "<%=getPageCallBack + "SpeakGettingRoute"%>"
						elseif	callbackurl == "<%=getPageCallBack + "MapWrap"%>"
							callbackurl = "<%=getPageCallBack + "SpeakGettingMap"%>"
						endif
						
						MenuItem.setAttribute("callback","url",callbackurl)
						TxNode node1
			        	TxNode.addMsg(node1,JSONObject.getString(joParameter,"callbackfunction")) 
			        	MenuItem.setBean("callback", "callFunction", node1)
			        	MenuItem.setBean("callback", "returnAddress", node)
			        	System.doAction("callback")
			        	return FAIL
					endif
				endif
			endif
		endfunc
        
		func showCommandDetail(int i)
			int dataTextValue = getDataTextValue(i)
			DSRCommon_M_saveSelectedTextValue(dataTextValue)
			
			int needPartialRecognize = 0
			int invalidAddress = FALSE
			JSONObject jo
			if dataTextValue == <%=AudioConstants.DSR_COMMAND_VALUE_HOME%>
				jo = SetUpHome_M_getHome()
				if !SetUpHome_M_isHome()
					needPartialRecognize = 1
				endif
			elsif dataTextValue == <%=AudioConstants.DSR_COMMAND_VALUE_OFFICE%>
				jo = SetUpHome_M_getHome()
				if SetUpHome_M_isHome()
					needPartialRecognize = 1
				endif
			else
				TxNode stop = getDataStop(i)
				if isInvalidAddress(stop)
					invalidAddress = TRUE
					handleInvalidAddress()
				else
					jo = convertStopToJSON(stop)
				endif			
			endif
			
			if !invalidAddress
				if jo == NULL || needPartialRecognize
					checkPartialRecognition()
				else
					showScreen(jo,dataTextValue)	
				endif
			endif
		endfunc
		
		func showScreen(JSONObject jo,int dataTextValue)
        	DSRCommon_M_saveSelectedAddress(jo)
			TxNode node
        	TxNode.addMsg(node,JSONObject.toString(jo))
        	
        	TxNode node1
	        TxNode.addMsg(node1,"CallBack_SelectAddress")
	        	
        	JSONObject joParameter = DSR_M_getParameter() 
	        string text = DSRCommon_M_getCommand()
        	if text == "<%=AudioConstants.DSR_COMMAND_TEXT_DRIVETO%>"
        	
        		RecentPlace_saveAddress(jo)
        		
	        	MenuItem.setAttribute("callback","url","<%=getPageCallBack + "SpeakGettingRoute"%>")
	        	MenuItem.setBean("callback", "callFunction", node1)
	        	MenuItem.setBean("callback", "returnAddress", node)
	        	System.doAction("callback")
	        	return FAIL
			endif

			if text == "<%=AudioConstants.DSR_COMMAND_TEXT_MAPIT%>"
				
				#RecentPlace_saveAddress(jo)
				
				if dataTextValue == <%=AudioConstants.DSR_COMMAND_VALUE_POI%>
					System.doAction("gettingPOI")
					return FAIL
				else
		        	System.doAction("gettingMap")
        			return FAIL
	        	endif
			endif 
			
			if text == "<%=AudioConstants.DSR_COMMAND_TEXT_SEARCH%>"
        		System.doAction("gettingPOI")
        		return FAIL
			endif		
		endfunc
		
		func isInvalidAddress(TxNode node)
			return FALSE
		endfunc
		
		func handleInvalidAddress()
			System.showGeneralMsg(NULL,"<%=msg.get("selectaddress.gps.error")%>","OK",NULL,NULL,"Callback_PopopNoGPS")	
		endfunc

        func Callback_PopopNoGPS(int param)
        	if param == 1
        		System.back()
        		return FAIL
        	endif
        endfunc
		]]>
	</tml:script>
	
<tml:block feature="<%=FeatureConstant.DSR%>">	
	<tml:menuItem name="callback" pageURL="" trigger="KEY_RIGHT | TRACKBALL_CLICK">
			<tml:bean name="callFunction" valueType="string" value=""/>
	</tml:menuItem>
	<tml:menuItem name="gettingMap" pageURL="<%=getPage + "SpeakGettingMap"%>"/>
	<tml:menuItem name="gettingPOI" pageURL="<%=getPage + "SpeakGettingPOI"%>"/>
	<tml:menuItem name="SpeakSearchAlong2" pageURL="<%=getDsrPage + "SpeakSearchAlong2"%>"/>
</tml:block>	
