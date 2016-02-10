/**
 * Created by Tom on 26/01/2016.
 */

angular.module('SLATE.user')
	.factory('SLATE.user.requestHelper', ['$http', 'SLATE.config', function($http, config){

		var requestHelper = {};

		requestHelper.login = function(username, password){


			return $http({
				method: 'POST',
				url: config.API_URL + '/users/login.json',
				data: {
					username: username,
					password: password
				}
			})

		};

		requestHelper.getUser = function(username){


			return $http({
				method: 'GET',
				url: config.API_URL + '/users/' + username + '.json'
			})

		};

		return requestHelper;

	}]);