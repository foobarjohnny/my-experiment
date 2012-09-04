package com.telenav.tnbrowser.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;

import com.telenav.j2me.datatypes.BizPOI;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;

public class DataHandler {
	
	protected static Logger logger = Logger.getLogger(DataHandler.class);
	
	public static final String KEY_INPUTPARAMETERS = "INPUTPARAMETERS"; 
	public static final String KEY_SHAREDATAS = "SHAREDATAS";
	
	public static final String KEY_LOCALE = "locale";
	public static final String KEY_REGION = "region";
	public static final String KEY_AUDIOFORMAT = "audioformat";
	public static final String KEY_CARRIER = "carrier";
	public static final String KEY_PLATFORM = "platform";
	public static final String KEY_DEVICEMODEL = "devicemodel";
	public static final String KEY_BUILDNUMBER = "buildnumber";
	public static final String KEY_WIDTH = "width";
	public static final String KEY_HEIGHT = "height";
	public static final String KEY_BROWSERVERSION = "browserversion";
	public static final String KEY_USERACCOUNT = "useraccount";
	public static final String KEY_USERPIN = "userpin";
	public static final String KEY_USERID = "userid";
	public static final String KEY_PRODUCTTYPE = "producttype";
	public static final String KEY_VERSION = "version";
	public static final String KEY_CLIENT_SUPPORT_SCREEN_WIDTH = "client_support_screen_width";
	public static final String KEY_CLIENT_SUPPORT_SCREEN_HEIGHT = "client_support_screen_height";
	public static final String KEY_DEVICE_INFO = "deviceinfo";
	public static final String KEY_SUBID2 = "subid2";
	public static final String KEY_GUIDETONE = "guidetone";
	public static final String KEY_DEVICE_ID = "deviceid";
	
	private Hashtable inputParameters = new Hashtable();
	private Hashtable outputParameters = new Hashtable();
	
	private Hashtable shareData = new Hashtable();
	
	//// special note. After parsing the incoming sharedata, if has sharedata, always to print
	private boolean hasIncomingShareData = false;
	
	private Hashtable clientInfo = new Hashtable();
	
	private TxNode AJAXBody;
	
	public static void main(String[] args)
	{
		DataHandler dh = new DataHandler(null);
	}
	
	public DataHandler(HttpServletRequest request)
	{
		init(request, false);
	}
	
	public DataHandler(HttpServletRequest request, boolean isHandleAJAXRequest)
	{
		init(request, isHandleAJAXRequest);
	}
	
