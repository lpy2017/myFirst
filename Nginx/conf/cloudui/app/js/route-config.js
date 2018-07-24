 appModule.config(['$stateProvider', '$locationProvider', '$urlRouterProvider', 'RouteHelpersProvider',
function ($stateProvider, $locationProvider, $urlRouterProvider, helper) {
  
  $locationProvider.html5Mode(false);

  // 设置初始路由

  $urlRouterProvider.otherwise('/summary_manage');

  // 路由配置

  $stateProvider
    .state('app', {
        url: '/',
        abstract: true,
        cache:false, 
        templateUrl: helper.basepath('common.html'+'?action='+(new Date().getTime())),
        controller: 'commonCtrl',
        resolve: helper.resolveFor('icons','cd-icons','ngDialog','whirl','loaders.css','slimscroll')
    })
    /* ---------------------------------概览-------------------------------- */
    .state('app.summary_manage', {
        url: 'summary_manage',
        title: '概览',
        cache:false,
        templateUrl: helper.basepath('summary-manage.html'+'?action='+(new Date().getTime())),
        resolve: helper.resolveFor('highchart','localytics.directives2'),
        controller: 'summaryManageCtrl'
    })
    /* ---------------------------------部署环境管理-------------------------------- */
    // 集群列表
    .state('app.cluster_manage', {
        url: 'cluster_manage',
        title: '部署环境管理',
        cache:false, 
        // abstract: true,
        templateUrl: helper.basepath('cluster-manage.html'+'?action='+(new Date().getTime())),
        controller:'clusterManageCtrl'
    })
    // 集群主机管理
    .state('app.cluster_detail', {
        url: 'cluster/:clusterid',
        title: '环境管理',
        cache:false,  
        abstract: true,
        templateUrl: helper.basepath('cluster-detail.html'+'?action='+(new Date().getTime()))
    })
    .state('app.cluster_detail.nodes', {
        url: '/nodes',
        title: '环境-主机列表',
        cache:false, 
        templateUrl: helper.basepath('cluster-nodes.html'+'?action='+(new Date().getTime())),
        controller:'nodesListCtrl'
    })
    .state('app.cluster_detail.blueprints', {
        url: '/blueprints',
        cache:false, 
        title: '环境-蓝图实例列表', 
        templateUrl: helper.basepath('cluster-blueprints.html'+'?action='+(new Date().getTime())),
        controller:'clusterBlueprintsCtrl'
    })
    .state('app.cluster_detail.components', {
        url: '/components',
        title: '环境-组件列表',
        cache:false, 
        templateUrl: helper.basepath('cluster-components.html'+'?action='+(new Date().getTime())),
        controller:'clusterComponentsCtrl'
    })
    // 添加主机
    .state('app.node_add', {
        url: 'cluster/:clustername/:clusterid/node_add',
        title: '添加主机',
        cache:false, 
        templateUrl: helper.basepath('node-add.html'+'?action='+(new Date().getTime())),
        controller:'addNodeCtrl'
    })
    // 编辑主机
    .state('app.node_edit', {
        url: 'cluster/:clusterid/:nodeid/node_edit',
        title: '更新主机',
        cache:false, 
        templateUrl: helper.basepath('node-edit.html'+'?action='+(new Date().getTime())),
        controller:'editNodeCtrl'
    })
    /* ---------------------------------蓝图模板管理-------------------------------- */
    .state('app.blueprint_manage', {
        url: 'blueprint_manage',
        title: '蓝图管理',
        cache:false,
        resolve: helper.resolveFor('gojs'),
        templateUrl: helper.basepath('blueprint-manage.html'+'?action='+(new Date().getTime())),
        controller:'blueprintManageCtrl'
    })
    .state('app.blueprint_add', {
        url: ':sys/blueprint_add',
        title: '添加蓝图',
        cache:false,
        resolve: helper.resolveFor('gojs','jquery-ui','blueprintDesigner'),
        templateUrl: helper.basepath('blueprint-add.html'+'?action='+(new Date().getTime()))/*,
        controller:'addBlueprintCtrl'*/
    })
    .state('app.blueprint_edit', {
        url: ':templateName/blueprint_edit',
        title: '编辑蓝图',
        cache:false,
        resolve: helper.resolveFor('gojs','jquery-ui','blueprintDesigner'),
        templateUrl: helper.basepath('blueprint-edit.html'+'?action='+(new Date().getTime()))/*,
        controller:'addBlueprintCtrl'*/
    })
    .state('app.blueprint_flow_create', {
        url: ':templateName/:blueprintId/blueprint_flow_create',
        title: '蓝图添加过程',
        cache:false, 
        resolve: helper.resolveFor('gojs','jquery-ui','flowDesigner'),
        templateUrl: helper.basepath('blueprint-flow-create.html'+'?action='+(new Date().getTime()))
    })
    .state('app.blueprint_flow_manage', {
        url: ':templateName/:blueprintId/blueprint_flow_manage',
        title: '蓝图过程管理',
        cache:false, 
        controller:'blueprintFlowManageCtrl',
        templateUrl: helper.basepath('blueprint-flow-manage.html'+'?action='+(new Date().getTime()))
    })
    .state('app.blueprint_flow_update', {
        url: ':templateName/:blueprintId/:flowId/blueprint_flow_update',
        title: '蓝图过程更新',
        cache:false, 
        resolve: helper.resolveFor('gojs','jquery-ui','flowDesigner'),
        templateUrl: helper.basepath('blueprint-flow-update.html'+'?action='+(new Date().getTime()))
    })
    .state('app.blueprint_ins_add', {
        url: ':templateName/:blueprintId/blueprint_ins_add',
        title: '创建蓝图实例',
        cache:false,
        resolve: helper.resolveFor('gojs','jquery-ui','blueprintDesigner'),
        templateUrl: helper.basepath('blueprint-ins-add.html'+'?action='+(new Date().getTime())),
        controller:'addBlueprintInsCtrl'
    })
	.state('app.blueprint_ins_clone', {
        url: ':insCloneId/:insCloneName/blueprint_ins_clone',
        title: '克隆蓝图实例',
        cache:false,
        resolve: helper.resolveFor('gojs','jquery-ui','blueprintDesigner'),
        templateUrl: helper.basepath('blueprint-ins-clone.html'+'?action='+(new Date().getTime())),
        controller:'cloneBlueprintInsCtrl'
    })
    .state('app.blueprint_view', {
        url: ':templateName/blueprint_view',
        title: '查看蓝图模板',
        cache:false,
        resolve: helper.resolveFor('gojs','jquery-ui','blueprintDesigner'),
        templateUrl: helper.basepath('blueprint-view.html'+'?action='+(new Date().getTime()))
    })
    /* ---------------------------------蓝图实例管理-------------------------------- */
    .state('app.blueprint_ins_manage', {
        url: 'blueprint_ins_manage',
        title: '蓝图实例管理',
        cache:false,
        templateUrl: helper.basepath('blueprint-ins-manage.html'+'?action='+(new Date().getTime())),
        controller:'blueprintInsManageCtrl'
    })
    .state('app.blueprint_ins_view', {
        url: ':insId/blueprint_ins_view',
        title: '查看蓝图实例模板',
        cache:false,
        resolve: helper.resolveFor('gojs','jquery-ui','blueprintDesigner'),
        templateUrl: helper.basepath('blueprint-ins-view.html'+'?action='+(new Date().getTime()))
    })
    .state('app.blueprint_ins_snapshoot', {
        url: ':insName/:INSTANCE_ID/blueprint_ins_snapshoot',
        title: '蓝图实例快照管理',
        cache:false,
        templateUrl: helper.basepath('blueprint-ins-snapshoot.html'+'?action='+(new Date().getTime())),
        controller:'blueprintInsSnapshoot'
    })
    .state('app.blueprint_ins_flow_view', {
        url: ':blueprintInsId/:templateName/:blueprintId/:insName/:flowName/:flowId/blueprint_ins_flow_view',
        title: '查看蓝图实例过程',
        cache:false,
        resolve: helper.resolveFor('gojs','jquery-ui','flowDesigner'),
        templateUrl: helper.basepath('blueprint-ins-flow-view.html'+'?action='+(new Date().getTime()))
    })
    .state('app.blueprint_ins_log_manage', {
        url: 'blueprint_ins/:insName/:INSTANCE_ID/:flowName/log_manage',
        title: '蓝图实例日志管理',
        cache:false,
        templateUrl: helper.basepath('blueprint-ins-log-manage.html'+'?action='+(new Date().getTime())),
        resolve: helper.resolveFor('spinkit'),
        controller:'blueprintInsLogManageCtrl'
    })
    .state('app.blueprint_ins_resource_configs', {
        url: 'blueprint_ins/:insName/:insId/:INSTANCE_ID/resource_configs',
        title: '蓝图实例资源配置管理',
        cache:false,
        templateUrl: helper.basepath('blueprint-ins-resource-configs.html'+'?action='+(new Date().getTime())),
        controller:'blueprintInsResourceConfigsManageCtrl'
    })
    .state('app.blueprint_ins_component_configs', {
        url: 'blueprint_ins/:insName/:INSTANCE_ID/component_configs',
        title: '蓝图实例组件配置管理',
        cache:false,
        templateUrl: helper.basepath('blueprint-ins-component-configs.html'+'?action='+(new Date().getTime())),
        controller:'blueprintInsComponentConfigsManageCtrl'
    })
    .state('app.blueprint_ins_configs_compare', {
        url: 'blueprint_ins/:insName/:Ori_INSTANCE_ID/:Ori_insId/:compare_insName/:Compare_INSTANCE_ID/:Compare_insId/configs_compare',
        title: '蓝图实例配置比对',
        cache:false,
        templateUrl: helper.basepath('blueprint_ins_configs_compare.html'+'?action='+(new Date().getTime())),
        controller:'blueprintInsConfigsCompare'
    })
    .state('app.blueprint_ins_flow_monitor', {
        url: 'blueprint_ins/:insName/:INSTANCE_ID/:flowMonitorInsId/:flowName/:flowId/flow_monitor',
        title: '蓝图监控',
        cache:false,
        resolve: helper.resolveFor('gojs','jquery-ui','flowDesigner','angularBootstrapNavTree3','codemirror', 'ui.codemirror', 'codemirror-modes-web'),
        templateUrl: helper.basepath('blueprint-ins-flow-monitor.html'+'?action='+(new Date().getTime())),
        controller:'blueprintInsFlowMonitorCtrl'
    })
    .state('app.blueprint_ins_components', {
        url: 'blueprint_ins/:insName/:insId/:INSTANCE_ID/components',
        title: '蓝图实例组件',
        cache:false,
        templateUrl: helper.basepath('blueprint-ins-components.html'+'?action='+(new Date().getTime())),
        controller:'blueprintInsComponentsCtrl'
    })
    .state('app.blueprint_ins_components_log_manage', {
        url: 'blueprint_ins/:insName/:insId/:INSTANCE_ID/:appName/:flowName/log_manage',
        title: '蓝图实例组件日志管理',
        cache:false,
        resolve: helper.resolveFor('spinkit'),
        templateUrl: helper.basepath('blueprint-ins-components-log-manage.html'+'?action='+(new Date().getTime())),
        controller:'componentsLogManageCtrl'
    })
    .state('app.blueprint_ins_component_flow_monitor', {
        url: 'blueprint_ins/:insName/:insId/:INSTANCE_ID/:appName/:flowMonitorInsId/:flowName/:flowId/flow_monitor',
        title: '组件监控',
        cache:false,
        resolve: helper.resolveFor('gojs','jquery-ui','flowDesigner','angularBootstrapNavTree3','codemirror', 'ui.codemirror', 'codemirror-modes-web'),
        templateUrl: helper.basepath('blueprint-ins-component-flow-monitor.html'+'?action='+(new Date().getTime())),
        controller:'blueprintInsFlowMonitorCtrl'
    })
    .state('app.blueprint_ins_component_instance', {
        url: 'blueprint_ins/:insName/:insId/:componentName/:componentId/instance',
        title: '实例',
        cache:false, 
        controller:"blueprintInsComponentDetailInstanceCtrl",
        templateUrl: helper.basepath('blueprint-ins-component-instance.html'+'?action='+(new Date().getTime())),
    })
    /* ---------------------------------组件管理-------------------------------- */
    .state('app.component_manage', {
        url: 'component_manage',
        title: '通用组件管理',
        cache:false, 
        templateUrl: helper.basepath('component-manage.html'+'?action='+(new Date().getTime())),
        controller:'componentManageCtrl',
        resolve: helper.resolveFor('localytics.directives')
    }) 
    .state('app.component_detail', {
        url: ':componentName/:resourceId/component_detail',
        title: '通用组件详情',
        cache:false, 
        controller:'componentDetailCtrl',
        templateUrl: helper.basepath('component-detail.html'+'?action='+(new Date().getTime())),
        resolve: helper.resolveFor('localytics.directives')
    })
    .state('app.component_flow_create', {
        url: ':componentName/:componentId/component_flow_create',
        title: '组件添加过程',
        cache:false, 
        resolve: helper.resolveFor('gojs','jquery-ui','flowDesigner'),
        templateUrl: helper.basepath('component-flow-create.html'+'?action='+(new Date().getTime()))
    })
    .state('app.component_flow_manage', {
        url: ':componentName/:componentId/component_flow_manage',
        title: '组件过程管理',
        cache:false, 
        controller:'componentFlowManageCtrl',
        templateUrl: helper.basepath('component-flow-manage.html'+'?action='+(new Date().getTime()))
    })
    .state('app.component_flow_update', {
        url: ':componentName/:componentId/:flowType/:flowId/:flowName/component_flow_update',
        title: '组件过程更新',
        cache:false, 
        resolve: helper.resolveFor('gojs','jquery-ui','flowDesigner'),
        controller:'componentFlowUpdateCtrl',
        templateUrl: helper.basepath('component-flow-update.html'+'?action='+(new Date().getTime()))
    })
    .state('app.component_version_add', {
        url: ':componentName/:componentId/component_version_add',
        title: '组件添加版本',
        cache:false, 
        templateUrl: helper.basepath('component-version-add.html'+'?action='+(new Date().getTime())),
        resolve: helper.resolveFor('angularFileUpload','filestyle'),
        controller:'componentVersionAddCtrl',
    }) 
    .state('app.component_version_update', {
        url: ':componentName/:versionId/component_version_update',
        title: '组件版本更新',
        cache:false, 
        templateUrl: helper.basepath('component-version-update.html'+'?action='+(new Date().getTime())),
        resolve: helper.resolveFor('angularFileUpload','filestyle'),
        controller:'componentVersionUpdateCtrl',
    })
    .state('app.component_version_clone', {
        url: ':componentName/:versionId/component_version_update',
        title: '组件版本克隆',
        cache:false, 
        templateUrl: helper.basepath('component-version-clone.html'+'?action='+(new Date().getTime())),
        resolve: helper.resolveFor('angularFileUpload','filestyle'),
        controller:'componentVersionCloneCtrl',
    })
    .state('app.component_version_template_mapping', {
        url: ':componentName/:componentId/:versionName/:versionId/component_version_template_mapping',
        title: '组件版本模板映射',
        cache:false, 
        resolve: helper.resolveFor('angularBootstrapNavTree2'),
        templateUrl: helper.basepath('component-version-template-mapping.html'+'?action='+(new Date().getTime())),
        controller:'componentVersionTempMappingCtrl',
    })
    /* ---------------------------------插件管理-------------------------------- */
    .state('app.plugin_manage', {
        url: 'plugin_manage',
        cache:false, 
        title: '插件管理',
        templateUrl: helper.basepath('plugin-manage.html'+'?action='+(new Date().getTime())),
        controller:'pluginManageCtrl',
        resolve: helper.resolveFor('localytics.directives')
    })
    .state('app.plugin_add', {
        url: 'plugin_add',
        cache:false, 
        title: '插件添加',
        templateUrl: helper.basepath('plugin-add.html'+'?action='+(new Date().getTime())),
        resolve: helper.resolveFor('angularFileUpload','filestyle','localytics.directives'),
        controller:'addPluginCtrl'
    })
    .state('app.plugin_detail', {
        url: 'plugin/:pluginName',
        title: '插件详情',
        cache:false, 
        templateUrl: helper.basepath('plugin-detail.html'+'?action='+(new Date().getTime())),
        controller:'pluginDetailCtrl'
    })
    .state('app.plugin_update', {
        url: 'plugin_update/:pluginName',
        cache:false, 
        title: '插件更新',
        templateUrl: helper.basepath('plugin-update.html'+'?action='+(new Date().getTime())),
        controller:'pluginUpdateCtrl'
    })
    /* ---------------------------------脚本管理-------------------------------- */
    .state('app.script_manage', {
        url: 'script_manage',
        cache:false, 
        title: '脚本库管理',
        templateUrl: helper.basepath('script-manage.html'+'?action='+(new Date().getTime())),
        controller:'scriptManageCtrl'
    })
    .state('app.script_add', {
        url: 'script_add',
        cache:false, 
        title: '插件脚本添加',
        templateUrl: helper.basepath('script-add.html'+'?action='+(new Date().getTime())),
        resolve: helper.resolveFor('angularFileUpload','filestyle'),
        controller:'addScriptCtrl'  
    })
    /* ---------------------------------工件包管理-------------------------------- */
    .state('app.workpiece_package_manage', {
        url: 'workpiece_package_manage',
        cache:false, 
        title: '工件包管理',
        templateUrl: helper.basepath('workpiece-package-manage.html'+'?action='+(new Date().getTime())),
        controller:'workpiecePackageManageCtrl'
    })
    .state('app.workpiece_package_add', {
        url: 'workpiece_package_add',
        cache:false, 
        title: '工件包添加',
        templateUrl: helper.basepath('workpiece-package-add.html'+'?action='+(new Date().getTime())),
        resolve: helper.resolveFor('angularFileUpload','filestyle'),
        controller:'addWorkpiecePackageCtrl'  
    })
    /* ---------------------------------操作统计-------------------------------- */
    .state('app.statistics_manage', {
        url: 'statistics_manage',
        cache:false,  
        title: '仪表盘',
        resolve: helper.resolveFor('moment','daterangepicker'),
        templateUrl: helper.basepath('statistics-manage.html'+'?action='+(new Date().getTime())),
        controller:'recordStatisticsCtrl'  
    })
    /* ---------------------------------权限管理-------------------------------- */
    // 权限管理  
    .state('app.limit_manage', {
        url: 'limit_manage',
        title: '权限管理',
        cache:false, 
        abstract: true,
        templateUrl: helper.basepath('limit-manage.html'+'?action='+(new Date().getTime()))
    })
    .state('app.limit_manage.user', {
        url: '/user',
        title: '用户管理',
        cache:false, 
        templateUrl: helper.basepath('limit-user.html'+'?action='+(new Date().getTime())),
        controller:'limitUserCtrl'
    })
    .state('app.limit_manage.role', {
    	url: '/role',
    	cache:false, 
        title: '角色管理', 
        templateUrl: helper.basepath('limit-role.html'+'?action='+(new Date().getTime())),
        resolve: helper.resolveFor('angularBootstrapNavTree'),
        controller:'limitRoleCtrl'
    })
    .state('app.limit_manage.menu', {
        url: '/menu',
        title: '菜单管理',
        cache:false, 
        templateUrl: helper.basepath('limit-menu.html'+'?action='+(new Date().getTime())),
        controller:'limitMenuCtrl'
    })
    /* ---------------------------------定制管理-------------------------------- */
    .state('app.custom_manage', {
        url: 'custom_manage',
        cache:false,  
        title: '定制管理',
        templateUrl: helper.basepath('custom-manage.html'+'?action='+(new Date().getTime())),
        resolve: helper.resolveFor('angularFileUpload','filestyle'),
        controller:'customManage'  
    })
    /*----------------------------------标签管理---------------------------------*/
    .state('app.label_manage', {
        url: 'label_manage',
        cache:false,  
        title: '标签管理',
        templateUrl: helper.basepath('label-manage.html'+'?action='+(new Date().getTime())),
        controller:'labelManageCtrl'
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
        url: ':quartzName/:quartzId',
        title: 'Jobs列表',
        templateUrl: helper.basepath('quartz_jobs.html'+'?action='+(new Date().getTime())),
        controller:'quartzJobs'
    })
     /*----------------------------------操作审计---------------------------------*/
    .state('app.operational', {
        url: 'operational',
        cache:false,  
        title: '操作审计',
        resolve: helper.resolveFor('moment','daterangepicker'),
        templateUrl: helper.basepath('operational.html'+'?action='+(new Date().getTime())),
        controller:'operationalManage'
    })
    .state('app.itsmPatchStatistics', {
        url: 'itsm/patch/statistics/:componentId',
        cache:false,  
        title: '补丁操作统计',
        resolve: helper.resolveFor('moment','daterangepicker'),
        templateUrl: helper.basepath('itsm-patch-statistics.html'+'?action='+(new Date().getTime())),
        controller:'itsmPatchStatisticsCtrl'
    })
    /* ---------------------------------发布任务管理-------------------------------- */
    // 发布任务列表
    .state('app.release_manage', {
        url: 'release_manage/bus/:busId',
        cache:false,  
        title: '发布任务管理',
        resolve: helper.resolveFor('moment','daterangepicker'),
        templateUrl: helper.basepath('release-manage.html'+'?action='+(new Date().getTime())),
        controller:'releaseManageCtrl'
    })
    // 添加发布任务
    .state('app.release_add', {
        url: 'release_add',
        cache:false,  
        title: '添加发布任务',
        resolve: helper.resolveFor('moment','daterangepicker'),
        templateUrl: helper.basepath('release-add.html'+'?action='+(new Date().getTime())),
        controller:'releaseAddCtrl'
    })
    // 编辑发布任务
    .state('app.release_edit', {
        url: 'release/:id/edit',
        cache:false,  
        title: '编辑发布任务',
        resolve: helper.resolveFor('moment','daterangepicker'),
        templateUrl: helper.basepath('release-edit.html'+'?action='+(new Date().getTime())),
        controller:'releaseEditCtrl'
    })
    // 发布任务详情
    .state('app.release_detail', {
        url: 'release/:id/detail',
        cache:false,  
        title: '发布任务详情',
        templateUrl: helper.basepath('release-detail.html'+'?action='+(new Date().getTime())),
        controller:'releaseDetailCtrl'
    })
    // 班车列表
    .state('app.bus_manage', {
        url: 'bus_manage',
        cache:false,  
        title: '发布班车管理',
        templateUrl: helper.basepath('bus-manage.html'+'?action='+(new Date().getTime())),
        controller:'busManageCtrl'
    })
    // 添加班车
    .state('app.bus_add', {
        url: 'bus_add',
        cache:false,  
        title: '添加班车',
        resolve: helper.resolveFor('moment','daterangepicker'),
        templateUrl: helper.basepath('bus-add.html'+'?action='+(new Date().getTime())),
        controller:'busAddCtrl'
    })
    // 修改班车
    .state('app.bus_edit', {
        url: 'bus/:id/edit',
        cache:false,  
        title: '修改班车',
        resolve: helper.resolveFor('moment','daterangepicker'),
        templateUrl: helper.basepath('bus-edit.html'+'?action='+(new Date().getTime())),
        controller:'busEditCtrl'
    })
    // 班车甘特图
    .state('app.bus_gantt', {
        url: 'bus/:id/gantt',
        cache:false,  
        title: '班车甘特图',
        templateUrl: helper.basepath('jQueryGantt-master/newindex.html'+'?action='+(new Date().getTime())),
        controller:'busGanttCtrl'
    })
    /* ---------------------------------审批管理-------------------------------- */
     .state('app.myApprove', {
        url: 'myApprove',
        title: '我的申请',
        cache:false,
        resolve: helper.resolveFor('gojs'),
        templateUrl: helper.basepath('approve-manage.html'+'?action='+(new Date().getTime())),
        controller:'approveManageCtrl'
    })
      /*----------------------------------大屏展示---------------------------------*/
   /* .state('app.screen', {
        url:'screen',
        cache:false,  
        title: '大屏展示',
        templateUrl: helper.basepath('screen-manage.html'+'?action='+(new Date().getTime())),
        controller:'screenManage'
    })*/
    /* ---------------------------------发布管理-------------------------------- */
     .state('app.release_ins', {
        url: 'release_ins',
        title: '发布',
        cache:false,
        resolve: helper.resolveFor('gojs'),
        templateUrl: helper.basepath('release-ins.html'+'?action='+(new Date().getTime())),
        controller:'releaseInsCtrl'
    }) 
    // 发布详情
    .state('app.release_ins_detail', {
        url: 'release_ins/:id/detail',
        cache:false,  
        title: '发布详情',
        templateUrl: helper.basepath('release-ins-detail.html'+'?action='+(new Date().getTime())),
        controller:'releaseInsDetailCtrl'
    })
    // 发布系统 20180611
    /* ---------------------------------生命周期管理-------------------------------- */
    .state('app.lifecycles', {
        url: 'lifecycles',
        title: '生命周期管理',
        cache:false,
        templateUrl: helper.basepath('lifecycles-manage.html'+'?action='+(new Date().getTime())),
        controller:'lifecyclesManageCtrl'
    })
    .state('app.lifecycle_add', {
        url: 'lifecycles',
        title: '添加生命周期',
        cache:false,
        templateUrl: helper.basepath('lifecycle_add.html'+'?action='+(new Date().getTime())),
        controller:'lifecycleAddCtrl'
    })
    /* ---------------------------------任务管理-------------------------------- */
    // 任务列表
    .state('app.release_task_manage', {
        url: 'release_task_manage',
        cache:false,  
        title: '任务管理',
        resolve: helper.resolveFor('moment','daterangepicker'),
        templateUrl: helper.basepath('release-task-manage.html'+'?action='+(new Date().getTime())),
        controller:'releaseTaskManageCtrl'
    })
    // 添加任务
    .state('app.release_task_add', {
        url: 'release_task_add',
        cache:false,  
        title: '添加任务',
        resolve: helper.resolveFor('moment','daterangepicker','localytics.directives','angularFileUpload','filestyle'),
        templateUrl: helper.basepath('release-task-add.html'+'?action='+(new Date().getTime())),
        controller:'releaseTaskAddCtrl'
    })
    // 编辑任务
    .state('app.release_task_edit', {
        url: 'releaseTask/:id/edit',
        cache:false,  
        title: '编辑任务',
        resolve: helper.resolveFor('moment','daterangepicker','localytics.directives','angularFileUpload','filestyle'),
        templateUrl: helper.basepath('release-task-edit.html'+'?action='+(new Date().getTime())),
        controller:'releaseTaskEditCtrl'
    })
    // 任务详情
    .state('app.release_task_detail', {
        url: 'releaseTask/:id/detail',
        cache:false,  
        title: '任务详情',
        templateUrl: helper.basepath('release-task-detail.html'+'?action='+(new Date().getTime())),
        controller:'releaseTaskDetailCtrl'
    })
    /* ---------------------------------发布策略-------------------------------- */
    // 发布策略列表
    .state('app.release_strategy_manage', {
        url: 'release_strategy_manage',
        cache:false,  
        title: '发布策略管理',
        templateUrl: helper.basepath('release-strategy-manage.html'+'?action='+(new Date().getTime())),
        controller:'releaseStrategyManageCtrl'
    })
    /* ---------------------------------同步管理平台-------------------------------- */
    // 集成列表
    .state('app.release_integration_manage', {
        url: 'release_integration_manage',
        cache:false,  
        title: '集成管理',
        templateUrl: helper.basepath('release-integration-manage.html'+'?action='+(new Date().getTime())),
        controller:'releaseIntegrationManageCtrl'
    })
    // 集成详情
    .state('app.release_integration_detail', {
        url: 'release_integration/:id/detail',
        cache:false,  
        title: '集成详情',
        templateUrl: helper.basepath('release-integration-detail.html'+'?action='+(new Date().getTime())),
        controller:'releaseIntegrationDetailCtrl'
    })
    /*
    // 修改清单
    .state('app.release_integration_edit', {
        url: 'releaseIntegration/:id/edit',
        cache:false,  
        title: '修改集成',
        resolve: helper.resolveFor('moment','daterangepicker'),
        templateUrl: helper.basepath('release_integration-edit.html'+'?action='+(new Date().getTime())),
        controller:'releaseIntegrationEditCtrl'
    })
    // 集成详情
    .state('app.release_integration_detail', {
        url: 'releaseIntegration/:id/detail',
        cache:false,  
        title: '集成详情',
        templateUrl: helper.basepath('release-integration-detail.html'+'?action='+(new Date().getTime())),
        controller:'releaseIntegrationDetailCtrl'
    })
    // 发布清单列表
    .state('app.release_list_manage', {
        url: 'release_list_manage',
        cache:false,  
        title: '发布清单管理',
        templateUrl: helper.basepath('release-list-manage.html'+'?action='+(new Date().getTime())),
        controller:'releaseListManageCtrl'
    })
    // 添加发布清单
    .state('app.release_list_add', {
        url: 'release_list_add',
        cache:false,  
        title: '添加清单',
        resolve: helper.resolveFor('moment','daterangepicker'),
        templateUrl: helper.basepath('release-list-add.html'+'?action='+(new Date().getTime())),
        controller:'releaseListAddCtrl'
    })
    // 修改清单
    .state('app.release_list_edit', {
        url: 'releaseList/:id/edit',
        cache:false,  
        title: '修改清单',
        resolve: helper.resolveFor('moment','daterangepicker'),
        templateUrl: helper.basepath('release-list-edit.html'+'?action='+(new Date().getTime())),
        controller:'releaseListEditCtrl'
    })
    */
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




