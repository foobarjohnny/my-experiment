package com.telenav.cserver.backend.billing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.billing2.offerrepository.dataTypes.GetPurchaseOptionResponse;
import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.backend.proxy.billing.OfferRepositoryProxy;
import com.telenav.cserver.backend.proxy.billing.OfferRepositoryProxyHelper;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

public class TestOfferRepositoryProxy
{
    private UserProfile user;

    private TnContext tc=new TnContext();
    
    private OfferRepositoryProxy proxy;

    @Before
    public void Setup()
    {
        user = new UserProfile();
        user.setPlatform("ANDROID");
        user.setDevice("default");
        user.setCarrier("ATT");
        user.setVersion("7.0.01");
        user.setUserId("16092933");
        user.setMin("1234567890");
        
        proxy=BackendProxyManager.getBackendProxyFactory().getBackendProxy(OfferRepositoryProxy.class);

    }
    
    @Test
    public void getPurchaseOption() throws ThrottlingException
    {
        GetPurchaseOptionResponse resp=proxy.getPurchaseOption(OfferRepositoryProxyHelper.newGetPurchaseOptionRequest(user, tc),tc);
        Assert.assertEquals("0000", resp.getResponseCode());
    }
    
   
}
