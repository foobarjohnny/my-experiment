/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.poi.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.executor.BannerAdsReponse;
import com.telenav.j2me.datatypes.TxNode;

/**
 * @author weiw
 * @date Mar 25, 2010
 * 
 */

public class BannerAdsResponseFormatter extends
		BrowserProtocolResponseFormatter {

	@Override
	public final void parseBrowserResponse(
			final HttpServletRequest httpRequest,
			final ExecutorResponse executorResponse) throws JSONException {
		// FIXME: REMOVE KEY STRING
		if (executorResponse instanceof BannerAdsReponse) {
			BannerAdsReponse response = (BannerAdsReponse) executorResponse;

			TxNode node = new TxNode();
			JSONObject bannerJo = new JSONObject();
			// bannerJo.put("imageUrl", response.getImageUrl());
			// bannerJo.put("clickUrl", response.getClickUrl());
			// bannerJo.put("imageHeight", response.getImageHeight());
			// bannerJo.put("imageWidth", response.getImageWidth());
			// bannerJo.put("imageData",
			// Utility.byteArrayToBase64(response.getImgData()));

			node.addMsg(bannerJo.toString());
			httpRequest.setAttribute("node", node);
		}
	}

}
