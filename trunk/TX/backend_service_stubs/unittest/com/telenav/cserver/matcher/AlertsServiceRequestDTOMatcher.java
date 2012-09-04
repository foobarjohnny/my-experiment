/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.matcher;

import org.apache.commons.lang.StringUtils;
import org.powermock.reflect.Whitebox;

import com.telenav.ws.datatypes.alerts.AlertsServiceRequestDTO;

/**
 * CreateRequestDTOMatcher.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2011-6-14
 *
 */
public class AlertsServiceRequestDTOMatcher extends AbstractMatcher<AlertsServiceRequestDTO>
{

    public AlertsServiceRequestDTOMatcher(AlertsServiceRequestDTO expected)
    {
        super(expected);
    }
    
    @Override
    boolean doMatch(AlertsServiceRequestDTO actual)
    {
        return    equals(expected, actual, new String[]{"localClientName","localClientVersion","localTransactionId","localContextString"})
               && expected.getAlertId() == actual.getAlertId();
    }
    
    
}
