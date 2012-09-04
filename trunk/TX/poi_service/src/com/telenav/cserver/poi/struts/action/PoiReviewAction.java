package com.telenav.cserver.poi.struts.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.telenav.cserver.PoiAjaxAction;
import com.telenav.cserver.backend.contents.ContentManagerServiceProxy;
import com.telenav.cserver.backend.contents.GetReviewRequest;
import com.telenav.cserver.backend.contents.GetReviewResponse;
import com.telenav.cserver.backend.datatypes.contents.ReviewServicePOIReview;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.tnbrowser.util.DataHandler;

public class PoiReviewAction extends PoiAjaxAction {

    public static String KEY_REVIEW_CHAR_LIMIT = "reviewCharLimit";

    protected ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DataHandler handler = (DataHandler) request
                .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);

        TxNode body = handler.getAJAXBody();
        String joString = body.msgAt(0);
        JSONObject jo = new JSONObject(joString);
        Long poiId = Long.parseLong(jo.getString("poiId"));
        int startIndex = 0;
        int endIndex = 9;
        TnContext tc = PoiUtil.getTnContext(handler);
        
//        ReviewDetailListRequest reviewDetailListRequest = new ReviewDetailListRequest();
        GetReviewRequest reviewDetailListRequest = new GetReviewRequest();
        reviewDetailListRequest.setOnlySummarizableAttributes(true);
//        reviewDetailListRequest.setContext(tc.toString());
        reviewDetailListRequest.setPoiId(poiId);
        reviewDetailListRequest.setStartIndex(startIndex);
        reviewDetailListRequest.setEndIndex(endIndex);
//        ContentManagerServiceProxy server = ContentManagerServiceProxy.getInstance();
//        ReviewDetailReponse reviewDetailReponse = server.getReviewTagList(reviewDetailListRequest, tc);
        ContentManagerServiceProxy contentManagerServiceProxy = ContentManagerServiceProxy.getInstance();
        GetReviewResponse reviewDetailReponse = contentManagerServiceProxy.getReviews(reviewDetailListRequest, tc);
        
//        POIReview[] review = reviewDetailReponse.getReview();
        ReviewServicePOIReview[] review = reviewDetailReponse.getReview();
        TxNode node = new TxNode();
        if(review != null && Axis2Helper.isNonEmptyArray(review)){
            JSONArray reviewDetailJo  = new JSONArray();
            for(int i=0; i<review.length;i++){
            	ReviewServicePOIReview poiReview =  review[i];
                JSONObject poiReviewJo = new JSONObject();
                
                try {
					poiReviewJo.put("rating", (int)Double.parseDouble(poiReview.getRating()));
		        } catch (NumberFormatException e) {
		        	poiReviewJo.put("rating", 0);
		        }
                
                poiReviewJo.put("poiId", poiId);
                poiReviewJo.put("reviewId", poiReview.getReviewId());
                poiReviewJo.put("reviewText", poiReview.getReviewText());
                Date updateDate = poiReview.getUpdateTime();
                SimpleDateFormat time = new SimpleDateFormat("MM/dd/yyyy");
                String dateStr = time.format(updateDate);
                poiReviewJo.put("updateDate", dateStr);
                poiReviewJo.put("reviewerName", poiReview.getReviewerName());
                reviewDetailJo.put(poiReviewJo);
            }
            jo.put("reviewDetail", reviewDetailJo);
        }
        node.addMsg(jo.toString());
        request.setAttribute("node", node);
        return mapping.findForward("success");
    }
}
