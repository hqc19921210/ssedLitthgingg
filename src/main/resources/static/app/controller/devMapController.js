function devMapCtrl($scope, $http, $rootScope,$routeParams,$timeout,$interval) {
    //滚动置顶
    window.scrollTo(0, 0);

	var elem = document.createElement("script");
	elem.src = 'assets/js/jquery.easyui.min.js';
	document.body.appendChild(elem);
	//计算窗口高度
    var map_height = $(window).height()- $(".container").height()-10;
    // var test_height = $(window).height()- $(".page-top").height()- $("#data_body").find("table").height()-92;
    var test_height = $(window).height()- $(".container").height()-112;
    $("#map_box").css("height",map_height);
    $("#map").css("height",map_height);
    $("#test_content").css("max-height",test_height);
	//End 计算窗口高度
	
    // 显示隐藏下方列表
    var trs;
    $("#device_content").on("DOMNodeInserted",function(){
        trs=$("#device_content").find("tr");
        for (var i=2; i<trs.length;i++){
            $(trs[i]).hide();
            $(trs[i]).attr("index","true")
        }
    });
    $("#display_block").click(function(){
        for (var i=2; i<trs.length;i++){
            if(typeof($(trs[i]).attr("index"))==='undefined' || $(trs[i]).attr("index")==='true'){
                $(trs[i]).show();
                $(trs[i]).attr("index","false");
                $("#display_block").removeClass("ion-chevron-up").addClass("ion-chevron-down");
            }else {
                $(trs[i]).attr("index","true");
                $(trs[i]).hide();
                $("#display_block").removeClass("ion-chevron-down").addClass("ion-chevron-up");
            }
        }
    });
	// End 显示隐藏下方列表
	//为后台请求参数 带分页数据
    $scope.quereyData={
        page:1, //当前页码 初始化为1
        size:10, //每页数据量 defaultSize全局变量
    };
	$scope.pages=0;
	$scope.total=0;
	$scope.loadCtl={
     		search:false,	
     		addEnq:false
     };
	//获取设备分组列表
    $scope.getDevGroupsList = function () {
    	$http.get("service/getEquGroups").success(function(data) {
    		setTimeout(function () {
	    		//构建下拉树
	    		var treeFmt=angular.fromJson(angular.toJson(data.resultObj));
                treeFmt.unshift({grpSort:-1,id:-1,name:"全部",pid:-1,text:"全部"});
	    		$('.easyui-combotree').combotree({
	    			data:treeFmt
	    		});
	    		$('.easyui-combotree').combotree({
                   onChange:function(){
//                       if($scope.devFlag===1) {
                           $(".monitor_devGroup").css("display", "none");
                           $scope.getDevList();
//                       }
                   }
               });
	    		$('.easyui-combotree').combotree('setValue',-1);
    		},400);
    	});
    };
    
    
    $scope.getDevList=function(page){
   	 $scope.loadCtl.search = true;
   	 $scope.getAlarmNewest();
   	 $scope.quereyData.page=page ? page : 1;
   	$scope.quereyData.gid=$('.easyui-combotree').combotree('getValue');
   	$scope.map = new BMap.Map("map", {enableMapClick: false});//关闭地图点击事件
    $scope.map.addControl(new BMap.NavigationControl()); //添加标准地图控件(左上角的放大缩小左右拖拽控件)
    $scope.map.addControl(new BMap.MapTypeControl()); //添加地图类型控件
    $scope.map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
     	$http.post("service/getEquipments",$scope.quereyData).success(function(data) {
     		//console.log(data.resultObj);
     		$scope.equipments = data.resultObj.list;
     		$scope.pages=data.resultObj.pages;
     		$scope.total=data.resultObj.total;
     		$scope.quereyData.page=data.resultObj.pageNum;
     		$scope.dataItemsTotal = data.resultObj.total;
            $scope.dataItems = data.resultObj.list;
            try {
                var markers = [];
                var points = [];
                $scope.markers = [];
                $scope.devidMap = [];
                for (var key in data.resultObj.list) {
                    var position = $scope.dataItems[key].site;
                    if(data.resultObj.list[key].online==1) {
                        var statusIco = 'assets/img/online.png';
                    }else {
                        var statusIco = 'assets/img/offline.png';
                    }
                    if (position && position !== '-1') {
                        var myIcon = new BMap.Icon(statusIco, new BMap.Size(20, 20),{anchor:new BMap.Size(10, 10)});
                        var content = data.resultObj.list[key].devId;
                        var marker = new BMap.Marker(new BMap.Point(data.resultObj.list[key].site.split(',')[0], data.resultObj.list[key].site.split(',')[1]), {
                            icon: myIcon,
                            title: data.resultObj.list[key].name+'('+data.resultObj.list[key].devId+')'
                        });
                        $scope.map.addOverlay(marker);
                        $scope.markers.push(marker);
                        points.push(new BMap.Point(data.resultObj.list[key].site.split(',')[0], data.resultObj.list[key].site.split(',')[1]));
                        addClickEvent(content, marker);
                    }
                }
                $scope.devidMap = function (devid, position) {
                	$http.post("service/getEquById",{'devId':devid}).success(function(data) {
                      var point = new BMap.Point(position.split(',')[0], position.split(',')[1]);
                      var devidState = (data.resultObj.online == 0) ? '<img src="assets/img/offline.png" />' : '<img src="assets/img/online.png" />';
                      $scope.map.centerAndZoom(point, 18);
//                      var devidManage;
                      var devidDataHistory;
                      var devidAlarmHistory;
//                      if($rootScope.ums_device_manage){
//                    	  devidManage='<a style="padding:5px 10px;margin:0 10px;background:#f60;color: #fff;line-height: 20px;border-radius:5px;" href="' + $scope.host + '/main.html#/device/deviceDatail/' + devid + '">设备管理</a>';
//                      }else {
//                    	  devidManage='<a style="padding:5px 10px;margin:0 10px;background:#f60;color: #fff;line-height: 20px;border-radius:5px;" href="' + $scope.host + '/main.html#/device/deviceDatail/' + devid + '">设备管理</a>';
////                          devidManage='';
//                      }
                        devidDataHistory='<a style="padding:5px 10px;margin:0 10px;background:#f60;color: #fff;line-height: 20px;border-radius:5px;" href="#/module/logShow/' + data.resultObj.devId + '">历史记录</a>';
                    	devidAlarmHistory='<a style="padding:5px 10px;margin:0 10px;background:#f60;color: #fff;line-height: 20px;border-radius:5px;"  href="#/module/alarmLogList/'+data.resultObj.devId + '">报警记录</a>';
                      var devidContent = '<div style="margin:0;line-height:20px;padding:2px;position:relative;font-size:12px;">' +
                          '<div style="padding-left:40px;line-height: 24px;color:#333;">' +
                          '<b>设备状态 ：</b>' + devidState + '<br />' +
                          '<b>设备名称 ：</b>' + data.resultObj.name + '<br />' +
                          '<b>设备编号 ：</b>' + data.resultObj.devId + '<br />' +
                          '<b>设备类型 ：</b>' + data.resultObj.typeName + '<br />' +
                          '<b>所属用户 ：</b>' + data.resultObj.uName + '<br />' +
                          '<b>设备地址 ：</b>' + (!data.resultObj.address ? data.resultObj.site : data.resultObj.address) +
                          '</div></div><div style="margin:20px auto;text-align: center;font-size:12px;">' +
//                          devidManage +
                          devidDataHistory +
                          devidAlarmHistory +
                          '</div>';
                      $scope.map.openInfoWindow(new BMap.InfoWindow(devidContent, {
                          width: 370,     // 信息窗口宽度
                          //title: "设备信息", // 信息窗口标题
                          enableMessage: true//设置允许信息窗发送短息
                      }), point);
                  });
              };
              if (points.length === 1) {
                  $scope.map.centerAndZoom(points[0], 12);
              } else if (points.length > 1) {
                  var view = $scope.map.getViewport(eval(points));
                  var mapZoom = view.zoom;
                  var centerPoint = view.center;
                  $scope.map.centerAndZoom(centerPoint, mapZoom);
              } else {
                  $scope.map.centerAndZoom(new BMap.Point(114.070855, 22.551052), 10);
              }

          } catch (e){}
          function addClickEvent(content, marker) {
              marker.addEventListener("click", function (e) {
            	  $http.post("service/getEquById",{'devId':content}).success(function(data) {
                      var point = new BMap.Point(e.target.getPosition().lng, e.target.getPosition().lat);
                      var devidState = (data.resultObj.onlineStatus == 0) ? '<img src="assets/img/offline.png" />' : '<img src="assets/img/online.png" />';
                      $scope.map.centerAndZoom(point, 18);
                      var devidDataHistory;
                      var devidAlarmHistory;

                      devidDataHistory='<a style="padding:5px 10px;margin:0 10px;background:#f60;color: #fff;line-height: 20px;border-radius:5px;" href="#/module/logShow/' + data.resultObj.devId + '">历史记录</a>';
                      devidAlarmHistory='<a style="padding:5px 10px;margin:0 10px;background:#f60;color: #fff;line-height: 20px;border-radius:5px;" href="#/module/alarmLogList/'+data.resultObj.devId + '">报警记录</a>';
                      var devidContent = '<div style="margin:0;line-height:20px;padding:2px;position:relative;font-size:12px;">' +
                          '<div style="padding-left:40px;line-height: 24px;color:#333;">' +
                          '<b>设备状态 ：</b>' + devidState + '<br />' +
                          '<b>设备名称 ：</b>' + data.resultObj.name + '<br />' +
                          '<b>设备编号 ：</b>' + data.resultObj.devId + '<br />' +
                          '<b>设备类型 ：</b>' + data.resultObj.typeName + '<br />' +
                          '<b>所属用户 ：</b>' + data.resultObj.uName + '<br />' +
                          '<b>设备地址 ：</b>' + (!data.resultObj.address ? data.resultObj.site : data.resultObj.address) +
                          '</div></div><div style="margin:20px auto;text-align: center;font-size:12px;">' +
                          devidDataHistory +
                          devidAlarmHistory +
                          '</div>';
                      $scope.map.openInfoWindow(new BMap.InfoWindow(devidContent, {
                          width: 370,     // 信息窗口宽度
                          //title: "设备信息", // 信息窗口标题
                          enableMessage: true//设置允许信息窗发送短息
                      }), point);
                  });
              });
          }
     		$scope.loadCtl.search = false;
     	});
    }
    
  //获取设备分组列表
    $scope.getAlarmNewest = function () {
    	$http.get("service/queryAlarmNewestFive").success(function(data) {
    		//console.log(data);
    		$scope.alarmItems=data.resultObj;
    	});
    }
    $scope.init=function(){
    	var display_flag=true;
        $("#close_btn").on("click",function(){
            display_flag=!display_flag;
            if(display_flag){
                $("#test_content").css("display","none");
                $(this).text($translate.instant(900140));
            }else{
                $("#test_content").css("display","block");
                $(this).text($translate.instant(900141));
            }
        });
		
    }
  //初始化
    $scope.getDevGroupsList();
  //翻页
    $scope.changePage=function(page){
        $scope.getDevList(page);
    }
  //每30s刷新一次
  /*  $scope.$on('$destroy',function(){
   	 $interval.cancel(timeout_upd);
    })
	var timeout_upd = $interval(function() {
        $scope.getDevList($scope.quereyData.page);
	}, 30*1000);*/
  //获取上下线图片
    $scope.getStatusImg = function (status) {
        if(status == 0){
            return "assets/img/offline.png";
        } else if(status == 1){
            return "assets/img/online.png";
        }else if(status == 2){
            return "assets/img/alarmline.png";
        }
    };
    var display_group=true;
    $("#group_btn").on("click",function(){
        display_group=!display_group;
        if($(".monitor_devGroup").css('display') == "block"){
            $(".monitor_devGroup").css("display","none");
            $('.easyui-combotree').combotree("hidePanel");
        }else{
            $(".monitor_devGroup").css("display","block");
            $('.easyui-combotree').combotree("showPanel");
        }
    })
}