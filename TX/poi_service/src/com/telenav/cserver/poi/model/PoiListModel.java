/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on 2009-8-31
 * File name: PoiListModel.java
 * Package name: com.telenav.cserver.poi.model
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 03:56:25 pm
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.poi.model;

import com.telenav.cserver.poi.struts.Constant;

/**
 * @author lwei (lwei@telenav.cn) 03:56:25 pm
 */
public class PoiListModel {
    private static final String INITIAL = "POI_LIST_MODEL_";
    private static final String MAX_PAGE_INDEX = INITIAL + "MAX_PAGE_INDEX";
    private static final String PAGE_INDEX = INITIAL + "PAGE_INDEX";
    private static final String PAGE_INDEX_TEMP = INITIAL + "PAGE_INDEX_TEMP";
    private static final String SEARCH_UID = INITIAL + "SEARCH_UID";
    private static final String SORT_TYPE = INITIAL + "SORT_TYPE";
    private static final String SORT_TYPE_TEMP = INITIAL + "SORT_TYPE_TEMP";
    private static final String KEY_WORD = INITIAL + "KEY_WORD";
    private static final String DISPLAY_WORD = INITIAL + "DISPLAY_WORD";
    private static final String AUDIO_STOPPED = INITIAL + "AUDIO_STOPPED";
    private static final String POI_COUNT = INITIAL + "POI_COUNT";
    private static final String MOST_POPULAR = INITIAL + "MOST_POPULAR";
    private static final String SPONSOR_LIST = INITIAL + "SPONSOR_LIST";
    private static final String SHOW_PROGRESS_BAR = INITIAL
            + "SHOW_PROGRESS_BAR";
    private static final String CATEGORY_ID = INITIAL + "CATEGORY_ID";
    private static final String WAIT_PROMPT_AUDIO_FINISH = INITIAL
            + "WAIT_PROMPT_AUDIO_FINISH";
    private static final String BACK_URL_WHEN_NO_FOUND = INITIAL
            + "BACK_URL_WHEN_NO_FOUND";
    private static final String INPUT_TYPE = INITIAL + "INPUT_TYPE";
    private static final String AUDIO_TYPE = INITIAL + "AUDIO_TYPE";
    private static final String SEARCH_TYPE = INITIAL + "SEARCH_TYPE";
    
    /** Search Type. */
    public static String getSearchType() {
        return getTempInt(SEARCH_TYPE,SearchType.SEARCH_ADDRESS);
    }

    public static String setSearchType(String searchType) {
        return setTempInt(SEARCH_TYPE, searchType);
    }
    
    /** Audio Type */
    public static String getAudioType() {
        return getTempInt(AUDIO_TYPE,AudioType.POI_TYPEIN);
    }

    public static String setAudioType(String AudioType) {
        return setTempInt(AUDIO_TYPE, AudioType);
    }
    
    /** TypeOrSpeak. */
    public static String getInputType() {
        return getTempStr(INPUT_TYPE,
                Constant.StorageKey.POI_MODULE_FROM_TYPE);
    }

    public static String setInputType(String inputType) {
        return setTempStr(INPUT_TYPE, inputType);
    }
    
    /** Max Page index. */
    public static String getMaxPageIndex() {
        return getTempInt(MAX_PAGE_INDEX, 0);
    }

    public static String setMaxPageIndex(String index) {
        return setTempInt(MAX_PAGE_INDEX, index);
    }

    /** Page index. */
    public static String getPageIndex() {
        return getTempInt(PAGE_INDEX, 0);
    }

    public static String setPageIndex(String index) {
        return setTempInt(PAGE_INDEX, index);
    }

    /** Sort type */
    public static String setSortType(String type) {
        return setTempInt(SORT_TYPE, type);
    }

    public static String getSortType() {
        return getTempInt(SORT_TYPE, Constant.SORT_BY_RELEVANCE);
    }

    public static String deleteSortType() {
        return delTempInt(SORT_TYPE);
    }

    /** Category id */
    public static String setCategoryId(String categoryId) {
        return setTempStr(CATEGORY_ID, categoryId);
    }

    public static String getCategoryId() {
        return getTempStr(CATEGORY_ID, "-1");
    }

    public static String deleteCategoryId() {
        return delTempStr(CATEGORY_ID);
    }

    /** Temporary sort type */
    public static String setSortTypeTemp(String type) {
        return setTempInt(SORT_TYPE_TEMP, type);
    }

    public static String getSortTypeTemp() {
        return getTempInt(SORT_TYPE_TEMP, Constant.SORT_BY_RELEVANCE);
    }

    public static String deleteSortTypeTemp() {
        return delTempInt(SORT_TYPE_TEMP);
    }

    /** Search key word. */
    public static String setKeyWord(String type) {
        return setTempStr(KEY_WORD, type);
    }

    public static String getKeyWord() {
        return getTempStr(KEY_WORD, "");
    }

    public static String deleteKeyWord() {
        return delTempStr(KEY_WORD);
    }

    /** Search display word. */
    public static String setDisplayWord(String type) {
        return setTempStr(DISPLAY_WORD, type);
    }

