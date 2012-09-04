<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.j2me.datatypes.TxNode"%>
<%
TxNode indexNode1 = (TxNode) request.getAttribute("indexNode");
int firstIndex1 = (int) indexNode1.valueAt(0);
int lastIndex1 = (int) indexNode1.valueAt(1);
String pageName1 = request.getParameter("pageName");
String feedbackTopic1 = request.getParameter("feedBackTopic");
System.out.println("firstIndex " + firstIndex1 + " lastIndex " + lastIndex1);
%>
<tml:script language="fscript" version="1">
		<![CDATA[
		    func checkBoxToString(String checkBoxName, String index)
				TxNode node
				TxNode resultNode
				String checkBoxId = checkBoxName + index
				node=ParameterSet.getParam(checkBoxId)
				if(node!=NULL)
				
					int len =TxNode.getValueSize(node)
					String questionType = questionType(index)
					if len > 1 && ( "SINGLE" == questionType || "SINGLE_WITH_TEXT" == questionType || "SINGLE_LINE_TEXT" == questionType)
						String numbering = questionNumbering(index)
						System.showErrorMsg("Question " + numbering + " allows single choice only!")
						return FAIL
					endif 
					TxNode.addValue(resultNode, len)
					
					int i =0
					while(i<len)
						TxNode.addMsg(resultNode, TxNode.msgAt(node, i))
						i=i+1
					endwhile
				else
					#System.showErrorMsg("node null")
				endif

				return resultNode
			endfunc

            func commentToString(String commentName, String index)
            	TxNode node
            	String commentId = commentName + index
            	node = ParameterSet.getParam(commentId)
            	return node
            endfunc
            
            func questionLabelString(int index)
                return questionAttr(index, 1, 1)
            endfunc
            
            func questionType(int index)
                return questionAttr(index, 0, 1)
            endfunc
            
            func questionNumbering(int index)
                return questionAttr(index, 2, 1)
            endfunc
            
            func questionID(int index)
            	return questionAttr(index, 0, 0)
            endfunc
            
            func questionAttr(int index, int attrIndex, int type)
                TxNode questionListNode = Survey_M_getQuestionListNode()
		        if NULL == questionListNode
		          return FAIL
		        endif
		        int size = TxNode.getChildSize(questionListNode)
		        if index >= size
		          return FAIL
		        endif
		        TxNode questionNode = TxNode.childAt(questionListNode,index)
		        if 1 == type
		        	# return string attribute
		        	return TxNode.msgAt(questionNode,attrIndex)
                else
                	# return int attribute
                	return TxNode.valueAt(questionNode, attrIndex)
                endif
            endfunc
            
            #collect answer for a suvery question
			#must be separated out into its own function
			#otherwise, TxNode allocation within while loop doesn't work
			func addQuestionNode(int startIndex)
				TxNode questionNode
				TxNode tempNode
				String tmpstr
				int questionID
					
				#add question string
				tmpstr = questionLabelString(startIndex)
				TxNode.addMsg(questionNode, tmpstr)
				println("add question: " + startIndex + " ." + tmpstr)
				
				#add question ID
				questionID = questionID(startIndex)
				TxNode.addValue(questionNode, questionID)
					
				#add choice response
				tempNode = checkBoxToString("feedbackOptions", startIndex)
					
				if NULL != tempNode
						TxNode.addChild(questionNode, tempNode)
				endif
					
				#add comment input response
				tempNode = commentToString("textChoice", startIndex)
				if NULL != tempNode
						TxNode.addChild(questionNode, tempNode)
						println("question comment: [" + TxNode.msgAt(tempNode, 0) + "]")
				endif
					
				return questionNode
			endfunc
			
				#Check if the particular question's part got answered
			#index -- 0 choice part
			#index -- 1 comment part
			func isQuestionNodeFilled(TxNode qNode, int index)
				TxNode tempNode
				int msgSize
				String msg
				int filled = 0
				tempNode = TxNode.childAt(qNode, index)
           		if NULL != tempNode
	           			msgSize = TxNode.getStringSize(tempNode)
	           			if msgSize > 0
	           				  msg = TxNode.msgAt(tempNode, 0)
	           				  if NULL != msg && "" != msg
	           						filled = 1
	           				  endif
	           			endif
           		endif	
				return filled
			endfunc
			
			#Validate at least one of the questions got answered without judging comments
			func validateAnswersWithoutComments(TxNode answerNode)
				int atLeastOne = 0
				int childSize = TxNode.getChildSize(answerNode)
				int i = 0
				TxNode qNode
				int qNodeSize 
       			while i < childSize
           			qNode = TxNode.childAt(answerNode,i)
           			qNodeSize = TxNode.getChildSize(qNode)
           		   # check single/multiple choice answer
           			if qNodeSize > 0
			        	atLeastOne = atLeastOne + isQuestionNodeFilled(qNode, 0) 			
           			endif
				i = i + 1
       			endwhile
       			return atLeastOne 
			endfunc
			
			#Validate at least one of the questions got answered
			func validateAnswers(TxNode answerNode)
				int atLeastOne = 0
				
				int childSize = TxNode.getChildSize(answerNode)
				int i = 0
				TxNode qNode
				int qNodeSize
				 
       			while i < childSize
           			qNode = TxNode.childAt(answerNode,i)
           			qNodeSize = TxNode.getChildSize(qNode)
           			# check comment answer
           			if qNodeSize > 1
           				atLeastOne = atLeastOne + isQuestionNodeFilled(qNode, 1)
           			endif
           			# check single/multiple choice answer
           			if qNodeSize > 0
						atLeastOne = atLeastOne + isQuestionNodeFilled(qNode, 0) 			
           			endif
           			i = i + 1
       			endwhile
       			return atLeastOne 
			endfunc
			
			func saveSurveyAnswers()
				TxNode answerNode
			    TxNode savedAnswerNode
			    TxNode tempNode
			    TxNode questionNode
			    
			    int startIndex = <%=firstIndex1%>
			    
			    savedAnswerNode = Survey_M_getAnswerNode()
			    
			    #copy the saved answers up to start index for this page.
			    if NULL != savedAnswerNode
			    	int copyIndex = 0
			    	println("copy stored answer node up to index: " + startIndex)
			    	while copyIndex< startIndex
			    		tempNode = TxNode.childAt(savedAnswerNode, copyIndex)
			    		println("copy stored answer node " + copyIndex)
			    		TxNode.addChild(answerNode, tempNode)
			    		copyIndex = copyIndex + 1
					endwhile
				endif
				
				#Feedback page name
				TxNode.addMsg(answerNode, "<%=pageName1%>")
				TxNode.addMsg(answerNode, "<%=feedbackTopic1%>")
				println("add feedbackname to answerNode")
				#Add question answers and question comment
				println("start " + startIndex + " last " + "<%=lastIndex1%>")
				while startIndex<=<%=lastIndex1%>
				    questionNode = addQuestionNode(startIndex)
					TxNode.addChild(answerNode, questionNode)
					println("save questionNode " + startIndex + " to answerNode")
					startIndex = startIndex + 1
				endwhile
				
				println("save answer code to cache")
				#save the answer node to cache
				Survey_M_saveAnswerNode(answerNode)
				return answerNode
			endfunc
		]]>
</tml:script>