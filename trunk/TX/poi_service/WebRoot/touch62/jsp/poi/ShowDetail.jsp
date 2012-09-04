<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%@ include file="../GetClientInfo.jsp"%>
<%@page import="java.util.PropertyResourceBundle"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page	import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>
<%@page import="com.telenav.cserver.stat.*"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>


<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>

<%
	String ShowDetailURL = getPageCallBack + "ShowDetail";
	String menuImage = imageCommonUrl + "menu.png";
	String couponsImage = imageCommonUrl + "coupon.png";

	String menuTabID = "menuTab";
	String couponTabID = "coupons";

	String movieModuleName = ClientHelper.getModuleNameForMovie(handlerGloble);
	String showMovieUrl = "{movie.http}/" + movieModuleName + "/ShowMovies.do";
	
	//FIXME ClientHelper.getModuleNameForMovie(handlerGloble) to ClientHelper.getModuleNameForRestaurant(handlerGloble)
	String restaurantHost = "{restaurant.http}/touch";
	String restaurantUrl = restaurantHost +"/goToJsp.do?pageRegion=" + region + "&amp;jsp=DetailAndReservationPage";
%>

<tml:TML outputMode="TxNode">
	<jsp:include page="/touch62/jsp/poi/model/ShowDetailModel.jsp"/>
	<jsp:include page="/touch62/jsp/controller/DriveToController.jsp"/>
	<jsp:include page="../ac/controller/EditRouteController.jsp" />
	<jsp:include
		page="/touch62/jsp/local_service/controller/MapWrapController.jsp" />
	<jsp:include page="StatLogger.jsp"/>
	<jsp:include page="ShowDetailScript.jsp"/>

	<tml:block feature="<%=FeatureConstant.POST_LOCATION%>">
		<jsp:include page="PostLocationIncl.jsp"/>
	</tml:block>

	<tml:menuItem name="DriveTo" trigger="TRACKBALL_CLICK"
		onClick="DriveTo">
	</tml:menuItem>
	<tml:menuItem name="DriveToMenu" text="<%=msg.get("poi.drive.to")%>"
		trigger="KEY_MENU" onClick="DriveTo">
	</tml:menuItem>
	<tml:menuItem name="getDirections" trigger="TRACKBALL_CLICK"
		onClick="staticRoute">
	</tml:menuItem>
	<tml:menuItem name="getDirectionsMenu"
		text="<%=msg.get("poi.get.directions")%>" trigger="KEY_MENU"
		onClick="staticRoute">
	</tml:menuItem>


	<tml:menuItem name="viewMovies"
			text="<%=msg.get("poi.details.movie")%>"
			pageURL="<%=showMovieUrl%>"
			trigger="KEY_MENU|TRACKBALL_CLICK">
			<tml:bean name="theaterId" valueType="String" value="" />
	</tml:menuItem>
	<tml:menuItem name="viewMoviesMenu"
			text="<%=msg.get("poi.details.movie")%>"
			pageURL="<%=showMovieUrl%>"
			trigger="KEY_MENU">
			<tml:bean name="theaterId" valueType="String" value="" />
	</tml:menuItem>

	<tml:actionItem name="makePhoneCallAction"
		action="<%=Constant.LOCALSERVICE_MAKEPHONECALL%>">
		<tml:input name="phonenumber" />
	</tml:actionItem>
	<tml:menuItem name="phoneCall" actionRef="makePhoneCallAction" />
	<tml:menuItem name="call" trigger="TRACKBALL_CLICK" onClick="call">
	</tml:menuItem>

	<tml:menuItem name="callMenu" text="<%=msg.get("poi.call.phone")%>"
		trigger="KEY_MENU" onClick="call">
	</tml:menuItem>

	<tml:actionItem name="showMapAction" action="mapIt">
		<tml:input name="pSrc" />
	</tml:actionItem>

	<tml:menuItem name="doShowmap" actionRef="showMapAction">
	</tml:menuItem>
	<tml:menuItem name="showMap" trigger="TRACKBALL_CLICK"
		onClick="showMap">
	</tml:menuItem>

	<tml:menuItem name="showMapMenu" text="<%=msg.get("poi.map.it")%>"
		trigger="KEY_MENU" onClick="showMap">
	</tml:menuItem>

	<tml:block feature="<%=FeatureConstant.SHARE_ADDRESS%>">
		<tml:menuItem name="shareAddress" trigger="TRACKBALL_CLICK"
			onClick="showShareAddress">
		</tml:menuItem>
		<tml:menuItem name="shareAddressMenu" trigger="KEY_MENU"
			text="<%=msg.get("poi.share.address")%>" onClick="showShareAddress">
		</tml:menuItem>
	</tml:block>

	<tml:block feature="<%=FeatureConstant.UGC_VIEW%>">
		<tml:menuItem name="viewReviews" trigger="KEY_MENU|TRACKBALL_CLICK"
			text="<%=msg.get("poi.view.history")%>" onClick="viewReviews">
		</tml:menuItem>
	</tml:block>

	<tml:menuItem name="favorites" trigger="TRACKBALL_CLICK"
		onClick="createFavorites">
	</tml:menuItem>

	<tml:menuItem name="favoritesMenu"
		text="<%=msg.get("poi.save.to.favorites")%>" trigger="KEY_MENU"
		onClick="createFavorites">
	</tml:menuItem>

	<tml:menuItem name="rateThis" trigger="TRACKBALL_CLICK"
		pageURL="<%=getPage + "RatePoi"%>" onClick="rateThis">
	</tml:menuItem>

	<tml:menuItem name="rateThisMenu" text="<%=msg.get("poi.rate.this")%>"
		trigger="KEY_MENU" pageURL="<%=getPage + "RatePoi"%>"
		onClick="rateThis">
	</tml:menuItem>

	<tml:block feature="<%=FeatureConstant.FEEDBACK_POI%>">
		<tml:menuItem name="poiDetailFeedbackMenu" onClick="poiDetailFeedback" text="<%=msg.get("common.givefeedback.menu")%>" trigger="KEY_MENU">
		</tml:menuItem>
		<tml:menuItem name="poiDetailFeedback" trigger="TRACKBALL_CLICK" onClick="poiDetailFeedback">
		</tml:menuItem>
		<%if(TnUtil.isEligibleForNewFeedBack(handlerGloble)){%>
		<tml:menuItem name="gotoPoiDetailFeedback" pageURL="">
		</tml:menuItem>
		<%}else{%>
		<tml:menuItem name="gotoPoiDetailFeedback" pageURL="<%= getPage + "POIDetailFeedback" %>">
		      <tml:bean name="feedbackNode" valueType="TxNode" value="" />
		</tml:menuItem>
		<%}%>
	</tml:block>

	<tml:page id="showDetailPage" url="<%=getPage + "ShowDetail"%>"
		groupId="<%=GROUP_ID_POI%>" type="<%=pageType%>" showLeftArrow="true"
		showRightArrow="true" helpMsg="$//$poidetails">

		<tml:menuRef name="viewMoviesMenu" />
		<tml:menuRef name="DriveToMenu" />
		<tml:menuRef name="getDirectionsMenu" />
		<tml:menuRef name="callMenu" />
		<tml:menuRef name="showMapMenu" />
		<tml:menuRef name="shareAddressMenu" />
		<tml:menuRef name="favoritesMenu" />
		<tml:menuSeperator/>
		<tml:menuRef name="rateThisMenu" />
		<tml:menuRef name="postLocationMI3" />
		<tml:menuRef name="poiDetailFeedbackMenu" />
		<tml:menuSeperator/>

		<tml:title id="title" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("poi.list.title")%>
		</tml:title>

		<tml:tabContainer id="showDetailContainer" style="vertical"
			defaultFocus="0">
			<tml:param name="onChange" value="onTabsChange"/>
			<tml:tab id="Details" label="<%=msg.get("poi.details") %>"
				fontWeight="bold|system_large">
				<tml:panel id="poiInfoPanel" layout="vertical">
					<tml:panel id="panelMargin" height="20"/>
					<tml:urlLabel id="dummyLabel1" isFocusable="true" height="1"/>
					<tml:panel id="poiNamePanel" layout="horizontal">
						<tml:multiline id="poiName"  fontWeight="system_large"
							align="left|top"/>
						<tml:label id="distance" align="right|top"/>
					</tml:panel>
          
	            		<tml:nullField id="nullField1" height="10"></tml:nullField>
	            		<tml:panel id="ratingPanel" layout="horizontal">	
							<tml:compositeListItem id="rating" bgColor="#FFFFFF">
								<tml:image id="starImage1_0"  />
								<tml:image id="starImage2_0"  />
								<tml:image id="starImage3_0"  />
								<tml:image id="starImage4_0"  />
								<tml:image id="starImage5_0"  />
								<tml:menuRef name="rateThis" />
							</tml:compositeListItem>
							<tml:label id="ratingNumber" align="left|middle" focusFontColor="white" />
						</tml:panel>
            		<tml:nullField id="nullField1" height="10"></tml:nullField>
            		<tml:multiline id="address" align="left" fontWeight="system_median"/>
					<tml:nullField id="nullFieldForAddress" height="1" />
					<%if(PoiUtil.showPostLocationAndOpenTable(handlerGloble)) {%>
					<tml:panel id="phonePanel" layout="horizontal" >
		            		<tml:image id="callIcon"/>
		   					<tml:urlLabel id="phone" fontWeight="system_median" align="left|bottom"
								fontColor="#005AFF">
								<tml:menuRef name="call" />
							</tml:urlLabel>
							<tml:nullField id="nullFieldFeedBackB" width="8"></tml:nullField>
							<tml:button id="feedbackB" text="<%=msg.get("common.givefeedback.menu")%>">
								<tml:menuRef name="poiDetailFeedback" />
							</tml:button>
					</tml:panel>
					<%}else{%>
					<tml:panel id="phonePanel">
		            		<tml:image id="callIcon"/>
		   					<tml:urlLabel id="phone" fontWeight="system_median" align="left|bottom"
								fontColor="#005AFF">
								<tml:menuRef name="call" />
							</tml:urlLabel>
							<tml:image id="citySearchDetail"/>
					</tml:panel>
					<%}%>
					
					<%if(PoiUtil.showPostLocationAndOpenTable(handlerGloble)) {%>
					
					<tml:menuItem name="viewReservationPage" pageURL="<%=restaurantUrl%>">
					</tml:menuItem>

					<tml:menuItem name="callViewReservationScript" trigger="TRACKBALL_CLICK" onClick="viewReservationScript">
						<tml:bean name="partnerPoiId" valueType="String" value="" />
					</tml:menuItem>
					<tml:panel id ="PostAndReservePanel" layout="horizontal">
						<tml:nullField id="nullFieldPR1" height="30"></tml:nullField>
						<tml:compositeListItem id="postLocation" bgColor="#FFFFFF">
							<tml:label id="postLocationLabel" align="center" focusFontColor="white">
								<%=msg.get("apps.PostLocation")%>
							</tml:label>
							<tml:image id="facebookLogo"/>
							<tml:image id="twitterLogo"/>
							<tml:menuRef name="postLocationMI2" />
						</tml:compositeListItem>
						<tml:nullField id="nullFieldPR2" height="30"></tml:nullField>
						<tml:compositeListItem id="reservation" bgColor="#FFFFFF">
								<tml:label id="reservationLabel" align="center" focusFontColor="white">
									<%=msg.get("restaurant.make.reservation.link")%>
								</tml:label>
								<tml:image id="openTableLogo"/>
								<tml:menuRef name="callViewReservationScript" />
						</tml:compositeListItem>
					</tml:panel>
					
					<tml:panel id="citySearchDetailPanel">
						<tml:image id="citySearchDetail" />
					</tml:panel>
					<tml:panel id="dashLinePanel">
						<tml:image id="dashLine" />
					</tml:panel>
					<%} else{%>
					<tml:block feature="<%=FeatureConstant.FEEDBACK_POI%>">
						<tml:panel id="feedbackPanel" layout="horizontal">
							<tml:button id="feedbackB" text="<%=msg.get("common.givefeedback.menu")%>">
								<tml:menuRef name="poiDetailFeedback" />
							</tml:button>
						</tml:panel>
					</tml:block>
					<%}%>
				
	           		<tml:nullField id="nullButtField" height="10"></tml:nullField>
	           		<tml:multiline id="sponsorInformation" align="left|top"
						isFocusable="true" heightAutoScale="true">
					</tml:multiline>
	           		
				</tml:panel>

			</tml:tab>

			<tml:block feature="<%=FeatureConstant.UGC_VIEW%>">
				<tml:tab id="Reviews" label="<%=" "+msg.get("poi.reviews") %>"
					fontWeight="bold|system_large">

					<tml:block feature="<%=FeatureConstant.UGC_EDIT%>">
						<tml:button id="reviewButton"
							text="<%=msg.get("poi.write.review")%>" fontWeight="system">
							<tml:menuRef name="rateThis" />
						</tml:button>
					</tml:block>

					<tml:label id="noReviews" align="center|bottom" visible="false"
						textWrap="ellipsis" fontWeight="system" heightAutoScale="true">
						<%=msg.get("poi.no.reviews")%>
					</tml:label>

					<tml:listBox id="reviewsList">
						<%
							for (int i = 0; i < 10; i++) {
						%>
						<tml:menuItem name="<%="item_" + i + "_clicked"%>"
							text="<%=msg.get("poi.show.reviews")%>" onClick="reviewsClick"
							pageURL="<%=getPage + "ShowReviews"%>" trigger="TRACKBALL_CLICK">
							<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
						</tml:menuItem>

						<tml:compositeListItem id="<%="item" + i%>" getFocus="false"
							visible="true" bgColor="#FFFFFF" transparent="false"
							isFocusable="true">
							<tml:label id="<%="reviews_info" + i%>" focusFontColor="white"
								fontWeight="system_median" textWrap="ellipsis"
								align="left">
							</tml:label>

							<tml:image
								id="<%="reviews_starImage1_"
                                                + i%>"/>
							<tml:image
								id="<%="reviews_starImage2_"
                                                + i%>"/>
							<tml:image
								id="<%="reviews_starImage3_"
                                                + i%>"/>
							<tml:image
								id="<%="reviews_starImage4_"
                                                + i%>"/>
							<tml:image
								id="<%="reviews_starImage5_"
                                                + i%>"/>

							<tml:label id="<%="name_time" + i%>" focusFontColor="white"
								fontWeight="system_median" textWrap="ellipsis"
								align="left">
							</tml:label>

							<tml:menuRef name="<%="item_" + i + "_clicked"%>" />

						</tml:compositeListItem>
						<%
							}
						%>
					</tml:listBox>
				</tml:tab>
			</tml:block>
			<tml:tab label="<%=" "+msg.get("poi.coupons") %>"
				id="<%=couponTabID%>" icon="<%=couponsImage%>" fontWeight="bold|system_large">
				<tml:panel id="couponPanel" layout="vertical">
			    	<tml:nullField id="nullMenu"/>
				    <tml:panel id="citySearchPanel">
						<tml:image id="citySearchCoupon" align="left|top"></tml:image>
					</tml:panel>
					<%
						for (int i = 0; i < 5; ++i) {
					%>
					<tml:multiline id="<%="couponDesc"+i %>" fontWeight="system_median"
						isFocusable="true" align="left|top" />
					<tml:image id="<%="couponImg"+i %>" align="center|top"></tml:image>

					<tml:label id="<%="expireDate"+i %>" fontWeight="system_small"
						align="center|top" />

					<%
						}
					%>
				</tml:panel>
			</tml:tab>

			<tml:tab id="<%=menuTabID%>"
				label="<%=" "+msg.get("poi.hours.menu") %>" icon="<%=menuImage%>"
				fontWeight="bold|system_large">
				<tml:panel id="menuPanel" layout="vertical">
					<tml:nullField id="nullMenu"/>
					<tml:panel id="citySearchMenuPanel">
						<tml:image id="citySearchMenu" align="left"></tml:image>
					</tml:panel>
					<tml:multiline id="menu" fontWeight="sytem_large" align="left"
						heightAutoScale="true" isFocusable="true" />
				</tml:panel>
			</tml:tab>
		</tml:tabContainer>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
