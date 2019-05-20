
function commandPostCtrl($scope, $http,$timeout,$routeParams, $rootScope, $location) {
    //滚动置顶
    window.scrollTo(0, 0);

    $scope.list=new Array();
    $scope.checkboxList=[];
    $scope.list.push({commandEid:null});
    $scope.devId=$routeParams.devId;
    
    //初始化数据
    $scope.init=function(){
    	
        if( $scope.devId){
            $http.post("/service/queryCMDModelByDevlId",{"devId":$scope.devId}).success(function(data) {
                $scope.cmdlist = data.resultObj.cmdlst;
                $scope.equInfo = data.resultObj.info;
                for(var i=0;i<$scope.cmdlist.length;i++){
            		$scope.checkboxList[i]=false;
            	}
            });
        }
    }

    //初始化
    $scope.init();
    
    $scope.save=function(){
		var postFalg = false;
    	for(let i=0;i<$scope.checkboxList.length;i++){//typeof(entity.param) == "undefined" 
    		//判断是否勾选命令
    		if($scope.checkboxList[i]){
    			postFalg = true;
    			let entity =  $scope.cmdlist[i];
    			if('DATE_TYPE' == entity.dataType){
    				if(!entity.param){
    					swal(entity.attrName+"请正确输入时间日期", null, "error");
    					return;
    				}
    				if( 'object' == typeof(entity.param)){
    					entity.paramDate = $scope.date2String(entity.param);
    				}else{
    					let dateArr = entity.param.split(" ");
    					let dateStr = dateArr[0];
    					let dateEnd = dateArr[1];
    					var patternDate = /^((((19|20)(([02468][048])|([13579][26]))-02-29))|((20[0-9][0-9])|(19[0-9][0-9]))[/]((((0[1-9])|(1[0-2]))[/]((0[1-9])|(1\d)|(2[0-8])))|((((0[13578])|(1[02]))-31)|(((01,3-9])|(1[0-2]))-(29|30)))))$/;
    					var res = "";
    					if(patternDate.test(dateStr)){
    						res += $scope.replaceAll(dateStr,"/","");
    					}else{
    						swal(entity.attrName+"请正确输入时间日期，注意空格和符号/：是否正确", null, "error");
        					return;
    					}
    					var patternTime = /([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])/;
    					if(patternTime.test(dateEnd)){
    						res += $scope.replaceAll(dateEnd,":","");
    					}else{
    						swal(entity.attrName+"请正确输入时间日期，注意空格和符号/：是否正确", null, "error");
    						return;
    					}
    					entity.paramDate = res;
    				}
    			}
    			
    			if('SWITCH_TYPE' == entity.dataType){
    				if("undefined" == typeof(entity.param)){
    					swal(entity.attrName+"请选择开关状态", null, "error");
    					return;
    				}
    			}
    			
    			if('ENUMERATION_TYPE' == entity.dataType){
    				if("undefined" == typeof(entity.param) || entity.param == null){
    					swal(entity.attrName+"请选择枚举值", null, "error");
    					return;
    				}else{
    					entity.paramKey = entity.param.key;
    					entity.paramValue = entity.param.value;
    				}
    			}
    			
    			if('INT_TYPE' == entity.dataType){
    				if("undefined" == typeof(entity.param) || null == entity.param){
    					swal(entity.attrName+"请输入参数值", null, "error");
    					return;
    				}
    				let max = null;
    				if('TWO_UNSIGNED' == entity.valueType){
    					max = !entity.numMax ? 65536 : entity.numMax;
    				}else{
    					max = !entity.numMax ? 2147483647 : entity.numMax;
    				}
    				let min = !entity.numMin ? 0 : entity.numMin;
    				if(min > entity.param || entity.param > max){
    					swal(entity.attrName+"参数值超出范围", null, "error");
    					return;
    				}
    			}
    			
    			
    		}else{
    			continue;
    		}
    	}
    	if(!postFalg){
			swal("没有可下发命令", null, "error");
			return;
		}
    	$scope.loading=true;
    	$http.post("service/postCommandList",
    			{"info":$scope.equInfo,
	    		"selectlist":$scope.checkboxList,
	    		"cmdlist":$scope.cmdlist}).success(function(data) {
			if(data.resultObj == "errorMsg"){
				swal(data.message, null, "error");
			}else{
				swal("已发送", null, "success");
			}
			$scope.loading=false;
		});
    }
    //全选
    $scope.selAll=function(){
    	if($scope.select_all) {
    		$scope.checkboxList=[];
            for(var i=0;i<$scope.cmdlist.length;i++){
        		$scope.checkboxList[i]=true;
        	}
        }else {
        	for(var i=0;i<$scope.cmdlist.length;i++){
        		$scope.checkboxList[i]=false;
        	}
        }
    }
    //取消按钮
    $scope.cancel=function(){
    	$location.path("/module/equView");
    }
    //input-date 日期格式化
    $scope.date2String = function(date){ 
    	  let year = date.getFullYear(); 
    	  let month =(date.getMonth() + 1).toString(); 
    	  let day = (date.getDate()).toString();  
    	  let hour = (date.getHours()).toString();  
    	  let minute = (date.getMinutes()).toString();  
    	  let second = (date.getSeconds()).toString();  
    	  if (month.length == 1) { 
    	      month = "0" + month; 
    	  } 
    	  if (day.length == 1) { 
    	      day = "0" + day; 
    	  }
    	  if (hour.length == 1) { 
    		  hour = "0" + hour; 
    	  }
    	  if (minute.length == 1) { 
    		  minute = "0" + minute; 
    	  }
    	  if (second.length == 1) { 
    		  second = "0" + second; 
    	  }
    	  var dateTime = year  + month  + day + hour + minute + second;
    	  return dateTime; 
    }
    //替换全部字符
    $scope.replaceAll = function (str,FindText, RepText) {
    	        let regExp = new RegExp(FindText, 'g');
    	        return str.replace(regExp, RepText);
    }
}