let Name = ""
let gameName = "BricksBreaking"

$(document).ready(function () {
    refreshField("/bricks/field");
    writeScores();
    updatePoints();
    getName();
});

function getName(){
    $.get('/bricks/name', function (response){
       Name = response;
    });
}

function updatePoints(){
    $.get('/bricks/score', function(response){
        console.log(response);
        document.getElementById("score").textContent = response;
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
                refreshField(url);
                updatePoints();
                isSolved();
            });
        })
    });
}

function isSolved(){
    $.get('/bricks/state', function (response){
        if(response === 'solved'){
            $.get('/bricks/score', function (response){
                var score = new Object();
                score.player = Name;
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
    });
}

function create(){
    var name = document.getElementById("n").value;
    if(name == ""){
        alert("You must write down your name!");
        return;
    }

    var width = document.getElementById("w").value;
    var height = document.getElementById("h").value;

    if(width == "" || height == ""){
        alert("You must write down the size of the field!");
        return;
    }
    if(width < 1 || height < 1){
        alert("Field size must be greater than 1!");
        return
    }
    if(width > 20 && height > 20){
        alert("Field size must be less than 20!");
        return;
    }
    
    document.location.href = "/bricks" + "/create?y=" + height + "&x=" + width + "&name=" + name;
}

function writeScores(){
    $.ajax({
        url: "/api/score/BricksBreaking",
    }).done(function (json) {
        $("#scoreTable tbody").empty();
        for (var i = 0; i < json.length; i++) {
            var score = json[i];
            $("#scoreTable tbody").append($("<tr><td>" + score.player + "<td>" + score.points + "<td>"));
        }
    });
}
