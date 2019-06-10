var defaultSize=4;
var wechatApp = ons.bootstrap();
wechatApp.config(['$locationProvider', function($locationProvider) {
    // $locationProvider.html5Mode(true);
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
}]);