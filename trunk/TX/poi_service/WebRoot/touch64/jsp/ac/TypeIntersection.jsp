<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%
	String pageUrl = getAcPage + "TypeIntersection";
	String callBackPageUrl = getAcPageCallBack + "TypeIntersection";
	String vaUrl = host + "/ValidateAddress.do";
%>

<tml:TML outputMode="TxNode">
	<%@ include file="model/AddressCaptureModel.jsp"%>
	<%@ include file="../dsr/controller/DSRController.jsp"%>
	<%@ include file="AddressAjax.jsp"%>
	<%@ include file="controller/SetUpHomeController.jsp"%>
	<%@ include file="/touch64/jsp/model/PrefModel.jsp"%>

	<tml:script language="fscript" version="1">
		<![CDATA[
		<%@ include file="../Stop.jsp"%>
			func CommonAPI_SetFocus(String it)
				<% if (!PoiUtil.isWarrior(handlerGloble)){ %>
				<%}else{%>
					Page.setComponentAttribute(it,"focused","true")		
				<%}%>
			endfunc
			
		    func onShow()
		        CommonAPI_SetFocus("street1")
		        Page.setComponentAttribute("countryButton","id",1122334455)
		    endfunc

		    		    
			func onLoad()
				AddressCapture_M_deleteInputAddress()
				AddressCapture_M_deleteCacheAddress()
				AddressCapture_M_initType("TypeIntersection")
				checkDefaultAddress()
				initAddress()
				
				# Set country button text
				String countryStr = AddressCapture_M_getCountry()
				if "" == countryStr
					countryStr = "<%=country%>"
					#AddressCapture_M_saveCountry(countryStr)
				endif
				if "CAN" == countryStr
				   Page.setComponentAttribute("lastLine","hint","<%=msg.get("ac.tips.lastLine.CAN")%>")
				else
				   Page.setComponentAttribute("lastLine","hint","<%=msg.get("ac.tips.lastLine.other")%>")
				endif
				Page.setComponentAttribute("countryButton","text",countryStr)
				
				String errorMsgStr = AddressCapture_M_getErrorMsg()
				if "" != errorMsgStr
					System.showErrorMsg(errorMsgStr)
				endif
			endfunc
			
			func initAddress()
			    TxNode nodeForStreet
				TxNode recentPlaceNode
				TxNode otherPlaceNode
				TxNode recentPlaceInfoNodeForStreet
				TxNode otherInfoPlaceNodeForStreet
				
				otherPlaceNode = Favorite.getAddresses()
				JSONObject homeOrOfficeAddress = SetUpHome_C_getHome()
		        if NULL != homeOrOfficeAddress
		           String address = ""
		           address = JSONObject.get(homeOrOfficeAddress,"firstLine")
		           if "" != address
		              address = address + ", " + JSONObject.get(homeOrOfficeAddress,"city") + ", " + JSONObject.get(homeOrOfficeAddress,"state")
		           else
		              address = JSONObject.get(homeOrOfficeAddress,"lastLine")
		           endif
		           String zipString = JSONObject.get(homeOrOfficeAddress,"zip")
		           if NULL != zipString && "" != zipString
		              address = address + " " + zipString
		           endif
		           
		           TxNode.addMsg(otherPlaceNode,address)
		        endif
				if NULL != otherPlaceNode
				    otherInfoPlaceNodeForStreet = filterAddressForIntersection(otherPlaceNode)
				endif
				recentPlaceNode = RecentPlaces.getAddresses()
				if NULL != recentPlaceNode
				    recentPlaceInfoNodeForStreet = filterAddressForIntersection(recentPlaceNode)
				endif
				
				TxNode.addChild(nodeForStreet,otherInfoPlaceNodeForStreet)
				TxNode.addChild(nodeForStreet,recentPlaceInfoNodeForStreet)
				Page.setFieldFilter("street1",nodeForStreet)
				
				 # Set city cache
				<%if (!"CN".equals(region)) {%>
				TxNode nearCityNode = AddressCapture_M_setCacheCity()
				if NULL != nearCityNode
				   Page.setFieldFilter("lastLine",nearCityNode)
				endif
				<%}%>
			endfunc
			
			func filterAddressForIntersection(TxNode oldTxNode)
			    TxNode newTxNode
			    int oldPlaceSize = TxNode.getStringSize(oldTxNode)
			    String placeString = ""
			    String startString = ""
			    int index
			    int i = 0
			    while i < oldPlaceSize
			       placeString = TxNode.msgAt(oldTxNode,i)
			       if "" != placeString
			          startString = String.at(placeString,0, 1)
			          if !String.isNumberString(startString)
			             index = String.find(placeString,0," AT ")
				         if index == -1
				            index = String.find(placeString,0," at ")
				         endif
				         if index == -1
				            index = String.find(placeString,0," Y ")
				         endif
				         if index != -1
				            TxNode.addMsg(newTxNode,placeString)
				         endif 
			          endif
			       endif
			       i = i + 1
			    endwhile
			    return newTxNode
			endfunc
				
			func setInformation()
			 	int i=1
			endfunc	
			
			func fillBox1()
			    fillBoxForintersection()
			endfunc
			
			func fillBox2()
				Page.setControlProperty("lastLine","focused","true")
			endfunc
			
			func fillBox3()
				Page.setControlProperty(getSubmitButtonId(),"focused","true")
			endfunc
			
			func fillBoxForintersection()
			    int selectIndex = Page.getControlProperty("street1", "selectedIndex")
			    if selectIndex == -1
			       Page.setControlProperty("street2","focused","true")
			       return FAIL
			    endif
			    
			    TxNode inputNode = ParameterSet.getParam("street1")
			    if NULL != inputNode
			       String  address = TxNode.msgAt(inputNode,0)
			       if address == NULL || address == ""
						return FAIL
				   endif
				
			       int length = String.getLength(address)
			       int splitLen = 4
			       int index = String.find(address,0," AT ")
			       if index == -1
			          index = String.find(address,0," at ")
			       endif
			       if index == -1
			          index = String.find(address,0," Y ")
			          splitLen = 3
			       endif
			       
			       int commaIndex = String.find(address,0,",")    
			       String street1 = ""
			       String street2 = ""
			       String lastLine = ""
			       if index != -1
			           street1 = String.at(address,0, index)
			           index = index + splitLen
			           int street2Len = commaIndex - index
			           commaIndex = commaIndex + 2
			           int lastLineLen = length - commaIndex
			           street2 = String.at(address,index,street2Len)
			           lastLine = String.at(address,commaIndex,lastLineLen)
			           Page.setComponentAttribute("street1","text",street1)
				       Page.setComponentAttribute("street2","text",street2)
				       Page.setComponentAttribute("lastLine","text",lastLine)
			           Page.setControlProperty(getSubmitButtonId(),"focused","true")
			       else
			           Page.setComponentAttribute("street1","text",address)
			           Page.setControlProperty("street2","focused","true")
			       endif
			    endif
			endfunc
			
			func fillValueForStreet(string address)
				if address == NULL || address == ""
					return FAIL
				endif
			
		       int length = String.getLength(address)
		       int index = String.find(address,0," AT ")
		       if index == -1
		          index = String.find(address,0," at ")
		       endif

		       if index == -1
		          index = String.find(address,0," Y ")
		       endif
		       		       
		       String street1 = ""
		       String street2 = ""
		       if index != -1
		           street1 = String.at(address,0, index)
		           index = index + 4
		           int street2Len = length - index
		           street2 = String.at(address,index,street2Len)
		       endif
		       Page.setComponentAttribute("street1","text",street1)
		       Page.setComponentAttribute("street2","text",street2)			
			endfunc
			
			func validateAddressOnClick()
		        String street1 = ""
		        String street2 = ""
				String lastLine = ""
				TxNode street1Node
				TxNode street2Node
				TxNode lastLineNode
				    
				#Street1
				street1Node = ParameterSet.getParam("street1")
				if NULL == street1Node
				    System.showErrorMsg("<%=msg.get("ac.enter.address")%>")
				    Page.setControlProperty("street1","focused","true")
		            return FAIL
				else
					street1 = TxNode.msgAt(street1Node, 0)
					if "" == street1
					    System.showErrorMsg("<%=msg.get("ac.enter.address")%>")
					    Page.setControlProperty("street1","focused","true")
		                return FAIL
					endif
				endif
				
				#Street2
				street2Node = ParameterSet.getParam("street2")
				if NULL == street2Node
				    System.showErrorMsg("<%=msg.get("ac.enter.address")%>")
				    Page.setControlProperty("street2","focused","true")
		            return FAIL
				else
				    street2 = TxNode.msgAt(street2Node, 0)
				    if "" == street2
					    System.showErrorMsg("<%=msg.get("ac.enter.address")%>")
					    Page.setControlProperty("street2","focused","true")
		                return FAIL
					endif
				endif
				    
				#lastLine
                lastLineNode = ParameterSet.getParam("lastLine")
				if NULL == lastLineNode
				    System.showErrorMsg("<%=msg.get("ac.enter.city")%>")
				    Page.setControlProperty("lastLine","focused","true")
		            return FAIL
				else
					lastLine = TxNode.msgAt(lastLineNode, 0)
					if "" == lastLine
					    System.showErrorMsg("<%=msg.get("ac.enter.city")%>")
					    Page.setControlProperty("lastLine","focused","true")
		                return FAIL
					endif
				endif
				
				if !Cell.isCoverage()
					System.showErrorMsg("<%=msg.get("common.nocell.error")%>")
		            return FAIL
				endif
								
				String country = AddressCapture_M_getCountry()
				
				JSONObject cacheJo = AddressCapture_M_getCacheAddress()
				if NULL != cacheJo
				   JSONObject inputAddressJo = AddressCapture_M_getInputAddress()
				   if NULL != inputAddressJo
				       String cacheStreet1 = JSONObject.get(inputAddressJo,"street1")
				       String cacheStreet2 =  JSONObject.get(inputAddressJo,"street2")
					   String cacheLastLine = JSONObject.get(inputAddressJo,"lastLine")
					   String cacheCountry = JSONObject.get(inputAddressJo,"country")
					   if NULL != cacheStreet1 && NULL != cacheStreet2 && NULL != cacheLastLine && NULL != cacheCountry && street1 == cacheStreet1 && street2 == cacheStreet2 && lastLine == cacheLastLine && country == cacheCountry
					      AddressCapture_M_returnAddressToInvokerPage(cacheJo)
						  return FAIL
					   endif
				   endif
				endif
				
				string inputAddress = street1 + " AT " + street2 + ", " + lastLine
				#if address exist in MyFavorite or Recent Place, does not do validation
				TxNode cacheAddressNode = AC.getStopByDetailLine(inputAddress)
				if cacheAddressNode != NULL && TxNode.getValueSize(cacheAddressNode) > 2
					cacheJo = convertStopToJSON(cacheAddressNode)
					AddressCapture_M_returnAddressToInvokerPage(cacheJo)
					return FAIL
				else		
			        JSONObject jo
			        JSONObject.put(jo,"street1",street1)
			        JSONObject.put(jo,"street2",street2)
					JSONObject.put(jo,"lastLine",lastLine)
					JSONObject.put(jo,"country",country)
					
					AddressCapture_M_saveInputAddress(jo)
					String s = JSONObject.toString(jo)
					doRequest(s)
				endif
		    endfunc
		     
			func onClickCountry()
				AddressCapture_M_country("<%=callBackPageUrl%>" + "#" + AddressCapture_M_getFrom())
			endfunc

			func checkDefaultAddress()
				JSONObject jo = AddressCapture_M_getDefaultAddress()
				if jo != NULL
					string zip = JSONObject.getString(jo,"zip")
					string city = JSONObject.getString(jo,"city")
					string state = JSONObject.getString(jo,"state")
					string firstLine = JSONObject.getString(jo,"firstLine")
					
					if city == NULL
						city = ""
					endif
					
					if state == NULL
						state = ""
					endif

					if zip == NULL
						zip = ""
					endif

					if firstLine == NULL
						firstLine = ""
					endif
					
					String lastLine = ""
					if city!="" && state!=""
						lastLine = city + "," + state + " " + zip
					endif
															
					fillValueForStreet(firstLine)
					Page.setComponentAttribute("lastLine","text",lastLine)
					
					Page.setControlProperty(getSubmitButtonId(),"focused","true")
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
				Page.setComponentAttribute("countryButton","visible","0")
				checkDSRAvail()
			endfunc
						
			func onClickSpeakIn()
				JSONObject jo
	        	JSONObject.put(jo,"callbackfunction",AddressCapture_M_getCallbackFunc())
				JSONObject.put(jo,"callbackpageurl",AddressCapture_M_getInvoker())
				JSONObject.put(jo,"from",AddressCapture_M_getFrom())
				invokeSpeakIntersection(jo)
				return FAIL
			endfunc		
			
	        func speakInClick()
	        	onClickSpeakIn()
	            return FAIL
	        endfunc
			
			func checkDSRAvail()
				if DSR_M_isDSRSupportedForDisable() == 1
					Page.setComponentAttribute("speakInButton","visible","1")
					Page.setComponentAttribute("submitButton","visible","1")
					Page.setComponentAttribute("submitButton1","visible","0")
				else
					Page.setComponentAttribute("speakInButton","visible","0")
					Page.setComponentAttribute("submitButton","visible","0")
					Page.setComponentAttribute("submitButton1","visible","1")					
				endif
			endfunc
		]]>
	</tml:script>

	<tml:menuItem name="validateAddress" onClick="validateAddressOnClick" trigger="TRACKBALL_CLICK"/>

	<tml:menuItem name="showAddress" pageURL="">
		<tml:bean name="callFunction" valueType="String" value="loadAddress">
		</tml:bean>
	</tml:menuItem>

	<tml:menuItem name="autoFill1" onClick="fillBox1"
		trigger="TRACKBALL_CLICK|KEY_MENU" />
	<tml:menuItem name="autoFill2" onClick="fillBox2"
		trigger="TRACKBALL_CLICK|KEY_MENU" />
	<tml:menuItem name="autoFill3" onClick="fillBox3"
		trigger="TRACKBALL_CLICK|KEY_MENU" />
	<tml:menuItem name="selectCountry" onClick="onClickCountry">
	</tml:menuItem>
	<tml:menuItem name="submitMenu" onClick="validateAddressOnClick"
		text="<%=msg.get("common.button.Submit")%>" trigger="KEY_MENU" />
	<tml:menuItem name="speakInMenu" onClick="speakInClick" trigger="TRACKBALL_CLICK"></tml:menuItem>
	
	<tml:page id="typeIntersectionPage" groupId="<%=GROUP_ID_AC%>"
		url="<%=pageUrl%>" type="<%=pageType%>" showLeftArrow="true"
		showRightArrow="true" helpMsg="$//$drivetointersection">
		<tml:button id="countryButton" text="<%=country%>"
			textVisible="true" isFocusable="true" fontWeight="system_median"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="selectCountry" />
			<tml:menuRef name="submitMenu" />
		</tml:button>

		<tml:panel id="interPanel">
			<tml:inputBox id="street1" fontWeight="system_large"
				isAlwaysShowPrompt="true" prompt="<%=msg.get("ac.street1")%>"
				type="dropdownfilterfield">
				<tml:menuRef name="autoFill1"></tml:menuRef>
				<tml:menuRef name="submitMenu" />
			</tml:inputBox>
			<tml:inputBox id="street2" fontWeight="system_large"
				isAlwaysShowPrompt="true" prompt="<%=msg.get("ac.street2")%>"
				type="dropdownfilterfield">
				<tml:menuRef name="autoFill2"></tml:menuRef>
				<tml:menuRef name="submitMenu" />
			</tml:inputBox>
			<%
				if (!"CN".equals(region)) {
			%>
			<tml:inputBox id="lastLine" fontWeight="system_large"
				isAlwaysShowPrompt="true"
				prompt="<%=msg.get("ac.tips.lastLine.other")%>"
				type="dropdownfilterfield">
				<tml:menuRef name="autoFill3"></tml:menuRef>
				<tml:menuRef name="submitMenu" />
			</tml:inputBox>
			<%
				} else {
			%>
			<tml:dropDownBox title="<%=msg.get("selectaddress.city")%>"
				id="lastLine" isFocusable="true" fontWeight="system_large">
				<tml:dataItem text="Beijing" />
				<tml:dataItem text="Shanghai" />
				<tml:dataItem text="Qingdao" />
				<tml:dataItem text="Shenyang" />
				<tml:dataItem text="Tianjin" />
				<tml:dataItem text="Qinhuangdao" />
			</tml:dropDownBox>
			<%
				}
			%>
			<tml:image id="bottomBgImg" url=""   visible="false" align="left|top"/>
			
			<tml:button id="submitButton"
				text="<%=msg.get("common.button.Submit")%>" fontWeight="system_large"
				imageClick=""
				imageUnclick="">
				<tml:menuRef name="validateAddress" />
				<tml:menuRef name="submitMenu" />
			</tml:button>

			<tml:button id="submitButton1"
				text="<%=msg.get("common.button.Submit")%>" fontWeight="system_large"
				imageClick=""
				imageUnclick="">
				<tml:menuRef name="validateAddress" />
				<tml:menuRef name="submitMenu" />
			</tml:button>
				
			<tml:button id="speakInButton" isFocusable="true" 
				text="<%=msg.get("common.button.sayIt")%>" fontWeight="system_large"
				imageClick=""
				imageUnclick="">
				<tml:menuRef name="speakInMenu" />
			</tml:button>
		</tml:panel>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
