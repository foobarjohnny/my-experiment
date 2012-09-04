package com.telenav.cserver.framework.html.taglib;


import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.telenav.cserver.framework.html.util.HtmlCommonUtil;


/**
 * Prepend static resource host and version to static resource URL.
 * e.g. 
 * original url --  /login_startup_service/html/js/test.js
 * after -- http://staticResources.teleanav.com/1_0_08/login_startup_service/html/js/test.js
 * 
 * @author jianyuz
 *
 */
public class TnStaticResourceTag extends TagSupport{

	private static Logger logger=Logger.getLogger(TnStaticResourceTag.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StringBuilder outputUrl = new StringBuilder();
	private String pathPrefix;
	private String filename;
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getPathPrefix() {
		return pathPrefix;
	}

	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}

	public int doStartTag() throws JspException{
		return SKIP_BODY;
	}
	
	public int doEndTag() throws JspException{
		
		String hostUrlWithIp = (String) this.pageContext.getRequest().getAttribute("Host_url");
		String staticResourceServer = HtmlCommonUtil.getStaticResourceServerURL(hostUrlWithIp);

		if(!StringUtils.isEmpty(staticResourceServer)){
			this.outputUrl.append(staticResourceServer).append(HtmlCommonUtil.addVersionToStaticContentUrl(pathPrefix + filename));
		}else{
			this.outputUrl.append(pathPrefix + filename);
		}
		try {
			this.pageContext.getOut().print(this.outputUrl.toString());
		} catch (IOException e) {
			if (logger.isDebugEnabled()) {
				logger.error("TnStaticResourceTag.doEndTag", e);
			}
		}
		this.outputUrl.setLength(0); //clear the output url buffer.
		return EVAL_PAGE;
	}

}
