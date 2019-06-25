
function logShowTableCtrl($scope, $http, $routeParams) {
    //滚动置顶
    window.scrollTo(0, 0);

    $scope.pages=0;
    //当前页
    $scope.curpage=1;
    //记录
    $scope.log=new Array();
    $scope.modelAttr=new Array();
    $scope.equ={};
    $scope.param={
        page:1, //当前页码 初始化为1
        size:defaultSize, //每页数据量 defaultSize全局变量
        devId:"",
        prodId:"",
    };
    $scope.pageArr=[1];//页码数组
    $scope.pages= $scope.pageArr.length; //总页数
    $scope.total=0;
    $scope.changePage =function(page){
        $scope.param.page=page;
        $scope.curpage=page;
        $scope.init();
    };


    $scope.fmtDate = function(date){
        var y = 1900+date.getYear();
        var m = "0"+(date.getMonth()+1);
        var d = "0"+date.getDate();
        return y+"-"+m.substring(m.length-2,m.length)+"-"+d.substring(d.length-2,d.length);
    };
    $scope.prodList=new Array();
    $scope.devList=new Array();

    $scope.param.end =$scope.fmtDate(new Date());
    var date = new Date();//获取当前时间
    date.setDate(date.getDate()-30);//设置天数 -30天
    $scope.param.start=$scope.fmtDate(date);

    //时间组件
    $("#datepickerStrat"). datepicker().on('changeDate', function () {
        $scope.param.start=$("#datepickerStrat").val();
        $scope.param.page=1;
        $scope.curpage=1;
        $scope.init();
    });
    $("#datepickerEnd"). datepicker().on('changeDate', function (e) {
        $scope.param.end =$("#datepickerEnd").val();
        $scope.param.page=1;
        $scope.curpage=1;
        $scope.init();
    });


    //初始化数据
    $scope.init=function(){
        $http.post("/service/queryEquAttrLogTable",$scope.param).success(function(data) {
            $scope.pages=0;
            $scope.log=[];
            if(!! data.resultObj.data){
                $scope.total=data.resultObj.data.total;
                $scope.param.page=data.resultObj.data.pageNum;
                if(!! data.resultObj.data.list){
                    $scope.log=data.resultObj.data.list;
                }
                if( data.resultObj.data.pages){
                    $scope.pages=data.resultObj.data.pages;
                }
            }
            if(!! data.resultObj.prodList){
                $scope.prodList = data.resultObj.prodList;
            }
            if(!! data.resultObj.devList){
                $scope.devList = data.resultObj.devList;
            }
            $scope.param.prodId=""+data.resultObj.prodId;
            $scope.param.devId=data.resultObj.devId;

            $scope.modelAttr=[];
            $scope.equ={};
            if(!! data.resultObj.equ){
                $scope.equ=data.resultObj.equ;
            }
            if(!! data.resultObj.modelAttr){
                $scope.modelAttr=data.resultObj.modelAttr;
            }

        });
    };

    //初始化
    $scope.init();

    $scope.changeProdId=function () {
        $scope.param.devId="";
        $scope.param.page=1;
        $scope.curpage=1;
        $scope.init();
    };

    $scope.changeDevId=function () {
        $scope.param.page=1;
        $scope.curpage=1;
        $scope.init();
    };


    $scope.getValue=function(item,index){
        if(index < $scope.modelAttr.length){
            return item[$scope.modelAttr[index].attrName];
        }
       return "";
    }


}