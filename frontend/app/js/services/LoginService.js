bodApp.service('LoginService',
    function ($http, ConfigurationService, $log) {
        var url = ConfigurationService.url;
        return {
            login: function (username, password) {
                return $http.post(url + 'user/login', {name: username, password: password}).then(function (response) {
                    return response.data.token;
                });
            },

            hasRole: function (role) {
                return $http.get(url + 'api/role/' + role).then(function (response) {
                    $log.debug(response);
                    return response.data;
                }, function () {
                    return false;
                });
            },

            isAuthenticated: function () {
                return $http.get(url + 'api/role/user').then(function (response) {
                    return response.data;
                }, function () {
                    return false;
                });
            }
        };
    });
