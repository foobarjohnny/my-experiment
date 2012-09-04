package com.telenav.cserver.webapp.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.html.util.XMLUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.ws.datatypes.feedback.TextAnswerType;

public class TnSurveyTag extends TagSupport{

	private static Logger logger=Logger.getLogger(TnSurveyTag.class);
	
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	private StringBuilder outputText = new StringBuilder();
	
	// how many survey questions to show in a page.
	private int pageSize;
	// current page number of the survey.
	private int pageNumber;
	// name of the txNode that stores the question list
	private String questionsTxNodeName;
	// id of the question label or introduction
	private String questionLabel;
	// id of the choices label.
	private String choiceLabel;
	// Substitute key and value for question label
	private String subKey;
	private String subValue;
	private String comments;
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSubKey() {
		return subKey;
	}

	public void setSubKey(String subKey) {
		this.subKey = subKey;
	}

	public String getSubValue() {
		return subValue;
	}

	public void setSubValue(String subValue) {
		this.subValue = subValue;
	}

	public String getChoiceLabel() {
		return choiceLabel;
	}

	public void setChoiceLabel(String choiceLabel) {
		this.choiceLabel = choiceLabel;
	}

	public String getQuestionLabel() {
		return questionLabel;
	}

	public void setQuestionLabel(String questionLabel) {
		this.questionLabel = questionLabel;
	}

	public String getQuestionsTxNodeName() {
		return questionsTxNodeName;
	}

