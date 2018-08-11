$(function(){
	$('#saveForm').validate({
		rules: {
			name       :{required:true}
        },messages:{
            name :{required:"必填"}
        }
 	});
	$('.saveBtn').click(function(){
	   if($('#saveForm').valid()){
           $.ajax({
               type: "POST",
               url: "/role/update",
               data: $("#saveForm").serialize(),
               headers: {"Content-type": "application/x-www-form-urlencoded;charset=UTF-8"},
               success: function (result) {
                   if (result.success) {
                       alert("编辑成功");
                       pageaction();
                       closeDialog();
                   } else {
                       alert(result.message);
                   }
               }
           });
	   }else{
		   alert('数据验证失败，请检查！');
	   }
	});

});	
