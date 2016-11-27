function valueData(){
	
	//非空验证
	var isnull=$(".isnull");
	isnull.each(function(){
		var value=$.trim($(this).val())
		if(value==null||value){
			alert($(this).attr("errmsg"));
		    return false; //退出循环
		}
	      
	});
}