/**
 * Created by Tom on 27/01/2016.
 */

angular.module('SLATE.user')
	.controller('SLATE.user.login.controller', ['$scope', 'SLATE.user.service', function($scope, userService){

		$scope.user = {
			username: null,
			password: null,
			accept: null,
			email: null
		};

		$scope.login = function(isValid){

			if(!isValid){
				return;
			}

			userService.login($scope.user.username, $scope.user.password);

		};


	}]);