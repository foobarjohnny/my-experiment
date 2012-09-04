<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../model/PoiListModel.jsp"%>
<tml:script language="fscript" version="1">
	<![CDATA[
			func PoiList_C_deleteSortTypeTemp()
			    <%=PoiListModel.deleteSortTypeTemp()%>
			endfunc
			
		    #If user is viewing results for ‘Gas by Price’, default all results should be sorted by price.
		    #Chengbiao: Per PM, for all food/coffee search, default sort by distance
		    #Any--2041 American--229 BBQ--232 Bakery--231 Burgers--236 Chinese--240 Coffee--241 Deli--243 French--251 German--664 Ice Cream--253 Indian--254 Italian--256
		    #Japanese--257 Mexican--261 Middle East--262 Pizza--263 Seafood--264 Steak House--267
		    func PoiList_C_saveSpecialSortForSpare(String categoryId,String isMostPopular)
		        println("isMostPopular................"+isMostPopular)
		        println("categoryId....................."+categoryId)
		        String foodCategoryId = "#2041#229#232#231#236#240#241#243#251#664#253#254#256#257#261#262#263#264#267#"
		        String categoryStr = "#" + categoryId + "#"
		        if "1" == isMostPopular
		            <%=PoiListModel.setSortTypeTemp(""
                                + Constant.SORT_BY_POPULAE)%>
		        elsif 1 == PoiList_C_isGasByPrice(categoryId)
				    <%=PoiListModel.setSortTypeTemp(""
                                + Constant.SORT_BY_GASPRICE)%>
                # WIFI default by distance to fix bug ATTBBPUB-386
                elsif "730" == categoryId               
                    <%=PoiListModel.setSortTypeTemp(""
                                + Constant.SORT_BY_DISTANCE)%>  
                elseif String.find(foodCategoryId,0,categoryStr) > -1
                    <%=PoiListModel.setSortTypeTemp(""
                                + Constant.SORT_BY_DISTANCE)%>  
				endif
		    endfunc
		    			
			func PoiList_C_searchPoiInterface(JSONObject otherParameter,JSONObject location)
				<%=PoiListModel.deleteCategoryId()%>
				PoiList_M_deleteCategoryNameForDsr()
				<%=PoiListModel.deleteSortTypeTemp()%>
				
				PoiList_M_saveAudioFinishFlag(0)
			    String inputString = JSONObject.get(otherParameter,"inputString")
			    if  String.equalsIgnoreCase(inputString, "Gas By Price") || String.equalsIgnoreCase(inputString, "GasBy Price") || String.equalsIgnoreCase(inputString, "GasByPrice")
			        inputString = ""
			        String categoryId = "50500"
			        <%=PoiListModel.setSortTypeTemp("" + Constant.SORT_BY_GASPRICE)%>
			        <%=PoiListModel.setCategoryId("categoryId")%>
				    PoiList_M_saveCategoryNameForDsr("Gas By Price > Any")
				elsif String.equalsIgnoreCase(inputString, "searchTheater")
					inputString = ""
			        String categoryId = "181"
			        <%=PoiListModel.setCategoryId("categoryId")%>
				    PoiList_M_saveCategoryNameForDsr("Movie Theaters")  
			    endif  
			    
			    PoiList_M_saveResentSearch(inputString)
			    String callBackUrl = JSONObject.get(otherParameter,"callBackUrl")
			    String callBackFunction = JSONObject.get(otherParameter,"callBackFunction")
			    int searchType = JSONObject.get(otherParameter,"searchType")
			    
			    int showProgressBar = JSONObject.get(otherParameter,"showProgressBar")
			    <%=PoiListModel.setShowProgressBar("showProgressBar")%>
			    
			    String noRecordBackUrl = JSONObject.get(otherParameter,"noRecordBackUrl")
			    if NULL == noRecordBackUrl
			       noRecordBackUrl = ""
			    endif
			    <%=PoiListModel.setBackURLWhenNoFound("noRecordBackUrl")%>
			    
			    int waitAudioFinish = JSONObject.get(otherParameter,"waitAudioFinish")
			    if NULL == waitAudioFinish
			       waitAudioFinish = 0
			    endif
			    <%=PoiListModel.setWaitPromptAudioFinish("waitAudioFinish")%>
			    
			    int searchAlongType = JSONObject.get(otherParameter,"searchAlongType")
			    if NULL != searchAlongType
			       PoiList_C_saveSearchAlongType(searchAlongType)
			    endif
			    PoiList_M_setCallBackInformation(callBackUrl,callBackFunction)
			 
				#save inputString to cookie
				<%=PoiListModel.setKeyWord("inputString")%>
				String displayString = inputString
				<%=PoiListModel.setDisplayWord("displayString")%>
				PoiList_M_saveLocation(location)
				<%=PoiListModel.setPageIndexTemp("0")%>
			    
			    String typeOrSpeak = "<%=Constant.StorageKey.POI_MODULE_FROM_SPEAK%>"
			    <%=PoiListModel.setInputType("typeOrSpeak")%>
			    <%=PoiListModel.setSearchType("searchType")%>
			    
			    int audioType = <%=PoiListModel.AudioType.POI_SPEAKIN%>
				<%=PoiListModel.setAudioType("audioType")%>
				PoiList_M_saveSearchPoiSuccessFlag(0)

				PoiList_M_deleteSponsorPoiList()
				
				JSONObject otherParameter1
			    JSONObject.put(otherParameter1,"showProgressBar",0)
			    JSONObject.put(otherParameter1,"showChangeLocationPopup",0)
			    JSONObject.put(otherParameter1,"categoryName","")
			    JSONObject.put(otherParameter1,"changeLocationFlag",0)
			    PoiList_M_savePoiSearchParameter(otherParameter1)	
			    
			    PoiList_M_deleteBackAction()	
				PoiList_M_searchPoiWithAjax()
			endfunc
			
			func PoiList_C_searchPoiWithAjax(JSONObject otherParameter)
				PoiList_M_clearParameter()
				
				String typeOrSpeak = "<%=Constant.StorageKey.POI_MODULE_FROM_TYPE%>"
			    <%=PoiListModel.setInputType("typeOrSpeak")%>
				
				int showProgressBar = JSONObject.get(otherParameter,"showProgressBar")
			    <%=PoiListModel.setShowProgressBar("showProgressBar")%>
			    PoiList_M_savePoiSearchParameter(otherParameter)
			    PoiList_M_deleteSponsorPoiList()
			    PoiList_M_searchPoiWithAjax()
	        endfunc

	        func PoiList_C_checkAudioFinishAndSubmit()
	            PoiList_M_saveAudioFinishFlag(1)
	            PoiList_M_checkAudioFinishAndSubmit()
	        endfunc
	        
            func PoiList_C_saveSearchAlongType(int searchAlongType)
	             PoiList_M_saveSearchAlongType(searchAlongType)
	        endfunc
	        
	        func PoiList_C_getSearchAlongType()
	             return PoiList_M_getSearchAlongType()
	        endfunc
	        
	        func PoiList_C_getSponsorPoiList()
			   return PoiList_M_getSponsorPoiList()
            endfunc
            
            func PoiList_C_getCurrentSize()
	            return <%=PoiListModel.getPoiCount()%>
	        endfunc
	        
	        func PoiList_C_resume()
	    	     PoiList_M_resume()
	    	endfunc
	    	
	    	func PoiList_C_getSearchCategory()
	    	     return PoiList_M_getSearchCategory()
	    	endfunc
	    	
	    	func PoiList_C_isGasByPrice(String categoryId)
	    	     int isGasByPrice = 0
	    	     if "50500" == categoryId || "702" == categoryId || "703" == categoryId || "704" == categoryId || "705" == categoryId
	    	        isGasByPrice = 1
	    	     endif
	    	     
	    	     return isGasByPrice
	    	endfunc
	    	
 		    func PoiList_C_deleteBannerAdsList()
	    		PoiList_M_deleteBannerAdsList()
	    	endfunc
	    	
	    	func PoiList_C_saveBackAction(String backAction)
	    	    PoiList_M_saveBackAction(backAction)
	    	endfunc
	    	
	    	func PoiList_C_deleteBackAction()
	    	    PoiList_M_deleteBackAction()	
	    	endfunc
	    	
	    	func PoiList_C_setCategory(String categoryId)
	    	    <%=PoiListModel.setCategoryId("categoryId")%>
				PoiList_M_saveCategoryNameForDsr("Gas By Price > Any")
	    	endfunc
		]]>
</tml:script>
