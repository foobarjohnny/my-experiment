package com.telenav.cserver.dsr.protocol;

public interface ProtocolConstants
{
	public static final String DSR_AUDIO_TYPE = "Dsr_Audio";
	public static final String DSR_SIGNAL_TYPE = "Dsr_Signal";
	public static final String DSR_REPONSE_TYPE = "Dsr_Response";
	public static final String USER_PROFILE_TYPE = "UserProfile";
	public static final String GPS_LIST_TYPE = "ProtoGpsList";
	
	public static final String AUDIO = "audio";
	public static final String REC_CONTEXT = "recContext";
	public static final String SIGNAL = "signal";
	
	// POSSIBLE SIGNAL VALUES
	public static final String INIT_RECOGNIZER_SIGNAL = "INIT_RECOGNIZER";
	public static final String END_AUDIO_SIGNAL = "END_AUDIO";
	public static final String CANCEL_SIGNAL = "DSR_CANCEL";
	
}
