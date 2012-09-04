<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
	func doRequest(String s)
		TxNode node
		TxNode.addMsg(node,s)
		AddressCapture_M_saveNavAddressNode(node)
		TxRequest req
		string url= "<%=host + "/ValidateAddress.do"%>"
		string scriptName="stateChange"
		TxRequest.open(req,url)
		TxRequest.setRequestData(req,node)
		TxRequest.onStateChange(req,scriptName)
		TxRequest.setProgressTitle(req,"<%=msg.get("ac.validating.address")%>", "", TRUE)
		TxRequest.send(req)
	endfunc
	
	func stateChange(TxNode node,int status)
		if status == 1
			JSONArray ja
			if NULL != node
				int size = TxNode.getValueSize(node)
				if 0 < size
					int ok = TxNode.valueAt(node,0)
					if 1 == ok
						size = TxNode.getStringSize(node)
						if 0 < size
							String strJa = TxNode.msgAt(node,0)
							if NULL != strJa
								ja = JSONArray.fromString(strJa)
								int addressSize = JSONArray.length(ja)
								if 0 < addressSize
									if 1 == addressSize
										JSONObject jo = JSONArray.get(ja,0)
										int isStreetChanged = JSONObject.get(jo,"isStreetChanged")
										String alertMsg = ""
										String addressStr = AddressCapture_M_getNavAddressNode()
										String  address = ""
										JSONObject addressJo = JSONObject.fromString(addressStr)
										if "" != addressStr
										   if NULL != JSONObject.get(addressJo,"firstLine")
										       address = JSONObject.get(addressJo,"firstLine")
										   elsif  NULL != JSONObject.get(addressJo,"street1")
										       address = JSONObject.get(addressJo,"street1") + " at " + JSONObject.get(addressJo,"street2")
										   endif
										endif
										
										int geoCodeStatusCode = 0
										if NULL != TxNode.valueAt(node,1)
										   geoCodeStatusCode = TxNode.valueAt(node,1)
										endif
										
										if 1 == isStreetChanged && 0 != geoCodeStatusCode
										   AddressCapture_M_saveAddressJSONForSearch(jo)
										   String newAddress = JSONObject.get(jo,"firstLine")
										   alertMsg = "\"" + address + "\" was not found. Would you like to use \"" + newAddress + "\" instead?"
										   System.showConfirm(alertMsg,"Yes","No","returnAddressToInvokerPage")
										   return FAIL
										endif
										int isCityChanged = JSONObject.get(jo,"isCityChanged")
										if 1 == isCityChanged && 0 != geoCodeStatusCode
										   AddressCapture_M_saveAddressJSONForSearch(jo)
										   String newLastLine = JSONObject.get(jo,"city") + ", " + JSONObject.get(jo,"state")
										   String oldLastLine = JSONObject.get(addressJo,"lastLine")
										   alertMsg = "\"" + oldLastLine + "\" was not found. Would you like to use \"" + newLastLine + "\" instead?"
										   
										   System.showConfirm(alertMsg,"Yes","No","returnAddressToInvokerPage")
										   return FAIL
										endif
										AddressCapture_M_saveCacheAddress(jo)
										AddressCapture_M_returnAddressToInvokerPage(jo)
										return FAIL
									else
										MenuItem.setBean("showAddress","addressList",node)
										string from = Page.getControlProperty("page","url_flag")
										string pageUrl = "<%=getPageCallBack%>" + "AddressList#" + from
										MenuItem.setAttribute("showAddress","url",pageUrl)
										System.doAction("showAddress")
										return FAIL
									endif
								endif
							endif
						endif
					endif
				endif
			endif
			
			showErrorMsgForNoFound()
		elsif status == 0
			System.showErrorMsg("<%=msg.get("common.internal.error")%>")
			return FAIL
		endif
	endfunc
	
	func showErrorMsgForNoFound()
	    String addressStrAlert = AddressCapture_M_getNavAddressNode()
		String msgAlert = ""
		String  addressAlert = ""
		String  firstLine = ""
		String  lastLine = ""
		if "" != addressStrAlert
		   JSONObject addressJoAlert = JSONObject.fromString(addressStrAlert)
		   if NULL != JSONObject.get(addressJoAlert,"firstLine")
		       firstLine = String.trim(JSONObject.get(addressJoAlert,"firstLine"))
		       if NULL != JSONObject.get(addressJoAlert,"lastLine")
		           lastLine = String.trim(JSONObject.get(addressJoAlert,"lastLine"))
		       endif

		       # "firstLine" = "" , "lastLine" = ", 94046"		       		       
	           if (1 < String.getLength(lastLine))
	               if ("," == String.at(lastLine, 0, 1))
	                   if ("" != firstLine)
	                       addressAlert = firstLine + lastLine
	                   else
	                       addressAlert = String.at(lastLine, 1, String.getLength(lastLine)-1)
	                   endif
	               elsif ("," == String.at(lastLine, String.getLength(lastLine)-1, 1))
	                   if ("" != firstLine)
	                       addressAlert = firstLine + "," + String.at(lastLine, 0, String.getLength(lastLine)-1)
	                   else
	                       addressAlert = String.at(lastLine, 0, String.getLength(lastLine)-1)
	                   endif
	               else
	                   if ("" != firstLine)
	                       addressAlert = firstLine + "," + lastLine
	                   else
	                       addressAlert = lastLine
	                   endif
	               endif		               
	           elsif ((1 == String.getLength(lastLine)) && "," != lastLine)
                   if ("" != firstLine)
                       addressAlert = firstLine + "," + lastLine
                   else
                       addressAlert = lastLine
                   endif
	           elsif ((1 == String.getLength(lastLine)) && "," == lastLine)
                   if ("" != firstLine)
                       addressAlert = firstLine
                   else
                       addressAlert = ""
                   endif
	           endif
		   endif
		   addressAlert = String.trim(addressAlert)
		endif
		if "" != addressAlert
		   msgAlert = "\"" + addressAlert + "\"" + "<%=msg.get("ac.no.valid.address")%>"
		else
		   msgAlert = "<%=msg.get("ac.no.valid.city")%>"
		endif
		System.showErrorMsg(msgAlert)
		return FAIL
	endfunc
	
	func returnAddressToInvokerPage(int selected)
	    if 1 == selected
	       JSONObject jo = AddressCapture_M_getAddressJSONForSearch()
	       AddressCapture_M_saveCacheAddress(jo)
	       AddressCapture_M_returnAddressToInvokerPage(jo)
	    else
	       AddressCapture_M_deleteCacheAddress()
	       AddressCapture_M_deleteInputAddress()
	    endif
	    return FAIL
	endfunc
	]]>
</tml:script>
