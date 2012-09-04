package com.telenav.browser.movie;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.TimeZone;
import org.apache.axis2.databinding.types.Day;
import org.apache.axis2.databinding.types.Month;
import org.apache.axis2.databinding.types.Year;
import org.apache.log4j.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import com.telenav.browser.movie.datatypes.MovieImageType;
import com.telenav.client.dsm.ContextMgrService;
import com.telenav.client.dsm.ContextMgrStatus;
import com.telenav.client.dsm.Error;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.holder.impl.DsmRuleHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.datatypes.content.movie.v10.Area;
import com.telenav.datatypes.content.movie.v10.MovieSearchDate;
import com.telenav.datatypes.content.movie.v10.Schedule;
import com.telenav.datatypes.content.movie.v10.Show;
import com.telenav.datatypes.content.tnpoi.v10.TnPoi;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.content.moviesearchservice.v10.MovieImageRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchService;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchServiceStub;
import com.telenav.services.content.v10.ContentSearchService;
import com.telenav.services.content.v10.ContentSearchServiceStub;
import com.telenav.services.ots.v10.OTServiceFault;
import com.telenav.services.ots.v10.OTServiceStub;
import com.telenav.services.ots.v10.QueryRequestDTO;
import com.telenav.services.ots.v10.QueryResponseDTO;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.ws.datatypes.address.Address;
import com.telenav.ws.datatypes.address.GeoCode;
import com.telenav.ws.datatypes.services.ResponseCodeEnum;
import com.telenav.ws.datatypes.services.ServiceRequest;

public class Util {
	private static final Logger log = Logger.getLogger(Util.class);
	
	private static String wsMovieURL;
	private static String wsPoiURL;
	private static String wsOtsURL;
	private static String wsMovieTicketURL;
	private static String wsMovieURL20;
	private static String checkOutURL;
	private static String checkOutURL_test;
	private static String checkOutURL_stage;
	private static HashMap<String, String> intefaces = new HashMap<String, String>();
	private static final String PREFIX = "\\{pref\\}";
	private static String poiInterfaceURL;
	private static String recipientInterfaceURL;
	private static String nameAndEmailInterfaceURL;
	private static String driveToInterfaceURL;
	private static String poiURL;
	private static final String TEN_POINT_ZERO = "10.0";
	private static final String TEN = "10";
	
	public static final String FIRST_LINE = "firstLine"; 
	public static final String STREET1 = "street1"; 
	public static final String STREET2 = "street2"; 
	public static final String CITY = "city"; 
	public static final String STATE = "state"; 
	public static final String ZIP = "zip"; 
	public static final String COUNTRY = "country";
	public static final String LAT = "lat";
	public static final String LON = "lon";
	public static final String TYPE = "type";
	public static final byte STOP_CURRENT_LOCATION = 6;
	
