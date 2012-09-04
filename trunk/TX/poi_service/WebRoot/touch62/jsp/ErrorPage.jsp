<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@ include file="Header.jsp"%>	
<tml:TML outputMode="TxNode">			
	<tml:page id="ErrorPage" url="ErrorPage.jsp" type="net">
		<tml:title id="title" fontWeight="bold|system_large" align="center" fontColor="white">
			<%=msg.get("error.sorry")%>
		</tml:title>
		<tml:label id="text1" fontWeight="bold|system_large" align="center" textWrap="wrap">
			<%=msg.get("error.not.available")%>
		</tml:label>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>