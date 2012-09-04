<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ include file="../Header.jsp"%>

<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>
<%@page import="com.telenav.cserver.stat.*"%>
<%
	String deviceModel = handlerGloble.getClientInfo(DataHandler.KEY_DEVICEMODEL);
	//how many items are in the list view including both sponsored listings and natural POIs
	// Applicable only for the list view
	int poisOnScreen = 6; // default for 9000
	if("8900".equals(deviceModel)){
		poisOnScreen = 7;
	}
	
%>
<tml:script language="fscript" version="1">
		<![CDATA[
			func Logger_logPOI_JSON(int eventType, String pageName, JSONObject poi, int poiType, int indexOnPage)
		   		String poiId = JSONObject.getString(poi, "poiId")
		   		String adId
		   		String adSource
	   			JSONObject ad = JSONObject.get(poi, "ad")
	   			if ad != NULL
	   				if JSONObject.has(ad, "adID")
	   					adId = JSONObject.getString(ad, "adID")
	   					adSource = JSONObject.getString(ad, "adSource")
	   				endif 
	   			endif
	   			
				JSONObject params
				JSONObject.put(params, "<%=AttributeID.POI_ID%>", poiId)
				JSONObject.put(params, "<%=AttributeID.POI_TYPE%>", poiType)
				if adId != NULL && adId != ""
					JSONObject.put(params, "<%=AttributeID.ADS_ID%>", adId)
   					if poiType == <%=AttributeValues.POI_TYPE_NORMAL%>
						JSONObject.put(params, "<%=AttributeID.POI_TYPE%>", <%=AttributeValues.POI_TYPE_WITH_ADD%>)
   					endif
				endif
				if adSource != NULL && adSource !=""
					JSONObject.put(params, "<%=AttributeID.AD_SOURCE%>", adSource)
				endif
				JSONObject.put(params, "<%=AttributeID.SEARCH_UID%>", <%=PoiListModel.getSearchUID()%>)
				JSONObject.put(params, "<%=AttributeID.PAGE_NAME%>", pageName)
				JSONObject.put(params, "<%=AttributeID.PAGE_NUMBER%>", <%=PoiListModel.getPageIndex()%>)
				JSONObject.put(params, "<%=AttributeID.PAGE_INDEX%>", indexOnPage)
				StatLogger.logEvent(eventType, params)
			endfunc
		]]>
</tml:script>