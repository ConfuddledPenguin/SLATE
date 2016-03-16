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
	.controller('SLATE.modules.admin.view.year.controller', ['$scope', function($scope){



	}]);
