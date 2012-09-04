/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.map;


import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.datatypes.map.LatLonPoint;
import com.telenav.cserver.framework.cli.CliTransactionFactory;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.kernel.util.datatypes.TnContext;
import com.televigation.mapproxy.ImageSettings;
import com.televigation.mapproxy.MapserverProxy;
import com.televigation.mapproxy.NearbyCrossLookup;
import com.televigation.mapproxy.NearbyCrossStatus;
import com.televigation.mapproxy.ProxySocketException;
import com.televigation.mapproxy.datatypes.mapserverstatus.ImageStatus;
import com.televigation.mapproxy.datatypes.mapserverstatus.RGCStatus;

/**
 * a proxy for {@link MapserverProxy}
 *
 * @author mmwang
 * @version 1.0 2010-7-20
 * 
 */
public class MapServiceProxy
{

	private static Logger logger = Logger.getLogger(MapServiceProxy.class);
	
	private static MapServiceProxy instance = new MapServiceProxy();
	
	public final static String SERVICE_MAPSERVER = "MAPSERVER";
	
	public static MapServiceProxy getInstance()
	{
		return instance;
	}
	
	private MapServiceProxy()
	{
		// private constructor
	}
	
	/**
	 * query image data from map server
	 * @param imageSetting
	 * @param tnContext
	 * @return
	 * @throws ThrottlingException
	 */
	public QueryMapImageResponse queryMapImage(QueryMapImageRequest imageSetting,TnContext tnContext) throws ThrottlingException
	{
		CliTransaction cli = CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
		cli.setFunctionName("Query_Map_Image");
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_MAPSERVER, tnContext);
			if(!startAPICall)
			{
				//can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}
			ImageSettings setting = DataConverter.convertImageSetting(imageSetting);
			cli.addData("query_map_img_setting", imageSetting.toString());
			ImageStatus wStatus = createProxy(tnContext).queryMapImage(setting);
			QueryMapImageResponse status = null;
			if (wStatus != null) 
			{
				cli.addData("query_map_img_status", "status=" + wStatus.status + "&msg=" + wStatus.errorMsg);
				status = new QueryMapImageResponse();
				status.setImageData(wStatus.imageData);
			}
			return status;
		}
		finally
		{
			cli.complete();
			if(startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_MAPSERVER, tnContext);
			}
		}
	}
	
	/**
	 * query cross status from map server
	 * @param request TODO
	 * @param tnContext
	 * @return
	 * @throws ThrottlingException
	 * @throws ProxySocketException
	 */
	public NearbyCrossStatusResponse findNearbyCross(FindNearbyCrossRequest request,TnContext tnContext) throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_MAPSERVER, tnContext);
			if(!startAPICall)
			{
				//can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}
			NearbyCrossStatus crossStatus = null;
			try
			{
				crossStatus = NearbyCrossLookup.FindNearbyCross(DataConverter.convertLatLonPoint(request.getLatLonPoint()), request.getTMCId(), request.getEdgeIDs(), request.getStreetName());
			} catch (ProxySocketException e)
			{
				logger.error("exception:", e);
			}
			return DataConverter.convertNearbyCrossStatus(crossStatus);
		}
		finally
		{
			if(startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_MAPSERVER, tnContext);
			}
		}
	}
    
	public RGCStatusResponse queryRGC(LatLonPoint latLonPoint, double radius, TnContext tnContext)throws ThrottlingException
	{
		CliTransaction cli = CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
		cli.setFunctionName("query_rgc");
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_MAPSERVER, tnContext);
			if(!startAPICall)
			{
				//can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}
			cli.addData("query_rgc_request", "lat=" + latLonPoint.getLat() + "&lon=" + latLonPoint.getLon() + "&radius=" + radius);
			RGCStatus rgcStatus = createProxy(tnContext).queryRGC(DataConverter.convertLatLonPoint(latLonPoint), radius);
			cli.addData("query_rgc_status", "status=" + rgcStatus.status + "&msg=" + rgcStatus.errorMsg);
			return DataConverter.convertRGCStatus(rgcStatus);
		}
		finally
		{
			cli.complete();
			if(startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_MAPSERVER, tnContext);
			}
		}
	}
	
	/**
	 * @param tnContext
	 * @return
	 */
	private MapserverProxy createProxy(TnContext tnContext)
	{
		return new MapserverProxy(tnContext, null, null);
	}
}
