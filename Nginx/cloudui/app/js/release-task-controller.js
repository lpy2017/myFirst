var releaseTaskModule=angular.module('release-task',[]);

// 发布任务列表
releaseTaskModule.controller('releaseTaskManageCtrl',['$rootScope','$scope','$state','ngDialog','Notify','$http','$stateParams','$cookieStore','$interval',function($rootScope,$scope,$state,ngDialog,Notify,$http,$stateParams,$cookieStore,$interval){
   $scope.pageSize=10; 
   $scope.pageNum=1;
  
   $scope.$on('$destroy', function() {
      $interval.cancel($scope.releaseTimer);  
   });

   var queryConfigCookie=$cookieStore.get('queryConfig');

   $scope.taskId=(queryConfigCookie&&queryConfigCookie.taskId)?queryConfigCookie.taskId:"";
   $scope.taskName=(queryConfigCookie&&queryConfigCookie.taskName)?queryConfigCookie.taskName:"";
   $scope.taskLabel=(queryConfigCookie&&queryConfigCookie.taskLabel)?queryConfigCookie.taskLabel:"";
   $scope.taskTime=(queryConfigCookie&&queryConfigCookie.timeVal)?queryConfigCookie.timeVal:"";
  
   // 类型
   $scope.typeList=[
     {'text':'(全部)','value':'00'},
     {'text':'需求','value':'01'},
     {'text':'缺陷','value':'02'},
     {'text':'优化','value':'03'},
     {'text':'任务','value':'04'},
     {'text':'其他','value':'05'}
   ]
   $scope.taskType=(queryConfigCookie&&queryConfigCookie.taskType)?queryConfigCookie.taskType:$scope.typeList[0].value;
   // 优先级
   $scope.levelList=[
     {'text':'(全部)','value':'00'},
     {'text':'紧急','value':'01'},
     {'text':'非常高','value':'02'},
     {'text':'高','value':'03'},
     {'text':'中','value':'04'},
     {'text':'低','value':'05'}
   ]
   $scope.taskLevel=(queryConfigCookie&&queryConfigCookie.taskLevel)?queryConfigCookie.taskLevel:$scope.levelList[0].value;
   // 状态
   $scope.statusList= $rootScope.statusList.concat([]);
   $scope.statusList.unshift({'text':'(全部)','value':'00'});
   $scope.taskStatus=(queryConfigCookie&&queryConfigCookie.taskStatus)?queryConfigCookie.taskStatus:$scope.statusList[0].value;

   // 状态颜色
   $scope.getStatusColorFn=function(n){
    switch(n)
    {
      case "01":
      return "bg-success-light";
      break;
      case "02":
      return "bg-yellow-light";
      break;
      case "03":
      return "bg-yellow";
      break;
      case "04":
      return "bg-success-dark";
      break;
      case "05":
      return "bg-green-dark";
      break;
      case "06":
      return "bg-yellow-dark";
      break;
      case "07":
      return "bg-success";
      break;
      case "08":
      return "bg-danger-dark";
      break;
      default:
      return "bg-green";
    }
   }

   // 任务来源
   $scope.sourceList=[
     {'text':'(全部)','value':'00'},
     {'text':'发布管理平台','value':'01'},
     {'text':'redmine','value':'02'},
     {'text':'jira','value':'03'},
     {'text':'it作业管理平台','value':'04'},
     {'text':'dragonFly','value':'05'},
     {'text':'其他','value':'06'}
   ]
   $scope.source=(queryConfigCookie&&queryConfigCookie.source)?queryConfigCookie.source:$scope.sourceList[0].value;

   // 系统列表
   $http.get('/cloudui/master/ws/resource/listAllSystems'+'?v='+(new Date().getTime()),{
      params:{
        systemName:''
      }
    }).
    success(function(res){
        if(res.result){
           $scope.systemList=res.data;
           $scope.systemList.unshift({
              id:'',
              resourceName:'(全部)'
           });
           $scope.taskSystem=(queryConfigCookie&&queryConfigCookie.taskSystem)?queryConfigCookie.taskSystem:$scope.systemList[0].id;
        }else{
           Notify.alert(
             '<em class="fa fa-times"></em> '+res.message,
              {status: 'danger'}
           );
        }
    })

    $scope.parentTask=-1;
    
    $scope.taskDepended=-1;

    $scope.$watch('taskSystem',function(newval,oldval){
         // 父任务列表
         $http({
             method:'post',
             url:'/cloudui/master/ws/releaseTask/listReleaseParentTaskOrders',
             data:$.param({
                 systemId:newval
             }),
             headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
         }).then(function(res){
             if(res.data.result){
                  $scope.parentList=res.data.data;
                  $scope.parentList.unshift({
                    id:-1,
                    name:'(全部)'
                  });
                
                  $scope.parentTask=(queryConfigCookie&&queryConfigCookie.taskParentId)?queryConfigCookie.taskParentId:$scope.parentList[0].id;
            
             }else{
               Notify.alert(
                   '<em class="fa fa-times"></em> '+res.data.message ,
                   {status: 'danger'}
               );
             }
         })
         // 依赖任务列表
         $http({
             method:'post',
             url:'/cloudui/master/ws/releaseTask/listReleaseDependTaskOrders',
             data:$.param({
                 systemId:newval
             }),
             headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
         }).then(function(res){
             if(res.data.result){
                  $scope.dependedList=res.data.data;
                  $scope.dependedList.unshift({
                    id:-1,
                    name:'(全部)'
                  });
                  $scope.taskDepended=(queryConfigCookie&&queryConfigCookie.taskDependId)?queryConfigCookie.taskDependId:$scope.dependedList[0].id;
             }else{
               Notify.alert(
                   '<em class="fa fa-times"></em> '+res.data.message ,
                   {status: 'danger'}
               );
             }
         })
    })

    // 创建人、负责人列表
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
        $scope.userList.unshift({
          ID:'',
          userName:'(全部)'
        });
        $scope.taskCreater=(queryConfigCookie&&queryConfigCookie.taskCreater)?queryConfigCookie.taskCreater:$scope.userList[0].ID;
        $scope.taskPrincipal=(queryConfigCookie&&queryConfigCookie.taskPrincipal)?queryConfigCookie.taskPrincipal:$scope.userList[0].ID;
    })
   
   
   $scope.sortOrder= 'DESC';
   $scope.sortName= '';
   $scope.fnSort = function (arg) {
            arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
            $scope.sortOrder= arguments.callee['fnSort' + arg]?'ASC':'DESC';
            $scope.sortName =arg;
            $scope.onPageChange();
  }
  $scope.pageSize=10;

  $scope.$watch('pageNum',function(newval,oldval){
     $scope.pageNum=newval;
  })

  $scope.setPageNumCookie=function() {
    $cookieStore.put('taskPageNum',$scope.pageNum);
  }

  var taskPageNumCookie=$cookieStore.get('taskPageNum');
  
  if(taskPageNumCookie){
      $http({
       method:'post',
         url:'/cloudui/master/ws/releaseTask/listReleaseTaskOrders',
         data:$.param({
               "pageSize": $scope.pageSize,
               "pageNum":1,
               "taskId":-1,
               "taskName":"",
               "taskType":"00",
               "taskLevel":"00",
               "taskStatus":"00",
               "taskLabel":"",
               "taskParentId":-1,
               "taskDependId":-1,
               "taskSystem":"",
               "taskCreater":"",
               "taskPrincipal":"",
               "taskCreateStartTime":"",
               "taskCreateEndTime":"",
               "source":"00"
         }),
         headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
     }).then(function(res){
          if(res.data.result){
             $scope.totalPageNum=res.data.page.totalPageNum;
             if(taskPageNumCookie<=$scope.totalPageNum){
                $scope.pageNum=taskPageNumCookie;
             }else{
                $scope.pageNum=$scope.totalPageNum;
             }
          }
     })
  }else{
      $scope.pageNum=1;
  }

   // 列表
   $scope.onPageChange = function ()
   {   
       queryConfigCookie=$cookieStore.get('queryConfig');
       if(queryConfigCookie){
           $scope.startTime=queryConfigCookie.timeVal.split(' - ')[0];
           $scope.stopTime=queryConfigCookie.timeVal.split(' - ')[1];
           var queryData={
               "taskId": queryConfigCookie.taskId||-1,
               "taskName": queryConfigCookie.taskName||'',
               "taskType": queryConfigCookie.taskType,
               "taskLevel": queryConfigCookie.taskLevel,
               "taskStatus":queryConfigCookie.taskStatus,
               "taskLabel": queryConfigCookie.taskLabel||'',
               "taskParentId":queryConfigCookie.taskParentId,
               "taskDependId":queryConfigCookie.taskDependId,
               "taskSystem": queryConfigCookie.taskSystem,
               "taskCreater": queryConfigCookie.taskCreater,
               "taskPrincipal": queryConfigCookie.taskPrincipal,
               "taskCreateStartTime": $scope.startTime,
               "taskCreateEndTime": $scope.stopTime,
               "source":queryConfigCookie.source,
               "pageSize": $scope.pageSize,
               "pageNum": $scope.pageNum,
               "sortOrder": $scope.sortOrder,
               "sortName": $scope.sortName
           }
       }else{
          var daterange=$('.daterange').val();
          $scope.startTime=daterange.split(' - ')[0];
          $scope.stopTime=daterange.split(' - ')[1];
          var queryData={
               "taskId": $scope.taskId||-1,
               "taskName": $scope.taskName||'',
               "taskType": $scope.taskType,
               "taskLevel": $scope.taskLevel,
               "taskStatus": $scope.taskStatus,
               "taskLabel": $scope.taskLabel||'',
               "taskParentId":$scope.parentTask,
               "taskDependId":$scope.taskDepended,
               "taskSystem": $scope.taskSystem,
               "taskCreater": $scope.taskCreater,
               "taskPrincipal": $scope.taskPrincipal,
               "taskCreateStartTime": $scope.startTime,
               "taskCreateEndTime": $scope.stopTime,
               "source":$scope.source,
               "pageSize": $scope.pageSize,
               "pageNum": $scope.pageNum,
               "sortOrder": $scope.sortOrder,
               "sortName": $scope.sortName
          } 
       }

       
      $http({
       method:'post',
         url:'/cloudui/master/ws/releaseTask/listReleaseTaskOrders',
         data:$.param(queryData),
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
       $scope.pageNum=1;
       var queryConfig={};
       queryConfig.taskId=$scope.taskId;
       queryConfig.taskName=$scope.taskName;
       queryConfig.taskType=$scope.taskType;
       queryConfig.taskLevel=$scope.taskLevel;
       queryConfig.taskStatus=$scope.taskStatus;
       queryConfig.taskLabel=$scope.taskLabel;
       queryConfig.taskParentId=$scope.parentTask;
       queryConfig.taskDependId=$scope.taskDepended;
       queryConfig.taskSystem=$scope.taskSystem;
       queryConfig.taskCreater=$scope.taskCreater;
       queryConfig.taskPrincipal=$scope.taskPrincipal;
       queryConfig.source=$scope.source;
       queryConfig.timeVal=$('.daterange').val();
       $cookieStore.put('queryConfig',queryConfig);
       $scope.onPageChange();
   }
   
   // 删除任务
   $scope.delTaskFn=function(index,id){
        ngDialog.openConfirm({
          template:
             '<p class="modal-header">您确定要删除此发布任务吗?</p>' +
             '<div class="modal-body text-right">' +
               '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
               '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
             '</button></div>',
          plain: true,
          className: 'ngdialog-theme-default'
      }).then(function(){
             $http({
           method:'post',
             url:'/cloudui/master/ws/releaseTask/deleteReleaseTaskOrder',
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
                   $state.go('app.release_task_manage',{},{reload:true});
                }else{
                     Notify.alert(
                       '<em class="fa fa-times"></em> '+res.data.message ,
                       {status: 'danger'}
                    ); 
                }
         })
      })
   }

   // 发布
   $scope.releaseFn=function(taskId,systemId,releaseId,releasePhaseId){
      var releaseDialog=ngDialog.open({
         template: 'app/views/dialog/task-release.html'+'?action='+(new Date().getTime()),
         className: 'ngdialog-theme-default',
         scope: $scope,
         cache:false,
         closeByDocument:false,
         data:{taskId:taskId,systemId:systemId,releaseId:releaseId,releasePhaseId:releasePhaseId},
         controller:'taskReleaseCtrl'
       });

      releaseDialog.closePromise.then(function(data){
         $interval.cancel($scope.releaseTimer);
         $scope.releaseTimer = $interval($scope.onPageChange, 5000);
      })
   }
}])

