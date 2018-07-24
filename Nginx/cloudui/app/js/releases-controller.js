var releaseModule=angular.module('releases',['ngDraggable']);
// 生命周期管理
releaseModule.controller('lifecyclesManageCtrl',['$rootScope','$scope','$state','ngDialog','Notify','$http','$stateParams','$timeout',function($rootScope,$scope,$state,ngDialog,Notify,$http,$stateParams,$timeout){
  // 添加、编辑生命周期
  $scope.lifecycleFn=function(data){
  	  ngDialog.open({
		  template: 'app/views/dialog/lifecycle.html'+'?action='+(new Date().getTime()),
		  className: 'ngdialog-theme-default',
		  scope: $scope,
		  data:{data:data},
		  closeByDocument:false,
		  cache:false,
		  controller:'lifecycleCtrl'
	 });
  }

  // 查看生命周期阶段
  $scope.stageFn=function(data){
  	$scope.stageShow=true;
  	$scope.stage.isAddStage=false;
  	$scope.curLife=data;
  	$http.get('/cloudui/master/ws/releaselifecycle/stage'+'?v='+(new Date().getTime()),{
  		params:{
  			lifecycleId:data.id
  		}
  	}).
  	success(function(data){
  	   $scope.noStage=data.length>0?false:true;
       $scope.lifeData=data;
  	})
  }

   // 生命周期阶段拖拽排序
   $scope.dropComplete = function(index, obj){
        if(!obj){
           return false;
        }
        var idx = $scope.lifeData.indexOf(obj); 
        $scope.lifeData[idx] = $scope.lifeData[index];
        $scope.lifeData[index] = obj;
        
        $scope.sortstage=[];
        for(var i=0;i<$scope.lifeData.length;i++)
        {
          $scope.sortstage.push($scope.lifeData[i].id);
        }
         
        $http.put('/cloudui/master/ws/releaselifecycle/sortstage',$scope.sortstage).success(function(data){
           if(!data.result){
                 Notify.alert(
                     '<em class="fa fa-times"></em> '+data.message ,
                     {status: 'danger'}
                  ); 
                 $scope.getList();
           } 
        })
};

  // 生命周期列表
  $scope.getList=function(){
  	 $http.get('/cloudui/master/ws/releaselifecycle/lifecycle'+'?v='+(new Date().getTime()),{
	  	params:{
	  		name:$scope.searchval||''
	  	}
	  })
	  .success(function(data){
	  	  $scope.listoff=data.length>0?true:false;
	  	  $scope.warninginfo='暂无数据';
	      $scope.lifecycleList=data;
	   })
  }
  $scope.getList();

  // 搜索
   $scope.search=function()
   {
      $scope.getList();
   }

  // 删除生命周期
  $scope.delLifecycleFn=function(index,id){
      ngDialog.openConfirm({
            template:
                 '<p class="modal-header">您确定要删除此生命周期吗?</p>' +
                 '<div class="modal-body text-right">' +
                   '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
                   '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
                 '</button></div>',
           plain: true,
           className: 'ngdialog-theme-default'
      }) .then(
         function(){
          $rootScope.app.layout.isShadow=true;

          $http.delete('/cloudui/master/ws/releaselifecycle/lifecycle',{
	        params:{ids:id}
	      })
          .success(function(data){
              $rootScope.app.layout.isShadow=false;
              if(data.result)
              {
                 $scope.lifecycleList.splice(index,1);
              }else
              {
                 Notify.alert(
                     '<em class="fa fa-times"></em> '+data.message ,
                     {status: 'danger'}
                  ); 
               }
          })    
      })
  }

  // 生命周期
  $scope.stage={};
  // 添加生命周期阶段dom
  $scope.addStageCont=function(){
  	$scope.stage.isAddStage=true;
  	//console.log(angular.element('#stagelist').length)
  	//console.log(angular.element('#stagelist').height())
  	//$('#stagelist').scrollTop(100)
  	

  	/*setTimeout(function(){
  		angular.element('#stagelist').scrollTop(760)
  	})*/
  	//angular.element('#stagelist').scrollTop(angular.element('#stagelist').height());
  }

  // 添加生命周期阶段
  $scope.addStageFn=function(stagelist){
  	 
        if($scope.lifeData.length>0){
            var data={
				name:$scope.stage.name,
				description:$scope.stage.description,
				release_lifecycle_id: $scope.curLife.id,
		        pre_stage_id:$scope.lifeData[$scope.lifeData.length-1].id
		    }
        }else{
            var data={
				name:$scope.stage.name,
				description:$scope.stage.description,
				release_lifecycle_id: $scope.curLife.id
		    }
        }
        

		$http.post('/cloudui/master/ws/releaselifecycle/stage',data).success(function(data){
			
			$rootScope.app.layout.isShadow=false;
			
			if(data.result)
			{  
				$scope.stage.isAddStage=false; 
				$scope.stageFn($scope.curLife);
				$scope.stage={};
				 
			}else{
				Notify.alert(
					'<em class="fa fa-times"></em> '+data.message ,
				    {status: 'danger'}
				);
			}
		})
  }
 
}])
// 新建、编辑生命周期
releaseModule.controller('lifecycleCtrl',['$rootScope','$scope','Notify','ngDialog','$state','$http',function($rootScope,$scope,Notify,ngDialog,$state,$http){
    
    $scope.isEdit=$scope.ngDialogData.data?true:false;
  
    if($scope.isEdit){ // 编辑
       $scope.id=$scope.ngDialogData.data.id;
       $scope.name=$scope.ngDialogData.data.name;
       $scope.description=$scope.ngDialogData.data.description;
       $scope.method="put";
    }else{ // 添加    
       $scope.method="post";
    }

     $scope.url="/cloudui/master/ws/releaselifecycle/lifecycle";
  
     $rootScope.submitted = false; 

     $scope.submitFn=function(){

		$rootScope.submitted = true;
		
		$rootScope.app.layout.isShadow=true;

		if($scope.isEdit){ // 编辑
            var data={
            	id:$scope.id,
				name:$scope.name,
				description:$scope.description
		    }
		}else{ // 添加
			var data={
				name:$scope.name,
				description:$scope.description
		    }
		}

       $http[$scope.method]($scope.url,data).success(function(data){
			
			$rootScope.app.layout.isShadow=false;
			
			if(data.result)
			{ 
				ngDialog.close();
                $scope.getList();
			}else{
				Notify.alert(
					'<em class="fa fa-times"></em> '+$scope.message ,
				    {status: 'danger'}
				);
			}
		})
	}
}])
 

