/**
 * Created by Tom on 04/03/2016.
 */

angular.module('SLATE.modules')
	.controller('SLATE.module.admin.view.controller', ['$scope', '$stateParams', '$state', 'toastr', 'SLATE.module.requestHelper', 'SLATE.statistics.requestHelper', '$interval', 'SLATE.config',
		function($scope, $stateParams, $state, toastr, moduleRequestHelper, statsRequestHelper, $interval, config){

			function modulesAdminViewController(){

				$scope.module = {
					data: null,
					stats: null
				};

				fetchModule();
				fetchStats();
				buildYearFinalMarkGraph();
				buildAttendanceGraph();
				buildAttendanceAttainmentGraph();
			}

			function fetchModule(){

				moduleRequestHelper.getModuleAdmin($stateParams.classCode)
					.then(function(data){

						if(data.data.successful === true){

							var response = data.data;

							$scope.module.data = response.result;

							console.log($scope.module.data);

							var years = $scope.module.data.years;

							$scope.module.data.yearsAsArray = Object.keys(years).map(function(key){
								var year = years[key];

								year.year = year.year + "/" + (parseInt(year.year) + 1)

								return year;
							});

							buildYearFinalMarkGraph();
							buildAttendanceGraph();
							buildAttendanceAttainmentGraph();
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

			function preformStatCalcs(){

				var checker = $interval(function(){

					if(!$scope.module.data || !$scope.module.stats)
						return;

					$interval.cancel(checker);

					var diff;

					diff = $scope.module.data.passRate - $scope.module.stats.PASS_RATE;

					if(diff < 0){
						$scope.module.stats.passratephrase = Math.abs(Math.trunc(diff)) + '% points lower than average';
					}else{
						$scope.module.stats.passratephrase = Math.abs(Math.trunc(diff)) + '% points higher than average';
					}

					diff = $scope.module.data.classAverage.mean - $scope.module.stats.PASSMARK_MEAN;

					if(diff < 0){
						$scope.module.stats.classaveragephrase = Math.abs(Math.trunc(diff)) + '% points lower than average';
					}else{
						$scope.module.stats.classaveragephrase = Math.abs(Math.trunc(diff)) + '% points higher than average';
					}

					diff = $scope.module.data.attendanceAverage.mean - $scope.module.stats.ATTENDANCE_AVERAGE;

					if(diff < 0){
						$scope.module.stats.attendanceaveragephrase = Math.abs(Math.trunc(diff)) + '% points lower than average';
					}else{
						$scope.module.stats.attendanceaveragephrase = Math.abs(Math.trunc(diff)) + '% points higher than average';
					}

				}, 500);

			}

			function buildYearFinalMarkGraph(){

				var firstRun = true;

				var build = function() {

					var classAverage = {
						x: [],
						y: [],
						mode: 'lines+markers',
						type: 'scatter',
						marker: {
							color: '#3055A6'
						},
						line: {
							color: '#3055A6'
						},
						name: 'Class Average'
					};

					var passRate = {
						x: [],
						y: [],
						mode: 'lines+markers',
						type: 'scatter',
						marker: {
							color: '#1A2E5A'
						},
						line: {
							color: '#1A2E5A'
						},
						name: 'Pass Rate'
					};

					if ($scope.module.data) {

						if($scope.module.yearFinalMarkGraph.userOptions.stdDev){

							passRate.error_y = {
								type: 'data',
								visible: true,
								array: []
							};

							classAverage.error_y = {
								type: 'data',
								visible: true,
								array: []
							};
						}

						if($scope.module.yearFinalMarkGraph.userOptions.minMax){

							passRate.error_y = {
								type: 'data',
								visible: true,
								symmetric: false,
								array: [],
								arrayminus: []
							};

							classAverage.error_y = {
								type: 'data',
								visible: true,
								symmetric: false,
								array: [],
								arrayminus: []
							};
						}

						$scope.module.data.yearsAsArray.forEach(function (item) {

							classAverage.x.push(item.year);
							classAverage.y.push(item.classAverage.mean);
							if($scope.module.yearFinalMarkGraph.userOptions.stdDev)
								classAverage.error_y.array.push(item.classAverage.stdDev);

							if($scope.module.yearFinalMarkGraph.userOptions.minMax){
								classAverage.error_y.array.push(item.classAverage.max - item.classAverage.mean);
								classAverage.error_y.arrayminus.push(item.classAverage.mean - item.classAverage.min);
							}

							passRate.x.push(item.year);
							passRate.y.push(item.passRate);
							if($scope.module.yearFinalMarkGraph.userOptions.stdDev)
								passRate.error_y.array.push(item.passRate.stdDev);
							if($scope.module.yearFinalMarkGraph.userOptions.minMax){
								passRate.error_y.array.push(item.passRate.max - item.passRate.mean);
								passRate.error_y.arrayminus.push(item.passRate.mean - item.passRate.min);
							}
						});
					}

					var holder = document.getElementById('year-final-mark-graph-holder');
					var width = (holder != null) ? holder.offsetWidth: 500;

					var layout = {
						showlegend: true,
						title: 'Tracking Graph',
						xaxis: {
							title: "Year",
							fixedrange: true
						},
						yaxis: {
							range: [0, 100],
							fixedrange: true,
							title: 'Percentage'
						},
						autosize: false,
						width: width
					};

					var options = {
						displayModeBar: false
					};

					$scope.module.yearFinalMarkGraph = {
						data: [classAverage, passRate],
						layout: layout,
						options: options,
						userOptions: {}
					};

				};

				build();

				$scope.$watch('module.yearFinalMarkGraph.userOptions', function(newVal, oldVal){

					if(newVal !== oldVal){

						if(newVal.minMax && oldVal.stdDev){
							$scope.module.yearFinalMarkGraph.userOptions.stdDev = false;
						}

						if(newVal.stdDev && oldVal.minMax){
							$scope.module.yearFinalMarkGraph.userOptions.minMax = false;
						}

						build();
						$scope.module.yearFinalMarkGraph.userOptions = newVal;
					}
				}, true)

			}

			function buildAttendanceGraph(){

				$scope.module.moduleAttendanceGraph = {
					data: [],
					layout: {},
					options: {},
					userOptions: {}
				};

				var firstRun = true;
				function build(){

					var userOptions = {};
					if(firstRun){

						if($scope.module.data){

							userOptions.sessiontypes = {
								ALL: {
									display: true,
									name: 'all'
								}
							};

							Object.keys($scope.module.data.attendance).forEach(function( value, key){

								if(value === 'ALL') return;

								userOptions.sessiontypes[value] = {
									display: false,
									name: value.toLowerCase()
								};

							});
						}

						$scope.module.moduleAttendanceGraph.userOptions = userOptions;

					}else{
						userOptions = $scope.module.moduleAttendanceGraph.userOptions;
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

					if($scope.module.data){

						data = [];

						Object.keys($scope.module.data.attendance).forEach(function(item, key){

							if(userOptions.sessiontypes[item].display){

								var x = [];
								var y = [];

								$scope.module.data.attendance[item].forEach(function(value, key){

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

					var holder = document.getElementById('year-final-mark-graph-holder');
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

					$scope.module.moduleAttendanceGraph.data = data;
					$scope.module.moduleAttendanceGraph.layout = layout;
					$scope.module.moduleAttendanceGraph.options = options;
				}

				build();

				$scope.$watch('module.moduleAttendanceGraph.userOptions', function(newVal, oldVal){

					if(newVal !== oldVal){

						build();
						$scope.module.moduleAttendanceGraph.userOptions = newVal;
					}
				}, true)
			}

			function buildAttendanceAttainmentGraph(){

				$scope.module.moduleAttendanceAttainmentGraph = {
					data: [],
					layout: {},
					options: {},
					userOptions: {}
				};

				var firstRun = true;
				function build(){

					var userOptions = {
						showScatter: true,
						showBestFit: false
					};
					if(firstRun){

						if($scope.module.data){

							userOptions.sessiontypes = {
								ALL: {
									display: true,
									name: 'all',
									color: '#F47B20'
								}
							};

							Object.keys($scope.module.data.enrollments[0].attendance).forEach(function( value, key){

								if(value === 'ALL') return;

								var color = config.GRAPH_COLORS[ key % config.GRAPH_COLORS.length];

								userOptions.sessiontypes[value] = {
									display: false,
									name: value.toLowerCase(),
									color: color
								};
							});
						}

						$scope.module.moduleAttendanceAttainmentGraph.userOptions = userOptions;

					}else{
						userOptions = $scope.module.moduleAttendanceAttainmentGraph.userOptions;
					}
					firstRun = false;

					var data = [];

					if($scope.module.data){

						Object.keys(userOptions.sessiontypes).forEach(function(item, key){

							var session = userOptions.sessiontypes[item];

							if(!session.display) return;

							if(userOptions.showScatter){
								var sessionData = {
									x: [],
									y: [],
									type: 'scatter',
									mode: 'markers',
									marker: {
										color: userOptions.sessiontypes[item].color
									},
									name: session.name
								};

								$scope.module.data.enrollments.forEach(function(value){

									sessionData.x.push(value.finalMark);
									sessionData.y.push(value.attendance[item].attendanceAverage.mean);
								});

								data.push(sessionData);
							}

							if(userOptions.showBestFit){

								var sessionData = {
									x: [],
									y: [],
									type: 'scatter',
									mode: 'lines',
									marker: {
										color: userOptions.sessiontypes[item].color
									},
									name: session.name
								};

								var fits = {
									minX: Number.MAX_VALUE,
									maxX: Number.MIN_VALUE
								};

								$scope.module.data.enrollments.forEach(function(value){

									if(fits.minX > value.finalMark) fits.minX = value.finalMark;
									if(fits.maxX < value.finalMark) fits.maxX = value.finalMark;

									//sessionData.y.push(value.attendance[item].attendanceAverage.mean);
								});

								console.log("fits)");
								console.log(fits);

								sessionData.x.push(fits.minX);
								sessionData.y.push(fits.minX * $scope.module.data.attendanceAttainmentCorrelation[item].linearSlope
									+ $scope.module.data.attendanceAttainmentCorrelation[item].linearIntercept);

								sessionData.x.push(fits.maxX);
								sessionData.y.push(fits.maxX * $scope.module.data.attendanceAttainmentCorrelation[item].linearSlope
									+ $scope.module.data.attendanceAttainmentCorrelation[item].linearIntercept);

								data.push(sessionData);
							}
						});

						console.log(data)
					}

					var holder = document.getElementById('year-final-mark-graph-holder');
					var width = (holder != null) ? holder.offsetWidth: 500;

					var layout = {
						showlegend: true,
						title: 'Attendance vs Attainment Graph',
						xaxis: {
							range: [0,100],
							fixedrange: true,
							title: 'Final Mark Percentage'
						},
						yaxis: {
							range: [0,100],
							fixedrange: true,
							title: 'Percentage Attendance'
						},
						autosize: false,
						width: width
					};

					var options = {
						displayModeBar: false
					};

					$scope.module.moduleAttendanceAttainmentGraph.data = data;
					$scope.module.moduleAttendanceAttainmentGraph.layout = layout;
					$scope.module.moduleAttendanceAttainmentGraph.options = options;
				}
				build();

				$scope.$watch('module.moduleAttendanceAttainmentGraph.userOptions', function(newVal, oldVal){

					if(newVal !== oldVal){

						build();
						$scope.module.moduleAttendanceAttainmentGraph.userOptions = newVal;
					}
				}, true)
			}

			modulesAdminViewController();

		}]);
