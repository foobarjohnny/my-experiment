package com.telenav.cserver.poi.executor;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.WebServiceConfigurator;
import com.telenav.ws.datatypes.feedback.FeedbackQuestion;
import com.telenav.ws.services.feedback.FeedbackServiceStub;
import com.telenav.ws.services.feedback.GetFeedbackQuestionsRequestDTO;
import com.telenav.ws.services.feedback.GetFeedbackQuestionsResponseDTO;

public class GenericFeedbackRetrievalExecutor extends AbstractExecutor{

	private static Logger logger = Logger.getLogger(GenericFeedbackRetrievalExecutor.class);
	/** feedback service end-point in property file.*/
    private static final String FEEDBACK_SERVICE_END_POINT = WebServiceConfigurator.getUrlOfFeedbackServer();
 
    private static final ConcurrentHashMap<String, FeedbackQuestion[]> surveyHashMap = new ConcurrentHashMap<String, FeedbackQuestion[]>(5, 0.75f, 1);
    
	@Override
	public void doExecute(ExecutorRequest request, ExecutorResponse response,
			ExecutorContext context) throws ExecutorException {
		// Get the request and response
		GenericFeedbackRetrievalRequest poiRequest   = (GenericFeedbackRetrievalRequest)request;
		GenericFeedbackRetrievalResponse poiResponse = (GenericFeedbackRetrievalResponse) response;
		logger.info("Feed back retrieval request: " + poiRequest);
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("GenericFeedbackRetrieval");
		try{
 			String feedbackTopic   = poiRequest.getFeedbackTopic();
 			String locale = poiRequest.getLocale();
			// call feedback service to send info
 			FeedbackServiceStub stub =  null;
			try 
			{
				logger.info("Retrieve feedback questions via feeback service ...");
				logger.info("Request topic: " + feedbackTopic + " locale: " + locale);
				FeedbackQuestion[] feedbackList = null;
				
				String surveyContentKey = feedbackTopic+ "_" + locale;
				if(surveyHashMap.containsKey(surveyContentKey)){
					//get feedbackList from cache
	 				feedbackList = surveyHashMap.get(surveyContentKey);
				} else {

					// FeedbackTopic topic =
					// FeedbackTopic.Factory.fromValue(feedbackTopic);

					System.out.println("feed back service end point: "
							+ FEEDBACK_SERVICE_END_POINT);
					stub = new FeedbackServiceStub(FEEDBACK_SERVICE_END_POINT);

					GetFeedbackQuestionsRequestDTO requestDTO = new GetFeedbackQuestionsRequestDTO();

					// requestDTO.setTopic(topic);
					requestDTO.setLocale(locale);
					requestDTO.setTopicString(feedbackTopic);
					requestDTO.setCarrier(null);// (userProfile.getCarrier());
					requestDTO.setPlatform(null);// userProfile.getPlatform());
					requestDTO.setClientName("default");// ("c-server");
					requestDTO.setClientVersion("default");// (userProfile.getVersion());
					// requestDTO.setDevice(null);//(userProfile.getDevice());
					requestDTO.setTransactionId("default");// ("unknown");

					GetFeedbackQuestionsResponseDTO responseDTO = stub
							.getAllFeedbackQuestions(requestDTO);

					// put feedback list into response. later be formatted into
					// txNode and
					// passed along to client end.
					feedbackList = responseDTO.getQuestion();
					if(feedbackList != null){
						surveyHashMap.put(surveyContentKey, feedbackList);
					}else{
						logger.error("feed back retrieval empty for topic " + feedbackTopic);
					}
				}
				poiResponse.setFeedbackList(feedbackList);
				poiResponse.setPageSize(poiRequest.getPageSize());
				poiResponse.setPageNumber(poiRequest.getPageNumber());
				
				logger.info("feedback retrieval requst sent: " + request);
			} catch(Exception e) {
				logger.error("feedback retrieval handling failed: " + e.getMessage());
				e.printStackTrace();
				throw e;
			}finally{
				WebServiceUtils.cleanupStub(stub);
			}
			
			poiResponse.setStatus(ExecutorResponse.STATUS_OK);
            
		} catch(Exception ex) {
			cli.setStatus(ex);
			poiResponse.setErrorMessage("COSE.notAvailable");
			poiResponse.setStatus(ExecutorResponse.STATUS_FAIL);
		}finally{
			cli.complete();
		}
		
		
	}

}
