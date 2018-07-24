var releaseModule=angular.module('releaseIns',[]);
/* ------------------------------------审批管理------------------------------------ */
releaseModule.controller('releaseInsCtrl',['$rootScope','$scope','$http','ngDialog','Notify','$cookieStore','$filter','$state',function($rootScope,$scope,$http,ngDialog,Notify,$cookieStore,$filter,$state){
	$scope.searchTypeList=[
	               	    {text:'发布名称',value:'releaseName'},
	               	    {text:'发布描述',value:'description'},
	               	    {text:'发布周期',value:'lifecycleName'}
	               	  ];
  $scope.releasePages=[
     "app.release_ins"
  ]

  $scope.pageSize=10;
  $scope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
    var isClear=$filter('filter')($scope.releasePages,toState.name).length>0?false:true;
    if(isClear){
      $scope.clearCookie(); 
    }
  });

  /*查询发布列表*/
  var url='/cloudui/master/ws/releaseManage/listReleases';
  $scope.releaseName='';
  $scope.description='';
  $scope.lifecycleName='';
  $scope.searchType=$cookieStore.get('releaseInsSearchType')?$cookieStore.get('releaseInsSearchType'):"releaseName";
  $scope.searchval=$cookieStore.get('releaseManageSearch')?$cookieStore.get('releaseManageSearch'):'';
  if($cookieStore.get('releaseManagePageNum')){
	  	 var data={
	  			releaseName:$scope.releaseName,
	  			description:$scope.description,
	  			lifecycleName:$scope.lifecycleName,
	  			pageNum:1,
	  			pageSize:$scope.pageSize
  			}
	     $scope.listDataPromise=$http({
	            method  : 'GET',
	            url     : url+'?action='+(new Date().getTime()),   
	            params  : data 
	      }).success(function(res){
	        
	         $scope.totalPageNum=res.total;
	         
	         if($cookieStore.get('releaseManagePageNum')<=$scope.totalPageNum){
	            $scope.pageNum=$cookieStore.get('releaseManagePageNum');
	         }else{
	            $scope.pageNum=$scope.totalPageNum;
	         }

	     }) 
	 
	  }else{
	     $scope.pageNum=1;
	  }
  
  	$scope.$watch('pageNum',function(newval,oldval){
  		$scope.pageNum=newval;
  	})
	
	$scope.setCookie=function(){
		$cookieStore.put('releaseManagePageNum',$scope.pageNum);
		$cookieStore.put('releaseInsSearchType',$scope.searchType);
        $cookieStore.put('releaseManageSearch',$scope.searchval);
	}
	
	$scope.clearCookie=function(){
		$cookieStore.remove("releaseManagePageNum");
		$cookieStore.remove("releaseManageSearch");
		$cookieStore.remove("releaseInsSearchType");
	}
	
   $scope.sortOrder= 'DESC';
   $scope.sortName= 'updateTime';
   $scope.fnSort = function (arg) {
            arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
            $scope.sortOrder= arguments.callee['fnSort' + arg]?'ASC':'DESC';
            $scope.sortName =arg;
            $scope.onPageChange();
        }
   $scope.onPageChange = function ()
   {   
      if(!$scope.pageNum){
           return ;
      }
	  var data={
			  releaseName:$scope.releaseName,
	  		  description:$scope.description,
	  		  lifecycleName:$scope.lifecycleName,
			  pageNum:$scope.pageNum,
	          pageSize:$scope.pageSize,
	          sortOrder:$scope.sortOrder,
	          sortName:$scope.sortName  
  		}
	  $http({
          method  : 'GET',
          url     : url+'?action='+(new Date().getTime()),   
          params  : data  
    }).success(function(data){
       $scope.releaseList = data.rows;
       $scope.listoff=data.total>0?true:false;
       $scope.warninginfo='提示：暂无数据';
       $scope.pageCount=Math.ceil(data.total/$scope.pageSize);
       if($scope.pageCount==0)
       {
           $scope.pageCount=1;
       }
     }).error(function(){
         $scope.listoff=false;
         $scope.warninginfo='暂无结果';
      })
    };
    
    $scope.search=function()
    {
    	$scope.searchValFn();
    	$scope.pageNum=1;
    	$scope.setCookie();
 	    $scope.onPageChange(); 
    }
    
    $scope.searchValFn=function(){
        if($scope.searchType=='releaseName'){ // 按发布名称搜索
      	  $scope.releaseName=$scope.searchval;
      	  $scope.description='';
      	  $scope.lifecycleName='';
       }else if($scope.searchType=='description'){ // 按发布描述搜索
      	  $scope.releaseName='';
     	  	  $scope.description=$scope.searchval;
     	  	  $scope.lifecycleName='';
       }else if($scope.searchType=='lifecycleName'){ // 按生命周期名称搜索
      	 $scope.releaseName='';
     	  	 $scope.description='';
     	     $scope.lifecycleName=$scope.searchval;
       }
    }
    $scope.$watch('searchType',function(newval,oldval){
        if(newval&&(newval!==oldval)){
           $scope.search();
        }else if(newval&&(newval==oldval)){
          $scope.searchValFn();
        } 
    })
    
    $scope.lifecycles =new Array();
	//获取生命周期
	$http.get('/cloudui/master/ws/releaseManage/listLifecycles'+'?v=' + (new Date().getTime())).success(function(data){
			 angular.forEach(data.rows,function(val,key){
				 var map = {};
				 map['id']=val.id;
				 map['name']=val.name;
				 $scope.lifecycles.push(map);
			 });
	     });
  /*新增发布*/
    $scope.releaseAddFn=function(){
    	ngDialog.open({
    		template:'app/views/dialog/release-create.html'+'?action='+(new Date().getTime()),
    		className:'ngdialog-theme-default',
    		scope: $scope,
    		closeByDocument:false,
    		cache:false,
    		controller:'addReleaseCtrl'
    	});
    }
  /*修改发布*/
    $scope.updateRelease=function(param){
    	ngDialog.open({
    		template:'app/views/dialog/release-update.html'+'?action='+(new Date().getTime()),
    		className:'ngdialog-theme-default',
    		scope: $scope,
    		data:{info:param},
    		closeByDocument:false,
    		cache:false,
    		controller:'updateReleaseCtrl'
    	});
	}
  /*删除发布*/
   $scope.deleteRelease=function(id)
    {
    	ngDialog.openConfirm({
        template:
             '<p class="modal-header">您确定要删除此发布吗?</p>' +
             '<div class="modal-body text-right">' +
               '<button type="button" class="btn btn-default mr20"  ng-click="closeThisDialog(0)">取消' +
               '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
             '</button></div>',
       plain: true,
       className: 'ngdialog-theme-default'
       }).then(function(){
    	   $rootScope.app.layout.isShadow=true;
           $http.delete('/cloudui/master/ws/releaseManage/deleteRelease',{
        	   	params:{
        	   		id:id
        	   	}
           }).success(function(data){
            	  $rootScope.app.layout.isShadow=false;
            	  if(data.result)
                  {
            	      
               	      Notify.alert(
                          '<em class="fa fa-check"></em>'+data.message ,
                          {status: 'success'}
                       );
               	       $scope.clearCookie();
               	    $state.go('app.release_ins',{},{ reload: true });
                  }else
                  {
               	     Notify.alert(
                          '<em class="fa fa-times"></em>'+data.message ,
                          {status: 'danger'}
                      );
                  }
              });
     
    })
   }
}])

