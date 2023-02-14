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
                $("#myModal").modal("dispose");
            }else{
                layer.msg(data.msg, {icon: 2});
            }
        },'json'
    );
});

function load(){
    $.get('/scan/list',
        function(data){
            console.log(data.page.list);
            if(data.code==200||data.code==0){
                addTable(data.page.list);
            }else{
                layer.msg(data.msg, {icon: 2});
            }
        }
    );
}

//获取扫描记录
function addTable(data){
    var count=0;
    var item="";
    $.each(data,function (i,m) {
        item+=`<tr><td>${i+1}</td><td>${m.address}</td><td>`;
        if(m.description==""){
            m.description="无";
        }
        item+=`${m.description}</td><td>`;
        //
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
        item+=`</td><td>${m.severityCounts}</td><td>`;
        item+=formData(m.scanTime);
        //
        if(m.status=="running"){
            item+=`</td><td><div class="d-flex align-items-center">
                              <strong>running...</strong>
                              <div class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></div>
                            </div>`;
        }else{
            item+=`</td><td>${m.status}</td>`;
        }
        //
        item+=`<td><button type="button" class="btn btn-primary btn-sm">PDF</button></td>
        <td><button type="button" class="btn btn-danger btn-sm">删除</button></td></tr>`;
        if(m.status=="r"){
            var progress=`<tr><td colspan="9" style="padding:0">
                <div class="progress">
                <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar"
                aria-valuenow="75" aria-valuemin="0" aria-valuemax="100" style="width: 75%"></div>
                </div></td></tr>`;
            item+=progress;
        }
    });
    $("#tablelist").html(item);
}
//格式化时间
function formData(datetime) {
    var date=new Date(datetime);
    var form_date=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes();
    return form_date;
}
