<%@ include file="../Header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%
	String cancelServiceUrl = "{login.http}/" + ClientHelper.getModuleNameForLogin(handlerGloble) + "/getFeedbackList.do";
	
	String carrier = handlerGloble.getClientInfo(handlerGloble.KEY_CARRIER);
	String platform = handlerGloble.getClientInfo(handlerGloble.KEY_PLATFORM);
	if(TnUtil.isRogersCarrier(carrier)){
		cancelServiceUrl = "{login.http}/" + ClientHelper.getModuleNameForLogin(handlerGloble) + "/rogers/goToJsp.do?pageRegion=" + region + "&amp;jsp=ManageSubscription";
	}
%>
<tml:TML outputMode="TxNode">
	<jsp:include page="/touch/jsp/AccountTypeForSprint.jsp"/>
	<tml:page id="AboutMenu" url="<%=getPage + "AboutMenu"%>" type="<%=pageType%>"
		background="" helpMsg="$//$about" groupId="<%=GROUP_ID_MISC%>">

		<tml:script language="fscript" version="1">
			<![CDATA[
				func preLoad()
					string text = "<%=msg.get("about.title")%>" + " "   
					if Account.isTnMaps()
						Page.setComponentAttribute("cancelService","visible","0")
						text = text + "<%=msg.get("common.attmap")%>"   
					else
						text = text + "<%=msg.get("common.attnav")%>"   
					endif
					Page.setComponentAttribute("item1","text",text)
					
					if AccountTypeForSprint_IsLiteUser() || AccountTypeForSprint_IsBundleUser() || AccountTypeForSprint_IsFreeTrial()
					   Page.setComponentAttribute("cancelService","visible","0") 
					else
					   Page.setComponentAttribute("cancelService","visible","1")
					endif
			    endfunc
			]]>
		</tml:script>


		<tml:menuItem name="about" pageURL="<%=getPage + "AboutAbout"%>"
			trigger="TRACKBALL_CLICK">
		</tml:menuItem>
		<tml:menuItem name="pin"
			pageURL="<%=host + "/About.do?pageRegion=" + region + "&amp;action=aboutPin"%>"
			trigger="TRACKBALL_CLICK">
		</tml:menuItem>
		<tml:menuItem name="support" pageURL="<%=getPage + "AboutSupport"%>"
			trigger="TRACKBALL_CLICK">
		</tml:menuItem>
		<tml:actionItem name="diagnostic"
			action="<%=Constant.LOCALSERVICE_DIAGNOSTIC%>">
		</tml:actionItem>
		<tml:menuItem name="diagnosticMenu" actionRef="diagnostic"
			trigger="TRACKBALL_CLICK" />
		<tml:menuItem name="location" pageURL="<%=getPage + "AboutLocation"%>" trigger="TRACKBALL_CLICK">
		</tml:menuItem>
		<tml:menuItem name="cancelService"
			pageURL="<%=cancelServiceUrl%>"
			trigger="TRACKBALL_CLICK">
		</tml:menuItem>
		
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
			<%if( ("RIM".equalsIgnoreCase(platform)) && ("T-Mobile".equalsIgnoreCase(carrier)) ){%>
				<tml:listItem id="item5" fontWeight="bold|system_large" align="left"
				isFocusable="true">
				<%=TnUtil.amend(msg.get("about.location"))%>
				<tml:menuRef name="location" />
				</tml:listItem>
			<%}%>
			<tml:block feature="<%=FeatureConstant.CANCEL_SERVICE%>">
				<tml:listItem id="cancelService" fontWeight="bold|system_large"
					align="left" isFocusable="true">
					<%=TnUtil.amend(msg.get("about.cancel.service"))%>
					<tml:menuRef name="cancelService"/>
				</tml:listItem>
			</tml:block>
		</tml:listBox>
		<tml:image id="titleShadow" url="" align="left|top"/>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>

