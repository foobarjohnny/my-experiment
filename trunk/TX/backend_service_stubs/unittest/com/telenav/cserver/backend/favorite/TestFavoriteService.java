package com.telenav.cserver.backend.favorite;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.favorites.Favorite;
import com.telenav.cserver.backend.favorites.CountReceivedAddressRequest;
import com.telenav.cserver.backend.favorites.CountReceivedAddressResponse;
import com.telenav.cserver.backend.favorites.FavoriteConfirmRequest;
import com.telenav.cserver.backend.favorites.FavoriteConfirmResponse;
import com.telenav.cserver.backend.favorites.FavoritesServiceProxy;
import com.telenav.cserver.backend.favorites.QueryFavoritesRequest;
import com.telenav.cserver.backend.favorites.QueryFavoritesResponse;
import com.telenav.cserver.backend.favorites.SyncFavoritesRequest;
import com.telenav.cserver.backend.favorites.SyncFavoritesResponse;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

public class TestFavoriteService 
{
	FavoritesServiceProxy proxy;
	
	TnContext tc;
	
	Favorite favorite;
	
	@Before
	public void init()
	{
		proxy = FavoritesServiceProxy.getInstance();
		tc = new TnContext();
//		tc.addProperty(TnContext.PROP_CARRIER, "ATT");
//		tc.addProperty(TnContext.PROP_DEVICE, "9000");
//		tc.addProperty(TnContext.PROP_PRODUCT, "RIM");
//		tc.addProperty(TnContext.PROP_VERSION, "6.0.01");
//		tc.addProperty("application", "ATT_NAV");
//		tc.addProperty("login", "3817799999");
//		tc.addProperty("userid", "3707312");
		
		favorite = new Favorite();
		favorite.setFavoriteId(7173661);
		favorite.setUserId(3707312);

		Address address = new Address();
		address.setLatitude(37.37895);
		address.setLongitude(-122.00776);
		address.setFirstLine("239 Commercial St");
		address.setCity("SunnyVale");
		address.setCountry("US");
		address.setPostalCode("94085");
		
		favorite.setFavoriteAddress(address);
	}
	
	@Test
	public void getFavoriteCategory()
	{
		QueryFavoritesRequest req = new QueryFavoritesRequest();
		req.setUserId(3707312);
		req.setContextString(tc.toContextString());
		QueryFavoritesResponse resp = null;
		
		System.out.println("QueryFavoritesRequest >>>>>>>>>>> " + req.toString());
		
		try {
			resp = proxy.getFavoriteCategory(req, tc);
		} catch (ThrottlingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(resp);
		Assert.assertEquals("OK", resp.getStatus_code());
		System.out.println("QueryFavoritesResponse >>>>>>>>>>> " + resp.toString());
	}

	@Test
	public void countNewlyReceivedAddresses()
	{
		CountReceivedAddressRequest req = new CountReceivedAddressRequest();
		req.setUserId(3707312);
		req.setContextString(tc.toContextString());
		
		System.out.println("CountReceivedAddressRequest >>>>>>>>>>> " + req.toString());
		
		CountReceivedAddressResponse resp = null;
		try {
			resp = proxy.countNewlyReceivedAddresses(req, tc);
		} catch (ThrottlingException e) {
			// TODO: handle exception
		}
		Assert.assertNotNull(resp);
		Assert.assertEquals("OK", resp.getStatus_code());
		System.out.println("CountReceivedAddressResponse >>>>>>>>>>> " + resp.toString());
	}
	
	@Test
	public void syncFavoriteCategory()
	{
		SyncFavoritesRequest req = new SyncFavoritesRequest();
		req.setUserId(3707312);
		req.setContextString(tc.toContextString());
		
		System.out.println("SyncFavoritesRequest >>>>>>>>>>> " + req.toString());
		
		SyncFavoritesResponse resp = null;
		try {
			resp = proxy.syncFavoriteCategory(req, tc);
		} catch (ThrottlingException e) {
			// TODO: handle exception
		}
		Assert.assertNotNull(resp);
		Assert.assertEquals("OK", resp.getStatus_code());
		
		System.out.println("CountReceivedAddressResponse >>>>>>>>>>> " + resp.toString());
		
		req.setAddFavorites(new Favorite[]{favorite});
		try {
			resp = proxy.syncFavoriteCategory(req, tc);
		} catch (ThrottlingException e) {
			// TODO: handle exception
		}
		Assert.assertNotNull(resp);
		Assert.assertEquals("OK", resp.getStatus_code());
		Assert.assertTrue(resp.getAddFavorites().length>0);
	}
	
	@Test
	public void confirmReceived()
	{
		FavoriteConfirmRequest req = new FavoriteConfirmRequest();
		req.setUserId(3707312);
		req.setContextString(tc.toContextString());
		req.setDeleteFavorites(new Favorite[]{favorite});
		
		System.out.println("FavoriteConfirmRequest >>>>>>>>>>> " + req.toString());
		
		FavoriteConfirmResponse resp = null;
		
		try {
			resp = proxy.confirmReceived(req, tc);
		} catch (ThrottlingException e) {
			// TODO: handle exception
		}
		Assert.assertNotNull(resp);
		Assert.assertEquals("OK", resp.getStatus_code());
		System.out.println("FavoriteConfirmResponse >>>>>>>>>>> " + resp.toString());
	}
}
