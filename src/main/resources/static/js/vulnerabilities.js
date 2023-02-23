function load(){
    $.post("/vulninfo/list",
        function(data){
            if(data.code == 0){
                //success
                console.log(data.page);
                addVulnInfo(data.page);
            }else{
                layer.msg(data.msg);
            }
        }
    )
}
//
function addVulnInfo(data){
    let pageSize=data.pageSize;
    let totalCount=data.totalCount;
    let totalPage=data.totalPage;
    let item="";
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