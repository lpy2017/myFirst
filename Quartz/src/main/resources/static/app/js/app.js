// 定义初始模块
var appModule = angular.module('app', [
    'ngRoute',
    'ngAnimate',
    'ngStorage',
    'ngCookies',
    'pascalprecht.translate',
    'ui.bootstrap',
    'ui.router',
    'oc.lazyLoad',
    'cfp.loadingBar',
    'ngSanitize',
    'ngResource',
    'tmh.dynamicLocale',
    'ui.utils',
    'custom-directive',// 自定义指令
    'custom-server',// 自定义服务
    'quartz'// 定时任务
]);

//定义全局作用域
appModule.run(["$rootScope", "$state", "$stateParams",  '$window', '$templateCache','$location','$http',function ($rootScope, $state, $stateParams, $window, $templateCache,$location,$http) {

  $rootScope.$state = $state;
  $rootScope.$stateParams = $stateParams;
  $rootScope.$storage = $window.localStorage;
 
  
  $rootScope.app = {
    name: '神州数码运维平台',
    description: '神州数码运维平台',
    layout: {
      isShadow: false,
      isCollapsed: false,
      isDropdown:true
    },
    viewAnimation: 'ng-fadeInUp'
  };

   $rootScope.$on('$stateChangeSuccess',
      function(event, toState, toParams, fromState, fromParams) {
        $rootScope.curPath=$location.path();
        $rootScope.getTabClasses=function(param){
            if($rootScope.curPath.indexOf(param)==-1)
              {
                return '';
              }else{
                return 'active';
              }   
        }     
  });
   
  /*// 用户信息 
  $http.post('/cloudui/ws/user/userInfo').
  success(function(data){
      $rootScope.user = data;
      $rootScope.userImg='app/images/user.jpg'
  })*/
  
  // 注销用户
  $rootScope.loginout=function(){
      $http.get('/cloudui/ws/user/logout'+'?v='+(new Date().getTime()))
      .success(function(data){
           if(data=='true')
           {
             window.location='app/pages/login.html';
           }else
           {
             return false;
           }
      })
   }
  
}]);


// 定义常量
appModule
  .constant('APP_COLORS', {
    'primary':                '#5d9cec',
    'success':                '#27c24c',
    'info':                   '#23b7e5',
    'warning':                '#ff902b',
    'danger':                 '#f05050',
    'inverse':                '#131e26',
    'green':                  '#37bc9b',
    'pink':                   '#f532e5',
    'purple':                 '#7266ba',
    'dark':                   '#3a3f51',
    'yellow':                 '#fad732',
    'gray-darker':            '#232735',
    'gray-dark':              '#3a3f51',
    'gray':                   '#dde6e9',
    'gray-light':             '#e4eaec',
    'gray-lighter':           '#edf1f2'
  })
  .constant('APP_MEDIAQUERY', {
    'desktopLG':             1200,
    'desktop':                992,
    'tablet':                 768,
    'mobile':                 480
  })
  .constant('APP_REQUIRES', {
    scripts: {
      'icons': [ 'vendor/fontawesome/css/font-awesome.min.css',
                 'vendor/simple-line-icons/css/simple-line-icons.css' ],
      'loaders.css':['vendor/loaders.css/loaders.css'] ,
      'whirl':['vendor/whirl/dist/whirl.css']
    },
    modules: [
      {
        name: 'ngDialog',                  
        files: ['vendor/ngDialog/js/ngDialog.min.js',
                'vendor/ngDialog/css/ngDialog.min.css',
                'vendor/ngDialog/css/ngDialog-theme-default.min.css'] 
      }
    ]
  })
;

// 在路由切换时加载所需要资源
appModule.provider('RouteHelpers', ['APP_REQUIRES',function (appRequires) {

  this.basepath = function (uri) {
    return 'app/views/' + uri;
  };

  this.resolveFor = function () {
    var _args = arguments;
    return {
      deps: ['$ocLazyLoad','$q', function ($ocLL, $q) {
       
        var promise = $q.when(1);  

        for(var i=0, len=_args.length; i < len; i ++){
          promise = andThen(_args[i]);
        }
        return promise;

        
        function andThen(_arg) {
          
          if(typeof _arg == 'function')
              return promise.then(_arg);
          else
              return promise.then(function() {
                
                var whatToLoad = getRequired(_arg);
               
                if(!whatToLoad) return $.error('Route resolve: Bad resource name [' + _arg + ']');
                 
                return $ocLL.load( whatToLoad );
              });
        }
       
        function getRequired(name) {
          if (appRequires.modules)
              for(var m in appRequires.modules)
                  if(appRequires.modules[m].name && appRequires.modules[m].name === name)
                      return appRequires.modules[m];
          return appRequires.scripts && appRequires.scripts[name];
        }

      }]};
  }; // resolveFor

   
  this.$get = function(){
    return {
      basepath: this.basepath
    }
  };

}]);

