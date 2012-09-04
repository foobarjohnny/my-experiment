<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>
<%
	String pageURL = getPage + "ThirdPartAddressList";
	int stopSize = 4;
%>
<tml:TML outputMode="TxNode">
	<tml:script language="fscript" version="1">
		<![CDATA[
		     func preLoad()
		        TxNode stopsNode = Cache.getFromTempCache("thirdPartyStopNodes")
		        int size = TxNode.getChildSize(stopsNode)
		        int i = 0
		        TxNode childNode
		        String addressText = ""
		        String firstLine = ""
		        String lastLine = ""
		        String addressStr = ""
		        String itemId = ""
		        String state = ""
		        String zip = ""
		        while i < size
		           childNode = TxNode.childAt(stopsNode,i)
		           addressText = TxNode.msgAt(childNode,0)
		           firstLine = TxNode.msgAt(childNode,1)
		           lastLine = TxNode.msgAt(childNode,2)
		           state = TxNode.msgAt(childNode,3)
		           zip = TxNode.msgAt(childNode,5)
		           if NULL != lastLine && "" != lastLine && NULL != state && "" != state
		              lastLine = lastLine + ", " + state
		           elsif NULL != state && "" != state
		              lastLine = state
		           endif
		           
		           if NULL != firstLine && "" != firstLine
		              addressStr = firstLine
		              if NULL != lastLine && "" != lastLine
              		     addressStr = addressStr + ", " + lastLine
              		  endif
		           elsif NULL != lastLine && "" != lastLine
		              addressStr = lastLine
		           elsif NULL != zip && "" != zip
		              addressStr = zip
		           endif
		           
		           if "" != addressText
		              if "" != addressStr
		              	 addressText = addressText + ": " + addressStr
		              endif
		           else
		              addressText = addressStr
		           endif
		           
		           itemId = "item" + i
		           Page.setComponentAttribute(itemId,"text",addressText)
		           Page.setComponentAttribute(itemId,"visible","1")
		           i = i + 1
        		endwhile
        		
        		int stopSize = <%=stopSize%>
        		while i < stopSize
					itemId = "item" + i
					Page.setComponentAttribute(itemId,"visible","0")
					i = i + 1
				endwhile
		     endfunc
		     
		     func selectAddress()
				TxNode indexClickNode = ParameterSet.getParam("indexClicked")
				int indexClicked = TxNode.valueAt(indexClickNode, 0)
				TxNode indexNode
				TxNode.addValue(indexNode,indexClicked)
				
				TxNode callFunctionNode
				TxNode.addMsg(callFunctionNode,"callbackFunc")
				MenuItem.setBean("thirdPartAction","callFunction",callFunctionNode)
			    MenuItem.setBean("thirdPartAction","indexNode",indexNode)
			    
			    TxNode callBackFromAddressNode
			    Cache.saveToTempCache("callBackFromAddressNode",callBackFromAddressNode)
			    System.doAction("thirdPartAction")
				return FAIL
	         endfunc
	         
	         func onBack()
	            System.quit()
	            return FAIL
	         endfunc
		]]>
	</tml:script>
	<tml:menuItem name="thirdPartAction" pageURL="<%=getPage+"ThirdPartAction"%>">
	</tml:menuItem>
	
	<tml:page id="page" url="<%=pageURL%>" type="<%=pageType%>"
		background=""
		groupId="<%=GROUP_ID_COMMOM%>">
		<tml:title id="title" align="center|middle"
			fontWeight="bold|system_large" fontColor="white">
			<%=msg.get("thirdpart.addresslist")%>
		</tml:title>

		<tml:listBox id="addressList">
			<%
				for (int i = 0; i < stopSize; i++) {
			%>
			<tml:menuItem name="<%="item_" + i + "_clicked"%>"
				onClick="selectAddress" trigger="TRACKBALL_CLICK">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:listItem id="<%="item" + i%>" align="left|middle"
				fontWeight="bold|system_large" isFocusable="true" multLine="true"
				showArrow="false" imageUrl="" heightAutoScale="true"
				textWrap="ellipsis">
				<tml:menuRef name="<%="item_" + i + "_clicked"%>"></tml:menuRef>
			</tml:listItem>
			<%
				}
			%>
		</tml:listBox>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>