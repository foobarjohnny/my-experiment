/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.cserver.html.util.HtmlPoiUtil;
import com.telenav.cserver.util.WebServiceConfigurator;
import com.telenav.datatypes.user.management.v10.UserQueryBy;
import com.telenav.datatypes.user.v20.User;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.user.management.v10.GetUserRequestDTO;
import com.telenav.services.user.management.v10.GetUserResponseDTO;
import com.telenav.services.user.management.v10.IsUniqueUsernameRequestDTO;
import com.telenav.services.user.management.v10.IsUniqueUsernameResponseDTO;
import com.telenav.services.user.management.v10.UpdateUserRequestDTO;
import com.telenav.services.user.management.v10.UserManagementResponseDTO;
import com.telenav.services.user.management.v10.UserManagementServiceStub;

/**
 * @TODO	Implement specific business logic
 * @author	chfzhang@telenav.cn
 * @version 1.0	Feb 21, 2011
 */

public class HtmlNickNameServiceProxy {
	
	private static HtmlNickNameServiceProxy instance = new HtmlNickNameServiceProxy(); 
	
	//log
	private static final Logger logger = Logger.getLogger(HtmlNickNameExecutor.class);

	/**
	 * 
	 * @return
	 */
	public static HtmlNickNameServiceProxy getInstance()
	{
		return instance;
	}
	
	/**
	 * @TODO	query nickname
	 * @param nickNameRequest
	 * @param nickNameResponse
	 * @param tnContext
	 * @return
	 */
	public  HtmlNickNameResponse queryNickName(HtmlNickNameRequest nickNameRequest,HtmlNickNameResponse nickNameResponse,TnContext tnContext){
		
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("queryNickName");
        long userId = nickNameRequest.getUserId();
    	nickNameResponse.setStatus(ExecutorResponse.STATUS_OK);
        nickNameResponse.setUserId(userId);
        nickNameResponse.setNickName("");
        UserManagementServiceStub stub = null;
		try {
			//set query conditions
			GetUserRequestDTO getUserRequest = new GetUserRequestDTO();
	        getUserRequest.setParam(Long.toString(userId));
	        getUserRequest.setParamType(UserQueryBy.USER_ID);
	        
	        getUserRequest.setClientName(HtmlConstants.clientName);
	        getUserRequest.setClientVersion(HtmlConstants.clientVersion);
	        getUserRequest.setContextString(tnContext.toContextString());
	        getUserRequest.setTransactionId(HtmlPoiUtil.getTrxId());
	        //get userInfo
	        stub = getServer();
	        GetUserResponseDTO responseDto = stub.getUser(getUserRequest);
	        if(responseDto != null)
	        {
		        User  user = responseDto.getUser();
	            if (user != null)// set response param
	            {
	    			logger.debug("queryNickName userId:" + userId + ",user name:" + user.getUserName());
	            	//set query result
	    	        nickNameResponse.setNickName(user.getUserName());
	            }
	            cli.addData("Response", "Status Code:" + responseDto.getResponseStatus());
	        }
           // nickNameResponse.setMessage("Status Code =" + statusCode + " Message=" + statusMsg);
        }catch (Exception e) {
			// TODO Auto-generated catch block
        	logger.error(e);
        	e.printStackTrace();
            cli.setStatus(e);
            cli.setState(CliConstants.STATUS_FAIL);
		} 
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }
        
