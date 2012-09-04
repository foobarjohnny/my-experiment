/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on May 8, 2009
 * File name: ValidateAddressRequestParser.java
 * Package name: com.telenav.cserver.ac.protocol
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 5:05:41 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.ac.protocol;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.ac.executor.ShareAddressRequest;
import com.telenav.cserver.backend.datatypes.addresssharing.ContactInfo;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.util.DataConverter;
import com.telenav.cserver.util.JsonUtil;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.datatype.backend.telenavfinder.IPoi;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.xnav.datatype.telenavfinder.Poi;

/**
 * @author pzhang (pzhang@telenav.cn) 5:05:41 PM, May 8, 2009
 */
public class ShareAddressRequestParser extends BrowserProtocolRequestParser {
	
	private Logger logger = Logger.getLogger(ShareAddressRequestParser.class);

    @Override
    public String getExecutorType() {
        return "ShareAddress";
    }

    public ExecutorRequest parseBrowserRequest(HttpServletRequest object)
            throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) object;
        ShareAddressRequest request = new ShareAddressRequest();

        // Get the JSON request.
        DataHandler handler = (DataHandler) httpRequest
                .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
        TxNode body = handler.getAJAXBody();
        
        request.setUserId(PoiUtil.getUserId(handler));
        request.setSenderPTN(handler.getClientInfo(DataHandler.KEY_USERACCOUNT));
        String jsonStr = body.msgAt(0);
        JSONObject jo = new JSONObject(jsonStr);

        String label = JsonUtil.getString(jo, "label");
        String sendTo = JsonUtil.getString(jo, "sendTo");
        String addressString = JsonUtil.getString(jo, "addressString");


        //convert sendTo to group list and contact list
        List<String> groupList = new ArrayList<String>(); 
        
        JSONArray sendToArray =  new JSONArray(sendTo);
        int sendToLength = sendToArray.length();
        List<ContactInfo> contacts = new ArrayList<ContactInfo>();
        for(int i=0;i<sendToLength;i++)
        {
            JSONObject sendToJO = sendToArray.getJSONObject(i);
            ContactInfo contact = new ContactInfo();
            contact.setName(sendToJO.getString("name"));
            contact.setPtn(sendToJO.getString("ptn"));
            
            contacts.add(contact);
        }
        request.setContactList(contacts);

        //request.setContactList(contactList);
        request.setGroupList(groupList);
        JSONObject addressJO =  new JSONObject(addressString);
        Stop address = PoiUtil.convertAddress(addressJO);
        request.setAddress(address);
        request.setLabel(label);
        
        

        String poiString = JsonUtil.getString(jo, "poiString");
        if(!"".equals(poiString))
        {
            //set the poi information
            IPoi poi = this.getPoi(poiString,request.getAddress());
            request.setPoi(poi);
        }
        
        return request;
    }
    
    /**
     * 
     * @param text
     * @param stop
     * @return
     */
    private IPoi getPoi(String text,Stop stop)
    {
        com.televigation.db.poi.Poi dbPoi = DataConverter.convertStopToPoi(stop);
        IPoi poi = new Poi(dbPoi);
        JSONObject poiJO;
        try {
            poiJO = new JSONObject(text);
            poi.setBrandName(poiJO.getString("name"));
            poi.setPoiId(poiJO.getInt("poiId"));
            //Fix bug TNSIXTWO-1133
            poi.setPhone(poiJO.getString("phoneNumber"));
            poi.setAverageRating(poiJO.getInt("rating"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
        	logger.error("JSONException occured! the original text is "+text+", we want to get name,poiId,phoneNumber,rating from it");
        	logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
        }
        
        return poi;
    }
}
