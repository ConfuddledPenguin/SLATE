/**
 * Created by Tom on 28/02/2016.
 */

angular.module('SLATE.assignment')
	.controller('SLATE.assignment.view.controller', ['$scope', 'toastr', '$stateParams', 'SLATE.assignment.requestHelper', 'SLATE.module.requestHelper',
		function($scope, toastr, $stateParams, assignmentRequestHelper, moduleRequestHelper){

			function assignmentViewController(){

				$scope.assignment = {
					module: {}
				};

				fetchModule();
				fetchAssignment();
				displayDistributionGraph();
			}

			function fetchAssignment(){

				assignmentRequestHelper.getAssignment($stateParams.assignmentNo)
					.then(function(data){

						if(data.data.successful === true){

							var response = data.data;

							$scope.assignment.assignment = response.result;

							displayDistributionGraph();

						}else{
							toastr.error(data.data.message, 'Error');
						}

					})
					.catch(function(data){

						toastr.error('Couldn\'t reach server sorry about that', 'Error');

					});

			}

			function fetchModule(){

				moduleRequestHelper.getModule($stateParams.year, $stateParams.classCode)
					.then(function(data){

						if(data.data.successful === true){

							var response = data.data;

							$scope.assignment.module = response.result;

						}else{
							toastr.error(data.data.message, 'Error');
						}

					})
					.catch(function(data){

						toastr.error('Couldn\'t reach server sorry about that', 'Error');

					});

			}

			function displayDistributionGraph(){

				var x = [];
				if($scope.assignment.assignment) {

					for(var i =0; i < $scope.assignment.assignment.marks.length; i++){
						x[i] = Math.floor($scope.assignment.assignment.marks[i] /10) * 10;
					}
				}

				var data = [
					{
						x: x,
						type: 'histogram'
					}
				];

				var layout = {
					showlegend: false,
					title: 'Distribution Graph',
					xaxis: {
						range: [0,100],
						fixedrange: true,
						title: 'Percentage (to the nearest 10%)'
					},
					yaxis: {
						title: 'No. of students'
					}
				};

				var options = {
					displayModeBar: false
				};

				$scope.assignment.distributionGraph = {
					data: data,
					layout: layout,
					options: options
				}

			}

			assignmentViewController();

		}]);
