<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
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
	String pageUrl = getAcPage+ "TypeAddress";
	String callBackPageUrl = getAcPageCallBack + "TypeAddress";
	String strCarrier = handlerGloble.getClientInfo(handlerGloble.KEY_CARRIER);
%>

<tml:TML outputMode="TxNode">
	<%@ include file="model/AddressCaptureModel.jsp"%>
	<%@ include file="../dsr/controller/DSRController.jsp"%>
	<%@ include file="AddressCommon.jsp"%>
	<%@ include file="../StopUtil.jsp"%>
	<%@ include file="/touch64/jsp/model/PrefModel.jsp"%>
	<jsp:include page="../AddressSearchScript.jsp"/>
	<tml:script language="fscript" version="1">
		<![CDATA[
		    <%@ include file="../Stop.jsp"%>

			func CommonAPI_SetFocus(String it)
				<% if (!PoiUtil.isWarrior(handlerGloble)){ %>
				<%}else{%>
					Page.setComponentAttribute(it,"focused","true")		
				<%}%>
			endfunc
		
			func preLoad()
				Page.setComponentAttribute("countryButton","visible","0")			
				checkDSRAvail_TypeAddress()
			endfunc
			
		    func onShow()
		        CommonAPI_SetFocus("firstLine")
		        Page.setComponentAttribute("countryButton","id",1122334455)
		    endfunc
			
		 func onLoad()
			   initPageComponent()
				<% if (!PoiUtil.isRimNonTouch(handlerGloble)){ %>
			    	Page.setComponentAttribute("firstLine","style", "nosuggest")
				<%}%>
			    AddressCapture_M_deleteInputAddress()
				AddressCapture_M_deleteCacheAddress()
				initAddress()
				checkDefaultAddress()
			endfunc
			
			]]>
	</tml:script>

	<tml:menuItem name="validateAddress" onClick="validateAddressOnClick" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="refresh" onClick="refreshOnClick" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="showAddress" pageURL="">
		<tml:bean name="callFunction" valueType="String" value="loadAddress">
		</tml:bean>
	</tml:menuItem>

	<tml:menuItem name="selectCountry" onClick="onClickCountry">
	</tml:menuItem>

	<tml:menuItem name="autoFillForStreet" onClick="fillBoxForStreet_Template" trigger="TRACKBALL_CLICK|KEY_MENU"/>
	<tml:menuItem name="autoFillForState" onClick="fillBoxForState" trigger="TRACKBALL_CLICK|KEY_MENU"/>
	<tml:menuItem name="autoFillForCity" onClick="fillBoxForCity" trigger="TRACKBALL_CLICK|KEY_MENU"/>
	<tml:menuItem name="autoFillForCityCountyOrPostalCode" onClick="fillBoxForCityCountyOrPostalCode" trigger="TRACKBALL_CLICK|KEY_MENU"/>	
	<tml:menuItem name="submitMenu" onClick="validateAddressOnClick" text="<%=msg.get("common.button.Submit")%>" trigger="KEY_MENU"/>
	<tml:menuItem name="speakInMenu" onClick="speakInClick" trigger="TRACKBALL_CLICK"></tml:menuItem>
	<tml:page id="typeAddressPage" url="<%=pageUrl%>" groupId="<%=GROUP_ID_AC%>"
		type="<%=pageType%>" showLeftArrow="true" showRightArrow="true"
		helpMsg="$//$drivetoaddress">
		<tml:button id="countryButton" text="<%=country%>" textVisible="true"
			isFocusable="true" fontWeight="system_median"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="selectCountry" />
			<tml:menuRef name="submitMenu" />
		</tml:button>
		<!-- template -->
		<tml:panel id="typeAddressPanel" layout="vertical">	
	         <%@include file="../Templates.jsp" %>
		</tml:panel>
		<tml:image id="bottomBgImg" url=""   visible="false" align="left|top"/> 
		
		<tml:button id="speakInButton" text="<%=msg.get("common.button.sayIt")%>"
			fontWeight="system_large" isFocusable="true" 
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="speakInMenu" />
		</tml:button>		
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>