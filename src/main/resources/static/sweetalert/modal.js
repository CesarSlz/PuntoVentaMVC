$(document).ready(function(){
	var swInfo = $("#swInfo");
	var title = swInfo.attr("data2");
	var text = swInfo.attr("data3");
	var icon = swInfo.attr("data4");
	if(title != null && text != null && icon != null){
		Swal.fire({
			title: title,
			text: text.replace(/\"/g, ""),
			icon: icon,
			confirmButtonColor: '#9dc15b'
		})
	}
});