/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.addresssharing;


import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.AddressDataConverter;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.datatypes.addresssharing.ContactInfo;
import com.telenav.cserver.backend.datatypes.addresssharing.ReceiverUserInfo;
import com.telenav.cserver.backend.datatypes.addresssharing.SharedAddressItem;
import com.telenav.datatypes.addresssharing.v20.AddressSharingInfo;
import com.telenav.datatypes.addresssharing.v20.NamedLocation;
import com.telenav.datatypes.addresssharing.v20.SharedAddress;
import com.telenav.datatypes.addresssharing.v20.SharingType;
import com.telenav.datatypes.addresssharing.v20.UserInfo;
import com.telenav.datatypes.contact.v20.Contact;
import com.telenav.datatypes.services.v20.ServiceRequest;
import com.telenav.datatypes.stop.v20.RecentStop;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.services.addresssharing.v20.AddressSharingInfoResponseDTO;
import com.telenav.services.addresssharing.v20.AddressSharingRequestDTO;
import com.telenav.services.addresssharing.v20.BasicResponseDTO;
import com.telenav.services.addresssharing.v20.LocationSharingRequestDTO;
import com.telenav.services.addresssharing.v20.PaginationRequestDTO;


/**
 * The utility of share adress data convertion
 * 
 * @author xfliu
 * 
 */
public class AddressSharingDataConvertV20
{
	public static final double DEGREE_MULTIPLIER = 1.e5;

	public static final int PAGEINFO_PAGE_SIZE = 100;
	public static final int PAGEINFO_PAGE_NUMBER = 0;
	
	
	/*
	 * set common request field data
	 */
	
	public static void setCommonRequestField(ServiceRequest s){
		
		s.setClientName(AddressSharingServiceProxyV20.clientName);
		s.setClientVersion(AddressSharingServiceProxyV20.clientVersion);
		s.setTransactionId(AddressSharingServiceProxyV20.transactionId);
	}
    /**
     * get response
     * 
     * @return
     */
    public static AddressSharingResponse convertShareAddressResponse2Proxy(BasicResponseDTO qResponse)
    {   
        if(qResponse==null) {
            return null;
        }
        AddressSharingResponse proxy = new AddressSharingResponse();
        com.telenav.datatypes.services.v20.ResponseStatus status = qResponse.getResponseStatus();
        proxy.setStatusCode(status.getStatusCode());
        proxy.setStatusMessage(status.getStatusMessage());

        return proxy;
    }


    /**
     * Convert the request to real one that can acceptable to server.
     */
    public static AddressSharingRequestDTO convertShareAddressRequestProxy2Request(AddressSharingRequestV20 proxy)
    {
        if (proxy != null)
        {
        	AddressSharingRequestDTO request = new AddressSharingRequestDTO();
        	
        	//set common fields
        	setCommonRequestField(request);
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
    			contacts[i].setPrimaryEmail("");                  // upgrade later, need email 
    		}
    		request.setToContacts(contacts);
    		request.setToGroups(null);                            // upgrade later, need group
            //set Shared address
            Stop addressStop = proxy.getAddress();
            
            RecentStop stop = new RecentStop();
    		stop.setSourceFlag("S");                             // upgrade later, how to set source flag

            if (addressStop != null)
            {
            	com.telenav.datatypes.address.v40.Address address = new com.telenav.datatypes.address.v40.Address();
            	com.telenav.cserver.backend.datatypes.Address csAddress = new com.telenav.cserver.backend.datatypes.Address();
            	
            	AddressDataConverter.convertCSAddressToWSAddressV40(AddressDataConverter.convertStopToCSAddress(addressStop, csAddress), address);
                stop.setRecentAddress(address);
                stop.setLabel(addressStop.label);
            }
    		
    		//FIX BUG TNSIXTWO-1133, share poi.
    		TnPoi addressPoi = proxy.getTnPoi();
			if (null != addressPoi) {
				com.telenav.datatypes.content.tnpoi.v20.TnPoi recentPoi = new com.telenav.datatypes.content.tnpoi.v20.TnPoi();
				//XNav guy Hu Yun said that it's enough to only set POI ID, they will retrieve POI from DB by POI ID.
				recentPoi.setPoiId(addressPoi.getPoiId());

				com.telenav.ws.datatypes.common.Property[] attributes = new com.telenav.ws.datatypes.common.Property[2];
				attributes[0] = new com.telenav.ws.datatypes.common.Property();
				attributes[0].setKey("phoneNumber");
				attributes[0].setValue(addressPoi.getPhoneNumber());
				attributes[1] = new com.telenav.ws.datatypes.common.Property();
				attributes[1].setKey("brandName");
				attributes[1].setValue(addressPoi.getBrandName());
				recentPoi.setProperties(attributes);
				com.telenav.datatypes.address.v40.Address address = new com.telenav.datatypes.address.v40.Address();
				address = AddressDataConverter.convertCSAddressToWSAddressV40(addressPoi.getAddress(), address);
				recentPoi.setAddress(address);

				stop.setRecentPoi(recentPoi);
				stop.setRecentAddress(address);
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
        com.telenav.datatypes.services.v20.ResponseStatus status = response.getResponseStatus();
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
					sharedAddressItem.setStreet(sharedAddress.getStreet().getName());
					sharedAddressItem.setCity(sharedAddress.getCity());
					sharedAddressItem.setProvince(sharedAddress.getState());
					sharedAddressItem.setCountry(sharedAddress.getCountry().getValue());
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
        	setCommonRequestField(request);
            request.setContextString(proxy.getContextString());
            request.setUserId(proxy.getUserId());
    		request.setPageNumber(PAGEINFO_PAGE_NUMBER);    // upgrade later, which value is more reasonable     
    		request.setPageSize(PAGEINFO_PAGE_SIZE);        // upgrade later, which value is more reasonable              
    		
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
        	setCommonRequestField(request);
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
    			contacts[i].setFirstName(info.getFirstName());
    			contacts[i].setLastName(info.getLastName());
    			contacts[i].setPrimaryEmail("");
    		}
    		request.setToContacts(contacts);
            //set Shared address
    		NamedLocation location = new NamedLocation();
    		location.setLocationName(proxy.getMovieName());
    		location.setSourceFlag("C");                                 // what is source flag?
    		location.setBrandName(proxy.getBrandName());
    		
            Stop addressStop = proxy.getAddress();
            Address address = new Address();
            address = AddressDataConverter.convertStopToCSAddress(addressStop, address);
			com.telenav.datatypes.address.v40.Address address40 = new com.telenav.datatypes.address.v40.Address();
			address40 = AddressDataConverter.convertCSAddressToWSAddressV40(address, address40);
    		location.setAddress(address40);
    		
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
