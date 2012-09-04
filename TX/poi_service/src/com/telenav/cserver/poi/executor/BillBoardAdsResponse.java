/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.poi.executor;

import java.util.List;

import com.telenav.cserver.backend.datatypes.adservice.BillBoardAds;
import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * POISearchResponse_WS.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-20
 */
public class BillBoardAdsResponse extends ExecutorResponse
{
    List<BillBoardAds> billBoardAdsList;

    public List<BillBoardAds> getBillBoardAdsList()
    {
        return billBoardAdsList;
    }

    public void setBillBoardAdsList(List<BillBoardAds> billBoardAdsList)
    {
        this.billBoardAdsList = billBoardAdsList;
    }
    
}
