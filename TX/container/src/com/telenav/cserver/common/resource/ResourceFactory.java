/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.telenav.cserver.common.resource.ext.BarIterationResourceLoader;
import com.telenav.cserver.common.resource.ext.BinDataResourceLoader;
import com.telenav.cserver.common.resource.ext.NativeResourecBundleLoader;
import com.telenav.cserver.common.resource.ext.ResourceBundleLoader;
import com.telenav.cserver.common.resource.ext.SpringResourceLoader;
import com.telenav.cserver.common.resource.ext.XmlResourceLoader;
import com.telenav.cserver.common.resource.orders.OrLoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.configuration.Configurator;
import com.telenav.cserver.framework.management.jmx.IBackendServerMonitor;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * Resource Factory
 * 
 * @author yqchen
 * @version 1.0 2007-2-6 10:28:43
 */
public class ResourceFactory 
{
	public static String TYPE_RESOURCE_BUNDLE = "resource_bundle";
	public static String TYPE_XML = "xml";
	public static String TYPE_BIN = "bin";
	public static String NATIVE_TYPE_RESOURCE_BUNDLE = "native_resource_bundle";
	public static String TYPE_SPRING = "spring";
	public static String TYPE_BAR_ITERATION = "bar_iteration";
	
	public static String SEPARATOR = "/";
	public static String FLOATING_PATH_DEFAULT = "default";
	
	private  static IBackendServerMonitor backendMonitor;
	
	/**
	 * initiate resource, should be called at system startup
	 *
	 */
	public static void init()
	{
		try
        {
		    ResourceLoaderConfig.init();
		    backendMonitor = (IBackendServerMonitor)Configurator.getObject("management/jmx.xml","backendServerMonitor");
        }
        catch (Exception e) {
            logger.warn(e,e);
        }
	}
	
	public IBackendServerMonitor getBackendServerMonitor(){
	    return backendMonitor;
	}
	
	
	protected static Category logger = Category.getInstance(ResourceFactory.class);
	
	
	static ResourceBundleLoader resourceBundleLoader = new ResourceBundleLoader();
    static XmlResourceLoader xmlResourceLoader = new XmlResourceLoader();
    static BinDataResourceLoader binResourceLoader = new BinDataResourceLoader();
    static NativeResourecBundleLoader nativeResourecBundleLoader = new NativeResourecBundleLoader();
    static SpringResourceLoader springResourceLoader = new SpringResourceLoader();
    static BarIterationResourceLoader barIterationResourceLoader = new BarIterationResourceLoader();
    
    private ResourceFactory()
    {
        init();
    }
    
    static ResourceFactory instance = new ResourceFactory();
    
    public static ResourceFactory getInstance()
    {
        return instance;
    }
    
    /**
     * create default/common resource
     */
    public Object createObject(Object key, Object argument)
    {
        String configPath = (String)key;
        ResourceLoadMeta meta = (ResourceLoadMeta)argument;
        String type = meta.type;
        String objectName = meta.objectName;
        ResourceLoader resourceLoader = getResourceLoader(type);
        Object result = resourceLoader.loadResource(configPath, objectName);
        return result;
    }
    
    /**
     * get a object from cache, disabled cache
     * 
     * @param key
     * @param argument argument to create the object
     */
    public Object get(Object key,Object argument)
    {
        return createObject(key, argument);     
    }
    
    static class ResourceLoadMeta
    {
        public String type;
        public String objectName = null;
    }
    
