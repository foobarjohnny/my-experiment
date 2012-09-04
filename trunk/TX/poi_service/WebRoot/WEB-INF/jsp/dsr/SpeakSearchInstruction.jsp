<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>	
<% 
%>
<tml:TML outputMode="TxNode">
<%@ include file="model/DSRModel.jsp"%>
	<tml:script language="fscript" version="1">
	<![CDATA[
		func onClickButton()
			DSR_M_saveSearchInstFlag(0)
			System.doAction("search1")
			return FAIL
		endfunc

		func onClickOkButton()
			System.doAction("search1")
			return FAIL
		endfunc
	]]>
	</tml:script>

	<tml:menuItem name="okButtonMenu" onClick="onClickOkButton" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="dontAskButtonMenu" onClick="onClickButton" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="okButtonMenu1" onClick="onClickOkButton" text="<%=msg.get("dsr.search.inst.button1")%>" trigger="KEY_MENU"/>
	<tml:menuItem name="dontAskButtonMenu1" onClick="onClickButton" text="<%=msg.get("dsr.search.inst.button2")%>" trigger="KEY_MENU"/>
	<tml:menuItem name="search1" pageURL="<%=getDsrPage + "SpeakSearch1"%>"/>
			
	<tml:page id="SpeakSearchInstruction" url="<%=getPage + "SpeakSearchInstruction"%>" type="<%=pageType%>" groupId="<%=GROUP_ID_DSR%>" 
		background="<%=imageUrl + "backgroud_no_title.png"%>" supportback="false">
		<tml:menuRef name="okButtonMenu1" />
		<tml:menuRef name="dontAskButtonMenu1" />
		
		<tml:label id="label1" align="center" fontWeight="system_large" textWrap="wrap">
			<%=msg.get("dsr.search.inst.text1")%>
		</tml:label>
		<tml:label id="label2" align="center" fontWeight="system_large|bold" textWrap="wrap">
			<%=msg.get("dsr.search.inst.text2")%>
		</tml:label>
		<tml:label id="label3" align="center" fontWeight="system_large" textWrap="wrap">
			<%=msg.get("dsr.search.inst.text3")%>
		</tml:label>
		<tml:label id="label4" align="center" fontWeight="system_large|bold" textWrap="wrap">
			<%=msg.get("dsr.search.inst.text4")%>
		</tml:label>
		<tml:button id="dontAskButton" text="<%=msg.get("dsr.search.inst.button2")%>"
			fontWeight="system_large" isFocusable="true"
			imageClick="<%=imageUrl + "button_large_on.png"%>"
			imageUnclick="<%=imageUrl + "button_large_off.png"%>">
			<tml:menuRef name="dontAskButtonMenu" />
			<tml:menuRef name="dontAskButtonMenu1" />
			<tml:menuRef name="okButtonMenu1" />
		</tml:button>
		
		<tml:button id="okButton" text="<%=msg.get("dsr.search.inst.button1")%>"
			fontWeight="system_large" isFocusable="true"
			imageClick="<%=imageUrl + "button_large_on.png"%>"
			imageUnclick="<%=imageUrl + "button_large_off.png"%>">
			<tml:menuRef name="okButtonMenu" />
			<tml:menuRef name="dontAskButtonMenu1" />
			<tml:menuRef name="okButtonMenu1" />
		</tml:button>		

	</tml:page>
	<cserver:outputLayout/>
</tml:TML>