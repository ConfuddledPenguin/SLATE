/**
 * Created by Tom on 09/03/2016.
 */

angular.module('SLATE.statistics')
	.factory('SLATE.statistics.requestHelper', ['$http', 'SLATE.config', function($http, config){

		var requestHelper = {};

		requestHelper.getStatistics = function(stats){

			var string = '?stats=';
			stats.map(function(item){
				string += item + ','
			});

			return $http({
				method: 'GET',
				url: config.API_URL + '/statistics/fetch.json' + string
			})
		};

		return requestHelper;

	}]);