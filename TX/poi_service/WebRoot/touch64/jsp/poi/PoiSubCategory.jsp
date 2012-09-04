<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%@ page import="com.telenav.cserver.poi.struts.Constant"%>
<%@ page import="com.telenav.j2me.datatypes.TxNode"%>
<%@ page import="com.telenav.tnbrowser.util.Utility"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
    String pageUrl =host + "/POICategory.do";
    TxNode listNode = (TxNode) request.getAttribute("node");
    final int maxSubCategoryCount = 20;
%>
<tml:TML outputMode="TxNode">
	<%@ include file="controller/SearchPoiController.jsp"%>
	<%@ include file="model/PoiSubCategoryModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
			func onClickCategory()
				TxNode node = ParameterSet.getParam("indexClicked")
				int indexClicked = TxNode.valueAt(node,0)
				
				JSONArray ja = PoiSubCategory_M_getSubCategory()
				JSONObject jo = JSONArray.get(ja,indexClicked)
				
				String subCategoryName = System.parseI18n("$(" + JSONObject.get(jo,"name") + ")")
				String name = SearchPoi_M_getSubCategoryName() + " -> " + subCategoryName
				String isMostPopular = JSONObject.get(jo,"mostPopular")
				String id = JSONObject.get(jo,"id")
				TxNode nodeId
				TxNode.addMsg(nodeId,id) 
				
				SearchPoi_M_saveCategory(name,isMostPopular,nodeId)
				SearchPoi_M_saveFlowFlag("1")
			    SearchPoi_C_showSearch()
			    return FAIL
			endfunc
			
			func preLoad()
				TxNode subCategory = ParameterSet.getParam("subCategory")
				JSONObject joAll = PoiSubCategory_M_getAllSubCategory()
				if joAll == NULL
					joAll = JSONObject.fromString(TxNode.msgAt(subCategory,0))
					PoiSubCategory_M_setAllSubCategory(joAll)
				endif
				
				JSONArray ja = JSONObject.get(joAll,PoiSubCategory_M_getCategoryId())
				
				JSONObject jo
				if ja != NULL
					int count,i
					string itemId,itemText,id,mostPopular,name
					count = JSONArray.length(ja)
					while count>i
						jo = JSONArray.get(ja,i)
						itemId = "item" + i
						id = JSONObject.get(jo,"id")
						mostPopular = JSONObject.get(jo,"mostPopular")
						name = JSONObject.get(jo,"name")
						name = "$(" + name + ")"
						itemText = System.parseI18n(name)
						Page.setComponentAttribute(itemId,"text",itemText)
						Page.setComponentAttribute(itemId,"visible","1")
						i = i+1
					endwhile

					# Hide the others
					while i < <%=maxSubCategoryCount%>
						itemId = "item" + i
						Page.setComponentAttribute(itemId,"visible","0")
						i = i + 1
					endwhile					
				endif
			endfunc
			]]>
	</tml:script>
	
	<tml:page id="poiSubCategoryPage" url="<%=pageUrl%>" type="<%=pageType%>" showLeftArrow="true"
		showRightArrow="true" helpMsg="$//$searchcategory" groupId="<%=GROUP_ID_POI%>" supportback="false">
		<tml:bean name="subCategory" valueType="TxNode"
			value="<%=Utility.TxNode2Base64(listNode)%>"></tml:bean>
		<tml:title id="title" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("startup.Search")%>
		</tml:title>
		<tml:listBox id="menuListBox" name="menuListBox" isFocusable="true" hotKeyEnable="false">
		<%
			for(int i=0;i< maxSubCategoryCount ;i++)
			{
		%>
			<tml:menuItem name="<%="choseCategoryMenu" + i%>" onClick="onClickCategory">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>	
			<tml:listItem id="<%="item" + i%>" fontWeight="system_large" showArrow="true">
				<tml:menuRef name="<%="choseCategoryMenu" + i%>" />
			</tml:listItem>
		<%
			}
		%>				
		</tml:listBox>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>
