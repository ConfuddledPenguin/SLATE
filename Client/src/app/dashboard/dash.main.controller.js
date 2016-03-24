/**
 * Created by Tom on 24/03/2016.
 */

angular.module('SLATE.dash')
	.controller('SLATE.dash.main.controller', ['$rootScope','$scope', 'SLATE.user.requestHelper', 'toastr', function($rootScope, $scope, userRequestHelper, toastr){


		function dashMainController(){

			$scope.$watch('app.user', function(){

				if($rootScope.app.user.role === 'STUDENT'){
					studentViewing();
				}

				if($rootScope.app.user.role === 'LECTURER'){
					lecturerViewing();
				}

				if($rootScope.app.user.role === 'ADMIN'){
					adminViewing();
				}
			});
		}

		function studentViewing(){

			$scope.dash = {

			};

			userRequestHelper.getWarnings($rootScope.app.user.username)
				.then(function(data){

					if(data.data.successful === true){

						var response = data.data;

						$scope.dash.warnings = response.result.result;

					}else{
						toastr.error(data.data.message, 'Error');
					}
				})
				.catch(function(data){

					toastr.error('Couldn\'t reach server sorry about that', 'Error');

				});

		}

		function lecturerViewing(){

		}

		function adminViewing(){

		}


		dashMainController();

	}]);
