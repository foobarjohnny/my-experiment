package com.telenav.cserver.dsr.framework;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.telenav.cserver.dsr.handler.IResultsHandler;

public class ProcessConfigurationFactory
{
	private static Logger logger = Logger.getLogger(ProcessConfiguration.class
			.getName());

	private static Map<String, ConfigurationTemplate> configurations
								= new HashMap<String, ConfigurationTemplate>();

	private static String DEFAULT = "default";

	private static final String CONFIG_FILE_PATH = "/process_config.xml";

	private static final String TAG_CASE = "case";
	private static final String TAG_PROXY = "proxy";
	private static final String TAG_HANDLER = "handler";
	private static final String ATTR_TYPE = "configId";
	private static final String VAL_DEFAULT = "default";
	private static final String TAG_HANDLER_LIST = "handlerList";

	static
	{
		loadConfig(CONFIG_FILE_PATH) ;
	}
	
	private static void loadConfig(String filePath)
	{
		Document doc = getDoc(filePath);
		if (doc == null)
		{
			return;
		}
		NodeList caseList = doc.getElementsByTagName(TAG_CASE);

		for (int i = 0; i < caseList.getLength(); i++)
		{
			addCase(caseList.item(i));
		}

		logger.info("Load Configuration : " + configurations.size() + " cases.");
	}

	private static void addCase(Node node)
	{
		String configId = getType(node);
		if (configId != null)
		{
			Node proxyNode = getOnlyChild(node, TAG_PROXY);
			List<Node> handlerList = getChildren(getOnlyChild(node, TAG_HANDLER_LIST), TAG_HANDLER);
			
			try
			{
				Class<RecognizerProxy> proxyClass = (Class<RecognizerProxy>)
														Class.forName(proxyNode.getTextContent());
				
				List<Class<IResultsHandler>> handlers = new ArrayList<Class<IResultsHandler>>();
				
				for(Node handlerNode : handlerList){
					Class<IResultsHandler> handlerClass = (Class<IResultsHandler>)
																Class.forName(handlerNode.getTextContent());
					handlers.add(handlerClass);				
				}

				ConfigurationTemplate config = new ConfigurationTemplate(proxyClass, handlers);
				configurations.put(configId, config) ;
				logger.log(Level.FINE, "configId: "+configId+" config: "+config.proxyClass.getCanonicalName());
			}
			catch (Exception e)
			{
				logger.log(Level.SEVERE, "Unable to create configuration for rec case: "+configId, e);
			}
			
			
		}
	}
	
	private static List<Node> getChildren(Node parent, String tagName){
		
		List<Node> handlerList = new ArrayList<Node>();
		NodeList handlers = ((Element)parent).getElementsByTagName(tagName);
		for(int i = 0; i < handlers.getLength(); i++){
			if(handlers.item(i)!=null){
				handlerList.add(handlers.item(i));
			}
		}
		
		return handlerList;
	}

	private static String getType(Node node)
	{
		String typeStr = getAttributeValue(node, ATTR_TYPE);
		String configId = null;
		if (VAL_DEFAULT.equals(typeStr))
		{
			configId = DEFAULT;
		}
		else
		{
			configId = typeStr.trim();
		}
		return configId;
	}

	private static Node getOnlyChild(Node parent, String tagName)
	{
		NodeList list = ((Element) parent).getElementsByTagName(tagName);
		return list.getLength() == 1 ? list.item(0) : null;
	}

	private static String getAttributeValue(Node node, String attriName)
	{
		Node attriNode = node.getAttributes().getNamedItem(attriName);
		return attriNode == null ? "" : attriNode.getNodeValue();
	}

	private static Document getDoc(String fileName)
	{
		InputStream is = ProcessConfiguration.class
				.getResourceAsStream(fileName);
		Document doc = null;
		try
		{
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			doc = docBuilder.parse(is);
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "error", e);
		}
		return doc;
	}

	public static ProcessConfiguration getConfiguration(String configId)
	{
		
		logger.log(Level.FINE, "configId "+configId);
		
		ConfigurationTemplate template = configurations.get(configId) ;
		
		ProcessConfiguration config = null ;
		try
		{
		
			config = new ProcessConfiguration(template.proxyClass.newInstance(), template.getIResultsHandlerInstances());
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "Unable to create process configuration.", e);
			return null ;
		}
		
		return config ;
	}
	
	private static class ConfigurationTemplate
	{
		public Class<RecognizerProxy> proxyClass ;
		public List<Class<IResultsHandler>> handlers;
		
		public ConfigurationTemplate(Class<RecognizerProxy> proxyClass, List<Class<IResultsHandler>> handlers){
			this.proxyClass = proxyClass;
			this.handlers = handlers;
		}
		
		public List<IResultsHandler> getIResultsHandlerInstances(){
			List<IResultsHandler> handlerInstances = new ArrayList<IResultsHandler>();
			for(Class<IResultsHandler> handler : handlers){
				try {
					handlerInstances.add(handler.newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			return handlerInstances;
		}
	}
}
