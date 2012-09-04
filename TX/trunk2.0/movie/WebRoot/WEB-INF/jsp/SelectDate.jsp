<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="header.jsp"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>

<%@page import="com.telenav.browser.movie.Constants"%>
<%@page import="com.telenav.browser.movie.layout.MoviesLayout"%>
<%@page import="com.telenav.browser.movie.util.MovieUtil"%>
<%
	Date currentDate = new Date();
	SimpleDateFormat sdf = Constants.DATE_FORMAT;
%>
<%@page import="java.net.URLEncoder"%>
<tml:TML outputMode="TxNode">
	<tml:page id="selectDatePage" url="<%=host + "/SelectDate.do"%>" x="0"
		y="0" width="<%=lScreenWidth%>" height="<%=lScreenHeight%>"
		type="prefetch">
		<%--title--%>
		<tml:title id="selectDateTitle" x="0" y="3" width="<%=lScreenWidth%>"
			height="<%=lTitleHeight%>" fontSize="18"
			fontWeight="bold|system_large" align="center" fontColor="<%=lTitleFontColor%>">
			<bean:message key="movies.selectdate.title" />
		</tml:title>

		<%--select date--%>
		<tml:listBox id="dateList" name="movies::dateList" x="0"
			y="<%=lTitleHeight%>" width="<%=lScreenWidth%>"
			height="<%=lScreenHeight-lTitleHeight%>" isFocusable="true"
			hotKeyEnable="false">
			<%
				for (int i = 0; i < 30; i++) {
								String menuName = "selectDate" + i;
								String dateStr = sdf.format(currentDate);
								if (dateStr == null) {
									dateStr = "";
								}
			%>
			<tml:menuItem name="<%=menuName%>"
				pageURL="<%=host + "/InputCondition.do"%>"
				trigger="KEY_RIGHT | TRACKBALL_CLICK">
				<tml:bean name="callFunction" valueType="String" value="setDate" />
				<tml:bean name="dateLong" valueType="String"
					value="<%=currentDate.getTime() + ""%>" />
				<tml:bean name="dateStr" valueType="String" value="<%=dateStr%>" />
			</tml:menuItem>

			<tml:urlImageLabel id="<%="date"+i%>" x="0" y="0"
				width="<%=lScreenWidth%>" height="<%=lHeight%>" align="left"
				fontSize="18" fontWeight="bold|system" showArrow="false">
				<%
					if (i != 0) {
				%>
				<%=MovieUtil.amend((i + 1) + ". "
													+ dateStr)%>
				<%
					} else {
				%>
				<%=MovieUtil.amend((i + 1) + ". "
													+ dateStr + " (Today)")%>
				<%
					}
				%>

				<tml:menuRef name="<%=menuName%>" />
			</tml:urlImageLabel>
			<%
				currentDate.setDate(currentDate.getDate() + 1);
							}
			%>
		</tml:listBox>
	</tml:page>
</tml:TML>