import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;



public class TestUtil
{

	public static byte[] loadData(File f)
	{
		InputStream is = null ;
		
		try
		{
			is = new FileInputStream(f) ;
			ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
			byte[] buf = new byte[1024] ;
			int length = 0 ;
			while ((length = is.read(buf)) > 0)
			{
				baos.write(buf, 0, length) ;
			}
			
			return baos.toByteArray() ;
		}
		catch (Exception e)
		{
			e.printStackTrace() ;
			return null ;
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close() ;
				}
				catch (Exception e)
				{}
			}
		}
	}
	
	public static void writeData(File f, byte[] audioData)
	{
		try
		{
			OutputStream os = new FileOutputStream(f) ;
			os.write(audioData) ;
			os.flush() ;
			os.close() ;
		}
		catch (Exception e)
		{
			e.printStackTrace() ;
		}
	}
}
