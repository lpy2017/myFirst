/**
 * Created by tangbt on 2018/3/2.
 */
//咨询窗口控制器
appModule.controller('helpCenterCtrl',
    ['$rootScope', '$scope', '$state', '$window', '$timeout', 'cfpLoadingBar', '$interval',
        function ($rootScope, $scope, $state, $window, $timeout, cfpLoadingBarlog, $interval) {
            $scope.centerToggle = function () {
                $('#hpCenter').fadeToggle(300)
            }

            // console.log($rootScope.customMade)
        }]);