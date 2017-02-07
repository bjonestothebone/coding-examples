$(function(){
	$("input.search").keydown(function(event){
		if(event.keyCode == 13){
		   event.preventDefault();
		   $(this).parent("form").submit();
		}
	});
});