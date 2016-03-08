/**
 * Created by Tom on 25/02/2016.
 */


angular.module('SLATE.user')
	.controller('SLATE.user.profile.controller', [ '$rootScope', '$scope', 'SLATE.user.service', '$stateParams', '$interval', '$state',
		function($rootScope, $scope, userService, $stateParams, $interval, $state){

			$scope.profile = {
				meta: {
					own: false,
					role: null
				},
				user: null,
				moduleTabs: [],
				goals:{
					total: 0
				}
			};

			/*
			 * constructor. Waits in case this is the first page loaded. In that case we nee to wait for the user service
			 * load the user, so we loop until its loaded.
			 *
			 * //TODO replace with events if I need to do this again.
			 */
			function userProfileController(){

				var setup = $interval(function(){

					if(userService.getUserLoaded()){

						$interval.cancel(setup);

						if($stateParams.username === $rootScope.app.user.username){
							$scope.profile.meta.own = true;
						}

						$scope.profile.meta.role = $rootScope.app.user.role;

						var string = '';
						if($rootScope.app.user.name){
							string = $rootScope.app.user.name + '\'s profile';
						}else{
							string = $rootScope.app.user.username + '\'s profile';
						}

						$rootScope.app.stateTitle = string;

						$scope.profile.user = userService.getUser();

						if(!$scope.$$phase) $scope.$apply();

						console.log($rootScope.app.user.enrolledModules);
					}

				}, 100);
			}

			$scope.navToModule = function(module){

				$state.go('frame.modules.moduleView', {year: module.year, classCode: module.classCode})

			};

			userProfileController();
	}]);