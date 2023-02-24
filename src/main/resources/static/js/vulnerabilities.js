var currPage=1;
var pageSize=3;
var totalCount
var totalPage
////////////////////////////////
function load(){
    loadPage(currPage,pageSize);
}
//分页查询
function loadPage(page,limit){
    let postdata={
        page:page,
        limit:limit,
        sidx:"last_seen",
        order:"desc"
    };
    $.post('/vulninfo/list',
        postdata,
        function(data){
            console.log(data.page);
            if(data.code==0){
                // 更新数据列表
                addVulnInfo(data.page);
                // 更新分页组件
                var $pagination = $('.pagination');
                var $pages = $pagination.find('.page-item:not(#pagePre,#pageNext)');
                $pages.removeClass('active');
                //更新
                if(page==1){
                    $('#pagePre').parent().addClass('disabled');
                    $('#page1').parent().addClass('active');
                }else if(page==totalPage){
                    $('#pageNext').parent().addClass('disabled');
                    $('#page3').parent().addClass('active');
                }else{
                    $('#page1').text(page-1);
                    $('#page2').text(page);
                    $('#page3').text(page+1);
                    $('#page2').parent().addClass('active');
                    $('#pagePre').parent().removeClass('disabled');
                    $('#pageNext').parent().removeClass('disabled');
                }
            }else{
                layer.msg(data.msg, {icon:2});
            }
        }
    );
}
//绑定上一页按钮点击事件
$('#pagePre').click(function(){
    let currentPage = parseInt($('.page-item.active a').text());
    if (currentPage > 1) {
      loadPage(currentPage - 1,pageSize);
    }
});
// 绑定下一页按钮点击事件
$('#pageNext').click(function(){
   let currentPage = parseInt($('.page-item.active a').text());
   if (currentPage < totalPage) {
     loadPage(currentPage + 1,pageSize);
   }
})
// 绑定页码按钮点击事件
$('.page-link:not(#pagePre,#pageNext)').click(function(e) {
    let page = parseInt($(this).text());
    loadPage(page,pageSize);
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