let Name = ""
let gameName = "BricksBreaking"
let width = 0
let height = 0

$(document).ready(function () {
    refreshField("/bricks/field");
    $("#media").hide();
    writeScores();
    updatePoints();
    updateWands();
    getName();
    getFieldHeight();
    getFieldWidth();
    //getLoggedUser();

});

function addButton(){
    st = document.getElementById("is").value;
    if(st === "true"){
        $("#but").empty().append("<a id=\"log\" onclick=\"logout()\">logout</a>");
    } else {
        $("#but").empty().append("<a id=\"log\" onclick='loginMain()'>login</a>");
    }
}

function loginMain(){
    document.getElementById("is").value = "false";
    document.getElementById("name").value = "Guest";
    document.location.href = "/bricks/login";
}

function getLoggedUser(){
    $("#ac").empty();
    let name = document.getElementById("name").value;
    if(name === ""){
        document.getElementById("is").value = "false";
        name = "Guest";
    } else {
        document.getElementById("is").value = "true";
    }
    $("#ac").append("<span>" + "Account: " + name +"</span>");
}

function getName(){
    $.get('/bricks/name', function (response){
       document.getElementById("name").value = response;
    }).done(function (){
        getLoggedUser();
        addButton();
    });

}

function updatePoints(){
    $.get('/bricks/score', function(response){
        console.log(response);
        document.getElementById("score").textContent = response;
    });
}

function updateWands(){
    $.ajax({
        url: '/bricks/wands',
    }).done(function (html){
        $("#wands").html(html);
    });
}

function refreshField(url){
    $.ajax({
        url: url,
    }).done(function (html){
        $("#field").html(html);
        $("#field a").each(function () {
            const url = $(this).attr("href").replace("/bricks", "/bricks/field");
            $(this).removeAttr("href");
            $(this).click(function () {
                updatePoints();
                updateWands();
                refreshField(url);
                updatePoints();
                updateWands();
                isSolved();
                isFailed();
            });
        })
    });
}

function addScore() {
    $.get('/bricks/score', function (response) {
        var score = new Object();
        score.player = document.getElementById("name").value;
        score.game = gameName;
        score.points = response;
        score.playedOn = new Date();
        $.ajax({
            url: '/api/score',
            type: 'POST',
            data: JSON.stringify(score),
            contentType: "application/json"
        });
    });
    setTimeout(function () {
        writeScores();
    }, 1000);
}

function isSolved(){
    $.get('/bricks/state', function (response){
        if(response === 'solved'){
            addScore();
        }
    });
}

function isFailed(){
    $.get('/bricks/state', function (response){
        if(response == "failed"){
            $("#field table").hide(300);
            $("#field").append("<h5>You failed, try again!</h5>");
            addScore();
        }

    });
}

function logout(){
    document.getElementById("name").value = "";
    document.getElementById("is").value = "false";
    document.location.href = "/bricks/init?name=";
}

function newGame(){
    console.log("new");
    var height = document.getElementById("height").value;
    var width = document.getElementById("width").value;
    var name = document.getElementById("name").value;
    document.location.href = "/bricks" + "/create?y=" + height + "&x=" + width + "&name=" + name;
}


function getFieldWidth(){
    $.get('/bricks/width', function (response){
        document.getElementById("width").value = response;
    });
}

function getFieldHeight(){
    $.get('/bricks/height', function (response){
        document.getElementById("height").value = response;
    });
}

function create(){
    var width = document.getElementById("w").value;
    var height = document.getElementById("h").value;

    if(width == "" || height == ""){
        alert("You must write down the size of the field!");
        return;
    }
    if(width < 4 || height < 4){
        alert("Field size must be greater than 1!");
        return
    }
    if(width > 20 && height > 20){
        alert("Field size must be less than 20!");
        return;
    }

    if(document.getElementById("is").value === "false") {
        if (confirm("To add comments, rating and save your score you need to log in. Do you want to continue to log in window?")) {
            document.location.href = "/bricks/login";
        } else {
            document.location.href = "/bricks" + "/create?y=" + height + "&x=" + width;
        }
    } else {
        document.location.href = "/bricks" + "/create?y=" + height + "&x=" + width;
    }
}

