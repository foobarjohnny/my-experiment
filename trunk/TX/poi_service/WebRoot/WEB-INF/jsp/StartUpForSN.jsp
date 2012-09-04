<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="Header.jsp"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.j2me.datatypes.TxNode"%>
<%@page import="org.json.me.JSONObject"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.cserver.stat.*"%>
<%@page import="com.telenav.cserver.billing.BillingConstants"%>

<%
	String pageURL = host + "/startUp.do?pageRegion=" + region;
	String purchasePageUrl = "{login.http}/getInterface.do?jsp=PurchaseInterface";
	final String USER_INFO_FREE_TRIAL = "USER_INFO_FREE_TRIAL";
	final String USER_INFO_PRODUCT_CODE = "USER_INFO_PRODUCT_CODE";
	
	int gpsValidTime = 240;
	int cellIdValidTime=1860;
	int gpsTimeout=12;
%>

<tml:TML outputMode="TxNode">
	<%@ include file="poi/controller/SearchPoiController.jsp"%>
	<%@ include file="ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="model/StartUpModel.jsp"%>
	<%@ include file="dsr/controller/DSRController.jsp"%>
	<%@ include file="/WEB-INF/jsp/model/PrefModel.jsp"%>
	<%@ include file="controller/ReferFriendController.jsp"%>
	<%@ include file="/WEB-INF/jsp/ac/model/SetUpHomeModel.jsp"%>
	<%@ include file="/WEB-INF/jsp/weather/controller/WeatherController.jsp"%>
	<jsp:include page="/WEB-INF/jsp/common/movie/controller/MovieController.jsp" />
	<jsp:include page="/WEB-INF/jsp/common/movie/controller/MovieListController.jsp" />
	<%@ include file="/WEB-INF/jsp/local_service/GetGps.jsp"%>
	<%@ include file="/WEB-INF/jsp/poi/controller/PoiListController.jsp"%>
	<%@ include file="/WEB-INF/jsp/StopUtil.jsp"%>
	
	<jsp:include 
	    page="/WEB-INF/jsp/AccountTypeForSprint.jsp"/>
	<jsp:include
		page="/WEB-INF/jsp/ac/controller/SelectAddressController.jsp" />
	<jsp:include
		page="/WEB-INF/jsp/local_service/controller/MapWrapController.jsp" />
	<jsp:include page="/WEB-INF/jsp/FreeTrialSDParamScriptForSprint.jsp" />
		
	<tml:script language="fscript" version="1">
		<![CDATA[
			<%@ include file="GetServerDriven.jsp"%>
			<%@ include file="BadgeScripts.jsp"%>
			<%@ include file="AdJugglerScript.jsp"%>
			func onBack()
				System.exit()
				return FAIL
			endfunc
			
			func checkTraffic()
				string text = "<%=msg.get("startup.Maps")%>"
				if ServerDriven_CanShowTraffic()
					text = "<%=msg.get("startup.MapsAndTraffic")%>"
				endif
				Page.setComponentAttribute("itemlabel2","text",text)
			endfunc
			
			func preLoad()
				checkUpgrade()
				checkTraffic()
			    AddressCapture_C_getCityString()
				SetUpHome_M_syncHomeFromClient()
			endfunc
			
			func onShow()
				string shortCutKey = "-" + "<%=Constant.SHORTCUT_KEY_UP%>" + "-" + "<%=Constant.SHORTCUT_KEY_DOWN%>"
				shortCutKey =  shortCutKey + "-"
				System.setKeyEventListener(shortCutKey,"onKeyPressed")
				checkAppNew()
				checkMyFavorite()
				homePageStatLogger()
				startRefreshAd()
				checkActionForLastItem()
			endfunc
			
			func onLoad()
				# Delete IN_LOGIN_FLOW flag
				# Set convienence key will check this flag.
				# If in, go to main screen.
				# If not in, exit browser shell.
				Cache.deleteFromTempCache("IN_LOGIN_FLOW")
				
				checkSprintUser()
				
				if isPremiumAccount()
					Page.setComponentAttribute("premiumBottom","visible","1")
					Page.setComponentAttribute("item4","visible","0")
				else
					logAdBannerLog(<%=EventTypes.AD_BANNER_VIEW%>,"PREM001")
					#PM new request show text from AdJuggler even when non prem user -- Chengbiao
					#Page.setComponentAttribute("premiumBottom","visible","0")
					#Page.setComponentAttribute("item4","visible","1")	
					Page.setComponentAttribute("premiumBottom","visible","1")
					Page.setComponentAttribute("item4","visible","0")						
				endif
	        endfunc
	
	 		func isFreeTrial()
		    	int freeTrial = UserInfoManager_getFreeTrial()
		        int freeTrialDays = FreeTrial_serverDriven_FreeTrialDays()
		        if 1 == freeTrial && 0 < freeTrialDays
		        	return 1
		        endif
		        return 0
		    endfunc
		    
			func UserInfoManager_getFreeTrial()
				TxNode node
				node = Cache.getCookie("<%=USER_INFO_FREE_TRIAL%>")
				if NULL == node
					return 0
				endif
				return TxNode.valueAt(node, 0) 	
			endfunc
	        
	        func UserInfoManager_setFreeTrial(int value)
		    	TxNode node
		    	TxNode.addValue(node, value)
		    	println("Store free trial flag " + value)
		    	Cache.saveCookie("<%=USER_INFO_FREE_TRIAL%>", node)
			endfunc
			
			##############################################################
			# PRODUCT CODE
			##############################################################
			func UserInfoManager_getProductCode()
				TxNode node
				node = Cache.getCookie("<%=USER_INFO_PRODUCT_CODE%>")
				if NULL == node
					return ""
				endif
				
				return TxNode.msgAt(node, 0)
			endfunc
			
	        func checkActionForLastItem()
	            TxNode node
	            JSONObject actionJo
	            JSONObject.put(actionJo,"<%=Constant.JSON_KEY_FROM%>","<%=Constant.KEY_ADJUGGLER_FROM_MAIN%>")
	            JSONObject adJugglerJO = StartUp_M_getAdJuggler()
	            
	            if NULL == adJugglerJO
	               hideAdJuggler()
	            else
	               setAdJugglerMessage(adJugglerJO)
	               if !AdJuggler_checkExpire(adJugglerJO,"LAST_TIME_AJUGGLER")
		              return FAIL
		           endif
	            endif
	            
	            if isPremiumAccount()
	               JSONObject.put(actionJo,"<%=Constant.JSON_KEY_ISPREMIUMACCOUNT%>","1")
	            else
	               JSONObject.put(actionJo,"<%=Constant.JSON_KEY_ISPREMIUMACCOUNT%>","0")
	            endif
	            
	            # add keyword for backward compatibility
	            JSONObject.put(actionJo,"keyword","v3")
	            
	            int freeTrialEligible = isFreeTrial()
	            JSONObject.put(actionJo,"<%=Constant.JSON_KEY_ISFREETRIALELIGIBLE%>",freeTrialEligible)
	            
	            String productCode = UserInfoManager_getProductCode()
	            JSONObject.put(actionJo,"<%=Constant.JSON_KEY_PRODUCTCODE%>",productCode)
	            
	            int serviceLevel = AccountTypeForSprint_GetServiceLevel()
	            JSONObject.put(actionJo,"<%=Constant.JSON_KEY_SERVICELEVEL%>",serviceLevel)
	            
	            
	            println("JSON TO " + actionJo)
	            TxNode lastKnownLocationNode = Gps.getLastKnownLocation()
	            JSONObject locationJO = convertStopToJSON(lastKnownLocationNode)
	            if NULL != locationJO
	            	JSONObject.put(actionJo,"<%=Constant.JSON_KEY_LOCATION%>",locationJO)
	            endif
	            
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
				   println("~~~~~~adjuggler callback failed~~~~~")
				   return FAIL
				else
				   String resultJoString = TxNode.msgAt(node,0)
				   println("~~~~~~result jo string " + resultJoString)
				   JSONObject jo = JSONObject.fromString(resultJoString)
				   AdJuggler_saveLastTime("LAST_TIME_AJUGGLER")
				   StartUp_M_saveAdJuggler(jo)
				   if JSONObject.has(jo,"<%=Constant.JSON_KEY_ISFREETRIALELIGIBLE%>")
			           String freeTrialFlag = JSONObject.get(jo,"<%=Constant.JSON_KEY_ISFREETRIALELIGIBLE%>")
			           println("~~~~~~~~~~~free trial flag returned from adjuggler~~~~~~~~~")
			           if "1" == freeTrialFlag
			           	UserInfoManager_setFreeTrial(1)
			           	println("~~~~~~~~~~~Set free trial flag from adjuggler~~~~~~~~~~")
			           endif
		           endif
				   setAdJugglerMessage(jo)
				   refreshAd()
				   return FAIL
				endif
		    endfunc
		    func refreshAd()
				String refreshState = shouldRefreshAd()	 
		      if refreshState == "YES"
		    	JSONObject adJugglerJO = StartUp_M_getAdJuggler()
			    int time = 0
			    if NULL != adJugglerJO
			    	time = AdJuggler_getTimeToExpire(adJugglerJO,"LAST_TIME_AJUGGLER")
			    endif
			 	if time > 0
			 		System.invokeWhenTimeout(time,"refreshAd")
				else
					checkActionForLastItem()
			    endif
			  endif
   			endfunc
		    func getFocusItem()
				 if TRUE == Page.getControlProperty("item0","isFocused")
				 	return "item0"
				 endif
				 if TRUE == Page.getControlProperty("item1","isFocused")
				 	return "item1"
				 endif
				 if TRUE == Page.getControlProperty("item2","isFocused")
				 	return "item2"
				 endif
				 if TRUE == Page.getControlProperty("item3","isFocused")
				 	return "item3"
				 endif
				 if TRUE == Page.getControlProperty("premiumBottom","isFocused")
				 	return "premiumBottom"
				 endif
				 return "item0"
			 endfunc
		    
		    func setAdJugglerMessage(JSONObject jo)
		    	String focusItem = getFocusItem()
		        Page.setComponentAttribute("premiumBottom","visible","1")
		        Page.setComponentAttribute("footerImage","visible","0")
		        
		        if JSONObject.has(jo,"iconImage")
		           String iconUrl = JSONObject.get(jo,"iconImage")
 		           Page.setControlProperty("bannerIcon", "image", iconUrl)
 		           Page.setComponentAttribute("bannerIcon","visible","1")
		        else
		           Page.setComponentAttribute("bannerIcon","visible","0")
		        endif
		        
		       	if JSONObject.has(jo,"backgroundImage")
		           JSONObject backgroundImageJO = JSONObject.get(jo,"backgroundImage")
		           String focusUrl = JSONObject.get(backgroundImageJO,"focusUrl")
		           String unfocusUrl = JSONObject.get(backgroundImageJO,"unfocusUrl")
		           Page.setComponentAttribute("premiumBottom","focusBgImage",focusUrl)
		           Page.setComponentAttribute("premiumBottom","blurBgImage",unfocusUrl)
		        else
		           Page.setComponentAttribute("premiumBottom","focusBgImage","<%=imageUrl + "home_bg2_on.png"%>")
		           Page.setComponentAttribute("premiumBottom","blurBgImage","<%=imageUrl + "home_bg2.png"%>")
		        endif
		        
		        JSONObject textJSON = JSONObject.get(jo,"text")
		        String text = ""
		        if NULL != textJSON
		           String locale = AdJuggler_getLocale()
		           text = JSONObject.get(textJSON,locale)
		        endif
			    if NULL != text && "" != text
			       Page.setComponentAttribute("actionMessage","text",text)
			       String campaignID = ""
			       if JSONObject.has(jo,"campaignID")
			          campaignID = JSONObject.get(jo,"campaignID")
			       endif
			       String pageName = JSONObject.get(jo,"pageName")
			      
			       String accountType = AdJuggler_getAccoutType()
			       AdJuggler_logForAdJuggler(<%=EventTypes.AD_BANNER_VIEW%>, pageName, campaignID, accountType, text)
			    endif
			    Page.setControlProperty(focusItem,"focused","true")
		    endfunc
		    
		    func hideAdJuggler()
		        Page.setComponentAttribute("premiumBottom","visible","0")
		        Page.setComponentAttribute("footerImage","visible","1")
		    endfunc
	        
	        func logAdBannerLog(int type,string message)
				if StatLogger.isStatEnabled(type)
					String accountType = AdJuggler_getAccoutType()
					JSONObject logJSON
					JSONObject.put(logJSON, "<%=AttributeID.AD_BANNER_MSG%>", message)
					JSONObject.put(logJSON, "<%=AttributeID.PAGE_NAME_TML%>", "<%=Constant.KEY_ADJUGGLER_FROM_MAIN%>")
					JSONObject.put(logJSON, "<%=AttributeID.ACCOUNT_TYPE%>", accountType)
					
					StatLogger.logEvent(type, logJSON)
				endif	        
	        endfunc
	        
	        func getReceivedAddress()
	        	int nReceivedAddress = 0
	        	TxNode node = Startup.getFlagInfos()
		    	if node != NULL
		    		int size = TxNode.getStringSize(node)
		    		if size > 2
		    			string receivedAddress = checkNULL(TxNode.msgAt(node,2))
		    			if String.isNumberString(receivedAddress)
		    				nReceivedAddress = String.convertToNumber(receivedAddress)
		    			endif
		    		endif 
		    	endif
		    	
		    	return nReceivedAddress
	        endfunc
	        
	        func checkMyFavorite()
		       	if 0 == ServerDriven_ShowNewAddressFlagOnMainPage()
		       	   showNewFav(FALSE)
		       	   return FAIL
		       	endif
		       	int receivedAddress = getReceivedAddress()
		       	if receivedAddress > 0
		       		showNewFav(TRUE)
		       		Page.setComponentAttribute("itemLabelFav","text",receivedAddress+"")
		       	else
		       		showNewFav(FALSE)
		       	endif
		    endfunc
			
			func showNewFav(int needShow)
				Page.setComponentAttribute("itemLabelFav","visible",needShow+"")
		      	Page.setComponentAttribute("itemImageNewFav","visible",needShow+"")
			endfunc
			
			func checkNULL(string s)
	        	if s== NULL
	        		return ""
	        	else
	        		return s	
	        	endif
	        endfunc
	        
	        func checkSprintUser()
	            int flag = 0
	            String text = "<%=PoiUtil.amend(msg.get("startup.title"))%>"
	            if AccountTypeForSprint_IsLiteUser()
	               flag = 1
	               text = "<%=PoiUtil.amend(msg.get("startup.title.lite"))%>"
	            elsif AccountTypeForSprint_IsBundleUser()
	               flag = 1
	            elsif AccountTypeForSprint_IsFreeTrial()
                   flag = 1
	        	   text = "<%=PoiUtil.amend(msg.get("startup.title.free"))%>"
	            else
	               text = "<%=PoiUtil.amend(msg.get("startup.title.premium"))%>"
	            endif
	            
	            MenuItem.setItemValid("item0",0,flag)
			    MenuItem.commitSetItemValid("item0")
	        	MenuItem.setItemValid("item1",0,flag)
	        	MenuItem.commitSetItemValid("item1")
	        	MenuItem.setItemValid("item2",0,flag)
	        	MenuItem.commitSetItemValid("item2")
	        	MenuItem.setItemValid("item3",0,flag)
	        	MenuItem.commitSetItemValid("item3")
	        	Page.setComponentAttribute("hometitle","text",text)
	        	
	        	TxNode node = UserInfo.getAccountInfo()
	        	if NULL != node && 0 != TxNode.getValueSize(node)
	        	   int status = TxNode.valueAt(node, 0)
		           if <%=BillingConstants.INFO_COMMON_TRIAL_END%> == status && FALSE == AccountTypeForSprint_IsBundleUser()
		              if 0 == StartUp_M_getExpireAlertFlag()
		                 StartUp_M_setExpireAlertFlag()
		                 System.showConfirm("<%=msg.get("startup.show.overdueMsg")%>","<%=msg.get("startup.update")%>","<%=msg.get("Cancel")%>","showOverDueCallBack")
					  	 return FAIL
		              endif
		           endif
	        	endif
	        endfunc
	        
	        func showOverDueCallBack(int selected)
	            if selected == 1
	        	   onClickUpgradeToPremium()
	        	   return FAIL
	        	endif
	        endfunc
	        
			func checkPoiLog()
	        	TxNode node = Cache.getCookie("POILogCookie")
	        	Cache.deleteCookie("POILogCookie")
	        	if NULL!=node
	        		MenuItem.setBean("searchPOI", "log", node)
	        	endif
	        	
				return TRUE
	        endfunc
	        
	        func homePageStatLogger()
				JSONObject empty
				if StatLogger.isStatEnabled(<%=EventTypes.CLICKSTREAM%>)
					StatLogger.clickStream()
				endif
				# time from startup till home screen
				if StatLogger.isStatEnabled(<%=EventTypes.HOME_SCREEN_TIME%>)
					int time = TempCache.getInt("LOGGIN_CHECK_ACCOUNT_TIME", -1)
					if time > 0
						JSONObject.put(empty, "<%=AttributeID.TIME_1%>", time)
						TempCache.deleteInt("LOGGIN_CHECK_ACCOUNT_TIME")
					endif
					StatLogger.eventEnds(<%=EventTypes.HOME_SCREEN_TIME%>, empty)
				endif
	        endfunc
	        
	        func onClickDriveTo1()
	        	if Cell.isCoverage()
		        	goToACPage()
				else
					ignoreCheck("False")
				endif
				return FAIL
	        endfunc
			
			func goToACPage()
					JSONObject jo
		        	JSONObject.put(jo,"title","<%=msg.get("selectaddress.title.driveto")%>")
		        	JSONObject.put(jo,"mask","10111111011")
		        	JSONObject.put(jo,"from","DriveTo")
		        	JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
					JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack + "DriveToWrap"%>")
					SelectAddress_C_SelectAddress(jo)
			endfunc
			
	        func checkCover()
	        	TxNode node 
       	        node = ParameterSet.getParam("checkCoverageBack")
       	        if TxNode.getStringSize(node)!=0
       	        	#check callback message
       	        	JSONObject msg = JSONObject.fromString(TxNode.msgAt(node,0))
       	        	String errorType
       	        	if JSONObject.has(msg,"Error")
 	      	       		errorType= JSONObject.get(msg,"Error")
 	      	       	else
 	      	       		errorType="back"
       	        	 endif
       	        	if errorType=="RadioIsOff"
						retry()
						return
					#go to AC page and show warning
       	        	else
       	        		SelectAddress_C_SetNoCoverage(TRUE)
       					goToACPage()	
       					return
    				endif
    			endif
	        endfunc
	        
	        func ignoreCheck(String needCheck)
	        		JSONObject ignore
					JSONObject.put(ignore,"IgnoreRadioOff",needCheck)
					String ignoreStr = JSONObject.toString(ignore)
					TxNode node
	        		TxNode.addMsg(node,ignoreStr)
	        		MenuItem.setBean("checkCoverage","IgnoreRadioOff",node)
					System.doAction("checkCoverage")
					return FAIL
	        endfunc

	        func retry()
	        	System.showConfirm("<%=msg.get("common.radio.off")%>","<%=msg.get("common.button.retry")%>","<%=msg.get("common.button.Ignore")%>","selectRetry")
	        	return FAIL
	        endfunc
	        
	        func selectRetry(int selected)
	        	if selected==1
	        		onClickDriveTo1()
	        	elsif selected==0
	        		ignoreCheck("True")
	        	else
	        		
	        	endif
	        endfunc
			
			func visibleImage(int i)
				string imageUrl = "<%=imageUrl%>" + "banner_" + i + ".png"
				Page.setControlProperty("imageTop","image",imageUrl)
			endfunc
			
			func onFocus(string id)
		    	int index = getItemIndex(id)
		    	visibleImage(index)
		    endfunc
		    
		    func getItemIndex(string id)
		    	int index = 0
		    	if "item0" == id
		    		index= 0
		    	elsif "item1" == id
		    		index= 1
		    	elsif "item2" == id
		    		index= 2
		    	elsif "item3" == id
		    		index= 3
		    	elsif "item4" == id
		    		index= 4
		    	endif
		    			    			
		    	return index
		    endfunc
			
			func mapOnClick()
				MapWrap_C_showCurrent()
				return FAIL
			endfunc
			
			func isSearchSpeakInput()
	 			return FALSE	       	
	        endfunc

	        func onClickSearch()
	        	checkPoiLog()
	        	if isSearchSpeakInput()
	        		JSONObject jo
					invokeSpeakSearch(jo)
	        	else
					SearchPoi_C_initial(5)
					SearchPoi_C_showSearch()        
	        	endif
	        	return FAIL	        	
	        endfunc
	        
	        func onClickApps()
	        	StartUp_M_clearAppNewFlag()
	        	checkAppNew()
	        endfunc
	        
	        func checkAppNew()
	        	String label = GetNewIndicatorLabel()
	        	int isNew
	        	if label == ""
		        	isNew = StartUp_M_getAppNewFlag()
			        if isNew
		        		label = "<%=msg.get("startup.New")%>"
		        	endif
	        	endif
	        	String text ="<red>"+label+"</red>"
	        	Page.setComponentAttribute("new","text",text)
	        endfunc

		    func StartUp_M_getListItemSize()
		    	int size = 4
		    	return size
		    endfunc
		    
		    func checkUpgrade()
		    	int flag = StartUp_M_getUpgradeFlag()
		    	MenuItem.setItemValid("item0",2,flag)
		    	MenuItem.commitSetItemValid("item0")
        		MenuItem.setItemValid("item1",2,flag)
        		MenuItem.commitSetItemValid("item1")
        		MenuItem.setItemValid("item2",2,flag)
        		MenuItem.commitSetItemValid("item2")
        		MenuItem.setItemValid("item3",2,flag)
        		MenuItem.commitSetItemValid("item3")
        		MenuItem.setItemValid("item4",2,flag)
        		MenuItem.commitSetItemValid("item4")
		    endfunc	
		    
		    func goToReferFriends()
		        ReferFriend_C_initalAndGoTo()
		        return FAIL
		    endfunc

			func onClickUpgrade()
				string upgradeUrl = StartUp_M_getUpgradeUrl()
				if "" != upgradeUrl
					TxNode node
					TxNode.addMsg(node,upgradeUrl)
					TxNode.addMsg(node,"TRUE")
					MenuItem.setBean("callLocalBrowserMenu", "url", node)
					System.doAction("callLocalBrowserMenu")
				endif
				return FAIL
			endfunc
			
			func onClickUpgradeToPremium()
				logAdBannerLog(<%=EventTypes.AD_BANNER_CLICK%>,"PREMMENU001")
				AdJuggler_setJSONForPurchase("PREMMENU001", "<%=Constant.KEY_ADJUGGLER_FROM_MAIN%>")
			    Purchase_C_purchase(1)
		        return FAIL
			endfunc
			
			func onClickUpgradeToPremiumViaClick()
				logAdBannerLog(<%=EventTypes.AD_BANNER_CLICK%>,"PREM001")
				Purchase_C_purchase(1)
		        return FAIL
			endfunc
			
			func upgradeCallBack(int selected)
			    if 1 == selected
		           Purchase_C_purchase(1)
		           return FAIL
		        endif
			endfunc
		    			
			func Purchase_C_purchase(int accountStatus)
				TxNode node
				TxNode.addValue(node, accountStatus)
				MenuItem.setBean("Purchase", "accountStatusNode", node)
				
				System.doAction("Purchase")
				return FAIL
			endfunc
			
			func isPremiumAccount()
				int flag = TRUE
	            if AccountTypeForSprint_IsLiteUser() || AccountTypeForSprint_IsBundleUser() || AccountTypeForSprint_IsFreeTrial()
	               flag = FALSE
	            endif
	            
	            return flag			
			endfunc
			
			func checkBlueTooth()										
				#Move the method from onLoad to here to avoid network conflit
				#Check Blue tooth GPS local service
				System.doAction("checkBlueTooth")
				return FAIL			
			endfunc
			
			func doActionForAdJuggler()
			    JSONObject adJugglerJO = StartUp_M_getAdJuggler()
			    AdJuggler_doActionForPremClick(adJugglerJO)
			    return FAIL
			endfunc
		]]>

	</tml:script>
	<tml:actionItem name="getGPSForMovie" action="getGPS"
		progressBarText="<%=msg.get("mSearch.bar.gps")%>">
		<tml:input name="locParam"></tml:input>
		<tml:output name="currentLocation" />
	</tml:actionItem>
	<tml:menuItem actionRef="getGPSForMovie" name="doGetGpsForMovie" onClick="AdJuggler_getLocationForMovieForMovie"/>

	<tml:menuItem name="Purchase" pageURL="<%=purchasePageUrl%>"/>
	<tml:actionItem name="checkBlueTooth" action="CheckBlueTooth"></tml:actionItem>
	<tml:menuItem name="checkBlueTooth" actionRef="checkBlueTooth">
	</tml:menuItem>

	<tml:menuItem name="about" text="<%=msg.get("startup.menu.About")%>"
		trigger="KEY_MENU" onClick="exitFromHomePage" pageURL="<%=getPage + "AboutMenu"%>">
	</tml:menuItem>
	
	<tml:menuItem name="productTour" text="<%=msg.get("startup.menu.ProductTour")%>"
		trigger="KEY_MENU" onClick="exitFromHomePage" pageURL="<%=getPage + "ProductTour"%>">
	</tml:menuItem>
	
	<tml:block feature="<%=FeatureConstant.REFER_FRIEND%>">
		<tml:menuItem name="referFriend" text="<%=msg.get("startup.menu.ReferFriends")%>"
			trigger="KEY_MENU" onClick="goToReferFriends">
		</tml:menuItem>
	</tml:block>
	
	<tml:actionItem name="callLocalBrowser" action="<%=Constant.LOCALSERVICE_INVOKEPHONEBROWSER%>">
		<tml:input name = "url"/>
	</tml:actionItem>
	<tml:menuItem name="callLocalBrowserMenu" actionRef="callLocalBrowser" trigger="TRACKBALL_CLICK" />
	
	<tml:menuItem name="upgrade" onClick="onClickUpgrade" text="<%=msg.get("startup.menu.Upgrade")%>" trigger="KEY_MENU" />
	<tml:menuItem name="upgradeToPremium" onClick="onClickUpgradeToPremiumViaClick" trigger="TRACKBALL_CLICK" />
	<tml:menuItem name="upgradeToPremiumMenu" onClick="onClickUpgradeToPremium" text="<%=msg.get("startup.upgradeSprint")%>" trigger="KEY_MENU" />
		
	<tml:actionItem name="doCheckCoverage" action="DriveToOffCoverageCheck">
		<tml:input name="IgnoreRadioOff"/>
		<tml:output name="checkCoverageBack" />
	</tml:actionItem>
	<tml:menuItem name="checkCoverage" actionRef="doCheckCoverage"
		onClick="checkCover" />
		
	<tml:menuItem name="showMap" onClick="mapOnClick"
		trigger="TRACKBALL_CLICK">
	</tml:menuItem>
	<tml:menuItem name="home" pageURL="<%=pageURL%>">
	</tml:menuItem>
	<tml:menuItem name="toolsMenu" onClick="onClickApps" pageURL="<%=getPage + "ToolsMain"%>"> trigger="TRACKBALL_CLICK">
	</tml:menuItem>
	
	<tml:menuItem name="searchPOI" onClick="onClickSearch" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="driveToItemAction1" onClick="onClickDriveTo1" trigger="TRACKBALL_CLICK"/>								
	<tml:actionItem name="SyncResource"
		action="<%=Constant.LOCALSERVICE_SYNCRESOURCE%>">
	</tml:actionItem>
	<tml:menuItem name="syncResouceMenu" actionRef="SyncResource"
		trigger="KEY_RIGHT | TRACKBALL_CLICK" />
	<tml:menuItem name="doActionForPrem" onClick="doActionForAdJuggler" trigger="TRACKBALL_CLICK">
	</tml:menuItem>
	<tml:menuItem name="dealWithURL" trigger="TRACKBALL_CLICK"/>
		
	<tml:page id="StartUp" url="<%=pageURL%>" type="<%=pageType%>" genericMenu="16"
		showLeftArrow="true" showRightArrow="true" helpMsg="$//$main" groupId="<%=GROUP_ID_COMMOM%>" defaultSelectedMenu="system_64" >

		<tml:image id="titleImage" url="<%=imageUrl + "hometitle.png"%>" />
		<tml:image id="imageTop" url="<%=imageUrl + "banner_0.png"%>" />
		<tml:label id="hometitle" align="center|middle" fontWeight="bold|system_large" fontColor="white">
			<%=PoiUtil.amend(msg.get("startup.title"))%>
		</tml:label>
			
		<tml:compositeListItem id="item0" visible="true" bgColor="#FFFFFF" isFocusable="true"
			transparent="false" focusBgImage="<%=imageUrl
								+ "home-list-item-on.png"%>"
			blurBgImage="<%=imageUrl + "home-list-item-off.png"%>">
			<tml:param name="onFocus" value="onFocus"/>
			<tml:image id="itemImage0" url="<%=imageUrl + "button_driveto.png"%>" />
			<tml:label id="itemlabel0" focusFontColor="white" 
				fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
				<%=msg.get("startup.DriveTo")%>
			</tml:label>
			<tml:image id="itemImageNewFav" url="<%=imageUrl+  "new_fav_bg.png"%>" />
			<tml:label id="itemLabelFav" fontColor="white"
				fontWeight="bold|system_small" 
				align="center|middle">
			</tml:label>
			<tml:menuRef name="upgradeToPremiumMenu" />
			<tml:menuRef name="driveToItemAction1" />
			<tml:menuRef name="upgrade" />
			<tml:menuRef name="about" />
			<tml:menuRef name="referFriend" />
		</tml:compositeListItem>
		<tml:compositeListItem id="item1" visible="true" bgColor="#FFFFFF" isFocusable="true"
			transparent="false" focusBgImage="<%=imageUrl
								+ "home-list-item-on.png"%>"
			blurBgImage="<%=imageUrl + "home-list-item-off.png"%>">
			<tml:param name="onFocus" value="onFocus"/>
			<tml:image id="itemImage1" url="<%=imageUrl + "button_search.png"%>" />
			<tml:label id="itemlabel1" focusFontColor="white" 
				fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
				<%=msg.get("startup.Search")%>
			</tml:label>
			<tml:menuRef name="upgradeToPremiumMenu" />
			<tml:menuRef name="searchPOI" />
			<tml:menuRef name="upgrade" />
			<tml:menuRef name="about" />
			<tml:menuRef name="referFriend" />
		</tml:compositeListItem>
		<tml:compositeListItem id="item2" visible="true" bgColor="#FFFFFF" isFocusable="true"
			transparent="false" focusBgImage="<%=imageUrl
								+ "home-list-item-on.png"%>"
			blurBgImage="<%=imageUrl + "home-list-item-off.png"%>">
			<tml:param name="onFocus" value="onFocus"/>
			<tml:image id="itemImage2" url="<%=imageUrl + "button_mapstraffic.png"%>" />
			<tml:label id="itemlabel2" focusFontColor="white" 
				fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
			</tml:label>
			<tml:menuRef name="upgradeToPremiumMenu" />
			<tml:menuRef name="showMap" />
			<tml:menuRef name="upgrade" />
			<tml:menuRef name="about" />
			<tml:menuRef name="referFriend" />
		</tml:compositeListItem>
		<tml:compositeListItem id="item3" visible="true" bgColor="#FFFFFF" isFocusable="true"
			transparent="false" focusBgImage="<%=imageUrl
								+ "home-list-item-on.png"%>"
			blurBgImage="<%=imageUrl + "home-list-item-off.png"%>">
			<tml:param name="onFocus" value="onFocus"/>
			<tml:image id="itemImage3" url="<%=imageUrl + "button_tools.png"%>" />
			<tml:label id="itemlabel3" focusFontColor="white" 
				fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
				<%=msg.get("startup.Apps")%>
			</tml:label>
			<tml:label id="new" focusFontColor="white" 
				fontWeight="bold|system_small" textWrap="ellipsis" align="right|middle">
			</tml:label>
			<tml:menuRef name="upgradeToPremiumMenu" />
			<tml:menuRef name="toolsMenu" />
			<tml:menuRef name="upgrade" />
			<tml:menuRef name="about" />
			<tml:menuRef name="referFriend" />
		</tml:compositeListItem>
		<tml:compositeListItem id="item4" visible="false" bgColor="#FFFFFF" isFocusable="true"
			transparent="false" focusBgImage="<%=imageUrl + "home_bg2_on.png"%>"
			blurBgImage="<%=imageUrl + "home_bg2.png"%>">
			<tml:param name="onFocus" value="onFocus"/>
			<tml:label id="purchaseMessage" fontWeight="system_small"  align="left|middle" focusFontColor="black" fontColor="white" textWrap="ellipsis">
				<%=msg.get("startup.purchaseMessage")%>
			</tml:label>
			<tml:menuRef name="upgradeToPremiumMenu" />
			<tml:menuRef name="upgradeToPremium" />
			<tml:menuRef name="upgrade" />
			<tml:menuRef name="about" />
			<tml:menuRef name="referFriend" />
		</tml:compositeListItem>
		<tml:compositeListItem id="premiumBottom" visible="false" bgColor="#FFFFFF" isFocusable="true"
			transparent="false" focusBgImage="<%=imageUrl + "home_bg2_on.png"%>"
			blurBgImage="<%=imageUrl + "home_bg2.png"%>">
		<tml:image id="bannerIcon" />
			<tml:label id="actionMessage" fontWeight="system_small"  align="left|middle" focusFontColor="black" fontColor="white" textWrap="ellipsis">
			</tml:label>
			<tml:menuRef name="doActionForPrem" />
		</tml:compositeListItem>
		<tml:image id="footerImage" url="<%=imageUrl + "footer-premium.png"%>" align="left|top"/>	
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>
