<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<tml:script language="fscript" version="1">
	<%@ include file="StopUtil.jsp"%>
	<![CDATA[
	   func PoiUtil_convertToNodeForResentSearch(JSONObject jo)
            TxNode node
            TxNode.addMsg(node,"supportInfo")
            TxNode.addMsg(node,JSONObject.getString(jo,"name"))
            String phoneNumber = JSONObject.getString(jo,"phoneNumber")
            if NULL == phoneNumber
               phoneNumber = ""
            endif
            TxNode.addMsg(node,phoneNumber)
            
            String distanceStr = JSONObject.get(jo,"distance") + ""
            TxNode.addMsg(node,distanceStr)
            TxNode.addMsg(node,JSONObject.getString(jo,"category"))
            TxNode.addMsg(node,"price")
            TxNode.addMsg(node,"vendorCode")

            TxNode.addValue(node,196)
            TxNode.addValue(node,0)
            JSONObject stop =  JSONObject.get(jo,"stop")
            TxNode stopNode = convertToStop(stop)
            TxNode.addChild(node,stopNode)
             
            TxNode newPlaceNode
            TxNode.addValue(newPlaceNode,196)
            TxNode.addValue(newPlaceNode,0)
            TxNode.addValue(newPlaceNode,1)
            TxNode.addValue(newPlaceNode,0)
     
            int poiId = JSONObject.get(jo,"poiId")
            TxNode.addValue(newPlaceNode,poiId)
            TxNode.addValue(newPlaceNode,0)
            TxNode.addMsg(newPlaceNode,"poi")
            TxNode.addChild(newPlaceNode,node)
            
            #Add an extend node to give client more info. Add by ChengBiao
            #Fix bug:76515 SN3.0
            if JSONObject.has(jo,"userPreviousRating")
               TxNode extendNode
               int lastRateIndex = JSONObject.get(jo,"userPreviousRating")
               TxNode.addValue(extendNode,252)
               
               TxNode extendChildNode
               TxNode.addMsg(extendChildNode, "userPreviousRating")
               TxNode.addValue(extendChildNode,1)
               TxNode.addValue(extendChildNode, lastRateIndex)
               TxNode.addChild(extendNode,extendChildNode)
               
               TxNode.addChild(newPlaceNode,extendNode)
            endif
            
            return newPlaceNode
        endfunc
	]]>
</tml:script>