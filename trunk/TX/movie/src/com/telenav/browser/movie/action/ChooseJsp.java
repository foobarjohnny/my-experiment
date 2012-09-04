package com.telenav.browser.movie.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.browser.MovieBaseAction;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.browser.movie.Constant;
import com.telenav.browser.movie.Util;

public class ChooseJsp extends MovieBaseAction {
	private static Logger logger = Logger.getLogger(ChooseJsp.class);
	public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
    	String value = request.getParameter("jsp").split(";")[0];

    	DataHandler handler = (DataHandler) request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);

    	// get values for current location lat and lon, and also distUnit
    	TxNode node = (TxNode) handler.getParameter("anchorLat");
    	long anchorLat = node!=null? node.valueAt(0) : -1;
	    
    	node = (TxNode) handler.getParameter("anchorLon");
    	long anchorLon = node!=null? node.valueAt(0) : -1;
	    
    	node = (TxNode) handler.getParameter("distUnit");
    	long distUnit = node!=null? node.valueAt(0) : 1;

    	logger.debug("Lat:" + anchorLat + " Lon:" + anchorLon + "Dist Unit:" + distUnit);

    	// convert distUnit to mi or km
    	String scale = "mi";
    	if (distUnit != Constant.DUNIT_MILES) {
    		scale = "km";
    	}

    	// set attributes for anchor lat, lon, distUnit and scale
    	request.setAttribute("anchorLat", anchorLat);
    	request.setAttribute("anchorLon", anchorLon);
    	request.setAttribute("distUnit",  distUnit);
    	request.setAttribute("scale",     scale);

		if ("MovieList".equals(value)) {
			TxNode gpsNode = (TxNode) handler.getParameter("itemsOnScreen");
			if (gpsNode != null) {
				int itemsOnScreen = (int) gpsNode.valueAt(0);
				logger.debug("Items on screen:" + itemsOnScreen);
				request.setAttribute("itemsOnScreen", itemsOnScreen);
			}

			String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
			String version = handler.getClientInfo(DataHandler.KEY_VERSION);
			if("Android".equalsIgnoreCase(platform)){
				value = "MovieListAndroid";
			}
			if (version != null && (version.startsWith("6.2") || version.startsWith("6.3"))) {
				value = "MovieListAndroid";
			}
			if (Util.isRimNonTouch(handler)) {
				value = "MovieListRimNonTouch";

			}
        }

        return mapping.findForward(value);
    }
}
