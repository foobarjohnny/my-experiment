import com.telenav.cserver.dsr.formatter.ResponseFormatterFactory;
import com.telenav.cserver.dsr.formatter.TxNodeFormatter;
import com.telenav.cserver.dsr.framework.ProcessConfiguration;
import com.telenav.cserver.dsr.framework.ProcessConfigurationFactory;
import com.telenav.cserver.dsr.framework.ResponseFormatter;
import com.telenav.cserver.dsr.handler.DefaultResultHandler;
import com.telenav.cserver.dsr.proxy.TNDSRProxy;

import junit.framework.TestCase;


public class TestFormatterFactory extends TestCase
{

	public void testConfig()
	{
		ResponseFormatter formatter50 = ResponseFormatterFactory.getResponseFormatter(50) ;
		ResponseFormatter formatter55 = ResponseFormatterFactory.getResponseFormatter(55) ;
		ResponseFormatter formatter551 = ResponseFormatterFactory.getResponseFormatter(551) ;
		
		assertTrue(formatter50 instanceof TxNodeFormatter) ;
		
		assertTrue(formatter55 instanceof TxNodeFormatter) ;
		
		assertTrue(formatter551 instanceof TxNodeFormatter) ;
		
	}
}
