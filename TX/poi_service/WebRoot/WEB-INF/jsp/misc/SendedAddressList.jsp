<%@ include file="../Header.jsp"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.misc.struts.datatype.Address"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%
	List<Address> addressList = (List<Address>)request.getAttribute("addressList");
	DataHandler handler = (DataHandler)request.getAttribute("DataHandler");
	String pageUrl = host + "/SentAddress.do?pageRegion=" + region;
%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/AddressInfoModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
			func onLoad()
				TxNode node = ParameterSet.getParam("addressDataNode")
				JSONArray ja = JSONArray.fromString(TxNode.msgAt(node,0))
				if NULL == ja || 0 == JSONArray.length(ja)
				   System.showErrorMsg("<%=msg.get("addressinfo.no.sent.address")%>")
				   return FAIL
				endif
				
				AddressInfo_M_setAddressList(ja)
			endfunc
						
			func onClickDetail()
				TxNode node = ParameterSet.getParam("indexClicked")
				AddressInfo_M_setIndex(node)
				System.doAction("showDetail");
			endfunc
		]]>
		</tml:script>

	<tml:menuItem name="showDetail" pageURL="<%=getPage + "AddressInfo"%>">
	</tml:menuItem>
	<tml:page id="SendedAdressList"  url="<%=pageUrl%>" type="net" groupId="<%=GROUP_ID_MISC%>">
		<%
			handler.toXML(out);
		%>
		<!-- title --> 
		<tml:title id="title" fontWeight="bold|system_large" align="center" fontColor="white">
			<%=msg.get("sendaddress.title")%>
		</tml:title>
		<tml:listBox id="menuListBox" 	isFocusable="true" getFocus="true" selectIndex="0" hotKeyEnable="false">
			<tml:menuItem name="SyncMenu" pageURL="<%=pageUrl +"&amp;action=sync"%>" trigger="KEY_RIGHT | TRACKBALL_CLICK" progressBarText="<%=msg.get("sendaddress.synmessage.title") + "$" + msg.get("sendaddress.synmessage.body")%>">
			</tml:menuItem>
			<tml:listItem id="item0" fontWeight="bold|system_large" align="left" 
				blurIconImage="<%=imageUrl + "/sync.png"%>" focusIconImage="<%=imageUrl + "/sync.png"%>"
				isFocusable="true" showArrow="false" multLine="true">
                <%=msg.get("sendaddress.sync")%>
            <tml:menuRef name="SyncMenu"/>
 			</tml:listItem>
			<% for ( int i = 0; i < addressList.size(); i++ )
			   {
					Address address = addressList.get(i);
					String text = "<bold>" + address.getLabel() + "</bold> \n" +  TnUtil.getFormatedDate(address.getCreateTime(),"MMM dd, yyyy hh:mm a");
					String menuName = "getInfo" + i;
			%>
			
			<tml:menuItem name="<%=menuName%>" onClick="onClickDetail" trigger="KEY_RIGHT | TRACKBALL_CLICK">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>
			<tml:listItem id="<%="item" + (i+1)%>" fontWeight="system_large" align="left" lines="2"
				isFocusable="true" showArrow="false" multLine="true">
                 <%=TnUtil.amend(text)%>
            <tml:menuRef name='<%=menuName%>'/>
 			</tml:listItem>
			<% } %>
		</tml:listBox>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>

