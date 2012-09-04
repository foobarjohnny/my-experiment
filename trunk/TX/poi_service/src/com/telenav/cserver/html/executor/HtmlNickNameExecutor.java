/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.kernel.util.datatypes.TnContext;

/**
* @TODO	Call the executor to implement business logic
* @author chfzhang@telenav.cn
* @version 1.0  Feb 18, 2011
*/
public class HtmlNickNameExecutor extends AbstractExecutor{
    
	@Override
	public void doExecute(ExecutorRequest request, ExecutorResponse response,
			ExecutorContext context) throws ExecutorException {
		
		 TnContext tnContext = context.getTnContext();
		
		HtmlNickNameRequest nickNameRequest = (HtmlNickNameRequest)request;
		HtmlNickNameResponse nickNameResponse = (HtmlNickNameResponse)response;
		String operaType = nickNameRequest.getOperateType();
		nickNameResponse.setOperateType(nickNameRequest.getOperateType());
		
		if(HtmlConstants.OPERATE_NICKNAME_GET.equals(operaType)){//query nick name by user id
			nickNameResponse = HtmlNickNameServiceProxy.getInstance().queryNickName(nickNameRequest,nickNameResponse,tnContext);
		}
		else if(HtmlConstants.OPERATE_NICKNAME_CHECK.equals(operaType)){//check the nick name has exist
			nickNameResponse = HtmlNickNameServiceProxy.getInstance().checkNickName(nickNameRequest,nickNameResponse,tnContext);
		}
		else if(HtmlConstants.OPERATE_NICKNAME_ADD.equals(operaType)){//add a record about the nick name
			nickNameResponse = HtmlNickNameServiceProxy.getInstance().addNickName(nickNameRequest,nickNameResponse,tnContext);
		}
		else if(HtmlConstants.OPERATE_NICKNAME_CHECKANDUPDATE.equals(operaType)){//add a record about the nick name
			nickNameResponse = HtmlNickNameServiceProxy.getInstance().checkAndaddNickName(nickNameRequest,nickNameResponse,tnContext);
		}
	}
}
