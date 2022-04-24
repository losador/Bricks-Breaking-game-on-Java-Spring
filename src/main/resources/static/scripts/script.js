let Name

$(document).ready(function () {
    refreshField("/bricks/field");
    updatePoints();
});

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
            });
        })
    });
}

function create(){
    Name = document.getElementById("n").value;
    if(Name == ""){
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
    
    document.location.href = "/bricks" + "/create?y=" + height + "&x=" + width;
}