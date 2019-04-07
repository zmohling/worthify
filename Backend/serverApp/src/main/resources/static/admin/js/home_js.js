var userDisplay;
var auth;

window.onload = function(){

    auth = getQueryString("auth");
    console.log(auth);
    //load the users table by default
    var tabs = document.getElementById("Users");
    tabs.style.display = "block";
    
    var onlineUsers = setInterval(updateOnlineUsers, 2500);
    userDisplay = document.getElementById("userDisplay")
    if(userDisplay == null){
        console.log("online display not found...")
    }
}

var getQueryString = function ( field, url ) {
    var href = url ? url : window.location.href;
    var reg = new RegExp( '[?&]' + field + '=([^&#]*)', 'i' );
    var string = reg.exec(href);
    return string ? string[1] : null;
};


function updateOnlineUsers(){

    var url = 'http://cs309-jr-1.misc.iastate.edu:8080/users/numOnline';
    url = 'http://localhost:8080/users/numOnline'

    $.ajax({dataType: 'json', type:'GET', url: url,
    success: function(json){
        if(userDisplay != null){
            userDisplay.innerHTML = "<b>Online: " + json.num + "";
        }else{
            console.log("Cannot update page.")
            console.log("online: " + json.num);
        }
    },
    error: function(){
        //do nothing
    }
    });
}

function switchTable(evt, tableName) {
    //switch the table that is being displayed
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
      tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
      tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(tableName).style.display = "block";
    evt.currentTarget.className += " active";
}

function logout(){
    window.location.href = "login.html";
}



