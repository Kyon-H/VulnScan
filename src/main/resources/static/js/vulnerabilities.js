var currPage=1;
var pageSize=10;
var totalCount
var totalPage
var URL="/vulninfo/list";
var sidx="last_seen";
var order="desc";
////////////////////////////////
function load(){
    let severity=$.getUrlParam("severity");
    let scanRecordId=$.getUrlParam("scan_record_id");
    var params={
        scan_record_id:scanRecordId,
        severity:severity,
    };

    loadPage(URL,currPage,pageSize,sidx,order,addVulnInfo,params);
}
//绑定上一页按钮点击事件
$('#pagePre').click(function(){
    let currentPage = parseInt($('.page-item.active a').text());
    if (currentPage > 1) {
      loadPage(URL,currentPage - 1,pageSize,sidx,order,addVulnInfo);
    }
});
// 绑定下一页按钮点击事件
$('#pageNext').click(function(){
   let currentPage = parseInt($('.page-item.active a').text());
   if (currentPage < totalPage) {
     loadPage(URL,currentPage + 1,pageSize,sidx,order,addVulnInfo);
   }
})
// 绑定页码按钮点击事件
$('.page-link:not(#pagePre,#pageNext)').click(function(e) {
    let page = parseInt($(this).text());
    loadPage(URL,page,pageSize,sidx,order,addVulnInfo);
});
//
function addVulnInfo(data){
    currPage=data.currPage;
    pageSize=data.pageSize;
    totalCount=data.totalCount;
    totalPage=data.totalPage;
    let item="";
    console.log("currPage:"+currPage);
    $.each(data.list,function(i,m){
        //# address
        item+=`<tr><td align="center">${i+1}</td>`;
        //severity
        switch(m.severity){
            case 0:
                item+=`<td align="center"><i class="bi bi-bug-fill" style="color:ForestGreen" title="informational"></i></td>`;
                break;
            case 1:
                item+=`<td align="center"><i class="bi bi-bug-fill" style="color:cornflowerblue" title="low"></i></td>`;
                break;
            case 2:
                item+=`<td align="center"><i class="bi bi-bug-fill" style="color:GoldenRod" title="medium"></i></td>`;
                break;
            case 3:
                item+=`<td align="center"><i class="bi bi-bug-fill"style="color:Crimson" title="high"></i></td>`;
                break;
        }
        //vulnerability
        item+=`<td><a href="/ActiveScan/vulnerabilities/detail?id=${m.id}">${m.vulnerability}</a></td>`;
        //targetAddress
        item+=`<td>${m.targetAddress}</td>`;
        //confidence
        item+=`<td align="center">${m.confidence}</td>`;
        //lastSeen
        item+=`<td>${formData(m.lastSeen)}</td>`;
        //导出文档
        item+=`<td align="center"><button type="button" class="btn btn-primary btn-sm">PDF</button></td></tr>`;
        $("#tablelist").html(item);
    })
}

$('#tablelist tr').click(function(){
    var id=this.id;
    alert(id);
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