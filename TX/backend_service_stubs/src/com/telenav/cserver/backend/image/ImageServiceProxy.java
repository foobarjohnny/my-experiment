/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.image;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.ImageServiceConfig;
import com.telenav.cserver.backend.config.WebServiceItem;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.framework.cli.CliTransactionFactory;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * 
 *
 * @author mmwang
 * @version 1.0 2010-7-20
 * 
 */
public class ImageServiceProxy
{

	private static Logger logger = Logger.getLogger(ImageServiceProxy.class);
	
	private static ImageServiceProxy instance = new ImageServiceProxy();
	
	public final static String SERVICE_GET_MAP_TILE = "IMAGE";
	
	public final static String WS_SERVICE_GET_MAP_TILE = "IMAGE_SERVICE";
	
	private ImageServiceConfig config;
	
	private HttpClient client;
	
	private ImageServiceProxy()
	{
		// private constructor
	}
	
	public static ImageServiceProxy getInstance()
	{
		return instance;
	}
	
    /**
     * This may need to be moved to the WebServiceItem itself Creates and returns the HTTP client based on the
     * connection parameters Before you call this method, make sure that the connection parameters have been loaded via
     * the spring framework Once created - the HTTP client is cached ; so in effect the HTTP Client is visible over the
     * life of this WebServiceItem This method is synchronized so that clients do not get half initialized http clients
     * the first time.
     * 
     * @return
     */
    synchronized HttpClient getMapTileHttpClient()
    {
        if (client != null)
        {
            return client;
        }
        
        WebServiceItem item = getConfig().getWebServiceItem();
        
        MultiThreadedHttpConnectionManager mgr = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams connectionManagerParams = new HttpConnectionManagerParams();
        connectionManagerParams.setDefaultMaxConnectionsPerHost(item.getWebServiceMaxConnectionPerHost());
        connectionManagerParams.setTcpNoDelay(true);
        connectionManagerParams.setStaleCheckingEnabled(true);
        connectionManagerParams.setLinger(0);

        mgr.setParams(connectionManagerParams);
        try
        {
            client = new HttpClient(mgr);
        }
        catch (Exception e)
        {
            client = new HttpClient();
        }

        client.getHttpConnectionManager().getParams().setConnectionTimeout(item.getWebServiceConnectionTimeout());
        client.getHttpConnectionManager().getParams().setSoTimeout(item.getWebServiceTimeout());
        return client;
    }

	/**
     * Get map tile from backend map server
     * 
     * @param request
     * @return
     * @throws IOException
     */
    public byte[] getMapTile(MapTileRequest request, TnContext tnContext) throws ThrottlingException
    {
    	CliTransaction cli = CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
    	cli.setFunctionName("get_map_tile");
        boolean startAPICall = false;
        byte[] bytes = null;
        HttpMethod method = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_GET_MAP_TILE, tnContext);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            String url = generateUrl(request);
            cli.addData("get_map_tile_url", url);
            method = new GetMethod(generateUrl(request));

            try
			{
				getMapTileHttpClient().executeMethod(method);
				bytes = method.getResponseBody();
			} catch (Exception e)
			{
				cli.setStatus(e);
				logger.fatal("exception", e);
			}
        }
        finally
        {
        	cli.complete();
            if (method != null)
            {
                try
                {
                    method.releaseConnection();
                }
                catch (Exception ex)
                {
                	logger.fatal("exception:", ex);
                }
            }
            if (startAPICall)
            {
                ThrottlingManager.endAPICall(SERVICE_GET_MAP_TILE, tnContext);
            }
        }
        return bytes;
    }

	/**
	 * @param request
	 * @return
	 */
	private String generateUrl(MapTileRequest request)
	{
        //map url should be http://dmap64-1.telenav.com/tile/?layers=NA_NT&x=2637&y=6353&zoom=3&tnv=1.00&style=ts=128
        //layers -> tile node; tnv -> cache version; style=ts -> pixell number
		String url = MessageFormat.format(getConfig().getServiceUrl()
				, request.getMapType(), request.getX(), request.getY(), request.getZoom()
				, getConfig().getMapTileVersion(), request.getPixel());
		if (logger.isDebugEnabled())
		{
			logger.debug("get image tile url" + url);
		}
		return url;
	}
	
    /**
	 * @return
	 */
	private synchronized ImageServiceConfig getConfig()
	{
        if (config == null) 
        {
        	config = (ImageServiceConfig) WebServiceConfiguration.getInstance().getServiceConfig(WS_SERVICE_GET_MAP_TILE); 
        }

        return config;
	}
}
