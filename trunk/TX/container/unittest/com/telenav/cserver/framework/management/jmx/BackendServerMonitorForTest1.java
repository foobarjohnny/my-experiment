/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx;

import com.telenav.cserver.framework.management.jmx.config.Monitor;
import com.telenav.cserver.framework.management.jmx.config.ServiceUrlParserFactory;


/**
 * BackendServerForMonitor1.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Jul 7, 2011
 *
 */
public class BackendServerMonitorForTest1 extends AbstractBackendServerMonitor
{
    
    @Monitor(server="test1", parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE,serviceUrlKeys="FEEDBACKSERVICE")
    private DetectResult monitorServer1()
    {
        DetectResult result = new DetectResult();
        return result;
    }
    
    @Monitor(server="test2", parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE,serviceUrlKeys="POI_SEARCH")
    private DetectResult monitorServer2()
    {
        DetectResult result = new DetectResult();
        return result;
    }
}
