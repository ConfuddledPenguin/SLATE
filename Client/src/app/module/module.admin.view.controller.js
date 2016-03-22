/**
 * Created by Tom on 04/03/2016.
 */

angular.module('SLATE.modules')
	.controller('SLATE.module.admin.view.controller', ['$scope', '$stateParams', '$state', 'toastr', 'SLATE.module.requestHelper', 'SLATE.statistics.requestHelper', '$interval', 'SLATE.config', '$timeout',
		function($scope, $stateParams, $state, toastr, moduleRequestHelper, statsRequestHelper, $interval, config, $timeout){

			function modulesAdminViewController(){

				$scope.module = {
					data: null,
					stats: null
				};

				$scope.redrawSliders = function(){

					$timeout(function(){
						$scope.$broadcast('reCalcViewDimensions');
					});
				};

				fetchModule();
				fetchStats();
			}

			function fetchModule(){

				moduleRequestHelper.getModuleAdmin($stateParams.classCode)
					.then(function(data){

						if(data.data.successful === true){

							var response = data.data;

							$scope.module.data = response.result;

							$scope.module.goals = {
								attainment: $scope.module.data.attainmentGoal,
								attendance: $scope.module.data.attendanceGoal
							};

							initUserGoalSliders();

							var years = $scope.module.data.years;

							$scope.module.data.yearsAsArray = Object.keys(years).map(function(key){
								var year = years[key];

								year.yearOriginal = year.year;
								year.year = year.year + "/" + (parseInt(year.year) + 1);

								return year;
							});

						}else{
							toastr.error(data.data.message, 'Error');
						}

					})
					.catch(function(data){

						toastr.error('Couldn\'t reach server sorry about that', 'Error');

					});

			}

			function fetchStats(){

				var stats = ['PASS_RATE','PASSMARK_MEAN','ATTENDANCE_MEAN'];

				statsRequestHelper.getStatistics(stats)
					.then(function(data){

						if(data.data.successful === true){

							var response = data.data;

							$scope.module.stats = response.result.result;

							preformStatCalcs();

						}else{
							toastr.error(data.data.message, 'Error');
						}

					})
					.catch(function(data){

						toastr.error('Couldn\'t reach server sorry about that', 'Error');

					});

			}

			function initUserGoalSliders(){

				var timeout;
				var firstTime = true;
				$scope.$watch('module.goals', function(){

					if(firstTime){
						firstTime = false;
						return
					}

					$timeout.cancel(timeout);

					timeout = $timeout(function(){

						moduleRequestHelper.updateModuleGoals($scope.module.data.classCode, $scope.module.goals.attendance, $scope.module.goals.attainment)
							.then(function(data){

								if(data.data.successful === true){

									toastr.success('Successfully updated goals');

								}else{
									toastr.error(data.data.message, 'Error');
								}

							})
							.catch(function(data){

								toastr.error('Couldn\'t reach server sorry about that', 'Error');

							});

					}, 1500);

				}, true)

			}

			function preformStatCalcs(){

				var checker = $interval(function(){

					if(!$scope.module.data || !$scope.module.stats)
						return;

					$interval.cancel(checker);

					var diff;

					diff = $scope.module.data.passRate - $scope.module.stats.PASS_RATE;

					if(diff < 0){
						$scope.module.stats.passratephrase = Math.abs(Math.trunc(diff)) + '% points lower than the global average';
					}else{
						$scope.module.stats.passratephrase = Math.abs(Math.trunc(diff)) + '% points higher than the global average';
					}

					diff = $scope.module.data.classAverage.mean - $scope.module.stats.PASSMARK_MEAN;

					if(diff < 0){
						$scope.module.stats.classaveragephrase = Math.abs(Math.trunc(diff)) + '% points lower than the global average';
					}else{
						$scope.module.stats.classaveragephrase = Math.abs(Math.trunc(diff)) + '% points higher than the global average';
					}

					diff = $scope.module.data.attendanceAverage.mean - $scope.module.stats.ATTENDANCE_MEAN;

					if(diff < 0){
						$scope.module.stats.attendanceaveragephrase = Math.abs(Math.trunc(diff)) + '% points lower than the global average';
					}else{
						$scope.module.stats.attendanceaveragephrase = Math.abs(Math.trunc(diff)) + '% points higher than the global average';
					}

				}, 500);

			}

			modulesAdminViewController();

		}]);
