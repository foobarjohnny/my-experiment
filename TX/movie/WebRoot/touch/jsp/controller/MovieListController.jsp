<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ include file="../Header.jsp"%>

<tml:script language="fscript" version="1">
		<![CDATA[
			# sortByName = 1, sortByRating = 0
			func MovieList_C_searchMovieInterface(TxNode inputString, JSONObject address, TxNode dateIndex, int sortByName)
				MovieList_M_saveAjaxParams(inputString, address, dateIndex)
				int batchNumber = 1
				MovieList_M_searchMovieWithAjax(batchNumber, sortByName)
			endfunc
			
			func MovieList_C_searchMovieWithAjax(int sortByName)
				int batchNumber = 1
				MovieList_M_searchMovieWithAjax(batchNumber, sortByName)
			endfunc
			
			func MovieList_C_searchMovieNextBatch(int batchNumber, int sortByName)
				MovieList_M_searchMovieWithAjax(batchNumber, sortByName)
			endfunc
		
		    func MovieList_C_showMovieList(int itemsOnScreen)
				TxNode itemsOnScreenNode
				TxNode.addValue(itemsOnScreenNode,itemsOnScreen)
				MenuItem.setBean("showMovieList", "itemsOnScreen", itemsOnScreenNode)

				TxNode tmpNode
				JSONObject add = Movie_M_getAddress()
				int lat = JSONObject.getInt(add, "lat")
				int lon = JSONObject.getInt(add, "lon")
				tmpNode = makeIntTxNode(lat)
				MenuItem.setBean("showMovieList", "anchorLat", tmpNode)
				tmpNode = makeIntTxNode(lon)
				MenuItem.setBean("showMovieList", "anchorLon", tmpNode)
				TxNode distanceUnitNode = Preference.getPreferenceValue(1)
				if NULL != distanceUnitNode
					MenuItem.setBean("showMovieList", "distUnit", distanceUnitNode)
				endif
				
		    	System.doAction("showMovieList")
		    endfunc
		
		    func makeIntTxNode(int value)
		       	TxNode node
		       	TxNode.addValue(node, value)
		       	return node
		    endfunc
		
		]]>
</tml:script>

<tml:menuItem name="showMovieList" pageURL="<%=getPage + "MovieList"%>">
		<tml:bean name="callFunction" valueType="String" value="showMovieList" />
</tml:menuItem>