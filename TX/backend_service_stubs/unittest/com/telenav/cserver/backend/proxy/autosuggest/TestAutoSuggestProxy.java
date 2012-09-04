package com.telenav.cserver.backend.proxy.autosuggest;



import org.apache.commons.httpclient.HttpClient;

import junit.framework.TestCase;

/**
 * AutoSuggestProxy.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Apr 13, 2011
 *
 */
public class TestAutoSuggestProxy extends TestCase
{
    private AutoSuggestProxy proxy = null;
    
    @Override
    protected void setUp() throws Exception
    {
        proxy = new TestableAutoSuggestProxy();
    }

    public void testGetSuggestList4Success()
    {
        String url = "http://10.224.88.171/inputsuggestion?hl=en&format=json&num=10&qu=coff&lat=37.37453&lon=-121.99983&ptn=5198887465&tid=1302597046686&uid=9422929&mapdataset=Navteq";
        GetSuggestListResponse response = proxy.getSuggestList(url);
        assertNotNull("response shoud not be null",response);
        assertTrue("status code should be "+GetSuggestListResponse.STATUS_SUCESS+"but is "+response.getStatusCode(),GetSuggestListResponse.STATUS_SUCESS == response.getStatusCode());
        assertTrue("should have suggest list",response.getSuggestList().size() > 0 );
    }
    
    public void testGetSuggestList4Fail()
    {
        String url = "http://ff";
        GetSuggestListResponse response = proxy.getSuggestList(url);
        assertNotNull("response shoud not be null",response);
        assertTrue("status code should not be "+GetSuggestListResponse.STATUS_SUCESS+"but is "+response.getStatusCode(),GetSuggestListResponse.STATUS_SUCESS != response.getStatusCode());
    }
    
    public void testCreateUrl()
    {
        String expect = "http://hqd-poiuserstore-04.telenav.com:8080/inputsuggestion?hl=en&format=json&num=10&qu=coff&lat=37.37453&lon=-121.99983&ptn=4165776630&tid=1302597046686&uid=9422929&mapdataset=Navteq";
        GetSuggestListRequest request = new GetSuggestListRequest();
        request.setCount(10);
        request.setQueryString("coff");
        request.setLat("37.37453");
        request.setLon("-121.99983");
        request.setPtn("4165776630");
        request.setTransactionId("1302597046686");
        request.setUserId("9422929");
        request.setMapDataSet("Navteq");
        String url = proxy.createSuggestListUrl(request);
        assertEquals("the url should be as expect",expect,url);
    }
    
    class TestableAutoSuggestProxy extends AutoSuggestProxy
    {

        @Override
        protected HttpClient getHttpClient()
        {
           return new HttpClient();
        }

        @Override
        protected String getServiceUrl()
        {
            return "http://hqd-poiuserstore-04.telenav.com:8080/inputsuggestion?hl=en&format=json&num={0}&qu={1}&lat={2}&lon={3}&ptn={4}&tid={5}&uid={6}&mapdataset={7}";
        }
        
    }

}
