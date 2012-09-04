package com.telenav.cserver.util.helper.log2txnode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/*
 * 
 * KISS principle, Keep it simple, stupid
 * this class will fetch TxNode data from log file and organized those TxNode data
 * 
 * 		Author 	: Larry Li, mingmingli519@gmail.com
 * 		Date	: 2011/12/30	
 */

class Meta {
	int id = 0;
	TxNode node = null;
	
	public Meta(int id, TxNode node){
		this.id = id;
		this.node = node;
	}
	
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}
	
	public void setData(TxNode node){
		this.node = node;
	}
	
	public TxNode getData(){
		return this.node;
	}
}


public class Log2TxNode {

	/*
	 * 	This program mainly provide two services, 
	 * 		1. fetching TxNode from log file and saving to a specific file, refer to log2TxNodeParser()
	 * 		2. getting required TxNode from file, refer to getTxNode()
	 */
	
	// for log file 
	private String srcFile = "";
	private String destFile = "";
	
	public static final String TxNodeSrc = "TxNode/saveTxNode.log";
	private static final String saveTxNode = "saved TxNode:";
	private static final String tab_key = "\t";
	private static final String line_key = "\n";
	private static final String separator = "%";
	private static final String CONFIG_FILE = "TxNode/log2TxNode.properties";
	private static final String KEY_FILE	= "TxNode/key.properties";
	private static final String TxNode_Template = "TxNode/txnode_template.log";
	
	// for parsing
	private static final String src				= "src";
	private static final String dest			= "dest";
	private static final String startFlag 		= "Log.S.K";
	private static final String overFlag 		= "Log.O.K";
	
	private static final String TxNode_start 	= "node.start.key";
	private static final String TxNode_over 	= "node.over.key";
	private static final String TxNode_msg		= "node.msg.key";
	private static final String TxNode_child	= "node.child";

	private String sString = "";
	private String oString = "";
	private String TxNodeStart = "";
	private String TxNodeOver = "";
	private String TxNodeMsg = "";
	private String TxNodeChild = "";
	
	private int maxLineNumber = 1024;
	private static final String maxLine = "maxLineNumber";
	private String unitTestConfigPath = "unittest/unittest-config";
	
	//lookup and register
	private Map<String, ArrayList<TxNode>> cache = null;
	private Map<String, String> keyDict = null;
	
	private static Log2TxNode instance = null;
	public static Log2TxNode getInstance()
	{
		if(instance == null)
		{
			instance = new Log2TxNode();
		}
		return instance;
	}
	
	private Log2TxNode()
	{
		cache = new HashMap<String, ArrayList<TxNode>>();
		keyDict = new HashMap<String, String>();
		init();
	}
	
