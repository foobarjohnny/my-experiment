/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.browser.movie.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.net.DefaultSocketFactory;
import org.apache.commons.net.SocketFactory;

/**
 * SocketFactory to create timeout socket, this timeout is for connection
 * establishment 
 * 
 * @author yqchen
 * @version 1.0 2007-2-13 8:54:19
 */
public class TimeoutSocketFactory extends DefaultSocketFactory
		implements
			SocketFactory
{
	private int timeout;
	public TimeoutSocketFactory(int timeout)
	{
		this.timeout = timeout;
	}

	public Socket createSocket(String host, int port)
			throws UnknownHostException, IOException
	{
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(host, port), timeout);
		return socket;
	}

	public Socket createSocket(InetAddress address, int port)
			throws IOException
	{
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(address, port), timeout);
		return socket;
	}	

}
