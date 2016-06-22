"use strict";

/*
 * Our controller for creating and populating our modules
 */
angular.module('de.isnow.myApp').controller('MainController', 
		['$location', '$rootScope', 'resourceService', '$http', 'userService', 
		 'sessionTimeout', '$log', 
          function($location, $rootScope, resourceService, $http,
                   userService, sessionTimeout, $log) {

	var model = this;
	$log.info ('Instantiating de.isnow.myApp.');

	sessionTimeout.setTimer();

	model.userinfo = {};
	
	model.onViewChange = function(){
		sessionTimeout.tickle();
		var q = userService.stillLoggedIn();
		q.promise.then(
			function(data) {	
				console.log (data);
				if ((!data) || (!data.uid))
					$location.path('/login');
				else 
					model.userinfo = data;
			});
		
	};


	model.logIn = function(){
		model.userinfo = userService.doLogIn(model.realm, model.username, model.password).promise.then(
			// successfully logged in   
			function(data) {
				if (null == data.uid) {
					$location.path('/login');
				} else {
					model.userinfo = data;
					$location.path('/home');
				}
			},
			// failed to log in
			function(err) {
				alert("Maintenance, please try again in a couple of minutes");
				model.userinfo = undefined;
				$location.path('/login');
			}
		);
	};
	
	model.loggedIn = function(){
		return userService.loggedIn;
	};
	
	model.logOut = function(){
		userService.doLogOut();
		$location.path('/login');
	};

	resourceService.teams('short').all(
	   function(data) {
		   model.teams= data;
   });
	
	
	
	
	/*resourceService.settings().get(function (data){
		model.systemSettings = data;
		if (data) {
			if ((data.instance == "DEV") || (data.instance == "TEST")) {
				//set jersey tracing for $resource calls
				$http.defaults.headers.common['X-Jersey-Tracing-Accept']="ALL"; 
			}
		}
	});*/
}]);