	private void init(){		
		try{

			String path = "";
			String keyPath = "";
			String configPath = "";
			
			String[] classPaths = System.getProperties().get("java.class.path").toString().split(":");
			for(String p: classPaths)
			{
				p = p.replace("\\", "/");
				if(p.contains(unitTestConfigPath))
				{
					path = p.split(unitTestConfigPath)[0];
					break;
				}
			}

			keyPath 	= path + unitTestConfigPath + "/" + KEY_FILE;				
			configPath 	= path + unitTestConfigPath + "/" + CONFIG_FILE;
	
			Properties properties = new Properties();
			InputStream fi = new FileInputStream(new File(configPath));
			properties.load(fi);

			srcFile 	= (properties.getProperty(Log2TxNode.src) != null) ? properties.getProperty(Log2TxNode.src) : srcFile;
			destFile 	= (properties.getProperty(Log2TxNode.dest) != null) ? properties.getProperty(Log2TxNode.dest) : destFile;
			sString 	= (properties.getProperty(Log2TxNode.startFlag) != null) ? properties.getProperty(Log2TxNode.startFlag) : sString;
			oString 	= (properties.getProperty(Log2TxNode.overFlag) != null) ? properties.getProperty(Log2TxNode.overFlag) :  oString;
			TxNodeStart	= (properties.getProperty(Log2TxNode.TxNode_start) != null) ? properties.getProperty(Log2TxNode.TxNode_start) :  TxNodeStart;	
			TxNodeOver  = (properties.getProperty(Log2TxNode.TxNode_over) != null) ? properties.getProperty(Log2TxNode.TxNode_over) :  TxNodeOver;
			TxNodeMsg	= (properties.getProperty(Log2TxNode.TxNode_msg) != null) ? properties.getProperty(Log2TxNode.TxNode_msg) :  TxNodeMsg;				
			TxNodeChild	= (properties.getProperty(Log2TxNode.TxNode_child) != null) ? properties.getProperty(Log2TxNode.TxNode_child) :  TxNodeChild;				
			maxLineNumber	= (properties.getProperty(Log2TxNode.maxLine) != null) ? Integer.valueOf(properties.getProperty(Log2TxNode.maxLine)) : maxLineNumber;
			
			properties.clear();
			fi.close();
			
			
			fi = new FileInputStream(new File(keyPath));
			properties.load(fi);
			Enumeration<?> list = properties.propertyNames();
			while(list.hasMoreElements()){
				String key = (String)list.nextElement();
				String value = properties.getProperty(key);
				if(value.equalsIgnoreCase("true"))
				keyDict.put(key, value);
			}
		}catch(IOException e){
			e.printStackTrace();
		}

	}
	
	
	/*
	 * 	Fetch all the TxNode from log file
	 * 	@Para	File
	 * 	@Return	ArrayList
	 */
	public ArrayList<TxNode> file2TxNodeList(File file) throws IOException
	{
		ArrayList<TxNode> txNodeList = new ArrayList<TxNode>();
		if(file.exists() && file.isFile())
		{
			FileReader fr = new FileReader(file);
			BufferedReader bufferReader = new BufferedReader(fr);
			
			int lineCount = 0;
			boolean readState = false;
			boolean lastLineState = false;
			boolean continueFlag = false;
			
			String line = "start, good luck";
			StringBuffer sbf = new StringBuffer();
			
			String[] txNodeStarts = sString.split(Log2TxNode.separator);
			
			while(true)
			{
				line = bufferReader.readLine();
				if(line == null)
				{
					break;
				}
				
				if(txNodeStarts != null && txNodeStarts.length > 0)
				{
					for(int i = 0; i < txNodeStarts.length; i++)
					{
						if(line.contains(txNodeStarts[i].trim()))
						{
							lineCount = 0;
							readState = true;
							continueFlag = true;
							break;
						}
					}
					
					if(continueFlag)
					{
						continueFlag = false;
						continue;
					}
					
					if(line.equals(oString) && readState)
					{
						readState = false;
						lastLineState = true;
					}
					
					if(readState)
					{
						lineCount++;
						if(lineCount > maxLineNumber)
						{
							lineCount = 0;
							readState = false;
							lastLineState = false;
							sbf = new StringBuffer();
						}
						sbf.append(line + Log2TxNode.line_key);
					} 
					else if(lastLineState)
					{
						lineCount = 0;
						lastLineState = false;
						sbf.append(line + Log2TxNode.line_key);
						TxNode txNode = string2TxNode(sbf);
						txNodeList.add(txNode);
						sbf = new StringBuffer();						
					}
				}
				
			}
			
		}
		
		return txNodeList;
	}
	
	/*
	 * 	log2TxNodeParser
	 * 	fetch TxNode from log file and save the TxNode to a specific file
	 * 	@Para String, String
	 * 	@Return
	 */
	public void log2TxNodeParser(String logFileName, String saveFileName)
	{
		log2TxNodeParser(logFileName, saveFileName, false);
	}
	
	/*
	 * 	log2TxNodeParser
	 * 	fetch TxNode from log file and save the TxNode to a specific file
	 * 	@Para String, String
	 * 	@Return
	 */
	
