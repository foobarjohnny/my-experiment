package com.telenav.cserver.html.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Category;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HtmlAcTemplateConfig
{
	private static String		CONFIG_FILE									= "actemplate.xml";
	private static final String	TAG_REGION									= "region";
	private static final String	TAG_FIELD									= "field";
	private static final String	TAG_FIELDS									= "fields";
	private static final String	TAG_MAPPINGS								= "mappings";
	private static final String	TAG_MAPPING									= "mapping";
	private static final String	TAG_ACTEMPLATES								= "actemplates";
	private static final String	TAG_ACTEMPLATE								= "actemplate";
	private static final String	TAG_ACE_STOP_MAPPINGS						= "ace_stop_mappings";

	private static final String	ATTR_REGION									= "region";
	private static final String	ATTR_ACTEMPLATE								= "act";
	private static final String	ATTR_ACE									= "ace";
	private static final String	ATTR_STOP									= "stop";

	public static final String	FIELD_HOUSENUMBER_STREET					= "housenumber_street";
	public static final String	FIELD_HOUSENUMBER_STREET_SUBLOCALITY		= "housenumber_street_sublocality";
	public static final String	FIELD_LOCALITY								= "locality";
	public static final String	FIELD_CITY									= "city";
	public static final String	FIELD_COUNTY								= "county";
	public static final String	FIELD_STATE									= "state";
	public static final String	FIELD_POSTALCODE							= "postalcode";
	public static final String	FIELD_NEIGHBORHOOD							= "neighborhood";
	public static final String	FIELD_CITY_STATE_POSTALCODE					= "city_state_postalcode";
	public static final String	FIELD_LOCALITY_CITY_COUNTY_STATE_POSTALCODE	= "locality_city_county_state_postalcode";

	public static final String	ATTRIBUTE_EXIST								= "exist";
	public static final String	ATTRIBUTE_MAX								= "maxLen";
	public static final String	ATTRIBUTE_MIN								= "minLen";
	public static final String	ATTRIBUTE_STYLE								= "style";
	public static final String	ATTRIBUTE_NAME								= "name";

	public static final String	ATTR_ID										= "id";

	public static final String	DEFAULT_TRIGGER_CHAR						= "1";
	public static final String	DEFAULT_OPTION_SIZE							= "8";
	public static final String	DEFAULT_NIL_TRIGGER_CHAR					= "-1";
	public static final String	DEFAULT_NIL_OPTION_SIZE						= "-1";
	public static final String	DEFAULT_MAX_LENGTH							= "-1";
	public static final String	DEFAULT_MIN_LENGTH							= "-1";
	public static final String	DEFAULT_CSS									= "actDefault";
	public static final String	DEFAULT_STYLE								= "text";
	public static final String	DEFAULT_AUTO_SUGGEST						= "false";
	public static final String	DEFAULT_AUTO_SUGGEST_FORMAT					= "";

	@SuppressWarnings("deprecation")
	private static Category		logger										= Category.getInstance(HtmlAcTemplateConfig.class);
	private static Map<?, ?>	regionTemplateMap;
	private static Map<?, ?>	acTemplateMap;
	private static Map<?, ?>	aceStopMap;
	private static Map<?, ?>	regionAutoSuggestMap;

	public static List<List<HtmlAcTemplateField>> getAcTemplate(String region)
	{
		Set<?> keySet = regionTemplateMap.keySet();
		for (Iterator<?> it = keySet.iterator(); it.hasNext();) {
			String s = (String) it.next();
			if (s.equalsIgnoreCase(region)) {
				region = s;
			}
		}
		String template = (String) regionTemplateMap.get(region);
		if (null == template || "" == template) {
			template = "US";
		}
		List templateList = (List) acTemplateMap.get(template);
		if (null == templateList || templateList.size() < 1) {
			templateList = (List) acTemplateMap.get("US");
		}
		return templateList;
	}

	public static JSONObject getRegionAutoSuggest(String region)
	{
		Set<?> keySet = regionTemplateMap.keySet();
		for (Iterator<?> it = keySet.iterator(); it.hasNext();) {
			String s = (String) it.next();
			if (s.equalsIgnoreCase(region)) {
				region = s;
			}
		}
		String template = (String) regionTemplateMap.get(region);
		if (null == template || "" == template) {
			template = "US";
		}
		JSONObject json = (JSONObject) regionAutoSuggestMap.get(template);
		if (null == json) {
			json = new JSONObject();
		}
		return json;
	}

	static {
		getAcTemplateConfig();
	}

	private static void getAcTemplateConfig()
	{
		Map<Object, Object> map = new HashMap<Object, Object>();
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream stream = cl.getResourceAsStream(CONFIG_FILE);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);

			// parse config
			Document doc = factory.newDocumentBuilder().parse(stream);
			Element root = doc.getDocumentElement();
			NodeList childNodes = root.getChildNodes();

			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				String nodeName = childNode.getNodeName();
				if (TAG_MAPPINGS.equalsIgnoreCase(nodeName)) {
					regionTemplateMap = parseMapping(childNode);
				}
				else if (TAG_ACE_STOP_MAPPINGS.equals(nodeName)) {
					aceStopMap = parseAceStopMapping(childNode);
				}
				else if (TAG_ACTEMPLATES.equalsIgnoreCase(nodeName)) {
					acTemplateMap = parseAcTemplates(childNode);
				}
			}
			regionAutoSuggestMap = parseRegionAutoSuggest(acTemplateMap);

		}
		catch (Exception e) {
			logger.warn("AcTemplateConfig::parseConfig:" + e.getMessage());
		}
	}

	private static Map<?, ?> parseRegionAutoSuggest(Map<?, ?> acTemplateMap)
	{
		// TODO Auto-generated method stub
		Map map = new HashMap();
		Set<?> regions = acTemplateMap.keySet();
		for (Iterator it = regions.iterator(); it.hasNext();) {
			String region = (String) it.next();
			List fieldsList = (List) acTemplateMap.get(region);
			for (Iterator it2 = fieldsList.iterator(); it2.hasNext();) {
				List fieldList = (List) it2.next();
				for (Iterator it3 = fieldList.iterator(); it3.hasNext();) {
					HtmlAcTemplateField field = (HtmlAcTemplateField) it3.next();
					String autoSuggest = field.getAutoSuggest();
					if (autoSuggest.length() > 0 && !"false".equalsIgnoreCase(autoSuggest)) {
						JSONObject regionAutoSuggest = (JSONObject) map.get(region);
						if (regionAutoSuggest == null) {
							regionAutoSuggest = new JSONObject();
						}
						JSONArray formatJsonArr = regionAutoSuggest.optJSONArray(autoSuggest);
						if (formatJsonArr == null) {
							formatJsonArr = new JSONArray();
						}
						String format = field.getAutoSuggestFormat();
						String[] formatArr = format.split("\\|");
						String temp;
						for (int i = 0; i < formatArr.length; i++) {
							temp = formatArr[i];
							if (formatJsonArr.toString().indexOf(temp) < 0) {
								formatJsonArr.put(temp);
							}
						}
						try {
							regionAutoSuggest.put(autoSuggest, formatJsonArr);
							if (formatJsonArr.length() < 1) {
								regionAutoSuggest.remove(autoSuggest);
							}
						}
						catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						map.put(region, regionAutoSuggest);
					}
				}
			}
		}
		return map;

	}

	private static Map<String, List<List<HtmlAcTemplateField>>> parseAcTemplates(Node childNode)
	{
		// TODO Auto-generated method stub
		Element element = (Element) childNode;
		Map<String, List<List<HtmlAcTemplateField>>> map = new HashMap<String, List<List<HtmlAcTemplateField>>>();
		NodeList nodeList = element.getChildNodes();
		// parse elements
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);

			if (TAG_ACTEMPLATE.equalsIgnoreCase(child.getNodeName())) {
				Element fieldElement = (Element) child;
				String acTemplateName = fieldElement.getAttribute(ATTR_ID);
				List<List<HtmlAcTemplateField>> fieldList = parseFieldsConfig(child);
				map.put(acTemplateName, fieldList);
			}
		}
		return map;
	}

	private static Map<String, String> parseAceStopMapping(Node childNode)
	{
		// TODO Auto-generated method stub
		Element element = (Element) childNode;
		Map<String, String> map = new HashMap<String, String>();
		NodeList nodeList = element.getChildNodes();
		// parse elements
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);

			if (TAG_MAPPING.equalsIgnoreCase(child.getNodeName())) {
				Element fieldElement = (Element) child;
				String ace = fieldElement.getAttribute(ATTR_ACE);
				String stop = fieldElement.getAttribute(ATTR_STOP);
				map.put(ace, stop);
			}
		}
		return map;
	}

	private static Map<String, String> parseMapping(Node childNode)
	{
		// TODO Auto-generated method stub
		Element element = (Element) childNode;
		Map<String, String> map = new HashMap<String, String>();
		NodeList nodeList = element.getChildNodes();
		// parse elements
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);

			if (TAG_MAPPING.equalsIgnoreCase(child.getNodeName())) {
				Element fieldElement = (Element) child;
				String name = fieldElement.getAttribute(ATTR_REGION);
				String acTemplate = fieldElement.getAttribute(ATTR_ACTEMPLATE);
				map.put(name, acTemplate);
			}
		}
		return map;
	}

	private static List<List<HtmlAcTemplateField>> parseFieldsConfig(Node node)
	{
		Element element = (Element) node;
		List<List<HtmlAcTemplateField>> fieldsList = new ArrayList<List<HtmlAcTemplateField>>();
		NodeList nodeList = element.getChildNodes();
		// parse elements
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);

			if (TAG_FIELDS.equalsIgnoreCase(child.getNodeName())) {
				List<HtmlAcTemplateField> fieldList = parseFieldConfig(child);
				fieldsList.add(fieldList);
			}
		}
		return fieldsList;

	}

	private static List<HtmlAcTemplateField> parseFieldConfig(Node node)
	{
		Element element = (Element) node;
		List<HtmlAcTemplateField> fieldList = new ArrayList<HtmlAcTemplateField>();
		NodeList nodeList = element.getChildNodes();
		// parse elements
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);

			if (TAG_FIELD.equalsIgnoreCase(child.getNodeName())) {
				Element fieldElement = (Element) child;
				String name = getString(fieldElement.getAttribute("name"));
				String stopField = getString(fieldElement.getAttribute("stopField"));
				String style = getString(fieldElement.getAttribute("style"));
				if (style.length() < 1) {
					style = DEFAULT_STYLE;
				}
				String maxLen = getString(fieldElement.getAttribute("maxLen"));
				if (maxLen.length() < 1) {
					maxLen = DEFAULT_MAX_LENGTH;
				}
				String minLen = getString(fieldElement.getAttribute("minLen"));
				if (minLen.length() < 1) {
					minLen = DEFAULT_MIN_LENGTH;
				}
				String label = getString(fieldElement.getAttribute("label"));
				String css = getString(fieldElement.getAttribute("css"));
				if (css.length() < 1) {
					css = DEFAULT_CSS;
				}
				String autoSuggest = getString(fieldElement.getAttribute("autoSuggest"));
				String triggerChar = getString(fieldElement.getAttribute("triggerChar"));
				String optionsSize = getString(fieldElement.getAttribute("optionsNumber"));
				String autoSuggestFormat = getString(fieldElement.getAttribute("autoSuggestFormat"));
				if (autoSuggest.length() < 1 || DEFAULT_AUTO_SUGGEST.equalsIgnoreCase(autoSuggest)) {
					autoSuggest = DEFAULT_AUTO_SUGGEST;
					triggerChar = DEFAULT_NIL_TRIGGER_CHAR;
					optionsSize = DEFAULT_NIL_OPTION_SIZE;
					autoSuggestFormat = DEFAULT_AUTO_SUGGEST_FORMAT;
				}
				else {
					if (triggerChar.length() < 1) {
						triggerChar = DEFAULT_TRIGGER_CHAR;
					}
					if (optionsSize.length() < 1) {
						optionsSize = DEFAULT_OPTION_SIZE;
					}
				}

				HtmlAcTemplateField field = new HtmlAcTemplateField();
				field.setName(name);
				field.setStopField(convertAce2Stop(stopField));
				field.setStyle(style);
				field.setMaxLen(maxLen);
				field.setMinLen(minLen);
				field.setLabel(label);
				field.setAutoSuggest(autoSuggest);
				field.setTriggerChar(triggerChar);
				field.setOptionsSize(optionsSize);
				field.setCss(css);
				field.setAutoSuggestFormat(convertAce2Stop(autoSuggestFormat));

				fieldList.add(field);
			}
		}
		return fieldList;

	}

	private static String convertAce2Stop(String s)
	{
		if (s == null || s.length() < 1) {
			return "";
		}
		String[] arr = s.split("\\|");
		String format = "";
		String temp;
		for (int i = 0; i < arr.length; i++) {
			temp = getString((String) aceStopMap.get(arr[i]));
			if (temp.length() > 0) {
				if (format.length() > 0) {
					format += "|";
				}
				format += temp;
			}
		}
		return format;
	}

	private static String getString(String s)
	{
		if (s == null) {
			return "";
		}
		return s;
	}

	public static void main(String[] z)
	{
		getAcTemplateConfig();
		// System.out.println(getAcTemplate("US"));
		System.out.println(regionAutoSuggestMap);
	}

}
