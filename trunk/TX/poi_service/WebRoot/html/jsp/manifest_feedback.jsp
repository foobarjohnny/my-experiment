<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@ page contentType="text/cache-manifest; charset=UTF-8"%><%@ page pageEncoding="UTF-8" %>CACHE MANIFEST
# version 20121010_01
NETWORK:
*
CACHE:
${cssProgPath}poifeedback_compressed.css
${cssProgPath}common_compressed.css
/poi_service/html/js/${platform}/sdk.js
/poi_service/html/js/${platform}/tnsdk.js
/poi_service/html/js/common_compressed.js
/poi_service/html/js/feedback.js
${imageKey}checkbox.png
${imageKey}loading_icon_medium_unfocused.png
<c:choose>
<c:when test="${needBackButton}">
${imageKey}backbutton.png
${imageKey}loading_icon_medium_inside_unfocused.png
${imageKey}toggler.png
</c:when>
</c:choose>