    public static Object createResource(
            ResourceHolder holder, UserProfile profile, TnContext tnContext)
    {
        String configFileName = holder.getConfigPath();
        String type = holder.getType();
        Object result = null;
        
        ResourceLoadMeta meta = new ResourceLoadMeta();
        meta.type = type;
        
        //handle spring type loader and pass on its object name
        if(type.equals(TYPE_SPRING)&&holder instanceof SpringObjectNameAware)
            meta.objectName=((SpringObjectNameAware)holder).getObjectName();
        
        String prefixConfigPath  = ResourceLoaderConfig.RESOURCE_PATH;
        if(holder.getPrefixStructureOrders() != null)
        {
            List<LoadOrder> orderList = holder.getPrefixStructureOrders().getOrders();
            prefixConfigPath = ResourceLoaderConfig.RESOURCE_PATH + getConfigSuffix(orderList, orderList.size() - 1, profile, tnContext);
        }
        
        String suffixConfigPath  = "";
        if(holder.getSuffixStructureOrders() != null)
        {
            List<LoadOrder> orderList = holder.getSuffixStructureOrders().getOrders();
            suffixConfigPath = getConfigSuffix(orderList, orderList.size() - 1, profile, tnContext);
        }
        
        String fullName = null;
        LoadOrders orders = holder.getFloatingStructureOrders();
        if(orders != null && profile != null)
        {
            List<LoadOrder> orderList = orders.getOrders();
            //platform, version, carrier, device
            int size = orderList.size();
            
            if(size == 0)
            {
                fullName = prefixConfigPath + SEPARATOR + suffixConfigPath
                   + SEPARATOR + configFileName;;

                logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+fullName);
                result = instance.get(fullName, meta);
    
            }
      
            for(int i = size - 1; i >= 0; i --)
            {
                List<String> suffixList = getConfigSuffixList(orderList, i, profile, tnContext);
                String suffix = null;
                for(int j=0; j<suffixList.size(); j++)
                {
                    suffix = suffixList.get(j);
                    
                    if(orders.isLowerCase())
                    {
                        suffix = suffix.toLowerCase();
                    }
                    
                    fullName = prefixConfigPath + SEPARATOR + suffix 
                                                       + SEPARATOR + suffixConfigPath
                                                       + SEPARATOR + configFileName;
                    
                    
                    logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+fullName);
                    result = instance.get(fullName, meta);
                    if (result == null)
                    {
                        List<String> resemFileList = ResourceResemblance.getResemblanceFullPath(fullName, configFileName, orderList, profile, tnContext);
                        for(String fileName: resemFileList)
                        {
                        	fullName=fileName;
                            logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+fullName);
                            result = instance.get(fullName, meta);
                            if (result != null)
                                break;
                        }
                    }
                    if (result != null)
                        break;
                    
                }
                
