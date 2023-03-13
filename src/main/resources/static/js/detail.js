function load(){
    $("#nav-placeholder").load("/navbar");
    let vulninfo_id=$.getUrlParam("id");
    $.get("/vulninfo/detail/"+vulninfo_id,
        function(data){
            if(data.code==0){
                console.log(data.detail);
                addDetail(data.detail);
            }else{
                layer.msg(data.msg,{icon:2});
            }
        }
    );
    setTimeout(function(){
            $('#home').parent().removeClass('active');
            $('#vulnerabilities').parent().addClass('active');
    },100);
}

function addDetail(data){
    //漏洞名称
    $('#vt_name').text(data.vt_name);
    //漏洞描述
    $('#description').append(data.description);
    $('#affects_url').text(data.affects_url);
    //HTTP Request
    $('#request').text(data.request);
    //该漏洞的影响
    $('#impact').append(data.impact);
    //修复建议
    $('#recommendation').append(data.recommendation);
    //HTTP Response
    $('#response').text(data.http_response);
    //详细描述
    if(data.long_description==""){
        $('#collapseDetailedInformation').removeClass('show');
    }
    $('#long_description').append(data.long_description);
    //攻击详情
    $('#details').append(data.details);
    var item="";
    //参考资料
    $.each(data.references,function(i,m){
        item+=`<a href=${m.href}>${m.rel}</a><br/>`;
    })
    $('#references').html(item);
    //漏洞分类
    //cwe
    $('#cwe_class').append(data.tags[0]+" : "+data.tags[1]+" "+data.tags[2]);
    //cvss3
    var cvss3=`Base Score : <strong>${data.cvss_score}</strong> -
        <a href="https://www.first.org/cvss/calculator/3.0#${data.cvss3}" target="_blank">${data.cvss3}</a>`;
    $('#cvss3_class').append(cvss3);
    //cvss2
    var cvss2=`${data.cvss2}`;
    $('#cvss2_class').append(cvss2);
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