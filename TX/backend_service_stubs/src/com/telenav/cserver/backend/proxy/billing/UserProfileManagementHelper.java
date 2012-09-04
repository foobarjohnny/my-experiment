/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.billing;

import com.telenav.billing2.userprofile2.commontypes.dataTypes.CServerRequestSource;
import com.telenav.billing2.userprofile2.commontypes.dataTypes.CredentialType;
import com.telenav.billing2.userprofile2.commontypes.dataTypes.UserCredential;
import com.telenav.billing2.userprofile2.userprofile.dataTypes.GetUserProfilePreferencesRequest;
import com.telenav.billing2.userprofile2.userprofile.dataTypes.UpdateUserProfilePreferencesRequest;
import com.telenav.billing2.userprofile2.userprofile.dataTypes.UserProfilePreferencesForUpdate;

/**
 * UserProfileManagementHelper
 * @author kwwang
 *
 */
public class UserProfileManagementHelper {
	 public static GetUserProfilePreferencesRequest createGetUserProfilePreferencesRequest(String deviceId)
	 {
		 GetUserProfilePreferencesRequest request = new GetUserProfilePreferencesRequest();
		 request.setRequestSource(CServerRequestSource.TelenavCServer);
		 UserCredential credential = new UserCredential();
		 credential.setCredentialType(CredentialType.DEVICE_ID);
		 credential.setCredentialId(deviceId);
		 request.setUserCredential(credential);
		 return request;
	 }
	 
	 public static GetUserProfilePreferencesRequest createGetUserProfilePreferencesRequestByUserId(String userId)
	 {
		 GetUserProfilePreferencesRequest request = new GetUserProfilePreferencesRequest();
		 request.setRequestSource(CServerRequestSource.TelenavCServer);
		 UserCredential credential = new UserCredential();
		 credential.setCredentialType(CredentialType.USER_ID);
		 credential.setCredentialId(userId);
		 request.setUserCredential(credential);
		 return request;
	 }
	 
	 public static UpdateUserProfilePreferencesRequest createUpdateUserProfilePreferencesRequest(String userID, String firstName, String lastName)
	 {
		 UpdateUserProfilePreferencesRequest request = new UpdateUserProfilePreferencesRequest();
		 request.setRequestSource(CServerRequestSource.TelenavCServer);
		 UserCredential credential = new UserCredential();
		 credential.setCredentialType(CredentialType.USER_ID);
		 credential.setCredentialId(userID);
		 
		 UserProfilePreferencesForUpdate uppfu = new UserProfilePreferencesForUpdate();
		 uppfu.setUserCredential(credential);
		 uppfu.setFirstName(firstName);
		 uppfu.setLastName(lastName);
		 request.setUserProfilePreferencesForUpdate(uppfu);
		 return request;
	 }
}