// 任务发布
releaseTaskModule.controller('taskReleaseCtrl',['$scope','$http','ngDialog','Notify','$filter',function($scope,$http,ngDialog,Notify,$filter){
 
    // 发布列表
    $http.get('/cloudui/master/ws/releaseTask/listReleasesBySystem',{
       params:{
          systemId:$scope.ngDialogData.systemId
       }
    }).success(function(data){
         if(data.result)
          { 
             $scope.releaseList=data.data;
             if($scope.releaseList.length>0){
                 $scope.release=$scope.ngDialogData.releaseId?$scope.ngDialogData.releaseId:$scope.releaseList[0].id;
             }
          }
    })
    // 发布阶段列表
    $scope.$watch('release',function(newval,oldval){
        if(newval){
           $http.get('/cloudui/master/ws/releaseTask/listPhasesByRelease',{
               params:{
                  systemId:$scope.ngDialogData.systemId,
                  releaseId:newval
               }
            }).success(function(data){
                 if(data.result)
                  { 
                     $scope.stageList=data.data;
                     if($scope.stageList.length>0){
                         $scope.stage=$scope.ngDialogData.releasePhaseId?$scope.ngDialogData.releasePhaseId:$scope.stageList[0].id;
                     }
                  }
            })
        }
    })
    // 发布
    $scope.releaseFn=function(){
      $http({
          method:'post',
          url:"/cloudui/master/ws/releaseTask/executeTaskOrderMannual",
          data:$.param(
              {
                 taskId:$scope.ngDialogData.taskId,
                 releaseId:$scope.release,
                 releasePhaseId:$scope.stage
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

// 添加发布任务
releaseTaskModule.controller('releaseTaskAddCtrl',['$rootScope','$scope','ngDialog','$http','Notify','$state','FileUploader','$timeout',function($rootScope,$scope,ngDialog,$http,Notify,$state,FileUploader,$timeout){
   $scope.formData={};
   $scope.formData.taskAuto=false;
       
   // 优先级
   $scope.levelList=[
     {'text':'紧急','value':'01'},
     {'text':'非常高','value':'02'},
     {'text':'高','value':'03'},
     {'text':'中','value':'04'},
     {'text':'低','value':'05'}
   ]
   $scope.formData.taskLevel=$scope.levelList[3].value;

   $scope.typeList=[
     {'text':'需求','value':'01'},
     {'text':'缺陷','value':'02'},
     {'text':'优化','value':'03'},
     {'text':'任务','value':'04'},
     {'text':'其他','value':'05'}
   ]
   $scope.formData.taskType=$scope.typeList[1].value;

    // 系统列表
    $http.get('/cloudui/master/ws/resource/listAllSystems'+'?v='+(new Date().getTime()),{
      params:{
        systemName:''
      }
    }).
    success(function(res){
        if(res.result){
           $scope.systemList=res.data;
           if($scope.systemList.length > 0){
        	   $scope.formData.taskSystem=$scope.systemList[0];
           }
        }else{
           Notify.alert(
             '<em class="fa fa-times"></em> '+res.message,
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
        	$scope.formData.taskPrincipal=$scope.userList[0].userName;
        }
    })

    $scope.$watch('formData.taskSystem',function(newval,oldval){

      $scope.formData.taskDepended=[];

      if(newval){
         var systemId=newval.id;
      }else{
         var systemId="";
      }

      // 父任务列表
       $http({
           method:'post',
           url:'/cloudui/master/ws/releaseTask/listReleaseParentTaskOrders',
           data:$.param({
               systemId:systemId
           }),
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
       }).then(function(res){
           if(res.data.result){
                $scope.parentTasksList=res.data.data;
                $scope.parentTasksList.unshift({
                   "id": "parent",
                   "name": "无",
                })
           }else{
             Notify.alert(
                 '<em class="fa fa-times"></em> '+res.data.message ,
                 {status: 'danger'}
             );
           }
       })

       // 依赖任务列表
       $http({
           method:'post',
           url:'/cloudui/master/ws/releaseTask/listReleaseDependTaskOrders',
           data:$.param({
               systemId:systemId
           }),
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
       }).then(function(res){
           if(res.data.result){
                $scope.dependedList=res.data.data;
                if($scope.dependedList != null && $scope.dependedList.length > 0){  
                     $('#task').html('');
                    for(var i = 0; i< $scope.dependedList.length; i++){ 

                        $('#task').html($('#task').html()+"<option value='"+$scope.dependedList[i].id+"'>"+$scope.dependedList[i].name+"</option>");
                       
                    }  
                    
                    $('#task').trigger("chosen:updated").chosen({
                        no_results_text:"--没有搜索到--"
                    });
                              
                }else{
                   $('#task').html('');
                   $('#task').trigger("chosen:updated");
                } 
           }else{
             Notify.alert(
                 '<em class="fa fa-times"></em> '+res.data.message ,
                 {status: 'danger'}
             );
           }
       })
    })

    // 添加文件

    var uploader = $scope.uploader = new FileUploader({
        url: '/cloudui/master/ws/releaseTask/saveReleaseTaskOrder',
        formData:[

        ]
   });
  
  $scope.isloading=true;
  $scope.isDisabled=true;
  
  //上传文件限制
  uploader.filters.push({
    name: 'customFilter',
    fn: function(item, options) {
        
        var nameArr=item.name.split('.');
          
          var name=item.name.split('.'+nameArr[nameArr.length-1])[0]; // 文件名称
  
          var fileType=nameArr[nameArr.length-1]; // 文件类型
          
          var reg=/^[^<>,'";:\?[\]{}()*&%$@!\s]+$/;
  
          /*if(!$scope.fileTypeArr.in_array(fileType)){
             return false;
          }*/ 
          if(!reg.test(name)){
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
      
        var reg=/^[^<>,'";:\?[\]{}()*&%$@!\s]+$/;

        if(this.queue.length<1){
           if(!reg.test(name)){
                Notify.alert(
                 '<em class="fa fa-check"></em> 上传包名称包含空格或特殊字符，请重新命名!',
                  {status: 'success'}
               );
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
        $state.go('app.release_task_manage',{},{reload:true}); 
 
      }else{

         Notify.alert(
             '<em class="fa fa-times"></em> '+response.message ,
             {status: 'danger'}
         );
      }
          
  };
    

   // 添加发布任务
   $rootScope.submitted = false;
   $scope.addTaskFn=function(){

       $rootScope.submitted = true;

       var expectTime=$('.daterange').val();

       $rootScope.app.layout.isShadow=true;
       
       var uploaderFile=uploader.queue[0];
      
       var parentId=$scope.formData.parentTask?$scope.formData.parentTask=="parent"?-1:$scope.formData.parentTask:-1;

        var submitData={
              "name": $scope.formData.taskName, 
              "type": $scope.formData.taskType, 
              "level":$scope.formData.taskLevel, 
              "label":$scope.formData.taskLabel||'', 
              "dependId": angular.toJson($scope.formData.taskDepended), 
              "parentId": parentId, 
              "systemId": $scope.formData.taskSystem.id, 
              "systemName": $scope.formData.taskSystem.resourceName, 
              "systemRequireId": $scope.formData.sysDemandId||'', 
              "systemRequireName": $scope.formData.sysDemandName||'', 
              "businessRequireId": $scope.formData.businessDemandId||'', 
              "businessRequireName": $scope.formData.businessDemandName||'', 
              "codeBranchName":$scope.formData.branchName||'', 
              "codeBaselineName": $scope.formData.baselineName||'', 
              "principal": $scope.formData.taskPrincipal, 
              "expectTime":expectTime, 
              "autoExecute":/*$scope.formData.taskAuto*/false, 
              "cronExpression": $scope.formData.cron||'', 
              "description": $scope.formData.taskDescription||'', 
              "remark": $scope.formData.taskRemark||''
        }
        if(!uploaderFile){
 
      
           submitData.attachment="";
 

            $http({
                 method:'post',
                 url:'/cloudui/master/ws/releaseTask/saveReleaseTaskOrder',
                 data:$.param(submitData),
                 headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
             }).then(function(res){
                 $rootScope.app.layout.isShadow=false;
                 if(res.data.result){
                      Notify.alert(
                          '<em class="fa fa-check"></em> '+res.data.message ,
                          {status: 'success'}
                      ); 
                      $state.go('app.release_task_manage',{},{reload:true});
                 }else{
                      Notify.alert(
                          '<em class="fa fa-times"></em> '+res.data.message ,
                          {status: 'danger'}
                      ); 
                 }
             })

        }else{
             uploaderFile.formData.push(
                 submitData  
              )
            
             uploaderFile.upload()
        } 
       
   }

   
}])   

// 发布任务编辑
releaseTaskModule.controller('releaseTaskEditCtrl',['$rootScope','$scope','ngDialog','$http','$stateParams','Notify','$filter','$state','$timeout','FileUploader',function($rootScope,$scope,ngDialog,$http,$stateParams,Notify,$filter,$state,$timeout,FileUploader){
   $scope.formData={};

   // 优先级
   $scope.levelList=[
     {'text':'紧急','value':'01'},
     {'text':'非常高','value':'02'},
     {'text':'高','value':'03'},
     {'text':'中','value':'04'},
     {'text':'低','value':'05'}
   ]
   // 类型
   $scope.typeList=[
     {'text':'需求','value':'01'},
     {'text':'缺陷','value':'02'},
     {'text':'优化','value':'03'},
     {'text':'任务','value':'04'},
     {'text':'其他','value':'05'}
   ]

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
    })

   // 系统列表
    $http.get('/cloudui/master/ws/resource/listAllSystems'+'?v='+(new Date().getTime()),{
      params:{
        systemName:''
      }
    }).
    success(function(res){
        if(res.result){
           $scope.systemList=res.data;
        }else{
           Notify.alert(
             '<em class="fa fa-times"></em> '+res.message,
              {status: 'danger'}
           );
        }
    })

   
    $scope.$watch('formData.taskSystem',function(newval,oldval){
       
      $scope.formData.taskDepended=[];

      if(newval){
         var systemId=newval.id;
      }else{
         var systemId="";
      }

       // 父任务列表
       $http({
           method:'post',
           url:'/cloudui/master/ws/releaseTask/listReleaseParentTaskOrders',
           data:$.param({
               systemId:systemId
           }),
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
       }).then(function(res){
           if(res.data.result){
                $scope.parentTasksList=res.data.data;
                for(var i=0;i<$scope.parentTasksList.length;i++){
                  if($scope.parentTasksList[i].id==$stateParams.id){
                     $scope.parentTasksList.splice(i,1);
                  }
                }
                $scope.parentTasksList.unshift({
                   "id": "parent",
                   "name": "无",
                })
           }else{
             Notify.alert(
                 '<em class="fa fa-times"></em> '+res.data.message ,
                 {status: 'danger'}
             );
           }
       })

        // 依赖任务列表
        $http({
           method:'post',
           url:'/cloudui/master/ws/releaseTask/listReleaseDependTaskOrders',
           data:$.param({
               systemId:systemId
           }),
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
       }).then(function(res){
           if(res.data.result){
                $scope.dependedList=res.data.data;   

                var selectedArr=angular.fromJson($scope.taskInfo.dependId);

                if($scope.dependedList != null && $scope.dependedList.length > 0){ 

                    for(var i=0;i<$scope.dependedList.length;i++){
                      if($scope.dependedList[i].id==$stateParams.id){
                         $scope.dependedList.splice(i,1);
                      }
                    }

                    $('#task').html(''); 

                    for(var i = 0; i< $scope.dependedList.length; i++){ 

                        var dataSelected=$filter('filter')(selectedArr,''+$scope.dependedList[i].id,true); 
                        
                        if(dataSelected&&dataSelected.length>0)
                        {
                            $('#task').html($('#task').html()+"<option value='"+$scope.dependedList[i].id+"' selected>"+$scope.dependedList[i].name+"</option>");
                        }else{
                            $("#task").html($('#task').html()+"<option value='"+$scope.dependedList[i].id+"'>"+$scope.dependedList[i].name+"</option>"); 
                        }
                       
                    }  
                    
                    $('#task').trigger("chosen:updated").chosen({
                        no_results_text:"--没有搜索到--"
                    });
                              
                }else{
                    $('#task').html("");
                    $('#task').trigger("chosen:updated");
                } 
           }else{
             Notify.alert(
                 '<em class="fa fa-times"></em> '+res.data.message ,
                 {status: 'danger'}
             );
           }
       })
    })


   // 任务详情信息
   $http({
       method:'post',
         url:'/cloudui/master/ws/releaseTask/getReleaseTaskOrderById',
         data:$.param({
               "taskId": $stateParams.id
         }),
         headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
    }).then(function(res){
      if(res.data.result){
            $scope.taskInfo=res.data.data;
            $scope.formData.taskName=$scope.taskInfo.name;
            $scope.formData.taskType=$scope.taskInfo.type;  
            $scope.formData.taskLevel=$scope.taskInfo.level;
            $scope.formData.taskLabel=$scope.taskInfo.label;
             
            $scope.$watch('systemList',function(newval,oldval){
                if(newval.length>0){
                   $scope.formData.taskSystem=$filter('filter')(newval,$scope.taskInfo.systemId)[0];
                }
            },true)
            
            $scope.$watch('parentTasksList',function(newval,oldval){
                if(newval.length>0){
                   for(var i=0;i<newval.length;i++){
                       if(newval[i].id==$scope.taskInfo.parentId){
                          $scope.formData.parentTask=newval[i].id;
                       }else{
                          $scope.formData.parentTask="";
                       }
                    }
                }
            },true)

            $scope.$watch('userList',function(newval,oldval){
                if(newval.length>0){
                   $scope.formData.taskPrincipal=$scope.taskInfo.principal;
                }
            },true)
           
            $scope.formData.taskAuto=$scope.taskInfo.autoExecute;
            $scope.formData.cron=$scope.taskInfo.cronExpression;
            $scope.formData.taskDescription=$scope.taskInfo.description;
            $scope.formData.taskRemark=$scope.taskInfo.remark;
            $scope.formData.time=$scope.taskInfo.expectTime;
            $scope.formData.sysDemandId=$scope.taskInfo.systemRequireId||'';
            $scope.formData.sysDemandName=$scope.taskInfo.systemRequireName||'';
            $scope.formData.businessDemandId=$scope.taskInfo.businessRequireId||'';
            $scope.formData.businessDemandName=$scope.taskInfo.businessRequireName||'';
            $scope.formData.branchName=$scope.taskInfo.codeBranchName;
            $scope.formData.baselineName=$scope.taskInfo.codeBaselineName;
            $scope.formData.attachment=$scope.taskInfo.attachment;

      }else{
             Notify.alert(
                  '<em class="fa fa-times"></em> '+res.data.message ,
                  {status: 'danger'}
              ); 
      }
    })

    // 添加文件

    var uploader = $scope.uploader = new FileUploader({
        url: '/cloudui/master/ws/releaseTask/updateReleaseTaskOrder',
        formData:[

        ]
   });
  
  $scope.isloading=true;
  $scope.isDisabled=true;
  
  //上传文件限制
  uploader.filters.push({
    name: 'customFilter',
    fn: function(item, options) {
        
        var nameArr=item.name.split('.');
          
          var name=item.name.split('.'+nameArr[nameArr.length-1])[0]; // 文件名称
  
          var fileType=nameArr[nameArr.length-1]; // 文件类型
          
          var reg=/^[^<>,'";:\?[\]{}()*&%$@!\s]+$/;
  
          /*if(!$scope.fileTypeArr.in_array(fileType)){
             return false;
          }*/ 
          if(!reg.test(name)){
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
      
        var reg=/^[^<>,'";:\?[\]{}()*&%$@!\s]+$/;

        if(this.queue.length<1){
           if(!reg.test(name)){
                Notify.alert(
                 '<em class="fa fa-check"></em> 上传包名称包含空格或特殊字符，请重新命名!',
                  {status: 'success'}
               );
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
    $scope.formData.attachment=fileItem.file.name;
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
        $state.go('app.release_task_manage',{},{reload:true}); 
 
      }else{

         Notify.alert(
             '<em class="fa fa-times"></em> '+response.message ,
             {status: 'danger'}
         );
      }
          
  };
   
    // 更新任务
     $rootScope.submitted = false;
    $scope.updateTaskFn=function(){
       $rootScope.submitted = true;

       $rootScope.app.layout.isShadow=true;

       var uploaderFile=uploader.queue[0];
       
       var expectTime=$('.daterange').val();
       
       var parentId=$scope.formData.parentTask?$scope.formData.parentTask=="parent"?-1:$scope.formData.parentTask:-1;

       var submitData={
              "taskId":$scope.taskInfo.id,
              "name": $scope.formData.taskName, 
              "type": $scope.formData.taskType, 
              "level":$scope.formData.taskLevel, 
              "label":$scope.formData.taskLabel||'', 
              "dependId": angular.toJson($scope.formData.taskDepended), 
              "parentId":parentId, 
              "systemId": $scope.formData.taskSystem.id, 
              "systemName": $scope.formData.taskSystem.resourceName, 
              "systemRequireId": $scope.formData.sysDemandId||'', 
              "systemRequireName": $scope.formData.sysDemandName||'', 
              "businessRequireId": $scope.formData.businessDemandId||'', 
              "businessRequireName": $scope.formData.businessDemandName||'', 
              "codeBranchName":$scope.formData.branchName||'', 
              "codeBaselineName": $scope.formData.baselineName||'', 
              "principal": $scope.formData.taskPrincipal, 
              "expectTime":expectTime, 
              "autoExecute":false/*$scope.formData.taskAuto*/, 
              "cronExpression": $scope.formData.cron||'', 
              "description": $scope.formData.taskDescription||'', 
              "remark": $scope.formData.taskRemark||''
        }

        if(!uploaderFile){

           submitData.attachment=$scope.formData.attachment;

            $http({
                 method:'post',
                 url:'/cloudui/master/ws/releaseTask/updateReleaseTaskOrder',
                 data:$.param(submitData),
                 headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
             }).then(function(res){
                 $rootScope.app.layout.isShadow=false;
                 if(res.data.result){
                      Notify.alert(
                          '<em class="fa fa-check"></em> '+res.data.message ,
                          {status: 'success'}
                      ); 
                      $state.go('app.release_task_manage',{},{reload:true});
                 }else{
                      Notify.alert(
                          '<em class="fa fa-times"></em> '+res.data.message ,
                          {status: 'danger'}
                      ); 
                 }
             })

        }else{
             uploaderFile.formData.push(
                 submitData  
              )
            
             uploaderFile.upload()
        }

    }

}]) 

// 发布任务详情
releaseTaskModule.controller('releaseTaskDetailCtrl',['$rootScope','$scope','$http','$stateParams','$state','Notify','$interval','ngDialog','$filter',function($rootScope,$scope,$http,$stateParams,$state,Notify,$interval,ngDialog,$filter){
	// 类型
	$scope.typeList=[
	     {'text':'(全部)','value':'00'},
	     {'text':'需求','value':'01'},
	     {'text':'缺陷','value':'02'},
	     {'text':'优化','value':'03'},
	     {'text':'任务','value':'04'},
	     {'text':'其他','value':'05'}
	   ]
	// 优先级
	$scope.levelList=[
	     {'text':'(全部)','value':'00'},
	     {'text':'紧急','value':'01'},
	     {'text':'非常高','value':'02'},
	     {'text':'高','value':'03'},
	     {'text':'中','value':'04'},
	     {'text':'低','value':'05'}
	   ]
	// 状态
	$scope.statusList=$rootScope.statusList.concat([]);
  $scope.statusList.unshift({'text':'(全部)','value':'00'});
	// 任务来源
	$scope.sourceList=[
	     {'text':'(全部)','value':'00'},
	     {'text':'发布管理平台','value':'01'},
	     {'text':'redmine','value':'02'},
	     {'text':'jira','value':'03'},
	     {'text':'it作业管理平台','value':'04'},
	     {'text':'dragonFly','value':'05'},
	     {'text':'其他','value':'06'}
	   ]
	
	$scope.$on('$destroy', function() {
       $interval.cancel($scope.historyTimer);  
    });

   $scope.doData={};
   $scope.pageSize=10;
   $scope.pageNum=1;

   // 任务详情信息
   $http({
       method:'post',
         url:'/cloudui/master/ws/releaseTask/getReleaseTaskOrderById',
         data:$.param({
               "taskId": $stateParams.id
         }),
         headers : { 'Content-Type': 'application/x-www-form-urlencoded' }   
    }).then(function(res){
      if(res.data.result){
            $scope.taskInfo=res.data.data;
            $scope.isAction=(!$scope.taskInfo.autoExecute)&&($scope.taskInfo.status!="03")&&($scope.taskInfo.status!="05")
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
             url:'/cloudui/master/ws/releaseTask/getReleaseTaskOrderById',
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

// 发布班车列表
releaseTaskModule.controller('releaseListManageCtrl',['$scope','$state','ngDialog','Notify','$http',function($scope,$state,ngDialog,Notify,$http){
   
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
releaseTaskModule.controller('releaseListAddCtrl',['$rootScope','$scope','$state','ngDialog','Notify','$http',function($rootScope,$scope,$state,ngDialog,Notify,$http){
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
releaseTaskModule.controller('releaseListEditCtrl',['$rootScope','$scope','$state','ngDialog','Notify','$http','$stateParams',function($rootScope,$scope,$state,ngDialog,Notify,$http,$stateParams){
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

// 验证cron表达式
releaseTaskModule.directive('validcron', function($http) {
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

// 集成列表
releaseTaskModule.controller('releaseIntegrationManageCtrl',['$scope','$state','ngDialog','Notify','$http','$stateParams',function($scope,$state,ngDialog,Notify,$http,$stateParams){
   $scope.pageSize=10; 
   $scope.pageNum=1;

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
         url:'/cloudui/master/ws/release/listReleaseIntegration',
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

   // 删除任务
   $scope.delIntegrationFn=function(index,id){
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

// 集成添加
releaseTaskModule.controller('releaseIntegrationAddCtrl',['$rootScope','$scope','ngDialog','$http','Notify','$state',function($rootScope,$scope,ngDialog,$http,Notify,$state){
   $scope.formData={};

   // 项目列表
   $scope.busList=[];
   $http({
         method:'post',
           url:'/cloudui/master/ws/release/listReleaseSystem',
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

   // 添加集成
   $rootScope.submitted = false;
   $scope.addIntegrationFn=function(){

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
         url:'/cloudui/master/ws/release/saveReleaseIntegration',
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

// 集成编辑
releaseTaskModule.controller('releaseIntegrationEditCtrl',['$rootScope','$scope','ngDialog','$http','$stateParams','Notify','$filter','$state','$timeout',function($rootScope,$scope,ngDialog,$http,$stateParams,Notify,$filter,$state,$timeout){
   $scope.formData={};

   // 系统列表
   $scope.busList=[];
   $http({
         method:'post',
           url:'/cloudui/master/ws/release/listReleaseSystem',
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

   // 集成详情信息
   $timeout(function(){
      $http({
       method:'post',
         url:'/cloudui/master/ws/release/getReleaseIntegrationById',
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
   
    // 更新集成
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
           url:'/cloudui/master/ws/release/updateReleaseIntegration',
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

// 集成详情
releaseTaskModule.controller('releaseIntegrationDetailCtrl',['$scope','$http','$stateParams','$state','Notify','$interval','ngDialog','$filter',function($scope,$http,$stateParams,$state,Notify,$interval,ngDialog,$filter){
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

   // 集成详情信息
   $http({
       method:'post',
         url:'/cloudui/master/ws/release/getReleaseIntegrationById',
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
             url:'/cloudui/master/ws/release/getReleaseIntegrationById',
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

    // 手动测试连接
    $scope.actionTaskFn=function(){
        $http({
           method:'post',
             url:'/cloudui/master/ws/release/testIntegrationServer',
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