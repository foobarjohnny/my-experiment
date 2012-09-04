/**
 *
 */
package com.telenav.cserver.dsr.protocol;

import java.net.URLDecoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telenav.audio.streaming.common.Util;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.common.encryption.CipherUtil;
import com.telenav.cserver.dsr.ds.AddressProcessedResult;
import com.telenav.cserver.dsr.ds.AirportProcessedResult;
import com.telenav.cserver.dsr.ds.Command;
import com.telenav.cserver.dsr.ds.CommandProcessedResult;
import com.telenav.cserver.dsr.ds.PersonalizedProcessedResult;
import com.telenav.cserver.dsr.ds.PoiProcessedResult;
import com.telenav.cserver.dsr.ds.PoiQueryProcessedResult;
import com.telenav.cserver.dsr.ds.PoiSearchResultsProcessedResult;
import com.telenav.cserver.dsr.ds.ProcessedResult;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.ds.ResultType;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.util.ConvertUtil;
import com.telenav.cserver.dsr.util.MatchingUtil;
import com.telenav.cserver.dsr.util.ProcessConstants;
import com.telenav.cserver.dsr.util.ProcessUtil;
import com.telenav.cserver.dsr.util.ResourceConst;
import com.telenav.cserver.dsr.util.StrUtil;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.impl.interceptor.TnContextInterceptor;
import com.telenav.cserver.framework.executor.protocol.txnode.TxNodeRequestParser;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.resource.ResourceConstants;
import com.telenav.resource.data.AudioElement;
import com.telenav.resource.data.AudioMessage;
import com.telenav.resource.data.AudioRule;
import com.telenav.resource.data.PromptItem;
import com.telenav.resource.data.ResourceInfo;

/**
 * @author joses
 */
public class TxNodeProtocolHandler implements ProtocolHandler {

    private static Logger logger = Logger.getLogger(TxNodeProtocolHandler.class.getName());

    //Handle Request Members
    private int txNodeVersion = TxNode.VERSION_50;
    private static TxNodeRequestParser requestParser = new TxNodeRequestParser();
    private static TnContextInterceptor tnContextInterceptor = new TnContextInterceptor();
    private static ExecutorContext executorContext = new ExecutorContext();
    private static final int START_OFFSET = 0;

    //Format Response Members
    public static int DEFAULT_VERSION = TxNode.HEADER_55;
    private static int TYPE_DSR_MSG = 1;

    public byte[] formatError(int statusCode, String message, int recType, long transactionId) {

        TxNode respNode = new TxNode();
        respNode.setVersion(DEFAULT_VERSION);
        respNode.addValue(statusCode);
        respNode.addValue(recType);
        respNode.addValue(transactionId);
        respNode.addMsg(message);
        return TxNode.toByteArray(respNode);
    }

    public byte[] formatError(int statusCode, String message, long transactionId) {

        TxNode respNode = new TxNode();
        respNode.setVersion(getTxNodeVersion());
        respNode.addValue(statusCode);
        respNode.addValue(6);
        respNode.addValue(transactionId);
        respNode.addMsg(message);
        return TxNode.toByteArray(respNode);
    }

    public byte[] formatResponse(ProcessObject procObj) {

        TxNode respNode = getRespNode(procObj);

        logger.info(respNode.toString());
        return TxNode.toByteArray(respNode);
    }

