/*
 *  @file GpsEncryptor.java	2006-12-25
 *  @copyright (c) 2005 Televigation, Inc.
 */
package com.telenav.cserver.backend.navstar;

import org.apache.log4j.Logger;


/**
 * copy from nav_map_service com.telenav.cserver.nav.util.GpsEncryptor
 * 
 * <pre>
 *  Singleton utility class to encrypt lat/lon by JNI
 * </pre>
 * 
 * @author mmwang
 * @version 1.0 2010-7-14
 * @author zywang
 * 
 */
public class GpsEncryptor
{

	private static Logger logger = Logger.getLogger(GpsEncryptor.class);
	
	private static GpsEncryptor encryptor = new GpsEncryptor();
	
	private static boolean loadLibSuccess = true;
	static
	{
		try
		{
			System.loadLibrary("encryption");
		}
		catch(Throwable t)
		{
			loadLibSuccess = false;
			t.printStackTrace();
			logger.fatal(t, t);
		}
	}
	
	private GpsEncryptor()
	{
	}

	/**
	 * Get singleton instance
	 * 
	 * @return
	 */
	public static GpsEncryptor getInstance()
	{
		return encryptor;
	}

	/**
	 * Use dll to encrypt
	 * 
	 * @param
	 * @param
	 */
	public int[] encryptGps(int lat, int lon, int time)
	{
		int[] rawGps = new int[3];
		rawGps[0] = lat;
		rawGps[1] = lon;
		rawGps[2] = time;

        //if the lon is not in china area,no need encrypt here
        if(lon < 6762609L || lon > 15316465L
         ||lat <  729703L || lat > 5618759L)
        {
            return rawGps;
        }

        
		

		if(loadLibSuccess)
		{
			int[] newGps = new int[3];
			// use JNI to encrypt
			encrypt(rawGps, newGps);
			return newGps;
		}
		else
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("can't encrypt gps, return raw data");
			}
			return rawGps;
		}

		
	}

	private native boolean encrypt(int raw_fix[], int new_fix[]);

//	public static void main(String[] args)
//	{
//		final int FIRST_LAT = 3131042;
//		final int FIRST_LON = 11167956;
//		final int OFFSET = 50000;
//		final int LAT = 0, LON = 1, TIME = 2;
//		int[] raw_fix = new int[3];
//		int new_fix[] = new int[3];
//		int newnew_fix[] = new int[3];
//		GpsEncryptor ge = new GpsEncryptor();
//
//		 Random rand = new Random();
//		 for(int i = 0; i < 100000; i++)
//		 {
//		 raw_fix[0] = FIRST_LAT + (rand.nextInt(OFFSET) - OFFSET/2);
//		 raw_fix[1] = FIRST_LON + (rand.nextInt(OFFSET) - OFFSET/2);
//		 //System.out.println(raw_fix[0] + "," + raw_fix[1]);
//		 raw_fix[2] = (int)(116712556343L /100); //in seconds, not 0.01 sec!!!
//		    
//		 ge.encrypt(raw_fix, new_fix);
//		 ge.encrypt(new_fix, newnew_fix);
//		 int offset_lat = (newnew_fix[0] - new_fix[0]) - (new_fix[0] -
//		 raw_fix[0]);
//		 int offset_lon = newnew_fix[1] - new_fix[1] - (new_fix[1] -
//		 raw_fix[1]);
//		 if(offset_lat >= 3 || offset_lon >=3 )
//		 System.out.println(i + ":" + offset_lat + "," + offset_lon);
//		 }
//		        
//		raw_fix[0] = 3123855;
//		raw_fix[1] = 12147801;
//		raw_fix[2] = 1169294955;
//		ge.encrypt(raw_fix, new_fix);
//		System.out.println((new_fix[0] + 3) + "," + (new_fix[1] + 10));
//
//		 System.out.println("lat = " + new_fix[0] + ", new_lon = " +
//		 new_fix[1]);
//	}
}
