<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%
	String QUESTION_LIST_NODE = "QUESTION_LIST_NODE";
	String ANSWER_NODE = "ANSWER_NODE";
	String QUESTION_INDEX = "QUESTION_INDEX";
	String ERROR_MSG_NODE = "ERROR_MSG_NODE";
%>
<tml:script language="fscript" version="1">
	<![CDATA[
	
	   func Survey_M_setSurveyDone(String surveyName)
	        TxNode valueNode
	        TxNode.addValue(valueNode, 1)
	        Cache.saveCookie(surveyName,valueNode)
	   endfunc
	    
	   func Survey_M_isSurveyDoneBefore(String surveyName)
	        int value = 0
	        TxNode valueNode = Cache.getCookie(surveyName)
	        if NULL != valueNode
	           value = TxNode.valueAt(valueNode, 0)
	        endif
	        
	        return value
	   endfunc
	    
	  func Survey_M_saveQuestionListNode(TxNode questionListNode)
		   String saveKey = "<%=QUESTION_LIST_NODE%>"
		   Cache.saveToTempCache(saveKey,questionListNode)
	  endfunc
	  
	  func Survey_M_getQuestionListNode()
		   TxNode questionListNode
		   String saveKey = "<%=QUESTION_LIST_NODE%>"
		   questionListNode = Cache.getFromTempCache(saveKey)
		   return questionListNode
	  endfunc
	  
	  func Survey_M_saveAnswerNode(TxNode answerNode)
		   String saveKey = "<%=ANSWER_NODE%>"
		   Cache.saveToTempCache(saveKey,answerNode)
	  endfunc
	  
	  func Survey_M_getAnswerNode()
		   TxNode answerNode
		   String saveKey = "<%=ANSWER_NODE%>"
		   answerNode = Cache.getFromTempCache(saveKey)
		   return answerNode
	  endfunc
	  
	  func Survey_M_saveQuestionIndex(int index)
	       TxNode indexNode
	       TxNode.addValue(indexNode,index)
	       String saveKey = "<%=QUESTION_INDEX%>"
		   Cache.saveToTempCache(saveKey,indexNode)
	  endfunc
	  
	  func Survey_M_getQuestionIndex()
	       int index = 0
	       String saveKey = "<%=QUESTION_INDEX%>"
		   TxNode indexNode = Cache.getFromTempCache(saveKey)
		   if NULL != indexNode
		      index = TxNode.valueAt(indexNode,0)
		   endif
		   return index
	  endfunc
	  
	  func Survey_M_saveErrorMsgNode(TxNode errorMsgNode)
	       String saveKey = "<%=ERROR_MSG_NODE%>"
		   Cache.saveToTempCache(saveKey,errorMsgNode)
	  endfunc
	  
	  func Survey_M_getErrorMsgNode()
		   String saveKey = "<%=ERROR_MSG_NODE%>"
		   TxNode errorMsgNode = Cache.getFromTempCache(saveKey)
		   return errorMsgNode
	  endfunc
	]]>
</tml:script>

