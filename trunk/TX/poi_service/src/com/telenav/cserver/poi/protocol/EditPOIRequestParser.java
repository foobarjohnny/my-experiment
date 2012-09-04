package com.telenav.cserver.poi.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.backend.datatypes.contents.EditablePoi;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.poi.executor.EditPOIRequest;

public class EditPOIRequestParser<address> extends BrowserProtocolRequestParser {

	static Logger logger = Logger.getLogger(EditPOIRequestParser.class);
	@Override
	public String getExecutorType() {
		// TODO Auto-generated method stub
		return "editPOI";
	}

	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest object)
			throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) object;

		// TODO Auto-generated method stub
		JSONObject jo = POIRequetUtil.getJo(httpRequest);
		EditPOIRequest request = new EditPOIRequest();
		try {

			long poiId = jo.getLong("poiId");
			
			if (logger.isDebugEnabled())
	        {
	            logger.debug("poiId: " + poiId);
	        }
			
			request.poiToEdit = new EditablePoi();
			
			request.poiToEdit.setPoiId(poiId);
			
			if(jo.has("price")){
				int price = jo.getInt("price");
				request.poiToEdit.setPriceRange(price);
			}
			
			
			//if (jo.has("address"))
			    //TODO chbzhang shou convert json to stop if need address
				//request.poiToEdit.setAddress(jo.get("address"));
			
			//TODO dummy data chbzhang
//			if(jo.has("userId")){
//			    request.setUseId(jo.getLong("userId"));
//	            request.poiToEdit.setUserId(jo.getLong("userId"));
//			}
			
			if (jo.has("brand"))
				request.poiToEdit.setBrandName(jo.getString("brand"));

			if (jo.has("phone"))
				request.poiToEdit.setPhone(jo.getString("phone"));

			//if (jo.has("catName"))
			    //TODO chbzhang need zhaojian add catName in EditablePoi.java
//				request.poiToEdit.bizPoi.parentCatName = jo
//						.getString("catName");
			if (jo.has("validated")) {
				int validated = jo.getInt("validated");
				if (validated == 1)
					System.out.println("hi");
			}
			
			//Add by chbzhang for rate poi
			if(jo.has("rateIndex")){
			    int rating = jo.getInt("rateIndex");
			    request.poiToEdit.setRating(rating);
			}
			
			if(jo.has("rateReview")){
                String rateReview = jo.getString("rateReview");
                request.poiToEdit.setReview(rateReview);
            }
			
			if(jo.has("categoryId")){
				request.setCategoryId(jo.getString("categoryId"));
			}else{
				request.setCategoryId("-1");
			}
			
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return request;
	}

	public void setParam(EditPOIRequest request, JSONObject jo) {
		try {
			request.poiToEdit.setPoiId(Long.parseLong(jo.getString("poiId")));
//			Object address = jo.get("address");
			 //TODO chbzhang shou convert json to stop if need address
//			if (address != null)
//				request.poiToEdit.bizPoi.address = (Stop) address;
			String brand = jo.getString("brand");
			if (brand != null) {
				request.poiToEdit.setBrandName(brand);
			}
			String phone = jo.getString("phone");
			if (phone != null) {
				request.poiToEdit.setPhone(phone);
			}
			
			//TODO chbzhang need zhaojian add catName in EditablePoi.java if need
//			String catName = jo.getString("catName");
//			if (catName != null) {
//				request.poiToEdit.bizPoi.parentCatName = catName;
//			}

		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

}
