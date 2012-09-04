<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.movie.html.datatypes.MovieItem"%>
<%@page import="com.telenav.cserver.movie.html.util.HtmlMovieUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	int movieCount = ((List)request.getAttribute("movieList")).size();
	int startIndex = (Integer)request.getAttribute("startIndex");
%>

<%@ include file="/html/jsp/HeaderOfSmall.jsp"%>

<div class="arrowlistmenu" >
	<c:forEach items="${movieList}" var="movie" varStatus="status1">
		<div class="menuheader expandable"  movieId="${movie.id}">
			<div style="width: 100%; display: table;">
				<div class="div_table" style="text-align: left;">
		        	<div class="div_cell moviename fs_middle clsFontColor_TheaterName"><b>${movie.name}</b></div>
					<div class="div_cell" style="text-align: center; width: 30%;"> 
					
						<c:set var="rating" value="${movie.rating}" scope="page"></c:set>
						<!-- two image must be placed in the same line, or the two images will have a space between them. So I use the following code to implement -->
						<%  
							double rating = (Double)pageContext.getAttribute("rating");
							String cominedImage = HtmlMovieUtil.getCominedImage(imageCommonUrl,rating,"medium");
							request.setAttribute("cominedImage",cominedImage);
						%>
						${cominedImage} 
					
					</div>
				</div>
				<div class="div_table" style="text-align: left;">
			    	<div class="div_cell theateraddress fs_middle fc_gray" style="vertical-align: bottom;">
					    <div class="clsMovieDesc" id="movieDec${status1.index + startIndex}"></div>
					   
					    <input id="hiddenMovieDec${status1.index + startIndex}" value="${movie.description}" type="hidden">
			    	</div>
				</div>	
			</div> 
		</div>
		<div style="width:100%" align="center"  class="showfilmdiv">
			<ul class="categoryitems">
				<div class="topstyle topstylebackground movieinfor${movie.id}" style="display:none"></div>
				<li  class="movieinfor${movie.id}" style="display:none">
					<div align="center" class="filminfodiv filminfodivBg">
						<div style="width: 100%; display: table;">
							<div class="div_row" style="text-align: left;">
								<div class="div_cell imgplace">
								    <c:choose>
										<c:when test="${!empty movie.image}">
											 	 <img id="movieImageId${status1.index + startIndex}-0" class="clsMovieImage" src="data:image/gif;base64,${movie.image}"></img>
										</c:when>
										<c:otherwise>
										 		<img id="movieImageId${status1.index + startIndex}-0" class="clsMovieImage" src="<%=imageCommonUrl%>movie_image_80x120.png"  ></img>
										</c:otherwise>
									</c:choose>
								</div>
								<div class="div_cell">
									<div style="width: 100%; display: table;">
										<div class="div_row" style="text-align: left;">
											<div class="div_cell">
												<span class="fc_gray fs_middle fw_bold"><html:msg key="movie.synopsis"/>&nbsp;</span>
												<span class="fc_gray fs_verySmall fw_bold">${movie.description}</span>
											</div>
										</div>
										<div class="div_row" style="text-align: left;">
											<div class="div_cell fs_verySmall clsFontColor_RunTime fw_bold">
												<b><html:msg key="movie.runtime"/>&nbsp;${movie.runtime}</b>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</li>
				<div class="placeHolder">
					<%
						MovieItem movie = (MovieItem)pageContext.getAttribute("movie");
						String ajaxUrl = hostUrl + "movieList.do?pageType=subList&movieId=" + movie.getId()  + "&" + request.getQueryString()+"&currTime="+System.currentTimeMillis();
					%>
					<a href="<%=ajaxUrl%>&parentIndex=${status1.index + startIndex}" class="hiddenajaxlink">Theater List</a>
				</div>
				<!--insert url to load detail list here-->
				<div class="bottomstyle bottomstylebackground movieinfor${movie.id}" style="display:none"></div>
			</ul>
		</div>
		<input id="movieId${status1.index + startIndex}" type="hidden" value="${movie.id}">
		<input id="movieName${status1.index + startIndex}" type="hidden" value="${movie.name}">
		<input id="hasLoaded${status1.index + startIndex}" type="hidden" value="false">
	</c:forEach>
</div>

