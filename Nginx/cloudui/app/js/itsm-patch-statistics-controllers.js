var itsmPatchStatisticsModule=angular.module('itsmPatchStatistics',[]);

itsmPatchStatisticsModule.controller('itsmPatchStatisticsCtrl',['$rootScope','$scope','$http','$state','$stateParams','$interval','$cookieStore','Notify',function($rootScope,$scope,$http,$state,$stateParams,$interval,$cookieStore,Notify){
	$scope.getList = function (resetPage) {
		if (resetPage) {
			$scope.pageSize = 10
			$scope.pageNum = 1
		}
		// $scope.list = [];
		$scope.daterange = $('.daterange').val();
		if ($scope.daterange) {
			$scope.beginTime = $scope.daterange.split(' - ')[0];
			$scope.endTime = $scope.daterange.split(' - ')[1];
		}
		$http.get('/cloudui/master/ws/ITSMPatchStatistics/list'+'?v='+(new Date().getTime()), {
			params: {
				patchName: $scope.patchName && $scope.patchName.name || '(全部)',
				opType: $scope.opType.opType || '(全部)',
				statusType: $scope.statusType.statusCode || '',
				beginTime: $scope.beginTime || '',
				endTime: $scope.endTime || '',
				pageSize: $scope.pageSize || 10,
				pageNum: $scope.pageNum || 1,
				componentId: $stateParams.componentId || '',
				sortName: $scope.sortName || 'updateTime',
				sortOrder: $scope.sortOrder || 'DESC'
			}
		}).then(function (response) {
			$scope.list = response.data.rows;
			$scope.totalPageNum = response.data.totalPageNum;
		});
	};
	$scope.fnSort = function(arg) {
		arguments.callee['fnSort' + arg] = !arguments.callee['fnSort' + arg];//false升序或true降序标识
		$scope.sortOrder = arguments.callee['fnSort' + arg]?'ASC':'DESC';
		$scope.sortName = arg;
		$scope.getList(true);
	};
	$scope.getPatchList = function () {
		$http.get('/cloudui/master/ws/ITSMPatchStatistics/listPatch/'+$stateParams.componentId+'?v='+(new Date().getTime())).then(function (response) {
			$scope.patchList = response.data;
			$scope.patchList.unshift({'name':'(全部)'});
			$scope.patchName = $scope.patchList[0];
		});
	};
	$scope.opTypeList = [{'opType':'(全部)'},{'opType':'新建'},{'opType':'发布'},{'opType':'更新状态'},{'opType':'追加描述'},{'opType':'回退'}];
	$scope.opType = $scope.opTypeList[0];
	$scope.statusTypeList = [
	{
		'statusType':'(全部)',
		'statusCode':""
	},{
		'statusType':'新建',
		'statusCode':"00"
	},{
		'statusType':'测试通过',
		'statusCode':"01"
	},{
		'statusType':'部分测试通过',
		'statusCode':"10"
	},{
		'statusType':'测试未通过',
		'statusCode':"11"
	// },{
	// 	'statusType':'不允许发布',
	// 	'statusCode':"20"
	// },{
	// 	'statusType':'允许发布',
	// 	'statusCode':"21"
	},{
		'statusType':'发布中...',
		'statusCode':"30"
	},{
		'statusType':'回退中...',
		'statusCode':"40"
	},{
		'statusType':'发布成功',
		'statusCode':"31"
	},{
		'statusType':'回退成功',
		'statusCode':"41"
	},{
		'statusType':'发布失败',
		'statusCode':"32"
	},{
		'statusType':'回退失败',
		'statusCode':"42"
	},{
		'statusType':'发布完成',
		'statusCode':"33"
	},{
		'statusType':'回退完成',
		'statusCode':"43"
	},{
		'statusType':'启动中...',
		'statusCode':"50"
	},{
		'statusType':'停止中...',
		'statusCode':"60"
	},{
		'statusType':'启动成功',
		'statusCode':"51"
	},{
		'statusType':'停止成功',
		'statusCode':"61"
	},{
		'statusType':'启动失败',
		'statusCode':"52"
	},{
		'statusType':'停止失败',
		'statusCode':"62"
	}];
	$scope.statusType = $scope.statusTypeList[0];
	$scope.getPatchList();
	// $scope.getList();
	$scope.pageNum = 1;
	$scope.list = [];
	$scope.checkappId=[];
	// 导出
	$scope.exportData=function(){
		// var checkbox = $scope.checkappId;
		// if(checkbox.length==0)
		// {
		// 	Notify.alert(
		// 		'请选择要导出的数据！' ,
		// 		{status: 'info'}
		// 		);
		// }else
		// {
		// 	window.location.href = '/cloudui/master/ws/ITSMPatchStatistics/export?ids='+checkbox;
		// }
		$scope.daterange = $('.daterange').val();
		if ($scope.daterange) {
			$scope.beginTime = $scope.daterange.split(' - ')[0];
			$scope.endTime = $scope.daterange.split(' - ')[1];
		}
		params = {
				patchName: $scope.patchName && $scope.patchName.name || '(全部)',
				opType: $scope.opType.opType || '(全部)',
				statusType: $scope.statusType.statusCode || '',
				beginTime: $scope.beginTime || '',
				endTime: $scope.endTime || '',
				componentId: $stateParams.componentId || '',
				sortName: $scope.sortName || 'updateTime',
				sortOrder: $scope.sortOrder || 'DESC'
			}
		url = '/cloudui/master/ws/ITSMPatchStatistics/exportAll'+'?v='+(new Date().getTime())
		for (item in params) {
			url = url + "&" + item + "=" + params[item]
		}

		window.location.href = url
	}
}]);