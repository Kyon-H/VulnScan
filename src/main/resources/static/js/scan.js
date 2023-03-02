var currPage=1;
var pageSize=10;
var totalCount
var totalPage
var URL="/scan/list";
var sidx="scan_time";
var order="desc";
////////////////////////////////
$("#addTargetBtn").on("click", function () {
    var address=$("#targetUrl").val();
    //var pattern = /(https?)://[a-zA-z]+://[^\s]*/

});

$("#scanSubmitBtn").on("click", function () {
    var address=$("#address").val();
    console.log(address)
    var scanType=$("#ScanType").val();
    console.log(scanType)
    var scanSpeed=$("input[name=customRadioInline]:checked").val();
    var description=$("#message-text").val();
    console.log(scanSpeed)
    var username=$("#userName").val();
    var formData={
        address:address,
        scanType:scanType,
        scanSpeed:scanSpeed,
        description:description
    };
    $.post('/scan/save',
        formData,
        function(data){
            console.log(data);
            if(data.code==200||data.code==0){
                layer.msg("添加描成功", {icon: 1});

                $("#myModal").modal("hide");
                load();
            }else{
                layer.msg(data.msg, {icon: 2});
            }
        },'json'
    );
});
//默认查询，page:1,limit:10
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
//获取扫描记录
function addTable(data){
    currPage=data.currPage;
    pageSize=data.pageSize;
    totalCount=data.totalCount;
    totalPage=data.totalPage;
    var item="";
    $.each(data.list,function (i,m) {
        //# address
        item+=`<tr><td align="center">${i+1}</td><td><a href="/ActiveScan/vulnerabilities?scan_record_id=${m.id}">${m.address}</a></td>`;
        if(m.description==""){
            m.description="无";
        }
        //description
        item+=`<td>${m.description}</td>`;
        //type
        item+="<td>";
        switch(m.type){
            case '11111111-1111-1111-1111-111111111111':
                item+="Full Scan";break;
            case '11111111-1111-1111-1111-111111111112':
                item+="High Risk";break;
            case '11111111-1111-1111-1111-111111111113':
                item+="SQL Injection";break;
            case '11111111-1111-1111-1111-111111111115':
                item+="Weak Passwords";break;
            case '11111111-1111-1111-1111-111111111116':
                item+="Cross Site Scripting";break;
            default:
                item+=m.type;break;
        }
        item+="</td>";
        //扫描结果分布
        let counts=m.severityCounts;
        item+=`<td>
            <a href="/ActiveScan/vulnerabilities?severity=3&scan_record_id=${m.id}" class="badge badge-danger">${counts.high}</a>
            <a href="/ActiveScan/vulnerabilities?severity=2&scan_record_id=${m.id}" class="badge badge-warning">${counts.medium}</a>
            <a href="/ActiveScan/vulnerabilities?severity=1&scan_record_id=${m.id}" class="badge badge-primary">${counts.low}</a>
            <a href="/ActiveScan/vulnerabilities?severity=0&scan_record_id=${m.id}" class="badge badge-success">${counts.info}</a>
        </td>`;
        //添加时间
        item+="<td>"+formData(m.scanTime)+"</td>";
        //扫描状态
        if(m.status=="processing"){
            item+=`<td><div class="d-flex align-items-center">
                   <strong>processing...</strong>
                   <div class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></div>
                   </div></td>`;
            var data={
                id:m.id,
                targetId:m.targetId,
                action:"getStatus"
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
        //
        item+=`<td align="center"><button type="button" class="btn btn-primary btn-sm">PDF</button></td>
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

