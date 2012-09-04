<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<tml:script language="fscript" version="1">
	<![CDATA[
	    func ProductTour_M_savePageIndex(int index)
	        TxNode pageIndexNode
	        TxNode.addValue(pageIndexNode,index)
	        
	        String saveKey = "<%=Constant.StorageKey.PRODUCT_TOUR_PAGE_INDEX%>"
			Cache.saveToTempCache(saveKey,pageIndexNode)
	    endfunc
	    
	    func ProductTour_M_getPageIndex()
	        String saveKey = "<%=Constant.StorageKey.PRODUCT_TOUR_PAGE_INDEX%>"
			TxNode pageIndexNode = Cache.getFromTempCache(saveKey)
			
			int index = 0
			if NULL != pageIndexNode
			   index = TxNode.valueAt(pageIndexNode,0)
			endif
			
			return index
	    endfunc
	]]>
</tml:script>