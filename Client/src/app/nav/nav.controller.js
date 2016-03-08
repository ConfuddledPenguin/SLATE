/**
 * Created by Tom on 19/01/2016.
 */

angular.module('SLATE.nav')
	.controller('SLATE.nav.controller', ['$rootScope','$scope', '$state', function($rootScope, $scope, $state){

		//constructor
		function navController(){

			$scope.nav = {
				showSubPartList: []
			};

			$scope.nav.showSubPartList[0] = true;
		}

		navController();

		$scope.navigate = function(){
			$rootScope.app.showMenu = false;
		};

		$scope.navigateModule = function(year, classCode){

			$state.go('frame.modules.moduleView', {year: year, classCode: classCode})
		};

		$scope.nav.showSubPart = function(key){

			if(!$scope.nav.showSubPartList[key]){
				//rather than reset everything just clear it
				$scope.nav.showSubPartList = [];

				$scope.nav.showSubPartList[key] = true;
			}else{
				$scope.nav.showSubPartList = [];

				if($scope.nav.showSubPartList[0] === false)
					$scope.nav.showSubPartList[0] = true;
			}

		};

		$scope.nav.adminModules = function(){

			$state.go('frame.modules.adminView');
		};
		$scope.nav.adminOverview = function(){

			$state.go('frame.dashboard');
		};
		$scope.nav.adminStudents = function(){

			alert('fucking work');
			$state.go('frame.modules.adminView');
		};
		$scope.nav.adminLecturers = function(){
			alert('fucking work');
			$state.go('frame.modules.adminView');
		};
	}]);
