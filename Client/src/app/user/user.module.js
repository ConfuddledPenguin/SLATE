/**
 * Created by Tom on 26/01/2016.
 */

angular.module('SLATE.user', [])
	.config(['$stateProvider',function($stateProvider) {

		$stateProvider
			.state('user', {
				url: '/users',
				abstract: true,
				views: {
					'header': {
						templateUrl: 'app/header/header.html'
					},
					'nav': {
						template: ''
					},
					'main': {
						template: '<div ng-class="{\'full-height\': !app.showNav}" ui-view></div>'
					}
				}
			})
			.state('user.auth', {
				template: '<div class="full-height" ui-view></div>',
				onEnter: function($rootScope){
					$rootScope.app.showHeader = false;
					$rootScope.app.showNav = false;
				},
				onExit: function($rootScope){
					$rootScope.app.showHeader = true;
					$rootScope.app.showNav = true;
				}
			})
			.state('user.auth.login', {
				url: '/login',
				templateUrl: 'app/user/user.login.html',
				controller: 'SLATE.user.login.controller'
			})
			.state('frame.user', {
				url: '/user',
				abstract: true,
				template: '<div ng-class="{\'full-height\': !app.showNav}" ui-view></div>'
			})
			.state('frame.user.profile',{
				url: '/profile/:username',
				templateUrl:'app/user/user.profile.html',
				controller: 'SLATE.user.profile.controller',
				onEnter: function($rootScope){


				}
			})

	}]);
