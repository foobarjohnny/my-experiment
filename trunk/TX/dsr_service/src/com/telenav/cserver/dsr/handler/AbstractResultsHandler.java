package com.telenav.cserver.dsr.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.telenav.cserver.dsr.ds.ProcessedResult;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.ds.RecResult;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.util.StrUtil;
import com.telenav.cserver.dsr.util.ProcessUtil;

/**
 * 
 * @author joses
 *
 */
public abstract class AbstractResultsHandler implements IResultsHandler {

	private static final Logger logger = Logger.getLogger(AbstractResultsHandler.class.getName());
    private static final int MAX_DSR_RESULT = 5;
	
	protected static final int STATUS_FAILED = 0;
    protected static final int STATUS_SUCCESS = 1;
    protected static final int STATUS_BREAK = 2;
    
    protected ProcessObject procObj;
    protected byte recType;
    protected RecContext context;
    protected List<ProcessedResult> processedResults = new ArrayList<ProcessedResult>();
    protected int id;
    
    
	public ProcessObject process(ProcessObject obj) {
		
		logger.log(Level.FINE, "Entering process()");
		procObj = obj;
		context = procObj.getContext();
		recType = context.recType;
		
		List<ProcessedResult> resultList = procObj.getProcessedResults();

		RecResult[] resultItems = procObj.getRawResults();
		logger.log(Level.FINE, "# of rawResults: "+resultItems.length);
		
		RecResult item = null;
		for(int i = 0; i < resultItems.length; i++){
			RecResult recItem = resultItems[i];
			
			if(recItem.equals(item))
				continue;
			
			id = recItem.getId();
			int status = processResult(recItem);
			
			logger.log(Level.FINE, "Return status: "+status);
			
			if(status == STATUS_FAILED)
				continue;
			
			if(processedResults.size() > 0){
				for(ProcessedResult procResult : processedResults){
					if(StrUtil.notBlank(procResult.getLiteral())){
						if(resultList.size() >= MAX_DSR_RESULT)
							break;
						
						boolean isDup = ProcessUtil.isDuplicateItem(procResult, resultList);
						logger.info("list: "+ resultList);
						logger.info(procResult+" is duplicate: "+isDup);
						
						if(!isDup)
						{
							resultList.add(procResult);
						}
					}
				}
			}
			
			postProcess();			
			
			if (status == STATUS_BREAK) {
                break;
            }
			
			item = recItem;			
		}
		procObj.setProcessedResults(resultList);
		return procObj;
	}

	abstract int processResult(RecResult recItem);
	abstract void postProcess();
}
