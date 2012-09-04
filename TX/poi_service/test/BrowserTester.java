/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.poi.handler.POIServiceResponse;

/**
 * BrowserTester.java
 *
 * @author zyawng@telenav.cn
 * @version 1.0 2009-4-14
 *
 */
public class BrowserTester {
    
    private ProtocolRequestParser requestParser = null;
    private ProtocolResponseFormatter responseFormatter = null;
    
    public void setRequestParser(ProtocolRequestParser requestParser)
    {
        this.requestParser = requestParser;
    }
    
    public void setResponseFormatter(ProtocolResponseFormatter responseFormatter)
    {
        this.responseFormatter = responseFormatter;
    }
    
    public void excecute(HttpServletRequest httpRequest, HttpServletResponse response) throws Exception
    {
        ExecutorRequest [] executorRequests = requestParser.parse(httpRequest);
        
        POIServiceResponse executorResponse = new POIServiceResponse();
        
        if(executorRequests != null && executorRequests.length > 0)
        {
            ExecutorDispatcher ac = ExecutorDispatcher.getInstance();
            ac.execute(executorRequests[0], executorResponse, new ExecutorContext());
        }
        
        responseFormatter.format(httpRequest, new ExecutorResponse[]{executorResponse});
        
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception 
	{
	    String configFilePath = "presentation/poi.xml";

        ApplicationContext context = new ClassPathXmlApplicationContext(
                configFilePath);

//            // of course, an ApplicationContext is just a BeanFactory
//            BeanFactory factory = context;
//            return factory.getBean(objectName);

	    HttpServletRequest httpRequest = new FakeHttpServletRequest();
	    HttpServletResponse httpResponse = new FakeHttpServletResponse();
	    httpRequest.setAttribute("categoryid", "bank");

	    BrowserTester tester = (BrowserTester)context.getBean("BrowserTester");
	    tester.excecute(httpRequest, httpResponse);
	    
	    String value = (String)httpRequest.getAttribute("poiresult");
	    
	    System.out.println("poiresult is " + value);

	}

	
	
}
