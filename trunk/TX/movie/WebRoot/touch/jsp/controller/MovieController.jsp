<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ include file="../model/MovieModel.jsp"%>
<tml:script language="fscript" version="1">
		<![CDATA[
		    func SearchMovie_C_showSearch()
		    	System.doAction("showSearch")
		    endfunc
		    
		    func SearchMovie_C_initForThirdPart(String backAction)
		    	Movie_M_saveBackAction(backAction)
	            System.doAction("goToStartUp")
	            return FAIL
		    endfunc
		]]>
</tml:script>

<tml:menuItem name="showSearch" pageURL='<%= getPage + "SearchMovie"%>'>
		<tml:bean name="callFunction" valueType="String" value="showSearchScreen" />
</tml:menuItem>

<tml:menuItem name="goToStartUp" pageURL='<%= getPage + "StartUp"%>'>
</tml:menuItem>
