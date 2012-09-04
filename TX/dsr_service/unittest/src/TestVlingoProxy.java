import java.io.File;

import com.telenav.j2me.datatypes.Stop ;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.framework.ProxyResponse;
import com.telenav.cserver.dsr.framework.RecognizerProxy;
import com.telenav.cserver.dsr.proxy.VlingoProxy;
import com.telenav.cserver.dsr.util.ResourceConst;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

import junit.framework.TestCase;


public class TestVlingoProxy extends TestCase
{
	private static byte[] amrData ;
	private static byte[] speexData ;
	private static UserProfile user ;
	private static Stop location ;
	
	static 
	{
		// amrData = TestUtil.loadData(new File("unittest/data/sample.amr")) ;
		speexData = TestUtil.loadData(new File("unittest/data/sample2.speex")) ;
		
		user = new UserProfile() ;
		user.setUserId("4083388211") ;
		
		location = new Stop() ;
		location.country = "us";
		location.stopId = "0";
		location.lat=3735550;
		location.lon=-12195424;
		
	}
	
	public void testDoRecognition()
	{
		ProxyResponse response ;
		RecContext context ;
		
		// Test AMR
		context = new RecContext((byte)12, (byte) ResourceConst.FMT_AMR,
									location, user, new TnContext(), "");
		
		RecognizerProxy proxy = new VlingoProxy() ;
		
		//response = proxy.doRecognition(context, amrData) ;
		//System.out.println(response) ;
		
		
		// Test SPEEX
		context = new RecContext((byte)12, (byte) ResourceConst.FMT_SPEEX,
									location, user, new TnContext(), "");
		
		//response = proxy.doRecognition(context, speexData) ;
		//System.out.println(response) ;
	}
	
}
