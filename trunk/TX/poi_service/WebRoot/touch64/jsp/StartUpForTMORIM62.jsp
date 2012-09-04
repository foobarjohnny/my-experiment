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
<%@page import="com.telenav.cserver.billing.BillingConstants"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>

<%
    String pageURL = host + "/startUp.do?pageRegion=" + region;
	TxNode listNode = (TxNode) request.getAttribute("node");
	
	String cancelServiceUrl = "{login.http}/" + ClientHelper.getModuleNameForLogin(handlerGloble) + "/getFeedbackList.do";
%>

<tml:TML outputMode="TxNode">
	<%@ include file="/touch64/jsp/weather/controller/WeatherController.jsp"%>
	<jsp:include page="/touch/jsp/common/movie/controller/MovieController.jsp" />
	<jsp:include page="/touch/jsp/common/movie/controller/MovieListController.jsp" />
	<%@ include file="/touch64/jsp/poi/controller/PoiListController.jsp"%>
	<%@ include file="/touch64/jsp/StopUtil.jsp"%>
	<jsp:include page="StartUpForSNScript.jsp" />

	<tml:actionItem name="getGPSForMovie" action="getGPS"
		progressBarText="<%=msg.get("mSearch.bar.gps")%>">
		<tml:input name="locParam"></tml:input>
		<tml:output name="currentLocation" />
	</tml:actionItem>
	<tml:menuItem actionRef="getGPSForMovie" name="doGetGpsForMovie" onClick="AdJuggler_getLocationForMovieForMovie"/>
	<tml:menuItem name="Purchase" pageURL="<%=purchasePageUrl%>"/>
	<tml:menuItem name="oneBoxSearchMenu" pageURL="<%=getPage + "GoToOneBoxSearch#Common"%>" trigger="TRACKBALL_CLICK">
	</tml:menuItem>
	<tml:actionItem name="checkBlueTooth" action="CheckBlueTooth"></tml:actionItem>
	<tml:menuItem name="checkBlueTooth" actionRef="checkBlueTooth">
	</tml:menuItem>
	<tml:menuItem name="doSearch" onClick="onClickGoToOneBox"  trigger="TRACKBALL_CLICK|KEY_MENU"/>
	
	<tml:menuItem name="about" text="<%=msg.get("startup.menu.About")%>"
		trigger="KEY_MENU" onClick="exitFromHomePage" pageURL="<%=getPage + "AboutMenu"%>">
	</tml:menuItem>
	
	<tml:menuItem name="productTour" text="<%=msg.get("startup.menu.ProductTour")%>"
		trigger="KEY_MENU" onClick="exitFromHomePage" pageURL="<%=getPage + "ProductTour"%>">
	</tml:menuItem>
	<tml:menuItem name="saycommand"  onClick="onClickSayCommand"  trigger="TRACKBALL_CLICK"/>
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
	<tml:menuItem name="upgradeToPremium" onClick="onClickUpgradeToPremium" trigger="TRACKBALL_CLICK" />
	<tml:menuItem name="upgradeToPremiumMenu" onClick="onClickUpgradeToPremium" text="<%=msg.get("startup.upgradeSprint")%>" trigger="KEY_MENU" />
	<tml:menuItem name="downgradeToFreemiumMenu" onClick="onClickDowngradeToFreemium" text="<%=msg.get("startup.downgradeSprint")%>" trigger="KEY_MENU" />
	<tml:menuItem name="doCancelService" pageURL="<%=cancelServiceUrl%>" trigger="TRACKBALL_CLICK" />
			
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
		showLeftArrow="true" showRightArrow="true" helpMsg="$//$main" groupId="<%=GROUP_ID_COMMOM%>" defaultSelectedMenu="system_64" scriptCallback="pageCallBack">
		<tml:bean name="hotBrandlist" valueType="TxNode"
			value="<%=Utility.TxNode2Base64(listNode)%>"></tml:bean>
		<tml:menuRef name="upgradeToPremiumMenu" />
		<tml:menuRef name="downgradeToFreemiumMenu" />
		<tml:menuRef name="upgrade" />
		<tml:menuRef name="about" />
		<tml:menuRef name="referFriend" />
		<tml:image id="oneBoxBgImg" visible="true" align="left|top"/>
		<tml:label id="titleLabel" align="center|middle" fontWeight="bold" fontColor="white">
			<%=PoiUtil.amend(msg.get("startup.title"))%>
		</tml:label>

		<tml:image id="imageTop" url="$imageTopTemp0" align="left|top"/>
		<tml:compositeListItem id="oneBoxSearchButton" >
			<tml:label id ="oneBoxSearchLabel" fontWeight="system_large">
				<%=TnUtil.amend(msg.get("onebox.what.prompt"))%>
			</tml:label>
			<tml:menuRef name="doSearch"/>
		    <tml:menuRef name="about" />
			<tml:menuRef name="referFriend" />
		</tml:compositeListItem>
		<!--tml:listBox id="menuListBox" name="pageListBox:settingsList"
			isFocusable="true" hotKeyEnable="false"-->
			<tml:compositeListItem id="item0" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage=""
				blurBgImage="">
				<tml:param name="onFocus" value="onFocus"/>
				<tml:image id="itemImage0" url="" align="left|top"/>
				<tml:label id="itemlabel0" focusFontColor="white" 
					 textWrap="ellipsis" align="left|middle">
					<%=msg.get("startup.DriveTo")%>
				</tml:label>
				<tml:image id="itemImageNewFav" visible="false"/>
					<tml:label id="itemLabelFav" fontColor="white"
					fontWeight="bold|system_small" 
					align="center|middle">
				</tml:label>
				<tml:menuRef name="driveToItemAction1" />
				<tml:menuRef name="upgradeToPremiumMenu" />
				<tml:menuRef name="downgradeToFreemiumMenu" />
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
			</tml:compositeListItem>
			<tml:compositeListItem id="item1" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage=""
				blurBgImage="">
				<tml:param name="onFocus" value="onFocus"/>
				<tml:image id="itemImage1" url=""  align="left|top"/>
				<tml:label id="itemlabel1" focusFontColor="white" 
					textWrap="ellipsis" align="left|middle">
					<%=msg.get("startup.Search")%>
				</tml:label>
				<tml:menuRef name="searchPOI" />
				<tml:menuRef name="upgradeToPremiumMenu" />
				<tml:menuRef name="downgradeToFreemiumMenu" />
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
			</tml:compositeListItem>
			<tml:compositeListItem id="item2" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage=""
				blurBgImage="">
				<tml:param name="onFocus" value="onFocus"/>
				<tml:image id="itemImage2" url="" align="left|top"/>
				<tml:label id="itemlabel2" focusFontColor="white" 
					textWrap="ellipsis" align="left|middle">
				</tml:label>
				<tml:menuRef name="showMap" />
				<tml:menuRef name="upgradeToPremiumMenu" />
				<tml:menuRef name="downgradeToFreemiumMenu" />
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
			</tml:compositeListItem>
			<tml:compositeListItem id="item3" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage=""
				blurBgImage="">
				<tml:param name="onFocus" value="onFocus"/>
				<tml:image id="itemImage3" url="" align="left|top"/>
				<tml:label id="itemlabel3" focusFontColor="white" 
					textWrap="ellipsis" align="left|middle">
					<%=msg.get("startup.Apps")%>
				</tml:label>
				<tml:label id="itemlabel3_1" focusFontColor="white" 
					textWrap="ellipsis" align="left|middle">
					<%=msg.get("startup.Apps")%>
				</tml:label>
				<tml:image id="itemImageNew" url="" align="left|top" visible="false"/>
				<tml:label id="new" fontColor="white"
					fontWeight="system" textWrap="ellipsis" align="center|middle">
				</tml:label>
				<tml:menuRef name="toolsMenu" />
				<tml:menuRef name="upgradeToPremiumMenu" />
				<tml:menuRef name="downgradeToFreemiumMenu" />
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
			</tml:compositeListItem>
			<tml:compositeListItem id="item4" visible="false" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="" blurBgImage="">
				<tml:param name="onFocus" value="onFocus"/>
				<tml:label id="purchaseMessage" fontWeight="system_small" align="left|middle" focusFontColor="white" fontColor="black" textWrap="ellipsis">
					<%=msg.get("startup.purchaseMessage")%>
				</tml:label>
				<tml:menuRef name="upgradeToPremiumMenu" />
				<tml:menuRef name="upgradeToPremium" />
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
			</tml:compositeListItem>			
			
			<tml:compositeListItem id="premiumBottom" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="" blurBgImage="">
				<tml:image id="bannerIcon" align="center|middle" />
				<tml:label id="actionMessage" fontWeight="system_small"  align="left|middle" focusFontColor="white" fontColor="black" textWrap="ellipsis">
				</tml:label>
				<tml:label id="nonIconActionMessage" fontWeight="system_small"  align="left|middle" focusFontColor="white" fontColor="black" textWrap="ellipsis">
				</tml:label>
				<tml:menuRef name="doActionForPrem" />
			</tml:compositeListItem>
			<tml:image id="footerImage" url="" align="left|top"/>
		<!--/tml:listBox-->
		<tml:image id="titleShadow1" visible="true" align="left|top"/>
		<tml:image id="bottomBgImg" visible="true" align="left|top"/>
		<tml:button id="dsrButton" text="  ">
				<tml:menuRef name="saycommand"/>
				<tml:menuRef name="upgradeToPremiumMenu" />
				<tml:menuRef name="downgradeToFreemiumMenu" />
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
		</tml:button>
		<tml:image id="imagePrem" url="" visible="false" align="left|top"/>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>
