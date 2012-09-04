/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.misc.executor.v20;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cserver.backend.addresssharing.AddressSharingServiceProxyV20;
import com.telenav.cserver.backend.addresssharing.FetchSharedAddressRequest;
import com.telenav.cserver.backend.addresssharing.FetchSharedAddressResponse;
import com.telenav.cserver.backend.datatypes.addresssharing.ReceiverUserInfo;
import com.telenav.cserver.backend.datatypes.addresssharing.SharedAddressItem;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.misc.struts.datatype.Address;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.util.TnUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-5-25
 */
public class SentAddressExecutor extends AbstractExecutor{
    private Logger logger = Logger.getLogger(SentAddressExecutor.class);
    public static final int PAGE_SIZE = 50;
    /**
     * 
     */
    public void doExecute(ExecutorRequest req, ExecutorResponse resp,
            ExecutorContext context) throws ExecutorException {
    	
    	UserProfile userProfile=req.getUserProfile();
        SentAddressResponse response = (SentAddressResponse) resp;
        TnContext tnContext = context.getTnContext();
        try {

        	FetchSharedAddressRequest fetchSharedAddressRequest = new FetchSharedAddressRequest();
        	fetchSharedAddressRequest.setContextString(tnContext.toContextString());
        	fetchSharedAddressRequest.setUserId(PoiUtil.getLongFrom(userProfile.getUserId()));
        	
        	FetchSharedAddressResponse fetchSharedAddressResponse = AddressSharingServiceProxyV20.getInstance().fetchSharedAddress(fetchSharedAddressRequest, tnContext);
        	List<SharedAddressItem> sharedAddressList = fetchSharedAddressResponse.getSharedAddressList();
        	String statusCode = fetchSharedAddressResponse.getStatusCode();
	        String statusMessage = fetchSharedAddressResponse.getStatusMessage();
	        
	        if ("OK".equalsIgnoreCase(statusCode)) {
	            resp.setStatus(ExecutorResponse.STATUS_OK);
	        } else {
	            resp.setStatus(ExecutorResponse.STATUS_FAIL);
	            resp.setErrorMessage(statusMessage);
	        }

            
            //invoke service to get address information
            //List<Address> addressList = getDummayData();
            List<Address> addressList = this.convertToAddressInfo(sharedAddressList);
            logger.info("SendAddress. pass the information to view.");
            //pass the information to view      
            if(addressList == null) addressList = new ArrayList<Address>();
            
            response.setAddressList(addressList);      
        } catch (Exception e) {
            throw new ExecutorException(e);
        }
    }
    
    /**
     * 
     * @param sharedAddressStatus
     * @return
     */
    private List<Address> convertToAddressInfo(List<SharedAddressItem> list)
    {
        logger.info("TnManagerImpl.convertToAddressInfo.");
        List<Address> addressList = new ArrayList<Address>();
        if(null == list){
        	return addressList;
        }

        for (SharedAddressItem sharedAddressItem : list) {
            //
            addressList.add(convertToAddress(sharedAddressItem));
        }

        return addressList;
    }
    
    
    /**
     * 
     * @param info
     * @return
     */
    private Address convertToAddress(SharedAddressItem info)
    {
        logger.info("TnManagerImpl.convertToAddress.");
        List<String> receiverList = new ArrayList<String>();
        Address address = new Address();
        address.setId(info.getId());
        address.setCity(TnUtil.getString(info.getCity()));
        address.setProvince(TnUtil.getString(info.getProvince()));
        address.setCreateTime(info.getCreateTime());
        address.setLabel(TnUtil.getString(info.getLabel()));
        address.setStreet(TnUtil.getString(info.getStreet()));
        address.setCountry(TnUtil.getString(info.getCountry()));
        address.setPostalCode(TnUtil.getString(info.getPostalCode()));
        
        address.setSentOn(TnUtil.getFormatedDate(info.getCreateTime(), "MM/dd/yyyy"));
        address.setSentAt(TnUtil.getFormatedDate(info.getCreateTime(), "hh:mm a"));
        
        StringBuffer displayText = new StringBuffer();
        displayText.append( address.getCity());
        displayText.append( "".equals(address.getProvince()) ? "": (", "));
        displayText.append( address.getProvince());
        displayText.append( "".equals(address.getPostalCode()) ? "": (" "));
        displayText.append( address.getPostalCode());
        address.setDisplayCityText(displayText.toString());
      
        String receiverText = "";
        for (ReceiverUserInfo receiverInfo : info.getReceiverList())
        {
           String ptn = TnUtil.getString(receiverInfo.getPtn());
           String displayPtn = ptn;
           if(displayPtn.length() == 10)
           {
               displayPtn = ptn.substring(0,3) + "-" + ptn.substring(3,6) + "-" + ptn.substring(6);
           }
           receiverText = TnUtil.getString(receiverInfo.getName()) + " (" + displayPtn + ")";
           receiverList.add(receiverText);
        }
        address.setReceiverList(receiverList);
        
        return address;
    }
}
