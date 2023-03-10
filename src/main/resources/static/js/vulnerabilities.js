var currPage=1;
var pageSize=10;
var totalCount
var totalPage
var URL="/vulninfo/list";
var sidx="last_seen";
var order="desc";
////////////////////////////////
function load(){
    $("#nav-placeholder").load("/navbar");
//    $.getScript("/js/navbar.js",function(){console.log("导入script成功");});
    let severity=$.getUrlParam("severity");
    let scanRecordId=$.getUrlParam("scan_record_id");
    if(scanRecordId){
        URL+="/"+scanRecordId;
        if(severity){
            URL+="/"+severity;
        }
    }
    loadPage(URL,currPage,pageSize,sidx,order,addTable);
    setTimeout(function(){
        $('#home').parent().removeClass('active');
        $('#vulnerabilities').parent().addClass('active');
    },100);
}
//
function addTable(data){
    currPage=data.currPage;
    pageSize=data.pageSize;
    totalCount=data.totalCount;
    totalPage=data.totalPage;
    let item="";
    console.log("currPage:"+currPage);
    $.each(data.list,function(i,m){
        //# address
        item+=`<tr><td>${(currPage-1)*pageSize+1+i}</td>`;
        //severity
        switch(m.severity){
            case 0:
                item+=`<td><i class="bi bi-bug-fill" style="color:ForestGreen" title="informational"></i></td>`;
                break;
            case 1:
                item+=`<td><i class="bi bi-bug-fill" style="color:cornflowerblue" title="low"></i></td>`;
                break;
            case 2:
                item+=`<td><i class="bi bi-bug-fill" style="color:GoldenRod" title="medium"></i></td>`;
                break;
            case 3:
                item+=`<td><i class="bi bi-bug-fill"style="color:Crimson" title="high"></i></td>`;
                break;
        }
        //vulnerability
        item+=`<td><a href="/ActiveScan/vulnerabilities/detail?id=${m.id}" title="${m.vulnerability}">${m.vulnerability}</a></td>`;
        //targetAddress
        item+=`<td><span title="${m.targetAddress}">${m.targetAddress}</span></td>`;
        //confidence
        item+=`<td>${m.confidence}</td>`;
        //lastSeen
        item+=`<td>${formData(m.lastSeen)}</td>`;
        //导出文档
        item+=`<td><button type="button" class="btn btn-primary btn-sm" data-toggle="modal"
            data-target="#reportModal" aria-controls="myCollapse" data-whatever="@mdo"
            data-id=${m.vulnId}>生成报告</button></td></tr>`;
        $("#tablelist").html(item);
    })
}
//report
$("#tablelist").delegate("td button.btn-primary","click",function(){
    $("#vuln_id").attr("value",$(this).data("id"));
})
$("#reportSubmitBtn").click(function(){
    let vulnId=$("#vuln_id").val();
    let templateId=$("#template_id").val();
    console.log(vulnId)
    console.log(templateId)
    let postdata={
        "templateId":templateId,
        "listType":"vulnerabilities",
        "idList":vulnId
    }
    //submit
    $.ajax({
      url: '/report/save',
      type: 'POST',
      data: JSON.stringify(postdata),  // 将 JavaScript 对象转换为 JSON 字符串
      contentType: 'application/json',
      dataType: 'json',
      success: function(data) {
        console.log(data);
        if(data.code==200||data.code==0){
            window.location.href="/ActiveScan/reports";
        }else{
            layer.msg(data.msg, {icon: 2});
        }
      }
    });
})
//格式化时间
function formData(datetime) {
    var date=new Date(datetime);
    var form_date=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes();
    return form_date;
}
//getUrlParam
(function ($) {
    $.getUrlParam = function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return decodeURI(r[2]);
        return null;
    }
})(jQuery);