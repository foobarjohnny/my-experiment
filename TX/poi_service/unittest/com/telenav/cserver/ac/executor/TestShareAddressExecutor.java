package com.telenav.cserver.ac.executor;

import java.util.ArrayList;
import java.util.List;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.ac.executor.ShareAddressExecutor;
import com.telenav.cserver.ac.executor.ShareAddressRequest;
import com.telenav.cserver.ac.executor.ShareAddressResponse;
import com.telenav.cserver.backend.datatypes.addresssharing.ContactInfo;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.util.TnUtil;
import com.telenav.j2me.datatypes.Stop;

import junit.framework.TestCase;

public class TestShareAddressExecutor extends TestCase {
	private ShareAddressResponse resp = new ShareAddressResponse();

	public void testDoExecute() throws ExecutorException, JSONException {
		ShareAddressExecutor excutor = new ShareAddressExecutor();
		excutor.doExecute(getShareAddressRequest(), resp, TestUtil
				.getExecutorContext());
	}

	private ShareAddressRequest getShareAddressRequest() throws JSONException {
		ShareAddressRequest request = new ShareAddressRequest();

		request.setUserId(9826225);
		request.setSenderPTN("4085057537");

		List<ContactInfo> contacts = new ArrayList<ContactInfo>();
		ContactInfo contact = new ContactInfo();
		contact.setName("TeleNav");
		contact.setPtn("4085057537");

		contacts.add(contact);
		request.setContactList(contacts);

		List<String> groupList = new ArrayList<String>();
		request.setGroupList(groupList);

		JSONObject searchLocationJson = new JSONObject();
		searchLocationJson.put("label", "");
		searchLocationJson.put("firstLine", "3755 EL CAMINO REAL");
		searchLocationJson.put("zip", "95051");
		searchLocationJson.put("state", "CA");
		searchLocationJson.put("country", "US");
		searchLocationJson.put("city", "SANTA CLARA");
		searchLocationJson.put("lat", 3735237);
		searchLocationJson.put("lon", -12199984);
		Stop address = TnUtil.convertToStop(searchLocationJson);
		request.setAddress(address);
		request.setLabel("");

		request.setUserProfile(TestUtil.getUserProfile());
		
		System.out.println(request.toString());

		return request;
	}
}
