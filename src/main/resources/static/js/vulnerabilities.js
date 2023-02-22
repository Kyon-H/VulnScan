function load(){
    $.post("/vulninfo/list",
        function(data){
            if(data.code == 0){
                //success
                console.log(data.page);

            }else{
                layer.msg(data.msg);
            }
        }
    )
}