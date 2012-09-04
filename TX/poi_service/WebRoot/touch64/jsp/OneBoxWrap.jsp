<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ page import="com.telenav.cserver.poi.struts.Constant"%>
<%@ include file="/touch64/jsp/Header.jsp"%>	
<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>
<%@page import="com.telenav.cserver.stat.*"%>
<%
	String pageURL = getPage + "OneBoxWrap";
%>
<tml:TML outputMode="TxNode">
<%@ include file="/touch64/jsp/poi/controller/PoiListController.jsp"%>
	<jsp:include
		page="/touch64/jsp/local_service/controller/MapWrapController.jsp" />
	<jsp:include page="/touch64/jsp/controller/OneBoxController.jsp"/>
	<tml:script language="fscript" version="1">
		<![CDATA[
			func addrCallBack()
				TxNode addressNode
				addressNode=ParameterSet.getParam("returnAddress")
				String joString = TxNode.msgAt(addressNode,0)
				JSONObject jo = JSONObject.fromString(joString)
			   	if JSONObject.getInt(jo,"type")==<%=Constant.OneBox.ADDRESS_RESULT%>
			   		if needChangeSearchPoiTypeForAddress(OneBox_M_getPageFrom())
						TxNode node
						TxNode.addMsg(node,OneBox_M_getCallBackFunction())
						MenuItem.setAttribute("returnToInvokerPage","url",OneBox_M_getCallBackUrl())
						MenuItem.setBean("returnToInvokerPage","callFunction",node)
						MenuItem.setBean("returnToInvokerPage","returnAddress",addressNode)
						
						System.doAction("returnToInvokerPage")			   			
			   		else
			    		MapWrap_C_showSingleAddress(jo)
			    	endif
			    	return FAIL
			    else
			    	if 1 == ServerDriven_CanOneBoxSearch() && 1 == ServerDriven_IsCallOneBox()
			    		String inputString = JSONObject.getString(jo,"search")
				    	if NULL == inputString || "" == inputString 
				    		inputString = JSONObject.getString(jo,"firstLine")+ ", " + getLastLine(jo)
				    	endif
				    	String displayString = JSONObject.getString(jo,"display")
				    	if NULL == displayString || "" == displayString
				    		displayString = JSONObject.getString(jo,"firstLine")+ ", " + getLastLine(jo)
				    	endif
					    oneBoxSearch(inputString,displayString)
			    		return FAIL
			    	else
			    		MapWrap_C_showSingleAddress(jo)
				    	return FAIL
			    	endif
			   	endif
	        endfunc
	        
	        func getLastLine(JSONObject jo)
	        	String city = String.trim(JSONObject.getString(jo,"city"))
	        	String stateAndZip = JSONObject.getString(jo,"state") + " " + JSONObject.getString(jo,"zip")
	        	if "" != city
	        		return city + ", " + stateAndZip
	        	endif
	        	return stateAndZip        
	        endfunc
	]]>
	</tml:script>
	
	<tml:menuItem name="returnToInvokerPage" pageURL="">
	</tml:menuItem>
	<tml:page id="OneBoxWrap" url="<%=pageURL%>" type="<%=pageType%>" supportback="false">
		</tml:page>
</tml:TML>
