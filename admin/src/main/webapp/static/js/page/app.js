var pushAdmin = angular.module('pushAdmin', ['ngCookies','pushAdmin.services', 'ui.bootstrap']);
var httpHeaders, pushCookie;

//Declare app level module which depends on services
pushAdmin.config(function($routeProvider, $httpProvider) {
    //TODO replace with index.html
    $routeProvider.when('/', {templateUrl: 'views/app-details.html', controller: initialController});

    $routeProvider.when('/login', {templateUrl: 'views/login.html', controller: loginController});
    $routeProvider.when('/dashboard', {templateUrl: 'views/dashboard.html', controller: dashboardController});
    $routeProvider.when('/app/create', {templateUrl: 'views/create-app.html', controller: createAppController});
    $routeProvider.when('/app/details', {templateUrl: 'views/app-details.html', controller: appDetailsController});
    $routeProvider.when('/compose', {templateUrl: 'views/compose-message.html', controller: messageController});
    $routeProvider.when('/history', {templateUrl: 'views/history.html', controller: dashboardController});
    $routeProvider.when('/schedule', {templateUrl: 'views/schedule.html', controller: dashboardController});
    $routeProvider.when('/targets', {templateUrl: 'views/targets.html', controller: dashboardController});
    $routeProvider.when('/reports', {templateUrl: 'views/reports.html', controller: dashboardController});
    $routeProvider.when('/settings', {templateUrl: 'views/settings.html', controller: dashboardController});

    $httpProvider.responseInterceptors.push(function ($rootScope, $q) {
        return function (promise) {
            return promise.then(
                function (response) {

                    if(isARequestToGetUserDetails(response.config.url)){
                       $rootScope.loginError = null;
                    }

                    return response;
                },

                function (response) {
                    if (response.status === 401) {
                        var deferred = $q.defer(), duplicate = false,
                            req = { config: response.config, deferred: deferred};

                        if(isARequestToGetUserDetails(response.config.url)){
                            $rootScope.loginError = "Invalid credentials.";
                        }

                        angular.forEach($rootScope.failedRequests, function(failedReq){
                            if(failedReq.config.url == req.config.url) {
                                duplicate = true;
                            }
                        });

                        if(!duplicate) {
                            $rootScope.failedRequests.push(req);
                        }

                        $rootScope.$broadcast('event:user-needed');
                        return deferred.promise;
                    }
                    return $q.reject(response);
                }
            );


        };

    });

    function isARequestToGetUserDetails(requestOrigin) {
        if(requestOrigin == '/push/user'){
            return true;
        }
        return false;
    }

    httpHeaders = $httpProvider.defaults.headers;
});


pushAdmin.run(function ($rootScope, $http, $location, $cookieStore) {
    pushCookie = $cookieStore.get("push");
    $rootScope.failedRequests = [];

    if (pushCookie != undefined){
        httpHeaders.common['Authorization'] = 'Basic ' + pushCookie.user;
        var parsedCred = CryptoJS.enc.Base64.parse(pushCookie.user);
        var userCred =  CryptoJS.enc.Utf8.stringify(parsedCred);
        $rootScope.authenticatedUser = {name: userCred.split(":")[0], roles: pushCookie.roles};
    }

    $rootScope.$watch('applications', function(newValue, oldValue){
        if (oldValue == undefined){
            //TODO after getting applications execute previous url
            return $location.path('/');
        }
    });

   $rootScope.$watch('currentApplication', function(newValue, oldValue){
        if(newValue != undefined && newValue.id==-1){
            $rootScope.currentApplication = oldValue;
            return $location.path('/app/create');
         }
         else if (newValue != undefined && oldValue != undefined && pushCookie != undefined){
            $cookieStore.put("push", {user:pushCookie.user, roles: pushCookie.roles, app:newValue.id});
         } 

         if(oldValue != undefined  && oldValue.id != -1){ // if 'Create Application' is not selected
        	 return $location.path('/app/details');
         }
     });

    $rootScope.$on('event:user-needed', function () {
        return $location.path('/login');
    });

    $rootScope.$on('event:user-confirmed', function () {
        angular.forEach($rootScope.failedRequests, function(failedReq){
            $http(failedReq.config).then(function (response) {
                failedReq.deferred.resolve(response);
            });
        });
        $rootScope.failedRequests = [];
    });

    $rootScope.$on('event:user-request', function (event, user) {
        var cred = CryptoJS.enc.Utf8.parse(user.name + ':' + user.password);
        var encodedCred = CryptoJS.enc.Base64.stringify(cred);
        httpHeaders.common['Authorization'] = 'Basic ' + encodedCred;

        $http({method: 'GET', url:'/push/user', cache:false, headers: { 'If-Modified-Since': "0" }}).success(function (data) {
            $cookieStore.put("push", {user:encodedCred, roles: data.roles});
            $rootScope.authenticatedUser = {name: data.username, roles: data.roles};
            $rootScope.$broadcast('event:user-confirmed');
        });
    });

    $rootScope.$on('event:forget-user', function () {
        $cookieStore.remove('push');
        httpHeaders.common['Authorization'] = undefined;
        $rootScope.authenticatedUser = undefined;
        $rootScope.currentApplication = undefined;
        $rootScope.applications = undefined;
    });
});