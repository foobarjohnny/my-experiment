/**
 * 
 */
package com.telenav.cserver.dsr.streaming.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;
import com.telenav.cserver.service.socket.ISocketServer;


/**
 * @author joses
 *
 * 
 */
public class ServerStartup {
	
	private static Logger logger = Logger.getLogger(ServerStartup.class.getName());

	private static final String SOCKET_SERVER_CONFIG = "socket_server/server_framework.xml";
	private static final String SOCKET_SERVER = "socket_server";

	public static void main(String[] args){

		startSocketServer();
	}
	
	private static void startSocketServer() {
		new Thread(new Runnable() {
			public void run(){
				ISocketServer nonStreamingServer;
				logger.info("Starting DSR Socket Server ...");
				try {
					nonStreamingServer = (ISocketServer)Configurator.getObject(SOCKET_SERVER_CONFIG, SOCKET_SERVER);
					nonStreamingServer.startup();
				} catch (ConfigurationException e) {
					logger.fatal("Error starting DSR Socket Server ..."+e);
					e.printStackTrace();
				}				
			}
		}).start();
	}
}
