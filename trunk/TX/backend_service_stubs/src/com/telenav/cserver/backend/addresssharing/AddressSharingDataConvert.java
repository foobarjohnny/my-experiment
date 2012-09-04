/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.addresssharing;


import java.util.ArrayList;
import java.util.List;

import com.telenav.ws.datatypes.address.Address;
import com.telenav.ws.datatypes.address.GeoCode;
import com.telenav.cserver.backend.datatypes.addresssharing.ContactInfo;
import com.telenav.cserver.backend.datatypes.addresssharing.ReceiverUserInfo;
import com.telenav.cserver.backend.datatypes.addresssharing.SharedAddressItem;
import com.telenav.datatypes.addresssharing.v10.AddressSharingInfo;
import com.telenav.datatypes.addresssharing.v10.NamedLocation;
import com.telenav.datatypes.addresssharing.v10.SharedAddress;
import com.telenav.datatypes.addresssharing.v10.SharingType;
import com.telenav.datatypes.addresssharing.v10.UserInfo;
import com.telenav.datatypes.contact.v10.Contact;
import com.telenav.datatypes.content.poi.v10.PoiAttributes;
import com.telenav.datatypes.stop.v10.RecentAddress;
import com.telenav.datatypes.stop.v10.RecentPoi;
import com.telenav.datatypes.stop.v10.RecentStop;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.datatype.backend.telenavfinder.IPoi;
import com.telenav.services.addresssharing.v10.AddressSharingInfoResponseDTO;
import com.telenav.services.addresssharing.v10.AddressSharingRequestDTO;
import com.telenav.services.addresssharing.v10.LocationSharingRequestDTO;
import com.telenav.services.addresssharing.v10.PaginationRequestDTO;
import com.telenav.ws.datatypes.services.ResponseStatus;
import com.telenav.ws.datatypes.services.mgmt.ServiceMgmtResponseDTO;


/**
 * The utility of share adress data convertion
 * 
 * @author pzhang
 * 
 */
public class AddressSharingDataConvert
{
	public static final double DEGREE_MULTIPLIER = 1.e5;

    /**
     * get response
     * 
     * @return
     */
    public static AddressSharingResponse convertShareAddressResponse2Proxy(ServiceMgmtResponseDTO response)
    {   
        if(response==null) {
            return null;
        }
        AddressSharingResponse proxy = new AddressSharingResponse();
        ResponseStatus status = response.getResponseStatus();
        proxy.setStatusCode(status.getStatusCode());
        proxy.setStatusMessage(status.getStatusMessage());

        return proxy;
    }


