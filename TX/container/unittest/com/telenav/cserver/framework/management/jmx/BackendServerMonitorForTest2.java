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
public class BackendServerMonitorForTest2 extends AbstractBackendServerMonitor
{

    @Monitor(server="test1", parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE,serviceUrlKeys="FEEDBACKSERVICE")
    private DetectResult monitorServer1()
    {
        DetectResult result = new DetectResult();
        return result;
    }

    @Monitor(server="test2", parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE,serviceUrlKeys="FEEDBACKSERVICE")
    private void LoadServer2(BackendServerConfiguration conf)
    {
        conf.setServiceUrl("test2Url");
    }
 
   
    @Monitor(server="test4", parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE,serviceUrlKeys="FEEDBACKSERVICE")
    private DetectResult monitorServer4()
    {
        DetectResult result = new DetectResult();
        return result;
    }
    @Monitor(server="test5", parserClass=ServiceUrlParserFactory.CLASS_XML_WEBSERVICE,serviceUrlKeys="FEEDBACKSERVICE")
    private void monitorServer5()
    {
    }
    
}
