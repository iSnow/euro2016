"use strict";


angular.module('de.isnow.myApp', 
		['ngRoute', 'ngResource', 'ngTable', 'ui.bootstrap',
		 'euro2016', 'de.isnow.filters']);

/**
 * ThanatosController
 * Is called from the logout page and takes care that the session is destroyed,
 * the application torn down and all data erased
 */
angular.module('de.isnow.myApp').controller('ThanatosController', 
		['$scope', '$rootScope', 'userService', '$log' , function($scope, $rootScope, userService, $log) {
	$log.info && $log.info ('This is Ripley, last survivor of the Nostromo, signing off.');
	$log.debug && $log.debug ($rootScope);
	
	userService.doLogOut();	
	angular.module('de.isnow.myApp').destroyContext($rootScope);
	
}]);


angular.module('de.isnow.myApp').destroyContext = function (rootScope) {
	/*
	 * Iterate through the child scopes and kill 'em all, because
	 * Angular 1.2 won't let us $destroy() the $rootScope
	 */
	var scope = rootScope.$$childHead;
	while (scope) {
		var nextScope = scope.$$nextSibling;
		scope.$destroy();
		scope = nextScope;
	}
	
	/*
	 * Iterate the properties of the $rootScope and delete any that possibly were set by us
	 * but leave the Angular-internal properties and functions intact so we can re-use the
	 * application.
	 */
	for(var prop in rootScope){
		if ((rootScope[prop]) && (prop.indexOf('$$') != 0) && (typeof(rootScope[prop]) === 'object')) {
			rootScope[prop] = null;
		}
	}
	
	/*
	 * $rootScope is empty now and contains no child scopes any more. 
	 * In the normal course of events tearing down the application like this is 
	 * overkill because the user can only navigate to the login-page via a 
	 * page reload. However, we don't want a logged out user to do funny stuff 
	 * via the back button.
	 * Additionally, remove display content from the DOM, so a casual observer
	 * cannot get to information (s)he is not supposed to see
	 */
	$('#maincontent').empty();
};


angular.module('de.isnow.myApp').config(['$httpProvider', function ($httpProvider) {
	  // Intercept POST requests, convert to standard form encoding
	  $httpProvider.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
	  $httpProvider.defaults.transformRequest.unshift(function (data, headersGetter) {
	    var key, result = [];

	    if (typeof data === "string")
	      return data;

	    for (key in data) {
	      if (data.hasOwnProperty(key))
	        result.push(encodeURIComponent(key) + "=" + encodeURIComponent(data[key]));
	    }
	    return result.join("&");
	  });
	}]);


