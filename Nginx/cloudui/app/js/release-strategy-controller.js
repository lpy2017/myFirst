var releaseStrategyModule=angular.module('release-strategy',[]);
/* ------------------------------------发布策略管理------------------------------------ */
releaseStrategyModule.controller('releaseStrategyManageCtrl',['$rootScope','$scope','$http','ngDialog','$state','Notify','$filter',function($rootScope,$scope,$http,ngDialog,$state,Notify,$filter){
   
   $scope.checkappId=[]; // 选中的策略

   // 任务状态
   $scope.statusList=$rootScope.statusList.concat([]);
   $scope.statusList.unshift({'text':'(全部)','value':'00'});
   $scope.taskStatus="00";

   $scope.sortOrder= 'DESC';
   $scope.sortName= '';
   $scope.fnSort = function (arg) {
	    arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
	    $scope.sortOrder= arguments.callee['fnSort' + arg]?'ASC':'DESC';
	    $scope.sortName =arg;
	    $scope.onPageChange();
   }

   $scope.pageSize=10;

   // 列表
   $scope.onPageChange = function ()
   {   
   	   var taskStatus=$scope.taskStatus=="00"?"":$scope.taskStatus;
       $http.get('/cloudui/master/ws/ApplicationReleaseStrategy',{
       	  params:{
             name:$scope.name||'',
             appName:$scope.appName||'',
             taskStatus:taskStatus,
             sortName:$scope.sortName,
             sortOrder:$scope.sortOrder,
             pageSize: $scope.pageSize,
             pageNum: $scope.pageNum
       	  }
       }).success(function(data){

       	    angular.forEach(data.rows,function(val,key){
	           var ischecked=$filter('filter')($scope.checkappId,val.id).length>0?true:false;
	           data.rows[key].ischecked=ischecked;
	        })

       	    $scope.listoff=data.rows.length>0?true:false;
	        $scope.warninginfo='暂无数据';
	        $scope.strategyList = data.rows;
	        $scope.pageCount=Math.ceil(data.total/$scope.pageSize);
	        if($scope.pageCount==0){
	           $scope.pageCount=1;
	        }

       }) 
   }

   // 查询
   $scope.querylog=function(){
       $scope.pageNum=1;
       $scope.onPageChange();
   }

   // 新增策略
    $scope.strategyAddFn=function(){
         ngDialog.open({
           template: 'app/views/dialog/release-strategy-add.html'+'?action='+(new Date().getTime()),
           className: 'ngdialog-theme-default ngdialog-sm',
           scope: $scope,
           cache:false,
           closeByDocument:false,
           controller:"strategyAddCtrl"
         });
    }

    // 编辑策略
    $scope.strategyEditFn=function(item){
         ngDialog.open({
           template: 'app/views/dialog/release-strategy-edit.html'+'?action='+(new Date().getTime()),
           className: 'ngdialog-theme-default ngdialog-sm',
           scope: $scope,
           cache:false,
           closeByDocument:false,
           data:{strategyInfo:item},
           controller:"strategyEditCtrl"
         });
    }

   // 删除策略
   $scope.openDelStrategyFn=function(params,index){
       if(params)
       {
       	  var ids=[];
       	  ids.push(params);
          $scope.delStrategyFn(ids,index);
       }else
       {
          var checkbox = $scope.checkappId;
          if(checkbox.length==0)
          {
             Notify.alert(
                '请选择您要删除的策略!' ,
                {status: 'info'}
             );
          }else
          {
             $scope.delStrategyFn(checkbox);
          }
       }
   } 

   $scope.delStrategyFn=function(params)
    {
      ngDialog.openConfirm({
          template:
              '<p class="modal-header">您确定要删除吗?</p>' +
              '<div class="modal-body text-right">' +
                '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
                '<button type="button" class="btn btn-success" ng-click="confirm(1)">确定' +
              '</button></div>',
          plain: true,
          closeByDocument:false,
          className: 'ngdialog-theme-default'
      })
      .then(
      function(){ 

           $http.delete('/cloudui/master/ws/ApplicationReleaseStrategy',{
           	  params:{
           	  	ids:angular.toJson(params)
           	  }
           }).success(function(data){
               if(data.result)
               {
	                 Notify.alert(
	                   '<em class="fa fa-check"></em> 删除成功！' ,
	                   {status: 'success'}
	                 );
                    $state.go('app.release_strategy_manage',{},{reload:true});
               }else
               {
	                 Notify.alert(
	                    '<em class="fa fa-times"></em> '+data.message ,
	                    {status: 'danger'}
	                 );
               }
            })  
         }
      )
  }

}])