		return nickNameResponse;
	}
	/**
	 * @TODO	check nickname if exist
	 * @param nickNameRequest
	 * @param nickNameResponse
	 * @param tnContext
	 * @return
	 */
	public HtmlNickNameResponse checkNickName(HtmlNickNameRequest nickNameRequest,HtmlNickNameResponse nickNameResponse,TnContext tnContext){
		
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("checkNickName");
        String nickNameStr = nickNameRequest.getNickName();
        nickNameResponse.setIsUniqueNickName(HtmlConstants.OPERATE_UNIQUE_NICKNAME);
    	nickNameResponse.setStatus(ExecutorResponse.STATUS_OK);
		UserManagementServiceStub stub = null;
		try {
			
	        //set conditions
	        IsUniqueUsernameRequestDTO isUniqueRequest = new IsUniqueUsernameRequestDTO();
	        isUniqueRequest.setUserName(nickNameStr);
	        isUniqueRequest.setClientName(HtmlConstants.clientName);
	        isUniqueRequest.setClientVersion(HtmlConstants.clientVersion);
	        isUniqueRequest.setContextString(tnContext.toContextString());
	        isUniqueRequest.setTransactionId(HtmlPoiUtil.getTrxId());
	        
	        //check NickName
	        stub = getServer();
	        IsUniqueUsernameResponseDTO isUniqueResponse = stub.isUniqueUsername(isUniqueRequest);
        	nickNameResponse.setNickName(nickNameStr);
            if (isUniqueResponse != null)
            {
            	boolean isUnique = isUniqueResponse.getUnique();
            	logger.debug("checkNickName userName:" + nickNameStr + ",isUnique" + isUnique);
            	cli.addData("data", "checkNickName userName:" + nickNameStr + ",isUnique" + isUnique);
    	        if (isUnique) {// the nick name is unique
    	        	nickNameResponse.setIsUniqueNickName(HtmlConstants.OPERATE_UNIQUE_NICKNAME);
    	        }else{//the nick name has exist
    	        	nickNameResponse.setIsUniqueNickName(HtmlConstants.OPERATE_NOT_UNIQUE_NICKNAME);
    	        }
            }

        }catch (Exception e) {
			// TODO Auto-generated catch block
        	logger.error(e);
        	e.printStackTrace();
            cli.setStatus(e);
            cli.setState(CliConstants.STATUS_FAIL);
		} 
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }
        
		return nickNameResponse;
	}
	/**
	 * TODO	add a new record about nickname
	 * @param nickNameRequest
	 * @param nickNameResponse
	 * @param tnContext
	 * @return
	 */
	public HtmlNickNameResponse checkAndaddNickName(HtmlNickNameRequest nickNameRequest,HtmlNickNameResponse nickNameResponse,TnContext tnContext){
		
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("checkAndaddNickName");
        nickNameResponse.setStatus(ExecutorResponse.STATUS_OK);
		UserManagementServiceStub stub = null;
		try {
			
            String userName = nickNameRequest.getNickName();
			//set conditions
	        IsUniqueUsernameRequestDTO isUniqueRequest = new IsUniqueUsernameRequestDTO();
	        isUniqueRequest.setUserName(userName);
	        isUniqueRequest.setClientName(HtmlConstants.clientName);
	        isUniqueRequest.setClientVersion(HtmlConstants.clientVersion);
	        isUniqueRequest.setContextString(tnContext.toContextString());
	        isUniqueRequest.setTransactionId(HtmlPoiUtil.getTrxId());
	        
	        //check NickName
	        stub = getServer();
	        IsUniqueUsernameResponseDTO isUniqueResponse = stub.isUniqueUsername(isUniqueRequest);
        	nickNameResponse.setNickName(userName);
        	
        	
            if (isUniqueResponse != null)
            {
            	boolean isUnique = isUniqueResponse.getUnique();

            	logger.debug("checkNickName userName:" + userName + ",isUnique" + isUnique);
            	cli.addData("data", "checkNickName userName:" + userName + ",isUnique" + isUnique);
    	        if (isUnique) {// the nick name is unique
    	        	nickNameResponse.setIsUniqueNickName(HtmlConstants.OPERATE_UNIQUE_NICKNAME);
    	        	
    	        	long userId = nickNameRequest.getUserId();

    	            logger.debug("addNickName userId:" + userId + ",userName" + userName);
    				//set conditions
    				GetUserRequestDTO getUserRequest = new GetUserRequestDTO();
    		        getUserRequest.setParam(Long.toString(userId));
    		        getUserRequest.setParamType(UserQueryBy.USER_ID);
    		        getUserRequest.setClientName(HtmlConstants.clientName);
    		        getUserRequest.setClientVersion(HtmlConstants.clientVersion);
    		        getUserRequest.setContextString(tnContext.toContextString());
    		        getUserRequest.setTransactionId(HtmlPoiUtil.getTrxId());

    		        //get user
    		        GetUserResponseDTO responseDto = stub.getUser(getUserRequest);
    		        User user = responseDto.getUser();
    		        if(user != null)
    		        {
	    		        user.setUserName(userName);
	    		        //update user
	    		        UpdateUserRequestDTO updateRequest = new UpdateUserRequestDTO();
	    	            updateRequest.setUser(user);
	    	            updateRequest.setClientName(HtmlConstants.clientName);
	    	            updateRequest.setClientVersion(HtmlConstants.clientVersion);
	    	            updateRequest.setContextString(tnContext.toContextString());
	    	            updateRequest.setTransactionId(HtmlPoiUtil.getTrxId());
	    		        
	    	            UserManagementResponseDTO updateResponse = stub.updateUser(updateRequest);
    		        }
    	        }else{//the nick name has exist
    	        	nickNameResponse.setIsUniqueNickName(HtmlConstants.OPERATE_NOT_UNIQUE_NICKNAME);
    	        }
            }
            else
            {
            	nickNameResponse.setIsUniqueNickName(HtmlConstants.OPERATE_UNIQUE_NICKNAME);
            }
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			cli.setStatus(e);
	        cli.setState(CliConstants.STATUS_FAIL);
		} 
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }
		
		return nickNameResponse;
	}
	/**
	 * TODO   add a new record about nickname
	 * @param nickNameRequest
	 * @param nickNameResponse
	 * @param tnContext
	 * @return
	 */
	public HtmlNickNameResponse addNickName(HtmlNickNameRequest nickNameRequest,HtmlNickNameResponse nickNameResponse,TnContext tnContext){
		
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("addNickName");
        nickNameResponse.setStatus(ExecutorResponse.STATUS_OK);
		long userId = nickNameRequest.getUserId();
        String userName = nickNameRequest.getNickName();
        nickNameResponse.setNickName(userName);
		UserManagementServiceStub stub = null;
		try {

            logger.debug("addNickName userId:" + userId + ",userName" + userName);
			//set conditions
			GetUserRequestDTO getUserRequest = new GetUserRequestDTO();
	        getUserRequest.setParam(Long.toString(userId));
	        getUserRequest.setParamType(UserQueryBy.USER_ID);
	        getUserRequest.setClientName(HtmlConstants.clientName);
	        getUserRequest.setClientVersion(HtmlConstants.clientVersion);
	        getUserRequest.setContextString(tnContext.toContextString());
	        getUserRequest.setTransactionId(HtmlPoiUtil.getTrxId());

	        //get user
	        stub = getServer();
	        GetUserResponseDTO responseDto = stub.getUser(getUserRequest);
	        User user = responseDto.getUser();
	        user.setUserName(userName);
	        
	        //update user
	        UpdateUserRequestDTO updateRequest = new UpdateUserRequestDTO();
            updateRequest.setUser(user);
            updateRequest.setClientName(HtmlConstants.clientName);
            updateRequest.setClientVersion(HtmlConstants.clientVersion);
            updateRequest.setContextString(tnContext.toContextString());
            updateRequest.setTransactionId(HtmlPoiUtil.getTrxId());
	        
            UserManagementResponseDTO updateResponse = stub.updateUser(updateRequest);
            
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			cli.setStatus(e);
	        cli.setState(CliConstants.STATUS_FAIL);
		} 
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }
		
		return nickNameResponse;
	}
	/**
	 * TODO	get server
	 * @return
	 */
	
	public UserManagementServiceStub getServer(){
		
		UserManagementServiceStub server = null;
		try {
			server = new UserManagementServiceStub(HtmlCommonUtil.getWSContext(),WebServiceConfigurator.getUrlOfPoiReviewWrite());
		}catch(AxisFault e) {
            e.getCause();
		}
		
		return server;
	}

}
