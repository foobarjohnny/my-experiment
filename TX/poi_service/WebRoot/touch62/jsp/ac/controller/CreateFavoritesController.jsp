<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@ include file="/touch62/jsp/ac/model/CreateFavoritesModel.jsp"%>
<tml:script language="fscript" version="1">
	<![CDATA[
        func CreateFavorites_C_onShow(JSONObject locationJo,JSONObject jo,JSONObject poiJo)
		    JSONObject.put(locationJo,"type",2)
		    TxNode addressNode
		    TxNode.addMsg(addressNode,JSONObject.toString(locationJo))
		    if poiJo!= NULL
		    	TxNode.addMsg(addressNode,JSONObject.toString(poiJo))
		    endif		    
		    
		    TxNode node
			TxNode.addMsg(node,"CallBack_SelectAddress")
		    MenuItem.setBean("goToFavorites","callFunction",node)
			MenuItem.setBean("goToFavorites","returnAddress",addressNode)
			
			string from = JSONObject.getString(jo,"from")
			string flag = JSONObject.getString(jo,"flag")
			if flag == NULL || flag == ""
				flag = "create"
			endif
			CreateFavorites_M_saveAddressForCreateOrEdit(flag)
			CreateFavorites_M_saveCreateFavoriteReturnUrl(JSONObject.getString(jo,"callbackpageurl"))
			CreateFavorites_M_setDoingCreateFavorites(0)
			MenuItem.setAttribute("goToFavorites","url","<%=getPageCallBack%>" + "CreateFavorites#" + from)
		    System.doAction("goToFavorites")
		    return FAIL
        endfunc

        func CreateFavorites_C_onEdit(TxNode addressNode,JSONObject jo)
        	TxNode node
	        TxNode.addChild(node,addressNode)
	        TxNode.addMsg(node,"edit")
        	
		    TxNode callFuncNode
			TxNode.addMsg(callFuncNode,"goToEditFavorite")
		    MenuItem.setBean("goToFavorites","callFunction",callFuncNode)
			MenuItem.setBean("goToFavorites","returnAddress",node)
			
			string from = JSONObject.getString(jo,"from")
			CreateFavorites_M_saveAddressForCreateOrEdit("edit")
			CreateFavorites_M_saveCreateFavoriteReturnUrl(JSONObject.getString(jo,"callbackpageurl"))
			CreateFavorites_M_setDoingCreateFavorites(0)
			MenuItem.setAttribute("goToFavorites","url","<%=getPageCallBack%>" + "CreateFavorites#" + from)
		    System.doAction("goToFavorites")
		    return FAIL
        endfunc
                
        func CreateFavorites_C_init(JSONObject jo)
			string from = JSONObject.getString(jo,"from")
			string flag = JSONObject.getString(jo,"flag")
			if flag == NULL || flag == ""
				flag = "create"
			endif
			CreateFavorites_M_saveAddressForCreateOrEdit(flag)
			CreateFavorites_M_saveCreateFavoriteReturnUrl(JSONObject.getString(jo,"callbackpageurl"))
			CreateFavorites_M_setDoingCreateFavorites(0)        
        endfunc
		]]>
</tml:script>

<tml:menuItem name="goToFavorites" pageURL="">
</tml:menuItem>