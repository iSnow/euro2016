"use strict";


angular.module('de.isnow.myApp').factory('teamService', ['resourceService', '$q', function(resourceService, $q) {
	var that = this || {};
	that.teams = {};
	/*
	 * Create and return a promise that can be used by the calling function
	 * to get to the teams data.
	 */
	that.load = function() {
		var deferred  = $q.defer();
		
		resourceService.teams().get().$promise.then(
			// successfully loaded data
			function(data) {
				that.teams = angular.copy(data);
				deferred.resolve(data);
			}, 
			// some error
			function(err) {
				deferred.reject(err);
			}
		);
		
		return that;
	};
	return that;
}]);