    public RecContext parseRequest(byte[] metaData) {

        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("TxNodeRequest.setMeta");

        int metaLen = metaData.length;
        if (metaLen < 7) {
            String metaInfo = "metaLen < 7, metaLen=" + metaLen;
            logger.severe(metaInfo);
            cli.addData("metaInfo", metaInfo);
            cli.complete();
            return null;
        }

        int offset = START_OFFSET;

        byte recType = metaData[offset++];//33rd byte 
        byte fmt = metaData[offset++];//34th byte
        String metaInfo = "recType=" + recType + ",fmt=" + fmt;
        
        int stopSize = Util.getInt(metaData, offset);//35th byte
        offset += 4;
        
        TxNode stopNode = TxNode.fromByteArray(metaData, offset);//39th byte
        
        if (stopNode == null) {
            metaInfo += ",stopNode=null";
            logger.severe(metaInfo);
            cli.addData("metaInfo", metaInfo);
            cli.complete();
            return null;
        }
        offset += stopSize;

        txNodeVersion = stopNode.getVersion();
        
        metaInfo += ",clientTxNodeVersion=" + txNodeVersion;
        Stop stop = Stop.fromTxNode(stopNode);
        
        if (stop == null) {
            metaInfo += ",stop=null";
            logger.severe(metaInfo);
            cli.addData("metaInfo", metaInfo);
            cli.complete();
            return null;
        }
        try{
        	if(Integer.parseInt(stop.stopId) == -1)
        		stop.stopId = "0";
        }catch(NumberFormatException nfe){
			logger.log(Level.WARNING, "Cannot parse stopId as Integer");
			stop.stopId = "0";
        }
        metaInfo += ",stop=\n" + stop;
        if (recType >= ResourceConst.DSR_RECOGNIZE_TICKET) {
            //TODO remove this iPhone demo hack
            UserProfile user = requestParser.createUserProfile(new TxNode());
            user.setDevice("iphone");
            TnContext tnContext = tnContextInterceptor.getTnContext(user, executorContext);
            tnContext.addProperty("userId", user.getUserId());
            RecContext context = new RecContext(recType, fmt, stop, user, tnContext, user.getAudioFormat());

            metaInfo += "iphone demo,tnContext=\n" + tnContext;
            logger.fine(metaInfo);
            cli.addData("metaInfo", metaInfo);
            return context;
        }
        if (recType == ResourceConst.DSR_RECOGNIZE_EMAIL_COMMAND) {
            UserProfile user = requestParser.createUserProfile(new TxNode());
            user.setDevice("iphone");
            TnContext tnContext = tnContextInterceptor.getTnContext(user, executorContext);
            tnContext.addProperty("userId", user.getUserId());
            RecContext context = new RecContext(recType, fmt, stop, user, tnContext, user.getAudioFormat());

            metaInfo += " email command";
            logger.fine(metaInfo);
            cli.addData("metaInfo", metaInfo);
            return context;
        }

        if (metaLen < offset + 4) {
            logger.severe(metaInfo);
            logger.severe("metaLen < 38 + stopSize + 4, metaLen=" + metaLen);
            cli.addData("metaInfo", metaInfo);
            cli.addData("metaLen", "metaLen<38 + stopSize + 4, metaLen=" + metaLen);
            cli.complete();
            return null;
        }

        int mandatorySize = Util.getInt(metaData, offset);
        offset += 4;
        
        TxNode mandatoryNode = null;
        if (mandatorySize > 0) {
            mandatoryNode = TxNode.fromByteArray(metaData, offset);
        } else {
            logger.severe(metaInfo);
            logger.severe("mandatorySize=" + mandatorySize);
            cli.addData("metaInfo", metaInfo);
            cli.addData("mandatorySize", "mandatorySize=" + mandatorySize);
            cli.complete();
            return null;
        }
        logger.fine("mandatoryNode :"+mandatoryNode);
        
        //get userProfile and tnContext from mandatoryNode
        UserProfile user = requestParser.createUserProfile(mandatoryNode);
        if (isTN70(user)) {
            try {
            	
                String userId = user.getUserId();
                userId = URLDecoder.decode(userId, CipherUtil.STRING_ENCODING);
                userId = CipherUtil.decrypt(userId);
                user.setUserId(userId);
                
                String ptn = user.getMin();
                ptn = URLDecoder.decode(ptn, CipherUtil.STRING_ENCODING);
                ptn = CipherUtil.decrypt(ptn);
                user.setMin(ptn);
            } catch (Exception ignored) {
            	logger.severe(ignored.toString());
            }
        }
        TnContext tnContext = tnContextInterceptor.getTnContext(user, executorContext);
        tnContext.addProperty("userId", user.getUserId());
        RecContext context = new RecContext(recType, fmt, stop, user, tnContext, user.getAudioFormat());

        metaInfo += ",ttsFormat=" + context.ttsFormat
                + ",needGeoCoding=" + context.needsGeocoding
                + ",tnContext=\n" + tnContext;
        logger.fine(metaInfo);
        cli.addData("metaInfo", metaInfo);
        cli.addData(CliConstants.LABEL_CLIENT_INTO, clientInfo(context));
        cli.complete();
        return context;
    }

