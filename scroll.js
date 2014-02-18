$(document).ready(function(){
	$('a').on('click', function(event) 
	{
		event.preventDefault();
	 	var sectionId = $(this).attr("data-section"),
		  	$toSlide= $("#container section#"+sectionId),
		  	$fromSlide= $('.active');
		if (!($toSlide.hasClass("active")))
	  	{  
		  	$fromSlide.animate({"left":"-100%"},100,'linear')
		  	$toSlide.animate({"left":"0%"},100,'linear',function()
		  	{    
				$fromSlide.css("left","100%");
				$fromSlide.removeClass("active");    
				$toSlide.addClass("active");    
		  	});
	  	}
  	});
});