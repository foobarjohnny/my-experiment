<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../Header.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.telenav.cserver.poi.holder.PoiCategoryItem"%>
<%@ page import="com.telenav.j2me.datatypes.*"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@ page import="com.telenav.cserver.util.FeatureConstant"%>
<%@ page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@ page import="com.telenav.cserver.util.TnUtil"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%
    String categoryPageUrl = getPage + "SearchCategory";
    String pageURL = getPageCallBack + "SearchPoi";
    List<PoiCategoryItem> hotList = (List<PoiCategoryItem>)request.getAttribute("hotCategoryData");
	String strCarrier = handlerGloble.getClientInfo(handlerGloble.KEY_CARRIER);
%>
<%
	int gpsValidTime = 240;
	int cellIdValidTime=480;
	int gpsTimeout=12;
%>
<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>
<%@page import="com.telenav.cserver.stat.*"%>
<tml:TML outputMode="TxNode">

	<%@ include file="../StatLoggerCommon.jsp"%>
	<%@ include file="model/SearchPoiModel.jsp"%>
	<%@ include file="controller/PoiListController.jsp"%>
	<%@ include file="../dsr/controller/DSRController.jsp"%>
	<jsp:include page="/touch64/jsp/ac/controller/SelectAddressController.jsp" />
	<%@ include file="/touch64/jsp/model/PrefModel.jsp"%>
	<%@ include file="/touch64/jsp/local_service/GetGps.jsp"%>
	<%@ include file="/touch64/jsp/model/OneBoxModel.jsp"%>
	<%@ include file="/touch64/jsp/poi/controller/PoiSubCategoryController.jsp"%>
	
	<tml:script language="fscript" version="1"
		feature="<%=FeatureConstant.DSR%>">
		<![CDATA[
		<%@ include file="../GetServerDriven.jsp"%>
			func pageCallBack(int type) 
				if 5 == type 
					onClickGoToOneBox()
				endif
				return FAIL
			endfunc
			
			func CommonAPI_SetFocus(String it)
				<% if (!PoiUtil.isWarrior(handlerGloble)){ %>
				<%}else{%>
					Page.setComponentAttribute(it,"focused","true")		
				<%}%>
			endfunc
			
			func preLoad()
				CommonAPI_SetFocus("oneBox")
				checkDSRAvail()
			endfunc

			func checkDSRAvail()
				if DSR_M_isDSRSupportedForDisable() == 1
					<%if(TnUtil.isTMOAndroidUser(handlerGloble)){%>
						Page.setComponentAttribute("speakInButton","visible","1")
						Page.setComponentAttribute("speakInButton","imageUnclick","$availableOffButton")
						Page.setComponentAttribute("speakInButton","imageClick","$availableOnButton")
					<%} else {%>
						Page.setComponentAttribute("speakInButton","visible","1")
					<%}%>
				elsif DSR_M_isDSRSupportedForDisable() == 2
					Page.setComponentAttribute("speakInButton","visible","1")
					Page.setComponentAttribute("speakInButton","imageUnclick","$disableOffButton")
					Page.setComponentAttribute("speakInButton","imageClick","$disableOnButton")
				else
					Page.setComponentAttribute("speakInButton","visible","0")					
				endif
			endfunc

		    func speakSearch()
	        	JSONObject jo
	        	int searchType = <%=PoiListModel.getSearchType()%>
	        	if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
	            	invokeSpeakSearchAlong(PoiList_M_getSearchInformation())
	            else
		        	JSONObject.put(jo,"poiSearchType",searchType)
		        	JSONObject.put(jo,"callbackpageurl",SearchPoi_M_getCallBackUrl())
		        	JSONObject.put(jo,"callbackfunction",SearchPoi_M_getCallBackFunction())
 					invokeSpeakSearch(jo)	            	
	            endif
	            return FAIL
		    endfunc
		]]>
	</tml:script>

	<tml:script language="fscript" version="1">
		<![CDATA[
		        func onShow()
					if "1" == SearchPoi_M_getFlowFlag()
						SearchPoi_M_saveFlowFlag("0")
						doSearchOnClick()
					elsif "2" == SearchPoi_M_getFlowFlag()
						SearchPoi_M_saveFlowFlag("0")
						doSearchOnClick()	
					endif
					System.setKeyEventListener("-up-down-","onKeyPressed")
		        endfunc
		        
		        func onKeyPressed(string s)
		        	if s == "down"
		        		int focusIdx = getFocusButton()
		        		if focusIdx >= 0 
		        			int nextFocusIdx = getNextFocusButton(focusIdx)
		        			if nextFocusIdx >= 0
		        				Page.setControlProperty("itemCategoryButton"+nextFocusIdx,"focused","true")
		        				return TRUE
		        			else
		        				Page.setControlProperty("speakInButton","focused","true")
		        				return TRUE
		        			endif
		        		endif
		        	elsif s == "up"
		        		int focusIdx = getFocusButton()
		        		if focusIdx >= 0 
		        			int preFocusIdx = getPreFocusButton(focusIdx)
		        			if preFocusIdx >= 0
		        				Page.setControlProperty("itemCategoryButton"+preFocusIdx,"focused","true")
		        				return TRUE
		        			else
		        				Page.setControlProperty("oneBoxSearchButton","focused","true")
		        				return TRUE
		        			endif
		        		endif
		        	endif
		        endfunc
		        
		        func getFocusButton()
					int i =0
		        	int size = <%=hotList.size()%>
		        	while i < size
		        		if TRUE == Page.getControlProperty("itemCategoryButton"+i,"isFocused")
		        			return i
		        		endif
		        		i = i+1
		        	endwhile
		        	return -1
		        endfunc
		        
		        func getNextFocusButton(int orginIdx)
		        	int i = orginIdx + 1
		        	int size = <%=hotList.size()%>
		        	int orginx = Page.getControlProperty("itemCategoryButton"+orginIdx,"x")
		        	while i < size
		        		if orginx == Page.getControlProperty("itemCategoryButton"+i,"x")
		        			return i
		        		endif
		        		i = i+1
		        	endwhile
		        	return -1
		        endfunc
		        
		        func getPreFocusButton(int orginIdx)
		        	int i = orginIdx - 1
		        	int orginx = Page.getControlProperty("itemCategoryButton"+orginIdx,"x")
		        	while i >= 0
		        		if orginx == Page.getControlProperty("itemCategoryButton"+i,"x")
		        			return i
		        		endif
		        		i = i-1
		        	endwhile
		        	return -1
		        endfunc
		        
				func speakInClick()		        
	                System.doAction("doSpeakSearch")
	                return FAIL
		        endfunc
		        
		        func doSearchOnClick()
					int searchType = <%=PoiListModel.getSearchType()%>
		            
		            #searchType=7: search along
		            if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
		               doSearchWithAjax()
		            else
		               JSONObject addressJO = SearchPoi_M_getLocation()
					   if NULL == addressJO
							doGetGPSForPoi()
					   else
						   int lon = JSONObject.getInt(addressJO,"lon")
						   if 0 == lon
						      doGetGPSForPoi()
						   else
						      doSearchWithAjax()
						   endif
					   endif
		            endif
		        endfunc
		        
		        func doSearchWithAjax()
					<%if(!PoiUtil.isWarrior(handlerGloble)){%>
				 		Page.preLoadPageToCache("<%=getPageCallBack + "PoiList"%>")
					<%}%>
		        	
		            #Do some initial work for result
				    PoiList_C_deleteSortTypeTemp()
		            PoiList_C_deleteBannerAdsList()
		            String inputString = ""
		            <%=PoiListModel.setKeyWord("inputString")%>
				    <%=PoiListModel.setPageIndexTemp("0")%>
				    
				    String categoryId = <%=PoiListModel.getCategoryId()%>
				    String isMostPopular = <%=PoiListModel.getMostPopular()%>
					PoiList_C_saveSpecialSortForSpare(categoryId,isMostPopular)
					
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
			    	JSONObject.put(otherParameter,"categoryName",SearchPoi_M_getCategoryName())
			    	JSONObject.put(otherParameter,"changeLocationFlag",0)
			    	
			    	##
			    	##If OBS was not supported or was disabled, when search sth in OBS, click "change location" on Pop-up, choose some address,
			    	##call this method, and inputString was initialized, so need to re-set search content to inputString again.
			    	##
			    	if NULL == categoryId || "" == categoryId || "-1" == categoryId
			    		inputString = SearchPoi_M_getCategoryName()
			    		<%=PoiListModel.setKeyWord("inputString")%>	
			    	endif
			    	
			    	PoiList_C_deleteBackAction()
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
		        
		        func onBack()
				   String backAction = SearchPoi_M_getBackAction()
                   if "" != backAction
                      if "<%=Constant.BACK_ACTION_MAIN_SCREEN%>" == backAction
                         SearchPoi_M_deleteBackAction()
                         System.doAction("home")
		                 return FAIL
                      else
                         System.quit()
                         return FAIL
                      endif
                   endif
                   
                   #When search along, back to Nav local service
				   int searchType = <%=PoiListModel.getSearchType()%>
		           if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
			          System.doAction("driveToMenu")
			          return FAIL
		           else
		           		System.back()
				   		return FAIL
		           endif
				endfunc
				
				func backPage()
				    System.back()
				    return FAIL
				endfunc

				
				func onClickCategory()
					TxNode nodeId = ParameterSet.getParam("id")
					TxNode nodeName = ParameterSet.getParam("name")
					TxNode nodeMostPopular = ParameterSet.getParam("mostPopular")
					TxNode nodeIndicator= ParameterSet.getParam("indicator")
					
					String name = System.parseI18n(TxNode.msgAt(nodeName,0))
					String isMostPopular = TxNode.msgAt(nodeMostPopular,0)
					String indicator = TxNode.msgAt(nodeIndicator,0)
					String id = TxNode.msgAt(nodeId,0)
					
					if 0 == ServerDriven_CanGasByPrice() && "50500" == id
					    indicator = "0"
					    TxNode node
					    TxNode.addMsg(node,"49")
					    nodeId = node
					    name = "<%=msg.get("poi.category.50500.popup")%>"
					    isMostPopular = "0"
					endif
					println("indicator="+indicator)
							
					if "2" == indicator
						System.doAction("selectCategory")
		                return FAIL
					elsif "1" == indicator
						SearchPoi_M_saveSubCategoryName(name)
			        	PoiSubCategory_C_show(id)
			        	return FAIL
					else
						SearchPoi_M_saveCategory(name,isMostPopular,nodeId)
						doSearchOnClick()
					endif
				endfunc


			func getOneBoxParam()
				JSONObject jo
				JSONObject.put(jo,"callbackfunction","addrCallBack")
				JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack+"OneBoxWrap#search"%>")
				JSONObject.put(jo,"from","Places")
	        	return jo
      		endfunc
      		
		    func onClickGoToOneBox()
		    	OneBox_M_saveAcParam(getOneBoxParam())
		    	System.doAction("oneBoxSearchMenu")
		    	return FAIL	
	    	endfunc
			
			func doGetGPSForPoi()
			   		getCurrentLocationWithoutBlocking(<%=Constant.CurrentLocation.CURRENT_LOCATION%>,<%=gpsValidTime%>,<%=cellIdValidTime%>,<%=gpsTimeout%>)		        
		    endfunc

	        func CallBack_GPS_Error(int param)
			endfunc
				        
	        # Back from "getGPS"
	        func setCurrentLocation(JSONObject jo)
        		SearchPoi_M_saveLocation(jo)
        		doSearchWithAjax()
	        endfunc
		]]>
	</tml:script>
	
	<tml:menuItem name="poiSubCategory" pageURL=""/>
	<tml:menuItem name="oneBoxSearchMenu" pageURL="<%=getPage + "GoToOneBoxSearch#Places"%>" trigger="TRACKBALL_CLICK">
	</tml:menuItem>
	<tml:menuItem name="home" pageURL="<%=host + "/startUp.do?pageRegion=" + region%>">
	</tml:menuItem>

	<tml:menuItem name="doSearch" onClick="onClickGoToOneBox" trigger="TRACKBALL_CLICK"/>

	<tml:block feature="<%=FeatureConstant.DSR%>">
		<tml:menuItem name="doSpeakSearch" text="<%=msg.get("poi.speak.search")%>"
			trigger="KEY_MENU" onClick="speakSearch">
		</tml:menuItem>
	</tml:block>
	
	<tml:menuItem name="addressSelect" onClick="chooseLocation">
	</tml:menuItem>

	<tml:menuItem name="selectCategory" pageURL="<%=categoryPageUrl%>">
	</tml:menuItem>

	<tml:menuItem name="driveToMenu" pageURL="<%=getPage + "DriveToWrap"%>" />
	<tml:menuItem name="speakInMenu" onClick="speakInClick"></tml:menuItem>

	<tml:page id="mainPagePoiPage" url="<%=getPage + "SearchPoi"%>" groupId="<%=GROUP_ID_POI%>" scriptCallback="pageCallBack"
		type="<%=pageType%>" showLeftArrow="true"
		showRightArrow="true" helpMsg="$//$search">
		<tml:image id="oneBoxBgImg" visible="true" align="left|top"/>
		<tml:label id="titleLabel" align="center|middle" fontWeight="system_large|bold" fontColor="white">
			<%=msg.get("startup.Search")%>
		</tml:label>
		
		<tml:compositeListItem id="oneBoxSearchButton" >
			<tml:label id ="oneBoxSearchLabel" fontWeight="system_large">
				<%=TnUtil.amend(msg.get("onebox.what.prompt"))%>
			</tml:label>
			<tml:menuRef name="doSearch"/>
		</tml:compositeListItem>
		<tml:panel id="searchPanel">
		<%
			for(int i=0;i<hotList.size();i++)
			{
				PoiCategoryItem itemCategory = hotList.get(i);
		%>
			<tml:menuItem name="<%="choseCategoryMenu" + i%>" onClick="onClickCategory">
				<tml:bean name="id" valueType="string" value="<%=itemCategory.getId()%>"/>
				<tml:bean name="name" valueType="string" value="<%=TnUtil.getXMLString(msg.get("poi.category." + itemCategory.getId()))%>"/>
				<tml:bean name="mostPopular" valueType="string" value="<%=itemCategory.getMostPopular()%>"/>
				<tml:bean name="indicator" valueType="string" value="<%=itemCategory.getIndicator()%>"/>
			</tml:menuItem>	
			<tml:button id="<%="itemCategoryButton" + i%>" text=""
				fontWeight="system_large" isFocusable="true" 
				imageClick="<%=imageCommonUrl + itemCategory.getImageId() + "-on.png"%>"
				imageUnclick="<%=imageCommonUrl + itemCategory.getImageId() + ".png"%>">
				<tml:menuRef name="<%="choseCategoryMenu" + i%>" />
			</tml:button>
			<tml:label id="<%="itemCategoryLabel" + i%>" focusFontColor="white" 
				fontWeight="system_small" align="top|center" textWrap="wrap">
				<%=PoiUtil.amend(msg.get("poi.category." + itemCategory.getId()))%>
			</tml:label>
		<%
			}
		%>
		</tml:panel>
		<tml:image id="titleShadow1"  visible="true" align="left|top"/>	
		<tml:image id="bottomBgImg" url=""   visible="false" align="left|top"/>
		<tml:button id="speakInButton" text="<%=msg.get("common.button.sayIt")%>"
			fontWeight="system_large" isFocusable="true">
			<tml:menuRef name="speakInMenu" />
		</tml:button>
		<cserver:outputLayout />
	</tml:page>
</tml:TML>