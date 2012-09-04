package com.telenav.cserver.ugc.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.PoiBaseAction;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.util.TnUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * 2009.06.04 To add poi jsp
 * @author chbzhang
 *
 */

public class SearchPOIBrandNames  extends PoiBaseAction {
	
	private Logger logger = Logger.getLogger(SearchPOIBrandNames.class);
	
    @SuppressWarnings("unchecked")
    @Override
    protected ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response){
        String value = request.getParameter("jsp").split(";")[0];
        try
        {
//          Get business name list from database to show the auto-completion menu when user enters the alphabets,
        	DataHandler handler = (DataHandler) request
	        .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
			TnContext tncontext = PoiUtil.getTnContext(handler);
            PoiFinderManager poiFinderManager = new PoiFinderManager();
    		String requestCountry = setCountryString(handler);
            List<String>  brandNames  = poiFinderManager.searchPOIBrandNames(requestCountry, 100,tncontext);
            TxNode poiBrandNamesNode = new TxNode();
            TxNode childNode = new TxNode();
            if ((brandNames != null) && (brandNames.size() > 0))
            {   
                for (int i = 0; i < brandNames.size(); i++)
                {
                    String brand = (String)brandNames.get(i);             
                    if (brand != null)
                    {
                        TxNode node = new TxNode();
                        node.addMsg(brand.toUpperCase());
                        childNode.addMsg(brand.toUpperCase());
                    }               
                }           
            }
            poiBrandNamesNode.addChild(childNode);
            request.setAttribute("node", poiBrandNamesNode);
            return mapping.findForward(value);
        }
        catch(Exception e)
        {
        	if(e instanceof ThrottlingException){
        		logger.error("failed to fetch poibrand names from XNAV!!!");
        	}
            e.printStackTrace();
            return mapping.findForward(value);
        } 
    }
    
	public static String setCountryString(DataHandler handler)
	{
		String countryString = "USA"; //defaut is USA
		if(TnUtil.isCanadianCarrier(handler))
		{
			countryString = "CA";
		}
		return countryString;
	}

}
