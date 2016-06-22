"use strict";

angular.module('de.isnow.myApp')
	.controller('TeamsController', [
                'resourceService', '$log', 'NgTableParams', 
          function(resourceService, $log, NgTableParams) {
   $log.info && $log.info('TeamsController inited'); 
   
   var model = this;
                	                      
   resourceService.teams({granularity: 'full'}).all(
	   function(data) {
		   model.teams = data;
		   model.tableParams = new NgTableParams({}, { dataset: data});
		   
		   data.forEach(function (d){
			   var team = d;
			   resourceService.players({teamid: team.Country, granularity: 'short'}).get(
				   function(players) {
					   team.players = players;
				   }
				);
		   });
	   }
   );
	
}]);
