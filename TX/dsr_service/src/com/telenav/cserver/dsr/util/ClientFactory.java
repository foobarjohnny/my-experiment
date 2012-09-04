package com.telenav.cserver.dsr.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

import java.util.logging.Logger;

/**
 * User: llzhang
 * Date: 2009-6-25
 * Time: 17:05:56
 */
public class ClientFactory {
    static HttpClient dsrClient = null;
    static int dsrConnectionTime = 2000;
    static int dsrSoTimeOut = 20 * 1000;
    static int dsrMaxConnections = 30;

    synchronized public static HttpClient getClient() {
        if (dsrClient != null) {
            return dsrClient;
        }
        MultiThreadedHttpConnectionManager mgr = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams connectionManagerParams = new HttpConnectionManagerParams();
        connectionManagerParams.setDefaultMaxConnectionsPerHost(dsrMaxConnections);
        connectionManagerParams.setTcpNoDelay(true);
        connectionManagerParams.setStaleCheckingEnabled(true);
        connectionManagerParams.setLinger(0);
        mgr.setParams(connectionManagerParams);
        try {
            dsrClient = new HttpClient(mgr);
        }
        catch (Exception e) {
            dsrClient = new HttpClient();
            Logger.getLogger(ClientFactory.class.getName()).severe("use MultiThreadedHttpConnectionManager fail");
        }

        dsrClient.getHttpConnectionManager().getParams().setConnectionTimeout(dsrConnectionTime);
        dsrClient.getHttpConnectionManager().getParams().setSoTimeout(dsrSoTimeOut);
        return dsrClient;
    }    
}
