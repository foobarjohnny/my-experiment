/******************************************************************************
 * Copyright (c) 2010 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *******************************************************************************/
package com.telenav.cserver.poi.protocol.v20;

import java.util.List;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.poi.datatypes.POI;
import com.telenav.cserver.poi.executor.v20.POISearchResponse_WS;
import com.telenav.cserver.poi.protocol.POIResponseUtil;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.protocol.constants.NodeTypeDefinitions;

/**
 * @author jzhu (jzhu@telenav.cn) 10:10:08 AM
 * copy and update by xfliu at 2011/12/6
 */
public class SearchPoiResponseFormatter_CS implements ProtocolResponseFormatter
{

    @Override
    public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException
    {
        TxNode root = (TxNode) formatTarget;
        POISearchResponse_WS resp = (POISearchResponse_WS) responses[0];

        // main node
        root.addValue(DataConstants.TYPE_SERVER_RESPONSE);
        root.addValue(resp.getStatus());
        root.addValue(resp.getTotalCount());

        root.addMsg(resp.getExecutorType());
        root.addMsg(resp.getErrorMessage());

        TxNode promptNode = POIResponseUtil.getAudioTxNode(resp.getPromptItems());
        root.addChild(promptNode);
        
        handlePOI(root, resp.getPoiList(), DataConstants.TYPE_POI);

        handlePOI(root, resp.getSponsorPoiList(), NodeTypeDefinitions.TYPE_SPONSOR_POI_NODE);

    }


    
    private void handlePOI(TxNode root, List<POI> pois, int poiType)
    {
        if (pois != null && pois.size() > 0)
        {
            for (POI poi : pois)
            {
                TxNode poiTxNode = PoiUtil.toTxNode(poi, poiType);
                if (poiTxNode != null)
                {
                    root.addChild(poiTxNode);
                }
            }
        }
    }
}
