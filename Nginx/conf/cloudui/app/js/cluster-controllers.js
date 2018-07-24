var clusterModule=angular.module('cluster',['ngResource']);

/* ------------------------------------环境管理------------------------------------ */
clusterModule.controller('clusterManageCtrl',['$rootScope','$scope','Notify','ngDialog','$http','$state','$cookieStore','$filter','$location',function($rootScope,$scope,Notify,ngDialog,$http,$state,$cookieStore,$filter,$location){
  
  $scope.clusterPages=[
     "app.cluster_manage",
     "app.cluster_detail",
     "app.node_add",
     "app.node_edit"
  ]
  
  // 环境列表
  $scope.isLoad=false;
  $scope.pageSize=8; 

  $scope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
    var isClear=$filter('filter')($scope.clusterPages,toState.name).length>0?false:true;
    if(isClear){
      $scope.clearCookie(); 
    }
  });

  
  $scope.searchval=$cookieStore.get('clusterSearch')?$cookieStore.get('clusterSearch'):'';
 

  if($cookieStore.get('clusterPageNum')){
     $scope.listDataPromise=$http.get('/cloudui/master/ws/cluster'+'?v=' + (new Date().getTime()),{
        params:
        {
          pageNum:1,
          pageSize:$scope.pageSize,
          name:$scope.searchval||''
        }
     }).then(function(res){
        
         $scope.totalPageNum=res.data.totalPageNum;
         
         if($cookieStore.get('clusterPageNum')<=$scope.totalPageNum){
            $scope.pageNum=$cookieStore.get('clusterPageNum');
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
    $cookieStore.put('clusterPageNum',$scope.pageNum);
    $cookieStore.put('clusterSearch',$scope.searchval);
  }
  
  $scope.clearCookie=function(){
    $cookieStore.remove("clusterPageNum");
    $cookieStore.remove("clusterSearch");
  }
     
  // 新建环境弹出框
  $scope.openCreateCluster = function () {
        ngDialog.open({
          template: 'app/views/dialog/cluster-create.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          scope: $scope,
          cache:false,
          closeByDocument:false,
          controller:'createClusterCtrl'
        });
  };

  // 删除环境
  $scope.openDelCluster=function(params){
      if(params.hostNum>0){
        Notify.alert( 
                '环境上有主机不能删除！', 
                {status: 'info'}
            );
        return false;
      }
        ngDialog.openConfirm({
           template:
                '<p class="modal-header">您确定要删除 '+params.name+' 环境吗?</p>' +
                '<div class="modal-body text-right">' +
                  '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
                  '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
                '</button></div>',
          plain: true,
          className: 'ngdialog-theme-default'
        })
        .then(
          function(){
           $rootScope.app.layout.isShadow=true;
           $scope.setCookie();
             $http.delete('/cloudui/master/ws/cluster',{
               params:{
                 clusterId:params.id 
               }
             })
             .success(function(data){
               $rootScope.app.layout.isShadow=false;
                 if(data.result)
                 {
                    Notify.alert( 
                       '<em class="fa fa-check"></em> 删除成功！', 
                       {status: 'success'}
                    );
                    $state.go('app.cluster_manage',{},{ reload: true });
                 }else
                 {
                     Notify.alert( 
                        '<em class="fa fa-times"></em> '+data.info, 
                        {status: 'danger'}
                     );
                 }
             })
          }
        )
    }
  
  $scope.sortOrder= 'DESC';
  $scope.sortName= '';
  $scope.fnSort = function (arg,order) {
           //arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
           $scope.sortOrder= order;
           $scope.sortName =arg;

           // alert($scope.sortOrder+":"+$scope.sortName);
           $scope.onPageChange();
       }
    $scope.onPageChange = function ()
    {
      if(!$scope.pageNum){
         return ;
      }
      $http.get('/cloudui/master/ws/cluster'+'?v=' + (new Date().getTime()),{
        params:
            {
              pageNum:$scope.pageNum,
              pageSize:$scope.pageSize,
              name:$scope.searchval||'',
              sortOrder:$scope.sortOrder||'DESC',
              sortName:$scope.sortName||''
            }
      }).success(function(data){
            $scope.isLoad=true;
            $scope.isExistCluster=data.rows.length>0?true:false;
              $scope.clusterList = data.rows;
              $scope.pageCount=Math.ceil(data.total/$scope.pageSize);
              if($scope.pageCount==0){
                $scope.pageCount=1;
              }
      }).error(function(){
          $scope.isExistCluster=false;
      })
    } 
    
    // 搜素环境
    $scope.search=function()
    { 
         $scope.pageNum=1;
         $scope.setCookie();
         $scope.onPageChange(); 
    } 
}])

/* -----------------------------------------创建环境---------------------------------- */
clusterModule.controller('createClusterCtrl',['$rootScope','$scope','$http','$state','ngDialog',function($rootScope,$scope,$http,$state,ngDialog){
   
  $scope.account = {};
 
  $scope.authMsg = '';
  
  $rootScope.submitted = false;

  $scope.createclusterFn = function(obj) {
    
  $rootScope.submitted = true;
     
    $scope.authMsg = '';

    // 验证环境名字 
    $http.get('/cloudui/master/ws/cluster/check'+'?v=' + (new Date().getTime()),{
      params:{
        name:$scope.account.name
      }
    }).success(function(data) {
           if(data.result)
           {
             $rootScope.app.layout.isShadow=true;
              // 新增环境
              $http({
                method  : 'POST',
                url     : '/cloudui/master/ws/cluster',
                data    : {name:$scope.account.name},  
                headers : { 'Content-Type': 'application/json' }
              })
              .then(function(response) {
                $rootScope.app.layout.isShadow=false;
                if (response.data.result ) {
                  ngDialog.close();
                  $state.go('app.cluster_detail.nodes',{clusterid:response.data.id},{ reload: true });
                }else{
                   $scope.authMsg = '创建失败，请重新添加环境！';  
                }
              }, function(x) {
                $scope.authMsg = '服务器请求错误';
              });
              
           }else{
              $scope.authMsg = '此环境已存在，请重新命名！';
              $scope.createclusterForm.$valid=false;
           }

        })
  };

}])

/* -------------------------------------重命名环境---------------------------------- */
clusterModule.controller('changeClusterNameCtrl',['$rootScope','$scope','$http','$state','$stateParams','ngDialog','Notify',function($rootScope,$scope,$http,$state,$stateParams,ngDialog,Notify){
      
  $scope.account = {};

  $scope.account.name=$scope.ngDialogData.name;
 
  $scope.authMsg = '';
  
  $rootScope.submitted = false;

  $scope.changeclusterNameFn = function() {
    
  $rootScope.submitted = true;
     
    $scope.authMsg = '';

    // 验证名字 
    $http.get('/cloudui/master/ws/cluster/checkOther'+'?v=' + (new Date().getTime()),{
      params:{
        name:$scope.account.name,
        id:$stateParams.clusterid
      }
    }).success(function(data) {
         if(data.result)
         {
          $rootScope.app.layout.isShadow=true;
            // 重命名环境  
            $http({
               method:'PUT',
               url:'/cloudui/master/ws/cluster',  
               data: {
                 id:$stateParams.clusterid,
                 name:$scope.account.name
                },
             headers : { 'Content-Type': 'application/json' }
            }).success(function(data){
              $rootScope.app.layout.isShadow=false;
              if(data.result)
                {
                   Notify.alert(
                      '<em class="fa fa-check"></em> 重命名成功!' ,
                      {status: 'success'}
                   );
                   ngDialog.close();
                   $state.go('app.cluster_detail.nodes',{clusterid:$stateParams.clusterid},{reload:true});
                }else{
                   Notify.alert(
                      '<em class="fa fa-times"></em> 重命名失败!' ,
                      {status: 'danger'}
                   );
                } 
            })
         }else{
            $scope.authMsg = '此环境已存在，请重新命名！';
            $scope.changeclusterNameForm.$valid=false;
         }

      })
  };

}])


/* --------------------------------------环境详情蓝图实例列表---------------------------------- */
clusterModule.controller('clusterBlueprintsCtrl',['$rootScope','$scope','$http','$stateParams','ngDialog','$state','Notify','$interval','$cookieStore',function($rootScope,$scope,$http,$stateParams,ngDialog,$state,Notify,$interval,$cookieStore){
    // 环境基本信息   
    $http.get('/cloudui/master/ws/cluster/'+$stateParams.clusterid).then(function (response) {
      $scope.clusterDetail = response.data;
      $scope.clusterId = response.data.id;
    },
    function (response) {
    });
    // $scope.$watch('pageNum',function(newval,oldval){
    //    $scope.pageNum=newval;
    // })
    // 重命名集群弹出框
    $scope.openchangeCluster = function (params) {
      ngDialog.open({
        template: 'app/views/dialog/cluster-changename.html'+'?action='+(new Date().getTime()),
        className: 'ngdialog-theme-default',
        scope: $scope,
        cache: false,
        closeByDocument:false,
        data:{name:params},
        controller:'changeClusterNameCtrl'
      });
    };
    $scope.blueprintInstanceFun = function () {
      $http.get('/cloudui/master/ws/dashboardnew/blueprintInstanceInCluster', {
        params:{
          blueprintName:$scope.blueprintName||'',
          instanceName:$scope.instanceName||'',
          clusterId:$scope.clusterId,
          sortName:$scope.sortName,
          sortOrder:$scope.sortOrder,
          pageSize:$scope.pageSize || 10,
          pageNum:$scope.pageNum || 1
        }
      }).then(function (response) {
        $scope.blueprintInstances = response.data.rows;
        $scope.totalPageNum = response.data.totalPageNum;
      });
    };
    $scope.sortName = 'NAME';
    $scope.sortOrder = 'ASC';
    $scope.pageSize = 10;
    $scope.pageNum = 1;
    // 搜索类型
    $scope.searchTypeList = [
      {text:'按蓝图模板名称搜索',value:'blueprint'},
      {text:'按蓝图实例名称搜索',value:'blueprint-ins'}
    ];
    $scope.searchType = 'blueprint';
    $scope.searchval = '';
    $scope.searchValFn=function(){
        if($scope.searchType=='blueprint'){ // 按蓝图搜索
           $scope.blueprintName=$scope.searchval;
           $scope.instanceName='';
       } else if ($scope.searchType=='blueprint-ins'){ // 按蓝图实例搜索
           $scope.blueprintName='';
           $scope.instanceName=$scope.searchval;
       }
    }
    
    // 搜索蓝图实例
    $scope.search=function()
    { 
       $scope.searchValFn();
       $scope.pageNum=1;
       $scope.blueprintInstanceFun(); 
    }
    // $scope.initPageFun = function () {
    //   if ($cookieStore.get('clusterBlueprintInstanceActivityPageNum')) {
    //     $http.get('/cloudui/master/ws/dashboardnew/blueprintInstanceInCluster', {
    //       params:{
    //         blueprintName:$scope.blueprintName||'',
    //         instanceName:$scope.instanceName||'',
    //         clusterId:$scope.clusterId,
    //         sortName:$scope.sortName,
    //         sortOrder:$scope.sortOrder,
    //         pageSize:$scope.pageSize,
    //         pageNum:1
    //       }
    //     }).then(function (response) {
    //       $scope.totalPageNum = response.data.totalPageNum;
    //       if($cookieStore.get('clusterBlueprintInstanceActivityPageNum') <= $scope.totalPageNum){
    //         $scope.pageNum = $cookieStore.get('clusterBlueprintInstanceActivityPageNum');
    //       }else{
    //         if ($scope.totalPageNum && $scope.totalPageNum > 0) {
    //           $scope.pageNum = $scope.totalPageNum;
    //         } else {
    //           $scope.pageNum = 1;
    //         }
    //       }
    //     });
    //   } else {
    //     $scope.pageNum = 1;
    //   }
    //   $cookieStore.put('clusterBlueprintInstanceActivityPageNum', $scope.pageNum);
    // };
    // $scope.initPageFun();
    $scope.blueprintInstanceTimer = $interval($scope.blueprintInstanceFun, 3000);
    $scope.fnSort = function(arg) {
      arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
      $scope.sortOrder = arguments.callee['fnSort' + arg]?'ASC':'DESC';
      $scope.sortName = arg;
      $scope.blueprintInstanceFun();
    };
    $scope.cancelTimer = function () {
      $interval.cancel($scope.blueprintInstanceTimer);
    }
    $scope.$on('$destroy', $scope.cancelTimer);
}])


/* --------------------------------------环境详情组件列表---------------------------------- */
clusterModule.controller('clusterComponentsCtrl',['$rootScope','$scope','$http','$stateParams','ngDialog','$state','Notify','$interval','$cookieStore',function($rootScope,$scope,$http,$stateParams,ngDialog,$state,Notify,$interval,$cookieStore){
    // 环境基本信息   
    $http.get('/cloudui/master/ws/cluster/'+$stateParams.clusterid).then(function (response) {
      $scope.clusterDetail = response.data;
      $scope.clusterId = response.data.id;
    },
    function (response) {
    });
    // 重命名集群弹出框
    $scope.openchangeCluster = function (params) {
      ngDialog.open({
        template: 'app/views/dialog/cluster-changename.html'+'?action='+(new Date().getTime()),
        className: 'ngdialog-theme-default',
        scope: $scope,
        cache: false,
        closeByDocument:false,
        data:{name:params},
        controller:'changeClusterNameCtrl'
      });
    };
    $scope.componentFun = function () {
      $http.get('/cloudui/master/ws/dashboardnew/componentInCluster', {
        params:{
          componentName:$scope.componentName||'',
          nodeName:$scope.nodeName||'',
          instanceName:$scope.instanceName||'',
          hostname:$scope.hostname||'',
          clusterId:$scope.clusterId,
          sortName:$scope.sortName,
          sortOrder:$scope.sortOrder,
          pageSize:$scope.pageSize || 10,
          pageNum:$scope.pageNum || 1
        }
      }).then(function (response) {
        $scope.components = response.data.rows;
        $scope.totalPageNum = response.data.totalPageNum;
      });
    };
    $scope.sortName = 'NODE_NAME';
    $scope.sortOrder = 'ASC';
    $scope.pageSize = 10;
    $scope.pageNum = 1;
    // 搜索类型
    $scope.searchTypeList = [
      {text:'按组件名称搜索',value:'componentName'},
      {text:'按组件自定义名称搜索',value:'nodeName'},
      {text:'按蓝图实例名称搜索',value:'instanceName'},
      {text:'按主机名称搜索',value:'hostname'}
    ];
    $scope.searchType = 'componentName';
    $scope.searchval = '';
    $scope.searchValFn=function(){
        if($scope.searchType=='componentName'){ // 按组件名称搜索
           $scope.componentName=$scope.searchval;
           $scope.nodeName='';
           $scope.instanceName='';
           $scope.hostname = '';
       } else if ($scope.searchType=='nodeName'){ // 按组件自定义名称搜索
           $scope.componentName='';
           $scope.nodeName=$scope.searchval;
           $scope.instanceName='';
           $scope.hostname = '';
       } else if ($scope.searchType=='instanceName'){ // 按蓝图实例名称搜索
           $scope.componentName='';
           $scope.nodeName='';
           $scope.instanceName=$scope.searchval;
           $scope.hostname = '';
       } else if ($scope.searchType=='hostname'){ // 按主机名称搜索
           $scope.componentName='';
           $scope.nodeName='';
           $scope.instanceName='';
           $scope.hostname = $scope.searchval;
       }
    }
    
    // 搜索蓝图实例
    $scope.search=function()
    { 
       $scope.searchValFn();
       $scope.pageNum=1;
       $scope.componentFun(); 
    }
    // $scope.initPageFun = function () {
    //   if ($cookieStore.get('clusterComponentActivityPageNum')) {
    //     $http.get('/cloudui/master/ws/dashboardnew/componentInCluster', {
    //       params:{
    //         componentName:$scope.componentName||'',
    //         nodeName:$scope.nodeName||'',
    //         instanceName:$scope.instanceName||'',
    //         clusterId:$scope.clusterId,
    //         sortName:$scope.sortName,
    //         sortOrder:$scope.sortOrder,
    //         pageSize:$scope.pageSize,
    //         pageNum:1
    //       }
    //     }).then(function (response) {
    //       $scope.totalPageNum = response.data.totalPageNum;
    //       if($cookieStore.get('clusterComponentActivityPageNum') <= $scope.totalPageNum){
    //         $scope.pageNum = $cookieStore.get('clusterComponentActivityPageNum');
    //       }else{
    //         if ($scope.totalPageNum && $scope.totalPageNum > 0) {
    //           $scope.pageNum = $scope.totalPageNum;
    //         } else {
    //           $scope.pageNum = 1;
    //         }
    //       }
    //     });
    //   } else {
    //     $scope.pageNum = 1;
    //   }
    //   $cookieStore.put('clusterComponentActivityPageNum', $scope.pageNum);
    // };
    // $scope.initPageFun();
    $scope.componentTimer = $interval($scope.componentFun, 3000);
    $scope.fnSort = function(arg) {
      arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
      $scope.sortOrder = arguments.callee['fnSort' + arg]?'ASC':'DESC';
      $scope.sortName = arg;
      $scope.componentFun();
    };
    $scope.cancelTimer = function () {
      $interval.cancel($scope.componentTimer);
    }
    $scope.$on('$destroy', $scope.cancelTimer);
}])



/* --------------------------------------环境上的主机---------------------------------- */
clusterModule.controller('nodesListCtrl',['$rootScope','$scope','$http','$stateParams','ngDialog','Notify','$state','$filter','$interval',function($rootScope,$scope,$http,$stateParams,ngDialog,Notify,$state,$filter,$interval){
  $scope.$on('$destroy', function() {
	 $interval.cancel($scope.nodeTimer);  
  });
	// 重命名集群弹出框
   $scope.openchangeCluster = function (params) {
      ngDialog.open({
        template: 'app/views/dialog/cluster-changename.html'+'?action='+(new Date().getTime()),
        className: 'ngdialog-theme-default',
        scope: $scope,
        cache: false,
        closeByDocument:false,
        data:{name:params},
        controller:'changeClusterNameCtrl'
      });
   };

   // 删除环境
   $scope.openDelCluster=function(params){
     if(params.hostNum>0) 
      {
       Notify.alert( 
                '环境上有主机不能删除！', 
                {status: 'info'}
           );
        return false;
      }
      ngDialog.openConfirm({
         template:
              '<p class="modal-header">您确定要删除此环境吗?</p>' +
              '<div class="modal-body text-right">' +
                '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
                '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
              '</button></div>',
        plain: true,
        className: 'ngdialog-theme-default'
      })
      .then(
        function(){
           $rootScope.app.layout.isShadow=true;
     
           $http.delete('/cloudui/master/ws/cluster',{
               params:{
                 clusterId:params.id 
               }
           })
           .success(function(data){
             $rootScope.app.layout.isShadow=false;
               if(data.result)
               {
                 Notify.alert( 
                  '<em class="fa fa-check"></em> 删除成功！', 
                  {status: 'success'}
                 );
                 $state.go('app.cluster_manage',{},{ reload: true });
               }else
               {
               Notify.alert( 
                     '<em class="fa fa-times"></em> '+data.info, 
                     {status:'danger'}
                  );
               }
          })  
        }
      )
  }

  // 环境基本信息   
  $http.get('/cloudui/master/ws/cluster/'+$stateParams.clusterid).success(function(data){ 
      $scope.clusterDetail = data;
  })

  $scope.checkappId=[]; // 选中的主机
  // 主机列表 
  $scope.pageSize=10;
  $scope.pageNum=1;
  
  $scope.sortOrder= 'DESC';
  $scope.sortName= '';
  $scope.fnSort = function (arg) {
          arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
          $scope.sortOrder= arguments.callee['fnSort' + arg]?'ASC':'DESC';
          $scope.sortName =arg;

          // alert($scope.sortOrder+":"+$scope.sortName);
          $scope.onPageChange();
    }
  $scope.onPageChange = function ()
  { 
    if(!$scope.pageNum){
         return ;
      } 
     $http.get('/cloudui/master/ws/node'+'?v='+(new Date().getTime()),{
     params:
         {
           pageNum:$scope.pageNum,
           pageSize:$scope.pageSize,
           clusterId:$stateParams.clusterid,
           sortOrder:$scope.sortOrder||'DESC',
           sortName:$scope.sortName||''
         }
    }).then(function(res){
    	var statusStr=(''+res.status).charAt(0);
    	if(statusStr=='5'){ // 异常码
             $interval.cancel($scope.nodeTimer); 
        }else{
        	var data = res.data;
        	angular.forEach(data.rows,function(val,key){
                var ischecked=$filter('filter')($scope.checkappId,val.id).length>0?true:false;
                data.rows[key].ischecked=ischecked;
            })
                
            $scope.nodesList = data.rows;
            $scope.listoff=data.rows.length>0?true:false;
            $scope.warninginfo='提示：暂无主机';
               
            $scope.pageCount=Math.ceil(data.total/$scope.pageSize);
              if($scope.pageCount==0){
                $scope.pageCount=1;
              }
        }
    },function(res){ // 404
        $scope.listoff=false;
        $scope.warninginfo='暂无结果';
        $interval.cancel($scope.nodeTimer); 
    })
  }
  $scope.nodeTimer=$interval(function(){
    if(!$scope.pageNum){
         return ;
      } 
      $http.get('/cloudui/master/ws/node'+'?v='+(new Date().getTime()),{
      params:
         {
           pageNum:$scope.pageNum,
           pageSize:$scope.pageSize,
           clusterId:$stateParams.clusterid,
           sortOrder:$scope.sortOrder||'DESC',
           sortName:$scope.sortName||''
         }
      }).then(function(res){
          var statusStr=(''+res.status).charAt(0);
  
          if(statusStr=='5'){ // 异常码
                $interval.cancel($scope.nodeTimer); 
          }else{
               for(var i=0;i<$scope.nodesList.length;i++)
              { 
                $scope.nodesList[i].isConnected=$filter('filter')(res.data.rows,$scope.nodesList[i].id)[0].isConnected;
              }
          }

      },function(res){ // 404
          $interval.cancel($scope.nodeTimer); 
      })
 
  },3000)
   // 删除主机
   
   $scope.delNodeHttp=function(ids,index){
     $rootScope.app.layout.isShadow=true;
     $http.delete('/cloudui/master/ws/node/multiDelete',{params:
         {
          nodes:ids,
          clusterId:$stateParams.clusterid
       }
     }).success(function(data){
          $rootScope.app.layout.isShadow=false;
            if(data.result)
            {
                Notify.alert(
                  '<em class="fa fa-check"></em> 删除成功！' ,
                  {status: 'success'}
                );
                
                if(index)
                {
                   $scope.nodesList.splice(index, 1);
                }else{
                  $state.go('app.cluster_detail.nodes',{clusterid:$stateParams.clusterid},{reload:true});
                }              
            }else
            {
                Notify.alert(
                  '<em class="fa fa-check"></em> '+data.info ,
                  {status: 'danger'}
                );
            }
        }) 
   }

   $scope.delNodeFn=function(params,index)
   {
     
       ngDialog.openConfirm({
           template:
               '<p class="modal-header">您确定要删除吗?</p>' +
               '<div class="modal-body text-right">' +
                 '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
                 '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
               '</button></div>',
           plain: true,
           className: 'ngdialog-theme-default'
       })
       .then(
          function(){  
            var ids=''; // 主机id字符串
            if(angular.isObject(params)){
                             
                ids=params.join(',');
               
              $scope.delNodeHttp(ids);
            }else{
              $scope.delNodeHttp(params,index);
            } 
          }
       )
   }

   $scope.openDelNodeFn=function(params,index){
    
       if(params)
       {
          $scope.delNodeFn(params,index);
       }else
       {
        var checkbox = $scope.checkappId;
          if(checkbox.length==0)
          {
             Notify.alert(
                '请选择您要删除的主机!' ,
                {status: 'info'}
             );
          }else
          {
             $scope.delNodeFn(checkbox);
          }
       }
   }  
     
     
}])


/* ------------------------------------添加主机------------------------------------ */
clusterModule.controller('addNodeCtrl',['$rootScope','$scope','$http','ngDialog','$stateParams','$state','$interval','Notify',function($rootScope,$scope,$http,ngDialog,$stateParams,$state,$interval,Notify){
 
  $scope.clusterid=$stateParams.clusterid;
  $scope.clustername=$stateParams.clustername;
  $scope.systems=[
                  "Centos 6.7",
                  "Centos 7.2",
                  "AIX 7.1",
                  "Redhat Linux 6.4",
                  "SUSE 11 sp3",
                  "Windows"
                  ]
  $scope.system='Centos 6.7';
 
  $scope.formnodeData={};

  var addNodeFn=function(){
    $http({
           method:'post',
           url:'/cloudui/master/ws/node',
           data: {
             name:$scope.formnodeData.name,
             description:$scope.formnodeData.description,
             ip:$scope.formnodeData.ip,
             userName:$scope.userName,
             userPassword:$scope.userPassword,
           clusterId:$stateParams.clusterid,
           osType:$scope.system
           },
           headers : { 'Content-Type': 'application/json' }
       }).success(function(data){
           if(data.result){
             Notify.alert(
                     '<em class="fa fa-check"></em> 添加主机成功！' ,
                     {status: 'success'}
                  );
             $state.go('app.cluster_detail.nodes',{clusterid:$stateParams.clusterid},{reload:true});
           }else{
             Notify.alert(
                     '<em class="fa fa-times"></em> '+data.info ,
                     {status: 'danger'}
                  );
           }
       })
  }
 // 发送命令
 $rootScope.submitted = false;
 $scope.addNodeFn=function(){
   $rootScope.submitted = true;
   if($scope.system !='Windows'){
     $scope.userName=$scope.formnodeData.username;
     $scope.userPassword=$scope.formnodeData.password;
   }else if($scope.system=='Windows'){
     $scope.userName='';
     $scope.userPassword='';
   }
   $rootScope.app.layout.isShadow=true;
   $http({
         method:'post',
         url:'/cloudui/master/ws/node/detectNode',
         data: {
           name:$scope.formnodeData.name,
           description:$scope.formnodeData.description,
           ip:$scope.formnodeData.ip,
           userName:$scope.userName,
           userPassword:$scope.userPassword,
         clusterId:$stateParams.clusterid,
         osType:$scope.system
         },
         headers : { 'Content-Type': 'application/json' }
     }).success(function(data){
       $rootScope.app.layout.isShadow=false;
         if(data.result){
          //添加主机
          addNodeFn();
         }else{
           ngDialog.openConfirm({
                template:
                     '<p class="modal-header">该主机网络不可达，您确定要添加此主机吗?</p>' +
                     '<div class="modal-body text-right">' +
                       '<button type="button" class="btn btn-default mr20"  ng-click="closeThisDialog(0)">取消' +
                       '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
                     '</button></div>',
               plain: true,
               className: 'ngdialog-theme-default'
               }).then(function(){
                 //添加主机
                 addNodeFn();
            })
         }
     })
   
 }

}])

/* ------------------------------------编辑主机------------------------------------ */
clusterModule.controller('editNodeCtrl',['$rootScope','$scope','$http','ngDialog','$stateParams','$state','$interval','Notify',function($rootScope,$scope,$http,ngDialog,$stateParams,$state,$interval,Notify){
 
  $scope.formnodeData={};
  
  
  
  //环境基本信息   
  $http.get('/cloudui/master/ws/cluster/'+$stateParams.clusterid).success(function(data){ 
      $scope.clustername=data.name;
  })
  
  // 主机信息
  $http.get('/cloudui/master/ws/node/'+$stateParams.nodeid).success(function(data){ 
      $scope.nodeDetail=data;
      $scope.formnodeData.name=data.name;
      $scope.formnodeData.description=data.description||'';
      $scope.formnodeData.ip=data.ip;
      $scope.system=data.osType;
  })

  var editNodeFn=function(){
     $rootScope.submitted = true;
     if($scope.system=='linux'){
       $scope.userName=$scope.formnodeData.username;
       $scope.userPassword=$scope.formnodeData.password;
     }else if($scope.system=='windows'){
       $scope.userName='';
       $scope.userPassword='';
     }
     $http({
           method:'put',
           url:'/cloudui/master/ws/node',
           data: {
             name:$scope.formnodeData.name,
             description:$scope.formnodeData.description,
             ip:$scope.formnodeData.ip,
             userName:$scope.userName,
             userPassword:$scope.userPassword,
           id:$stateParams.nodeid,
           osType:$scope.system
           },
           headers : { 'Content-Type': 'application/json' }
       }).success(function(data){
           if(data.result){
             Notify.alert(
                     '<em class="fa fa-check"></em> 修改主机成功！' ,
                     {status: 'success'}
                  );
             $state.go('app.cluster_detail.nodes',{clusterid:$stateParams.clusterid},{reload:true});
           }else{
             Notify.alert(
                     '<em class="fa fa-times"></em> '+data.info ,
                     {status: 'danger'}
                  );
           }
       })
   }
  //发送命令
  $rootScope.submitted = false;
  $scope.editNodeFn=function(){
     $rootScope.submitted = true;
     if($scope.system=='linux'){
       $scope.userName=$scope.formnodeData.username;
       $scope.userPassword=$scope.formnodeData.password;
     }else if($scope.system=='windows'){
       $scope.userName='';
       $scope.userPassword='';
     }
     $rootScope.app.layout.isShadow=true;
     $http({
           method:'post',
           url:'/cloudui/master/ws/node/detectNode',
           data: {
             name:$scope.formnodeData.name,
             description:$scope.formnodeData.description,
             ip:$scope.formnodeData.ip,
             userName:$scope.userName,
             userPassword:$scope.userPassword,
           clusterId:$stateParams.clusterid,
           osType:$scope.system
           },
           headers : { 'Content-Type': 'application/json' }
       }).success(function(data){
         $rootScope.app.layout.isShadow=false;
           if(data.result){
            //编辑主机
            editNodeFn();
           }else{
             ngDialog.openConfirm({
                  template:
                       '<p class="modal-header">该主机网络不可达，您确定要编辑此主机吗?</p>' +
                       '<div class="modal-body text-right">' +
                         '<button type="button" class="btn btn-default mr20"  ng-click="closeThisDialog(0)">取消' +
                         '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
                       '</button></div>',
                 plain: true,
                 className: 'ngdialog-theme-default'
                 }).then(function(){
                   //编辑主机
                   editNodeFn();
              })
           }
       })
     
   }

}])

//添加主机验证ip唯一
clusterModule.directive('addnodeonlyip', function($http) {
    return {
        require : 'ngModel',
        link : function(scope, elm, attrs, ctrl) {
           function validFn(){
             $http(
                {
                  method: 'get', 
                  url: '/cloudui/master/ws/node/checkIP'+'?v='+(new Date().getTime()),
                    params:{
                      ip:scope.formnodeData.ip
                    }
                }
                ). 
                success(function(data, status, headers, config) {
                    if(data.result){
                        ctrl.$setValidity('isonly',true);
                    }else{
                        ctrl.$setValidity('isonly',false);
                    }
                }).
                error(function(data, status, headers, config) {
                    ctrl.$setValidity('isonly', false);
                });
            }

            scope.$watch('formnodeData.ip',function(newval,oldval){
               if(newval&&(newval!==oldval)){
                   validFn();
               }
            })
        }
    };
}); 


//编辑主机验证ip唯一
clusterModule.directive('editnodeonlyip', function($http) {
    return {
        require : 'ngModel',
        link : function(scope, elm, attrs, ctrl) {

            function validFn(){
                $http(
                {
                  method: 'get', 
                  url: '/cloudui/master/ws/node/checkOtherIP'+'?v='+(new Date().getTime()),
                    params:{
                      ip:scope.formnodeData.ip,
                      id:scope.nodeDetail.id
                    }
                }
                ). 
                success(function(data, status, headers, config) {
                    if(data.result){
                        ctrl.$setValidity('isonly',true);
                    }else{
                        ctrl.$setValidity('isonly',false);
                    }
                }).
                error(function(data, status, headers, config) {
                    ctrl.$setValidity('isonly', false);
                });
            }

            elm.bind('change', function() {
                 validFn();
            });
        }
    };
}); 

