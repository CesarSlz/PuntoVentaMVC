$(document).ready(function(){
	$("#filtro").on("keyup", function() {
		var value = $(this).val().toLowerCase();
		$("#tabla tr").filter(function() {
			$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
		});
	});
});