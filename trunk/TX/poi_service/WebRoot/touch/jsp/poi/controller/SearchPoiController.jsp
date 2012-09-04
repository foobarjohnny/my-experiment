<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
	 <%@ include file="../model/SearchPoiModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
			func SearchPoi_C_saveCategory(String name,String isMostPopular,TxNode idNode)
			    SearchPoi_M_saveCategory(name,isMostPopular,idNode)
			endfunc
			
			func SearchPoi_C_showSearch()
			    System.doAction("showSearch")
			endfunc
			
			func SearchPoi_C_initial(int searchType)
                SearchPoi_M_initial(searchType)
			endfunc

			func SearchPoi_C_searchNearLocation(JSONObject address,String inputString)
                SearchPoi_M_initial(5)
                SearchPoi_M_setInput(inputString)
                SearchPoi_M_saveLocation(address)
                System.doAction("showSearch")
			endfunc
			
			func SearchPoi_C_searchAlongInitial(TxNode searchInformationNode)
			    SearchPoi_M_setSearchInformation(searchInformationNode)
			    SearchPoi_M_initial(7)
			    SearchPoi_M_deleteSearchAlongType()
			    System.doAction("showSearch")
			endfunc
			
			func SearchPoi_C_searchForBusinessInitial(String callBackUrl,String callBackFunction)
			    SearchPoi_M_setCallBackInformation(callBackUrl,callBackFunction)
			    SearchPoi_M_initial(100)
			    string pageUrl = "<%=getPageCallBack%>" + "SearchPoi#POI"
				MenuItem.setAttribute("showSearch","url",pageUrl)
			    System.doAction("showSearch")
			endfunc
			
			func SearchPoi_C_saveBackAction(String backAction)
	           SearchPoi_M_saveBackAction(backAction)
	        endfunc
	        
	        func SearchPoi_C_getBackAction()
	           return SearchPoi_M_getBackAction()
	        endfunc
	        
	        func SearchPoi_C_deleteBackAction()
	           SearchPoi_M_deleteBackAction()
	        endfunc
		]]>
	</tml:script>
	<tml:menuItem name="showSearch" pageURL="<%=getPage + "SearchPoi"%>">
	</tml:menuItem>
