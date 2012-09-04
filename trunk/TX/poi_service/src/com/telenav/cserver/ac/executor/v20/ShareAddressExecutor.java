/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on May 8, 2009
 * File name: ValidateAddressExecutor.java
 * Package name: com.telenav.cserver.ac.executor
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 5:12:07 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.ac.executor.v20;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.telenav.cserver.backend.addresssharing.AddressSharingRequest;
import com.telenav.cserver.backend.addresssharing.AddressSharingRequestV20;
import com.telenav.cserver.backend.addresssharing.AddressSharingResponse;
import com.telenav.cserver.backend.addresssharing.AddressSharingServiceProxyV20;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.poi.struts.util.RGCUtil;
import com.telenav.cserver.util.TnUtil;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;


/**
 * @author pzhang (pzhang@telenav.cn) 5:12:07 PM, May 8, 2009
 * copy and update by xfliu at 2011/12/6
 */
public class ShareAddressExecutor extends AbstractExecutor {
    /**
     * execute the action
     * 
     * @param request
     * @param response
     * @param context
     * @throws ExecutorException
     */
	private static Logger logger = Logger.getLogger(ShareAddressExecutor.class);
	
	public void doExecute(ExecutorRequest req, ExecutorResponse resp,
            ExecutorContext context) throws ExecutorException {
    	
    	UserProfile userProfile=req.getUserProfile();

        // Get the request and response
        ShareAddressRequest request = (ShareAddressRequest) req;
        ShareAddressResponse response = (ShareAddressResponse) resp;
        TnContext tnContext = context.getTnContext();
        Stop address = request.getAddress();
        //if not state/city, get the full location information
        if(address!= null && (TnUtil.getString(address.state).equals("") && (TnUtil.getString(address.city).equals(""))))
        {
            //do RGC
            request.setRGC(true);
            RGCUtil rgcutil = new RGCUtil();
            address = rgcutil.getCurrentLocation(address.lat, address.lon, tnContext); 
        }
        String label = request.getLabel();   
        if(label==null||(label!=null&&label.equals("")))
        {
            label = PoiUtil.getString(address.firstLine);
        }
        
        if(label!=null&&label.equals(""))
        {
            label = getDefaultLabel();
        }
        request.setLabel(label);
        address.label = label;
        
        response.setRGC(request.isRGC());
        response.setAddress(request.getAddress());

        AddressSharingRequestV20 addressSharingRequest = new AddressSharingRequestV20();
        addressSharingRequest.setUserId(PoiUtil.getLongFrom(userProfile.getUserId()));
        addressSharingRequest.setPtn(request.getSenderPTN());
        addressSharingRequest.setContextString(tnContext.toContextString());
        addressSharingRequest.setAddress(request.getAddress());
 
        addressSharingRequest.setContactList(request.getContactList());
        addressSharingRequest.setTnPoi(request.getPoi());
        try {
	        AddressSharingResponse addressSharingResponse = AddressSharingServiceProxyV20.getInstance().shareAddress(addressSharingRequest, tnContext);
	        String statusCode = addressSharingResponse.getStatusCode();
	        String statusMessage = addressSharingResponse.getStatusMessage();
	        
	        if ("OK".equalsIgnoreCase(statusCode)) {
	            resp.setStatus(ExecutorResponse.STATUS_OK);
	        } else {
	            resp.setStatus(ExecutorResponse.STATUS_FAIL);
	            resp.setErrorMessage(statusMessage);
	        }
        }
        catch (ThrottlingException e) {
			// TODO Auto-generated catch block
        	logger.error("error occured when sharing address!");
        	logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
			e.printStackTrace();
		}
    }

    /**
     * 
     * @return
     */
    private String getDefaultLabel()
    {
        String label = "MARK POINT LOC-";
        Date date = new Date();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-hh-mm",Locale.US);
        label += dateFormat.format(date);
        
        return label;
    }
}