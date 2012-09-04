/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.class4test;

import org.apache.log4j.Logger;

import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.common.resource.device.DevicePropertiesHolder;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * GetAudioExecutor.java
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-13
 * 
 */
public class GetAudioExecutor extends AbstractExecutor {

	static Logger logger = Logger.getLogger(GetAudioExecutor.class);

	/**
	 * 
	 */
	public GetAudioExecutor(String type) {
		// TODO Auto-generated constructor stub
		this.setExecutorType(type);
	}

	/**
	 * execute the executor
	 * 
	 * @param request
	 * @param response
	 * @param context
	 * @throws ExecutorException
	 */
	public void doExecute(ExecutorRequest req, ExecutorResponse resp,
			ExecutorContext context) throws ExecutorException {
	}

	String desc;

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
