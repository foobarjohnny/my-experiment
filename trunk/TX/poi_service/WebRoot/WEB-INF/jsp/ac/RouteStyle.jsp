<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../Header.jsp"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page
	import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>

<%
	String[] styles = { msg.get("ac.pref.fastest"),
			msg.get("ac.pref.shortest"), msg.get("ac.pref.streets"),
			msg.get("ac.pref.highways"), msg.get("ac.pref.pedestrian") };
	int[] values = { PreferenceConstants.VALUE_ROUTESTYLE_FASTEST,
			PreferenceConstants.VALUE_ROUTESTYLE_SHORTEST,
			PreferenceConstants.VALUE_ROUTESTYLE_STREET,
			PreferenceConstants.VALUE_ROUTESTYLE_HIGHWAY,
			PreferenceConstants.VALUE_ROUTESTYLE_PEDESTRIAN };
%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/RouteStyleModel.jsp"%>
	<%@ include file="model/EditRouteModel.jsp"%>

	<tml:script language="fscript" version="1">
		<![CDATA[
		    <%@ include file="../GetServerDriven.jsp"%>
			func changestyle()
				TxNode indexNode = ParameterSet.getParam("index")
				int index = TxNode.valueAt(indexNode,0)
				TxNode valueNode = ParameterSet.getParam("value")
				
				String value= TxNode.msgAt(valueNode,0)
				
				JSONObject routeStyle
				
				JSONObject.put(routeStyle,"index",index)
				JSONObject.put(routeStyle,"value",value)
				if 0 == ServerDriven_CanAvoid()
				    JSONObject route=getJSONRoute()
					JSONObject.put(route,"style",routeStyle)
					System.doAction("editRoute")
				elsif index==<%=PreferenceConstants.VALUE_ROUTESTYLE_PEDESTRIAN%>
					JSONObject route=getJSONRoute()
					JSONObject.put(route,"style",routeStyle)
					System.doAction("editRoute")
				else
					saveRouteStyle(routeStyle)
					System.doAction("avoidRoute")
				endif
				return FAIL
			endfunc
			
			
			func preLoad()
				
				JSONObject route  = getJSONRoute()
				JSONObject style = JSONObject.get(route,"style")
				int index = JSONObject.get(style,"index")
				Page.setControlProperty("item_"+index,"focused","true")
				
			endfunc
		]]>
	</tml:script>

	<tml:menuItem name="editRoute" pageURL="<%=getPage+"EditRoute"%>"></tml:menuItem>
	<tml:menuItem name="avoidRoute" pageURL="<%=getPage+"AvoidRoute" %>"></tml:menuItem>

	<tml:page url="<%=getPage + "RouteStyle"%>" type="<%=pageType%>"
		showLeftArrow="true" showRightArrow="true" helpMsg="" groupId="<%=GROUP_ID_COMMOM%>">
		<tml:title fontWeight="system_large|bold" fontColor="white">
			<%=msg.get("ac.route.style.title")%>
		</tml:title>


		<%
			for (int i = 0; i < styles.length; ++i) {
		%>
		<tml:menuItem name="<%=i+"_clicked" %>" onClick="changestyle">
			<tml:bean name="index" valueType="int"
				value="<%=String.valueOf(values[i])%>"></tml:bean>
			<tml:bean name="value" valueType="String" value="<%=styles[i]%>"></tml:bean>

		</tml:menuItem>

		<tml:urlImageLabel name="<%=i+"" %>" id="<%="item_"+values[i]%>"
			isFocusable="true" focusFontColor="white" fontWeight="system_large|bold">
			<%=styles[i]%>
			<tml:menuRef name="<%=i+"_clicked" %>" />
		</tml:urlImageLabel>
		<%
			}
		%>

	</tml:page>
	<cserver:outputLayout />
</tml:TML>