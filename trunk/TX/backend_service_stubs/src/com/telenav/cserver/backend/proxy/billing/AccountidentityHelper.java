/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.billing;

import com.telenav.billing2.accountidentity.common.dataTypes.NonEmptyString;
import com.telenav.billing2.accountidentity.common.dataTypes.NonEmptyLong;
import com.telenav.billing2.accountidentity.common.dataTypes.RequestSource;
import com.telenav.billing2.accountidentity.dataTypes.Account;
import com.telenav.billing2.accountidentity.dataTypes.AddGlobalUserAssociationRequest;
import com.telenav.billing2.accountidentity.dataTypes.AuthenticateRequest;
import com.telenav.billing2.accountidentity.dataTypes.ConfirmAccountRequest;
import com.telenav.billing2.accountidentity.dataTypes.CreateAccountRequest;
import com.telenav.billing2.accountidentity.dataTypes.CredentialType;
import com.telenav.billing2.accountidentity.dataTypes.GetExternalUserCredentialRequest;
import com.telenav.billing2.accountidentity.dataTypes.GetPasswordRequest;
import com.telenav.billing2.accountidentity.dataTypes.UserCredential;


/**
 * AccountidentityHelper
 * @author kwwang
 *
 */
public class AccountidentityHelper {
	
	public static AuthenticateRequest createAuthenticateRequest(String email,String password)
	{
		AuthenticateRequest request = new AuthenticateRequest();
		request.setRequestSource(RequestSource.C_SERVER);
		UserCredential credential = new UserCredential();
		
		credential.setCredentialType(CredentialType.EMAIL);
		NonEmptyString requiredEmail=new NonEmptyString();
		requiredEmail.setValue(email);
		credential.setCredentialId(requiredEmail);
		
		NonEmptyString requiredPwd=new NonEmptyString();
		requiredPwd.setValue(password);
		credential.setPassword(requiredPwd);
		request.setUserCredential(credential);
		return request;
	}
	
	public static GetExternalUserCredentialRequest createGetExternalUserCredentialRequest(long userId)
	{
		GetExternalUserCredentialRequest request= new GetExternalUserCredentialRequest();
		request.setRequestSource(RequestSource.C_SERVER);
		request.setUserId(userId);
		return request;
	}
	
	public static GetPasswordRequest createGetPasswordRequest(String email)
	{
		GetPasswordRequest request= new GetPasswordRequest();
		request.setRequestSource(RequestSource.C_SERVER);
		
		UserCredential credential = new UserCredential();
		
		credential.setCredentialType(CredentialType.EMAIL);
		NonEmptyString requiredEmail=new NonEmptyString();
		requiredEmail.setValue(email);
		credential.setCredentialId(requiredEmail);
		
		request.setUserCredential(credential);
		return request;
	}
	
	
	public static CreateAccountRequest createCreateAccountRequest(String email, String password, long userId)
	{
		CreateAccountRequest request= new CreateAccountRequest();
		request.setRequestSource(RequestSource.C_SERVER);
		request.setNeedConfirmed(false);
		request.setNeedGenerateToken(false);
		
		Account acct = new Account();
		
		acct.setAccountType(CredentialType.EMAIL);
		
		//set account Name
		NonEmptyString nameString=new NonEmptyString();
		nameString.setValue(email);
		acct.setAccountName(nameString);
		
		//set password
		NonEmptyString pwString=new NonEmptyString();
		pwString.setValue(password);
		acct.setPassword(pwString);
		
		if (userId > 0) {
			NonEmptyLong userIdLong = new NonEmptyLong();
			userIdLong.setValue(userId);
			acct.setUserId(userIdLong);
		}
		
		request.setAccount(acct);
		return request;
	}
	
	public static AddGlobalUserAssociationRequest createAddGlobalUserAssociationRequest(long userId, long globalUserId)
	{
		AddGlobalUserAssociationRequest request= new AddGlobalUserAssociationRequest();
		request.setRequestSource(RequestSource.C_SERVER);
		request.setUserId(userId);
		request.setGlobalUserId(globalUserId);
		return request;
	}
	
	
//	public static ConfirmAccountRequest createConfirmAccountRequest(String email, String token)
//	{
//		ConfirmAccountRequest request= new ConfirmAccountRequest();
//		request.setRequestSource(RequestSource.C_SERVER);
//		request.setToken(token);
//		
//		UserCredential credential = new UserCredential();
//		
//		credential.setCredentialType(CredentialType.EMAIL);
//		NonEmptyString requiredEmail=new NonEmptyString();
//		requiredEmail.setValue(email);
//		credential.setCredentialId(requiredEmail);
//		
//		request.setUserCredential(credential);
//		
//		return request;
//	}
}
