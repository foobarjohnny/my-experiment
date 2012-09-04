<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@ page contentType="text/cache-manifest; charset=UTF-8"%><%@ page pageEncoding="UTF-8" %>CACHE MANIFEST
# version 20121010_01
NETWORK:
*
CACHE:
${cssProgPath}weather_compressed.css
${cssProgPath}common_compressed.css
/poi_service/html/js/${platform}/sdk.js
/poi_service/html/js/${platform}/tnsdk.js
/poi_service/html/js/common_compressed.js
/poi_service/html/js/weather.js
${imageKey}change_location_button_big.png
${imageKey}loading_icon_medium_unfocused.png
${imageKey}weatherBig.png
${imageKey}weatherSmall.png
<c:choose>
<c:when test="${needBackButton}">
${imageKey}backbutton.png
${imageKey}loading_icon_medium_inside_unfocused.png
</c:when>
</c:choose>
