$(function() {
		
    function minus(){
	      if ($(".select_wrap").size() > 1) {
	        $(".input_minus").show();
	      } else {
	        $(".input_minus").hide();
	      }
	    }

	    minus();

	    
	    $("#iconplus").click(function(){
	      $(".select_wrap:last").clone(true).appendTo(".dwrap").find("input").val("");
	      $(this).hide();
	      minus();
	    });


	    $(".input_minus").click(function(){
	      $(this).parent().remove();
	      $(".select_wrap:last").find(".input_plus").show();
	      minus();
	    });

	   
	 
})



	  


