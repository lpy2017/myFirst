var componentModule=angular.module('component',[]);

componentModule.controller('componentManageCtrl',['$rootScope','$scope','$state','$location','$filter','ngDialog','Notify','$http','$cookieStore',function($rootScope,$scope,$state,$location,$filter,ngDialog,Notify,$http,$cookieStore){
	
  $scope.componentPages=[
      "app.component_manage",
      "app.component_detail",
      "app.component_flow_create",
      "app.component_flow_manage",
      "app.component_flow_update",
      "app.component_version_add",
      "app.component_version_update",
      "app.component_version_clone",
      "app.component_version_template_mapping"
  ]

  $scope.pageSize=5;

  $scope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
    var isClear=$filter('filter')($scope.componentPages,toState.name).length>0?false:true;
    if(isClear){
      $scope.clearCookie(); 
    }
  });

  $scope.sortOrder= 'DESC';
  $scope.sortName= '';
  $scope.fnSort = function (arg,order) {
           arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
           $scope.sortOrder= order;
           $scope.sortName =arg;
           $scope.onPageChange();
       }

  $scope.searchval=$cookieStore.get('componentSearch')?$cookieStore.get('componentSearch'):'';

  if($cookieStore.get('componentPageNum')){
     $scope.listDataPromise=$http.get('/cloudui/ws/resource/listResource?v=' + (new Date().getTime()),{
         params:
           {
             pageNum:1,
             pageSize:$scope.pageSize,
             registryId:3,
             resourceName:$scope.searchval,
             sortOrder:$scope.sortOrder||'DESC',
             sortName:$scope.sortName||''
           }
         }).then(function(res){
        
         $scope.totalPageNum=res.data.totalPageNum;
         
         if($cookieStore.get('componentPageNum')<=$scope.totalPageNum){
            $scope.pageNum=$cookieStore.get('componentPageNum');
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
	
	$scope.setCookie=function(val){
		$cookieStore.put('componentPageNum',$scope.pageNum);
    $cookieStore.put('componentSearch',$scope.searchval);
	}
	
	$scope.clearCookie=function(){
		$cookieStore.remove("componentPageNum");
		$cookieStore.remove("componentSearch");
	}
	  // 组件列表
	  
	 var picname=['tomcat','apache','nginx','haproxy','mysql','mongodb','redis',
	         	  'hbase','zookeeper','kafka','lvs','weblogic','oracle','db2','was','quartz','node','jdk','informix'];   
	           
      $scope.onPageChange = function ()
      {

        if(!$scope.pageNum){
           return ;
        }
    	 
        $scope.loadoff=true;
        var url = "/cloudui/ws/resource/listResource";
        
        $http.get(url + '?v=' + (new Date().getTime()),{
         params:
           {
             pageNum:$scope.pageNum,
             pageSize:$scope.pageSize,
             registryId:3,
             resourceName:$scope.searchval,
             sortOrder:$scope.sortOrder||'DESC',
             sortName:$scope.sortName||''
           }
         }).success(function(data){
            $scope.loadoff=false;
            $scope.resultoff=data.rows.length>0?true:false;
            $scope.warninginfo='提示：暂无数据！';
            
            if($scope.resultoff){
              for(var i=0;i<data.rows.length;i++)
              {
                 if(data.rows[i].icon){
                     data.rows[i].pic= angular.lowercase(data.rows[i].icon);
                 }else{
                    var pic=''
                    for(var j=0;j<picname.length;j++)
                    {
                      if(angular.lowercase(data.rows[i].resourceName).indexOf(picname[j])!==-1)
                      {
                        if(picname[j]=='node'){
                           var pic='nodeagent'
                        }else{
                          var pic=picname[j];
                        }
                      }
                    }

                    if(!pic){
                      pic='custom' 
                    }
                   
                    data.rows[i].pic=pic;
                 }
                 
                 
                 if(!data.rows[i].updateTime){
             	   data.rows[i].updateTime='无更新时间';
                 }
                 
                 if(!data.rows[i].versionName){
             	   data.rows[i].versionName='无';
                 }
               } 
               $scope.ftplist=data;
            }else{
               $scope.ftplist=data; 
            }
            
            $scope.pageCount=Math.ceil(data.total/$scope.pageSize);
            
            if($scope.pageCount==0){
              $scope.pageCount=1;
            }
        }).error(function(){
           $scope.resultoff=false;
           $scope.warninginfo='暂无结果';
        })
	 }
      
	 //搜素组件 
	 $scope.search=function()
	 {
		 $scope.pageNum=1;
		 $scope.setCookie();
		 $scope.onPageChange(); 
	 }
	 
	 // 创建组件
     $scope.addComponentFn=function(){
		ngDialog.open({
          template: 'app/views/dialog/component-create.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          scope: $scope,
          closeByDocument:false,
          cache:false,
          controller:'createComponentCtrl'
        });
    }
	          
	 // 删除组件
	 $scope.delComponent=function(param,index){
	    ngDialog.openConfirm({
          template:
             '<p class="modal-header">您确定要删除此组件?</p>' +
             '<div class="modal-body text-right">' +
               '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
               '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
             '</button></div>',
          plain: true,
          className: 'ngdialog-theme-default'
	    }).then(function(){
	          $rootScope.app.layout.isShadow=true;
            $scope.setCookie();
	          $http.delete('/cloudui/master/ws/resource/deleteNewResource'+'?v='+(new Date().getTime()),{
                params:{
             	   resourceId:param
                }
	          }).success(function(data){
	              $rootScope.app.layout.isShadow=false;
	              if(data.result)
	              {
	                   Notify.alert(
	                             '<em class="fa fa-check"></em> '+data.message ,
	                             {status: 'success'}
	                        );
	                  
                     $state.go('app.component_manage',{},{reload:true});
	               }else{
	                   Notify.alert(
	                        '<em class="fa fa-times"></em> '+data.message ,
	                        {status: 'danger'}
	                   );
	              }
	         })
	    })
	}
	// 导入组件
    $scope.importComponent=function(){
        ngDialog.open({
          template: 'app/views/dialog/component-import.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          scope: $scope,
          closeByDocument:false,
          cache:false,
          controller:'importComponentCtrl'
        });
    }
	// 导出组件
    $scope.exportComponent=function(){
        ngDialog.open({
          template: 'app/views/dialog/component-export.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          scope: $scope,
          closeByDocument:false,
          cache:false,
          controller:'exportComponentCtrl'
        });
    }
}])

/* ------------------------------------导入组件------------------------------------ */
componentModule.controller('importComponentCtrl',['$rootScope','$scope','$http','ngDialog','Notify','$state','$filter',function($rootScope,$scope,$http,ngDialog,Notify,$state,$filter){
    
	$rootScope.submitted = false;
	
	var importFn=function(url,fd){
		$http.post(url, fd, {
	           transformRequest: angular.identity,
	           headers: {'Content-Type': undefined}
	       }).success(function(data){
	           
	    	   $rootScope.app.layout.isShadow=false;
	    	   
			   if(data.result)
		       {
		    	   Notify.alert(
		               '<em class="fa fa-check"></em>'+data.message ,
		               {status: 'success'}
		           );
		    	   ngDialog.close();
		    	   $state.go('app.component_manage',{},{ reload: true });
		    	   
		       }else
		       {
		    	   Notify.alert(
		               '<em class="fa fa-times"></em>'+data.message ,
		               {status: 'danger'}
		           );
		       }
	     })
	     .error(function(){
	     });
	}
	
    $scope.importFn=function(){
       
       $rootScope.submitted = true;
       
       var url='/cloudui/ws/resource/importResource';
       var checkUrl='/cloudui/master/ws/packageResource/checkImportPackage';
       var fileType="resource";
       
       var fd = new FormData();
       
       var file = document.querySelector('input[type=file]').files[0];
       
       if(angular.isUndefined(file)){
	       Notify.alert(
               '<em class="fa fa-times"></em>'+"请选择文件！" ,
               {status: 'danger'}
           );
	      return;
       }
       $rootScope.app.layout.isShadow=true;
       fd.append("file",file);
       fd.append("fileType",fileType);
       $http.post(checkUrl, fd, {
           transformRequest: angular.identity,
           headers: {'Content-Type': undefined}
       }).success(function(data){
           
    	   $rootScope.app.layout.isShadow=false;
    	   
		   if(data.result)
	       {
			   importFn(url,fd);
	       }else
	       {
	    	   ngDialog.openConfirm({
	                template:
	                     '<p class="modal-header">'+data.message+'，您确定要覆盖吗?</p>' +
	                     '<div class="modal-body text-right">' +
	                       '<button type="button" class="btn btn-default mr20"  ng-click="closeThisDialog(0)">取消' +
	                       '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
	                     '</button></div>',
	               plain: true,
	               className: 'ngdialog-theme-default'
	               }).then(function(){
	            	 importFn(url,fd);
	            })
	       }
     })
     .error(function(){
     });
 }
}])

/* ------------------------------------导出组件------------------------------------ */
componentModule.controller('exportComponentCtrl',['$rootScope','$scope','$state','$location','$filter','ngDialog','Notify','$http',function($rootScope,$scope,$state,$location,$filter,ngDialog,Notify,$http){
	  $scope.checkData=[];
	  $scope.checkappId=[]; // 选中的组件
	  
	  // 组件列表   
	           
	  $scope.exportPageSize=5;
    $scope.exportPageNum=1;
	           
      $scope.onExportPageChange = function (pageNum,search)
      {
        
        var url = "/cloudui/ws/resource/listResource";
        
        $http.get(url + '?v=' + (new Date().getTime()),{
         params:
           {
             pageNum:$scope.exportPageNum,
             pageSize:$scope.exportPageSize,
             registryId:3,
             resourceName:$scope.exportSearchval
           }
         }).success(function(data){
        	 
        	angular.forEach(data.rows,function(val,key){
        		 var ischecked=$filter('filter')($scope.checkappId,val.resourceId).length>0?true:false;
        		 data.rows[key].ischecked=ischecked;
             data.rows[key].packageArr=[];
        	})
        	 
         	$scope.exportListoff=data.rows.length>0?true:false;
            $scope.exportWarninginfo='暂无数据';
            $scope.exportComponentList = data.rows;
            
            $scope.exportPageCount=Math.ceil(data.total/$scope.exportPageSize);
            if($scope.exportPageCount==0){
               $scope.exportPageCount=1;
            }
        }).error(function(){
        	 $scope.exportListoff=false;
             $scope.exportWarninginfo='暂无结果';
        })
	 } 
      
      // 导出组件
      $scope.exportFn=function()
      {
      	 
      	  var checkbox = $scope.checkappId;
          if(checkbox.length==0)
          {
             Notify.alert(
               '请选择要导出的组件！' ,
               {status: 'info'}
             );
          }else
          {
            var ids=[];

            for(var i=0;i<checkbox.length;i++)
            {
             
               if($filter('filter')($scope.checkData,checkbox[i])[0]){
                  var exportPackageId=$filter('filter')($scope.checkData,checkbox[i])[0].packageArr;
               }else{
                  var exportPackageId=[];
               }
               ids.push({
                  resourceId:checkbox[i],
                  exportPackageId:exportPackageId
               })
            }
            $http({
                method:'post',
                url:'/cloudui/master/ws/resource/exportResource',
                data: $.param({
               	 ids:angular.toJson(ids)
                }),
                responseType:'blob',
                headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
            }).success(function(data){
           	 var myDate = new Date();
                var myJsDate=$filter('date')(myDate.getTime(),'yyyyMMddHHmmss');
                var myJsDate_sss=$filter('date')(myDate.getTime(),'sss');
                var blob = new Blob([data], {type: data.type});
                var a = document.createElement("a");
                document.body.appendChild(a);
                 a.download = 'resource'+myJsDate+myJsDate_sss+'.zip'; 
                 a.href = URL.createObjectURL(blob);
                 a.click();
                 document.body.removeChild(a);
            });
//            window.location.href='/cloudui/master/ws/resource/exportResource?ids='+encodeURI(angular.toJson(ids));;
            ngDialog.close();
          }
      }
      
     //搜素组件 
 	 $scope.exportSearch=function()
 	 {
 		  $scope.exportPageNum=1;
      $scope.onExportPageChange(); 
 	 }
   // 选择要导出的工件
    $scope.exportPackage=function(id,packageArr){
        ngDialog.open({
          template: 'app/views/dialog/component-package-export.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          scope: $scope,
          closeByDocument:false,
          data:{componentId:id,packageArr:packageArr},
          cache:false,
          controller:'componentPackageExport'
        }) 
    }
}])

/* ------------------------------------导出组件里的工件包------------------------------------ */
componentModule.controller('componentPackageExport',['$rootScope','$scope','$http','$filter',function($rootScope,$scope,$http,$filter){
  
  $scope.checkappId=[]; // 选中的工件包
  $scope.parentData=$scope.$parent;
  
  // 工件包列表
  $scope.getPackageList=function(){
    $http.get('/cloudui/master/ws/resource/findPackageByResourceId'+'?v='+(new Date().getTime()),{
      params:{
        resourceId:$scope.ngDialogData.componentId
      }
    })
    .success(function(data){
       $scope.packageList=data;
    })
  }
  $scope.getPackageList();


  // 确定导出
  $scope.exportFn=function(closeThisDialog){
    closeThisDialog(0);
    $filter('filter')($scope.parentData.exportComponentList,$scope.ngDialogData.componentId)[0].packageArr=$scope.checkappId;
    $scope.parentData.checkData.push($filter('filter')($scope.parentData.exportComponentList,$scope.ngDialogData.componentId)[0]);
  }
 
}])

/* 创建通用组件 */
componentModule.controller('createComponentCtrl',['$rootScope','$state','$scope','$http','Notify','ngDialog',function($rootScope,$state,$scope,$http,Notify,ngDialog){
	$scope.formdata={}; // 表单数据
   // 图标列表
   $scope.iconList=[
      "",
      "Tomcat", 
      "Apache", 
      "Nginx", 
      "HAproxy", 
      "Mysql", 
      "MongoDB", 
      "Redis", 
      "Hbase", 
      "Zookeeper",
      "Kafka", 
      "LVS", 
      "Weblogic",
      "Oracle",
      "DB2", 
      "WAS", 
      "quartz",
      "NodeAgent",
      "JDK",
      "Informix"
   ]
   $scope.formdata.icon="";

   //获取组件标签
	$scope.labelsList=function(){
		$http.get('/cloudui/master/ws/labelManager/listLabels'+'?v=' + (new Date().getTime()),{
		      params:
		          {
		            pageNum:1,
		            pageSize:99999,
		            labelCode:3
		          }
		     }).success(function(data){
		    	 $scope.hasLabels=true;
		    	 for(var i = 0; i< data.rows.length; i++){
	                 $("<option value='"+data.rows[i].id+"'>"+data.rows[i].name+"</option>").appendTo("#optionalLabels");
	             };
	             $("#optionalLabels").chosen({
	                no_results_text:"没有搜索到此标签"
	             });
		     });	
	}

	
	 $scope.labelsList();
	
	
	  
	  
	  // 验证表单
	  $rootScope.submitted = false;
	  
	  // 创建组件
	  $scope.addComponentFn=function(){
		  $rootScope.submitted = true; 
		  if($scope.addComponentForm.$valid){
			  $rootScope.app.layout.isShadow=true;
			  // 提交
			  var labelIds="";
			  if(typeof($scope.labels) != "undefined"){
	              labelIds = $scope.labels.join(";");
			  }
			  var url='/cloudui/master/ws/resource/registNewResource';
			  
			  $scope.formdata.registryId=3;
			  $scope.formdata.resourceType="0";
			  $scope.formdata.labelIds=labelIds;

		     $http({
		    	 method:'post',
		         url:url,
		         data:$.param($scope.formdata),
		         headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
		     })
		     .success(function(data){
				$rootScope.app.layout.isShadow=false;
			   if(data.result)
		       {
		    	   Notify.alert(
		               '<em class="fa fa-check"></em>'+data.message ,
		               {status: 'success'}
		           );
		    	   ngDialog.close();
		    	   $state.go('app.component_manage',{},{reload:true}); 
		       }else{
		    	   Notify.alert(
		               '<em class="fa fa-times"></em>'+data.message ,
		               {status: 'danger'}
		           );
		       }
		   }).error(function(){
			   
		   });
		  }
	  }
}])


// 组件添加流程
componentModule.controller('componentFlowCreateCtrl',['$rootScope','$scope','$stateParams','$http','ngDialog','$state','Notify',function($rootScope,$scope,$stateParams,$http,ngDialog,$state,Notify){
	
	$scope.flowtypeList=[
  	  {value:'deploy',text:'部署'},
  	  {value:'start',text:'启动'},
  	  {value:'stop',text:'停止'},
  	  {value:'destroy',text:'卸载'},
  	  {value:'upgrade',text:'升级'},
  	  {value:'rollback',text:'回滚'},
  	  {value:'snapshot',text:'快照'}
  	]
	$scope.type='deploy';
	
	$rootScope.submitted = false;
	
	$scope.addFlowFn=function(){
		
		$rootScope.submitted = true;

		var url='/cloudui/master/ws/resource/addNewResourceFlow';
    	var data={
    			flowType:$scope.type,
    			flowName:$scope.name,
		    	flowInfo:$scope.ngDialogData.flowInfo,
		    	resourceId:$stateParams.componentId	
    	}

    	$http.post(url,data)
        .success(function(data){
        	if(data.result){
	    		Notify.alert(
                    '<em class="fa fa-check"></em> '+data.message ,
                    {status: 'success'}
	            );
	    		ngDialog.close();
	    		$state.go('app.component_manage',{},{reload:true});
	    	}else{
	    		Notify.alert(
                    '<em class="fa fa-check"></em> '+data.message ,
                    {status: 'success'}
	            );
	    	}
             
        })
    }
}])

// 组件流程管理
componentModule.controller('componentFlowManageCtrl',['$rootScope','$scope','$stateParams','$http','ngDialog','Notify',function($rootScope,$scope,$stateParams,$http,ngDialog,Notify){
	
	$scope.componentId=$stateParams.componentId;
	
	// 流程对应中文
    $scope.flowText=function(type){
    	switch(type)
        {
           case 'deploy':
            return '部署';
           break;
           case 'start':
            return '启动';
           break;
           case 'stop':
            return '停止';
           break;
           case 'destroy':
            return '卸载';
           break;
           case 'upgrade':
            return '升级';
           break;
           case 'rollback':
            return '回滚';
           break;
           case 'snapshot':
            return '快照';
           break;
        }
    }
    $scope.sortOrder= 'DESC';
    $scope.sortName= '';
    $scope.fnSort = function (arg,doItem) {
             arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
             $scope.sortOrder= arguments.callee['fnSort' + arg]?'ASC':'DESC';
             $scope.sortName =arg;
             $scope.listResourceOperationFlows(doItem);
         }
    $scope.listResourceOperationFlows=function(doItem){
    	// 组件流程列表
    	$http({
    		method:'post',
    		url:'/cloudui/master/ws/resource/getNewResourceOperationFlows',
    		data: $.param({
    			resourceId:$scope.componentId,
                sortOrder:$scope.sortOrder||'DESC',
                sortName:$scope.sortName||''
    		}),
    		headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
    	})
    	.success(function(data){
    		if(data.result){
    			var flowList=[];
    			var flowData=angular.fromJson(data.data);
    			angular.forEach(flowData,function(val,key){
    				var obj={};
    				obj.doName=key;
    				obj.doCont=val;
    				if(doItem && doItem.doName == obj.doName){
    					doItem.doCont=obj.doCont;
    				}
    				flowList.push(obj);
    			})
    			if(!doItem){
    				$scope.flowList=flowList;
				}
    			 
    		}    
    	})
    }
    $scope.listResourceOperationFlows();
   // 流程删除
   $scope.delFlow=function(id,outindex,innerindex){
		ngDialog.openConfirm({
	        template:
	             '<p class="modal-header">您确定要删除此流程吗?</p>' +
	             '<div class="modal-body text-right">' +
	               '<button type="button" class="btn btn-default mr20"  ng-click="closeThisDialog(0)">取消' +
	               '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
	             '</button></div>',
	       plain: true,
	       className: 'ngdialog-theme-default'
	     }).then(function(){
	    	$rootScope.app.layout.isShadow=true;
 
	        $http.delete('/cloudui/master/ws/resource/deleteNewResourceFlow',{
	        	params:{cdFlowId:id}
	        })
	        .then(function(response){
	        	$rootScope.app.layout.isShadow=false;
	            if(response.data.result)
	            {
	               Notify.alert(
	                  '<em class="fa fa-check"></em> '+response.data.message ,
	                  {status: 'success'}
	               );
                 var doCont=$scope.flowList[outindex].doCont;
	               for(var i=0;i<doCont.length;i++)
                 {
                    if(doCont[i].cdFlowId==id){
                        doCont.splice(i,1);
                    }
                 }
	            }else{
	               Notify.alert(
	                   '<em class="fa fa-times"></em> '+response.data.message ,
	                   {status: 'danger'}
	               );
	            }
	        })
	     })
	}
   
}])

/* 组件版本列表 */
componentModule.controller('componentDetailCtrl',['$rootScope','$scope','$http','$stateParams','Notify','ngDialog','$document','$state','$interval','$filter',function($rootScope,$scope,$http,$stateParams,Notify,ngDialog,$document,$state,$interval,$filter){
	var picname=['tomcat','apache','nginx','haproxy','mysql','mongodb','redis',
              'hbase','zookeeper','kafka','lvs','weblogic','oracle','db2','was','quartz','node','jdk','informix'];
  // 组件信息
	var componentInfo=$http.get('/cloudui/ws/resource/resourceDetail'+'?v='+(new Date().getTime()),{
	    params:{
	      resourceName:$stateParams.componentName
	    } 
	}).success(function(data){
		$scope.componentDetail=data;
    if($scope.componentDetail.icon){
       $scope.componentDetail.pic=angular.lowercase($scope.componentDetail.icon);
    }else{
        var pic=''
        for(var j=0;j<picname.length;j++)
        {
          if(angular.lowercase($scope.componentDetail.resourceName).indexOf(picname[j])!==-1)
          {
            if(picname[j]=='node'){
               var pic='nodeagent'
            }else{
              var pic=picname[j];
            }
          }
        }

        if(!pic){
          pic='custom' 
        }
       
        $scope.componentDetail.pic=pic;
    }
	$http({
		method:'post',
		url:'/cloudui/master/ws/resource/getResourceBindDetail',
		data: $.param({
			resourceId:$scope.componentDetail.resourceId
		}),
		headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
		})
		.success(function(data){
			if(data.result){
				$rootScope.bindReleaseBlueprint=angular.fromJson(data.releaseBlueprint);
				$rootScope.bindReleaseBlueprintFlow=angular.fromJson(data.releaseBlueprintFlow);
				$rootScope.bindRollbackBlueprint=angular.fromJson(data.rollbackBlueprint);
				$rootScope.bindRollbackBlueprintFlow=angular.fromJson(data.rollbackBlueprintFlow);
				$rootScope.bindStartAppBlueprint=angular.fromJson(data.startAppBlueprint);
				$rootScope.bindStartAppBlueprintFlow=angular.fromJson(data.startAppBlueprintFlow);
				$rootScope.bindStopAppBlueprint=angular.fromJson(data.stopAppBlueprint);
				$rootScope.bindStopAppBlueprintFlow=angular.fromJson(data.stopAppBlueprintFlow);
			}    
		})
	})

   $scope.sortOrder= 'DESC';
   $scope.sortName= 'versionNum';
   $scope.fnSort = function (arg) {
            arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
            $scope.sortOrder= arguments.callee['fnSort' + arg]?'ASC':'DESC';
            $scope.sortName =arg;
            $scope.onPageChange();
     }
	// 版本列表
	
	$scope.pageSize=10;
	    
	$scope.pageNum=1;

	$scope.checkappId=[];
    
	$scope.onPageChange = function (param)
    {    
        $http.get('/cloudui/master/ws/resource/listNewResourceVersionByPage'+'?v=' + (new Date().getTime()),{
	          params:
	          {
		          resourceId:$stateParams.resourceId,
		          pageSize:$scope.pageSize,
		          pageNum:$scope.pageNum,
		          versionName:$scope.keyword||'',
		          sortOrder:$scope.sortOrder||'DESC',
		          sortName:$scope.sortName||''
	          }
        }).success(function(data){
        	 var resourceStatus=data.property.resourceStatus;
        	 var resourceRunning=false;
        	 if(resourceStatus=='50'||resourceStatus=='60'||resourceStatus=='70'||resourceStatus=='51'||resourceStatus=='61'||resourceStatus=='73'){
        		 resourceRunning=true;
        	 }
        	 var versionsRunning=data.property.versionsRunning;
        	 var status=(versionsRunning||resourceRunning);
    		 $scope.releaseBatchDisabled=status;
    		 angular.forEach(data.rows,function(val,key){
    			 val.disabled=status;
    			 var checkedItem=$filter('filter')($scope.checkappId,val.id)[0];
    			 if(checkedItem){
    				 val.ischecked=true;
    			 }
    		 });
        	 if($rootScope[$stateParams.resourceId]){
        		 if(resourceRunning){
        			 if(resourceStatus=='50'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 批量发布正在执行启动...' ,
        						 {status: 'success'}
        				 ); 
        			 }
        			 else if(resourceStatus=='51'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 批量发布执行启动成功' ,
        						 {status: 'success'}
        				 );
        			 }
        			 else if(resourceStatus=='60'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 批量发布正在执行停止...' ,
        						 {status: 'success'}
        				 );
        			 }
        			 else if(resourceStatus=='61'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 批量发布执行停止成功' ,
        						 {status: 'success'}
        				 );
        			 }
        			 else if(resourceStatus=='70'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 批量发布正在执行单个版本发布...' ,
        						 {status: 'success'}
        				 );
        			 }
        			 else if(resourceStatus=='73'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 批量发布所有单个版本发布完成' ,
        						 {status: 'success'}
        				 );
        			 }
        			 else{
        			 }
        		 }
        		 else{
        			 $rootScope[$stateParams.resourceId]=null;
        			 if(resourceStatus=='71'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 批量发布执行成功！' ,
        						 {status: 'success'}
        				 );
        			 }
        			 else if(resourceStatus=='72'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 批量发布执行单个版本发布失败！' ,
        						 {status: 'danger'}
        				 );
        			 }
        			 else if(resourceStatus=='52'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 批量发布执行启动失败！' ,
        						 {status: 'danger'}
        				 );
        			 }
        			 else if(resourceStatus=='62'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 批量发布执行停止失败！' ,
        						 {status: 'danger'}
        				 );
        			 }
        			 else{
        			 }
        		 }
        	 }
        	 /*if($rootScope['version']){
        		 if(versionsRunning){
        			 if(resourceStatus=='30'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 发布执行中...' ,
        						 {status: 'success'}
        				 );
        			 }
        			 else if(resourceStatus=='40'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 回退执行中...' ,
        						 {status: 'success'}
        				 );
        			 }
        			 else if(resourceStatus=='33'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 发布流程完成' ,
        						 {status: 'success'}
        				 );
        			 }
        			 else if(resourceStatus=='43'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 回退流程完成' ,
        						 {status: 'success'}
        				 );
        			 }
        			 else if(resourceStatus=='51'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 启动成功！' ,
        						 {status: 'success'}
        				 );
        			 }
        			 else if(resourceStatus=='61'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 停止成功！' ,
        						 {status: 'success'}
        				 );
        			 }
        			 else{
        			 }
        		 }
        		 else{
        			 $rootScope['version']=null;
        			 if(resourceStatus=='31'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 发布成功！' ,
        						 {status: 'success'}
        				 );
        			 }
        			 else if(resourceStatus=='32'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 发布失败！' ,
        						 {status: 'danger'}
        				 );
        			 }
        			 else if(resourceStatus=='41'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 回退成功！' ,
        						 {status: 'success'}
        				 );
        			 }
        			 else if(resourceStatus=='42'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 回退失败！' ,
        						 {status: 'danger'}
        				 );
        			 }
        			 else if(resourceStatus=='52'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 启动失败！' ,
        						 {status: 'danger'}
        				 );
        			 }
        			 else if(resourceStatus=='62'){
        				 Notify.alert(
        						 '<em class="fa fa-check"></em> 停止失败！' ,
        						 {status: 'danger'}
        				 );
        			 }
        			 else{
        			 }
        		 }
        	 }*/
             $scope.componentVersionList = data;
             $scope.resultoff=data.rows.length>0?true:false;
             $scope.warninginfo='提示：没有数据';
             $scope.pageCount=Math.ceil($scope.componentVersionList.total/$scope.pageSize);
             
             if($scope.pageCount==0){
                $scope.pageCount=1;
             }
       }).error(function(){
             $scope.resultoff=false;
             $scope.warninginfo='暂无结果';
       })
    }
	
	$scope.versionTimer=$interval(function(){
		$scope.onPageChange();
	},5000);
	
	$scope.$on('$destroy', function(){
		$interval.cancel($scope.versionTimer);
	});
	
	// 搜素版本
    $scope.search=function()
    {
		$scope.pageNum=1;
		$scope.onPageChange(); 
        if($scope.keyword.length==0)
        {
           $scope.pageNum=1;
           $scope.onPageChange(); 
        }
    }
	
    // 删除组件版本
    $scope.delversion=function(param){
    	ngDialog.openConfirm({
            template:
                 '<p class="modal-header">您确定要删除此版本吗?</p>' +
                 '<div class="modal-body text-right">' +
                   '<button type="button" class="btn btn-default mr20"  ng-click="closeThisDialog(0)">取消' +
                   '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
                 '</button></div>',
           plain: true,
           className: 'ngdialog-theme-default'
         }).then(function(){
        	$rootScope.app.layout.isShadow=true;
        	$http({
                method:'delete',
                url:'/cloudui/master/ws/resource/deleteNewResourceVersion?versionId='+param,
            }).success(function(data){
            	$rootScope.app.layout.isShadow=false;
              if(data.result)
              {
                     Notify.alert(
                          '<em class="fa fa-check"></em> 删除成功！' ,
                          {status: 'success'}
                     );
                     $state.go('app.component_detail',{resourceName:$stateParams.componentName,resourceId:$stateParams.resourceId},{reload:true});
              }else
              {
                     Notify.alert(
                       '<em class="fa fa-times"></em> 删除失败',
                       {status: 'danger'}
                     );
              }
            }) 
         })
    }
    
    $scope.statuses = [
        {key:"SIT构建成功",value: "78"},
		{key:"SIT部署成功",value: "76"},
		{key:"SIT环境测试通过",value: "71"},
		{key:"UAT构建成功",value: "79"},
		{key:"UAT部署成功",value: "77"},
		{key:"UAT环境测试通过",value: "72"},
		{key:"上线前环境通过",value: "73"},
		{key:"实盘环境通过",value: "74"},
		{key:"生产环境上线",value: "75"},              
	    {key:"新建",value: "00"},
	    {key:"测试通过",value: "01"},
	    {key:"部分测试通过",value: "10"},
	    {key:"测试未通过",value: "11"},
	    {key:"发布中...",value: "30"},
	    {key:"回退中...",value: "40"},
	    {key:"发布成功",value: "31"},
	    {key:"回退成功",value: "41"},
	    {key:"发布失败",value: "32"},
	    {key:"回退失败",value: "42"},
	    {key:"发布完成",value: "33"},
	    {key:"回退完成",value: "43"},
	    {key:"启动中...",value: "50"},
	    {key:"停止中...",value: "60"},
	    {key:"启动成功",value: "51"},
	    {key:"停止成功",value: "61"},
	    {key:"启动失败",value: "52"},
	    {key:"停止失败",value: "62"}
   	];
	
   // 更新组件版本状态弹窗
    $scope.openUpdateCVStatus=function(param,index){
        ngDialog.open({
          template: 'app/views/dialog/component-updateStatus.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          cache:false,
          closeByDocument:false,
          scope: $scope,
          data:{info:param,index:index},
          controller:'updateCVStatus'
        })
    }
    
    $scope.$on('saveFn', function(e, newVal) {
    	$scope.componentVersionList.rows[newVal.index].status=newVal.selectedStatus;
    });
    
   // 更新组件标签弹窗
    $scope.openUpdateLabel=function(param){
        ngDialog.open({
          template: 'app/views/dialog/component-label-update.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          cache:false,
          closeByDocument:false,
          scope: $scope,
          data:{info:param},
          controller:'UpdateCompLabel'
        })
    }
    // 追加描述弹窗
    $scope.openAddDescription=function(param,index){
        ngDialog.open({
          template: 'app/views/dialog/component-addDescription.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          cache:false,
          closeByDocument:false,
          scope: $scope,
          data:{info:param,index:index},
          controller:'addDescription'
        })
    }
    
    
    //绑定发布流程
    $scope.bindReleaseComponentFlow=function(){
    	$scope.bindComponentFlow(0);
    }
    
    //绑定回退流程
    $scope.bindRollbackComponentFlow=function(){
    	$scope.bindComponentFlow(1);
    }
    
    //绑定启动流程
    $scope.bindStartAppComponentFlow=function(){
    	$scope.bindComponentFlow(2);
    }
    
    //绑定停止流程
    $scope.bindStopAppComponentFlow=function(){
    	$scope.bindComponentFlow(3);
    }
    
	//绑定发布流程
	$scope.bindComponentFlow=function(type){
		ngDialog.open({
			template: 'app/views/dialog/component-bind-flow.html'+'?action='+(new Date().getTime()),
			className: 'ngdialog-theme-default',
			cache:false,
			closeByDocument:false,
			scope: $scope,
			data:{componentId:$scope.componentDetail.resourceId,componentName:$scope.componentDetail.resourceName,bindType:type},
			controller:'bindComponentFlowCtrl'
		})
	}
	
	//应用启动
    $scope.startApp=function(){
		if($rootScope.bindStartAppBlueprint&&$rootScope.bindStartAppBlueprintFlow){
			ngDialog.openConfirm({
	           template:
	                '<p class="modal-header">您确定要启动应用吗?</p>' +
	                '<div class="modal-body text-right">' +
	                  '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
	                  '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
	                '</button></div>',
	          plain: true,
	          className: 'ngdialog-theme-default'
			}).then(function(){
				$scope.startAppDisabled=true;
				$scope.stopAppDisabled=true;
				$scope.releaseBatchDisabled=true;
				angular.forEach($scope.componentVersionList.rows,function(val,key){
	    			val.disabled=true;
	       	 	 });
				$scope.executeComponentVersionFlow($scope.componentDetail.resourceId,2);
			})
		}
		else{
			Notify.alert(
				'<em class="fa fa-check"></em> '+'组件未绑定启动流程，无法完成应用启动！',
				{status: 'danger'}
			);
		}
    }
    
   //应用停止
    $scope.stopApp=function(){
		if($rootScope.bindStopAppBlueprint&&$rootScope.bindStopAppBlueprintFlow){
			ngDialog.openConfirm({
	           template:
	                '<p class="modal-header">您确定要停止应用吗?</p>' +
	                '<div class="modal-body text-right">' +
	                  '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
	                  '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
	                '</button></div>',
	          plain: true,
	          className: 'ngdialog-theme-default'
			}).then(function(){
				$scope.startAppDisabled=true;
				$scope.stopAppDisabled=true;
				$scope.releaseBatchDisabled=true;
				angular.forEach($scope.componentVersionList.rows,function(val,key){
	    			val.disabled=true;
	       	 	 });
				$scope.executeComponentVersionFlow($scope.componentDetail.resourceId,3);
			})
		}
		else{
			Notify.alert(
				'<em class="fa fa-check"></em> '+'组件未绑定启动流程，无法完成应用启动！',
				{status: 'danger'}
			);
		}
    }

	//组件版本发布
    $scope.releaseComponentVersion=function(item){
    	if(!$rootScope.bindStartAppBlueprint&&!$rootScope.bindStartAppBlueprintFlow){
    		Notify.alert(
    				'<em class="fa fa-check"></em> '+'组件未绑定启动流程，无法完成发布！',
    				{status: 'danger'}
    			);
    		return;
    	}
    	else if(!$rootScope.bindStopAppBlueprint&&!$rootScope.bindStopAppBlueprintFlow){
    		Notify.alert(
    				'<em class="fa fa-check"></em> '+'组件未绑定停止流程，无法完成发布！',
    				{status: 'danger'}
    			);
    		return;
    	}
    	else if(!$rootScope.bindReleaseBlueprint&&!$rootScope.bindReleaseBlueprintFlow){
    		Notify.alert(
    				'<em class="fa fa-check"></em> '+'组件未绑定发布流程，无法完成发布！',
    				{status: 'danger'}
    			);
    		return;
		}
		else{
			ngDialog.openConfirm({
		           template:
		                '<p class="modal-header">您确定要发布此版本吗?</p>' +
		                '<div class="modal-body text-right">' +
		                  '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
		                  '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
		                '</button></div>',
		          plain: true,
		          className: 'ngdialog-theme-default'
				}).then(function(){
					$scope.releaseBatchDisabled=true;
					angular.forEach($scope.componentVersionList.rows,function(val,key){
		    			val.disabled=true;
		       	 	 });
					$scope.executeComponentVersionFlow(item.id,0);
				})
		}
    }

	//组件版本回退
    $scope.rollbackComponentVersion=function(item){
    	if(!$rootScope.bindStartAppBlueprint&&!$rootScope.bindStartAppBlueprintFlow){
    		Notify.alert(
    				'<em class="fa fa-check"></em> '+'组件未绑定启动流程，无法完成回退！',
    				{status: 'danger'}
    			);
    		return;
    	}
    	else if(!$rootScope.bindStopAppBlueprint&&!$rootScope.bindStopAppBlueprintFlow){
    		Notify.alert(
    				'<em class="fa fa-check"></em> '+'组件未绑定停止流程，无法完成回退！',
    				{status: 'danger'}
    			);
    		return;
    	}
    	else if(!$rootScope.bindRollbackBlueprint&&!$rootScope.bindRollbackBlueprint){
    		Notify.alert(
    				'<em class="fa fa-check"></em> '+'组件未绑定回退流程，无法完成回退！',
    				{status: 'danger'}
    			);
    		return;
		}
    	else{
			ngDialog.openConfirm({
	           template:
	                '<p class="modal-header">您确定要回退此版本吗?</p>' +
	                '<div class="modal-body text-right">' +
	                  '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
	                  '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
	                '</button></div>',
	          plain: true,
	          className: 'ngdialog-theme-default'
			}).then(function(){
				$scope.releaseBatchDisabled=true;
				angular.forEach($scope.componentVersionList.rows,function(val,key){
	    			val.disabled=true;
	       	 	 });
				$scope.executeComponentVersionFlow(item.id,1);
			})
		}
    }
	
	//版本执行流程
    $scope.executeComponentVersionFlow=function(id,type){
    	$http({
             method  : 'POST',
             url     : '/cloudui/master/ws/resource/executeNewComponentVersionFlow',
             data    : $.param({
            	 componentVersionId:id,
            	 componentExecuteType:type
             }),   
             headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
	   }).success(function(data){
		   if(data.result){
	    		Notify.alert(
                    '<em class="fa fa-check"></em> '+data.message ,
                    {status: 'success'}
	            );
	    		//$rootScope['version']=type;
				if(type==0){
					$state.go('app.blueprint_ins_log_manage',{insName:$rootScope.bindReleaseBlueprint.INSTANCE_NAME,INSTANCE_ID:$rootScope.bindReleaseBlueprint.INSTANCE_ID,flowName:$rootScope.bindReleaseBlueprintFlow.FLOW_TYPE},{reload:true});
				}
				if(type==1){
					$state.go('app.blueprint_ins_log_manage',{insName:$rootScope.bindRollbackBlueprint.INSTANCE_NAME,INSTANCE_ID:$rootScope.bindRollbackBlueprint.INSTANCE_ID,flowName:$rootScope.bindRollbackBlueprintFlow.FLOW_TYPE},{reload:true});
				}
				/*if(type==2){
					$state.go('app.blueprint_ins_log_manage',{insName:$rootScope.bindStartAppBlueprint.INSTANCE_NAME,INSTANCE_ID:$rootScope.bindStartAppBlueprint.INSTANCE_ID,flowName:$rootScope.bindStartAppBlueprintFlow.FLOW_TYPE},{reload:true});
				}
				if(type==3){
					$state.go('app.blueprint_ins_log_manage',{insName:$rootScope.bindStopAppBlueprint.INSTANCE_NAME,INSTANCE_ID:$rootScope.bindStopAppBlueprint.INSTANCE_ID,flowName:$rootScope.bindStopAppBlueprintFlow.FLOW_TYPE},{reload:true});
				}*/
	    	}else{
	    		Notify.alert(
                    '<em class="fa fa-check"></em> '+data.message ,
                    {status: 'danger'}
	            );
	    	}
	   })
    }
       
    //批量发布
    $scope.releaseComponentVersionsByBatch=function(){
		var checkbox = $scope.checkappId;
		var versionsList=[];
		var isRunning=false;
		angular.forEach(checkbox,function(val,key){
			//var item=$filter('filter')($scope.componentVersionList.rows,val)[0];
			versionsList.push(val);
		})
		if(isRunning){
			return;
		}
		if(versionsList.length==0){
			Notify.alert(
					'<em class="fa fa-check"></em> '+'请至少选择1个组件版本进行批量发布' ,
					{status: 'danger'}
			);
			return;
		}
		if(!$rootScope.bindStartAppBlueprint&&!$rootScope.bindStartAppBlueprintFlow){
    		Notify.alert(
    				'<em class="fa fa-check"></em> '+'组件未绑定启动流程，无法完成批量发布！',
    				{status: 'danger'}
    			);
    		return;
    	}
    	else if(!$rootScope.bindStopAppBlueprint&&!$rootScope.bindStopAppBlueprintFlow){
    		Notify.alert(
    				'<em class="fa fa-check"></em> '+'组件未绑定停止流程，无法完成批量发布！',
    				{status: 'danger'}
    			);
    		return;
    	}
    	else if(!$rootScope.bindReleaseBlueprint&&!$rootScope.bindReleaseBlueprintFlow){
    		Notify.alert(
    				'<em class="fa fa-check"></em> '+'组件未绑定发布流程，无法完成批量发布！',
    				{status: 'danger'}
    			);
    		return;
		}
    	else{
    		ngDialog.openConfirm({
    			template:
    				'<p class="modal-header">您确定要批量发布已选择的版本吗?</p>' +
    				'<div class="modal-body text-right">' +
    				'<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
    				'<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
    				'</button></div>',
    				plain: true,
    				className: 'ngdialog-theme-default'
    		}).then(function(){
    			$http({
    				method  : 'POST',
    				url     : '/cloudui/master/ws/resource/releaseNewComponentVersionsByBatch',
    				data    : $.param({
    					componentVersionIds:angular.toJson(versionsList),
    					componentExecuteType:0
    				}),   
    				headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
    			}).success(function(data){
    				$rootScope[$scope.componentDetail.resourceId]='batchRelease';
    				$scope.releaseBatchDisabled=true;
    				angular.forEach($scope.componentVersionList.rows,function(val,key){
    					val.disabled=true;
    				});
    				if(data.result){
    					Notify.alert(
    							'<em class="fa fa-check"></em> '+data.message ,
    							{status: 'success'}
    					);
    				}
    				$state.go('app.blueprint_ins_log_manage',{insName:$rootScope.bindReleaseBlueprint.INSTANCE_NAME,INSTANCE_ID:$rootScope.bindReleaseBlueprint.INSTANCE_ID,flowName:$rootScope.bindReleaseBlueprintFlow.FLOW_TYPE},{reload:true});
    			})
    		})
    	}
    }
    
    //版本合并
    $scope.mergeComponentVersions=function(){
		var checkbox = $scope.checkappId;
		var ids='';
		if(checkbox.length ==0){
			Notify.alert(
					'<em class="fa fa-check"></em> '+'请至少选择1个组件版本进行合并' ,
					{status: 'danger'}
			);
			return;
		}
		for(var i=0;i<checkbox.length;i++){
			if(i==(checkbox.length-1)){
				ids=ids+checkbox[i];
			}else{
				ids=ids+checkbox[i]+",";
			}
		}
		
		ngDialog.openConfirm({
	          template:
	                '<p class="modal-header">您确定要合并版本吗?</p>' +
	                '<div class="modal-body text-right">' +
	                  '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
	                  '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
	                '</button></div>',
	          plain: true,
	          className: 'ngdialog-theme-default'
		}).then(function(){
			$scope.checkappId=[];
			$("input[type='checkbox']").prop('checked',false);
			window.location.href='/cloudui/master/ws/resource/exportPatches?ids='+ids;
		})
		//$state.go('app.component_detail',{},{reload:true});
    }
}])


