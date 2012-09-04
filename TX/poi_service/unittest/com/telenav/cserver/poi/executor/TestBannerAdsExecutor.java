package com.telenav.cserver.poi.executor;

import junit.framework.TestCase;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.datatypes.content.ads.v10.ImageSizeType;
import com.telenav.ws.datatypes.address.Location;

public class TestBannerAdsExecutor extends TestCase {
	private BannerAdsReponse resp = new BannerAdsReponse();
	public void testDoExecute() throws ExecutorException {
		BannerAdsExecutor excutor = new BannerAdsExecutor();
		excutor.doExecute(getBannerAdsRequest(), resp, TestUtil
				.getExecutorContext());
	}

	private BannerAdsRequest getBannerAdsRequest() {
		BannerAdsRequest request = new BannerAdsRequest();

		Location loc = TestUtil.getLocation();
		request.setLoc(loc);
		request.setCurLoc(loc);
		
		final int preferredWidth = (int) (480 * 0.95);
        int preferredHeight = (int) (800 / 4);

        ImageSizeType maxSize = new ImageSizeType();
        maxSize.setHeight(preferredHeight);
        maxSize.setWidth(preferredWidth);
		request.setMaxSize(maxSize);

        ImageSizeType minSize = new ImageSizeType();
        minSize.setWidth(480 / 2);
        minSize.setHeight((int) (800 * 0.04));
        request.setMinSize(minSize);

        request.setPublicIP("s-tn6poicsvr-01 ");
        request.setCategory(374);
        request.setKeyWord("");
        request.setSearchId("1307512047395");
        request.setPageId("POIList");
        request.setPageIndex(0);
        System.out.println(request.toString());
        
		request.setUserProfile(TestUtil.getUserProfile());
		return request;
	}
}
