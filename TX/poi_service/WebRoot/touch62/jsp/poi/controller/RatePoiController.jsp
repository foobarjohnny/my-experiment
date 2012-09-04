<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
	 <%@ include file="../model/RatePoiModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
		    func RatePoi_C_savePoiToDo(JSONObject poiJo)
		       RatePoi_M_savePoiToDo(poiJo)
		    endfunc
		    
		    func RatePoi_C_showRatePoi()
		       System.doAction("showRatePoi")
		    endfunc
		    
		    func RatePoi_C_saveBackUrl(String url)
		       RatePoi_M_saveBackUrl(url)
		    endfunc
		]]>
	</tml:script>
	<tml:menuItem name="showRatePoi" pageURL="<%=getPage + "RatePoi"%>">
	</tml:menuItem>
