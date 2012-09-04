<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../Header.jsp"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%    
	MessageWrap msgTemp = MessageHelper.getInstance(true).getMessageWrap(msgKey);
    String[] types = msgTemp.get("ac.avoid.all").split(",");
%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/EditRouteModel.jsp"%>
	<%@ include file="model/RouteStyleModel.jsp"%>

	<tml:script language="fscript" version="1">
		<![CDATA[
					func saveAvoid()
						JSONObject route = getJSONRoute()
						JSONObject style = getRouteStyle()
						JSONObject value =checkBoxToString("avoids")
						JSONObject.put(route,"avoid",value)
						JSONObject.put(route,"style",style)
					endfunc		
					
					
					func checkBoxToString(String checkBoxId)
							TxNode node
							node=ParameterSet.getParam(checkBoxId)
							String indexes=""
							String values=""
							int index=0
							int len =TxNode.getValueSize(node)
							int val=0
							int power=1
							if(node!=NULL)
								int i =0
								while(i<len)
									val = TxNode.valueAt(node,i)
									if (val==1)
										power =2
									endif
									if(val==2)
										power=4
									endif
									indexes= indexes+TxNode.valueAt(node,i)*power
									values = values+TxNode.msgAt(node,i)
									
									i=i+1
									index = index+power
									
									if(i!=len)
										values=values+", "
									endif
									
								endwhile
							endif
							JSONObject avoid
							JSONObject.put(avoid,"index",index)
							JSONObject.put(avoid,"value",values)
							
							return avoid
				endfunc

					func preLoad()
						
						JSONObject route = getJSONRoute()
						JSONObject avoid = JSONObject.get(route,"avoid")
						
						int indexes = JSONObject.get(avoid,"index")
						String indexStr = convertToBitMap(indexes,3)
						
						int i=0
						String position
						int strLen=String.getLength(indexStr)
						int index =0
						while(i<strLen)
							position = String.at(indexStr,i,1)
							if(position=="1")
								index = strLen-i-1
								Page.setControlProperty("avoids",index+"$selected","true")
							endif
							i=i+1
						endwhile
					endfunc
					
		func convertToBitMap(int value, int length)
			String bits =""
			int index=0
			int bit=0
			int res =0
			while(index<length)
				bit =value%2
				value = value/2
				bits = bit+bits
				index=index+1
			endwhile
			return bits
		endfunc
		]]>
	</tml:script>

	<tml:menuItem name="back" pageURL="<%=getPage+"EditRoute" %>"
		onClick="saveAvoid"></tml:menuItem>

	<tml:page url="<%=getPage + "AvoidRoute"%>" type="<%=pageType%>"
		showLeftArrow="true" showRightArrow="true" helpMsg="" groupId="<%=GROUP_ID_COMMOM%>">

		<tml:title fontWeight="system_large|bold" id="title" fontColor="white">
			<%=msg.get("ac.avoid.title")%>
		</tml:title>
			<tml:checkBox id="avoids"  isFocusable="true" fontWeight="system_large|bold">
				<%
					for (int i = 0; i < types.length; i++) {
				%>
				<tml:checkBoxItem value="<%=i%>" text="<%=types[i] %>">
				</tml:checkBoxItem>
				<%
					}
				%>
			</tml:checkBox>

			<tml:button fontWeight="system_large" text="<%=msg.get("common.button.Done") %>" id="button"
				imageClick=""
				imageUnclick="">
				<tml:menuRef name="back"></tml:menuRef>
			</tml:button>
			<tml:image id="titleShadow" url=""   visible="true" align="left|top"/>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>