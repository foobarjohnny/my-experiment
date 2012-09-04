<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>	
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioUtil"%>
<%@page import="java.util.List"%>
<% 
	DataHandler handler = (DataHandler)request.getAttribute("DataHandler");
%>
<tml:TML outputMode="TxNode">
	<%@ include file="Search3Common.jsp"%>
	<tml:menuItem name="search1" pageURL=""/>
	<tml:menuItem name="cancel"  onClick="cancelAction" trigger="TRACKBALL_CLICK"/>	
	<tml:page id="SpeakFlow3" url="<%=getDsrPage + "SpeakFlow3"%>" background="" type="<%=pageType%>" 
		 genericMenu="4" supportback="false" groupId="<%=GROUP_ID_DSR%>">
		<%
			handler.toXML(out);
		%>
		<tml:title id="searchLabel" align="center|middle" fontColor="white" fontWeight="bold|system_large">
			<%=msg.get("dsr.didyoumean")%>
		</tml:title>
		<tml:listBox id="menuListBox" name="pageListBox:settingsList" isFocusable="true"
			hotKeyEnable="false">
		<%
			for(int i=0;i<AudioConstants.PAGE_SIZE;i++)
			{
		%>
			<tml:menuItem name="<%="goToDetailMenu" + i%>" onClick="onClickDetail">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>	
			
			<tml:listItem id="<%="item" + i%>" blurIconImage="" focusIconImage=""
				align="left|middle" isFocusable="true" fontWeight="bold|system_large" textWrap="ellipsis">
				<tml:menuRef name="<%="goToDetailMenu" + i%>"/>
			</tml:listItem>
		<%
			}
		%>
		</tml:listBox>	
		<tml:image id="titleShadow" url="" align="left|top"/>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>