    private boolean isTN70(UserProfile user) {
    	logger.fine("User version: "+user.getVersion());
    	if(user.getVersion()!=null)
    		return user.getVersion().startsWith("7");
    	else
    		return false;
    }

    private String clientInfo(RecContext context) {
        UserProfile user = context.user;
        return "url=service_DSR_Request"
                + "&userid=" + user.getUserId()
                + "&min=" + user.getMin()
                + "&carrier=" + user.getCarrier()
                + "&platform=" + user.getPlatform()
                + "&device=" + user.getDevice()
                + "&version=" + user.getVersion()
                + "&buildNo=" + user.getBuildNumber();
    }

    private int getTxNodeVersion() {
        return txNodeVersion;
    }

    private TxNode getRespNode(ProcessObject respObj) {
        TxNode respNode = new TxNode();
       	respNode.setVersion(txNodeVersion);
        respNode.addValue(DataConstants.SUCCESS);
        respNode.addValue(respObj.context.recType);
        respNode.addValue(respObj.transactionId);

        TxNode msgNode = toMsgNode(respObj);
        if (msgNode.childrenSize() > 0 || msgNode.msgsSize() > 0) {
            respNode.addChild(msgNode);
        }

        List<PromptItem[]> promptList = respObj.getPromptItems();
        for (PromptItem[] items : promptList) {
            if (items != null) {
                TxNode promptNode = new TxNode();
                promptNode.addValue(ResourceConstants.TYPE_AUDIO_SEQUENCE);
                for (PromptItem item : items) {
                    if (item == null)
                        continue;
                    TxNode itemNode = new TxNode();
                    AudioElement[] elements = item.getAudioElements();
                    itemNode.addValue(ResourceConstants.TYPE_AUDIO_ITEM);
                    getTxNode(elements, itemNode);
                    promptNode.addChild(itemNode);
                }
                respNode.addChild(promptNode);
            }
        }

        respNode.addValue(System.currentTimeMillis() - respObj.getProfile().getDsrStart());

        return respNode;
    }

    private static void getTxNode(AudioElement[] elements, TxNode root) {
        if (elements == null) {
            return;
        }
        for (AudioElement element : elements) {
            if (element == null) {
                continue;
            }
            TxNode elementNode = null;
            if (element.getType() == ResourceConstants.TYPE_MSG_AUDIO) {
                AudioMessage audio = (AudioMessage) element;
                ResourceInfo info = audio.getResourceInfo();

                elementNode = new TxNode();
                elementNode.addValue(ResourceConstants.TYPE_MSG_AUDIO);
                elementNode.addValue(info.getId());
                if (info.getId() == ResourceConstants.NO_INDEX) {
                    //do nothing
                } else {
                    elementNode.addValue(info.getVersion());
                    if (info.getData() != null) {
                        elementNode.setBinData(info.getData());
                    }
                }
                AudioElement[] children = audio.getChildren();
                getTxNode(children, elementNode);

            } else if (element.getType() == ResourceConstants.TYPE_AUDIO_PROMPT) {
                AudioRule rule = (AudioRule) element;
                // create the prompt node for this POI result
                elementNode = new TxNode();
                elementNode.addValue(ResourceConstants.TYPE_AUDIO_PROMPT);
                elementNode.addValue(rule.getRuleId()); // rule id

                // add int arguments node
                // int[0] = result position in the list
                TxNode intArgsNode = new TxNode();
                intArgsNode.addValue(ResourceConstants.TYPE_INT_ARGUMENTS);
                int[] intArgs = rule.getIntArgs();
                if (intArgs != null) {
                    for (int arg : intArgs) {
                        intArgsNode.addValue(arg);
                    }
                }
                elementNode.addChild(intArgsNode);

                TxNode nodeArgsNode = new TxNode();
                nodeArgsNode.addValue(ResourceConstants.TYPE_NODE_ARGUMENTS);
                AudioElement[] nodeArgs = rule.getNodeArgs();
                getTxNode(nodeArgs, nodeArgsNode);
                elementNode.addChild(nodeArgsNode);
            }
            root.addChild(elementNode);
        }
    }
    