	private void init(HttpServletRequest request, boolean isHandleAJAXRequest)
	{		
		if ( !isHandleAJAXRequest ) 
		{
			try 
			{
				////get the xml string from the request
				BufferedReader br = request.getReader();
				StringBuffer sb = new StringBuffer();
				String str = br.readLine();
				while (str != null) {
					sb.append(str);
					str = br.readLine();
				}
				String inputString = sb.toString();

				if (inputString != null && inputString.length() > 0) {
					parseXML(inputString);
				}

				//// special note
				if (!shareData.isEmpty()) {
					hasIncomingShareData = true;
				}
			} 
			catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug("DataHandler(HttpServletRequest)", e);
				}
			}
		}
		else
		{
			try 
			{
				BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
				
				int count = bis.available();
				byte[] data = new byte[count];
				
				bis.read(data);
				
				AJAXBody = TxNode.fromByteArray(data, 0, "UTF-8");
			} 
			catch (Exception e) 
			{
				if(logger.isDebugEnabled())
				{
					logger.debug("getAJAXBody(HttpServletRequest)",e);
				}
			}	
		}
		
		////get the client information key-value and store
		extractClientInfo(request);
	}
	
	public String getClientInfo(String key)
	{
		return (String)clientInfo.get(key);
	}
	
	public TxNode getAJAXBody()
	{
		return AJAXBody.childAt(0);
	}
	
	private void extractClientInfo(HttpServletRequest request)
	{
		String value=null;
		value = request.getParameter(KEY_DEVICE_ID);
		if(value!=null)
		{
			clientInfo.put(KEY_DEVICE_ID, value);
		}
		value = request.getParameter(KEY_GUIDETONE);
		if(value!=null)
		{
			clientInfo.put(KEY_GUIDETONE, value);
		}
		value = request.getParameter(KEY_LOCALE);
		if(value!=null)
		{
			clientInfo.put(KEY_LOCALE, value);
		}
		value = request.getParameter(KEY_REGION);
		if(value!=null)
		{
			clientInfo.put(KEY_REGION, value);
		}
		value = request.getParameter(KEY_AUDIOFORMAT);
		if(value!=null)
		{
			clientInfo.put(KEY_AUDIOFORMAT, value);
		}
		value = request.getParameter(KEY_CARRIER);
		if(value!=null)
		{
			clientInfo.put(KEY_CARRIER, value);
		}
		value = request.getParameter(KEY_PLATFORM);
		if(value!=null)
		{
			clientInfo.put(KEY_PLATFORM, value);
		}
		value = request.getParameter(KEY_DEVICEMODEL);
		if(value!=null)
		{
			clientInfo.put(KEY_DEVICEMODEL, value);
		}
		value = request.getParameter(KEY_BUILDNUMBER);
		if(value!=null)
		{
			clientInfo.put(KEY_BUILDNUMBER, value);
		}
		value = request.getParameter(KEY_WIDTH);
		if(value!=null)
		{
			clientInfo.put(KEY_WIDTH, value);
		}
		value = request.getParameter(KEY_HEIGHT);
		if(value!=null)
		{
			clientInfo.put(KEY_HEIGHT, value);
		}
		value = request.getParameter(KEY_BROWSERVERSION);
		if(value!=null)
		{
			clientInfo.put(KEY_BROWSERVERSION, value);
		}
		value = request.getParameter(KEY_USERACCOUNT);
		if(value!=null)
		{
			clientInfo.put(KEY_USERACCOUNT, value);
		}
		value = request.getParameter(KEY_USERPIN);
		if(value!=null)
		{
			clientInfo.put(KEY_USERPIN, value);
		}
		value = request.getParameter(KEY_USERID);
		if(value!=null)
		{
			clientInfo.put(KEY_USERID, value);
		}
		value = request.getParameter(KEY_PRODUCTTYPE);
		if(value!=null)
		{
			clientInfo.put(KEY_PRODUCTTYPE, value);
		}
		value = request.getParameter(KEY_VERSION);
		if(value!=null)
		{
			clientInfo.put(KEY_VERSION, value);
		}
		value = request.getParameter(KEY_CLIENT_SUPPORT_SCREEN_WIDTH);
		if(value!=null)
		{
			clientInfo.put(KEY_CLIENT_SUPPORT_SCREEN_WIDTH, value);
		}
		value = request.getParameter(KEY_CLIENT_SUPPORT_SCREEN_HEIGHT);
		if(value!=null)
		{
			clientInfo.put(KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, value);
		}
		value = request.getParameter(KEY_DEVICE_INFO);
		if(value!=null)
		{
			clientInfo.put(KEY_DEVICE_INFO, value);
		}
		value = request.getParameter(KEY_SUBID2);
		if(value!=null)
		{
			clientInfo.put(KEY_SUBID2, value);
		}
	}
	
	public void toXML(JspWriter out)
	{
		////print the inputParameters add through java code to xml in the following format
	    ////		<bean name="xx" valuetype="TxNode" value="...a base64 string..." />
	     Enumeration  keys=outputParameters.keys();
	     while(keys.hasMoreElements())
	     {
	    	 String beanName=(String)keys.nextElement();
	    	 //TxNode beanData = (TxNode)outputParameters.get(beanName);
	    	 
	    	 Object shareDataValue = outputParameters.get(beanName);
	    	 TxNode beanData = new TxNode();
	    	 if(shareDataValue instanceof TxNode)
	    	 {
	    		 beanData = (TxNode)shareDataValue;
	    	 }else if (shareDataValue instanceof Stop)
	    	 {
	    		 beanData = ((Stop)shareDataValue).toTxNode();
	    	 }else if (shareDataValue instanceof BizPOI)
	    	 {
	    		 beanData = ((BizPOI)shareDataValue).toTxNode();
	    	 }
	    	 
	    	 try 
	    	 {
				out.write("<bean name=\""+beanName+"\" valueType=\"TxNode\""+" value=\""+Utility.TxNode2Base64(beanData)+"\""+" />");
			 } 
	    	 catch (IOException e) 
			 {
	 			if(logger.isDebugEnabled())
				{
					logger.debug("DataHandler.toXML",e);
				}
			 }
	     }
	     
	     
	     ////print the sharedata to xml in the following format
	     ////	<shareData>
	     ////		<bean name="xx" valuetype="TxNode" value="...a base64 string..." />
	     ////		...........
	     ////		<bean name="xx" valuetype="TxNode" value="...a base64 string..." />
	     ////	</shareData>
	     ////if there is no sharedata in the page, will not print anything
	     keys = shareData.keys();
	     boolean hasShareData = false;
	     while(keys.hasMoreElements())
	     {
	    	 if(!hasShareData)
	    	 {
	    		 try {
					out.write("<shareData>");
				} catch (IOException e) {
					if(logger.isDebugEnabled())
					{
						logger.debug("DataHandler.toXML.writeShareData",e);
					}
				}
	    		 hasShareData = true;
	    	 }
	    	 String beanName = (String)keys.nextElement();
	    	 //TxNode beanData = (TxNode)shareData.get(beanName);
	    	 
	    	 Object shareDataValue = shareData.get(beanName);
	    	 TxNode beanData = new TxNode();
	    	 if(shareDataValue instanceof TxNode)
	    	 {
	    		 beanData = (TxNode)shareDataValue;
	    	 }else if (shareDataValue instanceof Stop)
	    	 {
	    		 beanData = ((Stop)shareDataValue).toTxNode();
	    	 }else if (shareDataValue instanceof BizPOI)
	    	 {
	    		 beanData = ((BizPOI)shareDataValue).toTxNode();
	    	 }
	    	 
	    	 try 
	    	 {
				out.write("<bean name=\""+beanName+"\" valueType=\"TxNode\""+" value=\""+Utility.TxNode2Base64(beanData)+"\"" +" />");
			 } 
	    	 catch (IOException e) 
			 {
	 			if(logger.isDebugEnabled())
				{
					logger.debug("DataHandler.toXML.wirtebeandata",e);
				}
			 }
	     }
	     if(hasShareData)
	     {
	    	 try {
				out.write("</shareData>");
			} catch (IOException e) {
				if(logger.isDebugEnabled())
				{
					logger.debug("DataHandler.toXML.endwriteSharedata",e);
				}
			}
	     }
	     
	     
	     //// special note for situation about have incoming sharedata but when output, the sharedata is empty
	     if((!hasShareData)&&(hasIncomingShareData))
	     {
	    	 try {
				out.write("<shareData></shareData>");
			} catch (IOException e) {
				if(logger.isDebugEnabled())
				{
					logger.debug("DataHandler.toXML.printlnEmptyShareData",e);
				}
			}
	     }
	     
	}


	
	private void parseXML(String inputString) 
	{
		try 
		{
			long ts1 = System.currentTimeMillis();

			
			////parse the xml using Digester and set to the proper fields: inputParameters and shareData
			ByteArrayInputStream bais = new ByteArrayInputStream(inputString.getBytes("UTF-8"));
					
			Digester digester = new Digester();
			digester.setValidating(false);
			
			////set the parse rule
			digester.addObjectCreate("*/root", "com.telenav.tnbrowser.util.RootWrapper");
			digester.addObjectCreate("*/inputParameter", "com.telenav.tnbrowser.util.InputParametersWrapper");
			digester.addObjectCreate("*/shareData", "com.telenav.tnbrowser.util.ShareDataWrapper");
			digester.addObjectCreate("*/param", "com.telenav.tnbrowser.util.ParamWrapper");
			digester.addObjectCreate("*/param/Stop", "com.telenav.tnbrowser.util.StopWrapper");
			digester.addObjectCreate("*/param/POIDetail", "com.telenav.tnbrowser.util.BizPOIWrapper");
			digester.addObjectCreate("*/param/POIDetail/Stop", "com.telenav.tnbrowser.util.StopWrapper");
			digester.addSetProperties("*/param");
			digester.addSetNext("*/param", "addParam","com.telenav.tnbrowser.util.ParamWrapper");
//			digester.addObjectCreate("*/TxNode", "com.telenav.tnbrowser.util.TxNodeWrapper");
			digester.addObjectCreate("*/param/TxNode", "com.telenav.tnbrowser.util.TxNodeWrapper");
			digester.addObjectCreate("*/TxNode/TxNode", "com.telenav.tnbrowser.util.TxNodeWrapper");
			digester.addCallMethod("*/value", "addValue", 0);
			digester.addCallMethod("*/message", "addMsg", 0);
			digester.addCallMethod("*/binary", "setBindata",0);	
			digester.addCallMethod("*/Stop/Lat", "addLat",0);
			digester.addCallMethod("*/Stop/Lon", "addLon",0);
			digester.addCallMethod("*/Stop/AddressType", "addAddressType",0);
			digester.addCallMethod("*/Stop/AddressStatus", "addAddressStatus",0);
			digester.addCallMethod("*/Stop/IsGeocoded", "addIsGeocoded",0);
			digester.addCallMethod("*/Stop/Hashcode", "addHashcode",0);
			digester.addCallMethod("*/Stop/IsSharedAddress", "addIsSharedAddress",0);
			digester.addCallMethod("*/Stop/Label", "addLabel",0);
			digester.addCallMethod("*/Stop/FirstLine", "addFirstLine",0);
			digester.addCallMethod("*/Stop/City", "addCity",0);
			digester.addCallMethod("*/Stop/State", "addState",0);
			digester.addCallMethod("*/Stop/StopId", "addStopId",0);
			digester.addCallMethod("*/Stop/Zip", "addZip",0);
			digester.addCallMethod("*/Stop/Country", "addCountry",0);
			digester.addCallMethod("*/POIDetail/SupportInfo", "addSupportInfo",0);
			digester.addCallMethod("*/POIDetail/BrandName", "addBrandName",0);
			digester.addCallMethod("*/POIDetail/PhoneNumber", "addPhoneNumber",0);
			digester.addCallMethod("*/POIDetail/SupplementInfo", "addSupplementInfo",0);
			digester.addCallMethod("*/POIDetail/ParentCateName", "addParentCateName",0);
			digester.addCallMethod("*/POIDetail/Price", "addPrice",0);
			digester.addCallMethod("*/POIDetail/GroupId", "addGroupId",0);
			digester.addCallMethod("*/POIDetail/Distance", "addDistance",0);
			digester.addCallMethod("*/POIDetail/Navigatable", "addNavigatable",0);
			digester.addCallMethod("*/POIDetail/Layout", "addLayout",0);
			digester.addSetNext("*/TxNode/TxNode", "addChild", "com.telenav.tnbrowser.util.TxNodeWrapper");
			digester.addSetNext("*/param/TxNode", "addChild","com.telenav.tnbrowser.util.IParamObject");
			digester.addSetNext("*/param/Stop", "addChild","com.telenav.tnbrowser.util.IParamObject");
			digester.addSetNext("*/param/POIDetail/Stop", "addStop","com.telenav.tnbrowser.util.IParamObject");
			digester.addSetNext("*/param/POIDetail", "addChild","com.telenav.tnbrowser.util.IParamObject");
			digester.addSetNext("*/inputParameter", "setInputParameters","com.telenav.tnbrowser.util.InputParametersWrapper");
			digester.addSetNext("*/shareData", "setShareData","com.telenav.tnbrowser.util.ShareDataWrapper");
			
			long ts2 = System.currentTimeMillis();
			
			
			////parse
			Object result = digester.parse(bais);
			
			
			///store to the fields
			Hashtable temp = ((RootWrapper)result).getInputParameters();
			if(temp!=null)
			{
				inputParameters = temp;
			}
			temp = ((RootWrapper)result).getShareDatas();
			if(temp!=null)
			{
				shareData = temp;
			}
			
			long ts3 = System.currentTimeMillis();
			

		} 
		catch (Exception e) 
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("DataHandler.parseXML",e);
			}
		}
	}
	
	
	public Object getParameter(String key)
	{
		if(key!=null&&key.length()>0&&outputParameters!=null)
		{
			return outputParameters.get(key);
		}
		else
		{
			return null;
		}
	}
	
	public void setParameter(String key, TxNode value)
	{
		
		if(key!=null&&key.length()>0&&value!=null)
		{
			outputParameters.put(key, value);
		}
	}
	
	public Object getShareData(String key)
	{
		if(key!=null&&key.length()>0&&shareData!=null)
		{
			return shareData.get(key);
		}
		else
		{
			return null;
		}
	}
	
	public void addShareData(String key, TxNode value)
	{
		if(key!=null&&key.length()>0&&value!=null)
		{
			shareData.put(key, value);
		}
	}
	
	public void removeShareData(String key)
	{
		if(key!=null&&key.length()>0)
		{
			shareData.remove(key);
		}
	}
	
	public void removeAllShareData()
	{
		shareData.clear();
	}
	
	public static void sendAJAXResponse(HttpServletResponse response, byte[] data) throws IOException
	{
		ServletOutputStream os = response.getOutputStream();
		
		os.write(data);
		
		os.flush();
	}
	
	public static void sendAJAXResponse(HttpServletResponse response, TxNode dataNode) throws IOException
	{
		byte[] data = TxNode.toByteArray(dataNode,"UTF-8");
		
		DataHandler.sendAJAXResponse(response, data);
	}

	public Hashtable getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(Hashtable clientInfo) {
		this.clientInfo = clientInfo;
	}
}
