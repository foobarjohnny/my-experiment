package com.telenav.cserver.misc.struts.action;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import com.telenav.cserver.PoiBaseAction;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.util.WebServiceConfigurator;
import com.telenav.datatypes.traffic.common.v10.AlertDTO;
import com.telenav.datatypes.traffic.common.v10.AreaTrafficReportDTO;
import com.telenav.datatypes.traffic.common.v10.AreaTrafficRequestDTO;
import com.telenav.datatypes.traffic.common.v10.AreaTrafficResponseDTO;
import com.telenav.datatypes.traffic.common.v10.BoundingBoxDTO;
import com.telenav.datatypes.traffic.common.v10.FilterDTO;
import com.telenav.datatypes.traffic.common.v10.FlowDTO;
import com.telenav.datatypes.traffic.common.v10.LatLonPointDTO;
import com.telenav.datatypes.traffic.common.v10.SeverityTypeDTO;
import com.telenav.datatypes.traffic.common.v10.TrafficDataReportDTO;
import com.telenav.datatypes.traffic.common.v10.TrafficDataRequestDTO;
import com.telenav.datatypes.traffic.common.v10.TrafficDataResponseDTO;
import com.telenav.datatypes.traffic.common.v10.TrafficFlowsReportDTO;
import com.telenav.datatypes.traffic.common.v10.TrafficFlowsRequestDTO;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.traffic.v10.TrafficServiceStub;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.xnav.traffic.filters.ContentFilter;
import com.telenav.xnav.traffic.filters.FilterFactory;

import edu.emory.mathcs.backport.java.util.Collections;

public class TrafficIncidentsAction extends PoiBaseAction
{
    private static Logger logger = Logger.getLogger(TrafficIncidentsAction.class);

    public ActionForward doAction(ActionMapping mapping,
                                  HttpServletRequest request,
                                  HttpServletResponse response)
            throws Exception
    {
        DataHandler handler = (DataHandler) request
                .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);

         // get values for current location lat and lon, and also distUnit
         TxNode node = (TxNode) handler.getParameter("locationJO");
         logger.info("locationJO node=" + node);
         String locationJOStr = node.msgAt(0);
         
         JSONObject locationJo = new JSONObject(locationJOStr);
         //locationJo.put("lat", anchorLat);
         //locationJo.put("lon", anchorLon);

         logger.info("anchorLat=" + locationJo.optInt("lat"));
         logger.info("anchorLon=" + locationJo.optInt("lon"));
         
         // TODO remove this
         //locationJo.put("lat", 3737289);
         //locationJo.put("lon", -12203819);

