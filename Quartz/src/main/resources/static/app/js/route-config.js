 appModule.config(['$stateProvider', '$locationProvider', '$urlRouterProvider', 'RouteHelpersProvider',
function ($stateProvider, $locationProvider, $urlRouterProvider, helper) {
  
  $locationProvider.html5Mode(false);

  // 设置初始路由

  $urlRouterProvider.otherwise('/quartz');

  // 路由配置
    
  $stateProvider
    .state('app', {
        url: '/',
        abstract: true,
        cache:'false', 
        templateUrl: helper.basepath('common.html'+'?action='+(new Date().getTime())),
        controller: 'commonCtrl',
        resolve: helper.resolveFor('icons','ngDialog','whirl','loaders.css')
    }) 
    /* -------------------定时任务--------------- */
    .state('app.listQuartz', {
        url: 'quartz',
        title: '定时任务管理',
        cache:'false', 
        templateUrl: helper.basepath('quartz.html'+'?action='+(new Date().getTime())),
		controller:'quartzList'
    })
    .state('app.quartz_create', {
        url: 'quartz_create',
        cache:'false', 
        title: '定时任务添加',
        templateUrl: helper.basepath('quartz_create.html'+'?action='+(new Date().getTime())),
        controller:'quartzCreate'  
    })
    .state('app.quartz_info', {
        url: 'getQuartz/:quartzName/:quartzId',
        title: '定时任务详情',
        templateUrl: helper.basepath('quartz_info.html'+'?action='+(new Date().getTime())),
        controller:'quartzInfo'
    })
	.state('app.quartz_update', {
        url: 'updateQuartz/:quartzId',
        title: '定时任务更新',
        templateUrl: helper.basepath('quartz_update.html'+'?action='+(new Date().getTime())),
        controller:'quartzUpdate'
    })
    .state('app.quartz_jobs', {
        url: 'jobs/:quartzId',
        title: 'Jobs列表',
        templateUrl: helper.basepath('quartz_jobs.html'+'?action='+(new Date().getTime())),
        controller:'quartzJobs'
    })
    ;

}])
.config(['$ocLazyLoadProvider', 'APP_REQUIRES', function ($ocLazyLoadProvider, APP_REQUIRES) {
     
    $ocLazyLoadProvider.config({
      debug: false,
      events: true, 
      modules: APP_REQUIRES.modules
    });

}])

;