/*-------------------------------------------新建发布------------------*/
releaseModule.controller('addReleaseCtrl',['$rootScope','$scope','$http','ngDialog','$state','Notify',function($rootScope,$scope,$http,ngDialog,$state,Notify){
    $scope.saveFn = function() {
        if($scope.addReleaseForm.$valid) {
           $http({
        	   method :'GET',
        	   url    :'/cloudui/master/ws/releaseManage/checkReleaseName'+'?action='+(new Date().getTime()),
        	   params :{
        		   releaseName:$scope.name
        	   }
           })
           .success(function(data){
			if(data.result){
              $http({
                method  : 'POST',
                url     : '/cloudui/master/ws/releaseManage/saveRelease',
                data    : $.param({
					      name:$scope.name,
                      	  description:$scope.description,
                      	  lifecycleId:$scope.selectedLifecycle.id
						}),  
                headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
              })
              .then(function(response) {
                $rootScope.app.layout.isShadow=false;
                if (response.data.result ) {
                  ngDialog.close();
				  $state.go('app.release_ins',{},{ reload: true });
                }else{
                   $scope.authMsg = '创建失败，请重新添加发布！';  
                }
              }, function(x) {
                $scope.authMsg = '服务器请求错误';
              });
        	   }else{
        		   Notify.alert(
                           '<em class="fa fa-times"></em> '+data.message ,
                           {status: 'danger'}
                        );
        	   }


		   })
         }
     else { 
       $scope.addReleaseForm.text.$dirty = true;
     }
   };
}])


