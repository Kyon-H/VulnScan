var currPage=1;
var pageSize=10;
var totalCount
var totalPage
var URL="/report/list";
var sidx="generation_date";
var order="desc";
////////////////////////////////
function load(){
    $("#nav-placeholder").load("/navbar");
    loadPage(URL,currPage,pageSize,sidx,order,addTable);
    setTimeout(function(){
            $('#home').parent().removeClass('active');
            $('#reports').parent().addClass('active');
        },100);
}

function addTable(data){
    currPage=data.currPage;
    pageSize=data.pageSize;
    totalCount=data.totalCount;
    totalPage=data.totalPage;
    var item="";
    $.each(data.list,function(i,m){
        item+=`<tr><td align="center">${i+1}</td><td>${m.templateName}</td><td>${m.listType}</td>
            <td>${formData(m.generationDate)}</td><td><div class="description" name="${m.id}">${m.description}</div></td>`;
        if(m.status=="processing"){
            item+=`<td><div name="${m.id}" class="d-flex align-items-center">
               <strong>processing...</strong>
               <div class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></div>
               </div></td>`;
            var data={
                id:m.id,
                reportId:m.reportId,
                action:"getReportStatus"
                };
            initWebSocket();
            setTimeout(function() {
              sendSock(data, function(backData) {
                if(backData.status!="processing"){
//                    window.location.reload();
                    $(`div[name="${backData.id}"].d-flex`).empty();
                    $(`div[name="${backData.id}"].d-flex`).append(backData.status);
                    $(`div[name="${backData.id}"].description`).empty();
                    $(`div[name="${backData.id}"].description`).append(backData.description);
                }
              });
            }, 1000); // 延时 1 秒钟调用 sendSock 方法
        }else{
            item+=`<td>${m.status}</td>`;
        }
        //download
        item+=`<td><a class="btn btn-success btn-sm" href="/report/download?id=${m.id}&type=html">HTML</a>
                   <a class="btn btn-primary btn-sm" href="/report/download?id=${m.id}&type=pdf">PDF</a></td>
            <td align="center"><button type="button" class="btn btn-danger btn-sm" data-id="${m.id}">删除</button></td></tr>`;
    });
    $("#tablelist").html(item);
}
//
$('#tablelist').delegate('td button.btn.btn-danger.btn-sm','click',function(){
    let id=$(this).data('id');
    layer.alert('确定要删除吗?',{icon:3,title:"提示"}, function(index){
      $.get('/report/delete/' + id,function(data){
        console.log(data);
        layer.msg(data.msg);
        if(data.code==0){
            setTimeout(function(){
                window.location.reload();
            },1000);
        }
      });
      layer.close(index);
    });
});
//格式化时间
function formData(datetime) {
    var date=new Date(datetime);
    var form_date=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes();
    return form_date;
}