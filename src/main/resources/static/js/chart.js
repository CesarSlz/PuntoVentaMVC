var ventas = [];
var fechas = [];

// Peticion para obtener la informacion de la ventas a graficar
$.ajax({
	type: "GET",
	dataType: "json",
	url: "http://localhost:8888/saldos",
	success: function(result){
		
		// Iterrar sobre el resultado y agregar a los a los array los datos de la venta y la fecha
		$.each(result.datos, function(index, item){
			
			// Dar formato a fecha
			var fecha = new Date(item.fechaCreacion);
			var año = fecha.getFullYear();
			var mes = fecha.getMonth()+1;
			var dia = fecha.getDate();
			
			if (mes < 10) {
				mes = '0' + mes;
			}
			if (dia < 10) {
				dia = '0' + dia;
			}
			
			// Agregar al array la venta
			ventas.push(item.ventaTotal);
			
			// Agregar al array la fecha
			fechas.push(dia + "/" + mes + "/" + año);
		});
		
		// Grafica
		var ctx = document.getElementById('grafica').getContext('2d');
		var grafica = new Chart(ctx, {
			type: "line",
			data: {
				labels: fechas,
				datasets: [{
					label: 'Venta',
					data: ventas,
				    backgroundColor: 'rgba(157, 193, 91, 0.3)',
				    borderColor: '#9dc15b',
				    pointBackgroundColor: '#9dc15b',
				    pointBorderColor: '#9dc15b'
				}]
			},
			options: {
				title: {
					display: true,
					text: 'Proyección de Ventas',
					fontSize: 15,
					
				},
				scales: {
					yAxes: [{
						ticks: {
							beginAtZero: true
						},
						scaleLabel: {
							display: true,
					        labelString: 'Pesos'
					    }
					}],
					xAxes: [{
		                ticks: {
		                	display: false,
		                    //autoSkip: false,
		                    //maxRotation: 40,
		                    //minRotation: 40
		                },
		                scaleLabel: {
							display: true,
					        labelString: 'Fecha'
					    }
		            }]
				}
			}
		})
	}
});