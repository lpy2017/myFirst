var releaseIntegrationMoudle=angular.module('release-integration',[]);
releaseIntegrationMoudle.controller('a',['$scope',function($scope){

}])
/* ------------------------------------集成列表------------------------------------ */
releaseIntegrationMoudle.controller('releaseIntegrationManageCtrl',['$scope','ngDialog','$http','Notify','$state',function($scope,ngDialog,$http,Notify,$state){
    // 类型列表 
    $scope.typeList=[
        {"value":"02","text":"redmine"},
        {"value":"03","text":"jira"},
        {"value":"04","text":"it作业管理平台"},
        {"value":"05","text":"dragonFly"},
        {"value":"06","text":"其他"}
    ]
    // 新增集成
    $scope.releaseIntegrationAddFn=function(item){
        ngDialog.open({
            template: 'app/views/dialog/release-integration-add.html'+'?action='+(new Date().getTime()),
            className: 'ngdialog-theme-default ngdialog-sm',
            scope: $scope,
            cache:false,
            closeByDocument:false,
            controller:"releaseIntegrationAddCtrl"
        });
    }
    // 编辑集成
    $scope.releaseIntegrationEditFn=function(item){
        ngDialog.open({
            template: 'app/views/dialog/release-integration-edit.html'+'?action='+(new Date().getTime()),
            className: 'ngdialog-theme-default ngdialog-sm',
            scope: $scope,
            cache:false,
            data:{detail:item},
            closeByDocument:false,
            controller:"releaseIntegrationEditCtrl"
        });
    }
    // 删除集成
    $scope.delIntegrationFn=function(index,id){
        ngDialog.openConfirm({
            template:
               '<p class="modal-header">您确定要删除此集成吗?</p>' +
               '<div class="modal-body text-right">' +
                 '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
                 '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
               '</button></div>',
            plain: true,
            className: 'ngdialog-theme-default'
        }).then(function(){
                $http({
                    method:'post',
                    url:'/cloudui/master/ws/releaseIntegration/deleteReleaseIntegration',
                    data:$.param({
                        integrationId:id
                    }),
                    headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
                }).then(function(res){
                        if(res.data.result){
                            Notify.alert(
                                '<em class="fa fa-check"></em> '+res.data.message ,
                                {status: 'success'}
                            ); 
                            $state.go('app.release_integration_manage',{},{reload:true});
                        }else{
                            Notify.alert(
                                '<em class="fa fa-times"></em> '+res.data.message ,
                                {status: 'danger'}
                            ); 
                        }
                })
        })
    }
    // 同步集成
    $scope.synReleaseIntegrationFn=function(id){
        $http({
            method:'post',
            url:'/cloudui/master/ws/releaseIntegration/synReleaseIntegration',
            data:$.param({
                integrationId:id
            }),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
        }).then(function(res){
                if(res.data.result){
                    Notify.alert(
                        '<em class="fa fa-check"></em> '+res.data.message ,
                        {status: 'success'}
                    ); 
                    $state.go('app.release_integration_manage',{},{reload:true});
                }else{
                    Notify.alert(
                        '<em class="fa fa-times"></em> '+res.data.message ,
                        {status: 'danger'}
                    ); 
                }
        })
    }

    // 集成列表
    // $scope.sortOrder= 'DESC';
    // $scope.sortName= 'startTime';
    // $scope.fnSort = function (arg) {
    //     arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
    //     $scope.sortOrder= arguments.callee['fnSort' + arg]?'ASC':'DESC';
    //     $scope.sortName =arg;
    //     $scope.onPageChange();
    // }

    $scope.pageSize=10;
    $scope.pageNum=1;
    $scope.onPageChange = function ()
    {   
       $http({
        method:'post',
          url:'/cloudui/master/ws/releaseIntegration/listReleaseIntegrations',
          data:$.param({
                "pageSize": $scope.pageSize,
                "pageNum": $scope.pageNum
          }),
          headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
      }).then(function(res){
         if(res.data.result){
             $scope.listoff=res.data.page.rows.length>0?true:false;
             $scope.warninginfo='暂无数据';
             $scope.integrationList = res.data.page.rows;
             $scope.pageCount=Math.ceil(res.data.page.total/$scope.pageSize);
             if($scope.pageCount==0){
                $scope.pageCount=1;
             }
         }else{
             $scope.listoff=false;
             $scope.warninginfo=res.data.message;
         }
      }) 
    }
}])
/* ------------------------------------新增集成------------------------------------ */
releaseIntegrationMoudle.controller('releaseIntegrationAddCtrl',['$rootScope','$scope','ngDialog','$http','Notify','$state',function($rootScope,$scope,ngDialog,$http,Notify,$state){
    // 类型列表 
    $scope.typeList=[
        {"value":"02","text":"redmine"},
        {"value":"03","text":"jira"},
        {"value":"04","text":"it作业管理平台"},
        {"value":"05","text":"dragonFly"},
        {"value":"06","text":"其他"}
    ]
    $scope.type="03";
    // 项目列表
    $http.get("/cloudui/master/ws/resource/listAllSystems?v="+(new Date().getTime()),{
        params:{
            systemName:""
        }
    }).success(function(data){
        if(data.result){
           $scope.projectList=data.data;
           if($scope.projectList.length>0){
              $scope.project=$scope.projectList[0].id;
           }
        }else{
            Notify.alert(
                '<em class="fa fa-times"></em> '+data.message ,
                {status: 'danger'}
             ); 
        }
    })
    // 用户列表
    $http.get('/cloudui/master/ws/admin/listAllUsers'+'?v='+(new Date().getTime()),{
        params:{
           "key":"",
           "pageNum":1,
           "pageSize":1000,
           "sortName": "",
           "sortOrder":"DESC"
        }
      }).success(function(data){
          $scope.userList=data.rows;
          if($scope.userList.length>0){
              $scope.username=$scope.userList[0].userName;
          }
      })
    // 保存集成
    $rootScope.submitted = false;
    $scope.saveFn=function(){
        $rootScope.submitted = true;
        $http({
            method:'post',
            url:'/cloudui/master/ws/releaseIntegration/saveReleaseIntegration',
            data:$.param({
                "name":$scope.name,								
                "type":$scope.type,			
                "systemId":$scope.project,
                "description":$scope.descriptor||'',	
                "url":$scope.url,						
                "user":$scope.username,
                "password":$scope.password
            }),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
        }).then(function(res){
           if(res.data.result){
                Notify.alert(
                  '<em class="fa fa-check"></em> '+res.data.message ,
                  {status: 'success'}
                 ); 
                 $state.go('app.release_integration_manage',{},{reload:true});
                 ngDialog.close();
           }else{
                Notify.alert(
                  '<em class="fa fa-times"></em> '+res.data.message ,
                  {status: 'danger'}
               ); 
           }
        })
    }
    // 连接测试
    $scope.testFn=function(){  
        if(!($scope.type&&$scope.url&&$scope.username&&$scope.password)){
               Notify.alert(
                '请先输入类型、用户名、密码、url，才能进行连接测试！' ,
                {status: 'info'}
               ); 
               return false;
        }
        $http({
            method:'post',
            url:'/cloudui/master/ws/releaseIntegration/releaseIntegrationConnectionTest',
            data:$.param({
            	"type":$scope.type,
                "url":$scope.url,						
                "user":$scope.username,
                "password":$scope.password
            }),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
        }).then(function(res){
           if(res.data.result){
                Notify.alert(
                  '<em class="fa fa-check"></em> '+res.data.message ,
                  {status: 'success'}
                 ); 
           }else{
                Notify.alert(
                  '<em class="fa fa-times"></em> '+res.data.message ,
                  {status: 'danger'}
               ); 
           }
        })
    }
}])
/* ------------------------------------编辑集成------------------------------------ */
releaseIntegrationMoudle.controller('releaseIntegrationEditCtrl',['$rootScope','$scope','ngDialog','$http','Notify','$state',function($rootScope,$scope,ngDialog,$http,Notify,$state){
    // 详情 
    $scope.integrationDetail=$scope.ngDialogData.detail;
    $scope.name=$scope.integrationDetail.name;
    $scope.descriptor=$scope.integrationDetail.description;
    $scope.url=$scope.integrationDetail.url;
    $scope.password=$scope.integrationDetail.password;
    // 类型列表 
    $scope.typeList=[
        {"value":"02","text":"redmine"},
        {"value":"03","text":"jira"},
        {"value":"04","text":"it作业管理平台"},
        {"value":"05","text":"dragonFly"},
        {"value":"06","text":"其他"}
    ]
    $scope.type=$scope.integrationDetail.type;
    
    // 项目列表
    $http.get("/cloudui/master/ws/resource/listAllSystems?v="+(new Date().getTime()),{
        params:{
            systemName:""
        }
    }).success(function(data){
        if(data.result){
            $scope.projectList=data.data;
            if($scope.projectList.length>0){
               $scope.project=$scope.integrationDetail.systemId;
            }
        }else{
           Notify.alert(
            '<em class="fa fa-times"></em> '+data.message ,
            {status: 'danger'}
          ); 
        }
    })

    // 用户列表
    $http.get('/cloudui/master/ws/admin/listAllUsers'+'?v='+(new Date().getTime()),{
        params:{
           "key":"",
           "pageNum":1,
           "pageSize":1000,
           "sortName": "",
           "sortOrder":"DESC"
        }
      }).success(function(data){
          $scope.userList=data.rows;
          if($scope.userList.length>0){
              $scope.username=$scope.integrationDetail.user;
          }
      })
    // 更新集成
    $rootScope.submitted = false;
    $scope.updateFn=function(){
        $rootScope.submitted = true;
        $http({
            method:'post',
            url:'/cloudui/master/ws/releaseIntegration/updateReleaseIntegration',
            data:$.param({
                "id":$scope.integrationDetail.id,
                "name":$scope.name,								
                "type":$scope.type,			
                "systemId":$scope.project,
                "description":$scope.descriptor||'',	
                "url":$scope.url,						
                "user":$scope.username,
                "password":$scope.password
            }),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
        }).then(function(res){
           if(res.data.result){
                Notify.alert(
                  '<em class="fa fa-check"></em> '+res.data.message ,
                  {status: 'success'}
                 ); 
                 $state.go('app.release_integration_manage',{},{reload:true});
                 ngDialog.close();
           }else{
                Notify.alert(
                  '<em class="fa fa-times"></em> '+res.data.message ,
                  {status: 'danger'}
               ); 
           }
        })
    }
    // 连接测试
    $scope.testFn=function(){
        if(!($scope.type&&$scope.url&&$scope.username&&$scope.password)){
               Notify.alert(
                '请先输入类型、用户名、密码、url，才能进行连接测试！' ,
                {status: 'info'}
               ); 
               return false;
        }
        $http({
            method:'post',
            url:'/cloudui/master/ws/releaseIntegration/releaseIntegrationConnectionTest',
            data:$.param({
            	"type":$scope.type,
                "url":$scope.url,						
                "user":$scope.username,
                "password":$scope.password
            }),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
        }).then(function(res){
           if(res.data.result){
                Notify.alert(
                  '<em class="fa fa-check"></em> '+res.data.message ,
                  {status: 'success'}
                 ); 
           }else{
                Notify.alert(
                  '<em class="fa fa-times"></em> '+res.data.message ,
                  {status: 'danger'}
               ); 
           }
        })
    }
}])

