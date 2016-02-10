/**
 * Created by Tom on 26/01/2016.
 */


angular.module('SLATE.interceptors')
	.factory('sessionInjector', ['SLATE.config','localStorageService', '$location', 'toastr', function(config, localStorageService, $location, toastr){

		var sessionInjector = {

			request: function(request){

				if(request.url.startsWith(config.API_URL)){

					var user = localStorageService.get('SLATE.user');

					if(user){
						request.headers['x-access-token'] = localStorageService.get('SLATE.user').token;
					}
				}
				return request;
			},

			response: function(response){

				if(response.headers('service-id') !== config.API_service_id) return response;

				if(response.data.status === 1002){
					localStorageService.remove('SLATE.user');
					toastr.warn("You have an old auth token, please re-auth");
					$location.path('/users/login');
				}

				if(response.headers('x-access-token')){

					var user = localStorageService.get('SLATE.user');

					if(user)
						localStorageService.get('SLATE.user').token = response.headers('x-access-token');
					else{
						response.data.result.token = response.headers('x-access-token');
					}
				}

				return response;

			}
		};

		return sessionInjector;

	}]);