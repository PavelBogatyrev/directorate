'use strict';

angular.module('bodApp.view_highlights', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view_highlights', {
            templateUrl: 'screens/view_highlights/view_highlights.html',
            controller: 'ViewHighlightsCtrl'
        });
    }])

    .controller('ViewHighlightsCtrl', ['$scope', '$routeParams', '$http', 'ConfigurationService', 'LabelService', '$log',
        function ($scope, $routeParams, $http, ConfigurationService, LabelService, $log) {
        }]);
