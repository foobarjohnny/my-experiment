<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@ page contentType="text/cache-manifest; charset=UTF-8"%><%@ page pageEncoding="UTF-8" %>CACHE MANIFEST
# version 20121010_01
NETWORK:
*
CACHE:
<%-- ${cssPath}style.css --%>
<%-- ${cssProgPath}prog.css --%>
${cssProgPath}common_compressed.css
<%-- ${cssPath}actemplate.css --%>
${cssProgPath}actemplate_compressed.css
${cssDeviceCommonPath}actemplatebasic.css
<%-- ${cssProgPath}actemplatebasic_compressed.css --%>
/poi_service/html/js/common_compressed.js
/poi_service/html/js/${platform}/sdk.js
/poi_service/html/js/${platform}/tnsdk.js
/poi_service/html/js/acTemplate_Compressed.js
<c:choose>
<c:when test="${needBackButton}">
${imageKey}loading_icon_medium_inside_unfocused.png
</c:when>
</c:choose>
