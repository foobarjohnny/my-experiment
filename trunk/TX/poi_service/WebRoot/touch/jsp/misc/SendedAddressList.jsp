<%@ include file="../Header.jsp"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.misc.struts.datatype.Address"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%
	List<Address> addressList = (List<Address>)request.getAttribute("addressList");
	String popupMessage = (String)request.getAttribute("popupMessage");
	DataHandler handler = (DataHandler)request.getAttribute("DataHandler");
	String pageUrl = host + "/SentAddress.do?pageRegion=" + region;
%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/AddressInfoModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
			func onLoad()
				TxNode node = ParameterSet.getParam("addressDataNode")
				JSONArray ja= JSONArray.fromString(TxNode.msgAt(node,0))
				AddressInfo_M_setAddressList(ja)
			endfunc
						
			func onClickDetail()
				TxNode node = ParameterSet.getParam("indexClicked")
				AddressInfo_M_setIndex(node)
				System.doAction("showDetail");
				return FAIL
			endfunc
		]]>
		</tml:script>

	<tml:menuItem name="showDetail" pageURL="<%=getPage + "AddressInfo"%>"/>
	<tml:menuItem name="SyncMenu" pageURL="<%=pageUrl +"&amp;action=sync"%>" trigger="TRACKBALL_CLICK" progressBarText="<%=msg.get("sendaddress.synmessage.title") + " \n " + msg.get("sendaddress.synmessage.body")%>">
	</tml:menuItem>
			
	<tml:page id="SendedAdressList"  url="<%=pageUrl%>" type="<%=pageType%>" popUpMsg="<%=popupMessage%>" groupId="<%=GROUP_ID_MISC%>">
		<%
			handler.toXML(out);
		%>
		
		<!-- title --> 
		<tml:title id="title" fontWeight="bold|system_large" align="center" fontColor="white">
			<%=msg.get("sendaddress.title")%>
		</tml:title>
		<tml:listBox id="menuListBox" 	isFocusable="true" getFocus="true" selectIndex="0" hotKeyEnable="false">
			<% for ( int i = 0; i < addressList.size(); i++ )
			   {
					Address address = addressList.get(i);
					String text = "<bold>" + address.getLabel() + "</bold> \n" +  TnUtil.getFormatedDate(address.getCreateTime(),"MMM dd, yyyy hh:mm a");
					String menuName = "getInfo" + i;
			%>
			
			<tml:menuItem name="<%=menuName%>" onClick="onClickDetail" trigger="TRACKBALL_CLICK">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>
			<tml:listItem id="<%="item" + (i+1)%>" fontWeight="system_large" align="left" lines="2"
				isFocusable="true" showArrow="false" multLine="true">
                 <%=TnUtil.amend(text)%>
            <tml:menuRef name='<%=menuName%>'/>
 			</tml:listItem>
			<% } %>
		</tml:listBox>
		<tml:image id="titleShadow" url="" align="left|top"/>
		<tml:image	id="shadowBg" url="" align="left|top"/>
		<tml:image	id="controlBg" url="" align="left|top"/>
		<tml:compositeListItem id="sync" >
			<tml:image id="syncIcon" align="right"/>
			<tml:label id="syncLabel" fontWeight="system_large" align="left">
				<%=PoiUtil.amend(msg.get("common.syncWithWebsite"))%>
			</tml:label>
			<tml:menuRef name="SyncMenu"/>
		</tml:compositeListItem>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>

