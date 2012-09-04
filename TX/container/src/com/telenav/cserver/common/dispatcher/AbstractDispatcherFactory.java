/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.dispatcher;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;

/**
 * The abstract Dispatcher factory, it defines the common way to create Dispatcher, by using spring way.
 * @author kwwang
 *
 */
public abstract class AbstractDispatcherFactory implements DispatcherFactory {

	protected Logger logger=Logger.getLogger(getClass());
	
	@Override
	public Dispatcher createDispatcher() {
		Object res=null;
		
		try {
			res=Configurator.getObject(getConfigPath(), getObjectName());
		} catch (ConfigurationException e) {
			logger.warn("Exception occurs when loading object->"+e.getMessage());
			logger.warn("From path->"+getConfigPath()+" and object name->"+getObjectName());
		}
		return (Dispatcher)res;
	}

}
