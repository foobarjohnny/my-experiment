<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<tml:script language="fscript" version="1">
	<![CDATA[
		func CreateFavorites_M_saveCreateFavoriteReturnUrl(String url)
		    TxNode urlNode
		    TxNode.addMsg(urlNode,url)
		    
		    String saveKey = "<%=Constant.StorageKey.CREATE_FAVORITE_RETURN_URL%>"
		    Cache.saveToTempCache(saveKey,urlNode)
		endfunc        

		func CreateFavorites_M_getCreateFavoriteReturnUrl()
		    String saveKey = "<%=Constant.StorageKey.CREATE_FAVORITE_RETURN_URL%>"
		    TxNode urlNode = Cache.getFromTempCache(saveKey)
		    String url = ""
		    if NULL != urlNode
		       url = TxNode.msgAt(urlNode,0)
		    endif
		    return url
		endfunc

		func CreateFavorites_M_isDoingCreateFavorites()
			int doing = 0
	        TxNode node = Cache.getFromTempCache("<%=Constant.StorageKey.CREATE_FAV_DOING_STATUS%>")
	        if NULL != node
	            doing = TxNode.valueAt(node,0)
	        endif
	        return doing
		endfunc
		
		func CreateFavorites_M_setDoingCreateFavorites(int showing)
			TxNode node
		    TxNode.addValue(node,showing)
		    Cache.saveToTempCache("<%=Constant.StorageKey.CREATE_FAV_DOING_STATUS%>",node)
		endfunc

		func CreateFavorites_M_saveAddressForCreateOrEdit(String createOrEdit)
		    TxNode createOrEditNode
		    TxNode.addMsg(createOrEditNode,createOrEdit)
		    String saveKey = "<%=Constant.StorageKey.CREATE_OR_EDIT_FAVORITE%>"
		    Cache.saveToTempCache(saveKey,createOrEditNode)
		endfunc
		
		func CreateFavorites_M_getAddressForCreateOrEdit()
		    String saveKey = "<%=Constant.StorageKey.CREATE_OR_EDIT_FAVORITE%>"
		    TxNode createOrEditNode = Cache.getFromTempCache(saveKey)
		    String createOrEdit = "create"
		    if NULL != createOrEditNode
		        createOrEdit = TxNode.msgAt(createOrEditNode,0)
		    endif
		    return createOrEdit
		endfunc
		
		func CreateFavorites_M_saveCanReturnToEditFavorites(int flag)
		    TxNode flagNode
		    TxNode.addValue(flagNode,flag)
		    String saveKey = "<%=Constant.StorageKey.CAN_RETURN_TO_EDITFAVORITE%>"
		    Cache.saveToTempCache(saveKey,flagNode)
		endfunc
		
		func CreateFavorites_M_getCanReturnToEditFavorites()
		    String saveKey = "<%=Constant.StorageKey.CAN_RETURN_TO_EDITFAVORITE%>"
		    TxNode flagNode = Cache.getFromTempCache(saveKey)
		    int flag = 1
		    if NULL != flagNode
		        flag = TxNode.valueAt(flagNode,0)
		    endif
		    return flag
		endfunc
		
		func CreateFavorites_M_deleteCanReturnToEditFavorites()
		    String saveKey = "<%=Constant.StorageKey.CAN_RETURN_TO_EDITFAVORITE%>"
		    Cache.deleteFromTempCache(saveKey)
		endfunc
		]]>
</tml:script>

