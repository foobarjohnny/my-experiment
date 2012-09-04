package com.telenav.cserver.dsr.util;

import com.telenav.audio.client.ResourceServiceClient;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.dsr.ds.AddressProcessedResult;
import com.telenav.cserver.dsr.ds.AirportProcessedResult;
import com.telenav.cserver.dsr.ds.Command;
import com.telenav.cserver.dsr.ds.CommandProcessedResult;
import com.telenav.cserver.dsr.ds.PersonalizedProcessedResult;
import com.telenav.cserver.dsr.ds.PoiProcessedResult;
import com.telenav.cserver.dsr.ds.ProcessedResult;
import com.telenav.cserver.dsr.ds.ResultType;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.resource.ResourceConstants;
import com.telenav.resource.TtsConstants;
import com.telenav.resource.data.*;
import com.telenav.resource.protocol.DsrAudioRequest;
import com.telenav.resource.protocol.DsrAudioResponse;

import java.util.*;
import java.util.logging.Logger;

/**
 * User: llzhang
 * Date: 2009-6-26
 * Time: 11:38:03
 */
public class TTSAudioUtil {

    private static Logger logger = Logger.getLogger(TTSAudioUtil.class.getName());
   
    public static List<PromptItem[]> getAudio(ProcessObject procObj) {
        List<TtsItem> ttsItemList = new ArrayList<TtsItem>();
        Locale locale = new Locale("en", "US");

        for (ProcessedResult result : procObj.getProcessedResults()) {
            TtsItem ttsItem = getRequestItem(result, procObj.context.recType);
            if (ttsItem != null)
                ttsItemList.add(ttsItem);
        }

        if (procObj.context.recType == ResourceConst.DSR_RECOGNIZE_CITY_STATE){
            stateModify(ttsItemList);
        }

        if (ttsItemList.size() > 0) {
            DsrAudioRequest audioReq = new DsrAudioRequest(
                    ResourceConstants.DATA_PACKET_TYPE,
                    procObj.context.ttsFormat,
                    locale.toString(),
                    null,
                    ttsItemList.toArray(new TtsItem[ttsItemList.size()]));
            return getTTsAudio(audioReq);
        } else {
            return new ArrayList<PromptItem[]>();
        }
    }

    private static List<PromptItem[]> getTTsAudio(DsrAudioRequest req) {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("TTSAudioUtil.getTTsAudio");

        PromptItem[] multiSelectItems = null, promptItems = null, secondPromptItems = null;
        
        try {
            ResourceServiceClient rsc = new ResourceServiceClient();
            long start = System.currentTimeMillis();
            DsrAudioResponse resp = rsc.getDsrAudios(req);
            long costTime = System.currentTimeMillis()-start;
            cli.addData("rsc.getDsrAudios cost time(ms) ", ""+costTime);                        
            
            if (resp.wasSuccessful()) {
                multiSelectItems = resp.getMultiSelectSequence();

                promptItems = resp.getPromptSequence();

                secondPromptItems = resp.getSecondPromptSequence();

            }
        } catch (Exception ex) {
            logger.severe("getTTsAudio error");
            cli.setStatus(ex);
        }
        cli.complete();
        return Arrays.asList(multiSelectItems, promptItems, secondPromptItems);
    }
    

    private static void stateModify(List<TtsItem> ttsItemList) {

        for (TtsItem item : ttsItemList) {
            if (item.getValues().containsKey(TtsItem.KEY_POI_NAME)){
                item.getValues().remove(TtsItem.KEY_STATE);
            }
        }
    }

    
    private static Map<Integer, String> ttsValues(ProcessedResult result) {
        Map<Integer, String> values = new HashMap<Integer, String>();

        ResultType type = result.getResultType();
        
        // Add content
        switch(type)
        {
        	case TYPE_ADDRESS:
        		AddressProcessedResult a = (AddressProcessedResult) result;
        		
        		if (a.getCity() != null)
        			values.put(TtsItem.KEY_CITY, a.getCity());
        		
        		if (a.getState() != null)
        			values.put(TtsItem.KEY_STATE, StateConverter.getInstance().convert(a.getState()));
        		
        		if (a.getAddress() != null)
        			values.putAll(MatchingUtil.streetParams(a.getAddress()));
        		break;
        		
        	case TYPE_POI_QUERY:
        	case TYPE_POI_SEARCH_RESULTS:
        		PoiProcessedResult p = (PoiProcessedResult) result;
        		
        		if (p.getCity() != null)
        			values.put(TtsItem.KEY_CITY, p.getCity());
        		
        		if (p.getState() != null)
        			values.put(TtsItem.KEY_STATE, StateConverter.getInstance().convert(p.getState()));
        		
        		if (p.getName() != null)
        			values.put(TtsItem.KEY_POI_NAME, p.getName());
        		break;
        		
        	case TYPE_AIRPORT:
        		AirportProcessedResult ar = (AirportProcessedResult) result;
        		if (ar.getAirportCode() != null)
        			values.put(TtsItem.KEY_AIR_LABEL, ar.getAirportCode());
        		if (ar.getAirportName() != null)
        			values.put(TtsItem.KEY_AIR_FULL, ar.getAirportName());
        		break;
        		
        	case TYPE_PERSONALIZED:
        		PersonalizedProcessedResult f = (PersonalizedProcessedResult) result;
        		
        		if (f.useAddress())
        			values.putAll(MatchingUtil.streetParams(f.getLocation().firstLine));
        		else
        			values.put(TtsItem.KEY_FAVORITE, f.getLabel());
        		break;
        }
        
        // add command if this is a command result
        if (result instanceof CommandProcessedResult)
        {
        	int cmdType = getTtsCommandType( (CommandProcessedResult) result);
        	if (cmdType != -1)
        		values.put(TtsItem.KEY_CMD_TYPE, ""+cmdType);
        	//AUDIO-26
        	if(cmdType==108 && (result.getLiteral().indexOf("work") != -1)){
        		values.put(TtsItem.KEY_MISC, "WORK");
        	}
        }

        return values;
    }
    
