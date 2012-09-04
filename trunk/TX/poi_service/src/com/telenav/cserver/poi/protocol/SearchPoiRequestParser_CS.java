/******************************************************************************
 * Copyright (c) 2010 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *******************************************************************************/
package com.telenav.cserver.poi.protocol;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.poi.datatypes.DataConvert;
import com.telenav.cserver.poi.executor.POISearchRequest_WS;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.protocol.constants.NodeTypeDefinitions;

/**
 * @author jzhu (jzhu@telenav.cn) 10:10:08 AM
 */
public class SearchPoiRequestParser_CS implements ProtocolRequestParser
{
    public static final int TYPE_POI_FROM_TYPEIN = 1;
    public static final int TYPE_POI_FROM_SPEAKIN = 2;
    public static final int TYPE_POI_TYPEIN_ALONG = 3;
    public static final int TYPE_POI_SPEAKIN_ALONG = 4;
    public static final int DEFAULT_RADIUS = 700;
    
    @Override
    public ExecutorRequest[] parse(Object object) throws ExecutorException
    {
        TxNode node = (TxNode) object;
        POISearchRequest_WS request = new POISearchRequest_WS();

        request.setExecutorType(node.msgAt(0));
        request.setSearchString(node.msgAt(1));
        
        request.setCategoryId((node.valueAt(0)));
        request.setSearchType((int)(node.valueAt(1)));
        
        int searchFromType = (int)node.valueAt(2);
        request.setSearchFromType(searchFromType);
        
        boolean needAudio = true;
        if(TYPE_POI_FROM_TYPEIN == searchFromType || TYPE_POI_TYPEIN_ALONG == searchFromType)
        {
            needAudio = false;
        }
        request.setNeedAudio(needAudio);
        
        request.setSortType((int)node.valueAt(3));
        request.setPageNumber((int)node.valueAt(4));
        request.setMaxResults((int)node.valueAt(5));
        
        // To request POI from COSE, Three parameters are required, page number, page size and maxResult
        // So we need add an individual parameter for page size
        // While it is impossible to add new parameter in client request since we have release many clients to market
        // After investigation, page size is equal to maxResults or 9 for 6.x product now.
        // If need, we can add new parameter in client request to set page size later.   
        request.setPageSize(Math.max(request.getMaxResults(),Constant.PAGE_SIZE));
        request.setMostPopular(node.valueAt(6)==1?true:false);
        request.setDistanceUnit((int)node.valueAt(7));
        if (node.getValuesCount()>8)
            request.setSponsorListingNumber((int)node.valueAt(8));
        else
            request.setSponsorListingNumber(1);
        
        request.setRadiusInMeter(DEFAULT_RADIUS);
        
        TxNode child = null;
        for (int i = 0; i < node.childrenSize(); i++)
        {
            child = node.childAt(i);
            if (DataConstants.TYPE_STOP == child.valueAt(0))
            {
                Stop stop = Stop.fromTxNode(child);
                request.setStop(DataConvert.convertToStop(stop));
            }
            else if (NodeTypeDefinitions.TYPE_ONE_BOX_ROUTE_NODE == child.valueAt(0))
            {
                request.setRouteID((int)child.valueAt(1));
                request.setSegmentId((int)child.valueAt(2));
                request.setEdgeId((int)child.valueAt(3));
                request.setShapePointId((int)child.valueAt(4));
                request.setRange((int)child.valueAt(5));
                request.setCurrentLat((int)child.valueAt(6));
                request.setCurrentLon((int)child.valueAt(7));
                request.setSearchAlongType((int)child.valueAt(8));
            }
        }

        return new ExecutorRequest[]
        { request };
    }
}
