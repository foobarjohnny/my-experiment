package com.telenav.cserver.dsr.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.WordUtils;

import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.dsr.ds.AddressProcessedResult;
import com.telenav.cserver.dsr.ds.AddressValidation;
import com.telenav.cserver.dsr.ds.AirportProcessedResult;
import com.telenav.cserver.dsr.ds.Command;
import com.telenav.cserver.dsr.ds.CommandProcessedResult;
import com.telenav.cserver.dsr.ds.PersonalizedProcessedResult;
import com.telenav.cserver.dsr.ds.PersonalizedType;
import com.telenav.cserver.dsr.ds.PoiQueryProcessedResult;
import com.telenav.cserver.dsr.ds.PoiSearchResultsProcessedResult;
import com.telenav.cserver.dsr.ds.ProcessedResult;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.ds.RecResult;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.util.GeoCodeUtil;
import com.telenav.cserver.dsr.util.ResourceConst;
import com.telenav.cserver.dsr.util.StrUtil;
import com.telenav.cserver.dsr.util.ProcessUtil;
import com.telenav.cserver.dsr.util.ProcessConstants;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.datatype.backend.teletrip.IPersonalStop;
import com.telenav.xnav.client.XNavClient;
import com.telenav.xnav.client.XNavServerResponseException;

/**
 * 
 * @author joses
 * 
 */
public class CommandControlHandler extends AbstractResultsHandler
{

	private static final Logger logger = Logger
			.getLogger(CommandControlHandler.class.getName());

	public ProcessObject process(ProcessObject procObj)
	{

		logger.log(Level.FINE, "Beginning to process");
		return super.process(procObj);
	}

	private Command handleDefaultCommand(RecResult recResult)
	{
		Command command = null;
		String mystuff = recResult.getSlot(
				ProcessConstants.SLOT_COMMAND_MYSTUFF).toLowerCase();

		if (recResult.getSlot(ProcessConstants.SLOT_COMMAND_NAME, null) != null)
		{
			command = Command.SEARCH;
		}
		else if (recResult.getSlot(ProcessConstants.SLOT_AIRPORT, null) != null)
		{
			command = Command.DRIVE;
		}
		else if (mystuff != null && !mystuff.equals(""))
		{

			if (mystuff.equals(ResourceConst.DSR_TARGET_HOME))
				command = Command.DRIVE_HOME;
			else if (mystuff.equals(ResourceConst.DSR_TARGET_WORK))
				command = Command.DRIVE_WORK;

		}
		else if (recResult
				.getSlot(ProcessConstants.SLOT_COMMAND_FAVORITE, null) != null)
		{
			command = Command.DRIVE;
		}
		else
		{// city
			command = Command.MAP;
		}

		return command;
	}

