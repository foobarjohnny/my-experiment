/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.telepersonalize;

import java.util.Calendar;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.telenav.billing.ws.datatypes.userprofile.UserCredentialProfile;
import com.telenav.billing.ws.datatypes.userprofile.UserVehicleProfile;
import com.televigation.db.address.Address;

/**
 * UserStatus
 * @author kwwang
 * @date 2010-6-4
 */
public class UserStatus
{
    private long userId;

    private int carrierCode;

    private long carrierId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String nickname;

    private long deviceId;

    private long workAddressId;

    private long homeAddressId;

    private String contactEmail;

    private int emailConfirmed;

    private Calendar createTime;

    private Calendar updateTime;

    private Address homeAddress;

    private Address workAddress;
    
    private String loginName;
    
    private String password;
    
    private UserCredentialProfile userCredentialProfile;
    
    private UserVehicleProfile userVehicleProfile;
    
    private boolean syncRecentStopsFavsFlag;
    

    public boolean isSyncRecentStopsFavsFlag() {
		return syncRecentStopsFavsFlag;
	}

	public void setSyncRecentStopsFavsFlag(boolean syncRecentStopsFavsFlag) {
		this.syncRecentStopsFavsFlag = syncRecentStopsFavsFlag;
	}

	public UserCredentialProfile getUserCredentialProfile()
    {
        return userCredentialProfile;
    }

    public void setUserCredentialProfile(UserCredentialProfile userCredentialProfile)
    {
        this.userCredentialProfile = userCredentialProfile;
    }

    public UserVehicleProfile getUserVehicleProfile()
    {
        return userVehicleProfile;
    }

    public void setUserVehicleProfile(UserVehicleProfile userVehicleProfile)
    {
        this.userVehicleProfile = userVehicleProfile;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Address getHomeAddress()
    {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress)
    {
        this.homeAddress = homeAddress;
    }

    public Address getWorkAddress()
    {
        return workAddress;
    }

    public void setWorkAddress(Address workAddress)
    {
        this.workAddress = workAddress;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public int getCarrierCode()
    {
        return carrierCode;
    }

    public void setCarrierCode(int carrierCode)
    {
        this.carrierCode = carrierCode;
    }

    public long getCarrierId()
    {
        return carrierId;
    }

    public void setCarrierId(long carrierId)
    {
        this.carrierId = carrierId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public long getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(long deviceId)
    {
        this.deviceId = deviceId;
    }

    public long getWorkAddressId()
    {
        return workAddressId;
    }

    public void setWorkAddressId(long workAddressId)
    {
        this.workAddressId = workAddressId;
    }

    public long getHomeAddressId()
    {
        return homeAddressId;
    }

    public void setHomeAddressId(long homeAddressId)
    {
        this.homeAddressId = homeAddressId;
    }

    public String getContactEmail()
    {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail)
    {
        this.contactEmail = contactEmail;
    }

    public int getEmailConfirmed()
    {
        return emailConfirmed;
    }

    public void setEmailConfirmed(int emailConfirmed)
    {
        this.emailConfirmed = emailConfirmed;
    }

    public Calendar getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Calendar createTime)
    {
        this.createTime = createTime;
    }

    public Calendar getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Calendar updateTime)
    {
        this.updateTime = updateTime;
    }

    public String toString()
    {
        return ReflectionToStringBuilder.toString(this);
    }

}
