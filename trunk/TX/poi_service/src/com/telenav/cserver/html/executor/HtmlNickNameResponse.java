/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * @TODO	Define the response Object
 * @author  chfzhang@telenav.cn
 * @version 1.0  Feb 18, 2011
 */
public class HtmlNickNameResponse extends ExecutorResponse{

	private long userId;
    private String nickName;
	private String operateType;
	private String isUniqueNickName;
    
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nuserId:");
		sb.append(this.getUserId());
		sb.append("\nnickName:");
		sb.append(this.getNickName());
		sb.append("\noperateType:");
		sb.append(this.getOperateType());
		sb.append("\nisUniqueNickName:");
		sb.append(this.getIsUniqueNickName());
		
		return sb.toString();
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getIsUniqueNickName() {
		return isUniqueNickName;
	}

	public void setIsUniqueNickName(String isUniqueNickName) {
		this.isUniqueNickName = isUniqueNickName;
	}
	
}
