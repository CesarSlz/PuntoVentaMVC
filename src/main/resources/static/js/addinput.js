function contadorInputs(){
	var contador = 0;
	$(".get-codigo").each(function(){
		contador = $(this).attr("id").match(/\d+/);
	});
	return contador;	
}

// Crear inputs
function addInputs(input, id, operacion){
	
	// Obtener el numero de inputs de codigo de barra que exiten
	var contador = contadorInputs();
	
	// Convertir a entero el numero del id del input que dispara este evento
	var i = parseInt(id);
	
	// Condicion para restringir la creacion de nuevos inputs que contendran la informacion del producto
	// Se podran crear nuevos inputs
	// Si se posiciona sobre el ultimo input en pantalla
	// Ej. si hay 3 inputs (contador) y se posiciona en el 1 (i) no se pueden crear inputs porque contador es mayor a i
	if(contador <= i && input.val() != ""){
		
		// Contenedor principal
		var container = $("#product-container");
		
		var row = $("<div class='row mb-2 product-row is-empty'></div>").appendTo(container);
			
		var col1 = $("<div class='col p-0 mr-2 col-s'></div>").appendTo(row);
		var inputDetalleId = $(
				"<input " +
				"type='hidden' " +
				"id='detalleId-" + (i+1) + "' " +
				"name='id[" + (i+1) + "]' " +
				"value='0' " +
				"required>"
		).appendTo(col1);
			
		var inputProducto = $(
				"<input " +
				"type='hidden' " +
				"id='producto-" + (i+1) + "' " +
				"name='producto[" + (i+1) + "].id' " +
				"required>"
		).appendTo(col1);
			
		var labelCodigo = $("<label class='h5 label-hide'>Código</label>").appendTo(col1);
		var inputCodigo = $(
				"<input " +
				"type='text' " +
				"class='form-control form-control-lg get-codigo' " +
				"id='codigoBarra-" + (i+1) + "' " +
				"name='producto[" + (i+1) + "].codigoBarra' " +
				"placeholder='Código' " +
				"pattern='[0-9]{1,20}' " +
				"title='Ingresar un código de barras valido' " +
				"required>"
		).appendTo(col1).focus();
			
		var col2 = $("<div class='col p-0 mr-2 col-s'></div>").appendTo(row);
		var labelCodigo = $("<label class='h5 label-hide'>Nombre</label>").appendTo(col2);
		var inputNombre = $(
				"<input " +
				"type='text' " +
				"class='form-control form-control-lg' " +
				"id='nombre-" + (i+1) + "' " +
				"name='producto[" + (i+1) + "].nombre' " +
				"placeholder='Nombre' " +
				"maxlength='100' " +
				"title='Nombre del producto' " +
				"readonly>"
		).appendTo(col2);
			
		var col3 = $("<div class='col p-0 mr-2 col-s'></div>").appendTo(row);
		var labelMarca = $("<label class='h5 label-hide'>Marca</label>").appendTo(col3);
		var inputMarca = $(
				"<input " +
				"type='text' " +
				"class='form-control form-control-lg' " +
				"id='marca-" + (i+1) + "' " +
				"name='producto[" + (i+1) + "].marca' " +
				"placeholder='Marca' " +
				"maxlength='100' " +
				"title='Marca del producto' " +
				"readonly>"
		).appendTo(col3);
		
		var col4 = $("<div class='col-1 p-0 mr-2 col-s'></div>").appendTo(row);
		var labelTamano = $("<label class='h5 label-hide'>Tamaño</label>").appendTo(col4);
		var inputTamano = $(
				"<input " +
				"type='text' " +
				"class='form-control form-control-lg' " +
				"id='tamano-" + (i+1) + "' " +
				"name='producto[" + (i+1) + "].tamano' " +
				"placeholder='Tam.' " +
				"maxlength='10' " +
				"title='Tamaño del producto' " +
				"readonly>"
		).appendTo(col4);
			
		var col5 = $("<div class='col-1 p-0 mr-2 col-s'></div>").appendTo(row);
		var labelPrecio = $("<label class='h5 label-hide'>Precio</label>").appendTo(col5);
		
		if(operacion == "Venta"){
			var inputPrecio = $(
					"<input " +
					"type='number' " +
					"class='form-control form-control-lg' " +
					"id='precio-" + (i+1) + "' " +
					"name='producto[" + (i+1) + "].precioVenta' " +
					"placeholder='Precio' " +
					"min='0.1' " +
					"step='0.1' " +
					"title='Precio de venta del producto' " +
					"readonly>"
			).appendTo(col5);
		}else{
			var inputPrecio = $(
					"<input " +
					"type='number' " +
					"class='form-control form-control-lg' " +
					"id='precio-" + (i+1) + "' " +
					"name='producto[" + (i+1) + "].precioCompra' " +
					"placeholder='Precio' " +
					"min='0.0' " +
					"step='0.1' " +
					"title='Precio de compra del producto' " +
					"readonly>"
			).appendTo(col5);
		}
			
		var col6 = $("<div class='col-1 p-0 mr-2 col-s'></div>").appendTo(row);
		var labelCantidad = $("<label class='h5 label-hide'>Marca</label>").appendTo(col6);
		var inputCantidad = $(
				"<input " +
				"type='number' " +
				"class='form-control form-control-lg get-cantidad' " +
				"id='cantidad-" + (i+1) + "' " +
				"name='cantidad[" + (i+1) + "]' " +
				"placeholder='Cant.' " +
				"min='1' " +
				"pattern='[0-9]' " +
				"title='Ingresar un cantidad valida' " +
				"required>"
		).appendTo(col6);
			
		var col7 = $("<div class='col-1 p-0 mr-2 col-s'></div>").appendTo(row);
		var labelMonto = $("<label class='h5 label-hide'>Marca</label>").appendTo(col7);
		var inputMonto = $(
				"<input " +
				"type='number' " +
				"class='form-control form-control-lg' " +
				"id='monto-" + (i+1) + "' " +
				"name='monto[" + (i+1) + "]' " +
				"placeholder='Monto' " +
				"min='0.1' " +
				"step='0.1' " +
				"title='Monto del producto' " +
				"readonly>"
		).appendTo(col7);
			
		var col8 = $("<div class='col-md-auto p-0 mr-2 d-flex align-items-center col-s'></div>").appendTo(row);
		var button = $(
				"<button " +
				"id='btn-delete-" + (i+1) + "' " +
				"type='button' " + 
				"class='btn btn-danger rounded-circle mx-auto btn-delete'>" +
					"<i class='fas fa-times'></i>" +
				"</button>"
		).appendTo(col8);
	}
}

