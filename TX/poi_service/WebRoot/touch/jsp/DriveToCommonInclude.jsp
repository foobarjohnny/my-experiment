<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page	import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.stat.*"%>


<%@ include file="./Header.jsp"%>

<%@ include file="poi/controller/SearchPoiController.jsp"%>
<%@ include file="misc/controller/SendUpdateController.jsp"%>
<%@ include file="ac/controller/AddressCaptureController.jsp"%>
<%@ include file="StopUtil.jsp"%>
<%@ include file="StatLoggerCommon.jsp"%>
<%@ include file="/touch/jsp/model/PrefModel.jsp"%>
<%@ include file="/touch/jsp/model/DriveToModel.jsp"%>
<%@ include file="/touch/jsp/dsr/controller/DSRController.jsp"%>
<jsp:include page="/touch/jsp/ac/controller/SelectAddressController.jsp" />
<jsp:include page="/touch/jsp/local_service/controller/MapWrapController.jsp" />
<jsp:include
		page="/touch/jsp/ac/controller/EditRouteController.jsp"></jsp:include>
<%
    String callBackUrl = getPageCallBack + "DriveToWrap";
%>
<tml:script language="fscript" version="1">
	<![CDATA[
        <%@ include file="GetServerDriven.jsp"%>
        func onShow()
			if DriveTo_M_isDoingNav() == 1
        		System.doAction("backToDriveToMenu")
        		return FAIL
        	elsif DriveTo_M_isDoingNav() == 2
        		TxNode addressNode
        		TxNode audioNode
        		MenuItem.setBean("doRestoreOrignOrDest", "inputName", addressNode)
        		MenuItem.setBean("doRestoreOrignOrDest", "audioInfo", audioNode)
        		System.doAction("doRestoreOrignOrDest")
        		logResumeTrip()
        		return FAIL
        	elsif DriveTo_M_isDoingNav() == 3
        	
        	elsif DriveTo_M_isDoingNav() == 4
        	    System.back()
        	    return FAIL
			else
				JSONObject jo = DriveTo_M_getAddress()
       		    if NULL != JSONObject.get(jo,"poiOrStop") && "poi" == JSONObject.get(jo,"poiOrStop")
				   	DriveTo_M_saveStopType(<%=Constant.STOP_POI%>)
       		    endif				
				
				String navType = DriveTo_M_getNavType()
				DriveTo_M_saveNavType("")
				if 0 == ServerDriven_CanDynamicNav()
				    DriveTo_M_setDoingNav(4)
				    EditRoute_C_StaticRoute(jo)
				elsif DriveTo_M_isFromDSR()
					DriveToControl_driveToActionFromDSR(jo,navType)
					return FAIL 
				else
					DriveToControl_driveToActionViaJSON(jo,navType)
					return FAIL
				endif
			endif
		endfunc
		
        func DriveToControl_driveToAction(TxNode poiNode)
        	DriveTo_M_setDoingNav(1)
        	TxNode audioNode
			MenuItem.setBean("driveToMenu", "inputName", poiNode)
			MenuItem.setBean("driveToMenu", "audioInfo", audioNode)
			System.doAction("driveToMenu")
			return FAIL   
        endfunc

        func DriveToControl_driveToActionFromDSR(JSONObject jo,String navType)
        	DriveTo_M_setDoingNav(1)
        	int stopType = DriveTo_M_getStopType()
      		TxNode address = convertToAddress(jo, stopType)
      		TxNode node
      		TxNode.addValue(node,1)
      		TxNode.addMsg(node,navType)
      		TxNode.addChild(node,address)
			MenuItem.setBean("driveToMenu", "inputName", node)
			MenuItem.setBean("driveToMenu", "audioInfo", DriveTo_M_getAudio())
			System.doAction("driveToMenu")
			logDriveToRequest(jo, stopType, 1)
			return FAIL   
        endfunc
        
        func DriveToControl_driveToActionViaJSON(JSONObject jo,String navType)
        	DriveTo_M_setDoingNav(0)
      		int stopType = DriveTo_M_getStopType()
            TxNode stopNode = convertToAddress(jo, stopType)
            TxNode node
            TxNode.addMsg(node,navType)
            if JSONObject.has(jo,"isResumeTrip") && "true" == JSONObject.get(jo,"isResumeTrip")
                if NULL != ShareData.Get("LAST_TRIP_ROUTE_SET")
					TxNode resumeStyleNode = ShareData.Get("LAST_TRIP_ROUTE_SET")
					TxNode.addChild(stopNode, resumeStyleNode)
                endif
            endif
            TxNode.addChild(node,stopNode)
            DriveToControl_driveToAction(node)   
            logDriveToRequest(jo, stopType, 0)
        endfunc

        
        func logDriveToRequest(JSONObject dest, int stopType, int isDSR)
        	if StatLogger.isStatEnabled(<%=EventTypes.ROUTE_REQUEST%>)
				JSONObject params
				StatLogger_CM_addAddressDetails(stopType, params)
				if JSONObject.has(dest, "isResumeTrip")
					JSONObject.put(params, "<%=AttributeID.ADDRESS_SOURCE%>", <%=AttributeValues.AS_RESUME_TRIP%>)
				endif
				# address input 1 speak in, 2 type in, 3 search
				if isDSR == 1
					JSONObject.put(params, "<%=AttributeID.ADDRESS_INPUT%>", "1")
				else
					JSONObject.put(params, "<%=AttributeID.ADDRESS_INPUT%>", "2")
				endif
				StatLogger.logEvent(<%=EventTypes.ROUTE_REQUEST%>, params)
        	endif
        endfunc
        
        func logResumeTrip()
        	if StatLogger.isStatEnabled(<%=EventTypes.ROUTE_REQUEST%>)
				JSONObject params
				JSONObject.put(params, "<%=AttributeID.ADDRESS_SOURCE%>", <%=AttributeValues.AS_RESUME_TRIP%>)
				JSONObject.put(params, "<%=AttributeID.ADDRESS_TYPE%>", <%=AttributeValues.AT_ADDRESS%>)
				JSONObject.put(params, "<%=AttributeID.ADDRESS_INPUT%>", "2")
				StatLogger.logEvent(<%=EventTypes.ROUTE_REQUEST%>, params)
			endif
        endfunc
        
        func navigationServiceReturned()	
            TxNode nextAction
			nextAction = ParameterSet.getParam("nextAction")
			DriveTo_M_setDoingNav(1)
			if NULL == nextAction
			    BackFromNavigation()
			    return FAIL
			else
                String nextActionStr = TxNode.msgAt(nextAction,0)
                
                if "" == nextActionStr
                   BackFromNavigation()
                   return FAIL
                elsif "Search Along"  ==  nextActionStr
				   #TxNode searchInformationNode = TxNode.childAt(nextAction,0)
				   if isSearchSpeakInput()
				   		invokeSpeakSearchAlong(nextAction)				   		
				   else
				   		SearchPoi_C_searchAlongInitial(nextAction)
				   endif
				   return FAIL
                elseif "ExitNav" == nextActionStr
                	BackFromNavigation()
                	return FAIL
                elseif "SendETA" == nextActionStr
                	TxNode nodeInfo = TxNode.childAt(nextAction,0)
                	TxNode nodeAddress = TxNode.childAt(nextAction,1)
               		JSONObject joAddress = convertStopToJSON(nodeAddress)
               	    JSONObject jo
		        	JSONObject.put(jo,"username",TxNode.msgAt(nodeInfo,0))
					JSONObject.put(jo,"time",TxNode.msgAt(nodeInfo,1))
					JSONObject.put(jo,"location",joAddress)
					SendUpdate_C_show(jo)
					return FAIL
				elsif "Select Destination Address" == nextActionStr
					DriveTo_M_setDoingNav(2)
			        JSONObject jo
	        	    JSONObject.put(jo,"title","<%=msg.get("selectaddress.title.driveto")%>")
	        	    JSONObject.put(jo,"mask","00111111111")
	        	    JSONObject.put(jo,"from","EditRoute")
	        	    JSONObject.put(jo,"returnAsIs","1")
	        	    JSONObject.put(jo,"callbackfunction","backFromSelectAddress")
				    JSONObject.put(jo,"callbackpageurl","<%=callBackUrl%>")
				    SelectAddress_C_SelectAddress(jo)
				    return FAIL
				elsif "Select Origin Address" == nextActionStr
					DriveTo_M_setDoingNav(2)
			        JSONObject jo
	        	    JSONObject.put(jo,"title","<%=msg.get("selectaddress.title.driveFrom")%>")
	        	    JSONObject.put(jo,"mask","01111111111")
	        	    JSONObject.put(jo,"from","EditRoute")
	        	    JSONObject.put(jo,"returnAsIs","1")
	        	    JSONObject.put(jo,"callbackfunction","backFromSelectAddress")
				    JSONObject.put(jo,"callbackpageurl","<%=callBackUrl%>")
				    
				    SelectAddress_C_SelectAddress(jo)
				    return FAIL
				elseif "Back" == nextActionStr
					 doBack()
					 return FAIL
				elseif "BackToAc" == nextActionStr
					 BackToAcAction()
					 return FAIL	 
				else
				   	 BackFromNavigation()
				   	 return FAIL	    
                endif
			endif
        endfunc

		func BackToAcAction()
			TxNode newAction
			TxNode.addMsg(newAction,"goToSelectAddress")
			Cache.saveToTempCache("nextAction",newAction)
			
			System.doAction("thirdParty")
        	return FAIL
		endfunc
			        
        func BackFromNavigation()
            String backActionForNav = DriveTo_M_getBackAction()
            String backActionForSearchPoi = SearchPoi_C_getBackAction()
            String backActionForMovie = DriveTo_M_getBackActionForMovie()
            String backActionForMap = MapWrap_C_getBackAction()
            String backActionForEditRoute = EditRoute_C_getBackAction()
            String exitString = "<%=Constant.BACK_ACTION_EXIT_APP%>"
            if "" != backActionForNav || "" != backActionForSearchPoi || "" != backActionForMovie || "" != backActionForMap || "" != backActionForEditRoute
               if exitString == backActionForNav || exitString == backActionForSearchPoi || exitString == backActionForMovie || exitString == backActionForMap || exitString == backActionForEditRoute
                  System.quit()
                  return FAIL
               else
                  MapWrap_C_deleteBackAction()
                  Movie_M_deleteBackAction()
                  SearchPoi_C_deleteBackAction()
                  EditRoute_C_deleteBackAction()
                  DriveTo_M_deleteBackAction()
                  System.doAction("home")
                  return FAIL
               endif
            else
               System.doAction("home")
               return FAIL
            endif
        endfunc
        
        func backFromSelectAddress()
        	DriveTo_M_setDoingNav(3)
            TxNode node = ParameterSet.getParam("returnAddress")
        	
        	TxNode addressNode
        	if NULL != node
        		JSONObject jo = JSONObject.fromString(TxNode.msgAt(node,0))
        		int type = JSONObject.getInt(jo,"type")
        		
        		if type == 6
        			TxNode.addMsg(addressNode,"current location")
        		else
        		    int stopType = DriveTo_M_getStopType()
        		    TxNode address = convertToAddress(jo, stopType)
        		    TxNode.addMsg(addressNode,"location")
        		    TxNode.addChild(addressNode,address)
        		endif
        	endif
        	
        	TxNode audioNode
        	MenuItem.setBean("doRestoreOrignOrDest", "inputName", addressNode)
        	MenuItem.setBean("doRestoreOrignOrDest", "audioInfo", audioNode)
        	logResumeTrip()
        	System.doAction("doRestoreOrignOrDest")
        	return FAIL
        endfunc
        
        func isSearchSpeakInput()
 			return FALSE	       	
        endfunc 
               
        func onResume()
        	System.doAction("backToDriveToMenu")
        	return FAIL
        endfunc       
	]]>
