
function commandLogCtrl($scope, $http,$timeout, $rootScope,$routeParams) {
    //滚动置顶
    window.scrollTo(0, 0);

    $scope.postFrom={
    		eid:null
    };
    $scope.dataOnReady=false;
    $scope.exportLoading=true;
    $scope.devId=$routeParams.devId;
    //为后台请求参数 带分页数据
    $scope.quereyData={
        page:1, //当前页码 初始化为1
        size:defaultSize, //每页数据量 defaultSize全局变量
    };
    $scope.quereyData.devId=$routeParams.devId;
	$scope.pages=0;
	$scope.total=0;
    //翻页
    $scope.changePage=function(page){
        $scope.quereyData.page=page;
        $scope.init();
    }
    
    //初始化数据
    $scope.init=function(){
        $http.post("/service/getCommandLogByDevId",$scope.quereyData).success(function(data) {
            $scope.data = data.resultObj.list;
            $scope.pages=data.resultObj.pages;
    		$scope.total=data.resultObj.total;
    		$scope.pageArr=data.resultObj.navigatepageNums;
    		$scope.quereyData.page=data.resultObj.pageNum;
            console.log(data.resultObj);
            $scope.dataOnReady=true;
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