<%@ page language="java" pageEncoding="UTF-8"%>

<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="/touch/jsp/Header.jsp"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%
    Object obj = request.getAttribute("errorMsg");
    String errorMsg = "";
    if (obj != null) {
        errorMsg = obj.toString();
    }
%>

<tml:TML outputMode="TxNode">
    <tml:script language="fscript" version="1">
		<![CDATA[
		    func onLoad()
		        String errorKey = "<%=Constant.StorageKey.POI_SHOW_ERRORMSG%>"
		        TxNode nofoundFlag
			    TxNode.addMsg(nofoundFlag,"<%=msg.get("common.internal.error")%>")
			    Cache.saveCookie(errorKey,nofoundFlag)
		        System.doAction("notFound")
		    endfunc
		]]>
	</tml:script>
	
	<tml:menuItem name="notFound" pageURL="<%=getPage + "SearchPoi"%>">
	</tml:menuItem>
	
	<tml:page id="errorMsgPage"
		url="<%=host + "/errorMsgPage.jsp"%>"
		type="net" showLeftArrow="true" showRightArrow="true" helpMsg="" x="0"
		y="0" width="480" height="320">

	</tml:page>
</tml:TML>