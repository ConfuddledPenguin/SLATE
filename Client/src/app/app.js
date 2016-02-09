/**
 * Created by Tom on 19/01/2016.
 */

angular.module('SLATE.app', ['SLATE.config', 'SLATE.polyfill', 'SLATE.interceptors', 'SLATE.header', 'SLATE.nav', 'SLATE.user']);

angular.module('SLATE.dependencies', ['ui.router', 'LocalStorageModule', 'toastr']);

app = angular.module('SLATE', ['SLATE.dependencies', 'SLATE.app']);

app.config(['$stateProvider', '$locationProvider', '$urlMatcherFactoryProvider', '$urlRouterProvider', function($stateProvider, $locationProvider, $urlMatcherFactory, $urlRouterProvider) {

	$urlMatcherFactory.caseInsensitive(true);
	$urlMatcherFactory.strictMode(false);

	$urlRouterProvider.otherwise( function($injector) {
		var $state = $injector.get("$state");
		$state.go('404');
	});

	$stateProvider
		.state('frame', {
			abstract: true,
			views: {
				'header': {
					//template: 'header'
					templateUrl: 'app/header/header.html',
					controller: 'SLATE.header.controller'
				},
				'nav': {
					//template: 'nav'
					templateUrl: 'app/nav/nav.html',
					controller: 'SLATE.nav.controller'
				},
				'main': {
					template: '<div ui-view style="height: 100%"></div> '
				}
			}
		})
		.state('frame.dashboard', {
			url: '/',
			template: 'panel',
			onEnter: function($rootScope){
				$rootScope.app.stateTitle = 'Dashboard';
			}
		})
		.state('404', {
			url: '/page-not-found',
			views: {
				'header': {
					template: ''
				},
				'nav': {
					template: ''
				},
				'main': {
					templateUrl: 'app/general/404.html'
				}
			},
			onEnter: function($rootScope){
				$rootScope.app.showHeader = false;
				$rootScope.app.showNav = false;
			},
			onExit: function($rootScope){
				$rootScope.app.showHeader = true;
				$rootScope.app.showNav = true;
			}
		});

	$locationProvider.html5Mode(true);

}]);

app.run(['$rootScope', '$state', '$location', '$timeout', 'SLATE.user.service', function($rootScope, $state, $location, $timeout, userService){

	$rootScope.app = {
		showNav: true,
		showHeader: true
	};

	userService.loadUser();

	$rootScope.$on("$stateChangeError", console.log.bind(console));

	$rootScope.app = {
		showNav: true,
		showHeader: true
	};

	$rootScope.$on("$stateChangeStart", function(args){

		if(!userService.getUser()){
			$location.path('/users/login');
		}

	});

	//lastly
	$timeout(function(){

		$rootScope.app.loaded = true;

	}, 1800);

}]);
