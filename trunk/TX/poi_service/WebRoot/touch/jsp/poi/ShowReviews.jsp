<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%@page import="java.util.PropertyResourceBundle"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%
    String ShowDetailURL = getPage + "ShowDetail";
    String reviews_imageSolid = imageCommonUrl + "big_solid_star.png";
    String reviews_imageUnSolid = imageCommonUrl + "big_unsolid_star.png";
    String sponsorImage = imageCommonUrl + "big_solid_star.png";

    String addressDetailListKey = Constant.StorageKeyForJSON.JSON_ARRAY_ADDRESS_DETAIL_LIST;
    String addressDetailListIndexKey = Constant.StorageKey.ADDRESS_DETAIL_LIST_INDEX;
%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/ShowReviewsModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
		      <%@ include file="ShowRateImageScriptForReviews.jsp"%>
		      func onLoad()
		         showReviews()
		      endfunc
		      
		      func onShow()
			     System.setKeyEventListener("-p-n-","onKeyPressed")
			  endfunc
			
			  func onKeyPressed(string s)
	        	 if s == "p"
	        	    showPrevious()
	        	 elsif s == "n"
	        	    showNext()
	        	 endif
			  endfunc
		      
		      func showReviews()
		         int index = ShowReviews_M_getIndex()
		         JSONArray reviewDetailArray = ShowReviews_M_getReviewsList()
		         int size = JSONArray.length(reviewDetailArray)
		         JSONObject reviewJo = JSONArray.get(reviewDetailArray,index)
		         String truncatedReview = JSONObject.get(reviewJo,"reviewText")
		         String reviewerName = JSONObject.get(reviewJo,"reviewerName")
                 String reviewTime = JSONObject.get(reviewJo,"updateDate")
                 String reviewTxt = JSONObject.get(reviewJo,"reviewText")
                 int rating = JSONObject.get(reviewJo,"rating")
                 String nameAndTime = "Submitted"
                 if NULL != reviewerName && "" != reviewerName
                    nameAndTime = nameAndTime + " by " + reviewerName
                 endif
                 
                 if NULL != reviewTime && "" != reviewTime
                    nameAndTime = nameAndTime + " on " + reviewTime
                 endif
                 Page.setComponentAttribute("briefIntroduction","text",nameAndTime)
                 Page.setComponentAttribute("particularIntroduction","text",reviewTxt)
		         index = index + 1
                 String reviewsTitle = "Reviews (" + index + " of " + size + ") "	         
		         Page.setComponentAttribute("ShowReviewsTitle","text",reviewsTitle)
		         showStarsForReviews(rating,0)
		      endfunc
		      
		      func showPrevious()
		            int index = ShowReviews_M_getIndex()
		            if index <= 0
						return FAIL
					endif
		            index = index - 1
		            ShowReviews_M_saveIndex(index)
		            showReviews()
		      endfunc
		      
		      func showNext()
		            int index = ShowReviews_M_getIndex()
		            JSONArray reviewDetailArray = ShowReviews_M_getReviewsList()
		            int size = JSONArray.length(reviewDetailArray)
		            index = index + 1
		            if index >= size
						return FAIL
					endif
		            ShowReviews_M_saveIndex(index)
		            showReviews()
		      endfunc
		]]>
	</tml:script>

	<tml:page id="ShowReviewsPage" url="<%=getPage + "ShowReviews"%>" groupId="<%=GROUP_ID_POI%>"
		type="<%=pageType%>" showLeftArrow="true" showRightArrow="true"
		helpMsg="">
		<tml:title id="ShowReviewsTitle" align="center|middle"
			fontColor="white" fontWeight="bold|system_large">
			<%=msg.get("poi.list.results")%>
		</tml:title>
		<tml:image id="reviews_starImage1_0" url="<%=reviews_imageUnSolid%>" />
		<tml:image id="reviews_starImage2_0" url="<%=reviews_imageUnSolid%>" />
		<tml:image id="reviews_starImage3_0" url="<%=reviews_imageUnSolid%>" />
		<tml:image id="reviews_starImage4_0" url="<%=reviews_imageUnSolid%>" />
		<tml:image id="reviews_starImage5_0" url="<%=reviews_imageUnSolid%>" />
		<tml:label id="briefIntroduction" align="left|bottom" textWrap="wrap"
			fontWeight="bold|system" heightAutoScale="true">
		</tml:label>
		<tml:multiline id="particularIntroduction" align="left|bottom"
			fontWeight="system" heightAutoScale="true">
		</tml:multiline>
		<cserver:outputLayout/>
	</tml:page>
</tml:TML>