/* 组件标签更新*/
componentModule.controller('UpdateCompLabel',['$rootScope','$scope','$http','$stateParams','ngDialog','Notify','$state','$filter',function($rootScope,$scope,$http,$stateParams,ngDialog,Notify,$state,$filter){
	
	$scope.comInfo=$scope.ngDialogData.info;
	$scope.selectedLabels=$scope.comInfo.labelIds.split(";");
	  //获取组件标签
	$scope.labelsList=function(){
		$http.get('/cloudui/master/ws/labelManager/listLabels'+'?v=' + (new Date().getTime()),{
		      params:
		          {
		            pageNum:1,
		            pageSize:99999,
		            labelCode:3
		          }
		     }).success(function(data){
		    	 $scope.hasLabels=true;
		    	 for(var i = 0; i< data.rows.length; i++){
		    		 var selected=$filter('filter')($scope.selectedLabels,data.rows[i].id).length>0?true:false;
		    		 if(selected){
		    			 $("<option value='"+data.rows[i].id+"' selected = 'selected'>"+data.rows[i].name+"</option>").appendTo("#optionalLabels");
		    		 }else{
		    			 $("<option value='"+data.rows[i].id+"'>"+data.rows[i].name+"</option>").appendTo("#optionalLabels");
		    		 }
	             };
	             $("#optionalLabels").chosen({
	                no_results_text:"没有搜索到此环境"
	             });
		     });	
	}
	$scope.labelsList();
    $scope.saveFn = function() {
    	var labelIds="";
		  if(typeof($scope.labels) != "undefined"){
            labelIds = $scope.labels.join(";");
		  }
    	 $http({
             method  : 'PUT',
             url     : '/cloudui/master/ws/labelManager/updateResourceLabel',
             data    : {
					      resourceId:$scope.comInfo.resourceId,
                   	      labelIds:labelIds,
                   	      labelCode:3
						},  
             headers : { 'Content-Type': 'application/json' }
           })
           .then(function(response) {
             $rootScope.app.layout.isShadow=false;
             if (response.data.result ) {
               ngDialog.close();
				  $state.go('app.component_detail',{},{ reload: true });
             }else{
                $scope.authMsg = '创建失败，请重新添加标签！';  
             }
           }, function(x) {
             $scope.authMsg = '服务器请求错误';
           });
   };
}])

