<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.browser.movie.Constant" %>

<tml:script language="fscript" version="1">
		<![CDATA[
		    func SelectDate_C_showDateList()
		        String saveKey="<%= Constant.StorageKeyForJSON.MOVIE_SELECT_DATE_PARAMS_JSON%>"
				Cache.deleteFromTempCache(saveKey);
		    	System.doAction("showDateList")
		    endfunc
		    
		    func SelectDate_C_showDateListInterface(String page, String callbackFunc, int theaterId, String movieId)
				JSONObject jo
			    JSONObject.put(jo,"<%= Constant.RRKey.M_PAGE_URL%>",page)
		    	checkAndPut(jo,"<%= Constant.RRKey.M_CALLBACK_FUNCTION%>",callbackFunc)
		    	JSONObject.put(jo,"<%= Constant.RRKey.M_THEATER_ID%>",theaterId)
		    	checkAndPut(jo,"<%= Constant.RRKey.M_MOVIE_ID%>",movieId)
		        String saveKey="<%= Constant.StorageKeyForJSON.MOVIE_SELECT_DATE_PARAMS_JSON%>"
			    Cache.saveToTempCache(saveKey,jo)
		    	System.doAction("showDateList")
		    endfunc
		    
		    func checkAndPut(JSONObject jo, String key, String value)
		    	if NULL != value
		    		JSONObject.put(jo,key,value)
		    	endif
		    endfunc
		    
		    func SelectDate_C_saveDate(TxNode id, TxNode labelStr)
		        String saveKey="<%= Constant.StorageKey.MOVIE_PAGE_DATE_ID%>"
			    Cache.saveToTempCache(saveKey,id)
		        saveKey="<%= Constant.StorageKey.MOVIE_PAGE_DATE_LABEL%>"
			    Cache.saveToTempCache(saveKey,labelStr)
		    endfunc
		    
		    func SelectDate_C_getDateID()
		        String saveKey="<%= Constant.StorageKey.MOVIE_PAGE_DATE_ID%>"
				TxNode id = Cache.getFromTempCache(saveKey)
				if id != NULL
					String idStr = TxNode.msgAt(id, 0)
					return idStr
				endif
				return "0"
		    endfunc
		    
		    func SelectDate_C_getDateLabel()
		        String saveKey="<%= Constant.StorageKey.MOVIE_PAGE_DATE_LABEL%>"
				TxNode label = Cache.getFromTempCache(saveKey)
				if label != NULL
					String labelStr = TxNode.msgAt(label, 0)
					return labelStr
				endif
				return NULL
		    endfunc
		]]>
</tml:script>

<tml:menuItem name="showDateList" pageURL='<%= getPage + "SelectDate"%>' />
