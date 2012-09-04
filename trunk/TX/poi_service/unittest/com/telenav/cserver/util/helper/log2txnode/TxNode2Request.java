package com.telenav.cserver.util.helper.log2txnode;

import org.apache.struts.mock.MockHttpServletRequest;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class TxNode2Request {
	
	private TxNode node = null;
	private static TxNode2Request instance = null;
	
	private TxNode2Request(){
		
	}
	
	public void setNode(TxNode node){
		this.node = node;
	}
	public TxNode getNode(){
		return this.node;
	}
	
	public static TxNode2Request getInstance(TxNode node){
		if(instance == null){
			instance = new TxNode2Request();
		}
		instance.setNode(node);
		return instance;
	}

	
	public void toMockHttpServletRequest(MockHttpServletRequest request){
		
		UserProfile userProfile = new UserProfile();

		for (int j = 0; j < node.childrenSize(); ++j)
		{
			TxNode child = node.childAt(j);
			if (child.getValuesCount() == 0) {
				continue;
			}
			
			byte childType = (byte)(int)child.valueAt(0);
			switch (childType){
		     
			case 82:
			         userProfile.setCarrier(child.msgAt(0));
			         userProfile.setPlatform(child.msgAt(1));
			         userProfile.setVersion(child.msgAt(2));
			         userProfile.setDevice(child.msgAt(3));
			         userProfile.setBuildNumber(child.msgAt(4));
			         userProfile.setGpsType(child.msgAt(5));
			         userProfile.setProduct(child.msgAt(6));
			         if (child.msgsSize() > 7){
			        	 	userProfile.setMapDpi(child.msgAt(7));
			         }
			         
			         userProfile.setOSVersion(child.msgAt(8));
			         userProfile.setProgramCode(child.msgAt(0));
			         break;
			         
			 case 86:
			         userProfile.setMin(child.msgAt(0));
			         userProfile.setPassword(child.msgAt(1));
			         userProfile.setUserId(child.msgAt(2));
			         break;
			         
			case 83:
			    	 userProfile.setLocale(child.msgAt(4));
			    	 userProfile.setRegion(child.msgAt(5));
			         userProfile.setAudioFormat(child.msgAt(6));
			         break;

			case 87:
					userProfile.setScreenWidth(child.msgAt(2));
					userProfile.setScreenHeight(child.msgAt(3));
			    	break;
			    	  
			      case 85:
			    	  
			   }
		}

		if(userProfile.getDeviceID() != null)
		{
			request.addParameter(DataHandler.KEY_DEVICE_ID,userProfile.getDeviceID());
		}
		
		if(userProfile.getGuideTone() != null)
		{
			request.addParameter(DataHandler.KEY_GUIDETONE, userProfile.getGuideTone());
		}
		
		if(userProfile.getLocale() != null)
		{
			request.addParameter(DataHandler.KEY_LOCALE, userProfile.getLocale());
		}
		
		request.addParameter(DataHandler.KEY_REGION, userProfile.getRegion());
		request.addParameter(DataHandler.KEY_AUDIOFORMAT, userProfile.getAudioFormat());
		request.addParameter(DataHandler.KEY_CARRIER, userProfile.getCarrier());
		request.addParameter(DataHandler.KEY_PLATFORM, userProfile.getPlatform());
		request.addParameter(DataHandler.KEY_DEVICEMODEL, userProfile.getDevice());
		request.addParameter(DataHandler.KEY_BUILDNUMBER, userProfile.getBuildNumber());
		
		request.addParameter(DataHandler.KEY_WIDTH, userProfile.getScreenWidth());
		request.addParameter(DataHandler.KEY_HEIGHT, userProfile.getScreenHeight());
		request.addParameter(DataHandler.KEY_BROWSERVERSION, (String)userProfile.getAttribute(DataHandler.KEY_BROWSERVERSION));
		
		request.addParameter(DataHandler.KEY_USERACCOUNT, userProfile.getMin());
		request.addParameter(DataHandler.KEY_USERPIN, userProfile.getPassword());
		request.addParameter(DataHandler.KEY_USERID, userProfile.getUserId());
		request.addParameter(DataHandler.KEY_PRODUCTTYPE, userProfile.getProduct());
		request.addParameter(DataHandler.KEY_VERSION, userProfile.getVersion());
		request.addParameter(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "");
		request.addParameter(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "");
		request.addParameter(DataHandler.KEY_DEVICE_INFO, userProfile.getDevice());
		request.addParameter(DataHandler.KEY_SUBID2, userProfile.getSsoToken());
		
		// modification
		
	
	}
}