	public void log2TxNodeParser(String logFileName, String saveFileName, boolean filter)
	{
		Map<String, String> register = new HashMap<String, String>();
		if(keyDict != null)
		{
			Set<String> keySet = keyDict.keySet();
			Iterator<String> itr = keySet.iterator();
			while(itr.hasNext())
			{
				String key = itr.next();
				if(keyDict.get(key).equalsIgnoreCase("true"))
				{
					register.put(key, "true");
				}
				
			}
		}
		
		File file = new File(logFileName);
		try
		{
			if(file.exists() && file.isFile())
			{
				ArrayList<TxNode> txNodeList = file2TxNodeList(file);
				
				if(txNodeList != null && txNodeList.size() > 0)
				{
					File saveFile = new File(saveFileName);
					FileWriter fw = new FileWriter(saveFile);
					BufferedWriter bfw = new BufferedWriter(fw);
					for(TxNode txNode : txNodeList)
					{
						if(filter)
						{
							if(register != null)
							{
								Set<String> keySet = register.keySet();
								Iterator<String> itr = keySet.iterator();
								while(itr.hasNext())
								{
									String key = itr.next();
									if(register.get(key).equalsIgnoreCase("true"))
									{
										if(isRequiredTxNode(key, txNode))
										{
											bfw.write(saveTxNode + "\n" + txNode.toString() + "\n");
											register.put(key, "false");
											break;
										}
									}
									
								}
							}
						}
						else
						{
							bfw.write(saveTxNode + "\n" + txNode.toString() + "\n");
						}
					}
					bfw.close();	
				}
			}
		}catch(Throwable e)
		{
			System.out.println("error occurs at log2TxNodeParser()");
			e.printStackTrace();
		}
	}
	
	/*
	 * 	Judge whether a TxNode contain key 
	 * 	@Para	String, TxNode
	 * 	@Return	boolean
	 */
	public boolean isRequiredTxNode(String key, TxNode txNode)
	{
		boolean isRequired = false;
		if(txNode != null)
		{
			String txNodeString = txNode.toString();
			String[] txNodeStrings = txNodeString.split("\n");
			if(txNodeStrings != null && txNodeStrings.length > 0)
			{
				for(String str : txNodeStrings)
				{
					if(str.contains(key))
					{
						isRequired = true;
						break;
					}
				}
			}
		}	
		return isRequired;
	}
	
	
	/*
	 * 	get required TxNode by key
	 * 	@Para	String, String
	 * 	@Return	ArrayList
	 */
	
	public ArrayList<TxNode> getTxNode(String key)
	{
		String TxNodePath = Log2TxNode.getTxNodePath();
		return getTxNode(key, TxNodePath);
	}
	
