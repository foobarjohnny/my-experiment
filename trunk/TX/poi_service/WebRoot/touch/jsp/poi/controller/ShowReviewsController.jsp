<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<%@ include file="../model/ShowReviewsModel.jsp"%>
<tml:script language="fscript" version="1" feature="<%=FeatureConstant.UGC_VIEW%>">
		<![CDATA[
		      func ShowReviews_C_saveReviewsList(TxNode node)
				  ShowReviews_M_saveReviewsList(node)
              endfunc 
              
              func ShowReviews_C_saveIndex(int index)
				  ShowReviews_M_saveIndex(index)
              endfunc 
		]]>
</tml:script>