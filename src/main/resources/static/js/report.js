//
var URL="/report/list";
const options={
    page: 1,
    limit: 10,
    sidx: 'generation_date',
    order: 'desc',
    totalCount: 0,
    totalPage: 0,
    templateId: undefined,
    listType: undefined,
    date: undefined
}
////////////////////////////////
function load(){
    $("#nav-placeholder").load("/navbar");
    loadPage(URL,options,addTable);
    setTimeout(function(){
            $('#home').parent().removeClass('active');
            $('#reports').parent().addClass('active');
        },100);
}
//loadPage callback方法
function addTable(data){
    options.page=data.currPage;
    options.limit=data.pageSize;
    options.totalCount=data.totalCount;
    options.totalPage=data.totalPage;
    var item="";
    $.each(data.list,function(i,m){
        item+=`<tr><td align="center">${(options.page-1)*options.limit+1+i}</td>
            <td>${m.templateName}</td><td>${m.listType}</td>
            <td>${formData(m.generationDate)}</td>
            <td><div class="description" name="${m.id}">${m.description}</div></td>`;
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
//报告模板select
$('#template_id').change(function(){
    //获取option元素上的value值
    var template_id = $(this).find('option:selected').val();
    console.log(template_id);
    options.templateId=template_id;
    if(template_id==""){
        options.templateId=undefined;
    }
    loadPage(URL, options, addTable);
})
//报告类型select
$('#list_type').change(function(){
    var list_type = $(this).find('option:selected').val();
    console.log(list_type)
    options.listType=list_type;
    if(list_type==""){
        options.listType=undefined;
    }
    loadPage(URL, options, addTable);
})
//生成时间
laydate.render({
  elem: '#time', //指定元素
  done: function(value, date, endDate){
    options.date=value;
    console.log(value)
    if(value==""){
        options.date=undefined;
    }
    loadPage(URL, options, addTable);
  }
});
//删除按钮click事件
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
