var currPage=1;
var pageSize=10;
var totalCount
var totalPage
var URL="/report/list";
var sidx="generation_date";
var order="desc";
////////////////////////////////
function load(){
    loadPage(URL,currPage,pageSize,sidx,order,addTable);
}
//绑定上一页按钮点击事件
$('#pagePre').click(function(){
    let currentPage = parseInt($('.page-item.active a').text());
    if (currentPage > 1) {
      loadPage(URL,currentPage - 1,pageSize,sidx,order,addTable);
    }
});
// 绑定下一页按钮点击事件
$('#pageNext').click(function(){
   let currentPage = parseInt($('.page-item.active a').text());
   if (currentPage < totalPage) {
     loadPage(URL,currentPage + 1,pageSize,sidx,order,addTable);
   }
})
// 绑定页码按钮点击事件
$('.page-link:not(#pagePre,#pageNext)').click(function(e) {
    let page = parseInt($(this).text());
    loadPage(URL,page,pageSize,sidx,order,addTable);
});
function addTable(data){
    currPage=data.currPage;
    pageSize=data.pageSize;
    totalCount=data.totalCount;
    totalPage=data.totalPage;
    var item="";
    $.each(data.list,function(i,m){

    });
    $("#tablelist").html(item);
}
//格式化时间
function formData(datetime) {
    var date=new Date(datetime);
    var form_date=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes();
    return form_date;
}