/* ------------------------------------集成详情------------------------------------ */
releaseIntegrationMoudle.controller('releaseIntegrationDetailCtrl',['$scope','$http','$stateParams','Notify',function($scope,$http,$stateParams,Notify){
  
    // 类型列表 
    $scope.typeList=[
        {"value":"02","text":"redmine"},
        {"value":"03","text":"jira"},
        {"value":"04","text":"it作业管理平台"},
        {"value":"05","text":"dragonFly"},
        {"value":"06","text":"其他"}
    ]
    $http({
        method:'post',
        url:"/cloudui/master/ws/releaseIntegration/getReleaseIntegrationById",
        data:$.param(
            {
                integrationId:$stateParams.id
            }   
        ),
        headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
     }).success(function(data){
       if(data.result){
            $scope.integrationDetail=data.data;
       }else{
            Notify.alert(
              '<em class="fa fa-times"></em> '+data.message ,
              {status: 'danger'}
           ); 
       }
     })
     // 连接测试
    $scope.testFn=function(){
        $http({
            method:'post',
            url:'/cloudui/master/ws/releaseIntegration/releaseIntegrationConnectionTest',
            data:$.param({
            	"type":$scope.integrationDetail.type,
                "url":$scope.integrationDetail.url,						
                "user":$scope.integrationDetail.user,
                "password":$scope.integrationDetail.password
            }),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
        }).then(function(res){
           if(res.data.result){
                Notify.alert(
                  '<em class="fa fa-check"></em> '+res.data.message ,
                  {status: 'success'}
                 ); 
           }else{
                Notify.alert(
                  '<em class="fa fa-times"></em> '+res.data.message ,
                  {status: 'danger'}
               ); 
           }
        })
    }
}])

