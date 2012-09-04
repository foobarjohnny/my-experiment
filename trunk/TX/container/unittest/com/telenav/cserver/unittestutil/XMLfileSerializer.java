/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.unittestutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * RootElementSerialize.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-5-13
 * @TODO: In unit test, we need create some objects to execute the test code. If the object depends on a xml file when creating, 
 *        it will become a waste of time task. So, we serialize these objects into a file, and then deserialize them into jvm when 
 *        executing unit test. It will save some time to create object by this way. 
 */
public class XMLfileSerializer {
	
	private String path = System.getProperty("user.dir")+ "/unittest/unittest-config/XMLfileContainer.tmp";
	private XMLfileContainer c = new XMLfileContainer();
	
	
	/**
	 * @throws IOException 
	 * 
	 */
	public XMLfileSerializer() throws IOException {
		// TODO Auto-generated constructor stub
		File file = new File(path);
		if(!file.exists()){
			file.createNewFile();
			write2File();//avoid EOFException
		}
		initXMLfileContainer();
	}
	/**
	 * initial the XMLfileContainer instance named c, read the object stored in XMLfileContainer.tmp file
	 */
	private void initXMLfileContainer(){
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(path));
			c = (XMLfileContainer)ois.readObject();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			try {
				if(ois !=null ) ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @TODO  serialize a object into a file
	 */
	private void write2File() {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(c);
			
			System.out.println("XMLfileContainer Serialization finished! Write " + c.getCount() + " Objects in total.");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(oos !=null )
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}
	/**
	 * @TODO  deserialize a object into jvm from a file
	 * @param xmlPath 	the xml file for creating object, it is a relative path. If the Object is never serialized before,
	 *                  the method will create a object first, and then serialize the object into file.
	 *                  e.g.  resource/device/test.xml 
	 * @return
	 */
	public Element read(String xmlPath) {
//		if(!(new File(xmlPath).exists())){
//			System.err.println("The file : " + xmlPath + " does not exist.");
//			return null;
//		}
		Element obj = c.getObject(xmlPath);
		if(obj !=null){
			return obj;
		}else{
			try {
				obj = createObject(xmlPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			c.setObject(xmlPath, obj);
			write2File();
			return obj;
		}
	}
	/**
	 * @param xmlPath     create a object of Element from the xml file
	 * @return
	 * @throws Exception
	 */
	private Element createObject(String xmlPath){
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream stream = cl.getResourceAsStream(xmlPath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		Document doc;
		Element root = null;
		try {
			doc = factory.newDocumentBuilder().parse(stream);
			root = doc.getDocumentElement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Error when creating ["+xmlPath+"]. \n messge is: " + e.getMessage()+". \n cause: "+e.getCause());
		}
		
		System.out.println(xmlPath + " has been created.");
		return root;
	}
	
	
	
	
	public void init(String path) throws Exception{
		File folder = new File(path);
		File[] files = folder.listFiles();
		for(File file : files){
			String fileName = file.getName();
			//[filter folder and file]
			if(file.isDirectory()){
				if(fileName.equalsIgnoreCase(".svn")) continue;//.svn folder is not our target
				init(file.getPath());
			}else{
				String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
				if(!extensionName.equalsIgnoreCase("xml")) continue;//we handle xml file only
				
				String relativePath = getRelativePath(file.getPath());
				Element obj =  createObject(relativePath);
				c.setObject(relativePath.replaceAll("\\\\", "/"),obj);
			}
		}
	}
	/**
	 * unittest/unittest-config has been used as source folder. We must delete string "unittest/unittest-config"
	 * from the path. Otherwise, we cannot get the InputStream of the xml file in method createObject()
	 * 
	 * @param path	unittest/unittest-config/resource/test.xml
	 * @return	resource/test.xml
	 */
	private String getRelativePath(String path){
		String buildPath = "unittest\\unittest-config\\";
		path = path.substring(path.indexOf(buildPath) + buildPath.length());
		return path;
	}
	public static void main(String[] args) throws Exception {
		XMLfileSerializer x = new XMLfileSerializer();
		x.init("unittest/unittest-config");
		x.write2File();
		x.initXMLfileContainer();
	}
}
