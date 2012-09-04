package com.telenav.cserver.backend.proxy.adsreport;

import com.telenav.j2me.datatypes.TxNode;

public class AdsReportRequest
{
	private int packetType = -1;

	private String min = "";

	private String pin = "";

	private String userID = "";

	private String carrier = "";

	private String platform = "";

	private String version = "";

	private String device = "";

	private String productType = "";

	private String timeStamp = "";

	private String serverTimeStamp = "";

	private String logContent = "";

	public static final int INDEX_PARAMETER_USER_MIN = 0;
	public static final int INDEX_PARAMETER_USER_PIN = 1;
	public static final int INDEX_PARAMETER_USER_ID = 2;

	public static final int INDEX_PARAMETER_CLIENT_CARRIER = 3;
	public static final int INDEX_PARAMETER_CLIENT_PLATFORM = 4;
	public static final int INDEX_PARAMETER_CLIENT_VERSION = 5;
	public static final int INDEX_PARAMETER_CLIENT_DEVICE = 6;
	public static final int INDEX_PARAMETER_CLIENT_PRODUCT = 7;

	public String getServerTimeStamp() {
		return serverTimeStamp;
	}

	public void setServerTimeStamp(String serverTimeStamp) {
		this.serverTimeStamp = serverTimeStamp;
	}

	public void setPacketType(int packetType)
	{
		this.packetType = packetType;
	}

	public String getPacketType()
	{
		return "" + this.packetType;
	}

	public String getCarrier()
	{
		return carrier;
	}

	public String getDevice() 
	{
		return device;
	}

	public String getMin()
	{
		return min;
	}

	public String getPin()
	{
		return pin;
	}

	public String getPlatform() 
	{
		return platform;
	}

	public String getProductType()
	{
		return productType;
	}

	public String getTimeStamp() 
	{
		return timeStamp;
	}

	public String getUserID() 
	{
		return userID;
	}

	public String getVersion()
	{
		return version;
	}

	public String getLogContent() 
	{
		return logContent;
	}

	public void setCarrier(String carrier) 
	{
		this.carrier = carrier;
	}

	public void setDevice(String device) 
	{
		this.device = device;
	}

	public void setLogContent(String logContent)
	{
		this.logContent = logContent;
	}

	public void setMin(String min) 
	{
		this.min = min;
	}

	public void setPin(String pin)
	{
		this.pin = pin;
	}

	public void setPlatform(String platform) 
	{
		this.platform = platform;
	}

	public void setProductType(String productType)
	{
		this.productType = productType;
	}

	public void setTimeStamp(String timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	public void setUserID(String userID) 
	{
		this.userID = userID;
	}

	public void setVersion(String version) 
	{
		this.version = version;
	}

	public TxNode toTxNode()
	{
		TxNode node = new TxNode();
		node.addValue(this.packetType);
		node.addMsg(this.logContent);
		node.addMsg(this.timeStamp);
		node.addMsg(this.serverTimeStamp);

		TxNode subNode = new TxNode();
		subNode.addMsg(this.min);
		subNode.addMsg(this.pin);
		subNode.addMsg(this.userID);

		subNode.addMsg(this.carrier);
		subNode.addMsg(this.platform);
		subNode.addMsg(this.version);
		subNode.addMsg(this.device);
		subNode.addMsg(this.productType);

		node.addChild(subNode);
		return node;
	}

	public void fromTxNode(TxNode node)
	{
		if(node != null)
		{
			this.setPacketType((int) node.valueAt(0));
			this.setLogContent(node.msgAt(0));
			this.setTimeStamp(node.msgAt(1));
			this.setServerTimeStamp(node.msgAt(2));
			if(node.childrenSize() > 0)
			{
				TxNode subNode = node.childAt(0);
				if(subNode.msgsSize() > 0)
				{
					this.setMin(subNode.msgAt(INDEX_PARAMETER_USER_MIN));//0
					this.setPin(subNode.msgAt(INDEX_PARAMETER_USER_PIN));//1
					this.setUserID(subNode.msgAt(INDEX_PARAMETER_USER_ID));//2
					this.setCarrier(subNode.msgAt(INDEX_PARAMETER_CLIENT_CARRIER));//3
					this.setPlatform(subNode.msgAt(INDEX_PARAMETER_CLIENT_PLATFORM));//4
					this.setVersion(subNode.msgAt(INDEX_PARAMETER_CLIENT_VERSION));//5
					this.setDevice(subNode.msgAt(INDEX_PARAMETER_CLIENT_DEVICE));//6
					this.setProductType(subNode.msgAt(INDEX_PARAMETER_CLIENT_PRODUCT));//7
				}
			}
		}
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("packetType : " + packetType);
		buffer.append(" min : " + min);
		buffer.append(" pin : " + pin);
		buffer.append(" userID : " + userID);
		buffer.append(" carrier : " + carrier);
		buffer.append(" platform : " + platform);
		buffer.append(" version : " + version);
		buffer.append(" device : " + device);
		buffer.append(" productType : " + productType);
		buffer.append(" timeStamp : " + timeStamp);
		buffer.append(" logContent : " + logContent);
		return buffer.toString();
	}
}