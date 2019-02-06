var GETButton = document.getElementById("GET");
var POSTButton = document.getElementById("POST");
var PUTButton = document.getElementById("PUT");
var DELETEButton = document.getElementById("DELETE");
var box = document.getElementById("box");


GETButton.onclick = function(){
     console.log("GET");
     $.ajax({dataType: 'json', type: 'GET', url: 'http://localhost:8080/youcoinme/practiceGET',
     success: function(json){
          if(!jQuery.isEmptyObject(json)){
               console.log(JSON.stringify(json));
               //$("#box").text(json.val);
               box.value = json.val;
          }
     },
     error: function(){
          alert("error on get");
     }
     });
};

POSTButton.onclick = function(){
     console.log("POST");
     var itemToPost = {
          "id":0,
          "val":box.value
     }
     $.ajax({type: 'POST', url: 'http://localhost:8080/youcoinme/practicePOST', data: JSON.stringify(itemToPost), dataType: 'json', contentType: 'application/json',
     success: function(){
          console.log("Posted: " + box.value);
          box.value = "";
     },
     error: function(e){
          alert("error posting: \"" + box.value + "\": ");
          console.log(e);
     }
     });
};

PUTButton.onclick = function(){
     console.log("PUT");
     var itemToPut = {
          "id":0,
          "val":box.value
     }
     $.ajax({type: 'PUT', url: 'http://localhost:8080/youcoinme/practicePUT', data: JSON.stringify(itemToPut), dataType:'json', contentType:'application/json',
     success: function(){
          console.log("Put: " + box.value);
          box.value = "";
     },
     error: function(e){
          alert("error putting: \"" + box.value + "\": ");
          console.log(e);
     }
     });
};

DELETEButton.onclick = function(){
     console.log("DELETE");

     $.ajax({type: 'DELETE', url: 'http://localhost:8080/youcoinme/practiceDELETE',
     success: function(){
          console.log("deleted item");
          box.value = "";
     },
     error: function(e){
          alert("error deleting");
          console.log(e);
     }

     });
};
