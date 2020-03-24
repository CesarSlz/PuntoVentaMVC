// Calcular ventas y compras
function calcularMonto(){
	var sum = 0;
	$(".get-codigo").each(function(){
		var number = $(this).attr("id").match(/\d+/);
		
		var inputPrecio = $("#precio-" + number);
		var precio = parseFloat(inputPrecio.val());
		
		var inputCantidad = $("#cantidad-" + number);
		var cantidad = parseFloat(inputCantidad.val());
		
		var inputMonto = $("#monto-" + number);
		var monto = 0;
		
		if(isNaN(precio) || isNaN(cantidad)){
			inputMonto.val("");
		}else{
			monto = precio * cantidad;
			inputMonto.val(monto.toFixed(2));
			
			sum += monto;
		}
	});
	
	return sum;
};

function calcularTotal(sum){
	if(sum == 0 || isNaN(sum)){
		$("#total-0").val("");
	}else{
		$("#total-0").val(sum.toFixed(2));
	}
	
	return sum;
}

function calcularCambio(sum){
	var inputEfectivo = $("#efectivo-0");
	var efectivo = parseFloat(inputEfectivo.val());
	var total = parseFloat(sum)
	var inputCambio = $("#cambio");
	var operacion = $("#operacion").text().split(" ")[1];
	
	// Verificar si estamos en la vista de compra o venta
	// ya que en venta el efectivo no puede ser menor que le total
	// sin embargo en la compra no existe esa restriccion
	if(operacion == "Venta"){
		// Validar que el efectivo es mayor al total de la venta
		if(efectivo < total || isNaN(total) || isNaN(efectivo)){
			inputEfectivo.attr("min", total);
			inputCambio.val("");
		}else{
			inputEfectivo.attr("min", total);
			inputCambio.val((efectivo-total).toFixed(2));
		}
	}else{
		if(isNaN(total) || isNaN(efectivo)){
			inputCambio.val("");
		}else{
			inputCambio.val((efectivo-total).toFixed(2));
		}
	}
}

$(document).on("keyup", ".get-cantidad", function(){
	var input = $(this);
	
	var sum = calcularMonto();
	var total = calcularTotal(sum);
	calcularCambio(total);
});

$(document).on("keyup", "#efectivo-0", function(){
	var total = $("#total-0").val();

	calcularCambio(total);
});

//Calcular saldo
function calcularSaldo(){
	var fondoCaja = parseFloat($("#fondo-caja").val());
	
	if(isNaN(fondoCaja)){
		fondoCaja = 0;
	}
	
	var abonoCompra = parseFloat($("#abono-compra").val());
	var compraTotal = parseFloat($("#compra-total").val());
	var ventaTotal = parseFloat($("#venta-total").val());
	var total = $("#total");
	
	total.val(((ventaTotal + fondoCaja) - (compraTotal + abonoCompra)).toFixed(2));
}

$(document).ready(function(){
	calcularSaldo();
});

$(document).on("keyup", "#fondo-caja", function(){
	calcularSaldo();
});