    private TxNode toMsgNode(ProcessObject respObj){
		
		TxNode msgNode = new TxNode();
        msgNode.addValue(TYPE_DSR_MSG);

        if (respObj.context.recType == ResourceConst.DSR_RECOGNIZE_COMMAND_CONTROL
        		&& !respObj.processedResults.isEmpty()
        		&& respObj.processedResults.get(0) instanceof CommandProcessedResult) {
        	Command command = ((CommandProcessedResult)
        							respObj.processedResults.get(0)).getCommand();
        	String commandStr = getCommandString(command);
        	if (commandStr != null)
        		msgNode.addMsg(commandStr);
        	else
        		logger.warning("Unable to resolve command: "+command);
        }

        for (ProcessedResult processedResult : respObj.processedResults) {
            msgNode.addChild(toTxNode(processedResult, respObj.getContext()));
        }

        return msgNode;
	}
    
    private String getCommandString(Command command)
    {
    	if (command == Command.DRIVE 
    			|| command == Command.DRIVE_HOME
    			|| command == Command.DRIVE_WORK)
    		return "drive";
    	else if (command == Command.MAP
    			|| command == Command.MAP_HOME
    			|| command == Command.MAP_WORK)
    		return "map";
    	else if (command == Command.SEARCH)
    		return "search";
    	else if (command == Command.RESUME)
    		return "resume";
    	else if (command == Command.SHOW_MOVIES
				|| command == Command.SHOW_TRAFFIC
				|| command == Command.SHOW_WEATHER
				|| command == Command.SHOW_COMMUTE)
    		return "show";
    	else
    		return null;
    }
    
    private TxNode toTxNode(ProcessedResult result, RecContext context) {
        TxNode node = new TxNode();
        
        int commandType = cmdCategory(result);
        node.addValue(commandType);
        
        for (String resultMsg : textResult(result, context.recType, commandType)) {
            if (resultMsg != null)
                node.addMsg(resultMsg);
        }
        
        Stop location = getLocation(result, context);
        if (location != null)
            node.addChild(location.toTxNode());
        return node;
    }
    
    private Stop getLocation(ProcessedResult result, RecContext context)
    {
    	ResultType type = result.getResultType();
    	if (type == ResultType.TYPE_ADDRESS)
    		return ((AddressProcessedResult) result).getStop();
    	else if (type == ResultType.TYPE_POI_QUERY)
    		return ((PoiQueryProcessedResult) result).getSearchLocation();
    	else if (type == ResultType.TYPE_POI_SEARCH_RESULTS)
    	{
    		return ((PoiSearchResultsProcessedResult)result).getAnchorPoint();
    		/**
    		List<TnPoi> searchResults = ((PoiSearchResultsProcessedResult)
    												result).getSearchResults();
    		
    		if (searchResults.size() > 0)
    			return ConvertUtil.convert2Stop(searchResults.get(0));
    			**/
    	}
    	else if (type == ResultType.TYPE_PERSONALIZED)
    		return ((PersonalizedProcessedResult) result).getLocation();
    	else if (type == ResultType.TYPE_COMMAND)
    	{
    		Command command = ((CommandProcessedResult) result).getCommand();
    		
    		if (command == Command.DRIVE_HOME
    		 || command == Command.DRIVE_WORK
    		 || command == Command.MAP_HOME
    		 || command == Command.MAP_WORK)
    			return context.location; // I have no Idea why we return the user's location
    	}
    	else if (type == ResultType.TYPE_AIRPORT)
    	{
    		TnPoi airport = ((AirportProcessedResult) result).getAirport();
			Stop stop = ConvertUtil.convert2Stop(airport);
			if (stop != null)
			{
				stop.firstLine = airport.getBrandName();
				stop.label = airport.getFeatureName();
			}

			return stop;
    	}
    	
    	return null;
    }

