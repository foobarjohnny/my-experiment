/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.poi.executor;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.WebServiceConfigurator;

/**
 * @author weiw
 */

public class BannerAdsExecutor extends AbstractExecutor {

	/** key to find browser user agent defined in device.properties */
	public static final String NATIVE_BROWSER_USER_AGENT = "NATIVE_BROWSER_USER_AGENT";

	/** web service end point in property file. */
	private static final String END_POINT = WebServiceConfigurator.getUrlOfBannerAds();

	/** logger for banner ads */
	private static Logger logger = Logger.getLogger(BannerAdsExecutor.class);

	@Override
	public final void doExecute(final ExecutorRequest req,
			final ExecutorResponse resp, final ExecutorContext executorContext)
			throws ExecutorException {

		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("bannerAds");

		BannerAdsRequest bannerRequest = (BannerAdsRequest) req;
		BannerAdsReponse bannerResp = (BannerAdsReponse) resp;

		// to set the public ip from cserver
		if (bannerRequest.getPublicIP() == null || "".equals(bannerRequest.getPublicIP())) 
		{
			String ip = executorContext.getAttribute(ExecutorContext.REQUEST_IP);
			if (ip != null) 
			{
				bannerRequest.setPublicIP(ip);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug(bannerRequest);
			logger.debug(bannerResp);
		}

		// BannerAdStub service = new BannerAdStub(END_POINT);
		// BannerAdResponse response = service.getBannerAd(request);
		// convertResp(bannerResp, response);
	}

	/**
	 * @param bannerRequest
	 * @return soapRequest
	 */
	/*
	 * private BannerAdRequest convertRequest( final BannerAdsRequest
	 * bannerRequest, final TnContext context) { BannerAdRequest soapRequest =
	 * new BannerAdRequest(); UserProfile userProfile =
	 * bannerRequest.getUserProfile(); String client = userProfile.getDevice();
	 * String version = userProfile.getVersion();
	 * 
	 * UserInformation userInfo = new UserInformation();
	 * userInfo.setPtn(userProfile.getMin());
	 * userInfo.setUserId(userProfile.getUserId());
	 * 
	 * AppInformation appInfo = new AppInformation();
	 * appInfo.setAppName(userProfile.getProduct());
	 * appInfo.setAppVersion(userProfile.getVersion());
	 * 
	 * DeviceInformation deviceInfo = new DeviceInformation(); String carrier =
	 * TnUtil .getCarrierForBannerAds(userProfile.getCarrier());
	 * deviceInfo.setCarrier(CarrierCode.Factory.fromValue(carrier));
	 * deviceInfo.setPlatform(MobilePlatform.Factory.fromValue(userProfile
	 * .getPlatform())); deviceInfo.setModel(userProfile.getDevice());
	 * 
	 * PageInformation pageInfo = new PageInformation();
	 * pageInfo.setPageId(bannerRequest.getPageId()); // use String index of
	 * Integer per backend's request
	 * pageInfo.setPageIndex(String.valueOf(bannerRequest.getPageIndex()));
	 * 
	 * SearchInformation searchInfo = new SearchInformation();
	 * searchInfo.setCategory(bannerRequest.getCategory());
	 * searchInfo.setSearchId(bannerRequest.getSearchId());
	 * searchInfo.setSearchString(bannerRequest.getKeyWord());
	 * 
	 * soapRequest.setClientName(client); soapRequest.setClientVersion(version);
	 * soapRequest.setTransactionId("transaction1"); // encrypt before sending
	 * to 3rd party soapRequest.setDeviceId(encrypt(userProfile.getUserId()));
	 * soapRequest.setUserAgent(getUserAgent(userProfile, context));
	 * 
	 * soapRequest.setPublicIp(bannerRequest.getPublicIP());
	 * soapRequest.setSearchLocation(bannerRequest.getLoc()); if
	 * (bannerRequest.getCurLoc() != null) {
	 * soapRequest.setCurrentLocation(bannerRequest.getCurLoc()); }
	 * soapRequest.setUserInfo(userInfo); soapRequest.setAppInfo(appInfo);
	 * soapRequest.setDeviceInfo(deviceInfo); soapRequest.setPageInfo(pageInfo);
	 * soapRequest.setMaxImageSize(bannerRequest.getMaxSize());
	 * soapRequest.setMinImageSize(bannerRequest.getMinSize());
	 * soapRequest.setSearchInfo(searchInfo); return soapRequest; }
	 */

	/**
	 * Return user agent read from device.properties which locates in
	 * config\device\carrier\platform\version\carrier\device
	 * 
	 * @return user agent
	 */
	/*
	 * private String getUserAgent(UserProfile profile, TnContext context) {
	 * DsmRuleHolder holder = (DsmRuleHolder) ResourceHolderManager
	 * .getResourceHolder("dsm"); String userAgent = ""; Map properties =
	 * holder.getDsmResponses(profile, context); if (properties != null &&
	 * properties.containsKey(NATIVE_BROWSER_USER_AGENT)) { userAgent = (String)
	 * properties.get(NATIVE_BROWSER_USER_AGENT); } return userAgent; }
	 */

	/**
	 * encrypt the device id before sending to third party
	 * 
	 * @param str
	 * @return
	 * @throws IllegalArgumentException
	 */
	/*
	 * private String encrypt(String str) throws IllegalArgumentException {
	 * return CipherUtil.encrypt(str); }
	 */

	/**
	 * Convert from web service response to C-Server response.
	 * 
	 * @param bannerResp
	 * @param response
	 */
	/*
	 * private void convertResp(final BannerAdsReponse bannerResp, final
	 * BannerAdResponse response) {
	 * 
	 * BannerAdType bannerAd = response.getAd(); if (bannerAd == null) { return;
	 * } bannerResp.setClickUrl(bannerAd.getClickUrl()); if (bannerAd.getMedia()
	 * != null) { String imgUrl = bannerAd.getMedia().getUrl();
	 * bannerResp.setImageUrl(imgUrl); ImageSizeType size =
	 * bannerAd.getMedia().getImageSize(); if (size != null) {
	 * bannerResp.setImageHeight(size.getHeight());
	 * bannerResp.setImageWidth(size.getWidth()); } } }
	 */

}
