/**
 * Created by Tom on 16/03/2016.
 */

angular.module('SLATE.modules')
	.directive('slateModuleAdminViewYear', function(){
		return {
			templateUrl: 'app/module/module.admin.view.year.html',
			scope: {
				args: '=args'
			},
			controller: 'SLATE.modules.admin.view.year.controller'
		}
	})
	.controller('SLATE.modules.admin.view.year.controller', ['$scope', '$stateParams', '$state', 'toastr', 'SLATE.module.requestHelper', 'SLATE.statistics.requestHelper', '$interval', 'SLATE.config',
		function($scope, $stateParams, $state, toastr, moduleRequestHelper, statsRequestHelper, $interval, config){

			function modulesAdminViewController(){

				$scope.year = {	};
				$scope.yearFetched = false;

				function buildGraphs(){
					buildAttendanceGraph();
				}

				$scope.$watch('args.active', function(){

					if($scope.args.active == true){

						var interval = $interval(function(){

							$interval.cancel(interval);

							buildGraphs();
						}, 100);
					}
				});


				$scope.$watch('args.year.load', function(){

					if(!$scope.year.data && $scope.args.year.load){

						fetchModule();
						fetchStats();
						setUpStatWatch();
						buildGraphs();

					}

				});
			}

			function fetchModule(){
				moduleRequestHelper.getModule($scope.args.year.yearOriginal, $scope.args.classCode)
					.then(function(data){

						if(data.data.successful === true){

							var response = data.data;
							$scope.year.data = response.result;

							console.log($scope.year.data);
							buildAttendanceGraph();

						}else{
							toastr.error(data.data.message, 'Error');
						}

					})
					.catch(function(){
						toastr.error('Couldn\'t reach server sorry about that', 'Error');
					})
			}

			function fetchStats(){

				var stats = ['PASS_RATE','PASSMARK_MEAN','ATTENDANCE_MEAN'];

				stats.push('PASSRATE_' + $scope.args.year.yearOriginal + '_' + $scope.args.moduleLevel);
				stats.push('PASSMARK_' + $scope.args.year.yearOriginal + '_' + $scope.args.moduleLevel + '_MEAN');

				statsRequestHelper.getStatistics(stats)
					.then(function(data){

						if(data.data.successful === true){

							var response = data.data;

							$scope.year.stats = response.result.result;

							console.log(response.result.result);

							setUpStatWatch();

						}else{
							toastr.error(data.data.message, 'Error');
						}

					})
					.catch(function(data){

						toastr.error('Couldn\'t reach server sorry about that', 'Error');

					});

			}

			function setUpStatWatch(){

				var checker = $interval(function(){

					if(!$scope.year.data)
						return;

					$interval.cancel(checker);

					var diff;

					diff = $scope.year.data.passRate - $scope.args.stats.PASS_RATE;

					if(diff < 0){
						$scope.year.stats.passratephrase = Math.abs(Math.trunc(diff)) + '% points lower than the global average';
					}else{
						$scope.year.stats.passratephrase = Math.abs(Math.trunc(diff)) + '% points higher than the global average';
					}

					console.log('PASSRATE_' + $scope.args.year.yearOriginal + '_' + $scope.args.moduleLevel);
					console.log($scope.year.stats);
					diff = $scope.year.data.passRate - $scope.year.stats['PASSRATE_' + $scope.args.year.yearOriginal + '_' + $scope.args.moduleLevel];

					if(diff < 0){
						$scope.year.stats.passrateyearphrase = Math.abs(Math.trunc(diff)) + '% points lower than the years average';
					}else{
						$scope.year.stats.passrateyearphrase = Math.abs(Math.trunc(diff)) + '% points higher than average';
					}

					diff = $scope.year.data.classAverage.mean - $scope.args.stats.PASSMARK_MEAN;

					if(diff < 0){
						$scope.year.stats.classaveragephrase = Math.abs(Math.trunc(diff)) + '% points higher than the global average';
					}else{
						$scope.year.stats.classaveragephrase = Math.abs(Math.trunc(diff)) + '% points higher than the global average';
					}

					diff = $scope.year.data.classAverage.mean - $scope.year.stats['PASSMARK_' + $scope.args.year.yearOriginal + '_' + $scope.args.moduleLevel + '_MEAN'];

					if(diff < 0){
						$scope.year.stats.classaverageyearphrase = Math.abs(Math.trunc(diff)) + '% points higher than the years average';
					}else{
						$scope.year.stats.classaverageyearphrase = Math.abs(Math.trunc(diff)) + '% points higher than the years average';
					}

					diff = $scope.year.data.attendanceAverage.mean - $scope.args.stats.ATTENDANCE_MEAN;

					if(diff < 0){
						$scope.year.stats.attendanceaveragephrase = Math.abs(Math.trunc(diff)) + '% points higher than the global average';
					}else{
						$scope.year.stats.attendanceaveragephrase = Math.abs(Math.trunc(diff)) + '% points higher than the global average';
					}

				}, 500);

			}

			function buildAttendanceGraph(){

				$scope.year.moduleAttendanceGraph = {
					data: [],
					layout: {},
					options: {},
					userOptions: {}
				};

				var firstRun = true;
				function build(){

					var userOptions = {};
					if(firstRun){

						if($scope.year.data){

							userOptions.sessiontypes = {
								ALL: {
									display: true,
									name: 'all'
								}
							};

							Object.keys($scope.year.data.attendance).forEach(function( value, key){

								if(value === 'ALL') return;

								userOptions.sessiontypes[value] = {
									display: false,
									name: value.toLowerCase()
								};

							});
						}

						$scope.year.moduleAttendanceGraph.userOptions = userOptions;

					}else{
						userOptions = $scope.year.moduleAttendanceGraph.userOptions;
					}
					firstRun = false;


					var data = [
						{
							x: ['week1', 'week2', 'week3', 'week4', 'week5', 'week6', 'week7', 'week8', 'week9','week10','week11','week12',
								'week13', 'week14', 'week15', 'week16', 'week17', 'week18', 'week19', 'week20', 'week21','week22','week23','week24'],
							y: [],
							type: 'bar',
							marker: {
								color: '#3055A6'
							}
						}
					];

					if($scope.year.data){

						data = [];

						Object.keys($scope.year.data.attendance).forEach(function(item, key){

							if(userOptions.sessiontypes[item].display){

								var x = [];
								var y = [];

								$scope.year.data.attendance[item].forEach(function(value, key){

									if(key == 0) return;

									x.push('week ' + key);
									y.push(value.mean);

								});

								var color;
								if(item == 'ALL')
									color = '#F47B20';
								else{
									color = config.GRAPH_COLORS[ key % config.GRAPH_COLORS.length];
								}

								data.push({
									x: x,
									y: y,
									type: 'bar',
									marker: {
										color: color
									},
									name: userOptions.sessiontypes[item].name
								})
							}
						});
					}

					var holder = document.getElementById('year-attendance-graph-holder-' + $scope.args.key);
					var width = (holder != null) ? holder.offsetWidth: 500;

					var layout = {
						showlegend: true,
						title: 'Attendance Graph',
						xaxis: {
							fixedrange: true,
							title: 'Week'
						},
						yaxis: {
							range: [0,100],
							fixedrange: true,
							title: 'Percentance Attendance'
						},
						autosize: false,
						width: width
					};

					var options = {
						displayModeBar: false
					};

					$scope.year.moduleAttendanceGraph.data = data;
					$scope.year.moduleAttendanceGraph.layout = layout;
					$scope.year.moduleAttendanceGraph.options = options;
				}

				build();

				$scope.$watch('year.moduleAttendanceGraph.userOptions', function(newVal, oldVal){

					if(newVal !== oldVal){

						build();
						$scope.year.moduleAttendanceGraph.userOptions = newVal;
					}
				}, true)
			}

			modulesAdminViewController();

	}]);
