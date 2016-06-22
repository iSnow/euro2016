"use strict";


angular.module('euro2016', [])
.directive('teambadge', function() {
    return {
        restrict: 'E',
        scope: {
            team: '=team'
        },
        templateUrl: 'assets/js/directives/teambadge.html',
        link: function(scope, element, attrs) {
           //console.log (scope.team);
        }
    }
})