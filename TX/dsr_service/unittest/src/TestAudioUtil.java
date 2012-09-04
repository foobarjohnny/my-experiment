import java.io.File;

import com.telenav.cserver.dsr.util.AudioUtil;

import junit.framework.TestCase;


public class TestAudioUtil extends TestCase
{
//	public void testRaw2Wav()
//	{
//		byte[] rawData = TestUtil.loadData(new File("unittest/data/8ksample.raw")) ;
//		
//		byte[] wavData = AudioUtil.rawPCMToWav(rawData,8000) ;
//		
//		TestUtil.writeData(new File("unittest/data/test.wav"), wavData) ;
//	}
	
	public void test16kSpeeksTo8kWav()
	{
		byte[] speexData = TestUtil.loadData(new File("unittest/data/sample.speex")) ;
		
		byte[] ogg = AudioUtil.convert2ogg(speexData) ;
		
		TestUtil.writeData(new File("unittest/data/16kogg.speex"), ogg) ;
		
		byte[] pcm = AudioUtil.decodeSpeex(speexData) ;
		
		TestUtil.writeData(new File("unittest/data/8ktest.pcm"), pcm) ;
		
		byte[] wavData = AudioUtil.rawPCMToWav(pcm,8000) ;
		
		TestUtil.writeData(new File("unittest/data/8ktest.wav"), wavData) ;
	}
}
