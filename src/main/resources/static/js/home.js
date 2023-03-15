var high_sever=0;
var low_sever=0;
var medium_sever=0;
var info_sever=0;
/////////////////////////////////////////////////////////////
function load(){
    $("#nav-placeholder").load("/navbar");
    $.get("/dashboard/severityCount",
        function(data){
            if(data.code==0){
                console.log(data.result);
                info_sever=data.result[0].number;
                low_sever=data.result[1].number;
                medium_sever=data.result[2].number;
                high_sever=data.result[3].number;
                echart();
            }
        }
    )
}
//
function echart(){
    // 基于准备好的dom，初始化echarts实例
    var chartDom = document.getElementById('myChart');
    var myChart = echarts.init(chartDom,null,{devicePixelRatio : 3});
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