    private static int getTtsCommandType(CommandProcessedResult result)
	{
		ResultType type = result.getResultType();
		Command command = ((CommandProcessedResult) result).getCommand();
		
		int ttsCommandtype = -1;
		if (command == Command.DRIVE_HOME || command == Command.MAP_HOME)
			ttsCommandtype = ProcessConstants.CMD_TYPE_LOCATE_MY_HOME;
		else if (command == Command.DRIVE_WORK || command == Command.MAP_WORK)
			ttsCommandtype = ProcessConstants.CMD_TYPE_LOCATE_MY_OFFICE;
		else if (type == ResultType.TYPE_PERSONALIZED)
			ttsCommandtype = ProcessConstants.CMD_TYPE_LOCATE_FAVOR;
		
		else if (type == ResultType.TYPE_POI_QUERY
				|| type == ResultType.TYPE_POI_SEARCH_RESULTS)
		{
			if (command == Command.DRIVE)
				ttsCommandtype = ProcessConstants.CMD_TYPE_DRIVE_POI;
			else if (command == Command.MAP)
				ttsCommandtype = ProcessConstants.CMD_TYPE_MAP_POI;
			else if (command == Command.SEARCH)
				ttsCommandtype = ProcessConstants.CMD_TYPE_SEARCH_POI;
		}
		else if (type == ResultType.TYPE_AIRPORT)
		{
			ttsCommandtype = ProcessConstants.CMD_TYPE_LOCATE_AIRPORT;
		}
		else if (type == ResultType.TYPE_ADDRESS)
		{
			ttsCommandtype = ProcessConstants.CMD_TYPE_ADDRESS;
		}
		else if (command == Command.SHOW_COMMUTE
			|| command == Command.SHOW_MOVIES
			|| command == Command.SHOW_TRAFFIC
			|| command == Command.SHOW_WEATHER)
		{
			ttsCommandtype = ProcessConstants.CMD_TYPE_SHORTCUT;
		}
		
		return ttsCommandtype;
	}

    private static TtsItem getRequestItem(ProcessedResult result, int recType) {
    	if(ProcessUtil.isCurrentLocation(result)){
    		return null;
    	}
        Map<Integer, String> values = ttsValues(result);
        int type = ttsType(recType);
        return new TtsItem(type, true, values);
    }

    private static int ttsType(int recType) {
        switch(recType){
            case ResourceConst.DSR_RECOGNIZE_ONE_SHOT_INTERSECTION:
            case ResourceConst.DSR_RECOGNIZE_ONE_SHOT_ADDRESS:
            case ResourceConst.DSR_RECOGNIZE_STREET_ADDRESS:
            case ResourceConst.DSR_RECOGNIZE_1_STREET_NAME:
            case ResourceConst.DSR_RECOGNIZE_2_STREET_NAME:
            case ResourceConst.DSR_RECOGNIZE_INTERSECTION:
                return TtsConstants.TTS_TYPE_STREET;
            case ResourceConst.DSR_RECOGNIZE_POI:
                return TtsConstants.TTS_TYPE_POI;
            case ResourceConst.DSR_RECOGNIZE_AIRPORT:
                return TtsConstants.TTS_TYPE_AIRPORT;
            case ResourceConst.DSR_RECOGNIZE_CITY_STATE:
                return TtsConstants.TTS_TYPE_CITY_STATE;
            case ResourceConst.DSR_RECOGNIZE_COMMAND_CONTROL:
                return TtsConstants.TTS_TYPE_COMMAND;
            case ResourceConst.DSR_RECOGNIZE_ONE_SHOT_POI:
                return TtsConstants.TTS_TYPE_ONE_SHOT_POI;
            default:
                return recType;
        }
    }
}
