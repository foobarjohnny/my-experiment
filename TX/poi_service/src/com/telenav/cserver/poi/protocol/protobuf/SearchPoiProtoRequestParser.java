package com.telenav.cserver.poi.protocol.protobuf;

import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.poi.datatypes.DataConvert;
import com.telenav.cserver.poi.executor.POISearchRequest_WS;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoOneBoxRoute;
import com.telenav.j2me.framework.protocol.ProtoPoiSearchReq;
import com.telenav.j2me.framework.protocol.ProtoSearchItem;
import com.telenav.j2me.framework.util.ToStringUtils;
import com.telenav.protocol.constants.NodeTypeDefinitions;

public class SearchPoiProtoRequestParser implements ProtocolRequestParser 
{
	public static final int TYPE_POI_FROM_TYPEIN = 1;
    public static final int TYPE_POI_FROM_SPEAKIN = 2;
    public static final int TYPE_POI_TYPEIN_ALONG = 3;
    public static final int TYPE_POI_SPEAKIN_ALONG = 4;
    public static final int DEFAULT_RADIUS = 700;
    
    private static final Logger logger = Logger.getLogger(SearchPoiProtoRequestParser.class);
	@Override
	public ExecutorRequest[] parse(Object object) throws ExecutorException 
	{
		POISearchRequest_WS request = new POISearchRequest_WS();
		ProtocolBuffer protoBuffer = (ProtocolBuffer) object;
		ProtoPoiSearchReq pPoiSearch = null;
		try
		{
			pPoiSearch = ProtoPoiSearchReq.parseFrom(protoBuffer.getBufferData());
			if(logger.isDebugEnabled())
			{
				logger.debug(ToStringUtils.toString(pPoiSearch));
			}
		}
		catch(IOException ex)
		{
			throw new ExecutorException("Failed to parse Proto PoiSearch Request");
		}
		if(pPoiSearch == null)
		{
			throw new ExecutorException("ProtoPoiSearchReq is null");
		}
		request.setSearchString(pPoiSearch.getSearchString());
		request.setCategoryId(pPoiSearch.getCategoryId());
		request.setSearchType(pPoiSearch.getSearchType());
		int searchFromType = pPoiSearch.getSearchFromType();
		request.setSearchFromType(searchFromType);
		boolean needAudio = true;
        if(TYPE_POI_FROM_TYPEIN == searchFromType || TYPE_POI_TYPEIN_ALONG == searchFromType)
        {
            needAudio = false;
        }
        request.setNeedAudio(needAudio);
        request.setSortType(pPoiSearch.getSortType());
        request.setPageNumber(pPoiSearch.getPageNumber());
        request.setMaxResults(pPoiSearch.getMaxResult());
        
        // To request POI from COSE, Three parameters are required, page number, page size and maxResult
        // So we need add an individual parameter for page size
        // While it is impossible to add new parameter in client request since we have release many clients to market
        // After investigation, page size is equal to maxResults for 7.x product now, So we use maxResults to set page size currently
        // If need, we can add new parameter in client request to set page size later.
        request.setPageSize(request.getMaxResults());  
        
        request.setMostPopular(pPoiSearch.getIsMostPopular());
        request.setDistanceUnit(pPoiSearch.getDistanceUnit());
        request.setRadiusInMeter(DEFAULT_RADIUS);
        request.setSponsorListingNumber(pPoiSearch.getSponsorListingNumber());
        request.setTransactionID(pPoiSearch.getTransactionID());
        
        Vector searchItems = pPoiSearch.getSearchItems();
        for(int i = 0; i < searchItems.size(); i++)
        {
            ProtoSearchItem item = (ProtoSearchItem)searchItems.get(i);
            if(item.getType() == DataConstants.TYPE_STOP)
            {
            	request.setStop(DataConvert.convertToStop(item.getStop()));
            }
            else if(item.getType() == NodeTypeDefinitions.TYPE_ONE_BOX_ROUTE_NODE)
            {
            	ProtoOneBoxRoute oneBox = item.getOneBoxRoute();
            	if(oneBox != null)
            	{
            		request.setRouteID(oneBox.getRouteId());
            		request.setSegmentId(oneBox.getSegmentId());
            		request.setEdgeId(oneBox.getEdgeId());
            		request.setShapePointId(oneBox.getShapePointId());
            		request.setRange(oneBox.getRange());
            		request.setCurrentLat(oneBox.getCurrentLat());
            		request.setCurrentLon(oneBox.getCurrentLon());
            		request.setSearchAlongType(oneBox.getSearchAlongType());
            	}
            }
        }
		return new ExecutorRequest[]{request};
	}

}
