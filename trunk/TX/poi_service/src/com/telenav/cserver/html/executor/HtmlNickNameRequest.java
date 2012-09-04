/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;

/**
* @TODO	Define the request Object
* @author chfzhang@telenav.cn
* @version 1.0 Feb 18, 2011
*
*/
public class HtmlNickNameRequest extends ExecutorRequest{
	private long userId;
    private String operateType;
	private String nickName;
    
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nuserId:");
		sb.append(this.getUserId());
		sb.append("\nnickName:");
		sb.append(this.getNickName());
		sb.append("\noperateType:");
		sb.append(this.getOperateType());
		
		return sb.toString();
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
}
