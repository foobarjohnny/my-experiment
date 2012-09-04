<%@ include file="../Header.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.misc.struts.datatype.Address"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
	//show index for each item
%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/AddressInfoModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
			func onLoad()
				display()
			endfunc

			func display()		
				JSONArray ja= AddressInfo_M_getAddressList()
				int index = AddressInfo_M_getIndex()
				JSONObject jo = JSONArray.get(ja,index)
				showDetail(jo)
				
				# Size
				int size = JSONArray.length(ja)
				showArrow(size,index)
			endfunc
						
			func showDetail(JSONObject jo)
				string sentOn = JSONObject.getString(jo,"sentOn")
				string sentAt = JSONObject.getString(jo,"sentAt")
				string label = JSONObject.getString(jo,"label")
				string street = JSONObject.getString(jo,"street")				
				string city = JSONObject.getString(jo,"city")

				string text1 = "<%=msg.get("addressinfo.sentOn")%>" + "  " + sentOn + "  "
				text1 = text1 + "<%=msg.get("addressinfo.at")%>" + "  "  + sentAt
				Page.setComponentAttribute("sentOn","text",text1)
				Page.setComponentAttribute("label","text",label)
				Page.setComponentAttribute("street","text",street)
				Page.setComponentAttribute("city","text",city)
				
				JSONArray ja = JSONObject.get(jo,"recipients")
				int size = JSONArray.length(ja)
				int i=0
				string recipient
				string itemId
				while size>i
					itemId = "recipient" + i
					recipient = JSONArray.get(ja,i)
					Page.setComponentAttribute(itemId,"text",recipient)
					Page.setComponentAttribute(itemId,"visible","1")
					i = i+1
				endwhile
				
				# Hide the others
				while i < <%=TnConstants.ADDRESSINFO_MAX_RECIPIENTS%>
					itemId = "recipient" + i
					Page.setComponentAttribute(itemId,"visible","0")
					i = i + 1
				endwhile
			endfunc

			func showNext()
	            JSONArray ja = AddressInfo_M_getAddressList()
				int size =  JSONArray.length(ja)
				
				int index = AddressInfo_M_getIndex()
				if 0 >= size
					return FAIL
				endif
				if index >= size - 1
					return FAIL
				endif
				
				index = index + 1
				TxNode newIndexNode
				TxNode.addValue(newIndexNode, index)
				AddressInfo_M_setIndex(newIndexNode)
				display()
			endfunc

			func showPrevious()
	            JSONArray ja = AddressInfo_M_getAddressList()
				int size =  JSONArray.length(ja)
			
				int index = AddressInfo_M_getIndex()
				if 0 >= size
					return FAIL
				endif
				if 0 >= index
					return FAIL
				endif
				
				index = index - 1
				TxNode newIndexNode
				TxNode.addValue(newIndexNode, index)
				AddressInfo_M_setIndex(newIndexNode)
				
				display()
			endfunc
			
			func showArrow(int size,int index)
			    #Set previous and next
				if 0 < size
					if index < size - 1
					    Page.setShowArrow("forward","true")
					else
					    Page.setShowArrow("forward","false")
					endif
					if index > 0
					    Page.setShowArrow("back","true")
					else
					    Page.setShowArrow("back","false")
					endif
				endif
			endfunc		
			
			func returnBack()
				System.back()
				return FAIL
			endfunc			
		]]>
		</tml:script>

	<tml:menuItem name="previous" onClick="showPrevious" trigger="KEY_LEFT"/>
	<tml:menuItem name="next" onClick="showNext" trigger="KEY_RIGHT"/>
	<tml:menuItem name="back" onClick="returnBack" trigger="KEY_MENU" text="Back"/>
	<tml:page id="AddressInfo" url="<%=getPage + "AddressInfo"%>" type="<%=pageType%>"  background="<%=imageUrl + "backgroud_no_title.png"%>" groupId="<%=GROUP_ID_MISC%>">
		<tml:menuRef name="previous" />
		<tml:menuRef name="next" />
		<tml:menuRef name="back" />
		<tml:title id="title" fontWeight="bold|system_large" align="center" fontColor="white">
			<%=msg.get("addressinfo.title")%>
		</tml:title>
		<tml:panel id="menuListBox" layout="vertical">
		<tml:multiline id="label" fontWeight="bold|system_medium"  align="left"/>
		<tml:nullField id="nullField1"/>
		<tml:multiline id="street" fontWeight="system_medium" align="left"/>	
		<tml:multiline id="city" fontWeight="system_medium"  align="left"/>
		<tml:nullField id="nullField2"/>
		<tml:multiline id="sentOn" fontWeight="system_medium"  align="left"/>
		<tml:nullField id="nullField3"/>
		<tml:multiline id="nullField4" fontWeight="system_medium"  align="left" isFocusable="true"/>
		<tml:multiline id="recipients" fontWeight="system_medium"  align="left">
			<%=msg.get("addressinfo.recipients")%>
		</tml:multiline>
		<% for ( int i = 0; i < TnConstants.ADDRESSINFO_MAX_RECIPIENTS; i++ )
		   {
		%>
		<tml:multiline id="<%="recipient" + i%>" fontWeight="system_medium"  align="left"/>
		<% } %>	
		</tml:panel>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>

