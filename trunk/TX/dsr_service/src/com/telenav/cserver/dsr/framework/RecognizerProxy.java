package com.telenav.cserver.dsr.framework;

import com.telenav.cserver.dsr.ds.RecContext;

public interface RecognizerProxy
{
	public ProcessObject doRecognition(ProcessObject procObj, RecContext context, byte[] audioData) ;
	
	public void init(ProcessObject procObj);
	public void addAudioSeg(byte[] audioData);
	public void endAudio();
	public void release();
	public void addResultListener(RecognizerResultListener listener);
}
