/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.navmap;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.cli.CliTransactionFactory;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.j2me.server.navmap.NavMapDirectClient;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * NavmapServiceProxy
 *
 * @author mmwang
 * @version 1.0 2010-7-19
 * 
 */
public class NavmapServiceProxy
{
	
	public final static String SERVICE_NAVMAP = "NAVMAP";

	private static Logger logger = Logger.getLogger(NavmapServiceProxy.class);
	
	private static NavmapServiceProxy instance = new NavmapServiceProxy();
	
	/**
	 * it's a private constructor, get instance through com.telenav.cserver.backend.navmap.NavmapServiceProxy.getInstance()
	 */
	private NavmapServiceProxy()
	{
		// can't instant outter the class
	}
	
	public static NavmapServiceProxy getInstance() 
	{
		return instance;
	}
	
	/**
	 * send a navmap request
	 * 
	 * @param request
	 * @return
	 * @throws ThrottlingException 
	 */
	public TxNode sendRequest(TxNode request, TnContext tnContext) throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVMAP, tnContext);
			if(!startAPICall)
			{
				throw new ThrottlingException();
			}
			if (logger.isDebugEnabled())
			{
				logger.debug(request.toString());
			}
			TxNode respNode = NavMapDirectClient.sendRequest(request);
			
			if (logger.isDebugEnabled() && respNode != null)
			{
				logger.debug(respNode.toString());
			}
			
			return respNode;
		}
		finally
		{
			if(startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVMAP, tnContext);
			}
		}
	}
}
