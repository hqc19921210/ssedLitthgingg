/**
 * Created by heqichao on 2018-11-29.
 */

function logShowCtrl($scope, $http, $rootScope,$routeParams,$location) {
    //滚动置顶
    window.scrollTo(0, 0);

    $scope.pages=1;
    //当前页
    $scope.curpage=1;
    //记录
    $scope.log=new Array();
    $scope.logArr =new Array();
    $scope.param={};
    $scope.param.devId="";
    $scope.param.attrId="";
    $scope.attrKey="";
    if($routeParams){
        $scope.param.devId=$routeParams.devId;
        $scope.param.attrId=$routeParams.attrId;
        //初始化选择
        //只有第一次从列表跳入时初始化
        $scope.param.initOption='TRUE';
    }

    $scope.changePage =function(page){
        $scope.curpage=page;
        $scope.logArr =new Array();
        for (var i =0; i < defaultSize; i++) {
        	if((page-1)*defaultSize+i>=$scope.log.length){
                return;
            }
            $scope.logArr.push($scope.log[(page-1)*defaultSize+i]);
        }
    }


    $scope.fmtDate = function(date){
        var y = 1900+date.getYear();
        var m = "0"+(date.getMonth()+1);
        var d = "0"+date.getDate();
        return y+"-"+m.substring(m.length-2,m.length)+"-"+d.substring(d.length-2,d.length);
    }
    //设备用户名
    $scope.equipUserName ="";
    $scope.devList=new Array();
    $scope.attrList=new Array();
    //图型数据
    $scope.plotDownloads =new Array();
    $scope.attrType="";

    $scope.param.end =$scope.fmtDate(new Date());
    var date = new Date();//获取当前时间
    date.setDate(date.getDate()-30);//设置天数 -30天
    $scope.param.start=$scope.fmtDate(date);

    //设备信息
    $scope.equip={};


    $scope.unit="";

    //时间组件
    $("#datepickerStrat"). datepicker().on('changeDate', function () {
        $scope.param.start=$("#datepickerStrat").val();
        $scope.init();
    });
    $("#datepickerEnd"). datepicker().on('changeDate', function (e) {
        $scope.param.end =$("#datepickerEnd").val();
        $scope.init();
    });
    
    // 百度地图API功能
    $scope.getMap=function () {
        var point={};
        if ($scope.equip.site) {
        	var position = $scope.equip.site.split(',');
            point = new BMap.Point(position[0], position[1]);
        }else {
            point=new BMap.Point(114.070855, 22.551052);
        }
        var map = new BMap.Map("map");    // 创建Map实例
        map.centerAndZoom(point, 11);  // 初始化地图,设置中心点坐标和地图级别
        map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
        map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
        var marker = new BMap.Marker(point);  // 创建标注
        map.addOverlay(marker);               // 将标注添加到地图中

    };
    //图表初始化
    var dom = document.getElementById("container");
    var myChart = echarts.init(dom);
    option = {
        tooltip: {
            trigger: 'axis',
            position: function (pt) {
                return [pt[0], '10%'];
            }
        },
        title: {
            left: 'center',
            text: '历史数据点',
        },
        toolbox: {
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                restore: {},
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%']
        },
        dataZoom: [ 
        	//start 滚轮缩放 
        	{
	            type: 'inside',
	            start: 0,//组件起始位置（影响下方区域组件）
	            end: 100//组件结束位置（影响下方区域组件）
	        },
	      //end 滚轮缩放 
	      //下方区域组件
        {
	        
            start: 0,//组件起始位置
            end: 100,//组件结束位置
            handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
            handleSize: '50%',
            handleStyle: {
                color: '#fff',
                shadowBlur: 3,
                shadowColor: 'rgba(0, 0, 0, 0.6)',
                shadowOffsetX: 2,
                shadowOffsetY: 2
            }
        }],
        series: [
            {
                name:'数据值',
                type:'line',
                smooth:true,
                symbol: 'none',
                sampling: 'average',
                itemStyle: {
                    color: 'rgb(49, 158, 203)'
                },
//                data: data 注释等待动态加载数据
            }
        ]
    };
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
    
    
    //初始化数据
    $scope.init=function(){
    	myChart.showLoading();
        $http.post("/service/queryEquAttrLog",$scope.param).success(function(data) {
            $scope.param.initOption='FALSE';
            if(data.resultObj.devList){
                $scope.devList = data.resultObj.devList;
            }
            $scope.param.devId=data.resultObj.devId;
            $scope.param.attrId=""+data.resultObj.attrId;
           if(data.resultObj.attrList){
               $scope.attrList= data.resultObj.attrList;
           }
            if(data.resultObj.dataType){
                $scope.attrType=data.resultObj.dataType;
            }
            if(data.resultObj.unit){
                $scope.unit=data.resultObj.unit;
            }

            if(data.resultObj.attrKey){
                $scope.attrKey=data.resultObj.attrKey;
            }

            if(data.resultObj.log){
                $scope.log=data.resultObj.log;
                if($scope.log && $scope.log.length>0){
                    $scope.pages =Math.ceil($scope.log.length/defaultSize);
                    $scope.changePage(1);
                }
                $scope.showChart();
            }
            if(data.resultObj.equip){
                $scope.equip=data.resultObj.equip;
                $scope.getMap();
            }
            if(data.resultObj.equipUserName) {
                //设备用户名
                $scope.equipUserName = data.resultObj.equipUserName
            }
        });
    };

    //初始化
    $scope.init();

    $scope.changeDevId=function () {
        $scope.param.attrId="";
        $scope.init();
    }

    $scope.changeAttr=function(){
        for(var i=0;i<$scope.attrList.length;i++){
            if($scope.param.attrId == $scope.attrList[i].id){
                $scope.attrType=$scope.attrList[i].dataType;
                $scope.unit=$scope.attrList[i].unit;
                $scope.attrKey=$scope.attrList[i].attrName;
                $scope.init();
                break;
            }
        }
    }

    $scope.asynLinePointData=function(){
    	console.log("plotDownloads",angular.fromJson(angular.toJson($scope.plotDownloads)));
    	console.log("attrKey",angular.fromJson(angular.toJson($scope.param.attrKey)));
    }

    $scope.showChart=function(){
        $scope.plotDownloads =[];
        //非波形
        if( $scope.attrType != "WAVE_TYPE"){
            for(var j=0;j<$scope.log.length;j++){
                var obj =$scope.log[j];
                var value =obj.dataValue;
                $scope.plotDownloads.push({value:[Date.parse(new Date(obj.addDate)),value]});
            }
        }else{
            //默认显示第一个波形
            $scope.showWave($scope.log[0]);
        }
        $scope.asynLinePointData();
//        $.Dashboard.init();
    }

    $scope.showWave=function (obj) {
        $scope.plotDownloads=[];
        if(obj && $scope.attrType == "WAVE_TYPE"){
            var values=obj.dataValue;
            var arr = values.split(",");
            for(var i=0;i<arr.length;i++){
                $scope.plotDownloads.push([i,arr[i]]);
            }
            $scope.asynLinePointData();
//            $.Dashboard.init();
        }
    }

    function formatDateTime(inputTime) {
        var date = new Date(inputTime);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = date.getHours();
        h = h < 10 ? ('0' + h) : h;
        var minute = date.getMinutes();
        var second = date.getSeconds();
        minute = minute < 10 ? ('0' + minute) : minute;
        second = second < 10 ? ('0' + second) : second;
        var time= y + '-' + m + '-' + d+' \n '+h+':'+minute+':'+second;
        // time =time.replace(" "," \n ");
        return time;
    };
    
    
}