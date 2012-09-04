<%@ include file="../Header.jsp"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>

<%@page import="com.telenav.datatypes.traffic.common.v10.AlertDTO"%>
<%@page import="org.json.me.*"%>

<tml:TML outputMode="TxNode">
	<jsp:include page="/WEB-INF/jsp/local_service/controller/MapWrapController.jsp" />

	
	<%
		String pageURL = getPage + "TrafficIncidents";
	
		AlertDTO[] alertDTOs = (AlertDTO[]) request.getAttribute("trafficAlerts");
		JSONObject json = (JSONObject) request.getAttribute("trafficAlertsJson");
		JSONArray locArr1 = new JSONArray();
		if(json != null)
		{
		    locArr1 = (JSONArray) json.get("trafficAlerts");
		}
		//System.out.println("alertdtos length=" + alertDTOs.length);
		//System.out.println("alertdtos json=" + json);
		System.out.println("out0:" + locArr1);
	%>
	
	<tml:script language="fscript" version="1">
		<![CDATA[

		
				func onLoad()
					println("herea")
					
					displayPurchaseBanner()
					if AccountTypeForSprint_IsLiteUser()
						println("onload lite")
						Page.setComponentAttribute("mainPanelPremium",       "visible","0")
						Page.setComponentAttribute("mainPanel",       "visible","1")
				  	elsif AccountTypeForSprint_IsBundleUser()
						println("onload bundle")
						Page.setComponentAttribute("mainPanelPremium",       "visible","0")
						Page.setComponentAttribute("mainPanel",       "visible","1")
					else
						println("onload other - premium ")
						Page.setComponentAttribute("mainPanelPremium",       "visible","1")
						Page.setComponentAttribute("mainPanel",       "visible","0")					
					endif
					Page.setComponentAttribute("footerImage","visible","0")
				endfunc
				
				func onBack()
	            	purchaseBanner_Back()
	        	endfunc
        func gotoMap()
       		 int freeTrial = UserInfoManager_getFreeTrial()
			JSONObject logJSON = compileLogJSON("Map", freeTrial)
	        Purchase_M_logAdBannerLog(<%=EventTypes.AD_BANNER_CLICK%>,logJSON)
        	MapWrap_C_showCurrent()
        endfunc
			]]>
	</tml:script>
			
	<tml:page id="CommuteReportWithPremUpsell" url="<%=pageURL%>" type="net" helpMsg="" groupId="<%=GROUP_ID_MISC%>">

		<tml:title id="title" fontWeight="bold|system_large" 
				align="center" fontColor="white">
			<![CDATA[<%="Nearby Traffic Report"%>]]>
		</tml:title>

		<tml:menuItem name="mapIt" onClick="gotoMap" text="<%=msg.get("Map")%>" trigger="KEY_MENU"></tml:menuItem>
		<tml:menuRef name="mapIt" />
		
		
		<tml:listBox id="mainPanel" isFocusable="true" hotKeyEnable="false">
			<%@ include file="TrafficIncidentsPanel.jsp"%>	
		</tml:listBox>

		<tml:listBox id="mainPanelPremium" isFocusable="true" hotKeyEnable="false">
			<%@ include file="TrafficIncidentsPanel.jsp"%>	
		</tml:listBox>
		
		<%String TML_PAGE_FOR_LOG = "CommuteReportWithPremUpsell";  %>
		<%@ include file="../purchase/PurchaseBanner.jsp"%>
		<cserver:outputLayoutWithAppend includeFiles="purchase/PurchaseBanner"/>
		
	</tml:page>
		
</tml:TML>
