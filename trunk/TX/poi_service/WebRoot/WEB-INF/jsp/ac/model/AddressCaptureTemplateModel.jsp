<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<tml:script language="fscript" version="1">
	<![CDATA[
		    # Template
		    func AddressCaptureTemplate_M_getTemplates()
		        String saveKey = "<%=Constant.StorageKey.ADDRESS_CAPTURE_TEMPLATES%>"
		        return Cache.getJSONObjectFromCookie(saveKey) 
		    endfunc
		    
		     func AddressCaptureTemplate_M_getTemplate(String country)
		     	JSONObject templates = AddressCaptureTemplate_M_getTemplates()
		     	JSONObject template = JSONObject.get(templates,country)
		     	return template
		    endfunc
		    
		    # Label Name
		    func AddressCaptureTemplate_M_getCityLabelName(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"cityLabelName")
				return s
		    endfunc
		    
		    func AddressCaptureTemplate_M_getStateLabelName(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"stateLabelName")
				return s
		    endfunc
		    
		    func AddressCaptureTemplate_M_saveTemplates(JSONObject jo)
		        String saveKey = "<%=Constant.StorageKey.ADDRESS_CAPTURE_TEMPLATES%>"
		        return Cache.saveCookie(saveKey,jo) 
		    endfunc
		    
		    func AddressCaptureTemplate_M_getZipLabelName(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"zipLabelName")
				return s
		    endfunc
		   
		    ########################no use function chbzhang#################### 
		    # Prompt
		    func AddressCaptureTemplate_M_getCityPrompt(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"cityPrompt")
				return s
		    endfunc
		    
		    func AddressCaptureTemplate_M_getStatePrompt(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"statePrompt")
				return s
		    endfunc
		    
		    func AddressCaptureTemplate_M_getZipPrompt(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"zipPrompt")
				return s
		    endfunc
		    
		    func AddressCaptureTemplate_M_getStreetPrompt(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"streetPrompt")
				return s
		    endfunc
		    
		    func AddressCaptureTemplate_M_getStreet1Prompt(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"street1Prompt")
				return s
		    endfunc
		    
		    func AddressCaptureTemplate_M_getStreet2Prompt(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"street2Prompt")
				return s
		    endfunc
		    
		    func AddressCaptureTemplate_M_getStreetLabelName(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"streetLabelName")
				return s
		    endfunc
		    
		    func AddressCaptureTemplate_M_getStreet1LabelName(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"street1LabelName")
				return s
		    endfunc
		    
		    func AddressCaptureTemplate_M_getStreet2LabelName(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"street2LabelName")
				return s
		    endfunc
		    
		     # Length
		    func AddressCaptureTemplate_M_getZipMinLength(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        int n = JSONObject.get(template,"zipMinLength")
				return n
		    endfunc
		    
		    func AddressCaptureTemplate_M_getZipMaxLength(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        int n = JSONObject.get(template,"zipMaxLength")
				return n
		    endfunc
		    
		    func AddressCaptureTemplate_M_getStateMinLength(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        int n = JSONObject.get(template,"stateMinLength")
				return n
		    endfunc
		    
		    func AddressCaptureTemplate_M_getStateMaxLength(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        int n = JSONObject.get(template,"stateMaxLength")
				return n
		    endfunc
		    
		    # zip is all digit
		    func AddressCaptureTemplate_M_getZipIsAllDigit(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String n = JSONObject.get(template,"zipIsAllDigit")
				return n
		    endfunc
		    
		    func AddressCaptureTemplate_M_getCountryAlias(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"countryAlias")
				return s
		    endfunc
		    
		    func AddressCaptureTemplate_M_getStateCandidate(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"stateCandidate")
				return s
		    endfunc
		    
		    func AddressCaptureTemplate_M_getMutexes(String country)
		        JSONObject template = AddressCaptureTemplate_M_getTemplate(country)
		        String s = JSONObject.get(template,"mutexes")
				return s
		    endfunc
		    ########################no use function chbzhang#################### 
		]]>
</tml:script>