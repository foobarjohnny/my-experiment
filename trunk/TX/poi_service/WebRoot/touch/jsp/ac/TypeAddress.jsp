<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.util.CommonUtil"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>
<%
	String pageUrl = getAcPage+ "TypeAddress";
	String callBackPageUrl = getAcPageCallBack + "TypeAddress";
    String defaultCountry = CommonUtil.getDefaultCountry(region);
%>

<tml:TML outputMode="TxNode">
	<%@ include file="model/AddressCaptureModel.jsp"%>
	<%@ include file="../dsr/controller/DSRController.jsp"%>
	<%@ include file="AddressCommon.jsp"%>
	<%@include file="../StopUtil.jsp"%>
	<%@ include file="/touch/jsp/model/PrefModel.jsp"%>
	
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
		        CommonAPI_SetFocus("firstLine")
		        Page.setComponentAttribute("countryButton","id",1122334455)
		    endfunc

		       
			func onLoad()
			    AddressCapture_M_deleteInputAddress()
				AddressCapture_M_deleteCacheAddress()
				initAddress()
				checkDefaultAddress()
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
															
					Page.setComponentAttribute("firstLine","text",firstLine)
					Page.setComponentAttribute("lastLine","text",lastLine)
					
					Page.setControlProperty(getSubmitButtonId(),"focused","true")
				endif
			endfunc
	        
	        func speakInClick()
	        	onClickSpeakIn()
	            return FAIL
	        endfunc
			
	        
			func onClickCountry()
				AddressCapture_M_country("<%=callBackPageUrl%>" + "#" + AddressCapture_M_getFrom())
			endfunc
			
			func getSubmitButtonId()
				string id = "submitButton1"
				if DSR_M_isDSRSupported()
					id = "submitButton"
				endif
				return id
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
				invokeSpeakAddress(jo)
				return FAIL
			endfunc	

			func checkDSRAvail()
				if DSR_M_isDSRSupported()
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

	<tml:menuItem name="selectCountry" onClick="onClickCountry">
	</tml:menuItem>

	<tml:menuItem name="autoFillForStreet" onClick="fillBoxForStreet" trigger="TRACKBALL_CLICK|KEY_MENU"/>

	<tml:menuItem name="autoFillForCity" onClick="fillBoxForCity" trigger="TRACKBALL_CLICK|KEY_MENU"/>
	<tml:menuItem name="submitMenu" onClick="validateAddressOnClick" text="<%=msg.get("common.button.Submit")%>" trigger="KEY_MENU"/>
	<tml:menuItem name="speakInMenu" onClick="speakInClick" trigger="TRACKBALL_CLICK"></tml:menuItem>
	<tml:page id="typeAddressPage" url="<%=pageUrl%>" groupId="<%=GROUP_ID_AC%>"
		type="<%=pageType%>" showLeftArrow="true" showRightArrow="true"
		helpMsg="$//$drivetoaddress">
		<tml:button id="countryButton" text="<%=defaultCountry%>" textVisible="true"
			isFocusable="true" fontWeight="system_median"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="selectCountry" />
			<tml:menuRef name="submitMenu" />
		</tml:button>
		<tml:inputBox id="firstLine" fontWeight="system_large" style="address"
			isAlwaysShowPrompt="true" prompt="<%=msg.get("home.firstLine")%>"
			type="dropdownfilterfield">
			<tml:menuRef name="autoFillForStreet"></tml:menuRef>
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>
	<% if (!"CN".equals(region)){%>
		<tml:inputBox id="lastLine" prompt="<%=msg.get("ac.tips.lastLine.other")%>"
			isAlwaysShowPrompt="true" fontWeight="system_large"
			type="dropdownfilterfield">
			<tml:menuRef name="autoFillForCity"></tml:menuRef>
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>
	<% }else{%>
		<tml:dropDownBox title="<%=msg.get("common.title.city")%>" id="lastLine" isFocusable="true" fontWeight="system_large" titleFontWeight="bold|system_large" >
			<tml:dataItem text="Beijing"/>
			<tml:dataItem text="Shanghai"/>
			<tml:dataItem text="Qingdao"/>
			<tml:dataItem text="Shenyang"/>
			<tml:dataItem text="Tianjin"/>
			<tml:dataItem text="Qinhuangdao"/>
		</tml:dropDownBox>
	<% }%>
		
		<tml:image id="bottomBgImg" url=""   visible="false" align="left|top"/>
		
		<tml:button id="submitButton" text="<%=msg.get("common.button.Submit")%>"
			fontWeight="system_large"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="validateAddress" />
			<tml:menuRef name="submitMenu" />
		</tml:button>

		<tml:button id="submitButton1" text="<%=msg.get("common.button.Submit")%>"
			fontWeight="system_large"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="validateAddress" />
			<tml:menuRef name="submitMenu" />
		</tml:button>
		
		<tml:button id="speakInButton" text="<%=msg.get("common.button.sayIt")%>"
			fontWeight="system_large" isFocusable="true" 
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="speakInMenu" />
		</tml:button>		
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>
