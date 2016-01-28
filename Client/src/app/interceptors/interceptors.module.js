

angular.module('SLATE.interceptors', [])
	.config(['$httpProvider', function($httpProvider){

		$httpProvider.interceptors.push('sessionInjector');

	}]);