/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import java.util.Calendar;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.cserver.util.WebServiceConfigurator;
import com.telenav.datatypes.restaurant.v10.AvailOffer;
import com.telenav.datatypes.restaurant.v10.AvailRequest;
import com.telenav.datatypes.restaurant.v10.AvailResponse;
import com.telenav.datatypes.restaurant.v10.GetPoiDetailRequest;
import com.telenav.datatypes.restaurant.v10.GetPoiDetailResponse;
import com.telenav.datatypes.restaurant.v10.Restaurant;
import com.telenav.datatypes.restaurant.v10.RestaurantAvailOffer;
import com.telenav.datatypes.restaurant.v10.TableAvailCriterion;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.restaurant.v10.RestaurantServices;
import com.telenav.services.restaurant.v10.RestaurantServicesStub;
/**
 * @TODO	Implement specific business logic
 * @author	
 * @version 1.0	Feb 21, 2011
 */
public class HtmlRestaurantProxy {
	/**
	 * TODO	get web-service
	 * @return
	 * @throws Exception
	 */
	public static RestaurantServices getService() throws Exception {
		RestaurantServices stub = new RestaurantServicesStub(
				WebServiceConfigurator.getUrlOfRestaurant());
		return stub;
	}
	/**
	 * TODO	get open table detail infomation
	 * @param rRequest
	 * @param rResponse
	 */
	public static void getTableDetail(GetTableDetailRequest rRequest,
			GetTableDetailResponse rResponse) {
		int partySize = rRequest.getPartySize();
		String date = rRequest.getSearchDate();
		String time = rRequest.getSearchTime();

		long partnerPoiId = rRequest.getPartnerPoiId();

		try {

			try {

				RestaurantServices stub = HtmlRestaurantProxy.getService();
				AvailRequest request = new AvailRequest();
				request.setClientName("LocalApp-Restaurant");
				request.setClientVersion("1.0");
				Calendar cal = Calendar.getInstance();
				request.setTransactionId("" + cal.getTimeInMillis());
				request.setPartnerCode("OPENTABLE_RESTAURANT"); // TODO

				// To Do remove this
				long[] partnerPoiIds = { -1 };
				partnerPoiIds[0] = partnerPoiId;
				request.setPartnerPoiIds(partnerPoiIds);
				request.setDataSet("YPC");
				request.setWithPoiDetails(true);

				// Set date time and partysize
				TableAvailCriterion tableAvailCriterion = new TableAvailCriterion();
				tableAvailCriterion.setPartySize(partySize);

				cal.setTime(HtmlConstants.DATE_TIME_FORMAT.parse(date + " "
						+ time));
				tableAvailCriterion.setDateTime(cal);
				request.setAvailCriterion(tableAvailCriterion);

				request.setContextString(new TnContext("poidataset=YPC")
						.toContextString());
				AvailResponse response = stub.searchAvail(request);
				rResponse.setDateTime(date + " " + time);
				rResponse.setDate(date);
				rResponse.setPartySize(partySize);
				AvailOffer[] availOffers = response.getAvailOffers();

				if (availOffers != null) {
					RestaurantAvailOffer rAvailOffer = (RestaurantAvailOffer) availOffers[0];
					rResponse.setAvailOffer(rAvailOffer);
				} else {
					
				}

			} catch (Exception e) {

				throw e;
			}

			rResponse.setStatus(ExecutorResponse.STATUS_OK);

		} catch (Exception ex) {
			rResponse.setErrorMessage("Exeception : " + ex.getMessage());
			rResponse.setStatus(ExecutorResponse.STATUS_FAIL);
		}
	}
	/**
	 * TODO get restaurant detail infomation
	 * @param rRequest
	 * @param rResponse
	 */
	public static void getRestaurantDetail(GetRestaurantDetailRequest rRequest,
			GetRestaurantDetailResponse rResponse) {
		try {
			long partnerPoiId = rRequest.getPartnerPoiId();

			try {
				// System.out.println("Getting Details");

				RestaurantServices stub = HtmlRestaurantProxy.getService();

				GetPoiDetailRequest gpdRequest = new GetPoiDetailRequest();
				gpdRequest.setClientName("LocalApp-Restaurant");
				gpdRequest.setClientVersion("1.0");
				Calendar cal = Calendar.getInstance();
				gpdRequest.setTransactionId("" + cal.getTimeInMillis());
				gpdRequest.setPartnerCode("OPENTABLE_RESTAURANT"); // TODO
				gpdRequest.setPartnerPoiId(partnerPoiId); // TODO
				gpdRequest.setDataSet("YPC");
				gpdRequest.setContextString(new TnContext("poidataset=YPC")
						.toContextString());

				GetPoiDetailResponse gpdResponse = stub
						.getPoiDetail(gpdRequest);

				Restaurant restaurant = (Restaurant) gpdResponse.getPoiDetail();
				System.out.println("restaurant:" + restaurant);

				if (restaurant != null) {
					rResponse.setRestaurant(restaurant);
				} else {

				}

			} catch (Exception e) {

				throw e;
			}

			rResponse.setStatus(ExecutorResponse.STATUS_OK);

		} catch (Exception ex) {
			rResponse.setErrorMessage("Exeception : " + ex.getMessage());
			rResponse.setStatus(ExecutorResponse.STATUS_FAIL);
		}
	}

}
