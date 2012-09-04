<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="/WEB-INF/jsp/model/DriveToModel.jsp"%>
<%@ include file="../StopUtil.jsp"%>
<%@ include file="../PoiUtil.jsp"%>
<jsp:include page="/WEB-INF/jsp/ac/controller/EditRouteController.jsp" />
<tml:script language="fscript" version="1">
	<![CDATA[
	    <%@ include file="../GetServerDriven.jsp"%>
	    func DriveTo_C_doNav(TxNode node,String navType,String from)
	    	DriveTo_M_saveNavType(navType)
	    	String addressString = TxNode.msgAt(node,0)
	    	JSONObject addressJO = JSONObject.fromString(addressString)
	    	DriveTo_C_saveToRencentPlaces(addressJO)
	    	
	    	if Account.isTnMaps() || 0 == ServerDriven_CanDynamicNav()
	    		String backActionForNav = DriveTo_M_getBackAction()
	    		if "" != backActionForNav
	    		   DriveTo_M_deleteBackAction()
	    		   EditRoute_C_saveBackAction(backActionForNav)
	    		endif
	    		EditRoute_C_StaticRoute(addressJO)
	    	else
		    	TxNode node1
		        TxNode.addMsg(node1,"CallBack_SelectAddress")
		        string pageUrl = "<%=getPageCallBack + "DriveToWrap"%>"
		        if "" != from
		        	pageUrl = pageUrl + "#" + from
		        endif
		        MenuItem.setAttribute("callback","url",pageUrl)
	        	MenuItem.setBean("callback", "callFunction", node1)
	        	MenuItem.setBean("callback", "returnAddress", node)
	        	System.doAction("callback")
	        	return FAIL
        	endif
	    endfunc
	    
	    func DriveTo_C_saveToRencentPlaces(JSONObject addressJO)
	        TxNode newPlaceNode
	    	if JSONObject.has(addressJO,"stop")
	    	   newPlaceNode = PoiUtil_convertToNodeForResentSearch(addressJO)
	    	else
	    	   newPlaceNode = DriveTo_M_convertStopToNodeForResentSearch(addressJO)
	    	endif
	    	RecentPlaces.saveAddress(newPlaceNode)
	    endfunc
	    
	    func DriveTo_C_saveStopType(int stopType)
	        DriveTo_M_saveStopType(stopType)
	    endfunc
	    
	    func DriveTo_C_saveBackAction(String backAction)
           DriveTo_M_saveBackAction(backAction)
        endfunc
	]]>
</tml:script>

<tml:menuItem name="callback" pageURL="" trigger="TRACKBALL_CLICK">
		<tml:bean name="callFunction" valueType="string" value=""/>
</tml:menuItem>