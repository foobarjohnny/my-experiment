
/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.framework.html.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.telenav.cserver.framework.html.datatype.HtmlMessageWrap;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.framework.html.util.HtmlMessageHelper;

/**
 * HTML Message tag used to retrieve message string based on message key.
 * 
 * @author pzhang
 *
 * @version 1.0, 2009-5-19
 */
public class HtmlMsgTag extends BodyTagSupport{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public StringBuilder outputText = new StringBuilder();
	private String key = "";
	
    /**
     * 
     */
    public int doStartTag() throws JspException
    {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String msgKey = (String)request.getAttribute(HtmlFrameworkConstants.HTML_MESSAGE_KEY); 
    	HtmlMessageWrap msg = HtmlMessageHelper.getInstance().getMessageWrap(msgKey);
    	String value = msg.get(this.getKey());
    	//outputText.append(value);
        try {
            pageContext.getOut().println(value);
        } catch (IOException e) {
        }
        return EVAL_BODY_INCLUDE;
    }
    

    
    public int doEndTag() throws JspException 
    {   
        this.clean();
        return EVAL_PAGE;
    }
    
    public int doAfterBody() throws JspException
    {
        return SKIP_BODY;
    }    
    
    public void clean()
    {
        outputText = new StringBuilder();
    }



	public String getKey() {
		return key;
	}



	public void setKey(String key) {
		this.key = key;
	}
}