	private int cmdCategory(ProcessedResult result)
	{
		int cmdCategory = -1;
		if (result instanceof CommandProcessedResult)
		{
			ResultType type = result.getResultType();
			Command command = ((CommandProcessedResult) result).getCommand();
			
			if (command == Command.SHOW_MOVIES
					|| command == Command.SHOW_TRAFFIC
					|| command == Command.SHOW_WEATHER
					|| command == Command.SHOW_COMMUTE)
				cmdCategory = ProcessConstants.CMD_TYPE_SHORTCUT;
			else if (command == Command.MAP && ProcessUtil.isCurrentLocation(result))
				cmdCategory = ProcessConstants.CMD_TYPE_MAP_CURRENT;
			else if (command == Command.DRIVE_HOME || command == Command.MAP_HOME)
				cmdCategory = ProcessConstants.CMD_TYPE_LOCATE_MY_HOME;
			else if (command == Command.DRIVE_WORK || command == Command.MAP_WORK)
				cmdCategory = ProcessConstants.CMD_TYPE_LOCATE_MY_OFFICE;
			else if (type == ResultType.TYPE_PERSONALIZED)
				cmdCategory = ProcessConstants.CMD_TYPE_LOCATE_FAVOR;
			
			else if (type == ResultType.TYPE_POI_QUERY
					|| type == ResultType.TYPE_POI_SEARCH_RESULTS)
			{
				if (command == Command.DRIVE)
					cmdCategory = ProcessConstants.CMD_TYPE_DRIVE_POI;
				else if (command == Command.MAP)
					cmdCategory = ProcessConstants.CMD_TYPE_MAP_POI;
				else if (command == Command.SEARCH)
					cmdCategory = ProcessConstants.CMD_TYPE_SEARCH_POI;
			}
			else if (type == ResultType.TYPE_AIRPORT)
			{
				cmdCategory = ProcessConstants.CMD_TYPE_LOCATE_AIRPORT;
			}
			else if (type == ResultType.TYPE_ADDRESS)
			{
				cmdCategory = ProcessConstants.CMD_TYPE_ADDRESS;
			}
		}
		
		return cmdCategory;
	}
    
    private static String[] textResult(ProcessedResult result, int recType, int commandType) {
        switch (recType) {
	        case ResourceConst.DSR_RECOGNIZE_CITY_STATE:
	        {
	        	AddressProcessedResult addr = (AddressProcessedResult) result;
	            return new String[]{city(addr.getCity(),addr.getState())};
	        }
	        case ResourceConst.DSR_RECOGNIZE_AIRPORT:
	            return new String[]{airport((AirportProcessedResult) result)};
	        case ResourceConst.DSR_RECOGNIZE_ONE_SHOT_ADDRESS:
	        case ResourceConst.DSR_RECOGNIZE_ONE_SHOT_INTERSECTION:
	        	AddressProcessedResult addr = (AddressProcessedResult) result;
	            return new String[]{StrUtil.firstLetterUppercase(addr.getAddress(), "at") + ", "
	            											+ city(addr.getCity(), addr.getState())};
	        case ResourceConst.DSR_RECOGNIZE_COMMAND_CONTROL:
	        	CommandProcessedResult cmd = (CommandProcessedResult) result;
	            return cmdResult(cmd, commandType);
	            
	        case ResourceConst.DSR_RECOGNIZE_ONE_SHOT_POI:
	        	PoiQueryProcessedResult poiRes = (PoiQueryProcessedResult) result;
	        	//VZN-286
	        	
	        	if(StrUtil.isBlank(poiRes.getCity()) || StrUtil.isBlank(poiRes.getState()))
	        		return new String[]{StrUtil.firstLetterUppercase(poiRes.getName())};
	        	else
	        		return new String[]{StrUtil.firstLetterUppercase(poiRes.getName()),
	            					city(poiRes.getCity(),poiRes.getState())};
	        default:
	            return new String[]{result.getLiteral()};
	    }
    }
    
