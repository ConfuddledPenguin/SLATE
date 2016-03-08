/**
 * Created by Tom on 26/01/2016.
 */


angular.module('SLATE.interceptors')
	.factory('sessionInjector', ['SLATE.config','localStorageService', '$location', '$injector', function(config, localStorageService, $location, $injector){

		var toastr = $injector.get('toastr');

		var sessionInjector = {

			request: function(request){

				if(request.url.startsWith(config.API_URL)){

					var token = localStorageService.get('SLATE.token');

					if(token){
						request.headers['x-access-token'] = localStorageService.get('SLATE.token');
					}
				}
				return request;
			},

			response: function(response){

				if(response.headers('service-id') !== config.API_service_id) return response;

				if(response.data.status === 1002){
					localStorageService.remove('SLATE.token');
					toastr.warn("You have an old auth token, please re-auth");
					$location.path('/users/login');
				}

				if(response.headers('x-access-token')){

					localStorageService.set('SLATE.token', response.headers('x-access-token'));

				}

				return response;

			}
		};

		return sessionInjector;

	}]);