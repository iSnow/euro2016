"use strict";


/*
 * This service implements a time-out on an inactive
 * session. Each navigation click resets it
 * TODO: refine this, it should also be reset on other
 * user interaction.
 */
angular.module('de.isnow.myApp').factory('sessionTimeout', ['userService', '$rootScope', function(userService, $rootScope) {
	var sessionTimeout = {};
	
	sessionTimeout.deathTimer = null;
	sessionTimeout.tickle = function () {
		sessionTimeout.setTimer();
	};
	
	sessionTimeout.setTimer = function() {
		if (sessionTimeout.deathTimer) {
			clearTimeout(sessionTimeout.deathTimer);
		}
		sessionTimeout.deathTimer = setTimeout(function () {
			userService.doLogOut();
			angular.module('de.isnow.myApp').destroyContext($rootScope);
			alert('You have been logged off due to inactivity');
			// TODO implement session termination
			// probably by navigating to /logout
		}, 2 * 60 * 60 * 1000);
	};

	return sessionTimeout;
}]);
