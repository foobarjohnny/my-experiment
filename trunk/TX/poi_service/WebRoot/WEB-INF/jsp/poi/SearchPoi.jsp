<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%
    String categoryPageUrl = getPage + "SearchCategory";
    String imageBg = imageUrl + "backgroud.png";
    String pageURL = getPageCallBack + "SearchPoi";
    String twoLinesImageFocus = imageUrl + "list_bg_highlight_45px.png";
    String twoLinesImageBlur = imageUrl + "list_bg_45px.png";
    TxNode listNode = (TxNode) request.getAttribute("node");
    String purchasePageUrl = "{login.http}/getInterface.do?jsp=PurchaseInterface";
	
%>
<%
	int gpsValidTime = 240;
	int cellIdValidTime=1860;
	int gpsTimeout=12;
%>
<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>
<%@page import="com.telenav.cserver.stat.*"%>
<tml:TML outputMode="TxNode">

	<%@ include file="../StatLoggerCommon.jsp"%>
	<%@ include file="model/SearchPoiModel.jsp"%>
	<%@ include file="controller/PoiListController.jsp"%>
	<%@ include file="../dsr/controller/DSRController.jsp"%>
	<jsp:include page="/WEB-INF/jsp/ac/controller/SelectAddressController.jsp" />
	<%@ include file="/WEB-INF/jsp/local_service/GetGps.jsp"%>
	<%@ include file="/WEB-INF/jsp/model/PrefModel.jsp"%>
	
	<tml:script language="fscript" version="1"
		feature="<%=FeatureConstant.DSR%>">
		<![CDATA[
			func preLoad()
				checkDSRAvail()
				displayButtonImage()
				hideAdJuggler()
			endfunc

		    func displayButtonImage()
		    	string locale = Pref_M_getLocale()
		    	string imageUrl = "<%=imageUrl%>" + "speakSearch_" + locale + ".png"
				Page.setControlProperty("speakSearchButton","image",imageUrl)
		    endfunc
		    	
			func checkDSRAvail()
				if DSR_M_isDSRSupported()
					Page.setComponentAttribute("speakSearchButton","visible","1")
				else
					Page.setComponentAttribute("speakSearchButton","visible","0")
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
			 <%@ include file="../GetServerDriven.jsp"%>
			 <%@ include file="../AdJugglerScript.jsp"%>
				func getAdJuglerObjectForPoiSearch()
					JSONObject result = Cache.getJSONObjectFromCookie("ADJUGGLER_JSON_KEY_POI_SEARCH")
					return result
				endfunc
				
				func saveAdJugglerObjectForPoiSearch(JSONObject adJuggler)
	        		Cache.saveCookie("ADJUGGLER_JSON_KEY_POI_SEARCH", adJuggler)
        		endfunc
        		
        		func clearAdJugglerObjectForPoiSearch(JSONObject adJuggler)
	        		Cache.deleteCookie("ADJUGGLER_JSON_KEY_POI_SEARCH")
        		endfunc
        		
        		func doActionForAdJuggler()
				    JSONObject adJugglerJO = getAdJuglerObjectForPoiSearch()
				    println(adJugglerJO)
				    AdJuggler_doActionForPremClick(adJugglerJO)
				    return FAIL
				endfunc
				func updateBannerAd()
				 TxNode node
		            JSONObject actionJo
		            JSONObject.put(actionJo,"<%=Constant.JSON_KEY_FROM%>","<%=Constant.KEY_ADJUGGLER_FROM_POISEARCH%>")
		            JSONObject adJugglerJO = getAdJuglerObjectForPoiSearch()
		            
		            if NULL == adJugglerJO
		               hideAdJuggler()
		            else
		               if !AdJuggler_checkExpire(adJugglerJO,"LAST_TIME_AJUGGLER_POISEARCH")
			              setAdJugglerMessage(adJugglerJO)
			              return FAIL
			           else
			              hideAdJuggler()
			           endif
		            endif
		            
		            if isPremiumAccount()
		               JSONObject.put(actionJo,"<%=Constant.JSON_KEY_ISPREMIUMACCOUNT%>","1")
		            else
		               JSONObject.put(actionJo,"<%=Constant.JSON_KEY_ISPREMIUMACCOUNT%>","0")
		            endif
		            
		            TxNode lastKnownLocationNode = Gps.getLastKnownLocation()
		            JSONObject locationJO = convertStopToJSON(lastKnownLocationNode)
		            if NULL != locationJO
		            	JSONObject.put(actionJo,"<%=Constant.JSON_KEY_LOCATION%>",locationJO)
		            endif
		            
		            # add keyword for backward compatibility
	            	JSONObject.put(actionJo,"keyword","v2")
		            
		            <%-- add app_start flag. 1 means first time, 0 is for subsequent calls --%>
	                String appStartFlag = getAppStartValue()
            	    JSONObject.put(actionJo,"<%=Constant.KEY_ADJUGGLER_APP_START%>",appStartFlag)
		            
		            String actionStr = JSONObject.toString(actionJo)
		            TxNode.addMsg(node,actionStr)
	                TxRequest req
					String url="<%=addonHost + "/CheckAdJuggler.do"%>"
					String scriptName="checkActionFromWebback"
					TxRequest.open(req,url)
					TxRequest.setRequestData(req,node)
					TxRequest.onStateChange(req,scriptName)
					TxRequest.send(req)
		        endfunc
	        
	        func checkActionFromWebback(TxNode node,int status)
	            checkBlueTooth()
		        if status == 0
				   #StartUp_M_clearAdJuggler()
				   return FAIL
				else
				   String resultJoString = TxNode.msgAt(node,0)
				   JSONObject jo = JSONObject.fromString(resultJoString)
				   AdJuggler_saveLastTime("LAST_TIME_AJUGGLER_POISEARCH")
				   saveAdJugglerObjectForPoiSearch(jo)
				   setAdJugglerMessage(jo)
				   return FAIL
				endif
		    endfunc
		    
		    func setAdJugglerMessage(JSONObject jo)
		        Page.setComponentAttribute("bannerAd","visible","1")
		        JSONObject textJSON = JSONObject.get(jo,"text")
		        String text = ""
		        if NULL != textJSON
		           String locale = AdJuggler_getLocale()
		           text = JSONObject.get(textJSON,locale)
		        endif
			    if NULL != text && "" != text
			       Page.setComponentAttribute("bannerAdLabel","text",text)
			       String campaignID = ""
			       if JSONObject.has(jo,"campaignID")
			          campaignID = JSONObject.get(jo,"campaignID")
			       endif
			       String pageName = JSONObject.get(jo,"pageName")
			      
			       String accountType = AdJuggler_getAccoutType()
			       AdJuggler_logForAdJuggler(<%=EventTypes.AD_BANNER_VIEW%>, pageName, campaignID, accountType, text)
			    endif
		    endfunc
		    
		    func hideAdJuggler()
		        Page.setComponentAttribute("bannerAd","visible","0")
		    endfunc
	        
	        func logAdBannerLog(int type,string message)
				if StatLogger.isStatEnabled(type)
					String accountType = AdJuggler_getAccoutType()
					JSONObject logJSON
					JSONObject.put(logJSON, "<%=AttributeID.AD_BANNER_MSG%>", message)
					JSONObject.put(logJSON, "<%=AttributeID.PAGE_NAME_TML%>", "<%=Constant.KEY_ADJUGGLER_FROM_POISEARCH%>")
					JSONObject.put(logJSON, "<%=AttributeID.ACCOUNT_TYPE%>", accountType)
					
					StatLogger.logEvent(type, logJSON)
				endif	        
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
					
					System.setKeyEventListener("-dial-","keypress")
					
					if ServerDriven_ShowPoiSearchBannerAd()
						updateBannerAd()
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
		        
		        func keypress(String s)
		            if "dial" == s
		                System.doAction("doSpeakSearch")
		                return TRUE
		            endif
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
					   
					   # Fix hard code bug: http://jira.telenav.com:8080/browse/TMORIM-759
					   String deleteSuccess = System.parseI18n("<%=msg.get("poi.delete.success")%>")
					   if "poi.delete.success" == deleteSuccess
							deleteSuccess = "History successfully deleted."
					   endif
					   
			           System.showErrorMsg(deleteSuccess)
 		           endif
 		           return FAIL
		        endfunc
		        
		        func inputClick()
		           Page.setControlProperty("searchButton","focused","true")
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

	<tml:menuItem name="doActionForPrem" onClick="doActionForAdJuggler" trigger="TRACKBALL_CLICK">	
	</tml:menuItem>
	<tml:menuItem name="dealWithURL" trigger="TRACKBALL_CLICK"/>
		
	<tml:page id="mainPagePoiPage" url="<%=getPage + "SearchPoi"%>" groupId="<%=GROUP_ID_POI%>"
		background="<%=imageBg%>" type="<%=pageType%>" showLeftArrow="true"
		showRightArrow="true" helpMsg="$//$search">
		<tml:bean name="hotBrandlist" valueType="TxNode"
			value="<%=Utility.TxNode2Base64(listNode)%>"></tml:bean>

		<tml:title id="searchLabel" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("poi.Search")%>
		</tml:title>

		<tml:menuItem name="autoFill" onClick="inputClick"  trigger="TRACKBALL_CLICK|KEY_MENU"/>

		<tml:inputBox id="poi" title="<%=msg.get("poi.what")%>" style="capital"
			titleAbove="false" titleFontWeight="system_large|bold"
			fontWeight="system_large" prompt="<%=msg.get("poi.what.prompt")%>"
			type="dropdownfilterfield">
			<tml:menuRef name="autoFill" />
			<tml:menuRef name="doSearchMenu" />
			<tml:menuRef name="deleteRecentSearch" />
		</tml:inputBox>
		
		<tml:compositeListItem id="category" getFocus="false"
			visible="true" bgColor="#FFFFFF" transparent="false"
			focusBgImage="<%=twoLinesImageFocus%>"
			blurBgImage="<%=twoLinesImageBlur%>" isFocusable="true">
			<tml:label id="categoryTitle" textWrap="ellipsis"
				fontWeight="bold|system_large" focusFontColor="white" align="left|middle">
				<%=msg.get("poi.category.title")%>
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
			focusBgImage="<%=twoLinesImageFocus%>"
			blurBgImage="<%=twoLinesImageBlur%>" isFocusable="true">
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

		<tml:button id="searchButton" text="<%=msg.get("poi.Search")%>"
			fontWeight="system_large"
			imageClick="<%=imageUrl + "button_small_on.png"%>"
			imageUnclick="<%=imageUrl + "button_small_off.png"%>">
			<tml:menuRef name="doSearch" />
			<tml:menuRef name="doSearchMenu" />
		</tml:button>
		<tml:block feature="<%=FeatureConstant.DSR%>">
			<tml:image id="speakSearchButton" url="<%=imageUrl + "speakSearch_en_CA.png"%>" />
		</tml:block>
		<tml:compositeListItem id="bannerAd" blurBgImage="<%=imageUrl+ "poi_banner_ad_text_bg.png"%>" focusBgImage="<%=imageUrl+ "poi_banner_ad_text_bg_highlight.png"%>">
			<tml:label id="bannerAdLabel" textWrap="ellipsis" fontWeight="system_small" focusFontColor="black" fontColor="black" align="middle|left"></tml:label>
			<tml:menuRef name="doActionForPrem" />
		</tml:compositeListItem>
	
		<cserver:outputLayout />
	</tml:page>
</tml:TML>
