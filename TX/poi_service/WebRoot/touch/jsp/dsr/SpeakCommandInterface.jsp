<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../Header.jsp"%>	
<tml:TML outputMode="TxNode">
	<%@ include file="/touch/jsp/dsr/controller/DSRController.jsp"%>
	<tml:script language="fscript" version="1">
	<![CDATA[
		func onShow()
			TxNode node = ParameterSet.getParam("SayCommand")
			if node == NULL
				Handset.stopAudio()
				System.doAction("home")	
			else
				JSONObject jo
				invokeSpeakCommand(jo)			
			endif
			return FAIL
		endfunc

	]]>
	</tml:script>
	<tml:menuItem name="home" pageURL="<%=host + "/startUp.do?pageRegion=" + region%>"/>	
	<tml:page id="SayCommand" url="<%=getPage + "SayCommand"%>" type="<%=pageType%>" groupId="<%=GROUP_ID_DSR%>"> 
	</tml:page>
</tml:TML>