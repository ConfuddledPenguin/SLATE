/**
 * Created by Tom on 25/02/2016.
 */


angular.module('SLATE.user')
	.controller('SLATE.user.profile.controller', [ '$rootScope', '$scope', 'SLATE.user.service', '$stateParams', '$interval', '$state', '$timeout', 'toastr',
		function($rootScope, $scope, userService, $stateParams, $interval, $state, $timeout, toastr){

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

						$scope.profile.goals = {
							attainment: $scope.profile.user.attainmentGoal,
							attendance: $scope.profile.user.attendanceGoal
						};

						if(!$scope.$$phase) $scope.$apply();

						initGoalSliders();

						console.log($rootScope.app.user.enrolledModules);
					}

				}, 100);
			}

			$scope.navToModule = function(module){

				$state.go('frame.modules.moduleView', {year: module.year, classCode: module.classCode})

			};

			function initGoalSliders(){

				var value;
				var timeout;
				var firstTime = true;
				$scope.$watch('profile.goals', function(newVal, oldVal){

					if(firstTime){
						firstTime = false;
						return
					}

					value = newVal;

					$timeout.cancel(timeout);

					timeout = $timeout(function(){

						userService.updateGoals($scope.profile.goals.attendance, $scope.profile.goals.attainment)
							.then(function(data){

								if(data.data.successful === true){

									toastr.success('Successfully updated goals');

									$scope.profile.user.attainmentGoal = $scope.profile.goals.attainment;
									$scope.profile.user.attendanceGoal = $scope.profile.goals.attendance;

								}else{
									toastr.error(data.data.message, 'Error');
								}

							})
							.catch(function(data){

								toastr.error('Couldn\'t reach server sorry about that', 'Error');

							});

					}, 1500);

				}, true);

			}

			userProfileController();
	}]);