	static{
		try{
			String wsFile = "web_services.properties";
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
        	InputStream stream = cl.getResourceAsStream(wsFile);
        	PropertyResourceBundle serverBundle = null;
            if (stream != null){
            	serverBundle = new PropertyResourceBundle(stream);
            }
            wsMovieURL = serverBundle.getString("URL_MOVIE_SEARCH");
            wsPoiURL = serverBundle.getString("URL_POI_SEARCH");
            wsOtsURL = serverBundle.getString("URL_OTS");
            wsMovieTicketURL = serverBundle.getString("URL_MOVIE_TICKET");
            wsMovieURL20 = serverBundle.getString("URL_MOVIE_SEARCH20");
        	checkOutURL = serverBundle.getString("URL_MOVIE_CHECKOUT");
        	checkOutURL_test = serverBundle.getString("URL_MOVIE_CHECKOUT_TEST");
        	checkOutURL_stage = serverBundle.getString("URL_MOVIE_CHECKOUT_STAGE");
        	
            wsFile = "config/commonServices.properties";
            stream = cl.getResourceAsStream(wsFile);
        	serverBundle = null;
            if (stream != null){
            	serverBundle = new PropertyResourceBundle(stream);
            }
            /*
            poiInterfaceURL = serverBundle.getString("POI_ADDRESS_CAPTURE_INTERFACE");
            intefaces.put(poiInterfaceURL, replacePrefix(poiInterfaceURL, ""));
            intefaces.put("touch/" + poiInterfaceURL, replacePrefix(poiInterfaceURL, "touch/"));
            
            recipientInterfaceURL = serverBundle.getString("POI_RECIPIENTS_INTERFACE");
            intefaces.put(recipientInterfaceURL, replacePrefix(recipientInterfaceURL, ""));
            intefaces.put("touch/" + recipientInterfaceURL, replacePrefix(recipientInterfaceURL, "touch/"));
            
            nameAndEmailInterfaceURL = serverBundle.getString("NAMEANDEMAIL_CONTROLLER_URL");
            intefaces.put(nameAndEmailInterfaceURL, replacePrefix(nameAndEmailInterfaceURL, ""));
            intefaces.put("touch/" + nameAndEmailInterfaceURL, replacePrefix(nameAndEmailInterfaceURL, "touch/"));
            
            driveToInterfaceURL = serverBundle.getString("DRIVE_TO_INTERFACE");
            intefaces.put(driveToInterfaceURL, replacePrefix(driveToInterfaceURL, ""));
            intefaces.put("touch/" + driveToInterfaceURL, replacePrefix(driveToInterfaceURL, "touch/"));
            
            poiURL = serverBundle.getString("SEARCH_POI_URL");
            intefaces.put(poiURL, replacePrefix(poiURL, ""));
            intefaces.put("touch/" + poiURL, replacePrefix(poiURL, "touch/"));
            */
            
		}catch(Exception ex){
			log.error("Uable to load property file for WS services & Interfaces.");
			log.error("cause:"+ex.getCause()+",message:" + ex.getMessage());
		}
	}

    public static MovieSearchServiceStub getService() throws Exception {
    	//TODO add retrieval from property file
    	MovieSearchServiceStub stub = new MovieSearchServiceStub(wsMovieURL);
    	return stub;
    }
    
    public static ContentSearchServiceStub getPoiService() throws Exception{
    	ContentSearchServiceStub stub = new ContentSearchServiceStub(wsPoiURL);
    	return stub;
    }
    
    private static TnContext _context = new TnContext("poidataset=YPC");
    public static void setClientProps(ServiceRequest req){
    	req.setClientName("cose");
    	req.setClientVersion("1.0");
    	req.setTransactionId("1");
    	req.setContextString(_context.toContextString());
    }
    
    public static String getPoiAcInterface(String prefix){
    	return getURL(poiInterfaceURL, prefix);
    }
    
    public static String getPoiRecipientInterface(String prefix){
    	return getURL(recipientInterfaceURL, prefix);
    }
    
    public static String getNameAndEmailInterface(String prefix){
    	return getURL(nameAndEmailInterfaceURL, prefix);
    }
    
    public static String getPoiURL(String prefix){
    	return getURL(poiURL, prefix);
    }
    
    public static String getDriveToInterface(String prefix){
    	return getURL(driveToInterfaceURL, prefix);
    }
    
    private static String getURL(String url, String prefix){
    	if (prefix == null || prefix.length() == 0){
    		return intefaces.get(url);
    	}else{
    		String key = prefix + url;
    		String value = intefaces.get(key);
    		if (value == null){
    			synchronized (Util.class) {
					value = replacePrefix(url, prefix);
					intefaces.put(key, value);
				}
    		}
    		return value;
    	}
    }
    
    private static String replacePrefix(String url, String prefix){
    	String result = new String(url);
    	return result.replaceFirst(PREFIX, prefix);
    }
	
//	public static ConfigurationContext createNewContext() throws AxisFault {
//		ConfigurationContext configContext = ConfigurationContextFactory
//			.createConfigurationContextFromFileSystem(null, null);
//		// create the multi-threaded HTTP manager & the client with it
//		MultiThreadedHttpConnectionManager mgr = new MultiThreadedHttpConnectionManager();
//		HttpConnectionManagerParams connectionManagerParams = new HttpConnectionManagerParams();
//		connectionManagerParams.setDefaultMaxConnectionsPerHost(10);
//		connectionManagerParams.setTcpNoDelay(true);
//		connectionManagerParams.setStaleCheckingEnabled(true);
//		connectionManagerParams.setLinger(0);
//		
//		// set the timeouts
//        connectionManagerParams.setConnectionTimeout(2000); // in milliseconds
//        connectionManagerParams.setSoTimeout(5000); // in milliseconds     
//
//
//		mgr.setParams(connectionManagerParams);
//		HttpClient client = new HttpClient(mgr);
//		// cache the HTTP client
//		configContext.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, Boolean.TRUE);
//		configContext.setProperty(HTTPConstants.CACHED_HTTP_CLIENT, client);
//		// set the thread pool
//		configContext.setThreadPool(new ThreadPool(10, 10));
//		return configContext;
//	}
	
