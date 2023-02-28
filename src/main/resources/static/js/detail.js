function load(){
    let vulninfo_id=$.getUrlParam("id");
    $.post("/vulninfo/detail",
        {vulninfo_id:vulninfo_id},
        function(data){
            if(data.code==0){
                console.log(data.detail);
                addDetail(data.detail);
            }else{
                layer.msg(data.msg,{icon:2});
            }
        }
    )

}

function addDetail(data){
    $('#vt_name').text(data.vt_name);
    $('#description').text(data.description);
    $('#affects_url').text(data.affects_url);
    $('#request').text(data.request);
    $('#impact').text(data.impact);
    $('#recommendation').text(data.recommendation);
    var item="";
    $.each(data.references,function(i,m){
        item+=`<a href=${m.href}>${m.rel}</a><br/>`;
    })
    $('#references').html(item);
}

$('#goBack').click(function(){
    //返回上一页：
    window.history.go(-1);
});

//getUrlParam
(function ($) {
    $.getUrlParam = function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return decodeURI(r[2]);
        return null;
    }
})(jQuery);