async function addUser(){
    var email = document.getElementById("n").value;
    let st = await $.get('/api/user/email/' + email);
    if(st === true){
        alert("This email already taken");
        return;
    }
    var name = document.getElementById("w").value;
    let nSt = await $.get('/api/user/' + name).fail(function (){
        return null;
    });
    if(nSt.login != null){
        alert("This username already taken");
        return;
    }
    var pass = document.getElementById("h").value;

    if(email == ""){
        alert("You must write down your email");
        return;
    }
    if(!email.match(/.+@.+\..+/)){
        alert("Invalid email!");
        return;
    }
    if(name == ""){
        alert("You must write down your username");
        return;
    }
    if(pass == ""){
        alert("You must write down your password");
        return;
    }

    var u = {
        email: email,
        login: name,
        password: pass
    };
    console.log(u);
    $.ajax({
        url: '/api/user',
        type: 'POST',
        data: JSON.stringify(u),
        contentType: "application/json"
    });
    setTimeout(function (){
        document.location.href = "/bricks/login";
    }, 300);
}

function addComment(){
    if(document.getElementById("is").value === "false") return;
    var message = document.getElementById('message').value;
    document.getElementById('message').value = "";
    if (message && message.length > 0) {
        var c = {
            player: document.getElementById("name").value,
            comment: message,
            game: gameName,
            commentedOn: new Date()
        };
        console.log(c);
         $.ajax({
             url: '/api/comment',
             type: 'POST',
             data: JSON.stringify(c),
             contentType: "application/json"
         });
    }
    setTimeout(function (){
        writeComments();
    }, 200);
}

function writeScores(){
    if(document.getElementById("is").value === "false") return;
    $.ajax({
        url: "/api/score/BricksBreaking",
    }).done(function (json) {
        $("#scoreTable tbody").empty();
        for (var i = 0; i < json.length; i++) {
            var score = json[i];
            $("#scoreTable tbody").append($("<tr><td>" + score.player + "</td>" + "<td>" + score.points + "</td></tr>"));
        }
    });
}

function writeComments(){
    jQuery.fx.speeds.turtle = 500;
    $("#media").show('turtle');
    document.getElementById("wButton").textContent = "Hide comments";
    document.getElementById("wButton").onclick = deleteComments;
    $.ajax({
        url: '/api/comment/BricksBreaking',
    }).done(function (json){
       $("#media").empty();
       $("#media").append("<div id=\"count\"></div><br><hr><br>");
       document.getElementById("count").textContent = json.length + " Comments";
       for(var i = json.length - 1; i >= 0; i--){
           var comment = json[i];
           var commentedOn = new Date(comment.commentedOn);
           console.log(commentedOn);
           var date = formatDate(commentedOn);
           console.log(typeof comment.commentedOn)
           $("#media").append($("<div class=\"media-body\">\n" + "<div class=\"media-heading\">\n" + "<h4 class='\"username\"'>" + comment.player + " - </h4>"
               + "<h4 class=\"date\">" + "&nbsp;" + date + "</h4>" + "</div>" + "<div id=\"text\">" +"<span>" + comment.comment + "</span></div>\n</div><br><hr><br>"));
       }
    });
}

function deleteComments(){
    jQuery.fx.speeds.turtle = 500;
    $("#media").hide('turtle');
    document.getElementById("wButton").textContent = "Show comments";
    document.getElementById("wButton").onclick = writeComments;

}

function formatDate(date) {
    var hour = date.getHours();
    var minutes = date.getMinutes();
    if(minutes < 10){
        minutes = '0' + minutes;
    }
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();

    return hour + ':' + minutes + ' ' + day + '/' + month + '/' + year;
}

async function login(){
    var login = document.getElementById("n").value;
    var pass = document.getElementById("p").value;

    if(login == ""){
        alert("You must write down your login");
        return;
    }
    if(pass == ""){
        alert("You must write down your password");
        return;
    }

    let state = await $.get('/api/user/' + login).fail(function (){
        alert("There is no user with login: \'" + login + "\'!");
    });

    if(pass != state.password){
        alert('Invalid password!');
        return false;
    }

    console.log(state.status);
    console.log(typeof state.status);

    document.location.href = "/bricks/init?name=" + login;

}


