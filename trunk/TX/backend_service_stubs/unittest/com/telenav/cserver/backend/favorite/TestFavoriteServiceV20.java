package com.telenav.cserver.backend.favorite;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.favoritesV40.Favorite;
import com.telenav.cserver.backend.favoritesV40.CountReceivedAddressRequest;
import com.telenav.cserver.backend.favoritesV40.CountReceivedAddressResponse;
import com.telenav.cserver.backend.favoritesV40.FavoriteConfirmRequest;
import com.telenav.cserver.backend.favoritesV40.FavoriteConfirmResponse;
import com.telenav.cserver.backend.favoritesV40.FavoritesServiceProxy;
import com.telenav.cserver.backend.favoritesV40.QueryFavoritesRequest;
import com.telenav.cserver.backend.favoritesV40.QueryFavoritesResponse;
import com.telenav.cserver.backend.favoritesV40.SyncFavoritesRequest;
import com.telenav.cserver.backend.favoritesV40.SyncFavoritesResponse;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

public class TestFavoriteServiceV20 
{
	FavoritesServiceProxy proxy;
	
	TnContext tc;
	
	Favorite favorite;
	
	@Before
	public void init()
	{
		proxy = FavoritesServiceProxy.getInstance();
		tc = new TnContext();
		
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
		
		try {
			resp = proxy.getFavoriteCategory(req, tc);
		} catch (ThrottlingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(resp);
		Assert.assertEquals("OK", resp.getStatus_code());
	}

	@Test
	public void countNewlyReceivedAddresses()
	{
		CountReceivedAddressRequest req = new CountReceivedAddressRequest();
		req.setUserId(3707312);
		req.setContextString(tc.toContextString());
		CountReceivedAddressResponse resp = null;
		try {
			resp = proxy.countNewlyReceivedAddresses(req, tc);
		} catch (ThrottlingException e) {
			// TODO: handle exception
		}
		Assert.assertNotNull(resp);
		Assert.assertEquals("OK", resp.getStatus_code());
	}
	
	@Test
	public void syncFavoriteCategory()
	{
		SyncFavoritesRequest req = new SyncFavoritesRequest();
		req.setUserId(3707312);
		req.setContextString(tc.toContextString());
		SyncFavoritesResponse resp = null;
		try {
			resp = proxy.syncFavoriteCategory(req, tc);
		} catch (ThrottlingException e) {
			// TODO: handle exception
		}
		Assert.assertNotNull(resp);
		Assert.assertEquals("OK", resp.getStatus_code());
		
		
		//add Favorite
		Favorite favorite = new Favorite();
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
		
		FavoriteConfirmResponse resp = null;
		
		try {
			resp = proxy.confirmReceived(req, tc);
		} catch (ThrottlingException e) {
			// TODO: handle exception
		}
		Assert.assertNotNull(resp);
		Assert.assertEquals("OK", resp.getStatus_code());
	}
}
