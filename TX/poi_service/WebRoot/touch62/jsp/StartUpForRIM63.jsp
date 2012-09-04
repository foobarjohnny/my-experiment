<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="Header.jsp"%>

<%
    String pageURL = host + "/startUp.do?pageRegion=" + region;
%>
<tml:TML outputMode="TxNode">
	<jsp:include page="/touch62/jsp/local_service/controller/MapWrapController.jsp" />
	<tml:script language="fscript" version="1">	
		<![CDATA[
			func onLoad()
				MapWrap_C_showCurrent()
				return FAIL
			endfunc
		]]>

	</tml:script>
	
	<tml:page url="<%=pageURL%>" type="<%=pageType%>"
		showLeftArrow="true" showRightArrow="true" helpMsg="" groupId="<%=GROUP_ID_COMMOM%>">
	</tml:page>
</tml:TML>
