'use strict';

// Declare app level module which depends on views, and components
var bodApp = angular.module('bodApp', [
    'ngRoute',
    'bodApp.view_main',
    'bodApp.view_kpi_month',
    'bodApp.view_kpi_year',
    'bodApp.view_revenue_lobs_forecast_forex',
    'bodApp.view_highlights',
    'bodApp.view_revenue_lobs_pl',
    'bodApp.view_revenue_clients_pl',
    'bodApp.view_revenue_2year_lobs',
    'bodApp.view_revenue_2year_clients',
    'bodApp.login',
    'bodApp.view_lobs_finx',
    'bodApp.view_lobs_fins_s_2',
    'bodApp.view_lobs_auto',
    'bodApp.view_lobs_enterprise',
    'bodApp.view_lobs_telecom',
    'bodApp.view_lobs_excelian',
    'bodApp.view_lobs_horizon',
    'bodApp.view_lobs_global',
    'bodApp.view_lobs_global_4practices',
    'bodApp.view_revenue_lobs_vsplan_forex',
    'bodApp.view_firstpair_practices',
    'bodApp.view_secondpair_practices',
    'bodApp.view_thirdpair_practices',
    'ngAnimate',
    'ngTouch',
    'LocalStorageModule',
    "bodAppControllers",
    'ngRepeatUtils'
]);


/**
*Setting log level for angular app
**/
bodApp.config(function($provide) {
    var LOG_LEVELS = ["LOG","INFO","DEBUG","WARN","ERROR"];
    var CURRENT_LOG_LEVEL = "DEBUG";
    var emptyF = function(){};
    $provide.decorator('$log', function($delegate) {
        var currentLevelIndex = LOG_LEVELS.indexOf(CURRENT_LOG_LEVEL);
        for (var ind=0; ind < currentLevelIndex; ind++) {
            $delegate[LOG_LEVELS[ind].toLowerCase()] = emptyF;
        }
        return $delegate;
    });
});


/**
 * Disable at production
 */
bodApp.config(['$compileProvider', function ($compileProvider) {
    $compileProvider.debugInfoEnabled(true);
}]);

bodApp.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.otherwise({redirectTo: '/view_main'});
}]);

