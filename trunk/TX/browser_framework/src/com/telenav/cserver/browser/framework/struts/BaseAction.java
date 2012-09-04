/*
 *  @file BaseAction.java	
 *  @copyright (c) 2008 Telenav, Inc.
 */
package com.telenav.cserver.browser.framework.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.browser.datatype.DeviceConfig;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.util.ClientHelper;
import com.telenav.cserver.browser.util.DeviceManager;
import com.telenav.cserver.browser.util.MessageHelper;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;
import com.televigation.log.TVCategory;

/**
 * 
 * @author zywang
 * @version 1.0 2009-4-14
 */

public abstract class BaseAction extends Action {

    /**
     * Log 4j.
     */
    private static final TVCategory logger = (TVCategory) TVCategory
            .getInstance(BaseAction.class);
    private static final Logger misLogger = (Logger) TVCategory
            .getInstance("mislog");

    protected ProtocolRequestParser requestParser = null;
    protected ProtocolResponseFormatter responseFormatter = null;

    public void setRequestParser(ProtocolRequestParser requestParser) {
        this.requestParser = new ProxyRequestParser(requestParser);
    }

    public void setResponseFormatter(ProtocolResponseFormatter responseFormatter) {
        this.responseFormatter = new ProxyResponseFormatter(responseFormatter);
    }

    /**
     * override from Action
     */
    public final ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CliTransaction cli = new CliTransaction(CliTransaction.TYPE_URL);
        cli.setApplicationId(getCliApplicationID());
        cli.setFunctionName(request.getServletPath() + "?" + request.getQueryString());
        
