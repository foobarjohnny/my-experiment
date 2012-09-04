package com.telenav.cserver.util.helper.log2protobuf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.mock.MockHttpServletRequest;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;

public class log2Protobuf {
	
		public static final String start_line1 	= "Proto";
		public static final String start_line2 	= "{";
		public static final String end_line 	= "}";
		
		public static final String colon = ":";
		public static final String bracket1 = "[";
		public static final String bracket2 = "]";
		
		FileWriter fw = null;
		public static final String saveProtoBuf = "saveProtoBuf.log";
		public static final String ProtobufSrc = "Protobuf/saveProtobuf.log";
		
		private Map<String, ArrayList<ReverseProtoBuf>> cache = null;
		
		private static log2Protobuf instance = null;
		public static log2Protobuf getInstance(){
			if(instance == null){
				instance = new log2Protobuf();
			}
			return instance;
		}
		
		public log2Protobuf()
		{
			cache = new HashMap<String, ArrayList<ReverseProtoBuf>>();
		}
		
		private static BufferedReader file2Buffer(String path) throws FileNotFoundException{
			
			File file = new File(path);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			return br;
		}
		
		

		
		public List<StringBuffer> buffer2StringBuffer(String logFilePath) throws IOException
		{
			return buffer2StringBuffer(logFilePath, null);
		}
		
		public List<StringBuffer> buffer2StringBuffer(String logFilePath, String saveFilePath) throws IOException{
			
			List<StringBuffer> result = new ArrayList<StringBuffer>();
			BufferedReader br = null;
			try
			{	
				br = file2Buffer(logFilePath);
			}catch(FileNotFoundException e)
			{
				e.printStackTrace();
				return result;
			}catch(Throwable e)
			{
				e.printStackTrace();
				return result;
			}
			
			String tmp = "";
			String line = "";
			boolean readState = false;
			StringBuffer buffer = null;
			
			while(true)
			{
				line = br.readLine();
				if(line == null)
				{
					break;
				}
				
				if(line.startsWith(start_line1))
				{
					tmp = line;
					line = br.readLine();
					if(line == null)
					{
						break;
					}
					
					if(line.equals(start_line2))
					{
						buffer = new StringBuffer();
						buffer.append(tmp + "\n");
						buffer.append(line + "\n");
						readState = true;
					}
				}
				else if(readState)
				{	
					if(line.equals(end_line))
					{
						buffer.append(line);
						result.add(buffer);
						readState = false;
						buffer = null;

					}
					else
					{
						buffer.append(line + "\n");
					}
				}
				
			}
			
			if(saveFilePath != null)
			{
				File saveFile = new File(saveFilePath);
				try
				{
					fw = new FileWriter(saveFile);
					for(StringBuffer pro: result)
					{
						fw.write("saved Protobuf: \n");
						fw.write(pro.toString() + "\n\n");
					}
					fw.close();
				}
				catch(Throwable e)
				{
					e.printStackTrace();
					fw.close();
				}
			}
			return result;
		}
		