                //when we got here, we will try the default setting for each LoadOrder
                if(result == null)
                {
                    if(i==0)
                    {
                        fullName = prefixConfigPath 
                                                + SEPARATOR + FLOATING_PATH_DEFAULT 
                                                + SEPARATOR + suffixConfigPath 
                                                + SEPARATOR + configFileName;
                    }
                    else
                    {
                        suffix = getConfigSuffix(orderList, i-1, profile, tnContext);
                        fullName = prefixConfigPath 
                                               + SEPARATOR + suffix 
                                               + SEPARATOR + FLOATING_PATH_DEFAULT 
                                               + SEPARATOR + suffixConfigPath
                                               + SEPARATOR + configFileName;;
                    }
                    
                    logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+fullName);
                    result = instance.get(fullName, meta);
                }
                if(result != null)
                {
                    break;
                }
            }
            
            
            //need one more search for 'prefixConfigPath + SEPARATOR + suffixConfigPath + SEPARATOR + configFileName' if result is null
            if( result == null && size != 0)
            {
                fullName = prefixConfigPath + SEPARATOR + suffixConfigPath + SEPARATOR + configFileName;
                logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+fullName);
                result = instance.get(fullName, meta);
            }
        }
        
        
        if(StringUtils.isNotBlank(fullName)&&result!=null)
        	holder.addUserProfileKeyMapping(((AbstractResourceHolder)holder).getKey(profile, tnContext), fullName);
        
        return result;
    }
    
    public static List<String> getConfigSuffixList(List<LoadOrder> orders, int index, UserProfile profile,
            TnContext tnContext)
    {
        List<String> list = new ArrayList<String>();
        List<StringBuffer> suffixList = new ArrayList<StringBuffer>();
                
        if(orders.size() == 0)
        {
            list.add("");
            return list;
        }
        
        if(index >= orders.size() || index < 0)
        {
             throw new IndexOutOfBoundsException(
                        "Index: " + index +", Size: " + orders.size());
        }
        
        StringBuffer sb = new StringBuffer();
        suffixList.add(sb);
        for(int i = 0 ;i <= index; i ++)
        {
            //sb.append(SEPARATOR);
            append(suffixList, SEPARATOR);
            LoadOrder order = orders.get(i);
            
            List<String> attributeList = order.getAttributeValueList(profile, tnContext);
            
            makeCopy(suffixList, attributeList);
        }

        
        for(int i=0; i<suffixList.size(); i++)
        {
            list.add(suffixList.get(i).toString());
        }
        
        return list;
    }

    private static void makeCopy(List<StringBuffer> suffixList, List<String> attributeList)
    {
        List<StringBuffer> swapList = new ArrayList<StringBuffer>();
        for(int i=0; i<suffixList.size(); i++)
        {
            swapList.add(new StringBuffer(suffixList.get(i)));            
        }
        
        suffixList.clear();

        for(int i=0; i<swapList.size(); i++)
        {
            for(int j=0; j<attributeList.size(); j++)
            {
                suffixList.add(new StringBuffer(swapList.get(i) + attributeList.get(j)));
            }
        }
    }


    private static void append(List<StringBuffer> suffixList, String attributeValue)
    {
        for(int i=0; i<suffixList.size(); i++)
        {
            StringBuffer sb = suffixList.get(i);
            sb.append(attributeValue);
        }
    }

    /**
     * get suffix to append in config file name
     * 
     * @return
     */
    public static String getConfigSuffix(List orders, UserProfile profile, TnContext tnContext)
    {
        return getConfigSuffix(orders, orders.size() - 1, profile, tnContext);
    }
    
    public static String getConfigSuffixForKey(List orders, UserProfile profile, TnContext tnContext)
    {
        return getConfigSuffixForKey(orders, orders.size() - 1, profile, tnContext);
    }
    
    /**
     * get suffix to append in config file name
     * 
     * @param index order index
     * @return
     */
    public static String getConfigSuffix(List<LoadOrder> orders, int index, UserProfile profile,
            TnContext tnContext)
    {
        if(orders.size() == 0)
        {
            return "";
        }
        
        if(index >= orders.size() || index < 0)
        {
             throw new IndexOutOfBoundsException(
                        "Index: " + index +", Size: " + orders.size());
        }
        
        StringBuffer sb = new StringBuffer();
        for(int i = 0 ;i <= index; i ++)
        {
            sb.append(SEPARATOR);
            LoadOrder order = orders.get(i);
            sb.append(order.getAttributeValue(profile, tnContext));
        }
        
        
        return sb.toString();
    }
    
    public static String getConfigSuffixForKey(List<LoadOrder> orders, int index, UserProfile profile,
            TnContext tnContext)
    {
        if(orders.size() == 0)
        {
            return "";
        }
        
        if(index >= orders.size() || index < 0)
        {
             throw new IndexOutOfBoundsException(
                        "Index: " + index +", Size: " + orders.size());
        }
        
        StringBuffer sb = new StringBuffer();
        for(int i = 0 ;i <= index; i ++)
        {
            sb.append(SEPARATOR); 
            LoadOrder order = orders.get(i);
            if( order instanceof OrLoadOrder )
            {
                List<String> list = order.getAttributeValueList(profile, tnContext);
                StringBuffer stringBuffer = new StringBuffer();
                for(String str : list)
                {
                    stringBuffer.append(str).append("_");
                }
                String str = stringBuffer.toString();
                //remove the last '_'
                if( str.length() > 0 )
                {
                    str = str.substring(0,str.length()-1);
                }
                sb.append(str);
                
            }
            else{
                sb.append(order.getAttributeValue(profile, tnContext));
            }
        }
        
        
        return sb.toString();
    }
    

    public static Object createResource(ResourceHolder holder)
    {
        return createResource(holder,  null, null);
    }
    
    public static ResourceLoader getResourceLoader(String type)
    {
        if(type.equalsIgnoreCase(TYPE_RESOURCE_BUNDLE))
        {
            return resourceBundleLoader;
        }
        else if(type.equalsIgnoreCase(TYPE_XML))
        {
            return xmlResourceLoader;
        }
        else if(type.equalsIgnoreCase(TYPE_BIN))
        {
            return binResourceLoader;
        }else if(type.equalsIgnoreCase(NATIVE_TYPE_RESOURCE_BUNDLE))
        {
            return nativeResourecBundleLoader;
        }else if(type.equalsIgnoreCase(TYPE_SPRING))
        {
            return springResourceLoader;
        }else if(type.equalsIgnoreCase(TYPE_BAR_ITERATION))
        {
            return barIterationResourceLoader;
        }
        
        
        throw new IllegalArgumentException("Invalid type to get ResourceLoader:" + type);
    }
    
    public static String getVersion(UserProfile profile)
    {
        String version = profile.getVersion();
        try
        {
            String[] suffix = {".d",".t"};
            for(int i=0; i<suffix.length; i++)
            {
                int idx = version.indexOf(suffix[i]);
                if (idx>0)
                {
                    version = version.substring(0,idx);
                }
            }
        }
        catch(Exception e)
        {
            logger.fatal("ResourceFactory getVersion error=", e);
        }
        return version;
    }
    


    /**
     * @param versionConfig
     * @param type_resource_bundle2
     * @return
     */
    public static Object createResource(String configPath, String type) 
    {
        ResourceLoadMeta meta = new ResourceLoadMeta();
        meta.type = ResourceFactory.TYPE_RESOURCE_BUNDLE;
        return ResourceFactory.getInstance().get(configPath, 
                meta);
    }


    

private static class ResourceLoaderConfig
{
    static String CONFIG_FILE = "device/resource_loader.xml";

    private static final String SET_CONFIG_ROOT = "sets";

    private static final String SET_NODE = "set";

    private static final String HOLDER_CONFIG_ROOT = "holders";

    private static final String HOLDER_NODE = "holder";
    
    public static String RESOURCE_PATH = "device/";

