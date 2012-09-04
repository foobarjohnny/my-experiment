package com.telenav.cserver.matcher;

import org.easymock.IArgumentMatcher;

import com.telenav.cserver.framework.UserProfile;

public class UserProfileMatcher implements IArgumentMatcher {

	private UserProfile userProfile;

	public UserProfileMatcher(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof UserProfile)
			return true;
		
		return false;
	}

	@Override
	public void appendTo(StringBuffer arg0) {

	}

}
