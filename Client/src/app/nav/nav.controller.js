/**
 * Created by Tom on 19/01/2016.
 */

angular.module('SLATE.nav')
	.controller('SLATE.nav.controller', ['$rootScope','$scope', function($rootScope, $scope){

		$scope.navigate = function(){
			$rootScope.app.showMenu = false;
		}

	}]);
