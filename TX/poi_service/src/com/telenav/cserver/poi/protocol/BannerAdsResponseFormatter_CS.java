/******************************************************************************
 * Copyright (c) 2010 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *******************************************************************************/
package com.telenav.cserver.poi.protocol;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.poi.executor.BannerAdsReponse;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.protocol.constants.NodeTypeDefinitions;

/**
 * @author jzhu (jzhu@telenav.cn) 10:10:08 AM
 */
public class BannerAdsResponseFormatter_CS implements ProtocolResponseFormatter
{

    @Override
    public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException
    {
        TxNode root = (TxNode) formatTarget;
        BannerAdsReponse resp = (BannerAdsReponse) responses[0];
                                    
        // main node
        root.addValue(DataConstants.TYPE_SERVER_RESPONSE);
        root.addValue(resp.getStatus());

        root.addMsg(resp.getExecutorType());
        root.addMsg(resp.getErrorMessage());

        TxNode bannerAdsNode = new TxNode();
        bannerAdsNode.addValue(NodeTypeDefinitions.TYPE_BANNER_ADS);
        bannerAdsNode.addValue(resp.getImageHeight());
        bannerAdsNode.addValue(resp.getImageWidth());
        bannerAdsNode.addMsg(resp.getClickUrl());
        bannerAdsNode.addMsg(resp.getImageUrl());
        bannerAdsNode.setBinData(resp.getImgData());
        
        root.addChild(bannerAdsNode);
        
    }


    

}
