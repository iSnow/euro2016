"use strict";

/*
 * Our route provider. Fetches page templates for different targets
 * in our single-page application
 * 
 * Fetched content will be injected where a <div ng-view/> is
 * encountered
 */
angular.module('de.isnow.myApp').config(['$routeProvider', function($routeProvider) {
	var baseRoute = 'assets/views/';
	$routeProvider

	// default route
	.when('/', {
		templateUrl : baseRoute+'home.html'
	})
	
	.when('/login', {
		templateUrl : baseRoute+'login.html'
	})

	.when('/home', {
		templateUrl : baseRoute+'home.html'
	})

	// route for the teams list
	.when('/teams', {
		templateUrl : baseRoute+'teams.html'
	})
	
	
	
	// route for a team detail view
	.when('/team/:id/', {
		templateUrl : baseRoute+'teamdetails.html'
	})

	
	// route for the logout page
	.when('/logout', {
		templateUrl : baseRoute+'logout.html',
		controller  : 'ThanatosController'
	})

	.otherwise ({
		redirectTo: '/home'
	});
}]);