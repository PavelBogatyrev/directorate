'use strict';

angular.module('bodApp.view_revenue_lobs_pl', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view_revenue_lobs_pl', {
            templateUrl: 'screens/view_revenue_lobs_pl/view_revenue_lobs_pl.html',
            controller: 'ViewRLPCtrl'
        });
    }])

    .controller('ViewRLPCtrl', ['$scope', '$routeParams', '$http', 'ConfigurationService', 'LabelService', '$log',
        function ($scope, $routeParams, $http, ConfigurationService, LabelService, $log) {

            $log.debug(ConfigurationService.getQuarters(function (data) {
                $log.debug(data);
            }));
            var url = ConfigurationService.url;

            var widgets = [
                {id: 'widget_5_1'},
                {id: 'widget_5_2'},
                {id: 'widget_5_3'},
                {id: 'widget_5_4'}];
            widgets.forEach(function (widget, index) {
                var widgetView = App.BIChart.createCombineLineBarChart("#" + widget.id);


                var config = widgetView.getConfig();
                config.animation.active = !ConfigurationService.printMode;
                config.legend.font.size = "0.5em";

                config.baseFont.size = 'default';
                config.baseFont.sizeMultiplier = 0.06;
                config.bar.values.font.size = "0.7em";
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


                $http.get(url + "api/lobsRevenueRight").then(function (response) {
                    widgets.forEach(function (widget) {
                        if (widget.id == "widget_5_2") {
                            widget.view.getConfig().bar.padding = 0.1;
                            widget.view.getConfig().barGroup.padding = 0.5;
                            widget.view.setData(response.data[0]);
                            widget.view.getConfig().showLegend = false;
                            widget.view.getConfig().padding.top = 30;
                            widget.view.render();
                            widget.view.fadeIn();
                        } else if (widget.id == "widget_5_4") {
                            widget.view.getConfig().barGroup.padding = 0.5;
                            widget.view.getConfig().bar.padding = 0.1;
                            widget.view.getConfig().showLegend = false;
                            widget.view.getConfig().padding.top = 30;
                            widget.view.setData(response.data[1]);
                            widget.view.render();
                            widget.view.fadeIn();
                        }
                        widget.view.render();
                        widget.view.fadeIn();
                    });
                });

                $http.get(url + "api/lobsRevenueLeft").then(function (response) {
                    widgets.forEach(function (widget) {
                        if (widget.id == "widget_5_1") {
                            widget.view.setData(response.data[0]);
                            widget.view.getConfig().bar.padding = 0.1;
                            widget.view.render();
                            widget.view.fadeIn();

                        } else if (widget.id == "widget_5_3") {
                            widget.view.setData(response.data[1]);
                            widget.view.getConfig().bar.padding = 0.1;
                            widget.view.render();
                            widget.view.fadeIn();
                        }

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
