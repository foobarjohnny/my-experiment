<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@ include file="Header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/NameAndEmailModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
		    func onLoad()
		        String source = NameAndEmail_M_getSource()
		        MenuItem.setItemValid("okButton",3,1)
		        MenuItem.setItemValid("cancelButton",3,1)
		        String text = ""
		        if "<%=Constant.shareAddress%>" == source
		           text = "<%=msg.get("att.share.address")%>"
		        elsif "<%=Constant.referFriend%>" == source
		           text = "<%=msg.get("att.refer.friend")%>"
		        elsif "<%=Constant.sendUpdate%>" == source
		           text = "<%=msg.get("att.send.eta")%>"
		        elsif "<%=Constant.commuteAlert%>" == source
		           text = "<%=msg.get("att.commute.alert")%>"
		           Page.setComponentAttribute("againButton","visible","0")
		           MenuItem.setItemValid("okButton",3,0)
		           MenuItem.setItemValid("cancelButton",3,0)
		        endif
		        
		        MenuItem.commitSetItemValid("okButton")
		        MenuItem.commitSetItemValid("cancelButton")
		        Page.setComponentAttribute("promptText","text",text)
		    endfunc
		    
		    func okClick()
		        System.doAction("nameAndEmail")
		        return FAIL
		    endfunc
		    
		    func cancelClick()
		        System.back()
		        return FAIL
		    endfunc
		    
		    func againClick()
		        String source = NameAndEmail_M_getSource()
		        NameAndEmail_M_saveDonotAskAgain(source)
		        System.back()
		        return FAIL
		    endfunc
		]]>
	</tml:script>

	<tml:menuItem name="nameAndEmail"
		pageURL="<%=getPageWithLocale + "NameAndEmail"%>">
	</tml:menuItem>

	<tml:menuItem name="doOK" onClick="okClick">
	</tml:menuItem>

	<tml:menuItem name="doCancel" onClick="cancelClick">
	</tml:menuItem>

	<tml:menuItem name="doAgain" onClick="againClick">
	</tml:menuItem>

	<tml:menuItem name="okMenu"  text="<%=msg.get("common.button.OK")%>" onClick="okClick" trigger="KEY_MENU"/>
	<tml:menuItem name="cancelMenu"  text="<%=msg.get("common.button.Cancel")%>" onClick="cancelClick" trigger="KEY_MENU"/>
	<tml:menuItem name="againMenu"  text="<%=msg.get("att.ask.again")%>" onClick="againClick" trigger="KEY_MENU"/>
	<tml:page id="NameAndEmailAskPage"
		url="<%=getPage + "NameAndEmailAskPage"%>"
		type="<%=pageType%>" showLeftArrow="true" showRightArrow="true"
		helpMsg="" groupId="<%=GROUP_ID_COMMOM%>">
		<tml:title id="title" align="center|middle"
			fontColor="white" fontWeight="bold|system_large">
			<%=msg.get("common.register")%>
		</tml:title>

		<tml:multiline id="promptText" fontWeight="system_large" align="left"
			isFocusable="false">
		</tml:multiline>

		<tml:button id="okButton" text="<%=msg.get("common.button.OK")%>" fontWeight="system_large"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="doOK" />
			<tml:menuRef name="okMenu" />
			<tml:menuRef name="cancelMenu" />
			<tml:menuRef name="againMenu" />
		</tml:button>

		<tml:button id="cancelButton" text="<%=msg.get("common.button.Cancel")%>" fontWeight="system_large"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="doCancel" />
			<tml:menuRef name="okMenu" />
			<tml:menuRef name="cancelMenu" />
			<tml:menuRef name="againMenu" />			
		</tml:button>

		<tml:button id="againButton" text="<%=msg.get("att.ask.again")%>"
			fontWeight="system_large"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="doAgain" />
			<tml:menuRef name="okMenu" />
			<tml:menuRef name="cancelMenu" />
			<tml:menuRef name="againMenu" />
		</tml:button>
		<tml:image id="titleShadow" url=""   visible="true" align="left|top"/>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>