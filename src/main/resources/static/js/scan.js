$("#addTargetBtn").on("click", function () {
    var address=$("#targetUrl").val();
    //var pattern = /(https?)://[a-zA-z]+://[^\s]*/

});

$("#scanSubmitBtn").on("click", function () {
    var address=$("#address").val();
    console.log(address)
    var scanType=$("#ScanType").val();
    console.log(scanType)
    var scanSpeed=$("input[name=customRadioInline]:checked").val();
    var description=$("#message-text").val();
    console.log(scanSpeed)
    var username=$("#userName").val();
    var formData={
        "username":username,
        "address":address,
        "scanType":scanType,
        "scanSpeed":scanSpeed,
        "description":description
    };
    $.post('/addTarget',
        formData,
        function(data){
            console.log(data);
        },'json'
    );
});
