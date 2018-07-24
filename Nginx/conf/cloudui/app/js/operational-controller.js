var operationalModule=angular.module('operational',[]);

operationalModule.controller('operationalManage',['$scope','$http','Notify','$timeout','$filter','ngDialog',function($scope,$http,Notify,$timeout,$filter,ngDialog){
   $scope.pageSize=10;
   $scope.pageNum=1;
   $scope.loadoff=false;
   $scope.checkappId=[]; // 选中的蓝图
   // 用户列表
   $http.get('/cloudui/master/ws/audit/getAllUsers?v='+(new Date().getTime())).
   success(function(data){
      $scope.userList=data;
      $scope.userList.unshift('(全部)');
      $scope.user=$scope.userList[0];
   })
   // 操作资源类型
   $http.get('/cloudui/master/ws/audit/getAllResourceType?v='+(new Date().getTime())).
   success(function(data){
      $scope.resourceTypeList=data;
      $scope.resourceTypeList.unshift('(全部)');
      $scope.resourceType=$scope.resourceTypeList[0];
   })
   // 操作结果
   $scope.resultList=[
      {value:"",text:"(全部)"},
      {value:0,text:"失败"},
      {value:1,text:"成功"}
   ]
   $scope.operateResult=$scope.resultList[0].value;
   // all 操作类型
   var operateTypeList_all=[
      {value:"",text:"(全部)"},
      {value:"add",text:"新增"},
      {value:"update",text:"更新"},
      {value:"delete",text:"删除"},
      {value:"import",text:"导入"},
      //{value:"export",text:"导出"},
      {value:"login",text:"登录"},
      {value:"logout",text:"注销"},
      {value:"clone",text:"克隆"}
   ]
  // 基础操作类型
   var operateTypeList_base=[
      {value:"",text:"(全部)"},
      {value:"add",text:"新增"},
      {value:"update",text:"更新"},
      {value:"delete",text:"删除"}
   ]
// 用户操作类型
   var operateTypeList_user=[
      {value:"",text:"(全部)"},
      {value:"login",text:"登录"},
      {value:"logout",text:"注销"}
   ]
   $scope.operateTypeList=operateTypeList_all;
  $scope.$watch("resourceType",function(newval,oldval){
	 if(newval){
		   var tmp = [];
		   if(newval=='用户'){
			   $scope.operateTypeList= operateTypeList_user; 
		   }else if(newval=='蓝图模板'||newval=='工件'){
			   tmp=operateTypeList_base.concat();
			   tmp.push({value:"import",text:"导入"});
			   $scope.operateTypeList= tmp;
		   }else if(newval=='蓝图实例'){
			   tmp=operateTypeList_base.concat();
			   tmp.push({value:"clone",text:"克隆"});
			   $scope.operateTypeList= tmp;
		   }else if(newval=='通用组件'){
			   tmp=operateTypeList_base.concat();
			   tmp.push({value:"import",text:"导入"});
			   tmp.push({value:"clone",text:"克隆"});
			   $scope.operateTypeList= tmp;
		   }else if(newval=='蓝图流程'){
			   tmp.push({value:"",text:"(全部)"});
			   tmp.push({value:"execute",text:"执行"});
			   tmp.push({value:"rollback",text:"回滚"});
			   tmp.push({value:"increase",text:"扩展"});
			   tmp.push({value:"reduce",text:"收缩"});
			   $scope.operateTypeList= tmp;
		   }else if(newval=='蓝图实例组件'){
			   tmp.push({value:"",text:"(全部)"});
			   tmp.push({value:"manage",text:"组件维护"});
			   $scope.operateTypeList= tmp;
		   }else if(newval=='通用组件流程'){
			   tmp.push({value:"",text:"(全部)"});
			   tmp.push({value:"deploy",text:"部署"});
			   tmp.push({value:"start",text:"启动"});
			   tmp.push({value:"stop",text:"停止"});
			   tmp.push({value:"destroy",text:"卸载"});
			   tmp.push({value:"upgrade",text:"升级"});
			   tmp.push({value:"rollback",text:"回滚"});
			   tmp.push({value:"snapshot",text:"快照"});
			   $scope.operateTypeList= tmp;
		   }else if(newval=='ITSM组件流程'){
			   tmp.push({value:"",text:"(全部)"});
			   tmp.push({value:"itsmRelease",text:"发布"});
			   tmp.push({value:"itsmRollback",text:"回退"});
			   tmp.push({value:"itsmStart",text:"启动"});
			   tmp.push({value:"itsmStop",text:"停止"});
			   tmp.push({value:"itsmBatchRelease",text:"批量发布"});
			   $scope.operateTypeList= tmp;
		   }else if(newval=='(全部)'){
			   $scope.operateTypeList= operateTypeList_all;
		   }else{
			   tmp=operateTypeList_base.concat();
			   $scope.operateTypeList= tmp; 
		   }
		   $scope.operateType=$scope.operateTypeList[0].value;
	   }
	   
   });
  $scope.operateType=$scope.operateTypeList[0].value;
   // 文字显示
   $scope.text=function(value,type){
      if(type=="operateType"){
         return $filter('filter')($scope.operateTypeList,value)[0].text;
      }else if(type=="operateResult"){
         return $filter('filter')($scope.resultList,value)[0].text;
      }
   }
   $scope.sortOrder= 'DESC';
   $scope.sortName= '';
   $scope.fnSort = function (arg) {
            arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
            $scope.sortOrder= arguments.callee['fnSort' + arg]?'ASC':'DESC';
            $scope.sortName =arg;
            $scope.onPageChange();
        }
   //查询
   $scope.queryFn=function(pageNum){
      $scope.loadoff=false;

      $timeout(function(){
       $scope.loadoff=true;
      })

      var daterange=$('.daterange').val();
     
      $scope.startdate=daterange.split(' - ')[0];
      $scope.enddate=daterange.split(' - ')[1];
 
      $scope.onPageChange = function (pageNum)
      {    
           $http.get('/cloudui/master/ws/audit/list?v='+'?v=' + (new Date().getTime()),{
           params:
           {
              "pageSize":$scope.pageSize,
              "pageNum":pageNum,
              "userId":$scope.user=='(全部)'?'':$scope.user,
              "resourceType":$scope.resourceType=='(全部)'?'':$scope.resourceType,
              "operateType":$scope.operateType||'',
              "operateResult":$scope.operateResult, 
              "startDate":$scope.startdate||'',
              "endDate":$scope.enddate||'',
              "sortOrder":$scope.sortOrder||'DESC',
              "sortName":$scope.sortName||''
           }
       }).success(function(data){
          angular.forEach(data.rows,function(val,key){
             var ischecked=$filter('filter')($scope.checkappId,val.id).length>0?true:false;
             data.rows[key].ischecked=ischecked;
              
             if(data.rows[key].detail){
                if(data.rows[key].detail.length>100){
                  data.rows[key].detailIntro=data.rows[key].detail.substring(0,100)+'...内容太多，请点击详情查看！';
                }else{
                  data.rows[key].detailIntro=data.rows[key].detail;
                }
             }
             
          })
          $scope.resultData=data.rows;   
          $scope.listoff=data.total>0?true:false;
          $scope.warninginfo='提示：暂无数据';
          $scope.pageCount=Math.ceil(data.total/$scope.pageSize);
          if($scope.pageCount==0){
            $scope.pageCount=1;
          } 
       }).error(function(){
          $scope.listoff=false;
          $scope.warninginfo='暂无结果';
       })
         
    }

   }

   // 导出
   $scope.exportData=function(){
        var checkbox = $scope.checkappId;
        if(checkbox.length==0)
        {
           Notify.alert(
             '请选择要导出的数据！' ,
             {status: 'info'}
           );
        }else
        {
           window.location.href = '/cloudui/master/ws/audit/export?ids='+checkbox;
        }
   }

   // 详情
   $scope.detailFn=function(detail){
        ngDialog.open({
          template: 'app/views/dialog/operational-detail.html'+'?action='+(new Date().getTime()),
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
}])

 



