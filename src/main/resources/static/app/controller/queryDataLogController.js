
function queryDataLogCtrl($scope, $http,$timeout, $rootScope) {
    //滚动置顶
    window.scrollTo(0, 0);

    $scope.postFrom={
    		eid:null
    };
    $scope.dataOnReady=false;
    $scope.exportLoading=true;
    //初始化数据
    $scope.init=function(){
        $http.get("/service/queryDataLog").success(function(data) {
            $scope.data = data.resultObj;
            $scope.dataOnReady=true;
        });
        $http.get("service/getEquSelectList").success(function(data) {
    		$scope.selEqus = data.resultObj;

    	});
    }

    //初始化
    $scope.init();
    
    $scope.exportPost=function(){
    	if(!$scope.postFrom.eid){
    		swal("选个设备吧，哥", null, "error");
    		return;
    	}
    	$scope.exportLoading=false;
		$http.post("service/setDataLogParam",$scope.postFrom).success(function(data) {
				if(data.resultObj == "errorMsg"){
					swal(data.message, null, "error");
				}else{
					$rootScope.downLoadFile("/service/exprDataLogByParam" );
					$timeout(function () {
						$scope.exportLoading=true;
					},5*1000)
				}
			});
    }

}