    public static void main(String[] args) throws Exception {
    	
    	
//    	ContentSearchService service = getPoiService();
//    	//ContentSearchService service = new ContentSearchServiceStub(createNewContext(), poiURL);
//    	GetPoiByIdsRequest getIdRequest = new GetPoiByIdsRequest();
//    	getIdRequest.setClientName("cose");
//    	getIdRequest.setClientVersion("1.0");
//    	getIdRequest.setTransactionId("1");
//    	getIdRequest.setPoiIds(new long[]{2042249540, 2046097453, 2052011067, 2044482794});
//    	getIdRequest.setContextString(new TnContext("poidataset=YPC").toContextString());
//		
//    	GetPoiByIdsResponse resp = service.getPoiByIds(getIdRequest);
//    	
//    	MovieSearchService stub = getService();
//        MovieSearchRequestDTO sMovie = new MovieSearchRequestDTO();
//        sMovie.setKeyword("");
//        Date searchDate = Util.getDateByIndex(0);
//        sMovie.setDate(searchDate);
//        Area area = Util.getArea(3735588, -12199834, Constant.SEARCH_RADIUS);
//        sMovie.setArea(area);
//        sMovie.setPageLength(20);
//        sMovie.setPageNumber(1);
//        sMovie.setSortType(MovieSortTypeEnum.ALPHABET);
//        Util.setClientProps(sMovie);
//    	MovieServiceResponse mResp = stub.searchMovies(sMovie);
//    	ResponseStatus status = resp.getResponseStatus();
//    	System.out.println("Util.getPoiByIds(). Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
//    	System.out.println("something...");
    	
    }
    
    public static TnContext getTnContext(DataHandler handler)
    {
        TnContext tc = new TnContext();
        String mapDataSet = "Navteq";   
        String account = handler.getClientInfo(DataHandler.KEY_USERACCOUNT);
        String userID = handler.getClientInfo(DataHandler.KEY_USERID);

        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        String device = handler.getClientInfo(DataHandler.KEY_DEVICEMODEL);
        
        
        long lUserID = -1;
        if (userID != null && userID.length() > 0) {
            lUserID = Long.parseLong(userID);
        }
        tc.addProperty(TnContext.PROP_LOGIN_NAME , account);
        tc.addProperty(TnContext.PROP_CARRIER , carrier);
        tc.addProperty(TnContext.PROP_DEVICE , device);
        tc.addProperty(TnContext.PROP_PRODUCT , platform);
        tc.addProperty(TnContext.PROP_VERSION , version);

        ContextMgrService cms = new ContextMgrService();
        ContextMgrStatus myStatus = cms.registerContext(lUserID, "BROWSER-SERVER", tc);

        if(myStatus == null || myStatus.getStatusCode() != Error.NO_ERROR)
        {
            tc.addProperty(TnContext.PROP_MAP_DATASET, mapDataSet);
        }
        
        return tc;
    }

    public static Area getArea(long lat, long lon, int radiusInMeters){
        Area area = new Area();
        GeoCode gc = new GeoCode();
        gc.setLatitude(lat/Constant.DEGREE_MULTIPLIER);
        gc.setLongitude(lon/Constant.DEGREE_MULTIPLIER);
        area.setGeoCode(gc);
        area.setRadiusInMeter(radiusInMeters);
    	return area;
    }
	
    public static int calDistanceInMeter(double lat, double lon, double refLat, double refLon){
        double dLon = (refLon - lon)* Math.cos((refLat + lat)/2.0/180.0*Math.PI);
        double dLat = refLat - lat;
        double ret = Math.hypot(dLat,dLon);
        ret *= 10000000.0 / 90.0;
        return (int)ret;
     }
    
    public static String distanceInMiles(int meters){
        double res = meters/Constant.MILE_METER;
        String result = "";
        if (res > 10d)
        	result = String.format("%.0f", res);
        else
        	result = String.format("%.1f", res);
    	return formatTen(result);
    }
    