    public static String getDisplayWord() {
        return getTempStr(DISPLAY_WORD, "");
    }

    public static String deleteDisplayWord() {
        return delTempStr(DISPLAY_WORD);
    }	
	
    /** Audio stop flag */
    public static String setAudioStopped() {
        return setTempInt(AUDIO_STOPPED, "0");
    }

    public static String getAudioStopped() {
        return getTempInt(AUDIO_STOPPED, 1);
    }

    public static String deleteAudioStopped() {
        return delTempInt(AUDIO_STOPPED);
    }

    /** Temporary page index. */
    public static String setPageIndexTemp(String index) {
        return setTempStr(PAGE_INDEX_TEMP, index);
    }

    public static String getPageIndexTemp() {
        return getTempStr(PAGE_INDEX_TEMP, "0");
    }

    public static String deletePageIndexTemp() {
        return delTempStr(PAGE_INDEX_TEMP);
    }
    
    /** Search UID, currently it is timestamp */
    public static String setSearchUID(String index) {
    	return setTempStr(SEARCH_UID, index);
    }
    
    public static String getSearchUID() {
    	return getTempStr(SEARCH_UID, "0");
    }
    
    public static String deleteSearchUID() {
    	return delTempStr(SEARCH_UID);
    }

    /** POI count */
    public static String setPoiCount(String count) {
        return setTempInt(POI_COUNT, count);
    }

    public static String getPoiCount() {
        return getTempInt(POI_COUNT, 0);
    }

    /** Most popular. */
    public static String setMostPopular(String value) {
        return setTempStr(MOST_POPULAR, value);
    }

    public static String getMostPopular() {
        return getTempStr(MOST_POPULAR, "0");
    }

    public static String deleteMostPopular() {
        return delTempStr(MOST_POPULAR);
    }

    /** Sponsor POI list. */
    public static String setSponsorPoiList(String value) {
        return set(SPONSOR_LIST, value);
    }

    public static String getSponsorPoiList() {
        return getJSONArray(SPONSOR_LIST);
    }

    /** Set progress bar */
    public static String setShowProgressBar(String value) {
        return setTempInt(SHOW_PROGRESS_BAR, value);
    }

    public static String getShowProgressBar() {
        return getTempInt(SHOW_PROGRESS_BAR, 1);
    }

    public static String deleteShowProgressBar() {
        return delTempInt(SHOW_PROGRESS_BAR);
    }

    /** Wait prompt audio finish */
    public static String setWaitPromptAudioFinish(String value) {
        return setTempInt(WAIT_PROMPT_AUDIO_FINISH, value);
    }

    public static String getWaitPromptAudioFinish() {
        return getTempInt(WAIT_PROMPT_AUDIO_FINISH, 0);
    }

    public static String deleteWaitPromptAudioFinish() {
        return delTempInt(WAIT_PROMPT_AUDIO_FINISH);
    }

    /** Back url when no found. */
    public static String setBackURLWhenNoFound(String value) {
        return setTempStr(BACK_URL_WHEN_NO_FOUND, value);
    }

    public static String getBackURLWhenNoFound() {
        return getTempStr(BACK_URL_WHEN_NO_FOUND, "");
    }

    public static String deleteBackURLWhenNoFound() {
        return delTempStr(BACK_URL_WHEN_NO_FOUND);
    }

    /** Base */
    private static String delTempInt(String key) {
        return "TempCache.deleteInt(\"" + key + "\")";
    }

    private static String delTempStr(String key) {
        return "TempCache.deleteString(\"" + key + "\")";
    }

    private static String getTempInt(String key, int defaultValue) {
        return "TempCache.getInt(\"" + key + "\", " + defaultValue + ")";
    }

    private static String setTempInt(String key, String value) {
        return "TempCache.saveInt(\"" + key + "\"," + value + ")";
    }

    private static String getTempStr(String key, String defaultValue) {
        return "TempCache.getString(\"" + key + "\", \"" + defaultValue + "\")";
    }

    private static String setTempStr(String key, String value) {
        return "TempCache.saveString(\"" + key + "\"," + value + ")";
    }

    private static String set(String key, String value) {
        return "Cache.saveCookie(\"" + key + "\"," + value + ")";
    }

    private static String getJSONArray(String key) {
        return "Cache.getJSONArrayFromCookie(\"" + key + "\")";
    }
    
    public static class AudioType {
        public static final int POI_TYPEIN = 1;
        public static final int POI_SPEAKIN = 2;
        public static final int POI_TYPEIN_ALONG = 3;
        public static final int POI_SPEAKIN_ALONG = 4;
    }
    
    public static class SearchType {
        public static final int SEARCH_AROUND_ME = 0;
        public static final int SEARCH_RECENT_STOPS = 1;
        public static final int SEARCH_AIRPORT = 2;
        public static final int SEARCH_CITY = 3;
        public static final int SEARCH_ZIP = 4;
        public static final int SEARCH_ADDRESS = 5;
        public static final int SEARCH_WAYPOINTS = 6;
        public static final int SEARCH_ALONGROUTE = 7;
    }
}
