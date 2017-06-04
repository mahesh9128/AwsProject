// index.js


function view(buttonid) {
    localStorage.setItem("viewcontbtn", buttonid);
    window.location.href = "./htmlPages/container.html";
}



window.onload = function() {

    loadcontainer();

    document.getElementById("logoutbtn").onclick = function() {
        window.location.href = "../index.html";
    };

    document.getElementById("delcontdetbtn").onclick = function() {
        var delconatinername = document.getElementById("delcontname");

        //ajax call
        var xmlhttp;
        if (window.XMLHttpRequest)
            xmlhttp = new XMLHttpRequest();
        else
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        xmlhttp.open("POST", "../DeleteS3Object", true);
        xmlhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        xmlhttp.send(delconatinername.value + "_" + localStorage.getItem("bucketname"));
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                if (xmlhttp.responseText == "success") {
                    alert("File deleted successfully!!");
                    window.location.href = "../html/homePage.html";
                } else if (xmlhttp.responseText == "failure") {
                    alert("Please Try again !!");
                    window.location.href = "../html/homePage.html";
                }
            }
        };
    };

    //uploading a file 
    document.getElementById('upload-form').onsubmit = function() {

        var form = document.getElementById('upload-form');
        var formData = new FormData(form);
        formData.append("bucket", localStorage.getItem("bucketname"));
        if (window.XMLHttpRequest)
            xmlhttp = new XMLHttpRequest();
        else
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");

        xmlhttp.open('POST', "../UploadS3Objects", true);
        xmlhttp.send(formData);
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                if (xmlhttp.responseText == "success") {
                    alert("File uploaded successfully!!");
                    window.location.href = "../html/homePage.html";
                } else if (xmlhttp.responseText == "failure") {
                    alert("Please Try again !!");
                    window.location.href = "../html/homePage.html";
                }
            }
        };

        return false; // To avoid actual submission of the form
    };


    //downloading a file 
    document.getElementById("downloadbtn").onclick = function() {
        var userdata = {};
        userdata["bucketname"] = localStorage.getItem("bucketname");
        userdata["filename"] = document.getElementById("downname").value;
        var jsonStr = JSON.stringify(userdata);
        if (window.XMLHttpRequest)
            xmlhttp = new XMLHttpRequest();
        else
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        xmlhttp.open("POST", "../DownloadS3Objects", true);
        xmlhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        xmlhttp.send(jsonStr);
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                downloadFile(xmlhttp.responseText, document.getElementById("downname").value);
            }
        };
    };

}


function loadcontainer() {
    var xmlhttp;
    if (window.XMLHttpRequest)
        xmlhttp = new XMLHttpRequest();
    else
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    xmlhttp.open("POST", "../ListS3Objects", true);
    xmlhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xmlhttp.send(localStorage.getItem("bucketname"));
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            var contdet = document.getElementById('contdetail');
            contdet.innerHTML = xmlhttp.responseText;
        }
    };
}

function downloadFile(text, fname) {
    var element = document.createElement('a');
    element.setAttribute('href', text);
    element.setAttribute('download', fname);
    element.style.display = 'none';
    document.body.appendChild(element);
    element.click();
    document.body.removeChild(element);
}

function saveByteArray(reportName, byte) {
    var blob = new Blob([byte]);
    var link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    var timeNow = new Date();
    var month = timeNow.getMonth() + 1;
    var fileName = reportName;
    link.download = fileName;
    link.click();
};

function base64ToArrayBuffer(base64) {
    var binaryString = window.atob(base64);
    var binaryLen = binaryString.length;
    var bytes = new Uint8Array(binaryLen);
    for (var i = 0; i < binaryLen; i++) {
        var ascii = binaryString.charCodeAt(i);
        bytes[i] = ascii;
    }
    return bytes;
}