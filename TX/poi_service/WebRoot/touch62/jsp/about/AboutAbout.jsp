<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.cserver.util.TnConstants"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="com.telenav.cserver.browser.framework.BrowserFrameworkConstants"%>
<%@page import="com.telenav.kernel.util.datatypes.TnContext"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
	String buildNo = handlerGloble.getClientInfo(DataHandler.KEY_BUILDNUMBER);
	
	MessageWrap msgTemp = MessageHelper.getInstance(true).getMessageWrap(msgKey);
	String mapData = PoiUtil.getTnContext(handlerGloble).getProperty(TnContext.PROP_MAP_DATASET);
	
	String helpMsg = "$//$aboutatt";
	String carrier = handlerGloble.getClientInfo(DataHandler.KEY_CARRIER);
	String product = handlerGloble.getClientInfo(DataHandler.KEY_PRODUCTTYPE);
	if (TnUtil.isSprintUser(carrier)){
		helpMsg = "$//$abouttn";
	}else if(TnUtil.isTMOUser(product)){
		helpMsg = "$//$abouttn";
	}
	String aboutMessage = "";
	if(TnUtil.isUsccUser(product))
	{
		aboutMessage = msgTemp.get("about.version") + buildNo+"\n\n"+ msgTemp.get("about.poweredBy") + msgTemp.get("about.company") + "\n"
						+ msgTemp.get("about.right") + "\n" + msgTemp.get("about.mapDataBy")+" "+msgTemp.get(mapData);
	}else if(TnUtil.isRogersCarrier(carrier))
	{
		aboutMessage = msgTemp.get("about.version") + buildNo+"\n"+ msgTemp.get("about.poweredBy")+" " + msgTemp.get("about.company") + "\n"
						+ msgTemp.get("about.mapDataBy")+" "+msgTemp.get(mapData);
	}else if(TnUtil.isBell_VMCUser(carrier))
	{
		aboutMessage = msgTemp.get("about.version") + buildNo +"\n"+ msgTemp.get("about.poweredBy")+" " + msgTemp.get("about.company") + "\n"
						+ msgTemp.get("about.mapDataBy")+" "+msgTemp.get(mapData);
	}
	else
	{
		aboutMessage = msgTemp.get("about.version") + buildNo+"\n"
						+ msgTemp.get("about.mapDataBy")+" "+msgTemp.get(mapData);	
	}	
	
%>
<tml:TML outputMode="TxNode">
	<jsp:include page="/touch62/jsp/AccountTypeForVN.jsp"/>
<tml:script language="fscript" version="1">
				<![CDATA[
			
			<%@ include file="../GetServerDriven.jsp"%>
			
			func preLoad()
				
				string text = "<%=msgTemp.get("about.title")%>" + " " 												
				string product = ""
				string tos = ""  
				if Account.isTnMaps()
					text = text + "<%=msgTemp.get("common.attmap")%>"   
					product = "<%=msgTemp.get("common.attmap")%>"
					tos = "<%=msgTemp.get("about.tos_text_attmap")%>"
				else
					text = text + "<%=msgTemp.get("common.attnav")%>"   
					product = "<%=msgTemp.get("common.attnav")%>"
					tos = "<%=msgTemp.get("about.tos_text_1")%>"
				endif

				if 1 == ServerDriven_ShowTOS()
					Page.setComponentAttribute("tos_text_1","visible","1")
					Page.setComponentAttribute("link","visible","1")
				else
					Page.setComponentAttribute("tos_text_1","visible","0")
					Page.setComponentAttribute("link","visible","0")
				endif


				<%if(TnUtil.isVNUser(product)){%>
					text = "<%=msgTemp.get("about.title")%>" + " " 
					if AccountTypeForSprint_IsLiteUser()
 						text = text + "<%=msgTemp.get("startup.title.lite")%>"
						product = "<%=msgTemp.get("startup.title.lite")%>"
						tos = "<%=msgTemp.get("about.tos_text_free")%>"
					elsif AccountTypeForSprint_IsPremUser()
 						text = text + "<%=msgTemp.get("startup.title.premium")%>"
						product = "<%=msgTemp.get("startup.title.premium")%>"
						tos = "<%=msgTemp.get("about.tos_text_1")%>"
					elsif AccountTypeForSprint_IsFreeTrial()
						text = text + "<%=msgTemp.get("startup.title.free")%>"
						product = "<%=msgTemp.get("startup.title.free")%>"
						tos = "<%=msgTemp.get("about.tos_text_1")%>"
					endif
				<%}%>
				
				<%if(TnUtil.isBAWPAIDUser(handlerGloble)){%>
					text = "<%=msgTemp.get("about.title")%>" + " " 
					if AccountTypeForSprint_IsLiteUser()
						text = text + "<%=msgTemp.get("startup.title.lite.paid")%>"
						product = "<%=msgTemp.get("startup.title.lite.paid")%>"
						tos = "<%=msgTemp.get("about.tos_text_paid")%>"
					elsif AccountTypeForSprint_IsPremUser()
						text = text + "<%=msgTemp.get("startup.title.premium.paid")%>"
						product = "<%=msgTemp.get("startup.title.premium.paid")%>"
						tos = "<%=msgTemp.get("about.tos_text_paid")%>"
					endif
				<%}%>


				Page.setComponentAttribute("product1","text",product)
				Page.setComponentAttribute("title","text",text)
				Page.setComponentAttribute("tos_text_1","text",tos)
				Page.setControlProperty("nullField1","focusable",1)
				
				TxNode node
				TxNode.addMsg(node,System.parseI18n("<%=msgTemp.get("about.TOS.Url")%>"))
				MenuItem.setBean("TOSLink", "url", node)
				MenuItem.setBean("TOSLinkClick", "url", node)
	        endfunc
						]]>
			</tml:script>
	<tml:page id="About" url="<%=getPage + "AboutAbout"%>" type="net"   background="" helpMsg="<%=helpMsg%>">
		<!-- title --> 
		<tml:title id="title" fontWeight="bold|system_large" align="center" fontColor="white"/>
		<tml:nullField id="nullField1"/>
		 <tml:multiline id="product1" fontWeight="system_large"/>
    	<tml:multiline id="product" fontWeight="system_large">
			<%=TnUtil.amend(aboutMessage)%>
		</tml:multiline>
		<!--if you don't want to show Terms and Conditions of Service ,just add this item in sdp:<entry key="productType(like TN)...TOS" value="0"/>-->
		<tml:actionItem name="TOS" action="<%=TnConstants.LOCALSERVICE_INVOKEPHONEBROWSER%>"> 
	        <tml:input name = "url"/>
	    </tml:actionItem>
	    
		<tml:menuItem name="TOSLink" actionRef="TOS" text="<%=msgTemp.get("about.visit")%>" trigger="KEY_MENU"/>
		<tml:menuItem name="TOSLinkClick" actionRef="TOS" trigger="TRACKBALL_CLICK"/>
		<tml:multiline id="tos_text_1" fontWeight="system_median"/>
		<tml:urlLabel id="link" fontWeight="system_median"  fontColor="blue">			
			<%=TnUtil.amend(msgTemp.get("about.link")) %>
			<tml:menuRef name="TOSLinkClick" />
			<tml:menuRef name="TOSLink" />
		</tml:urlLabel>

	
		<tml:image id="poweredby" url="" align="left|top"/>
		<tml:image id="titleShadow" url="" align="left|top"/>

	</tml:page>
	<cserver:outputLayout/>
</tml:TML>