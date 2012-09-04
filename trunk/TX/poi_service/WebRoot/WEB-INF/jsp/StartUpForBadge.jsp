<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
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

<%
    String pageURL = host + "/startUp.do?pageRegion=" + region;

    
    JSONObject jo = (JSONObject) request.getAttribute("ACTemplates");
    String joStr = jo.toString();
    TxNode acTemplates = new TxNode();
    acTemplates.addMsg(joStr);
%>

<tml:TML outputMode="TxNode">
	<%@ include file="poi/controller/SearchPoiController.jsp"%>
	<%@ include file="ac/controller/AddressCaptureTemplateController.jsp"%>
	<%@ include file="ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="model/StartUpModel.jsp"%>
	<%@ include file="dsr/controller/DSRController.jsp"%>
	<%@ include file="/WEB-INF/jsp/model/PrefModel.jsp"%>
	<%@ include file="controller/ReferFriendController.jsp"%>
	<%@include file="/WEB-INF/jsp/ac/model/SetUpHomeModel.jsp"%>
	<jsp:include
		page="/WEB-INF/jsp/ac/controller/SelectAddressController.jsp" />
	<jsp:include
		page="/WEB-INF/jsp/local_service/controller/MapWrapController.jsp" />
	<tml:script language="fscript" version="1">
		<![CDATA[
		    <%@ include file="GetServerDriven.jsp"%>
			func onBack()
				System.exit()
				return FAIL
			endfunc
			
			func checkTraffic()
				string text = "<%=msg.get("startup.Maps")%>"
				if StartUp_M_isTurnOnTraffic()
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
				initTopImage()
				checkAppNew()
				checkMyFavorite()
				homePageStatLogger()
			endfunc
			
			func onLoad()
				#TODO now Store fake AC template resource  data
				TxNode node
				node = ParameterSet.getParam("acTemplates")
				JSONObject jo = JSONObject.fromString(TxNode.msgAt(node,0))
				AddressCaptureTemplate_C_saveTemplates(jo)
				
				# Delete IN_LOGIN_FLOW flag
				# Set convienence key will check this flag.
				# If in, go to main screen.
				# If not in, exit browser shell.
				Cache.deleteFromTempCache("IN_LOGIN_FLOW")
				
				# Check Blue tooth GPS local service
				System.doAction("checkBlueTooth")
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
			        	        	        

	        
	        func initTopImage()
	        	int index = Page.getControlProperty("menuListBox","focusedIndex")
	        	visibleImage(index)
	        endfunc
			
			func visibleImage(int i)
				string imageUrl = "<%=imageUrl%>" + "banner_" + i + ".png"
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
	        	incrementBadgeCounter()
	        	checkAppNew()
	        endfunc
	        
	        func checkAppNew()
	        	string text = ""
	        	int isNew = StartUp_M_getAppNewFlag()
	        	if isNew
	        		setBadgeCounter(0)
	        		StartUp_M_clearAppNewFlag()
	        	endif
	        	showBadge()
	        endfunc

	        func showBadge()
	        	println("here1")
	        	string text = ""
	        	int counter = getBadgeCounter()
	        	if counter < 5
	        		text = " <red> " + "<%=msg.get("startup.New")%>" + "</red>"
	        	endif
	        	println("here2" + counter)
	        	Page.setComponentAttribute("new","text",text)
	        endfunc

			func setBadgeCounter(int value)

    			TxNode node
    			TxNode.addValue(node, value)
    			String saveKey = "<%=Constant.StorageKey.SHOW_NEW_BADGE%>"
    			Cache.saveCookie(saveKey, node)

			endfunc

			func getBadgeCounter()

			    int value
    			value = 0

    			String saveKey = "<%=Constant.StorageKey.SHOW_NEW_BADGE%>"
    			TxNode node = Cache.getCookie(saveKey)
    			if NULL != node
        			value = TxNode.valueAt(node,0);
    			endif
	        
    			return value 

			endfunc

			func incrementBadgeCounter()

    			int value
    			value = getBadgeCounter()
    			value = value + 1

    			setBadgeCounter(value)
    
    			return value

			endfunc


		    func StartUp_M_getListItemSize()
		    	int size = 4
		    	return size
		    endfunc
		    
		    func checkUpgrade()
		    	int flag = StartUp_M_getUpgradeFlag()
		    	MenuItem.setItemValid("item0",1,flag)
		    	MenuItem.commitSetItemValid("item0")
        		MenuItem.setItemValid("item1",1,flag)
        		MenuItem.commitSetItemValid("item1")
        		MenuItem.setItemValid("item2",1,flag)
        		MenuItem.commitSetItemValid("item2")
        		MenuItem.setItemValid("item3",1,flag)
        		MenuItem.commitSetItemValid("item3")
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
			
		    func onFocus(string id)
		    	int index = Page.getControlProperty("menuListBox","focusedIndex")
		    	visibleImage(index)
		    endfunc
			
		]]>

	</tml:script>

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

	<tml:menuItem name="toolsMenu" onClick="onClickApps" pageURL="<%="{poi.http}/ToolsMain.do?pageRegion=NA"%>" trigger="TRACKBALL_CLICK">
	</tml:menuItem>
	
	<tml:menuItem name="searchPOI" onClick="onClickSearch" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="driveToItemAction1" onClick="onClickDriveTo1" trigger="TRACKBALL_CLICK"/>								
	<tml:actionItem name="SyncResource"
		action="<%=Constant.LOCALSERVICE_SYNCRESOURCE%>">
	</tml:actionItem>
	<tml:menuItem name="syncResouceMenu" actionRef="SyncResource"
		trigger="KEY_RIGHT | TRACKBALL_CLICK" />
		
	<tml:page id="StartUp" url="<%=pageURL%>" type="<%=pageType%>" genericMenu="16"
		showLeftArrow="true" showRightArrow="true" helpMsg="$//$main" groupId="<%=GROUP_ID_COMMOM%>" defaultSelectedMenu="system_64" >
		
		<tml:bean name="acTemplates" valueType="TxNode"
			value="<%=Utility.TxNode2Base64(acTemplates)%>"></tml:bean>

		<tml:title id="title" align="center|middle" fontWeight="bold|system_large" fontColor="white">
			<%=PoiUtil.amend(msg.get("startup.title"))%>
		</tml:title>
		<tml:image id="imageTop" url="<%=imageUrl + "banner_0.png"%>" />
		<tml:listBox id="menuListBox" name="pageListBox:settingsList"
			isFocusable="true" hotKeyEnable="false">
			<tml:compositeListItem id="item0" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="<%=imageUrl + "list_bg_highlight_45px.png"%>"
				blurBgImage="<%=imageUrl + "list_bg_45px.png"%>">
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
				
				<tml:menuRef name="driveToItemAction1" />
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
			</tml:compositeListItem>
			<tml:compositeListItem id="item1" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="<%=imageUrl + "list_bg_highlight_45px.png"%>"
				blurBgImage="<%=imageUrl + "list_bg_45px.png"%>">
				<tml:param name="onFocus" value="onFocus"/>
				<tml:image id="itemImage1" url="<%=imageUrl + "button_search.png"%>" />
				<tml:label id="itemlabel1" focusFontColor="white" 
					fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
					<%=msg.get("startup.Search")%>
				</tml:label>
				<tml:menuRef name="searchPOI" />
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
			</tml:compositeListItem>
			<tml:compositeListItem id="item2" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="<%=imageUrl + "list_bg_highlight_45px.png"%>"
				blurBgImage="<%=imageUrl + "list_bg_45px.png"%>">
				<tml:param name="onFocus" value="onFocus"/>
				<tml:image id="itemImage2" url="<%=imageUrl + "button_mapstraffic.png"%>" />
				<tml:label id="itemlabel2" focusFontColor="white" 
					fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
				</tml:label>
				<tml:menuRef name="showMap" />
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
			</tml:compositeListItem>
			<tml:compositeListItem id="item3" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="<%=imageUrl + "list_bg_highlight_45px.png"%>"
				blurBgImage="<%=imageUrl + "list_bg_45px.png"%>">
				<tml:param name="onFocus" value="onFocus"/>
				<tml:image id="itemImage3" url="<%=imageUrl + "button_tools.png"%>" />
				<tml:label id="itemlabel3" focusFontColor="white" 
					fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
					<%=msg.get("startup.Apps")%>
				</tml:label>
				<tml:label id="new" focusFontColor="white" 
					fontWeight="system_small" textWrap="ellipsis" align="left|top">
				</tml:label>
				<tml:menuRef name="toolsMenu" />
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
			</tml:compositeListItem>
		</tml:listBox>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>
