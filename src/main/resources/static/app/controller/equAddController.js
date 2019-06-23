function equAddCtrl($scope, $http,$rootScope,$location,$timeout) {
    //滚动置顶
    window.scrollTo(0, 0);

    var elem = document.createElement("script");
	elem.src = 'assets/js/jquery.easyui.min.js';
	document.body.appendChild(elem);
	$scope.loadCtl={
    		addEnq:false
    };
	$scope.addFrom = {
			uid:$rootScope.user.id,
			typeCd : 'L'
	}
	$scope.chkType=false;
	$scope.globalCMP=$rootScope.user.competence;
	//获取设备分组列表
    $scope.getDevGroupsList = function () {
    	$http.get("service/getEquGroups").success(function(data) {
    		setTimeout(function () {
	    		//构建下拉树
	    		var treeFmt=angular.fromJson(angular.toJson(data.resultObj));
	    		$('.easyui-combotree').combotree({
	    			data:treeFmt
	    		});
	    		$('.easyui-combotree').combotree('setValue',1);
    		},400);
    	});
    };
    //获取用户列表
    $scope.seleUsersLst = function(){
    	$http.get("service/getCompanySeleList").success(function(data) {
    		if(data.resultObj == "errorMsg"){
    			swal(data.message, null, "error");
    		}else{
    			$scope.selCompanys = data.resultObj;
    		}
    	});
    };
    //获取产品列表
    $scope.getProductsList = function(){
    	$http.post("service/getProductsList",{type:$scope.addFrom.typeCd}).success(function(data) {
    		if(data.resultObj == "errorMsg"){
    			swal(data.message, null, "error");
    		}else{
    			$scope.selProducts = data.resultObj;
    		}
    	});
    };
	//初始化
    $scope.init = function() {
    	$scope.getDevGroupsList();
    	$scope.seleUsersLst();
    	$scope.getProductsList();
    }
	$scope.addEqu = function() {
    	$scope.loadCtl.addEnq = true;
    	$scope.addFrom.groupId = $('#equ_easyui_combotree').combotree('getValue');
    	if($scope.addFrom.uid == null || $scope.addFrom.verification == null ||
    			$scope.addFrom.typeCd == null || $scope.addFrom.name == null ||
    			$scope.addFrom.proId == null || $scope.addFrom.groupId == null ){
    		swal("请填写所有必填项", null, "error");
    		$scope.loadCtl.addEnq = false;
    		return;
    	}
        $http.post("service/addEqu",$scope.addFrom).success(function(data) {
			    	if(data.resultObj == "errorMsg"){
			    		swal(data.message, null, "error");
			    		$scope.loadCtl.addEnq = false;
			        }else{
			        	//修改成功后
			        	swal("新增成功", null, "success");
			        	$location.path("/module/equView");
			        }
        });
		
    };
    
    
    $scope.init();
	
	$scope.selEquType = function(type){
//		if(type=='N'){
//			$scope.chkType=true;
//		}else{
//			$scope.addFrom.appId=null;
//			$scope.chkType=false;
//		}
		$scope.addFrom.typeCd = type;
		$scope.getProductsList();
	}
	//切换所属用户
	$scope.seledUser = function(uid){
		if(uid){
			$scope.seleUserModelLst(uid);
		}
	}
	// 百度地图
	$timeout(function () {
        var map = new BMap.Map("add-site-map");
        map.centerAndZoom(new BMap.Point(114.070855, 22.551052), 12);
        var geolocation = new BMap.Geolocation();	
        geolocation.getCurrentPosition(function (r) {
            if (this.getStatus() == BMAP_STATUS_SUCCESS) {
                var mk = new BMap.Marker(r.point);
                map.addOverlay(mk);
                map.panTo(r.point);
                $scope.$apply(function () {
                    $scope.addFrom.site = r.point.lng + "," + r.point.lat;
                    $scope.locationPoint = "经度：" + r.point.lng + "，" + "纬度：" + r.point.lat;
                    var geoc = new BMap.Geocoder();
                    geoc.getLocation(new BMap.Point(r.point.lng, r.point.lat), function (rs) {
                        $scope.addFrom.address = rs.address;
                    });
                })
            }
        }, {enableHighAccuracy: true});


        map.enableScrollWheelZoom();  // 开启鼠标滚轮缩放
        map.enableContinuousZoom();   // 开启连续缩放效果
        map.enableInertialDragging(); // 开启惯性拖拽效果
        map.addControl(new BMap.NavigationControl()); //添加标准地图控件(左上角的放大缩小左右拖拽控件)
        map.addControl(new BMap.ScaleControl());      //添加比例尺控件(左下角显示的比例尺控件)
        map.addControl(new BMap.OverviewMapControl()); // 缩略图控件
        map.addControl(new BMap.MapTypeControl()); //添加地图类型控件
        // noinspection JSAnnotator
        function addbaidumap(x, y) {
            var point = new BMap.Point(x, y);
            var marker = new BMap.Marker(point);  // 创建标注
            map.addOverlay(marker);               // 将标注添加到地图中
            // marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
        }

        map.addEventListener("click", function (e) {
            map.clearOverlays();
            addbaidumap(e.point.lng, e.point.lat);
        });


        $scope.searchMap = function () {
            var ac = new BMap.Autocomplete(    //建立一个自动完成的对象用来做地图模糊搜索
                {
                    "input": "suggest-address"
                    , "location": map
                });
            var myValue;
            ac.addEventListener('onconfirm', function (e) {
                var _value = e.item.value;
                myValue = _value.province + _value.city + _value.district + _value.street + _value.business;
                setPlace();
            });

            function setPlace() {
                map.clearOverlays();    //清除地图上所有覆盖物
                function myFun() {
                    var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
                    map.centerAndZoom(pp, 18);
                    map.addOverlay(new BMap.Marker(pp));    //添加标注
                    $scope.$apply(function () {
                        $scope.addFrom.site = pp.lng + "," + pp.lat;
                        $scope.locationPoint ="经度：" + pp.lng + "，" + "纬度：" + pp.lat;
                    })

                }

                var local = new BMap.LocalSearch(map, { //智能搜索
                    onSearchComplete: myFun
                });
                local.search(myValue);
            }
        };

        map.addEventListener("click", function (e) {
            $scope.$apply(function () {
            	$scope.addFrom.site = e.point.lng + "," + e.point.lat;
                $scope.locationPoint = "经度：" + e.point.lng + "，" + "纬度：" + e.point.lat;
                var geoc = new BMap.Geocoder();
                geoc.getLocation(new BMap.Point(e.point.lng, e.point.lat), function (rs) {
                    $scope.addFrom.address = rs.address;
                });
            })
            console.log($scope.addFrom);
        });
    },10);
}