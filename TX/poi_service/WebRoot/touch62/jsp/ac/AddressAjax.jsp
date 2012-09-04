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
		Cache.deleteCookie("SAVE_FLAG_FOR_REQUEST")
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
								    
								    int exactMatchStatus = 0
								    if TxNode.getValueSize(node) > 2
								       exactMatchStatus = TxNode.valueAt(node,2)
								    endif
								     
									if 1 == addressSize && 0 == exactMatchStatus
										JSONObject jo = JSONArray.get(ja,0)
										int isStreetChanged = JSONObject.get(jo,"isStreetChanged")
										int isCityChanged = JSONObject.get(jo,"isCityChanged")
										String alertMsg = ""
										String addressStr = AddressCapture_M_getNavAddressNode()
										String  address = ""
										JSONObject addressJo 
										if "" != addressStr
											addressJo = JSONObject.fromString(addressStr)
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
										
										String notfound1
										String notfound2	
										String notfound3
										#PreferenceConstants.KEY_GENERAL_LANGUAGE=9
				   	                    TxNode langNode = Preference.getPreferenceValue(9)
				   	                    String language
				   	                    if NULL != langNode
				   	                        language = TxNode.msgAt(langNode,0)	
				   	                    endif		      		
				   	                    println("------------language="+language)
				   	                    if language == "fr_CA"
				   	                        notfound1 = " est introuvable. Souhaitez-vous utiliser "
				   	                        notfound2 = " à la place? "
										elsif language == "es_MX"
											notfound1 = "No se encontró "
											notfound2 = ". ¿Le gustaría usar "
				   	                        notfound3 = " en su lugar? "
				   	                    else
				   	                        notfound1 = " was not found. Would you like to use "
				   	                        notfound2 = " instead?"
				   	                    endif
										println("notfound1="+notfound1)
										println("notfound2="+notfound2)
										
										if 0 != geoCodeStatusCode
											AddressCapture_M_saveAddressJSONForSearch(jo)
											if 1 == isStreetChanged && 1 == isCityChanged
											   String newFirstLine = JSONObject.get(jo,"firstLine")
											   String newLastLine = JSONObject.get(jo,"city") + ", " + JSONObject.get(jo,"state")
											   
											   String oldLastLine = JSONObject.get(addressJo,"lastLine")
											   String oldFirstLine = address
											   
											   String tempOldLastLine = oldLastLine
											   String tempNewLastLine = ""
											   if newFirstLine != ""
											   	tempNewLastLine = newFirstLine + ", " 
											   endif
											   tempNewLastLine = tempNewLastLine + newLastLine
											   if tempOldLastLine == ""
											   		tempOldLastLine = oldFirstLine
											   else
											   		tempOldLastLine = oldFirstLine + ", " + oldLastLine	
											   endif
											   if language == "es_MX"
													alertMsg = notfound1 + "\"" + tempOldLastLine + "\"" + notfound2 +"\"" + tempNewLastLine + "\"" + notfound3
											   else 
													alertMsg = "\"" + tempOldLastLine + "\"" + notfound1 +"\"" + tempNewLastLine + "\"" + notfound2
											   endif
											   System.showConfirm(alertMsg,"<%=msg.get("common.button.Yes")%>","<%=msg.get("common.button.No")%>","returnAddressToInvokerPage")
											   return FAIL											
											elsif 1 == isStreetChanged
											   String newAddress = JSONObject.get(jo,"firstLine")
											   if language == "es_MX"
													alertMsg = notfound1 + "\"" + address + "\"" + notfound2 +"\"" + newAddress + "\"" + notfound3
											   else 
													alertMsg = "\"" + address + "\"" + notfound1 +"\"" + newAddress + "\"" + notfound2
											   endif
											   System.showConfirm(alertMsg,"<%=msg.get("common.button.Yes")%>","<%=msg.get("common.button.No")%>","returnAddressToInvokerPage")
											   return FAIL
											elsif 1 == isCityChanged
											   String newFirstLine = JSONObject.get(jo,"firstLine")
											   String newLastLine = JSONObject.get(jo,"city") + ", " + JSONObject.get(jo,"state")
											   
											   String oldLastLine = JSONObject.get(addressJo,"lastLine")
											   String oldFirstLine = address
											   
											   String tempOldLastLine = oldLastLine
											   String tempNewLastLine = newLastLine
											   if tempOldLastLine == ""
											   		tempOldLastLine = oldFirstLine
											   		if newFirstLine != ""
											   			tempNewLastLine = newFirstLine + ", "
											   		endif
											   		tempNewLastLine = tempNewLastLine + newLastLine
											   endif
											   if language == "es_MX"
													alertMsg = notfound1 + "\"" + tempOldLastLine + "\"" + notfound2 +"\"" + tempNewLastLine + "\"" + notfound3
											   else
													alertMsg = "\"" + tempOldLastLine + "\"" + notfound1 +"\"" + tempNewLastLine + "\"" + notfound2
											   endif
											   System.showConfirm(alertMsg,"<%=msg.get("common.button.Yes")%>","<%=msg.get("common.button.No")%>","returnAddressToInvokerPage")
											   return FAIL
											endif
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
		if "" != addressStrAlert
		   JSONObject addressJoAlert = JSONObject.fromString(addressStrAlert)
		   if NULL != JSONObject.get(addressJoAlert,"firstLine")
		       addressAlert = JSONObject.get(addressJoAlert,"firstLine")
		       #if NULL != JSONObject.get(addressJoAlert,"lastLine")
		       String lastString = JSONObject.get(addressJoAlert,"lastLine")
		       if NULL != lastString && "" != lastString
					if "" != addressAlert
						addressAlert = addressAlert + ", " +  lastString
					else
						addressAlert = lastString
					endif
		       endif
		   endif
		endif
		if "" != addressAlert
		   msgAlert = "\"" + addressAlert + "\" " + "<%=msg.get("ac.no.valid.address")%>"
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