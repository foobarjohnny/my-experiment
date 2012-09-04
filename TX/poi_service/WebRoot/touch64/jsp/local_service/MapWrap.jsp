<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@ page import="com.telenav.cserver.localservice.MapSerivce"%>
<%@page import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.cserver.browser.util.ClientHelper"%>

<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="/touch64/jsp/Header.jsp"%>
<%
    String pageUrl = getPage + "MapWrap";
	String commuteModuleName = ClientHelper.getModuleNameForCommute(handlerGloble);

	String commuteAlertUrl = "{commuteAlert.http}/" + commuteModuleName + "/goToJsp.do?pageRegion=" + region + "&amp;jsp=Startup";
	String aboutMapsUrl=getPage+"AboutMenu";
	
	
%>
<%@page import="com.telenav.cserver.stat.*"%>
<tml:TML outputMode="TxNode">
    <%@ include file="/touch64/jsp/ac/controller/AddressCaptureController.jsp"%>

	<%@ include file="/touch64/jsp/local_service/model/MapWrapModel.jsp"%>
	<jsp:include
		page="/touch64/jsp/local_service/controller/DriveToWrapController.jsp" />
			<%@ include
		file="/touch64/jsp/controller/ATTExtrasController.jsp"%>
		<%@ include file="/touch64/jsp/ac/controller/ShareAddressController.jsp"%>
		
	<jsp:include page="../ac/controller/EditRouteController.jsp" />
	<jsp:include page="/touch64/jsp/ac/controller/SelectAddressController.jsp" />
	<jsp:include page="/touch/jsp/poi/StatLogger.jsp" />
	<%@ include file="/touch64/jsp/poi/controller/SearchPoiController.jsp"%>
	<%@ include file="/touch64/jsp/poi/controller/ShowDetailController.jsp"%>
	<%@ include file="/touch64/jsp/poi/controller/PoiListController.jsp"%>
	<%@ include file="/touch64/jsp/ac/controller/RecordLocationController.jsp"%>
	<%@ include file="/touch64/jsp/ac/controller/CreateFavoritesController.jsp"%>
    <jsp:include page="/touch64/jsp/model/DriveToModel.jsp" />
	<jsp:include page="/touch/jsp/common/commute/controller/CommuteAlertController.jsp" />
	<%@ include file="/touch64/jsp/dsr/controller/DSRController.jsp"%>
	<%@ include file="/touch64/jsp/ac/model/SetUpHomeModel.jsp"%>
	<%@ include file="/touch64/jsp/model/StartUpModel.jsp"%>
	<jsp:include page="MapWrapScript.jsp" />

    <tml:menuItem name="home" pageURL="<%=host + "/startUp.do?pageRegion=" + region%>">
	</tml:menuItem>
     
	<tml:actionItem name="showMapAction"
		action="<%=Constant.LOCALSERVICE_MAPIT%>">
		<tml:output name="mapResponse" />
		<tml:input name="mapParm" />
	</tml:actionItem>
	<tml:menuItem name="showMap" actionRef="showMapAction"
		onClick="mapServiceReturned">
	</tml:menuItem>

	<tml:menuItem name="commuteAlert" pageURL="<%=commuteAlertUrl%>"></tml:menuItem>
	<tml:menuItem name="recordLocation" pageURL="" />
	<tml:menuItem name="aboutMaps" pageURL="<%=aboutMapsUrl%>"></tml:menuItem>

	<tml:page id="MapWrap" url="<%=pageUrl%>" type="<%=pageType%>"
		showLeftArrow="false" showRightArrow="false" helpMsg=""
		groupId="<%=GROUP_ID_COMMOM%>">
	</tml:page>
</tml:TML>