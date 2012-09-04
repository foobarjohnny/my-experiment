<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../model/PoiSubCategoryModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
	    func PoiSubCategory_C_show(String categoryId)
	    	PoiSubCategory_M_setCategoryId(categoryId)
            System.doAction("showPoiSubCategory")
            return FAIL
	    endfunc
		]]>
	</tml:script>
	<tml:menuItem name="showPoiSubCategory" pageURL="<%=host + "/POICategory.do"%>">
	</tml:menuItem>
