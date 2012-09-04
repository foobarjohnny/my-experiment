package com.telenav.browser.dispatcher;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.televigation.log.TVCategory;

/**
 * Monitor Servlet
 * 
 * @author yqchen
 * @version 1.0
 */
public class MonitorServlet extends HttpServlet {
    /** Serial id */
    private static final long serialVersionUID = 5025461952829623337L;

    protected static TVCategory logger = (TVCategory) TVCategory
            .getInstance(MonitorServlet.class);

    private static String STR_HEARTBEAT_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><admin><status><name>&1&</name><value>&2&</value></status>&3&</admin>";

    public static final String REPLACE_FLAG1 = "&1&";
    public static final String REPLACE_FLAG2 = "&2&";
    public static final String REPLACE_FLAG3 = "&3&";

    public MonitorServlet() {
    }

    public void init() {

    }

    /**
     * doGet() just calls doPost()
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        String statusString = generateHeartbeatResult(validateStatus());
        OutputStream os = response.getOutputStream();
        os.write(statusString.getBytes());
    }

    // static int count = -1;

    private boolean validateStatus() {
        // count++;
        logger.info("Start to do heartbeat check for Browser Dispatcher... ...");
        logger
                .info("End to do heartbeat check for browser Browser Dispatcher... ...");
        return true;
    }

    private String generateHeartbeatResult(boolean status) {
        String str_head = STR_HEARTBEAT_HEAD;
        str_head = str_head.replaceAll(REPLACE_FLAG1, "Browser_server");
        String sStatus = "";
        if (status) {
            sStatus = "ok";
        } else {
            sStatus = "fail";
        }
        str_head = str_head.replaceAll(REPLACE_FLAG2, sStatus);
        str_head = str_head.replaceAll(REPLACE_FLAG3, "");
        return str_head;
    }

}
