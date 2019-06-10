wechatApp.controller("userCtrl", function($scope, $http, $rootScope, $timeout) {
	$scope.username = $rootScope.user.company;
	$scope.checkOpenId = !$rootScope.user.openId;
	$scope.weAccess = function(){
		window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxdca63037b8e0c41f&redirect_uri=http://iot.szsset.com/wechatIndex.html&response_type=code&scope=snsapi_base&state=sset#wechat_redirect";
	}
	
	$scope.logout=function(){
		$http.get("service/logout").success(function(data) {
			window.location.href="/wechatLogin.html"
		});
 };
})