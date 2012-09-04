<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@ include file="Header.jsp"%>
<%@ include file="GetClientInfo.jsp"%>
<%@page import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/NameAndEmailModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
		    func preLoad()
		       TxNode firstNameNode = Preference.getPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_FIRSTNAME%>)
		       if NULL != firstNameNode
		          String firstName = TxNode.msgAt(firstNameNode,0)
			      if "" != firstName
			         Page.setComponentAttribute("firstName","text",firstName)
			      endif
		       endif
		       TxNode lastNameNode = Preference.getPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_LASTNAME%>)
		       if NULL != lastNameNode
		          String lastName = TxNode.msgAt(lastNameNode,0)
			      if "" != lastName
			         Page.setComponentAttribute("lastName","text",lastName)
			      endif
		       endif
		       TxNode mailNode = Preference.getPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_EMAIL%>)
		       if NULL != mailNode
		          String mail = TxNode.msgAt(mailNode,0)
			      if "" != mail
			         Page.setComponentAttribute("email","text",mail)
			      endif
		       endif
		       
		       Page.setComponentAttribute("firstName","hint","<%=msg.get("common.required")%>")
		       Page.setComponentAttribute("lastName","hint","<%=msg.get("common.required")%>")
			   String source = NameAndEmail_M_getSource()
			   if "<%=Constant.commuteAlert%>" == source
			   	   Page.setComponentAttribute("firstName","visible","0")
			   	   Page.setComponentAttribute("lastName","visible","0")
   			   	   Page.setComponentAttribute("alertEmailLabel","visible","1")
			       Page.setComponentAttribute("email","hint","<%=msg.get("nameAndEmail.email.required")%>") 
			   else
		   		   Page.setComponentAttribute("firstName","visible","1")
			   	   Page.setComponentAttribute("lastName","visible","1")
   			   	   Page.setComponentAttribute("alertEmailLabel","visible","0")
			       Page.setComponentAttribute("email","hint","<%=msg.get("common.optional")%>") 
			   endif
		    endfunc
		
		    func submitClick()
		       String source = NameAndEmail_M_getSource()
		       int nullFlag = checkAddress(source)
		       if 2 != nullFlag
		          if "<%=Constant.commuteAlert%>" == source
		              String alertMsg = "<%=msg.get("nameAndEmail.email")%>"
		              #if 0 == nullFlag
		              #   alertMsg = "<%=msg.get("nameAndEmail.name.commuteAlert")%>"
		              #endif
		              
		              if 3 == nullFlag
		                 alertMsg = "<%=msg.get("nameAndEmail.correct.email")%>"
		              endif
		              System.showErrorMsg(alertMsg)
			          return FAIL
			      elsif 0 == nullFlag
			          System.showErrorMsg("<%=msg.get("nameAndEmail.name.referFriend")%>")
			          return FAIL
			      elsif 3 == nullFlag
			          System.showErrorMsg("<%=msg.get("nameAndEmail.correct.email")%>")
			          return FAIL
			      endif
		       endif
		       TxNode firstNameNode = ParameterSet.getParam("firstName")
		       String firstName = TxNode.msgAt(firstNameNode,0)
		       
		       TxNode lastNameNode = ParameterSet.getParam("lastName")
		       String lastName = TxNode.msgAt(lastNameNode,0)
		       
		       JSONObject jo
		       JSONObject.put(jo,"firstName",firstName)
		       JSONObject.put(jo,"lastName",lastName)
		       
		       TxNode emailNode = ParameterSet.getParam("email")
		       String email = ""
		       if NULL != emailNode
		          email = TxNode.msgAt(emailNode,0)
		       endif
		       
		       JSONObject.put(jo,"email",email)
               String strJo=JSONObject.toString(jo)
               TxNode node
               TxNode.addMsg(node,strJo)
			   TxRequest req
			   String url="<%=host + "/updateUserInfo.do"%>"
			   String scriptName="updateUserCallback"
			   TxRequest.open(req,url)
			   TxRequest.setRequestData(req,node)
			   TxRequest.onStateChange(req,scriptName)
			   TxRequest.setProgressTitle(req,"<%=msg.get("common.updating")%>")
			   TxRequest.send(req)
		    endfunc
		    
		    func checkAddress(String source)
               if "<%=Constant.commuteAlert%>" != source
			       TxNode firstNameNode = ParameterSet.getParam("firstName")
			       if NULL == firstNameNode
			          return 0
			       endif
			       String firstName = TxNode.msgAt(firstNameNode,0)
			       if "" == firstName
			          return 0
			       endif
			       
			       TxNode lastNameNode = ParameterSet.getParam("lastName")
			       if NULL == lastNameNode
			          return 0
			       endif
			       String lastName = TxNode.msgAt(lastNameNode,0)
			       if "" == lastName
			          return 0
			       endif
		       endif
		       
		       TxNode emailNode = ParameterSet.getParam("email")
		       if NULL == emailNode
		          return 1
		       endif
		       String email = TxNode.msgAt(emailNode,0)
		       if "" == email
		          return 1
		       endif
		       
		       if(!isValidEmail(email))
		       	  return 3
		       endif
		       return 2
		    endfunc
		    
		    func updateUserCallback(TxNode node,int status)
		       if status == 0
	               System.showErrorMsg("<%=msg.get("common.internal.error")%>")
				   return FAIL
			   elsif status == 1
			       String firstName = TxNode.msgAt(node,0)
			       String lastName = TxNode.msgAt(node,1)
			       String email = TxNode.msgAt(node,2)
			       Preference.setPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_FIRSTNAME%>,firstName)
			       Preference.setPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_LASTNAME%>,lastName)
			       Preference.setPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_EMAIL%>,email)
			       Preference.savePreference()
			       NameAndEmail_M_goBack()
			   endif
		    endfunc
		    
			func isValidEmail(String email)
				if isNull(email) || String.getLength(email) == 0
					return TRUE
				endif
				
				int atIndex = String.find(email, 0, "@")
				int emailLength = String.getLength(email)
				if atIndex <= 0 || atIndex == emailLength - 1
					return FALSE
				endif
				int dotIndex = lastIndexOf(email, ".")
			    if dotIndex <= 0 || dotIndex <= atIndex || dotIndex == atIndex + 1 || dotIndex == emailLength - 1
					return FALSE
				endif
				
				String str1 = String.At(email, 0, atIndex)
				String str2 = String.At(email, atIndex+1, emailLength-atIndex-1)
				if str2 == ""
					return FALSE
				endif
				
				if !isValidPartOfEmail(str1)
					return FALSE
				endif
				
				if !isValidPartOfEmail(str2)
					return FALSE
				endif
				
				return TRUE
			endfunc
			
			func lastIndexOf(String str, String fstr)
				int idx = String.find(str, 0, fstr)
				int i = idx
				int len = String.getLength(str)
				while i <= len
					i = String.find(str, idx+1, fstr)
					if i > idx && i <= len
						idx = i
					else
						i = len + 1			
					endif
				endwhile
				return idx
			endfunc
						    
			func isValidPartOfEmail(String part)
				if isNull(part)
					return FALSE
				endif
				int i = 0
				int len = String.getLength(part)
				String c = ""
				while i < len
					c = String.At(part, i, 1)
					if !isValidEmailChar(c)
						return FALSE
					endif
					if i == 0 || i == len-1		
						if c == "."
							return FALSE
						endif
					endif
					i = i+1
				endwhile
				return TRUE
			endfunc
			
			func isValidEmailChar(String char)
				if isNull(char) || String.getLength(char) > 1
					return FALSE
				endif
				String allValidEmailChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#$%&'*+-./=?^_{|}~"
				int i = 0
				int len = String.getLength(allValidEmailChars)
				while i < len
					if char == String.At(allValidEmailChars, i, 1)
						return TRUE
					endif
					i = i+1
				endwhile
				return FALSE
			endfunc
		]]>
	</tml:script>

	<tml:menuItem name="doSubmit" text="Submit" onClick="submitClick">
	</tml:menuItem>

	<tml:page id="NameAndEmailPage" url="<%=getPageWithLocale + "NameAndEmail"%>" 
		type="<%=pageType%>" showLeftArrow="true" showRightArrow="true"
		helpMsg="" groupId="<%=GROUP_ID_COMMOM%>">
		<tml:title id="NameAndEmailTitle" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("common.register")%>
		</tml:title>

		<tml:inputBox id="firstName" fontWeight="system_large"
			title="<%=msg.get("common.first.name.title")%>" titleFontWeight="system_large|bold">
			<tml:menuRef name="doSubmit" />
		</tml:inputBox>

		<tml:inputBox id="lastName" fontWeight="system_large"
			title="<%=msg.get("common.last.name.title")%>" titleFontWeight="system_large|bold">
			<tml:menuRef name="doSubmit" />
		</tml:inputBox>
		<tml:multiline id="alertEmailLabel" fontWeight="system_large"> <%=TnUtil.amend(msg.get("att.commute.alert"))%></tml:multiline>
		<tml:inputBox id="email" fontWeight="system_large" title="<%=msg.get("common.email.title")%>" titleFontWeight="system_large|bold">
		    <tml:menuRef name="doSubmit" />
		</tml:inputBox>

		<tml:button id="doneButton" text="<%=msg.get("common.button.Submit")%>" fontWeight="system_large"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="doSubmit" />
		</tml:button>
		<tml:image id="titleShadow" url=""   visible="true" align="left|top"/>
	</tml:page>
	<cserver:outputLayout  supportLocal="Y"/>
</tml:TML>
