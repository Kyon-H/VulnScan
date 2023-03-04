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
        item+=`<tr><td align="center">${i+1}</td><td>${m.templateName}</td><td>${m.listType}</td>
            <td>${formData(m.generationDate)}</td><td>${m.description}</td>`;
        if(m.status=="processing"){
            item+=`<td><div class="d-flex align-items-center">
               <strong>processing...</strong>
               <div class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></div>
               </div></td>`;
            var data={
                reportId:m.reportId,
                action:"getReportStatus"
                };
            var domain = document.domain;
            var wsurl="ws://"+domain+"/ws";
            initWebSocket(wsurl);
            sendSock(data,function(backdata){
                console.log("callback data: " + JSON.stringify(backdata));
                if(backdata.message!="processing"){
                    window.location.reload();
                }
            });
        }else{
            item+=`<td>${m.status}</td>`;
        }
        //download
        item+=`<td><a class="btn btn-success btn-sm" href="#">HTML</a> <a class="btn btn-primary btn-sm" href="#">PDF</a></td>
            <td align="center"><button type="button" class="btn btn-danger btn-sm">删除</button></td></tr>`;
    });
    $("#tablelist").html(item);
}
//格式化时间
function formData(datetime) {
    var date=new Date(datetime);
    var form_date=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes();
    return form_date;
}