    public static String distanceInKilometers(int meters){
    	double res = meters/1000;
        String result = "";
        if (res > 10d)
        	result = String.format("%.0f", res);
        else
        	result = String.format("%.1f", res);
    	return formatTen(result);
    }

    /** format 10.0 to 10*/
    private static String formatTen(String result) {
		if (TEN_POINT_ZERO.equals(result)) {
			return TEN;
		}
		return result;

	}
    
    public static byte[] fileToImageData(String filePath) {
        FileInputStream in = null;
        byte[] imageData = null;
        try {
            in = new FileInputStream(filePath);
            if (in.available() == 0) {
                return null;
            }
            imageData = new byte[in.available()];
            in.read(imageData);
            in.close();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (in != null)
                    in.close();

            } catch (IOException e) {
            	
            }
        }
        return imageData;
    }
	
    public static void imageDataToFile(String filePath, byte[] imageData) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            out.write(imageData);

            out.close();
        } catch (FileNotFoundException e) {
        	log.error("Uable to find file "+filePath);
        	log.error("cause:"+e.getCause()+",message:" + e.getMessage());
        } catch (IOException e) {
        	log.error("Uable to write imageData into file "+filePath);
        	log.error("cause:"+e.getCause()+",message:" + e.getMessage());
        } finally {
            try {
                if (out != null)
                    out.close();

            } catch (IOException e) {
            }
        }

    }
   
    public static String formatRunTime(String runTimeString) {
        int runTime = Integer.parseInt(runTimeString);
        int hour = runTime / 60;
        int min = runTime % 60;
        String hourString;
        String minString;

        if (hour < 10) {
            hourString = "0" + hour;
        } else {
            hourString = "" + hour;
        }
        if (min < 10) {
            minString = "0" + min;
        } else {
            minString = "" + min;
        }
        return hourString + "H" + minString + "M";

    }
    
    public static String timeFormat(String s) {
        if (s == null || s.length() < 1)
            return "";
        String[] times = s.split("H|M");
        String hour = Integer.parseInt(times[0]) + "";
        String min = Integer.parseInt(times[1]) + "";
        return hour + " hr " + min + " mins";
    }
    
    public static String makeOneString(String[] arr, String delimeter){
    	if (arr == null) return "";
    	StringBuffer buff = new StringBuffer();
    	if (arr.length > 0) buff.append(arr[0]);
    	for(int i=1; i<arr.length; i++){
    		buff.append(delimeter);
    		buff.append(arr[i]);
    	}
    	return buff.toString();
    }
    
    public static String getDetailAddress(TnPoi theater){
    	StringBuffer buff = new StringBuffer();
    	Address adr = theater.getAddress();
        if (theater != null) {
//            buff.append(adr.getStreetNumber());
//            buff.append(" ");
            buff.append(adr.getStreetName());
            if (adr.getCity() != null && adr.getCity().length() > 0) {
                if (buff.length() > 0) {
                    buff.append(", ");
                }
                buff.append(adr.getCity());
            }
            if (adr.getState() != null && adr.getState().length() > 0) {
                if (buff.length() > 0) {
                    buff.append(", ");
                }
                buff.append(adr.getState());
            }
            if (adr.getPostalCode() != null
                    && adr.getPostalCode().length() > 0) {
                if (buff.length() > 0) {
                    buff.append(" ");
                }
                buff.append(adr.getPostalCode().substring(0,5));
            }
        }
    	return buff.toString();
    }
    
    public static String getAddressJsonStr(TnPoi theater) throws JSONException {
    	JSONObject addr = new JSONObject();
    	Address tAddress = theater.getAddress();
    	addr.put("firstLine", tAddress.getStreetName());
    	addr.put("city", tAddress.getCity());
    	addr.put("state", tAddress.getState());
    	addr.put("zip", tAddress.getPostalCode());
    	addr.put("country", tAddress.getCountry());
    	long lat = (long)(tAddress.getGeoCode().getLatitude()*Constant.DEGREE_MULTIPLIER);
    	long lon = (long)(tAddress.getGeoCode().getLongitude()*Constant.DEGREE_MULTIPLIER);
    	addr.put("lat", lat);
    	addr.put("lon", lon);
    	String result = addr.toString().replaceAll("\"", "_"); 
    	log.debug("Theater address from util:" + result);
    	return result;
    }
    
    
    /**
     * Format phone number.
     * 
     * @param s Number String.
     * @return
     */
    public static String formatPhoneNumber(String s) {
        if (s == null || s.length() == 0)
            return "";
        if (s.length() < 10)
            return s;
        StringBuffer sb = new StringBuffer(s);
        if (sb.charAt(0) == '1' && s.length() == 11) {
            sb.insert(1, "-");
            sb.insert(5, "-");
            sb.insert(9, "-");
        } else {
            sb.insert(0, "(");
            sb.insert(4, ") ");
            sb.insert(9, "-");
        }
        return sb.toString();
    }
    
    public static String stripPhoneNumber(String phone){
    	char[] arr = phone.toCharArray();
    	StringBuffer buff = new StringBuffer();
    	for (int i = 0; i < arr.length; i++) {
			if (Character.isDigit(arr[i]))
				buff.append(arr[i]);
		}
    	return buff.toString();
    }
    
    /**
     * Get ticket wap link according
     * 
     * @param vendorName
     * @param showTime
     * @param ticketURI
     * @param dateString
     * @param theaterVendorId
     * @param marginDay
     * @return
     */
    public static String getWapLink(String vendorName, String showTime,
            String ticketURI, String dateString, String theaterVendorId) {
        String wapLink = null;
        if ("TMS".equalsIgnoreCase(vendorName)) {
            String altFilmId = getAltFilmId(ticketURI);
            if (isNumeric(altFilmId)) {
                //String address = "http://mobile.fandango.com/tms.asp?a=11872&m={0}&t={1}&d={2}%20{3}";
                String address = "http://www.fandango.com/transaction/ticketing/mobile/jump.aspx?source=MobileWeb&af=11872&tmid={0}&tid={1}&sdate={2}%20{3}";
                wapLink = MessageFormat.format(address, altFilmId, theaterVendorId, dateString, showTime);
                return wapLink;
            }
        } else if ("MT".equalsIgnoreCase(vendorName)) {
            // MT is no longer supported.
        }

        return wapLink;

    }
    /**
     * Get movie altFilmId form ticketURI
     * <p>
     * e.g. http://www.fandango.com/tms.asp?t=AAVAC&m=69138&d=2008-11-24 -->
     * 69138
     * </p>
     * 
     * @param ticketURI
     * @return
     */

    private static String getAltFilmId(String ticketURI) {
        if (ticketURI == null || ticketURI.length() < 1) {
            return null;
        }
        String[] strs = ticketURI.toLowerCase().split("&m=");
        if (strs.length != 2) {
            return null;
        }
        strs = strs[1].toLowerCase().split("&d=");
        if (strs.length != 2) {
            return null;
        }
        return strs[0];
    }
    
    /**
     * Judge whether string is numeric
     * 
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.length() < 1) {
            return false;
        }
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if(!Character.isDigit(ch[i])){
            	if (ch[i]=='\n' || ch[i]=='\r') continue;
            	return false;
            }
		}
        return true;
    }

    /**
     * Get margin day from show time <br>
     * 24:15 -> 1 48:01-> 2 just consider hour >= 0
     * 
     * @param showTime
     * @return
     */
    public static int getMarginDay(String showTime) {
        if (showTime == null || showTime.length() < 1)
            return 0;
        String[] times = showTime.split(":");
        int hour = Integer.parseInt(times[0]);
        // Just for hour >= 0

        return hour / 24;

    }

    /**
     * Format show time to 12-hour, but just add suffix AM
     * <p>
     * e.g. 12:50->12:50, 9:50-> 9:50 am, 17:50->5:50, 24:50->12:50 am,
     * 0:10->12:10 am (only consider hour >= 0)
     * </p>
     * 
     * @param showTime
     * @return
     */
    public static String formatShowTimeAm(String showTime) {
        if (showTime == null || showTime.length() < 1)
            return "";
        String[] times = showTime.split(":");
        int hour = Integer.parseInt(times[0]);
        String min = times[1];
        // Just for hour >= 0
        hour %= 24;

        if (hour == 12)
            return times[0] + ":" + times[1];
        if (hour > 12) {
            return (hour - 12) + ":" + min;
        } else if (hour == 0) {
            return 12 + ":" + min + " am";
        } else {
            return hour + ":" + min + " am";
        }

    }

    /**
     * Format show time to 12-hour, add suffix AM/PM
     * <p>
     * e.g. 12:50->12:50 pm, 9:50-> 9:50 am, 17:50->5:50 pm, 24:50->12:50 am,
     * 0:10->12:10 am (only consider hour >= 0)
     * </p>
     * 
     * @param showTime
     * @return
     */
    public static String formatShowTimeAll(String showTime) {
        if (showTime == null || showTime.length() < 1)
            return "";
        String[] times = showTime.split(":");
        int hour = Integer.parseInt(times[0]);
        String min = times[1];
        // Just for hour >= 0
        hour %= 24;
        if (hour == 12)
            return 12 + ":" + min + " pm";
        if (hour > 12) {
            return (hour - 12) + ":" + min + " pm";
        } else if (hour == 0) {
            return 12 + ":" + min + " am";
        } else {
            return hour + ":" + min + " am";
        }

    }

    /**
     * /** Format show time to 24-hours with no suffix
     * <p>
     * e.g. 12:50->12:50, 24:50-> 0:50 (only consider hour >= 0)
     * </p>
     * 
     * @param showTime
     * @return
     */
    public static String formatShowTime(String showTime) {
        if (showTime == null || showTime.length() < 1)
            return "";
        String[] times = showTime.split(":");
        int hour = Integer.parseInt(times[0]);
        String min = times[1];
        // Just for hour >= 0
        hour %= 24;

        return hour + ":" + min;
    }
    
    /**
     * Replace & with &amp;
     * 
     * @param s
     * @return
     */
    public static String revise(String s) {

        return s.replaceAll("&", "&amp;");
    }

    /**
     * @param date
     * @param showTime
     * @return
     */
    public static Date getTrueDate(MovieSearchDate date, String showTime) {
        // Month in calendar 0 based (0-January), COSE 1 based (1-January)
        GregorianCalendar now = new GregorianCalendar(date.getYear().getYear(), date.getMonth().getMonth()-1, date.getDay().getDay());
        now.add(Calendar.DAY_OF_MONTH, getMarginDay(showTime));
        return now.getTime();
    }
    
    private static GregorianCalendar _cal = new GregorianCalendar();
    public static MovieSearchDate getDateByIndex(long index){
    	// according to google search results... it works faster... we'll see
        GregorianCalendar cal = (GregorianCalendar) _cal.clone(); 
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, (int)index);
        MovieSearchDate result = new MovieSearchDate();
        result.setDay(new Day(cal.get(Calendar.DAY_OF_MONTH)));
        // Month in calendar 0 based (0-January), COSE 1 based (1-January)
        result.setMonth(new Month(cal.get(Calendar.MONTH)+1));
        result.setYear(new Year(cal.get(Calendar.YEAR)));
        return result;
    }
    
    // date has format dd-MM-yyyy
    public static MovieSearchDate getSearchDate(String date){
    	SimpleDateFormat f = Constant.NUMBER_DATE_FORMAT;
    	Date d = null;
    	try{
    		d = f.parse(date);
    		log.debug("Date=" + d);
    	}catch(Exception ex){}
    	if (d==null) d = new Date();
    	
        GregorianCalendar cal = (GregorianCalendar) _cal.clone(); 
        cal.setTime(d);
        MovieSearchDate result = new MovieSearchDate();
        result.setDay(new Day(cal.get(Calendar.DAY_OF_MONTH)));
        // Month in calendar 0 based (0-January), COSE 1 based (1-January)
        result.setMonth(new Month(cal.get(Calendar.MONTH)+1));
        result.setYear(new Year(cal.get(Calendar.YEAR)));
        return result;
    }
    
    public static String getShowTimes(Schedule s){
    	String msg = "Show times are unavailable.";
    	if (s == null) return msg;
    	StringBuffer buff = new StringBuffer();
    	Show[] show = s.getShow();
    	if (show.length == 0) return msg;
    	buff.append(formatShowTimeAm(show[0].getTime().toString()));
    	for (int i = 1; i < show.length; i++) {
			buff.append(" | ");
			buff.append(formatShowTimeAm(show[i].getTime().toString()));
		}
    	return buff.toString();
    }

    public static String getShowTimesForDisplay(Schedule s){
        String msg = "Show times are unavailable.";
        if (s == null) return msg;
        StringBuffer buff = new StringBuffer();
        Show[] show = s.getShow();
        if (show.length == 0) return msg;
        buff.append(formatShowTimeAll(show[0].getTime().toString()));
        for (int i = 1; i < show.length; i++) {
            buff.append("\r\n");
            buff.append(formatShowTimeAll(show[i].getTime().toString()));
        }
        return buff.toString();
    }

    public static String getDisplayDate(MovieSearchDate date){
        // Month in calendar 0 based (0-January), COSE 1 based (1-January)
        GregorianCalendar now = new GregorianCalendar(date.getYear().getYear(), date.getMonth().getMonth()-1, date.getDay().getDay());
        SimpleDateFormat sf = (SimpleDateFormat)Constant.SHORT_DATE_FORMAT.clone();
        String result =  sf.format(now.getTime()); 
        return result;
    }
	
	public static String getDisplayDateWithLocale(MovieSearchDate date,String locale){
        // Month in calendar 0 based (0-January), COSE 1 based (1-January)
        GregorianCalendar now = new GregorianCalendar(date.getYear().getYear(), date.getMonth().getMonth()-1, date.getDay().getDay());
        SimpleDateFormat sdf = DateFormatCreater.getDateFormat(DateFormatCreater.DATE_FORMAT_MONTH_DATE,locale);
        String result =  sdf.format(now.getTime()); 
        return result;
    }	

    public static String getDisplayDate(MovieSearchDate date,String locale){
        GregorianCalendar now = new GregorianCalendar(date.getYear().getYear(), date.getMonth().getMonth()-1, date.getDay().getDay());
        SimpleDateFormat sf ;
        if(locale.equals("fr_CA")){
            sf= new SimpleDateFormat("d MMM", new Locale(locale.split("_")[0], locale.split("_")[1]));
        }else{
            sf= new SimpleDateFormat("MMM d", new Locale(locale.split("_")[0], locale.split("_")[1]));
        }
        String result =  sf.format(now.getTime()); 
        return result;
    }    
    
    /**
     * 
     * @param jo
     * @return
     */
    public static Stop convertAddress(JSONObject address)
    {
        Stop stop = new Stop();

		try{
	        if (address.has(STREET1)){
	        	stop.firstLine = address.getString(STREET1);
				if (address.has(STREET2)) stop.firstLine = stop.firstLine + " " + address.getString(STREET2);
				if (address.has(FIRST_LINE)) stop.label = address.getString(FIRST_LINE);
			}
			else if (address.has(FIRST_LINE)) stop.firstLine = address.getString(FIRST_LINE);
			
			if (address.has(CITY)) stop.city = address.getString(CITY);
			if (address.has(STATE)) stop.state = address.getString(STATE);
			if (address.has(ZIP)) stop.zip = address.getString(ZIP);
			if (address.has(COUNTRY)) stop.country = address.getString(COUNTRY);
		
			// either both ok or just invalid pair
			if (address.has(LAT) && address.has(LON)) {
				stop.lat = address.getInt(LAT);
				stop.lon = address.getInt(LON);
				stop.stopType = STOP_CURRENT_LOCATION;
			}
		}catch(JSONException ex){}
		
		return stop;
    }
    
    public static boolean isCanadianCarrier(DataHandler handler)
	{
		if(handler == null) return false;
		boolean isCanadianCarrier = false;
		String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
		if("Rogers".equals(carrier) || "Fido".equals(carrier) || "BellMob".equals(carrier) || "VMC".equals(carrier))
		{
			isCanadianCarrier = true;
		}

		return isCanadianCarrier;
	}
	
    
	public static TimeZone getTimeZoneFromOTService(double lat, double lon)
			throws RemoteException, OTServiceFault {
		try {
			OTServiceStub stub = new OTServiceStub(wsOtsURL);
			log.debug("wsOtsURL"+wsOtsURL);
			QueryRequestDTO request = new QueryRequestDTO();
			request.setClientName("B/S Movie Service");
			request.setClientVersion("6.0");
			request.setTransactionId("UNKNOWN");

			com.telenav.datatypes.address.v20.GeoCode gc = new com.telenav.datatypes.address.v20.GeoCode();
			gc.setLatitude(lat);
			gc.setLongitude(lon);
			request.setLocation(gc);

			QueryResponseDTO response = stub.getTimezone(request);
			log.debug("ResponseCode: "
					+ response.getResponseStatus().getStatusCode()
					+ "; ResposneMessage: "
					+ response.getResponseStatus().getStatusMessage());
			if (response.getResponseStatus().getStatusCode().equals(
					ResponseCodeEnum._OK)) {
				log.debug("OlsonTimeZone string value: "
						+ response.getTimezone().getStringValue());
				return TimeZone.getTimeZone(response.getTimezone()
						.getStringValue());
			} else {
				log.debug("throw new OTServiceFault()");
				throw new OTServiceFault();
			}

		} catch (RemoteException re) {
			log.debug(re);
			log.debug("throw new RemoteException()");
			throw new RemoteException();
		} catch (OTServiceFault e) {
			log.debug(e);
			log.debug("throw new OTServiceFault()");
			throw new OTServiceFault();
		}
	}
	//public static 
	
	 /**
     * check if this is rim platform & touch
     * @param handler
     * @return
     */
	public static boolean isRimTouch(DataHandler handler) {
		UserProfile userProfile = getUserProfile(handler);
		boolean isRimTouch = isRim(userProfile)
				&& getTouchFlag(userProfile);
		return isRimTouch;
	}
	
	 /**
     * check if this is rim platform & Non-touch
     * @param handler
     * @return
     */
	public static boolean isRimNonTouch(DataHandler handler) {
		UserProfile userProfile = getUserProfile(handler);
		boolean isRimNonTouch = isRim(userProfile)
				&& (!getTouchFlag(userProfile));
		return isRimNonTouch;
	}
	
	public static UserProfile getUserProfile(DataHandler handler)
    {
    	UserProfile userProfile = new UserProfile();

        userProfile.setVersion(handler.getClientInfo(DataHandler.KEY_VERSION));
        userProfile.setPlatform(handler.getClientInfo(DataHandler.KEY_PLATFORM));
        userProfile.setDevice(handler.getClientInfo(DataHandler.KEY_DEVICEMODEL));
        userProfile.setLocale(handler.getClientInfo(DataHandler.KEY_LOCALE));
        userProfile.setCarrier(handler.getClientInfo(DataHandler.KEY_CARRIER));
        userProfile.setProduct(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));
        userProfile.setRegion(handler.getClientInfo(DataHandler.KEY_REGION));
        userProfile.setScreenWidth(handler.getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH));
        userProfile.setScreenHeight(handler.getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT));
    	
        return userProfile;
    }
	public static boolean isRim(UserProfile profile) {
		boolean isRim = false;
		DsmRuleHolder dsmHolder = ResourceHolderManager
				.getResourceHolder("dsm");
		String flag = getString(dsmHolder.getDsmResponseBy(profile, "isRim",
				null));
		if ("TRUE".equalsIgnoreCase(flag)) {
			isRim = true;
		}
		return isRim;
	}
	
	public static boolean getTouchFlag(UserProfile profile){
		boolean supportTouch = false;
		DsmRuleHolder dsmHolder = ResourceHolderManager.getResourceHolder("dsm");
		String flag = getString(dsmHolder.getDsmResponseBy(profile,"supportTouch", null));
		if("TRUE".equalsIgnoreCase(flag))
		{
			supportTouch = true;
		}
		return supportTouch;
	}
	
	public static String getString(String string)
	{
		if(string == null) return "";
		
		return string.trim();
	}

	public static String getWsMovieTicketURL() {
		return wsMovieTicketURL;
	}

	public static String getWsMovieURL20() {
		return wsMovieURL20;
	}
	
	public static String getCheckOutURL() {
		return checkOutURL;
	}

	public static String getCheckOutURL_test() {
		return checkOutURL_test;
	}

	public static String getCheckOutURL_stage() {
		return checkOutURL_stage;
	}
	
	public static MovieImageRequestDTO createMovieImageRequestDTO(DataHandler handler, String movieId) {
		MovieImageRequestDTO iReq = new MovieImageRequestDTO();
        iReq.setMovieId(movieId);
        
        MovieImageType movieImageType = MovieImageType.selectImage(handler);
        
        iReq.setMovieImageType(movieImageType.getType());
        iReq.setDisplayHeight(movieImageType.getHeight());
        iReq.setDisplayWidth(movieImageType.getWidth());
        
        return iReq;
	}
}