/* 组件版本状态更新*/
componentModule.controller('updateCVStatus',['$rootScope','$scope','$http','$stateParams','ngDialog','Notify','$state','$filter',function($rootScope,$scope,$http,$stateParams,ngDialog,Notify,$state,$filter){
	
	$scope.comInfo=$scope.ngDialogData.info;
	$scope.currentStatus=$scope.comInfo.status;
	$scope.index=$scope.ngDialogData.index;

    $scope.saveFn = function() {
        
        $scope.authMsg = '';
    
        if($scope.updateComponentForm.$valid) {   
           $http({
             method  : 'POST',
             url     : '/cloudui/master/ws/resource/updateResourceVersionStatus',
             data    : $.param({
            	 resourceVersionId:$scope.comInfo.id,
            	 status:$scope.selectedStatus.value,
            	 description:$scope.description
             }),   
             headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
           }).then(function(response){
        	   $scope.authMsg =response.data.message;
               if ( !response.data.result ) {
                 Notify.alert(
                         '<em class="fa fa-times"></em>'+$scope.authMsg,
                         {status: 'danger'}
                       );
               }else{
            	   $scope.$emit('saveFn', {selectedStatus:$scope.selectedStatus.value,index:$scope.index});
            	   Notify.alert(
                           '<em class="fa fa-check"></em>'+$scope.authMsg,
                           {status: 'success'}
                      );
                   ngDialog.close();
               }
           },function(x) {
              $scope.authMsg = '服务器请求错误';
           })
         }
     else { 
       $scope.updateComponentForm.text.$dirty = true;
     }
   };
}])

