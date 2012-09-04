 /**
     * (c) Copyright 2009 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.backend.xnav;

/**
 * The request aid to get trigger action for resend pin.
 * @author pzhang
 * 2010-08-03
 */
public class ResendPinRequest
{   
	private String ptn;
    private String userClient;
    private String userClientVersion;
    private String contextString;
    
   public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append("ResendPinRequest=[");
       sb.append(", ptn=").append(this.ptn);
       sb.append(", userClient=").append(this.userClient);
       sb.append(", userClientVersion=").append(this.userClientVersion);
       sb.append("]");
       return sb.toString();
   }

    /**
     * @return the userClient
     */
    public String getUserClient()
    {
        return userClient;
    }
    /**
     * @param userClient the userClient to set
     */
    public void setUserClient(String userClient)
    {
        this.userClient = userClient;
    }
    /**
     * @return the userClientVersion
     */
    public String getUserClientVersion()
    {
        return userClientVersion;
    }
    /**
     * @param userClientVersion the userClientVersion to set
     */
    public void setUserClientVersion(String userClientVersion)
    {
        this.userClientVersion = userClientVersion;
    }

	public String getPtn() {
		return ptn;
	}

	public void setPtn(String ptn) {
		this.ptn = ptn;
	}

	public String getContextString() {
		return contextString;
	}

	public void setContextString(String contextString) {
		this.contextString = contextString;
	}
   
}
