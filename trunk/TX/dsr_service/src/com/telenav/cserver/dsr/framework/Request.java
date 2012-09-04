package com.telenav.cserver.dsr.framework;

import com.telenav.cserver.dsr.ds.RecContext;

public interface Request
{
	public static final byte NOT_DEFINED = -1 ;
	
	public byte[] getAudioData();

	public RecContext getContext();

	public boolean setMeta(byte[] metaData);
	
	public void setProtocolVersion(byte version);

	public boolean setAudio(byte[] audioData);

	public byte getProtocolVersion();

}