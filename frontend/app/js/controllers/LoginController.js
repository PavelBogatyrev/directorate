bodApp.controller('LoginController', ['LoginService', '$scope', '$http', 'ConfigurationService', '$location', 'localStorageService',
    function (LoginService, $scope, $http, ConfigurationService, $location, localStorageService) {
        $scope.token = null;
        $scope.error = null;
        $scope.roleUser = false;
        $scope.roleAdmin = false;

        $scope.login = function () {
            $scope.error = null;
            LoginService.login($scope.userName, $scope.password).then(function (token) {
                    $scope.token = token;
                    $http.defaults.headers.common.Authorization = 'Bearer ' + token;
                    $scope.checkRoles();
                    localStorageService.set('token', $http.defaults.headers.common.Authorization);
                },
                function (error) {
                    $scope.error = error;
                    $scope.userName = '';
                    angular.element(document.querySelector("form")).addClass('error');
                });
        };

        $scope.checkRoles = function () {
            LoginService.hasRole('user').then(function (user) {
                $scope.roleUser = user;
                $location.path("/view_main");
            });
            LoginService.hasRole('admin').then(function (admin) {
                $scope.adminUser = admin;
                ConfigurationService.adminMode = admin;
            });
        };

        $scope.logout = function () {
            $scope.userName = '';
            $scope.token = null;
            $http.defaults.headers.common.Authorization = '';
            $location.path("/login");
            localStorageService.remove('token');
        };
        // reload page
        if (localStorageService.get('token')) {
            $http.defaults.headers.common.Authorization = localStorageService.get('token');
            LoginService.isAuthenticated().then(function (state) {
                if (state) {
                    LoginService.hasRole('admin').then(function (admin) {
                        $scope.adminUser = admin;
                        ConfigurationService.adminMode = admin;
                    });
                }
            });
        }
        // check token on view change
        $scope.$on('$routeChangeStart', function (next, current) {
            if (localStorageService.get('token')) {
                $http.defaults.headers.common.Authorization = localStorageService.get('token');
                LoginService.isAuthenticated().then(function (state) {
                    if (!state) {
                        $scope.logout();
                    }
                });
            } else {
                $scope.logout();
            }
        })
    }]);

