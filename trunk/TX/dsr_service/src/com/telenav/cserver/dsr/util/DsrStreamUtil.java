package com.telenav.cserver.dsr.util;

import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.server.ServerSetting;

import java.io.InputStream;
import java.util.logging.Logger;

public class DsrStreamUtil
{
	private static Logger logger = Logger.getLogger(DsrStreamUtil.class.getName());
	
    public static byte[] read(InputStream is) {
        try {
            int len = 0;
            for (int i = 0; i < 4; i++) {
                int b = is.read();
                if (b == -1) {
                    // Socket closed
                    return null;
                }
                len += (b << ((3 - i) * 8));

                //len += (b << (i * 8));
            }
            logger.fine("read length:" + len);
            if (len >= ServerSetting.MAX_REQUEST_LENGTH) {
                //too long request
                logger.severe("too long request length:" + len);
                return null;
            }
            int READ_SIZE = 1024;
            byte[] buff = new byte[len];
            int totalReadLen = 0;

            while (totalReadLen < len) {
                int sizeWantToRead = Math.min(READ_SIZE, len - totalReadLen);
                int readLen = is.read(buff, totalReadLen, sizeWantToRead);

                logger.fine("sizeWantToRead:" + sizeWantToRead + " , totalReadLen:" + totalReadLen);
                logger.fine("readLen:" + readLen);
                if (readLen <= 0) {
                    return null;
                }
                totalReadLen += readLen;
            }
            return buff;

        } catch (Exception e) {
            logger.severe("Dsr respnse read failed.");
        }
        return null;
    }

	public static byte[] generateMeta(byte[] passcode, byte type, byte fmt, Stop stop)
	{
		DsrStreamStopAdapter stopAdapter = new DsrStreamStopAdapter(stop);
		byte[] stopBs = stopAdapter.toByteArray();

		byte[] meta = new byte[32 + 1 + 1 + 4 + stopBs.length];
		if (passcode == null || passcode.length < 32)
		{
			byte[] tmp = new byte[32];
			if (passcode != null)
			{
				System.arraycopy(passcode, 0, tmp, 0, passcode.length);
			}
			passcode = tmp;
		}
		System.arraycopy(passcode, 0, meta, 0, 32);
		meta[32] = type;
		meta[33] = fmt;
		DsrStreamUtil.getIntBytes(stopBs.length, meta, 34);
		System.arraycopy(stopBs, 0, meta, 38, stopBs.length);
		return meta;
	}
	
	public static byte[] generateMetaHttpRequest(byte[] contents, byte type, byte fmt, Stop stop, TnContext tnContext)
	{
		//TODO iphone type hack
		if(type == 13){
			type = 11;
		}else if(type == 14){
			type = 9;
		}else if(type == 15){
			type = 6;
		}
        type |= 0x10;
		DsrStreamStopAdapter stopAdapter = new DsrStreamStopAdapter(stop);
		byte[] stopBs = stopAdapter.toByteArray();
	    byte[] contextBs = tnContext.toContextString().getBytes();
	    int offset = 0;
		//meta
	    byte[] buffer = new byte[14 + stopBs.length + contents.length + contextBs.length];
		buffer[0] = type;
		buffer[1] = fmt;
		offset += 2;
	    DsrStreamUtil.getIntBytes(stopBs.length, buffer, offset);
	    offset += 4;
	    DsrStreamUtil.getIntBytes(contents.length, buffer, offset);
	    offset += 4;
	    //Stop
	    System.arraycopy(stopBs, 0, buffer, offset, stopBs.length);
	    //contents
	    offset += stopBs.length;
	    System.arraycopy(contents, 0, buffer, offset, contents.length);
	    //mapdateSet
	    offset += contents.length;
	    DsrStreamUtil.getIntBytes(contextBs.length, buffer, offset);
	    offset += 4;
	    System.arraycopy(contextBs, 0, buffer, offset, contextBs.length);
	    return buffer;
	}

    public static byte[] generateMetaHttpRequest(RecContext context, byte[] audioData){
        return generateMetaHttpRequest(audioData, context.recType, context.audioFormat, context.location, context.tnContext);
    }

	public static byte[] getIntBytes(int value)
	{
		byte[] bs = new byte[4];
		for (int i = 0; i < 4; i++)
		{
			bs[i] = (byte) (value >> (32 - (i + 1) * 8));
		}
		return bs;
	}

	public static void getIntBytes(int value, byte[] buf, int offset)
	{
		if (buf == null || offset + 4 > buf.length)
		{
			return;
		}
		for (int i = 0; i < 4; i++)
		{
			buf[i + offset] = (byte) (value >> (32 - (i + 1) * 8));
		}
	}

	public static int getInt(byte[] buf, int offset)
	{
		if (buf == null || offset + 4 > buf.length)
		{
			return -1;
		}
		// int value
		int ret = 0;
		int tmp = 0;
		for (int i = 3; i >= 0; i--)
		{
			tmp = buf[i + offset];
			if (tmp < 0)
			{
				tmp += 128;
				tmp |= 0x80;
			}
			ret |= (tmp << (3 - i) * 8);
		}
		return ret;
	}
	
	public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }
}
