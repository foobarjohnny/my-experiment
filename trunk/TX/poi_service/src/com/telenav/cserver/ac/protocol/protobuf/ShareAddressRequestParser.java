/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.protocol.protobuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cserver.ac.executor.ShareAddressRequest;
import com.telenav.cserver.backend.commutealert.DataConverter;
import com.telenav.cserver.backend.datatypes.addresssharing.ContactInfo;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.util.ProtoUtil;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.framework.protocol.ProtoBasePoi;
import com.telenav.j2me.framework.protocol.ProtoBizPoi;
import com.telenav.j2me.framework.protocol.ProtoContactInfo;
import com.telenav.j2me.framework.protocol.ProtoShareAddressReq;
import com.telenav.j2me.framework.util.ToStringUtils;
import com.telenav.kernel.datatype.backend.telenavfinder.IPoi;
import com.telenav.xnav.datatype.telenavfinder.Poi;

/**
 * ShareAddressRequestParser.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Feb 24, 2011
 *
 */
public class ShareAddressRequestParser  implements ProtocolRequestParser
{

	private static Logger logger = Logger.getLogger(ShareAddressRequestParser.class);
    public ExecutorRequest[] parse(Object object) throws ExecutorException
    {
        ProtocolBuffer protoBuffer = (ProtocolBuffer) object;
        ShareAddressRequest request = new ShareAddressRequest();
        
        ProtoShareAddressReq protoRequest = null;
        try
        {
            protoRequest = ProtoShareAddressReq.parseFrom(protoBuffer.getBufferData());
            if(logger.isDebugEnabled())
            {
            	logger.debug(ToStringUtils.toString(protoRequest));
            }
        }
        catch(IOException ex)
        {
            throw new ExecutorException("Failed to parse Proto Check PRotoShareAddress");
        }
        request.setLabel(protoRequest.getLabel());
        request.setSenderPTN(protoRequest.getSenderPTN());
        if(protoRequest.getStop() != null )
        {
            request.setAddress(ProtoUtil.convertProtoBufToStop(protoRequest.getStop()));
        }
        
        if(protoRequest.getPoi() != null )
        {
            convertProtoBufToIPOI(request,protoRequest.getPoi());
        }
        
        request.setContactList(new ArrayList<ContactInfo>());
        List<ProtoContactInfo> protoContactList = protoRequest.getContactList();
        for(ProtoContactInfo protoContactInfo : protoContactList)
        {
            ContactInfo contact = new ContactInfo();
            contact.setName(protoContactInfo.getName());
            contact.setPtn(protoContactInfo.getPtn());
            request.getContactList().add(contact);
        }
        return new ExecutorRequest[]{request};
    }
    
    public  void convertProtoBufToIPOI(ShareAddressRequest request, ProtoBasePoi protoBasePoi)
    {
        ProtoBizPoi pBizPoi = protoBasePoi.getBizPoi();
        IPoi poi = null;
        if (pBizPoi != null)
        {
            Stop poiStop = ProtoUtil.convertProtoBufToStop(pBizPoi.getAddress());
            poi = new Poi(convertStopToPoi(poiStop));
            poi.setPoiId(protoBasePoi.getPoiId());
            poi.setBrandName(pBizPoi.getBrand());
            poi.setPhone(protoBasePoi.getBizPoi().getPhoneNumber());
            poi.setAverageRating(protoBasePoi.getAvgRating());
            request.setPoi(poi);
            request.setAddress(poiStop);
        }
        
    }
    
    private com.televigation.db.poi.Poi convertStopToPoi(Stop stop)
    {
        com.televigation.db.poi.Poi dbPoi = new com.televigation.db.poi.Poi();

        dbPoi.setPostalCode(stop.zip);
        dbPoi.setCity(stop.city);
        dbPoi.setProvince(stop.state);
        dbPoi.setStreet(stop.firstLine);
        dbPoi.setLat(DataConverter.convertToDegree(stop.lat));
        dbPoi.setLon(DataConverter.convertToDegree(stop.lon));
        dbPoi.setCountry(stop.country);

        return dbPoi;
    }
    
}