    /**
     * Convert the request to real one that can acceptable to server.
     */
    public static AddressSharingRequestDTO convertShareAddressRequestProxy2Request(AddressSharingRequest proxy)
    {
        if (proxy != null)
        {
        	AddressSharingRequestDTO request = new AddressSharingRequestDTO();
        	
        	//set common fields
            request.setClientName(AddressSharingServiceProxy.clientName);
            request.setClientVersion(AddressSharingServiceProxy.clientVersion);
            request.setTransactionId(AddressSharingServiceProxy.transactionId);
            request.setContextString(proxy.getContextString());
            request.setSendSMS(true);
            //set Sender's PTN
            request.setSenderUserId(proxy.getUserId());
    		request.setSenderPtn(proxy.getPtn());
    		//set received contact's PTN
    		int contactSize = proxy.getContactList().size();
    		Contact[] contacts = new Contact[contactSize];
    		for(int i=0 ; i<contactSize; i++)
    		{
    			ContactInfo info = proxy.getContactList().get(i);
    			contacts[i] = new Contact();
    			contacts[i].setCellPhone(info.getPtn());
    			contacts[i].setFirstName(info.getFirstName());
    			contacts[i].setLastName(info.getLastName());
    			contacts[i].setPrimaryEmail("");
    		}
    		request.setToContacts(contacts);
            //set Shared address
            Stop addressStop = proxy.getAddress();
            
            RecentStop stop = new RecentStop();
    		stop.setSourceFlag("S");

            if (addressStop != null)
            {
                RecentAddress address = new RecentAddress();
                address.setCity(addressStop.city);
                address.setCountry(addressStop.country);
                address.setFirstLine(addressStop.firstLine);
                address.setPostalCode(addressStop.zip);
                GeoCode geoCode = new GeoCode();
                geoCode.setLatitude(convertToDegree(addressStop.lat));
                geoCode.setLongitude(convertToDegree(addressStop.lon));
                address.setGeoCode(geoCode);
                address.setState(addressStop.state);
                address.setLabel(addressStop.label);
                stop.setRecentAddress(address);
            }
    		
    		//FIX BUG TNSIXTWO-1133, share poi.
    		IPoi addressPoi = proxy.getPoi();
			if (null != addressPoi) {
				RecentPoi recentPoi = new RecentPoi();
				//XNav guy Hu Yun said that it's enough to only set POI ID, they will retrieve POI from DB by POI ID.
				recentPoi.setId(addressPoi.getPoiId() + "");
				recentPoi.setPoiRating(addressPoi.getAverageRating());
				recentPoi.setName("");

				PoiAttributes attributes = new PoiAttributes();
				attributes.setPhoneNumber(addressPoi.getPhone());
				attributes.setBrandName(addressPoi.getBrandName());
				recentPoi.setAttributes(attributes);

				Address poiAddress = new Address();
				poiAddress.setCity(addressPoi.getCity());
				poiAddress.setCountry(addressPoi.getCountry());
				poiAddress.setPostalCode(addressPoi.getPostalCode());
				poiAddress.setState(addressPoi.getProvince());
				poiAddress.setFirstLine(addressPoi.getStreet());
				poiAddress.setLastLine(addressPoi.getStreet2());
				recentPoi.setAddress(poiAddress);

				stop.setRecentPoi(recentPoi);
			}
    		
    		request.setAddress(stop);
    		
            return request;
        }
        return null;
    }
    
    
    /**
     * get response
     * 
     * @return
     */
    public static FetchSharedAddressResponse convertFetchSharedAddressResponse2Proxy(AddressSharingInfoResponseDTO response)
    {   
        if(response==null) {
            return null;
        }
        FetchSharedAddressResponse proxy = new FetchSharedAddressResponse();
        ResponseStatus status = response.getResponseStatus();
        proxy.setStatusCode(status.getStatusCode());
        proxy.setStatusMessage(status.getStatusMessage());

        List<SharedAddressItem> sharedAddressList = new ArrayList<SharedAddressItem>();
        AddressSharingInfo[] addressSharingInfos = response.getAddressSharingInfos();
        if(addressSharingInfos != null)
        {
        	for (AddressSharingInfo addressSharingInfo : addressSharingInfos) {
        		SharedAddressItem sharedAddressItem = new SharedAddressItem();
				//
        		sharedAddressItem.setId(addressSharingInfo.getAddressSharingId());
        		sharedAddressItem.setCreateTime(addressSharingInfo.getLifeCycle().getCreated().getTime());
        		//
        		SharedAddress sharedAddress = addressSharingInfo.getSharedAddress();
        		//get address detail
				if (sharedAddress != null) {
					
					sharedAddressItem.setLabel(sharedAddress.getLabel());
					sharedAddressItem.setStreet(sharedAddress.getFirstLine());
					sharedAddressItem.setCity(sharedAddress.getCity());
					sharedAddressItem.setProvince(sharedAddress.getState());
					sharedAddressItem.setCountry(sharedAddress.getCountry());
					sharedAddressItem.setPostalCode(sharedAddress.getPostalCode());
				}
		
				//get receiverUsers
				List<ReceiverUserInfo> receiverList = new ArrayList<ReceiverUserInfo>();
				UserInfo[] receiverUsers = addressSharingInfo.getReceiverUser();
				if (receiverUsers != null) 
				{
					for (UserInfo receiverUser : receiverUsers) {
						ReceiverUserInfo receiverUserInfo = new ReceiverUserInfo();
						receiverUserInfo.setName(receiverUser.getName());
						receiverUserInfo.setPtn(receiverUser.getPtn());
						receiverList.add(receiverUserInfo);
					}
				}
				sharedAddressItem.setReceiverList(receiverList);
				sharedAddressList.add(sharedAddressItem);
			}
        	
        	proxy.setSharedAddressList(sharedAddressList);
        }
        
        return proxy;
    }


