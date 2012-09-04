<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.CommonUtil"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="model/AddressCaptureModel.jsp"%>
<%@ include file="controller/SetUpHomeController.jsp"%>
<%@ include file="AddressAjax.jsp"%>
<% 
	String dCountry = CommonUtil.getDefaultCountry(region); 
%>
	<tml:script language="fscript" version="1">
		<![CDATA[
	func initAddress()
        TxNode addressNode
        TxNode otherAddressNode 
        otherAddressNode = Favorite.getAddresses()
        JSONObject homeOrOfficeAddress = SetUpHome_C_getHome()
        if NULL != homeOrOfficeAddress
           String address = ""
           if JSONObject.get(homeOrOfficeAddress,"firstLine") != NULL
              address = JSONObject.get(homeOrOfficeAddress,"firstLine")
           endif
           if "" != address
              address = address + ", " + JSONObject.get(homeOrOfficeAddress,"city") + ", " + JSONObject.get(homeOrOfficeAddress,"state") + " " + JSONObject.get(homeOrOfficeAddress,"zip")
           else
              address = JSONObject.get(homeOrOfficeAddress,"city") + ", " + JSONObject.get(homeOrOfficeAddress,"state") + " " + JSONObject.get(homeOrOfficeAddress,"zip")
           endif
           
           TxNode.addMsg(otherAddressNode,address)
        endif
        TxNode.addChild(addressNode,otherAddressNode)
        
        TxNode recentPlaceMsgNode = RecentPlaces.getAddresses()
        TxNode lastTenRecentPlace
        String recentPlace
        int i = 0
        int size = TxNode.getStringSize(recentPlaceMsgNode)
        int recentPlacesSize = <%=Constant.RECENT_PLACES_SIZE%>
        if recentPlacesSize < size
           size = recentPlacesSize
        endif
        
        while i < size
           recentPlace = TxNode.msgAt(recentPlaceMsgNode,i)
           TxNode.addMsg(lastTenRecentPlace,recentPlace)
           i = i + 1
        endwhile
        TxNode.addChild(addressNode,lastTenRecentPlace)
        Page.setFieldFilter("firstLine",addressNode)
       
        <% if (!"CN".equals(region)){%>
		TxNode nearCityNode = AddressCapture_M_setCacheCity()
		if NULL != nearCityNode
		   Page.setFieldFilter("lastLine",nearCityNode)
		endif
		AddressCapture_M_initType("TypeAddress")
		
		
		# Set country button text
		String countryStr = AddressCapture_M_getCountry()
		if "" == countryStr
			countryStr = "<%=dCountry%>"
			#AddressCapture_M_saveCountry(countryStr)
		endif
		
		if "CAN" == countryStr
		   Page.setComponentAttribute("lastLine","hint","<%=msg.get("ac.tips.lastLine.CAN")%>")
		else
		   Page.setComponentAttribute("lastLine","hint","<%=msg.get("ac.tips.lastLine.other")%>")
		endif
		
		Page.setComponentAttribute("countryButton","text",countryStr)
		<% }%>
		
		String errorMsgStr = AddressCapture_M_getErrorMsg()
		if "" != errorMsgStr
			System.showErrorMsg(errorMsgStr)
		endif			
	endfunc
	
	func fillBoxForStreet()
	    int selectIndex = Page.getControlProperty("firstLine", "selectedIndex")
	    if selectIndex == -1
	       Page.setControlProperty("lastLine","focused","true")
	       return FAIL
	    endif
	    
	    TxNode inputNode = ParameterSet.getParam("firstLine")
	    if NULL != inputNode
	       String  address = TxNode.msgAt(inputNode,0)
	       int length = String.getLength(address)
	       int index = String.find(address,0,",")
	       int nextIndex = String.find(address,index+1,",")
	       String firstLine
	       String lastLine
	       if nextIndex != -1
		       firstLine = String.at(address,0, index)
		       
		       int lastLineIndex = index + 2
		       int lastLineLength = length - index - 2
		       lastLine = String.at(address,lastLineIndex, lastLineLength)
	       else
	       	   firstLine = ""
	       	   lastLine = address
	       endif
	       
	       Page.setComponentAttribute("firstLine","text",firstLine)
	       Page.setComponentAttribute("lastLine","text",lastLine)
	       Page.setControlProperty("submitButton","focused","true")
	    endif
	endfunc
	
	func fillBoxForCity()
              Page.setControlProperty("submitButton","focused","true")
	endfunc
		
	func validateAddressOnClick()
        String firstLine = ""
		String lastLine = ""
		TxNode firstLineNode
		TxNode lastLineNode
		    
		#Street
		firstLineNode = ParameterSet.getParam("firstLine")
		lastLineNode = ParameterSet.getParam("lastLine")
		
		if NULL == firstLineNode && NULL == lastLineNode
		    System.showErrorMsg("<%=msg.get("ac.enter.address")%>")
		    Page.setControlProperty("firstLine","focused","true")
            return FAIL
		else
			firstLine = TxNode.msgAt(firstLineNode, 0)
			lastLine = TxNode.msgAt(lastLineNode, 0)
			String addressStr = firstLine + lastLine
			if "" == addressStr
			    System.showErrorMsg("<%=msg.get("ac.enter.address")%>")
			    Page.setControlProperty("firstLine","focused","true")
                return FAIL
			endif
			
			if "" == lastLine
			    System.showErrorMsg("<%=msg.get("ac.enter.city")%>")
			    Page.setControlProperty("lastLine","focused","true")
                return FAIL
			endif
		endif

		if !Cell.isCoverage()
			System.showErrorMsg("<%=msg.get("common.nocell.error")%>")
            return FAIL
		endif
						    
		String country = AddressCapture_M_getCountry()
		if "" == country
		   #country = "<%=dCountry%>"
		endif
		
		JSONObject cacheJo = AddressCapture_M_getCacheAddress()
		if NULL != cacheJo
		   JSONObject inputAddressJo = AddressCapture_M_getInputAddress()
		   if NULL != inputAddressJo
		       String cacheFirstLine = JSONObject.get(inputAddressJo,"firstLine")
			   String cacheLastLine = JSONObject.get(inputAddressJo,"lastLine")
			   String cacheCountry = JSONObject.get(inputAddressJo,"country")
			   if NULL != cacheFirstLine && NULL != cacheLastLine && NULL != cacheCountry && firstLine == cacheFirstLine && lastLine == cacheLastLine && country == cacheCountry
			      AddressCapture_M_returnAddressToInvokerPage(cacheJo)
				  return FAIL
			   endif
		   endif
		endif
		
		#check if address exist in My Favorite or Recent Place
		string inputAddress = ""
		if firstLine == ""
			inputAddress = lastLine
		else
			inputAddress = firstLine + ", " + lastLine	
		endif
		
		#if address exist in MyFavorite or Recent Place, does not do validation
		TxNode cacheAddressNode = AC.getStopByDetailLine(inputAddress)
		if cacheAddressNode != NULL && TxNode.getValueSize(cacheAddressNode) > 2
			cacheJo = convertStopToJSON(cacheAddressNode)
			AddressCapture_M_returnAddressToInvokerPage(cacheJo)
			return FAIL
		else
	        JSONObject jo
	        JSONObject.put(jo,"firstLine",firstLine)
			JSONObject.put(jo,"lastLine",lastLine)
			JSONObject.put(jo,"country",country)
			
			AddressCapture_M_saveInputAddress(jo)
			String s = JSONObject.toString(jo)
			
			doRequest(s)			
		endif
    endfunc
	]]>
</tml:script>