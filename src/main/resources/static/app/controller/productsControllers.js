function productsCtrl($scope, $http, $rootScope) {
	//滚动置顶
	window.scrollTo(0, 0);

	$scope.equStatus={
	        "N":"在线", 
	        "F":"故障", 
	        "B":"离线", 
	    };
	//为后台请求参数 带分页数据
    $scope.quereyData={
        page:1, //当前页码 初始化为1
        size:defaultSize, //每页数据量 defaultSize全局变量
    };
	$scope.pages=0;
	$scope.loadCtl={
    		search:false,	
    		addEnq:false
    };
	$scope.addFrom = {};
	$scope.chkCmp = $rootScope.user.competence;
	$scope.infoEditable = true;
	 //获取模板列表
    $scope.seleUserModelLst = function(){
    	$http.post("service/queryUserModelById",{uid:$rootScope.user.id}).success(function(data) {
    		if(data.resultObj == "errorMsg"){
    			swal(data.message, null, "error");
    		}else{
    			$scope.selUserModel = data.resultObj;
    		}
    	});
    };
    //获取应用列表
    $scope.seleAppLst = function(){
		$http.get("service/getAppSeleList").success(function(data) {
			if(data.resultObj == "errorMsg"){
    			swal(data.message, null, "error");
    		}else{
    			$scope.selApps = data.resultObj;
    		}
		});
	};
	//获取用户列表
	$scope.seleCompanyLst = function(){
		$http.get("service/getCompanySeleList").success(function(data) {
			if(data.resultObj == "errorMsg"){
				swal(data.message, null, "error");
			}else{
				$scope.selCompanys = data.resultObj;
			}
		});
	};
	
    $scope.init=function(){
    	$scope.loadCtl.search = true;
    	$http.post("service/queryProducts",$scope.quereyData).success(function(data) {
    		$scope.products = data.resultObj.list;
    		$scope.pages=data.resultObj.pages;
    		$scope.pageArr=data.resultObj.navigatepageNums;
    		$scope.quereyData.page=data.resultObj.pageNum;
    		$scope.loadCtl.search = false;
    	});
    }
  //初始化
    $scope.init();
    $scope.seleUserModelLst();
    $scope.seleAppLst();
    $scope.seleCompanyLst();

    //翻页
    $scope.changePage=function(page){
        $scope.quereyData.page=page;
        $scope.init();
    }
    $scope.resetSearch=function(){
        $scope.quereyData.name=null;
        $scope.quereyData.appId=null;
        $scope.quereyData.modelId=null;
        $scope.quereyData.page=1;
        $scope.pages=0;
        $scope.init();
    }
    
    $scope.addEqu = function() {
    	$scope.loadCtl.addEnq = true;
    	if(!$scope.addFrom.name || !$scope.addFrom.typeCd ||
    			!$scope.addFrom.modelId || 
    			($scope.addFrom.typeCd=="N" && 
    					(!$scope.addFrom.appId || 
    					!$scope.addFrom.appModel || 
    					!$scope.addFrom.deviceType || 
    					!$scope.addFrom.manufacturerId)) ){
    		swal("请填写所有必填项", null, "error");
    		$scope.loadCtl.addEnq = false;
    		return;
    	}
        $http.post("service/editProduct",$scope.addFrom).success(function(data) {
			    	if(data.resultObj == "errorMsg"){
			    		swal(data.message, null, "error");
			        }else{
			        	//修改成功后
			        	swal("保存成功", null, "success");
			        	$scope.init();
			        }
			    	$scope.loadCtl.addEnq = false;
			    	$scope.closeAddEquModal();
        });
		
    };
    
    $scope.editChg = function(type){
    	$scope.addFrom = {};
    	$scope.addFrom.edit = type;
	};
	$scope.selProdut = function(pro){
		$scope.addFrom = pro;
		$scope.addFrom.edit = "U";
	};
    
    $scope.closeAddEquModal = function(){
		$("#close-add-equ-modal").click();
	};
    
	$scope.delEqu = function(eid){
		swal({   
            title: "是否确定删除该产品？",   
            type: "warning",   
            showCancelButton: true,   
            confirmButtonColor: "#DD6B55",   
            confirmButtonText: "确定删除",   
            cancelButtonText: "取消", 
            closeOnConfirm: false,   
            closeOnCancel: false 
        }, function(isConfirm){   
            if (isConfirm) {     
            	$scope.delEquById(eid);
            }  else {     
                swal("操作取消", null, "error");   
            } 
        });
		
	}
	
	$scope.delEquById = function(eid){
		$scope.currDel = eid;
		$http.post("service/deleteProductsById",
				{eid:$scope.currDel}).success(function(data) {
				if(data.resultObj == "errorMsg"){
					swal(data.message, null, "error");
				}else{
					swal("删除成功", null, "success");
					$scope.init();
				}
			});
	}
	

	
	
	
	
	
	
	
	
}