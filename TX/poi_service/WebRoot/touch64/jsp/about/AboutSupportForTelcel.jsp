<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.cserver.util.TnConstants"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.cserver.util.FeatureUtil"%>

<%
	boolean isSupportFindOutMore = FeatureUtil.isSupportFindOutMore(handlerGloble);
	boolean isSupportEmailSupport = FeatureUtil.isSupportEmailSupport(handlerGloble);
	boolean isSupportCallSupport = FeatureUtil.isSupportCallSupport(handlerGloble);
	String strCarrier = handlerGloble.getClientInfo(handlerGloble.KEY_CARRIER);
	String menuPhoneText1=msg.get("about.support.menu.phonecall")+"("+msg.get("about.support.phoneNumber1")+")";
	String menuPhoneText2=msg.get("about.support.menu.phonecall")+"("+msg.get("about.support.phoneNumber2")+")";
%>
<tml:TML outputMode="TxNode">
	<tml:script language="fscript" version="1">
		<![CDATA[
				<%@ include file="/touch64/jsp/GetServerDriven.jsp"%>
			func preLoad()				
				TxNode phoneNumberNode1
				TxNode.addMsg(phoneNumberNode1,System.parseI18n("<%=msg.get("about.support.phoneNumber1")%>"))
				MenuItem.setBean("callSupportMenu1", "phonenumber", phoneNumberNode1)
				
				TxNode phoneNumberNode2
				TxNode.addMsg(phoneNumberNode2,System.parseI18n("<%=msg.get("about.support.phoneNumber2")%>"))
				MenuItem.setBean("callSupportMenu2", "phonenumber", phoneNumberNode2)
				
				TxNode node1
				TxNode.addMsg(node1,System.parseI18n("<%=msg.get("about.support.wapUrl")%>"))
				MenuItem.setBean("findOutMoreMenu", "url", node1)
				
				TxNode node2
				TxNode.addMsg(node2,System.parseI18n("<%=msg.get("about.support.emailAddress")%>"))
				
				TxNode node3
				TxNode.addMsg(node3,System.parseI18n("<%=msg.get("about.support.emailSubject")%>"))
				MenuItem.setBean("emailSupportMenu", "emailAddr", node2)
				MenuItem.setBean("emailSupportMenu", "emailSubject", node3)
				Page.setControlProperty("nullField1","focusable",1)
				
				if ServerDriven_CanPhoneNumber()
					Page.setComponentAttribute("phone1","visible","1")
					Page.setComponentAttribute("phone2","visible","1")
				else
					Page.setComponentAttribute("phone1","visible","0")
					Page.setComponentAttribute("phone2","visible","0")					
				endif
	        endfunc
						]]>
	</tml:script>
	<tml:page id="AboutSupport" url="<%=getPage + "AboutSupport"%>"
		type="<%=pageType%>" background="" helpMsg="$//$supportinfo"
		groupId="<%=GROUP_ID_MISC%>">

		<tml:actionItem name="sendEmailAction"
			action="<%=TnConstants.LOCALSERVICE_SENDEMAIL%>">
			<tml:input name="emailAddr" />
			<tml:input name="emailSubject" />
		</tml:actionItem>
		<tml:actionItem name="findOutMore"
			action="<%=TnConstants.LOCALSERVICE_INVOKEPHONEBROWSER%>">
			<tml:input name="url" />
		</tml:actionItem>
		<tml:actionItem name="makePhoneCallAction"
			action="<%=TnConstants.LOCALSERVICE_MAKEPHONECALL%>">
			<tml:input name="phonenumber" />
		</tml:actionItem>
		<tml:actionItem name="cancelSubscribe"
			action="<%=TnConstants.LOCALSERVICE_CANCEL_SUBSCRIBE%>">
		</tml:actionItem>

		<tml:menuItem name="emailSupportMenu" actionRef="sendEmailAction"
			text="<%=msg.get("about.support.menu.email")%>" trigger="KEY_MENU" />
		<tml:menuItem name="callSupportMenu1" actionRef="makePhoneCallAction"
			text="<%=menuPhoneText1%>"
			trigger="KEY_MENU|TRACKBALL_CLICK" />
		<tml:menuItem name="callSupportMenu2" actionRef="makePhoneCallAction"
			text="<%=menuPhoneText2%>"
			trigger="KEY_MENU|TRACKBALL_CLICK" />
		<tml:menuItem name="findOutMoreMenu" actionRef="findOutMore"
			text="<%=msg.get("about.support.menu.findOutMore")%>"
			trigger="KEY_MENU|TRACKBALL_CLICK" />
		<tml:menuItem name="cancelSubscribeMenu" actionRef="cancelSubscribe"
			text="<%=msg.get("about.support.menu.cancelSubscribe")%>"
			trigger="KEY_MENU" />

		<%if(isSupportFindOutMore){%>
		<tml:menuRef name="findOutMoreMenu" />
		<%}%>
		<%if(isSupportEmailSupport){%>
		<tml:menuRef name="emailSupportMenu" />
		<%}%>
		<%if(isSupportCallSupport){%>
		<tml:menuRef name="callSupportMenu1" />
		<tml:menuRef name="callSupportMenu2" />
		<%}%>

		<!-- title -->
		<tml:title id="title" fontWeight="bold|system_large" align="center"
			fontColor="white">
			<%=TnUtil.amend(msg.get("about.support.title"))%>
		</tml:title>

		<!-- Just for Telcel -->
		<tml:nullField id="nullField1" />
		<tml:multiline id="text1" fontWeight="system_medium">
			<%=TnUtil.amend(msg.get("about.support.text1"))%>
		</tml:multiline>
		<tml:multiline id="text2" fontWeight="system_medium">
			<%=TnUtil.amend(msg.get("about.support.text2"))%>
		</tml:multiline>

		<tml:urlLabel id="link" fontWeight="system_median" fontColor="blue">
			<%=TnUtil.amend(msg.get("about.support.urltext")) %>
			<tml:menuRef name="findOutMoreMenu" />
		</tml:urlLabel>

		<tml:urlLabel id="phone1" fontWeight="system_median"
			fontColor="#005AFF">
			<%=TnUtil.amend(msg.get("about.support.phonetext1"))%>
			<tml:menuRef name="callSupportMenu1" />
		</tml:urlLabel>

		<tml:urlLabel id="phone2" fontWeight="system_median"
			fontColor="#005AFF">
			<%=TnUtil.amend(msg.get("about.support.phonetext2"))%>
			<tml:menuRef name="callSupportMenu2" />
		</tml:urlLabel>
		<tml:image id="titleShadow" url="" align="left|top" />
	</tml:page>
	<cserver:outputLayout />
</tml:TML>