/**
 * Created by Tom on 28/02/2016.
 */

angular.module('SLATE.assignment', [])
	.config(['$stateProvider',function($stateProvider) {

		$stateProvider
			.state('frame.assignment',{
				url: '/modules',
				abstract: true,
				template: '<div class="full-height" ng-class="{\'full-height\': !app.showNav}" ui-view></div>'
			})
			.state('frame.assignment.assignmentView', {
				url: '/:year/:classCode/assignments/:assignmentName/:assignmentNo',
				templateUrl: 'app/assignment/assignment.view.html',
				controller: 'SLATE.assignment.view.controller',
				onEnter: function($rootScope){
					$rootScope.app.stateTitle = 'Assignments';
				}
			});
	}]);