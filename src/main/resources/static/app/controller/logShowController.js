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

    //初始化数据
    $scope.init=function(){
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



    $scope.showChart=function(){
        $scope.plotDownloads =[];
        //非波形
        if( $scope.attrType != "WAVE_TYPE"){
            for(var j=0;j<$scope.log.length;j++){
                var obj =$scope.log[j];
                var value =obj.dataValue;
                $scope.plotDownloads.push([Date.parse(new Date(obj.addDate)),value]);
            }
        }else{
            //默认显示第一个波形
            $scope.showWave($scope.log[0]);
        }
        $.Dashboard.init();
    }

    $scope.showWave=function (obj) {
        $scope.plotDownloads=[];
        if(obj && $scope.attrType == "WAVE_TYPE"){
            var values=obj.dataValue;
            var arr = values.split(",");
            for(var i=0;i<arr.length;i++){
                $scope.plotDownloads.push([i,arr[i]]);
            }
            $.Dashboard.init();
        }
    }

    var Dashboard = function() {
        this.$body = $("body")
        this.$realData = []
    };
    $scope.sum=0;



    var pcolors = '#2b9ac9';
    var borderColor = '#fff';
    var bgColor = '#fff';

    Dashboard.prototype.createPlotGraph = function(selector, data1,  labels, colors, borderColor, bgColor) {
        //shows tooltip
        function showTooltip(x, y, contents) {
            $('<div id="tooltip" class="tooltipflot">' + contents + '</div>').css( {
                position: 'absolute',
                top: y + 5,
                left: x + 5
            }).appendTo("body").fadeIn(200);
        }

        $.plot($(selector),
            [ { data: data1,
                label: labels,
                color: colors
            }
            ],
            {
                series: {
                    lines: {
                        show: true,
                        fill: true,
                        lineWidth: 2,
                        fillColor: {
                            colors: [ { opacity: 0.0 },
                                { opacity: 0.0 }
                            ]
                        }
                    },
                    points: {
                        show: true
                    },
                    shadowSize: 0,
                    /*dataLabels:{
                     enabled:false
                     }*/
                },
                legend: {
                    position: 'nw'
                },
                grid: {
                    hoverable: true,
                    clickable: true,
                    borderColor: borderColor,
                    borderWidth: 0,
                    labelMargin: 10,
                    backgroundColor: bgColor
                },
                yaxis: {
                    //min: 0,
                    //max: $scope.max,

                 //   ticks:6,
                //    autoscaleMargin:1,

                 //   tickDecimals:1,
                    color: 'rgba(0,0,0,0)',
                    show:true,
                   // mode:number,
                    tickFormatter: function (val, axis) {
                    	console.log("y",val);
                    	if(String(val).indexOf('.') != -1){
                    		val=Number(val).toFixed(2);
                    	}
                        if($scope.unit){
                            return val+$scope.unit ; //单位
                        }else{
                            return val;
                        }
                    },
                },
                xaxis: {
                  //  autoscaleMargin:'1',
                    color: 'rgba(0,0,0,0)',
                    tickFormatter: function (val, axis) {
                    	console.log(val);
                        //非波形
                        if( $scope.attrType != "WAVE_TYPE"){
                            return formatDateTime(val);
                        }else{
                            return val;
                        }

                    },
                    labelWidth:5
                },
                tooltip: true,
                tooltipOpts: {
                   content: '%y(%x) ',
                 /*   content : function(label, xval, yval, flotItem){
                        return label+"设备:"+yval+"台";
                    },*/
                    shifts: {
                        x: -60,
                        y: 25
                    },
                    defaultTheme: false
                }
            });
    },
        //end plot graph

        //initializing various charts and componentszuo
        Dashboard.prototype.init = function() {
    	    var points=angular.fromJson(angular.toJson($scope.plotDownloads));
    	    var keys=angular.fromJson(angular.toJson($scope.param.attrKey));
//    	    console.log("points",points);
//    	    console.log("keys",keys);
            this.createPlotGraph("#website-stats2", points , keys , pcolors, borderColor, bgColor);
        },

        $.Dashboard = new Dashboard, $.Dashboard.Constructor = Dashboard;

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
    $scope.getMap=function () {
        // 百度地图API功能
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
}