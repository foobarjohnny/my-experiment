/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx;

import java.util.List;

import com.telenav.cserver.framework.management.jmx.AbstractBackendServerMonitor;
import com.telenav.cserver.framework.management.jmx.BackendServerConfiguration;

import junit.framework.TestCase;

/**
 * TestAbstractBackendServerMonitor.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Jul 7, 2011
 *
 */
public class TestAbstractBackendServerMonitor extends TestCase
{
    
    public void testInit4NormalCase()
    {
        AbstractBackendServerMonitor backendServerMonitor = new BackendServerMonitorForTest1();
        List<BackendServerConfiguration> backendServers = backendServerMonitor.backendServers;
        assertEquals(2, backendServers.size());
        BackendServerConfiguration server = backendServers.get(0);
        assertEquals("test1",server.getName());
        assertEquals("http://feedback-ws.telenav.com:8080/tnws/services/FeedbackService30",server.getServiceUrl());
        
        BackendServerConfiguration server2 = backendServers.get(1);
        assertEquals("test2",server2.getName());
        assertEquals("POI.TA: http://poisearch-ws.mypna.com/tnws/services/ContentSearchService;POI.YPC: http://poisearch-ypc-ws.mypna.com/tnws/services/ContentSearchService;POI.CN: http://172.16.101.67:8080/tnws/services/ContentSearchService",server2.getServiceUrl());
    }
    
    public void testInit4WithoutAnyInitMethod()
    {
        AbstractBackendServerMonitor backendServerMonitor = new AbstractBackendServerMonitor(){};
        List<BackendServerConfiguration> backendServers = backendServerMonitor.backendServers;
        assertEquals(0, backendServers.size());
    }
    
    public void testInit4BackendServerMonitor_whichHasIssue()
    {
        AbstractBackendServerMonitor backendServerMonitor = new BackendServerMonitorForTest2();
        List<BackendServerConfiguration> backendServers = backendServerMonitor.backendServers;
        assertEquals(1, backendServers.size());
        BackendServerConfiguration server = backendServers.get(0);
        assertEquals("test1",server.getName());
        assertEquals("http://feedback-ws.telenav.com:8080/tnws/services/FeedbackService30",server.getServiceUrl());
    }
    
    public void testInit4BackendServerMonitor_hasDuplicateMethod()
    {
        AbstractBackendServerMonitor backendServerMonitor = new BackendServerMonitorForTest3();
        List<BackendServerConfiguration> backendServers = backendServerMonitor.backendServers;
        assertEquals(1, backendServers.size());
        BackendServerConfiguration server = backendServers.get(0);
        assertEquals("test6",server.getName());
        assertEquals("http://feedback-ws.telenav.com:8080/tnws/services/FeedbackService30",server.getServiceUrl());
    }

	public void testMissingFunction() {
		AbstractBackendServerMonitor backendServerMonitor = new BackendServerMonitorForTest1();
		List<BackendServerConfiguration> backendServers = backendServerMonitor.backendServers;
		assertEquals(2, backendServers.size());
		BackendServerConfiguration server = backendServers.get(0);

		assertNotNull(server.toString());
		assertNotNull(server.toJSON());
	}

}
