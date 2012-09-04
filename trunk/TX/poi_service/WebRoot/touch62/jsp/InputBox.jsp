<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
	String pageURL            = getPage + "InputBox";
	String callBackURL        = getPageCallBack + "InputBox";
%>
<tml:TML outputMode="TxNode">
	<%@ include file="/touch62/jsp/model/InputBoxModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
			func preLoad()
				Page.setComponentAttribute("title","text",InputBox_M_getTitle())
			endfunc
			
			func saveInputBox()
				String comments    = ""
		        TxNode commentNode = ParameterSet.getParam("comments")
		        if 	NULL != commentNode
					comments = TxNode.msgAt(commentNode, 0)
		        endif

				InputBox_M_saveComments(comments)
				
				MenuItem.setAttribute("callback","url",InputBox_M_getCallbackpageurl())
				TxNode node1
	        	TxNode.addMsg(node1,InputBox_M_getCallbackfunction()) 
	        	MenuItem.setBean("callback", "callFunction", node1)
	        	MenuItem.setBean("callback", "returnComments", commentNode)
	        	System.doAction("callback")
	        	return FAIL				
				
			endfunc

		]]>
	</tml:script>

	<tml:menuItem name="callback" pageURL="" trigger="TRACKBALL_CLICK">
			<tml:bean name="callFunction" valueType="string" value=""/>
	</tml:menuItem>
	<tml:menuItem name="submit" onClick="saveInputBox" trigger="TRACKBALL_CLICK" />
	<tml:page id="InputBox" url="<%=pageURL%>" type="<%=pageType%>"  groupId="<%=GROUP_ID_COMMOM%>">
		<tml:title id="title" fontWeight="system_large|bold" fontColor="white">
		</tml:title>
		<tml:inputBox id="comments" title="" titleAbove="false"
			titleFontWeight="bold|system_large" prompt="" fontWeight="system_large">
		</tml:inputBox>

		<tml:button id="submitButton" fontWeight="System_large" text="<%=msg.get("common.button.Save") %>">
    		<tml:menuRef name="submit" />
		</tml:button>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
