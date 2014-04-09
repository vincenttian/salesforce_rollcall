/* to use setSection on a specific section, do
setSection($("#container section#"+sectionId), $('.active'))*/

function setSection(to, from)
{
    if (!(to.hasClass("active")))
    {  
        from.animate({"left":"-100%"},100,'linear')
        to.animate({"left":"0%"},100,'linear',function()
        {    
            from.css("left","100%");
            from.removeClass("active");    
            to.addClass("active");    
        });
    }
}

$(document).ready(function(){
	$('a').on('click', function(event) 
	{
	 	var sectionId = $(this).attr("data-section"),
		  	$toSlide= $("#container section#"+sectionId),
		  	$fromSlide= $('.active');
		setSection($toSlide, $fromSlide);
  	});
});