<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<tml:script language="fscript" version="1">
	<![CDATA[
		func PoiSubCategory_M_getAllSubCategory()
			JSONObject jo= Cache.getJSONObjectFromCookie("POI_SUB_CATEGORY")
			return jo
		endfunc
		
		func PoiSubCategory_M_setAllSubCategory(JSONObject jo)
			Cache.saveCookie("POI_SUB_CATEGORY",jo)
		endfunc

		func PoiSubCategory_M_setCategoryId(String categoryId)
	        TxNode node
	        TxNode.addMsg(node,categoryId)
	        String saveKey = "POI_CATEGORY_ID"
	        Cache.saveToTempCache(saveKey,node)
	    endfunc
	     
	    func PoiSubCategory_M_getCategoryId()
	        String categoryId = ""
	        String saveKey = "POI_CATEGORY_ID"
	        TxNode node = Cache.getFromTempCache(saveKey)
	        if NULL != node
	           categoryId = TxNode.msgAt(node,0)
	        endif
	        return categoryId
	    endfunc
	    
	    func PoiSubCategory_M_getSubCategory()
	    	JSONObject joAll = PoiSubCategory_M_getAllSubCategory()
	    	JSONArray ja = JSONObject.get(joAll,PoiSubCategory_M_getCategoryId())
	    	return ja
	    endfunc
	]]>
</tml:script>
