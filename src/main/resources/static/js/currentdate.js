$(document).ready(function(){
	var date = new Date().toLocaleString("es-MX", {timeZone: "America/Mexico_City", dateStyle: "short"});
	$("#fecha").text(date)
});
