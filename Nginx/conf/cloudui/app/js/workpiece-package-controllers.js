var workpiecePackageModule=angular.module('workpiece-package',[]);

/* ------------------------------------工件包管理------------------------------------ */
workpiecePackageModule.controller('workpiecePackageManageCtrl',['$rootScope','$scope','$http','ngDialog','$state','Notify','$cookieStore',function($rootScope,$scope,$http,ngDialog,$state,Notify,$cookieStore){
   $scope.sortOrder= 'DESC';
	$scope.sortName= '';
	$scope.fnSort = function (arg) {
            arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
            $scope.sortOrder= arguments.callee['fnSort' + arg]?'ASC':'DESC';
            $scope.sortName =arg;
            $scope.onPageChange();	 
       }
   
	 $scope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {   
		 	$scope.clearCookie(); //跳入该页面清空cookie
		  });
	
	 $scope.setCookie=function(){
	    $cookieStore.put('workpieceSearch',$scope.searchval);
		}
		
	$scope.clearCookie=function(){
	    $cookieStore.remove("workpieceSearch");
		}
   $scope.searchval=$cookieStore.get('workpieceSearch')?$cookieStore.get('workpieceSearch'):'';
   // 工件包列表
   $scope.pageSize=10;
   $scope.pageNum=1;
   $scope.onPageChange = function ()
   {    
      $http.get('/cloudui/master/ws/packageResource/listPackagesByPage'+'?v=' + (new Date().getTime()),{
      params:
          {
            pageNum:$scope.pageNum,
            pageSize:$scope.pageSize,
            name:$scope.searchval,
      	    sortOrder:$scope.sortOrder||'DESC',
            sortName:$scope.sortName||''
          }
     }).success(function(data){
       $scope.workpiecePackageList = data.rows;
       $scope.listoff=data.rows.length>0?true:false;
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
    
    // 搜素
    $scope.search=function()
    {
       $scope.pageNum=1;
       $scope.setCookie();
 	   $scope.onPageChange(); 
       if($scope.searchval.length==0)
       {
            $scope.pageNum=1;
            $scope.onPageChange(); 
       }
    }
     
    // 下载工件包
    $scope.downFtpFn=function(id){
    	window.location.href="/cloudui/master/ws/packageResource/download?id="+id
    }
    
    // 删除工件包
    $scope.delFn=function(id,index){
	    ngDialog.openConfirm({
          template:
             '<p class="modal-header">您确定要删除此工件包吗?</p>' +
             '<div class="modal-body text-right">' +
               '<button type="button" class="btn btn-default mr20" ng-click="closeThisDialog(0)">取消' +
               '<button type="button" class="btn btn-primary" ng-click="confirm(1)">确定' +
             '</button></div>',
          plain: true,
          className: 'ngdialog-theme-default'
	    }).then(function(){
	          $rootScope.app.layout.isShadow=true;
	          $http({
	        	  method:"DELETE",
	        	  url:'/cloudui/master/ws/packageResource/deletePackage?id='+id
	          }).success(function(data){
	              $rootScope.app.layout.isShadow=false;
	              if(data.result)
	              {  
	                   Notify.alert(
	                             '<em class="fa fa-check"></em> '+data.message ,
	                             {status: 'success'}
	                        );
	                   $state.go('app.workpiece_package_manage',{},{reload:true});
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
    $scope.importWorkpiecePackage=function(){
        ngDialog.open({
          template: 'app/views/dialog/workpiece-package-import.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          scope: $scope,
          closeByDocument:false,
          cache:false,
          controller:'importWorkpiecePackageCtrl'
        });
    }
    // 导出工件
    $scope.exportWorkpiecePackage=function(){
        ngDialog.open({
          template: 'app/views/dialog/workpiece-package-export.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          scope: $scope,
          closeByDocument:false,
          cache:false,
          controller:'exportWorkpiecePackageCtrl'
        });
    }
}])

/* ------------------------------------导入工件------------------------------------ */
workpiecePackageModule.controller('importWorkpiecePackageCtrl',['$rootScope','$scope','$http','ngDialog','Notify','$state','$filter',function($rootScope,$scope,$http,ngDialog,Notify,$state,$filter){
    
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
		           $state.go('app.workpiece_package_manage',{},{reload:true});
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
       
       var url='/cloudui/ws/packageResource/importPackage';
       var checkUrl='/cloudui/master/ws/packageResource/checkImportPackage';
       var fileType="workpiece";
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

/* ------------------------------------导出工件------------------------------------ */
workpiecePackageModule.controller('exportWorkpiecePackageCtrl',['$rootScope','$scope','$http','ngDialog','Notify','$state','$filter',function($rootScope,$scope,$http,ngDialog,Notify,$state,$filter){
	
	$scope.checkappId=[]; // 选中的工件
	$scope.checkData=[];
	
	$scope.exportPageSize=5;
	$scope.exportPageNum=1;
	// 工件列表
    $scope.onExportPageChange = function ()
    {   
    	$http.get('/cloudui/master/ws/packageResource/listPackagesByPage'+'?v=' + (new Date().getTime()),{
	        params:
	          {
	            pageNum:$scope.exportPageNum,
	            pageSize:$scope.exportPageSize,
	            name:$scope.exportSearchval
	          }
        }).success(function(data){

	    	angular.forEach(data.rows,function(val,key){
	    		 var ischecked=$filter('filter')($scope.checkappId,val.id).length>0?true:false;
	    		 data.rows[key].ischecked=ischecked;
	    		 data.rows[key].isExport=false;
	    	})
	    	 
	     	$scope.exportListoff=data.rows.length>0?true:false;
	        $scope.exportWarninginfo='暂无数据';
	        $scope.exportWorkpiecePackageList = data.rows;
	        
	        $scope.exportPageCount=Math.ceil(data.total/$scope.exportPageSize);
	        if($scope.exportPageCount==0){
	           $scope.exportPageCount=1;
	        }
       }).error(function(){
         $scope.exportListoff=false;
         $scope.exportWarninginfo='暂无结果';
     })
    }
    
    // 导出工件
    $scope.exportFn=function()
    {  
    	var checkbox = $scope.checkappId;
        if(checkbox.length==0)
        {
           Notify.alert(
             '请选择要导出的工件！' ,
             {status: 'info'}
           );
        }else
        {
        	  var ids=[];

	          for(var i=0;i<checkbox.length;i++)
	          {
	             var needZipExport=$filter('filter')($scope.checkData,checkbox[i]).length>0?true:false;
	             
	             ids.push({
	                id:checkbox[i],
	                needZipExport:needZipExport
	             })
	          }
	          $http({
	                method:'post',
	                url:'/cloudui/master/ws/packageResource/exportPackage',
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
	                 a.download = 'workpiece'+myJsDate+myJsDate_sss+'.zip'; 
	                 a.href = URL.createObjectURL(blob);
	                 a.click();
	                 document.body.removeChild(a);
	            });
//          window.location.href = '/cloudui/master/ws/packageResource/exportPackage?ids='+encodeURI(angular.toJson(ids));
           ngDialog.close();
        }
    }
    
    // 搜素
    $scope.exportSearch=function()
    {
 	   $scope.exportPageNum=1;
       $scope.onExportPageChange(); 
    }
}])

/* ------------------------------------添加工件包------------------------------------ */
workpiecePackageModule.controller('addWorkpiecePackageCtrl',['$rootScope','$scope','$http','ngDialog','$state','Notify','FileUploader','$timeout',function($rootScope,$scope,$http,ngDialog,$state,Notify,FileUploader,$timeout){
    
	$rootScope.submitted = false;
	
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
	        
	        var reg=/^[^<>,'";:\?[\]{}()*&%$@!\s]+$/;
	
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
	    
	      var reg=/^[^<>,'";:\?[\]{}()*&%$@!\s]+$/;

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
	       $state.go('app.workpiece_package_manage',{},{reload:true}); 
 
	    }else{

	       Notify.alert(
	           '<em class="fa fa-times"></em> '+response.message ,
	           {status: 'danger'}
	       );
	    }
		      
	};
	
	// 添加脚本
	$scope.addFn=function(){
		 $rootScope.submitted = true;
		 $rootScope.app.layout.isShadow=true;
		 var uploaderFile=uploader.queue[0];
		 uploaderFile.formData.push(
			{
				description: $scope.description||''
			}	 
		 );
		
		 uploaderFile.upload()
	
	}
 
}])