wechatApp.controller("ListController", function($scope, $http, $rootScope, $timeout) {
	$scope.pages=0;
    $scope.fmtDate = function(date){
        var y = 1900+date.getYear();
        var m = "0"+(date.getMonth()+1);
        var d = "0"+date.getDate();
        return y+"-"+m.substring(m.length-2,m.length)+"-"+d.substring(d.length-2,d.length);
    }


    //为后台请求参数 带分页数据
    $scope.quereyData={
        page:1, //当前页码 初始化为1
        size:10000, //每页数据量 defaultSize全局变量
        devId:"",
        attrId:"",
        status:""  //默认查全部
    };


    $scope.alarmStatus='';
    $scope.record='';
    $scope.select={};

    $scope.attrList=new Array();
    $scope.devList=new Array();
    $scope.data=new Array();

    $scope.closeAlarm=function () {
        $scope.alarmStatus='A';
        $scope.record='';
        //关闭
    }
    $scope.selectAlarm=function (entity) {
        $scope.select=entity;
        $scope.alarmStatus=entity.data_status;
        $scope.record=entity.record;
        createAlertDialog();
    }
    $scope.updateAlarm=function () {
        $http.post("/service/updateAlarm",{"status":$scope.alarmStatus,"record":$scope.record,"id":$scope.select.id}).success(function(data) {
            //初始化
            $scope.init();
            $scope.closeAlarm();
        });
    }

    //初始化数据
    $scope.init=function(){
    	$rootScope.loadingModal.show();
        $http.post("/service/queryAlarmLog",$scope.quereyData).success(function(data) {
            $scope.quereyData.initOption='FALSE';
            $scope.data = data.resultObj.data.list;
            $scope.pages=data.resultObj.data.pages;
            $scope.total=data.resultObj.data.total;
            $scope.quereyData.page=data.resultObj.data.pageNum;
            if(data.resultObj.devList){
                $scope.devList=data.resultObj.devList;
            }
            if(data.resultObj.attrList){
                $scope.attrList=data.resultObj.attrList;
            }
            if(data.resultObj.devId){
                $scope.quereyData.devId=data.resultObj.devId;
            }
            if(data.resultObj.attrId){
                $scope.quereyData.attrId=data.resultObj.attrId;
            }
            $rootScope.loadingModal.hide();
        });
    }

    //初始化
    $scope.init();
    $scope.changeDevId=function () {
        $scope.quereyData.attrId="";
        $scope.init();
    };

    $scope.changeAttr=function(){
        $scope.init();
    };
    $scope.changeA=function(value){
    	$scope.alarmStatus=value;
    };
    $scope.changeRecord=function(value){
    	console.log(value);
    	$scope.record=value;
    };
    
    
	this.delegate = {
		      configureItemScope: function(index, itemScope) {
		        itemScope.item = $scope.data[index];
		      },
		      countItems: function() {
		        return $scope.data.length;
		      },
		      calculateItemHeight: function() {
		        return ons.platform.isAndroid() ? 48 : 44;
		      }
		    };
	this.showDialog = function(entity) {
		$scope.select=entity;
        $scope.alarmStatus=entity.data_status;
        $scope.record=entity.record;
	      if (this.dialog) {
	        this.dialog.show();
	      } else {
	        ons.createElement('wechat/modules/dialog.html', { parentScope: $scope, append: true })
	          .then(function(dialog) {
	            this.dialog = dialog;
	            dialog.show();
	          }.bind(this));
	      }
	    }.bind(this);
	
  })