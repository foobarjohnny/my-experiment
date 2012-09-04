import com.telenav.cserver.dsr.framework.ProcessConfiguration;
import com.telenav.cserver.dsr.framework.ProcessConfigurationFactory;
import com.telenav.cserver.dsr.handler.DefaultResultHandler;
import com.telenav.cserver.dsr.proxy.TNDSRProxy;

import junit.framework.TestCase;


public class TestProcessConfig extends TestCase
{

	public void testConfig()
	{
		ProcessConfiguration config = ProcessConfigurationFactory.getConfiguration(12) ;
		
		assertTrue(config.getProxy() instanceof TNDSRProxy) ;
		assertTrue(config.getHandler() instanceof DefaultResultHandler) ;
		
	}
}