// 新增策略
releaseStrategyModule.controller('strategyAddCtrl',['$rootScope','$scope','$http','Notify','ngDialog','$state',function($rootScope,$scope,$http,Notify,ngDialog,$state){
   // 任务状态
   $scope.statusList=$rootScope.statusList.concat([]);
   $scope.task_status=$scope.statusList[0].value;

   // 组件列表
   $http.get("/cloudui/ws/resource/listAllResource",{
   	    params:{
	  		resourceName:''
	  	}
   }).success(function(data){
       $scope.resourceList=data;
       $scope.app_id=$scope.resourceList[0].id;
   })

   // 提交
   $scope.saveFn=function(){
   	   $http.post('/cloudui/master/ws/ApplicationReleaseStrategy',{
   	   	   name:$scope.name,
   	   	   app_id:$scope.app_id,
   	   	   task_status:$scope.task_status,
   	   	   cron:$scope.cron
   	   }).success(function(data){
          if(data.result){
             ngDialog.close();
             $state.go('app.release_strategy_manage',{},{reload:true});
          }else{
              Notify.alert(
                '<em class="fa fa-times"></em> '+data.message ,
                {status: 'danger'}
              );
          }
   	   })
   }
}])

// 编辑策略
releaseStrategyModule.controller('strategyEditCtrl',['$rootScope','$scope','$http','Notify','ngDialog','$state',function($rootScope,$scope,$http,Notify,ngDialog,$state){
   $scope.strategyInfo=$scope.ngDialogData.strategyInfo;
   $scope.name=$scope.strategyInfo.name;
   $scope.cron=$scope.strategyInfo.cron;

   // 任务状态
   $scope.statusList=$rootScope.statusList.concat([]);
   $scope.task_status=$scope.strategyInfo.task_status;

   // 组件列表
   $http.get("/cloudui/ws/resource/listAllResource",{
   	    params:{
	  		resourceName:''
	  	}
   }).success(function(data){
       $scope.resourceList=data;
       $scope.app_id=$scope.strategyInfo.app_id;
   })

   // 提交
   $scope.updateFn=function(){
   	   $http.put('/cloudui/master/ws/ApplicationReleaseStrategy',{
   	   	   id:$scope.strategyInfo.id,
   	   	   name:$scope.name,
   	   	   app_id:$scope.app_id,
   	   	   task_status:$scope.task_status,
   	   	   cron:$scope.cron
   	   }).success(function(data){
          if(data.result){
             ngDialog.close();
             $state.go('app.release_strategy_manage',{},{reload:true});
          }else{
              Notify.alert(
                '<em class="fa fa-times"></em> '+data.message ,
                {status: 'danger'}
              );
          }
   	   })
   }
}])

// 验证cron表达式
releaseStrategyModule.directive('validcron', function($http) {
    return {
        require : 'ngModel',
        link : function(scope, elm, attrs, ctrl) {
            elm.bind('keyup', function() {
                $http({method: 'GET', url: '/cloudui/master/ws/quartz/checkCronExpression'+'?v='+(new Date().getTime())+'&cronExpression='+scope.cron||''}).
                success(function(data, status, headers, config) {
                    if(data){
                        ctrl.$setValidity('cron',true);
                    }else{
                        ctrl.$setValidity('cron',false);
                    }
                }).
                error(function(data, status, headers, config) {
                    ctrl.$setValidity('cron', false);
                });
            });
        }
    };
});