<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../model/ShowDetailModel.jsp"%>
<tml:script language="fscript" version="1">
		<![CDATA[
		    func ShowDetail_C_showDetail()
				ShowDetail_M_saveFromFlag("PoiList")
				System.doAction("showDetail")
				return FAIL
		    endfunc
			
			func ShowDetail_C_saveForDetail(JSONArray addressList, int nomalIndex,JSONArray sponsorList,int sponsorIndex)
			    JSONArray showDetailAddressList
			    int nomalSize = 0
			    int sponsorSize = 0
			    int totalSize = 0
			    int totalIndexForDetail = 0
			    int indexInList = 0
		        if NULL != sponsorList
		           sponsorSize = JSONArray.length(sponsorList)
		        endif
		        
		        if NULL != addressList
		           nomalSize = JSONArray.length(addressList)
		           totalSize = nomalSize
		           if 0 != sponsorSize
		              totalSize = totalSize + sponsorSize
		           endif
		        endif
			    
			    if nomalIndex > -1
			       indexInList = nomalIndex
			       showDetailAddressList = addressList
			       ShowDetail_C_saveIsSponsorForDetail(0)
			       if 0 != sponsorSize
			          int sponIndex = nomalIndex/9 + 1
			          if sponsorSize < sponIndex
			             sponIndex = sponsorSize
			          endif
			          totalIndexForDetail = nomalIndex + sponIndex
			       else
			          totalIndexForDetail = nomalIndex
			       endif
			    else
			       indexInList = sponsorIndex
			       showDetailAddressList = sponsorList
			       ShowDetail_C_saveIsSponsorForDetail(1)
			       totalIndexForDetail = sponsorIndex * <%=Constant.PAGE_SIZE + 1%>
			    endif
			    
				ShowDetail_M_saveSponsorSizeForDetail(sponsorSize)
				ShowDetail_M_saveTotalIndexForDetail(totalIndexForDetail)
				ShowDetail_M_saveTotalSize(totalSize)
				ShowDetail_M_saveForDetail(showDetailAddressList,indexInList)
			endfunc
			
			func ShowDetail_C_saveIsSponsorForDetail(int isSponsor)
	            ShowDetail_M_saveIsSponsorForDetail(isSponsor)
	        endfunc
	        
	        func ShowDetail_C_saveShowTabIndex(int tabIndex)
	            ShowDetail_M_saveShowTabIndex(tabIndex)
	        endfunc
	        
	        func ShowDetail_C_getAddressList()
				return ShowDetail_M_getAddressList()
		    endfunc
		    
		    func ShowDetail_C_saveAddressList(JSONArray addressList)
		        ShowDetail_M_saveAddressList(addressList)
		    endfunc
		    
		    func ShowDetail_C_getIndex()
				return ShowDetail_M_getIndex()
		    endfunc
		    
		    func ShowDetail_C_saveIndex(int index)
		        TxNode newIndexNode
		        TxNode.addValue(newIndexNode,index)
				ShowDetail_M_saveNewIndex(newIndexNode)
		    endfunc
		]]>
</tml:script>
<tml:menuItem name="showDetail" pageURL="<%=getPage + "ShowDetail"%>">
</tml:menuItem>