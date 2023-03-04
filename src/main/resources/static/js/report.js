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

function addTable(data){
    currPage=data.currPage;
    pageSize=data.pageSize;
    totalCount=data.totalCount;
    totalPage=data.totalPage;
    var item="";
    console.log(data);
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