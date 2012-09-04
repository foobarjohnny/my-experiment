package com.telenav.cserver.dsr.protocol;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.dsr.ds.AddressProcessedResult;
import com.telenav.cserver.dsr.ds.AirportProcessedResult;
import com.telenav.cserver.dsr.ds.CommandProcessedResult;
import com.telenav.cserver.dsr.ds.ListNumberProcessedResult;
import com.telenav.cserver.dsr.ds.PersonalizedProcessedResult;
import com.telenav.cserver.dsr.ds.PoiQueryProcessedResult;
import com.telenav.cserver.dsr.ds.PoiSearchResultsProcessedResult;
import com.telenav.cserver.dsr.ds.ProcessedResult;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.framework.DSRExecutorResponse;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.proto.util.ProtoAudioUtil;
import com.telenav.cserver.proto.util.ProtoUtil;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoAddressParse;
import com.telenav.j2me.framework.protocol.ProtoAirportParse;
import com.telenav.j2me.framework.protocol.ProtoCommandParse;
import com.telenav.j2me.framework.protocol.ProtoDSRResp;
import com.telenav.j2me.framework.protocol.ProtoDSRResult;
import com.telenav.j2me.framework.protocol.ProtoListNumberParse;
import com.telenav.j2me.framework.protocol.ProtoParse;
import com.telenav.j2me.framework.protocol.ProtoPersonalizedParse;
import com.telenav.j2me.framework.protocol.ProtoPoiQueryParse;
import com.telenav.j2me.framework.protocol.ProtoPoiResultsParse;
import com.telenav.resource.data.PromptItem;

public class ProtobufResponseFormatter implements ProtocolResponseFormatter
{
	private static Logger logger = Logger.getLogger(ProtobufResponseFormatter.class.getName());
	
	@Override
	public void format(Object formatTarget, ExecutorResponse[] responses)
											throws ExecutorException
	{
		ProtocolBuffer buffer = (ProtocolBuffer) formatTarget;
        buffer.setObjType(ProtocolConstants.DSR_REPONSE_TYPE);
		
		if(responses == null || responses.length == 0)
        	buffer.setBufferData(new byte[0]);
		else
		{
			ProcessObject procObj = ((DSRExecutorResponse) responses[0])
															.getProcessObject();
			buffer.setBufferData(formatResponse(procObj));
		}
	}
	
	 // COMMANDS
    private final String DRIVE = "drive";
    private final String DRIVE_HOME = "drive home";
    private final String DRIVE_WORK = "drive work";
    private final String SEARCH = "search";
    private final String MAP = "map";
    private final String MAP_HOME = "map home";
    private final String MAP_WORK = "map work";
    private final String SHOW_WEATHER = "show weather";
    private final String SHOW_TRAFFIC = "show traffic";
    private final String SHOW_MOVIES = "show movies";
    private final String SHOW_COMMUTE = "show commute";
    private final String RESUME = "resume";

	public byte[] formatResponse(ProcessObject procObj)
	{
		ProtoDSRResp.Builder builder = ProtoDSRResp.newBuilder();
		builder.setStatus(procObj.statusCode);
		if(procObj.errorMessage!=null)
			builder.setErrorMessage(procObj.errorMessage);
		builder.setTransactionId(procObj.getTransactionId());
		
		// format all of the results
		for (ProcessedResult result : procObj.getProcessedResults())
			builder.addElementResults(formatResult(result, procObj.context));
		
		// TODO format the audio items
		//builder.setPromptItem(ProtoAudioUtil.getPromptItem(procObj.getPromptItems()));
		for(PromptItem[] pItem : procObj.getPromptItems()){
			builder.addElementPromptItem(ProtoAudioUtil.getPromptItem(pItem));
		}
		
		byte[] out = null;
		
		try
		{
			out = builder.build().toByteArray();
		}
		catch (IOException e)
		{
			logger.log(Level.WARNING,
					"Unable to serialize protobuf DSR response.",e);
		}
		
       return out;
	}
	
