package com.telenav.cserver.ac.executor;

import java.util.ArrayList;
import java.util.List;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.datatypes.addresssharing.ContactInfo;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.TnUtil;
import com.telenav.cserver.weather.executor.TestUtil;
import com.telenav.j2me.datatypes.Stop;

public class ShareAddressExecutorTest {

	private ShareAddressExecutor instance = new ShareAddressExecutor();
	ShareAddressRequest req = new ShareAddressRequest();
	ShareAddressResponse resp = new ShareAddressResponse();
	
	@Before
	public void setUp(){
		try{
			req = getShareAddressRequest();
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSuite(){
		try {
			instance.doExecute(req, resp, getExecutorContext());
			Assert.assertEquals(ExecutorResponse.STATUS_OK, resp.getStatus());
		} catch (ExecutorException e) {
			// TODO Auto-generated catch block
			Assert.fail();
			e.printStackTrace();
		}
	}
	
	
	public ShareAddressRequest getShareAddressRequest() throws JSONException{
		
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

		request.setUserProfile(getUserProfile());
		return request;
	}
	
	public UserProfile getUserProfile() {
		return TestUtil.getUserProfile64();
	}
	
	public ExecutorContext getExecutorContext(){
		return TestUtil.getExecutorContext();
	}

}
