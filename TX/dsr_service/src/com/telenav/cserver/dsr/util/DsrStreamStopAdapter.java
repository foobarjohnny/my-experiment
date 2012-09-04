package com.telenav.cserver.dsr.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.telenav.audio.streaming.common.Util;
import com.telenav.j2me.datatypes.Stop;

public class DsrStreamStopAdapter
{
    private static Logger logger = Logger.getLogger(DsrStreamStopAdapter.class.getName());
	
	private Stop stop;
	public DsrStreamStopAdapter(Stop stop)
	{
		this.stop = stop;
	}

	public byte[] toByteArray()
	{
		byte[] result = null;
		ByteArrayOutputStream baos = null;
		try
		{
			baos = new ByteArrayOutputStream();
			baos.write(DsrStreamUtil.getIntBytes(stop.lat));
			baos.write(DsrStreamUtil.getIntBytes(stop.lon));
			writeString(baos, stop.firstLine);
			writeString(baos, "");
			//writeString(baos, stop.getCrossStreetName());//TODO need implement
			writeString(baos, stop.city);
			writeString(baos, stop.state);
			writeString(baos, stop.country);
			writeString(baos, stop.zip);
			writeString(baos, stop.label);
			writeBytes(baos, this.stop.getLastLineAudio());
			if(stop.stopId.length() == 0)
			{
				baos.write(DsrStreamUtil.getIntBytes(0));
			}
			else
			{
				int stopId = 0;
				try{
					stopId = Integer.parseInt(stop.stopId);
				}catch(Exception ex){
					logger.log(Level.SEVERE, "parse stopId:"+stop.stopId, ex);
				}
                if(stopId >= 0)
			    {
			    	baos.write(DsrStreamUtil.getIntBytes(stopId));
			    }
//                else if(stopId == -1)
//			    {
//			    	baos.write(DsrStreamUtil.getIntBytes(0));
//			    }
			    else
			    {
			    	baos.write(DsrStreamUtil.getIntBytes(0));
			    }
			}		    
			result = baos.toByteArray();
		}
		catch (Exception e)
		{
            logger.log(Level.SEVERE, "parse stopId:"+stop.stopId, e);
		}
		finally
		{
			if (baos != null)
			{
				try
				{
					baos.close();
				}
				catch (IOException ignored)
				{
				}
				finally
				{
					baos = null;
				}
			}
		}
		return result;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
        sb.append("id: ").append(this.stop.stopId);
        sb.append(", country: ").append(this.stop.country);
        sb.append(", state: ").append(this.stop.state);
        sb.append(", city: ").append(this.stop.city);
        sb.append(", firstline: ").append(this.stop.firstLine);
		sb.append(", crossStreetName: ");
		//sb.append(", crossStreetName: " + this.stop.getCrossStreetName());TODO  need implement
        sb.append(", zipcode: ").append(this.stop.zip);
        sb.append(", lat: ").append(this.stop.lat);
        sb.append(", lon: ").append(this.stop.lon);
        sb.append(", label: ").append(this.stop.label);

		return sb.toString();
	}

	private void writeString(ByteArrayOutputStream baos, String str) throws IOException
	{
		if (str != null)
		{
        	byte[] bs = str.getBytes("UTF-8");
            baos.write(DsrStreamUtil.getIntBytes(bs.length));
            baos.write(bs);
		}
		else
		{
			baos.write(DsrStreamUtil.getIntBytes(0));
		}
	}

	private void writeBytes(ByteArrayOutputStream baos, byte[] bs) throws IOException
	{
		if (bs != null)
		{
			baos.write(DsrStreamUtil.getIntBytes(bs.length));
		}
		else
		{
			baos.write(DsrStreamUtil.getIntBytes(0));
		}
		if (bs != null && bs.length > 0)
		{
			baos.write(bs);
		}
	}

}
