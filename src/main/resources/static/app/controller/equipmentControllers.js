function equCtrl($scope, $http,$location, $rootScope) {
    //滚动置顶
    window.scrollTo(0, 0);

	//为后台请求参数 带分页数据
    $scope.quereyData={
        page:1, //当前页码 初始化为1
        size:defaultSize, //每页数据量 defaultSize全局变量
    };
	$scope.pages=0;
	$scope.total=0;
	$scope.loadCtl={
    		search:false,	
    		addEnq:false
    };
	$scope.data= null;
	$scope.seleItem={};
	$scope.checkboxList=[];
	$scope.edit_cmp=$rootScope.edit_cmp;
    $scope.init=function(){
    	$scope.loadCtl.search = true;
    	$http.post("service/getEquipments",$scope.quereyData).success(function(data) {
    		$scope.equipments = data.resultObj.list;
    		$scope.pages=data.resultObj.pages;
    		$scope.total=data.resultObj.total;
    		$scope.pageArr=data.resultObj.navigatepageNums;
    		$scope.quereyData.page=data.resultObj.pageNum;
    		//初始化选中框
    		for(var i=0;i<$scope.equipments.length;i++){
        		$scope.checkboxList[i]=false;
        	}
    		$scope.loadCtl.search = false;
    	});
    }
  //获取产品列表
    $scope.getProductsList = function(){
    	$http.post("service/getProductsList").success(function(data) {
    		if(data.resultObj == "errorMsg"){
    			swal(data.message, null, "error");
    		}else{
    			$scope.selProducts = data.resultObj;
    		}
    	});
    };
  //初始化
    $scope.init();
    $scope.getProductsList();
    //翻页
    $scope.changePage=function(page){
        $scope.quereyData.page=page;
        $scope.init();
    }
    $scope.resetSearch=function(){
        $scope.quereyData.eid=null;
        $scope.quereyData.type=null;
        $scope.quereyData.proId=null;
        $scope.quereyData.seleStatus=null;
        $scope.quereyData.page=1;
        $scope.pages=0;
        $scope.init();
    }
    //选中行
    $scope.selEquInfo=function(equ){
    	$scope.seleItem=equ;
    }
    //关闭详情框
    $scope.closeEquModal=function(){
    	$scope.seleItem={};
    }
    
    
    $scope.gotoEdit=function(devId,id){
    	$location.path("/module/equEdit/"+devId+"/"+id);
    }
    
	$scope.delEqu = function(entity){
		//console.log(eid)
		swal({   
            title: "是否确定删除该设备？",   
            type: "warning",   
            showCancelButton: true,   
            confirmButtonColor: "#DD6B55",   
            confirmButtonText: "确定删除",   
            cancelButtonText: "取消", 
            closeOnConfirm: false,   
            closeOnCancel: false 
        }, function(isConfirm){   
            if (isConfirm) {     
            	$scope.delEquById(entity);
            }  else {     
                swal("操作取消", null, "error");   
            } 
        });
		
	}
	$scope.delEquAll = function(){
		swal({   
			title: "是否确定删除所以选中设备？",   
			type: "warning",   
			showCancelButton: true,   
			confirmButtonColor: "#DD6B55",   
			confirmButtonText: "确定删除",   
			cancelButtonText: "取消", 
			closeOnConfirm: false,   
			closeOnCancel: false 
		}, function(isConfirm){   
			if (isConfirm) {     
				$scope.delEquByIdAll();
			}  else {     
				swal("操作取消", null, "error");   
			} 
		});
		
	}
	//全选
    $scope.selAll=function(){
    	if($scope.select_all) {
    		$scope.checkboxList=[];
            for(var i=0;i<$scope.equipments.length;i++){
        		$scope.checkboxList[i]=true;
        	}
        }else {
        	for(var i=0;i<$scope.equipments.length;i++){
        		$scope.checkboxList[i]=false;
        	}
        }
    }
	$scope.delEquById = function(entity){
		$http.post("service/delEqu",{eid:entity.id,devId:entity.devId}).success(function(data) {
				if(data.resultObj == "errorMsg"){
					swal(data.message, null, "error");
				}else{
					swal("删除成功", null, "success");
					$scope.init();
				}
			});
	}
	$scope.delEquByIdAll = function(){
		var postFalg = false;
		$scope.delEqus="";
		for(let i=0;i<$scope.checkboxList.length;i++){
			if($scope.checkboxList[i]){
				postFalg = true;
				$scope.delEqus +=$scope.equipments[i].id+",";
			}
		};
		if(!postFalg){
			swal("没有选中任何设备", null, "error");
			return;
		}
		$http.post("service/delEquAll",{equs:$scope.delEqus.substr(0,$scope.delEqus.length-1)}).success(function(data) {
			if(data.resultObj == "errorMsg"){
				swal(data.message, null, "error");
			}else{
				swal("删除成功", null, "success");
				$scope.init();
			}
		});
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
    $scope.exportLora=function () {
        $rootScope.downLoadFile("/service/exprLora" );
    };
    $scope.exportNbiot=function () {
    	$rootScope.downLoadFile("/service/exprNbiot" );
    };
    $scope.uploadSuccess=function (data) {
    	$http.post("service/getUploadResult",{reskey:data.resultObj}).success(function(data) {
    		console.log(data.resultObj);
    		if(data.resultObj.length!=0){
    			$scope.uploadRes = data.resultObj;
    			$scope.resetSearch();
    		}else{
    			swal("无数据导入", null, "error");
    		}
    	});
    }
    $scope.clearUploadModal=function () {
		$scope.uploadRes = null;
    }
    $scope.gotoCommandPost=function(devId){
        $location.path("/module/commandPost/"+devId);
    }
    $scope.gotoCommandLog=function(devId){
    	$location.path("/module/commandLog/"+devId);
    }
    $scope.gotoDataLog=function(devId){
    	$location.path("/module/logShow/"+devId);
    }
}