/*-------------------------------------------更新发布------------------*/
releaseModule.controller('updateReleaseCtrl',['$rootScope','$scope','$http','ngDialog','$state','Notify','$filter',function($rootScope,$scope,$http,ngDialog,$state,Notify,$filter){
	$scope.id=$scope.ngDialogData.info.id;
	$scope.name=$scope.ngDialogData.info.releaseName;
	$scope.description=$scope.ngDialogData.info.description;
	$scope.lifecycleId=$scope.ngDialogData.info.lifecycleId;
	$scope.selectedLifecycle=$filter('filter')($scope.lifecycles,$scope.lifecycleId)[0];
	$scope.saveFn = function() {
        if($scope.updateReleaseForm.$valid) {
           $http({
        	   method :'GET',
        	   url    :'/cloudui/master/ws/releaseManage/checkReleaseName'+'?action='+(new Date().getTime()),
        	   params :{
        		   releaseName:$scope.name,
        		   id:$scope.id
        	   }
           })
           .success(function(data){
			if(data.result){
              $http({
            	  method  : 'PUT',
                  url     : '/cloudui/master/ws/releaseManage/updateRelease',
                  data    : $.param({
                	  		  id:$scope.id,
  					          name:$scope.name,
                        	  description:$scope.description,
                        	  lifecycleId:$scope.selectedLifecycle.id
  						}),  
                  headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
              })
              .then(function(response) {
                $rootScope.app.layout.isShadow=false;
                if (response.data.result ) {
                	Notify.alert(
	                          '<em class="fa fa-check"></em>'+response.data.message ,
	                          {status: 'success'}
	                       );
                  ngDialog.close();
                  $state.go('app.release_ins',{},{ reload: true });
                }else{
                   $scope.authMsg = '创建失败，请重新更新！';  
                }
              }, function(x) {
                $scope.authMsg = '服务器请求错误';
              });
        	   }else{
        		   Notify.alert(
                           '<em class="fa fa-times"></em> '+data.message ,
                           {status: 'danger'}
                        );
        	   }


		   })
         }
     else { 
       $scope.updateReleaseForm.text.$dirty = true;
     }
   };
}])

