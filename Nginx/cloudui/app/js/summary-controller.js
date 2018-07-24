var summaryModule=angular.module('summary',[]);

/* ------------------------------------资源池管理------------------------------------ */
summaryModule.controller('summaryManageCtrl',['$rootScope','$scope','$http','$state','$interval','$cookieStore','ngDialog',function($rootScope,$scope,$http,$state,$interval,$cookieStore,ngDialog){
	$scope.envFun = function() {
		$http.get('/cloudui/master/ws/dashboardnew/envNum'+'?v='+(new Date().getTime())).then(
			function(response){
				$scope.envNum = response.data;
			},
			function(response){
			}
		);
	};
	$scope.blueprintFun = function() {
		$http.get('/cloudui/master/ws/dashboardnew/blueprintNum'+'?v='+(new Date().getTime())).then(
			function(response){
				$scope.blueprintNum = response.data;
			},
			function(response){
			}
		);
	};
	$scope.blueprintInstanceFun = function() {
		$http.get('/cloudui/master/ws/dashboardnew/blueprintInstanceNum'+'?v='+(new Date().getTime())).then(
			function(response){
				$scope.blueprintInstanceNum = response.data;
			},
			function(response){
			}
		);
	};
	$scope.componentFun = function() {
		$http.get('/cloudui/master/ws/dashboardnew/componentNum'+'?v='+(new Date().getTime())).then(
			function(response){
				$scope.componentNum = response.data;
			},
			function(response){
			}
		);
	};
	$scope.cancelTimer = function() {
		$interval.cancel($scope.envTimer);
		$interval.cancel($scope.blueprintTimer);
		$interval.cancel($scope.blueprintInstanceTimer);
		$interval.cancel($scope.componentTimer);
		$interval.cancel($scope.getActivityTimer);
	};
	$scope.envTimer = $interval($scope.envFun, 3000);
	$scope.blueprintTimer = $interval($scope.blueprintFun, 3000);
	$scope.blueprintInstanceTimer = $interval($scope.blueprintInstanceFun, 3000);
	$scope.componentTimer = $interval($scope.componentFun, 3000);
	$scope.$on('$destroy', $scope.cancelTimer);
	$scope.sortName = 'startTime';
	$scope.sortOrder = 'DESC';
	$scope.pageSize = 10;
	$scope.pageNum = 1;
	// $scope.initPageFun = function() {
	// 	if($cookieStore.get('summaryActivityPageNum')) {
	// 		$http.get('/cloudui/master/ws/dashboardnew/getActivity'+'?v='+(new Date().getTime()),
	// 			{
	// 				params:{
	// 					pageSize:$scope.pageSize,
	// 					pageNum:1,
	// 					sortName:$scope.sortName,
	// 					sortOrder:$scope.sortOrder
	// 				}
	// 			}).then(
	// 			function(response){
	// 				$scope.totalPageNum = response.data.totalPageNum;
	// 				if($cookieStore.get('summaryActivityPageNum') <= $scope.totalPageNum){
	// 					$scope.pageNum = $cookieStore.get('summaryActivityPageNum');
	// 				}else{
	// 					$scope.pageNum = $scope.totalPageNum;
	// 				}
	// 			},
	// 			function(response){
	// 			}
	// 		);
	// 	} else {
	// 		$scope.pageNum = 1;
	// 	}
	// 	$cookieStore.put('summaryActivityPageNum', $scope.pageNum);
	// };
	// $scope.initPageFun();
	$scope.activityFun = function() {
		$http.get('/cloudui/master/ws/dashboardnew/getActivity'+'?v='+(new Date().getTime()),
			{
				params:{
					pageSize:$scope.pageSize || 10,
					pageNum:$scope.pageNum || 1,
					sortName:$scope.sortName,
					sortOrder:$scope.sortOrder
				}
			}).then(
			function(response){
				$scope.activities = response.data.rows;
				$scope.totalPageNum = response.data.totalPageNum;
			},
			function(response){
			}
		);
	}
	$scope.getActivityTimer = $interval($scope.activityFun, 10000);
	$scope.fnSort = function(arg) {
		arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
		$scope.sortOrder = arguments.callee['fnSort' + arg]?'ASC':'DESC';
		$scope.sortName = arg;
		$scope.activityFun();
	}
	$scope.refresh = function() {
		$scope.envFun();
		$scope.blueprintFun();
		$scope.blueprintInstanceFun();
		$scope.componentFun();
		// $scope.activityFun();
	};
	$scope.refresh();

	// 监控图表

    // 监控配置
	$scope.monitorConfig=function(){
		ngDialog.open({
          template: 'app/views/dialog/monitor-config.html'+'?action='+(new Date().getTime()),
          className: 'ngdialog-theme-default',
          scope: $scope,
          closeByDocument:false,
          cache:false,
          controller:'monitorConfigCtrl'
        });
	}


	if(!$cookieStore.get('monitorConfig')){
         return false;
	}  

	$scope.monitorConfigCookie=$cookieStore.get('monitorConfig');

	// 构建次数
 
    $http.get('/cloudui/master/ws/ci/dashboard/count',{
    	params:{
    		series:angular.toJson($scope.monitorConfigCookie.series),
    		start:$scope.monitorConfigCookie.start,
    		end:$scope.monitorConfigCookie.end,
    		intervalType:$scope.monitorConfigCookie.intervalType
    	}
    }).success(function(data){
	 
	var chart = Highcharts.chart('structure_frequency',{
		credits: {
           enabled: false
        }, 
	    chart: {
	        type: 'areaspline'
	    },
    title: {
        text: ''
    },
    legend: {
        align: 'left',
        verticalAlign: 'top'
    },
    xAxis: {
        allowDecimals: false,
        tickInterval:parseInt(data.x.length/10),
        categories:data.x 
    },
    yAxis: {
        title: {
            text: ''
        },
        labels: {
            formatter: function () {
                return this.value  + '次';
            }
        }
    },
    tooltip: {
        pointFormat: '{series.name} 发布 <b>{point.y:,.0f}</b>次'
    },
    plotOptions: {
        area: {
            pointStart: data.x[0], 
            marker: {
                enabled: false,
                symbol: 'circle',
                radius: 2,
                states: {
                    hover: {
                        enabled: true
                    }
                }
            }
        }
    },
    series: data.series
});
});


 // 构建成功次数
 $http.get('/cloudui/master/ws/ci/dashboard/successCount',{
    	params:{
    		series:angular.toJson($scope.monitorConfigCookie.series),
    		start:$scope.monitorConfigCookie.start,
    		end:$scope.monitorConfigCookie.end,
    		intervalType:$scope.monitorConfigCookie.intervalType
    	}
    }).success(function(data){
	 
	var chart = Highcharts.chart('structure_success_frequency',{
		credits: {
           enabled: false
        }, 
	    chart: {
	        type: 'areaspline'
	    },
    title: {
        text: ''
    },
    legend: {
        align: 'left',
        verticalAlign: 'top'
    },
    xAxis: {
        allowDecimals: false,
        tickInterval:parseInt(data.x.length/10),
        categories:data.x 
    },
    yAxis: {
        title: {
            text: ''
        },
        labels: {
            formatter: function () {
                return this.value  + '次';
            }
        }
    },
    tooltip: {
        pointFormat: '{series.name} 发布 <b>{point.y:,.0f}</b>次'
    },
    plotOptions: {
        area: {
            pointStart: data.x[0], 
            marker: {
                enabled: false,
                symbol: 'circle',
                radius: 2,
                states: {
                    hover: {
                        enabled: true
                    }
                }
            }
        }
    },
    series:data.series
});
});

// 构建平均时长
  $http.get('/cloudui/master/ws/ci/dashboard/averageDuration',{
    	params:{
    		series:angular.toJson($scope.monitorConfigCookie.series),
    		start:$scope.monitorConfigCookie.start,
    		end:$scope.monitorConfigCookie.end,
    		intervalType:$scope.monitorConfigCookie.intervalType
    	}
    }).success(function(data){
	 
	var chart = Highcharts.chart('structure_average_time',{
		credits: {
           enabled: false
        }, 
	    chart: {
	        type: 'areaspline'
	    },
    title: {
        text: ''
    },
    legend: {
        align: 'left',
        verticalAlign: 'top'
    },
    xAxis: {
        allowDecimals: false,
        tickInterval:parseInt(data.x.length/10),
        categories:data.x 
    },
    yAxis: {
        title: {
            text: ''
        },
        labels: {
            formatter: function () {
                return this.value  + '毫秒';
            }
        }
    },
    tooltip: {
        pointFormat: '{series.name} 发布 <b>{point.y:,.0f}</b>毫秒'
    },
    plotOptions: {
        area: {
            pointStart: data.x[0], 
            marker: {
                enabled: false,
                symbol: 'circle',
                radius: 2,
                states: {
                    hover: {
                        enabled: true
                    }
                }
            }
        }
    },
    series: data.series
});
});

// 构建实际时长
  $http.get('/cloudui/master/ws/ci/dashboard/duration',{
    	params:{
    		series:angular.toJson($scope.monitorConfigCookie.series),
    		start:$scope.monitorConfigCookie.start,
    		end:$scope.monitorConfigCookie.end,
    		intervalType:$scope.monitorConfigCookie.intervalType
    	}
    }).success(function(data){
	 
	var chart = Highcharts.chart('structure_actual_time',{
		credits: {
           enabled: false
        }, 
	    chart: {
	        type: 'areaspline'
	    },
    title: {
        text: ''
    },
    legend: {
        align: 'left',
        verticalAlign: 'top'
    },
    xAxis: {
        allowDecimals: false,
        tickInterval:parseInt(data.x.length/10),
        categories:data.x 
    },
    yAxis: {
        title: {
            text: ''
        },
        labels: {
            formatter: function () {
                return this.value  + '毫秒';
            }
        }
    },
    tooltip: {
        pointFormat: '{series.name} 发布 <b>{point.y:,.0f}</b>毫秒'
    },
    plotOptions: {
        area: {
            pointStart: data.x[0], 
            marker: {
                enabled: false,
                symbol: 'circle',
                radius: 2,
                states: {
                    hover: {
                        enabled: true
                    }
                }
            }
        }
    },
    series: data.series
});
});

// 部署次数
   $http({
           method:'post',
           url:'/cloudui/master/ws/monitor/getDeployIndicators',
           data: $.param({
                series:angular.toJson($scope.monitorConfigCookie.series),
	    		start:$scope.monitorConfigCookie.start,
	    		end:$scope.monitorConfigCookie.end,
	    		intervalType:$scope.monitorConfigCookie.intervalType,
	    		indicatorType:'frequency'
           }),
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
       })
    .success(function(data){
	 
	var chart = Highcharts.chart('deploy_frequency',{
		credits: {
           enabled: false
        }, 
	    chart: {
	        type: 'areaspline'
	    },
    title: {
        text: ''
    },
    legend: {
        align: 'left',
        verticalAlign: 'top'
    },
    xAxis: {
        allowDecimals: false,
        tickInterval:parseInt(data.x.length/10),
        categories:data.x 
    },
    yAxis: {
        title: {
            text: ''
        },
        labels: {
            formatter: function () {
                return this.value  + '次';
            }
        }
    },
    tooltip: {
        pointFormat: '{series.name} 发布 <b>{point.y:,.0f}</b>次'
    },
    plotOptions: {
        area: {
            pointStart: data.x[0], 
            marker: {
                enabled: false,
                symbol: 'circle',
                radius: 2,
                states: {
                    hover: {
                        enabled: true
                    }
                }
            }
        }
    },
    series: data.seris
});
});

// 部署成功次数
  $http({
           method:'post',
           url:'/cloudui/master/ws/monitor/getDeployIndicators',
           data: $.param({
                series:angular.toJson($scope.monitorConfigCookie.series),
	    		start:$scope.monitorConfigCookie.start,
	    		end:$scope.monitorConfigCookie.end,
	    		intervalType:$scope.monitorConfigCookie.intervalType,
	    		indicatorType:'success'
           }),
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
       }).success(function(data){
	 
	var chart = Highcharts.chart('deploy_success_frequency',{
		credits: {
           enabled: false
        }, 
	    chart: {
	        type: 'areaspline'
	    },
    title: {
        text: ''
    },
    legend: {
        align: 'left',
        verticalAlign: 'top'
    },
    xAxis: {
        allowDecimals: false,
        tickInterval:parseInt(data.x.length/10),
        categories:data.x 
    },
    yAxis: {
        title: {
            text: ''
        },
        labels: {
            formatter: function () {
                return this.value  + '次';
            }
        }
    },
    tooltip: {
        pointFormat: '{series.name} 发布 <b>{point.y:,.0f}</b>次'
    },
    plotOptions: {
        area: {
            pointStart: data.x[0], 
            marker: {
                enabled: false,
                symbol: 'circle',
                radius: 2,
                states: {
                    hover: {
                        enabled: true
                    }
                }
            }
        }
    },
    series: data.seris
});
});

// 部署平均时长
 $http({
           method:'post',
           url:'/cloudui/master/ws/monitor/getDeployIndicators',
           data: $.param({
                series:angular.toJson($scope.monitorConfigCookie.series),
	    		start:$scope.monitorConfigCookie.start,
	    		end:$scope.monitorConfigCookie.end,
	    		intervalType:$scope.monitorConfigCookie.intervalType,
	    		indicatorType:'averageTime'
           }),
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
       }).success(function(data){
	 
	var chart = Highcharts.chart('deploy_average_time',{
		credits: {
           enabled: false
        }, 
	    chart: {
	        type: 'areaspline'
	    },
    title: {
        text: ''
    },
    legend: {
        align: 'left',
        verticalAlign: 'top'
    },
    xAxis: {
        allowDecimals: false,
        tickInterval:parseInt(data.x.length/10),
        categories:data.x 
    },
    yAxis: {
        title: {
            text: ''
        },
        labels: {
            formatter: function () {
                return this.value  + '毫秒';
            }
        }
    },
    tooltip: {
        pointFormat: '{series.name} 发布 <b>{point.y:,.0f}</b>毫秒'
    },
    plotOptions: {
        area: {
            pointStart: data.x[0], 
            marker: {
                enabled: false,
                symbol: 'circle',
                radius: 2,
                states: {
                    hover: {
                        enabled: true
                    }
                }
            }
        }
    },
    series: data.seris
});
});

// 部署实际时长
  $http({
           method:'post',
           url:'/cloudui/master/ws/monitor/getDeployIndicators',
           data: $.param({
                series:angular.toJson($scope.monitorConfigCookie.series),
	    		start:$scope.monitorConfigCookie.start,
	    		end:$scope.monitorConfigCookie.end,
	    		intervalType:$scope.monitorConfigCookie.intervalType,
	    		indicatorType:'actualTime'
           }),
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
       }).success(function(data){
	 
	var chart = Highcharts.chart('deploy_actual_time',{
		credits: {
           enabled: false
        }, 
	    chart: {
	        type: 'areaspline'
	    },
    title: {
        text: ''
    },
    legend: {
        align: 'left',
        verticalAlign: 'top'
    },
    xAxis: {
        allowDecimals: false,
        tickInterval:parseInt(data.x.length/10),
        categories:data.x 
    },
    yAxis: {
        title: {
            text: ''
        },
        labels: {
            formatter: function () {
                return this.value  + '毫秒';
            }
        }
    },
    tooltip: {
        pointFormat: '{series.name} 发布 <b>{point.y:,.0f}</b>毫秒'
    },
    plotOptions: {
        area: {
            pointStart: data.x[0], 
            marker: {
                enabled: false,
                symbol: 'circle',
                radius: 2,
                states: {
                    hover: {
                        enabled: true
                    }
                }
            }
        }
    },
    series: data.seris
});
});


}]); 

