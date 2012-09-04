package com.telenav.cserver.html.util;

public class HtmlAcTemplateField
{

	@Override
	public String toString()
	{
		return "HtmlAcTemplateField [style=" + style + ", exist=" + exist + ", maxLen=" + maxLen + ", minLen=" + minLen + ", name=" + name + ", autoSuggest=" + autoSuggest + ", label=" + label
				+ ", css=" + css + ", optionsSize=" + optionsSize + ", triggerChar=" + triggerChar + ", autoSuggestFormat=" + autoSuggestFormat + ", stopField=" + stopField + "]";
	}

	private String	style;
	private String	exist	= "true";
	private String	maxLen;
	private String	minLen;
	private String	name;
	private String	autoSuggest;
	private String	label;
	private String	css;
	private String	optionsSize;

	private String	triggerChar;
	private String	autoSuggestFormat;

	private String	stopField;

	public String getStopField()
	{
		return stopField;
	}

	public void setStopField(String stopField)
	{
		this.stopField = stopField;
	}

	public String getAutoSuggestFormat()
	{
		return autoSuggestFormat;
	}

	public void setAutoSuggestFormat(String autoSuggestFormat)
	{
		this.autoSuggestFormat = autoSuggestFormat;
	}

	public String getOptionsSize()
	{
		return optionsSize;
	}

	public void setOptionsSize(String optionsSize)
	{
		this.optionsSize = optionsSize;
	}

	public String getTriggerChar()
	{
		return triggerChar;
	}

	public void setTriggerChar(String triggerChar)
	{
		this.triggerChar = triggerChar;
	}

	public String getAutoSuggest()
	{
		return autoSuggest;
	}

	public void setAutoSuggest(String autoSuggest)
	{
		this.autoSuggest = autoSuggest;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public String getMaxLen()
	{
		return maxLen;
	}

	public void setMaxLen(String maxLen)
	{
		this.maxLen = maxLen;
	}

	public String getMinLen()
	{
		return minLen;
	}

	public void setMinLen(String minLen)
	{
		this.minLen = minLen;
	}

	public String getExist()
	{
		return exist;
	}

	public void setExist(String exist)
	{
		this.exist = exist;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getCss()
	{
		return css;
	}

	public void setCss(String css)
	{
		this.css = css;
	}

	public String getType()
	{
		if ("alphanumeric".equalsIgnoreCase(getStyle())) {
			return "text";
		}
		else {
			return getStyle();
		}
	}

}
