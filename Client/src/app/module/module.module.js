/**
 * Created by Tom on 26/01/2016.
 */

angular.module('SLATE.modules', [])
	.config(['$stateProvider',function($stateProvider) {

		$stateProvider
			.state('frame.modules',{
				url: '/modules',
				abstract: true,
				template: '<div class="full-height" ng-class="{\'full-height\': !app.showNav}" ui-view></div>',
				onEnter: function($rootScope){
					$rootScope.app.stateTitle = 'Modules';
				}
			})
			.state('frame.modules.adminView', {
				url: '/admin',
				templateUrl: 'app/module/module.admin.html',
				controller: 'SLATE.module.admin.controller'
			})
			.state('frame.modules.adminModuleView',{
				url: '/admin/:classCode',
				templateUrl: 'app/module/module.admin.view.html',
				controller: 'SLATE.module.admin.view.controller'
			})
			.state('frame.modules.moduleView', {
				url: '/:year/:classCode',
				templateUrl: 'app/module/module.view.html',
				controller: 'SLATE.module.view.controller',
				onEnter: function($rootScope){
					$rootScope.app.stateTitle = 'Modules';
				}
			})

	}]);