    /**
     * Convert the request to real one that can acceptable to server.
     */
    public static PaginationRequestDTO convertFetchSharedAddressRequestProxy2Request(FetchSharedAddressRequest proxy)
    {
        if (proxy != null)
        {
        	PaginationRequestDTO request = new PaginationRequestDTO();
        	
        	//set common fields
            request.setClientName(AddressSharingServiceProxy.clientName);
            request.setClientVersion(AddressSharingServiceProxy.clientVersion);
            request.setTransactionId(AddressSharingServiceProxy.transactionId);
            request.setContextString(proxy.getContextString());
            request.setUserId(proxy.getUserId());
    		request.setPageNumber(0);
    		request.setPageSize(50);
    		
            return request;
        }
        return null;
    }
    
    
    /**
     * Convert the request to real one that can acceptable to server.
     */
    public static LocationSharingRequestDTO convertShareMovieRequestProxy2Request(LocationSharingRequest proxy)
    {
        if (proxy != null)
        {
        	LocationSharingRequestDTO request = new LocationSharingRequestDTO();
        	
        	//set common fields
            request.setClientName(AddressSharingServiceProxy.clientName);
            request.setClientVersion(AddressSharingServiceProxy.clientVersion);
            request.setTransactionId(AddressSharingServiceProxy.transactionId);
            request.setContextString(proxy.getContextString());
            request.setSendSMS(true);
            request.setSharingType(SharingType.MOVIE);
            request.setSaveSenderPtn(true);
            //set Sender's PTN
            request.setSenderUserId(proxy.getUserId());
    		request.setSenderPtn(proxy.getPtn());
    		//set received contact's PTN
    		int contactSize = proxy.getContactList().size();
    		Contact[] contacts = new Contact[contactSize];
    		for(int i=0 ; i<contactSize; i++)
    		{
    			ContactInfo info = proxy.getContactList().get(i);
    			contacts[i] = new Contact();
    			contacts[i].setCellPhone(info.getPtn());
    			contacts[i].setFirstName(info.getName());
    			contacts[i].setLastName("");
    			contacts[i].setPrimaryEmail("");
    		}
    		request.setToContacts(contacts);
            //set Shared address
    		NamedLocation location = new NamedLocation();
    		location.setLocationName(proxy.getMovieName());
    		location.setSourceFlag("C");
    		location.setBrandName(proxy.getBrandName());
    		
            Stop addressStop = proxy.getAddress();

    		Address address = new Address();
    		address.setCity(addressStop.city);
    		address.setCountry(addressStop.country);
    		address.setFirstLine(addressStop.firstLine);
    		address.setPostalCode(addressStop.zip);
    		GeoCode geoCode = new GeoCode();
    		geoCode.setLatitude(convertToDegree(addressStop.lat));
    		geoCode.setLongitude(convertToDegree(addressStop.lon));
    		address.setGeoCode(geoCode);
    		address.setState(addressStop.state);
    		address.setLabel(addressStop.label);
    		
    		location.setAddress(address);
    		
    		request.setLocation(location);
    		
            return request;
        }
        return null;
    }
    
    /**
     * 
     * @param dm5
     * @return
     */
    public static double convertToDegree(int dm5)
    {
        return dm5 / DEGREE_MULTIPLIER;
    }
}
