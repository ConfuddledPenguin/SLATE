/**
 * Created by Tom on 26/01/2016.
 */

angular.module('SLATE.user')
	.service('SLATE.user.service', ['SLATE.user.requestHelper', 'localStorageService','$location', 'toastr', '$rootScope', '$timeout',
		function(requestHelper, localStorageService, $location, toastr, $rootScope, $timeout){

			var user = null;
			var userloaded = false;
			var service = this;

			this.login = function(username, password){

				requestHelper.login(username, password)
					.then(function(data){

						if(data.data.successful === true){

							var response = data.data;

							user = response.result;

							//generateGravatar();
							saveUser();

							service.updateUserFromServer();

							$location.path('/');

						}else{
							toastr.error(data.data.message, 'Error');
						}

					})
					.catch(function(data){

						toastr.error('Couldn\'t reach server sorry about that', 'Error');

					});

			};

			this.updateUserFromServer = function(){

				requestHelper.getUser(user.username)
					.then(function(data){

						if(data.data.successful === true){

							var response = data.data;

							localStorageService.get('SLATE.user');
							user = response.result;

							generateGravatar();
							saveUser();
							sortGlobalUserInfo();

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
				localStorageService.remove('SLATE.token');

				$location.path('/users/login');
			};

			this.getUser = function(){
				return user;
			};

			this.getUserLoaded = function(){
				return userloaded;
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

				if(user){
					service.updateUserFromServer();
				}

				return user;
			};

			var saveUser = function(){

				//save to local storage
				localStorageService.set('SLATE.user', user);

				if(user.token)
					localStorageService.set('SLATE.token', user.token);
			};

			var generateGravatar = function(){

				if(!user.email) return;

				var hash = md5( user.email.toLowerCase().trim() );

				user.imageurl = 'https://www.gravatar.com/avatar/' + hash + '.jpeg';
				//to get own image add ?d= then url encoded path
			};

			var sortGlobalUserInfo = function(){

				$timeout(function(){

					$rootScope.app.user = {
						enrolledModules: [],
						role: user.role,
						username: user.username,
						name: user.name
					};

					if(user.role === 'STUDENT'){

						var currentYear = new Date().getFullYear();

						if( new Date().getMonth() < 7)
							--currentYear;

						for(var i = 0; i < user.enrolledModules.length; i++){

							var item = user.enrolledModules[i];

							var year_no = currentYear - item.year;
							var year_end = parseInt(item.year) + 1;

							if(!$rootScope.app.user.enrolledModules[year_no]){
								$rootScope.app.user.enrolledModules[year_no] = {
									year: item.year + " / " + year_end,
									modules: []
								};
							}

							$rootScope.app.user.enrolledModules[year_no].modules.push({
								classCode: item.classCode,
								year: item.year,
								name: item.name
							});
						}

						var newArray = [];
						for(i = 0; i < $rootScope.app.user.enrolledModules.length; i++){

							if($rootScope.app.user.enrolledModules[i]){
								newArray.push($rootScope.app.user.enrolledModules[i]);
							}
						}
						$rootScope.app.user.enrolledModules = newArray;

					}

					userloaded = true;

				})
			}

		}]);
