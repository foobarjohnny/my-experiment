<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<tml:script language="fscript" version="1" feature="<%=FeatureConstant.UGC_VIEW%>">
		<![CDATA[
              func ShowReviews_M_saveReviewsList(JSONArray reviewDetailArray)
				  String saveKey = "<%=Constant.StorageKeyForJSON.POI_REVIEWS_LIST%>"
				  Cache.saveToTempCache(saveKey,reviewDetailArray)
              endfunc 
              
              func ShowReviews_M_getReviewsList()
				  String saveKey = "<%=Constant.StorageKeyForJSON.POI_REVIEWS_LIST%>"
				  JSONArray reviewDetailArray = Cache.getJSONArrayFromTempCache(saveKey)
				  return reviewDetailArray
              endfunc
              
              func ShowReviews_M_saveIndex(int index)
                  TxNode indexNode
				  TxNode.addValue(indexNode, index)
                  String saveKey = "<%=Constant.StorageKey.POI_REVIEWS_INDEX%>"
				  Cache.saveToTempCache(saveKey,indexNode)
              endfunc 
              
              func ShowReviews_M_getIndex()
                  int index = 0
                  String saveKey = "<%=Constant.StorageKey.POI_REVIEWS_INDEX%>"
                  TxNode indexNode
                  indexNode = Cache.getFromTempCache(saveKey)
                  if NULL != indexNode
                      index = TxNode.valueAt(indexNode,0)
                  endif
                  return index
              endfunc
		]]>
</tml:script>