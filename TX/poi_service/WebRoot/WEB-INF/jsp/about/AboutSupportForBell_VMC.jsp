<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
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
%>
<tml:TML outputMode="TxNode">
<tml:script language="fscript" version="1">
				<![CDATA[
			func preLoad()
				TxNode node
				TxNode.addMsg(node,System.parseI18n("<%=msg.get("about.support.phoneNumber")%>"))
				MenuItem.setBean("callSupportMenu", "phonenumber", node)
				
				TxNode phoneNumberNode1
				TxNode.addMsg(phoneNumberNode1,System.parseI18n("<%=msg.get("about.support.phoneNumber1")%>"))
				MenuItem.setBean("callSupportMenu1", "phonenumber", phoneNumberNode1)
				
				TxNode phoneNumberNode2
				TxNode.addMsg(phoneNumberNode2,System.parseI18n("<%=msg.get("about.support.phoneNumber2")%>"))
				MenuItem.setBean("callSupportMenu2", "phonenumber", phoneNumberNode2)
				
				TxNode phoneNumberNode3
				TxNode.addMsg(phoneNumberNode3,System.parseI18n("<%=msg.get("about.support.phoneNumber3")%>"))
				MenuItem.setBean("callSupportMenu3", "phonenumber", phoneNumberNode3)

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
	        endfunc
						]]>
			</tml:script>
	<tml:page id="AboutSupport" url="<%=getPage + "AboutSupport"%>" type="<%=pageType%>" background=""  helpMsg="$//$supportinfo" groupId="<%=GROUP_ID_MISC%>">

		<tml:actionItem name="sendEmailAction" action="<%=TnConstants.LOCALSERVICE_SENDEMAIL%>">
	       <tml:input name = "emailAddr"/>
	       <tml:input name = "emailSubject"/>
	    </tml:actionItem>
	    <tml:actionItem name="findOutMore" action="<%=TnConstants.LOCALSERVICE_INVOKEPHONEBROWSER%>"> 
	        <tml:input name = "url"/>
	    </tml:actionItem>	
	    <tml:actionItem name="makePhoneCallAction" action="<%=TnConstants.LOCALSERVICE_MAKEPHONECALL%>"> 
	       <tml:input name = "phonenumber"/>
	    </tml:actionItem>
	    <tml:actionItem name="cancelSubscribe" action="<%=TnConstants.LOCALSERVICE_CANCEL_SUBSCRIBE%>"> 
		</tml:actionItem>
    
  		<tml:menuItem name="emailSupportMenu" actionRef="sendEmailAction" text="<%=msg.get("about.support.menu.email")%>" trigger="KEY_MENU" />
	    <tml:menuItem name="callSupportMenu" actionRef="makePhoneCallAction" text="<%=msg.get("about.support.menu.phonecall")%>" trigger="KEY_MENU|TRACKBALL_CLICK"/>
	    <tml:menuItem name="callSupportMenu1" actionRef="makePhoneCallAction" text="<%=msg.get("about.support.menu.phonecall")%>" trigger="KEY_MENU|TRACKBALL_CLICK"/>
	    <tml:menuItem name="callSupportMenu2" actionRef="makePhoneCallAction" text="<%=msg.get("about.support.menu.phonecall")%>" trigger="KEY_MENU|TRACKBALL_CLICK"/>
	    <tml:menuItem name="callSupportMenu3" actionRef="makePhoneCallAction" text="<%=msg.get("about.support.menu.phonecall")%>" trigger="KEY_MENU|TRACKBALL_CLICK"/>
	    <tml:menuItem name="findOutMoreMenu" actionRef="findOutMore" text="<%=msg.get("about.support.menu.findOutMore")%>" trigger="KEY_MENU"/>
        <tml:menuItem name="cancelSubscribeMenu" actionRef="cancelSubscribe" text="<%=msg.get("about.support.menu.cancelSubscribe")%>" trigger="KEY_MENU"/>
        
        <%if(isSupportFindOutMore){%>
        <tml:menuRef name="findOutMoreMenu" />
        <%}%>
        <%if(isSupportEmailSupport){%>
        <tml:menuRef name="emailSupportMenu" />
        <%}%>
        <%if(isSupportCallSupport){%>
        <tml:menuRef name="callSupportMenu" />
        <%}%>
		
		<!-- title --> 
		<tml:title id="title" fontWeight="bold|system_large" align="center" fontColor="white">
			<%=TnUtil.amend(msg.get("about.support.title"))%>
		</tml:title>
		<tml:nullField id="nullField1"/>
    	<tml:label id="text1" textWrap="wrap" fontWeight="system_large">
			<%=TnUtil.amend(msg.get("about.support.text1"))%>  
		</tml:label>
		<tml:label id="text2" textWrap="wrap" fontWeight="system_large">
			<%=TnUtil.amend(msg.get("about.support.text2"))%>  
		</tml:label>
		<tml:label id="text3" textWrap="wrap" fontWeight="system_large">
			<%=TnUtil.amend(msg.get("about.support.text3"))%>  
		</tml:label>
		<tml:urlLabel id="phone" fontWeight="system_median" fontColor="#005AFF">
			<%=TnUtil.amend(msg.get("about.support.phonetext"))%>			
			<tml:menuRef name="callSupportMenu" />
		</tml:urlLabel>
		
		<tml:urlLabel id="phone1" fontWeight="system_median" fontColor="#005AFF">
			<%=TnUtil.amend(msg.get("about.support.phonetext1"))%>			
			<tml:menuRef name="callSupportMenu1" />
		</tml:urlLabel>
		
		<tml:urlLabel id="phone2" fontWeight="system_median" fontColor="#005AFF">
			<%=TnUtil.amend(msg.get("about.support.phonetext2"))%>			
			<tml:menuRef name="callSupportMenu2" />
		</tml:urlLabel>
		
		<tml:urlLabel id="phone3" fontWeight="system_median" fontColor="#005AFF">
			<%=TnUtil.amend(msg.get("about.support.phonetext3"))%>			
			<tml:menuRef name="callSupportMenu3" />
		</tml:urlLabel>
		
		<tml:image id="titleShadow" url="" align="left|top"/>

	</tml:page>
	<cserver:outputLayout supportLocal="Y"/>
</tml:TML>

