var statisticsModule=angular.module('statistics',[]);

/* ------------------------------------操作记录统计------------------------------------ */
statisticsModule.controller('recordStatisticsCtrl',['$scope','$http','Notify',function($scope,$http,Notify){
	
	$scope.warninginfo='提示：请点击查询相关数据！';

	// 蓝图实例列表
    $http.get('/cloudui/master/ws/dashboard/listAllBlueprintInstances'+'?v=' + (new Date().getTime())).success(function(data){
      $scope.blueprintInstancesList = data;
      $scope.blueprintInstancesList.unshift({'INSTANCE_NAME':'(全部)'});
      $scope.blueprint=data[0];
    })
		 
    
    $scope.$watch('blueprint',function(newval,oldval){
    	if(newval){
	      if(newval.INSTANCE_ID){
	    	 // 获取蓝图实例下流程列表
	         $http.get('/cloudui/master/ws/dashboard/getFlows'+'?v=' + (new Date().getTime()),{
	             params:
	                 {
	            	    blueInstanceId:newval.INSTANCE_ID,
	                 }
	         }).success(function(data){
	               $scope.flowList = data;
	               //$scope.flow=data[0];
	               $scope.flowList.unshift({'FLOW_NAME':'(全部)'});
	               
	               if(newval.flow){
		               $scope.flow=$filter('filter')($scope.flowList.flow,newval.flow)[0]; 
	               }else{
	            	   $scope.flow=$scope.flowList[0]; 
		           }
	         })
	      }else{
	    	  /*$scope.flowList.splice(0,$scope.flowList.length);
	    	  $scope.flow=$scope.flowList[0]; */
	    	  $scope.flowList=[{'FLOW_NAME':'(全部)'}];
	    	  $scope.flow=$scope.flowList[0];
	      }
	      if(newval.ID){
	    	// 获取蓝图实例下组件列表
	        $http.get('/cloudui/master/ws/dashboard/listApps'+'?v=' + (new Date().getTime()),{
	           params:
	               {
	                  id:newval.ID
	               }
	        }).success(function(data){
	             $scope.componentList = data;
	             //$scope.component=data[0];
	             $scope.componentList.unshift({'nodeDisplay':'(全部)'});
	             
	             if(newval.component){
	                 $scope.blueprint=$filter('filter')($scope.componentList.component,newval.component)[0]; 
	             }else{
	           	  	 $scope.component=$scope.componentList[0]; 
	             }
	        }) 
	        
	      }else{
	    	  /*$scope.componentList.splice(0,$scope.componentList.length);
	    	  $scope.component=$scope.componentList[0]; */
	    	  $scope.componentList=[{'nodeDisplay':'(全部)'}];
	    	  $scope.component=$scope.componentList[0];
	      }
    	}
    },true)
    
    // 操作列表
    $scope.doList=[
       {"text":"(全部)","value":""},
       {"text":"部署","value":"deploy"},
       {"text":"启动","value":"start"},
       {"text":"停止","value":"stop"},
       {"text":"卸载","value":"destroy"},
       {"text":"升级","value":"upgrade"},
       {"text":"回滚","value":"rollback"},
       {"text":"快照","value":"snapshot"}
    ]
    
      $scope.clickApply=function () {
          if (!this.startDate.isSame(this.endDate) && !this.startDate.isBefore(this.endDate)) {
          	Notify.alert( 
	               '开始时间不能晚于结束时间', 
	               {status: 'info'}
	           );
               return;
            };

        },

    $scope.op=$scope.doList[0].value;
    
    $scope.resultText=function(text){
    	return text?'成功':'失败';
    }
    
//    $scope.warninginfo='提示：请选择日期区间查询数据！';
    $scope.sortOrder= 'DESC';
    $scope.sortName= '';
    $scope.fnSort = function (arg) {
             arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
             $scope.sortOrder= arguments.callee['fnSort' + arg]?'ASC':'DESC';
             $scope.sortName =arg;
             $scope.onPageChange();
         }
    $scope.pageSize=10;
    $scope.querylog = function (){
       $scope.pageNum=1;
  	   $scope.onPageChange();
    }
    // 查询
    $scope.onPageChange = function ()
	{   
		 var daterange=$('.daterange').val();
		 
/*		 if(!daterange){
	         Notify.alert( 
	               '请先选择日期区间，再进行查询！', 
	               {status: 'info'}
	           );
	         return false;
	     }*/
		 
		 $scope.startdate=daterange.split(' - ')[0];
		 $scope.enddate=daterange.split(' - ')[1];
 

 
            
          var blueInstanceName=$scope.blueprint.INSTANCE_NAME=='(全部)'?'':$scope.blueprint.INSTANCE_NAME;
          var blueInstanceId=$scope.blueprint.INSTANCE_NAME=='(全部)'?'':$scope.blueprint.INSTANCE_ID;
          var flowName=$scope.flow.FLOW_NAME=='(全部)'?'':$scope.flow.FLOW_NAME;
          var flowId=$scope.flow.FLOW_NAME=='(全部)'?'':$scope.flow.FLOW_ID;
          var componentName=$scope.component.nodeDisplay=='(全部)'?'':$scope.component.componentName;
          var appName=$scope.component.nodeDisplay=='(全部)'?'':$scope.component.appName;
          var nodeDisplay=$scope.component.nodeDisplay=='(全部)'?'':$scope.component.nodeDisplay;
		  var op =$scope.op?$scope.op:'';

		 
		 $http.get('/cloudui/master/ws/dashboard/getRecords'+'?v=' + (new Date().getTime()),{
		     params:
		     {
		    	 nodeDisplay:nodeDisplay,
		    	 appName:appName,
		    	 blueInstanceId:blueInstanceId,
		    	 blueInstanceName:blueInstanceName,
		    	 componentName:componentName,
		    	 endDate:$scope.enddate||'',
		    	 flowId:flowId,
		    	 flowName:flowName,
		    	 op:op,
		    	 pageNum:$scope.pageNum,
		         pageSize:$scope.pageSize,
		    	 startDate:$scope.startdate||'',
		         sortOrder:$scope.sortOrder||'DESC',
		         sortName:$scope.sortName||''
		     }
		 }).success(function(data){
			$scope.resultData=data;   
		    $scope.listoff=data.total>0?true:false;
		    $scope.warninginfo='提示：暂无数据！';
		    $scope.pageCount=Math.ceil(data.total/$scope.pageSize);
	         if($scope.pageCount==0)
	         {
	           $scope.pageCount=1;
	         }
		 }).error(function(){
		    $scope.listoff=false;
		    $scope.warninginfo='暂无结果！';
	     })
		};

}])

