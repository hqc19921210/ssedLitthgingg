
function dataDownloadCtrl($scope, $http,$timeout, $rootScope) {
    //滚动置顶
    window.scrollTo(0, 0);

    $scope.postFrom={
    		eid:null,
    		start:null,
    		end:null
    };
    $scope.postApps={
    		aid:null
    };
    $scope.list=new Array();
    $scope.list.push({commandEid:null});
    $scope.exportLoading=true;
    
  //时间组件
    $("#datepickerStrat"). datepicker().on('changeDate', function () {
        $scope.postFrom.start=$("#datepickerStrat").val();
    });
    $("#datepickerEnd"). datepicker().on('changeDate', function (e) {
        $scope.postFrom.end =$("#datepickerEnd").val();
    });
    //初始化数据
    $scope.init=function(){
    	//获取设备列表
        $http.get("service/getEquSelectList").success(function(data) {
    		$scope.selEqus = data.resultObj;

    	});
      //获取应用列表
        $http.get("service/getAppSeleList").success(function(data) {
			$scope.selApps = data.resultObj;
			
		});
    }

    //初始化
    $scope.init();
    
    $scope.exportPost=function(){
    	if(!$scope.postFrom.eid){
    		swal("请选择设备", null, "error");
    		return;
    	}
    	if($scope.postFrom.start>$scope.postFrom.end){
    		swal("开始日期大于结束日期", null, "error");
    		return;
    	}
    	$scope.exportLoading=false;
		$http.post("service/setDataDownloadParam",$scope.postFrom).success(function(data) {
				if(data.resultObj == "errorMsg"){
					swal(data.message, null, "error");
				}else{
					$rootScope.downLoadFile("/service/exprDataDownloadByParam" );
					$timeout(function () {
						$scope.exportLoading=true;
					},5*1000)
				}
			});
    };
    
    $scope.deleteItem=function (entity) {
        $scope.list.splice($scope.postApps.list.indexOf(entity), 1);
    };
    $scope.changepApp=function (aid) {
    	$http.post("service/getEquSelectListByAppId",{aid:aid}).success(function(data) {
			if(data.resultObj == "errorMsg"){
				swal(data.message, null, "error");
			}else{
				$scope.selEqusByApp = data.resultObj;
			}
		});
    };
    $scope.postCommand=function (index) {
    	console.log(index,$scope.list[index]);
    	$scope.postApps.params=$scope.list[index];
    	$http.post("service/postCommand",$scope.postApps).success(function(data) {
    		if(data.resultObj == "errorMsg"){
    			swal(data.message, null, "error");
    		}else{
    			console.log(data);
    		}
    	});
    };
    $scope.addList=function () {
        $scope.list.push({commandEid:null});
    };

}