/*-------------------------------------------发布详情------------------*/
releaseModule.controller('releaseInsDetailCtrl',['$rootScope','$scope','$http','ngDialog','$state','Notify','$filter','$stateParams',function($rootScope,$scope,$http,ngDialog,$state,Notify,$filter,$stateParams){
	var releaseId=$stateParams.id;
	$scope.releaseId=releaseId;

  $scope.dataObj={};

	$http({
 	   method :'GET',
 	   url    :'/cloudui/master/ws/releaseManage/getReleaseDetail'+'?action='+(new Date().getTime()),
 	   params :{
 		   releaseId:releaseId
 	   }
    })
    .success(function(data){
       
    	$scope.appList=data.appList;
      if($scope.appList.length>0){
           $scope.dataObj.curApp=$scope.appList[0].resourceName;
           $scope.changeStageFn();
      }else{
           $scope.stageList=data.stageList;
      }
    	$scope.releaseInfo=data.releaseInfo;
	 })
	   
	$scope.delAppFn=function(index,app){
		{
	    	ngDialog.openConfirm({
	        template:
	             '<p class="modal-header">您确定要删除此应用吗?</p>' +
	             '<div class="modal-body text-right">' +
	               '<button type="button" class="btn btn-default mr20"  ng-click="closeThisDialog(0)">取消' +
	               '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
	             '</button></div>',
	       plain: true,
	       className: 'ngdialog-theme-default'
	       }).then(function(){
	    	   $rootScope.app.layout.isShadow=true;
	           $http.delete('/cloudui/master/ws/releaseManage/deleteReleaseApp',{
	        	   	params:{
	        	   		releaseId:app.releaseId,
	        	   		resourceId:app.resourceId
	        	   	}
	           }).success(function(data){
	            	  $rootScope.app.layout.isShadow=false;
	            	  if(data.result)
	                  {
	            	      
	               	      Notify.alert(
	                          '<em class="fa fa-check"></em>'+data.message ,
	                          {status: 'success'}
	                       );
	               	      $state.go('app.release_ins_detail',{id:$scope.releaseId},{reload:true});
	                  }else
	                  {
	               	     Notify.alert(
	                          '<em class="fa fa-times"></em>'+data.message ,
	                          {status: 'danger'}
	                      );
	                  }
	              });
	     
	    })
	   }
	}
	
	// 关联应用
    $scope.linkComponent=function(){
        ngDialog.open({
          template: 'app/views/dialog/release-component-link.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          scope: $scope,
          closeByDocument:false,
          cache:false,
          controller:'linkComponentCtrl'
        });
    }

    // 关联环境
    $scope.linkRealseEnv=function(releaseName,item,app){
        if(item.envInfo.blueInstanceId){
             Notify.alert(
                 '已关联应用环境，若需重新关联，请先解绑！' ,
                 {status: 'info'}
              );
        }else{
            var linkRealseEnvDialog=ngDialog.open({
              template: 'app/views/dialog/relation-env.html'+'?action='+(new Date().getTime()),
              className: 'ngdialog-theme-default ngdialog-sm',
              scope: $scope,
              data:{releaseName:releaseName,stage:item.name,stageId:item.id,app:app},
              closeByDocument:false,
              cache:false,
              controller:'relationEnvCtrl'
            });

            linkRealseEnvDialog.closePromise.then(function(){
              $scope.getStageListFn($scope.releaseInfo.releaseName,$scope.dataObj.curApp);
            })
        } 
    }
    // 变更过程
    $scope.editPhaseFlow=function(releaseName,item){
  
     if(item.envInfo.blueInstanceId){
          var editPhaseFlowDialog=ngDialog.open({
            template: 'app/views/dialog/edit-phase-flow.html'+'?action='+(new Date().getTime()),
            className: 'ngdialog-theme-default',
            scope: $scope,
            data:{releaseName:releaseName,blueInstanceId:item.envInfo.blueInstanceId,flowId:item.envInfo.flowId},
            closeByDocument:false,
            cache:false,
            controller:'editPhaseFlowCtrl'
          });

          editPhaseFlowDialog.closePromise.then(function(){
            $scope.getStageListFn($scope.releaseInfo.releaseName,$scope.dataObj.curApp);
          })

      }else{
          Notify.alert(
             '还没有关联的应用环境，请先关联应用环境！' ,
             {status: 'info'}
          );
      }   
    }

    // 解绑应用环境
    $scope.untieEnvFn=function(releaseName,item,app){
        if(item.envInfo.blueInstanceId){
             ngDialog.openConfirm({
              template:
                   '<p class="modal-header">您确定要解绑吗?</p>' +
                   '<div class="modal-body text-right">' +
                     '<button type="button" class="btn btn-default mr20"  ng-click="closeThisDialog(0)">取消' +
                     '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
                   '</button></div>',
             plain: true,
             className: 'ngdialog-theme-default'
             }).then(function(){
               $rootScope.app.layout.isShadow=true;

               $http({
                method:'put',
                url:"/cloudui/master/ws/releaseManage/relieveReleaseStageEnv",
                data:$.param(
                    {
                      releaseName:releaseName,
                      resourceName:app,
                      phaseId:item.id
                    }   
                ),
                headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
             }).success(function(data){
                  $rootScope.app.layout.isShadow=false;
                  if (data.result ) {
                     Notify.alert(
                        '<em class="fa fa-check"></em>'+data.message ,
                        {status: 'success'}
                     );
                     $scope.getStageListFn(releaseName,app);
                  }else{
                     Notify.alert(
                        '<em class="fa fa-times"></em>'+data.message ,
                        {status: 'danger'}
                     );
                  }
             })
             })
             
        }else{
            Notify.alert(
               '还没有关联的应用环境，请先关联应用环境！' ,
               {status: 'info'}
            );
        }
        
    }

    // 配置应用环境
    $scope.configEnvFn=function(item){
        if(item.envInfo.blueInstanceId){
            $state.go('app.blueprint_ins_component_configs',{insName:item.envInfo.blueInstanceName,INSTANCE_ID:item.envInfo.blueInstanceId},{reload:true});
        }else{
            Notify.alert(
               '还没有关联的应用环境，请先关联应用环境！' ,
               {status: 'info'}
            );
        }
    }

    // 发布日志
    $scope.logFn=function(item){
        if(item.envInfo.blueInstanceId){
            $state.go('app.blueprint_ins_log_manage',{insName:item.envInfo.blueInstanceName,INSTANCE_ID:item.envInfo.blueInstanceId},{reload:true});
        }else{
            Notify.alert(
               '还没有关联的应用环境，请先关联应用环境！' ,
               {status: 'info'}
            );
        }
    }

    // 获取发布阶段信息

    $scope.getStageListFn=function(releaseName,resourceName){
        $http.get('/cloudui/master/ws/releaseManage/getReleaseStageEnvs',{
            params:{
               releaseName:releaseName,
               resourceName:resourceName
            }
         }).success(function(data){
              $scope.stageList=data;
         })
    }

    $scope.changeStageFn=function(){
        $scope.$watch('dataObj.curApp',function(newval,oldval){
             if(newval){
                 $scope.getStageListFn($scope.releaseInfo.releaseName,newval);
             }
        })
    }
    
}])