	private ProtoDSRResult formatResult(ProcessedResult result, RecContext context)
	{
		ProtoDSRResult.Builder resultBuilder = ProtoDSRResult.newBuilder();
		resultBuilder.setLiteral(result.getLiteral());
		resultBuilder.setConfidence(result.getConfidence());
		
		ProtoParse.Builder parseBuilder = null;
		String command = null;
		switch (result.getResultType())
		{
			case TYPE_ADDRESS:
				AddressProcessedResult addressResult = (AddressProcessedResult) result;
				parseBuilder = ProtoParse.newBuilder();
				parseBuilder.setParseType(ProtoParse.ProtoParseType.TYPE_ADDRESS);
				
				ProtoAddressParse.Builder addressParse =
											ProtoAddressParse.newBuilder();
				addressParse.setAddress(addressResult.getAddress());
				addressParse.setCity(addressResult.getCity());
				addressParse.setState(addressResult.getState());
				
				addressParse.setStop(ProtoUtil
							.convertStopToProtoBuf(addressResult.getStop()));
				
				command = getCommand(addressResult);
				if (command != null)
					addressParse.setCommand(command);
				
				parseBuilder.setAddressParse(addressParse.build());
				resultBuilder.addElementParses(parseBuilder.build());
				break;
				
			case TYPE_AIRPORT:
				AirportProcessedResult airportResult = (AirportProcessedResult) result;
				parseBuilder = ProtoParse.newBuilder();
				parseBuilder.setParseType(ProtoParse.ProtoParseType.TYPE_AIRPORT);
				
				ProtoAirportParse.Builder airportParse = ProtoAirportParse.newBuilder();
				airportParse.setAirportCode(airportResult.getAirportCode());
				airportParse.setAirportName(airportResult.getAirportName());
				TnPoi poi = airportResult.getAirport();
				
				airportParse.setAirport(ProtoUtil.convertTnPoiToProtoBuf(poi,context.tnContext));
				
				command = getCommand(airportResult);
				if (command != null)
					airportParse.setCommand(command);
				
				parseBuilder.setAirportParse(airportParse.build());
				resultBuilder.addElementParses(parseBuilder.build());
				break;
				
			case TYPE_PERSONALIZED:
				PersonalizedProcessedResult personalizedResult =
												(PersonalizedProcessedResult) result;
				parseBuilder = ProtoParse.newBuilder();
				parseBuilder.setParseType(ProtoParse.ProtoParseType.TYPE_PERSONALIZED);
				
				ProtoPersonalizedParse.Builder personalizedParse =
												ProtoPersonalizedParse.newBuilder();
				personalizedParse.setId(personalizedResult.getId());
				personalizedParse.setLabel(personalizedResult.getLabel());
				personalizedParse.setUseAddress(personalizedResult.useAddress());
				personalizedParse.setLocation(ProtoUtil
							.convertStopToProtoBuf(personalizedResult.getLocation()));
				
				command = getCommand(personalizedResult);
				if (command != null)
					personalizedParse.setCommand(command);
				
				parseBuilder.setPersonalizedParse(personalizedParse.build());
				resultBuilder.addElementParses(parseBuilder.build());
				break;
				
			case TYPE_POI_QUERY:
				PoiQueryProcessedResult poiResult = (PoiQueryProcessedResult) result;
				parseBuilder = ProtoParse.newBuilder();
				parseBuilder.setParseType(ProtoParse.ProtoParseType.TYPE_POI_QUERY);
				
				ProtoPoiQueryParse.Builder poiQueryParse = ProtoPoiQueryParse.newBuilder();
				poiQueryParse.setName(poiResult.getName());
				poiQueryParse.setCity(poiResult.getCity());
				poiQueryParse.setState(poiResult.getState());
				poiQueryParse.setSearchLocation(ProtoUtil
								.convertStopToProtoBuf(poiResult.getSearchLocation()));
				
				command = getCommand(poiResult);
				if (command != null)
					poiQueryParse.setCommand(command);
				
				parseBuilder.setPoiQueryParse(poiQueryParse.build());
				resultBuilder.addElementParses(parseBuilder.build());
				break;	
				
			case TYPE_POI_SEARCH_RESULTS:
				PoiSearchResultsProcessedResult poiSearchResult =
								(PoiSearchResultsProcessedResult) result;
				parseBuilder = ProtoParse.newBuilder();
				parseBuilder.setParseType(ProtoParse.ProtoParseType.TYPE_POI_RESULTS);
				
				ProtoPoiResultsParse.Builder poiResultsParse = ProtoPoiResultsParse.newBuilder();
				poiResultsParse.setName(poiSearchResult.getName());
				poiResultsParse.setCity(poiSearchResult.getCity());
				poiResultsParse.setState(poiSearchResult.getState());
				
				for (TnPoi searchResult : poiSearchResult.getSearchResults())
				{
					poiResultsParse.addElementSearchResults(ProtoUtil
									.convertTnPoiToProtoBuf(searchResult,context.tnContext));
				}
				
				command = getCommand(poiSearchResult);
				if (command != null)
					poiResultsParse.setCommand(command);
				
				parseBuilder.setPoiResulstParse(poiResultsParse.build());
				resultBuilder.addElementParses(parseBuilder.build());
				break;	
				
			case TYPE_LIST_NUMBER:
				ListNumberProcessedResult listNumberResult =
											(ListNumberProcessedResult) result;
				parseBuilder = ProtoParse.newBuilder();
				parseBuilder.setParseType(ProtoParse.ProtoParseType.TYPE_LIST_NUMBER);
				
				ProtoListNumberParse.Builder listNumberParse =
													ProtoListNumberParse.newBuilder();
				listNumberParse.setNumber(listNumberResult.getNumber());
				
				parseBuilder.setListNumberParse(listNumberParse.build());
				resultBuilder.addElementParses(parseBuilder.build());
				break;
				
			case TYPE_COMMAND:
				CommandProcessedResult commandResult =
												(CommandProcessedResult) result;
				parseBuilder = ProtoParse.newBuilder();
				parseBuilder.setParseType(ProtoParse.ProtoParseType.TYPE_COMMAND);
				
				ProtoCommandParse.Builder commandParse = ProtoCommandParse.newBuilder();
				command = getCommand(commandResult);
				if (command != null)
					commandParse.setCommand(command);
				
				parseBuilder.setCommandParse(commandParse.build());
				resultBuilder.addElementParses(parseBuilder.build());
				break;
				
			default:
				logger.log(Level.WARNING,"Don't know how to format "
													+result.getResultType());
				return null;
		}
		
		return resultBuilder.build();
	}
	
	private String getCommand(CommandProcessedResult result)
	{
		if (result.getCommand() == null)
			return null;
		
		switch (result.getCommand())
		{
			case DRIVE:
				return DRIVE;
			case DRIVE_HOME:
				return DRIVE_HOME;
			case DRIVE_WORK:
				return DRIVE_WORK;
			case SEARCH:
				return SEARCH;
			case MAP:
				return MAP;
			case MAP_HOME:
				return MAP_HOME;
			case MAP_WORK:
				return MAP_WORK;
			case SHOW_COMMUTE:
				return SHOW_COMMUTE;
			case SHOW_MOVIES:
				return SHOW_MOVIES;
			case SHOW_TRAFFIC:
				return SHOW_TRAFFIC;
			case SHOW_WEATHER:
				return SHOW_WEATHER;
			case RESUME:
				return RESUME;
			default:
				logger.warning("No protobuf command defined for "+result.getCommand()+"!");
				return null;
		}
	}
}
