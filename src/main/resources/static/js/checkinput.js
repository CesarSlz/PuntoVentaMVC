$("#check-empty-inputs").click(function(){
	$(".is-empty input.get-codigo").each(function(){
		if($(this).val() == ""){
			$(this).parent().parent().remove();
		}
	});
});