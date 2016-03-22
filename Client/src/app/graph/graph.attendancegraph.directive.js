/**
 * Created by Tom on 21/03/2016.
 */


angular.module('SLATE.graphs')
	.directive('slateGraphsAttendanceGraph', function(){
		return {
			templateUrl: 'app/graph/graph.attendancegraph.html',
			scope: {
				data: '=data',
				uniqueId: '@uniqueid'
			},
			controller: 'SLATE.graphs.attendancegraph.controller'
		}
	})
	.controller('SLATE.graphs.attendancegraph.controller', ['$scope', 'SLATE.config', function($scope, config){

		function graphsAttendanceGraphController(){

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
			var wait;
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

				if($scope.data.attendance){

					data = [];

					Object.keys(userOptions.sessiontypes).forEach(function(item, key){

						var session = userOptions.sessiontypes[item];

						if(!session.display) return;

							var sessionData = {
								x: [],
								y: [],
								type: 'bar',
								marker: {
									color: userOptions.sessiontypes[item].color
								},
								name: session.name
							};

						$scope.data.attendance[item].forEach(function(value, key){

							if(key == 0) return;

							sessionData.x.push('week ' + key);
							sessionData.y.push(value.mean);

						});

						data.push(sessionData);

					});
				}

				var holder = document.getElementById('attendance-graph-' + $scope.uniqueId);
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
					var holder = document.getElementById('attendance-graph-' + $scope.uniqueId);
					var width = (holder != null) ? holder.offsetWidth: 500;
					return width;
				},
				function(){
					build();
				}
			)
		}

		graphsAttendanceGraphController();

	}]);