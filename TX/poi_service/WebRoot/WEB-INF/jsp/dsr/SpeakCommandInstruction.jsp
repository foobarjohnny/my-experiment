<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>	
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<% 
    String backgroundUrl = imageUrl + "backgroud_no_title.png";
%>
<tml:TML outputMode="TxNode">
	<tml:script language="fscript" version="1">
		<![CDATA[
			func onBack()
				System.doAction("saycommand")
				return FAIL
			endfunc
		]]>
	</tml:script>		
	<tml:menuItem name="saycommand"  pageURL="<%=getDsrPage + "SpeakCommand1"%>" trigger="TRACKBALL_CLICK"/>
	<tml:page id="SpeakCommandInstruction" url="<%=getPage + "SpeakCommandInstruction"%>" type="<%=pageType%>" helpMsg="$//$examples" groupId="<%=GROUP_ID_DSR%>">
		<tml:title id="title" align="center|middle"  fontColor="white" fontWeight="bold|system_large">
			<%=msg.get("dsr.inst.title")%>
		</tml:title>
		<tml:tabContainer id="container" style="horizontal" defaultFocus="0">			
			<tml:tab id="tab1" label="<%=msg.get("dsr.inst.tab1")%>" fontWeight="system_medium|bold">
				<tml:label id="label11" fontWeight="system_medium" align="left|top" textWrap="wrap">
				<%=PoiUtil.amend(msg.get("dsr.inst.label11"))%>
				</tml:label>
				<tml:label id="label12" fontWeight="system_medium" align="left|top" textWrap="wrap">
				<%=PoiUtil.amend(msg.get("dsr.inst.label12"))%>
				</tml:label>
				<tml:label id="label13" fontWeight="system_medium" align="left|top" textWrap="wrap">
				<%=PoiUtil.amend(msg.get("dsr.inst.label13"))%>
				</tml:label>		
				<tml:label id="label14" fontWeight="system_medium" align="left|top" textWrap="wrap">
				<%=PoiUtil.amend(msg.get("dsr.inst.label14"))%>
				</tml:label>
				<tml:label id="label15" fontWeight="system_medium" align="left|top" textWrap="wrap">
				<%=PoiUtil.amend(msg.get("dsr.inst.label15"))%>
				</tml:label>
				<tml:label id="label16" fontWeight="system_medium" align="left|top" textWrap="wrap">
				<%=PoiUtil.amend(msg.get("dsr.inst.label16"))%>
				</tml:label>
			</tml:tab>
			<tml:tab id="tab2" label="<%=msg.get("dsr.inst.tab2")%>" fontWeight="system_medium|bold">
				<tml:label id="label21" fontWeight="system_medium" align="left|top" textWrap="wrap">
				<%=PoiUtil.amend(msg.get("dsr.inst.label21"))%>
				</tml:label>
				<tml:label id="label22" fontWeight="system_medium" align="left|top" textWrap="wrap">
				<%=PoiUtil.amend(msg.get("dsr.inst.label22"))%>
				</tml:label>
			</tml:tab>
			<tml:tab id="tab3" label="<%=msg.get("dsr.inst.tab3")%>" fontWeight="system_medium|bold">
				<tml:label id="label31" fontWeight="system_medium" align="left|top" textWrap="wrap">
				<%=PoiUtil.amend(msg.get("dsr.inst.label31"))%>
				</tml:label>
				<tml:label id="label32" fontWeight="system_medium" align="left|top" textWrap="wrap">
				<%=PoiUtil.amend(msg.get("dsr.inst.label32"))%>
				</tml:label>
				<tml:label id="label33" fontWeight="system_medium" align="left|top" textWrap="wrap">
				<%=PoiUtil.amend(msg.get("dsr.inst.label33"))%>
				</tml:label>
				<tml:label id="label34" fontWeight="system_medium" align="left|top" textWrap="wrap">
				<%=PoiUtil.amend(msg.get("dsr.inst.label34"))%>
				</tml:label>
			</tml:tab>
		</tml:tabContainer>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>