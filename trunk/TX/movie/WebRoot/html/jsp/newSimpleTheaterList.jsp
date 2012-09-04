<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.cserver.movie.html.datatypes.MovieItem"%>
<%@page import="com.telenav.cserver.movie.html.datatypes.TheaterItem"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="/html/jsp/HeaderOfSmall.jsp"%>

<%
	int startIndex = (Integer)request.getAttribute("startIndex");
%>

<div class="arrowlistmenu">
	<c:forEach items="${theaterList}" var="theater" varStatus="status1">
		<input id="theatherId${status1.index + startIndex}" type="hidden" value="${theater.id}">
		<div class="menuheader expandable">
			<div style="width: 100%; display: table;">
				<div style="width: 100%; display: table;">
					<div class="div_cell theatername fs_middle clsFontColor_TheaterName" style="text-align: left" >
						${theater.name}
					</div>
					<div class="div_cell distance fs_middle fc_gray" style="text-align: right" >
						${theater.distance}&nbsp;${theater.distanceUnit}
					</div>
				</div>
				<div style="width: 100%; display: table;">
					<div class="div_cell theateraddress fs_middle fc_gray" style="text-align: left" >
						${theater.addressDisplay}
					</div>
				</div>
			</div>
		</div>
		<div style="width: 100%" align="center" class="showfilmdiv">
			<%
				TheaterItem theater = (TheaterItem) pageContext.getAttribute("theater");
					String ajaxUrl = hostUrl + "theaterList.do?pageType=subList&theaterId=" + theater.getId() + "&"
							+ request.getQueryString()+"&currTime="+System.currentTimeMillis();
			%>
			<a href="<%=ajaxUrl%>&parentIndex=${status1.index + startIndex}" class="hiddenajaxlink">Movie List</a>
		</div>
		<input id="theaterId${status1.index + startIndex}" type="hidden" value="${theater.id}">
		<input id="theaterName${status1.index + startIndex}" type="hidden" value="${theater.name}">
		<input id="theaterAddress${status1.index + startIndex}" type="hidden" value="${theater.addressDisplay}">
		<input id="hasLoaded${status1.index + startIndex}" type="hidden" value="false">
	</c:forEach>
</div>


