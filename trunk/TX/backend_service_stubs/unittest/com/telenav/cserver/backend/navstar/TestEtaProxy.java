package com.telenav.cserver.backend.navstar;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Test;
import com.telenav.cserver.backend.datatypes.map.LatLonPoint;
import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.backend.proxy.navstar.EtaProxy;
import com.telenav.cserver.backend.proxy.navstar.EtaProxyHelper;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.impl.interceptor.TnContextInterceptor;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.eta.EtaStub.ETARequest;
import com.telenav.services.eta.EtaStub.ETAResponse;

public class TestEtaProxy
{
    @Test
    public void getEtaWithNTDataSet()
    {
    	UserProfile profile = new UserProfile();
    	TnContext tc= new TnContext();
    	profile.setRegion("NA");
    	tc.addProperty(TnContext.PROP_MAP_DATASET, "Navteq");
    	tc.addProperty(TnContextInterceptor.NAV_FLOW_DATA_SRC_KEY, "UGT,INRIXCANADA,ITIS,CCACIR");
    	  	
        String param = EtaProxyHelper.createEtaStartEndParam(new LatLonPoint(37.35242, -121.99751), new LatLonPoint(37.40325, -122.10621),
            null);
        ETARequest request=new ETARequest();
        request.addStartEnd(param);
        ETAResponse resp = BackendProxyManager.getBackendProxyFactory().getBackendProxy(EtaProxy.class).getEta(request, profile, tc);
        Assert.assertTrue(100==resp.getStatusCode());
    }
    
    
    
    @Test
    public void getEtaWithTADataSet()
    {
    	UserProfile profile = new UserProfile();
    	TnContext tc= new TnContext();
    	profile.setRegion("NA");
    	tc.addProperty(TnContext.PROP_MAP_DATASET, "Navteq");
    	tc.addProperty(TnContextInterceptor.NAV_FLOW_DATA_SRC_KEY, "UGT,INRIXCANADA,ITIS,CCACIR");
    	    	
        String param = EtaProxyHelper.createEtaStartEndParam(new LatLonPoint(37.35242, -121.99751), new LatLonPoint(37.40325, -122.10621),
            null);
        ETARequest request=new ETARequest();
        request.addStartEnd(param);
        ETAResponse resp = BackendProxyManager.getBackendProxyFactory().getBackendProxy(EtaProxy.class).getEta(request, profile, tc);
        Assert.assertTrue(100==resp.getStatusCode());
    }
    
       
//    @Test
//    public void getUrl_nullTrafficData() throws Exception{
//     	Map<String, String> urlMap = new HashMap<String, String>();
//       	urlMap.put("NA_TeleAtlas", "http://eta-ta.mypna.com:8080/tnws/services/eta");
//       	urlMap.put("NA_TeleAtlas_UGT", "http://eta-ta-tntraffic.mypna.com:8080/tnws/services/eta");
//       	String key = "NA_TeleAtlas";
//       	String url = BackendProxyManager.getBackendProxyFactory().getBackendProxy(EtaProxy.class).getUrl(urlMap, key);
//       	
//       	Assert.assertEquals("http://eta-ta.mypna.com:8080/tnws/services/eta", url);
//    }
//        
//    @Test
//    public void getUrl_INRIX() throws Exception{
//       	Map<String, String> urlMap = new HashMap<String, String>();
//       	urlMap.put("NA_TeleAtlas", "http://eta-ta.mypna.com:8080/tnws/services/eta");
//       	urlMap.put("NA_TeleAtlas_UGT", "http://eta-ta-tntraffic.mypna.com:8080/tnws/services/eta");
//       	String key = "NA_TeleAtlas_INRIX";
//       	String url = BackendProxyManager.getBackendProxyFactory().getBackendProxy(EtaProxy.class).getUrl(urlMap, key);
//       	
//       	Assert.assertEquals("http://eta-ta.mypna.com:8080/tnws/services/eta", url);
//    }
//        
//    @Test
//    public void getUrl_UGT() throws Exception{
//       	Map<String, String> urlMap = new HashMap<String, String>();
//       	urlMap.put("NA_TeleAtlas", "http://eta-ta.mypna.com:8080/tnws/services/eta");
//       	urlMap.put("NA_TeleAtlas_UGT", "http://eta-ta-tntraffic.mypna.com:8080/tnws/services/eta");
//       	String key = "NA_TeleAtlas_UGT";
//       	String url = BackendProxyManager.getBackendProxyFactory().getBackendProxy(EtaProxy.class).getUrl(urlMap, key);
//       	
//       	Assert.assertEquals("http://eta-ta-tntraffic.mypna.com:8080/tnws/services/eta", url);
//    }
//        
//    @Test
//    public void getUrl_NULL() throws Exception{
//       	Map<String, String> urlMap = new HashMap<String, String>();
//     	urlMap.put("NA_TeleAtlas", "http://eta-ta.mypna.com:8080/tnws/services/eta");
//       	urlMap.put("NA_TeleAtlas_UGT", "http://eta-ta-tntraffic.mypna.com:8080/tnws/services/eta");
//       	String key = null;
//       	String url = BackendProxyManager.getBackendProxyFactory().getBackendProxy(EtaProxy.class).getUrl(urlMap, key);
//       	
//       	Assert.assertEquals(null, url);
//    }
        
    @Test
    public void createKey_INRIX() throws Exception{
      	UserProfile profile = new UserProfile();
       	TnContext tc= new TnContext();
       	profile.setRegion("NA");
       	tc.addProperty(TnContextInterceptor.NAV_FLOW_DATA_SRC_KEY, "INRIX,INRIXCANADA,ITIS,CCACIR");
       	tc.addProperty(TnContext.PROP_MAP_DATASET, "TeleAtlas");
       	
       	String key = BackendProxyManager.getBackendProxyFactory().getBackendProxy(EtaProxy.class).createKey(profile, tc);
       	
       	assertEquals("NA_TeleAtlas_INRIX", key);
    }
        
    @Test
    public void createKey_UGT() throws Exception{
       	UserProfile profile = new UserProfile();
       	TnContext tc= new TnContext();
       	profile.setRegion("NA");
       	tc.addProperty(TnContextInterceptor.NAV_FLOW_DATA_SRC_KEY, "UGT,INRIXCANADA,ITIS,CCACIR");
       	tc.addProperty(TnContext.PROP_MAP_DATASET, "TeleAtlas");
       	
       	String key = BackendProxyManager.getBackendProxyFactory().getBackendProxy(EtaProxy.class).createKey(profile, tc);
       	
       	assertEquals("NA_TeleAtlas_UGT", key);
    }
        
    @Test
    public void createKey_NULL() throws Exception{
       	UserProfile profile = new UserProfile();
       	TnContext tc= new TnContext();
       	
       	String key = BackendProxyManager.getBackendProxyFactory().getBackendProxy(EtaProxy.class).createKey(profile, tc);
       	
       	assertEquals("", key);
       	
    }
}
