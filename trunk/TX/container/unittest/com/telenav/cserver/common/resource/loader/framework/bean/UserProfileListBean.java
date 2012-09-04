package com.telenav.cserver.common.resource.loader.framework.bean;

import java.util.List;

import com.telenav.cserver.framework.UserProfile;

public class UserProfileListBean
{
	private List<UserProfile> userProfiles;

	public List<UserProfile> getUserProfiles()
	{
		return userProfiles;
	}

	public void setUserProfiles(List<UserProfile> userProfiles)
	{
		this.userProfiles = userProfiles;
	}

	@Override
	public String toString()
	{
		return "UserProfileListBean [userProfiles=" + userProfiles + "]";
	}
}
