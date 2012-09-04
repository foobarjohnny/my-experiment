 /**
     * (c) Copyright 2009 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.backend.datatypes.feedback;

import org.apache.axis2.databinding.utils.ConverterUtil;

/**
 * feedback request text answer type
 * @author zhjdou
 * 2009-11-5
 */
public class TextAnswerType
{
    private String textAnswerType;
    public static final String _MULTIPLE = ConverterUtil.convertToString("MULTIPLE");
    public static final String _SINGLE = ConverterUtil.convertToString("SINGLE");
    public static final String _TEXT = ConverterUtil.convertToString("TEXT");
    
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("TextAnswerType=[");
        sb.append("textAnswerType=").append(this.textAnswerType);
        sb.append("]");
        return sb.toString();
    }
    
    public TextAnswerType(String textAnswerType)
    {
        this.textAnswerType = textAnswerType;
    }
    
    public static TextAnswerType createTextAnswerType(String type) {
        return new TextAnswerType(type);
    }

    /**
     * @return the textAnswerType
     */
    public String getTextAnswerType()
    {
        return textAnswerType;
    }

    /**
     * @param textAnswerType the textAnswerType to set
     */
    public void setTextAnswerType(String textAnswerType)
    {
        this.textAnswerType = textAnswerType;
    }
}
