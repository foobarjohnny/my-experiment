/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.dsr.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.PoiBaseAction;
import com.telenav.cserver.dsr.struts.util.AudioConstants;
import com.telenav.cserver.dsr.struts.util.AudioUtil;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author pzhang
 * 
 * @version 1.0, 2009-4-28
 */
public class RunDSRAction extends PoiBaseAction {
    public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest httpRequest, HttpServletResponse response)
            throws Exception {
        try {
            // get action
            String action = PoiUtil.getString(httpRequest
                    .getParameter("action"));
            action = PoiUtil.filterLastPara(action);
            DataHandler handler = new DataHandler(httpRequest);
            String path = action;
            String screenType = getScreenType(action);
            
            // Speak Search1
            if ("SpeakSearch1".equals(action)) {
                // prepare the audio
                TxNode audio1 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_SPEAKSEARCH_AUDIO1);
                TxNode audio2 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_SPEAKSEARCH_AUDIO2);
                TxNode audio3 = AudioUtil
                .getAuidoById(AudioConstants.STATIC_AUDIO_SPEAKSEARCH_AUDIO3);
                TxNode audio4 = AudioUtil
                .getAuidoById(AudioConstants.STATIC_AUDIO_SPEAKSEARCH_AUDIO4);
        
                handler.setParameter("audio1", audio1);
                handler.setParameter("audio2", audio2);
                handler.setParameter("audio3", audio3);
                handler.setParameter("audio4", audio4);
            }

            // Speak Search1
            if ("SpeakSearchAlong1".equals(action)) {
                // prepare the audio
                TxNode audio1 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_SPEAKALONGSEARCH_AUDIO1);
                TxNode audio2 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_SPEAKALONGSEARCH_AUDIO2);
                TxNode audio3 = AudioUtil
                .getAuidoById(AudioConstants.STATIC_AUDIO_SPEAKALONGSEARCH_AUDIO3);
                TxNode audio4 = AudioUtil
                .getAuidoById(AudioConstants.STATIC_AUDIO_SPEAKALONGSEARCH_AUDIO4);
        
                handler.setParameter("audio1", audio1);
                handler.setParameter("audio2", audio2);
                handler.setParameter("audio3", audio3);
                handler.setParameter("audio4", audio4);
            }
            
            // Speak City1
            if ("SpeakCity1".equals(action)) {
                // prepare the audio
                TxNode audio1 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_CITY_AUDIO1);
                TxNode audio2 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_CITY_AUDIO2);
                TxNode audio3 = AudioUtil
                .getAuidoById(AudioConstants.STATIC_AUDIO_CITY_AUDIO3);
                TxNode audio4 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_CITY_AUDIO4);
                
                handler.setParameter("audio1", audio1);
                handler.setParameter("audio2", audio2);
                handler.setParameter("audio3", audio3);
                handler.setParameter("audio4", audio4);
                //
                //httpRequest.setAttribute("isRetry", "N");
                //
            }
            
            
            // Speak Airport1
            if ("SpeakAirport1".equals(action)) {
                // prepare the audio
                TxNode audio1 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_AIRPORT_AUDIO1);
                TxNode audio2 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_AIRPORT_AUDIO2);
                TxNode audio3 = AudioUtil
                .getAuidoById(AudioConstants.STATIC_AUDIO_AIRPORT_AUDIO3);
                TxNode audio4 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_AIRPORT_AUDIO4);
                
                handler.setParameter("audio1", audio1);
                handler.setParameter("audio2", audio2);
                handler.setParameter("audio3", audio3);
                handler.setParameter("audio4", audio4);
                //
                //httpRequest.setAttribute("isRetry", "N");
                //
            }
            
            // Speak Address1
            if ("SpeakAddress1".equals(action)) {
                // prepare the audio
                TxNode audio1 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_ADDRESS_AUDIO1);
                TxNode audio2 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_ADDRESS_AUDIO2);
                TxNode audio3 = AudioUtil
                .getAuidoById(AudioConstants.STATIC_AUDIO_ADDRESS_AUDIO3);
                TxNode audio4 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_ADDRESS_AUDIO4);
                
                handler.setParameter("audio1", audio1);
                handler.setParameter("audio2", audio2);
                handler.setParameter("audio3", audio3);
                handler.setParameter("audio4", audio4);
                //
                //httpRequest.setAttribute("isRetry", "N");
                //
            }
            
            
            // Speak Intersection1
            if ("SpeakIntersection1".equals(action)) {
                // prepare the audio
                TxNode audio1 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_INTERSECTION_AUDIO1);
                TxNode audio2 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_INTERSECTION_AUDIO2);
                TxNode audio3 = AudioUtil
                .getAuidoById(AudioConstants.STATIC_AUDIO_INTERSECTION_AUDIO3);
                TxNode audio4 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_INTERSECTION_AUDIO4);
                
                handler.setParameter("audio1", audio1);
                handler.setParameter("audio2", audio2);
                handler.setParameter("audio3", audio3);
                handler.setParameter("audio4", audio4);
                //
                //httpRequest.setAttribute("isRetry", "N");
            }

            // Speak Command1
            if ("SpeakCommand1".equals(action)) {
                // prepare the audio
                TxNode audio1 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_COMMAND_AUDIO1);
                TxNode audio2 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_COMMAND_AUDIO2);
                TxNode audio3 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_COMMAND_AUDIO3);
                TxNode audio4 = AudioUtil
                        .getAuidoById(AudioConstants.STATIC_AUDIO_COMMAND_AUDIO4);
                TxNode audio5 = AudioUtil
						.getAuidoById(AudioConstants.STATIC_AUDIO_COMMAND_AUDIO5);
                TxNode audio6 = AudioUtil
						.getAuidoById(AudioConstants.STATIC_AUDIO_COMMAND_AUDIO6);
                
                if(PoiUtil.isTouchScreen(handler))
                {
                	audio2 = audio1;
                	audio3 = audio4;
                }
                handler.setParameter("audio1", audio1);
                handler.setParameter("audio2", audio2);
                handler.setParameter("audio3", audio3);
                handler.setParameter("audio4", audio4);
                handler.setParameter("audio5", audio5);
                handler.setParameter("audio6", audio6);
                //
                //httpRequest.setAttribute("isRetry", "N");
            }
            // Speak Search3
            if ("SpeakFlow3".equals(action)) {
                // prepare playaudio
                //TxNode audio7 = AudioUtil
                //        .getAuidoById(AudioConstants.STATIC_AUDIO_COMMON_NOMATCH);
                TxNode audio8 = new TxNode();
                //TxNode audio9 = AudioUtil
                //        .getAuidoById(AudioConstants.STATIC_AUDIO_COMMON_NUMBER1);
                //handler.setParameter("audio7", audio7);
                if(PoiUtil.isTouchScreen(handler))
                {
                	audio8 = AudioUtil
                    .getAuidoById(AudioConstants.STATIC_AUDIO_COMMON_MULTIMATCH1);
                }
                else
                {
                	audio8 = AudioUtil
                    .getAuidoById(AudioConstants.STATIC_AUDIO_COMMON_MULTIMATCH);
                }
                
                handler.setParameter("audio8", audio8);
                //handler.setParameter("audio9", audio9);
            }
            
            TxNode audioMaxSpeech = AudioUtil
            .getAuidoById(AudioConstants.STATIC_AUDIO_ENDING_AUDIO1);
    
            handler.setParameter("audioMaxSpeech", audioMaxSpeech);
            
            //set DSR type base on screenType
            httpRequest.setAttribute("DSRType", this.getDSRType(screenType));
            httpRequest.setAttribute("screenType", screenType);
            //
            httpRequest.setAttribute("DataHandler", handler);
            return mapping.findForward(path);
        } catch (Exception e) {
            return mapping.findForward("Globe_Exception");
        } 
    }
    
    /**
     * SpeakCity1->City
     * SpeakAddress1->Address
     * SpeakAirport1->Airport
     * SpeakIntersection1->Intersection
     * SpeakSearch1->Search
     * @param action
     * @return
     */
    private String getScreenType(String action)
    {
        String screenType = "";
        
        if(action.startsWith(AudioConstants.SCREEN_TYPE_INITIAL))
        {
            screenType = action.substring(AudioConstants.SCREEN_TYPE_INITIAL.length(), action.length()-1);
        }
        
        return screenType;
    }
    
    /**
     * 
     * @param screenType
     * @return
     */
    private String getDSRType(String screenType)
    {
        String DSRType = "";
        if(AudioConstants.SCREEN_TYPE_SEARCH.equals(screenType))
        {
            DSRType = String.valueOf(AudioConstants.DSR_RECOGNIZE_POI);
        }
        else if(AudioConstants.SCREEN_TYPE_CITY.equals(screenType))
        {
            DSRType = String.valueOf(AudioConstants.DSR_RECOGNIZE_CITY_STATE);
        }       
        else if(AudioConstants.SCREEN_TYPE_AIRPORT.equals(screenType))
        {
            DSRType = String.valueOf(AudioConstants.DSR_RECOGNIZE_AIRPORT);
        }  
        else if(AudioConstants.SCREEN_TYPE_ADDRESS.equals(screenType))
        {
            DSRType = String.valueOf(AudioConstants.DSR_RECOGNIZE_ONE_SHOT_ADDRESS);
        }       
        else if(AudioConstants.SCREEN_TYPE_INTERSECTION.equals(screenType))
        {
            DSRType = String.valueOf(AudioConstants.DSR_RECOGNIZE_ONE_SHOT_INTERSECTION);
        }  
        else if(AudioConstants.SCREEN_TYPE_COMMAND.equals(screenType))
        {
            DSRType = String.valueOf(AudioConstants.DSR_RECOGNIZE_COMMAND_CONTROL);
        }  
        return DSRType;
    }
}
