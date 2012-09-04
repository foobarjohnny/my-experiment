<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.util.TnConstants"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>

<%@page
	import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@ include file="../Header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>

<%@page import="com.telenav.cserver.stat.*"%>

<tml:TML outputMode="TxNode">
	<%@ include file="model/PoiListModel.jsp"%>
	<%@ include file="controller/ShowDetailController.jsp"%>
	<%@ include file="/touch62/jsp/model/PrefModel.jsp"%>
	<%@ include file="/touch62/jsp/model/DriveToModel.jsp"%>
	<%@ include file="controller/RatePoiController.jsp"%>
	<jsp:include
		page="/touch62/jsp/local_service/controller/MapWrapController.jsp"></jsp:include>
	<jsp:include page="/touch62/jsp/controller/DriveToController.jsp" />
	<%@ include
		file="/touch62/jsp/ac/controller/CreateFavoritesController.jsp"%>
	<%@ include file="../ac/controller/ShareAddressController.jsp"%>

	<jsp:include page="StatLogger.jsp" />
	<jsp:include page="PoiListScript.jsp"/>
	
	<tml:menuItem name="home" pageURL="<%=host + "/startUp.do?pageRegion=" + region%>">
	</tml:menuItem>
	
	<tml:menuItem name="searchAlongUpAhead" onClick="onClickSearchAlongUpAhead" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="searchAlongNearDest" onClick="onClickSearchAlongNearDest" trigger="TRACKBALL_CLICK"/>
	
	<tml:menuItem name="showMapViewClick" onClick="showMapView" text="<%=msg.get("poi.map.view")%>" trigger="KEY_MENU|TRACKBALL_CLICK"/>

	<tml:menuItem name="cancelAudioAndBack" onClick="backPage">
	</tml:menuItem>
	<tml:menuItem name="cancelAudio" text="Cancel Audio" trigger="KEY_MENU"
		onClick="cancelAudio">
	</tml:menuItem>

	<tml:menuItem name="previous" onClick="showPrevious">
	</tml:menuItem>

	<tml:actionItem name="makePhoneCallAction"
		action="<%=Constant.LOCALSERVICE_MAKEPHONECALL%>">
		<tml:input name="phonenumber" />
	</tml:actionItem>
	<tml:menuItem name="phoneCall" actionRef="makePhoneCallAction" />

	<tml:menuItem name="mapResults" onClick="showMapView" text="<%=msg.get("poi.list.mapResults")%>" trigger="KEY_MENU"/>
	
	<tml:menuItem name="popular"
		text="<%=msg.get("poi.list.sortByPopularity")%>" onClick="popular"
		trigger="KEY_MENU">
	</tml:menuItem>
	<tml:menuItem name="relevance"
		text="<%=msg.get("poi.list.sortByRelevance")%>" onClick="relevance"
		trigger="KEY_MENU">
	</tml:menuItem>
	<tml:menuItem name="distance"
		text="<%=msg.get("poi.list.sortByDistance")%>" onClick="distance"
		trigger="KEY_MENU">
	</tml:menuItem>
	<tml:menuItem name="rating"
		text="<%=msg.get("poi.list.sortByRating")%>" onClick="rating"
		trigger="KEY_MENU">
	</tml:menuItem>

	<tml:menuItem name="price" text="<%=msg.get("poi.list.sortByPrice")%>"
		onClick="price" trigger="KEY_MENU">
	</tml:menuItem>

	<tml:menuItem name="item_0_clicked_0" onClick="detailClick"
		trigger="TRACKBALL_CLICK|KEY_MENU">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="item_0_clicked_1" onClick="detailClick"
		trigger="TRACKBALL_CLICK|KEY_MENU">
		<tml:bean name="indexClicked" valueType="int" value="1000"></tml:bean>
	</tml:menuItem>
	
	<tml:menuItem name="item_0_clicked2_0" onClick="detailClick" text="<%=msg.get("poi.list.viewDetails")%>"
		trigger="TRACKBALL_CLICK|KEY_MENU">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="item_0_clicked2_1" onClick="detailClick" text="<%=msg.get("poi.list.viewDetails")%>"
		trigger="TRACKBALL_CLICK|KEY_MENU">
		<tml:bean name="indexClicked" valueType="int" value="1000"></tml:bean>
	</tml:menuItem>

	<tml:menuItem name="item_0_map_clicked_0" trigger="KEY_MENU"
		text="<%=msg.get("poi.list.mapResults")%>" onClick="mapResultOnclick">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="item_0_map_clicked_1" trigger="KEY_MENU"
		text="<%=msg.get("poi.list.mapResults")%>" onClick="mapResultOnclick">
		<tml:bean name="indexClicked" valueType="int" value="1000"></tml:bean>
	</tml:menuItem>
	
	<tml:menuItem name="<%="item_0_driveTo_clicked_0"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.drive.to")%>" onClick="DriveTo">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_driveTo_clicked_1"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.drive.to")%>" onClick="DriveTo">
		<tml:bean name="indexClicked" valueType="int" value="1000"></tml:bean>
	</tml:menuItem>
	
	<tml:menuItem name="<%="item_0_call_clicked_0"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.call")%>" onClick="call">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_call_clicked_1"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.call")%>" onClick="call">
		<tml:bean name="indexClicked" valueType="int" value="1000"></tml:bean>
	</tml:menuItem>
	
	<tml:menuItem name="<%="item_0_map_clicked_0"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.map.it")%>" onClick="mapResultOnclick">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_map_clicked_1"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.map.it")%>" onClick="mapResultOnclick">
		<tml:bean name="indexClicked" valueType="int" value="1000"></tml:bean>
	</tml:menuItem>
	
	<tml:menuItem name="<%="item_0_share_clicked_0"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.share.address")%>" onClick="showShareAddress">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_share_clicked_1"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.share.address")%>" onClick="showShareAddress">
		<tml:bean name="indexClicked" valueType="int" value="1000"></tml:bean>
	</tml:menuItem>
	
	<tml:menuItem name="<%="item_0_saveFav_clicked_0"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.save.to.favorites")%>" onClick="createFavorites">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_saveFav_clicked_1"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.save.to.favorites")%>" onClick="createFavorites">
		<tml:bean name="indexClicked" valueType="int" value="1000"></tml:bean>
	</tml:menuItem>
	
	<tml:menuItem name="<%="item_0_driveTo_contextMenu_0"%>"
		trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.drive.to")%>"
		onClick="DriveTo">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_driveTo_contextMenu_1"%>"
		trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.drive.to")%>"
		onClick="DriveTo">
		<tml:bean name="indexClicked" valueType="int" value="1000"></tml:bean>
	</tml:menuItem>

	<tml:menuItem name="<%="item_0_call_contextMenu_0"%>"
		trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.call")%>"
		onClick="call">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_call_contextMenu_1"%>"
		trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.call")%>"
		onClick="call">
		<tml:bean name="indexClicked" valueType="int" value="1000"></tml:bean>
	</tml:menuItem>

	<tml:menuItem name="<%="item_0_map_contextMenu_0"%>"
		trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.map.it")%>"
		onClick="mapResultOnclick">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_map_contextMenu_1"%>"
		trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.map.it")%>"
		onClick="mapResultOnclick">
		<tml:bean name="indexClicked" valueType="int" value="1000"></tml:bean>
	</tml:menuItem>

	<tml:menuItem name="<%="item_0_saveFav_contextMenu_0"%>"
		trigger="KEY_CONTEXT_MENU"
		text="<%=msg.get("poi.save.to.favorites")%>" onClick="createFavorites">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_saveFav_contextMenu_1"%>"
		trigger="KEY_CONTEXT_MENU"
		text="<%=msg.get("poi.save.to.favorites")%>" onClick="createFavorites">
		<tml:bean name="indexClicked" valueType="int" value="1000"></tml:bean>
	</tml:menuItem>

	<tml:menuItem name="<%="item_0_share_contextMenu_0"%>"
		trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.share.address")%>"
		onClick="showShareAddress">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_share_contextMenu_1"%>"
		trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.share.address")%>"
		onClick="showShareAddress">
		<tml:bean name="indexClicked" valueType="int" value="1000"></tml:bean>
	</tml:menuItem>
	
	<tml:menuItem name="setSortType" onClick="sortTypePop" />
	<tml:menuItem name="next" onClick="showNext">
	</tml:menuItem>
	<tml:menuItem name="previous" onClick="showPrevious">
	</tml:menuItem>
	<tml:actionItem name="invokeBrowser"
		action="<%=TnConstants.LOCALSERVICE_INVOKEPHONEBROWSER%>">
		<tml:input name="url" />
	</tml:actionItem>

    <tml:menuItem name="bannerAdsClick" actionRef="invokeBrowser" text="<%=msg.get("about.visit")%>" trigger="KEY_MENU|TRACKBALL_CLICK"/>


	<tml:block feature="<%=FeatureConstant.FEEDBACK_POI%>">
		<tml:menuItem name="poiListFeedbackMenu" onClick="poiListFeedback" text="<%=msg.get("common.givefeedback.menu")%>" trigger="KEY_MENU">
		</tml:menuItem>
	    <%if(TnUtil.isEligibleForNewFeedBack(handlerGloble)){%>
		<tml:menuItem name="gotoPoiListFeedback" pageURL="">
		</tml:menuItem>
		<%}else{%>
		<tml:menuItem name="gotoPoiListFeedback" pageURL="<%= getPage + "POIListFeedback" %>">
		      <tml:bean name="feedbackNode" valueType="TxNode" value="" />
		</tml:menuItem>
		<%}%>
	</tml:block>

	<tml:page id="PoiListPage" url="<%=getPage + "PoiList"%>"
		groupId="<%=GROUP_ID_POI%>" type="<%=pageType%>" showLeftArrow="true"
		showRightArrow="true" helpMsg="$//$searchresults">
		<tml:title id="title" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("poi.list.title")%>
		</tml:title>

		<tml:menuRef name="poiListFeedbackMenu" />
		<tml:compositeListItem id="mapViewComposite" getFocus="false"
			visible="true" bgColor="#FFFFFF" transparent="false"
			isFocusable="true">
			<tml:label id="mapViewLabel" focusFontColor="white">
			</tml:label>
			<tml:menuRef name="showMapViewClick" />
		</tml:compositeListItem>
		<tml:image id="searchAlongBackImg" align="left|top" />
		<tml:panel id="wholePanel" layout="vertical">
			<tml:panel id="searchAlongButtonPanel" layout="horizontal">
				<tml:button id="searchAlongUpAheadButton" visible="true" fontWeight="system" isFocusable="true" text="<%=msg.get("poi.close.ahead")%>" focusTextColor="white">
					<tml:menuRef name="searchAlongUpAhead" />
				</tml:button>
				<tml:button id="searchAlongNearDestButton" visible="true" fontWeight="system" isFocusable="true" text="<%=msg.get("poi.near.destination")%>" focusTextColor="white">
					<tml:menuRef name="searchAlongNearDest" />
				</tml:button>
			</tml:panel>
		<tml:listBox id="detailPanel">
			<tml:compositeListItem id="previousLabel" getFocus="false"
				visible="true" bgColor="#FFFFFF" transparent="false" isFocusable="true">
				<tml:label id="previous" textWrap="ellipsis"
					fontWeight="system_large|bold" focusFontColor="white" align="left">
					<%=msg.get("poi.list.showPreviousMore")%>
				</tml:label>
				<tml:menuRef name="previous" />
			</tml:compositeListItem>
			<tml:compositeListItem id="sponsorPoi_0" getFocus="false"
				visible="true" transparent="false"
				isFocusable="true">
				<tml:label id="itemInfo_name0_0" fontWeight="bold" textWrap="ellipsis" align="left"
				 focusFontColor="#FFFFFF" fontColor="#333333"
				/>
				<tml:label id="itemInfo_address0_0" textWrap="ellipsis"align="left"
				 focusFontColor="#FFFFFF" fontColor="#666666"
				/>
				<tml:label id="itemInfo_sponsor0_0" textWrap="ellipsis"align="left"
				 focusFontColor="#FFFFFF" fontColor="#004480"
				/>

				<tml:menuRef name="item_0_clicked_0" />
				<tml:menuRef name="item_0_clicked2_0" />
				<tml:menuRef name="mapResults" />
				<tml:menuSeperator/>
				
				<tml:menuRef name="<%="item_0_driveTo_clicked_0"%>" />
				<tml:menuRef name="<%="item_0_call_clicked_0"%>" />
				<tml:menuRef name="<%="item_0_map_clicked_0"%>" />
				<tml:menuRef name="<%="item_0_share_clicked_0"%>" />
				<tml:menuRef name="<%="item_0_saveFav_clicked_0"%>" />
				<tml:menuRef name="poiListFeedbackMenu" />
				
				<tml:menuSeperator/>
				<tml:menuRef name="popular" />
				<tml:menuRef name="price" />
				<tml:menuRef name="relevance" />
				<tml:menuRef name="rating" />
				<tml:menuRef name="distance" />
				
				<tml:menuRef name="<%="item_0_driveTo_contextMenu_0"%>" />
				<tml:menuRef name="<%="item_0_call_contextMenu_0"%>" />
				<tml:menuRef name="<%="item_0_map_contextMenu_0"%>" />
				<tml:menuRef name="<%="item_0_saveFav_contextMenu_0"%>" />
				<tml:menuRef name="<%="item_0_share_contextMenu_0"%>" />
			</tml:compositeListItem>
			<%
				for (int i = 1; i <= Constant.PAGE_SIZE; i++) {
			%>
			<tml:menuItem name="<%="item_" + i + "_clicked"%>"
				onClick="detailClick" trigger="TRACKBALL_CLICK|KEY_MENU">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>
			
			
			<tml:menuItem name="<%="item_" + i + "_clicked2"%>" text="<%=msg.get("poi.list.viewDetails")%>"
				onClick="detailClick" trigger="TRACKBALL_CLICK|KEY_MENU">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem
				name="<%="item_" + i
												+ "_driveTo_clicked"%>"
				trigger="KEY_MENU" text="<%=msg.get("poi.drive.to")%>"
				onClick="DriveTo">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem name="<%="item_" + i + "_call_clicked"%>"
				trigger="KEY_MENU" text="<%=msg.get("poi.call")%>" onClick="call">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem name="<%="item_" + i + "_map_clicked"%>"
				trigger="KEY_MENU" text="<%=msg.get("poi.map.it")%>"
				onClick="mapResultOnclick">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem name="<%="item_" + i + "_share_clicked"%>"
				trigger="KEY_MENU" text="<%=msg.get("poi.share.address")%>"
				onClick="showShareAddress">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem
				name="<%="item_" + i
												+ "_saveFav_clicked"%>"
				trigger="KEY_MENU"
				text="<%=msg
														.get("poi.save.to.favorites")%>"
				onClick="createFavorites">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>


			<tml:menuItem
				name="<%="item_" + i
										+ "_driveTo_contextMenu"%>"
				trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.drive.to")%>"
				onClick="DriveTo">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem
				name="<%="item_" + i
										+ "_call_contextMenu"%>"
				trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.call")%>"
				onClick="call">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem
				name="<%="item_" + i
												+ "_map_contextMenu"%>"
				trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.map.it")%>"
				onClick="mapResultOnclick">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem
				name="<%="item_" + i
										+ "_saveFav_contextMenu"%>"
				trigger="KEY_CONTEXT_MENU"
				text="<%=msg
														.get("poi.save.to.favorites")%>"
				onClick="createFavorites">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem
				name="<%="item_" + i
										+ "_rate_contextMenu"%>"
				trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.rate.it")%>"
				onClick="ratePoi" pageURL="<%=getPage + "RatePoi"%>">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem
				name="<%="item_" + i
										+ "_share_contextMenu"%>"
				trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.share.address")%>"
				onClick="showShareAddress">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:POICompositeListItem id="<%="item" + i%>">
				<tml:menuRef name="<%="item_" + i + "_clicked"%>" />
				<tml:menuRef name="<%="item_" + i + "_clicked2"%>" />
				<tml:menuRef name="mapResults" />
				
				<tml:menuSeperator/>
				<tml:menuRef name="<%="item_" + i + "_driveTo_clicked"%>" />
				<tml:menuRef name="<%="item_" + i + "_call_clicked"%>" />
				<tml:menuRef name="<%="item_" + i + "_map_clicked"%>" />
				<tml:menuRef name="<%="item_" + i + "_share_clicked"%>" />
				<tml:menuRef name="<%="item_" + i + "_saveFav_clicked"%>" />
				<tml:menuRef name="poiListFeedbackMenu" />
				
				<tml:menuSeperator/>
				<tml:menuRef name="popular" />
				<tml:menuRef name="price" />
				<tml:menuRef name="relevance" />
				<tml:menuRef name="rating" />
				<tml:menuRef name="distance" />

				<tml:menuRef name="<%="item_" + i + "_driveTo_contextMenu"%>" />
				<tml:menuRef name="<%="item_" + i + "_call_contextMenu"%>" />
				<tml:menuRef name="<%="item_" + i + "_map_contextMenu"%>" />
				<tml:menuRef name="<%="item_" + i + "_share_contextMenu"%>" />
				<tml:menuRef name="<%="item_" + i + "_saveFav_contextMenu"%>" />
				<tml:menuRef name="<%="item_" + i + "_rate_contextMenu"%>" />
				
			</tml:POICompositeListItem>
			<%
				}
			%>
			<tml:compositeListItem id="sponsorPoi_1" getFocus="false"
				visible="true" transparent="false"
				isFocusable="true">
				<tml:label id="itemInfo_name0_1" fontWeight="bold" textWrap="ellipsis" align="left"
				 focusFontColor="#FFFFFF" fontColor="#333333"
				/>
				<tml:label id="itemInfo_address0_1" textWrap="ellipsis"align="left"
				 focusFontColor="#FFFFFF" fontColor="#666666"
				/>
				<tml:label id="itemInfo_sponsor0_1" textWrap="ellipsis"align="left"
				 focusFontColor="#FFFFFF" fontColor="#004480"
				/>

				<tml:menuRef name="item_0_clicked_1" />
				<tml:menuRef name="item_0_clicked2_1" />
				<tml:menuRef name="mapResults" />
				<tml:menuSeperator/>
				
				<tml:menuRef name="<%="item_0_driveTo_clicked_1"%>" />
				<tml:menuRef name="<%="item_0_call_clicked_1"%>" />
				<tml:menuRef name="<%="item_0_map_clicked_1"%>" />
				<tml:menuRef name="<%="item_0_share_clicked_1"%>" />
				<tml:menuRef name="<%="item_0_saveFav_clicked_1"%>" />
				<tml:menuRef name="poiListFeedbackMenu" />
				
				<tml:menuSeperator/>
				<tml:menuRef name="popular" />
				<tml:menuRef name="price" />
				<tml:menuRef name="relevance" />
				<tml:menuRef name="rating" />
				<tml:menuRef name="distance" />
				
				<tml:menuRef name="<%="item_0_driveTo_contextMenu_1"%>" />
				<tml:menuRef name="<%="item_0_call_contextMenu_1"%>" />
				<tml:menuRef name="<%="item_0_map_contextMenu_1"%>" />
				<tml:menuRef name="<%="item_0_saveFav_contextMenu_1"%>" />
				<tml:menuRef name="<%="item_0_share_contextMenu_1"%>" />
			</tml:compositeListItem>
			<tml:compositeListItem id="nextLabel" getFocus="false" visible="true"
				bgColor="#FFFFFF" transparent="false" isFocusable="true">
				<tml:label id="next" textWrap="ellipsis" 
					fontWeight="system_large|bold" focusFontColor="white" align="left">
					<%=msg.get("poi.list.showMore")%>
				</tml:label>
				<tml:menuRef name="next" />
			</tml:compositeListItem>
			<tml:compositeListItem id="bannerAds">
				<tml:image id="bannerAdsImg" align="center" />
				<tml:menuRef name="bannerAdsClick" />
				<tml:menuRef name="poiListFeedbackMenu" />
			</tml:compositeListItem>
		</tml:listBox>
		</tml:panel>



		<tml:image id="titleShadow" visible="true" align="left|top" />
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
