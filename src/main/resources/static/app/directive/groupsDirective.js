demoApp.directive('treeView',[function(){
	 
    return {
    	restrict: 'E',
        templateUrl: 'treeView.html',
        scope: {
            treeData: '=',
            addGroup: '&',
            editGroup: '&',
            delGroup: '&'
        },
        controller:function($scope,$rootScope){
        	if($rootScope.edit_cmp){
        		$scope.edit_cmp=true;
        	}
        	$scope.isLeaf = function(item){
                return !item.children || !item.children.length;
            };
            $scope.toggleExpandStatus = function(item){
                item.isExpand = !item.isExpand;
            };
            $scope.warpCallback = function(callback, item, $event){
				  ($scope[callback] || angular.noop)({
					 $item:item,
					 $event:$event
				 });
            };
        }
    };
}]);