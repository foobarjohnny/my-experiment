/**
 * 
 */
package com.telenav.cserver.dsr.protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.telenav.cserver.dsr.util.XMLUtil;


/**
 * @author joses
 *
 * 
 */
public class ProtocolHandlerFactory {

	private static Logger logger = Logger.getLogger(ProtocolHandlerFactory.class.getName());
	
	private static final String CONFIG_FILE_PATH = "/protocolConfig.xml";
	private static final String TAG_PROTOCOL_HANDLER = "protocolHandler";
	private static final String ATTR_VERSION = "version";
	private static Map<String, Class<?>> protocolHandlers = new HashMap<String, Class<?>>();
	
	static {
        loadConfig(CONFIG_FILE_PATH);
    }
	
	private static void loadConfig(String filePath) {
		Document doc = XMLUtil.getDoc(filePath);
        if (doc == null) {
            return;
        }
        NodeList handlerList = doc.getElementsByTagName(TAG_PROTOCOL_HANDLER);

        for(int i = 0; i < handlerList.getLength(); i++){
        	addProtocolHandler(handlerList.item(i));
        }
        
        logger.info("Load protocol handler configuration : " + protocolHandlers.size() + " cases.");
	}
	
	private static void addProtocolHandler(Node node){
		String protocolVersion = getVersion(node);
		if(protocolVersion != null){
			try {
				Class<?> klass =  Class.forName(node.getTextContent());
				protocolHandlers.put(protocolVersion, klass);
				logger.log(Level.FINE, "Loading protocolHandler: "+node.getTextContent());
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Unable to create protocol handler for version: " + protocolVersion, e);
			} 			
		}
	}
	
	private static String getVersion(Node node){
		
		String versionStr = XMLUtil.getAttributeValue(node, ATTR_VERSION);
		String protocolVersion = null;
        try {
        	protocolVersion = versionStr;
        }
        catch (NumberFormatException ignored) {
            logger.log(Level.SEVERE, "Unable to parse protocol version: " + versionStr);
        }
        return protocolVersion;
	}
	
	public static ProtocolHandler getProtocolHandler(String protocolVersion){
			
		Class<?> klass = protocolHandlers.get(protocolVersion);
		logger.log(Level.FINE, "protocol handler class name: "+klass.getCanonicalName());
		if (klass != null)
        {
			try
	    	{
	        	ProtocolHandler handler = (ProtocolHandler) klass.newInstance();
	            return handler ;
	    	}
			catch (Exception e){
				logger.log(Level.SEVERE, "Unable to create Protocol Handler for version: "+ protocolVersion, e);				
			}
        }
        return null;
	}
}
