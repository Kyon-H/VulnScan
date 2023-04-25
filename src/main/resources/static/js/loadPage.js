
//分页查询
function loadPage(URL,options,callback){
    $.post(URL,
        options,
        function(data){
            console.log(data.page);
            if(data.code==0){
                // 更新分页组件
                var $pagination = $('.pagination');
                var $pages = $pagination.find('.page-item:not(#pagePre,#pageNext)');
                $pages.removeClass('active');
                $pages.removeClass('disabled');
                let page=options.page;
                if(data.page.totalPage<=2){
                    $('#pagePre').parent().addClass('disabled');
                    $('#pageNext').parent().addClass('disabled');
                    if(data.page.totalPage==1){
                        $('#page1').parent().addClass('active');
                        $('#page2').parent().addClass('disabled');
                        $('#page3').parent().addClass('disabled');
                    }else if(data.page.totalPage==2){
                        $('#page3').parent().addClass('disabled');
                        $pages.eq(page).addClass('active');
                    }
                }else{
                    //更新
                    if(page==1){
                        $('#pagePre').parent().addClass('disabled');
                        $('#page1').parent().addClass('active');
                    }else if(page==data.page.totalPage){
                        if(page>1)
                            $('#pagePre').parent().removeClass('disabled');
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
                }
                callback(data.page);
            }else{  //data.code!=0
                layer.msg(data.msg, {icon:2});
            }
        }
    );
}
//绑定上一页按钮点击事件
$('#pagePre').click(function(){
    if (options.page > 1) {
        options.page=options.page-1;
        loadPage(URL,options,addTable);
    }
});
// 绑定下一页按钮点击事件
$('#pageNext').click(function(){
   if (options.page < options.totalPage) {
       options.page=options.page+1;
       loadPage(URL,options,addTable);
   }
})
// 绑定页码按钮点击事件
$('.page-link:not(#pagePre,#pageNext)').click(function(e) {
    let page=parseInt($(this).text());
    if(options.totalPage>=page){
        options.page=page;
        loadPage(URL,options,addTable);
    }
});
