<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.j2me.datatypes.TxNode"%>
<%@page import="com.telenav.tnbrowser.util.Utility"%>
<%@page import="com.telenav.cserver.util.CommonUtil"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%
    String mainPageURL = host + "/startUp.do?pageRegion=" + region;
    String dCountry = CommonUtil.getDefaultCountry(region);
    String searchNearby = "searchNearby";
    String weather = "weather";
    String commuteAlert = "commuteAlert";
    String movies = "movies";
    String directions = "GetDirections";
%>
<tml:TML outputMode="TxNode" showUrl="<%=getPage + "ThirdPartAction"%>">
	<jsp:include page="/touch/jsp/common/movie/controller/MovieController.jsp" />
	<jsp:include page="/touch/jsp/common/login/controller/LoginStartupController.jsp" />
	<jsp:include page="/touch/jsp/common/commute/controller/CommuteAlertController.jsp" />
	<%@ include file="../StopUtil.jsp"%>
	<%@ include file="../ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="../ac/controller/ShareAddressController.jsp"%>
	<%@ include file="../weather/controller/WeatherController.jsp"%>
	<jsp:include page="/touch/jsp/controller/DriveToController.jsp"/>
	<jsp:include page="/touch/jsp/ac/controller/SelectAddressController.jsp" />
	<jsp:include
		page="/touch/jsp/local_service/controller/MapWrapController.jsp"></jsp:include>
	<jsp:include
		page="/touch/jsp/ac/controller/EditRouteController.jsp"></jsp:include>
	<%@ include file="/touch/jsp/poi/controller/SearchPoiController.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
			func callbackFunc()
				 TxNode indexNode = ParameterSet.getParam("indexNode")
			     TxNode stopsNode = Cache.getFromTempCache("thirdPartyStopNodes")
				 if stopsNode!=NULL
					int index = TxNode.valueAt(indexNode,0)
					TxNode stopNode=TxNode.childAt(stopsNode,index)
					Cache.saveToTempCache("thirdPartyStopNode",stopNode)
				 endif
			     doAction()
			     return FAIL
			endfunc
			
			func callbackFunc2(int slectedIndex)
				TxNode actionNode
				if slectedIndex == 11
					TxNode.addMsg(actionNode,"driveTo")
				elsif slectedIndex == 12
					TxNode.addMsg(actionNode,"mapIt")
				elsif slectedIndex == 13
					TxNode.addMsg(actionNode,"searchNearby")
				elsif slectedIndex == 3
					Cache.deleteCookie("thirdPart")
					Cache.deleteFromTempCache("nextAction")
					Cache.deleteFromTempCache("ThirdPartyStopsNode")
					System.exit()
				endif
				if slectedIndex !=3
					Cache.saveToTempCache("currentActionNode",actionNode)
					
					
					# Check whether user has login before.
		        	int login = 0
		        	login = LoginStartup_C_hasLogin()
		        	
		        	#login =1
				
					# Login flow.
					if login == 1
						validateAddress()
					else
						LoginStartup_C_start("<%=getPageCallBack + "ThirdPartAction"%>","callbackFromLogin")
					endif
				endif
			endfunc
			
			func doAction()			
				# Check whether user has login before.
	        	int login = 0
	        	login = LoginStartup_C_hasLogin()
	        	
	        	#login =1
				
				
				# Login flow.
				if login == 1
					validateAddress()
				else
					LoginStartup_C_start("<%=getPageCallBack + "ThirdPartAction"%>","callbackFromLogin")
				endif
			endfunc
			
			func getAddressFromAc(int slectedIndex)
				initAC()
				
				System.doAction("goTypeAddress")
			endfunc
			
			func initAC()
				AddressCapture_M_init("<%=getPageCallBack + "ThirdPartAction"%>","callbackFromAC","ThirdPartAction")
			endfunc
			
			func getLocationByGPS(int lat, int lon)
			    JSONObject jo
			    JSONObject.put(jo,"lat",lat)
			    JSONObject.put(jo,"lon",lon)
			    
			    String joStr=JSONObject.toString(jo)
			    TxNode node
		        TxNode.addMsg(node,joStr)
		        TxRequest req
				String url="<%=host + "/getCurrentLocation.do"%>"
				String scriptName="getCurrentLocationCallback"
				TxRequest.open(req,url)
				TxRequest.setRequestData(req,node)
				TxRequest.onStateChange(req,scriptName)
				TxRequest.setProgressTitle(req,"<%=msg.get("rgc.loading")%>")
				TxRequest.send(req)
			endfunc
			
			func getCurrentLocationCallback(TxNode node,int status)
	            if status == 0
	              JSONArray buttonStrings = JSONArray.fromString("[OK]")
				  JSONArray callbackParams = JSONArray.fromString("[0]")			
				  System.showGeneralMsgEx( NULL, "<%=msg.get("thirdpart.invalidaddress")%>", buttonStrings, callbackParams, 0 , "getAddressFromAc" )
				elseif status == 1
				  int stringSize = TxNode.getStringSize(node)
				  if stringSize <= 1
				     JSONArray buttonStrings = JSONArray.fromString("[OK]")
					 JSONArray callbackParams = JSONArray.fromString("[0]")			
					 System.showGeneralMsgEx( NULL, "<%=msg.get("thirdpart.invalidaddress")%>", buttonStrings, callbackParams, 0 , "getAddressFromAc" )
					 return FAIL
				  endif
				  String joStr = TxNode.msgAt(node,1)
				  JSONObject jo = JSONObject.fromString(joStr)
				  TxNode actionNode=Cache.getFromTempCache("currentActionNode")
				  String actionName = TxNode.msgAt(actionNode,0)
				  if (1 == String.equalsIgnoreCase(actionName,"mapIt"))
					  showMap(jo)
				  elsif (1 == String.equalsIgnoreCase(actionName,"searchNearby"))
					  searchNearBy(jo)
				  elsif (1 == String.equalsIgnoreCase(actionName,"driveTo"))
					  driveTo(jo)
				  elsif (1 == String.equalsIgnoreCase(actionName,"shareAddress"))
					  shareAddress(jo)
			      elsif (1 == String.equalsIgnoreCase(actionName,"GetDirections"))
					  getDirections(jo)
				  endif
				endif
				return FAIL
	        endfunc
			
			func validateAddress()
			#	TxNode stopNode = ParameterSet.getParam("stopNode")
				TxNode stopNode=Cache.getFromTempCache("thirdPartyStopNode")
				JSONObject jo
				String s
				if stopNode != NULL
					TxNode originNode = TxNode.childAt(stopNode,0)
					TxNode haveOriginNode
					if NULL != originNode
					   Cache.saveToTempCache("thirdPartyStopNode",originNode)
					   TxNode.addValue(haveOriginNode,1)
					   Cache.saveToTempCache("haveOriginNode",haveOriginNode)
					endif
					
					
					String firstLine = checkNULL(TxNode.msgAt(stopNode,1))
					String lastLine =checkNULL(TxNode.msgAt(stopNode,2)) + "," +checkNULL(TxNode.msgAt(stopNode,3)) +" " +checkNULL(TxNode.msgAt(stopNode,5))
					if "" == checkNULL(TxNode.msgAt(stopNode,2)) && "" == checkNULL(TxNode.msgAt(stopNode,3)) && "" == checkNULL(TxNode.msgAt(stopNode,5))
							lastLine = ""
					endif
					
					#add by ChengBiao: if address is null, get location with lat&lon
					if "" ==  firstLine && "" == lastLine
					   int lat = TxNode.valueAt(stopNode,1)
					   int lon = TxNode.valueAt(stopNode,2)
					   if NULL != lat && NULL != lon
					      getLocationByGPS(lat, lon)
					      return FAIL
					   endif
					endif
					
					JSONObject.put(jo,"firstLine",firstLine)
				    JSONObject.put(jo,"lastLine",lastLine)
				    String countryStr = checkNULL(TxNode.msgAt(stopNode,6))
				    #if "" == countryStr
				    #	countryStr = "<%=dCountry%>"
				    #endif
				    
					JSONObject.put(jo,"country",countryStr)
					s = JSONObject.toString(jo)
					#Cache.saveToTempCache("<%=Constant.StorageKey.ADDRESS_CAPTURE_DEFAULT_ADDRESS%>",jo)
				endif
				TxNode node
				TxNode.addMsg(node,s)
				TxRequest req
				string url= "<%=host + "/ValidateAddress.do"%>"
				string scriptName="stateChange"
				TxRequest.open(req,url)
				TxRequest.setRequestData(req,node)
				TxRequest.onStateChange(req,"stateChange")
				TxRequest.setProgressTitle(req,"<%=msg.get("ac.validating.address")%>", "", TRUE)
				TxRequest.send(req)
				Cache.deleteCookie("thirdPart")
				Cache.deleteFromTempCache("ThirdPartyStopsNode")
			endfunc
			
			func stateChange(TxNode node,int status)
				if status ==1
					JSONArray ja
					if node != NULL
						int size = TxNode.getValueSize(node)
						if 0 < size
							int ok = TxNode.valueAt(node,0)
							if ok == 1
								String strJa = TxNode.msgAt(node,0)
								if strJa != NULL
									ja = JSONArray.fromString(strJa)
									int addressSize = JSONArray.length(ja)
									if 0 < addressSize
										if addressSize == 1
											JSONObject jo = JSONArray.get(ja,0)
											if jo!= NULL
											    int isStreetChanged = JSONObject.get(jo,"isStreetChanged")
												int isCityChanged = JSONObject.get(jo,"isCityChanged")
												if 1 == isStreetChanged || 1 == isCityChanged
												   goToAddressList(node)
												   return FAIL
												endif
												TxNode actionNode=Cache.getFromTempCache("currentActionNode")
												String actionName = TxNode.msgAt(actionNode,0)
												if (1 == String.equalsIgnoreCase(actionName,"mapIt"))
													showMap(jo)
												elsif (1 == String.equalsIgnoreCase(actionName,"searchNearby"))
													searchNearBy(jo)
												elsif (1 == String.equalsIgnoreCase(actionName,"driveTo"))
													driveTo(jo)
												elsif (1 == String.equalsIgnoreCase(actionName,"shareAddress"))
													shareAddress(jo)
											    elsif (1 == String.equalsIgnoreCase(actionName,"GetDirections"))
													getDirections(jo)
												endif
											endif
											
											TxNode haveOriginNode = Cache.getFromTempCache("haveOriginNode")
											if NULL != haveOriginNode
											   int haveOrigin = TxNode.valueAt(haveOriginNode,0)
											   if 2 == haveOrigin
											      return FAIL
											   endif
											endif
											Cache.deleteFromTempCache("currentActionNode")
											return FAIL
										else
											goToAddressList(node)
											return FAIL
										endif
									endif
								endif
							endif
						endif
					endif
					JSONArray buttonStrings = JSONArray.fromString("[OK]")
					JSONArray callbackParams = JSONArray.fromString("[0]")
								
					System.showGeneralMsgEx( NULL, "<%=msg.get("thirdpart.invalidaddress")%>", buttonStrings, callbackParams, 0 , "getAddressFromAc" )
				elsif status ==0
				#	System.showErrorMsg("Ajax error")
					JSONArray buttonStrings = JSONArray.fromString("[OK]")
					JSONArray callbackParams = JSONArray.fromString("[0]")			
					System.showGeneralMsgEx( NULL, "<%=msg.get("thirdpart.invalidaddress")%>", buttonStrings, callbackParams, 0 , "getAddressFromAc" )
				endif				
			endfunc
			
			func goToAddressList(TxNode node)
			    TxNode haveOriginNode = Cache.getFromTempCache("haveOriginNode")
			    if NULL != haveOriginNode
			       AddressCapture_C_saveAddressListTitleForMaiTai(haveOriginNode)
			    else
			       AddressCapture_C_deleteAddressListTitleForMaiTai()
			    endif
			    
			    initAC()
				MenuItem.setBean("showAddress","addressList",node)											
				string pageUrl = "<%=getPageCallBack%>" + "AddressList#" + "ThirdPartAction"
				MenuItem.setAttribute("showAddress","url",pageUrl)
				System.doAction("showAddress")
				return FAIL
			endfunc
			
			func onShow()
				TxNode callBackFromAddressNode = Cache.getFromTempCache("callBackFromAddressNode")
				if NULL != callBackFromAddressNode
				   Cache.deleteFromTempCache("callBackFromAddressNode")
				   return FAIL
				endif
				
				TxNode nextAction=Cache.getFromTempCache("nextAction")
				TxNode backActionNode = ParameterSet.getParam("backAction")
				if NULL != backActionNode
				   Cache.saveToTempCache("backActionNode",backActionNode)
				endif
				
				if nextAction == NULL
					TxNode newAction
					TxNode.addMsg(newAction,"goToMain")
					Cache.saveToTempCache("nextAction",newAction)
					
					#save current action
					TxNode actionNode=ParameterSet.getParam("currentAction")
					if NULL != actionNode
					   Cache.saveToTempCache("currentActionNode",actionNode)
					else
					   actionNode = Cache.getFromTempCache("currentActionNode")
					endif
					
					#Begin
					TxNode node=ParameterSet.getParam("thirdPart")
					int stopCount = 0 
					if NULL != node
					    stopCount = TxNode.getChildSize(node)
					    if NULL != TxNode.msgAt(node,0)
					       String label = TxNode.msgAt(node,0)
					       TxNode labelNode
					       TxNode.addMsg(labelNode,label)
					       Cache.saveToTempCache("labelNode",labelNode)
					    endif
					endif
					
					if stopCount==1
					#	String label=TxNode.msgAt(node,0)											
					
					#	JSONArray bs = JSONArray.fromString("["+label+",cancel]")
					#	JSONArray cp = JSONArray.fromString("[11,0]")
					#	System.showGeneralMsgEx(NULL,"test new box", bs, cp, 0, "callbackFunc")						
					#	JSONArray bs = JSONArray.fromString("[Drive To,Map It,Search Nearby,cancel]")
					#	JSONArray cp = JSONArray.fromString("[11,12,13,3]")
					#	System.showGeneralMsgEx(NULL,"", bs, cp, 0, "callbackFunc2")
						
						TxNode stopNode=TxNode.childAt(node,0)
						Cache.saveToTempCache("thirdPartyStopNode",stopNode)
						
						#save the label for MaiTai
						if NULL != TxNode.msgAt(stopNode,0)
					       String labelOfStop = TxNode.msgAt(stopNode,0)
					       TxNode labelOfStopNode
					       TxNode.addMsg(labelOfStopNode,labelOfStop)
					       Cache.saveToTempCache("labelOfStopNode",labelOfStopNode)
					    endif
					    
						doAction()
						return FAIL
					elsif stopCount>1
						Cache.saveToTempCache("thirdPartyStopNodes",node)
						System.doAction("goToAddressList")
						return FAIL
					#stopCount=0 means from start up icons
				    else
				        String targetAction = TxNode.msgAt(actionNode,0)
						int login = 0
			        	login = LoginStartup_C_hasLogin()
						# Login flow.
						if login == 1
						 	goToTargetAction(targetAction)
						else
							LoginStartup_C_start("<%=getPageCallBack + "ThirdPartAction"%>","callbackFromLoginForTarget")
						endif
					endif
					#end
				else
					String actionName = TxNode.msgAt(nextAction,0)

					if(1 == String.equalsIgnoreCase(actionName,"goToMain"))
						
						int login = 0
	        			login = LoginStartup_C_hasLogin()
	        			if 1 != login
	        				System.exit()
	        			endif
						System.doAction("goToMain")
					elsif(1 == String.equalsIgnoreCase(actionName,"toDoAction"))
						TxNode node = Cache.getFromTempCache("validAddress")
						if NULL != node
							toDoAction(node)
						endif
						Cache.deleteFromTempCache("validAddress")
						Cache.deleteFromTempCache("nextAction")
						TxNode newAction
						TxNode.addMsg(newAction,"goToMain")
						Cache.saveToTempCache("nextAction",newAction)
						
						TxNode haveOriginNode = Cache.getFromTempCache("haveOriginNode")
						if NULL != haveOriginNode
						   int haveOrigin = TxNode.valueAt(haveOriginNode,0)
						   if 2 == haveOrigin
						      return FAIL
						   endif
						endif
						Cache.deleteFromTempCache("currentActionNode")
					elseif(1 == String.equalsIgnoreCase(actionName,"toValidateAddress"))
						validateAddress()
						Cache.deleteFromTempCache("nextAction")
						TxNode newAction
						TxNode.addMsg(newAction,"goToMain")
						Cache.saveToTempCache("nextAction",newAction)
					elseif(1 == String.equalsIgnoreCase(actionName,"goToSelectAddress"))
						BackToAcAction()
						Cache.deleteFromTempCache("nextAction")
						TxNode newAction
						TxNode.addMsg(newAction,"goToMain")
						Cache.saveToTempCache("nextAction",newAction)
					else
				        String targetAction = TxNode.msgAt(actionNode,0)
						int login = 0
			        	login = LoginStartup_C_hasLogin()
						# Login flow.
						if login == 1
						 	goToTargetAction(targetAction)
						else
							LoginStartup_C_start("<%=getPageCallBack + "ThirdPartAction"%>","callbackFromLoginForTarget")
						endif
					endif					
				endif													
			endfunc

			func BackToAcAction()
				JSONObject jo
	        	JSONObject.put(jo,"title","<%=msg.get("selectaddress.title.driveto")%>")
	        	JSONObject.put(jo,"mask","10111111011")
	        	JSONObject.put(jo,"from","DriveTo")
	        	JSONObject.put(jo,"returnAsIs","1")
	        	JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
				JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack + "DriveToWrap"%>")
				SelectAddress_C_SelectAddress(jo)
			endfunc
					
			func onResume()
				int login = 0
	        	login = LoginStartup_C_hasLogin()
	        	if 1 != login
	        	   System.exit()
	        	endif
	        	
				initAC()
				System.doAction("goTypeAddress")
			endfunc
			
			func goToTargetAction(String targetAction)
			    String backAction = getBackAction()
			    if (1 == String.equalsIgnoreCase(targetAction,"<%=searchNearby%>"))
					SearchPoi_C_saveBackAction(backAction)
					SearchPoi_C_initial(5)
					SearchPoi_C_showSearch()
				elsif (1 == String.equalsIgnoreCase(targetAction,"<%=weather%>"))
					WeatherController_C_initForThirdPart(backAction)
				elsif (1 == String.equalsIgnoreCase(targetAction,"<%=commuteAlert%>"))
					CommuteAlert_C_initForThirdPart(backAction)
				elsif (1 == String.equalsIgnoreCase(targetAction,"<%=movies%>"))
					SearchMovie_C_initForThirdPart(backAction)
				endif
			endfunc
			
			func getBackAction()
			    String backAction = ""
			    TxNode  backActionNode = Cache.getFromTempCache("backActionNode")
			    if NULL != backActionNode
			       backAction = TxNode.msgAt(backActionNode,0)
			       Cache.deleteFromTempCache("backActionNode")
			    endif
			    return backAction
			endfunc
			
			func callbackFromLoginForTarget()
			    Cache.deleteFromTempCache("nextAction")
			endfunc
			
			func getLabelMsg()
			   String label = ""
		       TxNode labelNode = Cache.getFromTempCache("labelNode")
		       if NULL != labelNode
		          label = TxNode.msgAt(labelNode,0)
		          Cache.deleteFromTempCache("labelNode")
		       endif
		       return label
			endfunc

			func getLabelOfStopMsg()
			   String label = ""
		       TxNode labelNode = Cache.getFromTempCache("labelOfStopNode")
		       if NULL != labelNode
		          label = TxNode.msgAt(labelNode,0)
		          Cache.deleteFromTempCache("labelOfStopNode")
		       endif
		       return label
			endfunc
						
			func searchNearBy(JSONObject jo)				
				String backAction = getBackAction()
				if "" != backAction
				   SearchPoi_C_saveBackAction(backAction)
				endif
				String inputString = getLabelMsg()
				SearchPoi_C_searchNearLocation(jo,inputString)
			endfunc
			
			func showMap(JSONObject jo)
				String backAction = getBackAction()
				if "" != backAction
				   MapWrap_C_saveBackAction(backAction)
				endif
				MapWrap_C_showSingleAddress(jo)
			endfunc
			
			func driveTo(JSONObject jo)
				String backAction = getBackAction()
				if "" != backAction
				   DriveTo_C_saveBackAction(backAction)
				endif
				#DriveToWrap_C_driveTo(jo)
				string defaultLabel = getLabelOfStopMsg()
				if "" != defaultLabel
					JSONObject.put(jo,"label",defaultLabel)
				endif
				TxNode node
        		TxNode.addMsg(node,JSONObject.toString(jo))
				DriveTo_C_doNav(node,"","ThirdPart")
				return FAIL
			endfunc
			
			func shareAddress(JSONObject joAddress)
				JSONObject jo
        		JSONObject.put(jo,"callbackfunction","callBackFromSA")
				JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack + "ThirdPartAction"%>")
				
				JSONObject.put(jo,"address",joAddress)
				ShareAddress_C_show(jo)
				
			endfunc	
			
			func getDirections(JSONObject jo)
			    String backAction = getBackAction()
				if "" != backAction
				   EditRoute_C_saveBackAction(backAction)
				endif
			    
			    TxNode haveOriginNode = Cache.getFromTempCache("haveOriginNode")
			    if NULL != haveOriginNode
			       int haveOrigin = TxNode.valueAt(haveOriginNode,0)
			       if 1 == haveOrigin
			          Cache.saveToTempCache("destJSONObject", jo)
			          
			          TxNode newHaveOriginNode
			          TxNode.addValue(newHaveOriginNode,2)
					  Cache.saveToTempCache("haveOriginNode",newHaveOriginNode)
					  TxNode actionNode
					  
					  TxNode.addMsg(actionNode,"GetDirections")
					  Cache.saveToTempCache("currentActionNode",actionNode)
			          validateAddress()
			          return FAIL
			       elsif 2 == haveOrigin
			          TxNode newHaveOriginNode
			          TxNode.addValue(newHaveOriginNode,0)
					  Cache.saveToTempCache("haveOriginNode",newHaveOriginNode)
					  
			          JSONObject destJSONObject = Cache.getJSONObjectFromTempCache("destJSONObject")
			          EditRoute_C_interfaceForThirdPart(jo, destJSONObject)
			          return FAIL
			       endif
			    endif
			   
				EditRoute_C_StaticRoute(jo)
			endfunc						
			
			func checkNULL(string s)
        		if s== NULL
        			return ""
        		else
        			return s	
        		endif
        	endfunc
        	
        	func callbackFromAC()       		
        		TxNode addressNode
				addressNode = ParameterSet.getParam("returnAddress")
				Cache.saveToTempCache("validAddress",addressNode)
				       		
				Cache.deleteFromTempCache("nextAction")
				TxNode newAction
				TxNode.addMsg(newAction,"toDoAction")
				Cache.saveToTempCache("nextAction",newAction)       		   		
        	endfunc
        	
        	func callbackFromLogin()       						       		
				Cache.deleteFromTempCache("nextAction")
				TxNode newAction
				TxNode.addMsg(newAction,"toValidateAddress")
				Cache.saveToTempCache("nextAction",newAction) 
        	endfunc  
        	
        	func callbackFromSA()       						       		
			#	Cache.deleteFromTempCache("nextAction")
			#	TxNode newAction
			#	TxNode.addMsg(newAction,"goToMain")
			#	Cache.saveToTempCache("nextAction",newAction)       		   		
        	endfunc      	
        	
        	func toDoAction(TxNode address)
				JSONObject jo = JSONObject.fromString(TxNode.msgAt(address,0))
				if NULL == jo
					System.showErrorMsg("Wrong address!")
				else
					TxNode actionNode=Cache.getFromTempCache("currentActionNode")
					String actionName = TxNode.msgAt(actionNode,0)
					if (1 == String.equalsIgnoreCase(actionName,"mapIt"))
						showMap(jo)
					elsif (1 == String.equalsIgnoreCase(actionName,"searchNearby"))
						searchNearBy(jo)
					elsif (1 == String.equalsIgnoreCase(actionName,"driveTo"))
						driveTo(jo)
					elsif (1 == String.equalsIgnoreCase(actionName,"shareAddress"))
						shareAddress(jo)
					elsif (1 == String.equalsIgnoreCase(actionName,"GetDirections"))
						getDirections(jo)
					endif
				endif
        	endfunc  

		]]>
	</tml:script>
	<tml:menuItem name="goToMain" pageURL="<%=mainPageURL%>">
	</tml:menuItem>
	<tml:menuItem name="goToAddressList" pageURL="<%=getPage + "ThirdPartAddressList"%>">
	</tml:menuItem>
	<tml:menuItem name="goTypeAddress"
		pageURL="<%=getAcPage + "TypeAddress#ThirdPartAction"%>">
	</tml:menuItem>
	<tml:menuItem name="showAddress" pageURL="">
		<tml:bean name="callFunction" valueType="String" value="loadAddress">
		</tml:bean>
	</tml:menuItem>
	<tml:page id="ThirdPartAction" url="<%=getPage+"ThirdPartAction"%>" type="<%=pageType%>"
		background="" groupId="<%=GROUP_ID_COMMOM%>">
	</tml:page>
</tml:TML>
