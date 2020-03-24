$("#categoria").change(function(){
	
	var select = $(this).val();
	
	if(select == "null"){
		$("#nuevaCategoria").removeClass("d-none");
	}else{
		$("#nuevaCategoria").addClass("d-none");
	}
});

$("#puesto").change(function(){
	
	var select = $(this).val();
	
	if(select == "null"){
		$("#nuevoPuesto").removeClass("d-none");
		$("#nuevoSalario").removeClass("d-none");
	}else{
		$("#nuevoPuesto").addClass("d-none");
		$("#nuevoSalario").addClass("d-none");
	}
});