<%@ page language="java" pageEncoding="UTF-8"%>

<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
    String searchPoiURL = getPage + "SearchPoi";
    String pageUrl = getPage + "SearchCategory";
    String categoryData = "CATEGORY_TREE";
%>
<%@page import="java.util.Vector"%>
<%@page import="javax.servlet.*"%>

<tml:TML outputMode="TxNode">
	<%@ include file="controller/SearchPoiController.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
		        func treeClick()
					TxNode categoryNode = ShareData.get("<%=categoryData%>")
					if NULL == categoryNode
					   return FAIL
					endif
					ShareData.delete("<%=categoryData%>")
					
					String name=TxNode.msgAt(categoryNode,0)
					String fatherName = TxNode.msgAt(categoryNode,1)
					if "" != fatherName
					   name = fatherName + " > " + name
					endif
					
					TxNode node 
					String id = TxNode.msgAt(categoryNode,2)
					TxNode.addMsg(node,id)
					
					String isMostPopular = TxNode.msgAt(categoryNode,3)
					if "" == isMostPopular
					   isMostPopular =  "0"
					endif
				    SearchPoi_C_saveCategory(name,isMostPopular,node)
				    SearchPoi_C_showSearch()
				    return FAIL
		        endfunc
			]]>
	</tml:script>
	
	<tml:menuItem name="treeClick" onClick="treeClick" trigger="TRACKBALL_CLICK">
	</tml:menuItem>

	<tml:menuItem name="chooseCatAll" onClick="setCat"
		trigger="KEY_RIGHT | TRACKBALL_CLICK">
		<tml:bean name="id" valueType="String" value="-1" />
		<tml:bean name="name" valueType="String" value="<%=msg.get("poi.category.allCategories")%>" />
	</tml:menuItem>
	<tml:page id="searchCategoryPage" url="<%=pageUrl%>" 
		type="<%=pageType%>" showLeftArrow="true"
		showRightArrow="true" helpMsg="$//$searchcategory" groupId="<%=GROUP_ID_POI%>">
		<tml:title id="CategoryTitle" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("poi.category")%>
		</tml:title>
		<tml:tree id="categoryTree" fontWeight="system_large">
		    <tml:menuRef name="treeClick" />
		</tml:tree>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>
