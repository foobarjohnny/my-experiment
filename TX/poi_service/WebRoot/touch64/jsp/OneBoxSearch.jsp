<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="Header.jsp"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%
    String pageURL = getPageCallBack + "OneBoxSearch";
    int gpsValidTime = 240;
	int cellIdValidTime=480;
	int gpsTimeout=12;
%>
<tml:TML outputMode="TxNode">
	<%@ include file="/touch64/jsp/local_service/GetGps.jsp"%>
	<%@ include file="/touch64/jsp/poi/controller/PoiListController.jsp"%>
	<jsp:include page="/touch64/jsp/controller/OneBoxController.jsp"/>
	<jsp:include page="/touch64/jsp/ac/controller/SelectAddressController.jsp" />
	<tml:script language="fscript" version="1">
		<![CDATA[
		<%@ include file="GetServerDriven.jsp"%>
			func preLoad()
				Page.setComponentAttribute("oneBox","style", "nosuggest")
			endfunc
			
			func onLoad()
				OneBox_M_initialForPOI()
				Page.setComponentAttribute("oneBox","focused","true")
				
				TxNode inputNode = ShareData.get("<%=Constant.StorageKey.BROWSER_SHARE_OBJECT_INPUT_CHAR%>")
				println("~~~~~~~~~~~~~~~inputNode~~~~~~~~"+inputNode)
				if NULL != inputNode
					String s = TxNode.msgAt(inputNode,0)
					if NULL != s && "" != s
						Page.setComponentAttribute("oneBox","text", s)
						ShareData.delete("<%=Constant.StorageKey.BROWSER_SHARE_OBJECT_INPUT_CHAR%>")
					endif
				endif
				loadLocalList()
			endfunc

			func onShow()
			    String transactionId = Time.get()+""
			    OneBox_M_saveTransactionId(transactionId)
			    
			    String inputString = <%=PoiListModel.getKeyWord()%>
			    String displayString = ""
				if "1" == SearchPoi_M_getFlowFlag()
					SearchPoi_M_saveFlowFlag("0")
					Page.setComponentAttribute("oneBox","text",inputString)
				    println(inputString)
					if 1 == ServerDriven_CanOneBoxSearch()
		    	    	oneBoxSearch(inputString,displayString)
		    		else
		    	    	oneBoxSearchOnlyPOI(inputString,displayString)
		    		endif
				elsif "2" == SearchPoi_M_getFlowFlag()
					SearchPoi_M_saveFlowFlag("0")
					Page.setComponentAttribute("oneBox","text",inputString)
				    println(inputString)
					if 1 == ServerDriven_CanOneBoxSearch()
		    	    	oneBoxSearch(inputString,displayString)
		    		else
		    	    	oneBoxSearchOnlyPOI(inputString,displayString)
		    		endif
				endif
		    endfunc

		    func onClickOneBoxSearch()
		    	String inputString = ""
		    	String displayString = ""	
		    	
		    	TxNode inputNode = ParameterSet.getParam("oneBox")
				if NULL == inputNode || "" == TxNode.msgAt(inputNode, 0)
				    System.showErrorMsg("<%=msg.get("onebox.enter.required")%>")
		            return FAIL
		        else
		        	displayString = TxNode.msgAt(inputNode, 0)
		        	if	TxNode.getStringSize(inputNode) > 1
		        		inputString = TxNode.msgAt(inputNode, 1)
		        	else
		        		inputString = displayString
		        	endif   
				endif
				Page.setComponentAttribute("oneBox","focused","false")
				Page.preLoadPageToCache("<%=getPageCallBack + "PoiList"%>")
				
				println(inputString)
				if 1 == ServerDriven_CanOneBoxSearch()
		    	    oneBoxSearch(inputString,displayString)
		    	else
		    	    oneBoxSearchOnlyPOI(inputString,displayString)
		    	endif
	    	endfunc
	    	
	    	func oneBoxSearchOnlyPOI(String inputString,String displayString)
	    	    #Do some initial work for result
				PoiList_C_deleteSortTypeTemp()
		        PoiList_C_deleteBannerAdsList()
		            
	    	    String categoryId = "-1"
	    	    <%=PoiListModel.setCategoryId("categoryId")%>
	    	    <%=PoiListModel.setKeyWord("inputString")%>	  
	    	    <%=PoiListModel.setPageIndexTemp("0")%> 
	    	    
				int searchType = <%=PoiListModel.getSearchType()%>
		            
		        #searchType=7: search along
		        println("searchType="+searchType)
		        if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
		            doSearchWithAjax(inputString,displayString)
		        else
		            JSONObject addressJO = SearchPoi_M_getLocation()
				    if NULL == addressJO
				        println("addressJO is NULL start to get GPS")
						doGetGPSForPoi()
					else
						int lon = JSONObject.getInt(addressJO,"lon")
						if 0 == lon
						   doGetGPSForPoi()
				           println("0 == lon start to get GPS")
						else
						   doSearchWithAjax(inputString,displayString)
						endif
					endif
		        endif
		    endfunc
		    
		    func doGetGPSForPoi()
			   	getCurrentLocationWithoutBlocking(<%=Constant.CurrentLocation.CURRENT_LOCATION%>,<%=gpsValidTime%>,<%=cellIdValidTime%>,<%=gpsTimeout%>)		        
		    endfunc
		    
		    # Back from "getGPS"
	        func setCurrentLocation(JSONObject jo)
        		SearchPoi_M_saveLocation(jo)
			    String inputString = <%=PoiListModel.getKeyWord()%>
			    String displayString = ""
        		doSearchWithAjax(inputString,displayString)
	        endfunc
	    	
	    	func doSearchWithAjax(String inputString,String displayString)
	    	    println("doSearchWithAjax="+inputString) 
	    	    
	    	    #searchType=5 means search poi, searchType=7 means search along. So audio types are different 
				int searchType = <%=PoiListModel.getSearchType()%>
			    if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
			        int audioType = <%=PoiListModel.AudioType.POI_TYPEIN_ALONG%>
				    <%=PoiListModel.setAudioType("audioType")%>
			    else
			        int audioType = <%=PoiListModel.AudioType.POI_TYPEIN%>
				    <%=PoiListModel.setAudioType("audioType")%>
			    endif
			        	
	    	    JSONObject otherParameter
			    JSONObject.put(otherParameter,"showProgressBar",0)
			    JSONObject.put(otherParameter,"showChangeLocationPopup",1)
			    JSONObject.put(otherParameter,"categoryName",inputString)
			    JSONObject.put(otherParameter,"changeLocationFlag",0)
			    println(JSONObject.toString(otherParameter))
			    
			    PoiList_M_saveBackAction("OneBoxSearch")
			    PoiList_C_searchPoiWithAjax(otherParameter)
	    	endfunc
	    	
	    	func chooseLocation()
		        JSONObject jo
		        JSONObject.put(jo,"title","<%=msg.get("common.chooseLocation")%>")
		        JSONObject.put(jo,"mask","01111111011")
		        JSONObject.put(jo,"from","POI")
		        JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
				JSONObject.put(jo,"callbackpageurl","<%=pageURL%>")
				SelectAddress_C_SelectAddress(jo)
				return FAIL
		    endfunc
		    
		    #Back from select address
		    func CallBack_SelectAddress()
		        TxNode addressNode
				addressNode=ParameterSet.getParam("returnAddress")
				   
				String joString = TxNode.msgAt(addressNode,0)
				JSONObject jo = JSONObject.fromString(joString)
                SearchPoi_M_saveLocation(jo)
                SearchPoi_M_saveFlowFlag("2")
		    endfunc

	        func deleteRecentSearch()
	           System.showConfirm("<%=msg.get("poi.clear.history")%>","<%=msg.get("common.button.Yes")%>","<%=msg.get("common.button.No")%>","deleteRecentSearchConfirm",1)
	           return FAIL
	        endfunc
	        
	        func deleteRecentSearchConfirm(int selected)
	           if 1 == selected
	               OneBox_M_deleteResentSearch()
	               String str = Page.getControlProperty("oneBox","text")
	               Page.setComponentAttribute("oneBox","text", "dummy")
	           	   Page.setComponentAttribute("oneBox","text", str)
				   #TxNode hotBrandNode
				   #hotBrandNode = ParameterSet.getParam("hotBrandlist")
				   
                   #To show tip information in poi inputbox
                   #TxNode finalNode
                   #TxNode.addChild(finalNode,hotBrandNode)
				   #Page.setFieldFilter("oneBox",finalNode)
				   #Page.setComponentAttribute("oneBox","showarrow","0")
		           hideRecentSearchMenu()
		           System.showErrorMsg("<%=msg.get("onebox.history.delete")%>")
		       endif
		       return FAIL
	        endfunc
	        
		    func loadLocalList()
        		TxNode node
				node = OneBox_M_getResentSearch()
				#Hot-Brand
				#TxNode hotBrandNode
			    #hotBrandNode = ParameterSet.getParam("hotBrandlist")
                #To show tip information in poi inputbox
                #TxNode finalNode
                #TxNode.addChild(finalNode,hotBrandNode)
                
                if NULL != node
                   showRecentSearchMenu()
                   #TxNode.addChild(finalNode,node)
                else
                   hideRecentSearchMenu()
                endif
				#if NULL != finalNode
				#   Page.setFieldFilter("oneBox",finalNode)
				#endif
	        endfunc
	        
	    	func hideRecentSearchMenu()
	            MenuItem.setItemValid("oneBox", 1, 0)
	            MenuItem.commitSetItemValid("oneBox")
	        endfunc
	        
	        func showRecentSearchMenu()
	            MenuItem.setItemValid("oneBox", 1, 1)
	            MenuItem.commitSetItemValid("oneBox")
	        endfunc
		]]>
	</tml:script>

	<tml:menuItem name="deleteRecentSearch" text="<%=msg.get("poi.clear.search.history")%>"
		trigger="KEY_MENU" onClick="deleteRecentSearch">
	</tml:menuItem>
	<tml:menuItem name="doSearch" onClick="onClickOneBoxSearch" trigger="TRACKBALL_CLICK"/>
	<tml:page id="OneBoxSearch" url="<%=getPage + "OneBoxSearch"%>" type="<%=pageType%>" helpMsg="$//$search"
		 groupId="<%=GROUP_ID_COMMOM%>" supportback="false">
		<tml:image id="oneBoxBgImg" visible="true" align="left|top"/>	
		<tml:inputBox id="oneBox" titleAbove="false"
			titleFontWeight="system_large|bold" fontWeight="system_large"
			prompt="<%=msg.get("onebox.what.prompt")%>" type="searchfield" length="300">
			<tml:menuRef name="doSearch" />
			<tml:menuRef name="deleteRecentSearch" />
		</tml:inputBox>
		<tml:button id="oneBoxSearchButton" text="  ">
			<tml:menuRef name="doSearch"/>
			<tml:menuRef name="deleteRecentSearch" />
		</tml:button>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>