/* 组件版本追加描述*/
componentModule.controller('addDescription',['$rootScope','$scope','$http','$stateParams','ngDialog','Notify','$state','$filter',function($rootScope,$scope,$http,$stateParams,ngDialog,Notify,$state,$filter){
	
	$scope.comInfo=$scope.ngDialogData.info;
	$scope.currentStatus=$scope.comInfo.status;
	$scope.index=$scope.ngDialogData.index;

    $scope.saveFn = function() {
        
        $scope.authMsg = '';
    
        if($scope.updateComponentForm.$valid) {   
           $http({
             method  : 'POST',
             url     : '/cloudui/master/ws/resource/addDescription',
             data    : $.param({
            	 resourceVersionId:$scope.comInfo.id,
            	 status:$scope.currentStatus,
            	 description:$scope.description
             }),   
             headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
           }).then(function(response){
        	   $scope.authMsg =response.data.message;
               if ( !response.data.result ) {
                 Notify.alert(
                         '<em class="fa fa-times"></em>'+$scope.authMsg,
                         {status: 'danger'}
                       );
               }else{
            	   //$scope.$emit('saveFn', {selectedStatus:$scope.selectedStatus.value,index:$scope.index});
            	   Notify.alert(
                           '<em class="fa fa-check"></em>'+$scope.authMsg,
                           {status: 'success'}
                      );
                   ngDialog.close();
               }
           },function(x) {
              $scope.authMsg = '服务器请求错误';
           })
         }
     else { 
       $scope.updateComponentForm.text.$dirty = true;
     }
   };
}])


