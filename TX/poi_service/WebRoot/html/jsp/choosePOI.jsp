<html >
<title>choose Poi</title>
<head>
<%@ include file="/html/jsp/Header.jsp"%>
<%
	String width = request.getParameter("width");
	String height = request.getParameter("height");
	String ci = request.getParameter("clientInfo");
%>
</head>
<body>
	<form>
		choose POI:
		<select id="poi" onchange="changeData(this.value)">
			<option value="default" selected="selected">default data</option>
			<option value="gas">Gas By Price</option>
			<option value="menuExtra">With Menu/Extra</option>
			<option value="sponsor">Sponsor POI</option>
			<option value="showtime">showtime</option>
			<option value="ads">Ads Poi</option>
			<option value="sponsorWithAD">Sponsor with Ad Source</option>
			<option value="new4IPhone">New interface with iPhone(Poi+Ads)</option>
			<option value="hotel">hotel</option>
			<option value="openTable">openTable</option>
			<option value="postOffice">post office(business hour)</option>
			<option value="ATM">ATM(business hour)</option>
			<option value="poiDesc">with poi Description</option>
			<option value="custom">Custom</option>
		</select>
		<input type="button" value="Go" onclick="go(this.form)">
		<br>
		<textarea style="width:100%;height:50%;display:none;" id="json"></textarea>
	</form>
<%@ include file="/html/jsp/Footer.jsp"%>
<script type="text/javascript">
	function changeData(data){
		if(data == "custom"){
			document.getElementById("json").style.display='';
		}else{
			document.getElementById("json").style.display='none';
		}
	}
	function go(obj){
		var selection = document.getElementById("poi").value;
		var json = encodeURIComponent(document.getElementById("json").value);
		if (selection == "custom" && !json){
			alert("plese input your own data!");
			document.getElementById("json").focus();
			return false;
		}
		location.href=GLOBAL_hostUrl+"choosePOIAction.do?poi="+selection+"&json="+json+"&width="+<%=width%>+"&height="+<%=height%>+"&clientInfo="+'<%=ci%>';
	}
</script>	
</body>
</html>