    private static String[] cmdResult(CommandProcessedResult result, int commandType){
    	
    	switch (commandType) {
    		case ProcessConstants.CMD_TYPE_MAP_POI:
    		case ProcessConstants.CMD_TYPE_SEARCH_POI:
    			PoiProcessedResult poi = (PoiProcessedResult) result;
    			//VZN-286
    			if(StrUtil.isBlank(poi.getCity()) || StrUtil.isBlank(poi.getState()))
    				return new String[]{poi.getName()};
    			else
    				return new String[]{poi.getName(),
	            				city(poi.getCity(),poi.getState())};
	            
	        case ProcessConstants.CMD_TYPE_LOCATE_AIRPORT:
	            return new String[]{airport((AirportProcessedResult) result)};
	        case ProcessConstants.CMD_TYPE_ADDRESS:
	        	AddressProcessedResult addr = (AddressProcessedResult) result;
	            return new String[]{city(addr.getCity(),addr.getState())};
	            
	        case ProcessConstants.CMD_TYPE_LOCATE_FAVOR:
	        	PersonalizedProcessedResult fav = (PersonalizedProcessedResult) result;
	        	return new String[]{""+fav.getId()};
	        	
	        case ProcessConstants.CMD_TYPE_LOCATE_MY_HOME:
	        	return new String[]{"Home"};
	        case ProcessConstants.CMD_TYPE_LOCATE_MY_OFFICE:
	        	return new String[]{"Office"};
	        case ProcessConstants.CMD_TYPE_DRIVE_POI:
	        	if (result.getResultType() == ResultType.TYPE_POI_SEARCH_RESULTS)
	        	{
	        		PoiSearchResultsProcessedResult p =
	        					(PoiSearchResultsProcessedResult) result;
	        		
	        		List<TnPoi> pois = p.getSearchResults();
	        		if (pois != null && pois.size() > 0)
	        			return new String[]{pois.get(0).getBrandName()};
	        	}
	        	
	        	PoiProcessedResult p = (PoiProcessedResult) result;
		        return new String[]{p.getName()};
	        	
	        case ProcessConstants.CMD_TYPE_MAP_CURRENT:
	            return new String[]{"current location"};
	        case ProcessConstants.CMD_TYPE_SHORTCUT:
	            return new String[]{getTarget(result.getCommand()), "0"};
	        default:
	            return new String[]{""};
	    }
    }
    
    private static String getTarget(Command command)
    {
    	switch (command)
    	{
    		case SHOW_COMMUTE:
    			return "commute";
    		case SHOW_TRAFFIC:
    			return "traffic";
    		case SHOW_WEATHER:
    			return "weather";
    		case SHOW_MOVIES:
    			return "movie";
    		default:
    			return "";
    	}
    }
    
    private static String city(String city, String state) {
        return MatchingUtil.cityStr(city, state);
    }

    private static String airport(AirportProcessedResult result) {
        return MatchingUtil.airportStr(result.getAirportCode(),result.getAirportName(),result.getLiteral());
    }

}
