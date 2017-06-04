// index.js





//creating a file 
window.onload = function() {

    document.getElementById("loginbtn").onclick = function() {


        var username = document.getElementById("username");
        var pwd = document.getElementById("pwd");

        var userdata = {};
        userdata["username"] = username.value;
        userdata["pwd"] = pwd.value;
        userdata["purpose"] = "login";

        var jsonStr = JSON.stringify(userdata);
        //ajax call
        var xmlhttp;
        if (window.XMLHttpRequest)
            xmlhttp = new XMLHttpRequest();
        else
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        xmlhttp.open("POST", "RegisterUser", true);
        xmlhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        xmlhttp.send(jsonStr);
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                if (xmlhttp.responseText == "fail") {
                    alert("Incorrect username or password!!");
                    window.location.href = "./index.html";
                } else {
                    localStorage.setItem("bucketname", document.getElementById("username").value);
                    window.location.href = "./html/homePage.html";
                }

            }
        };


    };

    document.getElementById("regbtn").onclick = function() {
        var regusername = document.getElementById("usernamereg");
        var regpwd = document.getElementById("pwdreg");
        var userdata = {};
        userdata["regusername"] = regusername.value;
        userdata["regpwd"] = regpwd.value;
        userdata["purpose"] = "register";
        var jsonStr = JSON.stringify(userdata);
        //ajax call
        var xmlhttp;
        if (window.XMLHttpRequest)
            xmlhttp = new XMLHttpRequest();
        else
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        xmlhttp.open("POST", "RegisterUser", true);
        xmlhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        xmlhttp.send(jsonStr);
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                alert(xmlhttp.responseText);
                window.location.href = "./index.html";
            }
        };
    };
}