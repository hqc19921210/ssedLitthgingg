wechatApp.controller("indexCtrl", function($scope, $http, $rootScope, $timeout,$interval,$location) {
	$rootScope.loadingModal = document.querySelector('ons-modal');//loading遮罩
	
	  $http.post("service/checkLogin").success(function(data) {
			if(data.resultObj){
				//保存全局的用户
				$rootScope.user=data.resultObj;
				//全局编辑权限
				$rootScope.edit_cmp=data.resultObj.cmp;
				if(data.resultObj.openId ==null && !!$location.search().code){
					$scope.user = data.resultObj;
					$scope.user.code = $location.search().code;
					$scope.setOpenId($scope.user);
				}
			}else{
				//未登录则跳回登录页
				window.location.href="/wechatLogin.html";
			}
		});
	  //点击tab触发事件
	 $scope.selectedTab = function($event) {
	      //console.log($event);
	    };
	  
    $scope.setOpenId = function(user) {
        $http.post("service/updateOpenIdById",user).success(function(data) {
			    	if(data.resultObj == "errorMsg"){
			    		$scope.options={message:data.message,title:"绑定失败"};
			        }else{
			        	$scope.options={message:"微信绑定成功",title:""};
			        }
			    	ons.notification.alert($scope.options);
        });

    }
	 
		   
	})