var currPage=1;
var pageSize=10;
var totalCount
var totalPage
var URL="/vulninfo/list";
var sidx="last_seen";
var order="desc";
////////////////////////////////
function load(){
    loadPage(URL,currPage,pageSize,sidx,order,addVulnInfo);
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
        item+=`<tr><td>${i+1}</td><td>${m.severity}</td>`;
        //vulnerability
        item+=`<td>${m.vulnerability}</td>`;
        //targetAddress
        item+=`<td>${m.targetAddress}</td>`;
        //confidence
        item+=`<td>${m.confidence}</td>`;
        //lastSeen
        item+=`<td>${formData(m.lastSeen)}</td>`;
        //导出文档
        item+=`<td><button type="button" class="btn btn-primary btn-sm">PDF</button></td></tr>`;
        $("#tablelist").html(item);
    })
}

//格式化时间
function formData(datetime) {
    var date=new Date(datetime);
    var form_date=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes();
    return form_date;
}