/* ------------------------------------关联应用------------------------------------ */
releaseModule.controller('linkComponentCtrl',['$rootScope','$scope','$http','ngDialog','Notify','$state','$filter',function($rootScope,$scope,$http,ngDialog,Notify,$state,$filter){
  
  $scope.checkappId=[]; // 选中的应用
  $scope.checkData=[];
  
  $scope.linkPageSize=5;
  $scope.linkPageNum=1;
  // 工件列表
    $scope.onLinkPageChange = function ()
    {   
      $http.get('/cloudui/ws/resource/listResource'+'?v=' + (new Date().getTime()),{
          params:
            {
              pageNum:$scope.linkPageNum,
              pageSize:$scope.linkPageSize,
              registryId:3,
              resourceName:$scope.linkSearchval
            }
        }).success(function(data){

        angular.forEach(data.rows,function(val,key){
           var ischecked=$filter('filter')($scope.checkappId,val.id).length>0?true:false;
           data.rows[key].ischecked=ischecked;
           data.rows[key].isLink=false;
        })
         
        $scope.linkListoff=data.rows.length>0?true:false;
          $scope.linkWarninginfo='暂无数据';
          $scope.linkWorkpiecePackageList = data.rows;
          
          $scope.linkPageCount=Math.ceil(data.total/$scope.linkPageSize);
          if($scope.linkPageCount==0){
             $scope.linkPageCount=1;
          }
       }).error(function(){
         $scope.linkListoff=false;
         $scope.linkWarninginfo='暂无结果';
     })
    }
    
    //关联应用
    $scope.linkFn=function()
    {  
      var checkbox = $scope.checkappId;
        if(checkbox.length==0)
        {
           Notify.alert(
             '请选择要关联的应用！' ,
             {status: 'info'}
           );
        }else
        {
            var ids="";

            for(var i=0;i<checkbox.length;i++)
            { 
              if(i==0){
                ids=checkbox[i];
              }else{
                ids=ids+","+checkbox[i];
              }
            }
            $http({
                  method  : 'POST',
                  url     : '/cloudui/master/ws/releaseManage/addReleaseApps',
                  data    : $.param({
                          releaseId:$scope.releaseId,
                          resourceIds:ids
              }),  
                  headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
                })
                .then(function(response) {
                  $rootScope.app.layout.isShadow=false;
                  if (response.data.result ) {
                     Notify.alert(
                              '<em class="fa fa-check"></em>'+data.message ,
                              {status: 'success'}
                           );
                  }else{
                     Notify.alert(
                              '<em class="fa fa-times"></em>'+data.message ,
                              {status: 'danger'}
                          );
                  }
                }, function(x) {
                  $scope.authMsg = '服务器请求错误';
                });
         $state.go('app.release_ins_detail',{id:$scope.releaseId},{ reload: true });  
           ngDialog.close();
        }
    }
    
    // 搜素
    $scope.linkSearch=function()
    {
        $scope.linkPageNum=1;
        $scope.onLinkPageChange();
    }
}])

