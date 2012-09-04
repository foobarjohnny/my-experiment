<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="Header.jsp"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.telenav.cserver.util.TnConstants"%>

<%
    String pageURL = getPageCallBack + "OneBoxSearch";
	String poiSearchURL = getPageCallBack + "SearchPoi";
	
    int gpsValidTime = 240;
	int cellIdValidTime=480;
	int gpsTimeout=12;
%>
<tml:TML outputMode="TxNode">
	<jsp:include page="AddressSearchScript.jsp"/>
	<%@ include file="/touch64/jsp/local_service/GetGps.jsp"%>
	<%@ include file="/touch64/jsp/poi/controller/PoiListController.jsp"%>
	<jsp:include page="/touch64/jsp/controller/OneBoxController.jsp"/>
	<jsp:include page="/touch64/jsp/ac/controller/SelectAddressController.jsp" />
	<tml:script language="fscript" version="1">
		<![CDATA[
		<%@ include file="GetServerDriven.jsp"%>
		<%@ include file="OneBoxSearchScript.jsp" %>
						
			func preLoad()
				Page.setComponentAttribute("countryButton","visible","0")			
				checkDSRAvail_TypeAddress()
			endfunc
			
			func onLoad()
				initPageComponent()
				initAddress()
				checkDefaultAddress()
				Page.setComponentAttribute("firstLine","style", "nosuggest")
				OneBox_M_initialForPOI()
				
				TxNode inputNode = ShareData.get("<%=Constant.StorageKey.BROWSER_SHARE_OBJECT_INPUT_CHAR%>")
				println("~~~~~~~~~~~~~~~inputNode~~~~~~~~"+inputNode)
				String pageUrlFlag = Page.getControlProperty("page","url_flag")
				if NULL != inputNode
					String s = TxNode.msgAt(inputNode,0)
					if NULL != s && "" != s
					    if "<%=TnConstants.OBS_COME_FROM_PLACES%>" == pageUrlFlag
						Page.setComponentAttribute("oneBox","text", s)
					    else
					        Page.setComponentAttribute("firstLine","text", s)
						Page.setComponentAttribute("firstLine","focused", "true")
					    endif
						ShareData.delete("<%=Constant.StorageKey.BROWSER_SHARE_OBJECT_INPUT_CHAR%>")
					endif
				endif
				loadLocalList()				
				
				if "<%=TnConstants.OBS_COME_FROM_PLACES%>" == pageUrlFlag
					Page.setComponentAttribute("Search_tabContainer","defaultFocus","1")
					Page.setComponentAttribute("oneBox","focused", "true")
				endif
				
			endfunc
		
		    func onShow()								
			    AddressCapture_M_deleteInputAddress()
				AddressCapture_M_deleteCacheAddress()
				initAddress()
				checkDefaultAddress()
		    endfunc
			
		]]>
	</tml:script>
	
	<tml:menuItem name="deleteRecentSearch" text="<%=msg.get("poi.clear.search.history")%>"
		trigger="KEY_MENU" onClick="deleteRecentSearch">
	</tml:menuItem>
	<tml:menuItem name="refresh" onClick="refreshOnClick" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="doSearch" onClick="onClickOneBoxSearch" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="speakInMenu" onClick="speakInClick" trigger="TRACKBALL_CLICK" />
	
	<tml:menuItem name="validateAddress" onClick="validateAddressOnClick" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="showAddress" pageURL="">
		<tml:bean name="callFunction" valueType="String" value="loadAddress">
		</tml:bean>
	</tml:menuItem>
	<tml:menuItem name="selectCountry" onClick="onClickCountry"/>
	<tml:menuItem name="autoFillForStreet" onClick="fillBoxForStreet_Template" trigger="TRACKBALL_CLICK|KEY_MENU"/>
	<tml:menuItem name="autoFillForCity" onClick="fillBoxForCity" trigger="TRACKBALL_CLICK|KEY_MENU"/>
	<tml:menuItem name="submitMenu" onClick="validateAddressOnClick" text="<%=msg.get("common.button.Submit")%>" trigger="KEY_MENU"/>
	<tml:menuItem name="speakInMenu" onClick="speakInClick" trigger="TRACKBALL_CLICK"></tml:menuItem>
	
	<tml:page id="InternationalSearch" url="<%=getPage + "InternationalSearch"%>" type="<%=pageType%>" helpMsg="$//$search"
		 groupId="<%=GROUP_ID_COMMOM%>">
		 <tml:tabContainer id="Search_tabContainer" defaultFocus="0" style="vertical">
		 	<tml:tab id="addressTab" label="<%=msg.get("onebox.Address")%>">
		 		 <tml:panel id="addressPanel" layout="vertical">
		 		 	<%@include file="Templates.jsp" %>
		 		 </tml:panel>
		 		 <tml:image id="bottomBgImg" url="" visible="false" align="left|top" />
		 		 <tml:button id="speakInButton" text="<%=msg.get("common.button.sayIt")%>"
					fontWeight="system_large" isFocusable="true" 
					imageClick=""
					imageUnclick="">
					<tml:menuRef name="speakInMenu" />
				</tml:button>	
		 	</tml:tab>
		 	<tml:tab id ="businessTab" label="<%=msg.get("onebox.Business")%>">
				<tml:image id="oneBoxBgImg" visible="true" align="left|top"/>
				 <%-- client side search for "searchfield" type and load hotbrand list--%>
				<tml:inputBox id="oneBox" titleAbove="false"
					titleFontWeight="system_large|bold" fontWeight="system_large" isAlwaysShowPrompt="true"
					prompt="<%=msg.get("onebox.what.prompt.business")%>" type="searchfield" length="300">
					<tml:menuRef name="doSearch" />
					<tml:menuRef name="deleteRecentSearch" />
				</tml:inputBox>
				<tml:button id="oneBoxSearchButton" text="  ">
					<tml:menuRef name="doSearch"/>
					<tml:menuRef name="deleteRecentSearch" />
				</tml:button>
		 	</tml:tab>		 	
		 </tml:tabContainer>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>
