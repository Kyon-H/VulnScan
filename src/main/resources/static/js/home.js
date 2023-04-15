
function load(){
    $("#nav-placeholder").load("/navbar");
    $.get("/dashboard/severityCount",
        function(data){
            if(data.code==0){
                console.log(data.result);
                echart(data.result);
            }
        }
    )
    $.get("/dashboard/mostTarget",
        function(data){
            if(data.code==0){
                console.log(data.result);
                mostTarget(data.result);
            }
        }
    )
    $.get("/dashboard/topVuln",
        function(data){
            if(data.code==0){
                console.log(data.result);
                topVulnerability(data.result);
            }
        }
    )
}
//
function echart(data){
    var info_sever=data[0].number;
    var low_sever=data[1].number;
    var medium_sever=data[2].number;
    var high_sever=data[3].number;
    // 基于准备好的dom，初始化echarts实例
    var chartDom = document.getElementById('myChart');
    var myChart = echarts.init(chartDom,null,{devicePixelRatio : 4});
    // 指定图表的配置项和数据
    var option = {
        title: {
          text: 'Vulnerabilities',
          left: 'center',
          top: 'center'
        },
        series: [
          {
            type: 'pie',
            data: [
                {value: low_sever, name: 'Low Severity Vulnerabilities'},
                {value: info_sever, name: 'Info Severity Vulnerabilities'},
                {value: medium_sever, name: 'Medium Severity Vulnerabilities'},
                {value: high_sever, name: 'High Severity Vulnerabilities'}
            ],
            radius: ['40%', '70%']
          }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}
function mostTarget(data) {
    var item="";
    $.each(data,function (i,m) {
        item+=`<tr><td>${m.address}</td></tr>`;
    });
    $("#mostTargetList").html(item);
}
function topVulnerability(data) {
    var item="";
    $.each(data,function (i,m) {
        item+=`<tr><td>${m.vulnerability}</td><td><p class="badge badge-danger">${m.count}</p></td></tr>`;
    });
    $("#topVulnList").html(item);
}