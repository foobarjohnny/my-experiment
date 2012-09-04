 /**
     * (c) Copyright 2009 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.backend.datatypes.feedback;

import org.apache.axis2.databinding.utils.ConverterUtil;

/**
 * media type
 * @author zhjdou
 * 2009-11-5
 */
public class MediumType
{   
    private String mediumType;
    public static final String _AUDIO = ConverterUtil.convertToString("AUDIO");
    public static final String _IMAGE = ConverterUtil.convertToString("IMAGE");
    public static final String _TEXT = ConverterUtil.convertToString("TEXT");
    
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("TextAnswerType=[");
        sb.append("textAnswerType=").append(this.mediumType);
        sb.append("]");
        return sb.toString();
    }
    
    public MediumType(String mediumType)
    {
        this.mediumType = mediumType;
    }
    
    public static MediumType createMediumType(String type) {
        return new MediumType(type);
    }

    /**
     * @return the mediumType
     */
    public String getMediumType()
    {
        return mediumType;
    }

    /**
     * @param mediumType the mediumType to set
     */
    public void setMediumType(String mediumType)
    {
        this.mediumType = mediumType;
    }
    
}
