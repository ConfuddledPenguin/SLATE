/**
 * Created by Tom on 26/01/2016.
 */

angular.module('SLATE.user')
	.service('SLATE.user.service', ['SLATE.user.requestHelper', 'localStorageService','$location', 'toastr', '$rootScope', '$timeout',
		function(requestHelper, localStorageService, $location, toastr, $rootScope, $timeout){

			var user = null;

			this.login = function(username, password){

				requestHelper.login(username, password)
					.then(function(data){

						if(data.data.successful === true){

							var response = data.data;

							user = response.result;

							//generateGravatar();
							saveUser();

							sortGlobalUserInfo();

							$location.path('/');

						}else{
							toastr.error(data.data.message, 'Error');
						}

					})
					.catch(function(data){

						toastr.error('Couldn\'t reach server sorry about that', 'Error');

					});

			};

			this.logout = function(){

				localStorageService.remove('SLATE.user');

				$location.path('/users/login');
			};

			this.getUser = function(){
				return user;
			};

			this.getToken = function(){
				return user.token;
			};

			this.setToken = function(token){
				user.token = token;

				saveUser();
			};

			this.loadUser = function(){

				user = localStorageService.get('SLATE.user');

				if(user !== undefined){
					sortGlobalUserInfo();
				}

				return user;
			};

			var saveUser = function(){

				//save to local storage
				localStorageService.set('SLATE.user', user);
			};

			var generateGravatar = function(){

				if(!user.email) return;

				var hash = md5( user.email.toLowerCase().trim() );

				user.imageurl = 'https://www.gravatar.com/avatar/' + hash + '.jpeg';
				//to get own image add ?d= then url encoded path
			};

			var sortGlobalUserInfo = function(){



				$timeout(function(){

					$rootScope.fuck = false;

					$rootScope.app.nav = {
						enrolledModules: []
					};

					if(user.role === 'STUDENT'){
						for(var i = 0; i < user.enrolledModules.length; i++){

							var item = user.enrolledModules[i];

							$rootScope.app.nav.enrolledModules.push({
								classCode: item.classCode,
								name: item.name
							})
						}
					}

				})
			}

		}]);
