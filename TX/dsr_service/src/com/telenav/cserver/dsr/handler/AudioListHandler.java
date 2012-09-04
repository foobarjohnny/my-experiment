/**
 * 
 */
package com.telenav.cserver.dsr.handler;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.telenav.cserver.dsr.util.ResourceConst;
import com.telenav.cserver.dsr.util.TTSAudioUtil;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.server.ServerSetting;
import com.telenav.resource.data.PromptItem;

/**
 * @author joses
 *
 * 
 */
public class AudioListHandler implements IResultsHandler {

	private static final Logger logger = Logger.getLogger(AudioListHandler.class.getName());
	public ProcessObject process(ProcessObject procObj) {

		int recType = procObj.context.recType;
		if(ServerSetting.TTSEnable
				&& ResourceConst.DSR_RECOGNIZE_NUMBER != recType
				&& ResourceConst.DSR_RECOGNIZE_EMAIL_COMMAND != recType
				&& recType < ResourceConst.DSR_RECOGNIZE_TICKET){
			
			logger.log(Level.FINE, "Get audio for DSR results.");
			List<PromptItem[]> audioList = TTSAudioUtil.getAudio(procObj);
			procObj.setPromptItems(audioList);
		}
		return procObj;
	}

}