/* 组件版本在线定义 */
componentModule.controller('componentVersionAddCtrl',['$rootScope','$scope','$http','$stateParams','Notify','$state','ngDialog',function($rootScope,$scope,$http,$stateParams,Notify,$state,ngDialog){
    
   $scope.inputConfigs=[];
   $scope.outputConfigs=[]; // output配置
   $scope.type="2";
  
   // 添加
   $scope.add=function(list){
	   var obj={};
       $scope[list].push(obj);
   }
	
   // 删除
   $scope.del=function(list,idx){
       $scope[list].splice(idx,1);
   }
   
   /*$scope.$watch('type',function(newval,oldval){
	   if(newval=='2'){
		   $scope.inputConfigs=[{key:'deployPath',value:''}]; // input配置
	   }else{
		   $scope.inputConfigs=[]; // input配置
	   }
   })*/

   // 上传新的工件包
   $rootScope.newFile='';

   $scope.newPackage=function(){
      ngDialog.open({
            template: 'app/views/dialog/new-package.html'+'?action='+(new Date().getTime()),
            className: 'ngdialog-theme-default ngdialog-sm',
            scope: $scope,
            closeByDocument:false,
            cache:false,
            controller:'newPackageCtrl'
       });
   }

   $scope.$watch('newFile',function(newval,oldval){
       if(newval){
           $scope.url=$rootScope.newFile;
       }
   })
   
   // 验证表单
   $rootScope.submitted = false;
   // 创建
   $scope.addVersionFn=function(){
	   $rootScope.submitted = true;
	  
       var input={};
       var output={};

	   $scope.delWithPassword($scope.inputConfigs, 'inputvalue');
	   $scope.delWithPassword($scope.outputConfigs, 'outputvalue');
       
       angular.forEach($scope.inputConfigs,function(val,key){
          input[val.key]=val.value||'';
       })
       
       angular.forEach($scope.outputConfigs,function(val,key){
          output[val.key]=val.value||'';
       })

	   $http({
            method:'post',
            url:'/cloudui/master/ws/resource/saveNewResourceVersion',
            data:$.param({
            	resourceId:$stateParams.componentId,
            	versionName:$scope.versionName,
            	versionDesc:$scope.versionDesc,
            	registryId:3,
            	file:$scope.url,
            	md5:'',
            	type:parseInt($scope.type),
            	input:angular.toJson(input),
            	output:angular.toJson(output)	
            }),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
       }).success(function(data){
    	   if(data.result){
    		   Notify.alert(
                   '<em class="fa fa-check"></em> '+data.message ,
                   {status: 'success'}
               );
    		   $state.go('app.component_detail',{resourceId:$stateParams.componentId,componentName:$stateParams.componentName},{reload:true});
    	   }else{
    		   Notify.alert(
                   '<em class="fa fa-times"></em> '+data.message,
                   {status: 'danger'}
               );
    	   }
       })
   }

   // 计算密码类checkbox是否选中,初始化input类型
   $scope.passwordChecked=function(obj,inputId){
	   if(obj.value.substring(0,4)=='DEC(' && obj.value.substring(obj.value.length-1, obj.value.length)==')'){
		  document.getElementById(inputId).setAttribute('type','password');
		  obj.value = obj.value.substring(4, obj.value.length-1);
	   }
	   var type = document.getElementById(inputId).getAttribute('type');
	   if(type=='password'){
		  return true;
	   }
	   else{
		  return false;
	   }
   }

   // 切换input的type为text/password
   $scope.passwordTypeChange=function(obj,inputId){
	   var type = document.getElementById(inputId).getAttribute('type');
	   if(type=='password'){
		  document.getElementById(inputId).setAttribute('type','text');
	   }
	   else{
		  document.getElementById(inputId).setAttribute('type','password');
	   }
   }

   // 计算需要加密的参数
   $scope.delWithPassword=function(obj,inputId){
	   angular.forEach(obj,function(ele,index){
			var type=document.getElementById(inputId+index).getAttribute('type');
			if(type=='password'){
				ele.value='DEC('+ele.value+')';
			}	
	   })
   }

   // 搜索工件包
   $scope.searchWorkpiecePackage=function(){
		ngDialog.open({
			template: 'app/views/dialog/workpiece-package-search.html'+'?action='+(new Date().getTime()),
			className: 'ngdialog-theme-default',
			scope: $scope,
			closeByDocument:false,
			cache:false,
			controller:'searchWorkpiecePackage'
		});
	}

   $scope.textAreaKeys = ['CMD','sql','include','exclude','dir','includes','excludes','mapperRules','explicitTokens','updateProps','deleteProps','propertys','propertyKeys','replaceText','remove','insert','setAttr','add','removeSection','files','sqlCommand','additionalArgs','contents','rule','content'];
   $scope.isTextAreaKey = function(key) {
    for (var i = $scope.textAreaKeys.length - 1; i >= 0; i--) {
     if ($scope.textAreaKeys[i] == key) {
      return true
     }
    }
    return false
   }

}])

