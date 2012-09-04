<%@ include file="header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.browser.movie.Constants"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>

<%
    int pageNo = ((Integer) request.getAttribute("startPage"))
            .intValue();
    String shortDate = (String) request.getAttribute("shortDate");
    StringBuffer sb = new StringBuffer();
    sb.append(host).append("/ChangePage.do");
    sb.append("?timestamp=");
    sb.append(request.getAttribute("timestamp"));
    sb.append("&amp;sortBy=");
    sb.append(request.getAttribute("sortBy"));
    sb.append("&amp;shortDate=");
    sb.append(shortDate);
    sb.append("&amp;pageNumber=");

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
		                    .getAttribute("moviePagesAmount")).intValue();
		            int count = pageNo + prefetchCount;
		            if (count > totalCount) {
		                count = totalCount;
		            }

		            for (int i = 0; i + pageNo < count; i++) {
		%>
		<c:import url="<%=host + "/ChangePage.do"%>">
			<c:param name="timestamp"
				value="<%=request.getAttribute("timestamp")
                                            .toString()%>" />
			<c:param name="pageNumber" value="<%=pageNo + i + ""%>" />
			<c:param name="fromWrap" value="true" />
			<c:param name="sortBy"
				value="<%=request.getAttribute("sortBy")
                                                    .toString()%>" />
			<c:param name="movieIds"
				value="<%=request.getAttribute("movieIds")
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
			<c:param name="shortDate"
				value="<%=request.getAttribute("shortDate")
                                            .toString()%>" />
		</c:import>
		<%
		    }
		%>
	</tml:bundlepage>
</tml:TML>
