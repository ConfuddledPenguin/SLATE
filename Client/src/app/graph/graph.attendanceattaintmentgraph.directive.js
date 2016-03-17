/**
 * Created by Tom on 17/03/2016.
 */

angular.module('SLATE.graphs')
	.directive('slateGraphsAttendanceAttainmentGraph', function(){
		return {
			templateUrl: 'app/graph/graph.attendanceattainmentgraph.html',
			scope: {
				data: '=data',
				uniqueId: '@uniqueid'
			},
			controller: 'SLATE.graphs.attendanceattainmentgraph.controller'
		}
	})
	.controller('SLATE.graphs.attendanceattainmentgraph.controller', ['$scope', 'SLATE.config', function($scope, config){

		function graphsAttendanceAttainmentGraphController(){

			$scope.graph = {};

			$scope.$watch('data', function(){

				buildGraph();

			}, true);
		}

		function buildGraph(){

			$scope.graph = {
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

					if($scope.data.attendance){

						userOptions.sessiontypes = {
							ALL: {
								display: true,
								name: 'all',
								color: '#F47B20'
							}
						};

						Object.keys($scope.data.attendance).forEach(function( value, key){

							if(value === 'ALL') return;

							var color = config.GRAPH_COLORS[ key % config.GRAPH_COLORS.length];

							userOptions.sessiontypes[value] = {
								display: false,
								name: value.toLowerCase(),
								color: color
							};
						});
					}

					$scope.graph.userOptions = userOptions;

				}else{
					userOptions = $scope.graph.userOptions;
				}
				firstRun = false;

				var data = [];

				if($scope.data.attendance){

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

							$scope.data.enrollments.forEach(function(value){

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

							$scope.data.enrollments.forEach(function(value){

								if(fits.minX > value.finalMark) fits.minX = value.finalMark;
								if(fits.maxX < value.finalMark) fits.maxX = value.finalMark;
							});

							sessionData.x.push(fits.minX);
							sessionData.y.push(fits.minX * $scope.data.attendanceAttainmentCorrelation[item].linearSlope
								+ $scope.data.attendanceAttainmentCorrelation[item].linearIntercept);

							sessionData.x.push(fits.maxX);
							sessionData.y.push(fits.maxX * $scope.data.attendanceAttainmentCorrelation[item].linearSlope
								+ $scope.data.attendanceAttainmentCorrelation[item].linearIntercept);

							data.push(sessionData);
						}
					});
				}

				var holder = document.getElementById('attendance-attainment-graph-' + $scope.uniqueId);
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

				$scope.graph.data = data;
				$scope.graph.layout = layout;
				$scope.graph.options = options;
			}
			build();

			$scope.$watch('graph.userOptions', function(newVal, oldVal){

				if(newVal !== oldVal){

					build();
					$scope.graph.userOptions = newVal;
				}
			}, true);

			$scope.$watch(
				function(){
					var holder = document.getElementById('attendance-attainment-graph-' + $scope.uniqueId);
					var width = (holder != null) ? holder.offsetWidth: 500;
					return width;
				},
				function(){
					build();
				}
			)
		}

		graphsAttendanceAttainmentGraphController();

	}]);