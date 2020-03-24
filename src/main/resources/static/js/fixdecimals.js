$(document).ready(function(){
	$(".fix-decimals").each(function(){
		$(this).val(parseFloat($(this).val()).toFixed(2));
	});
});