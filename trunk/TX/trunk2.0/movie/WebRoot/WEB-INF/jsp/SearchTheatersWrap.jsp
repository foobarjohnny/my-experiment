<%@ include file="header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.browser.movie.Constants"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>


<%
    int pageNo = ((Integer) request.getAttribute("theaterStartPage"))
            .intValue();

    StringBuffer sb = new StringBuffer();
    sb.append(host).append("/ChangeTheaterPage.do");
    sb.append("?theaterTimestamp=");
    sb.append(request.getAttribute("theaterTimestamp"));
    sb.append("&amp;lon=");
    sb.append(request.getAttribute("lon"));
    sb.append("&amp;lat=");
    sb.append(request.getAttribute("lat"));
    sb.append("&amp;radius=");
    sb.append(request.getAttribute("radius"));
    sb.append("&amp;theaterPageNumber=");

    String urlInit = sb.toString();
%>
<tml:TML outputMode="TxNode" showUrl="<%=urlInit + pageNo%>">
	<tml:page url="<%="movies_search_result_" + pageNo%>" x="0" y="0"
		width="<%=lScreenWidth%>" height="<%=lScreenHeight%>" type="prefetch">
		<%
		    DataHandler handler = (DataHandler) request
		                    .getAttribute("DataHandler");
		            handler.toXML(out);
		%>
	</tml:page>
	<tml:bundlepage>
		<%
		    int prefetchCount = Constants.PREFETCH_PAGE_COUNT;
		            int totalCount = ((Integer) request
		                    .getAttribute("theaterPagesAmount")).intValue();
		            int count = pageNo + prefetchCount;
		            if (count > totalCount) {
		                count = totalCount;
		            }

		            for (int i = 0; i + pageNo < count; i++) {
		%>
		<c:import url="<%=host + "/ChangeTheaterPage.do"%>">
			<c:param name="theaterTimestamp"
				value="<%=request.getAttribute("theaterTimestamp")
                                            .toString()%>" />
			<c:param name="theaterPageNumber" value="<%=pageNo + i + ""%>" />
			<c:param name="theaterFromWrap" value="true" />
			<c:param name="lon"
				value="<%=request.getAttribute("lon").toString()%>" />
			<c:param name="lat"
				value="<%=request.getAttribute("lat").toString()%>" />
			<c:param name="radius"
				value="<%=request.getAttribute("radius")
                                                    .toString()%>" />
			<c:param name="theaterIds"
				value="<%=request.getAttribute("theaterIds")
                                            .toString()%>" />
			<c:param name="<%=DataHandler.KEY_LOCALE%>" value="<%=request.getParameter(DataHandler.KEY_LOCALE)%>" />
			<c:param name="<%=DataHandler.KEY_REGION%>" value="<%=request.getParameter(DataHandler.KEY_REGION)%>" />
			<c:param name="<%=DataHandler.KEY_CARRIER%>" value="<%=request.getParameter(DataHandler.KEY_CARRIER)%>" />
			<c:param name="<%=DataHandler.KEY_PLATFORM%>" value="<%=request.getParameter(DataHandler.KEY_PLATFORM)%>" />
			<c:param name="<%=DataHandler.KEY_DEVICEMODEL%>" value="<%=request.getParameter(DataHandler.KEY_DEVICEMODEL)%>" />
			<c:param name="<%=DataHandler.KEY_WIDTH%>" value="<%=request.getParameter(DataHandler.KEY_WIDTH)%>" />
			<c:param name="<%=DataHandler.KEY_HEIGHT%>" value="<%=request.getParameter(DataHandler.KEY_HEIGHT)%>" />
			<c:param name="<%=DataHandler.KEY_PRODUCTTYPE%>" value="<%=request.getParameter(DataHandler.KEY_PRODUCTTYPE)%>" />
			<c:param name="<%=DataHandler.KEY_VERSION%>" value="<%=request.getParameter(DataHandler.KEY_VERSION)%>" />
		</c:import>
		<%
		    }
		%>
	</tml:bundlepage>
</tml:TML>
