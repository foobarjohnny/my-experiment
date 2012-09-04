package com.telenav.cserver.dsr.framework;

public class ProcessProfile {

	private long transactionId ;

	private long dsrTransactionId;
	private String dsrMessage;
	private boolean dsrSuccessful;
	private int dsrStatusCode;
	
	private long recStart ;
	private long recTime ;
	
	private long netIOStart ;
	private long netIOTime ;
	
	private long externalProcessStart ;
	private long externalProcessTime ;
	
	private long totalProcessStart ;
	private long totalProcessTime = 0;

	private String carrier = "";
	private String deviceType = "";
	private String literal = "";
	private String cserver_machine_name = "";
	
	private long dsrStart;

	private String ptn;
	private int recType;
	private float lat;
	private float lon;
	private int audioFormat;
	private String cserverresult;
	
	
	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getLiteral() {
		return literal;
	}

	public void setLiteral(String literal) {
		this.literal = literal;
	}

	public String getCserver_machine_name() {
		return cserver_machine_name;
	}

	public void setCserver_machine_name(String cserverMachineName) {
		cserver_machine_name = cserverMachineName;
	}

	public long getDsrTransactionId() {
		return dsrTransactionId;
	}

	public void setDsrTransactionId(long dsrTransactionId) {
		this.dsrTransactionId = dsrTransactionId;
	}
	
	public String getDsrMessage() {
		return dsrMessage;
	}

	public void setDsrMessage(String dsrMessage) {
		this.dsrMessage = dsrMessage;
	}

    public boolean wasDsrSuccessful() {
        return dsrSuccessful;
    }
    
    public void setDsrSuccessful(boolean dsrSuccessful) {
        this.dsrSuccessful = dsrSuccessful;
    }

	public int getDsrStatusCode() {
		return dsrStatusCode;
	}

	public void setDsrStatusCode(int dsrStatusCode) {
		this.dsrStatusCode = dsrStatusCode;
	}
	
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	
	public long getTransactionId()
	{
		return transactionId;
	}
	
	public void recBegin()
	{
		recStart = System.currentTimeMillis() ;
	}
	
	public void recEnd()
	{
		recTime = System.currentTimeMillis() - recStart ;
	}
	
	public long getRecProxyTime()
	{
		return recTime;
	}
	
	public void netIOBegin()
	{
		netIOStart = System.currentTimeMillis() ;
	}
	
	public void netIOEnd()
	{
		netIOTime = System.currentTimeMillis() - netIOStart ;
	}
	
	public long getNetIOTime()
	{
		return netIOTime;
	}
	
	public void externalProcessBegin()
	{
		externalProcessStart = System.currentTimeMillis() ;
	}
	
	public void externalProcessEnd()
	{
		externalProcessTime = System.currentTimeMillis() - externalProcessStart ;
	}
	
	public long getExternalProcessTime()
	{
		return externalProcessTime;
	}

	public void totalProcessBegin()
	{
		totalProcessStart = System.currentTimeMillis() ;
	}

	public long getCserverRequestTime(){
		return totalProcessStart;
	}
	
	public void totalProcessEnd()
	{
		totalProcessTime = System.currentTimeMillis() - totalProcessStart ;
	}
	
	public long getTotalProcessTime()
	{
		return totalProcessTime;
	}

	public long getRequestTimestamp()
	{
		return totalProcessStart;
	}

	public long getDsrStart() {
		return dsrStart;
	}

	public void setDsrStart(long dsrStart) {
		this.dsrStart = dsrStart;
	}
	
	
	public String getPtn() {
		return ptn;
	}

	public void setPtn(String ptn) {
		this.ptn = ptn;
	}

	public int getRecType() {
		return recType;
	}

	public void setRecType(int recType) {
		this.recType = recType;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(int lon) {
		this.lon = lon;
	}

	public int getAudioFormat() {
		return audioFormat;
	}

	public void setAudioFormat(int audioFormat) {
		this.audioFormat = audioFormat;
	}

	public String getCserverresult() {
		return cserverresult;
	}

	public void setCserverresult(String cserverresult) {
		this.cserverresult = cserverresult;
	}
}
