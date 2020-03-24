function existe(id){
	var bandera = false;
	
	// Iterar sobre los input de codigo de barra
	$(".get-codigo").each(function(){
		
		// Se compara el valor del input que se itera contra el input que llama esta funcion para saber si son iguales
		// Tambien se establece que no se compare contra si mismo
		// Condicion verdadera:
		// La cantidad correspondiente al input que se itera se aumenta en 1 ya que el producto ya existe
		// y el valor del input que llama la funcion se reinicia
		if($(this).val() == $("#codigoBarra-" + id).val() && $(this).attr("id") != ("codigoBarra-" + id)){
			var inputCantidad = $("#cantidad-" + $(this).attr("id").match(/\d+/));
			var cantidad = inputCantidad.val();
			
			inputCantidad.val(parseInt(cantidad) + 1);
			
			// Borrar su valor y colocar cursor sobre el
			$("#codigoBarra-" + id).val("");
			$("#codigoBarra-" + id).focus();
			
			bandera = true;
			
			// Terminar el ciclo cuando la condicion es verdadera
			return false;
		}
	});
	
	// Devolver bandera para saber si se encontro conicidencia del producto
	return bandera;
}