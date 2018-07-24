var releaseModule=angular.module('release',[]);

// 发布任务列表
releaseModule.controller('releaseManageCtrl',['$scope','$state','ngDialog','Notify','$http','$stateParams',function($scope,$state,ngDialog,Notify,$http,$stateParams){
   $scope.pageSize=10; 
   $scope.pageNum=1;

   // 状态
   $scope.stateList=[
     {'text':'(全部)','value':''},
     {'text':'审批中','value':'01'},
     {'text':'任务执行中','value':'02'},
     {'text':'任务执行成功','value':'03'},
     {'text':'任务执行失败','value':'04'},
     {'text':'审批通过','value':'05'},
     {'text':'审批拒绝','value':'06'}
   ]
   
   $scope.status=$scope.stateList[0].value;
   
   $scope.sortOrder= 'DESC';
   $scope.sortName= '';
   $scope.fnSort = function (arg) {
            arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
            $scope.sortOrder= arguments.callee['fnSort' + arg]?'ASC':'DESC';
            $scope.sortName =arg;
            $scope.onPageChange();
        }

   // 班车列表
   $scope.busList=[];
   $scope.busListFn=function(){
       $http({
         method:'post',
           url:'/cloudui/master/ws/release/listReleaseBus',
           data:$.param({
                 "pageSize": 5000,
                 "pageNum": 1
           }),
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
       }).then(function(res){
           if(res.data.result){
               $scope.busList=res.data.page.rows;
               $scope.busList.unshift({'id':-1,'name':'(全部)'});
               if($scope.busList.length>0){
                   if($stateParams.busId){
                      
                      for(var i=0;i<$scope.busList.length;i++)
                      {
                         if($scope.busList[i].id==$stateParams.busId){
                            $scope.bus=$scope.busList[i];
                         }
                      }
                   }else{
                      $scope.bus=$scope.busList[0];
                   }
                  
               }else{
                  $scope.bus="";
               }
           }else{
               Notify.alert(
                   '<em class="fa fa-times"></em> '+res.data.message ,
                   {status: 'danger'}
                ); 
           } 
       })
   }
   $scope.busListFn();
   // 列表
   $scope.onPageChange = function ()
   {   
 
      if(!$scope.bus){
           $scope.busId=$stateParams.busId?$stateParams.busId:-1;
      }else{
            $scope.busId=$scope.bus.id;
      }
      $http({
       method:'post',
         url:'/cloudui/master/ws/release/listReleaseTask',
         data:$.param({
               "busId": $scope.busId,
               "initiator": $scope.initiator||'',
               "taskName": $scope.taskName||'',
               "system": $scope.system||'',
               "module": $scope.module||'',
               "startTime": $scope.startTime||'',
               "stopTime":$scope.stopTime||'',
               "status": $scope.status||'',
               "pageSize": $scope.pageSize,
               "pageNum": $scope.pageNum,
               "sortOrder": $scope.sortOrder,
               "sortName": $scope.sortName
         }),
         headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
     }).then(function(res){
        if(res.data.result){
            $scope.listoff=res.data.page.rows.length>0?true:false;
          $scope.warninginfo='暂无数据';
          $scope.taskList = res.data.page.rows;
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

   // 查询
   $scope.querylog=function(){
       var daterange=$('.daterange').val();
       $scope.startTime=daterange.split(' - ')[0];
       $scope.stopTime=daterange.split(' - ')[1];
        $scope.pageNum=1;
       $scope.onPageChange();
   }
   // 删除任务
   $scope.delTaskFn=function(index,id){
        ngDialog.openConfirm({
          template:
             '<p class="modal-header">您确定要删除此任务吗?</p>' +
             '<div class="modal-body text-right">' +
               '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
               '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
             '</button></div>',
          plain: true,
          className: 'ngdialog-theme-default'
      }).then(function(){
             $http({
           method:'post',
             url:'/cloudui/master/ws/release/deleteReleaseTask',
             data:$.param({
                     taskId:id
             }),
             headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
         }).then(function(res){
                if(res.data.result){
                     Notify.alert(
                       '<em class="fa fa-check"></em> '+res.data.message ,
                       {status: 'success'}
                   ); 
                   $state.go('app.release_manage',{},{reload:true});
                }else{
                     Notify.alert(
                       '<em class="fa fa-times"></em> '+res.data.message ,
                       {status: 'danger'}
                    ); 
                }
         })
      })
   }
}])

// 发布任务添加
releaseModule.controller('releaseAddCtrl',['$rootScope','$scope','ngDialog','$http','Notify','$state',function($rootScope,$scope,ngDialog,$http,Notify,$state){
   $scope.formData={};
   // 添加审批人
   $scope.approverAddFn=function(){
        ngDialog.open({
          template: 'app/views/dialog/approver-add.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          scope: $scope,
          closeByDocument:false,
          cache:false,
          controller:'approverAddCtrl'
        });
   }
   $scope.approverList=[]; // 审批人
   $scope.$on('call', function(event,data){
      $scope.approverList.push(data);
   });

   // 删除审批人
   $scope.delApproverFn=function(index){
      $scope.approverList.splice(index,1);
   }

   // 班车列表
   $scope.busList=[];
   $http({
         method:'post',
           url:'/cloudui/master/ws/release/listReleaseBus',
           data:$.param({
                 "pageSize": 5000,
                 "pageNum": 1
           }),
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
       }).then(function(res){
           if(res.data.result){
               $scope.busList=res.data.page.rows;
               if($scope.busList.length>0){
                  $scope.bus=$scope.busList[0];
               }else{
                  $scope.bus="";
                  $scope.relytask="";
               }
           } 
       })

       // 依赖任务
       $scope.$watch('bus.id',function(newval,oldval){
            if(newval){
                 $http({
                   method:'post',
                     url:'/cloudui/master/ws/release/listReleaseTaskByBusId',
                     data:$.param({
                           "busId":newval,
                           "pageSize": 5000,
                           "pageNum": 1
                     }),
                     headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
                 }).then(function(res){
                     if(res.data.result){
                         $scope.relyTaskList=res.data.page.rows;
                         $scope.relyTaskList.unshift({'id':'','name':'(无)'});
                         if($scope.relyTaskList.length>0){
                            $scope.relytask=$scope.relyTaskList[0];
                         }else{
                            $scope.relytask="";
                         }
                     } 
                 })
            }
       })

   // 蓝图列表
   $scope.blueprintList=[];
   $http.get('/cloudui/master/ws/blueprintTemplate/listBlueprintTemplateByNameAndApp'+'?v=' + (new Date().getTime()),{
      params:
          {
            pageNum:1,
            pageSize:5000,
            templateName:"",
            appName:"",
            sortOrder:'DESC',
            sortName:'NAME'
          }
    }).success(function(data){
         $scope.blueprintList = data.rows; 
         $scope.blueprint=$scope.blueprintList[0];
    })

   // 蓝图实例列表
   $scope.blueprintInstancesList=[];
   $scope.$watch('blueprint',function(newval,oldval){
      if(newval&&newval.NAME){
           $http.get('/cloudui/master/ws/blueprint/listBlueprintByNameAndTemplateAndApp'+'?v=' + (new Date().getTime()),{
          params:
              {
                pageNum:1,
                pageSize:5000,
                blueprintName:"",
                templateName:newval.NAME,
                appName:"",
                sortOrder:'DESC',
                sortName:'INSTANCE_NAME'
              }
        }).success(function(data){
                $scope.blueprintInstancesList=data.rows;
                if(data.rows.length>0){
                    $scope.blueprintIns=$scope.blueprintInstancesList[0];
                }else{
                  $scope.blueprintIns="";
                  $scope.flowList=[];
                  $scope.flow="";
                }
                
        })
      }
   })
    
    // 流程列表
    $scope.flowList=[];
    $scope.$watch('blueprintIns',function(newval,oldval){
      if(newval&&newval.INSTANCE_ID){
        // 获取蓝图实例下流程列表
          $http.get('/cloudui/master/ws/dashboard/getFlows'+'?v=' + (new Date().getTime()),{
              params:
              {
                 blueInstanceId:newval.INSTANCE_ID,
              }
          }).success(function(data){
            $scope.flowList = data;
            if(data.length>0){
                  $scope.flow=$scope.flowList[0]; 
            }else{
                  $scope.flow=""; 
            }   
          })
      }
    },true)
    

   // 添加发布任务
   $rootScope.submitted = false;
   $scope.addTaskFn=function(){

       $rootScope.submitted = true;
       
       var daterange=$('.daterange').val();
       $scope.startTime=daterange.split(' - ')[0];
       $scope.stopTime=daterange.split(' - ')[1];

       if($scope.approverList.length<=0){
            Notify.alert(
                  '请添加审批人！',
                  {status: 'info'}
              ); 
              return false;
       }else if(!($scope.startTime&&$scope.stopTime)){
              Notify.alert(
                  '请输入任务时间范围，包含开始和结束时间！',
                  {status: 'info'}
              ); 
             return false;
       }

       $rootScope.app.layout.isShadow=true;
       
       $http({
         method:'post',
         url:'/cloudui/master/ws/release/saveReleaseTask',
         data:$.param({
               "busId":$scope.bus.id,
               "taskName": $scope.taskName,
               "system": $scope.sysName||'',
               "module": $scope.moduleName||'',
               "description": $scope.des||'',
               "startTime": $scope.startTime,
               "stopTime":$scope.stopTime,
               "blueprintTemplate": $scope.blueprint.ID,
               "blueprintInstance": $scope.blueprintIns.INSTANCE_ID,
               "blueprintFlow": $scope.flow.ID,
               "dependId":$scope.relytask.id||-1,
               "autoExecute":$scope.auto||false,
               "cronExepression":$scope.formData.cron,
               "approvals":angular.toJson($scope.approverList)
         }),
         headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
     }).then(function(res){
         $rootScope.app.layout.isShadow=false;
         if(res.data.result){
              Notify.alert(
                  '<em class="fa fa-check"></em> '+res.data.message ,
                  {status: 'success'}
              ); 
              $state.go('app.release_manage',{},{reload:true});
         }else{
              Notify.alert(
                  '<em class="fa fa-times"></em> '+res.data.message ,
                  {status: 'danger'}
              ); 
         }
     })
   }

   
}])   

// 发布任务编辑
releaseModule.controller('releaseEditCtrl',['$rootScope','$scope','ngDialog','$http','$stateParams','Notify','$filter','$state','$timeout',function($rootScope,$scope,ngDialog,$http,$stateParams,Notify,$filter,$state,$timeout){
   $scope.formData={};
   // 添加审批人
   $scope.approverAddFn=function(){
        ngDialog.open({
          template: 'app/views/dialog/approver-add.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          scope: $scope,
          closeByDocument:false,
          cache:false,
          controller:'approverAddCtrl'
        });
   }
   $scope.$on('call', function(event,data){
      $scope.approverList.push(data);
   });
  
  // 删除审批人
   $scope.delApproverFn=function(index){
      $scope.approverList.splice(index,1);
   }

   // 班车列表
   $scope.busList=[];
   $http({
         method:'post',
           url:'/cloudui/master/ws/release/listReleaseBus',
           data:$.param({
                 "pageSize": 5000,
                 "pageNum": 1
           }),
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
       }).then(function(res){
           if(res.data.result){
               $scope.busList=res.data.page.rows;
               
               /*if($scope.busList.length>0){
                  $scope.bus=$scope.busList[0];
               }else{
                  $scope.bus="";
                  $scope.relytask="";
               }*/
           } 
       })

      /* $scope.$watch('busList',function(newval,oldval){
          console.log(newval)
          console.log($scope.taskInfo)
          console.log(newval,$scope.taskInfo.busId)
         if(newval.length>0&&$scope.taskInfo.busId){
               
              $scope.bus=$filter('filter')(newval,$scope.taskInfo.busName)[0];
         }
    },true)*/

       // 依赖任务
       $scope.$watch('bus.id',function(newval,oldval){
            if(newval){
                 $http({
                   method:'post',
                     url:'/cloudui/master/ws/release/listReleaseTaskByBusId',
                     data:$.param({
                           "busId":newval,
                           "pageSize": 5000,
                           "pageNum": 1
                     }),
                     headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
                 }).then(function(res){
                     if(res.data.result){
                         $scope.relyTaskList=res.data.page.rows;

                         for(var i=0;i<$scope.relyTaskList.length;i++)
                         {
                           if($scope.relyTaskList[i].id==$scope.taskInfo.id){
                              $scope.relyTaskList.splice(i,1);
                           }
                         }
                     
                         $scope.relyTaskList.unshift({'id':'','name':'(无)'});

                     
                         if($scope.relyTaskList.length>0){
                    
                              if($scope.taskInfo.dependId){
                                   $scope.relytask=$filter('filter')($scope.relyTaskList,$scope.taskInfo.dependId)[0];
                              
                              }else{
                                     $scope.relytask=$scope.relyTaskList[0];
                              }
                          }else{
                              $scope.relytask="";
                          }
                     }

                 })
            }
       })

   // 蓝图列表
   $scope.blueprintList=[];
   $http.get('/cloudui/master/ws/blueprintTemplate/listBlueprintTemplateByNameAndApp'+'?v=' + (new Date().getTime()),{
      params:
          {
            pageNum:1,
            pageSize:5000,
            templateName:"",
            appName:"",
            sortOrder:'DESC',
            sortName:'NAME'
          }
    }).success(function(data){
         $scope.blueprintList = data.rows; 
    })

    $scope.$watch('blueprintList',function(newval,oldval){
         if(newval.length>0&&$scope.taskInfo.blueprintTemplate){
              $scope.blueprint=$filter('filter')(newval,$scope.taskInfo.blueprintTemplate)[0];
         }
    },true)

    // 蓝图实例列表
   $scope.blueprintInstancesList=[];
   $scope.$watch('blueprint',function(newval,oldval){
      if(newval&&newval.NAME){
           $http.get('/cloudui/master/ws/blueprint/listBlueprintByNameAndTemplateAndApp'+'?v=' + (new Date().getTime()),{
          params:
              {
                pageNum:1,
                pageSize:5000,
                blueprintName:"",
                templateName:newval.NAME,
                appName:"",
                sortOrder:'DESC',
                sortName:'INSTANCE_NAME'
              }
        }).success(function(data){
                $scope.blueprintInstancesList=data.rows;
                if(data.rows.length>0){
                    if($scope.taskInfo.blueprintInstance){
                       $scope.blueprintIns=$filter('filter')($scope.blueprintInstancesList,$scope.taskInfo.blueprintInstance)[0];
                  }else{
                         $scope.blueprintIns=$scope.blueprintInstancesList[0];
                  }
                }else{
                  $scope.blueprintIns="";
                  $scope.flowList=[];
                  $scope.flow="";
                }    
        })
      }
   })

   // 流程列表
    $scope.flowList=[];
    $scope.$watch('blueprintIns',function(newval,oldval){
      if(newval&&newval.INSTANCE_ID){
        // 获取蓝图实例下流程列表
          $http.get('/cloudui/master/ws/dashboard/getFlows'+'?v=' + (new Date().getTime()),{
              params:
              {
                 blueInstanceId:newval.INSTANCE_ID,
              }
          }).success(function(data){
            $scope.flowList = data;
            if(data.length>0){
              if($scope.taskInfo.blueprintFlow){
                        $scope.flow=$filter('filter')($scope.flowList,$scope.taskInfo.blueprintFlow)[0];
              }else{
                $scope.flow=$scope.flowList[0];
              }   
            }else{
                  $scope.flow=""; 
            }   
          })
      }
    },true)

   // 任务详情信息
   $timeout(function(){
      $http({
       method:'post',
         url:'/cloudui/master/ws/release/getReleaseTaskById',
         data:$.param({
               "id": $stateParams.id
         }),
         headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
    }).then(function(res){
      if(res.data.result){
            $scope.taskInfo=res.data.data;
            $scope.approverList=$scope.taskInfo.approvals||[];
            $scope.taskName=$scope.taskInfo.name;
            $scope.sysName=$scope.taskInfo.system;
            $scope.moduleName=$scope.taskInfo.module;
            $scope.des=$scope.taskInfo.description;
            $scope.blueprint=$filter('filter')($scope.blueprintList,$scope.taskInfo.blueprintTemplate)[0];
            $scope.blueprintIns=$filter('filter')($scope.blueprintInstancesList,$scope.taskInfo.blueprintInstance)[0];
            $scope.flow=$filter('filter')($scope.flowList,$scope.taskInfo.blueprintFlow)[0];
            $scope.bus=$filter('filter')($scope.busList,$scope.taskInfo.busName)[0];
            $scope.startTime=$scope.taskInfo.startTime;
            $scope.endTime=$scope.taskInfo.stopTime;
            $scope.taskTime=$scope.startTime+' - '+$scope.endTime;
            $scope.formData.cron=$scope.taskInfo.cronExepression;
            $scope.auto=$scope.taskInfo.autoExecute;   
      }else{
             Notify.alert(
                  '<em class="fa fa-times"></em> '+res.data.message ,
                  {status: 'danger'}
              ); 
      }
    })
   })
   
    // 更新任务
     $rootScope.submitted = false;
    $scope.updateTaskFn=function(){
       $rootScope.submitted = true;
       
       var daterange=$('.daterange').val();
       $scope.startTime=daterange.split(' - ')[0];
       $scope.stopTime=daterange.split(' - ')[1];

       if($scope.approverList.length<=0){
            Notify.alert(
                  '请添加审批人！',
                  {status: 'info'}
              ); 
              return false;
       }else if(!($scope.startTime&&$scope.stopTime)){
              Notify.alert(
                  '请输入任务时间范围，包含开始和结束时间！',
                  {status: 'info'}
              ); 
             return false;
       }

       $rootScope.app.layout.isShadow=true;

        $http({
         method:'post',
           url:'/cloudui/master/ws/release/updateReleaseTask',
           data:$.param({
                 "taskId":$scope.taskInfo.id,
                 "busId":$scope.bus.id,
                 "taskName": $scope.taskName,
                 "system": $scope.sysName||'',
                 "module": $scope.moduleName||'',
                 "description": $scope.des||'',
                 "startTime": $scope.startTime,
                 "stopTime":$scope.stopTime,
                 "blueprintTemplate": $scope.blueprint.ID,
                 "blueprintInstance": $scope.blueprintIns.INSTANCE_ID,
                 "blueprintFlow": $scope.flow.ID,
                 "dependId":$scope.relytask.id||-1,
                 "autoExecute":$scope.auto,
                 "cronExepression":$scope.formData.cron,
                 "approvals":angular.toJson($scope.approverList)
           }),
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
       }).then(function(res){
        $rootScope.app.layout.isShadow=false;
           if(res.data.result){
                Notify.alert(
                    '<em class="fa fa-check"></em> '+res.data.message ,
                    {status: 'success'}
                ); 
                $state.go('app.release_manage',{},{reload:true});
           }else{
                Notify.alert(
                    '<em class="fa fa-times"></em> '+res.data.message ,
                    {status: 'danger'}
                ); 
           }
      })
    }

}]) 

// 发布任务详情
releaseModule.controller('releaseDetailCtrl',['$scope','$http','$stateParams','$state','Notify','$interval','ngDialog','$filter',function($scope,$http,$stateParams,$state,Notify,$interval,ngDialog,$filter){
    $scope.$on('$destroy', function() {
       $interval.cancel($scope.historyTimer);  
    });
   // 状态
   $scope.stateList=[
     {'text':'初始状态','value':'00'},
     {'text':'待审批','value':'01'},
     {'text':'审批通过','value':'02'},
     {'text':'审批拒绝','value':'03'}
   ]

   $scope.doData={};
   $scope.pageSize=10;
   $scope.pageNum=1;

   // 任务详情信息
   $http({
       method:'post',
         url:'/cloudui/master/ws/release/getReleaseTaskById',
         data:$.param({
               "id": $stateParams.id
         }),
         headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
    }).then(function(res){
      if(res.data.result){
            $scope.taskInfo=res.data.data;
            $scope.approverList=$scope.taskInfo.approvals||[];
            $scope.isAction=(!$scope.taskInfo.autoExecute)&&($scope.taskInfo.status=="05")
            if($scope.taskInfo.blueprintInstance){
                  // 获取蓝图实例流程的操作类型
                  $scope.flowList=[];
                  $http.get('/cloudui/master/ws/blueprint/getFlowsByInstance',{
                     params:{
                       blueprintInstanceId:$scope.taskInfo.blueprintInstance
                     }
                  }).success(function(data){
                     angular.forEach(data,function(val,key){
                       val.off=false;
                     })
                     for(var i=0;i<data.length;i++)
                     {
                       if(data[i].FLOW_NAME==$scope.taskInfo.blueprintFlowName){
                          $scope.flowList.push(data[i])
                       }
                     }
                     $scope.flowList[0].off=true;
                     $scope.flowList[0].active=true;
                  })
            }
      }else{
             Notify.alert(
                  '<em class="fa fa-times"></em> '+res.data.message ,
                  {status: 'danger'}
              ); 
      }
    })

    $scope.taskInfoFn=function(){
        $http({
           method:'post',
             url:'/cloudui/master/ws/release/getReleaseTaskById',
             data:$.param({
                   "id": $stateParams.id
             }),
             headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
        }).then(function(res){
              if(res.data.result){
                $scope.taskInfo=res.data.data;
              }
          })
    }

    $scope.$watch('taskInfo.autoExecute',function(newval,oldval){
        if(newval){ // 自动执行
              $scope.taskTimer=$interval(function(){
                   $scope.taskInfoFn();
              },3000)
        }
    })

    // 流程实例id
    $scope.$watch('taskInfo.blueprintFlowInstance',function(newval,oldval){
         if(newval){
               
             if(!$scope.taskInfo.autoExecute){ // 手动执行
                 $interval.cancel($scope.taskTimer);
             } 

             $scope.historyTimer=$interval(function(){
                if($scope.curPageNum&&$scope.curFlowName&&$scope.curFlowId){
                  $scope.onPageChange($scope.curPageNum,$scope.curFlowName,$scope.curFlowId);
                }
            },3000)
         }
    })
    
     


   // 查看对应的流程操作类型操作历史记录
    $scope.doHistoryListFn=function(index,flowName){
      for(var i=0;i<$scope.flowList.length;i++)
      {
        $scope.flowList[i].off=false;
        $scope.flowList[i].active=false;
      }
        $scope.flowList[index].off=true;
        $scope.flowList[index].active=true;
    }
    
  

    // 流程实例列表
    $scope.onPageChange = function (pageNum,flowName,flowId)
    {  
        $scope.curPageNum=pageNum;
        $scope.curFlowName=flowName;
        $scope.curFlowId=flowId;
        $http.get('/cloudui/master/ws/monitor/getFlowInstanceIds'+'?v='+(new Date().getTime()),{
             params:{
                  bluePrintInsId:$scope.taskInfo.blueprintInstance,
                  flowId:flowId,
                  pageSize:$scope.pageSize, 
                  pageNum:pageNum,
                  sortOrder:'DESC',
                  sortName:'',
                  instanceId:$scope.taskInfo.blueprintFlowInstance,
                  flag:'task'
             }
         }).
         success(function(data){
              $scope.doData[flowName]=data.rows;
              $scope.pageCount=Math.ceil(data.total/$scope.pageSize);
              if($scope.pageCount==0){
                $scope.pageCount=1;
              }
             
         })
      }

    // 结束
    $scope.overFn=function(id){
      $http.get('/cloudui/master/ws/monitor/endInstance'+'?v='+(new Date().getTime()),
        {
          params:{
            id:id
          }
        }).success(function(data){
          if(data.result){
               Notify.alert(
                   '<em class="fa fa-check"></em> '+data.message,
                   {status: 'success'}
                );
          }else{
                Notify.alert(
                   '<em class="fa fa-times"></em> '+data.message,
                   {status: 'danger'}
                );
          }
      })
    }

    // 详情
    $scope.detailFn=function(detail){
         ngDialog.open({
           template: 'app/views/dialog/component-detail.html'+'?action='+(new Date().getTime()),
           className: 'ngdialog-theme-default ngdialog-lg',
           scope: $scope,
           cache:false,
           closeByDocument:false,
           data:{detail:detail},
           controller:['$scope',function($scope){
             $scope.detail=$scope.ngDialogData.detail;
           }]
         });
    }

    // 手动执行任务
    $scope.actionTaskFn=function(){
        $http({
           method:'post',
             url:'/cloudui/master/ws/release/executeTaskMannual',
             data:$.param({
                   "taskId": $stateParams.id
             }),
             headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
        }).then(function(res){
             if(res.data.result){
                   Notify.alert(
                       '<em class="fa fa-check"></em> '+res.data.message,
                       {status: 'success'}
                    );
                    $scope.taskTimer=$interval(function(){
                         $scope.taskInfoFn();
                    },3000)
              }else{
                    Notify.alert(
                       '<em class="fa fa-times"></em> '+res.data.message,
                       {status: 'danger'}
                    );
              }
        })
    }
}])

// 添加审批人
releaseModule.controller('approverAddCtrl',['$scope','$http',function($scope,$http){
   // 用户列表   
   $http.get('/cloudui/master/ws/admin/listAllUsers',{params:{
       "key":"",
     "pageNum":1,
     "pageSize":1000,
     "sortName": "",
     "sortOrder":"DESC"
   }}).success(function(data){
       $scope.userList=data.rows;
       $scope.approver=$scope.userList[0].userName;
   })
   // 保存
   $scope.saveFn=function(closeThisDialog){
      $scope.$emit('call', {approver:$scope.approver});
      closeThisDialog(0);
   }
}])

// 发布班车列表
releaseModule.controller('busManageCtrl',['$scope','$state','ngDialog','Notify','$http',function($scope,$state,ngDialog,Notify,$http){
   
   $scope.sortOrder= 'DESC';
   $scope.sortName= 'startTime';
   $scope.fnSort = function (arg) {
            arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
            $scope.sortOrder= arguments.callee['fnSort' + arg]?'ASC':'DESC';
            $scope.sortName =arg;
            $scope.onPageChange();
        }
  // 班车列表
   $scope.pageSize=10;
   $scope.pageNum=1;
   $scope.onPageChange = function ()
   {   
      $http({
       method:'post',
         url:'/cloudui/master/ws/release/listReleaseBus',
         data:$.param({
               "pageSize": $scope.pageSize,
               "pageNum": $scope.pageNum,
               "sortOrder":$scope.sortOrder,
             "sortName":$scope.sortName 
         }),
         headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
     }).then(function(res){
        if(res.data.result){
            $scope.listoff=res.data.page.rows.length>0?true:false;
            $scope.warninginfo='暂无数据';
            $scope.busList = res.data.page.rows;
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

   // 删除班车
   $scope.delBusFn=function(index,id){
        ngDialog.openConfirm({
          template:
             '<p class="modal-header">您确定要删除此班车吗?</p>' +
             '<div class="modal-body text-right">' +
               '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
               '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
             '</button></div>',
          plain: true,
          className: 'ngdialog-theme-default'
      }).then(function(){
             $http({
           method:'post',
             url:'/cloudui/master/ws/release/deleteReleaseBusById',
             data:$.param({
                     busId:id
             }),
             headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
         }).then(function(res){
                if(res.data.result){
                     Notify.alert(
                       '<em class="fa fa-check"></em> '+res.data.message ,
                       {status: 'success'}
                   ); 
                   $state.go('app.bus_manage',{},{reload:true});
                }else{
                     Notify.alert(
                       '<em class="fa fa-times"></em> '+res.data.message ,
                       {status: 'danger'}
                    ); 
                }
         })
      })
   }

}])
 
// 添加班车 
releaseModule.controller('busAddCtrl',['$rootScope','$scope','$state','ngDialog','Notify','$http',function($rootScope,$scope,$state,ngDialog,Notify,$http){
    $scope.busTime=$('.daterange').val();
    $scope.$watch('busTime',function(newval,oldval){
         console.log(newval);
    })
    // 添加班车
   $rootScope.submitted = false;
   $scope.addBusFn=function(){
       $rootScope.submitted = true;
       var daterange=$('.daterange').val();
       $scope.startTime=daterange.split(' - ')[0];
       $scope.stopTime=daterange.split(' - ')[1];
       if(!$scope.form.$valid){
             Notify.alert(
                  '请输入班车名称！',
                  {status: 'info'}
              ); 
              return false;
       }else if(!($scope.startTime&&$scope.stopTime)){
       
              Notify.alert(
                  '请输入时间范围，包含开始和结束时间！',
                  {status: 'info'}
              ); 
              return false;
       }

        $rootScope.app.layout.isShadow=true;
          
       $http({
         method:'post',
         url:'/cloudui/master/ws/release/saveReleaseBus',
         data:$.param({
               "name": $scope.busName,               
               "startTime": $scope.startTime,
               "stopTime": $scope.stopTime
         }),
         headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
     }).then(function(res){
         $rootScope.app.layout.isShadow=false;
         if(res.data.result){
              Notify.alert(
                  '<em class="fa fa-check"></em> '+res.data.message ,
                  {status: 'success'}
              ); 
              $state.go('app.bus_manage',{},{reload:true});
         }else{
              Notify.alert(
                  '<em class="fa fa-times"></em> '+res.data.message ,
                  {status: 'danger'}
              ); 
         }
     })
   }
 
}])
 
// 修改班车
releaseModule.controller('busEditCtrl',['$rootScope','$scope','$state','ngDialog','Notify','$http','$stateParams',function($rootScope,$scope,$state,ngDialog,Notify,$http,$stateParams){
   // 班车详情信息
   $http({
       method:'post',
         url:'/cloudui/master/ws/release/getReleaseBusById',
         data:$.param({
               "busId": $stateParams.id
         }),
         headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
    }).then(function(res){
      if(res.data.result){
            $scope.busInfo=res.data.data;
            $scope.busName=$scope.busInfo.name;
            $scope.busTime=$scope.busInfo.startTime+' - '+$scope.busInfo.stopTime;
            $scope.startTime=$scope.busInfo.startTime;
            $scope.stopTime=$scope.busInfo.stopTime;
      }else{
             Notify.alert(
                  '<em class="fa fa-times"></em> '+res.data.message ,
                  {status: 'danger'}
              ); 
      }
    })

    // 更新班车
     $rootScope.submitted = false;
    $scope.updateBusFn=function(){
       $rootScope.submitted = true;
       
       var daterange=$('.daterange').val();
       $scope.startTime=daterange.split(' - ')[0];
       $scope.stopTime=daterange.split(' - ')[1];
       if(!$scope.form.$valid){
             Notify.alert(
                  '请输入班车名称！',
                  {status: 'info'}
              ); 
              return false;
       }else if(!($scope.startTime&&$scope.stopTime)){
       
              Notify.alert(
                  '请输入时间范围，包含开始和结束时间！',
                  {status: 'info'}
              ); 
              return false;
       }

       $rootScope.app.layout.isShadow=true;

        $http({
         method:'post',
           url:'/cloudui/master/ws/release/updateReleaseBus',
           data:$.param({
                 "id":$stateParams.id,
                 "name": $scope.busName,               
                 "startTime": $scope.startTime,
                 "stopTime": $scope.stopTime
           }),
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
       }).then(function(res){
        $rootScope.app.layout.isShadow=false;
           if(res.data.result){
                Notify.alert(
                    '<em class="fa fa-check"></em> '+res.data.message ,
                    {status: 'success'}
                ); 
                $state.go('app.bus_manage',{},{reload:true});
           }else{
                Notify.alert(
                    '<em class="fa fa-times"></em> '+res.data.message ,
                    {status: 'danger'}
                ); 
           }
      })
    }

}])
 
 // 班车甘特图
releaseModule.controller('busGanttCtrl',['$scope','$state','ngDialog','Notify','$http','$stateParams',function($scope,$state,ngDialog,Notify,$http,$stateParams){
   $scope.busId=$stateParams.id;
}])

// 验证cron表达式
releaseModule.directive('validcron', function($http) {
    return {
        require : 'ngModel',
        link : function(scope, elm, attrs, ctrl) {
            elm.bind('keyup', function() {
                $http({method: 'GET', url: '/cloudui/master/ws/quartz/checkCronExpression'+'?v='+(new Date().getTime())+'&cronExpression='+scope.formData.cron||''}).
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
 