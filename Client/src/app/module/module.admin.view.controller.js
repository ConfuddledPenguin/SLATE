/**
 * Created by Tom on 04/03/2016.
 */

angular.module('SLATE.modules')
	.controller('SLATE.module.admin.view.controller', ['$scope', '$stateParams', '$state', 'toastr', 'SLATE.module.requestHelper',
		function($scope, $stateParams, $state, toastr, requestHelper){

			function modulesAdminViewController(){

				$scope.module = {
					data: null
				};

				fetchModule();
				buildYearFinalMarkGraph();

			}

			function fetchModule(){

				requestHelper.getModuleAdmin($stateParams.classCode)
					.then(function(data){

						if(data.data.successful === true){

							var response = data.data;

							$scope.module.data = response.result;

							var years = $scope.module.data.years;

							$scope.module.data.yearsAsArray = Object.keys(years).map(function(key){
								var year = years[key];

								year.year = year.year + "/" + (parseInt(year.year) + 1)

								return year;
							});

							buildYearFinalMarkGraph();
						}else{
							toastr.error(data.data.message, 'Error');
						}

					})
					.catch(function(data){

						toastr.error('Couldn\'t reach server sorry about that', 'Error');

					});

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

			modulesAdminViewController();

		}]);