/* 组件版本更新 */
componentModule.controller('componentVersionUpdateCtrl',['$rootScope','$scope','$http','$stateParams','Notify','$state','$filter','ngDialog',function($rootScope,$scope,$http,$stateParams,Notify,$state,$filter,ngDialog){
	$scope.inputConfigs=[]; // input配置
	$scope.outputConfigs=[]; // output配置
	   
	// 展示配置
    $scope.showConfig=function(configArr,config){
        if(config)
        {  
        	angular.forEach(config,function(val,key){
        		configArr.push({key:key,value:val})
            })
        }     
   }
    
   // 添加
    $scope.add=function(list){
 	   var obj={};
        $scope[list].push(obj);
    }
 	
    // 删除
    $scope.del=function(list,idx){
        $scope[list].splice(idx,1);
    }

    // 上传新的工件包
   $rootScope.newFile='';
   
   $scope.newPackage=function(){
      ngDialog.open({
            template: 'app/views/dialog/new-package.html'+'?action='+(new Date().getTime()),
            className: 'ngdialog-theme-default ngdialog-sm',
            scope: $scope,
            closeByDocument:false,
            cache:false,
            controller:'newPackageCtrl'
       });
   }

   $scope.$watch('newFile',function(newval,oldval){
       if(newval){
           $scope.url=$rootScope.newFile;
       }
   })
	
	// 组件版本详情
	$http.get('/cloudui/master/ws/resource/getNewResourceVersion'+'?v='+(new Date().getTime()),{
		params:{
			resourceVersionId:$stateParams.versionId
		}
	})
	.success(function(data){
		$scope.versionDetail=data;
		var index = $scope.versionDetail.resourcePath.lastIndexOf('/');
		var fileName = $scope.versionDetail.resourcePath.substring(index+1);
		$scope.url=fileName;
		$scope.showConfig($scope.inputConfigs,angular.fromJson(data.input));
		$scope.showConfig($scope.outputConfigs,angular.fromJson(data.output));
	})

	// 验证表单
	$rootScope.submitted = false;
	// 更新组件版本
	$scope.updateVersionFn=function(){
	   $rootScope.submitted = true;
       var input={};
       var output={};
       
	   $scope.delWithPassword($scope.inputConfigs, 'inputvalue');
	   $scope.delWithPassword($scope.outputConfigs, 'outputvalue');

       angular.forEach($scope.inputConfigs,function(val,key){
          input[val.key]=val.value||'';
       })
       
       angular.forEach($scope.outputConfigs,function(val,key){
          output[val.key]=val.value||'';
       })
    
	   $http({
	            method:'post',
	            url:'/cloudui/master/ws/resource/updateVersion',
	            data:$.param({
	            	resourceVersionId:$stateParams.versionId,
	            	input:angular.toJson(input),
	            	output:angular.toJson(output),
                file:$scope.url
	            }),
	            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
	   }).success(function(data){
    	   if(data.result){
    		   Notify.alert(
                   '<em class="fa fa-check"></em> '+data.message ,
                   {status: 'success'}
               );
    		   $state.go('app.component_detail',{componentName:$stateParams.componentName,resourceId:$scope.versionDetail.resourceId},{reload:true});
    	   }else{
    		   Notify.alert(
                   '<em class="fa fa-times"></em> '+data.message,
                   {status: 'danger'}
               );
    	   }
	  })
   }

   // 计算密码类checkbox是否选中,初始化input类型
   $scope.passwordChecked=function(obj,inputId){
	   if(obj.value.substring(0,4)=='DEC(' && obj.value.substring(obj.value.length-1, obj.value.length)==')'){
		  document.getElementById(inputId).setAttribute('type','password');
		  obj.value = obj.value.substring(4, obj.value.length-1);
	   }
	   var type = document.getElementById(inputId).getAttribute('type');
	   if(type=='password'){
		  return true;
	   }
	   else{
		  return false;
	   }
   }

   // 切换input的type为text/password
   $scope.passwordTypeChange=function(obj,inputId){
	   var type = document.getElementById(inputId).getAttribute('type');
	   if(type=='password'){
		  document.getElementById(inputId).setAttribute('type','text');
	   }
	   else{
		  document.getElementById(inputId).setAttribute('type','password');
	   }
   }

   // 计算需要加密的参数
   $scope.delWithPassword=function(obj,inputId){
	   angular.forEach(obj,function(ele,index){
			var type=document.getElementById(inputId+index).getAttribute('type');
			if(type=='password'){
				ele.value='DEC('+ele.value+')';
			}	
	   })
   }

   // 搜索工件包
   $scope.searchWorkpiecePackage=function(){
		ngDialog.open({
			template: 'app/views/dialog/workpiece-package-search.html'+'?action='+(new Date().getTime()),
			className: 'ngdialog-theme-default',
			scope: $scope,
			closeByDocument:false,
			cache:false,
			controller:'searchWorkpiecePackage'
		});
	}

   $scope.textAreaKeys = ['CMD','sql','include','exclude','dir','includes','excludes','mapperRules','explicitTokens','updateProps','deleteProps','propertys','propertyKeys','replaceText','remove','insert','setAttr','add','removeSection','files','sqlCommand','additionalArgs','contents','rule','content'];
   $scope.isTextAreaKey = function(key) {
    for (var i = $scope.textAreaKeys.length - 1; i >= 0; i--) {
     if ($scope.textAreaKeys[i] == key) {
      return true
     }
    }
    return false
   }
    
}])

/* 组件版本克隆 */
componentModule.controller('componentVersionCloneCtrl',['$rootScope','$scope','$http','$stateParams','Notify','$state','$filter','ngDialog',function($rootScope,$scope,$http,$stateParams,Notify,$state,$filter,ngDialog){
  $scope.inputConfigs=[]; // input配置
  $scope.outputConfigs=[]; // output配置
     
  // 展示配置
    $scope.showConfig=function(configArr,config){
        if(config)
        {  
          angular.forEach(config,function(val,key){
            configArr.push({key:key,value:val})
            })
        }     
   }
    
   // 添加
    $scope.add=function(list){
     var obj={};
        $scope[list].push(obj);
    }
  
    // 删除
    $scope.del=function(list,idx){
        $scope[list].splice(idx,1);
    }

    // 上传新的工件包
   $rootScope.newFile='';
   
   $scope.newPackage=function(){
      ngDialog.open({
            template: 'app/views/dialog/new-package.html'+'?action='+(new Date().getTime()),
            className: 'ngdialog-theme-default ngdialog-sm',
            scope: $scope,
            closeByDocument:false,
            cache:false,
            controller:'newPackageCtrl'
       });
   }

   $scope.$watch('newFile',function(newval,oldval){
       if(newval){
           $scope.url=$rootScope.newFile;
       }
   })
  
  // 组件版本详情
  $http.get('/cloudui/master/ws/resource/getNewResourceVersion'+'?v='+(new Date().getTime()),{
    params:{
      resourceVersionId:$stateParams.versionId
    }
  })
  .success(function(data){
    $scope.versionDetail=data;
	var index = $scope.versionDetail.resourcePath.lastIndexOf('/');
	var fileName = $scope.versionDetail.resourcePath.substring(index+1);
	$scope.url=fileName;
    $scope.showConfig($scope.inputConfigs,angular.fromJson(data.input));
    $scope.showConfig($scope.outputConfigs,angular.fromJson(data.output));
  })

  // 验证表单
  $rootScope.submitted = false;
  // 更新组件版本
  $scope.updateVersionFn=function(){
     $rootScope.submitted = true;
       var input={};
       var output={};
       
	   $scope.delWithPassword($scope.inputConfigs, 'inputvalue');
	   $scope.delWithPassword($scope.outputConfigs, 'outputvalue');

       angular.forEach($scope.inputConfigs,function(val,key){
          input[val.key]=val.value||'';
       })
       
       angular.forEach($scope.outputConfigs,function(val,key){
          output[val.key]=val.value||'';
       })

	   
         
     $http({
              method:'post',
              url:'/cloudui/master/ws/resource/cloneResourceVersion',
              data:$.param({
                resourceId:$scope.versionDetail.resourceId,
                versionName:$scope.versionName,
                versionDesc:$scope.versionDetail.versionDesc,
                registryId:$scope.versionDetail.registryId,
                file:$scope.url,
                md5:'',
                type:$scope.versionDetail.type,
                input:angular.toJson(input),
                output:angular.toJson(output),
                oldResourceVersionId:$stateParams.versionId 
              }),
              headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
     }).success(function(data){
         if(data.result){
           Notify.alert(
                   '<em class="fa fa-check"></em> '+data.message ,
                   {status: 'success'}
               );
           $state.go('app.component_detail',{componentName:$stateParams.componentName,resourceId:$scope.versionDetail.resourceId},{reload:true});
         }else{
           Notify.alert(
                   '<em class="fa fa-times"></em> '+data.message,
                   {status: 'danger'}
               );
         }
    })
   }

   // 计算密码类checkbox是否选中,初始化input类型
   $scope.passwordChecked=function(obj,inputId){
	   if(obj.value.substring(0,4)=='DEC(' && obj.value.substring(obj.value.length-1, obj.value.length)==')'){
		  document.getElementById(inputId).setAttribute('type','password');
		  obj.value = obj.value.substring(4, obj.value.length-1);
	   }
	   var type = document.getElementById(inputId).getAttribute('type');
	   if(type=='password'){
		  return true;
	   }
	   else{
		  return false;
	   }
   }

   // 切换input的type为text/password
   $scope.passwordTypeChange=function(obj,inputId){
	   var type = document.getElementById(inputId).getAttribute('type');
	   if(type=='password'){
		  document.getElementById(inputId).setAttribute('type','text');
	   }
	   else{
		  document.getElementById(inputId).setAttribute('type','password');
	   }
   }

   // 计算需要加密的参数
   $scope.delWithPassword=function(obj,inputId){
	   angular.forEach(obj,function(ele,index){
			var type=document.getElementById(inputId+index).getAttribute('type');
			if(type=='password'){
				ele.value='DEC('+ele.value+')';
			}	
	   })
   }

   // 搜索工件包
   $scope.searchWorkpiecePackage=function(){
		ngDialog.open({
			template: 'app/views/dialog/workpiece-package-search.html'+'?action='+(new Date().getTime()),
			className: 'ngdialog-theme-default',
			scope: $scope,
			closeByDocument:false,
			cache:false,
			controller:'searchWorkpiecePackage'
		});
	}

   $scope.textAreaKeys = ['CMD','sql','include','exclude','dir','includes','excludes','mapperRules','explicitTokens','updateProps','deleteProps','propertys','propertyKeys','replaceText','remove','insert','setAttr','add','removeSection','files','sqlCommand','additionalArgs','contents','rule','content'];
   $scope.isTextAreaKey = function(key) {
    for (var i = $scope.textAreaKeys.length - 1; i >= 0; i--) {
     if ($scope.textAreaKeys[i] == key) {
      return true
     }
    }
    return false
   }

}])

