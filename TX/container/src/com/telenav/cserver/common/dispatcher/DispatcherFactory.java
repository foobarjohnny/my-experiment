/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.dispatcher;

/**
 * This factory is intended for creating Dispatcher.Why I use factory here is
 * that there might be some kind of similar requirements like dispatching
 * request to different backend server in the future, if so, just implements
 * this DispatcherFactory interface and over-ride the
 * getConfigPath()&getObjectName() method, and from the viewpoint of design
 * principle, these two method should reside in another interface, but in order
 * to keep it simple,make it part of DispatcherFactory interface.
 * 
 * @author kwwang
 * 
 */
public interface DispatcherFactory {

	public Dispatcher createDispatcher();

	public String getConfigPath();

	public String getObjectName();
}
