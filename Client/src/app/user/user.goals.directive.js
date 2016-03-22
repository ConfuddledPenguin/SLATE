/**
 * Created by Tom on 22/03/2016.
 */

angular.module('SLATE.user')
	.directive('slateUserGoalsSliders', function(){

		return {
			templateUrl: 'app/user/user.goals.directive.html',
			scope: {
				attendance: '=attendance',
				attainment: '=attainment'
			},
			controller: 'SLATE.user.goalSliders.controller'
		}

	})
	.controller('SLATE.user.goalSliders.controller', ['$scope', function($scope){

	}]);
