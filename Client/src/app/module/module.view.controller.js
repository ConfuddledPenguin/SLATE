/**
 * Created by Tom on 11/02/2016.
 */

angular.module('SLATE.modules')
	.controller('SLATE.module.view.controller', ['$scope', 'toastr', '$stateParams', '$state', 'SLATE.module.requestHelper', '$timeout',
		function($scope, toastr, $stateParams, $state, requestHelper, $timeout){

			function moduleViewController(){

				$scope.module = {};

				setupScope();
				fetchModule();
				//displayAttendanceGraph();
				//displayProgressGraph();

			}

			function fetchModule(){

				requestHelper.getModule($stateParams.year, $stateParams.classCode)
					.then(function(data){

						if(data.data.successful === true){

							var response = data.data;

							$scope.module.data = response.result;

							$scope.module.goals = {
								attainment: $scope.module.data.attainmentGoal,
								attendance: $scope.module.data.attendanceGoal
							};

							initUserGoalSliders();

							//displayProgressGraph();

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

						requestHelper.updateUserModuleGoals($scope.module.data.year, $scope.module.data.classCode, $scope.module.goals.attendance, $scope.module.goals.attainment)
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

		function displayProgressGraph(){

			var student = {
				x: [],
				y: [],
				mode: 'lines+markers',
				type: 'scatter',
				marker:{
					color: '#F47B20'
				},
				line: {
					color: '#F47B20'
				},
				name: 'Your Mark'
			};

			var average = {
				x: [],
				y: [],
				mode: 'lines+markers',
				type: 'scatter',
				marker:{
					color: '#3055A6'
				},
				line: {
					color: '#3055A6'
				},
				name: 'Average Mark'
			};

			if($scope.module.data){

				$scope.module.data.assignments.forEach(function(item){
					student.x.push(item.name);
					student.y.push(item.percentage);
					average.x.push(item.name);
					average.y.push(item.average);

				});
			}

			var width = document.getElementById('module-progress-graph-holder').offsetWidth;

			var layout = {
				showlegend: false,
				title: 'Progress Graph',
				xaxis:{
					title: "Assignment",
					fixedrange: true
				},
				yaxis: {
					range: [0,100],
					fixedrange: true,
					title: 'Percentage'
				},
				autosize: false,
				width: width
			};

			var options = {
				displayModeBar: false
			};

			$scope.module.progressGraph = {
				data: [average, student],
				layout: layout,
				options: options
			};


			var gradeElements = document.getElementsByClassName('module-grade-info');
			var height = gradeElements[0].offsetHeight;



			for(var i = 0; i < gradeElements.length; i++){

				var item = gradeElements[i];

				var nodes = item.childNodes;

				nodes[1].style.lineHeight = height * 0.7 + 'px';
				nodes[1].style.fontSize = height / 2 * 0.7 + 'px';

			}
		}

		function displayAttendanceGraph(){

			var x = [];
			for (var i = 0; i < 500; i ++) {
				x[i] = Math.random();
			}

			var data = [
				{
					x: ['week1', 'week2', 'week3', 'week4', 'week5', 'week6', 'week7', 'week8', 'week9','week10','week11','week12',
						'week13', 'week14', 'week15', 'week16', 'week17', 'week18', 'week19', 'week20', 'week21','week22','week23','week24'],
					y: [20, 14, 23,20, 14, 23,20, 14, 23,20, 14, 23,20, 14, 23,20, 14, 23,20, 14, 23,20, 14, 23],
					type: 'bar',
					marker: {
						color: '#3055A6'
					}
				}
			];

			var layout = {
				xaxis: {
					fixedrange: true,
					title: 'Week'
				},
				yaxis: {
					range: [0,100],
					fixedrange: true,
					title: 'Percentance Attendance'
				}
			};

			var options = {
				displayModeBar: false
			};

			$scope.module.attendanceGraph = {
				data: data,
				layout: layout,
				options: options
			}
		}

		function setupScope(){

			$scope.module.navigateAssignment = function(name, no){
				$state.go('frame.assignment.assignmentView', {year: $scope.module.data.year, classCode: $scope.module.data.classCode, assignmentName: name, assignmentNo: no});
			};

			$scope.updateSliders = function(){

				$timeout(function(){
					$scope.$broadcast('reCalcViewDimensions');
				});
			}
		}

		moduleViewController();

	}]);
