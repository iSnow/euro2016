"use strict";


angular.module('de.isnow.myApp').factory('userService', ['resourceService', '$q', '$window', function(resourceService, $q, $window) {
	var userService = {};
	userService.loggedIn = false;
	userService.userdata = {};
	/*
	 * Create and return a promise that can be used by the calling function
	 * to get noticed about the fate of the login attempt.
	 * At the same time, intercept the higher-up promise to set the
	 * state of the user service accordingly 
	 * 
	 * the <em>j_realm</em> parameter is set on the login-page as a hidden parameter (currently, 
	 * it could be extended to be a HTML &lt;select&gt; control so the user can choose
	 * into which project she wants to log in). 
	 * On submit, j_realm parameter input's value is transferred to a cookie with the name 'j_realm'
	 * and this cookie in turn is read here in the Javascript <em>userService</em>.
	 * This here userService gets called from ng-header to retrieve information about
	 * the user from the backend. 
	 
	 * in UserService.java is the backend function that answers to this here userService call. Its 
	 * <em>realm</em> parameter comes from the cookie as described above.	
	 */
	userService.doLogIn = function(realm, username, password) {
		var deferred  = $q.defer();
		var realm = null;
		var c_name = 'j_realm';
		
		if (document.cookie.length > 0) {
	        var c_start = document.cookie.indexOf(c_name + "=");
	        if (c_start != -1) {
	            c_start = c_start + c_name.length + 1;
	            var c_end = document.cookie.indexOf(";", c_start);
	            if (c_end == -1) {
	                c_end = document.cookie.length;
	            }
	            realm = unescape(document.cookie.substring(c_start, c_end));
	        }
	    }
		
		resourceService.userLogin(realm, username, password).login().$promise.then(
			// successfully logged in	
			function(data) {				
				userService.loggedIn = true;
				deferred.resolve(data);
				userService.userdata = data;
			}, 
			// failed to log in
			function(err) {
				userService.loggedIn = false;
				userService.username = '';
				deferred.reject(err);
			}
		);
		
		return deferred;
	};
	
	userService.stillLoggedIn = function () {
		var deferred  = $q.defer();
		resourceService.userLogin().info().$promise.then(
			function(data) {				
				userService.loggedIn = (data.uid != false);
				deferred.resolve(data);
				userService.userdata = data;
			}, 
			// failed to log in
			function(err) {
				userService.loggedIn = false;
				userService.username = '';
				deferred.reject(err);
			}
		)

		return deferred;	
	}
	
	userService.doLogOut = function () {
		userService.loggedIn = false;
		userService.username = '';
		resourceService.userLogout().logout().$promise.then(
			function(data) {
				// OK logged out, do something
				//$window.location.reload();	//a page reload will trigger login modal again (if session invalidated)
			}, 
			function(err) {
				// FAIL, do something else
			}
		);
	};
	
	return userService;
}]);