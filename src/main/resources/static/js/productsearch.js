$(document).on("keyup", ".get-codigo", function(){
	var input = $(this);
	var id = input.attr("id").match(/\d+/);
	var codigo = input.val();
	var numbers = /^[0-9]+$/;
	
	// Verificar que el valor del input sea numerico, de lo contrario no se buscara y se colocaran los inputs vacios
	if(codigo.match(numbers)){
		$.ajax({
			type: "GET",
			dataType: "json",
			url: "http://localhost:8888/productos/codigo/" + codigo,
			success: function(result){
				// Verificar si se encontro el producto, de lo contrario el valor de los inputs sera vacio
				if(result.datos != null){
					// Variable que permite saber si estamos en la vista de compra o venta
					var operacion = $("#operacion").text().split(" ")[1];
					
					// Variable que permite saber si estamos en una vista de modificar
					var accion = $("#operacion").text().split(" ")[0];
					
					// Funcion que verifica si el producto ya existe
					var bandera = existe(id);
					
					// Si bandera es false quiere decir que no se encontro coincidencia
					// Por lo tanto se colocara la informacion del producto en los inputs
					// Si la bandera es true quiere decir que se encontro una coincidencia
					// Por lo tanto su cantatidad solo se aumenta
					if(!bandera && input.val() != ""){
						$("#producto-" + id).val(result.datos.id);
						$("#nombre-" + id).val(result.datos.nombre);
						$("#marca-" + id).val(result.datos.marca);
						$("#tamano-" + id).val(result.datos.tamano);
						
						// Comprobación que permitará colocar el precio venta cuando se trate de una venta
						// y el precio de compra cuando se trate de una compra
						if(operacion == "Venta"){
							$("#precio-" + id).val(result.datos.precioVenta.toFixed(2));
						}else{
							$("#precio-" + id).val(result.datos.precioCompra.toFixed(2));
						}
						
						// En la vista de nueva compra o venta se le colocara al input de cantidad un "1"
						// En la vista de modificar compra o venta solo se le colocara al input de cantidad un "1"
						// cuando se agregue un nuevo producto a la compra o venta. Para los productos ya existentes
						// se respetara su cantidad (no se reemplazará por un "1")
						if(accion == "Nueva"){
							$("#cantidad-" + id).val(1);
						}else if(accion == "Modificar" && $("#monto-" + id).val() == ""){
							$("#cantidad-" + id).val(1);
						}
						
						// Insertar nuevos inputs para el siguiente producto
						// Se le manda el input que dispara el evento, su id y en que vista se encuentra (compra o venta)
						addInputs(input, id, operacion);
					}
					
					// Realizar calculos
					var sum = calcularMonto();
					var total = calcularTotal(sum);
					calcularCambio(total);
				}else{
					$("#producto-" + id).val("");
					$("#nombre-" + id).val("");
					$("#marca-" + id).val("");
					$("#tamano-" + id).val("");
					$("#precio-" + id).val("");
					
					$("#cantidad-" + id).val("");
					
					var sum = calcularMonto();
					var total = calcularTotal(sum);
					calcularCambio(total);
				}
			}
		});
	}else{
		$("#producto-" + id).val("");
		$("#nombre-" + id).val("");
		$("#marca-" + id).val("");
		$("#tamano-" + id).val("");
		$("#precio-" + id).val("");
		
		$("#cantidad-" + id).val("");
		
		var sum = calcularMonto();
		var total = calcularTotal(sum);
		calcularCambio(total);
	}
});