let Name
function create(){
    Name = document.getElementById("n").value;
    console.log(Name);
    var width = document.getElementById("w").value;
    var height = document.getElementById("h").value;

    if(width === "" || height === "" || width < 1 || height < 1){
        width = height = 12;
    }

    if(width > 20 && height > 20){
        width = 20;
        height = 20;
    }
    
    document.location.href = "/bricks" + "/create?y=" + height + "&x=" + width;
}