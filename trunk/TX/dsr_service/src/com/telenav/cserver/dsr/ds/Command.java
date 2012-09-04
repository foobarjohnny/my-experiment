package com.telenav.cserver.dsr.ds;

import java.util.logging.Logger;

import com.telenav.cserver.dsr.util.ProcessUtil;
import com.telenav.cserver.dsr.util.ResourceConst;

public enum Command
{
	  DRIVE,
	  DRIVE_HOME,
	  DRIVE_WORK,
	  SEARCH,
	  MAP,
	  MAP_HOME,
	  MAP_WORK,
	  SHOW_WEATHER,
	  SHOW_TRAFFIC,
	  SHOW_MOVIES,
	  SHOW_COMMUTE,
	  RESUME;

	  
	private static Logger logger = Logger.getLogger(ProcessUtil.class.getName()) ;
	public static Command parseCommand(String cmd, String target)
	{
			if (cmd == null)
				return null;
			
			if (cmd.equals(ResourceConst.DSR_COMMAND_SEARCH))
				return SEARCH;
			else if (cmd.equals(ResourceConst.DSR_COMMAND_DRIVE))
			{
				if (ResourceConst.DSR_TARGET_HOME.equals(target))
					return DRIVE_HOME;
				else if (ResourceConst.DSR_TARGET_WORK.equals(target))
					return DRIVE_WORK;
				else
					return DRIVE;
			}
			else if (cmd.equals(ResourceConst.DSR_COMMAND_RESUME))
				return RESUME;
			else if (cmd.equals(ResourceConst.DSR_COMMAND_MAP)){
				if (ResourceConst.DSR_TARGET_HOME.equals(target))
					return MAP_HOME;
				else if (ResourceConst.DSR_TARGET_WORK.equals(target))
					return MAP_WORK;
				else
					return MAP;
			}
				
			else if (cmd.equals(ResourceConst.DSR_COMMAND_SHOW))
			{
				if (ResourceConst.DSR_TARGET_WEATHER.equals(target))
					return SHOW_WEATHER;
				else if (ResourceConst.DSR_TARGET_TRAFFIC.equals(target))
					return SHOW_TRAFFIC;
				else if (ResourceConst.DSR_TARGET_MOVIES.equals(target))
					return SHOW_MOVIES;
				else if (ResourceConst.DSR_TARGET_COMMUTE.equals(target))
					return SHOW_COMMUTE; 
				else
					return null;
			}
			else
			{
				logger.warning("Couldn't identify command: "+cmd + ", "+target);
				return null ;
			}
		}
}
