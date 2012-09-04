/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.commutealert;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.matcher.AlertsServiceRequestDTOMatcher;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.ws.datatypes.alerts.AlertDetails;
import com.telenav.ws.datatypes.alerts.AlertsServiceRequestDTO;
import com.telenav.ws.datatypes.alerts.AlertsServiceResponseDTO;
import com.telenav.ws.datatypes.alerts.AlertsServiceStatus;
import com.telenav.ws.datatypes.services.ResponseStatus;
import com.telenav.ws.services.alerts.AlertsServiceStub;

/**
 * TestCommuteAlertProxy.java
 * @author njiang
 * @version 1.0 2012-7-25
 */

@RunWith(PowerMockRunner.class)
public class TestCommuteAlertProxy extends TestCase{
	
	static CommuteAlertProxy proxy = new CommuteAlertProxy();
	
	public void testGetAlert4Success() throws Exception
    {
        //1.prepare 
        long alertId = 123;
        TnContext tc = new TnContext();
        
        AlertDetails result = proxy.getAlert(alertId, tc);
        
        assertNotNull(result);
    }
    
    public void testGetAlert4NotFoundAlert() throws Exception
    {
        //1.prepare 
        long alertId = 1;
        TnContext tc = new TnContext();
        
        try{
        	proxy.getAlert(alertId, tc);
            fail();
        }
        catch(CommuteAlertException ex)
        {
            assertEquals(CommuteAlertConstants.STATUS_WS_NULL_ALERT,ex.getCode());
        }
    }
    
    
    public void testGetTrafficSummary4Success() throws Exception
    {
        //1.prepare 
        long alertId = 1;
        TnContext tc = new TnContext();
        
        AlertsServiceResponseDTO result = proxy.getTrafficSummary(alertId, tc);
        
        assertNotNull(result);
    }
    
    
}
