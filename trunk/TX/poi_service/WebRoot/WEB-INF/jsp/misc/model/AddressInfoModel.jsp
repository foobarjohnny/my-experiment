<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@page import="com.telenav.cserver.util.TnConstants"%>
<tml:script language="fscript" version="1">
<![CDATA[
	func AddressInfo_M_getAddressList()
		JSONArray ja= Cache.getJSONArrayFromCookie("<%=TnConstants.StorageKey.SENTADDRESS_DATA%>")
		return ja
	endfunc
	
	func AddressInfo_M_setAddressList(JSONArray ja)
		Cache.saveCookie("<%=TnConstants.StorageKey.SENTADDRESS_DATA%>",ja)
	endfunc
		
	func AddressInfo_M_getIndex()
		TxNode indexNode = Cache.getFromTempCache("<%=TnConstants.StorageKey.COMMON_DATA_INDEX%>")
		int indexClicked = TxNode.valueAt(indexNode,0)
		return indexClicked
	endfunc
	
	func AddressInfo_M_setIndex(TxNode node)
		Cache.saveToTempCache("<%=TnConstants.StorageKey.COMMON_DATA_INDEX%>",node)
	endfunc			
]]>
</tml:script>


