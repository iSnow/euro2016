"use strict";


angular.module('de.isnow.myApp')
	.controller('TeamDetailsController', [
                'resourceService', '$log', 'NgTableParams', '$routeParams', 
          function(resourceService, $log, NgTableParams, $routeParams) {
   $log.info && $log.info('TeamDetailsController inited'); 
   
   var model = this;
          
   
   if ($routeParams.id) {
	   resourceService.teams({granularity : 'full', id: $routeParams.id}).get(
		   function(data) {
			   model.team = data;
			   resourceService.players({granularity : 'full', teamid: model.team.Country}).get(
				   function(data) {
					   model.tableParams = new NgTableParams({}, { dataset: data});
					   model.team.players = data;
				   }
			   );
		   }
	   );

   }
   
	
}]);
