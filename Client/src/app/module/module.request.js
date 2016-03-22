/**
 * Created by Tom on 11/02/2016.
 */

angular.module('SLATE.modules')
	.factory('SLATE.module.requestHelper', ['$http', 'SLATE.config', function($http, config) {

		var requestHelper = {};


		requestHelper.getModule = function(year, classCode){

			return $http({
				method: 'GET',
				url: config.API_URL + '/modules/get/' + year + '/' + classCode + '.json'
			})
		};

		requestHelper.getModuleAdmin = function(classCode){
			return $http({
				method: 'GET',
				url: config.API_URL + '/modules/overview/' + classCode + '.json'
			})
		};

		requestHelper.getModules = function(searchText){

			return $http({
				method: 'GET',
				url: config.API_URL + '/modules/search/' + searchText + '.json'
			})
		};

		requestHelper.updateUserModuleGoals = function(year, classCode,attendance, attainment){

			return $http({
				method: 'POST',
				url: config.API_URL + '/modules/setGoals/' + year + '/' + classCode + '.json',
				data: {
					attendanceGoal: attendance,
					attainmentGoal: attainment
				}
			})
		};

		requestHelper.updateModuleGoals = function( classCode,attendance, attainment){

			return $http({
				method: 'POST',
				url: config.API_URL + '/modules/setGoals/' + classCode + '.json',
				data: {
					attendanceGoal: attendance,
					attainmentGoal: attainment
				}
			})
		};

		return requestHelper;

	}]);