	/*
	 * 	get required TxNode from file by key 
	 * 	@Para	String, String
	 * 	@Return	ArrayList
	 */
	public ArrayList<TxNode> getTxNode(String key, String path){
		
		ArrayList<TxNode> nodeList = new ArrayList<TxNode>();
		if(cache.size() == 0)
		{
			try 
			{
				File file = new File(path);
				if(file.exists() && file.isFile())
				{
					ArrayList<TxNode> txNodeList = file2TxNodeList(file);
					for(TxNode txNode : txNodeList)
					{
						Set<String> keySet = keyDict.keySet();
						Iterator<String> itr = keySet.iterator();
						while(itr.hasNext())
						{
							String k = itr.next();
							if(isRequiredTxNode(k, txNode))
							{
								if(cache.get(k) == null)
								{
									ArrayList<TxNode> list = new ArrayList<TxNode>();
									list.add(txNode);
									cache.put(k, list);
								}
								else
								{
									cache.get(k).add(txNode);
								}
							}
						}
					}
				}
				
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		nodeList = cache.get(key);
		if(nodeList == null)
		{
			TxNode common = getCommonTxNode();
			nodeList = new ArrayList<TxNode>();
			nodeList.add(common);	
		}
		return nodeList;
	}
	
	
	/*
	 * 	convert String to single TxNode
	 * 	@Para	String
	 * 	@Return TxNode
	 */
	public TxNode string2SingleTxNode(String arg){
		
		TxNode tx = null;
		String[] lines = arg.split(Log2TxNode.line_key);
		
		if(lines != null){
			tx = new TxNode();
			boolean msg = false;
			boolean value = false;

			String tmp = "";
			for(int i = 0; i < lines.length; i++){
				tmp = lines[i].trim();
				if(tmp.equals(TxNodeStart)){
					value = true;
					msg = false;
					continue;
				}else if(tmp.equals(TxNodeMsg)){
					msg = true;
					value = false;
					continue;
				}else if(tmp.equals(TxNodeOver)){
					break;
				}
				if(value){
					if(tmp.startsWith("[")){
						String[] couple = tmp.replace("[", "").replace("]", "").split("=");
						if(couple != null && couple.length > 0){
							tx.addValue(Long.valueOf(couple[1].trim()));
						}
					}
				}else if(msg){
					if(!tmp.trim().contains(TxNodeChild)){
						tx.addMsg(tmp.trim());
					}
				}
					
			}
		}
		
		return tx;
	}
	
	
	/*
	 * 	convert String to TxNode
	 * 	@Para	String
	 * 	@Return TxNode
	 */
	private TxNode string2TxNode(StringBuffer stream){
		
		TxNode root = null;
		int count = 0;
		int maxLevel = 0;
		String prefix = "";
		String tempString = "";
		String[] lines = stream.toString().split(Log2TxNode.line_key);
		ArrayList<Meta> txNodeTree = new ArrayList<Meta>();
		
		for(int i = 0; i < lines.length; i++){
			if(lines[i].contains(TxNodeStart)){					
				String endString = lines[i].replace(TxNodeStart, TxNodeOver);
				for(int j = i + 1; j < lines.length; j++){
					if(lines[j].equals(endString)){
						count = 0;
						prefix = "";
						tempString = lines[i];
						while(tempString.startsWith(Log2TxNode.tab_key)){
							count++;
							prefix += Log2TxNode.tab_key;
							tempString = tempString.replaceFirst(Log2TxNode.tab_key, "");
						}
						
						StringBuffer sbf = new StringBuffer();
						for(int k = i; k <= j; k++){
							if(lines[k].startsWith(prefix) && !lines[k].replaceFirst(prefix, "").startsWith(Log2TxNode.tab_key)){
								sbf.append(lines[k] + Log2TxNode.line_key);
							}
						}
						if(count > maxLevel){
							maxLevel = count;
						}
						TxNode txNode = string2SingleTxNode(sbf.toString());
						txNodeTree.add(new Meta(count, txNode));
						break;
					}
				}
			}
		}
		
		int level = 0;
		root = txNodeTree.get(0).getData();
		TxNode[] txNodeContainer = new TxNode[maxLevel + 1];
		txNodeContainer[0] = root;
		
		for(int i = 1; i < maxLevel+1; i++){
			txNodeContainer[i] = null;
		}
		
		for(int i = 1; i < txNodeTree.size(); i++){
			level = txNodeTree.get(i).getId();
			if(txNodeContainer[level-1] != null){
				txNodeContainer[level-1].addChild(txNodeTree.get(i).getData());
			}
			txNodeContainer[level] = txNodeTree.get(i).getData();
		}
		return root;
	}
	
	
	/*
	 * 	get the TxNode log file path
	 * 	@Para	
	 * 	@Return String
	 */
	
	public static String getTxNodePath(){
		
		String unitTestConfigPath = "unittest/unittest-config";
		
		String[] classPaths;
		if(System.getProperty("os.name").toLowerCase().contains("win")){
			classPaths = System.getProperties().get("java.class.path").toString().split(";");	
		}else{			
			classPaths = System.getProperties().get("java.class.path").toString().split(":");	
		}

		String TxNodePath = "";
		for(String path: classPaths){
			path = path.replace("\\", "/");
			if(path.contains(unitTestConfigPath)){
				TxNodePath = path.split(unitTestConfigPath)[0];
				break;
			}
		}
		
		TxNodePath += unitTestConfigPath + "/" + Log2TxNode.TxNodeSrc;
		System.out.println("[unittest log]TxNode path: " + TxNodePath);
		return TxNodePath;
	}
	
	/*
	 * 	get common TxNode if specific TxNode is not found
	 * 	@Para	
	 * 	@Return	TxNode
	 */
	public TxNode getCommonTxNode()
	{
		StringBuffer commonTxNodeString = new StringBuffer();
		String txNodeTemplatePath = getTxNodePath().replace(TxNodeSrc, TxNode_Template);
		File f = new File(txNodeTemplatePath);
		if(f.exists() && f.isFile())
		{
			try {
				String line = "";
				FileReader fr = new FileReader(f);
				BufferedReader bfr = new BufferedReader(fr);
				while(true)
				{
					line = bfr.readLine();
					if(line == null)
					{
						break;
					}
					commonTxNodeString.append(line + "\n");
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Throwable e)
			{
				e.printStackTrace();
			}

			
		}
		TxNode txNode = string2TxNode(commonTxNodeString);
		return txNode;
	}
	
	/*
	 * 	from log to TxNode to HttpServlet
	 * 	@Para	HttpServletRequest, String, int
	 * 	@Return HttpServletRequest
	 */
	public HttpServletRequest log2TxNode2HttpServletRequest(HttpServletRequest request, String actionName, int ajaxId)
	{
		/*
		 *  step 1, get TxNode from file
		 *  TxNode is the most important data input for browser component unit test, we store required TxNode in one file, we will read 
		 *  the TxNode from the file before use it. In general, the TxNode is generated from the log file, you can use updated TxNode to replace
		 *  the previous one.
		 */		
		TxNode txNode = new TxNode();
		ArrayList<TxNode> nodeListFromFile = Log2TxNode.getInstance().getTxNode(actionName);
		if(nodeListFromFile != null && nodeListFromFile.size() > 0)
		{
			txNode = nodeListFromFile.get(0);
		}
		
		/*
		 *  step 2, get the AJAX body from the TxNode, 
		 *  generally, there is an AJAX body in each browser request, it exists as a child TxNode of the request, its value is 110. 
		 */
		TxNode ajaxBody = null;
		TxNode itrNode = txNode.childAt(0);
		if(itrNode != null && itrNode.childAt(0) != null)
		{
			if(itrNode.childAt(0).valueAt(0) == ajaxId)
			{
				ajaxBody = itrNode.childAt(0).childAt(0);
			}
		}

		/*
		 * 	step 3, initialize HttpServletRequest
		 */
		if(ajaxBody != null)
		{
			request = new MockHttpServletRequest(TxNode.toByteArray(ajaxBody));			
		}
		if(txNode != null && txNode.childAt(0) != null)
		{
			TxNode2Request.getInstance(txNode.childAt(0)).toMockHttpServletRequest((MockHttpServletRequest)request);
		}
		DataHandler handler = new DataHandler(request, true);
		request.setAttribute(BrowserFrameworkConstants.CLIENT_INFO, handler);
		
		return request;
	}

	
	/*
	 * 	example
	 */
	public static void main(String[] args){

//		Log2TxNode.getInstance().log2TxNodeParser("D:/developer/catalina.out", "D:/developer/save.log");
//		Log2TxNode.getInstance().log2TxNodeParser("D:/developer/catalina.out", "D:/developer/save.log", true);      // filter 
//		Log2TxNode.getInstance().getTxNode("LocationSearch", "D:/developer/saveTxNode.log");
//		Log2TxNode.getInstance().getTxNode("Weather.do", "D:/developer/saveTxNode.log");
//		Log2TxNode.getInstance().getTxNode("startup.do", "D:/developer/saveTxNode.log");
		
	}
	
}