var approveModule=angular.module('approve',[]);
/* ------------------------------------审批管理------------------------------------ */
approveModule.controller('approveManageCtrl',['$rootScope','$scope','$http','ngDialog','Notify','$cookieStore','$filter','$state',function($rootScope,$scope,$http,ngDialog,Notify,$cookieStore,$filter,$state){
	// 搜索类型
	$scope.searchTypeList=[
	    {text:'发布名称',value:'releaseName'},
	    {text:'发布描述',value:'description'},
	    {text:'发布周期',value:'lifecycleName'}
	  ];
  $scope.approvePages=[
     "app.myApprove"
  ]

  $scope.pageSize=10;

  $scope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
    var isClear=$filter('filter')($scope.approvePages,toState.name).length>0?false:true;
    if(isClear){
      $scope.clearCookie(); 
    }
  });

  var url='/cloudui/master/ws/release/listReadyReleaseApprovalsByUser';
  
  $scope.searchval=$cookieStore.get('approveSearch')?$cookieStore.get('approveSearch'):'';
  
  if($cookieStore.get('approvePageNum')){
	  	 var data={
	  			pageNum:1,
	  			pageSize:$scope.pageSize
  			}
	     $scope.listDataPromise=$http({
	            method  : 'POST',
	            url     : url,   
	            data    : $.param(data),   
	            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
	      }).success(function(res){
	        
	         $scope.totalPageNum=res.page.total;
	         
	         if($cookieStore.get('approvePageNum')<=$scope.totalPageNum){
	            $scope.pageNum=$cookieStore.get('approvePageNum');
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
		$cookieStore.put('approvePageNum',$scope.pageNum);
        $cookieStore.put('approveSearch',$scope.searchval);
	}
	
	$scope.clearCookie=function(){
		$cookieStore.remove("approvePageNum");
		$cookieStore.remove("approveSearch");
	}
	
   $scope.sortOrder= 'DESC';
   $scope.sortName= '';
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
			  pageNum:$scope.pageNum,
	          pageSize:$scope.pageSize,
	          sortOrder:$scope.sortOrder,
	          sortName:$scope.sortName  
  		}
	  $http({
          method  : 'POST',
          url     : url,   
          data    : $.param(data),   
          headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
    }).success(function(data){
//       $scope.approveList=[{"name":"test1","description":"test1","createTime":"2011-03-15","status":"成功"}]; 
       $scope.approveList = data.page.rows;
       $scope.listoff=data.page.total>0?true:false;
       $scope.warninginfo='提示：暂无数据';
       $scope.pageCount=Math.ceil(data.page.total/$scope.pageSize);
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
    	$scope.pageNum=1;
    	$scope.setCookie();
 	    $scope.onPageChange(); 
    }
     //获取任务
     $scope.myApprove = function (type)
     {   
        $('#tab1').toggleClass('active');
		$('#tab2').toggleClass('active');
        url='/cloudui/master/ws/release/listReadyReleaseApprovalsByUser';
        if(type !="Wait"){
        	url='/cloudui/master/ws/release/listDoneReleaseApprovalsByUser';
        }
  	   $scope.clearCookie();
  	   $scope.search();
      };
      //任务审批
      $scope.approve = function (idv,status)
      {   
         if(!$scope.pageNum){
              return ;
         }
         var updateUrl='/cloudui/master/ws/release/updateReleaseApprovalStatus';
   	     var updateData={
   	    		id:idv,
   	    		status:status
     		}
   	     $rootScope.app.layout.isShadow=true;
   	     $http({
   	    	 method  : 'POST',
   	    	 url     : updateUrl,   
   	    	 data    : $.param(updateData),   
   	    	 headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
   	     }).success(function(data){
        	 $rootScope.app.layout.isShadow=false;
             if(data.result)
             {
               Notify.alert(
                  '<em class="fa fa-check"></em> '+data.message ,
                   {status: 'success'}
               );
               $scope.onPageChange();
             }else{
               Notify.alert(
                  '<em class="fa fa-times"></em> '+data.message ,
                   {status: 'danger'}
               );
             }
             })
       };
}])

