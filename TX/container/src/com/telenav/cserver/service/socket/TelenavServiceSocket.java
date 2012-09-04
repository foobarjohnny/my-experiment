package com.telenav.cserver.service.socket;

import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;

import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TelenavServiceSocket {

    private static Logger logger = Logger.getLogger(TelenavServiceSocket.class.getName());

    private static ISocketServer server;

    public static void main(String[] args) {
        if (args != null && args.length >= 1) {
            if ("startup".equalsIgnoreCase(args[0])) {
                try {
                    logger.info("startup TelenavSocketServer ...");
                    server = (ISocketServer) Configurator.getObject(
                            "socket_server/server_framework.xml", "socket_server");
                    server.startup();
                } catch (ConfigurationException ex) {
                    logger.log(Level.SEVERE, "Socket server startup failed", ex);
                }
            } else if ("shutdown".equalsIgnoreCase(args[0])) {
                logger.info("shutdown TelenavSocketServer ...");
                try {
                    int port = 8889;
                    if (args.length >= 2) {
                        try {
                            port = Integer.parseInt(args[1]);
                        } catch (Exception e) {
                        }
                        String url = "http://localhost:" + port + "/admin?password=shutdown-socket-server-right-password";
                        URLConnection conn = null;
                        URL httpUrl = new URL(url);
                        conn = httpUrl.openConnection();
                        conn.getInputStream();
                    } else {
                        logger.severe("Please supply port parameter.");
                        logger.severe("java com.telenav.cserver.service.socket.TelenavServiceSocket shutdown port");
                        System.err.println("Please supply port parameter.");
                        System.err.println("java com.telenav.cserver.service.socket.TelenavServiceSocket shutdown port");
                    }
                } catch (Exception ex) {
                    System.out.println("Finish shut down");
                    System.exit(1);
                }
            } else {
                logger.severe("Please supply parameter.");
                logger.severe("java com.telenav.cserver.service.socket.TelenavServiceSocket startup|(shutdown port)");
                System.err.println("Please supply parameter.");
                System.err.println("java com.telenav.cserver.service.socket.TelenavServiceSocket startup|(shutdown port)");
            }
        }
    }
}
