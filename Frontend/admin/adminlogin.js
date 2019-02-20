function login(){

	var loginRequest = {
		"id":0,
		"lastName":"",
		"firstName":"",
		"email":document.getElementById("email").value,
		"password":document.getElementById("password").value,
		"type":1
	};

	$.ajax({contentType: 'application/json', type: 'POST',
			url:'http://cs309-jr-1.misc.iastate.edu:8080/adminlogin',
			data: JSON.stringify(loginRequest),
			success:function(data){
				if(!jQuery.isEmptyObject(data)){
					var response = JSON.parse(data);
					if(data.message == "success"){
						alert("login sucess");
					}else{
						alert("login failure")
					}
				}
			},
			error: function(xhr, status, error) {
               	alert("login failure: " + error);
          	}

		});
}