/* 组件版本模板映射 */
componentModule.controller('componentVersionTempMappingCtrl',['$rootScope','$scope','ngDialog','$http','$state','Notify','$stateParams',function($rootScope,$scope,ngDialog,$http,$state,Notify,$stateParams){
	
	$scope.isLoading=false;
	
	// 获取树节点的父级
	$scope.getParent=function(pid,data){
		for(var i=0;i<data.length;i++)
		{	 
			if(data[i].uid==pid){
				$scope.parentData=data[i];
				break;
			}else{
				$scope.getParent(pid,data[i].children);
			}
		}
	 
		return $scope.parentData;
	}
	
	// 获取树节点的路径
	$scope.getPath=function(branch,tree){
		if(branch.parent_uid){
			if(branch.file){
				$scope.path='/'+branch.file+$scope.path;
			}else if(branch.directory){
				$scope.path='/'+branch.directory+$scope.path;
			}
			var parentBranch=$scope.getParent(branch.parent_uid,tree);
			$scope.getPath(parentBranch,tree);
		}else{
			if(branch.file){
				$scope.path='/'+branch.file+$scope.path;
			}else if(branch.directory){
				$scope.path='/'+branch.directory+$scope.path;
			}
		}
		return $scope.path;
	}
	
	// 获取文件内容
	$scope.my_tree_handler = function(branch,tree) {
		$scope.path="";
		$scope.branch=branch;
		var path=$scope.getPath(branch,tree);
        if(branch.file){
        	$http.get('/cloudui/master/ws/resource/getWorkpieceFile'+'?v='+(new Date().getTime()),{
        		params:{
        			resourceVersionId:$stateParams.versionId,
        		    filePath:path
        		}
        	}).success(function(data){
        		if(data.result){
        			$scope.fileCont=data.message;
        			var fileSuffix='.'+$scope.path.split('.')[$scope.path.split('.').length-1]
              var fileSuffix2=$scope.path.split('/')[$scope.path.split('/').length-1]
              if(fileSuffix2.indexOf('.') > -1){
                $scope.mappingKey='.'+$scope.path.split(fileSuffix)[0]+'.ftl';
              }else{
                $scope.mappingKey='.'+ $scope.path +'.ftl';
              }

        			$scope.mappingValue='.'+$scope.path;
        		}
        	})
        }
     
     };
  
     // 树结构数据重组
     $scope.changeTreeData=function(arr){
	   angular.forEach(arr,function(val,key){
		  if(val.file){
			  val.label=val.file;
		  }else{
			  val.label=val.directory;
			  $scope.changeTreeData(val.children);
		  }
	   })
     }
	
     // 获取树形数据
    $scope.getWorkpieceTreeFn=function(){
		
		$http.get('/cloudui/master/ws/resource/getWorkpieceTree'+'?v='+(new Date().getTime()),
			{
			   params:{
				   resourceVersionId:$stateParams.versionId
			   }
			}	
		).success(function(data){
			if(data.result){
				$scope.isLoading=true;
				$scope.fileTree=angular.fromJson(data.message); 
				$scope.changeTreeData($scope.fileTree); 
			 
				if($scope.fileTree){
					$scope.treeoff=true;
					$scope.my_data=$scope.fileTree;
					var tree;
					$scope.my_tree = tree = {};
				}
				
			}else{
				$scope.isLoading=true;
				$scope.fail=true;
				$scope.TreeLoadMessage=data.message;
			}		
		})	
	}
	
	$scope.getWorkpieceTreeFn();
	
	
	// 保存文件
	$scope.saveFile=function(){
		var templates={};	
		templates[$scope.mappingKey]=$scope.mappingValue;
		var ftlName="";
		if(!$scope.branch){
			var value = $scope.mappingValue;
			ftlName=value.substring(value.lastIndexOf("/")+1);
		}else{
			ftlName=$scope.branch.file;
		}
		$http({
            method:'post',
            url:'/cloudui/master/ws/resource/saveVersionFtl',
            data:$.param({
            	resourceVersionId:$stateParams.versionId,
            	ftlName:ftlName,
            	ftlText:$scope.fileCont,
            	templates:angular.toJson(templates)
            }),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
	   }).success(function(data){
		   if(data.result){
			   Notify.alert(
	               '<em class="fa fa-check"></em> '+data.message ,
	               {status: 'success'}
	           );
			   $scope.fileCont=null;
			   $scope.mappingKey=null;
			   $scope.mappingValue=null;
			   $scope.getMappingConfigs();
		   }else{
			   Notify.alert(
	               '<em class="fa fa-times"></em> '+data.message,
	               {status: 'danger'}
	           );
		   }
	  })
	} 
	
	// 获取组件版本模板文件映射关系
	 
	$scope.getMappingConfigs=function(){
		$http.get('/cloudui/master/ws/resource/getVersionFtl'+'?v='+(new Date().getTime()),{
			params:{
				resourceVersionId:$stateParams.versionId
			}
		}).success(function(data){
			for(var i=0;i<data.length;i++){
				 
				var templates=angular.fromJson(data[i].templates);
				angular.forEach(templates,function(val,key){
					data[i].templateKey=key;
					data[i].templateValue=val;
				})
			}
			$scope.mappingConfigList=data;
		})
	}
	
	$scope.getMappingConfigs();
	
	// 更新文件
	$scope.updateFile=function(file){
		var fileDialog=ngDialog.open({
	        template: 'app/views/dialog/component-version-file-update.html'+'?action='+(new Date().getTime()),
	        className: 'ngdialog-theme-default ngdialog-lg',
	        scope: $scope,
	        cache:false,
	        closeByDocument:false,
	        data:{file:file},
	        controller:'componentVersionFileUpdate'
	    });
		fileDialog.closePromise.then(function(){
			$scope.getMappingConfigs();
		})
	}
	
	// 删除文件
	$scope.delFile=function(id,index){
		ngDialog.openConfirm({
	           template:
	                '<p class="modal-header">您确定要删除此文件吗?</p>' +
	                '<div class="modal-body text-right">' +
	                  '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
	                  '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
	                '</button></div>',
	          plain: true,
	          className: 'ngdialog-theme-default'
	    }) .then(
	        function(){
	         $rootScope.app.layout.isShadow=true;
	         $http.delete('/cloudui/master/ws/resource/deleteVersionFtl'+'?v='+(new Date().getTime()),{
	             params:{
	             	id:id
	             }
	         }).success(function(data){
	        	 $rootScope.app.layout.isShadow=false;
	         	 if(data.result){
	         		 Notify.alert(
	       	               '<em class="fa fa-check"></em> '+data.message ,
	       	               {status: 'success'}
	       	           );
	         		 $scope.mappingConfigList.splice(index,1)
	         	 } else{
	         		 Notify.alert(
	       	              '<em class="fa fa-times"></em> '+data.message,
	       	              {status: 'danger'}
	       	         );
	         	 }
	         })
	       } 
	    )
	}
}])

// 文件更新
componentModule.controller('componentVersionFileUpdate',['$rootScope','$scope','ngDialog','$http','$state','Notify','$stateParams',function($rootScope,$scope,ngDialog,$http,$state,Notify,$stateParams){
	$scope.file=$scope.ngDialogData.file;
	$scope.fileCont=$scope.file.ftlText;
	
	// 更新
	$scope.updateFile=function(){
		$http({
			method:"post",
			url:"/cloudui/master/ws/resource/updateVersionFtl",
			data: $.param({
				id:$scope.file.id,
				ftlText:$scope.fileCont
			}),   
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
       }).then(function(response){
    	   $rootScope.app.layout.isShadow=false;
           if(response.data.result)
           {
              Notify.alert(
                    '<em class="fa fa-check"></em> '+response.data.message ,
                    {status: 'success'}
              );
              ngDialog.close();
           }else{
              Notify.alert(
                '<em class="fa fa-times"></em> '+response.data.message ,
                {status: 'danger'}
              );
           }
	   })
	}
}])