// 关联应用环境
releaseModule.controller('relationEnvCtrl',['$rootScope','$scope','$http','ngDialog','Notify','$state','$filter',function($rootScope,$scope,$http,ngDialog,Notify,$state,$filter){
    
    $scope.name=$scope.ngDialogData.app+'_'+$scope.ngDialogData.stage;

    // 蓝图模板列表
    $http.get('/cloudui/master/ws/blueprintTemplate/listBlueprintTemplatesByComponentName'+'?v=' + (new Date().getTime()),{
      params:
          {
            resourceName:$scope.ngDialogData.app
          }
     }).success(function(data){
        $scope.blueprintList=data;
        $scope.blueprint=$scope.blueprintList[0];
     })


     
     $scope.$watch('blueprint',function(newval,oldval){
         if(newval){

            // 蓝图下的资源列表
            var nodeDataArray=angular.fromJson(newval.INFO).nodeDataArray;
            $scope.resourceList=[];
            for(var i=0;i<nodeDataArray.length;i++){
               if(nodeDataArray[i].eleType=="resource"){
                  $scope.resourceList.push(nodeDataArray[i]);
               }
            }

            // 蓝图下过程列表
            $http.get('/cloudui/ws/blueprintTemplate/listBlueprintTemplateFlow',{
               params:{
                 blueprintId:newval.ID,
                 sortOrder:'DESC',
                 sortName:''
               }
            }).success(function(data){
               $scope.flowList=data;
               $scope.flow=$scope.flowList[0].cdFlowId;
            })
         }
     })

     // 环境配置
     $scope.relationEnvConfigFn=function(resource){
       var relationEnvConfigDialog=ngDialog.open({
          template: 'app/views/dialog/relation-env-config.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          scope: $scope,
          data:{resource:resource},
          closeByDocument:false,
          cache:false,
          controller:'relationEnvConfigCtrl'
        });
     } 
     

    $scope.saveEnvFn=function(){
  
       var resPoolConfig={};
       for(var i=0;i<$scope.resourceList.length;i++)
       {
        
         if(!$scope.resourceList[i].cluster){
             Notify.alert(
                '有资源没添加环境配置，请添加！',
                {status: 'success'}
              );
             var isContinue=false;
             break;
             
         }else{
             var isContinue=true;
         }

       }
     
      if(isContinue){
       
         for(var i=0;i<$scope.resourceList.length;i++)
         {
            resPoolConfig[$scope.resourceList[i].key]={};
            resPoolConfig[$scope.resourceList[i].key].clusterId=$scope.resourceList[i].cluster.id;
            resPoolConfig[$scope.resourceList[i].key].nodeIds=$scope.resourceList[i].nodesIdList;
         }
                 
         $http({
            method:'post',
            url:"/cloudui/master/ws/releaseManage/saveReleaseStageEnv",
            data:$.param(
                {
                  releaseName:$scope.ngDialogData.releaseName,
                  resourceName:$scope.ngDialogData.app,
                  blueprintInsName:$scope.name,
                  phaseId:$scope.ngDialogData.stageId,
                  blueprintId:$scope.blueprint.ID,
                  cdFlowId:$scope.flow,
                  resPoolConfig:angular.toJson(resPoolConfig)
                }   
            ),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
         }).success(function(data){
                if(data.result)
                { 
                    Notify.alert(
                      '<em class="fa fa-check"></em> '+data.message ,
                      {status: 'success'}
                    ); 
                    ngDialog.close();
                }else{
                  Notify.alert(
                    '<em class="fa fa-times"></em> '+data.message ,
                      {status: 'danger'}
                  );
                }
         })
      }   
   }
}])

