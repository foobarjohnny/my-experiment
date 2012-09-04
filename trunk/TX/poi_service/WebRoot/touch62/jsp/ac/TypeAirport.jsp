<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@ include file="../Header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
	String pageUrl = host + "/FindAirPort.do";
	String callBackPageUrl = host + "/FindAirPort.do";

	TxNode listNode = (TxNode) request.getAttribute("node");
	String vaUrl = host + "/ValidateAirport.do";
	String callbackfunction = (String) request
			.getAttribute("callbackfunction");
	String pageURL = (String) request.getAttribute("callbackpageurl");
%>

<tml:TML outputMode="TxNode">
	<%@ include file="model/AddressCaptureModel.jsp"%>
	<%@ include file="../dsr/controller/DSRController.jsp"%>
	<%@include file="/touch62/jsp/StopUtil.jsp"%>
	<%@ include file="/touch62/jsp/model/PrefModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
			func CommonAPI_SetFocus(String it)
				<% if (!PoiUtil.isWarrior(handlerGloble)){ %>
				<%}else{%>
					Page.setComponentAttribute(it,"focused","true")		
				<%}%>
			endfunc		      
		          
		      func onLoad()
		         Page.setComponentAttribute("airportName","focused","true")
		         #CommonAPI_SetFocus("airportName")
		    	 AddressCapture_M_deleteInputAddress()
				 AddressCapture_M_deleteCacheAddress()
				 AddressCapture_M_initType("TypeAirport")
				 TxNode node
				 node=ParameterSet.getParam("airportlist")
				 Page.setFieldFilter("airportName",node)
				 Page.setComponentAttribute("airportName","showdropdown","1")
			  endfunc

			  
		      func fillBox()
		          Page.setComponentAttribute("airportName","cursorIndex","0")
		          Page.setControlProperty(getSubmitButtonId(),"focused","true")
		      endfunc
		      
		      func validateAddressOnClick()
		        TxNode airportNode
		        airportNode = ParameterSet.getParam("airportName")
				if NULL == airportNode
					System.showErrorMsg("<%=msg.get("ac.error.airport")%>")
					Page.setControlProperty("airportName","focused","true")
					return FAIL
				endif
				String airportName = TxNode.msgAt(airportNode, 0)
				if "" == airportName
				    System.showErrorMsg("<%=msg.get("ac.error.airport")%>")
				    Page.setControlProperty("airportName","focused","true")
					return FAIL
				endif
				
                if 3 > String.getLength(airportName)
                    System.showErrorMsg("<%=msg.get("ac.error.airport.letter")%>")
                    Page.setControlProperty("airportName","focused","true")
					return FAIL
                endif
				
				if !Cell.isCoverage()
					System.showErrorMsg("<%=msg.get("common.nocell.error")%>")
		            return FAIL
				endif
								
				if 0 != TxNode.getChildSize(airportNode)
				    TxNode airportSelectNode = TxNode.childAt(airportNode,0)
			        JSONObject jo = convertStopToJSON(airportSelectNode)
			        AddressCapture_M_returnAirportToInvokerPage(jo)
				    return FAIL
				endif
				
				JSONObject cacheJo = AddressCapture_M_getCacheAddress()
				if NULL != cacheJo
				   JSONObject inputAddressJo = AddressCapture_M_getInputAddress()
				   if NULL != inputAddressJo
					   String cacheAirportName = JSONObject.get(inputAddressJo,"airportName")
					   if NULL != cacheAirportName && airportName == cacheAirportName
					      AddressCapture_M_returnAirportToInvokerPage(cacheJo)
						  return FAIL
					   endif
				   endif
				endif
				
				JSONObject airportJo
				JSONObject.put(airportJo,"airportName",airportName)
				AddressCapture_M_saveInputAddress(airportJo)
				
				doRequest(airportName)
		      endfunc
		      
		      func doRequest(String s)
				TxNode node
				TxNode.addMsg(node,s)
				TxRequest req
				string url= "<%=vaUrl%>"
				string scriptName="stateChange"
				TxRequest.open(req,url)
				TxRequest.setRequestData(req,node)
				TxRequest.onStateChange(req,scriptName)
				TxRequest.setProgressTitle(req,"<%=msg.get("ac.validate.airport")%>")
				TxRequest.send(req)
			endfunc
			
			func speakInClick()
	        	onClickSpeakIn()
	            return FAIL
	        endfunc
			
			func stateChange(TxNode node,int status)
			    if  status == 0
			        System.showErrorMsg("<%=msg.get("error.no.found")%>")
			        return FAIL
			    else
			        JSONArray ja
			        if NULL != node
						int size = TxNode.getValueSize(node)
						if 0 < size
							int ok = TxNode.valueAt(node,0)
							if 1 == ok
								size = TxNode.getStringSize(node)
								if 0 < size
								    String strJa = TxNode.msgAt(node,0)
								    if NULL != strJa
								        ja = JSONArray.fromString(strJa)
										int airpotListSize = JSONArray.length(ja)
										if 0 < airpotListSize
											if 1 == airpotListSize
												JSONObject jo = JSONArray.get(ja,0)
												AddressCapture_M_saveCacheAddress(jo)
												AddressCapture_M_returnAirportToInvokerPage(jo)
												return FAIL
											else
												MenuItem.setBean("showAirport","airportList",node)
												string from = Page.getControlProperty("page","url_flag")
												string pageUrl = "<%=getPageCallBack%>" + "AirportList#" + from
												MenuItem.setAttribute("showAirport","url",pageUrl)
												System.doAction("showAirport")
											endif
										endif
								    endif
								endif
							else
							 	System.showErrorMsg("<%=msg.get("error.no.found")%>")
			        			return FAIL		
							endif
						endif
					endif
			    endif
			endfunc

			func getSubmitButtonId()
				string id = "submitButton1"
				if DSR_M_isDSRSupportedForDisable() == 1
					id = "submitButton"
				endif
				return id
			endfunc
		]]>
	</tml:script>

	<tml:script language="fscript" version="1"
		feature="<%=FeatureConstant.DSR%>">
		<![CDATA[
			func preLoad()
				checkDSRAvail()
			endfunc
						  
		    func onClickSpeakIn()
				JSONObject jo
	        	JSONObject.put(jo,"callbackfunction",AddressCapture_M_getCallbackFunc())
				JSONObject.put(jo,"callbackpageurl",AddressCapture_M_getInvoker())
				JSONObject.put(jo,"from",AddressCapture_M_getFrom())
				invokeSpeakAirport(jo)
				return FAIL
			endfunc	

			func checkDSRAvail()
				if DSR_M_isDSRSupportedForDisable() == 1
					<%if(TnUtil.isTMOAndroidUser(handlerGloble)){%>
						Page.setComponentAttribute("speakInButton","visible","1")
						Page.setComponentAttribute("speakInButton","imageUnclick","$availableOffButton")
						Page.setComponentAttribute("speakInButton","imageClick","$availableOnButton")
					<%} else {%>
						Page.setComponentAttribute("speakInButton","visible","1")
					<%}%>
					Page.setComponentAttribute("submitButton","visible","1")
					Page.setComponentAttribute("submitButton1","visible","0")
				elsif DSR_M_isDSRSupportedForDisable() == 2
					Page.setComponentAttribute("speakInButton","imageUnclick","$disableOffButton")
					Page.setComponentAttribute("speakInButton","imageClick","$disableOnButton")
					Page.setComponentAttribute("submitButton","visible","0")
					Page.setComponentAttribute("submitButton1","visible","1")
				else
					Page.setComponentAttribute("speakInButton","visible","0")
					Page.setComponentAttribute("submitButton","visible","0")
					Page.setComponentAttribute("submitButton1","visible","1")					
				endif
			endfunc
		]]>
	</tml:script>

	<tml:menuItem name="autoFill" onClick="fillBox"
		trigger="TRACKBALL_CLICK|KEY_MENU" />
	<tml:menuItem name="validateAirport" onClick="validateAddressOnClick" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="showAirport" pageURL="">
		<tml:bean name="callFunction" valueType="String" value="loadAirport">
		</tml:bean>
	</tml:menuItem>

	<tml:menuItem name="submitMenu" onClick="validateAddressOnClick" text="<%=msg.get("common.button.Submit")%>" trigger="KEY_MENU" />
	<tml:menuItem name="speakInMenu" onClick="speakInClick" trigger="TRACKBALL_CLICK"></tml:menuItem>
	<tml:page id="typeAirportPage" url="<%=pageUrl%>" type="<%=pageType%>"
		groupId="<%=GROUP_ID_AC%>" showLeftArrow="true" showRightArrow="true"
		helpMsg="$//$drivetoairport">
		<tml:bean name="airportlist" valueType="TxNode"
			value="<%=Utility.TxNode2Base64(listNode)%>"></tml:bean>
		<tml:inputBox id="airportName"
			prompt="<%=msg.get("ac.airport.letter")%>" style="capital"
			fontWeight="system_large" type="dropdownfilterfield" isAlwaysShowPrompt="true">
			<tml:menuRef name="autoFill" />
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>

		<tml:image id="bottomBgImg" url=""   visible="false" align="left|top"/>

		<tml:button id="submitButton"
			text="<%=msg.get("common.button.Submit")%>" fontWeight="system_large"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="validateAirport" />
			<tml:menuRef name="submitMenu" />
		</tml:button>

		<tml:button id="submitButton1"
			text="<%=msg.get("common.button.Submit")%>" fontWeight="system_large"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="validateAirport" />
			<tml:menuRef name="submitMenu" />
		</tml:button>
		
		<tml:button id="speakInButton" isFocusable="true" 
			text="<%=msg.get("common.button.sayIt")%>" fontWeight="system_large"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="speakInMenu" />
		</tml:button>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>