// 上传工件包
componentModule.controller('newPackageCtrl',['$rootScope','$scope','FileUploader','Notify','$timeout','ngDialog','$http',function($rootScope,$scope,FileUploader,Notify,$timeout,ngDialog,$http){
    
    var uploader = $scope.uploader = new FileUploader({
        url: '/cloudui/master/ws/packageResource/upload',
        formData:[

        ]
    });
  
    $scope.isloading=true;
    $scope.isDisabled=true;

    $scope.fileTypeArr = ['zip', 'jar', 'war', 'tar', 'gz']
  
    //上传文件限制
    uploader.filters.push({
      name: 'customFilter',
      fn: function(item, options) {
        
        var nameArr=item.name.split('.');
          
          var name=item.name.split('.'+nameArr[nameArr.length-1])[0]; // 文件名称
  
          var fileType=nameArr[nameArr.length-1]; // 文件类型
          
          var reg=/^[^<>,'";:\?[\]{}()*&%$#@!\s]+$/;
  
          if(!$scope.fileTypeArr.in_array(fileType)){
             return false;
          }else if(!reg.test(name)){
             return reg.test(name);
          } 
          return this.queue.length < 1;
      }
    });
  
    //当文件上传失败
    uploader.onWhenAddingFileFailed = function(item, filter, options) {
  
        var nameArr=item.name.split('.');
      
        var name=item.name.split('.'+nameArr[nameArr.length-1])[0];
        
        var fileType=nameArr[nameArr.length-1];
      
        var reg=/^[^<>,'";:\?[\]{}()*&%$#@!\s]+$/;

        if(this.queue.length<1){
            if(!$scope.fileTypeArr.in_array(fileType)){
                 Notify.alert(
                '<em class="fa fa-check"></em> 请添加zip/jar/war类型的工件包文件!',
                 {status: 'success'}
           );
            }else{
                 if(!reg.test(name)){
                Notify.alert(
                 '<em class="fa fa-check"></em> 上传包名称包含空格或特殊字符，请重新命名!',
                  {status: 'success'}
               );
          }
            }
        }else{
            Notify.alert(
             '<em class="fa fa-check"></em> 只能添加一个文件!',
              {status: 'success'}
          );
        }
        
        $scope.isloading=false;
        
        $timeout(function(){
             $scope.isloading=true;
        })
  };
  
  //上传文件后
  uploader.onAfterAddingFile=function(fileItem){
	// 校验文件名称是否存在
	  $http({
	  	method: 'get', 
	  	url: '/cloudui/master/ws/packageResource/checkPackageUnique'+'?v='+(new Date().getTime()),
	  	params:{fileName:fileItem.file.name}
	  }).then(function(res){
          if(!res.data.result){
             Notify.alert(
		         '<em class="fa fa-times"></em> '+res.data.message,
		          {status: 'danger'}
		       );
             fileItem.remove();
          }
	  })
	  
      $scope.isloading=false;
      $scope.isDisabled=false;
      $timeout(function(){
         $scope.isloading=true;
      })
        
      fileItem.remove=function(){
        $scope.isDisabled=true;
          this.uploader.removeFromQueue(this);
          $scope.uploadData=null;
      }
  }
  
  //上传文件成功
  uploader.onSuccessItem = function(fileItem, response, status, headers) {
    $rootScope.app.layout.isShadow=false; 
      if(response.result){
         $scope.uploadData=response;
         
         Notify.alert(
               '<em class="fa fa-check"></em>'+response.message ,
               {status: 'success'}
           );
         
         ngDialog.close();
         $rootScope.newFile=response.fileName;
 
      }else{

         Notify.alert(
             '<em class="fa fa-times"></em> '+response.message ,
             {status: 'danger'}
         );
      }
          
  };
  
  // 添加脚本
  $scope.addFn=function(){
     var uploaderFile=uploader.queue[0];
     uploaderFile.formData.push(
      {
        description: $scope.description||''
      }  
     );
    
     uploaderFile.upload()
  
  }
}])

// 组件过程更新
componentModule.controller('componentFlowUpdateCtrl',['$scope',function($scope){
    // 流程对应中文
    $scope.flowText=function(type){
      switch(type)
        {
           case 'deploy':
            return '部署';
           break;
           case 'start':
            return '启动';
           break;
           case 'stop':
            return '停止';
           break;
           case 'destroy':
            return '卸载';
           break;
           case 'upgrade':
            return '升级';
           break;
           case 'rollback':
            return '回滚';
           break;
           case 'snapshot':
            return '快照';
           break;
        }
    }
}])


// 验证工件包地址指令
componentModule.directive('validurl', function($http) {
    return {
        require : 'ngModel',
        link : function(scope, elm, attrs, ctrl) {
            elm.bind('keyup', function() {
            	if(scope.url){
            		$http({method: 'GET', url: '/cloudui/master/ws/resource/judgeWorkpieceUrl?url='+scope.url+'&v='+(new Date().getTime())}).
                    success(function(data, status, headers, config) {
                       
                    	if(data.result){
                            ctrl.$setValidity('url',true);
                        }else{
                            ctrl.$setValidity('url',false);
                        }
                    }).
                    error(function(data, status, headers, config) {
                        ctrl.$setValidity('url', false);
                    });
            	}
                
            });
        }
    };
}); 

/* ------------------------------------search工件------------------------------------ */
componentModule.controller('searchWorkpiecePackage',['$rootScope','$scope','$http','ngDialog','Notify','$state','$filter',function($rootScope,$scope,$http,ngDialog,Notify,$state,$filter){
    
	$scope.workpiecePackageName=''; // 选中工件
	$scope.searchPageNum=1;
	$scope.searchPageSize=5;
	// 工件列表
    $scope.onSearchPageChange = function(pageNum)
    {   
    	$http.get('/cloudui/master/ws/packageResource/listPackagesByPage'+'?v=' + (new Date().getTime()),{
	        params:
	          {
	            pageNum:pageNum,
	            pageSize:$scope.searchPageSize,
	            name:$scope.workpiecePackageSearchval
	          }
        }).success(function(data){
	    	angular.forEach(data.rows,function(val,key){
	    		 if($scope.workpiecePackageName==val.resourceName){
//					alert('true');
					data.rows[key].ischecked=true;
				 }
				 else{
					data.rows[key].ischecked=false;
				 }
	    	})
	     	$scope.searchListoff=data.rows.length>0?true:false;
	        $scope.searchWarninginfo='暂无数据';
	        $scope.searchWorkpiecePackageList = data.rows;
	        
	        $scope.searchPageCount=Math.ceil(data.total/$scope.searchPageSize);
	        if($scope.searchPageCount==0){
	           $scope.searchPageCount=1;
	        }
       }).error(function(){
         $scope.searchListoff=false;
         $scope.searchWarninginfo='暂无结果';
     })
    }

	//加载check
	$scope.workpiecePackageCheck=function(item)
	{
		if(item.ischecked){
			return true;
		}
		else{
			return false;
		}
	}

	//选中check
	$scope.workpiecePackageClick=function(item,event)
	{
		var check=event.target;
		var checked=check.checked;
		if(checked){
			$scope.workpiecePackageName=item.resourceName;
		}
	}
    
    // 确定
    $scope.confirmWorkpiecePackage=function()
    {
    	 $rootScope.newFile=$scope.workpiecePackageName;
		 ngDialog.close();
    }
    
    // 搜素
    $scope.workpiecePackageSearch=function()
    {
	   $scope.workpiecePackageName='';
	   $scope.searchPageNum=1;
 	   $scope.onSearchPageChange(1);
    }
   
}])

// 组件绑定流程
componentModule.controller('bindComponentFlowCtrl',['$rootScope','$scope','$stateParams','$http','ngDialog','$state','Notify',function($rootScope,$scope,$stateParams,$http,ngDialog,$state,Notify){

	$scope.type=$scope.ngDialogData.bindType;
	$http.get('/cloudui/master/ws/resource/listBlueprintByComponent'+'?v=' + (new Date().getTime()),{
		params:
		{
			userId:$rootScope.user.id,
			componentName:$scope.ngDialogData.componentName,
		}
	}).success(function(data){
		$scope.blueprintList=angular.fromJson(data);
		angular.forEach($scope.blueprintList,function(val,key){
			if($scope.type==0){
				if($rootScope.bindReleaseBlueprint){
					if($rootScope.bindReleaseBlueprint.ID==val.ID){
						$scope.blueprint=val;
						return;
					}
				}
			}
			if($scope.type==1){
				if($rootScope.bindRollbackBlueprint){
					if($rootScope.bindRollbackBlueprint.ID==val.ID){
						$scope.blueprint=val;
						return;
					}
				}
			}
			if($scope.type==2){
				if($rootScope.bindStartAppBlueprint){
					if($rootScope.bindStartAppBlueprint.ID==val.ID){
						$scope.blueprint=val;
						return;
					}
				}
			}
			if($scope.type==3){
				if($rootScope.bindStopAppBlueprint){
					if($rootScope.bindStopAppBlueprint.ID==val.ID){
						$scope.blueprint=val;
						return;
					}
				}
			}
		})
		//$scope.blueprint=$rootScope.bindReleaseBlueprint;
	}).error(function(){
	})
	
	$scope.$watch('blueprint',function(newval,oldval){
    	if(newval){
    		$http.get('/cloudui/master/ws/blueprint/getFlowsByInstance'+'?v=' + (new Date().getTime()),{
				params:
				{
					blueprintInstanceId:$scope.blueprint.INSTANCE_ID
				}
			}).success(function(data){
				$scope.blueprintFlow={};
				$scope.blueprintFlowList=angular.fromJson(data);
				angular.forEach($scope.blueprintFlowList,function(val,key){
					if($scope.type==0){
						if($rootScope.bindReleaseBlueprintFlow){
							if($rootScope.bindReleaseBlueprintFlow.ID==val.ID){
								$scope.blueprintFlow=val;
								return;
							}
						}
					}
					if($scope.type==1){
						if($rootScope.bindRollbackBlueprintFlow){
							if($rootScope.bindRollbackBlueprintFlow.ID==val.ID){
								$scope.blueprintFlow=val;
								return;
							}
						}
					}
					if($scope.type==2){
						if($rootScope.bindStartAppBlueprintFlow){
							if($rootScope.bindStartAppBlueprintFlow.ID==val.ID){
								$scope.blueprintFlow=val;
								return;
							}
						}
					}
					if($scope.type==3){
						if($rootScope.bindStopAppBlueprintFlow){
							if($rootScope.bindStopAppBlueprintFlow.ID==val.ID){
								$scope.blueprintFlow=val;
								return;
							}
						}
					}
				})
				//$scope.blueprintFlow=$rootScope.bindReleaseBlueprintFlow;
			}).error(function(){
			})
    	}
    })
		
	$scope.bindComponentFlow=function(){
		$http({
			method:'post',
			url:'/cloudui/master/ws/resource/bindComponentFlow',
			data: $.param({
				componentId:$scope.ngDialogData.componentId,
				bindType:$scope.ngDialogData.bindType,
				blueprintId:$scope.blueprint.ID,
				blueprintFlow:$scope.blueprintFlow.ID
			}),
			headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
		})
		.success(function(data){
        	if(data.result){
	    		Notify.alert(
                    '<em class="fa fa-check"></em> '+data.message ,
                    {status: 'success'}
	            );
	    		ngDialog.close();
				if($scope.type==0){
					$rootScope.bindReleaseBlueprint=$scope.blueprint;
					$rootScope.bindReleaseBlueprintFlow=$scope.blueprintFlow;
				}
				if($scope.type==1){
					$rootScope.bindRollbackBlueprint=$scope.blueprint;
					$rootScope.bindRollbackBlueprintFlow=$scope.blueprintFlow;
				}
				if($scope.type==2){
					$rootScope.bindStartAppBlueprint=$scope.blueprint;
					$rootScope.bindStartAppBlueprintFlow=$scope.blueprintFlow;
				}
				if($scope.type==3){
					$rootScope.bindStopAppBlueprint=$scope.blueprint;
					$rootScope.bindStopAppBlueprintFlow=$scope.blueprintFlow;
				}
	    	}else{
	    		Notify.alert(
                    '<em class="fa fa-check"></em> '+data.message ,
                    {status: 'danger'}
	            );
	    	}
        })
    }
}])

