<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%
    String categoryPageUrl = getPage + "SearchCategory";
    String pageURL = getPageCallBack + "SearchPoi";
    TxNode listNode = (TxNode) request.getAttribute("node");
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
	<jsp:include page="/touch/jsp/ac/controller/SelectAddressController.jsp" />
	<%@ include file="/touch/jsp/local_service/GetGps.jsp"%>
	<%@ include file="/touch/jsp/model/PrefModel.jsp"%>
	
	<tml:script language="fscript" version="1"
		feature="<%=FeatureConstant.DSR%>">
		<![CDATA[
			func CommonAPI_SetFocus(String it)
				<% if (!PoiUtil.isWarrior(handlerGloble)){ %>
				<%}else{%>
					Page.setComponentAttribute(it,"focused","true")		
				<%}%>
			endfunc
			
			func preLoad()
				CommonAPI_SetFocus("poi")
				checkDSRAvail()
			endfunc

			func checkDSRAvail()
				if DSR_M_isDSRSupported()
					Page.setComponentAttribute("speakInButton","visible","1")
					Page.setComponentAttribute("submitButton","visible","1")
					Page.setComponentAttribute("submitButton1","visible","0")
				else
					Page.setComponentAttribute("speakInButton","visible","0")
					Page.setComponentAttribute("submitButton","visible","0")
					Page.setComponentAttribute("submitButton1","visible","1")					
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

			func getSubmitButtonId()
				string id = "submitButton1"
				if DSR_M_isDSRSupported()
					id = "submitButton"
				endif
				return id
			endfunc
			
		        func onShow()
		            TxNode node
					node = SearchPoi_M_getResentSearch()
					
					#Hot-Brand
					TxNode hotBrandNode
				    hotBrandNode = ParameterSet.getParam("hotBrandlist")
					
                    #To show tip information in poi inputbox
                    TxNode finalNode
                    TxNode.addChild(finalNode,hotBrandNode)
                    
                    if NULL != node
                       showRecentSearchMenu()
                       TxNode.addChild(finalNode,node)
                    else
                       hideRecentSearchMenu()
                    endif
					if NULL != finalNode
					   Page.setFieldFilter("poi",finalNode)
					endif
					
					setInformation()
					
					int searchType = <%=PoiListModel.getSearchType()%>
					#searchType=7: search along
		            if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
		               String textAddress = "<%=msg.get("poi.close.ahead")%>"
		               if NULL == SearchPoi_M_getSearchAlongTypeNode()
		                  PoiList_C_saveSearchAlongType(<%=Constant.searchAlongType_closeAhead%>)
		               else
		                  int searchAlongType = PoiList_C_getSearchAlongType()
		                  if searchAlongType == <%=Constant.searchAlongType_closeAhead%>
		                     textAddress = "<%=msg.get("poi.close.ahead")%>"
		                  elsif searchAlongType == <%=Constant.searchAlongType_nearDestination%>
		                     textAddress = "<%=msg.get("poi.address.near.destination")%>"
		                  endif
		               endif
		               Page.setComponentAttribute("addressName","text",textAddress)
		            endif
					
		        endfunc
		        
		        func hideRecentSearchMenu()
		            MenuItem.setItemValid("poi", 2, 0)
		            MenuItem.commitSetItemValid("poi")
		        endfunc
		        
		        func showRecentSearchMenu()
		            MenuItem.setItemValid("poi", 2, 1)
		            MenuItem.commitSetItemValid("poi")
		        endfunc

				func speakInClick()		        
	                System.doAction("doSpeakSearch")
	                return FAIL
		        endfunc
		        
				func setInformation()
				    #Set what
				    String inputString = <%=PoiListModel.getKeyWord()%>
				    if NULL != inputString && "" != inputString
				       Page.setComponentAttribute("poi","text",inputString)
				    else
				       Page.setComponentAttribute("poi","text","")
				    endif
				    
				    #Set Category
				    TxNode nameNode = SearchPoi_M_getCategoryName()
				    String nameString = "<%=msg.get("poi.category.allCategories")%>"
				    if NULL != nameNode
				       nameString = TxNode.msgAt(nameNode,0)
				    endif
				    Page.setComponentAttribute("categoryName","text",nameString)
				    
				    #Set address
				    JSONObject addressJO = SearchPoi_M_getLocation()
				    
				    if NULL!=addressJO
				       string textAddress 
				       int type = JSONObject.getInt(addressJO,"type")
				       
				       #TODO type=6: current location 
				       if type == 6
				           textAddress = "<%=msg.get("poi.address.Current.Location")%>"
				       else
				           String addressString = JSONObject.get(addressJO,"firstLine")
				           String cityString = JSONObject.get(addressJO,"city")
				           String stateString = JSONObject.get(addressJO,"state")
				           String lastLineString = cityString
				           if "" == lastLineString
					          lastLineString = stateString
					       elsif NULL != lastLineString && NULL != stateString && "" != stateString
					          lastLineString = cityString + ", " + stateString
					       endif
				           
					       if "" == addressString
					          addressString = lastLineString
						   elsif NULL != lastLineString && "" != lastLineString
						      addressString = addressString + ", " + lastLineString
					       endif
					       
					       if "" == addressString
					           textAddress = "<%=msg.get("poi.address.Current.Location")%>"
					       else
					           textAddress = addressString
					       endif
				       endif
				       Page.setComponentAttribute("addressName","text",textAddress)
				    else
				       String textAddress = "<%=msg.get("poi.address.Current.Location")%>"
				       Page.setComponentAttribute("addressName","text",textAddress)
				    endif
				endfunc
		
		        func doSearchOnClick()
					int searchType = <%=PoiListModel.getSearchType()%>
		            
		            #searchType=7: search along
		            if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
		               doSearchWithAjax()
		            else
		               JSONObject addressJO = SearchPoi_M_getLocation()
					   if NULL == addressJO
							doGetGPS()
					   else
						   int lon = JSONObject.getInt(addressJO,"lon")
						   if 0 == lon
						      doGetGPS()
						   else
						      doSearchWithAjax()
						   endif
					   endif
		            endif
		        endfunc
		        
		        func doGetGPS()
			   		getCurrentLocation(<%=Constant.CurrentLocation.CURRENT_LOCATION%>,<%=gpsValidTime%>,<%=cellIdValidTime%>,<%=gpsTimeout%>)		        
		        endfunc

		        func CallBack_GPS_Error(int param)
				endfunc
					        
		        # Back from "getGPS"
		        func setCurrentLocation(JSONObject jo)
	        		SearchPoi_M_saveLocation(jo)
	        		doSearchWithAjax()
		        endfunc
		        
		        func doSearchWithAjax()
					<%if(!PoiUtil.isWarrior(handlerGloble)){%>
				 		Page.preLoadPageToCache("<%=getPageCallBack + "PoiList"%>")
					<%}%>
		        	
		            #Do some initial work for result
				    PoiList_C_deleteSortTypeTemp()
		            
		            TxNode addressInputNode 
				    addressInputNode = ParameterSet.getParam("poi")
				    
				    #Save rensent search
		            SearchPoi_M_saveResentSearch(addressInputNode)
		            String inputString = ""
		            if NULL != addressInputNode
		               inputString = TxNode.msgAt(addressInputNode,0)
		            endif
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
			        
			        PoiList_C_deleteBannerAdsList()
				    PoiList_C_searchPoiWithAjax()
		        endfunc
		        
		        func queryCategory()
		            saveInput()
		        endfunc
		        
		        func saveInput()
		            TxNode addressInputNode 
				    addressInputNode = ParameterSet.getParam("poi")
				    String inputString = ""
		            if NULL != addressInputNode
		               inputString = TxNode.msgAt(addressInputNode,0)
		            endif
		            <%=PoiListModel.setKeyWord("inputString")%>
		        endfunc
		        
		        func chooseLocation()
		            saveInput()
		            int searchType = <%=PoiListModel.getSearchType()%>
		            if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
		               System.showConfirm("<%=msg.get("poi.where.along.route")%>","<%=msg.get("poi.close.ahead")%>","<%=" "+msg.get("poi.near.destination")+" "%>","searchAlongType")
		               return TRUE
		            endif
		            JSONObject jo
		        	JSONObject.put(jo,"title","<%=msg.get("common.chooseLocation")%>")
		        	JSONObject.put(jo,"mask","01111111011")
		        	JSONObject.put(jo,"from","POI")
		        	JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
					JSONObject.put(jo,"callbackpageurl","<%=pageURL%>")
					SelectAddress_C_SelectAddress(jo)
					return FAIL
		        endfunc
		        
		        func searchAlongType(int selected)
		            String textAddress = ""
		            if 1 == selected
		               PoiList_C_saveSearchAlongType(<%=Constant.searchAlongType_closeAhead%>)
		               textAddress = "<%=msg.get("poi.close.ahead")%>"
		               Page.setComponentAttribute("address","addressName$text",textAddress)
			        elsif 0 == selected
			           PoiList_C_saveSearchAlongType(<%=Constant.searchAlongType_nearDestination%>)
			           textAddress = "<%=msg.get("poi.address.near.destination")%>"
			           Page.setComponentAttribute("address","addressName$text",textAddress)
			        endif
		        endfunc
		        
		        #Back from select address
		        func CallBack_SelectAddress()
		           TxNode addressNode
				   addressNode=ParameterSet.getParam("returnAddress")
				   
				   String joString = TxNode.msgAt(addressNode,0)
				   JSONObject jo = JSONObject.fromString(joString)
                   SearchPoi_M_saveLocation(jo)
				   
				   setInformation()
		        endfunc
		        
		        func deleteRecentSearch()
		           System.showConfirm("<%=msg.get("poi.clear.history")%>","<%=msg.get("common.button.Yes")%>","<%=msg.get("common.button.No")%>","deleteRecentSearchConfirm",1)
		           return FAIL
		        endfunc
		        
		        func deleteRecentSearchConfirm(int selected)
		           if 1 == selected
		               SearchPoi_M_deleteResentSearch()
		           
					   TxNode hotBrandNode
					   hotBrandNode = ParameterSet.getParam("hotBrandlist")
					   
	                   #To show tip information in poi inputbox
	                   TxNode finalNode
	                   TxNode.addChild(finalNode,hotBrandNode)
					   Page.setFieldFilter("poi",finalNode)
					   Page.setComponentAttribute("poi","showarrow","0")
			           hideRecentSearchMenu()
			           
			           String deleteSuccess = System.parseI18n("<%=msg.get("poi.delete.success")%>")
					   if "poi.delete.success" == deleteSuccess
							deleteSuccess = "History successfully deleted."
					   endif
					   
			           System.showErrorMsg(deleteSuccess)
 		           endif
 		           return FAIL
		        endfunc
		        
		        func inputClick()
		           Page.setControlProperty(getSubmitButtonId(),"focused","true")
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
		]]>
	</tml:script>
	
	<tml:menuItem name="home" pageURL="<%=host + "/startUp.do?pageRegion=" + region%>">
	</tml:menuItem>

	<tml:menuItem name="doSearch" onClick="doSearchOnClick"
		progressBarText="<%=msg.get("poi.Searching")%>">
	</tml:menuItem>
	
	<tml:menuItem name="doSearchMenu" onClick="doSearchOnClick" text="<%=msg.get("poi.Search")%>" trigger="KEY_MENU"
		progressBarText="<%=msg.get("poi.Searching")%>">
	</tml:menuItem>

	<tml:block feature="<%=FeatureConstant.DSR%>">
		<tml:menuItem name="doSpeakSearch" text="<%=msg.get("poi.speak.search")%>"
			trigger="KEY_MENU" onClick="speakSearch">
		</tml:menuItem>
	</tml:block>

	<tml:menuItem name="deleteRecentSearch" text="<%=msg.get("poi.clear.search.history")%>"
		trigger="KEY_MENU" onClick="deleteRecentSearch">
	</tml:menuItem>

	<tml:menuItem name="addressSelect" onClick="chooseLocation">
	</tml:menuItem>

	<tml:menuItem name="selectCategory" onClick="queryCategory"
		pageURL="<%=categoryPageUrl%>">
	</tml:menuItem>

	<tml:menuItem name="driveToMenu" pageURL="<%=getPage + "DriveToWrap"%>" />
	<tml:menuItem name="speakInMenu" onClick="speakInClick"></tml:menuItem>

	<tml:page id="mainPagePoiPage" url="<%=getPage + "SearchPoi"%>" groupId="<%=GROUP_ID_POI%>"
		type="<%=pageType%>" showLeftArrow="true"
		showRightArrow="true" helpMsg="$//$search">
		<tml:bean name="hotBrandlist" valueType="TxNode"
			value="<%=Utility.TxNode2Base64(listNode)%>"></tml:bean>
		<tml:menuItem name="autoFill" onClick="inputClick"  trigger="TRACKBALL_CLICK|KEY_MENU"/>
		
		<tml:panel id="searchPanel">
			<tml:inputBox id="poi" style="capital"
				titleAbove="false" titleFontWeight="system_large|bold"
				fontWeight="system_large" prompt="<%=msg.get("poi.what.prompt")%>" isAlwaysShowPrompt="true"
				type="dropdownfilterfield">
				<tml:menuRef name="autoFill" />
				<tml:menuRef name="doSearchMenu" />
				<tml:menuRef name="deleteRecentSearch" />
			</tml:inputBox>
			
			<tml:compositeListItem id="category" getFocus="false"
				visible="true" bgColor="#FFFFFF" transparent="false"
				isFocusable="true">
				<tml:label id="categoryTitle" textWrap="ellipsis"
					fontWeight="bold|system_large" focusFontColor="white" align="left|middle">
					<%=msg.get("poi.what")%>
				</tml:label>
				<tml:label id="categoryName" textWrap="ellipsis"
					focusFontColor="white" fontWeight="system_large" align="left|middle">
					<%=msg.get("poi.category.allCategories")%>
				</tml:label>
				<tml:menuRef name="selectCategory" />
				<tml:menuRef name="doSearchMenu" />
			</tml:compositeListItem>
			
			<tml:compositeListItem id="address" getFocus="false"
				visible="true" bgColor="#FFFFFF" transparent="false"
				isFocusable="true">
				<tml:label id="addressTitle" textWrap="ellipsis"
					fontWeight="bold|system_large" focusFontColor="white" align="left|middle">
					<%=msg.get("poi.address.title")%>
				</tml:label>
				<tml:label id="addressName" textWrap="ellipsis"
					focusFontColor="white" fontWeight="system_large" align="left|middle">
					<%=msg.get("poi.address.Current.Location")%>
				</tml:label>
				<tml:menuRef name="addressSelect" />
				<tml:menuRef name="doSearchMenu" />
			</tml:compositeListItem>
	
			<tml:image id="bottomBgImg" url=""   visible="false" align="left|top"/>
			
			<tml:button id="submitButton" text="<%=msg.get("poi.Search")%>"
				fontWeight="system_large">
				<tml:menuRef name="doSearch" />
				<tml:menuRef name="doSearchMenu" />
			</tml:button>
			<tml:button id="submitButton1" text="<%=msg.get("poi.Search")%>"
				fontWeight="system_large">
				<tml:menuRef name="doSearch" />
				<tml:menuRef name="doSearchMenu" />
			</tml:button>
			<tml:button id="speakInButton" text="<%=msg.get("common.button.sayIt")%>"
				fontWeight="system_large" isFocusable="true">
				<tml:menuRef name="speakInMenu" />
			</tml:button>
		</tml:panel>
		<cserver:outputLayout />
	</tml:page>
</tml:TML>
