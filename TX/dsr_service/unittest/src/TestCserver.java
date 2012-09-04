

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * @author Administrator
 */
public class TestCserver extends TestCase
{
	public static Test suite()
	{
		TestSuite suite =  new TestSuite();
		suite.addTestSuite(TestAudioUtil.class) ;
		return suite;
	}
}
