var baseUrl = "http://localhost:8080";
//var baseUrl =  "http://cs309-jr-1.misc.iastate.edu:8080";

function login(){
    var url = baseUrl + "/login";

    var userInfo = {
        email:document.getElementById("email").value,
        password:document.getElementById("pass").value
    };

    $.ajax({contentType: 'application/json', dataType: 'json', type:'POST', url: url, data:JSON.stringify(userInfo),
    success: function(json){
        console.log(json);
        if(json.user.type == 1 && json.message == "user login success"){
            //console.log(json)
            //console.log(json.user.authorization);
            window.location.href= "/admin/home.html?auth=" + json.user.authorization;
        }
    },
    error: function(){
        //do nothing
    }
    });
}