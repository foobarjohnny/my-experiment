/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.dsr.struts.util;

import java.util.StringTokenizer;

import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.j2me.datatypes.TxNode;

/**
 * @author pzhang
 * 
 * @version 1.0, 2009-5-5
 */
public class AudioUtil {

    /**
     * 
     * @param Id
     * @return
     */
    public static TxNode getAuidoById(String ids) {
        TxNode node = new TxNode();
        String id = "";
        StringTokenizer st = new StringTokenizer(ids, ",");
        while (st.hasMoreTokens()) {
            id = st.nextToken();
            if (!"".equals(PoiUtil.getString(id))) {
                node.addValue(Integer.parseInt(id));
            }
        }
        return node;
    }

    /**
     * 
     * @param screenType
     * @param flowType
     * @return
     */
    public static String getFlowAction(String screenType, String flowType) {
        return AudioConstants.SCREEN_TYPE_INITIAL + screenType + flowType;
    }

    public static String getFirstFlowAction(String screenType) {
        return getFlowAction(screenType, AudioConstants.SCREEN_FLOW_ONE);
    }

    public static String getSecondFlowAction(String screenType) {
        return getFlowAction(screenType, AudioConstants.SCREEN_FLOW_TWO);
    }
    
    public static String getThirdFlowAction(String screenType) {
        return getFlowAction(screenType, AudioConstants.SCREEN_FLOW_THREE);
    }
    
    public static String getForthFlowAction(String screenType) {
        return getFlowAction(screenType, AudioConstants.SCREEN_FLOW_FOUR);
    }
}
