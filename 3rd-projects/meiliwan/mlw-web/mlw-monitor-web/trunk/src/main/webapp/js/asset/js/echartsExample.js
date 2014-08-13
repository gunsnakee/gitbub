var myChart;
var domCode = document.getElementById('sidebar-code');
var domGraphic = document.getElementById('graphic');
var domMain = document.getElementById('main');
var domMessage = document.getElementById('wrong-message');
var iconResize = document.getElementById('icon-resize');
var needRefresh = false;

var mapData=[];
$("#requestMapData input").each(function(i){
    
    var name = $(this).attr("name");
     var val = $(this).val();
     var city={};
     city.name=name;
     city.value=val*1;
    mapData[i]=city;


});


var  option = {
    title : {
        text: '美丽湾访问分布',
        x:'center'
    },
    tooltip : {
        trigger: 'item'
    },
    dataRange: {
        min: 0,
        max: $("#requestMapMax").val(),
        text:['高','低'],           // 文本，默认为数值文本
        calculable : true,
        textStyle: {
            color: 'orange'
        }
    },
    toolbox: {
        show : true,
        orient : 'vertical',
        x: 'right',
        y: 'center',
        feature : {
            mark : true,
            dataView : {readOnly: false},
            restore : true,
            saveAsImage : true
        }
    },
    series : [
        {
            name: 'request',
            type: 'map',
            mapType: 'china',
            itemStyle:{
                normal:{label:{show:true}, color:'#ffd700'},// for legend
                emphasis:{label:{show:true}}
            },
            data:mapData
        }
    ]
};
function refresh(isBtnRefresh){
    if (isBtnRefresh) {
        needRefresh = true;
        focusGraphic();
        return;
    }
    needRefresh = false;
    
    myChart.setOption(option, true);

}

function needMap() {
    var href = location.href;
    return href.indexOf('map') != -1
           || href.indexOf('mix3') != -1
           || href.indexOf('mix5') != -1;

}

var echarts;
var developMode = true;

if (developMode) {
    // for develop
    require.config({
        packages: [
            {
                name: 'echarts',
                location: '/js/src',
                main: 'echarts'
            },
            {
                name: 'zrender',
                location: 'http://ecomfe.github.io/zrender/src',
                //location: '../../../zrender/src',
                main: 'zrender'
            }
        ]
    });
}
else {
    // for echarts online home page
    var fileLocation = needMap() ? './www/js/echarts-map' : './www/js/echarts';
    require.config({
        paths:{ 
            echarts: fileLocation,
            'echarts/chart/line': fileLocation,
            'echarts/chart/bar': fileLocation,
            'echarts/chart/scatter': fileLocation,
            'echarts/chart/k': fileLocation,
            'echarts/chart/pie': fileLocation,
            'echarts/chart/radar': fileLocation,
            'echarts/chart/map': fileLocation,
            'echarts/chart/chord': fileLocation,
            'echarts/chart/force': fileLocation
        }
    });
}

// 按需加载
require(
    [
        'echarts',
        'echarts/chart/line',
        'echarts/chart/bar',
        'echarts/chart/scatter',
        'echarts/chart/k',
        'echarts/chart/pie',
        'echarts/chart/radar',
        'echarts/chart/force',
        'echarts/chart/chord',
        needMap() ? 'echarts/chart/map' : 'echarts'
    ],
    requireCallback
);

function requireCallback (ec) {
    echarts = ec;
    if (myChart && myChart.dispose) {
        myChart.dispose();
    }
    myChart = echarts.init(domMain);
    refresh();
    window.onresize = myChart.resize;
}