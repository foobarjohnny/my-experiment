/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.xnav;


import java.util.ArrayList;
import java.util.List;

import com.telenav.datatypes.locale.v10.Country;
import com.telenav.services.xnav.v10.BrandNamesResponseDTO;
import com.telenav.services.xnav.v10.CountryRequestDTO;
import com.telenav.services.xnav.v10.MessageAndPtnsRequestDTO;
import com.telenav.services.xnav.v10.PtnsAndGroupsRequestDTO;
import com.telenav.services.xnav.v10.UserRequestDTO;
import com.telenav.ws.datatypes.services.ResponseStatus;
import com.telenav.ws.datatypes.services.mgmt.ServiceMgmtResponseDTO;


/**
 * The utility of xnav data convertion
 * 
 * @author pzhang
 * 
 */
public class XnavDataConvert
{

    /**
     * get response
     * 
     * @return
     */
    public static ResendPinResponse convertResendPinResponse2Proxy(ServiceMgmtResponseDTO response)
    {   
        if(response==null) {
            return null;
        }
        ResendPinResponse proxy = new ResendPinResponse();
        ResponseStatus status = response.getResponseStatus();
        proxy.setStatusCode(status.getStatusCode());
        proxy.setStatusMessage(status.getStatusMessage());

        return proxy;
    }


    /**
     * COnvert the request to real one that can acceptable to server.
     */
    public static UserRequestDTO convertResendPinRequestProxy2Request(ResendPinRequest proxy)
    {
        if (proxy != null)
        {
        	UserRequestDTO request = new UserRequestDTO();
            request.setClientName(XnavServiceProxy.clientName);
            request.setClientVersion(XnavServiceProxy.clientVersion);
            request.setTransactionId(XnavServiceProxy.transactionId);
            request.setPtn(proxy.getPtn());
            request.setContextString(proxy.getContextString());
            
            return request;
        }
        return null;
    }
    
    /**
     * get response
     * 
     * @return
     */
    public static ReferToFriendResponse convertReferToFriendResponse2Proxy(ServiceMgmtResponseDTO response)
    {   
        if(response==null) {
            return null;
        }
        ReferToFriendResponse proxy = new ReferToFriendResponse();
        ResponseStatus status = response.getResponseStatus();
        proxy.setStatusCode(status.getStatusCode());
        proxy.setStatusMessage(status.getStatusMessage());

        return proxy;
    }


    /**
     * COnvert the request to real one that can acceptable to server.
     */
    public static PtnsAndGroupsRequestDTO convertReferToFriendRequestProxy2Request(ReferToFriendRequest proxy)
    {
        if (proxy != null)
        {
        	PtnsAndGroupsRequestDTO request = new PtnsAndGroupsRequestDTO();
            request.setClientName(XnavServiceProxy.clientName);
            request.setClientVersion(XnavServiceProxy.clientVersion);
            request.setTransactionId(XnavServiceProxy.transactionId);
            request.setPtns(proxy.getPtns());
            request.setUserId(proxy.getUserId());
            request.setContextString(proxy.getContextString());
            
            return request;
        }
        return null;
    }
    
    /**
     * get response
     * 
     * @return
     */
    public static SendETAResponse convertSendToETAResponse2Proxy(ServiceMgmtResponseDTO response)
    {   
        if(response==null) {
            return null;
        }
        SendETAResponse proxy = new SendETAResponse();
        ResponseStatus status = response.getResponseStatus();
        proxy.setStatusCode(status.getStatusCode());
        proxy.setStatusMessage(status.getStatusMessage());

        return proxy;
    }


    /**
     * COnvert the request to real one that can acceptable to server.
     */
    public static MessageAndPtnsRequestDTO convertSendToETARequestProxy2Request(SendETARequest proxy)
    {
        if (proxy != null)
        {
        	MessageAndPtnsRequestDTO request = new MessageAndPtnsRequestDTO();
            request.setClientName(XnavServiceProxy.clientName);
            request.setClientVersion(XnavServiceProxy.clientVersion);
            request.setTransactionId(XnavServiceProxy.transactionId);
            request.setPtns(proxy.getPtns());
            request.setUserId(proxy.getUserId());
            request.setMessage(proxy.getMessage());
            request.setContextString(proxy.getContextString());
            
            return request;
        }
        return null;
    }
    
    /**
     * get response
     * 
     * @return
     */
    public static FetchBrandNamesResponse convertFetchBrandNamesResponse2Proxy(BrandNamesResponseDTO response)
    {   
        if(response==null) {
            return null;
        }
        FetchBrandNamesResponse proxy = new FetchBrandNamesResponse();
        ResponseStatus status = response.getResponseStatus();
        proxy.setStatusCode(status.getStatusCode());
        proxy.setStatusMessage(status.getStatusMessage());
        String[] brandNames = response.getBrandNames();
        List<String> brandNameList = new ArrayList<String>();
        if(brandNames != null)
        {
        	for(int i=0; i< brandNames.length ; i++)
        	{
        		brandNameList.add(brandNames[i]);
        	}
        }
        
        proxy.setBrandNames(brandNameList);
        return proxy;
    }


    /**
     * COnvert the request to real one that can acceptable to server.
     */
    public static CountryRequestDTO convertFetchBrandNamesProxy2Request(FetchBrandNamesRequest proxy)
    {
        if (proxy != null)
        {
        	String country = proxy.getCountry();
    		if (country == null || country.trim().length() == 0
    				|| "USA".equals(country)) {
    			country = "US";
    		}
    		
        	CountryRequestDTO request = new CountryRequestDTO();
            request.setClientName(XnavServiceProxy.clientName);
            request.setClientVersion(XnavServiceProxy.clientVersion);
            request.setTransactionId(XnavServiceProxy.transactionId);
            request.setContextString("");
            request.setCountry(Country.Factory.fromValue(country));
    		request.setMaxResultCount(proxy.getCount());
    		
            return request;
        }
        return null;
    }
}
