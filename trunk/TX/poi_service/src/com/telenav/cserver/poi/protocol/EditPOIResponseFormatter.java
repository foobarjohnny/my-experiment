package com.telenav.cserver.poi.protocol;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.telenav.cserver.backend.contents.GetReviewResponse;
import com.telenav.cserver.backend.datatypes.contents.ReviewServicePOIReview;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.datatypes.POI;
import com.telenav.cserver.poi.executor.EditPOIReponse;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.ws.axis2.Axis2Helper;

public class EditPOIResponseFormatter extends BrowserProtocolResponseFormatter {

	@Override
	public void parseBrowserResponse(HttpServletRequest httpRequest,
			ExecutorResponse executorResponse) throws Exception {
		TxNode node = new TxNode();
		EditPOIReponse response = (EditPOIReponse) executorResponse;
		POI poi = response.getPoi();
		if(poi != null){
			JSONObject bpjo = new JSONObject();
			double priceRange = poi.priceRange;
			if (poi.priceRange == -1) {
				priceRange = 0;
			}
			bpjo.put("priceRange", Integer
					.parseInt(String.format("%.0f", priceRange)));
			bpjo.put("ratingNumber", poi.ratingNumber);
			bpjo.put("rating", poi.avgRating);
			GetReviewResponse getReviewResponse = poi.getReviewResponse;
			JSONArray reviewDetailJo = new JSONArray();
			if (getReviewResponse != null) {
				ReviewServicePOIReview[] poiReviews = getReviewResponse.getReview();
				if (poiReviews != null  && Axis2Helper.isNonEmptyArray(poiReviews)) {
					int size = poiReviews.length;
					for (int i = 0; i < size; i++) {
						ReviewServicePOIReview poiReview = poiReviews[i];
						JSONObject poiReviewJo = new JSONObject();
						
						try {
							poiReviewJo.put("rating", (int)Double.parseDouble(poiReview.getRating()));
				        } catch (NumberFormatException e) {
				        	poiReviewJo.put("rating", 0);
				        }
						
						poiReviewJo.put("poiId", poiReview.getPoiId());
						poiReviewJo.put("reviewId", poiReview.getReviewId());
						poiReviewJo
								.put("reviewerName", poiReview.getReviewerName());
						poiReviewJo.put("reviewText", poiReview.getReviewText());
						Date updateDate = poiReview.getUpdateTime();
						SimpleDateFormat time = new SimpleDateFormat("MM/dd/yyyy");
						String dateStr = time.format(updateDate);
						poiReviewJo.put("updateDate", dateStr);
						reviewDetailJo.put(poiReviewJo);
					}
				}
			}
			bpjo.put("reviewDetail", reviewDetailJo);
			
			node.addMsg(bpjo.toString());
		}
		httpRequest.setAttribute("node", node);
	}

}
