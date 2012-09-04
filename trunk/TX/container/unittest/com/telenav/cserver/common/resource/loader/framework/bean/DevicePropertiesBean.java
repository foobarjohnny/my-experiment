package com.telenav.cserver.common.resource.loader.framework.bean;

public class DevicePropertiesBean
{
	private String maxPayloadSize; // MAX_PAYLOAD_SIZE
	private String navEnableLaneAddist; // NAV_ENABLE_LANE_ASSIST
	private String unsupportedMapIncident; // UNSUPPORTED_MAP_INCIDENTS

	private String needAudio; // NEED_AUDIO
	private String needServerDrivenParamters; // NEED_SERVER_DRIVEN_PARAMETERS
	private String needBarCollection; // NEED_BAR_COLLECTION
	private String smallMemory; // SMALL_MEMORY
	private String audioProfileKey; // AUDIO_PROFILE_KEY
	private String newAppVersion; // NEW_APP_VERSION
	private String paramsVersion; // PARAMS_VERSION
	private String audioFormat; // AUDIO_FORMAT
	private String imageType; // IMAGE_TYPE

	public String getMaxPayloadSize()
	{
		return maxPayloadSize;
	}

	public void setMaxPayloadSize(String maxPayloadSize)
	{
		this.maxPayloadSize = maxPayloadSize;
	}

	public String getNavEnableLaneAddist()
	{
		return navEnableLaneAddist;
	}

	public void setNavEnableLaneAddist(String navEnableLaneAddist)
	{
		this.navEnableLaneAddist = navEnableLaneAddist;
	}

	public String getUnsupportedMapIncident()
	{
		return unsupportedMapIncident;
	}

	public void setUnsupportedMapIncident(String unsupportedMapIncident)
	{
		this.unsupportedMapIncident = unsupportedMapIncident;
	}

	public String getNeedAudio()
	{
		return needAudio;
	}

	public void setNeedAudio(String needAudio)
	{
		this.needAudio = needAudio;
	}

	public String getNeedServerDrivenParamters()
	{
		return needServerDrivenParamters;
	}

	public void setNeedServerDrivenParamters(String needServerDrivenParamters)
	{
		this.needServerDrivenParamters = needServerDrivenParamters;
	}

	public String getNeedBarCollection()
	{
		return needBarCollection;
	}

	public void setNeedBarCollection(String needBarCollection)
	{
		this.needBarCollection = needBarCollection;
	}

	public String getSmallMemory()
	{
		return smallMemory;
	}

	public void setSmallMemory(String smallMemory)
	{
		this.smallMemory = smallMemory;
	}

	public String getAudioProfileKey()
	{
		return audioProfileKey;
	}

	public void setAudioProfileKey(String audioProfileKey)
	{
		this.audioProfileKey = audioProfileKey;
	}

	public String getNewAppVersion()
	{
		return newAppVersion;
	}

	public void setNewAppVersion(String newAppVersion)
	{
		this.newAppVersion = newAppVersion;
	}

	public String getParamsVersion()
	{
		return paramsVersion;
	}

	public void setParamsVersion(String paramsVersion)
	{
		this.paramsVersion = paramsVersion;
	}

	public String getAudioFormat()
	{
		return audioFormat;
	}

	public void setAudioFormat(String audioFormat)
	{
		this.audioFormat = audioFormat;
	}

	public String getImageType()
	{
		return imageType;
	}

	public void setImageType(String imageType)
	{
		this.imageType = imageType;
	}

	@Override
	public String toString()
	{
		return "DevicePropertiesBean [maxPayloadSize=" + maxPayloadSize + ", navEnableLaneAddist=" + navEnableLaneAddist
				+ ", unsupportedMapIncident=" + unsupportedMapIncident + ", needAudio=" + needAudio + ", needServerDrivenParamters="
				+ needServerDrivenParamters + ", needBarCollection=" + needBarCollection + ", smallMemory=" + smallMemory
				+ ", audioProfileKey=" + audioProfileKey + ", newAppVersion=" + newAppVersion + ", paramsVersion=" + paramsVersion
				+ ", audioFormat=" + audioFormat + ", imageType=" + imageType + "]";
	}
}
