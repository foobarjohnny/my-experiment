package com.telenav.cserver.backend.ace;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.backend.proxy.ace.ValidateAddressProxy;
import com.telenav.cserver.backend.proxy.ace.ValidateAddressRequest;
import com.telenav.cserver.backend.proxy.ace.ValidateAddressResponse;
import com.telenav.kernel.util.datatypes.TnContext;

public class TestBRValidateAddressProxy
{
    private ValidateAddressRequest request;
    
    private TnContext tc;

    @Before
    public void init()
    {
        request = new ValidateAddressRequest();
        tc=new TnContext();
    }

    @Test
    public void validateBrAddress() throws Exception
    {
        request.parseFirstLine("Avenida Paulista 2300");
        request.setCity("Sao Paulo");
        request.setState("SP");

        ValidateAddressResponse resp = BackendProxyManager.getBackendProxyFactory().getBackendProxy(ValidateAddressProxy.class)
                .validateAddress(request, tc);
        verifyResponse(resp);
    }
    
    
    @Test
    public void validateBrAddressWithSpecialChars() throws Exception
    {
        request.parseFirstLine("%#$# Avenida Brasil 1800");
        request.setCity("Sao Paulo");
        request.setState("SP");

        ValidateAddressResponse resp = BackendProxyManager.getBackendProxyFactory().getBackendProxy(ValidateAddressProxy.class)
                .validateAddress(request, tc);
        verifyResponse(resp);
    }
    
    
    @Test
    public void validateBrAddressWithoutState() throws Exception
    {
        request.parseFirstLine("Avenida Brigadeiro Faria Lima 345");
        request.setCity("Sao Paulo");

        ValidateAddressResponse resp = BackendProxyManager.getBackendProxyFactory().getBackendProxy(ValidateAddressProxy.class)
                .validateAddress(request, tc);
        verifyResponse(resp);
    }
    
    @Test
    public void validateBrAddressWithoutFirstLine() throws Exception
    {
        request.setCity("Rio de Janeiro");
        request.setState("RJ");
        
        ValidateAddressResponse resp = BackendProxyManager.getBackendProxyFactory().getBackendProxy(ValidateAddressProxy.class)
                .validateAddress(request, tc);
        verifyResponse(resp);
    }
    
    @Test
    public void validateOtherBrAddress() throws Exception
    {
       // request.parseFirstLine("Aeroporto de Altamira");
        request.setCity("Aeroporto de Altamira");
//        request.setState("SP");

        ValidateAddressResponse resp = BackendProxyManager.getBackendProxyFactory().getBackendProxy(ValidateAddressProxy.class)
                .validateAddress(request, tc);
        verifyResponse(resp);
    }
    

    public void verifyResponse(ValidateAddressResponse resp)
    {
        Assert.assertTrue(resp.getAddresses().size() > 0);
        Assert.assertNotNull(resp.getAddresses().get(0).getCityName());
        Assert.assertNotNull(resp.getAddresses().get(0).getCountry());
        Assert.assertNotNull(resp.getAddresses().get(0).getCounty());
        Assert.assertNotNull(resp.getAddresses().get(0).getDoor());
        Assert.assertNotNull(resp.getAddresses().get(0).getFirstLine());
        Assert.assertNotNull(resp.getAddresses().get(0).getPostalCode());
        Assert.assertNotNull(resp.getAddresses().get(0).getState());
        Assert.assertNotNull(resp.getAddresses().get(0).getStreetName());
        Assert.assertNotNull(resp.getAddresses().get(0).getLatitude());
        Assert.assertNotNull(resp.getAddresses().get(0).getLongitude());
    }
}
