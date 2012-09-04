package com.telenav.cserver.service.socket;

import com.sun.grizzly.*;
import com.sun.grizzly.filter.ReadFilter;
import com.sun.grizzly.http.SelectorThread;
import com.sun.grizzly.tcp.http11.GrizzlyAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyRequest;
import com.sun.grizzly.tcp.http11.GrizzlyResponse;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultSocketServer implements ISocketServer {

    private static Logger logger = Logger.getLogger(DefaultSocketServer.class.getName());

    private ProtocolFilter businessFilter;
    private Controller controller = new Controller();
    private int port = 8123;
    private int timeout = 1000 * 15;
    private int readThreadsCount = 4;
    private int corePoolSize = 10;
    private int maximumPoolSize = 50;

    private GrizzlyAdapter httpAdapter = null;

    public ProtocolFilter getBusinessFilter() {
        return businessFilter;
    }

    public void setBusinessFilter(ProtocolFilter businessFilter) {
        this.businessFilter = businessFilter;
    }

    public void setHttpAdapter(GrizzlyAdapter httpAdapter) {
        this.httpAdapter = httpAdapter;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * @param readThreadsCount the readThreadsCount to set
     */
    public void setReadThreadsCount(int readThreadsCount) {
        this.readThreadsCount = readThreadsCount;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public void startup() {
        logger.info("startup server ...");
        CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliTransaction.TYPE_SQL);
        cli.setFunctionName("startService");
        String jarFileNames = Configuration.getJarFileNames();
        cli.addData("DEPLOYINFO", jarFileNames);
        cli.complete();

        cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_URL);
        cli.setFunctionName("DefaultSocketServer.startup");
        TCPSelectorHandler tcpHandler = new TCPSelectorHandler();
        tcpHandler.setPort(port);
        TelenavSelectionKeyHandler keyHandler = new TelenavSelectionKeyHandler();
        keyHandler.setTimeout(timeout);
        tcpHandler.setServerTimeout(0);
        tcpHandler.setSocketTimeout(0);
        tcpHandler.setSelectionKeyHandler(keyHandler);
        controller.addSelectorHandler(tcpHandler);
        controller.setReadThreadsCount(readThreadsCount);

        Pipeline pool = new DefaultPipeline();
        pool.setMinThreads(corePoolSize);
        pool.setMaxThreads(maximumPoolSize);
        controller.setPipeline(pool);
//		DefaultThreadPool dfp = new DefaultThreadPool();
//		dfp.setCorePoolSize(corePoolSize);
//		dfp.setMaximumPoolSize(maximumPoolSize);
//		controller.setThreadPool(dfp);

        ProtocolChainInstanceHandler pciHandler = new ProtocolChainInstanceHandler() {

            final private ProtocolChain protocolChain = new DefaultProtocolChain();

            public ProtocolChain poll() {
                return protocolChain;
            }

            public boolean offer(ProtocolChain instance) {
                return true;
            }
        };
        controller.setProtocolChainInstanceHandler(pciHandler);

        ProtocolChain protocolChain = pciHandler.poll();
        protocolChain.addFilter(new ReadFilter());
        protocolChain.addFilter(businessFilter);

        controller.addStateListener(new ControllerStateListener() {

            public void onStarted() {
                logger.info("Started...");
            }

            public void onReady() {
                logger.info("I'm ready");
            }

            public void onStopped() {
                logger.info("Stop server");
            }

            public void onException(Throwable ex) {
                logger.log(Level.SEVERE, "Socket server error", ex);
            }
        });

        final int httpPort = port + 1;
        new Thread(new Runnable() {

            public void run() {
                CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_URL);
                cli.setFunctionName("DefaultSocketServer.startup");
                try {
                    logger.info("Start http server for admin, port is " + httpPort);
                    cli.addData("http server", "Start http server, port is " + httpPort);
                    SelectorThread st = new SelectorThread();
                    st.setPort(httpPort);
                    st.setReuseAddress(true);
                    st.setAdapter(new GrizzlyAdapter() {

                        public void service(GrizzlyRequest req, GrizzlyResponse resp) {
                            if (httpAdapter != null) {
                                httpAdapter.service(req, resp);
                            }
                            shutdown(req);
                        }
                    });
                    st.setDisplayConfiguration(true);
                    st.initEndpoint();
                    st.startEndpoint();
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Start http server error", ex);
                    cli.setStatus(ex);
                } finally {
                    cli.complete();
                }
            }
        }).start();

        try {
            logger.info("Server started at port:" + port + ", with timeout:" + timeout + "ms, readThreadsCount:" + readThreadsCount);
            cli.addData("port", "" + port);
            cli.addData("timeout", "" + timeout);
            cli.addData("readThreadsCount", "" + readThreadsCount);
            cli.complete();
            controller.start();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Socket server startup failed", ex);
            cli.setStatus(ex);
            cli.complete();
        }
    }

    public void shutdown(GrizzlyRequest req) {
        String url = req.getRequestURI();
        if ("/admin".equals(url)) {
            CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_URL);
            cli.setFunctionName("handleAdmin");
            String passwd = req.getParameter("password");
            if ("shutdown-socket-server-right-password".equals(passwd)) {
                logger.log(Level.INFO, "Shut down now");
                cli.addData("shutdown", "password is right");
                try {
                    controller.stop(true);
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "controller.stop failed", ex);
                }
                logger.info("shutdown server complete");
                cli.complete();
                System.exit(0);
            } else {
                logger.log(Level.INFO, "Wrong password" + passwd);
                cli.addData("shutdown", "password is wrong:" + passwd);
                cli.complete();
            }
        }
    }
}
