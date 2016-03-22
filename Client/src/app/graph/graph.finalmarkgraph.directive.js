/**
 * Created by Tom on 21/03/2016.
 */

angular.module('SLATE.graphs')
	.directive('slateFinalMarkGraph', function(){
		return {
			templateUrl: 'app/graph/graph.finalmarkgraph.html',
			scope: {
				data: '=data',
				uniqueId: '@uniqueid'
			},
			controller: 'SLATE.graphs.finalmarkgraph.controller'
		}
	})
	.controller('SLATE.graphs.finalmarkgraph.controller', ['$scope', 'SLATE.config', function($scope, config){

		function graphsFinalMarkGraphController(){

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

				if(firstRun){
					var userOptions = {
						showScatter: true,
						showBestFit: false
					};

					$scope.graph.userOptions = userOptions;
				}else{
					var userOptions = $scope.graph.userOptions;
				}
				firstRun = false;

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

				var data = [classAverage, passRate];

				if($scope.data.yearsAsArray){

					if(userOptions.stdDev){

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

					if(userOptions.minMax){

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

					$scope.data.yearsAsArray.forEach(function (item) {

						classAverage.x.push(item.year);
						classAverage.y.push(item.classAverage.mean);
						if(userOptions.stdDev)
							classAverage.error_y.array.push(item.classAverage.stdDev);

						if(userOptions.minMax){
							classAverage.error_y.array.push(item.classAverage.max - item.classAverage.mean);
							classAverage.error_y.arrayminus.push(item.classAverage.mean - item.classAverage.min);
						}

						passRate.x.push(item.year);
						passRate.y.push(item.passRate);
						if(userOptions.stdDev)
							passRate.error_y.array.push(item.passRate.stdDev);
						if(userOptions.minMax){
							passRate.error_y.array.push(item.passRate.max - item.passRate.mean);
							passRate.error_y.arrayminus.push(item.passRate.mean - item.passRate.min);
						}
					});
				}

				var holder = document.getElementById('final-mark-graph-' + $scope.uniqueId);
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

				$scope.graph.data = data;
				$scope.graph.layout = layout;
				$scope.graph.options = options;
			}
			build();

			$scope.$watch('graph.userOptions', function(newVal, oldVal){

				if(newVal !== oldVal){

					if(newVal.minMax && oldVal.stdDev){
						$scope.graph.userOptions.stdDev = false;
					}

					if(newVal.stdDev && oldVal.minMax){
						$scope.graph.userOptions.minMax = false;
					}

					build();
					$scope.graph.userOptions = newVal;
				}
			}, true);

			$scope.$watch(
				function(){
					var holder = document.getElementById('final-mark-graph-' + $scope.uniqueId);
					var width = (holder != null) ? holder.offsetWidth: 500;
					return width;
				},
				function(){
					build();
				}
			)
		}

		graphsFinalMarkGraphController();

	}]);