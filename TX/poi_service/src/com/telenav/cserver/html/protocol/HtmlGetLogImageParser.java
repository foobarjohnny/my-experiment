/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.framework.html.util.HtmlFeatureHelper;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.html.executor.HtmlGetLogImageRequest;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.cserver.util.WebServiceConfigurator;
/**
 * @TODO	Define executor type
 * 			Parse the browser request
 * @author  jhjin@telenav.cn
 * @version 1.0 Feb 22, 2011
 */
public class HtmlGetLogImageParser extends HtmlProtocolRequestParser
{

    @Override
    public String getExecutorType()
    {
        return "getLogImage";
    }

    @Override
    public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest) throws Exception
    {
    	HtmlClientInfo clientInfo = (HtmlClientInfo)httpRequest.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
    	String theme = HtmlFeatureHelper.getInstance().getFeature(clientInfo, HtmlFrameworkConstants.FEATURE.POI_LOGO_THEME);
        HtmlGetLogImageRequest getLogImageRequest = new HtmlGetLogImageRequest();
        String jsonString = httpRequest.getParameter("jsonStr");
        JSONObject json = new JSONObject(jsonString);
        String operateType = httpRequest.getParameter("operateType");
        getLogImageRequest.setImageName(json.getString("imageName"));
        getLogImageRequest.setOperateType(operateType);
        getLogImageRequest.setTheme(theme);
        
//        if(HtmlConstants.OPERATE_FETCH_IMAGE.equalsIgnoreCase(operateType))
//        {
//        	String width = json.getString("width");
//        	String height = json.getString("height");
//            String center = json.getString("center");
//            String markers = json.getString("markers"); 
//            //http://mapapi.telenav.com/maps/staticmap?width=460&height=270&zoom=1&center=37.3882,-121.98374&markers=color:red|label:A|37.3882,-121.98374&apiKey=AQAAAS8nsECof/////////8AAAABAAAAAQEAAAAQn6ZL7khhwwoabLSN9d0PSwEAAAAOAwAAAGcAAACSAAAAAQA=
//            //String apiKey = "AQAAAS+jF+1of/////////8AAAABAAAAAQEAAAAQZUmvC1JKzdtXA+1AkO2ZqwEAAAAOAwAAAGsAAACYAAAAAgA=";
//            String apiKey = WebServiceConfigurator.getStaticMapKey();
//            String mapImageUrl = WebServiceConfigurator.getUrlOfStaticMap() + "?width=" + width + "&height=" + height + "&zoom=1&center=" + center + "&markers=" + URLEncoder.encode(markers, "utf-8")+ "&apiKey=" + URLEncoder.encode(apiKey, "utf-8");
//            getLogImageRequest.setMapImageUrl(mapImageUrl);
//        }
//        else
//        {
	        getLogImageRequest.setWidth(json.getString("width"));
	        getLogImageRequest.setHeight(json.getString("height"));
//        }
        return getLogImageRequest;
    }

}
