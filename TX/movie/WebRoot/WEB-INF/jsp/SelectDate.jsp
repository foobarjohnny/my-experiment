<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="Header.jsp" %>

<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>

<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.telenav.browser.movie.Constant"%>
<%@page import="com.telenav.browser.movie.Util"%>

<%
    String imageBg = imageUrl + "background_telenav.png";
    String pageURL = getPage + "SelectDate";
	
    Locale loc = null;
	if (!"en_US".equals(locale)){
		String lang = locale.substring(0,2);
		String country = locale.substring(3);
		loc = new Locale(lang, country);
	}
	Date currentDate = new Date();
    SimpleDateFormat sdf = null; 
    if (loc != null){
    	// TODO optimize me... use it from cache
    	sdf = new SimpleDateFormat(Constant.DATE_FORMAT_STR , loc);
    }else{
    	// default formatter for US
    	sdf = (SimpleDateFormat)Constant.DATE_FORMAT.clone();
    }
    
%>

<tml:TML outputMode="TxNode">
	<jsp:include page="controller/SelectDateController.jsp" />
	<jsp:include page="controller/MovieController.jsp" />
	<jsp:include page="model/MovieModel.jsp" />
	
	<%@include file="DateUtil.jsp" %>

	<tml:script language="fscript" version="1">
		<![CDATA[
		        
		        func preLoad()
		        	int i = 0
	        		int time = Time.get()
	        		String timeStr 
	        		TxNode node
		        	while i<10
		        		timeStr = Time.format("EEEE, MMM d", time)
		        		if i ==0
		        			Page.setComponentAttribute("i"+i,"text",timeStr + " <%=msg.get("mSearch.today")%>")
		        		else
		        			Page.setComponentAttribute("i"+i,"text",timeStr)
		        		endif
		        		node = makeTxNode(timeStr)
						MenuItem.setBean("selectDate"+i, "dateStr", node)
		        		time = time + 86400000
		        		i = i + 1
		        	endwhile
		        endfunc
		        
		        func saveWhen()
				    TxNode id = ParameterSet.getParam("id")
				    TxNode label = ParameterSet.getParam("dateStr")
				    SelectDate_C_saveDate(id, label)
			        String saveKey="<%= Constant.StorageKeyForJSON.MOVIE_SELECT_DATE_PARAMS_JSON%>"
				    
					JSONObject jo = Cache.getJSONObjectFromTempCache(saveKey)
					if NULL != jo
						int idx = String.convertToNumber(TxNode.msgAt(id,0))
						String dateStr = getDateStr(idx)
						TxNode nodeDate = makeTxNode(dateStr)
						MenuItem.setBean("CallBack", "dateIndex", nodeDate)
						
						String tmp = JSONObject.getString(jo,"<%=Constant.RRKey.M_PAGE_URL%>")
						MenuItem.setAttribute("CallBack", "url", tmp)
						
						tmp = JSONObject.getString(jo,"<%=Constant.RRKey.M_CALLBACK_FUNCTION%>")
						TxNode tmpNode
						if NULL != tmp
							tmpNode = makeTxNode(tmp)
							MenuItem.setBean("CallBack", "callFunction", tmpNode)
						endif
						
						String tmpStr = "" + JSONObject.get(jo,"<%=Constant.RRKey.M_THEATER_ID%>")
						if tmpStr != "-1"
							tmpNode = makeTxNode(tmpStr)
							MenuItem.setBean("CallBack", "theaterId", tmpNode)
						else
							#only movieId then we need to add address information
							JSONObject add = Movie_M_getAddress()
							int lat = JSONObject.getInt(add, "lat")
							int lon = JSONObject.getInt(add, "lon")
							tmpNode = makeIntTxNode(lat)
							MenuItem.setBean("CallBack", "lat", tmpNode)
							tmpNode = makeIntTxNode(lon)
							MenuItem.setBean("CallBack", "lon", tmpNode)
						endif
						
						tmp = JSONObject.getString(jo,"<%=Constant.RRKey.M_MOVIE_ID%>")
						if NULL != tmp
							tmpNode = makeTxNode(tmp)
							MenuItem.setBean("CallBack", "movieId", tmpNode)
						endif
				    else
				    	SearchMovie_C_showSearch()
				    endif
				    System.doAction("CallBack")
		        endfunc
		        
		        func makeTxNode(String value)
		        	TxNode node
		        	TxNode.addMsg(node, value)
		        	return node
		        endfunc
		        
		        func makeIntTxNode(int value)
		        	TxNode node
		        	TxNode.addValue(node, value)
		        	return node
		        endfunc
			]]>
	</tml:script>
	
	<tml:menuItem name="CallBack" pageURL="">
			<tml:bean name="theaterId" valueType="String" value="-1" />
			<tml:bean name="movieId" valueType="String" value="" />
			<tml:bean name="dateIndex" valueType="String" value="0" />
			<tml:bean name="dateChanged" valueType="String" value="true" />
	</tml:menuItem>
	
	<tml:page url="<%=pageURL%>" id="selectDate" type="<%=pageType%>" helpMsg="$//$moviesshowtime" groupId="<%=MOVIE_GROUP_ID%>">
		<%--title--%>
		<tml:title id="title" align="center|middle"
			fontWeight="bold|system_large" fontColor="white">
			<%=msg.get("date.title")%>
		</tml:title>

		<%--select date--%>
		<tml:listBox id="dateList" isFocusable="true" hotKeyEnable="false">
			<%
			    for (int i = 0; i < 10; i++) {
                    String menuName = "selectDate" + i;
                    String id = "" + i;
			%>
			<tml:menuItem name='<%=menuName%>' onClick="saveWhen">
				<tml:bean name="id" valueType="String" value="<%=id%>"/> 
				<tml:bean name="dateStr" valueType="String" value="" />
			</tml:menuItem>
			<tml:listItem id="<%="i" + id%>" align="left" fontWeight="system_large|bold">
				<tml:menuRef name="<%=menuName%>" />
			</tml:listItem>
			<%
                }
			%>
		</tml:listBox>
		<cserver:outputLayout />
	</tml:page>
</tml:TML>