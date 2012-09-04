package com.telenav.cserver.dsr.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.telenav.audio.streaming.audiolog.AudioLogService;
import com.telenav.cserver.dsr.ds.ProcessedResult;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.ds.RecResult;
import com.telenav.cserver.dsr.util.ResourceConst;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.resource.data.PromptItem;

/**
 * 
 * @author joses
 *
 */
public class ProcessObject {

	protected static Logger logger = Logger.getLogger(ProcessObject.class.getName());
	private static int TYPE_DSR_MSG = 1;
    private static final int MAX_DSR_RESULT = 5;
    
    public long transactionId ;
	public int statusCode = 0;
	public String errorMessage;
    public RecContext context = null;
    public RecResult[] rawResults;
	public List<ProcessedResult> processedResults = new ArrayList<ProcessedResult>();
	public List<PromptItem[]> promptItems = new ArrayList<PromptItem[]>();
	public ProcessProfile profile = new ProcessProfile();
	public byte[] audioData;
	public boolean pilot = false;
	
	public boolean isPilot() {
		return pilot;
	}

	public void setPilot(boolean pilot) {
		this.pilot = pilot;
	}

	public ProcessObject()
	{
		transactionId = AudioLogService.instance.getNewTransactionId();
	}
	
	public ProcessObject(long transactionId)
	{
		this.transactionId = transactionId;
	}
	
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public RecContext getContext() {
		return context;
	}
	public void setContext(RecContext context) {
		this.context = context;
	}
	public List<ProcessedResult> getProcessedResults() {
		return processedResults;
	}
	public void setProcessedResults(List<ProcessedResult> processedResults) {
		this.processedResults = processedResults;
	}
    public RecResult[] getRawResults() {
		return rawResults;
	}
	public void setRawResults(RecResult[] rawResults) {
		this.rawResults = rawResults;
	}    
	public List<PromptItem[]> getPromptItems() {
		return promptItems;
	}
	public void setPromptItems(List<PromptItem[]> promptItems) {
		this.promptItems = promptItems;
	}	
	public ProcessProfile getProfile() {
		return profile;
	}
	public void setProfile(ProcessProfile profile) {
		this.profile = profile;
		profile.setTransactionId(transactionId);
	}
	
	public byte[] getAudioData() {
		return audioData;
	}
	public void setAudioData(byte[] audioData) {
		this.audioData = audioData;
	}
	
    public String getDsrResultsString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n-----Result-----\n");
        sb.append("wasSuccessful=").append(profile.wasDsrSuccessful()).append("\n");
        sb.append("statusCode=").append(profile.getDsrStatusCode()).append("\n");
        sb.append("message=").append(profile.getDsrMessage()).append("\n");
        sb.append("transactionID=").append(profile.getDsrTransactionId()).append("\n");
        sb.append("RecResults:").append("\n");

        if (rawResults != null) {
            for (int i = 0; i < rawResults.length; i++)
                sb.append(i).append(":").append(rawResults[i]).append("\n");
        }
        
        return sb.toString();
    }
	public int getStatusCode()
	{
		return statusCode;
	}
	public void setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
	}
	public String getErrorMessage()
	{
		return errorMessage;
	}
	public void setErrorMessage(String statusMessage)
	{
		this.errorMessage = statusMessage;
	}
	
}