	int processResult(RecResult recResult)
	{

		logger.log(Level.FINE, " " + recResult.getId() + "Processing : "
				+ recResult.getValue());

		String literal = recResult.getValue();
		if (!StrUtil.notBlank(literal))
		{
			return STATUS_FAILED;
		}

		String commandStr = recResult.getSlot(
				ProcessConstants.SLOT_COMMAND_ACTION).toLowerCase();

		String target = recResult.getSlot(ProcessConstants.SLOT_COMMAND_TARGET)
				.toLowerCase();
		String mystuff = recResult.getSlot(
				ProcessConstants.SLOT_COMMAND_MYSTUFF).toLowerCase();

		String nameSlot = recResult.getSlot(ProcessConstants.SLOT_COMMAND_NAME);
		
		//drive home sometimes gets recognized by POI and City State language models
		if((mystuff == null || mystuff.length() == 0) && (nameSlot.equalsIgnoreCase("home")))
			mystuff = nameSlot;
		
		// NOTE: We don't expect target and mystuff to be filled at the same
		// time
		// FIXME: We should fix the simple cc grammar to fill work/home in the
		// target slot
		if (target == null || target.length() == 0)
			target = mystuff;

		Command command = Command.parseCommand(commandStr, target);
		double confidence = recResult.getConfidence();

		// Handle the default cases, i.e. someone didn't give a command
		if (command == null)
		{
			command = handleDefaultCommand(recResult);
		}

		// Handle command only cases, i.e. there is no other content associated
		// with the
		// command.
		if (command == Command.RESUME
				|| command == Command.SHOW_COMMUTE
				|| command == Command.SHOW_MOVIES
				|| command == Command.SHOW_TRAFFIC
				|| command == Command.SHOW_WEATHER
				|| command == Command.DRIVE_HOME
				|| command == Command.DRIVE_WORK)
		{
			processedResults.add(new CommandProcessedResult(literal,
					confidence, command));
			return STATUS_BREAK;
		}

		String state = recResult.getSlot(ProcessConstants.SLOT_STATE, null);
		String city = recResult.getSlot(ProcessConstants.SLOT_CITY, null);
		String airport = recResult.getSlot(ProcessConstants.SLOT_AIRPORT, null);
		String poi = recResult
				.getSlot(ProcessConstants.SLOT_COMMAND_NAME, null);
		String address = recResult
					.getSlot(ProcessConstants.SLOT_ADDRESS, null);
		int favoriteId = Integer.parseInt(recResult.getSlot(
				ProcessConstants.SLOT_COMMAND_FAVORITE, "-1"));
		boolean useCurrent = "true".equals(recResult
				.getSlot(ProcessConstants.SLOT_CURRENT_LOC));

		// CONTENT IS AIRPORT
		if (airport != null)
		{
			List<AirportProcessedResult> res = AirportHandler.handleAirportResult(
												recResult, context, command);
			
			if (res.size() > 0)
			{
				processedResults.addAll(res);
				return STATUS_BREAK;
			}
			else
				return STATUS_FAILED;
		}
		// CONTENT IS A FAVORITE
		else if (favoriteId != -1)
		{
			String userIdStr = context.user.getUserId();
			long userId = (userIdStr == null) ? -1 : Long.parseLong(userIdStr);
			
			boolean useAddress = Boolean.parseBoolean(recResult.getSlot(
    							ProcessConstants.SLOT_FAVORITE_USE_ADDRESS, null));
			PersonalizedProcessedResult result = getFavoriteResult(
							favoriteId,userId,literal,confidence,command,useAddress);
			
			if (result == null)
				return STATUS_FAILED;
			else
			{
				processedResults.add(result);
				return STATUS_BREAK;
			}
		}
		// CONTENT IS ONESHOT POI
		else if (poi != null)
		{
			ProcessedResult res = getPoiResult(poi, city, state, literal,
									confidence, command, context, recResult);

			if (res != null)
			{
				processedResults.add(res);
				return STATUS_BREAK;
			}
			else
				return STATUS_FAILED;
		}
		// MAP OF CURRENT LOCATION COMMAND
		else if (command == Command.MAP && useCurrent)
		{
			AddressProcessedResult result = new AddressProcessedResult(literal,
					confidence, command);

			// NOTE: We handle this by setting the address text to
			// "current location"
			// and setting the stop to the user's location. The client will
			// display
			// the address and show a map based on the stop, so this should
			// simulate
			// the original behavior.
			result.setAddress(ProcessConstants.CURRENT_LOCATION_HEADER);
			result.setStop(context.location);
			processedResults.add(result);
			return STATUS_BREAK;
		}
		// CONTENT IS AN ADDRESS
		else if (address != null || city != null)
		{

			logger.log(Level.FINE, "This is a street / xstreet");
			AddressValidation validation = GeoCodeUtil.validateAddress(
					recResult, context.tnContext);

			if (validation.isSuccess())
			{

				processedResults.addAll(OneshotAddressHandler
						.handleAddressResult(recResult, context, validation,
								command));

				if (validation.isExact())
				{
					procObj.setProcessedResults(ProcessUtil
							.exactMatchingFirst(processedResults));
					return STATUS_BREAK;
				}
				else
					return STATUS_SUCCESS;

			}
			else
			{
				logger.log(Level.FINE, "validation not successful");
				return STATUS_FAILED;
			}
		}
		else
		{
			// Ok, there is no content so just return a command processed result
			processedResults.add(new CommandProcessedResult(literal,
													confidence, command));
			return STATUS_BREAK;
		}
	}
	