// Eliminar inputs
$(document).on("click", ".btn-delete", function(){
	
	// Obtener el numero de inputs de codigo de barra que exiten
	var contador = contadorInputs();
	
	// Obtener el numero id del boton que dispara esta evento. Y se convierte a entero
	var id = parseInt($(this).attr("id").match(/\d+/));
	
	// Obtener el row a eliminar
	var deleteRow = $(this).parent().parent();
	
	// Eliminar el row
	deleteRow.remove();
	
	// Codigo para obtener todos los inputs dentro del row y disminuir su numero id
	// Ej. hay 3 rows y se elimina el row correspondiente al numero id 2, el numero id del row 3 debe de pasar a ser 2
	// Condicion que restringe disminuir el numero id cuando se borra el ultimo row
	if(id != contador){
		$(".product-row").each(function(i){
			
			// Condicion que restring disminuir el numero id de los row inferiores cuando se borra un row intermedio
			if(id <= i){
				$(this).find(":input").not(":button").each(function(){
					var input = $(this);
					input.attr("id", input.attr("id").replace(/\d+/, i));
					input.attr("name", input.attr("name").replace(/\d+/, i));
				});
				
				$(this).find(":button").each(function(){
					var button = $(this);
					button.attr("id", button.attr("id").replace(/\d+/, i));
				});
			}
		});
	}
	
	var sum = calcularMonto();
	var total = calcularTotal(sum);
	calcularCambio(total);
});
