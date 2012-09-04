package com.telenav.cserver.poi.protocol;

import java.util.Vector;

import net.jarlehansen.protobuf.javame.ByteString;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.poi.datatypes.POI;
import com.telenav.cserver.poi.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.j2me.framework.protocol.ProtoAudioElement;
import com.telenav.j2me.framework.protocol.ProtoAudioMessage;
import com.telenav.j2me.framework.protocol.ProtoAudioRule;
import com.telenav.j2me.framework.protocol.ProtoPromptItem;
import com.telenav.j2me.framework.protocol.ProtoResourceInfo;
import com.telenav.resource.ResourceConstants;
import com.telenav.resource.data.AudioElement;
import com.telenav.resource.data.AudioMessage;
import com.telenav.resource.data.AudioRule;
import com.telenav.resource.data.PromptItem;
import com.telenav.resource.data.ResourceInfo;

public class POIResponseUtil {

    public static JSONObject convertPOIToJSONObject(POI bp)
            throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("distance", bp.bizPoi.supplementalInfo);
        jo.put("rating", bp.avgRating);
        jo.put("phoneNumber", bp.bizPoi.phoneNumber);
        jo.put("name", bp.bizPoi.brand);
        jo.put("poiId", bp.poiId);
        jo.put("category", bp.bizPoi.parentCatName);

        Stop stop = bp.bizPoi.address;
        JSONObject stopJo = new JSONObject();
        stopJo.put("firstLine", stop.firstLine);
        stopJo.put("city", stop.city);
        stopJo.put("state", stop.state);
        stopJo.put("country", stop.country);
        stopJo.put("lon", stop.lon);
        stopJo.put("zip", stop.zip);
        stopJo.put("lat", stop.lat);
        stopJo.put("lon", stop.lon);
        stopJo.put("label", stop.label);
        jo.put("stop", stopJo);
        return jo;
    }
    
    public static TxNode getAudioTxNode(PromptItem[] items)
    {
        // TxNode node = TxNode.fromByteArray(resp.getData(), 0);
        TxNode poiAudioNode = new TxNode();
        poiAudioNode.addValue(ResourceConstants.TYPE_AUDIO_SEQUENCE);
        if (items != null) {
            for (PromptItem item : items) {
                if (item == null) {
                    continue;
                }
                TxNode poiItemNode = new TxNode();
                poiItemNode.addValue(ResourceConstants.TYPE_AUDIO_ITEM);
                AudioElement[] elements = item.getAudioElements();
                getPoiAudioTxNode(elements, poiItemNode);
                // add to the audio sequence node
                poiAudioNode.addChild(poiItemNode);
            }
        }
        
        return poiAudioNode;
    }
    
    public static ProtoPromptItem getPromptItem(PromptItem[] items)
    {
    	ProtoPromptItem.Builder promptBuilder = ProtoPromptItem.newBuilder();
    	if(items != null)
    	{
    		for (PromptItem item : items)
    		{
    			if(item == null)
    			{
    				continue;
    			}
    			AudioElement[] elements = item.getAudioElements();
    			Vector vc = getPoiAudioElement(elements);
    			promptBuilder.setElements(vc);
    		}
    	}
    	return promptBuilder.build();
    }
    
    private static Vector getPoiAudioElement(AudioElement[] elements)
    {
    	Vector vElement = new Vector();
        if(elements == null)
        	return null;
        for(AudioElement element : elements)
        {
        	if(element == null)
        		continue;
        	ProtoAudioElement.Builder elementBuilder = ProtoAudioElement.newBuilder();
        	if(element.getType() == ResourceConstants.TYPE_MSG_AUDIO)
        	{
        		AudioMessage audio = (AudioMessage) element;
        		ProtoAudioMessage.Builder messageBuilder = ProtoAudioMessage.newBuilder();
        		messageBuilder.setInfo(convertResourceInfotoProto(audio.getResourceInfo()));
        		Vector subElements = getPoiAudioElement(audio.getChildren());
        		messageBuilder.setElements(subElements);
        		elementBuilder.setMessage(messageBuilder.build());
        	}
        	else if(element.getType() == ResourceConstants.TYPE_AUDIO_PROMPT)
        	{
        		AudioRule rule = (AudioRule) element;
        		ProtoAudioRule.Builder ruleBuilder = ProtoAudioRule.newBuilder();
        		ruleBuilder.setId(rule.getRuleId());
        		int[] intArgs = rule.getIntArgs();
        		if (intArgs != null) 
        		{
                    for (int arg : intArgs)
                    {
                    	ruleBuilder.addElementIntArgs(arg);
                    }
        		}
        		Vector subElements = getPoiAudioElement(rule.getNodeArgs());
        		ruleBuilder.setElements(subElements);
        		elementBuilder.setRule(ruleBuilder.build());
        	}
        	vElement.add(elementBuilder.build());
        }
        return vElement;
    }
    
    /**
     * 
     * @param elements
     * @param root
     */
    private static void getPoiAudioTxNode(AudioElement[] elements, TxNode root) {
        if (elements == null) {
            return;
        }
        for (AudioElement element : elements) {
            if (element == null) {
                continue;
            }
            TxNode elementNode = null;
            if (element.getType() == ResourceConstants.TYPE_MSG_AUDIO) {
                AudioMessage audio = (AudioMessage) element;
                ResourceInfo info = audio.getResourceInfo();

                elementNode = new TxNode();
                elementNode.addValue(ResourceConstants.TYPE_MSG_AUDIO);
                elementNode.addValue(info.getId());
                if (info.getId() == ResourceConstants.NO_INDEX) {
                    // do nothing
                } else {
                    elementNode.addValue(info.getVersion());
                    if (info.getData() != null) {
                        elementNode.setBinData(info.getData());
                    }
                }
                AudioElement[] children = audio.getChildren();
                getPoiAudioTxNode(children, elementNode);

            } else if (element.getType() == ResourceConstants.TYPE_AUDIO_PROMPT) {
                AudioRule rule = (AudioRule) element;
                // create the prompt node for this POI result
                elementNode = new TxNode();
                elementNode.addValue(ResourceConstants.TYPE_AUDIO_PROMPT);
                elementNode.addValue(rule.getRuleId()); // rule id

                // add int arguments node
                // int[0] = result position in the list
                TxNode intArgsNode = new TxNode();
                intArgsNode.addValue(ResourceConstants.TYPE_INT_ARGUMENTS);
                int[] intArgs = rule.getIntArgs();
                if (intArgs != null) {
                    for (int arg : intArgs) {
                        intArgsNode.addValue(arg);
                    }
                }
                elementNode.addChild(intArgsNode);

                TxNode nodeArgsNode = new TxNode();
                nodeArgsNode.addValue(ResourceConstants.TYPE_NODE_ARGUMENTS);
                AudioElement[] nodeArgs = rule.getNodeArgs();
                getPoiAudioTxNode(nodeArgs, nodeArgsNode);
                elementNode.addChild(nodeArgsNode);
            }
            root.addChild(elementNode);
        }
    }
    
    private static ProtoResourceInfo convertResourceInfotoProto(ResourceInfo info)
    {
    	ProtoResourceInfo.Builder builder = ProtoResourceInfo.newBuilder();
    	builder.setId(info.getId());
    	if (info.getId() == ResourceConstants.NO_INDEX) 
    	{
            // do nothing
        }
    	else
    	{
    		builder.setVersion(info.getVersion());
    		if(info.getData() != null && info.getData().length > 0)
        	{
        		builder.setData(ByteString.copyFrom(info.getData()));
        	}
    	}
    	return builder.build();
    }
    
}
