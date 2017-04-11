'use strict';

angular.module('bodApp.view_revenue_clients_pl', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view_revenue_clients_pl', {
            templateUrl: 'screens/view_revenue_clients_pl/view_revenue_clients_pl.html',
            controller: 'ViewRCPCtrl'
        });
    }])

    .controller('ViewRCPCtrl', ['$scope', '$routeParams', '$http', 'ConfigurationService', 'LabelService', '$log',
        function ($scope, $routeParams, $http, ConfigurationService, LabelService, $log) {

            $log.debug(ConfigurationService.getQuarters(function (data) {
                $log.debug(data);
            }));

            var widgets = [
                {id: 'widget_6_1', url:"api/revenueByClientsPl"},
                {id: 'widget_6_2', url:"api/cmByClientsPl"}];

            widgets.forEach(function (widget, index) {
                var widgetView = App.BIChart.createCombineLineBarChart("#" + widget.id);
                var config = widgetView.getConfig();
                config.animation.active = !ConfigurationService.printMode;
                config.bar.padding = 0.1;
                config.baseFont.size = 'default';
                config.baseFont.sizeMultiplier = 0.06;
                config.legend.font.size = "0.5em";
                widgetView.render();
                widget.view = widgetView;
            });

            var listener = function () {
                widgets.forEach(function (widget) {
                    var widgetView = widget.view;
                    widgetView.resize();
                    widgetView.render();
                    widgetView.fadeIn();
                });
            };

            window.addEventListener("resize", listener, false);


            var fadeInListener = function () {
                $log.debug("transition end start rendering widgets");



                    widgets.forEach(function (widget) {
                        $http.get(ConfigurationService.url+widget.url).then(function (response) {
                            widget.view.setData(response.data);
                            widget.view.render();
                            widget.view.fadeIn();
                        });
                    });


                };
            var page = document.getElementsByClassName("page")[0];
            page.addEventListener("transitionend", fadeInListener, false);
            $scope.$on('$routeChangeStart', function () {
                $log.debug("remove");
                window.removeEventListener("resize", listener, false);
                page.removeEventListener("resize", fadeInListener, false);
            })



        }]);
