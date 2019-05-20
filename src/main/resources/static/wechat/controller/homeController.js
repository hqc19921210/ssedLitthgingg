wechatApp.controller("homeCtrl", function($scope, $http, $rootScope, $timeout) {
	  $scope.quereyData={
	        page:1, //当前页码 初始化为1
	        size:defaultSize, //每页数据量 defaultSize全局变量
	    };
		$scope.pages=0;
		$scope.total=0;
		$scope.currPage=0;
		$scope.loadCtl={
	    		search:false,	
	    		addEnq:false
	    };
		$scope.data= null;
		$scope.seleItem={};
		$scope.edit_cmp=$rootScope.edit_cmp;
	    $scope.init=function(){
	    	$scope.loadCtl.search = true;
	    	$http.post("service/getEquipments",$scope.quereyData).success(function(data) {
	    		$scope.equipments = data.resultObj.list;
	    		$scope.pages=data.resultObj.pages;//总页数
	    		$scope.currPage=data.resultObj.pageNum;//当前页
	    		$scope.total=data.resultObj.total;//总记录数
	    		$scope.quereyData.page=data.resultObj.pageNum;
	    		$scope.loadCtl.search = false;
	    	});
	    }
	  //初始化
	    $scope.init();
	
	    $scope.resetSearch=function(){
	        $scope.quereyData.eid=null;
	        $scope.quereyData.type=null;
	        $scope.quereyData.seleStatus=null;
	        $scope.quereyData.page=1;
	        $scope.pages=0;
	        $scope.init();
	    };
	    $scope.getStatusImg = function (status) {
	        if(status == 0){
	            return "assets/img/offline.png";
	        } else if(status == 1){
	            return "assets/img/online.png";
	        }else if(status == 2){
	            return "assets/img/alarmline.png";
	        }
	   };
	   
	   $scope.nextPage = function(pageNum) {
		 if(!(pageNum<1) && pageNum<$scope.pages && $scope.currPage !== pageNum){
			 $scope.quereyData.page=pageNum;
			 $scope.init();
		 }
		 return;
	   }
  })