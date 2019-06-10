wechatApp.controller("dataCtrl", function($scope, $http, $rootScope, $timeout) {
		var data = {"pointStart":1230764400000,"pointInterval":1000,"dataLength":78907,"data":[0.1,-0.7,-0.9,-1.7,-2.5,-3,-3.3,-3.8,-4,-4.5,-4.7,-4.8,-4.5,-4.5,-4.7,-4.8,-5.2,-5.2,-5,-5.4,-5.8,-5.8,-5.5,-4.7,-4.1,-3.5,-3.2,-2.8,-2.4,-1.7,-1.2,-0.9,-0.7,-0.4,-0.4,-0.3,0.2,0.2,0.3,0.3,0.5,0.9,0.7,1.1,1.3,1.4,1,0.3,0.1,0.1,0.3,0.7,1.2,1.1,2.1,3.7,4,4.1,3.9,3.7,3.3,3.7,4.1,4.1,3.6,0.1,-0.7,-0.9,-1.7,-2.5,-3,-3.3,-3.8,-4,-4.5,-4.7,-4.8,-4.5,-4.5,-4.7,-4.8,-5.2,-5.2,-5,-5.4,-5.8,-5.8,-5.5,-4.7,-4.1,-3.5,-3.2,-2.8,-2.4,-1.7,-1.2,-0.9,-0.7,-0.4,-0.4,-0.3,0.2,0.2,0.3,0.3,0.5,0.9,0.7,1.1,1.3,1.4,1,0.3,0.1,0.1,0.3,0.7,1.2,1.1,2.1,3.7,4,4.1,3.9,3.7,3.3,3.7,4.1,4.1,3.6,3.1,2.7,2.1,2,0.9,0,-0.5,-1.2,-1.1,-1,-0.7,-1.4,-1.4,-2,-1.8,-2,-3.4,-4.6,-5.3,-5.7,-6,-6.1,-6.5,-7.2,-7.8,-7.9,-8.1,-8.4,-8.5,-8.7,-8.9,-9.1,-9.3,-9.2,-9.4,-9.5,-9.7,-9.7,-9.7,-9.6,-8.1,-7.5,-6.1,-5,-4.3,-4,-4,-4.1,-4.2,-4.4,-4.5,-4.6,-4.5,-4.1,-3.7,-3.4,-3.1,-1.8,1.4,2.5,2.8,3,3.2,3.2,2.9,3.1,3.1,2.2,2.3,3,3,2.9,2.6,2,1.1,0.2,-0.2,-0.3,-0.2,-0.2,-0.3,-0.4,-0.7,-1,-1.1,-1.1,-1.2,-1.2,-1.1,-1.2,-1.2,-0.8,-1.2,-1.5,-1.7,-2.9,-3.9,-3.1,-2.7,-3,-2.7,-2.4,-2.2,-2.1,-2.3,-2.3,-2.3,-2.4,-2.4,-2.2,-2.2,-2.1,-1.6,-1.3,-0.8,-0.1,1.1,2.3,2.8,3.4,4.5,5.5,6.1,6.7,6.5,5.8,5.3,4.5,4,3.9,3.8,3.9,4.5,4.5,4.7,5,5.4,5.7,5.6,5.4,5.4,5.3,5.2,4.2,3.8,4.2,5,5.5,6.3,6.8,6.9,6.7,6.7,6.8,6.1,5.5,6.2,6.5,6.2,5.9,5.7,5.1,4.6,4.7,5.8,6.3,6.5,6.3,5.3,5.3,5.1,5.7,5.5,4.5,3.1,0,0,0,0,0,0,0,0,0,0,0,6.7,6.5,6.4,6.2,6.3,7,7.5,7.3,7.2,7.3,7.2,6.8,7.6,8.4,8.4,8.8,8.8,8.7,8.8,8.9,8.6,8.4,8.5,8.6,8.9,9.4,9.2,8.9,8.7,8.2,5.6,7.2,6.9,6.9,6.6,6.8,6.8,6.5,6.3,6,5.7,5.5,5.3,5.1,5.5,5.5,5.8,5.4,5.3,5.3,5.3,5.3,5.1,4.4,4.2,4.1,4.4,4,3.6,3.7,4.1,3.7,3.7,3.8,3.8,3.4,3.6,3.6,3.4,3,2.4,2.2,1.2,0.1,0.3,0.5,0.5,0.5,-0.3,-1,-2.1,-2.4,-3,-3.4,-3.6,-3.6,-3.9,-3.7,-2.4,-1.5,-1.7,-1.5,-2.4,-3.7,-4.5,-5.1,-5.1,-4.8,-4.6,-4.6,-4.5,-4.9,-5.3,-5.8,-6,-5.7,-5.2,-4.8,-4.6,-3.8,-2.5,-1.8,-1.5,-0.8,-0.6,-0.4,-0.3,-0.2,-0.1,0,0.2,0.8,1.3,1.7,2,1.3,1.2,1.3,1.6,1.8,1.8,2.3,2.4,2.8,2.6,2.6,2.8,3.3,3,2.5,2.1,1.5,1.8,2.3,3.1,3.3,3.8,4.1,3.9,3.6,4.2,4.2,4,3.9,3.7,3.7,3.5,4.2,3.9,3.8,3.9,3.7,3.6,3.5,3.9,3.7,3.2,3.6,4.1,4.3,4.5,4.4,3,2.4,2.4,2.7,2.4,1.9,0,0,0,0,0,0,0,0,0,0,0,0,0,0.2,0.8,1.8,2.6,3.2,2.3,1,0,-0.8,-1.1,-1,0.1,3.2,3.6,3.2,4.1,3.3,4,4.5,5.1,5,4.6,4.3,4.4,4.1,2.9,1.4,1.4,1.6,1.4,1.3,0.8,0.6,0.6,0.6,3.1,2.7,2.1,2,0.9,0,-0.5,-1.2,-1.1,-1,-0.7,-1.4,-1.4,-2,-1.8,-2,-3.4,-4.6,-5.3,-5.7,-6,-6.1,-6.5,-7.2,-7.8,-7.9,-8.1,-8.4,-8.5,-8.7,-8.9,-9.1,-9.3,-9.2,-9.4,-9.5,-9.7,-9.7,-9.7,-9.6,-8.1,-7.5,-6.1,-5,-4.3,-4,-4,-4.1,-4.2,-4.4,-4.5,-4.6,-4.5,-4.1,-3.7,-3.4,-3.1,-1.8,1.4,2.5,2.8,3,3.2,3.2,2.9,3.1,3.1,2.2,2.3,3,3,2.9,2.6,2,1.1,0.2,-0.2,-0.3,-0.2,-0.2,-0.3,-0.4,-0.7,-1,-1.1,-1.1,-1.2,-1.2,-1.1,-1.2,-1.2,-0.8,-1.2,-1.5,-1.7,-2.9,-3.9,-3.1,-2.7,-3,-2.7,-2.4,-2.2,-2.1,-2.3,-2.3,-2.3,-2.4,-2.4,-2.2,-2.2,-2.1,-1.6,-1.3,-0.8,-0.1,1.1,2.3,2.8,3.4,4.5,5.5,6.1,6.7,6.5,5.8,5.3,4.5,4,3.9,3.8,3.9,4.5,4.5,4.7,5,5.4,5.7,5.6,5.4,5.4,5.3,5.2,4.2,3.8,4.2,5,5.5,6.3,6.8,6.9,6.7,6.7,6.8,6.1,5.5,6.2,6.5,6.2,5.9,5.7,5.1,4.6,4.7,5.8,6.3,6.5,6.3,5.3,5.3,5.1,5.7,5.5,4.5,3.1,0,0,0,0,0,0,0,0,0,0,0,6.7,6.5,6.4,6.2,6.3,7,7.5,7.3,7.2,7.3,7.2,6.8,7.6,8.4,8.4,8.8,8.8,8.7,8.8,8.9,8.6,8.4,8.5,8.6,8.9,9.4,9.2,8.9,8.7,8.2,5.6,7.2,6.9,6.9,6.6,6.8,6.8,6.5,6.3,6,5.7,5.5,5.3,5.1,5.5,5.5,5.8,5.4,5.3,5.3,5.3,5.3,5.1,4.4,4.2,4.1,4.4,4,3.6,3.7,4.1,3.7,3.7,3.8,3.8,3.4,3.6,3.6,3.4,3,2.4,2.2,1.2,0.1,0.3,0.5,0.5,0.5,-0.3,-1,-2.1,-2.4,-3,-3.4,-3.6,-3.6,-3.9,-3.7,-2.4,-1.5,-1.7,-1.5,-2.4,-3.7,-4.5,-5.1,-5.1,-4.8,-4.6,-4.6,-4.5,-4.9,-5.3,-5.8,-6,-5.7,-5.2,-4.8,-4.6,-3.8,-2.5,-1.8,-1.5,-0.8,-0.6,-0.4,-0.3,-0.2,-0.1,0,0.2,0.8,1.3,1.7,2,1.3,1.2,1.3,1.6,1.8,1.8,2.3,2.4,2.8,2.6,2.6,2.8,3.3,3,2.5,2.1,1.5,1.8,2.3,3.1,3.3,3.8,4.1,3.9,3.6,4.2,4.2,4,3.9,3.7,3.7,3.5,4.2,3.9,3.8,3.9,3.7,3.6,3.5,3.9,3.7,3.2,3.6,4.1,4.3,4.5,4.4,3,2.4,2.4,2.7,2.4,1.9,0,0,0,0,0,0,0,0,0,0,0,0,0,0.2,0.8,1.8,2.6,3.2,2.3,1,0,-0.8,-1.1,-1,0.1,3.2,3.6,3.2,4.1,3.3,4,4.5,5.1,5,4.6,4.3,4.4,4.1,2.9,1.4,1.4,1.6,1.4,1.3,0.8,0.6,0.6,0.6]}

	    // Create the chart
	    Highcharts.stockChart('container', {
	        chart: {
	            events: {
	            },
	            zoomType: 'x'
	        },

	        rangeSelector: {

	            buttons: [{
	                type: 'minute',
	                count: 1,
	                text: '1分钟'
	            },{
	                type: 'hour',
	                count: 1,
	                text: '1小时'
	            },{
	                type: 'day',
	                count: 3,
	                text: '3天'
	            }, {
	                type: 'week',
	                count: 1,
	                text: '1周'
	            }, {
	                type: 'month',
	                count: 1,
	                text: '1个月'
	            }, {
	                type: 'month',
	                count: 6,
	                text: '6个月'
	            }, {
	                type: 'year',
	                count: 1,
	                text: '1年'
	            }, {
	                type: 'all',
	                text: '全部'
	            }],
	            selected: 3
	        },

	        yAxis: {
	            title: {
	                text: '数据点值'
	            }
	        },

	        title: {
	            text: '测试图表功能'
	        },

	        subtitle: {
	            text: '测试数据' // dummy text to reserve space for dynamic subtitle
	        },

	        series: [{
	            name: '值',
	            data: data.data,
	            pointStart: data.pointStart,
	            pointInterval: data.pointInterval,
	            tooltip: {
	                valueDecimals: 2,
	                valueSuffix: ''
	            }
	        }]

	    });
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
    
//    if($routeParams){
//        $scope.param.devId=$routeParams.devId;
//        $scope.param.attrId=$routeParams.attrId;
//        //初始化选择
//        //只有第一次从列表跳入时初始化
//        $scope.param.initOption='TRUE';
//    }

    $scope.changePage =function(page){
        $scope.curpage=page;
        $scope.logArr =new Array();
        for (var i =0; i < 10000; i++) {
        	if((page-1)*10000+i>=$scope.log.length){
                return;
            }
            $scope.logArr.push($scope.log[(page-1)*10000+i]);
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
    $scope.plotKeys =new Array();
    $scope.attrType="";

    $scope.param.end =$scope.fmtDate(new Date());
    var date = new Date();//获取当前时间
    date.setDate(date.getDate()-30);//设置天数 -30天
    $scope.param.start=$scope.fmtDate(date);

    //设备信息
    $scope.equip={};

    $scope.unit="";


    //初始化数据
    $scope.init=function(){
    	$rootScope.loadingModal.show();
        $http.post("/service/queryEquAttrLog",$scope.param).success(function(data) {
            $scope.param.initOption='FALSE';
            if(data.resultObj.devList){
                $scope.devList = data.resultObj.devList;
            }
            $scope.param.devId=data.resultObj.devId;
            $scope.param.attrId=""+data.resultObj.attrId.toString();
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
                    $scope.pages =Math.ceil($scope.log.length/10000);
                    $scope.changePage(1);
                }
                $scope.showChart();
            }
            if(data.resultObj.equip){
                $scope.equip=data.resultObj.equip;
            }
            if(data.resultObj.equipUserName) {
                //设备用户名
                $scope.equipUserName = data.resultObj.equipUserName
            }
            $rootScope.loadingModal.hide();
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



    $scope.showChart=function(){
        $scope.plotDownloads =[];
        $scope.plotKeys =[];
        //非波形
        if( $scope.attrType != "WAVE_TYPE"){
            for(var j=0;j<$scope.log.length;j++){
                var obj =$scope.log[j];
                var value =obj.dataValue;
                $scope.plotDownloads.push({x:new Date(obj.addDate),y:value});
                $scope.plotKeys.push(Date.parse(new Date(obj.addDate)));
            }
        }else{
            //默认显示第一个波形
            $scope.showWave($scope.log[0]);
        }
    }

    $scope.showWave=function (obj) {
        $scope.plotDownloads=[];
        $scope.plotKeys=[];
        if(obj && $scope.attrType == "WAVE_TYPE"){
            var values=obj.dataValue;
            var arr = values.split(",");
            for(var i=0;i<arr.length;i++){
                $scope.plotDownloads.push({x:i,y:arr[i]});
                $scope.plotKeys.push(i);
            }
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
	
    this.delegate = {
			  configureItemScope: function(index, itemScope) {
			    itemScope.item = $scope.logArr[index];
			    $scope.logArr[index].index = index;
			  },
			  countItems: function() {
			    return $scope.logArr.length;
			  },
			  calculateItemHeight: function() {
			    return ons.platform.isAndroid() ? 48 : 44;
			  }
			};
	
	
	
})