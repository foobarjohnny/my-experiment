<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@page import="com.telenav.cserver.browser.framework.BrowserFrameworkConstants"%>
<%@page import="com.telenav.cserver.stat.AttributeID"%>
<%@page import="com.telenav.cserver.stat.EventTypes"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@ include file="Header.jsp"%> 

<%-- 
   Dependencies for this script. This script is to be included dynamically by the calling script, 
   and the caller should have met the following dependency.
   1. Included AdJugglerScript.jsp
   2. Include code for isPremiumAccount, convertStopToJSON, Gps.getLastKnownLocation, 
   3. 
--%>
<tml:script language="fscript" version="1">

	<![CDATA[
		<%--
			pageName - field that identifies on which page, adjuggler is being shown. Eg. "MainPage", "PoiSearchPage", etc
			TODO later - change the function name to something else?
		--%>
		func checkActionForLastItem(String pageName)
			println("******************m")
			TxNode node
			JSONObject actionJo

			<%-- Get ad from cache. Show ad if available in cache and not expired --%>
			<%-- Show ad in UI, if available in cache and not expired --%>
			<%-- If ad in not available, then get new ad --%>
			<%-- If ad in available but expired, then get new ad --%>
			JSONObject adJugglerJO = AdJuggler_M_getAdJuggler(pageName)
			if NULL == adJugglerJO
				hideAdJuggler()
			else
				setAdJugglerMessage(adJugglerJO)
				if !AdJuggler_checkExpire(adJugglerJO,"LAST_TIME_AJUGGLER" + pageName)
					return FAIL
				endif
			endif
            
			<%-- add FROM page name in input parameter --%>
			JSONObject.put(actionJo, "<%=Constant.JSON_KEY_FROM%>", pageName)

			<%-- add premium account in input parameter --%>
			if isPremiumAccount()
				JSONObject.put(actionJo,"<%=Constant.JSON_KEY_ISPREMIUMACCOUNT%>","1")
			else
				JSONObject.put(actionJo,"<%=Constant.JSON_KEY_ISPREMIUMACCOUNT%>","0")
			endif

			<%-- add keyword in input parameter for backward compatibility --%>
			JSONObject.put(actionJo,"keyword","v2")

			<%-- add location object in input parameter --%>
			TxNode lastKnownLocationNode = Gps.getLastKnownLocation()
			JSONObject locationJO = convertStopToJSON(lastKnownLocationNode)
			if NULL != locationJO
				JSONObject.put(actionJo,"<%=Constant.JSON_KEY_LOCATION%>",locationJO)
			endif

            <%-- add app_start flag. 1 means first time, 0 is for subsequent calls --%>
	        String appStartFlag = getAppStartValue()
            JSONObject.put(actionJo,"<%=Constant.KEY_ADJUGGLER_APP_START%>",appStartFlag)
	        
			<%-- Make call to adjuggler servlet to get a new banner ad --%>
			<%-- checkActionFromWebback callback will be called when response is received --%>
			String actionStr = JSONObject.toString(actionJo)
			TxNode.addMsg(node,actionStr)
			TxRequest req
			println("*************here p: call")
			String url="<%=addonHost + "/CheckAdJuggler.do"%>"
			String scriptName="checkActionFromWebback"
			TxRequest.open(req,url)
			TxRequest.setRequestData(req,node)
			TxRequest.onStateChange(req,scriptName)
			TxRequest.send(req)
		endfunc

		func checkActionFromWebback(TxNode node,int status)
			# checkBlueTooth()
			println("*************here p: callback")

			if status == 0
				#AdJuggler_M_clearAdJuggler()
				return FAIL
			else
				String resultJoString = TxNode.msgAt(node,0)
				println("resultJoString: " + resultJoString)
				JSONObject jo = JSONObject.fromString(resultJoString)
				String pageName = ""
				if JSONObject.has(jo, "pageName")
					pageName = JSONObject.get(jo,"pageName")
				endif
				println("resultJoString:pageName=" + pageName)
				String saveLastTime = "LAST_TIME_AJUGGLER" + pageName
				AdJuggler_saveLastTime(saveLastTime)
				AdJuggler_M_saveAdJuggler(jo, pageName)
				setAdJugglerMessage(jo)
				println("resultJoString:done")
				return FAIL
			endif
		endfunc

		<%-- 
		    Assumes that UI elements "premiumBottom", "footerImage", "bannerIcon", "actionMessage" are available
		    1. Gets iconImage from jo and displays in "bannerIcon"
		    2. Gets backgroundImage, and related focusUrl/unfocusUrl from jo and displays in "premiumBottom"
		    3. Gets text and related locale from jo and displays in "actionMessage"
		    4. Gets campaignID and pageName from jo and logs in MIS Log
		     
		--%>
		func setAdJugglerMessage(JSONObject jo)
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
		endfunc

		func hideAdJuggler()
			Page.setComponentAttribute("premiumBottom","visible","0")
			Page.setComponentAttribute("footerImage","visible","1")
		endfunc

		<%-- Cache code to get/set/remove or CRUD operation --%>
		<%-- can be moved to model --%>
		func AdJuggler_M_saveAdJuggler(JSONObject adJuggler, String pageName)
			String key = AdJuggler_M_getAdJugglerKy(pageName)
			Cache.saveCookie(key, adJuggler)
		endfunc

		func AdJuggler_M_getAdJuggler(String pageName)
			String key = AdJuggler_M_getAdJugglerKy(pageName)
			JSONObject result = Cache.getJSONObjectFromCookie(key)
			return result
		endfunc

		func AdJuggler_M_clearAdJuggler(String pageName)
			String key = AdJuggler_M_getAdJugglerKy(pageName)
			Cache.deleteCookie(key)
		endfunc

		func AdJuggler_M_getAdJugglerKy(String pageName)
			String key = "ADJUGGLER_JSON_KEY_PREM" + pageName
			if !isPremiumAccount()
				key = "ADJUGGLER_JSON_KEY_NON_PREM" + pageName
			endif

			return key
		endfunc

	]]>

</tml:script>
