<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map.Entry"%>

<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>	
<%@ include file="../Header.jsp"%>

<%
	String strCarrier = handlerGloble.getClientInfo(handlerGloble.KEY_CARRIER);
%>

<tml:TML outputMode="TxNode">
	<%@ include file="AddressCommon.jsp"%>
	<%@ include file="model/SetUpHomeModel.jsp"%>
	<%@ include file="model/AddressCaptureModel.jsp"%>
	<jsp:include page="../AddressSearchScript.jsp"/>
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
				checkDSRAvail_TypeAddress()
			endfunc
			
			func onLoad()
				initPageComponent()
			endfunc
			
			func initHome()
				JSONObject jo = SetUpHome_M_getHome()
				if jo != NULL && SetUpHome_M_isFromHome()
					string firstLine = JSONObject.getString(jo,"firstLine")
					string city = JSONObject.getString(jo,"city")
					string state = JSONObject.getString(jo,"state")
					string zipcode = JSONObject.getString(jo,"zip")
					string locality = JSONObject.getString(jo,"locality")
					string county = JSONObject.getString(jo,"county")
					
					String country = JSONObject.getString(jo,"country")
					if NULL != country && "" != country
						EditHome_M_saveCountry(country)
						EditHome_M_FromHome()
					endif
						
					string lastLine
					
				   if  locality != NULL && locality != ""
						   if lastLine != NULL && lastLine != ""
						      lastLine = lastLine + "," + locality 
						   else
						      lastLine = locality
						   endif
				    endif
					
					if  city != NULL && city != ""
						   if lastLine != NULL && lastLine != ""
						      lastLine = lastLine + "," + city 
						   else
						      lastLine = city
						   endif
				    endif
				    
				       if  county != NULL && county != ""
						   if lastLine != NULL && lastLine != ""
						      lastLine = lastLine + "," + county 
						   else
						      lastLine = county
						   endif
						endif
						
						 if  state != NULL && state != ""
						   if lastLine != NULL && lastLine != ""
						      lastLine = lastLine + "," + state 
						   else
						      lastLine = state
						   endif
						endif
				
				
					if "" != zipcode
						lastLine = lastLine + " " + zipcode
					endif
					
					Page.setComponentAttribute("firstLine","text",firstLine)
					Page.setComponentAttribute("cityName","text",city)
					Page.setComponentAttribute("county","text",county)
					Page.setComponentAttribute("state","text",state)		
					Page.setComponentAttribute("postalCode","text",zipcode)							
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
	<tml:menuItem name="refresh" onClick="refreshOnClick" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="showAddress" pageURL="">
		<tml:bean name="callFunction" valueType="String" value="loadAddress">
		</tml:bean>
	</tml:menuItem>

	<tml:menuItem name="selectCountry" onClick="onClickCountry">
	</tml:menuItem>

    <tml:menuItem name="autoFillForStreet" onClick="fillBoxForStreet"  trigger="TRACKBALL_CLICK|KEY_MENU"/>
	<tml:menuItem name="autoFillForState" onClick="fillBoxForState" trigger="TRACKBALL_CLICK|KEY_MENU"/>
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
		<!-- template -->
		<tml:panel id="typeAddressPanel" layout="vertical">	
	         <%@include file="../Templates.jsp" %>
		</tml:panel>
	</tml:page>
	<cserver:outputLayout/>			
</tml:TML>