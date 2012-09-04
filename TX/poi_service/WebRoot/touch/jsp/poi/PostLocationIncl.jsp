<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<%@ include file="../Header.jsp"%>
<jsp:include page="/touch/jsp/common/postlocation/controller/PostLocationController.jsp" />
	
	<tml:menuItem name="postLocationMI" text="<%=msg.get("apps.PostLocation")%>"
		trigger="KEY_MENU|TRACKBALL_CLICK"
		onClick="PostLocationOnClick">
	</tml:menuItem>
	
	<tml:script language="fscript" version="1">
			<![CDATA[
				func PostLocationOnClick()
					JSONArray poiJsonArray = ShowDetail_M_getAddressList()
					int index = ShowDetail_M_getIndex()
					JSONObject locationJo = JSONArray.get(poiJsonArray,index)
					println("#### GOING TO POST_LOCATION=" + locationJo)
					PostLocation_C_post(locationJo)
				endfunc
			]]>
	</tml:script>
