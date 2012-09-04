package com.telenav.cserver.backend.ace;

import com.telenav.datatypes.address.v20.Language;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: rapu
 * @date: 2009-5-19
 * @time: 16:25:05
 * TeleNav Inc.
 */
public final class LanguageUtils
{
    private static final Map<Language, String> LangNameMap = new HashMap<Language, String>();
    private static final Map<String, Language> NameLangMap = new HashMap<String, Language>();

    static
    {
        LangNameMap.put(Language.GERMAN, "GER");
        LangNameMap.put(Language.ENGLISH, "ENG");
        LangNameMap.put(Language.PORTUGUESE, "POR");
        LangNameMap.put(Language.FRENCH, "FRE");
        LangNameMap.put(Language.SPANISH, "SPA");
        LangNameMap.put(Language.CHINESE, "CHI");
        LangNameMap.put(Language.ITALIAN, "ITA");
        LangNameMap.put(Language.TAI, "TAI");
        LangNameMap.put(Language.DUTCH, "DUT");
        LangNameMap.put(Language.DANISH, "DAN");
        LangNameMap.put(Language.NORWEGEAN, "NOR");
        LangNameMap.put(Language.FINISH, "FIN");
        LangNameMap.put(Language.SWEDISH, "SWE");
        LangNameMap.put(Language.GREEK, "GRE");
        LangNameMap.put(Language.CATALAN, "CAT");
        LangNameMap.put(Language.BASQUE, "BAQ");
        LangNameMap.put(Language.GALLEGAN, "GLG");
        LangNameMap.put(Language.WELSH, "WEL");
        LangNameMap.put(Language.SLOVAK, "SLO");
        LangNameMap.put(Language.JAPANESE, "JPN");
        LangNameMap.put(Language.KOREAN, "KOR");


        NameLangMap.put("GER", Language.GERMAN);
        NameLangMap.put("DEU", Language.GERMAN);
        NameLangMap.put("ENG", Language.ENGLISH);
        NameLangMap.put("POR", Language.PORTUGUESE);
        NameLangMap.put("FRE", Language.FRENCH);
        NameLangMap.put("FRA", Language.FRENCH);
        NameLangMap.put("SPA", Language.SPANISH);
        NameLangMap.put("CHI", Language.CHINESE);
        NameLangMap.put("ITA", Language.ITALIAN);
        NameLangMap.put("TAI", Language.TAI);
        NameLangMap.put("DUT", Language.DUTCH);
        NameLangMap.put("DAN", Language.DANISH);
        NameLangMap.put("NOR", Language.NORWEGEAN);
        NameLangMap.put("FIN", Language.FINISH);
        NameLangMap.put("SWE", Language.SWEDISH);
        NameLangMap.put("GRE", Language.GREEK);
        NameLangMap.put("GRX", Language.GREEK);
        NameLangMap.put("GRT", Language.GREEK);
        NameLangMap.put("CAT", Language.CATALAN);
        NameLangMap.put("BAQ", Language.BASQUE);
        NameLangMap.put("GLG", Language.GALLEGAN);
        NameLangMap.put("WEL", Language.WELSH);
        NameLangMap.put("SLO", Language.SLOVAK);
        NameLangMap.put("SLX", Language.SLOVAK);
        NameLangMap.put("JPN", Language.JAPANESE);
        NameLangMap.put("KOR", Language.KOREAN);

        NameLangMap.put(Language._GERMAN, Language.GERMAN);
        NameLangMap.put(Language._ENGLISH, Language.ENGLISH);
        NameLangMap.put(Language._PORTUGUESE, Language.PORTUGUESE);
        NameLangMap.put(Language._FRENCH, Language.FRENCH);
        NameLangMap.put(Language._SPANISH, Language.SPANISH);
        NameLangMap.put(Language._CHINESE, Language.CHINESE);
        NameLangMap.put(Language._ITALIAN, Language.ITALIAN);
        NameLangMap.put(Language._TAI, Language.TAI);
        NameLangMap.put(Language._DUTCH, Language.DUTCH);
        NameLangMap.put(Language._DANISH, Language.DANISH);
        NameLangMap.put(Language._NORWEGEAN, Language.NORWEGEAN);
        NameLangMap.put(Language._FINISH, Language.FINISH);
        NameLangMap.put(Language._SWEDISH, Language.SWEDISH);
        NameLangMap.put(Language._GREEK, Language.GREEK);
        NameLangMap.put(Language._CATALAN, Language.CATALAN);
        NameLangMap.put(Language._BASQUE, Language.BASQUE);
        NameLangMap.put(Language._GALLEGAN, Language.GALLEGAN);
        NameLangMap.put(Language._WELSH, Language.WELSH);
        NameLangMap.put(Language._SLOVAK, Language.SLOVAK);
        NameLangMap.put(Language._JAPANESE, Language.JAPANESE);
        NameLangMap.put(Language._KOREAN, Language.KOREAN);
    }

    public static String getName(Language lang)
    {
        return LangNameMap.get(lang);
    }

    public static Language parse(String s)
    {
        if (null == s) return null;
        String c = s.toUpperCase();
        return NameLangMap.get(c);
    }
}