        try
        {
            AlertDTO[] alertDTOs = queryTrafficIncidents(handler, locationJo, 10);
            if (alertDTOs == null)
            {
                alertDTOs = new AlertDTO[0]; 
            }
                        
            request.setAttribute("trafficAlerts", alertDTOs);
            
            // first do sort by distance on alertdtos array
            //sortAlerts(alertDTOs);
            AlertDTO[] sortedAlertDTOs = null;
            try
            {
                sortedAlertDTOs = sortAlerts(alertDTOs, locationJo);
            }
            catch(Exception ex1)
            {
                logger.error("TrafficIncidentsAction: exception=" + ex1.getMessage());
            }
            //
            // group by location
            //
            
            // get flow data
            logger.info("*************************** sortedAlertDTOs length" + sortedAlertDTOs.length);
            HashMap<String,FlowDTO> flowHash = queryTrafficFlows(handler, sortedAlertDTOs);
            request.setAttribute("trafficFlows", flowHash);
           
            // convert to json data
            JSONObject json = convertAlertDTOToJson(sortedAlertDTOs, flowHash, locationJo);
            logger.info("*************************** out json" + json.toString());
            //json = convertAlertDTOToJson(alertDTOs, locationJo);
            //logger.info("*************************** out json" + json.toString());
            
            request.setAttribute("trafficAlertsJson", json);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            logger.error("TrafficIncidentsAction: exception=" + ex.getMessage());
            request.setAttribute("ErrorMessage", "COSE.notAvailable");
            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

    private static AlertDTO[] sortAlerts(AlertDTO[] alertDTOs,
                                         JSONObject locationJo)
            throws JSONException
    {
        if(alertDTOs == null)
        {
            return null;
        }
        
        int centerLat = locationJo.getInt("lat");
        int centerLon = locationJo.getInt("lon");

        // convert AlertDTO to TrafficAlert
        ArrayList<TrafficAlert> arrList1 = new ArrayList<TrafficAlert>();
        for(AlertDTO alertDTO : alertDTOs)
        {
            arrList1.add(new TrafficAlert(centerLat, centerLon, alertDTO));
        }
                
        Collections.sort(arrList1, new AlertsDTOComparator());

        AlertDTO[] retAlertDTOs = new AlertDTO[arrList1.size()];
        for(int i = 0; i < arrList1.size(); i++)
        {
            retAlertDTOs[i] = (AlertDTO) ((TrafficAlert)arrList1.get(i)).getAlertDTO();
        }
        
        return retAlertDTOs;
    }
    
    // locationJo is passed to find the distance and put it in 
    // alertDTOs and flowDTOs array values match 1-1 
    // flowDTOs - can be null through one of the flows
    private static JSONObject convertAlertDTOToJson(AlertDTO[] alertDTOs, HashMap<String, FlowDTO> flowHash, JSONObject locationJo)
            throws JSONException
    {
        int centerLat = locationJo.getInt("lat");
        int centerLon = locationJo.getInt("lon");
        
        // Hashmap has key as location (e.g. "101", "Lawrence"), and value as ArrayList, where
        // each element in array is of type AlertDTO
        //
        // sortedLoc is an array list that has each element as string for location name. one location
        // name appears only one time in the array
        HashMap<String,ArrayList<AlertDTO>> hm1 = new HashMap<String,ArrayList<AlertDTO>>();

        ArrayList<String> sortedLoc = new ArrayList<String>();
        
        for(int i = 0; i < alertDTOs.length; i++)
        {
            // for each location
            String key1 = alertDTOs[i].getLocation();
            ArrayList<AlertDTO> arr1 = (ArrayList<AlertDTO>) hm1.get(key1);
            if(arr1 == null)
            {
                arr1 = new ArrayList<AlertDTO>();
                sortedLoc.add(key1);
            }

            arr1.add(alertDTOs[i]);
            
            hm1.put(key1, arr1);            
        } // end for i
        
        //
        // construct the json object from alertdtos
        //
        JSONObject json = new JSONObject();
        JSONArray jsonLocArray = new JSONArray();
        for(String location: sortedLoc)
        {
            ArrayList<AlertDTO> locAlertArrayList = (ArrayList<AlertDTO>) hm1.get(location);
            AlertDTO[] lAlertDTOs = new AlertDTO[locAlertArrayList.size()];
            locAlertArrayList.toArray(lAlertDTOs);
            JSONArray jsonDetailArray2 = new JSONArray();
            
            for(int i = 0; i < lAlertDTOs.length; i ++)
            {
                JSONObject jsonDetail3 = new JSONObject();
                jsonDetail3.put("location", lAlertDTOs[i].getLocation());
                jsonDetail3.put("desc", lAlertDTOs[i].getDesc());
                jsonDetail3.put("type", lAlertDTOs[i].getType());
                jsonDetail3.put("displayType", toCamelCase(lAlertDTOs[i].getType().getValue()));
                jsonDetail3.put("lat", lAlertDTOs[i].getLat());
                jsonDetail3.put("lon", lAlertDTOs[i].getLon());
                jsonDetail3.put("distance",
                                calDistanceInMilesDM5(centerLat, centerLon,
                                                      lAlertDTOs[i].getLat(),
                                                      lAlertDTOs[i].getLon()));
                jsonDetail3.put("crossStreet", lAlertDTOs[i].getCrossStreet());
                jsonDetail3.put("crossStreet2", lAlertDTOs[i].getCrossStreet2());
               
                
                  boolean hasFlowData=false;
                int speedColor=3;
                if (flowHash != null)
                {
                    FlowDTO flowDTO = flowHash.get(lAlertDTOs[i].getTmcId());
                    if (flowDTO != null)
                    {
                        logger.debug("here 6: not null flowDTO="
                                + lAlertDTOs[i].getTmcId());
                        if (flowDTO.getAverageSpeed() > 0)
                        {
                            jsonDetail3.put("averageSpeed",
                                            (int) dm6psToMph(flowDTO.getAverageSpeed()));
                            hasFlowData=true;
                        }
                       
                        if (flowDTO.getFreeFlowSpeed() > 0)
                        {
                            jsonDetail3.put("freeFlowSpeed",
                                            (int) dm6psToMph(flowDTO.getFreeFlowSpeed()));
                           if(hasFlowData)
                           {
                        	   int quarterSpeed = flowDTO.getFreeFlowSpeed() >> 2;
        					   speedColor = flowDTO.getAverageSpeed()/quarterSpeed;
                           }
                           else
                           {
                        	   String severity =lAlertDTOs[i].getSeverity().getValue();
                        	  if(severity.equalsIgnoreCase(SeverityTypeDTO.CRITICAL.getValue()))
                        	  {
                        		speedColor=0;  
                        	  }
                        	  else if(severity.equalsIgnoreCase(SeverityTypeDTO.MAJOR.getValue()))
                        	  {
                          		speedColor=1;  
                          	  }
                        	  else if(severity.equalsIgnoreCase(SeverityTypeDTO.MINOR.getValue()))
                        	  {
                          		speedColor=2;  
                          	  }
                        	  else
                        	  {
                        		speedColor=3;
                        	  }
                           }
        		
                        }                       
                    }
                    else
                    {
                        logger.warn("here 6: null flowDTO="
                                + alertDTOs[i].getTmcId());
                    }
                }
                jsonDetail3.put("hasFlowData",hasFlowData);
                jsonDetail3.put("speedColor",speedColor);
                jsonDetailArray2.put(jsonDetail3);  
            }
            
//            for(AlertDTO alertDTO: locAlertArrayList)
//            {
//                JSONObject jsonDetail3 = new JSONObject();
//                jsonDetail3.put("location", alertDTO.getLocation());
//                jsonDetail3.put("desc", alertDTO.getDesc());
//                jsonDetail3.put("type", alertDTO.getType());
//                jsonDetail3.put("lat", alertDTO.getLat());
//                jsonDetail3.put("lon", alertDTO.getLon());
//                jsonDetail3.put("distance", calDistanceInMilesDM5(centerLat, centerLon, alertDTO.getLat(), alertDTO.getLon()));
//                jsonDetail3.put("crossStreet", alertDTO.getCrossStreet());
//                jsonDetail3.put("crossStreet2", alertDTO.getCrossStreet2());
//                jsonDetailArray2.put(jsonDetail3);
//            }
            
            JSONObject jsonLoc1 = new JSONObject();
            jsonLoc1.put("name", location);
            jsonLoc1.put("details", jsonDetailArray2);
            jsonLocArray.put(jsonLoc1);
        }
        
        json.put("trafficAlerts", jsonLocArray);
        return json;
    }

    // Traffic incidents
    public static JSONObject getTrafficIncidents(JSONObject adJo, DataHandler handler,
            JSONObject locationJO, String replacementToken)
    {
        logger.info("here 20 - ad");
        JSONObject adJo_0 = adJo;
        try
        {
            JSONObject textADJO = adJo.getJSONObject("text");
            String enUS = textADJO.getString("en_US");
            String esMX = textADJO.getString("es_MX");
            if (enUS.contains(replacementToken)
                    || esMX.contains(replacementToken))
            {
                AlertDTO[] unSortedAlertsDTO = queryTrafficIncidents(handler,
                		                             locationJO, 10);

                AlertDTO[] alertsDTO = sortAlerts(unSortedAlertsDTO, locationJO);
                String alertsStr = "";
                if (alertsDTO != null && alertsDTO.length > 0)
                {                
                    JSONObject alertsJson = convertAlertDTOToJson(alertsDTO, null, locationJO);
                    JSONArray locArr1 = new JSONArray();

                    if(alertsJson != null)
                    {
                        locArr1 = (JSONArray) alertsJson.get("trafficAlerts");
                    }

                    for(int i = 0; i < locArr1.length(); i++)
                    {
                        if(i != 0)
                        {
                            alertsStr += ", ";
                        }
                        JSONObject locJson = locArr1.optJSONObject(i);
                        logger.info("\nout0.5:" + locJson);
                        
                        String location = (String) locJson.optString("name");
                        JSONArray detailsArr = (JSONArray) locJson.optJSONArray("details");

                        alertsStr += location + " " + detailsArr.length() + " incidents";
                    }
                }
                
                logger.info("here ad 22 - alertsStr=" + alertsStr);
                if (!alertsStr.equals(""))
                {
                    enUS = enUS.replace(replacementToken, alertsStr);
                    esMX = esMX.replace(replacementToken, alertsStr);

                    textADJO.put("en_US", enUS);
                    textADJO.put("es_MX", esMX);
                }
                else
                {
                    textADJO = adJo.getJSONObject("defaultText");
                }

                adJo.put("text", textADJO);
            }

            return adJo;
        }
        catch (JSONException e)
        {
            return adJo_0;
        }
    }

    public static AlertDTO[] queryTrafficIncidents(DataHandler handler,
            JSONObject locationJO, int mileValue)
    {
        try
        {
           //  String endPoint =
            // "http://d-traffic-01.mypna.com:8080/tnws/services/TrafficService";
            String endPoint = WebServiceConfigurator.getUrlOfTrafficServer();
            String alertDataSrc = WebServiceConfigurator.getTrafficAlertDataSrc();
            String flowDataSrc = WebServiceConfigurator.getTrafficFlowDataSrc();
            
            AreaTrafficRequestDTO areaTrafficRequest = new AreaTrafficRequestDTO();
            LatLonPointDTO high = new LatLonPointDTO();
            LatLonPointDTO low = new LatLonPointDTO();
            BoundingBoxDTO box = new BoundingBoxDTO();

            //
            // get area based on lat and lon
            int centerLat = locationJO.getInt("lat");
            int centerLon = locationJO.getInt("lon");

            HashMap<String,Integer> hashMap = getBoundingBox(mileValue, centerLat, centerLon);
            high.setLat(((Integer) hashMap.get("latHigh")).intValue());
            high.setLon(((Integer) hashMap.get("lonHigh")).intValue());
            low.setLat(((Integer) hashMap.get("latLow")).intValue());
            low.setLon(((Integer) hashMap.get("lonLow")).intValue());

            box.setHigh(high);
            box.setLow(low);

            areaTrafficRequest.setBoundingBox(box);

            // This should be passed as is from the client, but if you don't
            // have it, it should have AT LEAST the following parameters:
            // dataset: Navteq or TeleAtlas
            // flow_data_src : Source for Traffic Flows.
            // alert_data_src : Source for Traffic Alerts/Incidents.
            // locale: For alert description localization
            // application: The name of your application
            // login: The PTN of the requester, if any.
            // TODO - tidy up
//            TnContext tnContext = new TnContext("poidataset=YPC");
//            TnContext tnContext = new TnContext("poidataset=YPC&alert_data_src=WESTWOOD,WESTWOODBLOCKING,ITIS,NJECTION,TIMS,ROADPILOT,CCACIR,INRIXCANADA,UGT,TELENAV&flow_data_src=UGT,INRIXCANADA,ITIS,CCACIR");
            TnContext tnContext = new TnContext();
            tnContext.addProperty("poidataset", "YPC");
            tnContext.addProperty("alert_data_src", alertDataSrc);
            tnContext.addProperty("flow_data_src", flowDataSrc);
            
            areaTrafficRequest.setClientName("TN6.0");
            areaTrafficRequest.setClientVersion("6.0");
            areaTrafficRequest.setTransactionId("1");
            areaTrafficRequest.setContextString(tnContext.toContextString());
            logger.info("tnContext.toContextString():" + tnContext.toContextString());
            // Content Filter for incidents. Create a Filter with the old
            // datatypes
            // And then set its 'level' value in a FilterDTO object array in the
            // request
            //
            ContentFilter cf = FilterFactory.newContentFilter();

            // Set the flags according to your need
            cf.setShowAccidents(true);
            cf.setShowCongestion(true);
            cf.setShowConstructions(true);
            cf.setShowRoadHazards(true);
            cf.setShowEvents(false);
            cf.setShowSpeedCameras(false);
            cf.setShowSpeedTraps(false);
            cf.setShowTrafficCameras(false);

            FilterDTO f = new FilterDTO();
            f.setName("content");
            f.setLevel(cf.getLevel() + ""); // Convert to String

            areaTrafficRequest.setFilter(new FilterDTO[] { f });

            AreaTrafficReportDTO report = null;
            TrafficServiceStub stub = null;

            // logger.info("endPoint = " + endPoint);
            stub = new TrafficServiceStub(endPoint);

            // logger.info("stub = " + stub);

            AreaTrafficResponseDTO response = stub
                    .queryAreaTraffic(areaTrafficRequest);

            logger.info("response = " + response);

            report = response.getAreaTrafficReport();

            logger.info("report = " + report);

            if (report != null)
            {

                AlertDTO[] alertDTOs = report.getAlert();
                logger.info("alert dto = " + alertDTOs);
                if (alertDTOs != null)
                {
                    logger.info("alert dto length = " + alertDTOs.length);
                    for (AlertDTO alertDTO : alertDTOs)
                    {
                        if (alertDTO.getType() != null)
                        {
                            // logger.info("alert dto type=" +
                            // alertDTO.getType());
                            logger.info(", type="
                                    + alertDTO.getType().getValue());
                        }
                        // logger.info("alert dto=" + alertDTO);
                        // logger.info("alert dto id=" +
                        // alertDTO.getId());
//                        logger.info(", location=" + alertDTO.getLocation());
//                        logger.info(", desc =" + alertDTO.getDesc());
//                        logger.info(", distance =" + calDistanceInMilesDM5(alertDTO.getLat(), alertDTO.getLon(), centerLat, centerLon));
//                        logger.info(", lat/lon =" + alertDTO.getLat()
//                                + "/" + alertDTO.getLon());

                        // logger.info("alert dto long desc=" +
                        // alertDTO.getLongDesc());
                    }
                }

                return alertDTOs;
                // use the method of report.getAlert() to get alert data, use
                // the method of report.getFlow() to get flow data.

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            logger.info("Query area traffic failed." + ex.getMessage());
        }
        finally
        {
            // TODO
            // cleanUpStub(stub);
        }

        return null;
    }

    // given a center lat and lon, find a bounding box that has a side of width
    // n miles
    public static HashMap<String, Integer> getBoundingBox(double miles, int centerLat,
            int centerLon)
    {
        int latDelta = convertMilesToLatDegrees(miles / 2.0, centerLat,
                centerLon);
        int lonDelta = convertMilesToLonDegrees(miles / 2.0, centerLat,
                centerLon);

        int latHigh = centerLat + latDelta;
        int latLow = centerLat - latDelta;

        int lonHigh = centerLon - lonDelta;
        int lonLow = centerLon + lonDelta;

//        logger.info("Center=(" + centerLat + "," + centerLon + ")");
//        logger.info("High=(" + latHigh + "," + lonHigh + ")");
//        logger.info("Low=(" + latLow + "," + lonLow + ")");

        HashMap<String, Integer> retHash = new HashMap<String, Integer>();
        retHash.put("latHigh", latHigh);
        retHash.put("lonHigh", lonHigh);
        retHash.put("latLow", latLow);
        retHash.put("lonLow", lonLow);

        return retHash;
    }

    private static final double LAT_BY_MILE = 1.0 / 69.0;
    public static final double DEGREE_MULTIPLIER = 1.e5; // 1e-5 deg units

    private static int convertMilesToLatDegrees(double miles, int refLat,
            int refLon)
    {
        return (int) (miles * LAT_BY_MILE * DEGREE_MULTIPLIER);
    }

    private static int convertMilesToLonDegrees(double miles, int refLat,
            int refLon)
    {
        return (int) ((miles * LAT_BY_MILE * DEGREE_MULTIPLIER) / Math.cos(refLon / 1e5));
    }


    public static int calDistanceInMilesDM5(int lat, int lon, int refLat,
                                            int refLon)
    {
        return calDistanceInMiles(lat / DEGREE_MULTIPLIER, lon
                / DEGREE_MULTIPLIER, refLat / DEGREE_MULTIPLIER, refLon
                / DEGREE_MULTIPLIER);
    }

    public static int calDistanceInMiles(double lat, double lon, double refLat,
                                    double refLon)
    {
        return (int) (((double) calDistanceInMeter(lat, lon, refLat, refLon))/1609.344);
    }

    public static int calDistanceInMeter(double lat, double lon, double refLat,
                                         double refLon)
    {
        double dLon = (refLon - lon)
                * Math.cos((refLat + lat) / 2.0 / 180.0 * Math.PI);
        double dLat = refLat - lat;
        double ret = Math.hypot(dLat, dLon);
        ret *= 10000000.0 / 90.0;
        return (int) ret;
    }
 
    public static HashMap<String,FlowDTO> queryTrafficFlows(DataHandler handler,
    		AlertDTO[] alertDTOs)
    {
        HashMap<String, FlowDTO> flowHash = new HashMap<String, FlowDTO>();
        
        try
        {
            // String endPoint =
            // "http://d-traffic-01.mypna.com:8080/tnws/services/TrafficService";
            String endPoint = WebServiceConfigurator.getUrlOfTrafficServer();
            String alertDataSrc = WebServiceConfigurator.getTrafficAlertDataSrc();
            String flowDataSrc = WebServiceConfigurator.getTrafficFlowDataSrc();

            TrafficFlowsReportDTO reportFlow = null;
            TrafficFlowsRequestDTO requestFlow = new TrafficFlowsRequestDTO();

            
            
            TrafficDataRequestDTO trafficDataRequest = new TrafficDataRequestDTO();

            // TODO
            String[] trafficIds = new String[alertDTOs.length];
            for(int i = 0; i < alertDTOs.length; i++)
            {
                trafficIds[i] = alertDTOs[i].getTmcId();
            }
            trafficDataRequest.setTrafficId(trafficIds);
            requestFlow.setTrafficId(trafficIds);
            
            logger.debug("queryTrafficFlows: trafficIds:" + trafficIds.toString());
            
            // This should be passed as is from the client, but if you don't
            // have it, it should have AT LEAST the following parameters:
            // dataset: Navteq or TeleAtlas
            // flow_data_src : Source for Traffic Flows.
            // alert_data_src : Source for Traffic Alerts/Incidents.
            // locale: For alert description localization
            // application: The name of your application
            // login: The PTN of the requester, if any.
            // TODO - tidy up
//            TnContext tnContext = new TnContext("poidataset=YPC");
//            TnContext tnContext = new TnContext("poidataset=YPC&alert_data_src=WESTWOOD,WESTWOODBLOCKING,ITIS,NJECTION,TIMS,ROADPILOT,CCACIR,INRIXCANADA,UGT,TELENAV&flow_data_src=UGT,INRIXCANADA,ITIS,CCACIR");
            TnContext tnContext = new TnContext();
            tnContext.addProperty("poidataset", "YPC");
            tnContext.addProperty("alert_data_src", alertDataSrc);
            tnContext.addProperty("flow_data_src", flowDataSrc);            trafficDataRequest.setClientName("TN6.0");
            trafficDataRequest.setClientVersion("6.0");
            trafficDataRequest.setTransactionId("1");
            trafficDataRequest.setContextString(tnContext.toContextString());
            logger.info("tnContext.toContextString():"
                    + tnContext.toContextString());

            TrafficDataReportDTO report = null;
            TrafficServiceStub stub = null;

            // logger.info("endPoint = " + endPoint);
            stub = new TrafficServiceStub(endPoint);

            // logger.info("stub = " + stub);
            
            TrafficDataResponseDTO response = stub.queryTrafficData(trafficDataRequest);

            logger.info("response = " + response);

            report = response.getTrafficDataReport();

            logger.info("report = " + report);

            if (report != null)
            {

                FlowDTO[] flowDTOs = report.getFlow();
                logger.info("flow dto = " + flowDTOs);
                if (flowDTOs != null)
                {
                    logger.info("flow dto length = " + flowDTOs.length);
                    for (FlowDTO flowDTO : flowDTOs)
                    {
                        logger.info("flow dto=" + flowDTO);
                        logger.info("flow dto tmcid =" + flowDTO.getTmcId());
                        logger.info("flow dto id=" + flowDTO.getId());
                        logger.info(", free flow speed=" + flowDTO.getFreeFlowSpeed());
                        logger.info(", average speed=" + dm6psToMph(flowDTO.getAverageSpeed()));
                        flowHash.put(flowDTO.getTmcId(), flowDTO);
                        // logger.info(", desc =" + flowDTO.getDesc());
                        // logger.info(", distance =" +
                        // calDistanceInMilesDM5(flowDTO.getLat(),
                        // flowDTO.getLon(), centerLat, centerLon));
                        // logger.info(", lat/lon =" + flowDTO.getLat()
                        // + "/" + flowDTO.getLon());

                        // logger.info("flow dto long desc=" +
                        // flowDTO.getLongDesc());
                    }
                }

//                return flowDTOs;
                // use the method of report.getflow() to get flow data, use
                // the method of report.getFlow() to get flow data.

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            logger.error("Query traffic data failed." + ex.getMessage());
        }
        finally
        {
            // TODO
            // cleanUpStub(stub);
        }

        return flowHash;
    }

    public static String toCamelCase(String s)
    {
        String camelCaseString = "";
        if(s == null)
        {
            return camelCaseString;
        }
        
        try
        {
            String[] parts = s.split("_");
            for (String part : parts)
            {
                camelCaseString = camelCaseString + toProperCase(part);
            }
        }
        catch (Exception ex)
        {
            logger.error("TrafficIncidentsAction:toCamelCase exception for " + s + ". " + ex.getMessage());
            return s;
        }
        
        return camelCaseString;
    }

    public static String toProperCase(String s)
    {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static void printAlertDTOs(AlertDTO[] alertDTOs, JSONObject locationJo)
    {
        logger.info("Printing AlertDTOs");
        for (int i = 0; i < alertDTOs.length; i++)
        {
            logger.info("index=" + i);
            printAlertDTO(alertDTOs[i], locationJo);
        }
    }

    public static double dm6psToMph(int dm6ps)
    {
      return dm6ps / 4.01581262368D;
    }
    
    public static void printAlertDTO(AlertDTO alertDTO, JSONObject locationJo)
    {
        logger.info("AlertDTO");
        int distance = TrafficIncidentsAction.calDistanceInMilesDM5(alertDTO.getLat(),
                                                                    alertDTO.getLon(),
                                                                    locationJo.optInt("lat"),
                                                                    locationJo.optInt("lon"));
        logger.info("Id=" + alertDTO.getId() + ", Distance=" + distance
                + ", Location=" + alertDTO.getLocation() + ", Desc="
                + alertDTO.getDesc() + ", Cross Street="
                + alertDTO.getCrossStreet() + ", Type=" + alertDTO.getType());        
    }
}
