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
public class BackendServerMonitorForTest3 extends AbstractBackendServerMonitor
{
    
    @Monitor(server="test6", parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE,serviceUrlKeys="FEEDBACKSERVICE")
    private DetectResult monitorServer6()
    {
        DetectResult result = new DetectResult();
        return result;
    }
    
    @Monitor(server="test6", parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE,serviceUrlKeys="FEEDBACKSERVICE")
    private DetectResult monitorServer7()
    {
        DetectResult result = new DetectResult();
        return result;
    }
}
