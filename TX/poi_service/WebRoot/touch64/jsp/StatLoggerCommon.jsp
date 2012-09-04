<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>

<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.stat.*"%>

<tml:script language="fscript" version="1">
		<![CDATA[
		    func StatLogger_CM_addAddressDetails(int stopType, JSONObject event)
				if stopType == <%=Constant.STOP_POI%>
					JSONObject.put(event, "<%=AttributeID.ADDRESS_SOURCE%>", <%=AttributeValues.AS_SEARCH_POI%>)
					JSONObject.put(event, "<%=AttributeID.ADDRESS_TYPE%>", <%=AttributeValues.AT_POI%>)
				elseif stopType == <%=Constant.STOP_GENERIC%>
					JSONObject.put(event, "<%=AttributeID.ADDRESS_SOURCE%>", <%=AttributeValues.AS_OTHER%>)
					JSONObject.put(event, "<%=AttributeID.ADDRESS_TYPE%>", <%=AttributeValues.AT_ADDRESS%>)
				elseif stopType == <%=Constant.STOP_FAVORITE%>
					JSONObject.put(event, "<%=AttributeID.ADDRESS_SOURCE%>", <%=AttributeValues.AS_FAVORITES%>)
					JSONObject.put(event, "<%=AttributeID.ADDRESS_TYPE%>", <%=AttributeValues.AT_ADDRESS%>)
				elseif stopType == <%=Constant.STOP_RECENT%>
					JSONObject.put(event, "<%=AttributeID.ADDRESS_SOURCE%>", <%=AttributeValues.AS_RECENT_PLACES%>)
					JSONObject.put(event, "<%=AttributeID.ADDRESS_TYPE%>", <%=AttributeValues.AT_ADDRESS%>)
				elseif stopType == <%=Constant.STOP_AIRPORT%>
					JSONObject.put(event, "<%=AttributeID.ADDRESS_SOURCE%>", <%=AttributeValues.AS_OTHER%>)
					JSONObject.put(event, "<%=AttributeID.ADDRESS_TYPE%>", <%=AttributeValues.AT_AIRPORT%>)
				elseif stopType == <%=Constant.STOP_CITY%>
					JSONObject.put(event, "<%=AttributeID.ADDRESS_SOURCE%>", <%=AttributeValues.AS_OTHER%>)
					JSONObject.put(event, "<%=AttributeID.ADDRESS_TYPE%>", <%=AttributeValues.AT_CITY%>)
				elseif stopType == <%=Constant.STOP_CURSOR_ADDRESS%>
					JSONObject.put(event, "<%=AttributeID.ADDRESS_SOURCE%>", <%=AttributeValues.AS_MAP%>)
					JSONObject.put(event, "<%=AttributeID.ADDRESS_TYPE%>", <%=AttributeValues.AT_ADDRESS%>)
				else
					JSONObject.put(event, "<%=AttributeID.ADDRESS_SOURCE%>", <%=AttributeValues.AS_OTHER%>)
					JSONObject.put(event, "<%=AttributeID.ADDRESS_TYPE%>", <%=AttributeValues.AT_ADDRESS%>)
				endif
		    endfunc
		]]>
</tml:script>