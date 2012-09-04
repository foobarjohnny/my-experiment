<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ include file="../Header.jsp"%>

<tml:script language="fscript" version="1">
		<![CDATA[
		
			# 1 - sort by Name, 0 - sort by Rating - TODO - add more sort types
		    func MovieList_C_getNewSortBy()
		    	return MovieList_M_getNewSortBy()
		    endfunc
		    
		    func MovieList_C_deleteNewSortBy()
				MovieList_M_deleteNewSortBy()
		    endfunc
		    
		    func MovieList_C_saveNewSortBy(String newSortBy)
				MovieList_M_saveNewSortBy(newSortBy)
		    endfunc
		
			# sortByName = 1, sortByRating = 0
			func MovieList_C_searchMovieInterface(TxNode inputString, JSONObject address, TxNode dateIndex, int sortByName, String newSortBy)
				MovieList_M_saveAjaxParams(inputString, address, dateIndex)
				int batchNumber = 1
				MovieList_M_searchMovieWithAjax(batchNumber, sortByName, newSortBy)
			endfunc
			
			func MovieList_C_searchMovieWithAjax(int sortByName, String newSortBy)
				int batchNumber = 1
				MovieList_M_searchMovieWithAjax(batchNumber, sortByName, newSortBy)
			endfunc
			
			func MovieList_C_searchMovieNextBatch(int batchNumber, int sortByName, String newSortBy)
				MovieList_M_searchMovieWithAjax(batchNumber, sortByName, newSortBy)
			endfunc
		
		    func MovieList_C_showMovieList(int itemsOnScreen)
				TxNode itemsOnScreenNode
				TxNode.addValue(itemsOnScreenNode,itemsOnScreen)
				MenuItem.setBean("showMovieList", "itemsOnScreen", itemsOnScreenNode)
		    	System.doAction("showMovieList")
		    endfunc
		
		]]>
</tml:script>

<tml:menuItem name="showMovieList" pageURL="<%=getPage + "MovieList"%>">
		<tml:bean name="callFunction" valueType="String" value="showMovieList" />
</tml:menuItem>