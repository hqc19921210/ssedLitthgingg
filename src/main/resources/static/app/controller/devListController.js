function devLstCtrl($scope, $http,$rootScope,$location,$timeout, $anchorScroll,$interval) {
    //滚动置顶
    window.scrollTo(0, 0);

	var elem = document.createElement("script");
	elem.src = 'assets/js/jquery.easyui.min.js';
	document.body.appendChild(elem);
	$anchorScroll.yOffset = 20;
   //为后台请求参数 带分页数据
     $scope.quereyData={
         page:1, //当前页码 初始化为1
         size:defaultSize, //每页数据量 defaultSize全局变量
     };
    $scope.dataLoading = false;
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
                        $(".monitor_devGroup").css("display", "none");
                        $scope.getDevList();
                    }
                });
 	    		$('.easyui-combotree').combotree('setValue',-1);
     		},400);
     	});
     };
     $scope.getDevList=function(page){
    	 $scope.loadCtl.search = true;
    	 $scope.quereyData.page=page ? page : 1;
    	 $scope.quereyData.gid=$('.easyui-combotree').combotree('getValue');
      	$http.post("service/getEquPage",$scope.quereyData).success(function(data) {
//      		console.log(data.resultObj);
      		$scope.equipments = data.resultObj.list;
      		$scope.pages=data.resultObj.pages;
      		$scope.total=data.resultObj.total;
      		$scope.quereyData.page=data.resultObj.pageNum;
      		$scope.loadCtl.search = false;
      		$rootScope.hideLoading();
      	});
     }
     $scope.init=function(){
    	 $rootScope.showLoading();
    	 $scope.getDevGroupsList();
     }
   //初始化
     $scope.init();
   //翻页
     $scope.changePage=function(page){
         $scope.quereyData.page=page;
         $scope.getDevList(page);
     }
     //滚动条锚点
     $scope.jumper = function(key){
    	 if($location.hash()!==key){
    		 $location.hash(key);
    		 
    	 }else{
    		 $anchorScroll();
    		 
    	 }
    	 }
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
   //每30s刷新一次
     $scope.$on('$destroy',function(){
    	 $interval.cancel(timeout_upd);
     })
     var timeout_upd = $interval(function() {
			$scope.getDevList($scope.quereyData.page);
	}, 30*1000);
	
     $("#monitor_toggle").click(function () {
         if ($(this).is('.monitor_show')) {
             $(this).removeClass("monitor_show");
             $(this).addClass("monitor_hide");
             $(this).attr("title", "隐藏");
             $('.left_monitor').hide();
             $('.right_monitor').addClass("bwidth");
         }
         else {
             $(this).removeClass("monitor_hide");
             $(this).addClass("monitor_show");
             $(this).attr("title", "显示");
             $('.left_monitor').show();
             $('.right_monitor').removeClass("bwidth");
         }
     });
     $(function(){
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
     });
     if(document.body.clientWidth>=990) {
         $(function () {
             var Height = $(window).height() - 250;
             $(".left_monitor").css("height", Height + 30 + 'px');
             $(".monitor_left").css("height", Height + 30 + 'px');
             $(".monitor_body").css("minHeight", Height + 70 + 'px');
         });
         $(window).resize(function () {
             var Height = $(window).height() - 250;
             $(".left_monitor").css("height", Height + 30 + 'px');
             $(".monitor_left").css("height", Height + 30 + 'px');
             $(".monitor_body").css("minHeight", Height + 70 + 'px');
         })
         $(document).scroll(function () {
             var top = $(document).scrollTop();
             if (top <= 77) {
                 $(".monitor_left").css("top", "15px");
             }
             else {
                 $(".monitor_left").css("top", (top - 62) + 'px');
             }
         })
     }

     $scope.gotoLog=function(devId,attrId){
         $location.path("/module/logShow/"+devId+"/"+attrId);
     }
    
}