    static void parseConfig()
    {
        // 1. read set config and store in a Map
        // load config file
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream stream =  cl.getResourceAsStream(CONFIG_FILE);
//      InputStream stream = ResourceLoaderConfig.class
//      .getResourceAsStream(CONFIG_FILE);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);

        try
        {
            // parse config
            Document doc = factory.newDocumentBuilder().parse(stream);
            Element root = doc.getDocumentElement();
            NodeList childNodes = root.getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i++)
            {
                Node childNode = childNodes.item(i);
                String nodeName = childNode.getNodeName();
                if (SET_CONFIG_ROOT.equalsIgnoreCase(nodeName))
                {
                    // parse config in <sets></sets>
                    parseSetConfig(childNode);
                } else if (HOLDER_CONFIG_ROOT.equalsIgnoreCase(nodeName))
                {
                    // parse config in <holders></holders>
                    parseHolderConfig(childNode);
                }
            }
            
        }
        catch (Exception e)
        {
            logger.fatal("Exception when parsing configuration file.", e);
        }

        // 2. read hodler config, construct holder and call init()
    }
    
    private static void parseSetConfig(Node node)
    {
        Element element = (Element) node;

        NodeList nodeList = element.getChildNodes();
        // parse elements
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node child = nodeList.item(i);

            if (SET_NODE.equalsIgnoreCase(child.getNodeName()))
            {
                Element serverElement = (Element) child;

                String name = serverElement.getAttribute("name");
                String path = serverElement.getAttribute("path");

                logger.info("Set name: " + name + ",path=" + path);
        //      System.out.println("Set name: " + name + ",path=" + path);
                ResourceSetManager.getInstance().addResourceSet(name, path);
            }
        }
    }

    private static void parseHolderConfig(Node node)
    {
        Element element = (Element) node;

        NodeList nodeList = element.getChildNodes();
        // parse elements
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node child = nodeList.item(i);

            if (HOLDER_NODE.equalsIgnoreCase(child.getNodeName()))
            {
                Element serverElement = (Element) child;

                String name = serverElement.getAttribute("name");
                String className = serverElement.getAttribute("class");

                String set = serverElement.getAttribute("set");
                String configFile = serverElement.getAttribute("config_path");
                String type = serverElement.getAttribute("type");
//              String orders = serverElement.getAttribute("orders");
                String prefixStructure = serverElement.getAttribute("prefix_structure");
                String floatingStructure = serverElement.getAttribute("floating_structure");
                String suffixStructure = serverElement.getAttribute("suffix_structure");
                String filenameLowerCase = serverElement.getAttribute("filename_lower_case");

                ResourceHolder holder = newHodler(className);
                holder.setName(name);
                holder.setConfigPath(configFile);
                holder.setResourceSet(set);
                holder.setType(type);
                
                boolean lowerCase = filenameLowerCase != null 
                && filenameLowerCase.equalsIgnoreCase("true");
                LoadOrders floatingStructureOrders = new LoadOrders();
                floatingStructureOrders.addOrderString(floatingStructure);
                floatingStructureOrders.setLowerCase(lowerCase);
                
                LoadOrders prefixStructureOrders = new LoadOrders();
                prefixStructureOrders.addOrderString(prefixStructure);
                prefixStructureOrders.setLowerCase(lowerCase);
                
                LoadOrders suffixStructureOrders = new LoadOrders();
                suffixStructureOrders.addOrderString(suffixStructure);
                suffixStructureOrders.setLowerCase(lowerCase);
                
                holder.setPrefixStructureOrders(prefixStructureOrders);
                holder.setSuffixStructureOrders(suffixStructureOrders);
                holder.setFloatingStructureOrders(floatingStructureOrders);
                
                holder.init();
                
                ResourceHolderManager.register(holder);
                
                logger.info("Holder name: " + name + ",className=" + className
                        + ",set=" + set + ",configFile=" + configFile
                        + ",type=" + type 
                        + ",prefixStructureOrders=" + prefixStructure
                        + ",floatingStructureOrders=" + floatingStructure
                        + ",suffixStructureOrders=" + suffixStructure);
            }
        }
    }

    private static ResourceHolder newHodler(String className)
    {
        try
        {
            Class clazz = Class.forName(className);
            return (ResourceHolder)clazz.newInstance();

        } catch (Exception e)
        {
            logger.fatal("There must be static getInstance() in class:" + className);
            logger.fatal("Exception when initiate hodler:" + className, e);
        }
        return null;
    }
    
    private static void init() throws Exception {
        Configurator.loadConfigFile(ResourceLoaderConfig.RESOURCE_PATH + "/resource_load_orders.xml");
        ResourceLoaderConfig.parseConfig();
    }
   
}

}