		private ReverseProtoBuf parseProtoBufString(ReverseProtoBuf root, StringBuffer buf){
			
			if(buf == null){
				return root;
			}
	
			String tmp1 = "";
			String tmp2 = "";
			String tmp = "";
			StringBuffer tmpStringBuf = new StringBuffer();
			StringBuffer array = new StringBuffer();
			StringBuffer child = new StringBuffer();
			
			ReverseProtoBuf arrayElement = new ReverseProtoBuf();
			ReverseProtoBuf children = new ReverseProtoBuf();
			
			String[] lines = buf.toString().split("\n");
			for(int i = 0; i < lines.length - 1; i++){
				tmp1 = lines[i];
				tmp2 = lines[i+1];
				
				if(tmp1.contains(colon)){
					tmp = tmp1.split(colon)[0];
					if(tmp1.contains(bracket1)){
						array = new StringBuffer();
						array.append(tmp1 + "\n");
						int count = 1;
						for(int j = i+1; j < lines.length; j++){
							array.append(lines[j] + "\n");
							if(lines[j].contains(bracket1)){
								count++;
							}else if(lines[j].contains(bracket2)){
								count--;
								if(count <= 0){
									i = j;							
									break;
								}
							}
						}
						
						boolean readState = false;
						String[] lines2 = array.toString().split("\n");
						for(int l = 0; l < lines2.length; l++){
							if(lines2[l].contains(start_line2)){
								tmpStringBuf = new StringBuffer();
								arrayElement = new ReverseProtoBuf();
								root.addArray(tmp.trim(), arrayElement);
								tmpStringBuf.append(lines2[l] + "\n");
								readState = true;
							}else{
								if(lines2[l].contains(end_line)){
									tmpStringBuf.append(end_line);
									parseProtoBufString(arrayElement, tmpStringBuf);
									readState = false;
								}else if(readState){
									tmpStringBuf.append(lines2[l] + "\n");
								}
							}
						}
						
					}else{
						if(tmp2.contains(start_line2)){
							children = new ReverseProtoBuf();
							root.addChild(tmp1.trim(), children);
							
							child = new StringBuffer();
							child.append(start_line2 + "\n");
							int count = 1;
							for(int j = i+2; j < lines.length; j++){
								if(lines[j].contains(start_line2)){
									count++;
								}else if(lines[j].contains(end_line)){
									count--;
									if(count <= 0){
										i = j;
										child.append(end_line + "\n");
										parseProtoBufString(children, child);									
										break;
									}
								}
								child.append(lines[j] + "\n");
							}
						}else{
							String[] coupled = tmp1.split(colon);
							if(coupled != null && coupled.length == 2){
								root.addElement(coupled[0], coupled[1]);
							}
						}
					}
				}
			}
			
			return root;
		}
		
		
		/*
		 * 	get protobuf path
		 * 	@Para
		 * 	@Return	String
		 */
		public static String getProtobufPath()
		{
			String unitTestConfigPath = "unittest/unittest-config";
			
			String[] classPaths;
			if(System.getProperty("os.name").toLowerCase().contains("win")){
				classPaths = System.getProperties().get("java.class.path").toString().split(";");	
			}else{			
				classPaths = System.getProperties().get("java.class.path").toString().split(":");	
			}

			String ProtobufPath = "";
			for(String path: classPaths){
				path = path.replace("\\", "/");
				if(path.contains(unitTestConfigPath)){
					ProtobufPath = path.split(unitTestConfigPath)[0];
					break;
				}
			}
			
			ProtobufPath += unitTestConfigPath + "/" + log2Protobuf.ProtobufSrc;
			System.out.println("[unittest log]TxNode path: " + ProtobufPath);
			return ProtobufPath;
		}
		
		
		public HttpServletRequest getProtobuf(String key, String ProtobufPath)
		{
			MockHttpServletRequest request = new MockHttpServletRequest();
			
			if(!key.equals("mock"))
			{
				if(cache.size() == 0)
				{
					try 
					{
						List<StringBuffer> list = buffer2StringBuffer(ProtobufPath);
						for(StringBuffer sbf : list)
						{
							String protoKey = sbf.toString().split("\n")[0];
							if(cache.get(protoKey) == null)
							{
								ArrayList<ReverseProtoBuf> ProtobufList = new ArrayList<ReverseProtoBuf>();
								ReverseProtoBuf reverseProtobuf = new ReverseProtoBuf();
								parseProtoBufString(reverseProtobuf ,sbf);
								ProtobufList.add(reverseProtobuf);
								cache.put(protoKey, ProtobufList);
							}
							else
							{
								ReverseProtoBuf reverseProtobuf = new ReverseProtoBuf();
								parseProtoBufString(reverseProtobuf ,sbf);
								cache.get(protoKey).add(reverseProtobuf);
							}
						}
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
				
				if(cache.get(key) != null)
				{
					ReverseProtoBuf reverseProtobuf = cache.get(key).get(0);
				}
			}
			
			setHtmlClientInfo(request);
			return request;
		}
		
		public void setHtmlClientInfo(HttpServletRequest request)
		{
			if(null != request)
			{
				HtmlClientInfo htmlClientInfo = new HtmlClientInfo();
				htmlClientInfo.setBuildNo("7101233");
				htmlClientInfo.setCarrier("SprintPCS");
				htmlClientInfo.setDevice("Druid");
				htmlClientInfo.setDeviceCarrier("");
				htmlClientInfo.setHeight("800");
				htmlClientInfo.setLocale("en_US");
				htmlClientInfo.setPlatform("ANDROID");
				htmlClientInfo.setProduct("SN_lite");
				htmlClientInfo.setProgramCode("SNNAVPROG");
				htmlClientInfo.setUserId("");
				htmlClientInfo.setVersion("7.1.0");
				htmlClientInfo.setWidth("480");
				
				request.setAttribute("HTML_CLIENT_INFO", htmlClientInfo);
			}
		}
		
		public HttpServletRequest getProtobuf(String key)
		{
			String ProtobufPath = log2Protobuf.getProtobufPath();
			return getProtobuf(key, ProtobufPath);
		}
		
		public static void main(String[] args) throws IOException
		{
			log2Protobuf.getInstance().buffer2StringBuffer("c:/pro.txt");
			// log2Protobuf.getInstance().buffer2StringBuffer("D:/developer/workspace/poi_unittest/unittest/unittest-config/Protobuf/saveProtobuf.log", "c:/pro.txt");
		}
		
	}