        try {
            cli.addData(CliTransaction.LABEL_CLIENT_INTO, getClientInfoFromRequest(request));
            String requestURL = request.getRequestURL().toString();
            String servletPath = request.getServletPath();

            request.setAttribute("Host_url", ClientHelper.getHostURL(requestURL,servletPath));

            // check the log if the log exist
            DataHandler handler = getHandler(request);
            request
                    .setAttribute(BrowserFrameworkConstants.CLIENT_INFO,
                            handler);

            TxNode logNode = (TxNode) handler.getParameter("log");
            if (logNode != null) {

                int msgCount = logNode.msgsSize();
                for (int i = 0; i < msgCount; i++) {
                    String logMsg = logNode.msgAt(i);
                    misLogger.info(logMsg);
                }
            }
            
            //prepare the resource needed in screen
            initializeScreen(request,handler);
            // Do action.
            return doAction(mapping, request, response);
        } catch (Exception e) {
            logger.error("Browser Application Server Exception.", e);
            cli.setStatus(e);
            cli.setState(CliTransaction.STATUS_FAIL);
            return mapping.findForward("Globe_Exception");
        } finally {
            cli.complete();
        }
    }
    
    /**
     * Get the cli client information from request.
     * @param request 
     * 
     * @return
     */
    private String getClientInfoFromRequest(HttpServletRequest request) {
        DataHandler handler = getHandler(request);
        StringBuffer sb = new StringBuffer();
        sb.append("ServletUrl=[").append(request.getServletPath() + "?" + request.getQueryString()).append("]&");
        sb.append("Ptn=[").append(handler.getClientInfo(DataHandler.KEY_USERACCOUNT)).append("]&");
        sb.append("UserId=[").append(handler.getClientInfo(DataHandler.KEY_USERID)).append("]&");
        sb.append("Pin=[").append(handler.getClientInfo(DataHandler.KEY_USERPIN)).append("]&");
        sb.append("Carrier=[").append(handler.getClientInfo(DataHandler.KEY_CARRIER)).append("]&");
        sb.append("Platform=[").append(handler.getClientInfo(DataHandler.KEY_PLATFORM)).append("]&");
        sb.append("Device=[").append(handler.getClientInfo(DataHandler.KEY_DEVICEMODEL)).append("]&");
        sb.append("Version=[").append(handler.getClientInfo(DataHandler.KEY_VERSION)).append("]&");
        sb.append("BuildNumber[").append(handler.getClientInfo(DataHandler.KEY_BUILDNUMBER)).append("]&");
        sb.append("ProductType=[").append(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE)).append("]&");
        sb.append("Locale=[").append(handler.getClientInfo(DataHandler.KEY_LOCALE)).append("]&");
        sb.append("Region=[").append(handler.getClientInfo(DataHandler.KEY_REGION)).append("]&");
        sb.append("screenWidth=[").append(handler.getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH)).append("]&");
        sb.append("screenHeight=[").append(handler.getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT)).append("]&");
        sb.append("InputParm=[").append(handler.getClientInfo(DataHandler.KEY_INPUTPARAMETERS)).append("]");
        return sb.toString();
    }

    /**
     * 
     * @param request
     * @param handler
     * @throws TnException
     */
    private void initializeScreen(HttpServletRequest request,
            DataHandler handler){
        request.setAttribute(BrowserFrameworkConstants.LOCALE_KEY, handler.getClientInfo(DataHandler.KEY_LOCALE));
        
         // Layout.
        MessageHelper.getInstance(false).initMessage(handler);
        /*
        request.setAttribute(BrowserFrameworkConstants.MESSAGE_KEY, ClientHelper.getMessageI18NKey(handler));
        request.setAttribute(BrowserFrameworkConstants.IMAGE_KEY, ClientHelper.getImageKey(handler));
        request.setAttribute(BrowserFrameworkConstants.IMAGE_KEY_WITHOUT_SIZE, ClientHelper.getImageKeyWithoutSize(handler));*/

        DeviceConfig config = DeviceManager.getInstance().getDeviceConfig(handler);
        request.setAttribute(BrowserFrameworkConstants.MESSAGE_KEY, config.getMessageI18NKey());
        request.setAttribute(BrowserFrameworkConstants.IMAGE_KEY, config.getImageKey());
        request.setAttribute(BrowserFrameworkConstants.IMAGE_KEY_WITHOUT_SIZE, config.getImageKeyWithoutSize());
    }
    
    
    protected DataHandler getHandler(HttpServletRequest request) {
        return new DataHandler(request);
    }

    /**
     * Abstract method do the action.
     * 
     * @throws MovieException
     */
    protected abstract ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception;

    /**
     * Get the application id for CLI log.
     * 
     * @return The Cli log application id.
     */
    protected abstract String getCliApplicationID();

    /**
     * Request parser proxy.
     * 
     * @author lwei (lwei@telenav.cn)
     */
    private class ProxyRequestParser implements ProtocolRequestParser {

        /** Request Parser. */
        private ProtocolRequestParser parser;

        ProxyRequestParser(ProtocolRequestParser parser) {
            this.parser = parser;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser
         * #parse(java.lang.Object)
         */
        public ExecutorRequest[] parse(Object arg0) throws ExecutorException {
            CliTransaction cli = new CliTransaction(CliTransaction.TYPE_MODULE);
            cli.setFunctionName("ParseRequest");
            try {
                ExecutorRequest[] requests = parser.parse(arg0);
                for (int i = 0; i < requests.length; i++) {
                    ExecutorRequest executorRequest = requests[i];
                    cli.addData("RequestName", executorRequest
                            .getExecutorType());
                    cli.addData("RequestDetail", executorRequest.toString());
                }
                return requests;
            } catch (Exception e) {
                cli.setStatus(e);
                throw new ExecutorException(e);
            } finally {
                cli.complete();
            }
        }
    }

    
    /**
     * Response formatter proxy.
     * 
     * @author lwei (lwei@telenav.cn)
     */
    private class ProxyResponseFormatter implements ProtocolResponseFormatter {

        /** Response formatter. */
        private ProtocolResponseFormatter formatter;

        ProxyResponseFormatter(ProtocolResponseFormatter formatter) {
            this.formatter = formatter;
        }

        @Override
        public void format(Object arg0, ExecutorResponse[] responses)
                throws ExecutorException {
            CliTransaction cli = new CliTransaction(CliTransaction.TYPE_MODULE);
            cli.setFunctionName("FormatResponse");
            try {
                formatter.format(arg0, responses);
                for (int i = 0; i < responses.length; i++) {
                    ExecutorResponse executorResponse = responses[i];
                    cli.addData("ResponseName", executorResponse
                            .getExecutorType());
                    cli.addData("ResponseDetail", executorResponse.toString());
                }
            } catch (Exception e) {
                cli.setStatus(e);
                throw new ExecutorException(e);
            } finally {
                cli.complete();
            }
            
        }
    }
}

