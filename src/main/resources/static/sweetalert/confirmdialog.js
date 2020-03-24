$("form.get-form").submit(function(){
	event.preventDefault();
	var form = $(this);
	var swInfo = $("#swInfo");
	Swal.fire({
		title: swInfo.attr("data1"),
	    icon: 'question',
	    showCancelButton: true,
	    confirmButtonColor: '#9dc15b',
	    cancelButtonColor: '#6c757d',
	    confirmButtonText: 'Si',
	    cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.value) {
			form.submit();
		}
	})
});