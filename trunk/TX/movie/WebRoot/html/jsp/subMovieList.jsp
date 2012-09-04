<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.cserver.movie.html.util.HtmlMovieUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlFeatureHelper"%>
<%
	request.setAttribute("parentIndex", request.getParameter("parentIndex"));
	HtmlClientInfo clientInfo = (HtmlClientInfo) request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	boolean movieBuyFeature = HtmlFeatureHelper.getInstance().supportFeature(clientInfo, HtmlFrameworkConstants.FEATURE.FEATURE_MOVIE_BUY);
	pageContext.setAttribute("movieBuyFeature", movieBuyFeature);
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="/html/jsp/HeaderOfSmall.jsp"%>

<ul class="categoryitems">
	<div class="topstyle topstylebackground"></div>
	<c:forEach items="${movieList}" var="movie" varStatus="status2">
		<c:set var="movie" value="${movie}" scope="request"></c:set>
		<li>
			<div style="display: table; text-align: center" class="theaterMovieinfoDiv theaterMovieinfoDivBg">
				<table width="100%" cellpadding="0" border="0" cellspacing="0">
					<tr>
						<td width="20%" rowspan="2" class="imgplace"><c:choose>
								<c:when test="${!empty movie.scheduleItem.ticketURI && movieBuyFeature}">
									<c:choose>
										<c:when test="${!empty movie.image}">
											<img id="movieImageId${parentIndex}-${status2.index}" class="clsMovieImage" src="data:image/gif;base64,${movie.image}"
												onClick='onClickBuyInTheaterList(${parentIndex},${status2.index},this)'></img>
										</c:when>
										<c:otherwise>
											<img id="movieImageId${parentIndex}-${status2.index}" class="clsMovieImage" src="<%=imageCommonUrl%>movie_image_80x120.png"
												onClick='onClickBuyInTheaterList(${parentIndex},${status2.index},this)'></img>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${!empty movie.image}">
											<img id="movieImageId${parentIndex}-${status2.index}" class="clsMovieImage" src="data:image/gif;base64,${movie.image}"></img>
										</c:when>
										<c:otherwise>
											<img id="movieImageId${parentIndex}-${status2.index}" class="clsMovieImage" src="<%=imageCommonUrl%>movie_image_80x120.png"></img>
										</c:otherwise>
									</c:choose>

								</c:otherwise>
							</c:choose></td>
						<td width="80%" valign="top">
							<table width="100%" height="100%" cellpadding="0" cellspacing="0" border="0">
								<tr height="20%">
									<td class="fc_gray fs_middle fw_bold" colspan="2">${movie.name}</td>
								</tr>
								<tr height="80%">
									<td class="fc_gray fs_smallest fw_bold" colspan="2"><html:msg key="movie.showtime" />&nbsp;<span id="showTimesLabel${parentIndex}-${status2.index}"></span></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td width="80%" valign="bottom" class="movieTicShowTime">
							<table width="100%" cellpadding="0" cellspacing="0" border="0">
								<tr>
									<td valign="bottom"><c:set var="rating" value="${movie.rating}" scope="page"></c:set> <%
 	double rating = (Double) pageContext.getAttribute("rating");
 		String cominedImage = HtmlMovieUtil.getCominedImage(imageCommonUrl, rating, "big");
 		request.setAttribute("cominedImage", cominedImage);
 %> ${cominedImage}</td>
									<td width="12%" align="right" valign="bottom" onClick="forwardToRating('${movie.tomatoRatingUrl}')"><c:choose>
												<c:when test="${movie.tomatoRatingStatus == 1}">
													<img class="clsTomatoIcon clsTomatoFreshUnfocused"></img>
												</c:when>
												<c:when test="${movie.tomatoRatingStatus == -1}">
													<img class="clsTomatoIcon clsTomatoRottenUnfocused"></img>
												</c:when>
											</c:choose>
									</a></td>
									<td class="clsTomatoRatingUrl" align="center" valign="bottom" onClick="forwardToRating('${movie.tomatoRatingUrl}')"><c:choose>
												<c:when test="${movie.tomatoRatingStatus == 1}">
													<b class="tomatoRating fs_middle clsFontColor_red">${movie.tomatoRating}</b>
												</c:when>
												<c:when test="${movie.tomatoRatingStatus == -1}">
													<b class="tomatoRating fs_middle clsFontColor_green">${movie.tomatoRating}</b>
												</c:when>
											</c:choose>
									</a></td>
									<td class="clsTheaterListBuyTd" valign="bottom"><c:choose>
											<c:when test="${!empty movie.scheduleItem.ticketURI && movieBuyFeature}">
												<input type='button' class='buttonstyle clsBuyButtonBk fs_middle' value='<%=msg.get("buy.msg") %>' onClick='onClickBuyInTheaterList(${parentIndex},${status2.index},this)' />
											</c:when>
											<c:otherwise>

											</c:otherwise>
										</c:choose></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<div class="div_table showfilmtr2 showfilmtr2Bg">
					<div class="div_cell" style="width: 1%;"></div>
					<div class="div_cell" style="width: 65%; text-align: left;">
						<p class="fc_white fs_verySmall fw_bold">
							<html:msg key="movie.runtime" />
							&nbsp;${movie.runtime}
						</p>
					</div>
					<div class="div_cell" style="padding-right: 12px; vertical-align: middle; text-align: right;">
						<c:if test="${not empty movie.grade}">
							<span class="clsPStyle fc_black fs_verySmall fw_bold" align="right">${movie.grade}</span>
						</c:if>
					</div>
				</div>


			</div>
		</li>
		<input id="movieId${parentIndex}-${status2.index}" type="hidden" value="${movie.id}">
		<input id="movieName${parentIndex}-${status2.index}" type="hidden" value="${movie.name}">
		<input id="showTimes${parentIndex}-${status2.index}" type="hidden" value="${movie.scheduleItem.showTimeString}">
		<input id="ticketURI${parentIndex}-${status2.index}" type="hidden" value="${movie.scheduleItem.ticketURI}">
		<input id="wangdong${parentIndex}" type="hidden" value="${movie.scheduleItem.ticketURI}">
	</c:forEach>
	<div class="bottomstyle bottomstylebackground"></div>
</ul>
