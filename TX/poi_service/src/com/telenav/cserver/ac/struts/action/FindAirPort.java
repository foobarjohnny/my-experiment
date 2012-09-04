package com.telenav.cserver.ac.struts.action;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.PoiBaseAction;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;

/**
 * Get airport list from csv file
 * @author chbzhang
 *
 */

public class FindAirPort extends PoiBaseAction {
	
    @SuppressWarnings("unchecked")
    private static Logger logger = Logger.getLogger(FindAirPort.class);
    private static HashMap cache = new HashMap();
    public static final double DEGREE_MULTIPLIER = 1.e5;
    
    private static final String FILE_SUFFIX = ".csv";

	private static final String UNDER_LINE_SEPARATOR = "_";

	//private static final String DEFAULT_PATH = "/text/top_airports.csv";
	
	private static final String PREFIX_PATH = "/text/top_airports";
	
    protected ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        String from = PoiUtil.filterLastPara(request.getParameter("from"));
        request.setAttribute("from", from);
//        TxNode respNode = new TxNode();
        
        DataHandler handler = (DataHandler) request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
        String region = handler.getClientInfo(DataHandler.KEY_REGION);
        
        if(null == region || "".equals(region)){
        	region = "NA";
        }

        TxNode respNode = (TxNode) cache.get("AirPortNode"+region);
        if(respNode == null){
            respNode = new TxNode();
            String fileName = getFilePath(region);
            if(!"".equals(fileName))
            {
            	String line = "";
                BufferedReader in;
                try {
                    in = new BufferedReader(new FileReader(fileName));
                    try {
                        int lineIndex = 0;
                        while((line = in.readLine()) != null){
                            lineIndex++;
                            if(1 == lineIndex) continue;
                            Stop airportStop = convertLineToStop(line);
                            String showMsg = airportStop.label;
                            if("".equals(showMsg)){
                                showMsg = airportStop.firstLine;
                            }else{
                                showMsg = airportStop.label + ": " + airportStop.firstLine;
                            }
                            respNode.addMsg(showMsg);
                            respNode.addChild(airportStop.toTxNode());
                        }
                    } catch (IOException e) {
                    	logger.error("IOException occured when get stream form file "+fileName);
                    	logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
						e.printStackTrace();
                    }
                } catch (FileNotFoundException e1) {
                	logger.error(fileName+" can not be found!!!");
                	logger.error("cause:"+e1.getCause()+",message:"+e1.getMessage());
					e1.printStackTrace();
                }
                
                if(null != respNode && respNode.childrenSize() != 0){
                    cache.put("AirPortNode"+region, respNode);
                }
            }
        }
        request.setAttribute("node", respNode);
        return mapping.findForward("success");
    }
    
    public static Stop convertLineToStop(String line)
    {
        String[] stopPropertys = line.split(",");
        Stop stop = new Stop();
        stop.lat = convertToDM5(Double.parseDouble(stopPropertys[1]));
        stop.lon = convertToDM5(Double.parseDouble(stopPropertys[2]));
        stop.isGeocoded = true;
        stop.city = stopPropertys[5];
        stop.state = stopPropertys[6];
        stop.country = stopPropertys[7];
        stop.zip = stopPropertys[8];
        stop.firstLine = stopPropertys[9];
        stop.label = stopPropertys[10];

        // ACE 4.0, firstLine is missing after sync with XNAV, so save the firstLine to streetName
        stop.setExtraProperty(Stop.KEY_STREETNAME, stopPropertys[9]);
        return stop;
    }
    
    public static int convertToDM5(double degree)
    {
        return (int) (degree * DEGREE_MULTIPLIER);
    }
    
    private String getFilePath(String region)
    {
    	String path = PREFIX_PATH+UNDER_LINE_SEPARATOR+region.toUpperCase()+FILE_SUFFIX;
    	URL url =  getClass().getResource(path);
    	if(url!=null)
    	{
    		return url.getPath();
    	}
    	return "";
    }
}
