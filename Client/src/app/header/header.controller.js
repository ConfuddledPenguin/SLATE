/**
 * Created by Tom on 19/01/2016.
 */

angular.module('SLATE.header')
	.controller('SLATE.header.controller', ['$rootScope', '$scope', 'SLATE.user.service','$state', function($rootScope, $scope, userService, $state){

		$scope.imageurl = userService.getUser().imageurl;

		$scope.back = function(){
			window.history.back();
		};

		$scope.toggleUserMenu = function(){

			if($rootScope.app.showMenu) $scope.toggleGlobalMenu();

			$scope.showUserMenu = !$scope.showUserMenu;

		};

		$scope.toggleGlobalMenu = function(){

			if($scope.showUserMenu) $scope.toggleUserMenu();

			$rootScope.app.showMenu = !$rootScope.app.showMenu;
		};

		$scope.profile = function(){
			$state.go('frame.user.profile', {username: $rootScope.app.user.username})
		};

		$scope.logout = function(){
			userService.logout();
		}

	}]);
