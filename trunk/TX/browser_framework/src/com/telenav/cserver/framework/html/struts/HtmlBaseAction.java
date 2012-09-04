/*
 *  @file BaseAction.java	
 *  @copyright (c) 2008 Telenav, Inc.
 */
package com.telenav.cserver.framework.html.struts;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.datatype.HtmlDeviceConfig;
import com.telenav.cserver.framework.html.util.HtmlClientInfoFactory;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.framework.html.util.HtmlClientHelper;
import com.telenav.cserver.framework.html.util.HtmlDeviceManager;
import com.telenav.cserver.framework.html.util.HtmlMessageHelper;
import com.telenav.cserver.framework.html.util.HtmlServiceLocator;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
/**
 * 
 * @author panzhang
 * @version 1.0 2010-12-09
 */

public abstract class HtmlBaseAction extends Action {

    /**
     * Log 4j.
     */
	private Logger logger = Logger.getLogger(HtmlBaseAction.class);
	
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
        cli.setFunctionName(request.getServletPath());
        
        try {
            String requestURL = request.getRequestURL().toString();
            String servletPath = request.getServletPath();

            request.setAttribute("Host_url", HtmlClientHelper.getHostURL(requestURL,servletPath));
            HtmlClientInfo clientInfo = getClientInfo(request);
            //Put clientInfo to request
            request.setAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO,clientInfo);
            //prepare the resource needed in screen
            initializeScreen(request,clientInfo);            
            // Do action.
            ActionForward forward = doAction(mapping, request, response);
            
            //add cli object
            cli.addData(CliTransaction.LABEL_CLIENT_INTO, getClientInfoFromRequest(request));
            
