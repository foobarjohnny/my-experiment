/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.framework.html.util.HtmlMessageHelper;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * @TODO	Call the executor to implement business logic
 * @author  zhhyan@telenav.cn
 * @version 1.0 
 */
public class HtmlAboutExecutor extends AbstractExecutor {

	@Override
	public void doExecute(ExecutorRequest req, ExecutorResponse resp,
			ExecutorContext context) throws ExecutorException {
		HtmlAboutRequest aboutRequest = (HtmlAboutRequest)req;
		HtmlAboutResponse aboutResponse = (HtmlAboutResponse)resp;
		
		HtmlClientInfo clientInfo = aboutRequest.getClientInfo();
		//String ssoToken = aboutRequest.getSsoToken();
		
		String dataSetKey = HtmlCommonUtil.getMapProvider(clientInfo);
		String dataSet = HtmlMessageHelper.getInstance().getMessageValue(clientInfo, dataSetKey);
		
		aboutResponse.setDataSet(dataSet);
	}
}