	private ProcessedResult getPoiResult(String poi,
										 String city,
										 String state,
										 String literal,
										 double confidence,
										 Command command,
										 RecContext context,
										 RecResult recResult)
	{
		ProcessedResult res = null;
		if (command == Command.DRIVE)
		{
			// If command is drive, we do the search and return the first
			// matching result as a PoiSearchResult

			//res = OneShotPOIHandler.handlePoiQueryResult(recResult,
			//		context, poi, command);
			PoiQueryProcessedResult procResult = new PoiQueryProcessedResult(literal, confidence, command);
			
            AddressValidation validation = GeoCodeUtil.validatePoi(poi, context);
            if (validation.isSuccess()) {
                Stop stop = validation.getValidatedStop().get(0);
                procResult.setName(WordUtils.capitalize(poi));
                procResult.setCity(stop.city);
                procResult.setState(stop.state);
                procResult.setSearchLocation(stop);
                res = procResult;
                return res;
                
            } else {
                logger.warning("Can't validate poi : " + poi);
                return null;
            }

		}
		else
		{
			// If command is not drive, we don't do search and return a
			// PoiQuery

			Stop searchLocation = ProcessUtil.getStop(recResult, true, context);
			
			boolean searchNearby = true;
			if (city != null)
				searchNearby = false; // user spoke city, so do within city search
			
			List<TnPoi> searchResults = GeoCodeUtil.searchPoi(poi, searchLocation, context, searchNearby);
			if (searchResults != null && searchResults.size() > 0)
			{
				PoiSearchResultsProcessedResult poiRes = new PoiSearchResultsProcessedResult(
						literal, confidence, command);
				poiRes.setName(WordUtils.capitalize(poi));
				poiRes.setCity(city);
				poiRes.setState(state);

				
				poiRes.setSearchNearBy(searchNearby);
				if(searchNearby)
					poiRes.setAnchorPoint(context.location);
				else
					poiRes.setAnchorPoint(searchLocation);
				
				// only return the first result
				List<TnPoi> topResult = new ArrayList<TnPoi>();
				topResult.add(searchResults.get(0));
				poiRes.setSearchResults(topResult);
				
				poiRes.getAnchorPoint().firstLine = searchResults.get(0).getBrandName();
				
				res = poiRes;
			}
			else
				logger.warning("Can't validate poi : " + poi);
		}
		
		return res;
	}
	
	private PersonalizedProcessedResult getFavoriteResult(int favoriteId,
														long userId,
														String literal,
														double confidence,
														Command command,
														boolean useAddress)
	{
		IPersonalStop favorite = null;
		try
		{
			favorite = XNavClient.FAVORITES.fetchFavoritesById(
					userId, favoriteId, 2, context.tnContext.toContextString());
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE,
					"Favorites service exception while fetching favorite,userId="
					+ userId + ", favoriteId=" + favoriteId,e);
		}

		if (favorite == null)
		{
			logger.warning("can't fetchFavoritesById,userId=" + userId
					+ ", favoriteId=" + favoriteId);
			return null;
		}
		else
		{

			PersonalizedProcessedResult result = new PersonalizedProcessedResult(
					literal, confidence, command);
			result.setType(PersonalizedType.FAVORITE);
			result.setId(favoriteId);
			result.setLabel(favorite.getLabel());
			result.setUseAddress(useAddress);
			result.setLocation(ProcessUtil.getFavoriteStop(favorite));

			return result;
		}
	}

	void postProcess()
	{

		List<ProcessedResult> resultList = procObj.getProcessedResults();

		if (!resultList.isEmpty())
		{
			ProcessedResult last = resultList.get(resultList.size() - 1);
			resultList.clear();
			resultList.add(last);
			procObj.setProcessedResults(resultList);
		}
	}

}
