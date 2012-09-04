package com.telenav.cserver.backend.msg;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Stub;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.ws.services.messaging.EmailMessage;
import com.telenav.ws.services.messaging.MsgServiceRequestDTO;
import com.telenav.ws.services.messaging.MsgServiceStub;

/**
 * Message service proxy
 * Send email message to a recipientEmail
 * @author jianyu zhou
 * @date 02/03/2012
 */
public class MsgProxy {


	public final static String SERVICE_MSG = "MSGSERVICE";

	private static Logger logger = Logger.getLogger(MsgProxy.class);
	
	private static MsgProxy instance = new MsgProxy();
	
	private static WebServiceConfigInterface msgWs = WebServiceConfiguration.getInstance().getServiceConfig(SERVICE_MSG);
    private static ConfigurationContext msgServiceContext = WebServiceUtils.createConfigurationContext(SERVICE_MSG);
	
    
	/**
	 * it's a private constructor, get instance through com.telenav.cserver.backend.msg.MsgProxy.getInstance()
	 */
	private MsgProxy()
	{
		// can't instantiate outter the class
	}
	
	public static MsgProxy getInstance() 
	{
		return instance;
	}
	
	/**
	 * Send email to recipient.
	 * 
	 * @param tnContext
	 * @param fromEmail
	 * @param recipientEmail
	 * @param emailSubject
	 * @param emailBody
	 * @throws ThrottlingException
	 */
	public void sendEmail(TnContext tnContext, String fromEmail, String recipientEmail, String emailSubject, String emailBody) throws ThrottlingException
	{
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
	    cli.setFunctionName("sendEmail");
		boolean startAPICall = false;
		MsgServiceStub stub = null;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_MSG, tnContext);
			if(!startAPICall)
			{
				throw new ThrottlingException();
			}
			try
            {
                cli.addData("Request", recipientEmail);
                String[] sendToEmails = {recipientEmail};
        		stub = createMsgServiceStub();
        		MsgServiceRequestDTO request2 = new com.telenav.ws.services.messaging.MsgServiceRequestDTO();
        		EmailMessage email = new com.telenav.ws.services.messaging.EmailMessage();
        		email.setEmail(sendToEmails);
        		email.setFrom(fromEmail);
        		email.setSubject(emailSubject);
        		email.setSendType(3);
        		email.setContentType("text/html");
        		email.setMessage(emailBody);
        		request2.addMessages(email);
        		stub.send(request2);

            }
            catch (Exception ex)
            {
                logger.fatal("msgProxy#sendEmail()", ex);
            }
			
		}
		finally
		{
			if(startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_MSG, tnContext);
			}
			cli.complete();
            closeResource(stub);
		}
		
	}
	
	protected MsgServiceStub createMsgServiceStub()
			throws AxisFault {
		return new com.telenav.ws.services.messaging.MsgServiceStub(msgServiceContext,
				msgWs.getServiceUrl());
	}
	
	/**
     * Close stub, actually it is to return the connection back to pool.
     * 
     */
    private static void closeResource(Stub stub)
    {
        try
        {
            Axis2Helper.close(stub);
        }
        catch (AxisFault fault)
        {
            logger.fatal("MsgProxy#closeResource", fault);
        }
    }
}