// 环境配置
releaseModule.controller('relationEnvConfigCtrl',['$scope','ngDialog','$http','Notify',function($scope,ngDialog,$http,Notify){
    // 环境列表
    $http.get('/cloudui/master/ws/cluster/listAll'+'?v='+(new Date().getTime())).
    success(function(data){
        $scope.clusterList=data;
        $scope.cluster=$scope.clusterList[0];
    })
    // 环境下的主机列表
    $scope.$watch('cluster',function(newval,oldval){
        if(newval){

           $scope.checkappId=[]; 
         
           $scope.authMsg="";
           $http.get('/cloudui/master/ws/node/listAll'+'?v='+(new Date().getTime()),{
            params:{
              clusterId:newval.id
            }
           }).
           success(function(data){
               $scope.nodeList=data;
           })
        }
    })
    // 关闭窗口
    $scope.closeFn=function(closeThisDialog){
      closeThisDialog(0)
    }
    // 保存环境配置
    $scope.saveEnvConfigFn=function(closeThisDialog){
       // 选中节点
       var checkbox = $scope.checkappId;
       var nodesIdList=[];
       var nodesList=[];
      
       angular.forEach(checkbox,function(val,key){
          var obj={};

          var valData=val.split('_');
          obj.id=valData[0];
          obj.name=valData[1];
          obj.ip=valData[2];
      
          nodesIdList.push(obj.id);
          nodesList.push(obj);
       })
     
       if(nodesIdList.length==0){
         Notify.alert(
               '<em class="fa fa-check"></em> 请添加节点！' ,
               {status: 'info'}
            );
         return false;
       }

       $scope.ngDialogData.resource.cluster=$scope.cluster;
       $scope.ngDialogData.resource.nodesIdList=nodesIdList;
       $scope.ngDialogData.resource.nodesList=nodesList;
       closeThisDialog(0);
    }
}])

// 变更过程 
releaseModule.controller('editPhaseFlowCtrl',['$rootScope','$scope','$http','ngDialog','Notify','$state','$filter',function($rootScope,$scope,$http,ngDialog,Notify,$state,$filter){
     
    // 过程列表
    $http.get('/cloudui/master/ws/blueprint/getFlowsByInstance',{
     params:{
       blueprintInstanceId:$scope.ngDialogData.blueInstanceId
     }
    }).success(function(data){
      $scope.flowList=data;
      $scope.flow=$scope.ngDialogData.flowId;
    })

    $scope.saveFlowFn=function(){
       $http({
          method:'put',
          url:"/cloudui/master/ws/releaseManage/updateReleaseStageEnv",
          data:$.param(
              {
                releaseName:$scope.ngDialogData.releaseName,
                blueInstanceId:$scope.ngDialogData.blueInstanceId,
                cdFlowId:$scope.flow
              }   
          ),
          headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
       }).success(function(data){
            if(data.result)
            { 
                Notify.alert(
                  '<em class="fa fa-check"></em> '+data.message ,
                  {status: 'success'}
                ); 
                ngDialog.close();
            }else{
              Notify.alert(
                '<em class="fa fa-times"></em> '+data.message ,
                  {status: 'danger'}
              );
            }
        })
       
   }
}])

