<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="Header.jsp"%>
<tml:TML outputMode="TxNode">
	<tml:script language="fscript" version="1">
		<%@include file="model/OneBoxModel.jsp"%>
		<![CDATA[
			func onLoad()
				String url_flag = OneBox_M_getPageFrom()
				String url = ""
				#if "NA" == "<%=region%>"
				#	url = "<%=getPageCallBack + "OneBoxSearch"%>"
				#else
					url = "<%=getPageCallBack + "InternationalSearch"%>"
				#endif
								
				if "" == url_flag
					url = url + "#Common"
				else
					url = url + "#" + url_flag
				endif
				
				MenuItem.setAttribute("goToOneBoxSearch","url",url)
				System.doAction("goToOneBoxSearch")
			endfunc
		]]>
	</tml:script>

	<tml:menuItem name="goToOneBoxSearch" pageURL="" />
	<tml:page id="GoToOneBoxSearch" url="<%=getPage + "GoToOneBoxSearch"%>" type="<%=pageType%>" helpMsg="$//$search"
		 groupId="<%=GROUP_ID_COMMOM%>" supportback="false">
	</tml:page>
</tml:TML>
