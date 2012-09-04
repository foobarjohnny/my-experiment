<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.util.CommonUtil"%>
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
	<%@ include file="/WEB-INF/jsp/model/PrefModel.jsp"%>
	
	<tml:script language="fscript" version="1">
		<![CDATA[
		    <%@ include file="../Stop.jsp"%>
		    func onShow()
		        Page.setComponentAttribute("countryButton","id",1122334455)
		        Page.setControlProperty("firstLine","focused","true")
		        System.setKeyEventListener("-dial-","keypress")
		    endfunc

		    func displayButtonImage()
		    	string locale = Pref_M_getLocale()
		    	string imageUrl = "<%=imageUrl%>" + "speakInAddress_" + locale + ".png"
				Page.setControlProperty("speakInButton","image",imageUrl)
		    endfunc
		       
			func onLoad()
				displayButtonImage()
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
					
					if "" == lastLine
					   fillBoxFordefault(firstLine)
					endif
					
					Page.setControlProperty("submitButton","focused","true")
				endif
			endfunc
			
			func fillBoxFordefault(String address)
		        int length = String.getLength(address)
		        int index = String.find(address,0,",")
		        int nextIndex = String.find(address,index+1,",")
		        String firstLine
		        String lastLine
		        if nextIndex != -1
			        firstLine = String.at(address,0, index)
			        
			        int lastLineIndex = index + 1
			        int lastLineLength = length - index - 1
			        lastLine = String.at(address,lastLineIndex, lastLineLength)
		        else
		       	    firstLine = ""
		       	    lastLine = address
		        endif
		       
		        Page.setComponentAttribute("firstLine","text",firstLine)
		        Page.setComponentAttribute("lastLine","text",lastLine)
		        Page.setControlProperty("submitButton","focused","true")
			endfunc
						
			func keypress(String s)
	            if "dial" == s
	                onClickSpeakIn()
	                return TRUE
	            endif
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
				invokeSpeakAddress(jo)
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

	<tml:menuItem name="autoFillForStreet" onClick="fillBoxForStreet" trigger="TRACKBALL_CLICK|KEY_MENU"/>

	<tml:menuItem name="autoFillForCity" onClick="fillBoxForCity" trigger="TRACKBALL_CLICK|KEY_MENU"/>
	<tml:menuItem name="submitMenu" onClick="validateAddressOnClick" text="<%=msg.get("common.button.Submit")%>" trigger="KEY_MENU"/>
	
	<tml:page id="typeAddressPage" url="<%=pageUrl%>" groupId="<%=GROUP_ID_AC%>"
		type="<%=pageType%>" showLeftArrow="true" showRightArrow="true"
		helpMsg="$//$drivetoaddress">
		<tml:title id="title" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("common.address")%>
		</tml:title>
		<tml:menuRef name="submitMenu" />
		<tml:button id="countryButton" text="<%=defaultCountry%>" textVisible="true"
			isFocusable="true" fontWeight="system_median"
			imageClick="<%=imageUrl + "titlebutton2_2.png"%>"
			imageUnclick="<%=imageUrl + "titlebutton2_1.png"%>">
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
		<tml:button id="submitButton" text="<%=msg.get("common.button.Submit")%>"
			fontWeight="system_large"
			imageClick="<%=imageUrl + "button_small_on.png"%>"
			imageUnclick="<%=imageUrl + "button_small_off.png"%>">
			<tml:menuRef name="validateAddress" />
			<tml:menuRef name="submitMenu" />
		</tml:button>

		<tml:block feature="<%=FeatureConstant.DSR%>">
			<tml:image id="speakInButton" url="<%=imageUrl + "speakInAddress_en_US.png"%>" />
		</tml:block>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>
