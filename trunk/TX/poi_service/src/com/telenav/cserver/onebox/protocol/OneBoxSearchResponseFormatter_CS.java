/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.onebox.protocol;

import java.util.List;

import com.telenav.cserver.backend.commutealert.DataConverter;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.onebox.executor.OneBoxResponse;
import com.telenav.cserver.poi.datatypes.POI;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.protocol.constants.NodeTypeDefinitions;
import com.telenav.services.search.onebox.v10.QuerySuggestion;
import com.telenav.ws.datatypes.address.Address;

/**
 * OneBoxSearchResponseFormatter
 * 
 * @author kwwang
 * 
 */
public class OneBoxSearchResponseFormatter_CS implements ProtocolResponseFormatter
{

    @Override
    public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException
    {
        TxNode root = (TxNode) formatTarget;
        OneBoxResponse resp = (OneBoxResponse) responses[0];

        // main node
        root.addValue(DataConstants.TYPE_SERVER_RESPONSE);
        root.addValue(resp.getStatus());
        root.addMsg(resp.getExecutorType());
        root.addMsg(resp.getErrorMessage());
        if (resp.getPoiResp() != null)
            root.addMsg(String.valueOf(resp.getPoiResp().getTotalCount()));

        handleSuggesstion(root, resp);

        handleAddressList(root, resp);
        
        if (resp.getPoiResp() != null)
        {
            handlePOI(root, resp.getPoiResp().getPoiList(), DataConstants.TYPE_POI);

            handlePOI(root, resp.getPoiResp().getSponsorPoiList(), NodeTypeDefinitions.TYPE_SPONSOR_POI_NODE);
        }

    }

    private void handleSuggesstion(TxNode root, OneBoxResponse resp)
    {
        if (resp.getSuggestions() != null && resp.getSuggestions().length > 0)
        {
            for (QuerySuggestion suggession : resp.getSuggestions())
            {
                TxNode suggessionNode = new TxNode();
                suggessionNode.addValue(NodeTypeDefinitions.TYPE_SUGGESSION_NODE);
                suggessionNode.addMsg(suggession.getDisplayLabel());
                suggessionNode.addMsg(suggession.getQuery());
                root.addChild(suggessionNode);
            }
        }
    }

    private void handleAddressList(TxNode root, OneBoxResponse resp)
    {
        if (resp.getAddressList() != null && resp.getAddressList().size() > 0)
        {
            for (Address addr : resp.getAddressList())
            {
                TxNode addrNode = new TxNode();
                addrNode.addValue(DataConstants.TYPE_STOP);
                addrNode.addValue(DataConverter.convertToDM5(addr.getGeoCode().getLatitude()));
                addrNode.addValue(DataConverter.convertToDM5(addr.getGeoCode().getLongitude()));
                addrNode.addValue(1);// stop type
                addrNode.addValue(1);// isgeocoding
                addrNode.addMsg(addr.getFirstLine());
                addrNode.addMsg(addr.getFirstLine());
                addrNode.addMsg(addr.getCity());
                addrNode.addMsg(addr.getState());
                addrNode.addMsg(addr.getPostalCode());
                addrNode.addMsg(addr.getCountry());
                root.addChild(addrNode);
            }
        }
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
