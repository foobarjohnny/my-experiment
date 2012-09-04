/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html;

import junit.framework.TestCase;

import com.telenav.cserver.html.datatypes.AdsDetail;
import com.telenav.cserver.html.datatypes.PoiDetail;
import com.telenav.cserver.html.executor.HtmlAdsRequest;
import com.telenav.cserver.html.executor.HtmlAdsResponse;
import com.telenav.cserver.html.executor.HtmlAdsServiceProxy;
import com.telenav.cserver.html.executor.HtmlPoiDetailRequest;
import com.telenav.cserver.html.executor.HtmlPoiDetailResponse;
import com.telenav.cserver.html.executor.HtmlPoiDetailServiceProxy;
import com.telenav.cserver.util.WebServiceConfigurator;
import com.telenav.datatypes.address.v30.GeoCode;
import com.telenav.datatypes.ads.v20.AdIdentifier;
import com.telenav.datatypes.ads.v20.MobileDisplayAd;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.ads.v20.AdServiceStub;
import com.telenav.services.ads.v20.BillboardAdsRequestDTO;
import com.telenav.services.ads.v20.BillboardAdsResponseDTO;

/**
 * TestAdsService
 * @author panzhang
 * @date 2011-12-22
 */
public class TestAdsService extends TestCase
{

	long adId = 0;
	TnContext tnContext;
	@Override
    protected void setUp() throws Exception
    {
		//
		tnContext = new TnContext();
		tnContext.addProperty(TnContext.PROP_POI_DATASET, "TA");
		//
		adId = getTestAdsId();
    }
	
	/**
	 * 
	 */
	public void testGetAdsBasic()
	{
		
		HtmlAdsRequest request = new HtmlAdsRequest();
		request.setAdId(adId);
		HtmlAdsResponse response = new HtmlAdsResponse();
		HtmlAdsServiceProxy.getInstance().getAdsBasic(request, response, tnContext);
		AdsDetail adsDetail = response.getAdsDetail();
		System.out.println("getAdsBasic¡¡return:" + adsDetail.toString());
	}

	/**
	 * 
	 */
	public void testGetAdsDetail()
	{
		HtmlAdsRequest request = new HtmlAdsRequest();
		request.setAdId(adId);
		request.setFromPoiDetail(true);
		HtmlAdsResponse response = new HtmlAdsResponse();
		HtmlAdsServiceProxy.getInstance().getAdsDetailData(request, response, tnContext);
		
		AdsDetail adsDetail = response.getAdsDetail();
		System.out.println("getAdsDetailData¡¡return:" + adsDetail.toString());
	}

	/**
	 * 
	 */
	public void testGetOrganicAds()
	{
		HtmlPoiDetailRequest poiRequest = new HtmlPoiDetailRequest();
		//3411883255
		//3411883249
		long l = Long.parseLong("3424545294");
		poiRequest.setPoiId(l);
		HtmlPoiDetailResponse poiResponse = new HtmlPoiDetailResponse();
		HtmlPoiDetailServiceProxy.getInstance().getOrganicAds(poiRequest, poiResponse, tnContext);
		PoiDetail poidetail = poiResponse.getPoiDetail();
		
//		System.out.println("getOrganicAds¡¡return:" + poidetail.toString());
	}

	/**
	 * simulator to get one adsId to test by AdsDetail api
	 * @return
	 */
	public long getTestAdsId()
	{
		long tttadid = 0;
		try
		{
       	 	String endPoints = WebServiceConfigurator.getUrlOfBillingBoard() ;
       	 	AdServiceStub ass = new AdServiceStub(endPoints);
       	 	BillboardAdsRequestDTO reqdto = createBillboardAdsRequest();
       	 	BillboardAdsResponseDTO resp = ass.getBillboardAds(reqdto);
       	 	MobileDisplayAd[] ads = resp.getAdvertisement();
       	 	if (ads!=null)
       	 	{
               System.out.println("we have ads");
               AdIdentifier id = ads[0].getAdIdentifier();
               if (id!=null)
               {
                   tttadid = id.getAdId();
               }
               System.out.println(tttadid);
           }

       } catch (Exception ex) {
           ex.printStackTrace();
       }
       return tttadid;
	}
	
	/**
	 * 
	 * @return
	 */
    private static BillboardAdsRequestDTO createBillboardAdsRequest()
    {
        BillboardAdsRequestDTO bbareq = new BillboardAdsRequestDTO();


        GeoCode param = new GeoCode();

        param.setLatitude(37.352245);

        param.setLongitude(-122.014565);

        bbareq.addRoutePoint(param);



        GeoCode param2 = new GeoCode();

        param2.setLatitude(37.352047);

        param2.setLongitude(-122.004917);

        bbareq.addRoutePoint(param2);



        com.telenav.datatypes.address.v30.GeoCode position = new com.telenav.datatypes.address.v30.GeoCode();

        position.setLatitude(37.352115);

        position.setLongitude(-122.014115);


        bbareq.setPosition(position);


        bbareq.setClientName("AdService JUNIT");

        bbareq.setClientVersion("1.0");

        bbareq.setTransactionId("unknown");

        //requestDTO.setPoiQuery(brandName);

        //bbareq.setStartIndexOfSponsoredPois;

        //bbareq.setNumberOfSponsoredPois(10);

        String tnString = "device=9000_Amr;c-server url=192.168.109.223:8080;needsponsor=TRUE;c-server class=5.x;requestor=tnclient;dataset=TeleAtlas;adengine=telenav,citysearch;adtype=SPONSORED_TEXT,MERCHANT_CONTENT,COUPON,MENU;version=5.5.13;carrier=Cingular;application=AN;poidataset=YPC;user_family=28855;login=4082039146;product=RIM;XNAV_TIMESTAMP=1242028942237";

        bbareq.setContextString(tnString);
        //bbareq.setContextString("");
        return bbareq;


    }
}
