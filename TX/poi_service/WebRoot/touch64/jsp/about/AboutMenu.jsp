<%@ include file="../Header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%
	//just vivo use getLoginUrl currently,so we do not have Judgment statements here. 
	String getLoginUrl = "{login.http}/" + ClientHelper.getModuleNameForLogin(handlerGloble) + "/international/goToJsp.do?pageRegion=" + region + "&amp;jsp=";
%>
<tml:TML outputMode="TxNode">
    <%@ include file="/touch64/jsp/GetClientInfo.jsp"%>
	<%if(TnUtil.isTMOUser(product)){%>
	<jsp:include page="/touch64/jsp/AccountTypeForTMO.jsp"/>
	<%}else if (TnUtil.isVNUser(product)) {%>
	<jsp:include page="/touch64/jsp/AccountTypeForVN.jsp"/>
	<%}else{%>
	<jsp:include page="/touch64/jsp/AccountTypeForSprint.jsp"/>
	<%}%>
	<tml:page id="AboutMenu" url="<%=getPage + "AboutMenu"%>" type="<%=pageType%>"
		background="" helpMsg="$//$about" groupId="<%=GROUP_ID_MISC%>">

		<tml:script language="fscript" version="1">
			<![CDATA[
				<%@ include file="/touch64/jsp/GetServerDriven.jsp"%>
				func preLoad()
					string text = "<%=msg.get("about.title")%>" + " "   
					if Account.isTnMaps()
						text = text + "<%=msg.get("common.attmap")%>"   
					else
						text = text + "<%=msg.get("common.attnav")%>"   
					endif

					<%if(TnUtil.isVNUser(product)){%>
						text = "<%=msg.get("about.title")%>" + " "   
						if AccountTypeForSprint_IsLiteUser()
 							text = text + "<%=msg.get("startup.title.lite")%>"
						elsif AccountTypeForSprint_IsPremUser()
 							text = text + "<%=msg.get("startup.title.premium")%>"
						endif
					<%}%>

					Page.setComponentAttribute("item1","text",text)
					
					if ServerDriven_CanLocationStatement()
					   Page.setComponentAttribute("item5","visible","1")
					else
					   Page.setComponentAttribute("item5","visible","0")
					endif

					if ServerDriven_CanDiagnostic()
					   Page.setComponentAttribute("item4","visible","1")
					else
					   Page.setComponentAttribute("item4","visible","0")
					endif
					
					if ServerDriven_HavePIN()
						Page.setComponentAttribute("item3","visible","1")
					else
						Page.setComponentAttribute("item3","visible","0")
					endif
					
					if ServerDriven_CanManageSubscription()
						Page.setComponentAttribute("item6","visible","1")
					else
						Page.setComponentAttribute("item6","visible","0")
					endif
			    endfunc
			]]>
		</tml:script>


		<tml:menuItem name="about" pageURL="<%=getPage + "AboutAbout"%>" trigger="TRACKBALL_CLICK" />
		<tml:menuItem name="pin"
			pageURL="<%=host + "/About.do?pageRegion=" + region + "&amp;action=aboutPin"%>"
			trigger="TRACKBALL_CLICK">
		</tml:menuItem>
		<tml:menuItem name="support" pageURL="<%=getPage + "AboutSupport"%>" trigger="TRACKBALL_CLICK" />
		<tml:actionItem name="diagnostic" action="<%=Constant.LOCALSERVICE_DIAGNOSTIC%>" />
		<tml:menuItem name="diagnosticMenu" actionRef="diagnostic" trigger="TRACKBALL_CLICK" />
		<tml:menuItem name="location" pageURL="<%=getPage + "AboutLocation"%>" trigger="TRACKBALL_CLICK" />
		<tml:menuItem name="manageSubscribe" pageURL="<%=getLoginUrl +"ManageMySubscription"%>" trigger="TRACKBALL_CLICK" />
				
		<!-- title -->
		<tml:title id="title" fontWeight="bold|system_large" align="center"
			fontColor="white">
			<%=TnUtil.amend(msg.get("about.title"))%>
		</tml:title>

		<tml:listBox id="listBox" isFocusable="true" getFocus="true"
			hotKeyEnable="false">
			<tml:listItem id="item1" fontWeight="bold|system_large" align="left"
				isFocusable="true">
				<tml:menuRef name="about" />
			</tml:listItem>

			<tml:listItem id="item2" fontWeight="bold|system_large" align="left"
				isFocusable="true">
				<%=TnUtil.amend(msg.get("about.support"))%>
				<tml:menuRef name="support" />
			</tml:listItem>

			<tml:listItem id="item3" fontWeight="bold|system_large" align="left"
				isFocusable="true">
				<%=TnUtil.amend(msg.get("about.pin"))%>
				<tml:menuRef name="pin" />
			</tml:listItem>

			<tml:listItem id="item4" fontWeight="bold|system_large" align="left"
				isFocusable="true">
				<%=TnUtil.amend(msg.get("about.diagnostic"))%>
				<tml:menuRef name="diagnosticMenu" />
			</tml:listItem>
			
			<tml:listItem id="item5" fontWeight="bold|system_large" align="left"
				isFocusable="true">
				<%=TnUtil.amend(msg.get("about.location"))%>
				<tml:menuRef name="location" />
			</tml:listItem>			
	
			<tml:listItem id="item6" fontWeight="bold|system_large" align="left"
				isFocusable="true">
				<%=TnUtil.amend(msg.get("about.subscribe"))%>
				<tml:menuRef name="manageSubscribe" />
			</tml:listItem>	
			
		</tml:listBox>
		<tml:image id="titleShadow" url="" align="left|top"/>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
