
//分页查询
function loadPage(URL,page=1,limit=10,sidx,order="desc",callback){
    let postdata={
        page:page,
        limit:limit,
        sidx:sidx,
        order:order
    };
    $.post(URL,
        postdata,
        function(data){
            console.log(data.page);
            if(data.code==0){
                // 更新分页组件
                var $pagination = $('.pagination');
                var $pages = $pagination.find('.page-item:not(#pagePre,#pageNext)');
                $pages.removeClass('active');
                //更新
                if(page==1){
                    $('#pagePre').parent().addClass('disabled');
                    $('#page1').parent().addClass('active');
                }else if(page==data.page.totalPage){
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
                callback(data.page);
            }else{
                layer.msg(data.msg, {icon:2});
            }
        }
    );
}

