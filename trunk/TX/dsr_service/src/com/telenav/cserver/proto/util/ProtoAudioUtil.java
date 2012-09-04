package com.telenav.cserver.proto.util;

import java.util.Vector;

import net.jarlehansen.protobuf.javame.ByteString;

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

public class ProtoAudioUtil
{
	public static ProtoPromptItem getPromptItem(PromptItem[] items)
	{
		ProtoPromptItem.Builder promptBuilder = ProtoPromptItem.newBuilder();
		if (items != null)
		{
			for (PromptItem item : items)
			{
				if (item == null)
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
		if (elements == null)
			return null;
		for (AudioElement element : elements)
		{
			if (element == null)
				continue;
			ProtoAudioElement.Builder elementBuilder = ProtoAudioElement
					.newBuilder();
			if (element.getType() == ResourceConstants.TYPE_MSG_AUDIO)
			{
				AudioMessage audio = (AudioMessage) element;
				ProtoAudioMessage.Builder messageBuilder = ProtoAudioMessage
						.newBuilder();
				messageBuilder.setInfo(convertResourceInfotoProto(audio
						.getResourceInfo()));
				Vector subElements = getPoiAudioElement(audio.getChildren());
				messageBuilder.setElements(subElements);
				elementBuilder.setMessage(messageBuilder.build());
			}
			else if (element.getType() == ResourceConstants.TYPE_AUDIO_PROMPT)
			{
				AudioRule rule = (AudioRule) element;
				ProtoAudioRule.Builder ruleBuilder = ProtoAudioRule
						.newBuilder();
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
	
	private static ProtoResourceInfo convertResourceInfotoProto(
			ResourceInfo info)
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
			if (info.getData() != null && info.getData().length > 0)
			{
				builder.setData(ByteString.copyFrom(info.getData()));
			}
		}
		return builder.build();
	}
}