//公用页面
appModule.controller('commonCtrl',
  ['$rootScope', '$scope', '$state', '$window', '$timeout', 'cfpLoadingBar','$location',
  function($rootScope, $scope, $state, $window, $timeout, cfpLoadingBar,$location) {
 
    var thBar;
    
    $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {

        if($('.wrapper > section').length)  
          thBar = $timeout(function() {
            cfpLoadingBar.start();
          }, 0);  
    });
     

    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
        event.targetScope.$watch("$viewContentLoaded", function () {
          $timeout.cancel(thBar);
          cfpLoadingBar.complete();
        });
    });

    $rootScope.$on('$stateChangeSuccess',
      function(event, toState, toParams, fromState, fromParams) {
   
        $rootScope.currTitle = $state.current.title;
        $rootScope.pageTitle();
    
    });

    $rootScope.currTitle = $state.current.title;

    $rootScope.pageTitle = function() {
      var title = $rootScope.app.name + ' - ' + ($rootScope.currTitle || $rootScope.app.description);
      document.title = title;
      return title;
    };  

}]);



appModule.service('Utils', ["$window", "APP_MEDIAQUERY", function($window, APP_MEDIAQUERY) {
    'use strict';
    
    var $html = angular.element("html"),
        $win  = angular.element($window),
        $body = angular.element('body');

    return {
      // DETECTION
      support: {
        transition: (function() {
                var transitionEnd = (function() {

                    var element = document.body || document.documentElement,
                        transEndEventNames = {
                            WebkitTransition: 'webkitTransitionEnd',
                            MozTransition: 'transitionend',
                            OTransition: 'oTransitionEnd otransitionend',
                            transition: 'transitionend'
                        }, name;

                    for (name in transEndEventNames) {
                        if (element.style[name] !== undefined) return transEndEventNames[name];
                    }
                }());

                return transitionEnd && { end: transitionEnd };
            })(),
        animation: (function() {

            var animationEnd = (function() {

                var element = document.body || document.documentElement,
                    animEndEventNames = {
                        WebkitAnimation: 'webkitAnimationEnd',
                        MozAnimation: 'animationend',
                        OAnimation: 'oAnimationEnd oanimationend',
                        animation: 'animationend'
                    }, name;

                for (name in animEndEventNames) {
                    if (element.style[name] !== undefined) return animEndEventNames[name];
                }
            }());

            return animationEnd && { end: animationEnd };
        })(),
        requestAnimationFrame: window.requestAnimationFrame ||
                               window.webkitRequestAnimationFrame ||
                               window.mozRequestAnimationFrame ||
                               window.msRequestAnimationFrame ||
                               window.oRequestAnimationFrame ||
                               function(callback){ window.setTimeout(callback, 1000/60); },
        touch: (
            ('ontouchstart' in window && navigator.userAgent.toLowerCase().match(/mobile|tablet/)) ||
            (window.DocumentTouch && document instanceof window.DocumentTouch)  ||
            (window.navigator['msPointerEnabled'] && window.navigator['msMaxTouchPoints'] > 0) || //IE 10
            (window.navigator['pointerEnabled'] && window.navigator['maxTouchPoints'] > 0) || //IE >=11
            false
        ),
        mutationobserver: (window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver || null)
      },
      // UTILITIES
      isInView: function(element, options) {

          var $element = $(element);

          if (!$element.is(':visible')) {
              return false;
          }

          var window_left = $win.scrollLeft(),
              window_top  = $win.scrollTop(),
              offset      = $element.offset(),
              left        = offset.left,
              top         = offset.top;

          options = $.extend({topoffset:0, leftoffset:0}, options);

          if (top + $element.height() >= window_top && top - options.topoffset <= window_top + $win.height() &&
              left + $element.width() >= window_left && left - options.leftoffset <= window_left + $win.width()) {
            return true;
          } else {
            return false;
          }
      },
      langdirection: $html.attr("dir") == "rtl" ? "right" : "left",
      isTouch: function () {
        return $html.hasClass('touch');
      },
      isSidebarCollapsed: function () {
        return $body.hasClass('aside-collapsed');
      },
      isSidebarToggled: function () {
        return $body.hasClass('aside-toggled');
      },
      isMobile: function () {
        return $win.width() < APP_MEDIAQUERY.tablet;
      }
    };
}]);

