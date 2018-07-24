var directiveMoudle=angular.module('custom-directive',[]);

/* -----------------------------------------分页------------------------------------- */
directiveMoudle.directive('page',function(){
    return {
        restrict:'E',
        templateUrl:'app/views/partials/page.html'+'?action='+(new Date().getTime()),
        scope:{
            onPageChange: '&',
            pageNum: '=',
            pageCount:'='
        }, 
        link:function(scope,element,attr)
        {
           scope.pageNum=1;
           scope.pageChange = function(page) {
              if (page >= 1 && page <= scope.pageCount) {
                scope.pageNum = page;
              } else {
                scope.pageNum = 1;
              }
           }
           
           scope.pageRefresh=function(pagenum){
        	   
        	   scope.pageNum=pagenum;
        	   if(!scope.pageNum)
        	   {
        		   scope.pageNum=1;
        	   }
        	   
        	   
        	   pageData()
           }
            

           function pageData()
           {
              scope.onPageChange();
           }
         
           scope.$watch('pageNum',function(newval,oldval){
             pageData()
           })

           
        }

    }
})

/* -----------------------------------------右侧边栏------------------------------------- */
directiveMoudle.directive('toggleState', ['toggleStateService', function(toggle) {
  'use strict';
  
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {

      var $body = $('body');

      $(element)
        .on('click', function (e) {
          e.preventDefault();
          var classname = attrs.toggleState;
          
          if(classname) {
            if( $body.hasClass(classname) ) {
              $body.removeClass(classname);
              if( ! attrs.noPersist)
                toggle.removeState(classname);
            }
            else {
              $body.addClass(classname);
              if( ! attrs.noPersist)
                toggle.addState(classname);
            }
            
          }

      });
    }
  };
  
}]);
 