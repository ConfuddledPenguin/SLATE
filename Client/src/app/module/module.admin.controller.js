/**
 * Created by Tom on 03/03/2016.
 */

angular.module('SLATE.modules')
	.controller('SLATE.module.admin.controller', ['$scope', 'toastr', 'SLATE.module.requestHelper', '$state',
		function($scope, toastr, requestHelper, $state){

			function moduleAdminController(){
				$scope.modules = {
					data: {}
				};

				fetchModules("*");
			}

			function fetchModules(searchText){

				requestHelper.getModules(searchText)
					.then(function(data){

						if(data.data.successful === true){

							var response = data.data;

							$scope.modules.data = response.result;

						}else{
							toastr.error(data.data.message, 'Error');
						}

					})
					.catch(function(data){

						toastr.error('Couldn\'t reach server sorry about that', 'Error');

					});
			}

			$scope.navigateModule = function(classCode){
				$state.go("frame.modules.adminModuleView", {classCode: classCode});
			};

			moduleAdminController();
		}]);
