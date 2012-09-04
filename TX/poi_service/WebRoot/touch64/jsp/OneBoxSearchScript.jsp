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
				if 1 == ServerDriven_CanOneBoxSearch() && 1 == ServerDriven_IsCallOneBox()
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
			    
			    if 1 != ServerDriven_CanOneBoxSearch() || 1 != ServerDriven_IsCallOneBox()
			    	TxNode nameTxNode
				    TxNode.addMsg(nameTxNode,inputString)
				    Cache.saveToTempCache("<%=Constant.StorageKey.SEARCH_POI_CATEGORY_NAME%>",nameTxNode)
			    endif
			    
			    PoiList_M_saveBackAction("OneBoxSearch")
			    PoiList_C_searchPoiWithAjax(otherParameter)
	    	endfunc
	    	
	    	func chooseLocation()
		        JSONObject jo
		        JSONObject.put(jo,"title","<%=msg.get("common.chooseLocation")%>")
		        JSONObject.put(jo,"mask","01111111011")
		        JSONObject.put(jo,"from","POI")
		        JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
		        
		        if 1 == ServerDriven_CanOneBoxSearch() && 1 == ServerDriven_IsCallOneBox()
					JSONObject.put(jo,"callbackpageurl","<%=pageURL%>")
		        else
		        	JSONObject.put(jo,"callbackpageurl","<%=poiSearchURL%>")
		        endif
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