// 监控配置
summaryModule.controller('monitorConfigCtrl',['$scope','$http','Notify','$cookieStore','ngDialog','$state',function($scope,$http,Notify,$cookieStore,ngDialog,$state){
   $scope.formData={};
   // 年
   $scope.getYears=function(){
   	  var years=[];
   	  var startYear=1990;//起始年份
   	  var endYear=new Date().getUTCFullYear();//结束年份，默认为当前年份
   	  for (var i=startYear;i<=endYear;i++){
   	  	  years.push(i);
   	  }
   	  return years;
   }
   $scope.yearList=$scope.getYears();
   $scope.formData.year=2018;
   // 季度
   $scope.quarterList=[
     {"text":"第一","value":1},
     {"text":"第二","value":2},
     {"text":"第三","value":3},
     {"text":"第四","value":4}
   ]
   $scope.formData.quarter=$scope.quarterList[0].value;
   // 月
   $scope.monthList=[
      {"text":"Jan","value":0},
      {"text":"Feb","value":1},
      {"text":"Mar","value":2},
      {"text":"Apr","value":3},
      {"text":"May","value":4},
      {"text":"Jun","value":5},
      {"text":"Jul","value":6},
      {"text":"Aug","value":7},
      {"text":"Sep","value":8},
      {"text":"Oct","value":9},
      {"text":"Nov","value":10},
      {"text":"Dec","value":11}
   ]
   $scope.formData.month=$scope.monthList[0].value;
   $scope.formData.condition="year";
   // 组件列表
   $http.get('/cloudui/ws/resource/listResource?v=' + (new Date().getTime()),{
     params:
       {
         pageNum:1,
         pageSize:10000,
         registryId:3,
         resourceName:"",
         sortOrder:'DESC',
         sortName:''
       }
    }).then(function(res){
       if(res.data.rows != null && res.data.rows.length > 0){  

            for(var i = 0; i< res.data.rows.length; i++){  

                $("<option value='"+res.data.rows[i].resourceName+"'>"+res.data.rows[i].resourceName+"</option>").appendTo("#app"); 
               
            }  

            $("#app").chosen({
               no_results_text:"没有搜索到应用"
            });  
                      
        }
    })
    // 环境列表
    $http.get('/cloudui/master/ws/cluster'+'?v=' + (new Date().getTime()),{
        params:
        {
          pageNum:1,
          pageSize:10000,
          name:''
        }
    }).then(function(res){
     	if(res.data.rows != null && res.data.rows.length > 0){  

            for(var i = 0; i< res.data.rows.length; i++){  

                $("<option value='"+res.data.rows[i].name+"'>"+res.data.rows[i].name+"</option>").appendTo("#env"); 
               
            }  

            $("#env").chosen({
               no_results_text:"没有搜索到环境"
            });  
                      
        }
    })

    // 时间格式化
    function year(year) {
		start = new Date(year, 0, 1, 0, 0, 0)
		end = new Date(year + 1, 0, 1, 0, 0, 0)
		result = {}
		result.start = start.getTime()
		result.end = end.getTime()
		return result
	}
	function quarter(year, quarter) {
		map = {'one':0,'two':3,'three':6,'four':9};
		month = 0;
		if (1 == quarter) {month = map.one};
		if (2 == quarter) {month = map.two};
		if (3 == quarter) {month = map.three};
		if (4 == quarter) {month = map.four};
		start = new Date(year, month, 1, 0, 0, 0);
		if (4 == quarter) {year++};
		end = new Date(year, (month+3)%12, 1, 0, 0, 0);
		result = {};
		result.start = start.getTime();
		result.end = end.getTime();
		return result;
	}
	function monthes(year, month) {
		start = new Date(year, month, 1, 0, 0, 0);
		if (11 == month) {year++};
		end = new Date(year, (month+1)%12, 1, 0, 0, 0);
		result = {};
		result.start = start.getTime();
		result.end = end.getTime();
		return result;
	}



    // 保存配置
    $scope.configFn=function(){
             $scope.appnameList=[];
             for(var i=0;i<$('#app_chosen .search-choice').length;i++){
                 $scope.appnameList.push($('#app_chosen .search-choice')[i].textContent)
             }

             $scope.envList=[];
             for(var i=0;i<$('#env_chosen .search-choice').length;i++){
                 $scope.envList.push($('#env_chosen .search-choice')[i].textContent)
             }

           if($scope.appnameList.length!==$scope.envList.length){
	              Notify.alert(
	                  '应用和环境个数要保持一致，请重新选择',
	                  {status: 'info'}
	              ); 
	              return false;
           }

           // 查询条件：年
           if($scope.formData.condition=='year'){
               var timeRange=year($scope.formData.year)
           }else if($scope.formData.condition=='quarter'){
               var timeRange=quarter($scope.formData.year,$scope.formData.quarter)
           }else if($scope.formData.condition=='month'){
               var timeRange=monthes($scope.formData.year,$scope.formData.month)
           }
           var monitorConfig={};
           $scope.series=[];
           for(let i=0;i<$scope.appnameList.length;i++){
           	   $scope.series.push($scope.appnameList[i]+'_'+$scope.envList[i]);
           }
           monitorConfig.series=$scope.series;
           monitorConfig.intervalType=$scope.formData.condition;
           monitorConfig.start=timeRange.start;
           monitorConfig.end=timeRange.end;
           
           $cookieStore.put('monitorConfig',monitorConfig);

           ngDialog.close();
           $state.go('app.summary_manage',{},{reload:true});
    }
}]); 