package com.telenav.cserver.poi.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.executor.GenericFeedbackRetrievalResponse;
import com.telenav.cserver.util.TnUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.ws.datatypes.feedback.FeedbackQuestion;
import com.telenav.ws.datatypes.feedback.TextAnswerType;

public class GenericFeedbackRetrievalResponseFormatter extends BrowserProtocolResponseFormatter{

	private static Logger logger = Logger.getLogger(GenericFeedbackRetrievalResponseFormatter.class);
	
	
	@Override
	public void parseBrowserResponse(HttpServletRequest httpRequest,
			ExecutorResponse executorResponse) throws Exception {
			
			if (executorResponse instanceof GenericFeedbackRetrievalResponse) {
				GenericFeedbackRetrievalResponse response = (GenericFeedbackRetrievalResponse) executorResponse;

	            TxNode node = new TxNode();
	            TxNode indexNode = new TxNode();
	            FeedbackQuestion[] feedbackList = response.getFeedbackList();
	            convertFeedbackListIntoTxNode(node, indexNode, feedbackList, response.getPageNumber(), response.getPageSize());
	            logger.debug("Set feedback response into node");
	            httpRequest.setAttribute("questionNode", node);
	            httpRequest.setAttribute("indexNode", indexNode);
	        }
	}
	
	/**
	 * Wrap feed back question list into TxNode
	 * Find the index of the last question on the current page.
	 * @param feedbackList
	 * @return TxNode
	 */
	private void convertFeedbackListIntoTxNode(TxNode node, TxNode indexNode, FeedbackQuestion[] feedbackList, 
			int pageNumber, int pageSize){
		if(feedbackList != null && feedbackList.length > 0){
			node.addValue(feedbackList.length);
			int i=0, page=1, count=0, numbering=1;
			for (FeedbackQuestion feedback : feedbackList) {
				if(feedback == null) { continue; }
				TxNode questionNode = new TxNode();
				String question = feedback.getQuestion();
				Long questionId = feedback.getId();
				TextAnswerType answerType = feedback.getTextAnswerType();
				questionNode.addMsg(answerType.getValue());
				questionNode.addMsg(question);
				questionNode.addMsg("" +numbering);
				questionNode.addValue(questionId);
				
				if(TextAnswerType._INFO.equals(answerType.getValue())){
					//just show an introduction label on its own the page
					if(count > 0 ){
						if(page == pageNumber){
							indexNode.addValue(i-count); //the index of the first question node for the page
							indexNode.addValue(i-1); //last index for the page
						}
						page++; //increment page number if we encounter a info label.
						count = 0; //reset non-info question counter.
					}
					if(page == pageNumber){ 
						indexNode.addValue(i); //the index of the first question node for the page
						indexNode.addValue(i); //last index for the page
					}
					++page;
				} else {
					//count++;
					numbering++;
					if (++count == pageSize) {
						count = 0;
						if(page == pageNumber){
							indexNode.addValue(i-pageSize+1); //the index of the first question node for the page
							indexNode.addValue(i); //last index for the page
						}
						++page;
					}
				} 
				String[] choiceList = feedback.getChoice();
				
				if (choiceList != null) {
					TxNode choiceNode = new TxNode();
					for (String choice : choiceList) {
						choiceNode.addMsg(TnUtil.getXMLString(choice));
					}
					questionNode.addChild(choiceNode);
				}
				node.addChild(questionNode);
				i++;
			}
			if(page == pageNumber){
				indexNode.addValue(i-count);
				indexNode.addValue(i-1);
			}
			
		}
	}

}
