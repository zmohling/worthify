var userDisplay;
var auth;
var baseUrl = "http://localhost:8080";
//var baseUrl = "http://cs309-jr-1.misc.iastate.edu:8080";
var userJSON;
var articleJSON;

window.onload = function(){

    auth = getQueryString("auth");
    //console.log(auth);

    if(auth != null){

        var url = baseUrl + '/getInfo/' + auth;

        $.ajax({dataType: 'json', type:'GET', url: url,
        success: function(json){
            if(json.error == "auth success"){
                document.getElementById("nameDisplay").innerHTML = "<b><u>Admin Account:</u></b><br>" + json.firstName + " " + json.lastName;
                updateUserTable();
                updateArticleTable();
            }else{
                document.getElementById("mainBody").innerHTML = "Login failed... try again";
            }
        },
        error: function(){
            document.getElementById("mainBody").innerHTML = "Login failed... try again";
        }
        });

    }else{
        document.getElementById("mainBody").innerHTML = "Login failed... try again";
    }


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

    var url = baseUrl + '/users/numOnline';

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

function updateUserTable(){
    console.log("updating user table...");
    var url = baseUrl + "/users/listAll";

    var tableHTML = "<tr><th>First Name</th><th>Last Name</th><th>Email</th><th>Number of Accounts</th></tr>";

    $.ajax({dataType: 'json', type:'GET', url: url,
    success: function(json){
        console.log(json);
        userJSON = json;
        for(var i = 0; i < json.numUsers; i ++){
            tableHTML += "<tr>" +
                "<td>" + json.users[i].firstName + "</td>" + 
                "<td>" + json.users[i].lastName + "</td>" + 
                "<td>" + json.users[i].email + "</td>" + 
                "<td>" + json.users[i].numAccounts + "</td>" +
                "<td class = \"userRemoveCol\"><button class=\"tableButtonUser\">Remove</button></td></tr>";
        }
        document.getElementById("userTable").innerHTML = tableHTML;
    },
    error: function(){
        console.log("error updating user table");
    }
    });
}

function updateArticleTable(){
    console.log("updating article table...");
    var url = baseUrl + "/article/adminGetAll";

    var tableHTML = "<tr><th>Title</th><th>URL</th><th>Active</th><th>UserID (0 = automated)</th></tr>";

    $.ajax({dataType: 'json', type:'GET', url: url,
    success: function(json){
        console.log(json);
        articleJSON = json;
        for(var i = 0; i < json.numArticles; i ++){
            tableHTML += "<tr>" +
                "<td>" + json.articles[i].title + "</td>" + 
                "<td><a href=\"" + json.articles[i].url + "\" target=\"_blank\">" + json.articles[i].url + "</a></td>" + 
                "<td>" + json.articles[i].isActive + "</td>" + 
                "<td>" + json.articles[i].userId + "</td>" +
                "<td><button class=\"tableButton\">Delete</button></td></tr>";
        }
        //console.log(tableHTML);
        document.getElementById("articleTable").innerHTML = tableHTML;
    },
    error: function(){
        console.log("error updating article table");
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



