function homeCtrl($scope, $http, $rootScope,$timeout) {
    //滚动置顶
    window.scrollTo(0, 0);
    $scope.param={};
    $scope.param.type="daily";
    $scope.sum=0;

    $scope.changeType =function(type){
        $scope.param.type=type;
        $scope.EquCount();
    };
	$scope.cmp = !$rootScope.user ? true : ($rootScope.user.competence == 2 ? false : true) ;

    $scope.home={
        'equAll':0
    };

    $scope.alarmSunCount=0;

    $scope.fmtDate = function(date){
        var y = 1900+date.getYear();
        var m = "0"+(date.getMonth()+1);
        var d = "0"+date.getDate();
        return y+"-"+m.substring(m.length-2,m.length)+"-"+d.substring(d.length-2,d.length);
    }
    $scope.param.end =$scope.fmtDate(new Date());
    var date = new Date();//获取当前时间
    date.setDate(date.getDate()-7);//设置天数 -7天
    $scope.param.start=$scope.fmtDate(date);

    //时间组件
    $("#datepickerStrat1"). datepicker().on('changeDate', function () {
        $scope.param.start=$("#datepickerStrat1").val();
        $scope.EquCount();
    });
    $("#datepickerEnd1"). datepicker().on('changeDate', function () {
        $scope.param.end =$("#datepickerEnd1").val();
        $scope.EquCount();
    });

    //图表初始化-- 设备新增数
    var dom = document.getElementById("website-stats");
    var myChart = echarts.init(dom);
    var equOption = {
        color: ['#3398DB'],
        tooltip : {
            trigger: 'item',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            //formatter: '新增了{c0}个设备'
            formatter:function (params ,ticket) {
                return $scope.fmtDate(addDate(new Date($scope.param.start),params.dataIndex))+($scope.param.type == 'sum'?" 累计总数为":" 新增")+params.value+"个";
            }
        },
        title: {
            left: 'center',
            text: '设备数量走势图',
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                data : [],
                axisTick: {
                    alignWithLabel: true
                },
                axisLabel:{
                    formatter: function (value, index) {
                       return $scope.fmtDate(addDate(new Date($scope.param.start),index));
                       // return value+"个";
                    }
                }
            }
        ],
        yAxis : [
            {
                type : 'value',
                minInterval: 1
            }
        ],
        series : [
            {
                name:'设备新增',
                type:'line',
               // barWidth: '60%',
                data:[]
            }
        ]
    };


    //图表初始化-- 设备新增数
    var pieDoc = document.getElementById("pie-chart-container");
    var pieChart = echarts.init(pieDoc);
    var pieOption = {
        title : {
            text: '设备统计图',
            x:'left'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{b} : {c} ({d}%)"
        },
        color:[ '#7e57c2', '#58c9c7', '#95cde4'],
        series : [
            {
                type:'pie',
                radius : '55%',
               // center: ['40%', '50%'],
                label: {
                    formatter: '{b}({d}%)'
                },
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                },
                data:[
                    {value:0, name:'GPRS'},
                    {value:0, name:'LORA'},
                    {value:0, name:'NBIOT'}

                ]
            }
        ]
    };

    $scope.EquCount=function () {
        $http.post("service/getAddEquCount",$scope.param).success(function(data) {
            equOption.series[0].data=[];
            equOption.xAxis[0].data=[];
            var dayff=datedifference(new Date($scope.param.end),new Date($scope.param.start))
            for(var i=0;i<=dayff;i++){
                equOption.xAxis[0].data[i]=i;
                equOption.series[0].data[i]=0;
            }
            if(data.resultObj.log && data.resultObj.log.length >0){
                for(var i=0;i<data.resultObj.log.length;i++){
                  equOption.series[0].data[data.resultObj.log[i].times] = data.resultObj.log[i].count;
                }
                if("sum" == $scope.param.type){
                    var dataValue=0;
                    var dataSum =data.resultObj.sum;
                    for(var i=0;i<=dayff;i++){
                        dataValue= dataValue + equOption.series[0].data[i];
                        equOption.series[0].data[i]=dataSum+dataValue;
                    }
                }

            };
            myChart.setOption(equOption);
        });
    };
    $scope.EquCount();

    $http.get("service/getHomePie").success(function(data) {
		$scope.home=data.resultObj;
        for(var i=0;i<data.resultObj.pieMap.length;i++){
            pieOption.series[0].data[i].value = data.resultObj.pieMap[i];
           // pieOption.series[0].data[i].name =  pieOption.series[0].data[i].name +" - "+ data.resultObj.pieMap[i]/;
        }
        pieChart.setOption(pieOption);
	});

    //获取当年第一天
     function getFirstDayOfYear (date) {
             date.setDate(1);
             date.setMonth(0);
             return date;
     }

     //加上天数
     function addDate(date , number) {
         date.setDate(date.getDate() + number);
         return date;
     }

    function datedifference(sDate1, sDate2) {    //sDate1和sDate2是2006-12-18格式
        var dateSpan,
            iDays;
        sDate1 = Date.parse(sDate1);
        sDate2 = Date.parse(sDate2);
        dateSpan = sDate2 - sDate1;
        dateSpan = Math.abs(dateSpan);
        iDays = Math.floor(dateSpan / (24 * 3600 * 1000));
        return iDays
    };
}