            return forward;
            
        } catch (Exception e) {
        	e.printStackTrace();
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
        StringBuilder sb = new StringBuilder();
        HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
        String userId = clientInfo.getUserId();
        if("".equals(userId))
        {
        	userId = HtmlCommonUtil.getString((String)request.getAttribute(HtmlFrameworkConstants.CLI_KEY_USERID));
        }
        String macAddress = HtmlCommonUtil.getString((String)request.getAttribute(HtmlFrameworkConstants.CLI_KEY_MACADDRESS));
        String email = HtmlCommonUtil.getString((String)request.getAttribute(HtmlFrameworkConstants.CLI_KEY_EMAIL));
        
        String servletPath = request.getServletPath();
        sb.append("ServletUrl=[").append(servletPath).append("]&");
        //hardcode ptn for cli search since we can't get PTN directly since 7.0
        sb.append("Ptn=[").append("HTML5").append("]&");
        sb.append("UserId=[").append(userId).append("]&");
        sb.append("macAddress=[").append(macAddress).append("]&");
        sb.append("email=[").append(email).append("]&");
        //sb.append("Pin=[").append(clientInfo.getPin()).append("]&");
        sb.append("ProgramCode=[").append(clientInfo.getProgramCode()).append("]&");
        //sb.append("Carrier=[").append(clientInfo.getCarrier()).append("]&");
        sb.append("Platform=[").append(clientInfo.getPlatform()).append("]&");
        //sb.append("Device=[").append(clientInfo.getDevice()).append("]&");
        sb.append("Version=[").append(clientInfo.getVersion()).append("]&");
        //sb.append("BuildNumber[").append(clientInfo.getBuildNo()).append("]&");
        sb.append("ProductType=[").append(clientInfo.getProduct()).append("]&");
        sb.append("Locale=[").append(clientInfo.getLocale()).append("]&");
        //sb.append("Region=[").append(clientInfo.getRegion()).append("]&");
        sb.append("screenWidth=[").append(clientInfo.getWidth()).append("]&");
        sb.append("screenHeight=[").append(clientInfo.getHeight()).append("]");
        
        
        return sb.toString();
    }

    /**
     * initScreen method loads messages for the particular client setup.
     * from HTML DeviceManager, it retrieves CSERVER's device configuration
     * Set the configuration key for message, image, CSS, shared image key, shared csskey, manifest key into servlet request as attributes.
     * the configuration key is typically composed of path to reach to the configuration files.
     * e.g.
     * "/" + programCode + "/" + platform + "/" + version + "/" + product + "/" + device + "/common/style/"  
     * @param request
     * @param handler
     * @throws TnException
     */
    private void initializeScreen(HttpServletRequest request,HtmlClientInfo clientInfo)
    {

    	HtmlMessageHelper.getInstance().initMessage(clientInfo);

        HtmlDeviceConfig config = HtmlDeviceManager.getInstance().getDeviceConfig(clientInfo);
        
    	String moduleName = this.getModuleName();
    	String hostUrlWithIp = (String) request.getAttribute("Host_url");

        request.setAttribute(HtmlFrameworkConstants.HTML_MESSAGE_KEY, config.getMessageI18NKey());
        //request.setAttribute(HtmlFrameworkConstants.HTML_IMAGE_KEY, config.getImageKey());
        //request.setAttribute(HtmlFrameworkConstants.HTML_CSS_KEY, config.getCssKey());
        //request.setAttribute(HtmlFrameworkConstants.HTML_IMAGE_KEY_WITHOUT_SIZE, config.getImageKeyWithoutSize());
        //request.setAttribute(HtmlFrameworkConstants.HTML_DEVICENAME_KEY, config.getUsedDeviceName());
        //request.setAttribute(HtmlFrameworkConstants.HTML_SHARED_IMAGE_KEY, config.getSharedImageKey());
        //request.setAttribute(HtmlFrameworkConstants.HTML_SHARED_CSS_KEY, config.getSharedCssKey());
        
        //set manifest file path
        //String host = hostUrlWithIp;
//        String imageHost = HtmlClientHelper.getImageHost();
//        if("".equals(imageHost))
//        {
//        	imageHost = host;
//        }
//        //imageHost += config.getManifestKey();
        //request.setAttribute(HtmlFrameworkConstants.HTML_MANIFEST_KEY, "");
        
        
        //setup the real device with host ip
    	String resourceURL = HtmlServiceLocator.getInstance().getServiceUrl(hostUrlWithIp,HtmlFrameworkConstants.SERVICE.RESOURCE);
    	String resourceHost = resourceURL + "/" + moduleName;
    	
    	String cssProgPath = resourceHost + config.getCssKey();
    	String cssPath = resourceHost + config.getSharedCssKey();
    	String cssDeviceCommonPath = resourceHost + config.getSharedCssDeviceCommonKey();
    	String cssProgDeviceCommonPath = resourceHost + config.getCssDeviceCommonKey();
    	String audioPath = resourceHost + config.getAudioKey();
    	
    	String imageKey = resourceHost + config.getImageKey();
    	String sharedImageKey = resourceHost + config.getSharedImageKey();
    	String js =  resourceHost + "/js/";
    	String jsCommon = resourceURL + "/common/js/";
    		
    	request.setAttribute(HtmlFrameworkConstants.FULLPATH_CSS, cssPath); 
    	request.setAttribute(HtmlFrameworkConstants.FULLPATH_CSSPROG, cssProgPath); 
    	request.setAttribute(HtmlFrameworkConstants.FULLPATH_CSS_DEVCIECOMMON_KEY, cssDeviceCommonPath); 
    	request.setAttribute(HtmlFrameworkConstants.FULLPATH_SHARED_CSS_DEVCIECOMMON_KEY, cssProgDeviceCommonPath); 
    	request.setAttribute(HtmlFrameworkConstants.FULLPATH_IMAGE, imageKey); 
    	request.setAttribute(HtmlFrameworkConstants.FULLPATH_SHARED_IMAGE, sharedImageKey); 
    	request.setAttribute(HtmlFrameworkConstants.FULLPATH_JS, js); 
    	request.setAttribute(HtmlFrameworkConstants.FULLPATH_JS_COMMON, jsCommon); 
    	request.setAttribute(HtmlFrameworkConstants.FULLPATH_AUDIO, audioPath); 
    	
    	request.setAttribute("platform", clientInfo.getPlatform()); 
    	boolean needBackButton = false;
    	if("IPHONE".equalsIgnoreCase(clientInfo.getPlatform()))
    	{
    		needBackButton = true;
    	}
    	request.setAttribute("needBackButton", needBackButton); 
    	
//    	logger.info("cssPath:" + cssPath);
//    	logger.info("cssProgPath:" + cssProgPath);
//    	logger.info("imageKey:" + imageKey);
//    	logger.info("sharedImageKey:" + sharedImageKey);
//    	logger.info("needBackButton:" + needBackButton);
//    	logger.info("js:" + js);
//    	logger.info("jsCommon:" + jsCommon);
//    	logger.info("jsCommon:" + jsCommon);
    }
    
    /**
     * Check if html client info already exists in servlet request as one of the attributes.
     * If not, parse HTML request parameter to get html client info a json string.
     * Use it to construct html client info object with HtmlClientInfoFactory.  
     * Note that carrier info is derived from devicecarrierMapping file based on program code.
     * 
     * @param request
     * @return HtmlClientInfo
     */
    protected HtmlClientInfo getClientInfo(HttpServletRequest request) {
        
    	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
    	if(clientInfo == null)
    	{
    		String clientInfoString = HtmlCommonUtil.filterLastPara(request.getParameter("clientInfo"));
    		try {
				clientInfoString = URLDecoder.decode(clientInfoString, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		String clientWidth = HtmlCommonUtil.filterLastPara(request.getParameter(HtmlFrameworkConstants.CLIENT_INFO_KEY_WIDTH));
    		String clientHeight = HtmlCommonUtil.filterLastPara(request.getParameter(HtmlFrameworkConstants.CLIENT_INFO_KEY_HEIGHT));
    		String ssoToken = HtmlCommonUtil.filterLastPara(request.getParameter(HtmlFrameworkConstants.CLIENT_INFO_KEY_SSOTOKEN));
    		
    		clientInfo = HtmlClientInfoFactory.getInstance().build(clientInfoString,clientWidth,clientHeight,ssoToken);
    	}
        return clientInfo;
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
    protected String getModuleName()
    {
    	return "";
    }

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

