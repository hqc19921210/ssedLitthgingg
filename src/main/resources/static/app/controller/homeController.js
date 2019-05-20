function homeCtrl($scope, $http, $rootScope,$timeout) {
    //滚动置顶
    window.scrollTo(0, 0);

	$scope.cmp = !$rootScope.user ? true : ($rootScope.user.competence == 2 ? false : true) ;
	var Dashboard = function() {
        this.$body = $("body")
        this.$realData = []
    };
    $scope.home={
        'equAll':33
    }
    $scope.param={};

    var plabels =  "故障统计";
    var pcolors = '#2b9ac9';
    $scope.plotAlarmDatas = [];
    $scope.xTicks=[];
    $scope.alarmSunCount=0;
    var borderColor = '#fff';

    var bgColor = '#fff';
    var pielabels = ["GPRS","LORA","NBIOT"];
    var pieColors = ['#7e57c2', '#2b9ac9', "#58c9c7"];
	var pieDatas = [0,0,0];
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
        $scope.initAlarm();
    });
    $("#datepickerEnd1"). datepicker().on('changeDate', function () {
        $scope.param.end =$("#datepickerEnd1").val();
        $scope.initAlarm();
    });

    //无效
    var axisLabel1 = {
        // 方法1：倾斜显示
        interval: 0, //强制全部显示，1表示隔一个；2隔两个
        rotate: "45", //文字倾斜的角度
    }
    //creates plot graph
    Dashboard.prototype.createAlarmGraph = function(selector,  data2, labels, colors, borderColor, bgColor) {


      var option ={

          series: {
              lines: {
                  show: true,
                  fill: false,
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
              shadowSize: 0
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
              /*  min: 0,
               max: $scope.max,*/
              color: 'rgba(0,0,0,0)'
          },
          xaxis: {
              //    ticks:$scope.xTicks,
             // tickSize:1,
              color: 'rgba(0,0,0,0)',
              tickFormatter: function (val, axis) {
                  var year = getFirstDayOfYear(new Date());
                  var d = addDate(year,val-1)
                  return $scope.fmtDate(d);
                  //  return val;
              },
             // axisLabel: axisLabel1
          },

          tooltip: true,
          tooltipOpts: {
              content: '%x共报警%y次',
              shifts: {
                  x: -60,
                  y: 25
              },
              defaultTheme: false
          }
      };

      if($scope.xTicks.length<=9){
          option.xaxis.ticks=$scope.xTicks;
          option.xaxis.tickSize=1;
      }
      $.plot($(selector),
          [
          { data: data2,
            label: labels,
            color: colors
          }
        ],option
        );
    },
    //end plot graph

    //creates Pie Chart
    Dashboard.prototype.createPieGraph = function(selector, labels, datas, colors) {
        var data = [{
            label: labels[0],
            data: datas[0]
        }, {
            label: labels[1],
            data: datas[1]
        }, {
            label: labels[2],
            data: datas[2]
        }];
        var options = {
            series: {
                pie: {
                    show: true
                }
            },
            legend : {
              show : false
            },
            grid : {
              hoverable : true,
              clickable : true
            },
            colors : colors,
            tooltip : true,
            tooltipOpts : {
                defaultTheme:false,
              content : function(label, xval, yval, flotItem){
            	  return label+"设备:"+yval+"台";
              }
            }
        };

        $.plot($(selector), data, options);
    },

    Dashboard.prototype.initAlarm = function() {
        //plot graph data
        this.createAlarmGraph("#website-stats", $scope.plotAlarmDatas, plabels, pcolors, borderColor, bgColor);
    }

    $scope.initAlarm=function () {
        $http.post("service/queryAlarmByTimeType",$scope.param).success(function(data) {
            $scope.plotAlarmDatas=[];
            $scope.xTicks=[];
            $scope.alarmSunCount=0;
            if(data.resultObj && data.resultObj.length >0){
              for(var i=0;i<data.resultObj.length;i++){
                    $scope.plotAlarmDatas.push([data.resultObj[i].times,data.resultObj[i].count]);
                    $scope.xTicks.push(data.resultObj[i].times);
                    $scope.alarmSunCount=$scope.alarmSunCount+data.resultObj[i].count;
                }
              /* for(var i=0;i<15;i++){
                    $scope.plotAlarmDatas.push([i,i]);
                    $scope.xTicks.push(i);
                    $scope.alarmSunCount=$scope.alarmSunCount+i;
                }*/
            };
            $.Dashboard.initAlarm();
        });
    }
    $scope.initAlarm();

    Dashboard.prototype.initPie = function() {
        //Pie graph data
        this.createPieGraph("#pie-chart #pie-chart-container", pielabels , pieDatas, pieColors);
    },

    //init Dashboard
    $.Dashboard = new Dashboard, $.Dashboard.Constructor = Dashboard;
   // $.Dashboard.initPie();
    $http.get("service/getHomePie").success(function(data) {
		pieDatas = data.resultObj.pieMap;
		$scope.home=data.resultObj;
		$.Dashboard.initPie();
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


}