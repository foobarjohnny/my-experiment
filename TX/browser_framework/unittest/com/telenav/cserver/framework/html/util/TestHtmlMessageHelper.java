package com.telenav.cserver.framework.html.util;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.mock.MockHttpServletRequest;

import com.telenav.cserver.browser.util.MessageHelper;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.datatype.HtmlDeviceConfig;
import com.telenav.cserver.framework.html.datatype.HtmlMessageWrap;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;

import junit.framework.TestCase;

public class TestHtmlMessageHelper extends TestCase {
	
	HtmlMessageHelper mh = null;
	HtmlClientInfo clientInfo = new HtmlClientInfo();
	
	public void setUp()
	{
        clientInfo.setProgramCode("default");
        clientInfo.setCarrier("default");
        clientInfo.setPlatform("default");
        clientInfo.setVersion("7_0");
        clientInfo.setProduct("default");
        clientInfo.setDevice("default");
        
        clientInfo.setWidth("480");
        clientInfo.setHeight("800");
        clientInfo.setLocale("es_MX");
	}
	
	public void testInitial()
	{	
    	HtmlMessageHelper.getInstance().initMessage(clientInfo);
        HtmlDeviceConfig config = HtmlDeviceManager.getInstance().getDeviceConfig(clientInfo);
        
        HtmlMessageWrap msg = HtmlMessageHelper.getInstance().getMessageWrap(config.getMessageI18NKey());
        System.out.println(msg.getValue("poidetail.tab.reviews"));
	}

}
