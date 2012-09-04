<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
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
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>
<%
	int gpsValidTime = 240;
	int cellIdValidTime=1860;
	int gpsTimeout=12;
	final String USER_INFO_FREE_TRIAL = "USER_INFO_FREE_TRIAL";
	final String USER_INFO_PRODUCT_CODE = "USER_INFO_PRODUCT_CODE";
%>
	<%@ include file="poi/controller/SearchPoiController.jsp"%>
	<%@ include file="ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="model/StartUpModel.jsp"%>
	<%@ include file="dsr/controller/DSRController.jsp"%>
	<%@ include file="/touch62/jsp/model/PrefModel.jsp"%>
	<%@ include file="controller/ReferFriendController.jsp"%>
	<%@ include file="/touch62/jsp/ac/model/SetUpHomeModel.jsp"%>
	<%if(TnUtil.isTMOUser(product)){%>
	<jsp:include page="/touch62/jsp/AccountTypeForTMO.jsp"/>
	<%}else if (TnUtil.isVNUser(product)) {%>
	<jsp:include page="/touch62/jsp/AccountTypeForVN.jsp"/>
	<%}else{%>
	<jsp:include page="/touch62/jsp/AccountTypeForSprint.jsp"/>
	<%}%>
	<jsp:include page="/touch62/jsp/ac/controller/SelectAddressController.jsp" />
	<jsp:include page="/touch62/jsp/local_service/controller/MapWrapController.jsp" />
	<jsp:include page="/touch62/jsp/FreeTrialSDParamScriptForSprint.jsp" />
	
	
	<%@ include file="/touch62/jsp/model/OneBoxModel.jsp"%>
	<%@ include file="/touch62/jsp/local_service/GetGps.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
			<%@ include file="GetServerDriven.jsp"%>
			<%@ include file="AdJugglerScript.jsp"%>
			<%@ include file="BadgeScripts.jsp"%>
			func pageCallBack(int type) 
				if 3 == type 
					checkMyFavorite()
				elsif 5 == type
					onClickGoToOneBox()
				endif
				return FAIL
			endfunc
			
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
			
			func displayButtonImage()
				string locale = Pref_M_getLocale()
				string imageUrl = "<%=imageUrl%>" + "icon-for-new_" + locale + ".png"
				
				Page.setControlProperty("itemImageNew","image",imageUrl)
			endfunc
			
			func preLoad()
				checkAppNew()
				checkUpgrade()
				checkTraffic()
				<% if( TnUtil.isTMORIM62(handlerGloble) ) { %>
					displayButtonImage()
				<% } %>	
			    AddressCapture_C_getCityString()
			    TxNode hotBrandNode = ParameterSet.getParam("hotBrandlist")
			    OneBox_M_setHotBrandNode(hotBrandNode)
				SetUpHome_M_syncHomeFromClient()
				checkDSRAvail()
				
				checkSprintUser()
				<%if(!PoiUtil.isWarrior(handlerGloble)){%>
				   Page.addGenericMenu(64)
				<%}%>
			endfunc
			
			func onShow()
				checkServerDrivenForMenu()
				
				string shortCutKey = "-" + "<%=Constant.SHORTCUT_KEY_UP%>" + "-" + "<%=Constant.SHORTCUT_KEY_DOWN%>"
				shortCutKey =  shortCutKey + "-"
				System.setKeyEventListener(shortCutKey,"onKeyPressed")
				homePageStatLogger()
				checkMyFavorite()
				<%if(TnUtil.isSprint62(handlerGloble) || TnUtil.isTMORIM62(handlerGloble) || TnUtil.isVNRIM62(handlerGloble)|| TnUtil.isTMOAndroid62(handlerGloble) || TnUtil.isBoostRIM62(handlerGloble) ){%>
				if Cell.isCoverage()
					startRefreshAd()
					checkActionForLastItem()
				endif
				<%}%>
				println("---System.delPages begin")
				System.delPages(1)
				Uitility.clearLocalRelatedData()
				println("---Uitility.clearLocalRelatedData end")
				<%if(!PoiUtil.isWarrior(handlerGloble)){%>
				 	Page.preLoadPageToCache("<%=host + "/selectAddress.do?pageRegion=" + region + "&jsp=SelectAddress"%>")
				<%}%>
				System.setKeyEventListener("-up-down-","onKeyPressed")
			endfunc
			
		        func onKeyPressed(string s)
		        	if s == "down"
		        		int focusIdx = getFocusButton()
		        		if focusIdx >= 0 
		        			int nextFocusIdx = getNextFocusButton(focusIdx)
		        			if nextFocusIdx >= 0
		        				Page.setControlProperty("item"+nextFocusIdx,"focused","true")
		        				return TRUE
		        			else
		        				Page.setControlProperty("premiumBottom","focused","true")
		        				return TRUE
		        			endif
		        		endif
		        	elsif s == "up"
		        		int focusIdx = getFocusButton()
		        		if focusIdx >= 0 
		        			int preFocusIdx = getPreFocusButton(focusIdx)
		        			if preFocusIdx >= 0
		        				Page.setControlProperty("item"+preFocusIdx,"focused","true")
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
		        	int size = 4
		        	while i < size
		        		if TRUE == Page.getControlProperty("item"+i,"isFocused")
		        			return i
		        		endif
		        		i = i+1
		        	endwhile
		        	return -1
		        endfunc
		        
		        func getNextFocusButton(int orginIdx)
		        	int i = orginIdx + 1
		        	int size = 4
		        	int orginx = Page.getControlProperty("item"+orginIdx,"x")
		        	while i < size
		        		if orginx == Page.getControlProperty("item"+i,"x")
		        			return i
		        		endif
		        		i = i+1
		        	endwhile
		        	return -1
		        endfunc
		        
		        func getPreFocusButton(int orginIdx)
		        	int i = orginIdx - 1
		        	int orginx = Page.getControlProperty("item"+orginIdx,"x")
		        	while i >= 0
		        		if orginx == Page.getControlProperty("item"+i,"x")
		        			return i
		        		endif
		        		i = i-1
		        	endwhile
		        	return -1
		        endfunc
			
			func onLoad()
				println("--in onLoad")
				# Delete IN_LOGIN_FLOW flag
				# Set convienence key will check this flag.
				# If in, go to main screen.
				# If not in, exit browser shell.
				Cache.deleteFromTempCache("IN_LOGIN_FLOW")
				# Check Blue tooth GPS local service

			    showPopupWhenLoad()
			    <%if(!TnUtil.isSprint62(handlerGloble) && !TnUtil.isVNRIM62(handlerGloble) && !TnUtil.isTMORIM62(handlerGloble)&& !TnUtil.isTMOAndroid62(handlerGloble)){%>
				System.doAction("checkBlueTooth")
				<%} else {%>
				if isPremiumAccount()
					Page.setComponentAttribute("premiumBottom","visible","1")
					Page.setComponentAttribute("item4","visible","0")
				else
					logAdBannerLog(<%=EventTypes.AD_BANNER_VIEW%>,"PREM001")
					Page.setComponentAttribute("premiumBottom","visible","1")
					Page.setComponentAttribute("item4","visible","0")						
				endif
				<%}%>
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
				println("~~~~~~~~~~~~fire check adjuggler call~~~~~~~~")
	        endfunc
	       
	        func checkActionFromWebback(TxNode node,int status)
	            println("~~~~~~~~~~~~back~~~~~~~~")
	            checkBlueTooth()
		        if status == 0
				   #StartUp_M_clearAdJuggler()
				   println("~~~~~~~~~~~~back fail~~~~~~~~")
				   return FAIL
				else
				   String resultJoString = TxNode.msgAt(node,0)
				   println("~~~~~~~~~~~~back~~~~~~~~ + resultJoString")
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
		        
		        <%if(!PoiUtil.isDual(handlerGloble)){%>
		       	if JSONObject.has(jo,"backgroundImage")
		           JSONObject backgroundImageJO = JSONObject.get(jo,"backgroundImage")
		           String focusUrl = JSONObject.get(backgroundImageJO,"focusUrl")
		           String unfocusUrl = JSONObject.get(backgroundImageJO,"unfocusUrl")
		           Page.setComponentAttribute("premiumBottom","focusBgImage",focusUrl)
		           Page.setComponentAttribute("premiumBottom","blurBgImage",unfocusUrl)
		        else
		           Page.setComponentAttribute("premiumBottom","focusBgImage","<%=imageUrl + "home-list-premium-on.png"%>")
		           Page.setComponentAttribute("premiumBottom","blurBgImage","<%=imageUrl + "home-list-premium.png"%>")
		        endif
		        <%}%>
		        
		        JSONObject textJSON = JSONObject.get(jo,"text")
		        String text = ""
		        if NULL != textJSON
		           String locale = AdJuggler_getLocale()
		           text = JSONObject.get(textJSON,locale)
		        endif
			    if NULL != text
			       String tmp = String.trim(text)
			       if tmp != ""
				       if JSONObject.has(jo,"iconImage")
				          Page.setComponentAttribute("nonIconActionMessage","visible","0")
				          Page.setComponentAttribute("actionMessage","visible","1")
				          Page.setComponentAttribute("actionMessage","text",tmp)
				       else
				          Page.setComponentAttribute("nonIconActionMessage","visible","1")
				          Page.setComponentAttribute("actionMessage","visible","0")
				          Page.setComponentAttribute("nonIconActionMessage","text",tmp)
				       endif
				       String campaignID = ""
				       if JSONObject.has(jo,"campaignID")
				          campaignID = JSONObject.get(jo,"campaignID")
				       endif
				       String pageName = JSONObject.get(jo,"pageName")
				      
				       String accountType = AdJuggler_getAccoutType()
				       AdJuggler_logForAdJuggler(<%=EventTypes.AD_BANNER_VIEW%>, pageName, campaignID, accountType, tmp)
			       endif
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
					JSONObject.put(logJSON, "<%=AttributeID.CAMPAIGN_VERSION%>", "V1")
					
					StatLogger.logEvent(type, logJSON)
				endif	        
	        endfunc
	        
	        func checkServerDrivenForMenu()
	            int canTellFriends = ServerDriven_CanTellFriends()
	            MenuItem.setItemValid("page",4,canTellFriends)
		    	MenuItem.commitSetItemValid("page")
	            MenuItem.setItemValid("item0",5,canTellFriends)
		    	MenuItem.commitSetItemValid("item0")
        		MenuItem.setItemValid("item1",5,canTellFriends)
        		MenuItem.commitSetItemValid("item1")
        		MenuItem.setItemValid("item2",5,canTellFriends)
        		MenuItem.commitSetItemValid("item2")
        		MenuItem.setItemValid("item3",5,canTellFriends)
        		MenuItem.commitSetItemValid("item3")
        		MenuItem.setItemValid("dsrButton",5,canTellFriends)
        		MenuItem.commitSetItemValid("dsrButton")
        		MenuItem.setItemValid("sprintUpgradeButton",4,canTellFriends)
        		MenuItem.commitSetItemValid("sprintUpgradeButton")
        		MenuItem.setItemValid("oneBoxSearchButton",2,canTellFriends)
        		MenuItem.commitSetItemValid("oneBoxSearchButton")
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
	            String text = "<%=msg.get("startup.title")%>"
	            if AccountTypeForSprint_IsLiteUser()||AccountTypeForTMO_IsLiteUser()
	               flag = 1
	               text = "<%=msg.get("startup.title.lite")%>"
	               <%if(TnUtil.isBAWPAIDUser(handlerGloble)){%>
	               		text = "<%=msg.get("startup.title.lite.paid")%>"
	               <%}%>
	               Page.setComponentAttribute("sprintUpgradeButton","visible","1")
	               Page.setComponentAttribute("imagePrem","visible","0")
	            elsif AccountTypeForSprint_IsBundleUser()
	               flag = 1
	               Page.setComponentAttribute("sprintUpgradeButton","visible","1")
	               Page.setComponentAttribute("imagePrem","visible","0")
	            elsif AccountTypeForSprint_IsFreeTrial()
                   flag = 1
	        	   text = "<%=msg.get("startup.title.free")%>"
	        	   Page.setComponentAttribute("sprintUpgradeButton","visible","1")
	        	   Page.setComponentAttribute("imagePrem","visible","1")
	            else
	               text = "<%=msg.get("startup.title.premium")%>"
	               <%if(TnUtil.isBAWPAIDUser(handlerGloble)){%>
	               		text = "<%=msg.get("startup.title.premium.paid")%>"
	               <%}%>
	        	   Page.setComponentAttribute("sprintUpgradeButton","visible","0")
	        	   Page.setComponentAttribute("imagePrem","visible","1")
	            endif
	            
	            MenuItem.setItemValid("item0",1,flag)
			    MenuItem.commitSetItemValid("item0")
	        	MenuItem.setItemValid("item1",1,flag)
	        	MenuItem.commitSetItemValid("item1")
	        	MenuItem.setItemValid("item2",1,flag)
	        	MenuItem.commitSetItemValid("item2")
	        	MenuItem.setItemValid("item3",1,flag)
	        	MenuItem.commitSetItemValid("item3")
	        	MenuItem.setItemValid("dsrButton",1,flag)
		    	MenuItem.commitSetItemValid("dsrButton")
		    	MenuItem.setItemValid("page",0,flag)
		    	MenuItem.commitSetItemValid("page")
	        	Page.setComponentAttribute("titleLabel","text",text)
		    	
		    	#if not tmo nor vn user
		    	<%if(!TnUtil.isTMOUser(product) && !TnUtil.isVNUser(product)){%>
		            MenuItem.setItemValid("item0",2,0)
				    MenuItem.commitSetItemValid("item0")
		        	MenuItem.setItemValid("item1",2,0)
		        	MenuItem.commitSetItemValid("item1")
		        	MenuItem.setItemValid("item2",2,0)
		        	MenuItem.commitSetItemValid("item2")
		        	MenuItem.setItemValid("item3",2,0)
		        	MenuItem.commitSetItemValid("item3")
		        	MenuItem.setItemValid("dsrButton",2,0)
			    	MenuItem.commitSetItemValid("dsrButton")
			    	MenuItem.setItemValid("page",1,0)
			    	MenuItem.commitSetItemValid("page")
		    	<%}else if(TnUtil.isVNUser(product)){%>
			    	#if VN user

				    # for verizon, users cannot downgrade to free.
				    flag = 1	
		            MenuItem.setItemValid("item0",2,!flag)
				    MenuItem.commitSetItemValid("item0")
		        	MenuItem.setItemValid("item1",2,!flag)
		        	MenuItem.commitSetItemValid("item1")
		        	MenuItem.setItemValid("item2",2,!flag)
		        	MenuItem.commitSetItemValid("item2")
		        	MenuItem.setItemValid("item3",2,!flag)
		        	MenuItem.commitSetItemValid("item3")
		        	MenuItem.setItemValid("dsrButton",2,!flag)
			    	MenuItem.commitSetItemValid("dsrButton")
			    	MenuItem.setItemValid("page",1,!flag)
			    	MenuItem.commitSetItemValid("page")
			    <%}else{%>
			        #if tmo user
			        
			        MenuItem.setItemValid("item0",2,!flag)
				    MenuItem.commitSetItemValid("item0")
		        	MenuItem.setItemValid("item1",2,!flag)
		        	MenuItem.commitSetItemValid("item1")
		        	MenuItem.setItemValid("item2",2,!flag)
		        	MenuItem.commitSetItemValid("item2")
		        	MenuItem.setItemValid("item3",2,!flag)
		        	MenuItem.commitSetItemValid("item3")
		        	MenuItem.setItemValid("dsrButton",2,!flag)
			    	MenuItem.commitSetItemValid("dsrButton")
			    	MenuItem.setItemValid("page",1,!flag)
			    	MenuItem.commitSetItemValid("page")
		    	<%}%>
	        endfunc

			func showPopupWhenLoad()
				<%if(!TnUtil.isTMOUser(product) && !TnUtil.isVNUser(product)){%>
					TxNode node = UserInfo.getAccountInfo()
					if NULL != node && 0 != TxNode.getValueSize(node)
						int status = TxNode.valueAt(node, 0)
						println("status......................."+status)
		                if <%=BillingConstants.INFO_COMMON_TRIAL_END%> == status || <%=BillingConstants.INFO_COMMON_TRIAL_EXPIRED%> == status
					  		System.showConfirm("<%=msg.get("startup.show.overdueMsg")%>","<%=msg.get("startup.update")%>","<%=msg.get("Cancel")%>","showOverDueCallBack")
							return FAIL
						endif
					endif
				<%}%>
			endfunc
	        
	        func showOverDueCallBack(int selected)
	            if selected == 1
	        	   onClickUpgradeToPremium()
	        	   return FAIL
	        	else
	        	   System.exit()
	        	endif
	        endfunc
	        
	        func showStartFreeSN(int index)
	        	System.showErrorMsg("<%=msg.get("startup.startFreeSN")%>")
	        	return FAIL	        
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
	       	 	#syn home again in case client does not finish the syn.
	        	SetUpHome_M_syncHomeFromClient()
	        	if Cell.isCoverage()
		        	goToACPage()
				else
					<%if(TnUtil.isTMOUser(product) || TnUtil.isVNUser(product)){%>
					if ( AccountTypeForSprint_IsLiteUser() || AccountTypeForTMO_IsLiteUser())
			        	System.showErrorMsg("<%=msg.get("common.network.error")%>")
			        	return FAIL
			        else
			        	ignoreCheck("False")
			        endif
					<%}else{%>
						ignoreCheck("False")
					<%}%>
				endif
				return FAIL
	        endfunc
			
			func goToACPage()
					JSONObject jo
		        	JSONObject.put(jo,"title","<%=msg.get("selectaddress.title.driveto")%>")
		        	JSONObject.put(jo,"mask","10111111111")
		        	JSONObject.put(jo,"from","DriveTo")
		        	JSONObject.put(jo,"returnAsIs","1")
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
						return FAIL
					#go to AC page and show warning
       	        	else
       	        		SelectAddress_C_SetNoCoverage(TRUE)
       					goToACPage()	
       					return FAILWITHOUTREFRESH
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
				string imageUrl = "$imageTopTemp" + i
				Page.setControlProperty("imageTop","image",imageUrl)
			endfunc
			
			func mapOnClick()
				MapWrap_C_showCurrent()
				return FAIL
			endfunc
			
			func isSearchSpeakInput()
	 			return FALSE	       	
	        endfunc

	        func onClickSearch()
	        	println("--onClickSearch")
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
	        	GetNewIndicatorLabel()
	        endfunc
	        
	        func checkAppNew()
	        	string text = ""
	        	int isNew = StartUp_M_getAppNewFlag()
	        	if isNew
	        		text = "<%=msg.get("startup.New")%>"
	        	else	
	        	endif
	        	Page.setComponentAttribute("new","text",text)
	        	Page.setComponentAttribute("itemImageNew","visible",isNew + "")
	        	if isNew
	        		Page.setComponentAttribute("itemlabel3","visible",1 + "")
	        		Page.setComponentAttribute("itemlabel3_1","visible",0 + "")
	        	else	
	        		Page.setComponentAttribute("itemlabel3","visible",0 + "")
	        		Page.setComponentAttribute("itemlabel3_1","visible",1 + "")
	        	endif	
	        endfunc

		    func StartUp_M_getListItemSize()
		    	int size = 4
		    	return size
		    endfunc
		    
		    func checkUpgrade()
		    	int flag = StartUp_M_getUpgradeFlag()
		    	MenuItem.setItemValid("item0",3,flag)
		    	MenuItem.commitSetItemValid("item0")
        		MenuItem.setItemValid("item1",3,flag)
        		MenuItem.commitSetItemValid("item1")
        		MenuItem.setItemValid("item2",3,flag)
        		MenuItem.commitSetItemValid("item2")
        		MenuItem.setItemValid("item3",3,flag)
        		MenuItem.commitSetItemValid("item3")
        		MenuItem.setItemValid("sprintUpgradeButton",2,flag)
        		MenuItem.commitSetItemValid("sprintUpgradeButton")
		    	MenuItem.setItemValid("dsrButton",3,flag)
		    	MenuItem.commitSetItemValid("dsrButton")
		    	MenuItem.setItemValid("page",2,flag)
		    	MenuItem.commitSetItemValid("page")
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
			
			func onClickUpgradeToPremiumBadge()
				logAdBannerLog(<%=EventTypes.AD_BANNER_CLICK%>,"UpgradeBadge")
				AdJuggler_setJSONForPurchase("UpgradeBadge", "<%=Constant.KEY_ADJUGGLER_FROM_MAIN%>")
			    Purchase_C_purchase(1)
		        return FAIL
			endfunc
			
			func onClickDowngradeToFreemium()
				System.showConfirm("<%=msg.get("about.cancel.confirm")%>","<%=msg.get("common.button.Yes")%>","<%=msg.get("common.button.No")%>","confirmCancelCallback")
				return FAIL
			endfunc

			func confirmCancelCallback(int selected)
				if(selected == 1)
					System.doAction("doCancelService")
				endif
			endfunc

			func upgradeCallBack(int selected)
			    if 1 == selected
		           Purchase_C_purchase(1)
		           return FAIL
		        endif
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
		    
			func checkDSRAvail()
				if DSR_M_isDSRSupportedForDisable() == 1
					<%if(TnUtil.isTMOUser(product)  || TnUtil.isVNUser(product)){%>
						Page.setComponentAttribute("dsrButton","visible","1")
						Page.setComponentAttribute("dsrButton","imageUnclick","$availableOffButton")
						Page.setComponentAttribute("dsrButton","imageClick","$availableOnButton")
					<%} else {%>
						Page.setComponentAttribute("dsrButton","visible","1")
					<%}%>
				elsif DSR_M_isDSRSupportedForDisable() == 2
					Page.setComponentAttribute("dsrButton","visible","1")
					Page.setComponentAttribute("dsrButton","imageUnclick","$disableOffButton")
					Page.setComponentAttribute("dsrButton","imageClick","$disableOnButton")
				else
					Page.setComponentAttribute("dsrButton","visible","0")					
				endif
			endfunc
			
			func onClickSayCommand()
				JSONObject jo
				invokeSpeakCommand(jo)
				return FAIL	
			endfunc		    
			
		func Purchase_C_purchase(int accountStatus)
			TxNode node
			TxNode.addValue(node, accountStatus)
			MenuItem.setBean("Purchase", "accountStatusNode", node)
			System.doAction("Purchase")
		endfunc
		
		func getOneBoxParam()
				JSONObject jo
				JSONObject.put(jo,"callbackfunction","addrCallBack")
				JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack+"OneBoxWrap#search"%>")
				JSONObject.put(jo,"from","Common")
				println("AC Parameters " +jo)
	        	return jo
      		endfunc
      		
		    func onClickGoToOneBox()
		    	OneBox_M_saveAcParam(getOneBoxParam())
		    	OneBox_M_resetSearchType()
		    	System.doAction("oneBoxSearchMenu")
		    	return FAIL	
	    	endfunc
	    	
			func checkBlueTooth()										
				System.doAction("checkBlueTooth")
				return FAIL			
			endfunc
	    	
			func doActionForAdJuggler()
			    JSONObject adJugglerJO = StartUp_M_getAdJuggler()
			    AdJuggler_doActionForPremClick(adJugglerJO)
			    return FAIL
			endfunc
			
			func isPremiumAccount()
				int flag = TRUE
	            if AccountTypeForSprint_IsLiteUser() || AccountTypeForSprint_IsBundleUser() || AccountTypeForSprint_IsFreeTrial()||AccountTypeForTMO_IsLiteUser()
	               flag = FALSE
	            endif
	            
	            return flag			
			endfunc
		]]>

	</tml:script>
