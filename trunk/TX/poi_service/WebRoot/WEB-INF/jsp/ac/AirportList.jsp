<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@ include file="../Header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/AddressCaptureModel.jsp"%>
	<%@ include file="/WEB-INF/jsp/StopUtil.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
		<%@ include file="../Stop.jsp"%>
		        func loadAirport()
		            TxNode node = ParameterSet.getParam("airportList")
		            if NULL != node
		            	AddressCapture_M_saveMultiMatchAirport(node)
						int size = TxNode.getStringSize(node)
						if 0 < size
							String strJa = TxNode.msgAt(node,0)
							if NULL != strJa
								JSONArray ja
								ja = JSONArray.fromString(strJa)
								int addressSize = JSONArray.length(ja)
								if 10 < addressSize
									addressSize = 10
								endif
								int i = 0
								JSONObject jo
								String text
								while i < addressSize
									jo = JSONArray.get(ja,i)
									text = getSingleAddressFromJO(jo)
									Page.setComponentAttribute("item"+i,"visible","1")
									Page.setComponentAttribute("item"+i,"text",text)
									i = i + 1
								endwhile
							endif
						endif
					endif
		        endfunc
		        
		        func getSingleAddressFromJO(JSONObject jo)
					String addressStr = ""
					if NULL != jo
                         addressStr = JSONObject.get(jo,"airportMsg")
					endif
					return addressStr
				endfunc
		        
		        func selectAirport()
		            TxNode indexClickNode = ParameterSet.getParam("indexClicked")
					int indexClicked = TxNode.valueAt(indexClickNode, 0)
					TxNode node = AddressCapture_M_getMultiMatchAirport()
					if NULL != node
						int size = TxNode.getStringSize(node)
						if 0 < size
							String strJa = TxNode.msgAt(node,0)
							if NULL != strJa
								JSONArray ja
								ja = JSONArray.fromString(strJa)
								int addressSize = JSONArray.length(ja)
								if indexClicked < addressSize
									JSONObject jo = JSONArray.get(ja,indexClicked)
									if NULL != jo
										AddressCapture_M_returnAirportToInvokerPage(jo)
									endif
								endif
							endif
						endif
					endif
					return FAIL
		        endfunc
			]]>
	</tml:script>

	<tml:page id="AirportListPage" url="<%=getPage + "AirportList"%>" groupId="<%=GROUP_ID_AC%>"
		type="<%=pageType%>" showLeftArrow="true" showRightArrow="true"
		helpMsg="">

		<tml:title id="title" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("ac.airport.list")%>
		</tml:title>


		<tml:listBox id="airportList">

			<%
			    for (int i = 0; i < 10; i++) {
			%>
			<tml:menuItem name="<%="item_" + i + "_clicked"%>"
				onClick="selectAirport" trigger="TRACKBALL_CLICK">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:listItem id="<%="item" + i%>" align="left|middle" visible="false"
				isFocusable="true" multLine="true" showArrow="false" imageUrl=""
				heightAutoScale="true" textWrap="ellipsis" fontWeight="bold|system_large">
				<tml:menuRef name="<%="item_" + i + "_clicked"%>"></tml:menuRef>
			</tml:listItem>
			<%
			    }
			%>

		</tml:listBox>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>