</tml:script>

<tml:actionItem name="restoreOrignOrDest" action="RestoreOrignOrDest">
	<tml:output name="nextAction" />
	<tml:input name="inputName" />
	<tml:input name="audioInfo" />
</tml:actionItem>
<tml:menuItem name="doRestoreOrignOrDest" actionRef="restoreOrignOrDest"
	onClick="navigationServiceReturned">
</tml:menuItem>

<tml:actionItem name="driveTo"
	action="<%=Constant.LOCALSERVICE_DRIVETO%>">
	<tml:output name="nextAction" />
	<tml:input name="inputName" />
	<tml:input name="audioInfo" />
</tml:actionItem>
<tml:menuItem name="driveToMenu" actionRef="driveTo"
	onClick="navigationServiceReturned"
	trigger="KEY_RIGHT | TRACKBALL_CLICK" />

<tml:actionItem name="backToDriveTo"
	action="RestoreNav">
	<tml:output name="nextAction" />
</tml:actionItem>
<tml:menuItem name="backToDriveToMenu" actionRef="backToDriveTo"
	onClick="navigationServiceReturned"
	trigger="KEY_RIGHT | TRACKBALL_CLICK" />

<tml:actionItem name="doDriveTo" action="RestoreNav">
	<tml:output name="nextAction" />
</tml:actionItem>
<tml:menuItem name="goToRestoreNav" actionRef="doDriveTo"
	onClick="navigationServiceReturned"
	trigger="KEY_RIGHT | TRACKBALL_CLICK" />

<tml:menuItem name="home" pageURL="<%=host + "/startUp.do?pageRegion=" + region%>">
</tml:menuItem>

<tml:menuItem name="thirdParty" pageURL="<%=getPage+"ThirdPartAction"%>">
</tml:menuItem>
