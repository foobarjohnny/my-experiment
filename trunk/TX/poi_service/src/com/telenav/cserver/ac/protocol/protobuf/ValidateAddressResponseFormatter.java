/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.protocol.protobuf;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.telenav.cserver.ac.executor.ValidateAddressResponseACEWS;
import com.telenav.cserver.backend.datatypes.ace.GeoCodeSubStatus;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoGeoCodeSubStatus;
import com.telenav.j2me.framework.protocol.ProtoStop;
import com.telenav.j2me.framework.protocol.ProtoValidateAddressResp;
import com.telenav.j2me.framework.util.ToStringUtils;

/**
 * ValidateAddressResponseParser.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Feb 25, 2011
 *
 */
public class ValidateAddressResponseFormatter implements ProtocolResponseFormatter
{

    private static Logger logger = Logger.getLogger(ValidateAddressResponseFormatter.class);
    
    @Override
    public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException
    {
        ValidateAddressResponseACEWS resp = (ValidateAddressResponseACEWS) responses[0];
        
        ProtocolBuffer protoBuffer = (ProtocolBuffer)formatTarget;
        
        ProtoValidateAddressResp.Builder validateAddressRespBuilder = ProtoValidateAddressResp.newBuilder();
        validateAddressRespBuilder.setStatus(resp.getStatus())
                                  .setErrorMessage(resp.getErrorMessage())
                                  .setTotalCount(resp.getTotalCount())
                                  .setGeoCodeStatusCode(resp.getGeoCodeStatusCode());
        
        if (resp.getAddresses() != null)
        {
            for (GeoCodedAddress geoCodedAddress : resp.getAddresses())
            {
                validateAddressRespBuilder.addElementAddresses(convertGeoCodedAddressToProtoBuffer(geoCodedAddress));
            }
        }
        
        try
        {
        	ProtoValidateAddressResp protoResp = validateAddressRespBuilder.build();
        	if( logger.isDebugEnabled() )
        	{
        		logger.debug(ToStringUtils.toString(protoResp));
        	}
            protoBuffer.setBufferData(protoResp.toByteArray());
        }
        catch (IOException ex)
        {
            logger.fatal("Failed to convert ValidateAddressResponse to ProtoBuffer", ex);
        }
    }
    
    private static final double DEGREE_MULTIPLIER = 1.e5; 
    
    private  ProtoStop convertGeoCodedAddressToProtoBuffer(GeoCodedAddress geoCodedAddress)
    {
    	ProtoStop.Builder builder = ProtoStop.newBuilder();
        builder.setCity(geoCodedAddress.getCityName())
               .setCountry(geoCodedAddress.getCountry())
               .setCounty(geoCodedAddress.getCounty())
               .setCrossStreetName(geoCodedAddress.getCrossStreetName())
               .setDoor(geoCodedAddress.getDoor())
               .setFirstLine(geoCodedAddress.getFirstLine())
               .setLabel(geoCodedAddress.getLabel())
               .setLastLine(geoCodedAddress.getLastLine())
               .setLat((int)(geoCodedAddress.getLatitude()*DEGREE_MULTIPLIER))
               .setLon((int)(geoCodedAddress.getLongitude()*DEGREE_MULTIPLIER))
               .setZip(geoCodedAddress.getPostalCode())
               .setState(geoCodedAddress.getState())
               .setStreetName(geoCodedAddress.getStreetName())
               .setGeoCodeSubStatus(convertGeoSubStatusToProtoBuffer(geoCodedAddress.getSubStatus()));
        return builder.build();
    }
    
    private ProtoGeoCodeSubStatus convertGeoSubStatusToProtoBuffer(GeoCodeSubStatus geoCodeSubStatus)
    {
        ProtoGeoCodeSubStatus.Builder builder = ProtoGeoCodeSubStatus.newBuilder();
        builder.setSubstatus_CITY_CHANGED(geoCodeSubStatus.isSubstatus_CITY_CHANGED())
               .setSubstatus_COUNTRY_CHANGED(geoCodeSubStatus.isSubstatus_COUNTRY_CHANGED())
               .setSubstatus_COUNTY_CHANGED(geoCodeSubStatus.isSubstatus_COUNTY_CHANGED())
               .setSubstatus_CROSSSTREET_CHANGED(geoCodeSubStatus.isSubstatus_CROSSSTREET_CHANGED())
               .setSubstatus_DOOR_CHANGED(geoCodeSubStatus.isSubstatus_DOOR_CHANGED())
               .setSubstatus_STATE_CHANGED(geoCodeSubStatus.isSubstatus_STATE_CHANGED())
               .setSubstatus_STREET_CHANGED(geoCodeSubStatus.isSubstatus_STREET_CHANGED())
               .setSubstatus_ZIP_CHANGED(geoCodeSubStatus.isSubstatus_ZIP_CHANGED());
        return builder.build();
    }

}
