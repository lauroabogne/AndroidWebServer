
changeMenuColor();

function changeMenuColor(){

    var location = window.location.pathname;

    var splitedLocation = location.split("/");
    var requestedUrl = splitedLocation[1];

    if(requestedUrl.trim().length <=0){

        document.getElementById("home").style.backgroundColor = "blue";

    }else{

        document.getElementById(requestedUrl).style.backgroundColor = "blue";

    }

}

