var customModule=angular.module('custom',[]);

/* ------------------------------------定制管理------------------------------------ */
customModule.controller('customManage',['$rootScope','$scope','$http','Notify','fileReader','$timeout',"$filter",function($rootScope,$scope,$http,Notify,fileReader,$timeout,$filter){
	 

   $scope.displayList=[
      {text:'显示',value:true},
      {text:'不显示',value:false}
   ];

   $scope.initFn=function(){
   	   $scope.name=$rootScope.app.name;
	     $scope.description=$rootScope.app.description;
       $scope.nameDisplay=$rootScope.app.nameDisplay;
       $scope.companyName=$rootScope.app.companyName;
       $scope.companyDes=$rootScope.app.companyDes;
       $scope.technical_hotline=$rootScope.app.technical_hotline;
       $scope.product_hotline=$rootScope.app.product_hotline;
       $scope.difficult_consultation=$rootScope.app.difficult_consultation;
       $scope.wechat=$rootScope.app.wechat;
       $scope.qq=$rootScope.app.qq;
       $scope.website=$rootScope.app.website;
       $scope.copyright=$rootScope.app.copyright;
	     $scope.biglogo=$rootScope.app.logosrc_big;
	     $scope.smalllogo=$rootScope.app.logosrc_small; 
	     $scope.faviconlogo=$rootScope.app.logosrc_favicon;
	     $scope.picBigValid=true;
	     $scope.picSmallValid=true;
	     $scope.picIconValid=true;
       $scope.bigSizeError=false;
       $scope.smallSizeError=false;
       $scope.faviconSizeError=false;
       $scope.faviconTypeError=false;
   }
   
   $scope.initFn();

   $scope.reset=function(){
   	  $scope.initFn();
   }

   

   $scope.$watch('bigSizeError',function(newval,oldval){
      if(newval){
      	 $scope.picBigValid=false;
      } else {
    	 $scope.picBigValid=true;
      }
   })

   $scope.$watch('smallSizeError',function(newval,oldval){
      if(newval){
      	 $scope.picSmallValid=false;
      } else {
    	 $scope.picSmallValid=true;
      }
   })

   $scope.$watch('faviconSizeError',function(newval,oldval){
      if(newval){
      	 $scope.picIconValid=false;
      } else {
    	 $scope.picIconValid=true;
      }
   })

   $scope.$watch('faviconTypeError',function(newval,oldval){
      if(newval){
         $scope.picIconTypeValid=false;
      } else {
       $scope.picIconTypeValid=true;
      }
   })


	$scope.getFile = function (file,pic) {
	    fileReader.readAsDataUrl(file,$scope)
	        .then(function (result) {
	        	$scope[pic]=result;
	    });
	};

	$("#bigfile").change(function(){
		$scope.bigFile= this.files[0];

		var bigEle=angular.element("#biglogo");
		 

		if($scope.bigFile.size>100*1024){
            $scope.bigSizeError=true;
		} else {
			$scope.bigSizeError=false;
		}

		$scope.getFile($scope.bigFile,'biglogo');
	});

	$("#smallfile").change(function(){
		$scope.smallFile = this.files[0];
		if($scope.smallFile.size>100*1024){
            $scope.smallSizeError=true;
		} else {
			$scope.smallSizeError=false;
		}
		$scope.getFile($scope.smallFile,'smalllogo');
	});

  $("#faviconfile").change(function(){
    $scope.faviconfile = this.files[0];

    if($scope.faviconfile.type!=='image/x-icon'){
        $scope.faviconTypeError=true;
    } else {
      $scope.faviconTypeError=false;
    }

    if($scope.faviconfile.size>30*1024){
        $scope.faviconSizeError=true;
    } else {
    	$scope.faviconSizeError=false;
    }
    $scope.getFile($scope.faviconfile,'faviconlogo');
  });

	 
   // 验证表单
   $rootScope.submitted = false;

   // 保存
   $scope.addFn=function(){

	   $rootScope.submitted = true;

	   var info={
	   	  name:$scope.name,
        description:$scope.description,
        nameDisplay:$scope.nameDisplay,
        companyName:$scope.companyName,
        companyDes:$scope.companyDes,
        technical_hotline:$scope.technical_hotline,
        product_hotline:$scope.product_hotline,
        difficult_consultation:$scope.difficult_consultation,
        wechat:$scope.wechat,
        qq:$scope.qq,
        website:$scope.website,
        copyright:$scope.copyright
	   }

	   var data={
	   	   	  big:$scope.bigFile,
	   	   	  small:$scope.smallFile,
            icon:$scope.faviconfile,
	   	   	  info:angular.toJson(info)
	   	}

	   var fd = new FormData();
        angular.forEach(data, function(val, key) {
            fd.append(key, val);
        });

	   $http({
	   	   method:"post",
	   	   url:"/cloudui/master/ws/logo/upload",
	   	   data: fd,
           headers: {'Content-Type': undefined},
           transformRequest: angular.identity
	   }).success(function(data){
           if(data.result){
                Notify.alert(
                  '<em class="fa fa-check"></em> 保存成功' ,
                  {status: 'success'}
                ); 
                $timeout(function(){
                	$rootScope.app.name=$scope.name;
                	$rootScope.app.description=$scope.description;
                  $rootScope.app.nameDisplay=$scope.nameDisplay; 
                  $rootScope.app.companyName= $scope.companyName;
                  $rootScope.app.companyDes= $scope.companyDes;
                  $rootScope.app.technical_hotline=$scope.technical_hotline; 
                  $rootScope.app.product_hotline= $scope.product_hotline;
                  $rootScope.app.difficult_consultation=$scope.difficult_consultation; 
                  $rootScope.app.wechat= $scope.wechat;
                  $rootScope.app.qq= $scope.qq;
                  $rootScope.app.website=$scope.website; 
                  $rootScope.app.copyright= $scope.copyright;
                	$rootScope.app.logosrc_big=$scope.biglogo;
                	$rootScope.app.logosrc_small=$scope.smalllogo;
                	$rootScope.app.logosrc_favicon=$scope.faviconlogo;
                }) 
           }else{
                Notify.alert(
                  '<em class="fa fa-check"></em> '+data.message ,
                  {status: 'danger'}
              );  
           } 
	   })
   }
}])

