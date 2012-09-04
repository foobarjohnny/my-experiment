<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>	
<%@ include file="../Header.jsp"%>

<tml:TML outputMode="TxNode">
	<%@ include file="AddressCommon.jsp"%>
	<%@ include file="model/SetUpHomeModel.jsp"%>
	
	<tml:script language="fscript" version="1">
		<![CDATA[
			func CommonAPI_SetFocus(String it)
				<% if (!PoiUtil.isWarrior(handlerGloble)){ %>
				<%}else{%>
					Page.setComponentAttribute(it,"focused","true")		
				<%}%>
			endfunc
			
			func preLoad()
			    Page.setComponentAttribute("countryButton","visible","0")
				<% if (!PoiUtil.isRimNonTouch(handlerGloble)){ %>
			    	Page.setComponentAttribute("firstLine","style", "nosuggest")
				<%}%>
			    Page.setComponentAttribute("lastLine","style", "nosuggest")
			    CommonAPI_SetFocus("firstLine")
				Page.setComponentAttribute("countryButton","id",1122334455)
				AddressCapture_M_deleteInputAddress()
				AddressCapture_M_deleteCacheAddress()
				initAddress()
				initHome()
			endfunc
			
			func initHome()
				JSONObject jo = SetUpHome_M_getHome()
				if jo != NULL && SetUpHome_M_isFromHome()
					string firstLine = JSONObject.getString(jo,"firstLine")
					string city = JSONObject.getString(jo,"city")
					string state = JSONObject.getString(jo,"state")
					string zipcode = JSONObject.getString(jo,"zip")
					string lastLine
					if city != NULL && city != ""
						lastLine = city
						if state != NULL && state != ""
							lastLine = city + "," + state
						endif	
					else
						lastLine = state
					endif
				
					if "" != zipcode
						lastLine = lastLine + " " + zipcode
					endif
					
					Page.setComponentAttribute("firstLine","text",firstLine)
					Page.setComponentAttribute("lastLine","text",lastLine)					
				endif
			endfunc
			
			func onClickSubmit()
				validateAddressOnClick()
				return FAIL
			endfunc

			func onClickCountry()
				SetUpHome_M_fromHome(0)
				AddressCapture_M_country("<%=getPageCallBack + "HomeAddress#Home"%>")
				return FAIL
			endfunc						
			]]>
	</tml:script>

	<tml:menuItem name="validateAddress" onClick="onClickSubmit">
	</tml:menuItem>

	<tml:menuItem name="showAddress" pageURL="">
		<tml:bean name="callFunction" valueType="String" value="loadAddress">
		</tml:bean>
	</tml:menuItem>

	<tml:menuItem name="selectCountry" onClick="onClickCountry">
	</tml:menuItem>

    <tml:menuItem name="autoFillForStreet" onClick="fillBoxForStreet"  trigger="TRACKBALL_CLICK|KEY_MENU"/>
	
	<tml:menuItem name="autoFillForCity" onClick="fillBoxForCity"  trigger="TRACKBALL_CLICK|KEY_MENU"/>
	<tml:menuItem name="submitMenu" onClick="validateAddressOnClick" text="<%=msg.get("common.button.Submit")%>" trigger="KEY_MENU"/>
	
	<tml:page id="typeAddressPage" url="<%=getPage + "HomeAddress"%>" groupId="<%=GROUP_ID_AC%>"
		type="<%=pageType%>" helpMsg="$//$drivehome">
		<tml:menuRef name="submitMenu" />
		<tml:button id="countryButton" text="<%=msg.get("home.button.USA")%>" textVisible="true"
			isFocusable="true" fontWeight="system_median"
			imageClick="" 
			imageUnclick="">
			<tml:menuRef name="selectCountry" />
			<tml:menuRef name="submitMenu" />
		</tml:button>
		<tml:menuItem name="autoFill" onClick="fillBox" />
		
		<tml:inputBox id="firstLine" prompt="<%=msg.get("home.firstLine")%>" 
			isAlwaysShowPrompt="true" fontWeight="system_large" type="dropdownfilterfield">
			<tml:menuRef name="autoFillForStreet"></tml:menuRef>
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>
		<tml:inputBox id="lastLine" prompt="<%=msg.get("home.lastLine")%>"
			isAlwaysShowPrompt="true" fontWeight="system_large" type="dropdownfilterfield">
		    <tml:menuRef name="autoFillForCity"></tml:menuRef>
		    <tml:menuRef name="submitMenu" />
		</tml:inputBox>
		
		<tml:button id="submitButton" text="<%=msg.get("common.button.Submit")%>"
			fontWeight="system_large"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="validateAddress" />
			<tml:menuRef name="submitMenu" />
		</tml:button>
	</tml:page>
	<cserver:outputLayout/>			
</tml:TML>