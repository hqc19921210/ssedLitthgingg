wechatApp.controller("indexCtrl", function($scope, $http, $rootScope, $timeout,$interval,$location) {
	  $http.post("service/checkLogin").success(function(data) {
			if(data.resultObj){
				//保存全局的用户
				$rootScope.user=data.resultObj;
				$scope.username = data.resultObj.company;
				//全局编辑权限
				$rootScope.edit_cmp=data.resultObj.cmp;
				//$scope.ajxUserInfo($rootScope.user);
			}else{
				//未登录则跳回登录页
				window.location.href="/wechatLogin.html";
			}
		});
	  $scope.selectedTab = function($event) {
	      //console.log($event);
	    };
	  
	  $scope.logout=function(){
			$http.get("service/logout").success(function(data) {
				window.location.href="/wechatLogin.html"
			});
	 };
	 
		   
	})