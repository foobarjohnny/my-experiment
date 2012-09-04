/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.protocol;

import java.util.ArrayList;

import com.telenav.cserver.ac.executor.ShareAddressRequest;
import com.telenav.cserver.backend.commutealert.DataConverter;
import com.telenav.cserver.backend.datatypes.addresssharing.ContactInfo;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.datatype.backend.telenavfinder.IPoi;
import com.telenav.protocol.constants.NodeTypeDefinitions;
import com.telenav.xnav.datatype.telenavfinder.Poi;

/**
 * ShareAddressRequestParser
 * @author kwwang
 *
 */
public class ShareAddressRequestParser_CS implements ProtocolRequestParser
{

    @Override
    public ExecutorRequest[] parse(Object object) throws ExecutorException
    {
        TxNode node = (TxNode) object;
        ShareAddressRequest request = new ShareAddressRequest();
        request.setExecutorType(node.msgAt(0));
        request.setUserId(Long.valueOf(node.msgAt(1)));
        request.setLabel(node.msgAt(2));
        request.setSenderPTN(node.msgAt(3));
        request.setContactList(new ArrayList<ContactInfo>());

        TxNode child = null;
        for (int i = 0; i < node.childrenSize(); i++)
        {
            child = node.childAt(i);
            if (DataConstants.TYPE_STOP == child.valueAt(0))
            {
                request.setAddress(convert2StopFrom(child));
            }
            else if (DataConstants.TYPE_POI == child.valueAt(0))
            {
                Stop poiStop = convert2StopFrom(child.childAt(0));
                IPoi poi = new Poi(convertStopToPoi(poiStop));
                poi.setPoiId(Long.valueOf(child.msgAt(0)));
                poi.setBrandName(child.msgAt(1));
                poi.setPhone(child.msgAt(2));
                poi.setAverageRating((Double.valueOf(child.msgAt(3)).doubleValue()));
                request.setPoi(poi);
                request.setAddress(poiStop);
            }
            else if (NodeTypeDefinitions.TYPE_CONTACT_INFO_NODE == child.valueAt(0))
            {
                ContactInfo contact = new ContactInfo();
                contact.setName(child.msgAt(0));
                contact.setPtn(child.msgAt(1));
                request.getContactList().add(contact);
            }
        }

        return new ExecutorRequest[]{request};
    }

    private Stop convert2StopFrom(TxNode child)
    {
        Stop stop = new Stop();
        stop.lat = (int) child.valueAt(1);
        stop.lon = (int) child.valueAt(2);
        stop.label = child.msgAt(0);
        stop.firstLine = child.msgAt(1);
        stop.city = child.msgAt(2);
        stop.state = child.msgAt(3);
        stop.zip = child.msgAt(4);
        stop.country = child.msgAt(5);
        return stop;
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
