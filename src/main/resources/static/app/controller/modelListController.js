
function modelListCtrl($scope, $http, $rootScope,$filter,$location) {
    //滚动置顶
    window.scrollTo(0, 0);

    $scope.pages=0;
    $scope.clear=function(){
        $scope.quereyData.modelName="";
    }

    //为后台请求参数 带分页数据
    $scope.quereyData={
        page:1, //当前页码 初始化为1
        size:defaultSize, //每页数据量 defaultSize全局变量
        modelName:""//其他默认的业务参数
    };
    $scope.pageArr=[1];//页码数组
    $scope.pages= $scope.pageArr.length; //总页数
    $scope.total=0;
    //初始化数据
    $scope.loading=false;
    $scope.init=function(){
        $scope.loading=true;
        $http.post("/service/queryModelList",$scope.quereyData).success(function(data) {
            $scope.data = data.resultObj.list;
            $scope.pages=data.resultObj.pages;
            $scope.total=data.resultObj.total;
            $scope.quereyData.page=data.resultObj.pageNum;
            $scope.loading=false;
        });
    }

    //初始化
    $scope.init();
    //翻页
    $scope.changePage=function(page){
        $scope.quereyData.page=page;
        $scope.init();
    }
    $scope.exportAll=function () {
        $rootScope.downLoadFile("/service/exportAllUserModel" );
    };

    $scope.exportModel=function (entity) {
        $rootScope.downLoadFile("/service/exportModel?id="+entity.id+"&name="+entity.modelName );
    };
   /* $scope.fileToUpload;
    $scope.sendFile = function(){
        var url = "/service/importModel",
            file = $scope.fileToUpload;
        if ( !file ) return;
        fileUpload.uploadFileToUrl( file, url );
    };*/

   $scope.uploadSuccess=function () {
       swal("导入成功", null, "success");
       $scope.init();
   }

    $scope.gotoModel=function(entity){
        $location.path("/module/modelEdit/"+entity.id);
    }

    $scope.addModel=function () {
        $location.path("/module/modelEdit/add");
    }



    $scope.deleteModel = function(entity){
        swal({
            title: "是否删除该模板数据？",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定删除",
            cancelButtonText: "取消",
            closeOnConfirm: false,
            closeOnCancel: false
        }, function(isConfirm){
            if (isConfirm) {
                $http.post("service/deleteModel",{"id":entity.id}).success(function(data) {
                    if(data.resultObj == "errorMsg"){
                        swal(data.message, null, "error");
                    }else{
                        swal("删除成功", null, "success");
                        $scope.init();
                    }
                });
            }  else {
                swal("操作取消", null, "error");
            }
        });

    }

}