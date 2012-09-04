package com.telenav.cserver.ac.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.PoiAjaxAction;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.cserver.poi.datatypes.Stop;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.util.JsonUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;
import com.televigation.mapproxy.MapserverProxy;
import com.televigation.mapproxy.datatypes.mapserverstatus.RGCStatus;
import com.televigation.proxycommon.LatLonPoint;
import com.televigation.proxycommon.RGCAddress;

/**
 * Get information of location by lat and lon
 * RGC Action.
 * 
 * @author chbzhang
 * 
 */
public class GetCurrentLocationAction extends PoiAjaxAction {
    
    public final static String SERVICE_MAPSERVER = "MAPSERVER";
    
    @SuppressWarnings("null")
    public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response) {
        
        CliTransaction cli = new CliTransaction(CliTransaction.TYPE_MODULE);
        cli.setApplicationId(getCliApplicationID());
        cli.setFunctionName(request.getServletPath());
        try {
            DataHandler handler = (DataHandler) request
                    .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
            TxNode body = handler.getAJAXBody();
            String joString = body.msgAt(0);
            JSONObject jo = new JSONObject(joString);
            int slat = jo.getInt("lat");
            int slon = jo.getInt("lon");
            String label = JsonUtil.getString(jo, "label");
            boolean maitai = JsonUtil.getBoolean(jo, "maitai");
            double lat = PoiUtil.convertToDegree(slat);
            double lon = PoiUtil.convertToDegree(slon);
            LatLonPoint latLonPoint = new LatLonPoint(lat,lon);
            Stop currentLoc = new Stop();
            TnContext tc = PoiUtil.getTnContext(handler);
            String addressString = null;
            double radius = 0.2;
            
            cli.addData("input lat/lon:", "lat:" + lat + ",lon:" + lon);
            try
            {
                RGCStatus rgcStatus = queryRGC(latLonPoint, radius, tc);
                if(rgcStatus != null && rgcStatus.status == RGCStatus.S_OK 
                        && rgcStatus.addresses != null && rgcStatus.addresses.size() > 0)
                {
                    cli.addData("status", "RGC success");
                    RGCAddress rgcAddress = (RGCAddress)rgcStatus.addresses.get(0);
                    currentLoc.firstLine = rgcAddress.getFirstLine();
                    currentLoc.city = rgcAddress.getCity();
                    currentLoc.state = rgcAddress.getState();
                    currentLoc.country = rgcAddress.getCounty();
                    currentLoc.zip = rgcAddress.getZip();
                    currentLoc.lat = convertToDM5(rgcAddress.getLat());
                    currentLoc.lon = convertToDM5(rgcAddress.getLon());
                    addressString = convertAddressToJSONObject(rgcAddress, label, maitai).toString();
                }
                else
                {
                    cli.addData("status", "RGC fail");
                    if(rgcStatus!=null)
                    {
                        cli.addData("errorMsg", rgcStatus.errorMsg);
                    }
                    currentLoc = new Stop();
                    currentLoc.firstLine = "";
                    currentLoc.city = "";
                    currentLoc.state = "";
                    currentLoc.zip = "";
                    currentLoc.lat = slat;
                    currentLoc.lon = slon;
                    currentLoc.isGeocoded = true;
                    currentLoc.country = "";
                    currentLoc.label = "unknow";
                }
                
            }
            catch(Exception e)
            {
            }
            
            TxNode stopNode = currentLoc.toTxNode();

            TxNode node = new TxNode();
            node.addMsg("stop");
            node.addChild(stopNode);
            if (addressString != null) {
                node.addMsg(addressString);
            }

            request.setAttribute("node", node);
            return mapping.findForward("success");
        } catch (Exception e) {
            cli.setStatus(e);
            cli.setState(CliTransaction.STATUS_FAIL);
            return mapping.findForward("failure");
        }
        finally {
            cli.complete();
        }
    }
    
    public static RGCStatus queryRGC(LatLonPoint latLonPoint, double radius, TnContext tnContext)throws ThrottlingException
    {
        boolean startAPICall = false;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_MAPSERVER, tnContext);
            if(!startAPICall)
            {
                throw new ThrottlingException();
            }
            RGCStatus rgcStatus = new MapserverProxy(tnContext, null, null).queryRGC(latLonPoint, radius);
            return rgcStatus;
        }
        finally
        {
            if(startAPICall)
            {
                ThrottlingManager.endAPICall(SERVICE_MAPSERVER, tnContext);
            }
        }
    }

    public static JSONObject convertAddressToJSONObject(RGCAddress addr, String label, boolean maitai)
            throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("firstLine", convertNull(addr.getFirstLine()));
        
        if(maitai) {
            jo.put("label", convertNull(label));
        } else {
            jo.put("label", convertNull(addr.getLabel()));
        }
        jo.put("city", convertNull(addr.getCity()));
        jo.put("state", convertNull(addr.getState()));
        jo.put("zip", convertNull(addr.getZip()));
        jo.put("lat", convertToDM5(addr.getLat()));
        jo.put("lon", convertToDM5(addr.getLon()));
        jo.put("country", convertNull(addr.getCountry().getISO2Code()));
        jo.put("isGeocoded", 1);
        jo.put("type", 1);
        return jo;
    }

    private static String convertNull(String s) {
        if (null == s) {
            s = "";
        }
        return s;
    }

    public static final double DEGREE_MULTIPLIER = 1.e5;

    public static int convertToDM5(double degree) {
        return (int) (degree * DEGREE_MULTIPLIER);
    }
}