	public void setQuestionsTxNodeName(String questionsTxNodeName) {
		this.questionsTxNodeName = questionsTxNodeName;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int doStartTag() throws JspException{
		return SKIP_BODY;
	}
	
	public int doEndTag() throws JspException{
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		TxNode questionsNode = (TxNode) request.getAttribute(questionsTxNodeName);
		if(questionsNode == null){
			throw new JspException("Empty survey question txNode!! ");
		}
		long questionsLength = questionsNode.valueAt(0);
		int childSize = questionsNode.childrenSize();
		if(questionsLength != childSize){
			throw new JspException("Survey question TxNode's question records are not consistent!!!");
		}
	    
	    TxNode qNode;
	    String qType;
	    String qMsg;
	    
	    int i = 0, page =1, count=0, numbering=1;
	    if(questionsLength == 1){
	    	numbering = -1; // don't show numbering for question label if there is only one question.
	    }
		while(i < questionsLength){
			
			qNode = questionsNode.childAt(i);
			qType = qNode.msgAt(0);
			qMsg = qNode.msgAt(1);
			
			if(TextAnswerType._INFO.equals(qType)){
				//just show an introduction label on its own the page
				if(count > 0 ){ 
					page++; //increment page number if we encounter a info label.
					count = 0; //reset counter of non info questions.
				}
				
				if(page == pageNumber){//
					addQuestionLabel(qMsg, i, -1);
					//addNextButton();
					break;
				}
				if(++page > pageNumber)break;
			} else {
				if (page == pageNumber) {
					if (TextAnswerType._MULTIPLE.equals(qType)) {
						// question text with multiple choices
						addQuestionLabel(qMsg, i, numbering);
						addMultipleChoice(qNode, i);
					} else if (TextAnswerType._MULTIPLE_WITH_TEXT.equals(qType)) {
						// question text with multiple choices and a free form
						// text
						// input field
						if ("" !=comments || null !=comments){
							addQuestionLabelForFeedback(qMsg, i, numbering);
							addTextInputForFeedback(i, comments);
							addMultipleChoice(qNode, i);
						}else{
							addQuestionLabel(qMsg, i, numbering);
							addMultipleChoice(qNode, i);
							addTextInput(i);
						}
					} else if (TextAnswerType._SINGLE.equals(qType)) {
						// question text with single choice
						addQuestionLabel(qMsg, i, numbering);
						addSingleChoice(qNode, i);
					} else if (TextAnswerType._SINGLE_LINE_TEXT.equals(qType)) {
						// question text with single choice and a free form text
						// input field
						addQuestionLabel(qMsg, i, numbering);
						addSingleChoice(qNode, i);
						addTextInput(i);
					} else if (TextAnswerType._SINGLE_WITH_TEXT.equals(qType)) {
						// question text with single choice and a free form text
						// input field.
						addQuestionLabel(qMsg, i, numbering);
						addSingleChoice(qNode, i);
						addTextInput(i);
					} else if (TextAnswerType._TEXT.equals(qType)) {
						// free form text input field
						addQuestionLabel(qMsg, i, numbering);
						addTextInput(i);
					}
				}
				count++;
				numbering ++; //keep track of numbering for non-info question.
				if (count == pageSize) {
					count = 0;
					//if(page == pageNumber) addNextButton();
					if(++page > pageNumber)break;
				}
			} 
		    i++;
			
			
		}
		
//		if(i >= questionsLength){
//			addSaveButton();
//		}
		try {
			this.pageContext.getOut().println(this.outputText.toString());
		} catch (IOException e) {
			if (logger.isDebugEnabled()) {
				logger.error("TmlSurveyTag.doEndTag", e);
			}
		}
		this.clean(); //clear the output text buffer.
		return EVAL_PAGE;
	}
	
//	private void addSaveButton(){
//		outputText.append("<tml:button id=\"saveButton\" fontWeight=\"System_large\" text=\"")
//		.append(saveButtonText)
//		.append("\"> ")
//		.append("<tml:menuRef name=\"")
//		.append(saveButtonMenuRef)
//		.append("\" /> ")
//		.append("</tml:button>");
//	}
//	
//	private void addNextButton(){
//		outputText.append("<tml:button id=\"nextButton\" fontWeight=\"System_large\" text=\"")
//		.append(nextButtonText)
//		.append("\"> ")
//		.append("<tml:menuRef name=\"")
//		.append(nextButtonMenuRef)
//		.append("\" /> ")
//		.append("</tml:button>");
//	}
	
	private void addQuestionLabel(String qMsg, int index, int numbering){
		outputText.append("<nullField id=\"nullField1\"/>");
		if(numbering > 0){
			outputText.append("<multiline id=\"" + questionLabel + index + "\" fontWeight=\"bold|system_large\" align=\"left|middle\">");
			outputText.append(numbering + ". ");
		}else{
			outputText.append("<multiline id=\"" + questionLabel + index + "\" fontWeight=\"system_large\" align=\"left|middle\">");
		}
		
		if(subKey != null && subKey != "" && subValue != null){
			qMsg = qMsg.replaceAll(subKey, XMLUtil.getXMLString("<bold>" + subValue + "</bold>"));	
		} 
		outputText.append(qMsg)
		.append("</multiline>")
		.append("<nullField id=\"nullField2\"/>");
	}
	
	private void addQuestionLabelForFeedback(String qMsg, int index, int numbering){
		outputText.append("<nullField id=\"nullField1\"/>");
		if(numbering > 0){
			outputText.append("<multiline id=\"" + questionLabel + index + "\" fontWeight=\"bold|system_large\" align=\"center|middle\">");
			outputText.append(numbering + ". ");
		}else{
			outputText.append("<multiline id=\"" + questionLabel + index + "\" fontWeight=\"bold|system_large\" align=\"center|middle\">");
		}
		
		if(subKey != null && subKey != "" && subValue != null){
			qMsg = qMsg.replaceAll(subKey, XMLUtil.getXMLString(subValue));	
		} 
		outputText.append(qMsg)
		.append("</multiline>")
		.append("<nullField id=\"nullField2\"/>");
	}
	
	private void addSingleChoice(TxNode qNode, int index){
		outputText.append("<checkBox id=\"" + choiceLabel + index + "\" isFocusable=\"true\" fontWeight=\"system_large\">");
		TxNode cNode = qNode.childAt(0);
		int choiceSize = cNode.msgsSize();
		for(int j=0; j< choiceSize; j++ ){
			outputText.append("<checkBoxItem text=\"" + cNode.msgAt(j) + "\" value=\"" + j + "\"/>");
		}
		outputText.append("</checkBox>")
		.append("<nullField id=\"nullField3\"/>");
	}
	
	private void addTextInput(int index){
		outputText.append("<inputBox id=\"textChoice" + index + "\" fontWeight=\"system_large\" titleFontWeight=\"system_large|bold\" isAlwaysShowPrompt=\"true\"")
		.append(" title=\"" + "" + "\">")
		.append("</inputBox>");
	}
	
	private void addTextInputForFeedback(int index,String comments){
		outputText.append("<inputBox id=\"textChoice" + index + "\" fontWeight=\"system_large\" titleFontWeight=\"system_large|bold\" isAlwaysShowPrompt=\"true\"")
		.append(" title=\"" + comments + "\">")
		.append("</inputBox>");
	}
	
	private void addMultipleChoice(TxNode qNode, int index){
		outputText.append("<checkBox id=\"" + choiceLabel + index +  "\" isFocusable=\"true\" fontWeight=\"system_large\">");
		TxNode cNode = qNode.childAt(0);
		int choiceSize = cNode.msgsSize();
		for(int j=0; j< choiceSize; j++ ){
			outputText.append("<checkBoxItem text=\"" + cNode.msgAt(j) + "\" value=\"" + j + "\"/>");
		}
		outputText.append("</checkBox>").append("<nullField id=\"nullField3\"/>");
	}
	
	
	public void clean()
    {
        outputText.setLength(0);
        setPageSize(1);
        setPageNumber(1);
        setQuestionsTxNodeName("");
        setQuestionLabel("");
        setChoiceLabel("");
    }

}
