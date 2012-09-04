package com.telenav.cserver.backend.recentstop;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.datatypes.recentstop.RecentAddress;
import com.telenav.cserver.backend.datatypes.recentstop.RecentStop;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

public class TestRecentStopsServiceProxy 
{
	RecentStopsServiceProxy proxy;
	
	TnContext tc;
	
	int userId = 3707312;
	
	RecentStop stop;
	
	long recentStopId;
	
	@Before
	public void init()
	{
		proxy = RecentStopsServiceProxy.getInstance();
		tc = new TnContext();
		
		stop = new RecentStop();
		
		RecentAddress address = new RecentAddress();
		address.setLatitude(37.37895);
		address.setLongitude(-122.00776);
		address.setFirstLine("239 Commercial St");
		address.setCity("SunnyVale");
		address.setCountry("US");
		address.setPostalCode("94085");
		
		stop.setRecentAddress(address);
		stop.setPoi(false);
		stop.setOwnerUserId(3707312);
		stop.setRecentStopId(100604151);
	}
	
	@Test
	public void getRecentStop()
	{
		QueryRequest req = new QueryRequest();
		req.setUserId(userId);
		req.setContext(tc.toContextString());
		req.setMaxResult(10);
		
		System.out.println("QueryRequest >>>>>>>>>>> "+ req.toString());
		
		QueryResponse resp = null;
		try {
			resp = proxy.getRecentStops(req, tc);
		} catch (ThrottlingException e) {
			// TODO: handle exception
		}
		Assert.assertNotNull(resp);
		Assert.assertEquals(0, resp.getStatusCode());
		System.out.println("QueryResponse >>>>>>> "+resp.toString());
	}
	
	@Test
	public void syncRecentStop()
	{
		RecentStopsRequest req = new RecentStopsRequest();
		req.setUserId(userId);
		req.setContextString(tc.toContextString());
		req.setAddStops(new RecentStop[]{stop});
		
		System.out.println("RecentStopsRequest >>>>>>> "+ req.toString());
		
		RecentStopsResponse resp = null;
		try {
			resp = proxy.syncRecentStops(req, tc);
		} catch (ThrottlingException e) {
			// TODO: handle exception
		}
		Assert.assertNotNull(resp);
		Assert.assertEquals(0, resp.getStatus());
		System.out.println("RecentStopsResponse >>>>>>> "+ resp.toString());
		recentStopId = resp.getInsertedStops()[0].getRecentStopId();
	}
	
	@Test
	public void deleteStops()
	{
		//
		syncRecentStop();
		//
		stop.setRecentStopId(recentStopId);
		DeleteStopsRequest req = new DeleteStopsRequest();
		req.setUserId(userId);
		req.setContext(tc.toContextString());
		req.setDelStops(new RecentStop[]{stop});
		
		System.out.println("DeleteStopsRequest >>>>>>> "+ req.toString());
		
		DeleteStopsResponse resp = null;
		try {
			resp = proxy.DeleteStops(req, tc);
		} catch (ThrottlingException e) {
			// TODO: handle exception
		}
		Assert.assertNotNull(resp);
		Assert.assertEquals("0", resp.getStatusCode());
		System.out.println("DeleteStopsRequest >>>>>>> "+ resp.toString());
	}
	
	@Test
	public void confirmReceived()
	{
		ConfirmRequest req = new ConfirmRequest();
		req.setUserId(userId);
		req.setContextString(tc.toContextString());
		req.setInsertStops(new long[]{100654740L,100654739L});
		
		System.out.println("ConfirmRequest >>>>>>> "+ req.toString());
		
		ConfirmResponse resp = null;
		try {
			resp = proxy.confirmReceived(req, tc);
		} catch (ThrottlingException e) {
			// TODO: handle exception
		}
		Assert.assertNotNull(resp);
		Assert.assertEquals("0", resp.getStatusCode());
		
		System.out.println("ConfirmResponse >>>>>>> "+ resp.toString());
		
		req.setDeleteStops(new long[]{100654740L,100654739L});
		try {
			resp = proxy.confirmReceived(req, tc);
		} catch (ThrottlingException e) {
			// TODO: handle exception
		}
		Assert.assertNotNull(resp);
		Assert.assertEquals("0", resp.getStatusCode());
	}
}
