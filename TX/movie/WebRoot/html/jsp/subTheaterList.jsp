<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlFeatureHelper"%>
<%
	request.setAttribute("parentIndex",request.getParameter("parentIndex"));
	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	boolean movieBuyFeature = HtmlFeatureHelper.getInstance().supportFeature(clientInfo, HtmlFrameworkConstants.FEATURE.FEATURE_MOVIE_BUY);
	pageContext.setAttribute("movieBuyFeature", movieBuyFeature);
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="/html/jsp/HeaderOfSmall.jsp"%>

<c:forEach items="${theaterList}" var="theater" varStatus="status2">
	<li>
		<div align="center" class="filminfodiv filminfodivBg">
			<div style="display: table; width: 100%;">
				<div class="div_row" style="text-align:left; font-color: #E9E9E9;">
					<div class="div_cell clsMovieTheater">
						<div style="display: table; width: 100%;">
							<div class="div_cell fs_large fc_gray fw_bold">${theater.name}</div>
							<div style="width: 30%;" class="div_cell clsMovieTheater">
								<c:choose>
									<c:when test="${!empty theater.scheduleItem.ticketURI && movieBuyFeature}">
								    	<input type='button' class='buttonstyle clsBuyButtonBk fs_middle' value='<%=msg.get("buy.msg") %>' onClick='onClickBuyInMovieList(${parentIndex},${status2.index},"${theater.id}","${theater.name}","${theater.addressDisplay}",this)' />
									</c:when>
									<c:otherwise>
											 	
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="div_table">
								<div class="div_cell fc_gray fs_verySmall fw_bold" colspan="2"> ${theater.addressDisplay}</div>
						</div>
						<div class="div_table">
							<div class="div_cell" colspan="2"> <br/></div>
						</div>
						<div class="div_table">
							<div class="div_cell fc_gray fs_smallest fw_bold" colspan="2"><html:msg key="movie.showtime"/>&nbsp;<span id="showTimesLabel${parentIndex}-${status2.index}"></span>
							</div>
						</div>
					</div>
				</div>
				<div class="div_row">
					<div class="div_cell fc_white fs_verySmall fw_bold showfilmtr2 showfilmtr2Bg" style="text-align: center;" colspan="2">
						<b>${theater.distance} ${theater.distanceUnit} <html:msg key="movie.away"/></b>
					</div>
				</div>
			</div>
		</div>
	</li>
	<input id="showTimes${parentIndex}-${status2.index}" type="hidden" value="${theater.scheduleItem.showTimeString}">
	<input id="ticketURI${parentIndex}-${status2.index}" type="hidden" value="${theater.scheduleItem.ticketURI}">
</c:forEach>
