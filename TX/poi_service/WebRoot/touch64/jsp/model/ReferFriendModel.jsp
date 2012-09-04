<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<tml:script language="fscript" version="1">
	<![CDATA[
	    func ReferFriend_M_saveContactListNode(TxNode contactListNode)
	          String saveKey = "<%=Constant.StorageKey.REFERFRIEND_CONTACT_LIST%>"
			  Cache.saveToTempCache(saveKey,contactListNode)
	    endfunc
	    
	    func ReferFriend_M_getContactListNode()
	          String saveKey = "<%=Constant.StorageKey.REFERFRIEND_CONTACT_LIST%>"
			  TxNode contactListNode = Cache.getFromTempCache(saveKey)
			  return contactListNode
	    endfunc
	    
	    func ReferFriend_M_deleteContactListNode()
	          String saveKey = "<%=Constant.StorageKey.REFERFRIEND_CONTACT_LIST%>"
			  Cache.deleteFromTempCache(saveKey)
	    endfunc
	    
	    func ReferFriend_M_savePhoneInfoForBackShow(TxNode phoneInfoNode)
	          String saveKey = "<%=Constant.StorageKey.REFERFRIEND_PHONE_INFOR%>"
			  Cache.saveToTempCache(saveKey,phoneInfoNode)
	    endfunc
	    
	    func ReferFriend_M_getPhoneInfoForBackShow()
	         String saveKey = "<%=Constant.StorageKey.REFERFRIEND_PHONE_INFOR%>"
	         TxNode phoneInfoNode = Cache.getFromTempCache(saveKey)
	         return phoneInfoNode
	    endfunc
	    
	    func ReferFriend_M_deletePhoneInfoForBackShow()
	          String saveKey = "<%=Constant.StorageKey.REFERFRIEND_PHONE_INFOR%>"
			  Cache.deleteFromTempCache(saveKey)
	    endfunc
	]]>
</tml:script>