/**
 * Created by Tom on 28/02/2016.
 */

angular.module('SLATE.assignment')
	.factory('SLATE.assignment.requestHelper', ['$http', 'SLATE.config', function($http, config) {

		var requestHelper = {};


		requestHelper.getAssignment = function(assignmentNo){

			return $http({
				method: 'GET',
				url: config.API_URL + '/assignments/' + assignmentNo + '.json'
			})

		};

		return requestHelper;

	}]);
