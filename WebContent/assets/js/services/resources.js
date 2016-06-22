"use strict";


/*
 * Our resource router service. Fetches JSON data from a RESTful
 * web service. 
 * Different routes (services) for different entities
 */
angular.module('de.isnow.myApp').factory('resourceService', function ($resource, $http) {
	var resourceService = {};
	
	resourceService.userLogin = function(realm, username, password) {
		var args = {};
		args.realm = realm;
		args.username = username;
		args.password = password;
		return $resource('v1/user/login', {},
		{
			login:{method: 'POST', 
				isArray: false,
				params: {
					realm: args.realm,
					username: args.username,
					password: args.password
				},
				headers: {'Content-Type': 'application/x-www-form-urlencoded'}
			},
			info:{method: 'GET', 
				isArray: false, 
				url: 'v1/user/info'
			}
		});
	};

	resourceService.userLogout = function() {
		return $resource('v1/user/logout',
		{},
		{
			logout: {method: 'GET'}
		});
	};
	
	resourceService.settings = function(realm) {
		return realm ? $resource('v1/settings/'+realm) : $resource('v1/settings/system');
	};
	
	
	resourceService.teams = function(args) {
		args = args || {granularity: 'medium'};
		return $resource('', {},
			{
				get: {method: 'GET', 
					isArray: false, 
					url: 'v1/teams/details',
					params: {
						teamid: args.id,
						granularity : args.granularity
					}
				},
				all: {method: 'GET', 
					isArray: true, 
					url: 'v1/teams/all',
					params: {
						granularity : args.granularity
					}
				}
			}
		);
	};
	
	resourceService.players = function(args) {
		args = args || {granularity: 'medium'};
		return $resource('', {},
			{
				get: {method: 'GET', 
					isArray: true, 
					url: 'v1/teams/players',
					params: {
						teamid: args.teamid,
						granularity : args.granularity
					}
				}
			}
		);
	};
	

	
	return resourceService;
});