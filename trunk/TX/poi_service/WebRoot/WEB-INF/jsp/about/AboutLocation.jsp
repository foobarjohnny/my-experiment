<%@ include file="../Header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
	String message = msg.get("about.location.msg1") + "\n\n"
			+ msg.get("about.location.msg2");
%>
<tml:TML outputMode="TxNode">
	<tml:page id="AboutLocation" url="<%=getPage + "AboutLocation"%>"
		genericMenu="8" type="<%=pageType%>" helpMsg="$//$about"
		groupId="<%=GROUP_ID_MISC%>">
		<tml:title id="title" fontWeight="bold|system_large" align="center"
			fontColor="white">
			<%=TnUtil.amend(msg.get("about.location"))%>
		</tml:title>
		<tml:panel id="panel" layout="vertical">
			<tml:multiline id="textLabel" fontWeight="system"
				align="center|middle" heightAutoScale="true" isFocusable="true">
				<%=TnUtil.amend(message)%>
			</tml:multiline>
		</tml:panel>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
