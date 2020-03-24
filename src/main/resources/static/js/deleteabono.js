$(".delete-abono").click(function(){
	var botonEliminar = $(this);
	var id = botonEliminar.attr("id").match(/\d+/);
	
	$.ajax({
		type: "DELETE",
		dataType: "json",
		url: "http://localhost:8888/abonos/codigo/" + id,
		success: function(result){
			var inputPago = $("#pago-" + id);
			var inputSaldo = $("#saldo");
			var inputMonto = $("#monto");
			var inputAdeudo = $("#estatus");
			var botonGuardar = $("#check-adeudo");
			var saldo = parseFloat(inputSaldo.val()) + parseFloat(inputPago.val());
			
			botonEliminar.parent().parent().remove();
			
			inputSaldo.val(saldo.toFixed(2));
			inputMonto.attr("max", saldo);
			inputAdeudo.val("Pendiente");
			
			if(botonGuardar.attr("disabled") == "disabled"){
				botonGuardar.removeAttr("disabled");
			}
			
			if($('.delete-abono').length == 0){
				$("#pago-header").remove();
			}
				
		}
	});
});