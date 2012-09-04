<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@include file="/touch/jsp/ac/model/SetUpHomeModel.jsp"%>
<tml:script language="fscript" version="1">
	<![CDATA[
        func SetUpHome_C_show(JSONObject jo)
        	SetUpHome_M_setParameter(jo)
            System.doAction("homeMain")
        endfunc
        
		func SetUpHome_C_showHowAddress()
			SetUpHome_M_saveAddressListCallBack("<%=getPageCallBack + "HomeMain"%>","CallBack_SelectAddress")
        	SetUpHome_M_fromHome(1)
            System.doAction("homeAddress")
        endfunc
        
        func SetUpHome_C_getHome()
	        return SetUpHome_M_getHome()
	    endfunc
	]]>
</tml:script>
<tml:menuItem name="homeMain" pageURL="<%=getPage + "HomeMain#Home"%>"/>
<tml:menuItem name="homeAddress" pageURL="<%=getPage + "HomeAddress#Home"%>" trigger="KEY_RIGHT | TRACKBALL_CLICK"/>
