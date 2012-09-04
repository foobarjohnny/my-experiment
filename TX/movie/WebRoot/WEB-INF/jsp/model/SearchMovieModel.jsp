<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.browser.movie.Constant" %>

<tml:script language="fscript" version="1">
		<![CDATA[
		    func SearchMovie_M_getInput()
		        String saveKey = "<%=Constant.StorageKey.MOVIE_PAGE_KEYWORD%>"
				return Cache.getFromTempCache(saveKey)
		    endfunc
		    
		    func SearchMovie_M_saveInput(TxNode node)
		        String saveKey="<%=Constant.StorageKey.MOVIE_PAGE_KEYWORD%>"
			    Cache.saveToTempCache(saveKey,node)
		    endfunc
		    
			func SearchMovie_M_deleteInput()
			   String saveKey = "<%=Constant.StorageKey.MOVIE_PAGE_KEYWORD%>"
			   Cache.deleteFromTempCache(saveKey)
			endfunc
			
		]]>
</tml:script>