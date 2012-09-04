<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.util.CommonUtil"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%
	String pageUrl = getAcPage+ "TypeCity";
	String callBackPageUrl = getAcPageCallBack + "TypeCity";


    String vaUrl = host + "/ValidateAddress.do";
    String speakInCityImage = imageUrl + "speakInCity.png";
    String defaultCountry = CommonUtil.getDefaultCountry(region);
%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/AddressCaptureModel.jsp"%>
	<%@ include file="../dsr/controller/DSRController.jsp"%>
	<%@ include file="AddressAjax.jsp"%>
	<%@ include file="/WEB-INF/jsp/StopUtil.jsp"%>
	<%@ include file="/WEB-INF/jsp/model/PrefModel.jsp"%>
	
	<tml:script language="fscript" version="1">
		<![CDATA[
		<%@ include file="../Stop.jsp"%>
		    func onShow()
		        Page.setComponentAttribute("countryButton","id",1122334455)
		        Page.setControlProperty("lastLine","focused","true")
		        System.setKeyEventListener("-dial-","keypress")
		    endfunc
		    
		    func displayButtonImage()
		    	string locale = Pref_M_getLocale()
		    	string imageUrl = "<%=imageUrl%>" + "speakInCity_" + locale +  ".png"
				Page.setControlProperty("speakInButton","image",imageUrl)
		    endfunc
		    
			func onLoad()
				displayButtonImage()
				AddressCapture_M_deleteInputAddress()
				AddressCapture_M_deleteCacheAddress()
				AddressCapture_M_initType("TypeCity")
			    # Set address cache
				<% if (!"CN".equals(region)){%>
				TxNode nearCityNode = AddressCapture_M_setCacheCity()
				if NULL != nearCityNode
				   Page.setFieldFilter("lastLine",nearCityNode)
				endif
				
				# Set country button text
				String countryStr = AddressCapture_M_getCountry()
				if "" == countryStr
					countryStr = "<%=defaultCountry%>"
					#AddressCapture_M_saveCountry(countryStr)
				endif
				
				if "CAN" == countryStr
				   Page.setComponentAttribute("lastLine","hint","<%=msg.get("ac.city.province")%>")
				else
				   Page.setComponentAttribute("lastLine","hint","<%=msg.get("ac.city.state")%>")
				endif
				
				Page.setComponentAttribute("countryButton","text",countryStr)
				
				String errorMsgStr = AddressCapture_M_getErrorMsg()
				if "" != errorMsgStr
					System.showErrorMsg(errorMsgStr)
				endif
					
				checkDefaultAddress()
				
				<% }%>
			endfunc
			
			func checkDefaultAddress()
				JSONObject jo = AddressCapture_M_getDefaultAddress()
				if jo != NULL
					string city = JSONObject.getString(jo,"city")
					string state = JSONObject.getString(jo,"state")
					
					if city == NULL
						city = ""
					endif
					
					if state == NULL
						state = ""
					endif
					
					string lastLine = ""
					
					if city!="" && state!=""
						lastLine = city + "," + state
					endif
					
					Page.setComponentAttribute("lastLine","text",lastLine)
					Page.setControlProperty("submitButton","focused","true")
				endif
			endfunc
			
			func keypress(String s)
	            if "dial" == s
	                onClickSpeakIn()
	                return TRUE
	            endif
	        endfunc
				
			func setInformation()
			 	int i=1
			endfunc	
			
			func fillBox()
				Page.setControlProperty("submitButton","focused","true")
			endfunc
			
			func validateAddressOnClick()
			    TxNode inputNode = ParameterSet.getParam("lastLine")
				String lastLine = ""
				
				if NULL == inputNode
				    System.showErrorMsg("<%=msg.get("ac.enter.address")%>")
		            return FAIL
				else
					lastLine = TxNode.msgAt(inputNode, 0)
					if "" == lastLine
					    System.showErrorMsg("<%=msg.get("ac.enter.address")%>")
		                return FAIL
					endif
				endif
				
				if !Cell.isCoverage()
					System.showErrorMsg("<%=msg.get("common.nocell.error")%>")
		            return FAIL
				endif
				
				TxNode cacheCityNode = checkCacheCity(lastLine)
				if NULL != cacheCityNode
				   JSONObject jo = convertStopToJSON(cacheCityNode)
				   AddressCapture_M_returnAddressToInvokerPage(jo)
				   return FAIL
				endif
				
				String country = AddressCapture_M_getCountry()
				if "" == country
					#country = "<%=defaultCountry%>"
				endif
				
				JSONObject cacheJo = AddressCapture_M_getCacheAddress()
				if NULL != cacheJo
				   JSONObject inputAddressJo = AddressCapture_M_getInputAddress()
				   if NULL != inputAddressJo
					   String cacheLastLine = JSONObject.get(inputAddressJo,"lastLine")
					   String cacheCountry = JSONObject.get(inputAddressJo,"country")
					   if NULL != cacheLastLine && NULL != cacheCountry && lastLine == cacheLastLine && country == cacheCountry
					      AddressCapture_M_returnAddressToInvokerPage(cacheJo)
						  return FAIL
					   endif
				   endif
				endif
				
		        JSONObject jo
				JSONObject.put(jo,"lastLine",lastLine)
				JSONObject.put(jo,"country",country)
				
				AddressCapture_M_saveInputAddress(jo)
				String s = JSONObject.toString(jo)
				doRequest(s)
		    endfunc
		    
		    func checkCacheCity(String lastLine)
		        TxNode nearCityNode = AddressCapture_M_getCacheCity()
		        if NULL != nearCityNode
				   int childSize = TxNode.getChildSize(nearCityNode)
				   int i = 0
				   String cityMsg = ""
				   String statesStr = ""
				   TxNode childNode
				   while i < childSize
				      childNode = TxNode.childAt(nearCityNode,i)
				      cityMsg = TxNode.msgAt(childNode,2)
				      statesStr = TxNode.msgAt(childNode,3)
				      if NULL != cityMsg && "" != cityMsg
				          if NULL != statesStr && "" != statesStr
				             cityMsg = cityMsg + ", " + statesStr
				          endif
				          if lastLine == cityMsg
				             String countryStr = AddressCapture_M_getCountry()
				             String countryForCity = TxNode.msgAt(childNode,6)
				             String countryForCityStr = "USA"
				             if "CA" == countryForCity
				                countryForCityStr = "CAN"
				             elsif "MX" == countryForCity
				                countryForCityStr = "MEX"
				             endif
				             if countryStr == "" || countryStr == countryForCityStr
				                return childNode
				             endif
				          endif
				      endif
				      i = i + 1
				   endwhile
				endif
				return NULL
		    endfunc
			
			func onClickCountry()
				AddressCapture_M_country("<%=callBackPageUrl%>" + "#" + AddressCapture_M_getFrom())
			endfunc
			]]>
	</tml:script>
	
	<tml:script language="fscript" version="1" feature="<%=FeatureConstant.DSR%>">
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
				invokeSpeakCity(jo)
				return FAIL
			endfunc	
	    
			func checkDSRAvail()
				if DSR_M_isDSRSupported()
					Page.setComponentAttribute("speakInButton","visible","1")
				else
					Page.setComponentAttribute("speakInButton","visible","0")					
				endif
			endfunc
		]]>
	</tml:script>

	
	<tml:menuItem name="validateAddress" onClick="validateAddressOnClick">
	</tml:menuItem>

	<tml:menuItem name="showAddress" pageURL="">
		<tml:bean name="callFunction" valueType="String" value="loadAddress">
		</tml:bean>
	</tml:menuItem>

	<tml:menuItem name="selectCountry" onClick="onClickCountry">
	</tml:menuItem>
	<tml:menuItem name="submitMenu" onClick="validateAddressOnClick" text="<%=msg.get("common.button.Submit")%>" trigger="KEY_MENU"/>
	
	<tml:page id="typeCityPage" url="<%=pageUrl%>" groupId="<%=GROUP_ID_AC%>"
		type="<%=pageType%>" showLeftArrow="true" showRightArrow="true"
		helpMsg="$//$drivetocity">
		<tml:title id="title" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("selectaddress.city")%>
		</tml:title>
		<tml:menuRef name="submitMenu" />
		<tml:button id="countryButton" text="<%=defaultCountry%>" textVisible="true"
			isFocusable="true" fontWeight="system_median"
			imageClick="<%=imageUrl + "titlebutton2_2.png"%>"
			imageUnclick="<%=imageUrl + "titlebutton2_1.png"%>">
			<tml:menuRef name="selectCountry" />
			<tml:menuRef name="submitMenu" />
		</tml:button>

		<tml:menuItem name="autoFill" onClick="fillBox"  trigger="TRACKBALL_CLICK|KEY_MENU"/>

	<% if (!"CN".equals(region)){%>
		<tml:inputBox id="lastLine" fontWeight="system_large"
			isAlwaysShowPrompt="true" prompt="<%=msg.get("ac.city.state")%>"
			type="dropdownfilterfield">
			<tml:menuRef name="autoFill"></tml:menuRef>
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>
	<% }else{%>
		<tml:dropDownBox title="<%=msg.get("selectaddress.city")%>" id="lastLine" isFocusable="true" fontWeight="system_large">
			<tml:dataItem text="Beijing"/>
			<tml:dataItem text="Shanghai"/>
			<tml:dataItem text="Qingdao"/>
			<tml:dataItem text="Shenyang"/>
			<tml:dataItem text="Tianjin"/>
			<tml:dataItem text="Qinhuangdao"/>
		</tml:dropDownBox>
	<% }%>
		
		<tml:button id="submitButton" text="<%=msg.get("common.button.Submit")%>"
			fontWeight="system_large"
			imageClick="<%=imageUrl + "button_small_on.png"%>"
			imageUnclick="<%=imageUrl + "button_small_off.png"%>">
			<tml:menuRef name="validateAddress" />
			<tml:menuRef name="submitMenu" />
		</tml:button>

		<tml:block feature="<%=FeatureConstant.DSR%>">
			<tml:image id="speakInButton" url="<%=imageUrl + "speakInCity_en_US.png"%>" />
		</tml:block>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>
